package in.co.iodev.keralarescue.Activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import in.co.iodev.keralarescue.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class RequestStatusActivity extends Activity {
ImageView statusimage1,statusimage2,statusimage3;
TextView location;
TextView statustext1,statustext2,statustext3,rescue_text,rescue_number;
String StringData,request_post_url="https://byw1s98hik.execute-api.ap-south-1.amazonaws.com/dev/androidapp/get-from-timeindex",
cancel_post_url="https://byw1s98hik.execute-api.ap-south-1.amazonaws.com/dev/androidapp/remove-request-from-timeindex",TimeIndex;
String receivedData;
Button cancel_request;
SharedPreferences sharedPref;
Boolean cancel=false;


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
        rescue_text=findViewById(R.id.rescue_text);
        rescue_number=findViewById(R.id.rescue_number);
        cancel_request=findViewById(R.id.cancel_request);
        sharedPref=this.getPreferences(Context.MODE_PRIVATE);
        cancel_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel=true;
                new HTTPAsyncTask().execute(cancel_post_url);
            }
        });
        JSONObject timeindex=new JSONObject();

        checkStatus(getIntent().getStringExtra("status"));
        location.setText(getIntent().getStringExtra("location"));
        TimeIndex=getIntent().getStringExtra("TimeIndex");
        try {
            timeindex.put("TimeIndex",TimeIndex);
            Log.v("TimeIndex",TimeIndex);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringData=timeindex.toString();
        Log.v("stringdata",StringData);
        IntentFilter filter = new IntentFilter("status_broadcast");
        this.registerReceiver(new Receiver(), filter);


    }
    @Override
    protected void onStart() {
        super.onStart();
        new HTTPAsyncTask().execute(request_post_url);


    }
    @Override
    protected void onResume() {
        super.onResume();
        new HTTPAsyncTask().execute(request_post_url);


    }
    private void checkStatus(String status)
    {
        if(status.equals("true"))
        {
            statusimage1.setColorFilter(Color.parseColor("#ffffff"));
            statustext1.setTextColor(Color.parseColor("#ffffff"));
            statusimage2.setColorFilter(Color.parseColor("#ffffff"));
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
                    statusimage1.setColorFilter(Color.parseColor("#ffffff"));
                    statustext1.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "received" :
                    statusimage2.setColorFilter(Color.parseColor("#ffffff"));
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

    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                try {
                    return HttpPost(urls[0]);
                } catch (Exception e) {
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
            if (!cancel) {
                JSONArray response = null;
                JSONObject responseObject = null;
                try {
                    response = new JSONArray(receivedData);
                    responseObject = response.getJSONObject(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (responseObject != null && responseObject.has("AcceptedBy")) {
                    statusimage3.setColorFilter(Color.parseColor("#ffffff"));
                    statustext3.setTextColor(Color.parseColor("#ffffff"));
                    rescue_text.setVisibility(View.VISIBLE);
                    rescue_number.setVisibility(View.VISIBLE);
                    try {
                        rescue_number.setText(responseObject.getString("AcceptedBy"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.IsRequestSent), "false");
                editor.apply();
                finish();

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

            Log.d("Response", conn.getResponseMessage().toString());
            int responseCode = conn.getResponseCode();
            Log.d("Response Code:", String.valueOf(responseCode));
            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                while ((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }
                in.close();
                receivedData = sb.toString();
                Log.d("Response", receivedData);
                return conn.getResponseMessage() + "";

            } else {
                return null;
            }
            // 5. return response message

        }
    }}
