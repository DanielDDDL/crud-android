package daniel.com.br.crud;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import daniel.com.br.crud.database.BookDaoSQLite;
import daniel.com.br.crud.model.Book;

/**
 * Created by Dias on 11/06/2017.
 */

public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private List<Book> bookList;
    private RecyclerView recyclerView;

    public BookRecyclerAdapter(Context mContext, List<Book> bookList){
        this.mContext = mContext;
        this.bookList = bookList;

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View viewItem = layoutInflater.inflate(R.layout.book_card,parent,false);

        return new MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Book clickedBook = bookList.get(position);
        holder.txtTitle.setText(clickedBook.getTitle());
        holder.txtAuthor.setText(clickedBook.getAuthor());

        //menu click listener
        holder.ivOverflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.ivOverflow, clickedBook);
            }
        });

    }

    private void showPopupMenu (View view, final Book clickedBook){
        //inflating menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.book_menu,popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_edit:
                        Intent intent = new Intent(mContext,UpdateOrDeleteActivity.class);
                        intent.putExtra("id",clickedBook.getId());
                        intent.putExtra("title",clickedBook.getTitle());
                        intent.putExtra("author",clickedBook.getAuthor());
                        intent.putExtra("genre",clickedBook.getGenre());

                        mContext.startActivity(intent);
                        return true;
                    case R.id.action_delete:

                        //ask for the user to confirm that he wants to delete the book
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                        alertDialogBuilder.setTitle("Deleting book")
                            .setMessage("Are you sure you really wanna do this?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //confirming willing to delete book...
                                    new DeleteBookLoader().execute(clickedBook.getId());
                                    //TODO: reload data
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

                        return true;
                    default:
                }

                return false;
            }
        });
        popup.show();
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    /**
     * the view and its components
     * */
    public class MyViewHolder extends RecyclerView.ViewHolder{

        //widgets held by the adapter
        public TextView txtTitle, txtAuthor;
        public ImageView ivOverflow; //clickable menu

        public MyViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            txtAuthor = (TextView) itemView.findViewById(R.id.txt_author);
            ivOverflow = (ImageView) itemView.findViewById(R.id.overflow);
        }
    }

    class DeleteBookLoader extends AsyncTask<Integer,Void,Integer>{

        @Override
        protected Integer doInBackground(Integer... params) {
            Integer id = params[0];
            new BookDaoSQLite(mContext).delete(id);

            return findBookByIdOnBookList(id);
        }

        @Override
        protected void onPostExecute(Integer index) {
            super.onPostExecute(index);
            if (index != -1){
                //delete from the list as well
                //and notifying changes
                bookList.remove(index);
                notifyDataSetChanged();
//                recyclerView.setAdapter(new BookRecyclerAdapter(mContext,bookList));
//                recyclerView.invalidate();
//                recyclerView.removeViewAt(index);
//                notifyItemRemoved(index);
//                notifyItemRangeChanged(index,bookList.size());

            }

        }
    }

    private int findBookByIdOnBookList(int id){
        for (int i = 0; i < bookList.size(); i++){
            if (bookList.get(0).getId() == id){
                return i;
            }
        }
        //not found
        return -1;

    }

}
