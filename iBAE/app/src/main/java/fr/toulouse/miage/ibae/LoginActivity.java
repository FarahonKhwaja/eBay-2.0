package fr.toulouse.miage.ibae;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class LoginActivity extends AppCompatActivity {

    private EditText nom, password, ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nom = findViewById(R.id.et_nom);
        password = findViewById(R.id.et_password);
        ip = findViewById(R.id.et_ip);

        //Récupération des paramètres éventuels
        Bundle bund = getIntent().getExtras();
        String usernameInscription = "";
        if (bund != null)
            usernameInscription = bund.getString("name");
        Logger.getAnonymousLogger().log(Level.SEVERE, "");
        nom.setText(usernameInscription);
    }

    /**
     * Méthode lancée au clic sur le bouton Connexion.
     * Vérifie les informations saisies, et connecte (ou non) l'utilisateur
     *
     * @param v
     */
    protected void onClickConnexion(final View v) {
        if (!ip.getText().toString().equals("")) {
            Ressources.URL = "http://" + ip.getText().toString() + ":8080";
        }
        if (checkParametres()) {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            Logger.getAnonymousLogger().log(Level.SEVERE, "URL Ressources : " + Ressources.URL + "/usercheck");

            // Request a string response from the provided URL.
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Ressources.URL.toString() + "/usercheck", writeJSON(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (checkCredentials(response))
                        ConnexionOK(v);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.getAnonymousLogger().log(Level.SEVERE, "ERROR SERVER : " + error.toString() + ", host : '" + Ressources.URL + "/usercheck" + "'");
                }
            });
            queue.add(request);
        }
    }

    /**
     * Méthode appelée lors de la réponse du serveur
     * @param response Réponse du serveur
     * @return true si connexion OK / sinon false
     */
    private boolean checkCredentials(JSONObject response) {
        JSONObject modeleUtilisateurIncorrect = new JSONObject();
        boolean ok = true;
        if (response.toString().equals(modeleUtilisateurIncorrect.toString())) {
            Toast.makeText(LoginActivity.this, "Identifiants incorrects", Toast.LENGTH_LONG).show();
            ok = false;
        } else {
            Toast.makeText(LoginActivity.this, "Vous êtes connecté", Toast.LENGTH_LONG).show();
        }
        return ok;
    }

    /**
     * Vérifie si les paramètres sont correctement remplis
     * @return true si OK / sinon false
     */
    private boolean checkParametres() {
        boolean ok = true;
        //CHECK REMPLISSAGE DES CHAMPS
        if (nom.getText().toString().equals(null) || password.getText().toString().equals(null)) {
            //un des champs est vide
            Toast.makeText(this, "Un des champs est incorrect", Toast.LENGTH_SHORT).show();
            ok = false;
        }
        if (nom.getText().toString().equals("") || password.getText().toString().equals("")) {
            //un des champs est vide
            Toast.makeText(this, "Un des champs est vide", Toast.LENGTH_SHORT).show();
            ok = false;
        }
        return ok;
    }

    /**
     * Méthode lancée au clic sur le bouton inscription
     * Lance l'activité d'inscription
     *
     * @param v Origine
     */
    protected void onClickInscription(View v) {
        Intent myIntent = new Intent(this, InscriptionActivity.class);
        //myIntent.putExtra("key", "value"); //Optional parameters
        this.startActivity(myIntent);
    }

    /**
     * Méthode lançant l'activité principale de iBAE, avec en paramètre le username de l'utilisateur
     *
     * @param v Origine
     */
    protected void ConnexionOK(View v) {
        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.putExtra("username", nom.getText().toString()); //Optional parameters
        this.startActivity(myIntent);
    }

    /**
     * Méthode créant un objet JSON contenant les informations de l'utilisateur à connecter
     * Fait appel à /usercheck sur le serveur afin de vérifier les informations
     *
     * @return JSONObject identifiant l'utilisateur à connecter
     */
    protected JSONObject writeJSON() {
        JSONObject utilisateur = new JSONObject();
        try {
            utilisateur.put("username", nom.getText().toString());
            utilisateur.put("pwd", password.getText().toString());
        } catch (JSONException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Erreur JSON Exception");
        }
        return utilisateur;
    }
}