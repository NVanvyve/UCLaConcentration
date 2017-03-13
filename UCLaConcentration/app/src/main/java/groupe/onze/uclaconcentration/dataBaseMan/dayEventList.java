package groupe.onze.uclaconcentration.dataBaseMan;

import android.app.usage.UsageEvents;
import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

import groupe.onze.uclaconcentration.objetPerso.EventPerso;
import groupe.onze.uclaconcentration.objetPerso.eventTime;

/**
 * Created by alexis on 13-03-17.
 */

public class dayEventList {
    /*variable classe */
    private ArrayList<EventPerso> EventList=null;

    /* Cosntructeur */
    public  dayEventList(ArrayList<EventPerso> List){
        EventList=List;
    }
    /* getter */
    public ArrayList<EventPerso> getEventList(){
        return EventList;
    }
    /* Adder */
    public void addEvent(Context context,Date date, String titre, eventTime heureDeb, eventTime heureFin){
        dayEventListDB DAO = new dayEventListDB(context);
        DAO.insertEvent(date,titre,heureDeb,heureFin);
    }
    public void addEvent(Context context,Date date, String titre,String descr, eventTime heureDeb, eventTime heureFin){
        dayEventListDB DAO = new dayEventListDB(context);
        DAO.insertEvent(date,titre,descr,heureDeb,heureFin);
    }
    public void removeEvent(Context context,Date date,String titre){
        dayEventListDB DAO = new dayEventListDB(context);
        DAO.removeDateDispo(date,titre);
    }
    public ArrayList<EventPerso> getDateFromDatabase(Context context,Date date){
        dayEventListDB DAO = new dayEventListDB(context);
        return  DAO.getCalendrier(date);
    }
}
