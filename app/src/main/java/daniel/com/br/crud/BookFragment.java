package daniel.com.br.crud;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import daniel.com.br.crud.database.DatabaseCreator;
import daniel.com.br.crud.model.Book;

public class BookFragment extends Fragment {

    private boolean isLoaded;
    private List<Book> bookList;

    private RecyclerView mRecyclerView;
    private BookRecyclerAdapter mBookAdapter;

    private Context mContext;

    public BookFragment() {
        isLoaded = false;
        bookList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        View rootView = inflater.inflate(R.layout.fragment_book, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //setting the adapter
        //and the actions of its buttons after object was constructed
        mBookAdapter = new BookRecyclerAdapter(mContext,bookList);
        mBookAdapter.setEditEvent(new IEvent() {
            @Override
            public void run(View view, int position) {
                //clicked book
                Book selectedBook = bookList.get(position);

                //information passed to the next activity
                Intent intent = new Intent(mContext,UpdateOrDeleteActivity.class);
                intent.putExtra("id",selectedBook.getId());
                intent.putExtra("title",selectedBook.getTitle());
                intent.putExtra("author",selectedBook.getAuthor());
                mContext.startActivity(intent);
            }
        });
        mBookAdapter.setDeleteEvent(new IEvent() {
            @Override
            public void run(View view, final int position) {
                //ask for the user to confirm that he wants to delete the book
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
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

        //recycler view and its layout manager and adapter set
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_book_list);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mBookAdapter);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        //loading data only if visible to user
        if (isVisibleToUser && !isLoaded && isAdded()){
            new LoadListBook().execute();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //reloading data when back button pressed
        if(getUserVisibleHint()){
            new LoadListBook().execute();
        }
    }

    class AddDummyBook extends AsyncTask<Void,Void,Void>{

        private DatabaseCreator databaseCreator;

        public AddDummyBook(){
            databaseCreator = DatabaseCreator.getsInstance(mContext);
            databaseCreator.createDatabase(mContext);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Book book = new Book();
            book.setTitle("Dummy Title");
            book.setAuthor("Dummy Author");
            databaseCreator.getDatabase().bookModel().insertBook(book);

            return null;
        }
    }

    class LoadListBook extends AsyncTask<Void,Void,List<Book>>{

        private DatabaseCreator databaseCreator;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            databaseCreator = DatabaseCreator.getsInstance(mContext);
            databaseCreator.createDatabase(mContext);
        }

        @Override
        protected List<Book> doInBackground(Void... params) {
            List<Book> listBook = databaseCreator.getDatabase().bookModel().findAllBooks();
            return listBook;
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            super.onPostExecute(books);
            //setting the books gotten from the database
            //notifying the change to reload
            bookList.clear();
            bookList.addAll(books);
            mBookAdapter.notifyDataSetChanged();

            isLoaded = true;
        }
    }

    class LoadDeleteBook extends AsyncTask<Integer,Void,Integer>{

        private DatabaseCreator databaseCreator;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            databaseCreator = DatabaseCreator.getsInstance(mContext);
            databaseCreator.createDatabase(mContext);
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            //position passed as argument
            int index = params[0];

            //deleting from the database
            //and from the current list
            databaseCreator.getDatabase().bookModel().deleteBook(bookList.get(index));
            bookList.remove(index);

            return index;
        }

        @Override
        protected void onPostExecute(Integer index) {
            super.onPostExecute(index);
            //updating adapter about the changes on the list
            //just the removed row
            mBookAdapter.notifyItemRemoved(index);
            mBookAdapter.notifyItemRangeChanged(index,bookList.size());

        }
    }
}
