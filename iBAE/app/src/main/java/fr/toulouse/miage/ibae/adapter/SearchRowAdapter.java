package fr.toulouse.miage.ibae.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fr.toulouse.miage.ibae.R;
import fr.toulouse.miage.ibae.metier.Annonce;

public class SearchRowAdapter extends BaseAdapter {

    private Context context;
    private List<Annonce> annonces;

    private static LayoutInflater inflater = null;

    public SearchRowAdapter(Context context, List<Annonce> lesAnnonces) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.annonces = lesAnnonces;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null){
            vi = inflater.inflate(R.layout.row_search_result, null);
        }
        TextView titre = vi.findViewById(R.id.row_title);
        TextView desc = vi.findViewById(R.id.row_description);
        TextView prix = vi.findViewById(R.id.row_price);
        ImageView img = vi.findViewById(R.id.row_pic);

        Annonce current = annonces.get(position);

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
        return vi;
    }
}
