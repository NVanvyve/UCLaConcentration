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
    /* Renvoie tout les évenement associés a une date */
    public ArrayList<EventPerso> getCalendrier(Date date){
        open();
        SQLiteDatabase db = getDatabase();
        ArrayList <EventPerso> list=new ArrayList<EventPerso>();
        Cursor cursor=db.rawQuery(" SELECT * FROM CALENDRIER WHERE JOUR=?;",new String[]{date.toString()});
        while ( cursor.moveToNext()){
            EventPerso event;
                int id=cursor.getInt(0);
                Date nwDate = EventPerso.StringToDate(cursor.getString(1));
                String titre= cursor.getString(2);
                String desc= cursor.getString(3);
                String []hdepTemp=cursor.getString(4).split(":");
                eventTime hdep=new eventTime(parseInt(hdepTemp[0]),parseInt(hdepTemp[1]));
                String []hfinTemp=cursor.getString(5).split(":");
                eventTime hfin=new eventTime(parseInt(hfinTemp[0]),parseInt(hfinTemp[1]));
                event=new EventPerso(id,nwDate,titre,desc,hdep,hfin);
                list.add(event);
        }
        cursor.close();
        close();
        return list;

    }
    /* On retire l'event avec l'id fournis en argment de la base de donnée */
    public void removeDateDispo(int id){
        open();
        SQLiteDatabase db = getDatabase();
        db.rawQuery(" DELETE FROM CALENDRIER WHERE ID="+id,null);
        close();
    }
    /* On met a jour un évenement dans la base de données */
    public void updateDate(ArrayList<EventPerso> list){
        int size=list.size();
        for (int i =0;i<size;i++){
            EventPerso current=list.get(i);
            SQLiteDatabase db = getDatabase();
            db.rawQuery("UPDATE CALENDRIER SET JOUR=?,TITRE=?,DESCR=?, HEUREDEP=?,HEUREFIN=? WHERE ID="+current.getId(),
                    new String[]{current.getDateToString(),current.getEventName(),current.getEventDescr(), current.getHeureDeb().toString(),current.getHeureFin().toString()}); // On récupère tout les champs et on les met en texte

        }
    }

}
