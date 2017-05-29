package daniel.com.br.crud;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import daniel.com.br.crud.database.BookDaoSQLite;
import daniel.com.br.crud.model.Book;

public class MainActivity extends AppCompatActivity {

    //widgets
    private ListView lvBooks;
    private Button btnNewBook;

    //adapter dealing with the books
    private ArrayAdapter<Book> adapter;

    //list of books objects
    private List<Book> listBooks;


    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;

        //getting all the books from the database
        //adding them to the current working book list
        listBooks = new BookDaoSQLite(context).selectBooks();

        //loading all the books on the list onto the list view
        adapter = new ArrayAdapter<Book>(context,R.layout.simple_list_item,listBooks);
        lvBooks = (ListView)findViewById(R.id.lvBooks);
        lvBooks.setAdapter(adapter);

        //add new book button
        Button btnNewBook = (Button)findViewById(R.id.btnNewBook);
        btnNewBook.setOnClickListener(new BtnNewBookListener());

//        testes
//        this.testContext = this;
//        listBooks = (ListView)findViewById(R.id.listBooks);
//        testAdapter = new ArrayAdapter<String>(this,R.layout.simple_list_item,FRUITS);
//        listBooks.setAdapter(testAdapter);
//        Intent newActivityIntent = new Intent(MainActivity.this,UpdateOrDeleteActivity.class);
//        newActivityIntent.putExtra("title","Titulo");
//        newActivityIntent.putExtra("author","Autor");
//        newActivityIntent.putExtra("id",10);
//        startActivity(newActivityIntent);

    }

    private class BtnNewBookListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context,NewBookActivity.class);
            context.startActivity(intent);
        }
    }

}
