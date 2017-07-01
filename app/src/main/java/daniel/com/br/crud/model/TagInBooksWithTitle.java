package daniel.com.br.crud.model;

/**
 * Created by Dias on 30/06/2017.
 */

public class TagInBooksWithTitle {

    private int id;
    private int tagId;
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
