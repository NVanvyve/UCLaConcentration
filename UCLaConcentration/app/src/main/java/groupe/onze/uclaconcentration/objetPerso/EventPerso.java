package groupe.onze.uclaconcentration.objetPerso;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alexis on 11-03-17.
 */

public class EventPerso {
    private Date eventDate;
    private String eventName;
    private String eventDescr;
    private eventTime heureDeb;
    private eventTime heureFin;
    public EventPerso(){
        eventDate=null;
        eventName=" Not defined";
        eventDescr=" Not defined";
    }
    public EventPerso(Date date, String name, eventTime deb, eventTime fin){
        eventDate=date;
        eventName=name;
        eventDescr=" Not defined";
        heureDeb=deb;
        heureFin=fin;
    }
    public EventPerso(Date date, String name, String descr, eventTime deb, eventTime fin){
        eventDate=date;
        eventName=name;
        eventDescr=descr;
        heureDeb=deb;
        heureFin=fin;
    }
    public void setEventName(String name){
        eventName=name;
    }
    public void setEventDescr(String descr){
        eventDescr=descr;
    }
    public  void  setEventDate(Date date){
        eventDate=date;
    }
    public String getEventName(){
        return eventName;
    }
    public String getEventDescr(){
        return eventDescr;
    }
    public Date getEventDate(){ return eventDate;}
    public eventTime getHeureDeb() { return heureDeb;}
    public eventTime getHeureFin() { return heureFin;}
}
