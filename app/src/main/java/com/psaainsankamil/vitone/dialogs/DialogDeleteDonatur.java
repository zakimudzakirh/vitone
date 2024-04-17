package com.psaainsankamil.vitone.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.psaainsankamil.vitone.R;
import com.psaainsankamil.vitone.activities.DonaturViewActivity;
import com.psaainsankamil.vitone.database.DatabaseVitone;

/**
 * Created by zaki on 28/05/18.
 */

public class DialogDeleteDonatur extends Dialog implements View.OnClickListener{
    private Button btn_yes;
    private Button btn_no;
    private TextView textAsk;

    private DonaturViewActivity activity;

    private DatabaseVitone dbVitone;

    private int position;

    public DialogDeleteDonatur(DonaturViewActivity activity, int position){
        super(activity);
        this.activity = activity;
        this.position = position;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delete_donatur);
        getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        getWindow().setTitle("Delete Donatur");
        getWindow().setTitleColor(Color.WHITE);
        initViews();
        initObjects();
        initListeners();
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.btn_yes:
                deleteDonatur();
                break;
            case R.id.btn_no:
                dismiss();
                break;
        }
    }

    private void initViews(){
        this.btn_yes = (Button) findViewById(R.id.btn_yes);
        this.btn_no = (Button) findViewById(R.id.btn_no);
        this.textAsk = (TextView) findViewById(R.id.textAsk);

        this.textAsk.setText("Apa anda yakin ingin menghapus Donatur "
                + activity.getListDonatur().get(position).getNama() + "?");

    }

    private void initObjects(){
        this.dbVitone = new DatabaseVitone(activity);
    }

    private void initListeners(){
        this.btn_yes.setOnClickListener(this);
        this.btn_no.setOnClickListener(this);
    }

    private void deleteDonatur(){
        dbVitone.deleteDonatur(activity.getListDonatur().get(position).getId());

        activity.done();
        dismiss();
    }
}
