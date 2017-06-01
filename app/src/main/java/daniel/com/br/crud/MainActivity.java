package daniel.com.br.crud;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import daniel.com.br.crud.database.BookDaoSQLite;
import daniel.com.br.crud.model.Book;

public class MainActivity extends AppCompatActivity {

    //widgets
    private ListView lvBooks;
    private Button btnNewBook;
    private ProgressBar progressBar;

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

        lvBooks = (ListView)findViewById(R.id.lvBooks);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        new LoadBooks().execute();

        //list events listener
        lvBooks.setOnItemClickListener(new LvBooksItemClickListener());
        lvBooks.setOnItemLongClickListener(new LvBookItemLongClickListener());

        //add new book button
        Button btnNewBook = (Button)findViewById(R.id.btnNewBook);
        btnNewBook.setOnClickListener(new BtnNewBookListener());

    }

    @Override
    protected void onRestart() {
        //updating data when back button is pressed
        super.onRestart();

        //showing the progress bar while loading
        progressBar.setVisibility(View.VISIBLE);

        new LoadBooks().execute();

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

            final Book clickedBook = (Book)parent.getAdapter().getItem(position);

            //ask for the user to confirm that he wants to delete the book
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("Deleting book")
                    .setMessage("Are you sure you really wanna do this?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //confirming willing to delete book...

                            //deleting form the database and removing it from the list
                            new BookDaoSQLite(context).delete(clickedBook.getId());
                            listBooks.remove(getBookPositionOnList(clickedBook.getId()));

                            //refreshing list
                            adapter.notifyDataSetChanged();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //cacelling opertation
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return true; //prevent the "short click" event from firing
        }
    }

    //return the index of the book from the book list based on its id
    private int getBookPositionOnList(int idBook){
        for(int i = 0; i < listBooks.size(); i++){
            if (listBooks.get(i).getId() == idBook){
                return i;
            }
        }

        return -1;//book not found
    }

    private class BtnNewBookListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context,NewBookActivity.class);
            context.startActivity(intent);
        }
    }

    //1 - Void: parameter used to execute the task in the  backgroud
    //2 - Integer: parameter passed to the "onProgressUpdate" method, which will be used to show our progress
    //3 - Result of the "doInBackground" method, which will be used to show the results on "onPostExecute"
    public class LoadBooks extends AsyncTask<Void,Integer,List<Book>> {

        //before the processing starts
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        //processing done in the background
        //no connection with main thread
        @Override
        protected List<Book> doInBackground(Void... params) {
            //simulating time it would take to get information from request
            for (int i = 0; i < 5; i++){
                try {
                    Thread.sleep(500);
                    publishProgress(i);
                } catch (InterruptedException  e) {
                    Thread.interrupted();
                }
            }

            //getting all the books from the database
            return new BookDaoSQLite(context).selectBooks();

        }

        //getting the current state of processign from "doInBackground"
        //shows it to the view, if desired
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        //to wrap up the result of the task
        //hide progress bar, show List gotten from the process in the background, etc.
        @Override
        protected void onPostExecute(List<Book> books) {

            super.onPostExecute(books);

            //hiding progress bar
            progressBar.setVisibility(View.GONE);

            //puttin the results of the processing into the adapter
            listBooks = books;
            adapter = new ArrayAdapter<Book>(context,R.layout.simple_list_item,listBooks);
            lvBooks.setAdapter(adapter);
        }

    }

}
