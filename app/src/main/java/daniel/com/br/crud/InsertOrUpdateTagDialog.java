package daniel.com.br.crud;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by Dias on 05/07/2017.
 */

public class InsertOrUpdateTagDialog extends DialogFragment {

    private ITagTextEvent mOnPositiveAnswer;
    private String mInitialText;

    public InsertOrUpdateTagDialog(){
        //must be empty
    }

    public InsertOrUpdateTagDialog newInstance(ITagTextEvent onPositiveAnswer, String initialText){

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
        return super.onCreateDialog(savedInstanceState);
    }
}
