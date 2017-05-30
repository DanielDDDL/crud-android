package daniel.com.br.crud;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import daniel.com.br.crud.database.BookDaoSQLite;
import daniel.com.br.crud.model.Book;

public class NewBookActivity extends AppCompatActivity {

    //widgets
    private EditText txtTitle, txtAuthor;
    private Button btnRegister, btnBack;
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
        btnBack = (Button)findViewById(R.id.btnBack);

        //buttons click events
        btnRegister.setOnClickListener(new BtnRegisterOnClickListener());
        btnBack.setOnClickListener(new BtnBackOnClickListener());

    }

    //buttons on click listeners
    private class BtnRegisterOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            if (areFieldsValid()){
                //get book's information
                String title = txtTitle.getText().toString();
                String author = txtAuthor.getText().toString();
                Book book = new Book(title,author); //setting values

                //register book on database
                new BookDaoSQLite(context).insertBook(book);

                //show message
                String messageToast = title + " was sucessfully registered";
                Toast.makeText(context,messageToast,Toast.LENGTH_SHORT).show();

                //clean fields
                txtTitle.setText("");
                txtAuthor.setText("");
            }
        }
    }

    private class BtnBackOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent newActivityMain = NavUtils.getParentActivityIntent(NewBookActivity.this);
            context.startActivity(newActivityMain);

            finish();
        }
    }

    //return true if all the fields text are valid
    //also it sets an error message on the txts invalid, so that the user knows
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
