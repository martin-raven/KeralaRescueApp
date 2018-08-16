package in.co.iodev.keralarescue;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


public class MainActivity extends Activity {
    Button Malayalam, English, Help_English, Help_malayalam;
    LinearLayout Malayal_layout;
    LinearLayout English_layout;


    String batteryPercentage;

    private View.OnClickListener Inform_btn_listerner = new View.OnClickListener() {
        public void onClick(View view) {
            batteryPercentage = getBatteryPercentage();
        }
    };
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
           Log.d("Location",location.toString());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Malayalam = findViewById(R.id.malayalam);
        English = findViewById(R.id.english);
        Malayal_layout = findViewById(R.id.malayalam_layout);
        English_layout = findViewById(R.id.english_layout);
        Help_English = findViewById(R.id.get_help_english);
        Help_malayalam = findViewById(R.id.get_help_malayalam);

        Help_English.setOnClickListener(Inform_btn_listerner);
        Help_malayalam.setOnClickListener(Inform_btn_listerner);

        Malayalam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Malayalam.setBackgroundResource(R.drawable.cornered_edges_malayalam_selected);
                English.setBackgroundResource(R.drawable.cornered_edges_english_deselected);
                Malayalam.setTextColor(Color.parseColor("#ffffff"));
                English.setTextColor(Color.parseColor("#ff000000"));
                Malayal_layout.setVisibility(View.VISIBLE);
                English_layout.setVisibility(View.INVISIBLE);
            }
        });

        English.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Malayalam.setBackgroundResource(R.drawable.cornered_edges_malayalam_deselected);
                English.setBackgroundResource(R.drawable.cornered_edges_english_selected);
                Malayalam.setTextColor(Color.parseColor("#ff000000"));
                English.setTextColor(Color.parseColor("#ffffff"));
                Malayal_layout.setVisibility(View.INVISIBLE);
                English_layout.setVisibility(View.VISIBLE);
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
        return String.valueOf(Math.round(p));
    }

    String getLocation() {
        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return "TODO";
            }
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10,
                100, mLocationListener);
        return "WIP";
    }
}