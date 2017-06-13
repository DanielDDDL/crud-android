package daniel.com.br.crud;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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

        //setting the adapter
        //and the actions of its buttons after object was constructed
        bookAdapter = new BookRecyclerAdapter(this,bookList);
        bookAdapter.setEditEvent(new IEvent() {
            @Override
            public void run(View view, int position) {
                //clicked book
                Book selectedBook = bookList.get(position);

                //information passed to the next activity
                Intent intent = new Intent(MainActivity.this,UpdateOrDeleteActivity.class);
                intent.putExtra("id",selectedBook.getId());
                intent.putExtra("title",selectedBook.getTitle());
                intent.putExtra("author",selectedBook.getAuthor());
                intent.putExtra("genre",selectedBook.getGenre());
                MainActivity.this.startActivity(intent);
            }
        });
        bookAdapter.setDeleteEvent(new IEvent() {
            @Override
            public void run(View view, final int position) {
                //ask for the user to confirm that he wants to delete the book
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("Deleting book")
                        .setMessage("Are you sure you really wanna do this?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //confirming willing to delete book...
                                new LoadDeleteBook().execute(position);
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
        });
        /*
        //IN THE CONSTRUCTOR OF THE CLASS
        //PASSING ACTIONS OF THE EDIT AND THE DELETE BUTTONS
        bookAdapter = new BookRecyclerAdapter(this, bookList, new IEvent() {
            edit
            @Override
            public void run(View view, int position) {
                //clicked book
                Book selectedBook = bookList.get(position);
                //information passed to the next activity
                Intent intent = new Intent(MainActivity.this,UpdateOrDeleteActivity.class);
                intent.putExtra("id",selectedBook.getId());
                intent.putExtra("title",selectedBook.getTitle());
                intent.putExtra("author",selectedBook.getAuthor());
                intent.putExtra("genre",selectedBook.getGenre());
                MainActivity.this.startActivity(intent);
            }
        }, new IEvent() {
            //delete
            @Override
            public void run(View view, final int position) {
                //ask for the user to confirm that he wants to delete the book
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setTitle("Deleting book")
                        .setMessage("Are you sure you really wanna do this?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //confirming willing to delete book...
                                new LoadDeleteBook().execute(position);
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
        });
        */

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

    class LoadDeleteBook extends AsyncTask<Integer,Void,Integer>{

        @Override
        protected Integer doInBackground(Integer... params) {
            //position passed as argument
            int index = params[0];

            //deleting from the database
            //and from the current list
            new BookDaoSQLite(MainActivity.this).delete(bookList.get(index).getId());
            bookList.remove(index);

            return index;
        }

        @Override
        protected void onPostExecute(Integer index) {
            super.onPostExecute(index);
            //updating adapter about the changes on the list
            //just the removed row
            bookAdapter.notifyItemRemoved(index);
            bookAdapter.notifyItemRangeChanged(index,bookList.size());

        }
    }

}