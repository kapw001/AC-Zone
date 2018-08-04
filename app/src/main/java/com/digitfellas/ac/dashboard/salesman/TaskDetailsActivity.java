package com.digitfellas.ac.dashboard.salesman;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.digitfellas.ac.R;
import com.digitfellas.ac.base.BaseActivity;
import com.digitfellas.ac.customview.CustomMessageView;
import com.digitfellas.ac.dashboard.customerservices.MultipleLayoutMyServicesDetailsAdapter;
import com.digitfellas.ac.dashboard.customerservices.MyServiceDetailsActivity;
import com.digitfellas.ac.dashboard.postcomments.PostFragment;
import com.digitfellas.ac.data.model.response.CommentsCount;
import com.digitfellas.ac.data.model.response.CustomerServicesDetails;
import com.digitfellas.ac.data.model.response.TasksDetails;
import com.digitfellas.ac.utility.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TaskDetailsActivity extends BaseActivity {

    private static final String TAG = "TaskDetailsActivity";

    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.listview)
    CustomMessageView customMessageView;
    private MultipleLayoutMyTaskDetailsAdapter myServiceAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        setButterKnife();
        setBackButtonEnabledAndTitle("Task details");


        myServiceAdapter = new MultipleLayoutMyTaskDetailsAdapter(new ArrayList<Object>());
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


    private void loadData() {

        int id = getIntent().getIntExtra("id", 0);

        showLoading();

        dataSource.getTaskDetails(dataSource.getAuthendicate(), id, this);
    }


    class Data {

        public String title, des;


    }


    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);

        TasksDetails customerServicesDetails = (TasksDetails) object;


        if (customerServicesDetails.getSuccess()) {

            TasksDetails.Data.Task task = customerServicesDetails.getData().getTask();

            List list = new ArrayList<>();

            TaskDetailsActivity.Data data0 = new TaskDetailsActivity.Data();
            data0.title = "Task Date";
            data0.des = task.getTaskDate();
            list.add(data0);

            List<TasksDetails.Data.TaskPerform> taskPerformList = customerServicesDetails.getData().getTaskPerforms();

            for (int i = 0; i < taskPerformList.size(); i++) {

                TaskDetailsActivity.Data datal = new TaskDetailsActivity.Data();

                TasksDetails.Data.TaskPerform taskPerform = taskPerformList.get(i);
                datal.title = taskPerform.getPerform();
                datal.des = taskPerform.getPerformDesc() != null && taskPerform.getPerformDesc().length() > 0 ? taskPerform.getPerformDesc() : "";

                list.add(datal);

            }


            TaskDetailsActivity.Data data1 = new TaskDetailsActivity.Data();
            data1.title = "Remarks";
            data1.des = task.getRemarks() != null && task.getRemarks().length() > 0 ? task.getRemarks() : "There is no remarks";
            list.add(data1);

            List<TasksDetails.Data.TaskComment> taskCommentList = customerServicesDetails.getData().getTaskComments();


            CommentsCount commentsCount = new CommentsCount();

            commentsCount.setCount(taskCommentList.size());

            list.add(commentsCount);


            list.addAll(taskCommentList);


            myServiceAdapter.updateList(list);


        } else {

            customMessageView.setMsg("Oops", customerServicesDetails.getMessage(), CustomMessageView.StateView.ERROR);
        }
//

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


    @OnClick(R.id.fab)
    public void onClick(View v) {

        final int id = getIntent().getIntExtra("id", 0);

        Log.e(TAG, "onClick: id " + id);

        PostFragment postFragment = PostFragment.getInstance(id, "task");

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
}
