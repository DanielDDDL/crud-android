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

    private ISelectTagsDialogEvent mOnPositeveAnswer;
    private List<Tag> mRegisteredTags;

    public SelectTagsDialog(){
        //constructor must be empty
    }

    public static SelectTagsDialog newInstance(ISelectTagsDialogEvent onPositiveAnswer, List<Tag> registeredTags){

        //TODO: I know this is not the way to do these things
        //TODO: I will study more about saving things to Bundle
        //TODO: But for now, just to get this thing over, let's do it like this, ok?
        SelectTagsDialog frag = new SelectTagsDialog();
        frag.mOnPositeveAnswer = onPositiveAnswer;
        frag.mRegisteredTags = registeredTags;

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

        final List<Integer> selectedItems = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Tags:")
        .setMultiChoiceItems(tagsTitle, null, new DialogInterface.OnMultiChoiceClickListener() {
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
                mOnPositeveAnswer.run(addedTags);

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
}
