package daniel.com.br.crud.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Dias on 27/05/2017.
 */

public class DatabaseManager {

    private AtomicInteger openCounter = new AtomicInteger();

    private static DatabaseManager instance;
    private SQLiteDatabase database;
    private SQLiteOpenHelper databaseHelper;

    //singleton constructor
    private DatabaseManager (SQLiteOpenHelper databaseHelper){
        this.databaseHelper = databaseHelper;
    }

    public static synchronized void initializeInstance(SQLiteOpenHelper databaseHelper){
        if (instance == null){
            instance = new DatabaseManager(databaseHelper);
        }
    }

    public static synchronized DatabaseManager getInstance(){
        if (instance == null){
            //method being called before objects being instantiated
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() + " is null. " +
                                            "Call initializeInstance(..) method first");
        }

        return instance;
    }

    private synchronized SQLiteDatabase openDatabase (){
        //increase the number of databases
        if (openCounter.incrementAndGet() == 1){
            database = databaseHelper.getWritableDatabase();
        }

        return database;
    }

    public synchronized void closeDatabase(){
        //decreasing the number of databases
        if (openCounter.decrementAndGet() == 0){
            //closing the connections if none
            database.close();
        }
    }

    public void executeQuery(QueryExecutor executor){
        openDatabase();
        executor.run(database);
        closeDatabase();
    }

    public Object executeQueryGet(QueryExecutorGet executorGet){
        openDatabase();
        Object result = executorGet.run(database);
        closeDatabase();

        return result;
    }

}
