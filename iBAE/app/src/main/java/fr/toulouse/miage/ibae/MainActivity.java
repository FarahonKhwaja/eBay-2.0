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

import java.text.SimpleDateFormat;
import java.util.Date;

import fr.toulouse.miage.ibae.fragments.HomeFragment;
import fr.toulouse.miage.ibae.fragments.ProfileFragment;
import fr.toulouse.miage.ibae.fragments.VendreFragment;

public class MainActivity extends FragmentActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int GALLERY_REQUEST = 2;

    String mCurrentPhotoPath;

    private String urlServer;


    private TextView title;
    private TextView description;
    private TextView prixMin;
    private ImageView img;
    private int nbAnnonces;



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
        addFragment(R.id.contenu, new HomeFragment(), "home");
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        urlServer = "http://172.20.10.2:8080";

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

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

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

    public void clickGetPhoto(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    public void clickVendre(View view) throws JSONException {
        ajouterAnnonce();
    }

    private void ajouterAnnonce() throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = urlServer + "/annonces";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST ,url, buidJsonAnnonceObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("SERVER RESPONSE", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SERVER RESPONSE", error.toString());
            }
        });
        queue.add(request);
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
    }


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

        return jsonObject;
    }

    private void getNbAnnonces(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = urlServer + "/annonces";
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

    public void clickUpdateServer(View view) {

        EditText ip = findViewById(R.id.param_ip);

        Ressources.URL = "http://" + ip.getText().toString().trim() + ":8080";

        Toast.makeText(this, R.string.message_update_server, Toast.LENGTH_SHORT).show();
    }
}
