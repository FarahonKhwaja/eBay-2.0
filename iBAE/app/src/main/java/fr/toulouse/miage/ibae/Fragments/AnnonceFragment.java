package fr.toulouse.miage.ibae.fragments;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import fr.toulouse.miage.ibae.MainActivity;
import fr.toulouse.miage.ibae.R;
import fr.toulouse.miage.ibae.Ressources;
import fr.toulouse.miage.ibae.metier.Annonce;

/**
 * Fragment d'une annonce sur laquelle on peut enchérir
 */
public class AnnonceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CODE = "code";

    // TODO: Rename and change types of parameters
    private String code;
    private Annonce content;


    public AnnonceFragment() {
        // Required empty public constructor
    }


    public static AnnonceFragment newInstance(String code) {
        AnnonceFragment fragment = new AnnonceFragment();
        Bundle args = new Bundle();
        args.putString(CODE, code);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            code = getArguments().getString(CODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_annonce, container, false);
        rechercheAnnonce(code);
        return v;
    }

    private void rechercheAnnonce(String code){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = Ressources.URL + "/annonce/"+code;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET ,url,null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                content = JSONToAnnonce(response);
                setAnnonce(getView());
                Log.i("SERVER RESPONSE", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SERVER RESPONSE", error.toString());
            }
        });
        queue.add(request);
    }

    /**
     * Parse un objet JSON en Annonce
     * @param obj JSONObject qui contient les données
     * @return
     */
    private Annonce JSONToAnnonce(JSONObject obj){
        Annonce annonce = new Annonce();
        try {
            annonce.setId(obj.getInt("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            annonce.setNom(obj.getString("nom"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try{
            annonce.setDesciption(obj.getString("description"));
        } catch (JSONException e){
            annonce.setDesciption("");
        }
        try {
            annonce.setPrixMin(obj.getDouble("prix_min"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try{
            Long dateCreation = obj.getLong("dateCreation");
            annonce.setDateCreation(dateCreation);
            Date date = new Date(dateCreation);
            Date current = new Date();
            long diff = minutesBetween(date, current);
            if (diff > 5){
                annonce.setDuree((long)0);
            } else{
                annonce.setDuree(5 - diff);
            }
        } catch (JSONException e){
        }
        try {
            annonce.setPhoto(obj.getString("photo"));
        } catch (JSONException e){
            annonce.setPhoto("");

        }
        try{
            annonce.setCreePar(obj.getString("utilisateurCreation"));
        } catch (JSONException e){

        }
        try{
            annonce.setEtat(obj.getString("etat"));
        } catch (JSONException e){

        }
        try{
            annonce.setDerniereEnchere(obj.getDouble("derniereEnchere"));
            annonce.setUtilisateurEnchere(obj.getString("utilisateurEnchere"));
        } catch (JSONException e){
            annonce.setDerniereEnchere(0.00);
            annonce.setUtilisateurEnchere("");
        }
        return annonce;
    }

    /**
     * Donne la différence en minute entre deux dates
     * @param first Première date
     * @param second Seconde date
     * @return
     */
    private Long minutesBetween(Date first, Date second){
        return (second.getTime() - first.getTime())/(1000*60);
    }

    /**
     * Intégre les données de l'annonces dans les composants de la vue
     * @param v View de l'annonce
     */
    private void setAnnonce(View v){
        ImageView img = v.findViewById(R.id.annonce_img);
        TextView name = v.findViewById(R.id.annonce_titre);
        TextView description = v.findViewById(R.id.annonce_description);
        TextView lastEnchere = v.findViewById(R.id.annonce_lastenchere);
        TextView prix = v.findViewById(R.id.annonce_price);
        TextView vendeur = v.findViewById(R.id.annonce_vendeur);
        TextView timeLeft = v.findViewById(R.id.annonce_timeleft);

        if (content.getPhoto() != null){
            byte[] data = Base64.decode(content.getPhoto(), Base64.DEFAULT);
            img.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
        }
        name.setText(content.getNom());
        description.setText(content.getDesciption());
        lastEnchere.setText(content.getUtilisateurEnchere());
        vendeur.setText(content.getCreePar());
        timeLeft.setText(content.getDuree()+" min");
        if (content.getDerniereEnchere() != 0.0){
            prix.setText(content.getDerniereEnchere() + " €");
        } else {
            prix.setText(content.getPrixMin() + " €");
        }
        MainActivity activity = (MainActivity) getActivity();
        activity.content = content;
    }

}
