package daniel.com.br.crud.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Dias on 16/06/2017.
 */

@Entity
public class Tag {

    @PrimaryKey
    private int id;

    private String text;

    //constructors
    public Tag (int id, String text){
        this.id = id;
        this.text = text;
    }

    public Tag (String text){
        this.text = text;
    }

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


}
