package fr.toulouse.miage.ibae;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    protected void onClickConnexion(View v){
        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.putExtra("key", "value"); //Optional parameters
        this.startActivity(myIntent);
    }
}