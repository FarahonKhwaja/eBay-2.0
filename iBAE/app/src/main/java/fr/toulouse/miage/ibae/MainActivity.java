package fr.toulouse.miage.ibae;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.toulouse.miage.ibae.Fragments.HomeFragment;
import fr.toulouse.miage.ibae.Fragments.VendreFragment;

public class MainActivity extends FragmentActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int GALLERY_REQUEST = 2;


    private TextView title;
    private TextView description;
    private TextView prixMin;
    private ImageView img;



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

    public void clickAjoutPhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

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
                case REQUEST_IMAGE_CAPTURE:
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imgArticle.setImageBitmap(imageBitmap);
                    break;
            }

        }
    }

    public void clickGetPhoto(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    public void clickVendre(View view) throws IOException, JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.58:8080/annonces";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST ,url, buidJsonObject(), new Response.Listener<JSONObject>() {
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
        /*AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpPost("http://localhost:8080/annonces");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });*/

    }

    private String HttpPost(String myUrl) throws IOException, JSONException {
        String result = "";

        URL url = new URL(myUrl);

        // 1. create HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        // 2. build JSON object
        JSONObject jsonObject = buidJsonObject();

        // 3. add JSON content to POST request body
        setPostRequestContent(conn, jsonObject);

        // 4. make POST request to the given URL
        conn.connect();

        // 5. return response message
        return conn.getResponseMessage()+"";

    }

    private void setPostRequestContent(HttpURLConnection conn, JSONObject jsonObject) throws IOException {
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        Log.i(MainActivity.class.toString(), jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();
    }

    private JSONObject buidJsonObject() throws JSONException {
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

        //jsonObject.accumulate("id", );
        jsonObject.accumulate("nom",  title.getText().toString());
        jsonObject.accumulate("decription",  description.getText().toString());
        jsonObject.accumulate("prix_min", prixMin.getText().toString());
        jsonObject.accumulate("img", strImg);

        return jsonObject;
}
}
