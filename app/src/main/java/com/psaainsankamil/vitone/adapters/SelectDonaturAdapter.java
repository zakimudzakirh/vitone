package com.psaainsankamil.vitone.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.psaainsankamil.vitone.R;
import com.psaainsankamil.vitone.activities.SendSelectDonaturActivity;
import com.psaainsankamil.vitone.models.Donatur;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zaki on 28/05/18.
 */

public class SelectDonaturAdapter extends RecyclerView.Adapter<SelectDonaturAdapter.ViewHolder> {

    private List<Donatur> listDonatur;
    private Context context;
    private SendSelectDonaturActivity activity;


    public SelectDonaturAdapter(List<Donatur> listDonatur, Context context, SendSelectDonaturActivity activity){
        this.listDonatur = listDonatur;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_select_donatur_data, parent, false);
        return new ViewHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textNama.setText(listDonatur.get(position).getNama());
        holder.textTelp.setText(listDonatur.get(position).getKontak());
        holder.checkContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                listDonatur.get(position).setCheck(b);
            }
        });
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

        private ImageView image;
        private TextView textNama;
        private TextView textTelp;
        private CheckBox checkContact;

        public ViewHolder(View view, final SendSelectDonaturActivity activity){
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
            textNama = (TextView) view.findViewById(R.id.textNama);
            textTelp = (TextView) view.findViewById(R.id.textTelp);
            checkContact = (CheckBox) view.findViewById(R.id.checkContact);

        }
    }

}
