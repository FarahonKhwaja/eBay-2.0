package fr.toulouse.miage.ibae;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import fr.toulouse.miage.ibae.Fragments.HomeFragment;
import fr.toulouse.miage.ibae.Fragments.VendreFragment;

public class MainActivity extends FragmentActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(R.id.contenu, new HomeFragment(), "home");
                    return true;
                case R.id.navigation_sell:
                    replaceFragment(R.id.contenu, new VendreFragment(), "vendre");
                    return true;
                case R.id.navigation_profile:
                    //TODO : Fragment profil
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addFragment(R.id.contenu, new HomeFragment(), "home");
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    protected void addFragment(int containerViewId, Fragment fragment, String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .commit();
    }

    protected void replaceFragment(int containerViewId, Fragment fragment, String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .commit();
    }

}
