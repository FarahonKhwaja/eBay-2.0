package fr.toulouse.miage.ibae.custom;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Requête custom sur l'API REST : on passe un JSONObject en paramètre et on obtient un JSONArray en réponse
 * @param <T>
 */
public class CustomJsonRequest<T> extends JsonRequest<JSONArray> {

    private JSONObject mRequestObject;
    private Response.Listener<JSONArray> mResponseListener;

    /**
     * Requête custom sur l'API REST : on passe un JSONObject en paramètre et on obtient un JSONArray en réponse
     * @param method Method de requête sur l'API
     * @param url URL de l'API
     * @param requestObject JSONObject passé dans la requête
     * @param responseListener Listener de réponse sur le JSONArray renvoyé par le serveur
     * @param errorListener Listener d'erreur
     */
    public CustomJsonRequest(int method, String url, JSONObject requestObject, Response.Listener<JSONArray> responseListener,  Response.ErrorListener errorListener) {
        super(method, url, (requestObject == null) ? null : requestObject.toString(), responseListener, errorListener);
        mRequestObject = requestObject;
        mResponseListener = responseListener;
    }

    @Override
    protected void deliverResponse(JSONArray response) {
        mResponseListener.onResponse(response);
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            try {
                return Response.success(new JSONArray(json),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (JSONException e) {
                return Response.error(new ParseError(e));
            }
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}
