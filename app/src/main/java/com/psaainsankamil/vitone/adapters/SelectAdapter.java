package com.psaainsankamil.vitone.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.psaainsankamil.vitone.R;
import com.psaainsankamil.vitone.activities.SendSelectActivity;
import com.psaainsankamil.vitone.models.Donatur;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zaki on 08/06/18.
 */

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ViewHolder> {

    private List<Donatur> listDonatur;
    private Context context;
    private SendSelectActivity activity;


    public SelectAdapter(List<Donatur> listDonatur, Context context, SendSelectActivity activity){
        this.listDonatur = listDonatur;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public SelectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_select_donatur, parent, false);
        return new SelectAdapter.ViewHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(SelectAdapter.ViewHolder holder, int position) {
        holder.index = position;
        holder.textNama.setText(listDonatur.get(position).getNama());
        holder.textTelp.setText(listDonatur.get(position).getKontak());

    }

    @Override
    public int getItemCount() {
        return (listDonatur != null? listDonatur.size(): 0);
    }

    public void setFilter(List<Donatur> listDonatur){
        this.listDonatur = new ArrayList<>();
        this.listDonatur.addAll(listDonatur);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private int index;
        private ImageView image;
        private TextView textNama;
        private TextView textTelp;

        public ViewHolder(View view, final SendSelectActivity activity){
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
            textNama = (TextView) view.findViewById(R.id.textNama);
            textTelp = (TextView) view.findViewById(R.id.textTelp);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    Donatur donatur = new Donatur();
                    donatur.setNama(textNama.getText().toString().trim());
                    donatur.setKontak(textTelp.getText().toString().trim());
                    activity.selected(donatur);
                }
            });
        }
    }

}
