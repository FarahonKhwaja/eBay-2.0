package fr.toulouse.miage.ibae;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.logging.Level;
import java.util.logging.Logger;


public class InscriptionActivity extends AppCompatActivity {

    private EditText nom, prenom, mail, adresse, username, password1, password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        nom = findViewById(R.id.et_nom);
        prenom = findViewById(R.id.et_prenom);
        mail = findViewById(R.id.et_mail);
        adresse = findViewById(R.id.et_postal);
        username = findViewById(R.id.et_pseudo);
        password1 = findViewById(R.id.et_pass1);
        password2 = findViewById(R.id.et_pass2);
    }

    protected void onClickInscription(final View v) {
        boolean ok = true;

        //CHECK REMPLISSAGE DES CHAMPS
        if (nom.getText().toString().equals(null) || prenom.getText().toString().equals(null) || mail.getText().toString().equals(null) ||
                username.getText().toString().equals(null) || password1.getText().toString().equals(null) || password2.getText().toString().equals(null)) {
            //un des champs est vide
            Toast.makeText(this, "Un des champs est incorrect", Toast.LENGTH_SHORT).show();
            ok = false;
        }
        if (nom.getText().toString().equals("") || prenom.getText().toString().equals("") || mail.getText().toString().equals("") ||
                username.getText().toString().equals("") || password1.getText().toString().equals("") || password2.getText().toString().equals("")) {
            //un des champs est vide
            Toast.makeText(this, "Un des champs est vide", Toast.LENGTH_SHORT).show();
            ok = false;
        }
        //FIN CHECK REMPLISSAGE DES CHAMPS


        if (ok) {
            Logger.getAnonymousLogger().log(Level.WARNING, "CHECKS OK");

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = Ressources.URL + "/users";

            // Request a string response from the provided URL.
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, writeJSON(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("SERVER RESPONSE : ", response.toString());
                    Toast.makeText(InscriptionActivity.this, "Vous êtes inscrit, connectez-vous", Toast.LENGTH_LONG).show();
                    startLoginPage(v);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.toString().equals("com.android.volley.ClientError")) {
                        Toast.makeText(InscriptionActivity.this, "L'utilisateur existe déjà", Toast.LENGTH_LONG).show();
                    } else
                        Logger.getAnonymousLogger().log(Level.SEVERE, "ERROR SERVER : " + error.toString());
                }
            });
            queue.add(request);
        }
    }

    private void startLoginPage(View v) {
        Intent myIntent = new Intent(this, LoginActivity.class);
        myIntent.putExtra("key", "value"); //Optional parameters
        this.startActivity(myIntent);
    }

    protected JSONObject writeJSON() {
        JSONObject inscrit = new JSONObject();
        try {
            inscrit.put("nom", nom.getText().toString());
            inscrit.put("prenom", prenom.getText().toString());
            inscrit.put("mail", mail.getText().toString());
            inscrit.put("username", username.getText().toString());
            inscrit.put("pwd", password1.getText().toString());
            inscrit.put("adresse", adresse.getText().toString());
        } catch (JSONException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Erreur JSON Exception");
        }
        return inscrit;
    }
}
