package com.digitfellas.ac.dashboard.salesman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.digitfellas.ac.R;
import com.digitfellas.ac.base.BaseActivity;
import com.digitfellas.ac.data.listener.DataListener;
import com.digitfellas.ac.data.model.request.TaskPost;
import com.digitfellas.ac.data.model.response.TaskAdd;
import com.digitfellas.ac.gps.GPSTracker;
import com.digitfellas.ac.utility.KeyboardUtils;
import com.digitfellas.ac.utility.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class AddTaskActivity extends BaseActivity {

    private static final String TAG = "AddTaskActivity";
    @BindView(R.id.customer_name)
    Spinner customerName;
    @BindView(R.id.task_date)
    TextView taskDate;
    @BindView(R.id.taskPerformedView)
    RecyclerView taskPerformedView;
    @BindView(R.id.remarks)
    AppCompatEditText remarks;

    private TaskPerformedAdapter adapter;

    private ArrayAdapter<TaskAdd.Data.Dealer> dealerArrayAdapter;

    private GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);
        setBackButtonEnabledAndTitle("Add Task");

        initView();

        loadData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.save_cancel_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.save) {

            if (validation()) {

                saveTask();
            } else {

                Log.e(TAG, "onOptionsItemSelected: something went wrong ");
            }

        } else if (item.getItemId() == R.id.cancel) {

            onBackPressed();
        }


        return super.onOptionsItemSelected(item);
    }

    private void initView() {

        gpsTracker = new GPSTracker(this);
        adapter = new TaskPerformedAdapter(new ArrayList<TaskAdd.Data.Perform>());

        taskPerformedView.setLayoutManager(new LinearLayoutManager(this));
        taskPerformedView.setNestedScrollingEnabled(false);
        taskPerformedView.setAdapter(adapter);

        dealerArrayAdapter = new ArrayAdapter<TaskAdd.Data.Dealer>(this, android.R.layout.simple_spinner_item, new ArrayList<TaskAdd.Data.Dealer>());

        dealerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        customerName.setAdapter(dealerArrayAdapter);


        taskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.callDatePickerShowAllDate(getSupportFragmentManager(), new Utils.DateCallBack() {
                    @Override
                    public void showDate(String date) {

                        taskDate.setText(date);
                    }
                });

            }
        });

    }

    private void loadData() {


        showLoading();

        dataSource.getTask(dataSource.getAuthendicate(), new DataListener() {
            @Override
            public void onSuccess(Object object) {

                AddTaskActivity.super.onSuccess(object);

                TaskAdd taskAdd = (TaskAdd) object;

                if (taskAdd.getSuccess()) {


                    List<TaskAdd.Data.Dealer> dealerList = taskAdd.getData().getDealers();

                    TaskAdd.Data.Dealer dealer = new TaskAdd.Data.Dealer();
                    dealer.setName("Choose Customer");

                    dealerList.add(0, dealer);

                    List<TaskAdd.Data.Perform> performList = taskAdd.getData().getPerforms();

                    dealerArrayAdapter.addAll(dealerList);
                    adapter.updateList(performList);


                } else {

                    showToast(taskAdd.getMessage());
                }


            }

            @Override
            public void onFail(Throwable throwable) {
                AddTaskActivity.super.onFail(throwable);
            }

            @Override
            public void onNetworkFailure() {
                AddTaskActivity.super.onNetworkFailure();

            }
        });

    }


    private void saveTask() {


        KeyboardUtils.hideKeyboard(this);

        TaskAdd.Data.Dealer dealer = dealerArrayAdapter.getItem(customerName.getSelectedItemPosition());

        List<TaskAdd.Data.Perform> performList = getPerform();


        TaskPost taskPost = new TaskPost();

        taskPost.setDealer(dealer);
        taskPost.setPerforms(performList);
        taskPost.setTeskDate(taskDate.getText().toString());
        taskPost.setRemark(remarks.getText().toString());


        if (gpsTracker.canGetLocation()) {

            gpsTracker.getLocation();

            taskPost.setLatitude(gpsTracker.getLatitude());
            taskPost.setLongtitude(gpsTracker.getLongitude());

        }


        Log.e(TAG, "saveTask: " + new Gson().toJson(taskPost));

        showLoading();
        dataSource.postTask(dataSource.getAuthendicate(), taskPost, new DataListener() {
            @Override
            public void onSuccess(Object object) {

                AddTaskActivity.super.onSuccess(object);

                showToast("Task submitted successfully.");

                Intent intent = new Intent();

                setResult(SalesManActivity.KEY_REQUEST_CODE, intent);
                onBackPressed();


            }

            @Override
            public void onFail(Throwable throwable) {

                AddTaskActivity.super.onFail(throwable);

                showToast("Something went wrong, Please try again");

            }

            @Override
            public void onNetworkFailure() {
                AddTaskActivity.super.onNetworkFailure();

            }
        });


    }

    private List<TaskAdd.Data.Perform> getPerform() {

        List<TaskAdd.Data.Perform> performList = new ArrayList<>(adapter.getList());

        for (Iterator<TaskAdd.Data.Perform> performIterator = performList.iterator(); performIterator.hasNext(); ) {

            if (!performIterator.next().isSelected()) {
                performIterator.remove();
            }

        }

//        List<TaskAdd.Data.Perform> performListNew = new ArrayList<>();
//
//        for (int i = 0; i < performList.size(); i++) {
//
//            TaskAdd.Data.Perform perform = performList.get(i);
//
//            if (perform.isSelected()) {
//
//                performListNew.add(perform);
//            }
//
//        }

        return performList;
    }


    private boolean validation() {

        boolean isValid = false;

        if (!(customerName.getSelectedItemPosition() > 0)) {

            showToast("Please choose customer");
        } else if (!(taskDate.getText().length() > 0)) {

            showToast("Please choose task date");

        } else if (!validateTaskPerformed()) {

            showToast("Please select any one task performed and add description / comments");
        } else {

            isValid = true;
        }


        return isValid;
    }


    private boolean validateTaskPerformed() {

        List<TaskAdd.Data.Perform> performList = adapter.getList();

        boolean isValid = false;

        int count = 0;
        int pos = -1;
        for (int i = 0; i < performList.size(); i++) {

            TaskAdd.Data.Perform perform = performList.get(i);

            if (perform.isSelected()) {
                count++;
//                if (!(perform.getDescription() != null && perform.getDescription().length() > 0)) {
//
//                    pos = i;
//
//                    break;
////                count++;
//
//
//                }
            }

        }


        if (count > 0 && pos == -1) {
            isValid = true;
        }

//        else {
//
//            TaskAdd.Data.Perform perform = performList.get(pos);
//
//            showToast("Please enter " + perform.getPerform() + " comments ");
//
//
//        }

        return isValid;
    }


}
