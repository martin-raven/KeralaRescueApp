package in.co.iodev.keralarescue.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import in.co.iodev.keralarescue.Models.DataModel;
import in.co.iodev.keralarescue.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class Request extends Activity {
private static final int LOCATION_PERMISSIONS_REQUEST = 10;
Button requestHelp;
Intent intent;
ImageView edit_location;
EditText people,contact,alternate_contact,help_location;
String location;
    DataModel datatobesent=new DataModel();
    Gson gson = new Gson();
    SharedPreferences sharedPref;
    String StringData,post_url="https://byw1s98hik.execute-api.ap-south-1.amazonaws.com/dev/androidapp/post";
    public static final int LOCATION_UPDATE_INTERVAL = 10;  //mins
    String batteryPercentage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_request);
        intent=new Intent();
        intent.setAction("status_broadcast");
        people=findViewById(R.id.numbe_of_people);
        contact=findViewById(R.id.contact_number);
        alternate_contact=findViewById(R.id.alternate_contact_number);
        help_location=findViewById(R.id.location_text);
        edit_location=findViewById(R.id.location_edit);
        checkGPS();//to check  location is enabled or not
        getLocation();
        sharedPref = getDefaultSharedPreferences(getApplicationContext());
        try {
            String RequestStatus = sharedPref.getString(getResources().getString(R.string.IsRequestSent), null);
            String RequestLocation = sharedPref.getString("location", null);
            String TimeIndex = sharedPref.getString("TimeIndex", null);
            Log.d("Request Status", RequestStatus);
            if(RequestStatus.equals("true")){
                Intent i=new Intent(Request.this,RequestStatusActivity.class);
                i.putExtra("status","true");
                i.putExtra("location",RequestLocation);
                i.putExtra("TimeIndex",TimeIndex);
                startActivity(i);


            }
        }catch (Exception e){
            e.printStackTrace();
        }
       edit_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                help_location.setEnabled(true);
                help_location.requestFocus();
                showKeyboard((LinearLayout) findViewById(R.id.location_layout));
            }
        });
        batteryPercentage = getBatteryPercentage();
        getLocation();

        requestHelp=findViewById(R.id.request_help);
        requestHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidPhone(contact.getText().toString())&&isValidPhone(alternate_contact.getText().toString()))
                {
                        batteryPercentage = getBatteryPercentage();
                        checkGPS();

                        datatobesent.setNumber_of_people(people.getText().toString());
                        datatobesent.setContactNumber(contact.getText().toString());
                        datatobesent.setAlternateContactNumber(alternate_contact.getText().toString());
                        datatobesent.setTimeIndex(String.valueOf(System.currentTimeMillis()));
                        datatobesent.setLocality(help_location.getText().toString());
                        StringData=gson.toJson(datatobesent);
                        Log.d("Data in json ",StringData);
                        new HTTPAsyncTask().execute(post_url);
                        Intent i=new Intent(Request.this,RequestStatusActivity.class);
                        i.putExtra("status","false");
                        i.putExtra("location",datatobesent.getLocality());
                         i.putExtra("TimeIndex",datatobesent.getTimeIndex());
                        startActivity(i);}
                        else
                {
                    Toast.makeText(getApplicationContext(), "phone number in valid",Toast.LENGTH_SHORT).show();
                }

            }


        });
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        intent=new Intent();
        intent.setAction("status_broadcast");
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Request.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Give location permission")
                        .setMessage("Location permission is needed for this app")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(Request.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(Request.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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

                    Geocoder gcd = new Geocoder(Request.this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (addresses.size() > 0) {
                            datatobesent.setLattitude(String.valueOf(location.getLatitude()));
                            datatobesent.setLongitude(String.valueOf(location.getLongitude()));
                            datatobesent.setLocality(addresses.get(0).getLocality());
                            datatobesent.setDistrict(addresses.get(0).getSubAdminArea());
                            Log.d("District", addresses.get(0).getSubAdminArea());
                            if(addresses.get(0).getLocality()!=null) {
                                Log.d("Locality", addresses.get(0).getLocality());
                                help_location.setText(addresses.get(0).getLocality());
                            }
                            else {
                                help_location.setText(addresses.get(0).getSubAdminArea());
                                help_location.setText(addresses.get(0).getSubAdminArea());
                            }

                        }
                    }catch (Exception e)
                    {e.printStackTrace();}


                }


            }
        };
        mLocationRequest = new LocationRequest();
        int inteval_ms = LOCATION_UPDATE_INTERVAL * 60 * 1000;
        mLocationRequest.setInterval(inteval_ms);
        mLocationRequest.setFastestInterval(inteval_ms);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Request.this,
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

    /**
     * Method to make network call to push the data using OkHTTP
     */
//    private void makeNetworkCall()
//    {
//        OkHttpClient client=new OkHttpClient();
//        Request request=new Request.Builder().url(post_url).build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, final IOException e)
//            {
//
//                Log.e(getClass().getSimpleName(), "Exception parsing JSON", e);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                Log.e("TAG","SUCCESS");
//            }
//        });
//    }
    private void showKeyboard(LinearLayout layout){
        InputMethodManager inputMethodManager =
                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                layout.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
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
            intent.putExtra("status","sent");
            sendBroadcast(intent);

            Log.d("data is being sent",result);
            if (result.equals("OK")){
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.IsRequestSent), "true");
                editor.putString("location", help_location.getText().toString());
                editor.putString("TimeIndex",datatobesent.getTimeIndex());
                editor.apply();
                intent.putExtra("status","received");
                sendBroadcast(intent);

            }
            else {
                intent.putExtra("status","failed");
                sendBroadcast(intent);
                Toast.makeText(Request.this, "Error In sending Data", Toast.LENGTH_SHORT).show();
            }
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
     try {

         Log.d("exceptionintry","e");

        intent.putExtra("status","sent");
        sendBroadcast(intent);}
        catch (Exception e)
        {
            Log.d("exception in catch","e");
            e.printStackTrace();
        }
        conn.connect();
        Log.d("in first","HEY");
        Log.d("Responsefrom1st",conn.getResponseMessage().toString());

        // 5. return response message
        return conn.getResponseMessage()+"";

    }
    public  void checkGPS(){
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(Request.this);
            dialog.setMessage(getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });

            dialog.show();
        }
    }
    private boolean isValidPhone(String phone) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            if(phone.length()!=10) {

                check = false;
            } else {
                check = true;
            }
        } else {
            check=false;
        }
        return check;
    }
}

