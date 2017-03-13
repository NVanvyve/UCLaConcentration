package groupe.onze.uclaconcentration.objetPerso;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alexis on 11-03-17.
 */

public class EventPerso {
    /* variable de classe */
    private int id;
    private Date eventDate;
    private String eventName;
    private String eventDescr;
    private eventTime heureDeb;
    private eventTime heureFin;

    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd";
    public static final DateFormat dateFormat= new SimpleDateFormat(DATE_FORMAT_NOW);

    public EventPerso(){
        eventDate=null;
        eventName=" Not defined";
        eventDescr=" Not defined";
    }
    public EventPerso(int id,Date date, String name, eventTime deb, eventTime fin){
        this.id=id;
        eventDate=date;
        eventName=name;
        eventDescr=" Not defined";
        heureDeb=deb;
        heureFin=fin;
    }
    public EventPerso(int id,Date date, String name, String descr, eventTime deb, eventTime fin){
        this.id=id;
        eventDate=date;
        eventName=name;
        eventDescr=descr;
        heureDeb=deb;
        heureFin=fin;
    }
    /* Setter */
    public void setEventName(String name){
        eventName=name;
    }
    public void setEventDescr(String descr){
        eventDescr=descr;
    }
    public void setEventDate(Date date){eventDate=date;}
    public void setId(int id) {this.id = id;}

    /* getters */
    public String getEventName(){
        return eventName;
    }
    public String getEventDescr(){
        return eventDescr;
    }
    public Date getEventDate(){ return eventDate;}
    public eventTime getHeureDeb() { return heureDeb;}
    public eventTime getHeureFin() { return heureFin;}
    public int getId() {return id;}

    public String getDateToString() {
        return dateFormat.format(eventDate);
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
}
