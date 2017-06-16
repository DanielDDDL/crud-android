package daniel.com.br.crud.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import daniel.com.br.crud.model.Tag;

/**
 * Created by Dias on 16/06/2017.
 */

@Dao
public interface TagDAO {

    @Query("SELECT * " +
            "FROM tbTags " +
            "WHERE id = :id")
    Tag findTagById(int id);

    @Query("SELECT * FROM tbTags")
    List<Tag> findAllTags();

    @Insert
    void insertTag(Tag tag);

    @Update
    void updateTag(Tag tag);

    @Delete
    void deleteTag(Tag tag);

    @Query("DELETE FROM tbTags")
    void deleteAllTags();

}
