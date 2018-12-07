package fr.toulouse.miage.ibae.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.toulouse.miage.ibae.MainActivity;
import fr.toulouse.miage.ibae.R;
import fr.toulouse.miage.ibae.fragments.AnnonceFragment;
import fr.toulouse.miage.ibae.metier.Annonce;

/**
 * Adaptateur custom pour afficher une annonce dans un ListView suite à une recherche
 */
public class SearchRowAdapter extends ArrayAdapter<Annonce> {

    ArrayList<Annonce> annonces = new ArrayList<>();
    MainActivity activity;

    /**
     * Constucteur de l'Adaptateur de ligne de recherche
     * @param context Context d'application
     * @param ressourceID ID du layout ressource à une ligne de la liste
     * @param annonces Données à afficher dans la liste
     */
    public SearchRowAdapter(Context context, int ressourceID ,ArrayList<Annonce> annonces, MainActivity activity) {
        super(context,ressourceID, annonces);
        this.annonces = annonces;
        this.activity = activity;
    }


    /**
     * Retourne le nombre d'éléments à afficher
     * @return Le nombre d'éléments présents dans les données de l'adaptateur
     */
    @Override
    public int getCount() {
        return super.getCount();
    }


    /**
     * Crée une ligne de la liste
     * @param position Position dans la liste de données à afficher
     * @param convertView Vue servant de template de ligne
     * @param parent Context de l'application
     * @return La View représentant la ligne à afficher
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.row_search_result, null);

        final Annonce current = annonces.get(position);

        TextView titre = v.findViewById(R.id.row_title);
        TextView desc = v.findViewById(R.id.row_description);
        TextView prix = v.findViewById(R.id.row_price);
        ImageView img = v.findViewById(R.id.row_pic);

        //Mise en place des données dans la ligne
        titre.setText(current.getNom());
        desc.setText(current.getDesciption());
        if (current.getDerniereEnchere() == 0) {
            prix.setText(current.getPrixMin() + " €");
        } else{
            prix.setText(current.getDerniereEnchere() + " €");
        }
        if (current.getPhoto() != null){
            byte[] data = Base64.decode(current.getPhoto(), Base64.DEFAULT);
            img.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
        }

        if (current.getDuree() == 0){
            titre.setTextColor(Color.RED);
        }

        // ajout du listener quand on sélectionne une Annonce
        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (current.getDuree() == 0){
                    Toast.makeText(getContext(), R.string.message_fini, Toast.LENGTH_LONG).show();
                } else{
                    activity.replaceFragment(R.id.contenu, AnnonceFragment.newInstance(current.getId()+""), "annonce");
                    Log.i("LISTENER", "ELEMENT : " + current.getNom());
                }
            }

        });
        return v;
    }
}
