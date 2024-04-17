package com.psaainsankamil.vitone.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.psaainsankamil.vitone.R;
import com.psaainsankamil.vitone.models.Program;

import java.util.List;

/**
 * Created by zaki on 27/05/18.
 */

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ViewHolder> {

    private List<Program> listProgram;
    private Context context;

    public ProgramAdapter(List<Program> listProgram, Context context){
        this.listProgram = listProgram;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_program, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Program program = listProgram.get(position);

        holder.textJudul.setText(program.getJudul());
        holder.textIsi.setText(program.getIsi());
    }

    @Override
    public int getItemCount() {
        return listProgram.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textJudul;
        public TextView textIsi;

        public ViewHolder(View menuView){
            super(menuView);
            textJudul = (TextView) menuView.findViewById(R.id.textJudul);
            textIsi = (TextView) menuView.findViewById(R.id.textIsi);
        }
    }
}
