package cadesus.co.cadesus.Main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cadesus.co.cadesus.DB.Entidades.Remedio;
import cadesus.co.cadesus.R;

/**
 * Created by fraps on 7/14/16.
 */
public class MeusRemediosHolder extends RecyclerView.ViewHolder {

    TextView mNome;
    TextView mQuantidade;
    public ImageView mEdit;

    public MeusRemediosHolder(View itemView) {
        super(itemView);
        prepareViews(itemView);
    }

    private void prepareViews(View v)
    {
        mNome = (TextView) v.findViewById(R.id.cell_remedio_nome);
        mQuantidade = (TextView) v.findViewById(R.id.cell_remedio_quantidade);
        mEdit = (ImageView) v.findViewById(R.id.cell_remedio_edit);
    }

    public void setRemedio(Remedio remedio, Long quantidade)
    {
        mNome.setText(remedio.principio_ativo);
        mQuantidade.setText(String.valueOf(quantidade));
    }
}
