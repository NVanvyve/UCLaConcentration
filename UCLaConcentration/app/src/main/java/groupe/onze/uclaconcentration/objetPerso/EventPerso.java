package groupe.onze.uclaconcentration.objetPerso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alexis on 11-03-17.
 */

@SuppressWarnings("ALL")
public class EventPerso {
    /* variable de classe */
    private int _id;
     String _eventDate;
     private String _eventName;
     private String _eventDescr;
     private eventTime _heureDeb;
    private eventTime _heureFin;

    private static final String DATE_FORMAT_NOW = "yyyy-MM-dd";
    private static final DateFormat dateFormat= new SimpleDateFormat(DATE_FORMAT_NOW);

    public EventPerso(){
        _eventDate=null;
        _eventName=" Not defined";
        _eventDescr=" Not defined";
    }
    public EventPerso(int id,String date, String name, eventTime deb, eventTime fin){
        this._id=id;
        this._eventDate=date;
        this._eventName=name;
        this._eventDescr=" Not defined";
        this._heureDeb=deb;
        this._heureFin=fin;
    }
    public EventPerso(int id,String date, String name, eventTime deb, eventTime fin, String descr){
        this._id=id;
        this._eventDate=date;
        this._eventName=name;
        this._eventDescr=descr;
        this._heureDeb=deb;
        this._heureFin=fin;
    }
    /* Setter */
    public void setEventName(String name){
        this._eventName=name;
    }
    public void setEventDescr(String descr){
        this._eventDescr=descr;
    }
    public void setEventDate(String date){this._eventDate=date;}
    public void set_heureDeb(eventTime heureDeb){this._heureDeb=heureDeb;}
    public void set_heureFin(eventTime heureFin){this._heureFin=heureFin;}
    public void setId(int id) {this._id = id;}

    /* getters */
    public String getEventName(){
        return this._eventName;
    }
    public String getEventDescr(){
        return this._eventDescr;
    }
    public String getEventDate(){ return this._eventDate;}
    public eventTime getHeureDeb() { return this._heureDeb;}
    public eventTime getHeureFin() { return this._heureFin;}
    public int getId() {return this._id;}

    public String getDateToString() {
        return dateFormat.format(_eventDate);
    }
    public static String dateToString(Date date) {
    return dateFormat.format(date);
    }
    public static Date StringToDate(String str){
        Date date=null;
        try {
           date=dateFormat.parse(str);
        }catch(ParseException e) {/* TODO Gerer l'exception */}
        return  date;
    }

    public static ArrayList<EventPerso> getAListOfEventPerso() {
        ArrayList<EventPerso> listEventPerso = new ArrayList<EventPerso>();

        listEventPerso.add(new EventPerso(123,"1994-12-22", "Piscine",new eventTime(9,30) , new eventTime(10,30),"yo"));
        listEventPerso.add(new EventPerso(456,"1994-12-22" , "Gym",new eventTime(14,30) , new eventTime(16,30),"yi"));
        listEventPerso.add(new EventPerso(789,"1994-12-25" , "Volley",new eventTime(18,30) , new eventTime(20,30),"yu"));

        return listEventPerso;
    }
}
