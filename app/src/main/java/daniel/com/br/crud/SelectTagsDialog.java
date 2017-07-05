package daniel.com.br.crud;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import daniel.com.br.crud.model.Tag;

/**
 * Created by Dias on 01/07/2017.
 */

public class SelectTagsDialog extends DialogFragment {

    private ISelectTagsDialogEvent mOnPositiveAnswer;
    private List<Tag> mRegisteredTags;
    private List<Tag> mSelectedTags;

    public SelectTagsDialog(){
        //constructor must be empty
    }

    public static SelectTagsDialog newInstance(ISelectTagsDialogEvent onPositiveAnswer, List<Tag> registeredTags, List<Tag> selectedTags){

        //TODO: I know this is not the way to do these things
        //TODO: I will study more about saving things to Bundle
        //TODO: But for now, just to get this thing over, let's do it like this, ok?
        SelectTagsDialog frag = new SelectTagsDialog();
        frag.mOnPositiveAnswer = onPositiveAnswer;
        frag.mRegisteredTags = registeredTags;
        frag.mSelectedTags = selectedTags;

        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        try {
            String[] tagsTitle = StringUtils.tagListToTagArray(mRegisteredTags);
            return createSelectionDialog(tagsTitle);

        } catch (NoTagsException e) {
            Log.e(SelectTagsDialog.class.getSimpleName(),"No tag registered");
            return createNoTagDialog();

        }

    }

    public Dialog createNoTagDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("No tag registered")
        .setMessage("Please register a tag before continuing this operation")
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }

    public Dialog createSelectionDialog(String[] tagsTitle){

        boolean [] selectedTagsBooleanArray = gettingSelectedTagsArray(mRegisteredTags,mSelectedTags);
        final List<Integer> selectedItems = getListOfSelectedItems(selectedTagsBooleanArray);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Tags:")
        .setMultiChoiceItems(tagsTitle, selectedTagsBooleanArray, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                //if selected
                if(isChecked)
                    selectedItems.add(which);

                //if selected
                else if(selectedItems.contains(which))
                    selectedItems.remove(Integer.valueOf(which));
            }
        })

        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //adding all selected tags
                List<Tag> addedTags = new ArrayList<>();
                for(int i = 0; i < selectedItems.size(); i++){
                    Tag newTag = mRegisteredTags.get(selectedItems.get(i));
                    addedTags.add(newTag);
                }

                //running event passed to this dialog on creation
                mOnPositiveAnswer.run(addedTags);

            }
        })

        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //simpling dismissing the dialog if operation cancelled
                dialog.dismiss();
            }
        });

        return builder.create();
    }

    /**
     * return a array of boolean to be used on the creation of the dialog
     * */
    private boolean[] gettingSelectedTagsArray (List<Tag> allRegisteredTags,List<Tag> selectedTags){

        //none of the list can be null
        if(selectedTags == null || allRegisteredTags == null)
            throw new IllegalStateException();

        boolean [] selectionArray = new boolean [allRegisteredTags.size()];

        for(int i = 0; i < allRegisteredTags.size(); i++){
            if(isTagIn(allRegisteredTags.get(i),selectedTags)){
                selectionArray[i] = true;
            } else {
                selectionArray[i] = false;
            }
        }

        Log.e(SelectTagsDialog.class.getSimpleName(),"Number of selectedTags: " + selectedTags.size());
        Log.e(SelectTagsDialog.class.getSimpleName(),"Number of registeredTags: " + allRegisteredTags.size());
        Log.e(SelectTagsDialog.class.getSimpleName(),"Number of true values: " + countTrueValues(selectionArray));
        return selectionArray;
    }

    /**
     * return true if currentTag in is selectedTags
     * false otherwise
     * */
    private boolean isTagIn (Tag currentTag, List<Tag> selectedTags){

        //iterating on the selected tags array
        for(int i = 0; i < selectedTags.size(); i++)
            if(currentTag.equals(selectedTags.get(i))) {
                Log.e(SelectTagsDialog.class.getSimpleName(),"Found on the list");
                return true; //found
            }

        return false;
    }

    /**
     * TESTS PURPOSES. TO BE DELETED
     *
     * */
    private int countTrueValues(boolean[] arrayToBeChecked){
        int count = 0;
        for(int i = 0; i < arrayToBeChecked.length; i++)
            if(arrayToBeChecked[i] == true)
                count++;


        return count;
    }

    /**
     * get the array defining what books were selected
     * return a List of Integers correspondents
     * */
    private List<Integer> getListOfSelectedItems(boolean[] selectedItens){

        List<Integer> listOfSelectedItens = new ArrayList<>();
        for(int i = 0; i < selectedItens.length; i++)
            if(selectedItens[i])
                listOfSelectedItens.add(i);

        return listOfSelectedItens;

    }

}
