package daniel.com.br.crud.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import daniel.com.br.crud.model.Book;
import daniel.com.br.crud.model.Tag;
import daniel.com.br.crud.model.TagsInBooks;

/**
 * Created by Dias on 16/06/2017.
 */

@Database(entities = {Book.class, Tag.class, TagsInBooks.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{

    public static final String DATABASE_NAME = "dbBooks";

    public abstract BookDAO bookModel();
    public abstract TagDAO tagModel();
    public abstract TagsInBooksDAO tagsInBooksModel();



}
