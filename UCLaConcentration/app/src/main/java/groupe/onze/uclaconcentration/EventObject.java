package groupe.onze.uclaconcentration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by alexis on 11-03-17.
 */

class EventObject {
    private SimpleDateFormat eventDate;
    private String eventName;
    private String eventDescr;
    public EventObject(){
        eventDate=null;
        eventName=" Not defined";
        eventDescr=" Not defined";
    }
    public EventObject(String name,SimpleDateFormat date){
        eventDate=date;
        eventName=name;
        eventDescr=" Not defined";
    }
    public EventObject(String name,SimpleDateFormat date,String descr){
        eventDate=date;
        eventName=name;
        eventDescr=descr;
    }
    public void setEventName(String name){
        eventName=name;
    }
    public void setEventDescr(String descr){
        eventDescr=descr;
    }
    public  void  setEventDate(SimpleDateFormat date){
        eventDate=date;
    }
    public String getEventName(){
        return eventName;
    }
    public String getEventDescr(){
        return eventDescr;
    }
    public SimpleDateFormat getEventDate(){
        return eventDate;
    }
}
