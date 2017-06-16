package daniel.com.br.crud.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.annotations.Nullable;

/**
 * Created by Dias on 16/06/2017.
 */

public class DatabaseCreator {

    private static DatabaseCreator sInstance;
    private AppDatabase mDatabase;

    //for Singleton instantiation
    private static final Object LOCK = new Object();

    //thread safety purposes
    private final AtomicBoolean mInitializing = new AtomicBoolean(true);

    /**
     * Singleton instance getter
     * */
    public synchronized static DatabaseCreator getsInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK){
                if (sInstance == null){
                    sInstance = new DatabaseCreator();
                }
            }
        }

        return sInstance;
    }

    /**
     * get usable instance of database
     * if not created, exception thrown
     * */
    public AppDatabase getDatabase(){
        if (mDatabase == null){
            String errorMessage = "Initialize the database first by calling 'createDatabase' function";
            throw new IllegalStateException(errorMessage);
        }

        return mDatabase;
    }

    /**
     * create the instance of database
     * making sure is thread safe
     * */
    public void createDatabase(Context context){

        if (!mInitializing.compareAndSet(true,false)){
            //the database is already being initialized
            //changing status if is not the case
            return;
        }

        new AsyncTask<Context,Void,Void>(){

            @Override
            protected Void doInBackground(Context... params) {

                Context context = params[0].getApplicationContext();

                // building database
                AppDatabase database = Room.databaseBuilder(context.getApplicationContext(),
                                                            AppDatabase.class,
                                                            AppDatabase.DATABASE_NAME).build();

                //associating it with the instance database
                mDatabase = database;

                return null;
            }

        }.execute(context.getApplicationContext());
    }

}
