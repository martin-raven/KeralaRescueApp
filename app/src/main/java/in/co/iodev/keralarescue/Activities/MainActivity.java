package in.co.iodev.keralarescue.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import in.co.iodev.keralarescue.Models.DataModel;
import in.co.iodev.keralarescue.R;


public class MainActivity extends Activity {
    private static final int LOCATION_PERMISSIONS_REQUEST = 10;
    Button Malayalam, English, Help_English, Help_malayalam;
    LinearLayout Malayalam_layout,status_malayalam;
    LinearLayout English_layout,status_english;
    EditText location_place_english, location_place_malayalam,num_of_people_english,num_of_people_malayalam;
    Boolean enlish_selected=true;
    DataModel datatobesent=new DataModel();
    Gson gson = new Gson();
    String StringData,post_url="https://byw1s98hik.execute-api.ap-south-1.amazonaws.com/dev/androidapp/post";
    public static final int LOCATION_UPDATE_INTERVAL = 10;  //mins


    String batteryPercentage;

    private View.OnClickListener edit_location_listener = new View.OnClickListener() {
        public void onClick(View view) {
            getLocation();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Malayalam = findViewById(R.id.malayalam);
        English = findViewById(R.id.english);
        Malayalam_layout = findViewById(R.id.malayalam_layout);
        English_layout = findViewById(R.id.english_layout);
        Help_English = findViewById(R.id.get_help_english);
        Help_malayalam = findViewById(R.id.get_help_malayalam);
        location_place_english = findViewById(R.id.location_text_english);
        location_place_malayalam = findViewById(R.id.location_text_malayalam);

        status_english = findViewById(R.id.help_status_english);
        status_malayalam = findViewById(R.id.help_status_malayalam);
        status_malayalam.setVisibility(View.GONE);
        status_english.setVisibility(View.GONE);

        num_of_people_english=findViewById(R.id.no_of_people_english);
        num_of_people_malayalam=findViewById(R.id.no_of_people_malayalam);

        findViewById(R.id.edit_location_english).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"test",Toast.LENGTH_SHORT).show();
                location_place_english.setEnabled(true);
            }
        });
        findViewById(R.id.edit_location_malayalam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location_place_malayalam.setEnabled(true);


            }
        });

        batteryPercentage = getBatteryPercentage();
        getLocation();

        Help_English.setOnClickListener(new View.OnClickListener() {
          @Override
            public void onClick(View view) {
                batteryPercentage = getBatteryPercentage();
                getLocation();
                Help_English.setVisibility(View.GONE);
                status_english.setVisibility(View.VISIBLE);
                datatobesent.setNumber_of_people(num_of_people_english.getText().toString());

                datatobesent.setTimeIndex(String.valueOf(System.currentTimeMillis()));
                StringData=gson.toJson(datatobesent);
                Log.d("Data in json ",StringData);
                new HTTPAsyncTask().execute(post_url);
            }
        });
        Help_malayalam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                batteryPercentage = getBatteryPercentage();
                getLocation();
                Help_malayalam.setVisibility(View.GONE);
                status_malayalam.setVisibility(View.VISIBLE);
                datatobesent.setNumber_of_people(num_of_people_malayalam.getText().toString());
                datatobesent.setTimeIndex(String.valueOf(System.currentTimeMillis()));
                StringData=gson.toJson(datatobesent);
                Log.d("Data in json ",StringData);
                new HTTPAsyncTask().execute(post_url);
            }
        });

        Malayalam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Malayalam.setBackgroundResource(R.drawable.cornered_edges_malayalam_selected);
                English.setBackgroundResource(R.drawable.cornered_edges_english_deselected);
                Malayalam.setTextColor(Color.parseColor("#ffffff"));
                English.setTextColor(Color.parseColor("#ff000000"));
                Malayalam_layout.setVisibility(View.VISIBLE);
                English_layout.setVisibility(View.INVISIBLE);
                enlish_selected=false;
            }
        });

        English.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Malayalam.setBackgroundResource(R.drawable.cornered_edges_malayalam_deselected);
                English.setBackgroundResource(R.drawable.cornered_edges_english_selected);
                Malayalam.setTextColor(Color.parseColor("#ff000000"));
                English.setTextColor(Color.parseColor("#ffffff"));
                Malayalam_layout.setVisibility(View.INVISIBLE);
                English_layout.setVisibility(View.VISIBLE);
                enlish_selected=true;
            }
        });

    }

    String getBatteryPercentage() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level / (float) scale;
        float p = batteryPct * 100;
        Log.d("Battery percentage", String.valueOf(p));
        datatobesent.setBattery_percentage(String.valueOf(Math.round(p)));
        return String.valueOf(Math.round(p));
    }

    void getLocation() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    new AlertDialog.Builder(this)
                            .setTitle("Give location permission")
                            .setMessage("Location permission is needed for this app")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                                }
                            })
                            .create()
                            .show();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }
        }
        getPosition();
    }

    public void getPosition() {
        LocationRequest mLocationRequest;
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    Log.d("location", location.getLatitude() + " " + location.getLongitude());

                    Geocoder gcd = new Geocoder(MainActivity.this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addresses.size() > 0) {
                        try {
                            datatobesent.setLattitude(String.valueOf(location.getLatitude()));
                            datatobesent.setLongitude(String.valueOf(location.getLongitude()));
                            datatobesent.setLocality(addresses.get(0).getLocality());
                            datatobesent.setDistrict(addresses.get(0).getSubAdminArea());
                            Log.d("District", addresses.get(0).getSubAdminArea());
                            if(addresses.get(0).getLocality()!=null)
                                Log.d("Locality", addresses.get(0).getLocality());
                            location_place_english.setText(addresses.get(0).getLocality());
                            location_place_malayalam.setText(addresses.get(0).getLocality());
                            String District = addresses.get(0).getSubAdminArea();
                            status_english.setVisibility(View.VISIBLE);
                            status_malayalam.setVisibility(View.VISIBLE);
                        }catch (Exception e)
                        {e.printStackTrace();}
                    }

                }


            }
        };
        mLocationRequest = new LocationRequest();
        int inteval_ms = LOCATION_UPDATE_INTERVAL * 60 * 1000;
        mLocationRequest.setInterval(inteval_ms);
        mLocationRequest.setFastestInterval(inteval_ms);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSIONS_REQUEST);
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        getLocation();
                    }
                }else{
                    Toast.makeText(this, "Please provide the permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                try {
                    return HttpPost(urls[0]);
                } catch(Exception e) {
                    e.printStackTrace();
                    return "Error!";
                }
            } catch (Exception e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.d("data is being sent",result);
        }
    }
    private String HttpPost(String myUrl) throws IOException {
        String result = "";

        URL url = new URL(myUrl);

        // 1. create HttpURLConnection Error!
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(StringData);
        writer.flush();
        writer.close();
        os.close();

        // 4. make POST request to the given URL
        conn.connect();

        // 5. return response message
        return conn.getResponseMessage()+"";

    }
}