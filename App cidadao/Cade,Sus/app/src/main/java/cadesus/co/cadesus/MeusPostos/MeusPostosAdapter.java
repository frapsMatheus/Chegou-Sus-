package cadesus.co.cadesus.MeusPostos;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import cadesus.co.cadesus.AdicionarRemedios.AdicionarRemedioHolder;
import cadesus.co.cadesus.DB.Entidades.PostoDeSaude;
import cadesus.co.cadesus.R;

/**
 * Created by fraps on 7/14/16.
 */
public class MeusPostosAdapter extends RecyclerView.Adapter<MeusPostosViewHolder> {


    private ArrayList<PostoDeSaude> mPostosDeSaude;
    private HashMap<String, Boolean> mPostosSelected;


    MeusPostosAdapter(ArrayList<PostoDeSaude> postos, HashMap<String, Boolean> selected)
    {
        mPostosDeSaude = postos;
        mPostosSelected = selected;
    }


    @Override
    public MeusPostosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_adicionar_posto, parent, false);
        MeusPostosViewHolder holder = new MeusPostosViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MeusPostosViewHolder holder, int position) {
        View itemView = holder.itemView;
        final PostoDeSaude posto = mPostosDeSaude.get(position);
        holder.setView(posto, mPostosSelected.get(posto.uid));
        CompoundButton.OnCheckedChangeListener check = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mPostosSelected.put(posto.uid,b);
            }
        };
        holder.setOnClickCheckBox(check);
    }

    @Override
    public int getItemCount() {
        return mPostosDeSaude.size();
    }
}
