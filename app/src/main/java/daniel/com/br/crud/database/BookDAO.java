package daniel.com.br.crud.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import daniel.com.br.crud.model.Book;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Dias on 16/06/2017.
 */

@Dao
public interface BookDAO {

    @Query("SELECT * " +
            "FROM tbBooks " +
            "WHERE id = :id")
    Book findBookById(int id);

    @Query("SELECT * FROM tbBooks")
    List<Book> findAllBooks();

    @Insert(onConflict = IGNORE)
    void insertBook(Book book);

    @Update(onConflict = REPLACE)
    void updateBook(Book book);

    @Delete
    void deleteBook(Book book);

    @Query("DELETE FROM tbBooks " +
           "WHERE id = :id")
    void deleteBookWithId(int id);

    @Query("DELETE FROM tbBooks")
    void deleteAllBooks();

}
