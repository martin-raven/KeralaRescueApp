package in.co.iodev.keralarescue.Fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import in.co.iodev.keralarescue.Fragments.Adapter.BankAdapter;
import in.co.iodev.keralarescue.Fragments.Adapter.RecyclerViewClickListener;
import in.co.iodev.keralarescue.Models.Bank;
import in.co.iodev.keralarescue.R;

public class BankListFragment extends Fragment {
    private List<Bank> bankList;
    private BankAdapter adapter;
    private Button back;
    View rootView;

    public static final String TAG = "qr_code_fragment";

    public BankListFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_bank_list, container, false);
        back = rootView.findViewById(R.id.btn_go_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                Bank bank = bankList.get(position);
                bundle.putSerializable("bank" , bank);

                QrCodeFragment fragment = new QrCodeFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment , fragment)
                        .addToBackStack(null)
                        .commit();
            }
        };
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        bankList = new ArrayList<>();
        adapter = new BankAdapter(getActivity() , bankList , listener);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity() , 2);
        recyclerView.setLayoutManager(layoutManager);
        //dpToPx() returns the value of 8dp in px
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2 , true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        prepareBanks(); //Adding banks to the RecyclerView
        return rootView;
    }

    private void prepareBanks() {
        Bank sib = new Bank(
                "South Indian Bank",
                R.drawable.gateway_southindianbank,
                R.drawable.upi_southindianbank,
                "Corporate Branch, Thiruvananthapuram",
                "Chief Minister’s Distress Relief Fund",
                "0721053000002584",
                "SIBL0000721",
                "keralacmdrf@sib");
        Bank fbl = new Bank(
                "Federal Bank" ,
                R.drawable.gateway_federalbank ,
                R.drawable.upi_federalbank ,
                "Thiruvananthapuram/Palayam" ,
                "Chief Minister’s Distress Relief Fund" ,
                "10210100412397" ,
                "FDRL0001021" ,
                "cmdrf@fbl");
        Bank sbi = new Bank(
                "State Bank of India" ,
                R.drawable.gateway_sbi ,
                R.drawable.upi_sbi ,
                "City Branch, Thiruvananthapuram" ,
                "Chief Minister’s Distress Relief Fund" ,
                "67319948232" ,
                "SBIN0070028" ,
                "keralacmdrf@sbi");
        Bank paytm = new Bank(
                "PayTM",
                R.drawable.gateway_paytm,
                R.drawable.upi_paytm,
                "Not Available",
                "Not Available",
                "Not Available",
                "Not Available",
                "PAYTM app only");
        bankList.add(sib);
        bankList.add(fbl);
        bankList.add(sbi);
        bankList.add(paytm);

        adapter.notifyDataSetChanged();
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        GridSpacingItemDecoration(int spanCount, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }
}
