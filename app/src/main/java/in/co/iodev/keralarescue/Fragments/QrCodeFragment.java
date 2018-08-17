package in.co.iodev.keralarescue.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import in.co.iodev.keralarescue.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class QrCodeFragment extends Fragment {

    View rootView;
    Bank bank;
    TextView vpa , acNo , acName , branch , ifsc;
    Button back;
    ImageView qrCode;

    public QrCodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_qr_code, container, false);
        bank = (Bank) getArguments().getSerializable("bank");
        vpa = rootView.findViewById(R.id.vpa);
        acNo = rootView.findViewById(R.id.qr_ac_no);
        acName = rootView.findViewById(R.id.qr_ac_name);
        branch = rootView.findViewById(R.id.qr_branch);
        ifsc = rootView.findViewById(R.id.qr_ifsc);
        back = rootView.findViewById(R.id.btn_go_back);
        qrCode = rootView.findViewById(R.id.img_qr_code);
        setBank(bank);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return rootView;
    }

    private void setBank(Bank bank) {
        if (!bank.getAcName().equals("Not Available")) {
            acNo.setText("Account Number: " + bank.getAcNumber());
            acName.setText("Account Name: " + bank.getAcName());
            branch.setText("Branch: " + bank.getBranch());
            ifsc.setText("IFSC: " + bank.getIfsc());
        }
        vpa.setText("UPI: " + bank.getUpi());
        Glide.with(getActivity()).load(bank.getQrcode()).into(qrCode);
    }

}
