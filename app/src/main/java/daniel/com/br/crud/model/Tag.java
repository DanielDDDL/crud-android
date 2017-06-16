package daniel.com.br.crud.model;

/**
 * Created by Dias on 16/06/2017.
 */

public class Tag {

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
