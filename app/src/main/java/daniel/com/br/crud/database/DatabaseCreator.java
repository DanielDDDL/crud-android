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

    private final AtomicBoolean mInitializing = new AtomicBoolean(true);

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

    @Nullable
    public AppDatabase getDatabase(){
        return mDatabase;
    }

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
