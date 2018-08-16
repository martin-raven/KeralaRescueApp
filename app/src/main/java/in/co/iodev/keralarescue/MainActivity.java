package in.co.iodev.keralarescue;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
    Button Malayalam;
    Button English;
    LinearLayout Malayal_layout;
    LinearLayout English_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Malayalam=findViewById(R.id.malayalam);
        English=findViewById(R.id.english);
        Malayal_layout=findViewById(R.id.malayalam_layout);
        English_layout=findViewById(R.id.english_layout);
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

}
