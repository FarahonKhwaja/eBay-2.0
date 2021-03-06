package fr.toulouse.miage.ibae;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.toulouse.miage.ibae.fragments.HomeFragment;
import fr.toulouse.miage.ibae.fragments.ProfileFragment;
import fr.toulouse.miage.ibae.fragments.SearchResultFragment;
import fr.toulouse.miage.ibae.fragments.VendreFragment;
import fr.toulouse.miage.ibae.metier.Annonce;

public class MainActivity extends FragmentActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int GALLERY_REQUEST = 2;

    String mCurrentPhotoPath;



    private TextView title;
    private TextView description;
    private TextView prixMin;
    private ImageView img;
    private int nbAnnonces;

    public Annonce content;

    private String username = "toto";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(R.id.contenu, HomeFragment.newInstance(username), "home");
                    return true;
                case R.id.navigation_sell:
                    replaceFragment(R.id.contenu, new VendreFragment(), "vendre");
                    return true;
                case R.id.navigation_profile:
                    replaceFragment(R.id.contenu, new ProfileFragment(), "profil");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = getIntent().getStringExtra("username");
        addFragment(R.id.contenu, HomeFragment.newInstance(username), "home");
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Log.i("USERNAME", "username : " + username);
    }

    /**
     * Ajout d'un fragment dans la MainActivity
     * @param containerViewId ID du conteneur du fragment
     * @param fragment Fragment à instancier
     * @param fragmentTag Tag pour identifier le fragment
     */
    protected void addFragment(int containerViewId, Fragment fragment, String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .commit();
    }

    /**
     * Remplacement d'un fargment dans la MainActivity
     * @param containerViewId ID du conteneur du fragment
     * @param fragment Fragment à instancier
     * @param fragmentTag Tag pour identifier le fragment
     */
    public void replaceFragment(int containerViewId, Fragment fragment, String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .commit();
    }

    /**
     * Action du bouton pour prendre une photo
     * @param view origine
     */
    public void clickAjoutPhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "fr.toulouse.miage.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /**
     * Creation d'un fichier image pour avoir la photo en bonne qualité
     * @return Le fichier photo
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Ajout de la photo à la galerie pour y avoir accès depuis d'autres applications
     */
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    /**
     * Ajout de la photo dans l'ImageView prévue à cet effet
     * @param image ImageView récepeteur de la photo
     */
    private void setPic(ImageView image) {
        // Get the dimensions of the View
        int targetW = image.getWidth();
        int targetH = image.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        image.setImageBitmap(bitmap);
    }

    /**
     * Action à mener lorqu'une activité taggée se termine
     * @param requestCode Code/Tag de l'activité se terminant
     * @param resultCode Code de retour de l'activité
     * @param data Données contenue dans l'Intent de retour
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            ImageView imgArticle = findViewById(R.id.sell_img);
            switch (requestCode){
                case GALLERY_REQUEST:
                    Uri selectedImage = data.getData();
                    try {
                        InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                        Bitmap image = BitmapFactory.decodeStream(imageStream);
                        imgArticle.setImageBitmap(image);
                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;
                case REQUEST_TAKE_PHOTO:
                    galleryAddPic();
                    setPic(imgArticle);
                    break;
            }

        }
    }

    /**
     * Click sur le bouton d'ajout d'une photo à partir de la gallerie
     * @param view Origine
     */
    public void clickGetPhoto(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    /**
     * Click sur le bouton de mise en ligne
     * @param view
     * @throws JSONException
     */
    public void clickVendre(View view) throws JSONException {
        ajouterAnnonce();
    }

    /**
     * Action d'ajout d'une annonce en base de données
     * @throws JSONException
     */
    private void ajouterAnnonce() throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Ressources.URL + "/annonces";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST ,url, buidJsonAnnonceObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                toastConfirmMiseEnLigne();
                title.setText("");
                description.setText("");
                img.setImageBitmap(null);
                prixMin.setText("");
                Log.i("SERVER RESPONSE", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                toastErreurMiseEnLigne();
                Log.e("SERVER RESPONSE", error.toString());
            }
        });
        queue.add(request);
    }

    /**
     * Affiche un toast de confirmation de mise en ligne
     */
    private void toastConfirmMiseEnLigne(){
        Toast.makeText(this, R.string.message_annonce_cree, Toast.LENGTH_SHORT).show();
    }

    /**
     * Affiche un toast de message d'erreur lors de la mise en ligne
     */
    private void toastErreurMiseEnLigne(){
        Toast.makeText(this, R.string.message_annonce_cree_e, Toast.LENGTH_LONG).show();
    }


    /**
     * Construction d'un Objet JSON d'une Annonce à mettre en ligne
     * @return L'objet JSON
     * @throws JSONException
     */
    private JSONObject buidJsonAnnonceObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        title = findViewById(R.id.sell_title);
        description = findViewById(R.id.sell_description);
        prixMin = findViewById(R.id.sell_price_init);
        img = findViewById(R.id.sell_img);
        img.buildDrawingCache();
        Bitmap bm = img.getDrawingCache();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image = stream.toByteArray();
        String strImg = Base64.encodeToString(image, 0);

        getNbAnnonces();
        jsonObject.accumulate("id", nbAnnonces+1);
        jsonObject.accumulate("nom",  title.getText().toString());
        jsonObject.accumulate("description",  description.getText().toString());
        jsonObject.accumulate("prix_min", prixMin.getText().toString());
        jsonObject.accumulate("photo", strImg);
        jsonObject.accumulate("etat", "En cours");
        Timestamp creation = new Timestamp(new Date().getTime());
        Long value = creation.getTime();
        jsonObject.accumulate("dateCreation", value);
        jsonObject.accumulate("duree", 5);
        jsonObject.accumulate("utilisateurCreation", username);

        return jsonObject;
    }

    /**
     * Retourne le nombre d'annonces disponibles pour créer un identidfiant différent
     * de celui en base de données
     */
    private void getNbAnnonces(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Ressources.URL + "/annonces";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("ServResponse", response.toString());
                Log.i("ServResponse", ""+response.length());
                nbAnnonces = response.length();
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
     * Click sur le bouton de mise à jour du serveur node (uniquement disponible en version de DEV)
     * @param view Origine
     */
    public void clickUpdateServer(View view) {

        EditText ip = findViewById(R.id.param_ip);

        Ressources.URL = "http://" + ip.getText().toString().trim() + ":8080";

        Toast.makeText(this, R.string.message_update_server, Toast.LENGTH_SHORT).show();
    }

    /**
     * Click sur le bouton de recherche des annonces
     * @param view Origine
     * @throws InterruptedException
     */
    public void clickRechercheAnnonce(View view) throws InterruptedException {
        EditText filter = findViewById(R.id.home_search);
        replaceFragment(R.id.contenu, SearchResultFragment.newInstance(filter.getText().toString()), "search");
    }

    /**
     * Requête sur l'API pour mettre à jour l'enchère
     * @param value nouvelle valeur de l'enchère
     */
    private void encherir(Double value){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Ressources.URL + "/annonce/" + content.getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("id", content.getId());
            jsonObject.accumulate("derniereEnchere", value);
            jsonObject.accumulate("utilisateurEnchere", username);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    toastEnchereSucces();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    toastEnchereErreur();
                }
            });
            queue.add(request);
        } catch (JSONException e) {
            Toast.makeText(this, R.string.message_enchere_erreur, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Met la vue à jour après une enchère
     * @param v
     */
    private void miseAjourVueAnnonce(View v){
        EditText value = findViewById(R.id.annonce_et_enchere);
        TextView prix = findViewById(R.id.annonce_price);
        TextView lastEnch = findViewById(R.id.annonce_lastenchere);
        prix.setText(value.getText().toString()+" €");
        value.setText("");
        lastEnch.setText(username);
    }

    /**
     * Affiche un toast si succès de l'enchère
     */
    private void toastEnchereSucces(){
        Toast.makeText(this, R.string.message_enchere, Toast.LENGTH_LONG).show();
    }

    /**
     * Affiche un toast en cas d'erreur
     */
    private void toastEnchereErreur(){
        Toast.makeText(this, R.string.message_enchere_erreur, Toast.LENGTH_LONG).show();
    }

    /**
     * Méthode appelée au click sur le bouton enchérir
     * @param view Origine
     */
    public void clickEncherir(View view) {
        EditText value = findViewById(R.id.annonce_et_enchere);
        TextView prix = findViewById(R.id.annonce_price);
        String[] split = prix.getText().toString().split(" ");
        String strPrix = split[0];
        Double prixAnnonce = Double.parseDouble(strPrix);
        Double prixPropose = Double.parseDouble(value.getText().toString());
        if (prixPropose > prixAnnonce){
            encherir(Double.parseDouble(value.getText().toString()));
            miseAjourVueAnnonce(view);
        } else{
            Toast.makeText(this, R.string.message_enchere_radine, Toast.LENGTH_LONG).show();
        }
    }
}
