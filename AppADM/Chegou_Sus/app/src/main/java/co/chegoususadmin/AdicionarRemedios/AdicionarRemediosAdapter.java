package co.chegoususadmin.AdicionarRemedios;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import co.chegoususadmin.DB.Entidades.Remedio;
import co.chegoususadmin.R;

/**
 * Created by fraps on 7/13/16.
 */
public class AdicionarRemediosAdapter extends RecyclerView.Adapter<AdicionarRemedioHolder> {

    private final Activity mActivity;
    private ArrayList<Remedio> mRemedios;
    private AdicionarRemedioCallback mCallback;

    AdicionarRemediosAdapter(ArrayList<Remedio> remedios, Activity activity,
                             AdicionarRemedioCallback callback)
    {
        mRemedios = remedios;
        mActivity = activity;
        mCallback = callback;
    }

    private void criarDialog(final Remedio remedio)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        View view = mActivity.getLayoutInflater().inflate(R.layout.dialog_quantas_caixas,null);
        final EditText quantidadeRemedio = (EditText)view.findViewById(R.id.dialog_caixas);
        builder.setView(view);
        builder.setTitle("Adicionar remédio");
        builder.setMessage("Adicione a quantidade de caixas de "
                + remedio.principio_ativo + " que você necessita.");
        builder.setCancelable(true);
        builder.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int quantidade = Integer.parseInt(quantidadeRemedio.getText().toString());
                mCallback.remedioAdicionado(remedio,quantidade);
            }
        });
        builder.show();
    }

    @Override
    public AdicionarRemedioHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_adicionar_remedio, parent, false);
        AdicionarRemedioHolder holder = new AdicionarRemedioHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AdicionarRemedioHolder holder, final int position) {
        final View itemView = holder.itemView;
        holder.setFields(mRemedios.get(position));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarDialog(mRemedios.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRemedios.size();
    }
}
