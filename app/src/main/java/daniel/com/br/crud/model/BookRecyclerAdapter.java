package daniel.com.br.crud.model;

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

/**
 * Created by Dias on 11/06/2017.
 */

public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private List<Book> bookList;

    public BookRecyclerAdapter(Context mContext, List<Book> bookList){
        this.mContext = mContext;
        this.bookList = bookList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View viewItem = layoutInflater.inflate(R.layout.book_card,parent,false);

        return new MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Book clickedBook = bookList.get(position);
        holder.txtTitle.setText(clickedBook.getTitle());
        holder.txtAuthor.setText(clickedBook.getAuthor());

        //menu click listener
        holder.ivOverflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.ivOverflow);
            }
        });

    }

    private void showPopupMenu (View view){
        //inflating menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.book_menu,popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenuItemClickListener());
        popup.show();
    }

    class PopupMenuItemClickListener implements PopupMenu.OnMenuItemClickListener{

        //TODO: what is this boolean return for?

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()){
                case R.id.action_edit:
                    return true;
                case R.id.action_delete:
                    return true;
                default:
            }

            return false;
        }
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

        //TODO: get the images for the menu

        public MyViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            txtAuthor = (TextView) itemView.findViewById(R.id.txt_author);
        }
    }


}
