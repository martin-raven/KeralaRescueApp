package in.co.iodev.keralarescue.Activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import in.co.iodev.keralarescue.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class RequestStatusActivity extends Activity {
ImageView statusimage1,statusimage2,statusimage3;
TextView location;
TextView statustext1,statustext2,statustext3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_request_status);
        location=findViewById(R.id.location_non_editable);
        statusimage1=findViewById(R.id.status1_image);
        statusimage2=findViewById(R.id.status2_image);
        statusimage3=findViewById(R.id.status3_image);
        statustext1=findViewById(R.id.status1_text);
        statustext2=findViewById(R.id.status2_text);
        statustext3=findViewById(R.id.status3_text);
        checkStatus(getIntent().getStringExtra("status"));
        location.setText(getIntent().getStringExtra("location"));
        IntentFilter filter = new IntentFilter("status_broadcast");
        this.registerReceiver(new Receiver(), filter);

    }
    private void checkStatus(String status)
    {
        if(status.equals("true"))
        {
            statusimage1.setColorFilter(null);
            statustext1.setTextColor(Color.parseColor("#ffffff"));
            statusimage2.setColorFilter(null);
            statustext2.setTextColor(Color.parseColor("#ffffff"));
        }
    }
    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            String status = arg1.getExtras().getString("status");
            switch (status)
            {
                case "sent":
                    statusimage1.setColorFilter(null);
                    statustext1.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "received" :
                    statusimage2.setColorFilter(null);
                    statustext2.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "failed" :
                    Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                    finish();
                    break;

            }
        }
}@Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
}
