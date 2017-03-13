package groupe.onze.uclaconcentration.dataBaseMan;
import groupe.onze.uclaconcentration.objetPerso.*;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import static java.lang.Integer.parseInt;

/**
 * Created by alexis on 13-03-17.
 */

public class dayEventListDB extends DAOB {
    /* Nome de la table*/
    private final static String TABLE_CALENDRIER = "CALENDRIER";
    /* Nom des champs */
    private final static String JOUR= "JOUR";
    private final static String TITRE= "TITRE";
    private final static String DESCR = "DESCR";
    private final static String  HEUREDEP= "HEUREDEP";
    private final static String  HEUREFIN= "HEUREFIN";
    private final String DATE_FORMAT_NOW = "yyyy-MM-dd";
    private final DateFormat dateFormat= new SimpleDateFormat(DATE_FORMAT_NOW);

    public dayEventListDB(Context context){super(context);}

    public void insertEvent(Date date, String titre, eventTime dep, eventTime fin){
        open();
        SQLiteDatabase db = getDatabase();
        String dateS=dateFormat.format(date);
        db.rawQuery(" INSERT INTO CALENDRIER VALUES ( ?, ?,NULL,?,?);",new String[]{dateS,titre,dep.toString(),fin.toString()});
        close();
    }
    public void insertEvent(Date date, String titre,String descr, eventTime dep, eventTime fin){
        open();
        SQLiteDatabase db = getDatabase();
        String dateS=dateFormat.format(date);
        db.rawQuery(" INSERT INTO CALENDRIER VALUES ( ?, ?,?,?,?);",new String[]{dateS,titre,descr,dep.toString(),fin.toString()});
        close();
    }
    public ArrayList<EventPerso> getCalendrier(Date date){
        open();
        SQLiteDatabase db = getDatabase();
        ArrayList <EventPerso> list=new ArrayList<EventPerso>();
        Cursor cursor=db.rawQuery(" SELECT * FROM CALENDRIER WHERE JOUR=?;",new String[]{date.toString()});
        while ( cursor.moveToNext()){
            EventPerso event;
            try {
                Date nwDate = dateFormat.parse(cursor.getString(0));
                String titre= cursor.getString(1);
                String desc= cursor.getString(2);
                String []hdepTemp=cursor.getString(3).split(":");
                eventTime hdep=new eventTime(parseInt(hdepTemp[0]),parseInt(hdepTemp[1]));
                String []hfinTemp=cursor.getString(4).split(":");
                eventTime hfin=new eventTime(parseInt(hfinTemp[0]),parseInt(hfinTemp[1]));
                event=new EventPerso(nwDate,titre,desc,hdep,hfin);
                list.add(event);
            }catch (ParseException e) { /* TODO Mettre un code pour gérer l'exception  */}
        }
        cursor.close();
        close();
        return list;

    }
    public void removeDateDispo(Date date,String titre){
        open();
        SQLiteDatabase db = getDatabase();
        String dateS=dateFormat.format(date);
        db.rawQuery(" DELETE FROM CALENDRIER WHERE JOUR=? and TITRE=? ;",new String[]{dateS,titre});
        close();
    }

}
