package src.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.src.R;

public class Fragment_Request extends Fragment implements CallBack_setDate {
    protected View view;
    private Fragment_select_requests fragment_select_requests;

    public Fragment_Request() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* set fragments on frames layouts */
        Fragment_calender fragment_calender = new Fragment_calender();
        fragment_calender.setCallBack_setDate(this);
        fragment_select_requests = new Fragment_select_requests();
        initFragments(fragment_calender, R.id.Requests_LAY_calender);
        initFragments(fragment_select_requests, R.id.Requests_LAY_request);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_requests, container, false);
        return view;
    }

    /* set fragment on frame layout */
    private void initFragments(Fragment fragment, int layout) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(layout, fragment);
        transaction.commit();
    }

    @Override
    public void setDate(String date) {
        fragment_select_requests.setDate(date);
    }
}