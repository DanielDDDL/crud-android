package daniel.com.br.crud.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import daniel.com.br.crud.R;
import daniel.com.br.crud.model.Book;
import daniel.com.br.crud.model.IBookDao;

/**
 * Created by Dias on 27/05/2017.
 */

public class IBookDaoSQLite implements IBookDao {

    interface Table {
        String COLUMN_ID = "id";
        String COLUMN_TITLE = "title";
        String COLUM_AUTHOR = "author";
    }

    private SQLiteDatabase database;
    private Context context;

    public IBookDaoSQLite(SQLiteDatabase database, Context context){
        this.database = database;
        this.context = context;
    }

    //queries sendo executadas pelo banco para criar e atualizar as tabelas
    public static String getCreateTable(Context context){
        return context.getString(R.string.create_table_books);
    }

    public static String getDropTable(Context context){
        return context.getString(R.string.drop_table_books);
    }

    @Override
    public void insertBook(Book book) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public List<Book> selectBooks() {
        return null;
    }

    @Override
    public void update(Book book, int id) {

    }
}
