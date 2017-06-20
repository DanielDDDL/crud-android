package daniel.com.br.crud;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import daniel.com.br.crud.model.Tag;

/**
 * Created by Dias on 20/06/2017.
 */

public class TagRecyclerAdapter extends RecyclerView.Adapter<TagRecyclerAdapter.MyViewHolder>{

    private Context mContext;
    private List<Tag> mTagList;
    private IEvent mEditEvent;
    private IEvent mDeleteEvent;

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

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getItemCount() {
        throw new UnsupportedOperationException();
    }

}
