package cadesus.co.cadesus.PostosComRemedio;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import cadesus.co.cadesus.DB.Entidades.PostoDeSaude;
import cadesus.co.cadesus.R;

/**
 * Created by fraps on 7/12/16.
 */
public class PostosComRemedioHolder extends RecyclerView.ViewHolder {

    private TextView mName;
    private TextView mDistance;

    public PostosComRemedioHolder(View itemView) {
        super(itemView);
        prepareView(itemView);
    }

    private void prepareView(View v)
    {
        mName = (TextView)v.findViewById(R.id.cell_post_nome);
        mDistance = (TextView)v.findViewById(R.id.cell_posto_distancia);
    }

    public void setView(PostoDeSaude posto, double distance)
    {
        mName.setText(posto.nome);
        mDistance.setText(String.format("Disntância até o local de %.2f", distance/1000) + " Km.");
    }
}
