package daniel.com.br.crud;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Dias on 05/07/2017.
 */

public class InsertOrUpdateTagDialog extends DialogFragment {

    private ITagTextEvent mOnPositiveAnswer;
    private String mInitialText;

    public InsertOrUpdateTagDialog(){
        //must be empty
    }

    public static InsertOrUpdateTagDialog newInstance(ITagTextEvent onPositiveAnswer, String initialText){

        //TODO: I know this is not the way to do these things
        //TODO: I will study more about saving things to Bundle
        //TODO: But for now, just to get this thing over, let's do it like this, ok?
        //TODO: yep, again...
        InsertOrUpdateTagDialog frag = new InsertOrUpdateTagDialog();
        frag.mOnPositiveAnswer = onPositiveAnswer;
        frag.mInitialText = initialText;

        return frag;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //utils for creating dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //inflating view based on our layout
        View rootView = inflater.inflate(R.layout.dialog_tag_text_input, null);
        builder.setView(rootView);

        //defining text based on wether or not the text passed is null
        String title = mInitialText == null ? "Insert new tag" : "Update tag";
        String posButton = mInitialText == null ? "New tag" : "Update";

        final EditText text = (EditText)rootView.findViewById(R.id.txt_tag);
        text.setText(mInitialText != null ? mInitialText : "");

        builder.setTitle(title);
        builder.setPositiveButton(posButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mOnPositiveAnswer.run(text.getText().toString());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }
}
