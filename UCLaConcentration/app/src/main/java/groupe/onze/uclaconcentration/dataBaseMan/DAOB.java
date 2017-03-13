package groupe.onze.uclaconcentration.dataBaseMan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by alexis on 13-03-17.
 */

public abstract class DAOB {
    protected SQLiteDatabase database;
    protected accesDB handler;

    public DAOB(Context context){
        this.handler = accesDB.getInstance(context);
    }

    public void open(){
        database = handler.getWritableDatabase();
    }

    public void close(){
        database.close();
    }

    public SQLiteDatabase getDatabase(){return database;}
}
