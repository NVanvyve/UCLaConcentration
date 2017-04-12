package groupe.onze.uclaconcentration.dataBaseMan;

import java.util.ArrayList;

import groupe.onze.uclaconcentration.objetPerso.EventPerso;

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
    }}
    /* Adder */
    /*
    public void addEvent(Context context,Date date, String titre, eventTime heureDeb, eventTime heureFin){
        dayEventListDB DAO = new dayEventListDB(context);
        DAO.insertEvent(date,titre,heureDeb,heureFin);
    }
    public void addEvent(Context context,Date date, String titre,String descr, eventTime heureDeb, eventTime heureFin){
        dayEventListDB DAO = new dayEventListDB(context);
        DAO.insertEvent(date,titre,descr,heureDeb,heureFin);
    }
    public void removeEvent(Context context,int id){
        dayEventListDB DAO = new dayEventListDB(context);
        DAO.removeDateDispo(id);
    }
    public ArrayList<EventPerso> getDateFromDatabase(Context context,Date date){
        dayEventListDB DAO = new dayEventListDB(context);
        return  DAO.getCalendrier(date);
    }
}
*/
