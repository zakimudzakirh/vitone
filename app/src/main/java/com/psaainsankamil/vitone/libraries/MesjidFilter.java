package com.psaainsankamil.vitone.libraries;

import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.psaainsankamil.vitone.models.Lokasi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zaki on 30/05/18.
 */

public class MesjidFilter extends Filter {

    private List<Lokasi> listMesjid;
    private ArrayAdapter<Lokasi> searchAdapter;

    public MesjidFilter(List<Lokasi> listMesjid, ArrayAdapter<Lokasi> searchAdapter){
        this.listMesjid = listMesjid;
        this.searchAdapter = searchAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        //List<JSONObject> list = new ArrayList<JSONObject>(venues);
        FilterResults result = new FilterResults();
        String substr = "";
        if(constraint != null){
            substr = constraint.toString().toLowerCase();
        }

        // if no constraint is given, return the whole list
        if (substr == null || substr.length() == 0) {
            result.values = listMesjid;
            result.count = listMesjid.size();
        } else {
            // iterate over the list of venues and find if the venue matches the constraint. if it does, add to the result list
            final ArrayList<Lokasi> retList = new ArrayList<>();
            for (Lokasi lokasi : listMesjid) {
                if (lokasi.getNama().toLowerCase().contains(constraint.toString().toLowerCase())) {
                    retList.add(lokasi);
                }
            }
            result.values = retList;
            result.count = retList.size();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        // we clear the adapter and then pupulate it with the new results
        searchAdapter.clear();
        if (results.count > 0) {
            for (Lokasi o : (ArrayList<Lokasi>) results.values) {
                searchAdapter.add(o);
                //searchAdapter.add(o.getNama());
            }
        }
    }

}
