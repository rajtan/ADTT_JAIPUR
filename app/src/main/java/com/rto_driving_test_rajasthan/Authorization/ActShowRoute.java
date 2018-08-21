package com.rto_driving_test_rajasthan.Authorization;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rto_driving_test_rajasthan.Adapters.CustomSpinnerAdapter;
import com.rto_driving_test_rajasthan.Models.MainCat;
import com.rto_driving_test_rajasthan.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utility.BaseActivity;
import utility.ErrorLayout;

public class ActShowRoute extends BaseActivity {

    Context context;
    @BindView(R.id.toolbar_default)
    Toolbar tootlbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_start)
    TextView tvStart;

    @BindView(R.id.tv_end)
    TextView tvEnd;

    @BindView(R.id.tv_retest)
    TextView tvRestart;

    @BindView(R.id.spi_dist)
    Spinner spDist;
    ArrayList<MainCat> arMainCat=new ArrayList<>();
    String sDist="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.act_show_route);
        context = this;
        ButterKnife.bind(this);
        setAppBar(getAppString(R.string.show_route), true);
        tootlbar.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp);
        tvTitle.setText(getAppString(R.string.show_route));
        tootlbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ActShowRoute.this,LoginActivity.class));
                finish();
            }
        });
        initViews();
        DummyData();
        spDist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                sDist=arMainCat.get(position).getCatName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {

            }
        });
    }

    private ErrorLayout errorLayout;

    private void initViews() {
        errorLayout = new ErrorLayout(findViewById(R.id.error_rl));
    }

    public void DummyData() {

        MainCat bean;
        arMainCat = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            bean = new MainCat();
            bean.setCatId(i + "");
            switch (i) {
                case 0:
                    bean.setCatName("Select Track");
                    break;
                case 1:
                    bean.setCatName(getAppString(R.string.dist_name));

                    break;
                case 2:
                    bean.setCatName(getAppString(R.string.dist_name1));

                    break;
                case 3:
                    bean.setCatName(getAppString(R.string.dist_name2));

                    break;
                case 4:
                    bean.setCatName(getAppString(R.string.dist_name3));
                    break;
                case 5:
                    bean.setCatName(getAppString(R.string.dist_name4));
                    break;
                case 6:
                    bean.setCatName(getAppString(R.string.dist_name5));
                    break;
                case 7:
                    bean.setCatName(getAppString(R.string.dist_name6));
                    break;

                default:
                    bean.setCatName(getAppString(R.string.dist_name));
                    break;
            }
            arMainCat.add(bean);
        }
        setValueOnCatAdapter();
    }

    public void setValueOnCatAdapter() {

        if (arMainCat.size() > 0) {
            CustomSpinnerAdapter spAdapterCourtName = new CustomSpinnerAdapter(
                    ActShowRoute.this, R.layout.convert_spinner_item,
                    arMainCat);
            spDist.setAdapter(spAdapterCourtName);
        } else {
            Toast.makeText(ActShowRoute.this, "No Data", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @OnClick({R.id.tv_end,R.id.tv_start,R.id.tv_retest})//R.id.login_btn
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_start:
                startActivity(new Intent(ActShowRoute.this,ResultActivity.class));
                break;

            case R.id.tv_end:
                break;

            case R.id.tv_retest:
                break;

        }
    }

}
