package daniel.com.br.crud;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import daniel.com.br.crud.model.TagInBooksWithTitle;
import daniel.com.br.crud.model.TagsInBooks;

public class UpdateOrDeleteActivity extends AppCompatActivity {

    //widgets
    private EditText txtTitle, txtAuthor;
    private TextView lblTags;
    private Button btnUpdate, btnDelete, btnSelectTags;

    private Context context;
    private Book activityBook;
    private List<Tag> activityTags, allTagsRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_or_delete);

        this.context = this;

        //getting information from the intent
        Intent intent = getIntent();
        int id = intent.getIntExtra("id",0);
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");

        activityBook = new Book();
        activityBook.setId(id);
        activityBook.setTitle(title);
        activityBook.setAuthor(author);

        //getting widgets from view
        //adding current information from the book into the text fields
        txtTitle = (EditText)findViewById(R.id.txtTitle);
        txtTitle.setText(activityBook.getTitle());
        txtAuthor = (EditText)findViewById(R.id.txtAuthor);
        txtAuthor.setText(activityBook.getAuthor());

        lblTags = (TextView)findViewById(R.id.lblTags);

        //setting buttons actions
        btnUpdate = (Button)findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new BtnUpdateOnClickListener());
        btnDelete = (Button)findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new BtnDeleteOnClickListener());
        btnSelectTags = (Button)findViewById(R.id.btnSelectTags);
        btnSelectTags.setOnClickListener(new BtnSelectTagsOnClickListener());

        new LoadAllTagsTask().execute();
        new LoadTagsForCurrentBookTask().execute(activityBook.getId());
    }

    private class BtnUpdateOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (areFieldsValid()){
                //if all the fields were filled correctly...

                //get book's new information and adding it to the activity book
                activityBook.setTitle(txtTitle.getText().toString());
                activityBook.setAuthor(txtAuthor.getText().toString());

                //update it on the database
                new UpdateBookLoader().execute(activityBook);


            }
        }
    }

    private class BtnDeleteOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //ask for the user to confirm that he wants to delete the book
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("Deleting book")
                    .setMessage("Are you sure you really wanna do this?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //confirming willing to delete book...
                            new DeleteBookLoader().execute(activityBook);

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //cancelling operation
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
    }

    private void showSelectionDialog(ISelectTagsDialogEvent onPositiveAnswer){
        SelectTagsDialog dialog = SelectTagsDialog.newInstance(onPositiveAnswer,allTagsRegistered);
        dialog.show(getSupportFragmentManager(),"select_book_dialog");
    }

    private class BtnSelectTagsOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            showSelectionDialog(new ISelectTagsDialogEvent() {
                @Override
                public void run(List<Tag> selectedTags) {

                    //changing the activityTags to be the selected ones
                    activityTags.clear();
                    activityTags.addAll(selectedTags);

                    //updating lblTags
                    lblTags.setText(StringUtils.tagListToContinuousString(selectedTags));
                }
            });
        }
    }

    class UpdateBookLoader extends AsyncTask<Book,Void,Void>{

        private DatabaseCreator databaseCreator;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            databaseCreator = DatabaseCreator.getsInstance(getApplicationContext());
            databaseCreator.createDatabase(getApplicationContext());
        }

        @Override
        protected Void doInBackground(Book... params) {
            //updating book
            databaseCreator.getDatabase().bookModel().updateBook(params[0]);

            //updating tags related to that book


            //deleting previous tags
            databaseCreator.getDatabase().tagsInBooksModel().deleteAllTagsForBookWithId(activityBook.getId());

            //getting the relation objects
            for (int i = 0; i < activityTags.size(); i++){
                TagsInBooks newTagInBook = new TagsInBooks();
                newTagInBook.setBookId(activityBook.getId());
                newTagInBook.setTagId(activityTags.get(i).getId());
                //inserting into the database
                databaseCreator.getDatabase().tagsInBooksModel().insertTagInBook(newTagInBook);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //show message
            String messageToast = "Book updated successfully";
            Toast.makeText(context,messageToast,Toast.LENGTH_SHORT).show();

            //going back to the previous screen
            Intent newMainActivity = NavUtils.getParentActivityIntent(UpdateOrDeleteActivity.this);
            context.startActivity(newMainActivity);

            //prevent from coming back to this activity after back button is pressed
            finish();
        }
    }

    class DeleteBookLoader extends AsyncTask<Book,Void,Void>{

        private DatabaseCreator databaseCreator;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            databaseCreator = DatabaseCreator.getsInstance(getApplicationContext());
            databaseCreator.createDatabase(getApplicationContext());
        }

        @Override
        protected Void doInBackground(Book... params) {
            databaseCreator.getDatabase().bookModel().deleteBook(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //showing message
            String message = "Book deleted successfully";
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

            //going back to the main activity
            Intent intent = new Intent(context,MainActivity.class);
            context.startActivity(intent);

            //prevent from coming back to this activity if back button pressed
            finish();
        }
    }

    class LoadTagsForCurrentBookTask extends AsyncTask<Integer,Void,List<Tag>>{

        DatabaseCreator databaseCreator;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            databaseCreator = DatabaseCreator.getsInstance(getApplicationContext());
            databaseCreator.createDatabase(getApplicationContext());
        }

        @Override
        protected List<Tag> doInBackground(Integer... params) {
            //get tags related to the book with id passed as params[0]
            int bookId = params[0];
            List<TagInBooksWithTitle> tagsForBook = databaseCreator.getDatabase().tagsInBooksModel().findTagsWithNameForBookWithId(bookId);

            Log.e("Size first list", String.valueOf(tagsForBook.size()));

            Log.e(UpdateBookLoader.class.getSimpleName(),
                    String.valueOf(
                            databaseCreator.getDatabase().tagsInBooksModel().findAllTagsForBookWithId(bookId).size()
                    )
                );

            //return tags gotten from the database
            return convertTagsWithtitleToTags(tagsForBook);
        }

        @Override
        protected void onPostExecute(List<Tag> databaseTags) {
            super.onPostExecute(databaseTags);

            //adding tags for the activity tags
            if(activityTags == null)
                activityTags = new ArrayList<>();
            else
                activityTags.clear();

            activityTags.addAll(databaseTags);

            //set a text on lblTags equivalent to the tags from the database
            lblTags.setText(StringUtils.tagListToContinuousString(activityTags));
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
            if(allTagsRegistered == null)
                allTagsRegistered = new ArrayList<>();
            else
                allTagsRegistered.clear();

            allTagsRegistered.addAll(tags);

        }
    }

    private List<Tag> convertTagsWithtitleToTags(List<TagInBooksWithTitle> listTagsWithTitles){
        List<Tag> listTags = new ArrayList<>();
        for (int i = 0; i < listTagsWithTitles.size(); i++){
            Tag tag = new Tag();
            tag.setId(listTagsWithTitles.get(i).getTagId());
            tag.setText(listTagsWithTitles.get(i).getText());

            listTags.add(tag);
        }

        return listTags;
    }

    /**
     * return true if all the fields text are valid
     * also it sets an error message on the txts invalid, so that the user knows
     * */
    private boolean areFieldsValid(){
        boolean isValid = true;
        //title
        if (txtTitle.getText().toString().isEmpty()){
            isValid = false;
            txtTitle.setError("New title's field must be filled");
        }
        //author
        if (txtAuthor.getText().toString().isEmpty()){
            isValid = false;
            txtAuthor.setError("New author's field must be filled");
        }

        return isValid;
    }


}
