package groupe.onze.uclaconcentration.objetPerso;

/**
 * Created by alexis on 13-03-17.
 */
/*
crée un objet temps qui ne se préocuppe que des heures et des minutes
 */

@SuppressWarnings("ALL")
public class eventTime {
    /* Variable de classe */
    private int heure=0;
    private int minute=0;
    /*
    constructeur
     */
    public  eventTime(int heure, int minute){
        int tempInt=minute;
        while ( tempInt>60){ // tant que les minutes sont supérieures a 60 , on fait heure++ et minutes-=60
            tempInt-=60;
            this.heure++;
        }
        this.minute = tempInt;
        if((this.heure+heure)<24){
            this.heure+=heure;
        }
    }
/* Getters */
    public int getHeure() {
        return heure;
    }

    public int getMinute() {
        return minute;
    }

    public void setHeure(int heure) {
        if (heure>23){return;}
        this.heure = heure;
    }
/*setters */
    public void setMinute(int minute) {
        int tempInt=minute;
        while ( tempInt>60){
            tempInt-=60;
            this.heure++;
        }
        this.minute = tempInt;
    }
/* comparateurs */
    public boolean equals(eventTime eT ) {
        return (this.heure==eT.heure && this.minute==eT.minute);
    }
/* ToString au format  */
    public String toString() {
        return heure+":"+minute;
    }
    public static eventTime stringToEventTime(String string){
        String[] parts = string.split(":");


        return new eventTime(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }
}
