package com.psaainsankamil.vitone.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.psaainsankamil.vitone.R;
import com.psaainsankamil.vitone.database.DatabaseVitone;
import com.psaainsankamil.vitone.dialogs.DialogAddMesjid;
import com.psaainsankamil.vitone.layouts.ClearableAutoCompleteTextView;
import com.psaainsankamil.vitone.libraries.Configuration;
import com.psaainsankamil.vitone.libraries.DirectionsJSONParser;
import com.psaainsankamil.vitone.libraries.MesjidFilter;
import com.psaainsankamil.vitone.libraries.RequestHandler;
import com.psaainsankamil.vitone.models.Lokasi;
import com.psaainsankamil.vitone.session.Member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zaki on 29/05/18.
 */

public class MesjidMapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener{

    private GoogleMap googleMap;

    private ClearableAutoCompleteTextView search_box;

    private Button btnAdd;
    private Button btnMode;
    private Button btnSearch;
    private Button btnGps;
    private Button btnTraffic;

    private RelativeLayout layoutInfo;
    private TextView textJarak;
    private TextView textWaktu;
    //Button btnMyLocation = (Button) view.findViewById(R.id.btnMyLocation);

    private DatabaseVitone dbVitone;
    private List<Lokasi> listMesjid;
    private Lokasi panti;
    private Lokasi destination;
    private Lokasi source;

    private ArrayAdapter<Lokasi> searchAdapter;

    private Polyline polyline;

    private boolean myLocation = false;
    private boolean traffic = true;
    private String mode = "driving";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesjid_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Peta Mesjid");
        initViews();
        initObjects();
        initListeners();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAdd:
                add();
                break;
            case R.id.btnMode:
                mode();
                break;
            case R.id.btnGps:
                gps();
                break;
            case R.id.btnSearch:
                search();
                break;
            case R.id.btnTraffic:
                setTraffic();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, MesjidMenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

        initMap();

    }

    private void initViews(){
        search_box = (ClearableAutoCompleteTextView) findViewById(R.id.search_box);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnMode = (Button) findViewById(R.id.btnMode);
        btnGps = (Button) findViewById(R.id.btnGps);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnTraffic = (Button) findViewById(R.id.btnTraffic);

        layoutInfo = (RelativeLayout) findViewById(R.id.layoutInfo);
        textJarak = (TextView) findViewById(R.id.textJarak);
        textWaktu = (TextView) findViewById(R.id.textWaktu);
        layoutInfo.setVisibility(View.INVISIBLE);
    }

    private void initObjects(){
        //member = new Member(this);
        dbVitone = new DatabaseVitone(this);
        listMesjid = dbVitone.getListMesjid();
        panti = dbVitone.getPanti();

        searchAdapter = new ArrayAdapter<Lokasi>(this, android.R.id.text1){

            private Filter filter;

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.dropdown_search_box, parent, false);
                }

                TextView venueName = (TextView) convertView
                        .findViewById(R.id.search_item_venue_name);
                TextView venueAddress = (TextView) convertView
                        .findViewById(R.id.search_item_venue_address);

                final Lokasi venue = this.getItem(position);
                convertView.setTag(venue);

                venueName.setText(venue.getNama());
                venueAddress.setText(venue.getAlamat());


                return convertView;
            }

            @Override
            public Filter getFilter() {
                if(filter == null){
                    filter = new MesjidFilter(listMesjid, searchAdapter);
                }
                return filter;
            }
        };

        search_box.setAdapter(searchAdapter);
    }

    private void initListeners(){
        btnAdd.setOnClickListener(this);
        btnMode.setOnClickListener(this);
        btnGps.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnTraffic.setOnClickListener(this);

        search_box.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // do something when the user clicks
                search_box.setText(searchAdapter.getItem(position).getNama());
                destination = searchAdapter.getItem(position);
            }
        });

        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                destination = null;
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void initMap(){
        if(panti.getId() != null) {
            googleMap.addMarker(panti.getMarker());
            googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                            new LatLng(Double.parseDouble(panti.getLat()), Double.parseDouble(panti.getLon())
                            ), 18)
            );
        }
        for(int i=0; i<listMesjid.size(); i++){
            googleMap.addMarker(listMesjid.get(i).getMarker());
        }
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                Context context = getApplicationContext(); //or getActivity(), YourActivity.this, etc.

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
    }

    private void search(){
        if(destination == null){
            AlertDialog.Builder alert = new AlertDialog.Builder(MesjidMapActivity.this);
            alert.setMessage("Anda belum memilih Mesjid tujuan")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setTitle("Mencari Rute")
                    .create();
            alert.show();
            return;
        }
        System.out.println("mode adalah "+mode);
        class Telusuri extends AsyncTask<Void, Void, Object[]>{

            ProgressDialog pd = new ProgressDialog(MesjidMapActivity.this);

            @Override
            protected Object[] doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Configuration.URL+"map_api", params);

                Object[] result = new Object[3];
                result[0] = false;
                result[1] = false;
                result[2] = "";
                try{
                    JSONObject obj = new JSONObject(res);
                    //ad = new JSONArray(new JSONObject())
                    if(obj.getBoolean("status")){
                        if(myLocation) {
                            res = rh.sendGetRequest(Configuration.URL_MAP + "&key=" + obj.getString("map_api") +
                                    "&origin=" + source.getLat() + "," + source.getLon()
                                    + "&destination=" + destination.getLat() + "," + destination.getLon() + "&mode="+mode);
                            result[1] = true;
                        }else{
                            res = rh.sendGetRequest(Configuration.URL_MAP + "&key=" + obj.getString("map_api") +
                                    "&origin=" + panti.getLat() + "," + panti.getLon() + "&destination=" + destination.getLat()
                                    + "," + destination.getLon() + "&mode="+mode);
                        }
                        result[0] = true;
                        result[2] = res;
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPreExecute() {
                pd = ProgressDialog.show(MesjidMapActivity.this, "Mencari Rute...", "tunggu sebentar...");
            }

            @Override
            protected void onPostExecute(Object[] result) {
                pd.dismiss();
                System.out.println(result[2]);
                AlertDialog.Builder alert = new AlertDialog.Builder(MesjidMapActivity.this);
                if((boolean) result[0]){
                    DirectionsJSONParser parser = new DirectionsJSONParser();
                    List<List<HashMap<String, String>>> routes = new ArrayList<>();
                    try {
                        routes = parser.parse(new JSONObject((String) result[2]));
                        JSONObject obj = new JSONObject((String) result[2]);

                        JSONObject info = (JSONObject) obj.getJSONArray("routes").get(0);
                        info = (JSONObject) info.getJSONArray("legs").get(0);
                        textJarak.setText(info.getJSONObject("distance").getString("text"));
                        textWaktu.setText(info.getJSONObject("duration").getString("text"));
                        layoutInfo.setVisibility(View.VISIBLE);
                        if(obj.getString("status").equals("NOT_FOUND") || obj.getString("status").equals("ZERO_RESULTS")) {
                            alert.setMessage("Pencarian rute tidak ditemukan!")
                                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .setTitle("Pencarian Rute")
                                    .create();
                            alert.show();
                        }
                    }catch(JSONException er){
                        er.printStackTrace();
                    }
                    if(polyline != null){
                        polyline.remove();
                    }
                    ArrayList<LatLng> points = null;
                    PolylineOptions lineOptions = new PolylineOptions();
                    //if(myLocation) {
                    //    lineOptions.add(new LatLng(Double.parseDouble(source.getLat()), Double.parseDouble(source.getLon())));
                    //}else{
                        lineOptions.add(new LatLng(Double.parseDouble(panti.getLat()), Double.parseDouble(panti.getLon())));
                    //}


                    // Traversing through all the routes
                    for(int i=0;i<routes.size();i++){
                        points = new ArrayList<LatLng>();

                        // Fetching i-th route
                        List<HashMap<String, String>> path = routes.get(i);

                        // Fetching all the points in i-th route
                        for(int j=0;j<path.size();j++){
                            HashMap<String,String> point = path.get(j);

                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);

                            points.add(position);
                        }

                        // Adding all the points in the route to LineOptions
                        lineOptions.addAll(points);
                        lineOptions.add(new LatLng(Double.parseDouble(destination.getLat()),Double.parseDouble(destination.getLon())));
                        lineOptions.width(10);
                        lineOptions.color(Color.BLACK);
                        if(mode.equals("walking"))
                            lineOptions.pattern(Arrays.asList(new Gap(20), new Dash(20)));
                        googleMap.setTrafficEnabled(false);
                    }

                    // Drawing polyline in the Google Map for the i-th route
                    polyline = googleMap.addPolyline(lineOptions);
                    if(myLocation){
                        googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(Double.parseDouble(source.getLat()), Double.parseDouble(source.getLon())
                                        ), 18)
                        );
                    }else{
                        googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(Double.parseDouble(panti.getLat()), Double.parseDouble(panti.getLon())
                                        ), 18)
                        );
                    }
                }else{
                    layoutInfo.setVisibility(View.GONE);
                    alert.setMessage("Koneksi sedang bermasalah")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("Pencarian Rute")
                            .create();
                    alert.show();
                }
            }
        }
        Telusuri t = new Telusuri();
        t.execute();
    }

    private void add(){
        //initMap();
        if(myLocation){
            new DialogAddMesjid(this, googleMap.getMyLocation()).show();
        }
    }

    private void mode(){
        if(mode.equals("driving")){
            btnMode.setBackgroundResource(R.drawable.icon_ic_walk);
            mode = "walking";
        }else{
            btnMode.setBackgroundResource(R.drawable.icon_ic_car);
            mode = "driving";
        }
    }

    private void gps() {
        if (ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);
        criteria.setSpeedRequired(true);
        criteria.setCostAllowed(true);

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            // Getting latitude of the current location
            // Getting Current Location From GPS
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            source = new Lokasi();
            source.setNama("You are here");
            source.setLat(String.valueOf(location.getLatitude()));
            source.setLon(String.valueOf(location.getLongitude()));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            myLocation = true;
        }else{
            //System.out.println("kosong");

        }

    }

    private void setTraffic(){
        if(traffic){
            googleMap.setTrafficEnabled(traffic);
            btnTraffic.setText("Hidden");
            traffic = false;
        }else{
            googleMap.setTrafficEnabled(traffic);
            btnTraffic.setText("Macet");
            traffic = true;
        }
    }

    public void done(Lokasi lokasi){
        listMesjid.add(lokasi);
        googleMap.addMarker(lokasi.getMarker());

    }
}
