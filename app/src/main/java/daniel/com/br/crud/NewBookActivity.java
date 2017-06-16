package daniel.com.br.crud;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import daniel.com.br.crud.database.DatabaseCreator;
import daniel.com.br.crud.model.Book;

public class NewBookActivity extends AppCompatActivity {

    //widgets
    private EditText txtTitle, txtAuthor;
    private Button btnRegister;
    private Context context;

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
                new InsertBookLoader().execute(book);
            }
        }
    }

    class InsertBookLoader extends AsyncTask<Book,Void,Void>{

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
