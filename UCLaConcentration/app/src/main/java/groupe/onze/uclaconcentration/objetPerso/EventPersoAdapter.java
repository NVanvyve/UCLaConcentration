package groupe.onze.uclaconcentration.objetPerso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import groupe.onze.uclaconcentration.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aetheya on 5/05/2016.
 * Interface pour écouter les évènements sur le nom d'une personne
 */


public class EventPersoAdapter extends BaseAdapter {
    public interface DispoAdapterListener {
        void onClickNom(EventPerso item,int position);
    }


    //Contient la liste des listeners
    private ArrayList<DispoAdapterListener> mListListener = new ArrayList<>();

    /**
     * Pour ajouter un listener sur notre adapter
     */
    public void addListener(DispoAdapterListener aListener) {
        mListListener.add(aListener);
    }

    private void sendListener(EventPerso item, int position) {
        for (int i = mListListener.size() - 1; i >= 0; i--) {
            mListListener.get(i).onClickNom(item, position);
        }
    }

    // Une liste d'evenements
    private List<EventPerso> mListM;

    //Un mécanisme pour gérer l'affichage graphique depuis un layout XML
    private LayoutInflater mInflater;

    public EventPersoAdapter(Context context, List<EventPerso> aListM) {
        Context mContext = context;
        mListM = aListM;
        mInflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return mListM.size();
    }

    public Object getItem(int position) {
        return mListM.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout layoutItem;
        //(1) : Réutilisation des layouts
        if (convertView == null) {
            //Initialisation de notre item à partir du  layout XML "message_left.xml"
            layoutItem = (RelativeLayout) mInflater.inflate(R.layout.activity_list_event, parent, false);
        } else {
            layoutItem = (RelativeLayout) convertView;
        }
        //(2) : Récupération des TextView de notre layout
        TextView textrdv = (TextView) layoutItem.findViewById(R.id.descr);

        //(3) : Renseignement des valeurs
        textrdv.setText(mListM.get(position)._eventDate+"\n"
                +mListM.get(position)._eventName+"\n" + "De :"
                +mListM.get(position)._heureDeb+"\n"+ "A :"
                +mListM.get(position)._heureFin+"\n" +"[ "
                +mListM.get(position)._eventDescr+ " ]");

        //------------ Début de l'ajout -------
        //On mémorise la position de l'event dans le composant textview
        textrdv.setTag(position);
        //CHOIX DE LA OU IL FAUT CLIQUER

        //On ajoute un listener
        textrdv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Lorsque l'on clique sur le nom, on récupère la position de de l'event
                Integer position = (Integer) v.getTag();

                //On prévient les listeners qu'il y a eu un clic sur le TextView "TV_nom"
                sendListener(mListM.get(position), position);
            }

        });

        return layoutItem;
    }

    public List<EventPerso> getData() {
        return mListM;
    }
}
