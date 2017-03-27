package groupe.onze.uclaconcentration.objetPerso;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import groupe.onze.uclaconcentration.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import groupe.onze.uclaconcentration.objetPerso.EventPerso;

/**
 * Created by Aetheya on 5/05/2016.
 * <p/>
 * /**
 * Interface pour écouter les évènements sur le nom d'une personne
 */


public class EventPersoAdapter extends BaseAdapter {
    public interface DispoAdapterListener {
        public void onClickNom(EventPerso item, int position);
    }


    //Contient la liste des listeners
    private ArrayList<DispoAdapterListener> mListListener = new ArrayList<DispoAdapterListener>();

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

    // Une liste de personnes
    private List<EventPerso> mListM;

    //Le contexte dans lequel est présent notre adapter
    private Context mContext;

    //Un mécanisme pour gérer l'affichage graphique depuis un layout XML
    private LayoutInflater mInflater;

    public EventPersoAdapter(Context context, List<EventPerso> aListM) {
        mContext = context;
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
        textrdv.setText(mListM.get(position)._eventDate);

        //------------ Début de l'ajout -------
        //On mémorise la position de la "Personne" dans le composant textview
        textrdv.setTag(position);
        //CHOIX DE LA OU IL FAUT CLIQUER

        //On ajoute un listener
        textrdv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Lorsque l'on clique sur le nom, on récupère la position de la "Personne"
                Integer position = (Integer) v.getTag();

                //On prévient les listeners qu'il y a eu un clic sur le TextView "TV_Nom".
                sendListener(mListM.get(position), position);
            }

        });

        return layoutItem;
    }

    public List<EventPerso> getData() {
        return mListM;
    }
}
