package daniel.com.br.crud;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class NewBookActivity extends AppCompatActivity {

    //widgets
    private EditText txtTitle, txtAuthor;
    private Button btnRegister, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);

        //getting widgets from view
        txtTitle = (EditText)findViewById(R.id.txtTitle);
        txtAuthor = (EditText)findViewById(R.id.txtAuthor);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnBack = (Button)findViewById(R.id.btnBack);

    }
}
