package groupe.onze.uclaconcentration.objetPerso;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import groupe.onze.uclaconcentration.objetPerso.EventPerso;
import groupe.onze.uclaconcentration.objetPerso.eventTime;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "EventManager";

    // Event table name
    private static final String TABLE_EVENT = "events";

    //TABLE event name column
    private static final String KEY_ID = "id";
    private static final String KEY_DATEM = "datem";
    private static final String KEY_EVENT_NAME = "event_name";

    private static final String KEY_DEBUT = "debut";
    private static final String KEY_FIN = "fin";
    private static final String KEY_CONTENT = "content";


    // Preferences Table Columns
    private static final String KEY_LANGUAGE = "language";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_EVENT_TABLE = "CREATE TABLE " + TABLE_EVENT + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATEM + " TEXT," + KEY_EVENT_NAME + " TEXT,"
                + KEY_DEBUT + " TEXT," + KEY_FIN + " TEXT," + KEY_CONTENT + ")";
        db.execSQL(CREATE_EVENT_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
        // Create tables again
        onCreate(db);
    }

    /* Add the event event to the database */
    public void addEvent(EventPerso event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATEM, event.getEventDate());
        values.put(KEY_EVENT_NAME, event.getEventName());


        values.put(KEY_DEBUT, event.getHeureDeb().toString());
        values.put(KEY_FIN, event.getHeureFin().toString());
        values.put(KEY_CONTENT, event.getEventDescr());

        // Inserting Row
        db.insert(TABLE_EVENT, null, values);
        Log.v("Event added at date: ", event.getEventDate());
        db.close(); // Closing database connection
    }

    /* Deleting the event corresponding to the id*/
    public boolean deleteEvent(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.v("DATABASE", "try to delete number: " + id);
        return db.delete(TABLE_EVENT, KEY_ID + "=" + Integer.toString(id), null) > 0;
        //   return db.delete(TABLE_EVENT, KEY_ID + " = ?", new String[] {Integer.toString(id)})>0;

    }

    /*Return a list of all the event saved in the database*/
    public List<EventPerso> getAllEvents() {

        List<EventPerso> list = new ArrayList<EventPerso>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EVENT;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        Log.v("DATABASE, id: ", String.valueOf(cursor.getInt(0)));
                        Log.v("DATABASE, date: ", cursor.getString(1));
                        EventPerso obj = new EventPerso(

                                cursor.getString(1), cursor.getString(2),
                                eventTime.stringToEventTime(cursor.getString(3)),
                                eventTime.stringToEventTime(cursor.getString(4)),
                                cursor.getString(5)
                        );


                        list.add(obj);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return list;
    }
}