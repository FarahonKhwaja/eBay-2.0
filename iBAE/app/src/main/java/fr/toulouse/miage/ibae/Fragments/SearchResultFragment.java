package fr.toulouse.miage.ibae.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import fr.toulouse.miage.ibae.R;
import fr.toulouse.miage.ibae.Ressources;
import fr.toulouse.miage.ibae.adapter.SearchRowAdapter;
import fr.toulouse.miage.ibae.metier.Annonce;


public class SearchResultFragment extends Fragment {


    private ListView listView;
    private ArrayList<Annonce> lesAnnonces;
    View view;
    List<String> test;

    SearchRowAdapter adapter;

    public SearchResultFragment() {
        lesAnnonces = new ArrayList<>();
        test = new ArrayList<>();
    }


    public static SearchResultFragment newInstance() {
        SearchResultFragment fragment = new SearchResultFragment();

        return fragment;
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
        lesAnnonces.add(new Annonce());
        adapter = new SearchRowAdapter(getContext(), R.layout.row_search_result,lesAnnonces);
        listView.setAdapter(adapter);
        Log.i("TEST", "Count adapter : " + adapter.getCount());
        requeteRecherche();
        return view;
    }

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

    private ArrayList<Annonce> parseAnnonces (JSONArray response) throws JSONException {
        ArrayList<Annonce> lesAnnonces = new ArrayList<>();
        for (int i = 0 ; i < response.length() ; i++ ){
            JSONObject obj = response.getJSONObject(i);
            Annonce annonce = new Annonce();
            annonce.setId(obj.getInt("id"));
            annonce.setNom(obj.getString("nom"));
            test.add(obj.getString("nom"));
            try{
                annonce.setDesciption(obj.getString("description"));
            } catch (JSONException e){
                annonce.setDesciption("");
            }
            annonce.setPrixMin(obj.getDouble("prix_min"));
            try{
                Timestamp dateCreation = new Timestamp(Long.parseLong(obj.getString("dateCreation")));
                annonce.setDateCreation(dateCreation);
                annonce.setDuree(obj.getInt("duree"));
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
