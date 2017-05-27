package daniel.com.br.crud.model;

/**
 * Created by Dias on 27/05/2017.
 */

public class Book {
    private int id;
    private String title;
    private String author;

    //constructors
    public Book(int id, String title, String author){
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public Book(){ }

    //getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString(){
        return title;
    }

}
