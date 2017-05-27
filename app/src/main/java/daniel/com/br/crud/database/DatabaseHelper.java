package daniel.com.br.crud.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dias on 27/05/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "db_books";
    public static int VERSAO = 1;
    private Context context;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSAO);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating tables
        db.execSQL(BookDaoSQLite.getCreateTable(context));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(BookDaoSQLite.getDropTable(context));
        onCreate(db);
    }
}
