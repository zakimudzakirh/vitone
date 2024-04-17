package com.psaainsankamil.vitone.adapters;

import android.app.Dialog;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.psaainsankamil.vitone.R;
import com.psaainsankamil.vitone.models.Anak;

import java.util.List;

/**
 * Created by zaki on 27/05/18.
 */

public class AnakAdapter extends RecyclerView.Adapter<AnakAdapter.ViewHolder> {

    private List<Anak> listAnak;
    private Context context;

    public AnakAdapter(List<Anak> listAnak, Context context){
        this.listAnak = listAnak;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_anak, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Anak anak = listAnak.get(position);
        holder.textNama.setText(anak.getNama());
        holder.textMukim.setText(anak.getInfomukim());
        holder.textYatim.setText(anak.getInfoDhuafa());
        holder.container.setOnClickListener(onClickListener(position));
    }

    private View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.recycler_anak);
                //dialog.setTitle("Position " + position);
                dialog.setCancelable(true); // dismiss when touching outside Dialog

                // set the custom dialog components - texts and image
                TextView nama = (TextView) dialog.findViewById(R.id.textNama);
                TextView mukim = (TextView) dialog.findViewById(R.id.textMukim);
                TextView yatim = (TextView) dialog.findViewById(R.id.textYatim);
                ImageView icon = (ImageView) dialog.findViewById(R.id.image);

                setDataToView(nama, mukim, yatim, icon, position, dialog);

                dialog.show();
            }
        };
    }

    private void setDataToView(TextView nama, TextView mukim, TextView yatim, ImageView genderIcon, int position, Dialog dialog) {
        dialog.setTitle(listAnak.get(position).getNama());
        nama.setText(listAnak.get(position).getNama());
        mukim.setText(listAnak.get(position).getInfomukim());
        yatim.setText(listAnak.get(position).getInfoDhuafa());

    }

    @Override
    public int getItemCount() {
        return (null != listAnak ? listAnak.size() : 0);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView textNama;
        private TextView textMukim;
        private TextView textYatim;
        private View container;

        public ViewHolder(View menuView){
            super(menuView);
            imageView = (ImageView) menuView.findViewById(R.id.image);
            textNama = (TextView) menuView.findViewById(R.id.textNama);
            textMukim = (TextView) menuView.findViewById(R.id.textMukim);
            textYatim = (TextView) menuView.findViewById(R.id.textYatim);
            container = menuView.findViewById(R.id.card_view);
        }
    }
}
