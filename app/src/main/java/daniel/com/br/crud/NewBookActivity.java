package daniel.com.br.crud;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import daniel.com.br.crud.database.DatabaseCreator;
import daniel.com.br.crud.model.Book;
import daniel.com.br.crud.model.Tag;
import daniel.com.br.crud.model.TagsInBooks;

public class NewBookActivity extends AppCompatActivity {

    //widgets
    private EditText txtTitle, txtAuthor;
    private TextView lblTags;
    private Button btnRegister;
    private Context context;

    private List<Tag> mAllRegisteredTags,mSelectedTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);

        this.context = this;

        //getting widgets from view
        txtTitle = (EditText)findViewById(R.id.txtTitle);
        txtAuthor = (EditText)findViewById(R.id.txtAuthor);
        btnRegister = (Button)findViewById(R.id.btnRegister);

        //buttons click events
        btnRegister.setOnClickListener(new BtnRegisterOnClickListener());

        lblTags = (TextView)findViewById(R.id.lblTags);
        lblTags.setOnClickListener(new BtnSelectTagsOnClickListener());


        //loading all options for registering tags
        new LoadAllTagsTask().execute();

    }

    //buttons on click listeners
    private class BtnRegisterOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            if (areFieldsValid()){
                //get book's information
                String title = txtTitle.getText().toString();
                String author = txtAuthor.getText().toString();

                //creating book out of information gotten from the text fields
                Book book = new Book();
                book.setTitle(title);
                book.setAuthor(author);

                //inserting book into the database
                new InsertBookTask().execute(book);
            }
        }
    }

    private void showSelectionDialog(ISelectTagsDialogEvent onPositiveAnswer){
        SelectTagsDialog dialog = SelectTagsDialog.newInstance(onPositiveAnswer,mAllRegisteredTags);
        dialog.show(getSupportFragmentManager(),"select_tags_dialog");
    }

    private class BtnSelectTagsOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            showSelectionDialog(new ISelectTagsDialogEvent() {
                @Override
                public void run(List<Tag> selectedTags) {

                    if(mSelectedTags == null)
                        mSelectedTags = new ArrayList<>();

                    //getting the select tags from the dialog
                    //adding it to the list of selected tags on the activity
                    mSelectedTags.clear();
                    mSelectedTags.addAll(selectedTags);


                    //updating textview to represent new selected tags
                    lblTags.setText(StringUtils.tagListToContinuousString(selectedTags));
                }
            });
        }
    }

    class InsertBookTask extends AsyncTask<Book,Void,Void>{

        DatabaseCreator databaseCreator;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            databaseCreator = DatabaseCreator.getsInstance(getApplicationContext());
            databaseCreator.createDatabase(getApplicationContext());
        }

        @Override
        protected Void doInBackground(Book... params) {
            databaseCreator.getDatabase().bookModel().insertBook(params[0]);

            //getting the id of the book inserted
            int idBook = databaseCreator.getDatabase().bookModel().getIdOfLastInsertedBook();

            if(mSelectedTags != null)
                for(Tag tag : mSelectedTags){
                    TagsInBooks tagsInBooks = new TagsInBooks();
                    tagsInBooks.setBookId(idBook);
                    tagsInBooks.setTagId(tag.getId());

                    databaseCreator.getDatabase().tagsInBooksModel().insertTagInBook(tagsInBooks);
                }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //show message
            String messageToast = "Book was successfully registered";
            Toast.makeText(context,messageToast,Toast.LENGTH_SHORT).show();

            //clean fields
            txtTitle.setText("");
            txtAuthor.setText("");
            lblTags.setText("Select Tags");

            if(mSelectedTags != null)
                mSelectedTags.clear();
        }
    }

    class LoadAllTagsTask extends AsyncTask<Void,Void,List<Tag>>{

        private DatabaseCreator databaseCreator;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            databaseCreator = DatabaseCreator.getsInstance(getApplicationContext());
            databaseCreator.createDatabase(getApplicationContext());
        }

        @Override
        protected List<Tag> doInBackground(Void... params) {
            return databaseCreator.getDatabase().tagModel().findAllTags();
        }

        @Override
        protected void onPostExecute(List<Tag> tags) {
            super.onPostExecute(tags);
            if(mAllRegisteredTags == null)
                mAllRegisteredTags = new ArrayList<>();
            else
                mAllRegisteredTags.clear();

            mAllRegisteredTags.addAll(tags);

            //now is possible to select tags
            //showing it to the user
            lblTags.setText("Select Tags");
        }
    }

    /**
     * return true if all the fields text are valid
     * also it sets an error message on the txts invalid, so that the user knows
     */
    private boolean areFieldsValid(){
        boolean isValid = true;
        //title
        if (txtTitle.getText().toString().isEmpty()){
            isValid = false;
            txtTitle.setError("Title's field must be filled");
        }
        //author
        if (txtAuthor.getText().toString().isEmpty()){
            isValid = false;
            txtAuthor.setError("Author's field must be filled");
        }

        return isValid;
    }

}
