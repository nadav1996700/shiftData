package src.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.src.R;

public class Fragment_Requestss extends Fragment implements CallBack_RequestsActivity {
    protected View view;
    private Fragment_requests fragment_requests;
    private Fragment_calender fragment_calender;

    public Fragment_Requestss() {}

    public static Fragment_Requestss newInstance() {
        Fragment_Requestss fragment = new Fragment_Requestss();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragment_calender = Fragment_calender.newInstance();
        fragment_calender.setCallBack_RequestsActivity(this);
        fragment_requests = Fragment_requests.newInstance();
        initFragments(fragment_calender, R.id.Requests_LAY_calender);
        initFragments(fragment_requests, R.id.Requests_LAY_request);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null)
            view = inflater.inflate(R.layout.fragment_requestss, container, false);
        return view;
    }

    private void initFragments(Fragment fragment, int layout) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(layout, fragment);
        transaction.commit();
    }

    @Override
    public void setDate(String date) {
        fragment_requests.setDate(date);
    }
}