package com.digitfellas.ac.dashboard.salesman;


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
import com.digitfellas.ac.dashboard.MyServices.MyServicePaginationAdapter;
import com.digitfellas.ac.dashboard.customerservices.MyServiceDetailsActivity;
import com.digitfellas.ac.dashboard.customerservices.PostServiceActivity;
import com.digitfellas.ac.dashboard.serviceman.ServiceManServiceDetailsActivity;
import com.digitfellas.ac.data.listener.DataListener;
import com.digitfellas.ac.data.model.response.CustomerServices;
import com.digitfellas.ac.data.model.response.Tasks;
import com.digitfellas.ac.utility.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.digitfellas.ac.dashboard.salesman.SalesManActivity.KEY_REQUEST_CODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SalesManTasksFragment extends BaseFragment implements MyTasksPaginationAdapter.OnItemClickListener {


    private static final String TAG = "OpenFragment";
    public static final int KEY_REFRESH_RESULT_CODE = 123;
    private static final int RC_LOCATION = 12;
    @BindView(R.id.custom_message_view)
    CustomMessageView customMessageView;

    @BindView(R.id.fab)
    FloatingActionButton mFab;

    private MyTasksPaginationAdapter myOderAdapter;
    private LinearLayoutManager linearLayoutManager;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 3;
    private int currentPage = 1;
    private List<Tasks.Data.Datum> originalDataList = new ArrayList<>();
    private boolean hasBeenVisibleOnce = false;
    // create boolean for fetching data
    private boolean isViewShown = false;

    public SalesManTasksFragment() {
        // Required empty public constructor
    }


    public static SalesManTasksFragment getInstance(int status) {

        SalesManTasksFragment myServicesStatusFragment = new SalesManTasksFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("status", status);

        myServicesStatusFragment.setArguments(bundle);

        return myServicesStatusFragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_open, container, false);
        setButterKnife(this, view);

        myOderAdapter = new MyTasksPaginationAdapter(getContext(), this);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        customMessageView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayout.VERTICAL);

        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));

        customMessageView.addItemDecoration(dividerItemDecoration);
//        mMyOrder_ListView.addItemDecoration(new EqualSpacingItemDecoration(16,EqualSpacingItemDecoration.VERTICAL));
        customMessageView.setAdapter(myOderAdapter);

//        if (dataSource.getRoleType().toLowerCase().equalsIgnoreCase("ServiceRep")) {
//
//
//
//        }
        if (!dataSource.getRoleType().toLowerCase().equalsIgnoreCase("ServiceRep")) {


            customMessageView.addOnScrollListener(Utils.fabShowHide(mFab));

        } else {
            mFab.setVisibility(View.GONE);
        }


        customMessageView.setMessageTitle("No data");
        customMessageView.setMessageDescription("There is no data");

//        customMessageView.showMessage();
//        customMessageView.showContent();
        customMessageView.showMessage();
        customMessageView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                loadData();
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
            currentPage = 1;
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
//            if (visible && !hasBeenVisibleOnce) {
            currentPage = 1;
            loadDataFirstTime();
            hasBeenVisibleOnce = true;

//            }
        }
    }

    private void loadDataFirstTime() {


        myOderAdapter = new MyTasksPaginationAdapter(getContext(), this);

        customMessageView.setAdapter(myOderAdapter);

        int status = getArguments().getInt("status");

        Map<String, String> param = dataSource.getAuthendicate();

        showLoading();

        dataSource.getAllTask(param, currentPage, status, new DataListener() {
            @Override
            public void onSuccess(Object object) {

                SalesManTasksFragment.super.onSuccess(object);

                Tasks myOrderResponse = (Tasks) object;

                if (myOrderResponse.getSuccess()) {
                    originalDataList.clear();
                    myOderAdapter.clearAllData();
                    myOderAdapter.removeLoadingFooterCheck();
                    List<Tasks.Data.Datum> datumList = myOrderResponse.getData().getData();


                    if (datumList != null && datumList.size() > 0) {

                        if (originalDataList.isEmpty()) {
                            originalDataList.addAll(datumList);
                            myOderAdapter.addAll(originalDataList);

                        } else {

                            datumList.removeAll(originalDataList);

                            originalDataList.addAll(datumList);
                            myOderAdapter.addAll(originalDataList);

                            customMessageView.smoothScrollTo(originalDataList.size() - 1);

                        }

                        currentPage++;

                        isLoading = false;
                        if (datumList != null && datumList.size() == myOrderResponse.getData().getPerPage()) {


                            myOderAdapter.addLoadingFooter();
                        }
                        customMessageView.showContent();
                    } else {


                        customMessageView.setMsg("Oops", "There is no data", CustomMessageView.StateView.ERROR);


                    }

                } else {
//                        customMessageView.showMessage();

                    customMessageView.setMsg("Oops", myOrderResponse.getMessage(), CustomMessageView.StateView.ERROR);

                }

            }

            @Override
            public void onFail(Throwable throwable) {
                SalesManTasksFragment.super.onFail(throwable);
                customMessageView.setMsg("Oops", "There is no data", CustomMessageView.StateView.ERROR);
                isLoading = false;
                myOderAdapter.removeLoadingFooterCheck();
            }

            @Override
            public void onNetworkFailure() {
                SalesManTasksFragment.super.onNetworkFailure();

                customMessageView.setMsg("Oops", "There is no internet connection", CustomMessageView.StateView.ERROR);

                isLoading = false;
            }
        });

    }


    private void loadData() {

//        isLoading = true;

//        showToast("Called");

        Map<String, String> param = dataSource.getAuthendicate();

        dataSource.getAllServicesForCustomer(param, currentPage, 1, new DataListener() {
            @Override
            public void onSuccess(Object object) {

                Tasks myOrderResponse = (Tasks) object;

                if (myOrderResponse.getSuccess()) {

                    myOderAdapter.removeLoadingFooterCheck();
                    List<Tasks.Data.Datum> datumList = myOrderResponse.getData().getData();


                    if (originalDataList.isEmpty()) {
                        originalDataList.addAll(datumList);
                        myOderAdapter.addAll(originalDataList);
                    } else {


                        datumList.removeAll(originalDataList);

                        originalDataList.addAll(datumList);
                        myOderAdapter.addAll(originalDataList);


                    }


                    isLoading = false;
                    if (datumList != null && datumList.size() == myOrderResponse.getData().getPerPage()) {

                        currentPage++;


                        myOderAdapter.addLoadingFooter();
                    }

                } else {
                }

            }

            @Override
            public void onFail(Throwable throwable) {
                isLoading = false;
                myOderAdapter.removeLoadingFooterCheck();

            }

            @Override
            public void onNetworkFailure() {
                isLoading = false;
                showToast("Something went wrong");
            }
        });


        Log.e(TAG, "loadData: " + currentPage);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == KEY_REFRESH_RESULT_CODE) {
            currentPage = 1;
            isLoading = true;
            loadDataFirstTime();
        }

//        showToast("Called");
    }

    @AfterPermissionGranted(RC_LOCATION)
    private void locationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            // Already have permission, do the thing

            Intent intent = new Intent(getContext(), AddTaskActivity.class);

            startActivityForResult(intent, KEY_REQUEST_CODE);
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
//        if (dataSource.getRoleType().toLowerCase().equalsIgnoreCase("customer")) {
//
//            startActivity(new Intent(getContext(), AddOrderForCustomerActivity.class).putExtra("isfinish", false));
//        } else {
//            startActivity(new Intent(getContext(), AddOrderForDealerActivity.class).putExtra("isfinish", false));
//        }


    }


    @Override
    public void OnItemClick(View view, Tasks.Data.Datum datum, int position) {

        Intent intent = new Intent(getContext(), TaskDetailsActivity.class);
        intent.putExtra("id", datum.getId());
//        intent.putExtra("data", datum);
        startActivity(intent);


    }
}
