package daniel.com.br.crud;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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
        adapter = new ArrayAdapter<>(context,R.layout.simple_list_item,listBooks);

        lvBooks = (ListView)findViewById(R.id.lvBooks);
        lvBooks.setAdapter(adapter);
        //list events listener
        lvBooks.setOnItemClickListener(new LvBooksItemClickListener());
        lvBooks.setOnItemLongClickListener(new LvBookItemLongClickListener());

        //add new book button
        Button btnNewBook = (Button)findViewById(R.id.btnNewBook);
        btnNewBook.setOnClickListener(new BtnNewBookListener());

    }

    @Override
    protected void onRestart() {
        //updating data whem back button is pressed
        super.onRestart();
        listBooks = new BookDaoSQLite(context).selectBooks();
        adapter = new ArrayAdapter<>(context,R.layout.simple_list_item,listBooks);
        lvBooks.setAdapter(adapter);
    }

    private class LvBooksItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //getting information from the clicked book
            Book clickedBook = (Book)parent.getAdapter().getItem(position);

            //passing on that information for the next activity
            Intent intent = new Intent(context,UpdateOrDeleteActivity.class);
            intent.putExtra("id",clickedBook.getId());
            intent.putExtra("title",clickedBook.getTitle());
            intent.putExtra("author",clickedBook.getAuthor());

            context.startActivity(intent);

        }
    }

    private class LvBookItemLongClickListener implements AdapterView.OnItemLongClickListener{

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            //make sure the user knows what he is doing
            //delete it from the database
            //...
            return false;
        }
    }

    private class BtnNewBookListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context,NewBookActivity.class);
            context.startActivity(intent);
        }
    }



}
