package in.co.iodev.keralarescue.Activities;

import android.app.FragmentManager;
import android.app.Activity;
import android.os.Bundle;

import in.co.iodev.keralarescue.Fragments.DonateActivityFragment;
import in.co.iodev.keralarescue.R;

public class DonateActivity extends Activity {

    public static final String BACK_STACK_ROOT_TAG = "root_fragment";
    public FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        fragmentManager = getFragmentManager();
        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG , FragmentManager.POP_BACK_STACK_INCLUSIVE);

        fragmentManager.beginTransaction()
                .replace(R.id.fragment , new DonateActivityFragment() , DonateActivityFragment.TAG)
                .addToBackStack(BACK_STACK_ROOT_TAG)
                .commit();
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        }
        else {
            getFragmentManager().popBackStack();
        }
    }
}
