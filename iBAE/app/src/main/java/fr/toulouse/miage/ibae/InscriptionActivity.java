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

    private EditText nom;
    private EditText prenom;
    private EditText mail;
    private EditText adresse;
    private EditText username;
    private EditText password1;
    private EditText password2;
    private String usernameInscription;

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

    /**
     * Méthode lancée au clic sur le bouton inscription
     *
     * @param v
     */
    protected void onClickInscription(final View v) {
        boolean ok = checkParametres();
        if (ok) {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);

            // Request a string response from the provided URL.
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Ressources.URL + "/users", writeJSON(), new Response.Listener<JSONObject>() {
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

    private boolean checkParametres() {
        boolean ok = true;
        //CHECK REMPLISSAGE DES CHAMPS
        /*if (nom.getText().toString().equals(null) || prenom.getText().toString().equals(null) || mail.getText().toString().equals(null) ||
                username.getText().toString().equals(null) || password1.getText().toString().equals(null) || password2.getText().toString().equals(null)) {
            //un des champs est vide
            Toast.makeText(this, "Un des champs est incorrect", Toast.LENGTH_SHORT).show();
            ok = false;
        }*/
        //Tests nom, prenom, adresse mail
        if (!checkInfos()) {
            Toast.makeText(this, "Saisissez votre nom, prénom et adresse email", Toast.LENGTH_SHORT).show();
            ok = false;
        }
        //Tests username, passwords
        if (checkUserInfos()) {
            //un des champs est vide
            Toast.makeText(this, "Saisissez votre username, et votre mot de passe (x2)", Toast.LENGTH_SHORT).show();
            ok = false;
        }
        //Test passwords identiques
        if (!password1.getText().toString().equals(password2.getText().toString())) {
            Toast.makeText(this, "Les mots de passe sont différents", Toast.LENGTH_SHORT).show();
            ok = false;
        }
        //FIN CHECK REMPLISSAGE DES CHAMPS
        Logger.getAnonymousLogger().log(Level.SEVERE, "TESTS : " + ok);
        return ok;
    }

    private boolean checkUserInfos() {
        if (username.getText().toString().equals("") || password1.getText().toString().equals("") || password2.getText().toString().equals("")) {
            return false;
        }
        return true;
    }

    private boolean checkInfos() {
        if (nom.getText().toString().equals("") || prenom.getText().toString().equals("") || mail.getText().toString().equals("")) {
            return false;
        }
        return true;
    }

    /**
     * Méthode lançant l'activité de login, avec en paramètre le username de l'utilisateur
     *
     * @param v
     */
    private void startLoginPage(View v) {
        Intent myIntent = new Intent(this, LoginActivity.class);
        try {
            myIntent.putExtra("name", writeJSON().get("username").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.startActivity(myIntent);
    }

    /**
     * Méthode créant un objet JSON contenant les informations de l'utilisateur à inscrire
     *
     * @return JSONObject identifiant l'utilisateur à inscrire
     */
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