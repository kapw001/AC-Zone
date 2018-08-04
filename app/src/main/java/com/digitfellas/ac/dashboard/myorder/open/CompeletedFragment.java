package com.digitfellas.ac.dashboard.myorder.open;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
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
import com.digitfellas.ac.data.model.response.MyOrderResponse;
import com.digitfellas.ac.utility.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompeletedFragment extends BaseFragment implements PaginationAdapter.OnItemClickListener {


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

    public CompeletedFragment() {
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

        customMessageView.showMessage();

        customMessageView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;


//                // mocking network delay for API call
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        myOderAdapter.removeLoadingFooter();
//                        loadData();
//                    }
//                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return -1;
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

//        loadData();

        return view;
    }


    private void loadData() {
        isLoading = false;

        List<MyOrderResponse.Data.Datum> data = new ArrayList<>();

        for (int i = 0; i < 20; i++) {

            MyOrderResponse.Data.Datum datum = new MyOrderResponse.Data.Datum();
            datum.setCategoryName("Mobile");
            datum.setDeliveryDate("2018-07-14");
            datum.setId(i);
            datum.setTotalAmount("2000");
            datum.setName("Testing");
            data.add(datum);

        }

        myOderAdapter.addAll(data);

        currentPage++;

        myOderAdapter.addLoadingFooter();

        loge("loadData: " + currentPage);





    }


    @OnClick(R.id.fab)
    public void onClick(View v) {

        startActivity(new Intent(getContext(), AddOrderForCustomerActivity.class));

    }


    @Override
    public void OnItemClick(View view, MyOrderResponse.Data.Datum datum, int position) {

    }
}
