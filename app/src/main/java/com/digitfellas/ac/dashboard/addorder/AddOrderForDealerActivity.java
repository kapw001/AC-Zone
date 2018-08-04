package com.digitfellas.ac.dashboard.addorder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.digitfellas.ac.R;
import com.digitfellas.ac.adapter.viewpager.BusinessCategoryAdapter;
import com.digitfellas.ac.adapter.viewpager.CustomerCategoryAdapter;
import com.digitfellas.ac.base.BaseActivity;
import com.digitfellas.ac.customview.CustomMessageView;
import com.digitfellas.ac.dashboard.addproduct.AddProductActivity;
import com.digitfellas.ac.dashboard.addproduct.EditProductActivity;
import com.digitfellas.ac.dashboard.dealer.DealerDashboardActivity;
import com.digitfellas.ac.dashboard.myorder.open.OpenFragment;
import com.digitfellas.ac.data.listener.DataListener;
import com.digitfellas.ac.data.model.request.OrderRequest;
import com.digitfellas.ac.data.model.request.Products;
import com.digitfellas.ac.data.model.response.CreateOrderForDealer;
import com.digitfellas.ac.data.model.response.OrderConfirm;
import com.digitfellas.ac.data.model.response.createorder.Brand;
import com.digitfellas.ac.data.model.response.createorder.Category;
import com.digitfellas.ac.data.model.response.createorder.ProductCategory;
import com.digitfellas.ac.data.pref.PreferencesHelper;
import com.digitfellas.ac.gps.GPSTracker;
import com.digitfellas.ac.utility.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class AddOrderForDealerActivity extends BaseActivity implements ProductAdapter.ProductChangeCallBack {

    private static final String TAG = "AddOrderForCustomerActivity";

    public static int KEY_REQUEST = 123;

    @BindView(R.id.product_list)
    CustomMessageView product_List;
    @BindView(R.id.product_content)
    LinearLayout mProductContent;

    @BindView(R.id.delivery_date)
    TextView mDeliveryDate;
    @BindView(R.id.total_anmount)
    TextView mTotalAmount;

    @BindView(R.id.customer_name)
    Spinner mCustomerName;

    @BindView(R.id.business_category)
    Spinner mBusinessCategory;

    private ProductAdapter productAdapter;

    private CreateOrderForDealer response;
    private BusinessCategoryAdapter businessCategoryAdapter;
    private CustomerCategoryAdapter customerNameAapter;

    private List<Products> productsList = new ArrayList<>();

    private Menu menu;

    private boolean isFinish = true;

    private GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        setBackButtonEnabledAndTitle("Add Order");
        setButterKnife();

        gpsTracker = new GPSTracker(this);
        initViews();

        if (getIntent() != null) {

            isFinish = getIntent().getBooleanExtra("isfinish", true);
        }


//        showToast(gpsTracker.getLatitude() + "  " + gpsTracker.getLongitude());

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_cancel_menu, menu);

        this.menu = menu;
        showHideMenu(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.save:
//                showToast("Save");
                saveAllProduct();
                return true;

            case R.id.cancel:

                onBackPressed();

                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("LongLogTag")
    private void saveAllProduct() {


        if (productsList != null && productsList.size() > 0 && businessCategoryAdapter.getCount() > 0) {

//            Map userDetails = dataSource.getUserDetails();
//
//            long customer_id = (long) userDetails.get(PreferencesHelper.KEY_USER_ID);

            CreateOrderForDealer.Data.Customer customer = customerNameAapter.getItem(mCustomerName.getSelectedItemPosition());

            if (customer != null) {

                String deliver_date = mDeliveryDate.getText().toString();

                Category category = businessCategoryAdapter.getItem(mBusinessCategory.getSelectedItemPosition());

                int category_id = category.getId();

                OrderRequest orderRequest = new OrderRequest();

                orderRequest.setCategory(category_id);
                orderRequest.setCategory_name(category.getCategoryName());

                orderRequest.setCustomer(customer.getId());
                orderRequest.setCustomer_name(dataSource.getName());
                orderRequest.setDelivery_date(deliver_date);
                if (gpsTracker.canGetLocation()) {
                    gpsTracker.getLocation();
                    orderRequest.setLatitude(gpsTracker.getLatitude() + "");
                    orderRequest.setLongitude(gpsTracker.getLongitude() + "");

                } else {

                    Toast.makeText(getApplicationContext(), "No Location", Toast.LENGTH_SHORT).show();

                }

                orderRequest.setProductsList(productsList);

                Map<String, String> token = dataSource.getAuthendicate();
                showLoading();
                dataSource.saveCustomerOrder(token, orderRequest, new DataListener() {
                    @Override
                    public void onSuccess(Object object) {

                        AddOrderForDealerActivity.super.onSuccess(object);
                        OrderConfirm orderConfirm = (OrderConfirm) object;
                        if (orderConfirm.getSuccess()) {

                            showToast(orderConfirm.getMessage());

//                        onBackPressed();

                            if (isFinish) {
                                onBackPressed();
                            } else {

//                                Intent intent = new Intent();
//
//                                setResult(OpenFragment.KEY_REFRESH_RESULT_CODE, intent);
//                                finish();

                                Intent intent = new Intent(AddOrderForDealerActivity.this, DealerDashboardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                finish();
                                startActivity(intent);
                            }

                        } else {

                            showToast("Something went wrong, Please try again");
                        }


                    }

                    @Override
                    public void onFail(Throwable throwable) {
                        AddOrderForDealerActivity.super.onFail(throwable);
                    }

                    @Override
                    public void onNetworkFailure() {
                        AddOrderForDealerActivity.super.onNetworkFailure();
                    }
                });

                Log.e(TAG, "saveAllProduct: " + new Gson().toJson(orderRequest));
            } else {

                Log.e(TAG, "saveAllProduct: there is no customer");
            }


        } else {

            showToast("There is no product");
        }


    }


    private void showHideMenu(boolean isShow) {

        this.menu.findItem(R.id.save).setVisible(isShow);
        this.menu.findItem(R.id.cancel).setVisible(isShow);
//           supportInvalidateOptionsMenu();

    }


    private void initViews() {


//
        product_List.setLayoutManager(new LinearLayoutManager(this));
        product_List.setNestedScrollingEnabled(false);
        productAdapter = new ProductAdapter(this, productsList);
//
        product_List.setAdapter(productAdapter);

        product_List.showView(CustomMessageView.StateView.CONTENT);

        businessCategoryAdapter = new BusinessCategoryAdapter(this, new ArrayList<Category>());
        mBusinessCategory.setAdapter(businessCategoryAdapter);


        customerNameAapter = new CustomerCategoryAdapter(this, new ArrayList<CreateOrderForDealer.Data.Customer>());

        mCustomerName.setAdapter(customerNameAapter);

        mCustomerName.setEnabled(false);
        mBusinessCategory.setEnabled(false);
        mTotalAmount.setVisibility(View.GONE);
        setmMessageShow(false);
        callCreateOrderApi();

    }


    @SuppressLint("LongLogTag")
    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);

        response = (CreateOrderForDealer) object;

        Log.e(TAG, "onSuccess: CreateOrderResponse " + new Gson().toJson(response));

        if (response.getSuccess()) {

//            List<CreateOrderForDealer.Data.Customer> customerList=response.getData().getCustomers();

            CreateOrderForDealer.Data.Customer dealer = new CreateOrderForDealer.Data.Customer();
            Map userDetails = dataSource.getUserDetails();

            long dealer_id = (long) userDetails.get(PreferencesHelper.KEY_USER_ID);
            dealer.setId((int) dealer_id);
            dealer.setName(dataSource.getName());
            customerNameAapter.addAll(dealer);


            Log.e(TAG, "onSuccess: dealer details " + new Gson().toJson(dealer));

            List<Category> categoriesList = response.getData().getCategories();

            businessCategoryAdapter.updateCategory(categoriesList);


        } else {

            showToast("Something went wrong, Please try again");

        }


    }

    private void callCreateOrderApi() {

        Map<String, String> header = dataSource.getAuthendicate();

        showLoading();
        dataSource.createOrderForDealer(header, this);

    }


    @SuppressLint("LongLogTag")
    @OnClick({R.id.add_product_to_order, R.id.delivery_date})
    public void onCLick(View v) {

        switch (v.getId()) {


            case R.id.add_product_to_order:

//                Intent intent = new Intent(AddOrderForCustomerActivity.this, AddProductActivity.class);
//
//                intent.putExtra("bCategory", "");
//
//                intent.putExtra("productandbrand", (Parcelable) new Mobile());
//
//                startActivity(intent);

                if (businessCategoryAdapter.getCount() > 0) {

                    Category category = businessCategoryAdapter.getItem(mBusinessCategory.getSelectedItemPosition());


                    if (category != null && mDeliveryDate.getText().length() > 0 && response != null && response.getData() != null) {


                        List<ProductCategory> productCategoryList = response.getData().getProductCategories();

                        List<Brand> brandList = response.getData().getBrands();


//                        Log.e(TAG, "onCLick: "+new Gson().toJson(productCategoryList) );
//
//                        Log.e(TAG, "onCLick: "+new Gson().toJson(brandList) );


                        if (productCategoryList != null && brandList != null && brandList.size() > 0 && productCategoryList.size() > 0) {
                            Intent intent = new Intent(AddOrderForDealerActivity.this, AddProductActivity.class);

                            intent.putExtra("bCategory", (Parcelable) category);

                            intent.putParcelableArrayListExtra("productCategoryList", (ArrayList<? extends Parcelable>) productCategoryList);
                            intent.putParcelableArrayListExtra("brandList", (ArrayList<? extends Parcelable>) brandList);

                            startActivityForResult(intent, KEY_REQUEST);

                        } else {

                            showToast("There is no product or brand");

                        }

                    } else {
                        showToast("Please select the delivery date ");
                    }
                } else {
                    showToast("There is no business category to add product ");
                }


//                setmMessageShow(true);
                break;


            case R.id.delivery_date:

//
//                if (gpsTracker.canGetLocation()){
//
//                    gpsTracker.getLocation();
//
//                    showToast(gpsTracker.getLatitude() +"  "+gpsTracker.getLongitude());
//
//                }


                Utils.callDatePicker(getSupportFragmentManager(), new Utils.DateCallBack() {
                    @Override
                    public void showDate(String date) {
                        mDeliveryDate.setText(date);
                    }
                });

                break;

        }
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == KEY_REQUEST) {


//            Log.e(TAG, "onActivityResult: "+data.getData().toString() );
//            Log.e(TAG, "onActivityResult: "+data.getExtras().toString() );

            boolean isUpdated = data.getBooleanExtra("isUpdated", false);

            if (isUpdated) {

                int position = data.getIntExtra("position", 0);

                Products products = (Products) data.getSerializableExtra("products");

                updateProductAndView(products, position);

                Log.e(TAG, "onActivityResult: updating pos " + position);


            } else {

                Products products = (Products) data.getSerializableExtra("products");

                updateProductAndView(products);
            }


        }

    }

    private void updateProductAndView(Products products, int position) {


        productsList.set(position, products);

        productAdapter.updateItems(productsList);

        showTotalAmount();
    }

    private void updateProductAndView(Products products) {

        setmMessageShow(true);

        showHideMenu(true);

        productsList.add(products);

        productAdapter.updateItems(productsList);
        showTotalAmount();
    }

    @SuppressLint("LongLogTag")
    private void showTotalAmount() {

        if (productsList != null && productsList.size() > 0) {

            mTotalAmount.setVisibility(View.VISIBLE);


            Observable.just(productsList).map(new Function<List<Products>, Double>() {
                @Override
                public Double apply(List<Products> products) throws Exception {

                    double total = 0;

                    for (int i = 0; i < products.size(); i++) {

                        Products products1 = products.get(i);

                        total += products1.getSub_total();

                    }


                    return total;
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread()).subscribe(new Consumer<Double>() {
                @Override
                public void accept(Double aDouble) throws Exception {
                    mTotalAmount.setText("Total Amount is : " + aDouble);

                }
            });


        } else {
            mTotalAmount.setVisibility(View.GONE);
            Log.e(TAG, "showTotalAmount:productsList is empty ");
        }


    }

    private void setmMessageShow(boolean isShow) {

        if (isShow) {
            product_List.showContent();
        } else {
            product_List.setMessageDescription("There is no product added");
            product_List.setMessageTitle("No Product");
            product_List.showMessage();
        }

    }

    @Override
    public void deleteProduct(int position) {

        if (productsList != null && productsList.size() > 0) {

            productsList.remove(position);

            productAdapter.updateItems(productsList);

            showTotalAmount();

        }

        if (productsList.size() <= 0) {

            showHideMenu(false);
            setmMessageShow(false);

        }
    }

    @Override
    public void editProduct(int position, Products products) {

        Intent intent = new Intent(AddOrderForDealerActivity.this, EditProductActivity.class);

        intent.putExtra("products", products);
        intent.putExtra("position", position);
        Category category = businessCategoryAdapter.getItem(mBusinessCategory.getSelectedItemPosition());
        intent.putExtra("bCategory", category);

        startActivityForResult(intent, KEY_REQUEST);


    }
}
