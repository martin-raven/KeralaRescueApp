package in.co.iodev.keralarescue.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import in.co.iodev.keralarescue.R;

public class DonateActivityFragment extends Fragment implements View.OnClickListener {

    Button bankTransfer , chequeDd , qrCode, back;
    View rootView;
    public static final String TAG = "donate_activity_fragment";

    public DonateActivityFragment() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bank_transfer:
                getActivity().getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment , new BankTransferFragment() , BankTransferFragment.TAG)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.btn_chk_ds:
                getActivity().getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment , new ChequeDDFragment() , ChequeDDFragment.TAG)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.btn_qr_code:
                getActivity().getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment , new BankListFragment(), BankListFragment.TAG)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.btn_go_back:
                getActivity().finish();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_donate, container, false);
        back = rootView.findViewById(R.id.btn_go_back);
        back.setOnClickListener(this);
        bankTransfer = rootView.findViewById(R.id.btn_bank_transfer);
        bankTransfer.setOnClickListener(this);
        chequeDd = rootView.findViewById(R.id.btn_chk_ds);
        chequeDd.setOnClickListener(this);
        qrCode = rootView.findViewById(R.id.btn_qr_code);
        qrCode.setOnClickListener(this);
        return rootView;
    }
}
