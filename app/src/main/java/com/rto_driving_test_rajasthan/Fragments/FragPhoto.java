package com.rto_driving_test_rajasthan.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rto_driving_test_rajasthan.R;



public class FragPhoto extends Fragment {
    View view;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frag_samachar,container,false);
        context=FragPhoto.this.getActivity();
//        InitCompo();

        return view;
    }
//    public void InitCompo()
//    {
//        tvNoData = (TextView)view.findViewById(R.id.tv_no_data);
//        studentDetailsRv = (RecyclerView)view.findViewById(R.id.rc_view_samachar);
//    }


}
