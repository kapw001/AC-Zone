package com.digitfellas.ac.dashboard.customerservices;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digitfellas.ac.R;
import com.digitfellas.ac.base.BaseActivity;
import com.digitfellas.ac.base.BaseAdapter;
import com.digitfellas.ac.base.VH;
import com.digitfellas.ac.customview.CustomMessageView;
import com.digitfellas.ac.dashboard.postcomments.PostFragment;
import com.digitfellas.ac.data.model.response.CommentsCount;
import com.digitfellas.ac.data.model.response.CustomerServices;
import com.digitfellas.ac.data.model.response.CustomerServicesDetails;
import com.digitfellas.ac.utility.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyServiceDetailsActivity extends BaseActivity {

    private static final String TAG = "MyServiceDetailsActivit";
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.listview)
    CustomMessageView customMessageView;
    private MultipleLayoutMyServicesDetailsAdapter myServiceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service_details);

        setButterKnife();
        setBackButtonEnabledAndTitle("Details");


        myServiceAdapter = new MultipleLayoutMyServicesDetailsAdapter(new ArrayList<Object>());
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


    class Data {

        public String title, des;


    }

    private void loadData() {


        int id = getIntent().getIntExtra("id", 0);

        showLoading();

        dataSource.getServiceDetailsForCustomer(dataSource.getAuthendicate(), id, this);


//        CustomerServices.Data.Datum datum=getIntent().getParcelableExtra("data");
//
//
//        Log.e(TAG, "loadData: "+new Gson().toJson(datum));
//
//        List<Data> data=new ArrayList<>();
//        try {
//            JSONObject jsonObject=new JSONObject(new Gson().toJson(datum).toString());
//
//            Iterator<?> iterator=jsonObject.keys();
//
//            while (iterator.hasNext()){
//                String key = (String)iterator.next();
//
////                    if (!key.equalsIgnoreCase("user_id")
////
////                            && !key.equalsIgnoreCase("user_id")
////
////
////                            )
//
//                Data data1=new Data();
//                String k=key.replaceAll("_"," ");
//                data1.title=capitalizeString(k);
//                data1.des=jsonObject.getString(key);
//
//                data.add(data1);
//
//
//            }
//            myServiceAdapter.updateList(data);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


    }


    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);

        CustomerServicesDetails customerServicesDetails = (CustomerServicesDetails) object;


        if (customerServicesDetails.getSuccess()) {

            CustomerServicesDetails.Data.Service data = customerServicesDetails.getData().getService();

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

            data1 = new Data();
            data1.title = "Status";
            data1.des = data.getStatus();
            list.add(data1);

            List<CustomerServicesDetails.Data.ServiceComment> serviceComment = customerServicesDetails.getData().getServiceComments();


            CommentsCount commentsCount = new CommentsCount();

            commentsCount.setCount(serviceComment.size());

            list.add(commentsCount);


            list.addAll(serviceComment);


            myServiceAdapter.updateList(list);
//            try {
//                JSONObject jsonObject = new JSONObject(new Gson().toJson(data).toString());
//
//                Iterator<?> iterator = jsonObject.keys();
//
//                while (iterator.hasNext()) {
//                    String key = (String) iterator.next();
//
//                    if (!key.equalsIgnoreCase("user_id")
//
//                            && !key.equalsIgnoreCase("assigned_by")
//                            && !key.equalsIgnoreCase("assigned_to")
//                            && !key.equalsIgnoreCase("state_id")
//                            && !key.equalsIgnoreCase("brand_id")
//                            && !key.equalsIgnoreCase("problem_type_id")
//                            && !key.equalsIgnoreCase("prefer_time_id")
//                            && !key.equalsIgnoreCase("created_at")
//                            && !key.equalsIgnoreCase("updated_at")
//
//                            ) {
//
//                        Data data1 = new Data();
//                        String k = key.replaceAll("_", " ");
//                        data1.title = capitalizeString(k);
//                        data1.des = jsonObject.getString(key);
//
//                        list.add(data1);
//                    }
//
//
//                }
//                myServiceAdapter.updateList(list);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }


        } else {

            customMessageView.setMsg("Oops", customerServicesDetails.getMessage(), CustomMessageView.StateView.ERROR);
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

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
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
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//            Data data = getValue(position);
//
//            ((ViewHolder) holder).title.setText(data.title);
//            ((ViewHolder) holder).description.setText(data.des);
//
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
