package daniel.com.br.crud;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import daniel.com.br.crud.database.DatabaseCreator;
import daniel.com.br.crud.model.Tag;

public class TagFragment extends Fragment {

    private boolean isLoaded;
    private List<Tag> mTagList;

    private RecyclerView mRecyclerView;
    private TagRecyclerAdapter mTagAdapter;

    private FloatingActionButton mFloatingActionButton;

    private Context mContext;

    public TagFragment() {
        isLoaded = false;
        mTagList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        return inflater.inflate(R.layout.fragment_tag, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //setting the adapter
        //and the actions of its buttons after object was constructed
        mTagAdapter = new TagRecyclerAdapter(mContext,mTagList);
        mTagAdapter.setEditEvent(new IEvent() {
            @Override
            public void run(View view, int position) {
                //clicked tag
                Tag selectedTag = mTagList.get(position);

                //TODO: show here editing dialog
//                //information passed to the next activity
//                Intent intent = new Intent(mContext,UpdateOrDeleteActivity.class);
//                intent.putExtra("id",selectedBook.getId());
//                intent.putExtra("title",selectedBook.getTitle());
//                intent.putExtra("author",selectedBook.getAuthor());
//                mContext.startActivity(intent);
            }
        });
        mTagAdapter.setDeleteEvent(new IEvent() {
            @Override
            public void run(View view, final int position) {
                //ask for the user to confirm that he wants to delete the book
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                alertDialogBuilder.setTitle("Deleting Tag")
                        .setMessage("Are you sure you really wanna do this?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //confirming willing to delete tag...
//                                new LoadDeleteBook().execute(position);
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

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_tag_list);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mTagAdapter);

        //showing and hiding fab according to scrolling
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    mFloatingActionButton.show();
                }
                super.onScrollStateChanged(recyclerView,newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && mFloatingActionButton.isShown()){
                    mFloatingActionButton.hide();
                }
            }
        });

        //fab and its action
        mFloatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab_add_new_tag);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: add here insert tag dialog
//                Intent intent = new Intent(mContext,NewBookActivity.class);
//                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        //loading data only if visible to user
        if (isVisibleToUser && !isLoaded && isAdded()){
            new LoadListTag().execute();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //reloading data when back button pressed
        if(getUserVisibleHint()){
            new LoadListTag().execute();
        }
    }

    class LoadListTag extends AsyncTask<Void,Void,List<Tag>>{

        private DatabaseCreator databaseCreator;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            databaseCreator = DatabaseCreator.getsInstance(mContext);
            databaseCreator.createDatabase(mContext);
        }

        @Override
        protected List<Tag> doInBackground(Void... params) {
            List<Tag> tagList = databaseCreator.getDatabase().tagModel().findAllTags();
            return tagList;
        }

        @Override
        protected void onPostExecute(List<Tag> tags) {
            //setting the tags gotten from the database
            //notifying the change to reload
            mTagList.clear();
            mTagList.addAll(tags);
            mTagAdapter.notifyDataSetChanged();

            isLoaded = true;
        }
    }
}
