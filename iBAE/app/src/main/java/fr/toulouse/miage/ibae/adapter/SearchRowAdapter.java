package fr.toulouse.miage.ibae.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.toulouse.miage.ibae.R;
import fr.toulouse.miage.ibae.metier.Annonce;

public class SearchRowAdapter extends ArrayAdapter<Annonce> {

    ArrayList<Annonce> annonces = new ArrayList<>();

    public SearchRowAdapter(Context context, int ressourceID ,ArrayList<Annonce> annonces) {
        super(context,ressourceID, annonces);
        this.annonces = annonces;
    }


    @Override
    public int getCount() {
        return super.getCount();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.row_search_result, null);

        Annonce current = annonces.get(position);



        TextView titre = v.findViewById(R.id.row_title);
        TextView desc = v.findViewById(R.id.row_description);
        TextView prix = v.findViewById(R.id.row_price);
        ImageView img = v.findViewById(R.id.row_pic);



        titre.setText(current.getNom());
        desc.setText(current.getDesciption());
        if (current.getDerniereEnchere() == 0) {
            prix.setText(current.getPrixMin() + " €");
        } else{
            prix.setText(current.getDerniereEnchere() + " €");
        }
        if (current.getPhoto() != null){
            byte[] data = Base64.decode(current.getPhoto(), Base64.DEFAULT);
            img.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
        }
        return v;
    }
}
