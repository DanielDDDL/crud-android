package daniel.com.br.crud.model;

/**
 * Created by Dias on 27/05/2017.
 */

public class Book {
    private int id;
    private String title;
    private String author;
    private String genre;

    //constructors
    public Book(int id, String title, String author, String genre){
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
    }

    public Book (String title, String author, String genre){
        this.title = title;
        this.author = author;
        this.genre = genre;
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

    public String getGenre() { return genre; }

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

    public void setGenre (String genre) { this.genre = genre; }

    @Override
    public String toString(){
        return title;
    }

}
