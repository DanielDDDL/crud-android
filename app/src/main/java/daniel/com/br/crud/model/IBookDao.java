package daniel.com.br.crud.model;

import java.util.List;

/**
 * Created by Dias on 27/05/2017.
 */

public interface IBookDao {

    public void insertBook (Book book);
    public void delete (int id);
    public List<Book> selectBooks();
    public void update (Book book, int id);

}
