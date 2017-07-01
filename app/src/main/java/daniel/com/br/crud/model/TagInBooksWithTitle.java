package daniel.com.br.crud.model;

/**
 * Created by Dias on 30/06/2017.
 */

public class TagInBooksWithTitle {

    private int id;
    private int idTag;
    private String text;

    public int getId() {
        return id;
    }

    public int getIdTag() {
        return idTag;
    }

    public void setIdTag(int idTag) {
        this.idTag = idTag;
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
