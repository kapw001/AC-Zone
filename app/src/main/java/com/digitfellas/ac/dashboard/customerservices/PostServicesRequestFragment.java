package com.digitfellas.ac.dashboard.customerservices;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.digitfellas.ac.R;
import com.digitfellas.ac.adapter.viewpager.CreateBrandAdapter;
import com.digitfellas.ac.adapter.viewpager.CreatePrefereTimeAdapter;
import com.digitfellas.ac.adapter.viewpager.CreateProblemTypeAdapter;
import com.digitfellas.ac.adapter.viewpager.CreateStateAdapter;
import com.digitfellas.ac.base.BaseFragment;
import com.digitfellas.ac.dashboard.MyServices.MyServicesStatusFragment;
import com.digitfellas.ac.data.listener.DataListener;
import com.digitfellas.ac.data.model.response.CreateService;
import com.digitfellas.ac.data.model.response.CreateService.Data.Brand;
import com.digitfellas.ac.data.model.response.PostedServicesRes;
import com.digitfellas.ac.gps.GPSTracker;
import com.digitfellas.ac.utility.TextInputUtil;
import com.digitfellas.ac.utility.Utils;
import com.google.gson.Gson;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostServicesRequestFragment extends BaseFragment {

    private static final String TAG = "PostServicesRequestFrag";

    @BindView(R.id.mobile_no)
    TextInputLayout mobileNo;
    @BindView(R.id.email)
    TextInputLayout email;
    @BindView(R.id.address1)
    TextInputLayout address1;
    @BindView(R.id.address2)
    TextInputLayout address2;
    @BindView(R.id.city)
    TextInputLayout city;
    @BindView(R.id.state)
    Spinner state;
    @BindView(R.id.zipcode)
    TextInputLayout zipcode;
    @BindView(R.id.model_name)
    TextInputLayout modelName;
    @BindView(R.id.capacity)
    TextInputLayout capacity;
    @BindView(R.id.where_at_home)
    TextInputLayout whereAtHome;
    @BindView(R.id.complaint)
    TextInputLayout complaint;
    @BindView(R.id.service_date)
    TextView serviceDate;
    @BindView(R.id.comments)
    TextInputLayout comments;
    @BindView(R.id.brand_name)
    Spinner brandName;
    @BindView(R.id.problem_type)
    Spinner problemType;
    @BindView(R.id.prefered_time)
    Spinner preferedTime;

    private CreateBrandAdapter createBrandAdapter;
    private CreatePrefereTimeAdapter createPrefereTimeAdapter;
    private CreateProblemTypeAdapter createProblemTypeAdapter;
    private CreateStateAdapter stateAdapter;

    private GPSTracker gpsTracker;

    public PostServicesRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        gpsTracker = new GPSTracker(getContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_services, container, false);
        setButterKnife(this, view);


        createBrandAdapter = new CreateBrandAdapter(getContext(), new ArrayList<Brand>());
        createPrefereTimeAdapter = new CreatePrefereTimeAdapter(getContext(), new ArrayList<CreateService.Data.PreferTime>());
        createProblemTypeAdapter = new CreateProblemTypeAdapter(getContext(), new ArrayList<CreateService.Data.ProblemType>());
        stateAdapter = new CreateStateAdapter(getContext(), new ArrayList<CreateService.Data.State>());

        state.setAdapter(stateAdapter);
        brandName.setAdapter(createBrandAdapter);
        preferedTime.setAdapter(createPrefereTimeAdapter);
        problemType.setAdapter(createProblemTypeAdapter);

        serviceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.callDatePicker(getChildFragmentManager(), new Utils.DateCallBack() {
                    @Override
                    public void showDate(String date) {

                        serviceDate.setText(date);

                    }
                });

            }
        });

        problemType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                CreateService.Data.ProblemType problemType = createProblemTypeAdapter.getItem(position);

                if (problemType.getProblemType().toLowerCase().equalsIgnoreCase("others")) {

                    complaint.setVisibility(View.VISIBLE);

                } else {
                    complaint.setVisibility(View.GONE);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        loadData();
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.save_cancel_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.save:

                if (isValidate()) {

                    postService();

                }

                break;

            case R.id.cancel:

                getActivity().onBackPressed();

                break;

        }
        return super.onOptionsItemSelected(item);

    }

    private void postService() {


        CreateService.Data.State stateData = stateAdapter.getItem(state.getSelectedItemPosition());
        CreateService.Data.Brand brandData = createBrandAdapter.getItem(brandName.getSelectedItemPosition());
        CreateService.Data.ProblemType problemTypeData = createProblemTypeAdapter.getItem(problemType.getSelectedItemPosition());
        CreateService.Data.PreferTime preferTimeData = createPrefereTimeAdapter.getItem(preferedTime.getSelectedItemPosition());


        Map<String, Object> params = new HashMap<>();


        Map<String, Object> customerinfo = new HashMap<>();
        customerinfo.put("customer_email", getText(email));
        customerinfo.put("address_1", getText(address1));
        customerinfo.put("address_2", getText(address2));
        customerinfo.put("city", getText(city));
        customerinfo.put("zipcode", getText(zipcode));
        customerinfo.put("mobile_no", getText(mobileNo));
        customerinfo.put("state", stateData.getId());


        Map<String, Object> serviceinfo = new HashMap<>();
        serviceinfo.put("brand_name", brandData.getId());
        serviceinfo.put("model_name", getText(modelName));
        serviceinfo.put("ton_capacity", getText(capacity));
        serviceinfo.put("problem_type", problemTypeData.getId());
        serviceinfo.put("place_at_home", getText(whereAtHome));
        serviceinfo.put("complaint", getText(complaint));
        serviceinfo.put("prefer_date", serviceDate.getText());
        serviceinfo.put("prefer_time", preferTimeData.getId());

        if (gpsTracker.canGetLocation()) {
            gpsTracker.getLocation();

            serviceinfo.put("longitude", gpsTracker.getLatitude());
            serviceinfo.put("latitude", gpsTracker.getLongitude());

        } else {
            showToast("There is no location");
        }


        params.put("customerinfo", customerinfo);
        params.put("serviceinfo", serviceinfo);


        Log.e(TAG, "isValidate: " + new JSONObject(params).toString());


        showLoading();

        dataSource.postService(dataSource.getAuthendicate(), new JSONObject(params).toString(), new DataListener() {
            @Override
            public void onSuccess(Object object) {

                PostServicesRequestFragment.super.onSuccess(object);

                PostedServicesRes res = (PostedServicesRes) object;

                if (res.getSuccess()) {

                    showToast(res.getMessage());

                    Intent intent = new Intent();

                    getActivity().setResult(MyServicesStatusFragment.KEY_REFRESH_RESULT_CODE, intent);

                    getActivity().onBackPressed();

                } else {
                    showToast(res.getMessage());
                }

                Log.e(TAG, "onSuccess: " + new Gson().toJson(object));

            }

            @Override
            public void onFail(Throwable throwable) {
                PostServicesRequestFragment.super.onFail(throwable);
            }

            @Override
            public void onNetworkFailure() {
                PostServicesRequestFragment.super.onNetworkFailure();

            }
        });


    }


    private boolean isValidate() {

        boolean isValid = true;

        if (TextUtils.isEmpty(mobileNo.getEditText().getText())) {
            TextInputUtil.setError(mobileNo, getString(R.string.mobileempty));
            isValid = false;
        } else if (!(mobileNo.getEditText().getText().toString().length() >= 10)) {
            TextInputUtil.setError(mobileNo, getString(R.string.mobile_must_be));
            isValid = false;
        } else {
            TextInputUtil.setDisableError(mobileNo);
        }


        if (TextUtils.isEmpty(address1.getEditText().getText())) {
            TextInputUtil.setError(address1, getString(R.string.addressempty));
            isValid = false;
        }

        int statePos = state.getSelectedItemPosition();
        int problemTye = problemType.getSelectedItemPosition();
        int prefetime = preferedTime.getSelectedItemPosition();
        int bName = brandName.getSelectedItemPosition();

        if (statePos <= 0) {

            showToast("Choose state");

            isValid = false;
        }

        if (bName <= 0) {

            showToast("Choose Brand Name");

            isValid = false;
        }

        if (problemTye <= 0) {

            showToast("Choose Problem Type");

            isValid = false;
        }

        if (prefetime <= 0) {

            showToast("Choose Prefer Time");

            isValid = false;
        }

        return isValid;

    }


    private void loadData() {


        showLoading();
        dataSource.createService(dataSource.getAuthendicate(), new DataListener() {
            @Override
            public void onSuccess(Object object) {

                PostServicesRequestFragment.super.onSuccess(object);

                CreateService createService = (CreateService) object;

                if (createService.getSuccess()) {

                    List<CreateService.Data.Brand> brandList = createService.getData().getBrands();

                    Brand brand = new Brand();
                    brand.setBrandName("Choose Brand Name");
                    brandList.add(0, brand);
                    createBrandAdapter.updateBrand(brandList);

                    List<CreateService.Data.State> stateList = createService.getData().getStates();
                    CreateService.Data.State state = new CreateService.Data.State();
                    state.setStateName("Choose State");
                    stateList.add(0, state);

                    stateAdapter.updateBrand(stateList);
                    List<CreateService.Data.PreferTime> preferTimes = createService.getData().getPreferTimes();

                    CreateService.Data.PreferTime preferTime = new CreateService.Data.PreferTime();

                    preferTime.setPreferTime("Choose Prefer Time");

                    preferTimes.add(0, preferTime);

                    createPrefereTimeAdapter.updateBrand(preferTimes);


                    List<CreateService.Data.ProblemType> problemTypes = createService.getData().getProblemTypes();

                    CreateService.Data.ProblemType problemType = new CreateService.Data.ProblemType();
                    problemType.setProblemType("Choose Problem Type");

                    problemTypes.add(0, problemType);

                    createProblemTypeAdapter.updateBrand(problemTypes);


                } else {

                    showToast(createService.getMessage());
                }


            }

            @Override
            public void onFail(Throwable throwable) {

                PostServicesRequestFragment.super.onFail(throwable);

            }

            @Override
            public void onNetworkFailure() {
                PostServicesRequestFragment.super.onNetworkFailure();
            }
        });


    }

}
