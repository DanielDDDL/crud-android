package daniel.com.br.crud;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import daniel.com.br.crud.database.BookDaoSQLite;
import daniel.com.br.crud.model.Book;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookRecyclerAdapter bookAdapter;
    private List<Book> bookList;
    private FloatingActionButton btnNewBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting the list and the adapter the uses it
        bookList = new ArrayList<>();
        bookAdapter = new BookRecyclerAdapter(this, bookList, new IEvent() {
            /*edit event*/
            @Override
            public void run(View view, int position) {
                String text = "Edit event on position number " + position;
                Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
            }
        }, new IEvent() {
            /*delete event*/
            @Override
            public void run(View view, int position) {
                String text = "Delete event on position number " + position;
                Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
            }
        });

        //recycler view and its layout manager and adapter set
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(bookAdapter);

        //loading books from the database
        new LoadBooks().execute();

        //new book button
        btnNewBook = (FloatingActionButton) findViewById(R.id.btn_add);
        btnNewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NewBookActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

    }


    @Override
    protected void onRestart() {
        //updating data when back button is pressed
        super.onRestart();
        new LoadBooks().execute();

    }

    class LoadBooks extends AsyncTask<Void,Void,List<Book>>{

        @Override
        protected List<Book> doInBackground(Void... params) {
            return new BookDaoSQLite(MainActivity.this).selectBooks();
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            super.onPostExecute(books);
            //setting the books gotten from the database
            //notifying the change to reload
            bookList.clear();
            bookList.addAll(books);
            bookAdapter.notifyDataSetChanged();
        }
    }

}
