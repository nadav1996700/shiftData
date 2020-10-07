package src.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.src.R;

public class Fragment_Request extends Fragment implements CallBack_RequestsFragment {
    protected View view;
    private Fragment_select_requests fragment_select_requests;
    private Fragment_calender fragment_calender;

    public Fragment_Request() {}

    public static Fragment_Request newInstance() {
        Fragment_Request fragment = new Fragment_Request();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragment_calender = Fragment_calender.newInstance();
        fragment_calender.setCallBack_RequestsFragment(this);
        fragment_select_requests = Fragment_select_requests.newInstance();
        initFragments(fragment_calender, R.id.Requests_LAY_calender);
        initFragments(fragment_select_requests, R.id.Requests_LAY_request);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null)
            view = inflater.inflate(R.layout.fragment_requests, container, false);
        return view;
    }

    private void initFragments(Fragment fragment, int layout) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(layout, fragment);
        transaction.commit();
    }

    @Override
    public void setDate(String date) {
        fragment_select_requests.setDate(date);
    }
}