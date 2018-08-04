package com.digitfellas.ac.dashboard.myorder.open;


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
import com.digitfellas.ac.dashboard.addorder.AddOrderForCustomerActivity;
import com.digitfellas.ac.dashboard.myorder.PaginationAdapter;
import com.digitfellas.ac.data.listener.DataListener;
import com.digitfellas.ac.data.model.response.MyOrderResponse;
import com.digitfellas.ac.utility.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class OpenFragmentBackup extends BaseFragment implements PaginationAdapter.OnItemClickListener {


    private static final String TAG = "OpenFragment";

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
    private List<MyOrderResponse.Data.Datum> originalDataList=new ArrayList<>();



    public OpenFragmentBackup() {
        // Required empty public constructor
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
        customMessageView.setMessageDescription("No data\nTap + to add one");

        customMessageView.showContent();

        customMessageView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
//                isLoading = true;


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
                return currentPage;
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

        customMessageView.showMessage();
        loadData();

        return view;
    }


    private void loadData() {
        isLoading = true;


        Map<String, String> param = dataSource.getAuthendicate();

        dataSource.loadMyOrder(param, currentPage + "", 1 + "", new DataListener() {
            @Override
            public void onSuccess(Object object) {

                MyOrderResponse myOrderResponse = (MyOrderResponse) object;

                if (myOrderResponse.getSuccess()) {


                    List<MyOrderResponse.Data.Datum> datumList = myOrderResponse.getData().getData();

                    Log.e(TAG, "onSuccess: " + new Gson().toJson(datumList));
                    isLoading=false;
                    if(!isLoading){


//                        if (originalDataList.isEmpty()){
//                            originalDataList.addAll(datumList);
//                            myOderAdapter.addAll(originalDataList);
//                        }else {
//
//                            for (int i = 0; i <originalDataList.size() ; i++) {
//
//                                MyOrderResponse.Data.Datum oDatum=originalDataList.get(i);
//
//
//                                for (int j = 0; j <datumList.size() ; j++) {
//                                    MyOrderResponse.Data.Datum sDatum=datumList.get(j);
//
//                                    if (!oDatum.getId().equals(sDatum.getId())){
//                                        originalDataList.add(sDatum);
//                                        myOderAdapter.add(sDatum);
//                                        Log.e(TAG, "onSuccess: check "+oDatum.getId().equals(sDatum.getId()) );
//                                    }
//
//                                }
//
//
//                            }
//
//
//                        }



                                                    /*if(result.size()==PAGE_SIZE)
                                                        progressBar.setVisibility(View.VISIBLE);
                                                    if((result.size()%PAGE_SIZE)!=1){
                                                        progressBar.setVisibility(View.GONE);}*/



//                        myOderAdapter.removeLoadingFooter();
                        originalDataList.addAll(datumList);
                        myOderAdapter.addAll(datumList);
//                        myOderAdapter.addLoadingFooter();

                    }


//                    currentPage++;

                    if (datumList != null && datumList.size()== myOrderResponse.getData().getPerPage()) {

                        currentPage++;

                        isLoading = false;
                        isLastPage=false;
                        myOderAdapter.addLoadingFooter();
                    }else {

                        isLastPage=true;
                    }


                    customMessageView.showContent();

                    if (originalDataList.isEmpty()){
                        customMessageView.showMessage();
                    }

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
                    if (originalDataList.isEmpty()){
                        customMessageView.showMessage();
                    }
                }

            }

            @Override
            public void onFail(Throwable throwable) {
                if (originalDataList.size()<=0){
                    customMessageView.showMessage();
                }
//                isLoading=true;
//                isLastPage=false;
//                myOderAdapter.removeLoadingFooter();

//                currentPage--;
//                showToast("There is no more data");
            }

            @Override
            public void onNetworkFailure() {
                if (originalDataList.isEmpty()){
                    customMessageView.showMessage();
                }
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

    }


    @OnClick(R.id.fab)
    public void onClick(View v) {

        startActivity(new Intent(getContext(), AddOrderForCustomerActivity.class));

    }


    @Override
    public void OnItemClick(View view, MyOrderResponse.Data.Datum datum, int position) {

    }
}
