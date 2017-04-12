package groupe.onze.uclaconcentration.dataBaseMan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by alexis in avr. 2017.
 */

public class accesDB extends SQLiteOpenHelper {
    private static accesDB instance;
    private final static String DATABASE_NAME = "BD_UCLaConcentration";
    private static int  DATABASE_VERSION = 1;
    private Context context;

    private accesDB(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }
    public static synchronized accesDB getInstance(Context context){
        if(instance == null){
            instance = new accesDB(context);
        }
        return instance;
    }
    public void onCreate(SQLiteDatabase db){
        InputStream inputStream = null;
        String line = "";
        try{
            inputStream = context.getAssets().open("generate_bd.sql");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
            while((line = buffer.readLine()) != null){
                db.execSQL(line);
            }

        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(inputStream != null)
                try {inputStream.close();} catch (Throwable ignore){}
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXIST CALENDRIER ");
        onCreate(db);


    }

}
