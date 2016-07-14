package cadesus.co.cadesus.Main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import cadesus.co.cadesus.DB.Entidades.Remedio;
import cadesus.co.cadesus.R;

/**
 * Created by fraps on 7/14/16.
 */
public class MeusRemediosAdapter extends RecyclerView.Adapter<MeusRemediosHolder> {

    private Map<String,Long> mQuantidades;
    private ArrayList<Remedio> mRemedios = new ArrayList<>();

    MeusRemediosAdapter(ArrayList<Remedio> remedios, Map<String,Long> quantidades)
    {
        mRemedios = remedios;
        mQuantidades = quantidades;
    }


    @Override
    public MeusRemediosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_remedio, parent, false);
        MeusRemediosHolder holder = new MeusRemediosHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MeusRemediosHolder holder, int position) {
        View view = holder.itemView;
        holder.setRemedio(mRemedios.get(position),mQuantidades.get(mRemedios.get(position).uid));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mRemedios.size();
    }
}
