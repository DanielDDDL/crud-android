package daniel.com.br.crud.view.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import daniel.com.br.crud.R;
import daniel.com.br.crud.model.Book;
import daniel.com.br.crud.view.callbacks.IEvent;

/**
 * Created by Dias on 11/06/2017.
 */

public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private List<Book> bookList;
    private IEvent editEvent;
    private IEvent deleteEvent;

    //defines whether of not the list is empty
    private static final int VIEW_TYPE_EMPTY =  0;
    private static final int VIEW_TYPE_NOT_EMPTY = 1;

    //constructors
    public BookRecyclerAdapter(Context mContext, List<Book> bookList){
        this.mContext = mContext;
        this.bookList = bookList;

    }

    //in case there is the need to implement the buttons action right from the start
    public BookRecyclerAdapter(Context mContext, List<Book> bookList, IEvent editEvent, IEvent deleteEvent){
        this.mContext = mContext;
        this.bookList = bookList;
        this.editEvent = editEvent;
        this.deleteEvent = deleteEvent;

    }

    /**
     * the view and its components
     * */
    public class MyViewHolder extends RecyclerView.ViewHolder{

        //widgets held by the adapter
        public TextView lblTitle, lblAuthor;
        public ImageView ivOverflow; //clickable menu

        public MyViewHolder(View itemView) {
            super(itemView);
            lblTitle = (TextView) itemView.findViewById(R.id.txt_title);
            lblAuthor = (TextView) itemView.findViewById(R.id.txt_author);
            ivOverflow = (ImageView) itemView.findViewById(R.id.overflow);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (bookList.size() == 0){
            return VIEW_TYPE_EMPTY;
        } else {
            return VIEW_TYPE_NOT_EMPTY;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View viewItem = null;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        //checking if the list is empty
        //inflating accordingly
        if (viewType == VIEW_TYPE_EMPTY){
            viewItem = layoutInflater.inflate(R.layout.empty_list_item,parent,false);
        } else {
            viewItem = layoutInflater.inflate(R.layout.book_card, parent, false);
        }

        //creating item out of the view creating from inflating
        return new MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        //adding information to item(s) if the list if not empty
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_NOT_EMPTY){
            Book book = bookList.get(position);
            holder.lblTitle.setText(book.getTitle());
            holder.lblAuthor.setText(book.getAuthor());

            //menu click listener
            holder.ivOverflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(holder.ivOverflow, position);
                }
            });
        }


    }

    private void showPopupMenu (final View view, final int position){
        //inflating menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.book_menu,popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_edit:
                        editEvent.run(view,position);
                        return true;
                    case R.id.action_delete:
                        deleteEvent.run(view,position);
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
        //there will be an item if the list is empty
        if (bookList.size() == 0)
            return 1;

        return bookList.size();

    }

    //setters for the buttons action
    public void setEditEvent(IEvent editEvent) {
        this.editEvent = editEvent;
    }

    public void setDeleteEvent(IEvent deleteEvent) {
        this.deleteEvent = deleteEvent;
    }

}