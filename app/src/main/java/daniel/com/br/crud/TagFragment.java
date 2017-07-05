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
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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
            public void run(View view, final int position) {
                //clicked tag
                final Tag selectedTag = mTagList.get(position);

                //EdiText that is going to be featured on the dialog
                final EditText txtTagName = new EditText(mContext);
                txtTagName.setInputType(InputType.TYPE_CLASS_TEXT); //data that can be inserted
                txtTagName.setText(selectedTag.getText());

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setView(txtTagName); //adding EditText

                //setting up dialog and its components
                builder.setTitle("Update Tag:")
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //if not empty...
                                String nameInserted = txtTagName.getText().toString();
                                if (!nameInserted.isEmpty()){
                                    //executing update
                                    String oldText = selectedTag.getText();
                                    String newText = txtTagName.getText().toString();
                                    new LoadEditTag(position).execute(oldText,newText);
                                } else {
                                    //informing error
                                    String errorMessage = "You must fill the specified field";
                                    Toast.makeText(mContext,errorMessage,Toast.LENGTH_SHORT).show();
                                }
                            }})
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //cancelling dialog
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

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
                                new LoadDeleteTag().execute(position);
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

                //EdiText that is going to be featured on the dialog
                final EditText txtTagName = new EditText(mContext);
                txtTagName.setInputType(InputType.TYPE_CLASS_TEXT); //data that can be inserted

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setView(txtTagName); //adding EditText

                //setting up dialog and its components
                builder.setTitle("Insert new Tag:")
                        .setPositiveButton("Insert", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //if not empty...
                                String nameInserted = txtTagName.getText().toString();
                                if (!nameInserted.isEmpty()){
                                    //inserting into the database
                                    new LoadInsertTag().execute(nameInserted);
                                } else {
                                    //informing error
                                    String errorMessage = "You must fill the specified field";
                                    Toast.makeText(mContext,errorMessage,Toast.LENGTH_SHORT).show();

                                }
                            }})
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //cancelling dialog
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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

    class LoadDeleteTag extends AsyncTask<Integer,Void,Integer>{

        private DatabaseCreator databaseCreator;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            databaseCreator = DatabaseCreator.getsInstance(mContext);
            databaseCreator.createDatabase(mContext);
        }

        @Override
        protected Integer doInBackground(Integer... params) {

            //id passed as argument
            int position = params[0];
            Tag deletedTag = databaseCreator.getDatabase().tagModel().findTagBytext(mTagList.get(position).getText());

            //deleting corresponding tag from the database
            //and from the current list
            databaseCreator.getDatabase().tagsInBooksModel().deleteAllTagsForTagsWithId(deletedTag.getId());
            databaseCreator.getDatabase().tagModel().deleteTag(deletedTag);
            mTagList.remove(position);

            return position;
        }

        @Override
        protected void onPostExecute(Integer index) {
            super.onPostExecute(index);
            //updating adapter on the changes occurred
            mTagAdapter.notifyItemRemoved(index);
            mTagAdapter.notifyItemRangeChanged(index,mTagList.size());

        }
    }

    class LoadInsertTag extends AsyncTask<String,Void,Integer>{

        private DatabaseCreator databaseCreator;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            databaseCreator = DatabaseCreator.getsInstance(mContext);
            databaseCreator.createDatabase(mContext);
        }

        @Override
        protected Integer doInBackground(String... params) {

            //creating tag that is going to be inserted
            Tag newTag = new Tag();
            newTag.setText(params[0]);

            int tagsRegisterWithText = databaseCreator.getDatabase().tagModel().numberTagswithText(newTag.getText());
            if(tagsRegisterWithText == 0){
                //inserting tag passed as parameter into the database
                //and into the list as well
                databaseCreator.getDatabase().tagModel().insertTag(newTag);
                mTagList.add(newTag);

                //to notify changes
                return mTagList.size();

            } else {
                return -1;
            }

        }

        @Override
        protected void onPostExecute(Integer index) {
            super.onPostExecute(index);

            if(index != -1)
                //notifying adapter on changes
                mTagAdapter.notifyItemInserted(index);
            else{
                String text = "Tag already registered";
                Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT).show();
            }

        }
    }

    class LoadEditTag extends AsyncTask<String,Void,Boolean>{

        private DatabaseCreator databaseCreator;
        private int mTagIndex; //index on the list of the tag being edited

        public LoadEditTag(int tagIndex){
            mTagIndex = tagIndex;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            databaseCreator = DatabaseCreator.getsInstance(mContext);
            databaseCreator.createDatabase(mContext);
        }

        @Override
        protected Boolean doInBackground(String... params) {

            String oldText = params[0];
            String newText = params[1];

            if(oldText.equals(newText))
                return false;

            //getting the id from the database
            //setting up the object so that holds the tag's id and text
            Tag tag = databaseCreator.getDatabase().tagModel().findTagBytext(oldText);
            tag.setText(newText);

            //editing tag passed as parameter
            //and on the list as well
            databaseCreator.getDatabase().tagModel().updateTag(tag);
            mTagList.remove(mTagIndex);
            mTagList.add(mTagIndex,tag);

            return true;
        }

        @Override
        protected void onPostExecute(Boolean successful) {
            super.onPostExecute(successful);
            if(successful)
                //informing changes to adapter
                mTagAdapter.notifyItemChanged(mTagIndex);
            else{
                String text = "New text is equal to old text. No changes were made.";
                Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT).show();
            }


        }
    }
}
