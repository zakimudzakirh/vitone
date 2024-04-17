package com.psaainsankamil.vitone.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.psaainsankamil.vitone.R;
import com.psaainsankamil.vitone.models.Lokasi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zaki on 29/05/18.
 */

public class MesjidAdapter extends RecyclerView.Adapter<MesjidAdapter.ViewHolder> {

    private List<Lokasi> listMesjid;
    private Context context;

    public MesjidAdapter(List<Lokasi> listMesjid, Context context){
        this.listMesjid = listMesjid;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_mesjid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textNama.setText(listMesjid.get(position).getNama());
        holder.textAlamat.setText(listMesjid.get(position).getAlamat());
    }

    @Override
    public int getItemCount() {
        return (listMesjid != null? listMesjid.size(): 0);
    }

    public void setFilter(List<Lokasi> listMesjid){
        this.listMesjid = new ArrayList<>();
        this.listMesjid.addAll(listMesjid);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textNama;
        private TextView textAlamat;

        public ViewHolder(View view){
            super(view);
            textNama = (TextView) view.findViewById(R.id.textNama);
            textAlamat = (TextView) view.findViewById(R.id.textAlamat);
        }
    }
}
