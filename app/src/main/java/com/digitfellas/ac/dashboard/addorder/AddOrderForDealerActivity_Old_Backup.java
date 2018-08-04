package com.digitfellas.ac.dashboard.addorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.digitfellas.ac.R;
import com.digitfellas.ac.adapter.viewpager.BusinessCategoryAdapter;
import com.digitfellas.ac.base.BaseActivity;
import com.digitfellas.ac.customview.CustomMessageView;
import com.digitfellas.ac.dashboard.addproduct.AddProductActivity;
import com.digitfellas.ac.data.model.request.Products;
import com.digitfellas.ac.data.model.response.createorder.Category;
import com.digitfellas.ac.data.model.response.createorder.CreateOrderResponse;
import com.digitfellas.ac.utility.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class AddOrderForDealerActivity_Old_Backup extends BaseActivity implements ProductAdapter.ProductChangeCallBack {

    private static final String TAG = "AddOrderForCustomerActivity";

    @BindView(R.id.product_list)
    CustomMessageView product_List;
    @BindView(R.id.product_content)
    LinearLayout mProductContent;

    @BindView(R.id.delivery_date)
    TextView mDeliveryDate;

    @BindView(R.id.customer_name)
    Spinner mCustomerName;

    @BindView(R.id.business_category)
    Spinner mBusinessCategory;

    private ProductAdapter productAdapter;

    private CreateOrderResponse response;
    private BusinessCategoryAdapter businessCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        setBackButtonEnabledAndTitle("Add Order");
        setButterKnife();

        initViews();
//        mCustomerName.setEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_cancel_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.save:
                showToast("Save");
                return true;

            case R.id.cancel:
                showToast("Cancel");
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private void initViews(){




//
        product_List.setLayoutManager(new LinearLayoutManager(this));
        product_List.setNestedScrollingEnabled(false);
        productAdapter=new ProductAdapter(this, new ArrayList<Products>());
//
        product_List.setAdapter(productAdapter);

        product_List.showView(CustomMessageView.StateView.CONTENT);

        businessCategoryAdapter=new BusinessCategoryAdapter(this,new ArrayList<Category>());
        mBusinessCategory.setAdapter(businessCategoryAdapter);

//        mBusinessCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//
//              Category category=  businessCategoryAdapter.getItem(mBusinessCategory.getSelectedItemPosition());
//
//                Log.e(TAG, "onItemSelected: "+category.getCategoryName()+"   " +category.getId());
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });



        callCreateOrderApi();

    }


    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);

        response= (CreateOrderResponse) object;

        if (response.getSuccess()){


            List<Category> categoriesList=response.getData().getCategories();

//            Log.e(TAG, "onSuccess: "+new Gson().toJson(categoriesList));

            businessCategoryAdapter.updateCategory(categoriesList);









        }else {

            showToast("Something went wrong, Please try again");

        }


    }

    private void callCreateOrderApi(){

        Map<String, String> header=dataSource.getAuthendicate();

        dataSource.createOrder(header, this);

    }



    @OnClick({R.id.add_product_to_order,R.id.delivery_date})
    public void onCLick(View v){

        switch (v.getId()){


            case R.id.add_product_to_order:

                Category category=  businessCategoryAdapter.getItem(mBusinessCategory.getSelectedItemPosition());

                if (category!=null && mDeliveryDate.getText().length()>0){

                    startActivity(new Intent(AddOrderForDealerActivity_Old_Backup.this, AddProductActivity.class));

                }else {
                    showToast("Please select the delivery date ");
                }



//                setmMessageShow(true);
                break;


            case R.id.delivery_date:

                Utils.callDatePicker(getSupportFragmentManager(), new Utils.DateCallBack() {
                    @Override
                    public void showDate(String date) {
                        mDeliveryDate.setText(date);
                    }
                });

                break;

        }
    }







    private void setmMessageShow(boolean isShow){

        if (isShow){
            product_List.showContent();
        }else {
            product_List.showMessage();
        }

    }

    @Override
    public void deleteProduct(int position) {

    }

    @Override
    public void editProduct(int position, Products products) {

    }
}
