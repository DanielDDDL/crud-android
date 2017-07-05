package daniel.com.br.crud.model;

/**
 * Created by Dias on 16/06/2017.
 */

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/**
 * relationship class between books and tags
 * */
@Entity(foreignKeys = {

        @ForeignKey(entity = Book.class,
                    parentColumns = "id",
                    childColumns = "bookId"),

        @ForeignKey(entity = Tag.class,
                    parentColumns = "id",
                    childColumns = "tagId")
},
tableName = "tbTagsBooks")
public class TagsInBooks {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int bookId;
    private int tagId;

    //getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
}
