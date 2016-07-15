package cadesus.co.cadesus.PostosComRemedio;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import cadesus.co.cadesus.DB.Entidades.PostoDeSaude;
import cadesus.co.cadesus.PostoInfo.PostoInfoActivity;
import cadesus.co.cadesus.R;

/**
 * Created by fraps on 7/14/16.
 */
public class PostosComRemedioRecyclerAdapter extends RecyclerView.Adapter<PostosComRemedioHolder> {


    ArrayList<PostoDeSaude> mPostos;
    LinkedHashMap<String,Double> mDistancias;
    Activity mActivity;

    PostosComRemedioRecyclerAdapter(ArrayList<PostoDeSaude> postos,
                                    LinkedHashMap<String,Double> distancias, Activity context)
    {
        mPostos = postos;
        mDistancias = distancias;
        mActivity = context;
    }

    @Override
    public PostosComRemedioHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_posto_de_saude, parent, false);
        PostosComRemedioHolder holder = new PostosComRemedioHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PostosComRemedioHolder holder, int position) {
        final PostoDeSaude posto = mPostos.get(position);
        holder.setView(posto,mDistancias.get(posto.uid));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, PostoInfoActivity.class);
                intent.putExtra("postoID",posto.uid);
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPostos.size();
    }
}
