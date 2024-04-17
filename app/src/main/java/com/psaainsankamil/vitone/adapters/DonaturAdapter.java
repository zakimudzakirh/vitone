package com.psaainsankamil.vitone.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.psaainsankamil.vitone.R;
import com.psaainsankamil.vitone.activities.DonaturViewActivity;
import com.psaainsankamil.vitone.dialogs.DialogDeleteDonatur;
import com.psaainsankamil.vitone.models.Donatur;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zaki on 28/05/18.
 */

public class DonaturAdapter extends RecyclerView.Adapter<DonaturAdapter.ViewHolder> {

    private List<Donatur> listDonatur;
    private DonaturViewActivity activity;

    public DonaturAdapter(List<Donatur> listDonatur, DonaturViewActivity activity){
        this.listDonatur = listDonatur;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_donatur, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textNama.setText(listDonatur.get(position).getNama());
        holder.textTelp.setText(listDonatur.get(position).getKontak());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogDeleteDonatur(activity, position).show();
            }
        });
    }

    public void setFilter(List<Donatur> listDonatur){
        this.listDonatur = new ArrayList<>();
        this.listDonatur.addAll(listDonatur);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (listDonatur != null? listDonatur.size() : 0);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView textNama;
        private TextView textTelp;
        private Button btnDelete;

        public ViewHolder(View view){
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
            textNama = (TextView) view.findViewById(R.id.textNama);
            textTelp = (TextView) view.findViewById(R.id.textTelp);
            btnDelete = (Button) view.findViewById(R.id.btnDelete);
        }
    }
}
