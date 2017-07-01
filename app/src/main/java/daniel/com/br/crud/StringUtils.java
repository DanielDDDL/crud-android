package daniel.com.br.crud;

import java.util.List;

import daniel.com.br.crud.model.Tag;

/**
 * Created by Dias on 01/07/2017.
 */

public class StringUtils {

    /**
     * convert a list of tags passed as parameter into a string array,
     * just using their title
     * */
    public static String[] tagListToTagArray (List<Tag> tagList) throws NoTagsException {

        //TODO: check if tagList if not null

        if(tagList.size() == 0){
            throw new NoTagsException();
        }

        String[] tagArray = new String[tagList.size()];
        for (int i = 0; i < tagList.size(); i++){
            tagArray[i] = tagList.get(i).getText();
        }

        return tagArray;
    }

    /**
     * return a string presentable to the user based
     * on the list of tags passed as parameter
     * */
    public static String tagListToContinuousString(List<Tag> tagList){

        if(tagList.size() == 0)
            return "There is no tag registered for this book";

        String continuousString = "";
        for (int i = 0; i < tagList.size(); i++){

            //separating them
            if(i != 0)
                continuousString += ", ";

            continuousString += tagList.get(i).getText();

        }

        return continuousString;
    }

}
