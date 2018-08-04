package com.digitfellas.ac.dashboard.OrderViewAndCommonts;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digitfellas.ac.R;
import com.digitfellas.ac.base.BaseActivity;
import com.digitfellas.ac.customview.CustomMessageView;
import com.digitfellas.ac.dashboard.myorder.PaginationAdapter;
import com.digitfellas.ac.dashboard.postcomments.PostFragment;
import com.digitfellas.ac.data.listener.DataListener;
import com.digitfellas.ac.data.model.ProductDetails;
import com.digitfellas.ac.data.model.response.Comments;
import com.digitfellas.ac.data.model.response.CommentsCount;
import com.digitfellas.ac.data.model.response.OrderDetailsInfo;
import com.digitfellas.ac.utility.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderViewANdCommentsActivity extends BaseActivity {

    private static final String TAG = "OrderViewANdCommentsAct";

    @BindView(R.id.custom_message_view)
    CustomMessageView customMessageView;

    @BindView(R.id.total_anmount)
    TextView mTotalAmount;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    private MultipleLayoutAdapter myOderAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view_and_comments);
        setButterKnife();

        setBackButtonEnabledAndTitle("Ordered Product details");

        initView();
        loadData();
    }


    @OnClick(R.id.fab)
    public void onClick(View v) {

        final int id = getIntent().getIntExtra("id", 0);

        Log.e(TAG, "onClick: id " + id);

        PostFragment postFragment = PostFragment.getInstance(id, "order");

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


    private void initView() {


        myOderAdapter = new MultipleLayoutAdapter(new ArrayList<Object>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        customMessageView.setLayoutManager(linearLayoutManager);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayout.VERTICAL);
//
//        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
//
//        customMessageView.addItemDecoration(dividerItemDecoration);
//        mMyOrder_ListView.addItemDecoration(new EqualSpacingItemDecoration(16,EqualSpacingItemDecoration.VERTICAL));
        customMessageView.setAdapter(myOderAdapter);

        customMessageView.addOnScrollListener(Utils.fabShowHide(mFab));

        customMessageView.setMessageTitle("No data");
        customMessageView.setMessageDescription("There is no data");

        customMessageView.showContent();

    }


    private void loadData() {


        final int id = getIntent().getIntExtra("id", 0);

        showLoading();
        dataSource.getOrderDetails(dataSource.getAuthendicate(), id, new DataListener() {
            @Override
            public void onSuccess(Object object) {

                OrderViewANdCommentsActivity.super.onSuccess(object);


                OrderDetailsInfo info = (OrderDetailsInfo) object;

                if (info.getSuccess()) {

                    OrderDetailsInfo.Data.OrderInfo orderInfo = info.getData().getOrderInfo();

                    mTotalAmount.setText("Total Amount: " + orderInfo.getTotalAmount());

                    List<Object> list = new ArrayList<>();

                    List<OrderDetailsInfo.Data.OrderDetail> orderDetailList = info.getData().getOrderDetails();
                    List<OrderDetailsInfo.Data.OrderComment> orderCommentList = info.getData().getOrderComments();

                    list.addAll(orderDetailList);

                    CommentsCount commentsCount = new CommentsCount();

                    commentsCount.setCount(orderCommentList.size());

                    list.add(commentsCount);


                    list.addAll(orderCommentList);

                    if (list.size() > 0) {

                        myOderAdapter.updateList(list);

                    } else {

                        customMessageView.showMessage();
                    }


                } else {

                    customMessageView.showMessage();

                    showToast(info.getMessage());
                }


                Log.e(TAG, "onSuccess: chaeck " + object.toString());

            }

            @Override
            public void onFail(Throwable throwable) {
                customMessageView.showMessage();
                OrderViewANdCommentsActivity.super.onFail(throwable);
            }

            @Override
            public void onNetworkFailure() {
                customMessageView.showMessage();
                OrderViewANdCommentsActivity.super.onNetworkFailure();

            }
        });


//        List<Object> list = new ArrayList<>();
//
//        for (int i = 0; i < 5; i++) {
//
//            ProductDetails productDetails = new ProductDetails();
//
//            list.add(productDetails);
//
//        }
//
//
//        list.add(new CommentsCount());
//
//
//        for (int i = 0; i < 10; i++) {
//
//            Comments comments = new Comments();
//
//            list.add(comments);
//
//        }


//        myOderAdapter.updateList(list);

    }

}
