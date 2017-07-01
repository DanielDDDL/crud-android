package daniel.com.br.crud;

import java.util.List;

import daniel.com.br.crud.model.Tag;

/**
 * Created by Dias on 01/07/2017.
 */

public class StringUtils {

    public static String[] tagListToTagArray (List<Tag> tagList) throws NoTagsException {
        if(tagList.size() == 0){
            throw new NoTagsException();
        }

        String[] tagArray = new String[tagList.size()];
        for (int i = 0; i < tagList.size(); i++){
            tagArray[i] = tagList.get(i).getText();
        }

        return tagArray;
    }

}
