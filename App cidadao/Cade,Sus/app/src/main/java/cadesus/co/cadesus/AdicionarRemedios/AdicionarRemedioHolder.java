package cadesus.co.cadesus.AdicionarRemedios;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cadesus.co.cadesus.DB.Entidades.Remedio;
import cadesus.co.cadesus.R;

/**
 * Created by fraps on 7/13/16.
 */
public class AdicionarRemedioHolder extends RecyclerView.ViewHolder {

    TextView mNome;
    TextView mPatologia;

    public AdicionarRemedioHolder(View itemView) {
        super(itemView);
        prepareView(itemView);
    }

    private void prepareView(View v)
    {
        mNome = (TextView)v.findViewById(R.id.adicionar_remedio_nome);
        mPatologia = (TextView)v.findViewById(R.id.adicionar_remedio_patologia);
    }

    public void setFields(Remedio remedio)
    {
        mNome.setText(remedio.principioAtivo);
        mPatologia.setText(remedio.patologia);
    }

}
