package com.digitfellas.ac.dashboard.serviceman;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.digitfellas.ac.R;
import com.digitfellas.ac.adapter.viewpager.StatusChangeAdapter;
import com.digitfellas.ac.base.BaseActivity;
import com.digitfellas.ac.base.BaseAdapter;
import com.digitfellas.ac.base.VH;
import com.digitfellas.ac.customview.CustomMessageView;
import com.digitfellas.ac.dashboard.MyServices.MyServicesStatusFragment;
import com.digitfellas.ac.dashboard.customerservices.MyServiceDetailsActivity;
import com.digitfellas.ac.dashboard.postcomments.PostFragment;
import com.digitfellas.ac.data.listener.DataListener;
import com.digitfellas.ac.data.model.response.CommentsCount;
import com.digitfellas.ac.data.model.response.CustomerServicesDetails;
import com.digitfellas.ac.data.model.response.ServiceManServicesDetails;
import com.digitfellas.ac.utility.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ServiceManServiceDetailsActivity extends BaseActivity {


    private static final String TAG = "ServiceManServiceDetail";

    @BindView(R.id.listview)
    CustomMessageView customMessageView;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.spinner_state)
    Spinner mStatesSpinner;

    private StatusChangeAdapter statusChangeAdapter;

    private MultipleLayoutServicesManDetailsAdapter myServiceAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_man_service_details);

        setButterKnife();
        setBackButtonEnabledAndTitle("Service details");


        statusChangeAdapter = new StatusChangeAdapter(this, new ArrayList<ServiceManServicesDetails.Data.ServiceStatus>());


        mStatesSpinner.setAdapter(statusChangeAdapter);

        myServiceAdapter = new MultipleLayoutServicesManDetailsAdapter(new ArrayList<Object>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        customMessageView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayout.VERTICAL);

        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));

        customMessageView.addItemDecoration(dividerItemDecoration);
//        mMyOrder_ListView.addItemDecoration(new EqualSpacingItemDecoration(16,EqualSpacingItemDecoration.VERTICAL));
        customMessageView.setAdapter(myServiceAdapter);

        customMessageView.addOnScrollListener(Utils.fabShowHide(mFab));
        customMessageView.setMessageTitle("No data");
        customMessageView.setMessageDescription("There is no data");

        customMessageView.showContent();

        loadData();
    }


    @OnClick(R.id.fab)
    public void onClick(View v) {

        final int id = getIntent().getIntExtra("id", 0);

        Log.e(TAG, "onClick: id " + id);

        PostFragment postFragment = PostFragment.getInstance(id, "service");

        postFragment.show(getSupportFragmentManager(), "Post_Comments");
        postFragment.setListener(new PostFragment.CommentsCallBack() {
            @Override
            public void onSuccess() {
                loadData();
            }

            @Override
            public void onFail() {


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.update_cancel_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case R.id.update:

                updateStatus();

                break;

            case R.id.cancel:


                onBackPressed();


                break;


        }


        return super.onOptionsItemSelected(item);
    }


    private void updateStatus() {


//        int position = mStatesSpinner.getSelectedItemPosition();

//        showToast(getStatus() + "");

        if (statusChangeAdapter.getCount() > 0 && !(getStatus() == 3)) {

            ServiceManServicesDetails.Data.ServiceStatus serviceStatus = statusChangeAdapter.getItem(mStatesSpinner.getSelectedItemPosition());


            Map<String, Object> param = new HashMap<>();

            param.put("status", serviceStatus.getId());
            param.put("_method", "PUT");

            int id = getIntent().getIntExtra("id", 0);

            dataSource.changeServiceManStatus(dataSource.getAuthendicate(), id, new JSONObject(param).toString(), new DataListener() {
                @Override
                public void onSuccess(Object object) {

                    ServiceManServiceDetailsActivity.super.onSuccess(object);

                    String s = object.toString();

                    Log.e(TAG, "onSuccess: " + s);

                    Intent intent = new Intent();

                    setResult(MyServicesStatusFragment.KEY_REFRESH_RESULT_CODE, intent);

                    onBackPressed();


                }

                @Override
                public void onFail(Throwable throwable) {
                    ServiceManServiceDetailsActivity.super.onFail(throwable);
                }

                @Override
                public void onNetworkFailure() {
                    ServiceManServiceDetailsActivity.super.onNetworkFailure();
                }
            });


        } else {

            showToast("You already closed the services");
        }


    }


    private int getStatus() {

        return getIntent().getIntExtra("status", 0);
    }

    private void loadData() {
        int id = getIntent().getIntExtra("id", 0);
        int status = getStatus();

        if (status == 3) {

            mStatesSpinner.setEnabled(false);
        }

        showLoading();

        dataSource.getServiceDetailsForServiceMan(dataSource.getAuthendicate(), id, this);

    }


    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);

        ServiceManServicesDetails serviceManServicesDetails = (ServiceManServicesDetails) object;


        if (serviceManServicesDetails.getSuccess()) {

            ServiceManServicesDetails.Data.Service data = serviceManServicesDetails.getData().getService();

            List list = new ArrayList<>();

            Data data1 = new Data();
            data1.title = "Mobile No";
            data1.des = data.getMobileNo();
            list.add(data1);

            data1 = new Data();
            data1.title = "Customer Email";
            data1.des = data.getCustomerEmail();
            list.add(data1);

            data1 = new Data();
            data1.title = "Address 1";
            data1.des = data.getAddress1();
            list.add(data1);

            data1 = new Data();
            data1.title = "Address 2";
            data1.des = data.getAddress2();
            list.add(data1);

            data1 = new Data();
            data1.title = "City/Town";
            data1.des = data.getCity();
            list.add(data1);


            data1 = new Data();
            data1.title = "State";
            data1.des = data.getStateName();
            list.add(data1);

            data1 = new Data();
            data1.title = "Address 1";
            data1.des = data.getCustomerEmail();
            list.add(data1);

            data1 = new Data();
            data1.title = "Zipcode";
            data1.des = data.getZipcode();
            list.add(data1);

            data1 = new Data();
            data1.title = "Brand Name";
            data1.des = data.getBrandName();
            list.add(data1);

            data1 = new Data();
            data1.title = "Model Name";
            data1.des = data.getModelName();
            list.add(data1);

            data1 = new Data();
            data1.title = "Ton/Capacity";
            data1.des = data.getTonCapacity();
            list.add(data1);

            data1 = new Data();
            data1.title = "Problem Type";
            data1.des = data.getProblemType();
            list.add(data1);

            data1 = new Data();
            data1.title = "Where is it placed at home?";
            data1.des = data.getPlaceAtHome();
            list.add(data1);

            data1 = new Data();
            data1.title = "Preferred Time of Service";
            data1.des = data.getPreferTime();
            list.add(data1);

            data1 = new Data();
            data1.title = "Preferred Date of Service";
            data1.des = data.getPreferDate();
            list.add(data1);

//            data1 = new Data();
//            data1.title = "Status";
//            data1.des = data.getStatus();
//            list.add(data1);


            List<ServiceManServicesDetails.Data.ServiceComment> serviceComment = serviceManServicesDetails.getData().getServiceComments();


            CommentsCount commentsCount = new CommentsCount();

            commentsCount.setCount(serviceComment.size());

            list.add(commentsCount);


            list.addAll(serviceComment);


            List<ServiceManServicesDetails.Data.ServiceStatus> statusList = serviceManServicesDetails.getData().getServiceStatus();


            statusChangeAdapter.updateBrand(statusList);

            setSpinnerToValue(mStatesSpinner, statusList, data.getStatus());

            myServiceAdapter.updateList(list);


        } else {

            customMessageView.setMsg("Oops", serviceManServicesDetails.getMessage(), CustomMessageView.StateView.ERROR);
        }


    }


    @Override
    public void onFail(Throwable throwable) {
        super.onFail(throwable);

        customMessageView.setMsg("Oops", "Something went wrong", CustomMessageView.StateView.ERROR);
    }

    @Override
    public void onNetworkFailure() {
        super.onNetworkFailure();
        customMessageView.setMsg("Oops", "There is no internet", CustomMessageView.StateView.ERROR);
    }


    public void setSpinnerToValue(Spinner spinner, List<ServiceManServicesDetails.Data.ServiceStatus> list, String value) {
        int index = 0;

        for (int i = 0; i < list.size(); i++) {
            ServiceManServicesDetails.Data.ServiceStatus serviceStatus = list.get(i);
            if (serviceStatus.getSstatus().toLowerCase().equals(value.toLowerCase())) {
                index = i;
                break; // terminate loop
            }
        }
        spinner.setSelection(index);
    }

    class Data {
        public String title, des;

    }

//    public class MyServiceDetailsAdapter extends BaseAdapter<Data, RecyclerView.ViewHolder> {
//
//        protected MyServiceDetailsAdapter(List<Data> list) {
//            super(list);
//        }
//
//        @NonNull
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_myservice_details, parent, false);
//
//            return new MyServiceDetailsAdapter.ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//            Data data = getValue(position);
//
//            ((ViewHolder) holder).title.setText(data.title);
//            ((ViewHolder) holder).description.setText(data.des);
//        }
//
//        @Override
//        public int getItemCount() {
//            return getCount();
//        }
//
//        public class ViewHolder extends VH {
//            @BindView(R.id.title)
//            TextView title;
//            @BindView(R.id.description)
//            TextView description;
//
//
//            public ViewHolder(View itemView) {
//                super(itemView);
//            }
//        }
//    }
}
