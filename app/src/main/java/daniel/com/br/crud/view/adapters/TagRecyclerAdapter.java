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
import daniel.com.br.crud.model.Tag;
import daniel.com.br.crud.view.callbacks.IEvent;

/**
 * Created by Dias on 20/06/2017.
 */

public class TagRecyclerAdapter extends RecyclerView.Adapter<TagRecyclerAdapter.MyViewHolder>{

    private Context mContext;
    private List<Tag> mTagList;
    private IEvent mEditEvent;
    private IEvent mDeleteEvent;

    //defines whether of not the list is empty
    private static final int VIEW_TYPE_EMPTY =  0;
    private static final int VIEW_TYPE_NOT_EMPTY = 1;

    public TagRecyclerAdapter(Context context, List<Tag> tagList){
        mContext = context;
        mTagList = tagList;
    }

    //passing on click action right from the start
    public TagRecyclerAdapter(Context context, List<Tag> tagList, IEvent editEvent, IEvent deleteEvent){
        mContext = context;
        mTagList = tagList;
        mEditEvent = editEvent;
        mDeleteEvent = deleteEvent;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView lblTagName;
        ImageView ivOverflow;

        public MyViewHolder(View itemView) {
            super(itemView);
            lblTagName = (TextView) itemView.findViewById(R.id.txt_name_tag);
            ivOverflow = (ImageView) itemView.findViewById(R.id.overflow);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mTagList.size() == 0)
            return VIEW_TYPE_EMPTY;
        else
            return VIEW_TYPE_NOT_EMPTY;
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
            viewItem = layoutInflater.inflate(R.layout.tag_card, parent, false);
        }

        return new MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        //adding information to item(s) if the list if not empty
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_NOT_EMPTY){
            Tag tag = mTagList.get(position);
            holder.lblTagName.setText(tag.getText());
            //menu click listener
            holder.ivOverflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(holder.ivOverflow, position);
                }
            });
        }
    }

    /**
     * executed when overflow clicked
     * */
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
                        mEditEvent.run(view,position);
                        return true;
                    case R.id.action_delete:
                        mDeleteEvent.run(view,position);
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
        //when the list is empty, there is one item
        //the item showing that the list is empty
        if (mTagList.size() == 0)
            return 1;

        //otherwise, the normal quantity of items on the list
        return mTagList.size();
    }

    //setters for the buttons action
    public void setEditEvent(IEvent editEvent) {
        mEditEvent = editEvent;
    }

    public void setDeleteEvent(IEvent deleteEvent) {
        mDeleteEvent = deleteEvent;
    }

}
