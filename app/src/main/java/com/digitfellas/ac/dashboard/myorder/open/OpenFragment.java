package com.digitfellas.ac.dashboard.myorder.open;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.digitfellas.ac.R;
import com.digitfellas.ac.base.BaseFragment;
import com.digitfellas.ac.common.PaginationScrollListener;
import com.digitfellas.ac.customview.CustomMessageView;
import com.digitfellas.ac.dashboard.OrderViewAndCommonts.OrderViewANdCommentsActivity;
import com.digitfellas.ac.dashboard.addorder.AddOrderForCustomerActivity;
import com.digitfellas.ac.dashboard.addorder.AddOrderForDealerActivity;
import com.digitfellas.ac.dashboard.dealer.DealerDashboardActivity;
import com.digitfellas.ac.dashboard.myorder.PaginationAdapter;
import com.digitfellas.ac.data.listener.DataListener;
import com.digitfellas.ac.data.model.response.MyOrderResponse;
import com.digitfellas.ac.utility.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class OpenFragment extends BaseFragment implements PaginationAdapter.OnItemClickListener {
    private static final int RC_LOCATION = 12;

    private static final String TAG = "OpenFragment";
    public static final int KEY_REFRESH_RESULT_CODE = 456;

    @BindView(R.id.custom_message_view)
    CustomMessageView customMessageView;

    @BindView(R.id.fab)
    FloatingActionButton mFab;

    private PaginationAdapter myOderAdapter;
    private LinearLayoutManager linearLayoutManager;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 3;
    private int currentPage = 1;
    private List<MyOrderResponse.Data.Datum> originalDataList = new ArrayList<>();
    private boolean hasBeenVisibleOnce = false;
    // create boolean for fetching data
    private boolean isViewShown = false;

    public OpenFragment() {
        // Required empty public constructor
    }


    public static OpenFragment getInstance(int status) {

        OpenFragment openFragment = new OpenFragment();

        Bundle bundle = new Bundle();

        bundle.putInt("status", status);

        openFragment.setArguments(bundle);

        return openFragment;


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_open, container, false);
        setButterKnife(this, view);

        myOderAdapter = new PaginationAdapter(getContext(), this);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        customMessageView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayout.VERTICAL);

        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));

        customMessageView.addItemDecoration(dividerItemDecoration);
//        mMyOrder_ListView.addItemDecoration(new EqualSpacingItemDecoration(16,EqualSpacingItemDecoration.VERTICAL));
        customMessageView.setAdapter(myOderAdapter);

        customMessageView.addOnScrollListener(Utils.fabShowHide(mFab));

        customMessageView.setMessageTitle("No data");
        customMessageView.setMessageDescription("There is no data");

//        customMessageView.showMessage();
//        customMessageView.showContent();
        customMessageView.showMessage();
        customMessageView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;


//                // mocking network delay for API call
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {

                loadData();
//                    }
//                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return currentPage + 1;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

//        customMessageView.showMessage();


        if (getUserVisibleHint()) {
            loadDataFirstTime();
            hasBeenVisibleOnce = true;
        }


        return view;
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);

        Log.e(TAG, "setUserVisibleHint: open " + visible);
        if (visible && getView() != null) {
            if (visible && !hasBeenVisibleOnce) {
                loadDataFirstTime();
                hasBeenVisibleOnce = true;

            }
        }
    }

    private void loadDataFirstTime() {

        int status = getArguments().getInt("status");

        Map<String, String> param = dataSource.getAuthendicate();

        showLoading();

        dataSource.loadMyOrder(param, currentPage + "", status + "", new DataListener() {
            @Override
            public void onSuccess(Object object) {

                OpenFragment.super.onSuccess(object);

                MyOrderResponse myOrderResponse = (MyOrderResponse) object;

                if (myOrderResponse.getSuccess()) {

                    myOderAdapter.removeLoadingFooterCheck();
                    List<MyOrderResponse.Data.Datum> datumList = myOrderResponse.getData().getData();


                    if (datumList != null && !datumList.isEmpty()) {

                        if (originalDataList.isEmpty()) {
                            originalDataList.addAll(datumList);
                            myOderAdapter.addAll(originalDataList);

                        } else {

                            datumList.removeAll(originalDataList);

                            originalDataList.addAll(datumList);
                            myOderAdapter.addAll(originalDataList);

                            customMessageView.smoothScrollTo(originalDataList.size() - 1);

                        }


                        isLoading = false;
                        if (datumList != null && datumList.size() == myOrderResponse.getData().getPerPage()) {

                            currentPage++;

                            myOderAdapter.addLoadingFooter();

                        }

                        customMessageView.showContent();
                    } else {

                        customMessageView.setMsg("Oops", "There is no oders", CustomMessageView.StateView.ERROR);

                    }

                } else {
                    customMessageView.setMsg("Oops", myOrderResponse.getMessage(), CustomMessageView.StateView.ERROR);

                }

            }

            @Override
            public void onFail(Throwable throwable) {
                OpenFragment.super.onFail(throwable);
//                customMessageView.showMessage();
                isLoading = false;
                myOderAdapter.removeLoadingFooterCheck();
                customMessageView.setMsg("Oops", "There is no orders", CustomMessageView.StateView.ERROR);
            }

            @Override
            public void onNetworkFailure() {
                OpenFragment.super.onNetworkFailure();

                customMessageView.setMsg("No Internet", "There is no Internet,please check your internet connection", CustomMessageView.StateView.ERROR);

                isLoading = false;
            }
        });

    }


    private void loadData() {

//        isLoading = true;

        int status = getArguments().getInt("status");

        Map<String, String> param = dataSource.getAuthendicate();

        dataSource.loadMyOrder(param, currentPage + "", status + "", new DataListener() {
            @Override
            public void onSuccess(Object object) {

                MyOrderResponse myOrderResponse = (MyOrderResponse) object;

                if (myOrderResponse.getSuccess()) {

                    myOderAdapter.removeLoadingFooterCheck();
                    List<MyOrderResponse.Data.Datum> datumList = myOrderResponse.getData().getData();

//                    Log.e(TAG, "onSuccess: " + new Gson().toJson(datumList));
//
////                    if(!isLoading){
//
//                        showToast("cc");
//                    customMessageView.showContent();

                    if (originalDataList.isEmpty()) {
                        originalDataList.addAll(datumList);
                        myOderAdapter.addAll(originalDataList);
                    } else {


                        datumList.removeAll(originalDataList);

                        originalDataList.addAll(datumList);
                        myOderAdapter.addAll(originalDataList);

//                            customMessageView.smoothScrollTo(originalDataList.size()-1);


                    }



                                                    /*if(result.size()==PAGE_SIZE)
                                                        progressBar.setVisibility(View.VISIBLE);
                                                    if((result.size()%PAGE_SIZE)!=1){
                                                        progressBar.setVisibility(View.GONE);}*/


//                        myOderAdapter.removeLoadingFooter();
//                        originalDataList.addAll(datumList);
//                        myOderAdapter.addAll(datumList);
//                        myOderAdapter.addLoadingFooter();

//                    }


//                    currentPage++;
                    isLoading = false;
                    if (datumList != null && datumList.size() == myOrderResponse.getData().getPerPage()) {

                        currentPage++;


                        myOderAdapter.addLoadingFooter();
                    }


//                    customMessageView.showContent();
//
//                    if (originalDataList.isEmpty()){
//                        customMessageView.showMessage();
//                    }

//                    else {
//                        isLastPage = true;
//                    }

//                    if (result.size() > PAGE_SIZE) {
//                        isLoading = false;
//                        dataAdapter .addLoadingFooter();
//                    } else {
//                        isLastPage = true;
//                    }


                } else {
//                    if (originalDataList.isEmpty()){
//                        customMessageView.showMessage();
//                    }
                }

            }

            @Override
            public void onFail(Throwable throwable) {
                isLoading = false;
                myOderAdapter.removeLoadingFooterCheck();
//                if (originalDataList.size()<=0){
//                    customMessageView.showMessage();
//                }
//                isLoading=true;
//                isLastPage=false;
//                myOderAdapter.removeLoadingFooter();

//                currentPage--;
//                showToast("There is no more data");
            }

            @Override
            public void onNetworkFailure() {
                isLoading = false;
//                if (originalDataList.isEmpty()){
//                    customMessageView.showMessage();
//                }
//                currentPage--;
//                myOderAdapter.removeLoadingFooter();
                showToast("Something went wrong");
            }
        });


//        List<MyOrderResponse.Data.Datum> data = new ArrayList<>();
//
//        for (int i = 0; i < 20; i++) {
//
//            MyOrderResponse.Data.Datum datum = new MyOrderResponse.Data.Datum();
//            datum.setCategoryName("Mobile");
//            datum.setDeliveryDate("2018-07-14");
//            datum.setId(i);
//            datum.setTotalAmount("2000");
//            datum.setName("Testing");
//            data.add(datum);
//
//        }
//
//        myOderAdapter.addAll(data);
//
//        currentPage++;
//
//        myOderAdapter.addLoadingFooter();

        Log.e(TAG, "loadData: " + currentPage);

//        showToast(currentPage+"");

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == KEY_REFRESH_RESULT_CODE) {
            isLoading = true;
            loadData();
        }

//        showToast("Called");
    }


    @AfterPermissionGranted(RC_LOCATION)
    private void locationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            // Already have permission, do the thing

            if (dataSource.getRoleType().toLowerCase().equalsIgnoreCase("customer")) {

                startActivity(new Intent(getContext(), AddOrderForCustomerActivity.class).putExtra("isfinish", false));
            } else {
                startActivity(new Intent(getContext(), AddOrderForDealerActivity.class).putExtra("isfinish", false));
            }
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationale),
                    RC_LOCATION, perms);
        }
    }


    @OnClick(R.id.fab)
    public void onClick(View v) {

        locationPermission();

//
//        if (dataSource.getRoleType().toLowerCase().equalsIgnoreCase("customer")) {
//
//            startActivity(new Intent(getContext(), AddOrderForCustomerActivity.class).putExtra("isfinish", false));
//        } else {
//            startActivity(new Intent(getContext(), AddOrderForDealerActivity.class).putExtra("isfinish", false));
//        }


    }


    @Override
    public void OnItemClick(View view, MyOrderResponse.Data.Datum datum, int position) {


        Intent intent = new Intent(getContext(), OrderViewANdCommentsActivity.class);
        intent.putExtra("id", datum.getId());
        startActivity(intent);

    }
}
