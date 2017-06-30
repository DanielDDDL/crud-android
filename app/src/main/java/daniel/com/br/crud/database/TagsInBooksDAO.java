package daniel.com.br.crud.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import daniel.com.br.crud.model.TagInBooksWithTitle;
import daniel.com.br.crud.model.TagsInBooks;

/**
 * Created by Dias on 16/06/2017.
 */

@Dao
public interface TagsInBooksDAO {

    @Query("SELECT * " +
            "FROM tbTagsBooks " +
            "WHERE bookId = :bookId")
    List<TagsInBooks> findAllTagsForBookWithId(int bookId);

    @Query("SELECT * " +
            "FROM tbTagsBooks " +
            "WHERE tagId = :tagId")
    List<TagsInBooks> findAllBooksForTagsWithId(int tagId);

    @Query("SELECT tbTagsBooks.id, tbTags.text " +
           "FROM tbTagsBooks " +
           "INNER JOIN tbTags ON tbTags.id = tbTagsBooks.tagId " +
           "WHERE tbTagsBooks.bookId = :bookId")
    List<TagInBooksWithTitle> findTagsWithNameForBookWithId(int bookId);

    @Insert
    void insertTagInBook(TagsInBooks tagsInBooks);

    @Insert
    void insertMultipleTagsInBook(TagsInBooks... tagsInBooks);

    @Query("DELETE FROM tbTagsBooks WHERE bookId = :bookId")
    void deleteAllTagsForBookWithId(int bookId);

    @Query("DELETE FROM tbTagsBooks WHERE tagId = :tagId")
    void deleteAllTagsForTagsWithId(int tagId);

}
