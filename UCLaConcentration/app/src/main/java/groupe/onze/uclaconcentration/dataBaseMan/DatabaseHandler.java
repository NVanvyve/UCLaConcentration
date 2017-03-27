package groupe.onze.uclaconcentration.dataBaseMan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.EventLog;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import groupe.onze.uclaconcentration.objetPerso.EventPerso;
import groupe.onze.uclaconcentration.objetPerso.eventTime;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "EventManager";

    // Persons table name
    private static final String TABLE_EVENT = "events";

    //TABLE comptes name column
    private static final String KEY_ID="id";
    private static final String KEY_DATEM="datem";
    private static final String KEY_EVENT_NAME ="event_name";

    private static final String KEY_DEBUT="debut";
    private static final String KEY_FIN="fin";
    private static final String KEY_CONTENT="content";


    //Preferences Table name
    private static final String TABLE_PREF="preferences";

    // Preferences Table Columns
    private static final String KEY_LANGUAGE = "language";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_EVENT_TABLE = "CREATE TABLE " + TABLE_EVENT + "(" +
                KEY_ID + " TEXT," + KEY_DATEM + " TEXT," + KEY_EVENT_NAME + " TEXT,"
                + KEY_DEBUT + " TEXT," + KEY_FIN + " TEXT,"+ KEY_CONTENT+ ")"
                ;
        db.execSQL(CREATE_EVENT_TABLE);
/*
        String Create_PREF_TABLE = "CREATE TABLE " + TABLE_PREF + "("
                + KEY_LANGUAGE + "INT" + ")" ;
        db.execSQL(Create_PREF_TABLE);
        */
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PREF);
        // Create tables again
        onCreate(db);
    }

    public void addEvent(EventPerso event){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, event.getId());
        values.put(KEY_DATEM, event.getEventDate());
        values.put(KEY_EVENT_NAME, event.getEventName());


        values.put(KEY_DEBUT, event.getHeureDeb().toString());
        values.put(KEY_FIN, event.getHeureFin().toString());
        values.put(KEY_CONTENT, event.getEventDescr());

        // Inserting Row
        db.insert(TABLE_EVENT, null, values);
        Log.v("YOUPIIIIIII", event.getId()+event.getEventDate());
        db.close(); // Closing database connection
    }
/*

    // Adding new preferences
    public void addPref(Preferences preferences) {
        SQLiteDatabase db = this.getWritableDatabase();



        ContentValues values = new ContentValues();
        values.put(KEY_LOGIN, preferences.get_login());
        values.put(KEY_V_AGE, preferences.get_visibilityAge());
        values.put(KEY_V_NAME,preferences.get_visibilityName());
        values.put(KEY_V_DESCRIPTION,preferences.get_visibilityDescription());
        values.put(KEY_V_NATIONALITY,preferences.get_visibilityNationality());
        values.put(KEY_V_PERSONALITY,preferences.get_visibilityPersonality());
        values.put(KEY_LANGUAGE,preferences.get_language());


        // Inserting Row
        db.insert(TABLE_PREF, null, values);
        db.close(); // Closing database connection
    }


    public Compte getEvent(String id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EVENT, new String[]{KEY_EVENT_NAME,
                        KEY_PASSWORD,}, KEY_LOGIN + "=?",
                new String[]{login}, ")"null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Compte compte = new Compte(cursor.getString(0),
                cursor.getString(1));
        // return person
        return compte;

    }

    public Message getMessage(String loginE, String loginR, String dateM){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MESSAGES, new String[] { KEY_LOGINE,
                        KEY_LOGINR, KEY_DATEM, KEY_CONTENT, }, KEY_LOGINE + "=? AND "+ KEY_LOGINR+ "=? AND "+ KEY_DATEM +"= ?",
                new String[] { loginE, loginR, dateM }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Message message = new Message(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));
        // return person
        return message;

    }

    public List<Message> getAllMessage(String loginE, String loginR){
        List<Message> messageList = new ArrayList<Message>();
        // Select All Query
        Log.v("getAllMess","lancée");

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_MESSAGES, new String[] { KEY_LOGINE,
                        KEY_LOGINR, KEY_DATEM, KEY_CONTENT, }, KEY_LOGINE + "=? AND "+ KEY_LOGINR+ "= ?",
                new String[] { loginE, loginR }, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Message message = new Message(cursor.getString(0),
                        cursor.getString(1), cursor.getString(2),cursor.getString(3));

                Log.v("getAllMess",cursor.getString(0));
                Log.v("getAllMess",cursor.getString(1));
                Log.v("getAllMess",cursor.getString(2));
                Log.v("getAllMess",cursor.getString(3));
                messageList.add(message);
            } while (cursor.moveToNext());
        }

        // return person list
        return messageList;
    }
*/
    public List<EventPerso> getAllEvent(Date date){
        List<EventPerso> rdvList = new ArrayList<EventPerso>();
        // Select All Query
        Log.v("getAllEvent","lancée");

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_EVENT, new String[] {KEY_ID,
                        KEY_DATEM, KEY_EVENT_NAME,  KEY_DEBUT, KEY_FIN, KEY_CONTENT }, KEY_DATEM + "=?",
                new String[] { EventPerso.dateToString(date)}, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                EventPerso datel = new EventPerso(cursor.getInt(0),
                        cursor.getString(1), cursor.getString(2),
                        eventTime.stringToEventTime(cursor.getString(3)),
                        eventTime.stringToEventTime(cursor.getString(4)),
                        cursor.getString(5)
                );

                rdvList.add(datel);
            } while (cursor.moveToNext());
        }

        // return person list
        return rdvList;
    }



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
                        EventPerso obj = new EventPerso(cursor.getInt(0),

                                cursor.getString(1), cursor.getString(2),
                                eventTime.stringToEventTime(cursor.getString(3)),
                                eventTime.stringToEventTime(cursor.getString(4)),
                                cursor.getString(5)
                        );
                        Log.v("DATABASE", String.valueOf(cursor.getInt(0)));
                        Log.v("DATABASE", cursor.getString(1));

                        list.add(obj);
                    } while (cursor.moveToNext());
                }

            } finally {
                try { cursor.close(); } catch (Exception ignore) {}
            }

        } finally {
            try { db.close(); } catch (Exception ignore) {}
        }

        return list;
    }


    public void updateDate(ArrayList<EventPerso> list){
        int size=list.size();
        for (int i =0;i<size;i++){
            EventPerso current=list.get(i);
            SQLiteDatabase db = this.getWritableDatabase();
            db.rawQuery("UPDATE TABLE_EVENT SET JOUR=?,TITRE=?,DESCR=?, HEUREDEP=?,HEUREFIN=? WHERE ID="+current.getId(),
                    new String[]{current.getEventDate(),current.getEventName(),current.getEventDescr(), current.getHeureDeb().toString(),current.getHeureFin().toString()}); // On récupère tout les champs et on les met en texte

        }
    }




    /*
    public int getMessageCount(String loginE, String loginR) {
        int count=0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_MESSAGES, new String[] { KEY_LOGINE,
                        KEY_LOGINR, KEY_DATEM, KEY_CONTENT, }, KEY_LOGINE + "=? AND "+ KEY_LOGINR+ "= ?",
                new String[] { loginE, loginR }, null, null, null, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                count++;
            } while (cursor.moveToNext());
        }

        // return person list
        return count;
    }

    //GETTING ACTUAL USER
    public String getUser() {
        List<User> userList = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.set_login(cursor.getString(0));
                // Adding person to list
                userList.add(user);
                for (User cn : userList) {
                    return cn.get_login();



                }
            } while (cursor.moveToNext());
        }
        return "FAIL";
    }


    // Getting single person
    public Person getPerson(String login) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PERSONS, new String[]{KEY_LOGIN,
                        KEY_NAME, KEY_SURNAME, KEY_NAISSANCE, KEY_FAC, KEY_ORIENTATION, KEY_GENRE, KEY_NATIONALITE, KEY_DESCRIPTION, KEY_IMAGE}, KEY_LOGIN + "=?",
                new String[]{login}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Person person = new Person(cursor.getString(0),
                cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));
        // return person

        db.close();
        return person;
    }

    // Getting the preferences
    public Preferences getPreferences(String login) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PREF, new String[]{KEY_LOGIN,
                        KEY_V_AGE, KEY_V_NAME, KEY_V_PERSONALITY, KEY_V_DESCRIPTION, KEY_V_NATIONALITY, KEY_LANGUAGE}, KEY_LOGIN + "=?",
                new String[]{login}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Preferences preferences = new Preferences(cursor.getString(0),cursor.getInt(1),cursor.getInt(2),cursor.getInt(3),cursor.getInt(4),cursor.getInt(5),cursor.getInt(6));

        // return pref
        return preferences;
    }


    public Person getPersonFromUrl (String imagepath) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PERSONS, new String[]{KEY_LOGIN,
                        KEY_NAME, KEY_SURNAME, KEY_NAISSANCE, KEY_FAC, KEY_ORIENTATION, KEY_GENRE, KEY_NATIONALITE, KEY_DESCRIPTION, KEY_IMAGE}, KEY_IMAGE+ "=?",
                new String[]{imagepath}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Person person = new Person(cursor.getString(0),
                cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));
        // return person

        db.close();

        return person;
    }

    // GET tous les comptes
    public List<Compte> getAllComptes() {
        List<Compte> compteList = new ArrayList<Compte>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_COMPTES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Compte compte = new Compte();
                compte.set_login(cursor.getString(0));
                compte.set_password(cursor.getString(1));
                // Adding person to list
                compteList.add(compte);
            } while (cursor.moveToNext());
        }

        // return person list
        return compteList;
    }

    public List<Person> getAllPersons() {
        List<Person> personList = new ArrayList<Person>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PERSONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Person person = new Person();
                person.set_login(cursor.getString(0));
                person.set_name(cursor.getString(1));
                person.set_surname(cursor.getString(2));
                person.set_naissance(cursor.getString(3));
                person.set_fac(cursor.getString(4));
                person.set_orientation(cursor.getString(5));
                person.set_genre(cursor.getString(6));
                person.set_nationalite(cursor.getString(7));
                person.set_description(cursor.getString(8));
                person.set_photo(cursor.getString(9));
                // Adding person to list
                personList.add(person);
            } while (cursor.moveToNext());
        }

        // return person list
        return personList;
    }
    // Getting persons Count
    public int getPersonsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PERSONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return persons
        return cursor.getCount();
    }

    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        User temp=new User(getUser());
        Log.v("UPDATEUSER", temp.get_login());
        deleteUser(temp);
        Log.v("UPDATEUSER2", temp.get_login());
        addUser(user);
    }

    public int updateCompte(Compte compte) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOGIN, compte.get_login());
        values.put(KEY_PASSWORD, compte.get_password()); // Person Name

        // updating row
        return db.update(TABLE_COMPTES, values, KEY_LOGIN + " = ?",
                new String[]{String.valueOf(compte.get_login())});
    }


    // Updating single person
    public int updatePerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOGIN, person.get_login());
        values.put(KEY_NAME, person.get_name()); // Person Name
        values.put(KEY_SURNAME, person.get_surname()); // Person Name
        values.put(KEY_NAISSANCE, person.get_naissance());
        values.put(KEY_FAC, person.get_fac());
        values.put(KEY_ORIENTATION, person.get_orientation()); // Person Name
        values.put(KEY_GENRE, person.get_genre());
        values.put(KEY_NATIONALITE, person.get_nationalite());
        values.put(KEY_DESCRIPTION, person.get_description());
        values.put(KEY_IMAGE, person.get_photo());



        // updating row
        return db.update(TABLE_PERSONS, values, KEY_LOGIN + " = ?",
                new String[]{String.valueOf(person.get_login())});
    }
    // Deleting single person
    public void deletePerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PERSONS, KEY_LOGIN + " = ?",
                new String[]{String.valueOf(person.get_login())});
        db.close();
    }

    // Deleting compte
    public void deleteCompte(Compte compte) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COMPTES, KEY_LOGIN + " = ?",
                new String[] { String.valueOf(compte.get_login()) });
        db.close();
    }
    // Deleting compte
    public void deleteDate(String login, String daterdv) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DISPO, KEY_LOGIN + "=? AND "+ KEY_DISPO+ "= ?",
                new String[] { login, daterdv });
        db.close();
    }

    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, KEY_LOGIN + " = ?",
                new String[] { String.valueOf(user.get_login()) });
        db.close();
    }

    public int changePref(Preferences preferences) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LOGIN, preferences.get_login());
        values.put(KEY_V_AGE, preferences.get_visibilityAge());
        values.put(KEY_V_NAME,preferences.get_visibilityName());
        values.put(KEY_V_DESCRIPTION,preferences.get_visibilityDescription());
        values.put(KEY_V_NATIONALITY,preferences.get_visibilityNationality());
        values.put(KEY_V_PERSONALITY,preferences.get_visibilityPersonality());
        values.put(KEY_LANGUAGE,preferences.get_language());

        // updating row
        return db.update(TABLE_PREF, values, KEY_LOGIN + " = ?",
                new String[]{String.valueOf(preferences.get_login())});

    }




    public boolean findE(String E){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_AIME, new String[]{KEY_ID_E, KEY_ID_R}, KEY_ID_E + "=?", new String[]{E}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        String emetteur = cursor.getString(0);

        return (emetteur != null);

    }

    public boolean findR(String R){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_AIME, new String[]{KEY_ID_E, KEY_ID_R}, KEY_ID_R + "=?", new String[]{R}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        String emetteur = cursor.getString(0);


        return (emetteur != null);

    }






    public void addAime(String E, String R) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_E, E);
        values.put(KEY_ID_R,R);


        // Inserting Row
        db.insert(TABLE_AIME, null, values);
        db.close(); // Closing database connection
    }
    */
}