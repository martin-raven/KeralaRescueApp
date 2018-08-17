package in.co.iodev.keralarescue.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import in.co.iodev.keralarescue.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChequeDDFragment extends Fragment {

    public static final String TAG = "cheque_dd_fragment";

    public ChequeDDFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cheque_dd, container, false);
        Button back = rootView.findViewById(R.id.btn_go_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return rootView;
    }

}
