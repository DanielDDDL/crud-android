package daniel.com.br.crud.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

import static daniel.com.br.crud.StringUtils.stringEquals;

/**
 * Created by Dias on 16/06/2017.
 */

@Entity(tableName = "tbTags")
public class Tag {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String text;

    //getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Tag))
            return false;


        Tag otherOne = (Tag)obj;
        if(otherOne.getId() == this.getId() &&
           stringEquals(otherOne.getText(),this.getText())) {
            return true;
        }

        return false;

    }
}
