package fr.toulouse.miage.ibae;

import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

        /*mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (!s.toString().contains(".") || !s.toString().contains("@")) {
                    Toast.makeText(InscriptionActivity.this, "Adresse mail invalide", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }

    protected void onClickInscription(View v){
        boolean ok = true;
        Logger.getAnonymousLogger().log(Level.INFO, "OnClickInscription : " + nom.getText().toString());
        if(nom.getText().toString().equals(null) || prenom.getText().toString().equals(null) || mail.getText().toString().equals(null) ||
                username.getText().toString().equals(null) || password1.getText().toString().equals(null) || password2.getText().toString().equals(null))
        {
            //un des champs est vide
            Toast.makeText(this, "Un des champs est incorrect", Toast.LENGTH_SHORT).show();
            ok = false;
        }
        if(nom.getText().toString().equals("") || prenom.getText().toString().equals("") || mail.getText().toString().equals("") ||
                username.getText().toString().equals("") || password1.getText().toString().equals("") || password2.getText().toString().equals(""))
        {
            //un des champs est vide
            Toast.makeText(this, "Un des champs est vide", Toast.LENGTH_SHORT).show();
            ok = false;
        }

        if(ok){
            JSONObject utilisateur = writeJSON();
            SendUserCreation.execute((Runnable) utilisateur);
        }
    }
    protected JSONObject writeJSON(){
        JSONObject inscrit = new JSONObject();
        try {
            inscrit.put("nom", nom.getText().toString());
            inscrit.put("prenom", prenom.getText().toString());
            inscrit.put("mail", mail.getText().toString());
            inscrit.put("username", username.getText().toString());
            inscrit.put("pwd", password1.getText().toString());
            inscrit.put("adresse", adresse.getText().toString());
        }
        catch(JSONException e)
        {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Erreur JSON Exception");
        }
        return inscrit;
    }
}
