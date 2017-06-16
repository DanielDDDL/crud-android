package daniel.com.br.crud.model;

/**
 * Created by Dias on 16/06/2017.
 */

/**
 * relationship class between books and tags
 * */
public class TagsInBooks {

    private int id;
    private int bookId;
    private int tagId;

    //constructors
    public TagsInBooks(int id, int bookId, int tagId) {
        this.id = id;
        this.bookId = bookId;
        this.tagId = tagId;
    }

    public TagsInBooks(int bookId, int tagId) {
        this.bookId = bookId;
        this.tagId = tagId;
    }

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
