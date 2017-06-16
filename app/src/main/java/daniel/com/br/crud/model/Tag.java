package daniel.com.br.crud.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

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


}
