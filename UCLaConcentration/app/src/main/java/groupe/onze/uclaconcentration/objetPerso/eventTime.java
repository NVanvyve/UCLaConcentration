package groupe.onze.uclaconcentration.objetPerso;

/**
 * Created by alexis on 13-03-17.
 */

public class eventTime {
    /* Variable de classe */
    int heure=0;
    int minute=0;
    public  eventTime(int heure, int minute){
        int tempInt=minute;
        while ( tempInt>60){
            tempInt-=60;
            this.heure++;
        }
        this.minute = tempInt;
        if((this.heure+heure)<24){
            this.heure+=heure;
        }
    }

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

    public void setMinute(int minute) {
        int tempInt=minute;
        while ( tempInt>60){
            tempInt-=60;
            this.heure++;
        }
        this.minute = tempInt;
    }

    public boolean equals(eventTime eT ) {
        return (this.heure==eT.heure && this.minute==eT.minute);
    }

    public String toString() {
        return heure+":"+minute;
    }
}
