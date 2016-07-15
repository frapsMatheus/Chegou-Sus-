package cadesus.co.cadesus.PostoInfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Locale;

import cadesus.co.cadesus.DB.Entidades.PostoDeSaude;
import cadesus.co.cadesus.R;

/**
 * Created by fraps on 7/14/16.
 */
public class PostoInfoHolder extends RecyclerView.ViewHolder {

    private TextView mNome;
    private TextView mLocation;
    private TextView mPhone;
    private TextView mCep;
    private ImageView mMapa;

    public PostoInfoHolder(View itemView) {
        super(itemView);
        prepareView(itemView);
    }

    private void prepareView(View v)
    {
        mNome = (TextView)v.findViewById(R.id.posto_nome);
        mLocation = (TextView)v.findViewById(R.id.posto_endereco);
        mPhone = (TextView)v.findViewById(R.id.post_telefone);
        mCep = (TextView)v.findViewById(R.id.post_cep);
        mMapa = (ImageView)v.findViewById(R.id.posto_mapa);
    }

    public void setView(final Context context, PostoDeSaude posto)
    {
        mNome.setText(posto.nome);
        mLocation.setText(posto.endereco);
        if (posto.telefone != null) {
            mPhone.setVisibility(View.VISIBLE);
            mPhone.setText(posto.telefone);
        } else {
            mPhone.setVisibility(View.GONE);
        }
        if (posto.cep != null) {
            mCep.setVisibility(View.VISIBLE);
            mCep.setText(posto.cep);
        } else {
            mCep.setVisibility(View.GONE);
        }
        final String latitude = String.valueOf(posto.location.get(0));
        final String longitude = String.valueOf(posto.location.get(1));
        int height = dpToPx(context,180);
        String heigth = String.valueOf(height);
        String width = String.valueOf(16*height);
        String mapa = "https://maps.googleapis.com/maps/api/staticmap?center="+ latitude +
                "," + longitude + "&zoom=15&size="+ width +"x"+heigth+"&sensor=false&markers=color:blue%7Clabel:S%7C"+
                latitude +","+longitude;
        mMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format(Locale.ENGLISH, "geo:%s,%s", latitude, longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                context.startActivity(intent);
            }
        });
        Glide.with(context).load(mapa).into(mMapa);
    }

    public int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
