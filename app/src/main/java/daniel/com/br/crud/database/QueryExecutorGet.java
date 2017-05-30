package daniel.com.br.crud.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Daniel D. de Lima on 29/05/2017.
 */

public interface QueryExecutorGet {

    Object run(SQLiteDatabase database);

}