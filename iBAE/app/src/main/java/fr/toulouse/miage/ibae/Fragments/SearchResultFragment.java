package fr.toulouse.miage.ibae.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import fr.toulouse.miage.ibae.MainActivity;
import fr.toulouse.miage.ibae.R;
import fr.toulouse.miage.ibae.Ressources;
import fr.toulouse.miage.ibae.adapter.SearchRowAdapter;
import fr.toulouse.miage.ibae.metier.Annonce;


/**
 * Fragment restituant les résultats d'une recherche dans un ListView
 */
public class SearchResultFragment extends Fragment {


    private ListView listView;
    private ArrayList<Annonce> lesAnnonces;
    View view;

    SearchRowAdapter adapter;

    /**
     * Constructeur du Fragment
     */
    public SearchResultFragment() {
        lesAnnonces = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search_result, container, false);
        listView = view.findViewById(R.id.search_list);
        adapter = new SearchRowAdapter(getContext(), R.layout.row_search_result,lesAnnonces, (MainActivity)getActivity());
        listView.setAdapter(adapter);
        requeteRecherche();
        return view;
    }

    /**
     * Requête sur la base de données mettant la liste des données à afficher à jour
     */
    public void requeteRecherche(){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = Ressources.URL + "/annonces";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    lesAnnonces = parseAnnonces(response);
                    adapter.addAll(lesAnnonces);
                    adapter.notifyDataSetChanged();
                    Log.i("TRAITEMENT", "terminé");
                    Log.i("TRAITEMENT", "Count adapter : " + adapter.getCount());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ServResponse", error.toString());
            }
        });
        queue.add(request);
    }

    /**
     * Convertit un tableau de données JSON en une ArrayList d'Annonces
     * @param response JSONArray : réponse du serveur contenant les données venant de Mongo
     * @return ArrayList des Annonces à afficher
     * @throws JSONException
     */
    private ArrayList<Annonce> parseAnnonces (JSONArray response) throws JSONException {
        ArrayList<Annonce> lesAnnonces = new ArrayList<>();
        for (int i = 0 ; i < response.length() ; i++ ){
            JSONObject obj = response.getJSONObject(i);
            Annonce annonce = new Annonce();
            annonce.setId(obj.getInt("id"));
            annonce.setNom(obj.getString("nom"));
            try{
                annonce.setDesciption(obj.getString("description"));
            } catch (JSONException e){
                annonce.setDesciption("");
            }
            annonce.setPrixMin(obj.getDouble("prix_min"));
            try{
                Long dateCreation = obj.getLong("dateCreation");
                annonce.setDateCreation(dateCreation);
            } catch (JSONException e){
            }
            try {
                annonce.setPhoto(obj.getString("photo"));
            } catch (JSONException e){
                annonce.setPhoto("");

            }
            try{
                annonce.setEtat(obj.getString("etat"));
            } catch (JSONException e){

            }
            try{
                annonce.setCreePar(obj.getString("utilisateurCreation"));
            } catch (JSONException e){

            }
            try{
                annonce.setDerniereEnchere(obj.getDouble("derniereEnchere"));
                annonce.setUtilisateurEnchere(obj.getString("utilisateurEnchere"));
            } catch (JSONException e){
                annonce.setDerniereEnchere(0.00);
                annonce.setUtilisateurEnchere("");
            }
            lesAnnonces.add(annonce);
        }
        return lesAnnonces;
    }
}
