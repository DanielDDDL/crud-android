package daniel.com.br.crud.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import daniel.com.br.crud.R;
import daniel.com.br.crud.model.Book;
import daniel.com.br.crud.model.IBookDao;

/**
 * Created by Dias on 27/05/2017.
 */

public class BookDaoSQLite implements IBookDao {

    interface Table {
        String TABLE_NAME = "books";
        String COLUMN_ID = "id";
        String COLUMN_TITLE = "title";
        String COLUM_AUTHOR = "author";
    }

    private Context context;

    public BookDaoSQLite(Context context){
        this.context = context;

        //initiating our singleton class
        DatabaseManager.initializeInstance(new DatabaseHelper(context));
    }

    //queries being executed to create and drop database tables
    public static String getCreateTable(Context context){
        return context.getString(R.string.create_table_books);
    }

    public static String getDropTable(Context context){
        return context.getString(R.string.drop_table_books);
    }

    @Override
    public void insertBook(final Book book) {

        DatabaseManager.getInstance().executeQuery(new IQueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                ContentValues values = new ContentValues();
                values.put(Table.COLUMN_TITLE,book.getTitle());
                values.put(Table.COLUM_AUTHOR,book.getAuthor());

                database.insert(Table.TABLE_NAME,null,values);
            }
        });
    }

    @Override
    public void delete(final int id) {

        DatabaseManager.getInstance().executeQuery(new IQueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                String whereClause = Table.COLUMN_ID + " = " + id;
                database.delete(Table.TABLE_NAME,whereClause,null);
            }
        });
    }

    @Override
    public List<Book> selectBooks() {

        //I am sorry...
        final List<List<Book>> data = new ArrayList<List<Book>>();

        DatabaseManager.getInstance().executeQuery(new IQueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                String[] columns = {
                        Table.COLUMN_ID,
                        Table.COLUMN_TITLE,
                        Table.COLUM_AUTHOR
                };
                Cursor cursor = database.query(Table.TABLE_NAME,columns,null,null,null,null,null,null);
                data.add(manageCursor(cursor));
            }
        });

        return data.get(0);
    }

    private List<Book> manageCursor(Cursor cursor){
        List<Book> data = new ArrayList<>();
        if (cursor != null){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                Book book = cursorToData(cursor);
                data.add(book);
                cursor.moveToNext();
            }
        }
        return data;
    }

    private Book cursorToData(Cursor cursor){
        int idIndex = cursor.getColumnIndex(Table.COLUMN_ID);
        int titleIndex = cursor.getColumnIndex(Table.COLUMN_TITLE);
        int authorIndex = cursor.getColumnIndex(Table.COLUM_AUTHOR);

        Book book = new Book();
        book.setId(cursor.getInt(idIndex));
        book.setTitle(cursor.getString(titleIndex));
        book.setAuthor(cursor.getString(authorIndex));

        return book;
    }

    @Override
    public void update(final Book book, final int id) {

        DatabaseManager.getInstance().executeQuery(new IQueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                //new values
                ContentValues values = new ContentValues();
                values.put(Table.COLUMN_TITLE,book.getTitle());
                values.put(Table.COLUM_AUTHOR,book.getAuthor());

                String whereClause = Table.COLUMN_ID + " = " + id;
                database.update(Table.TABLE_NAME,values,whereClause,null);
            }
        });
    }
}
