package daniel.com.br.crud.view.callbacks;

import java.util.List;

import daniel.com.br.crud.model.Tag;

/**
 * Created by Dias on 01/07/2017.
 */

public interface ISelectTagsDialogEvent {

    void run(List<Tag> selectedTags);

}
