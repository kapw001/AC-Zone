package com.digitfellas.ac.dashboard.addproduct;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.digitfellas.ac.R;
import com.digitfellas.ac.adapter.viewpager.BrandCategoryAdapter;
import com.digitfellas.ac.adapter.viewpager.ProductCategoryAdapter;
import com.digitfellas.ac.base.BaseActivity;
import com.digitfellas.ac.customview.ProductQuantity;
import com.digitfellas.ac.dashboard.addorder.AddOrderForCustomerActivity;
import com.digitfellas.ac.dashboard.checkstock.SampleSearchModel;
import com.digitfellas.ac.data.listener.DataListener;
import com.digitfellas.ac.data.model.UserModel;
import com.digitfellas.ac.data.model.request.Products;
import com.digitfellas.ac.data.model.response.ProductInfo;
import com.digitfellas.ac.data.model.response.ProductSearch;
import com.digitfellas.ac.data.model.response.createorder.Ac;
import com.digitfellas.ac.data.model.response.createorder.Brand;
import com.digitfellas.ac.data.model.response.createorder.Category;
import com.digitfellas.ac.data.model.response.createorder.Mobile;
import com.digitfellas.ac.data.model.response.createorder.ProductCategory;
import com.digitfellas.ac.utility.Utils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseFilter;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;

public class AddProductActivity extends BaseActivity {

    private static final String TAG = "AddProductActivity";
    @BindView(R.id.product_search)
    TextView mProductSearch;

    @BindView(R.id.sku)
    TextView mSKU;
    @BindView(R.id.model_number)
    TextView mModelNumber;
    @BindView(R.id.price)
    TextView mPrice;
    @BindView(R.id.dealer_price)
    TextView mDealePrice;
    @BindView(R.id.quantity)
    ProductQuantity mQuantity;
    @BindView(R.id.subtotal)
    TextView mSubtotal;

    @BindView(R.id.layout_product_info)
    LinearLayout mProductInfoLayout;


    @BindView(R.id.product_category)
    Spinner mProductCategory;

    @BindView(R.id.brand)
    Spinner mBrand;


    private ProductCategoryAdapter productCategoryAdapter;
    private BrandCategoryAdapter brandCategoryAdapter;

    private Menu menu;

    private Category category;
    private ProductInfo productInfo;

    private List<ProductCategory> productCategoryList;
    private List<Brand> brandList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        setBackButtonEnabledAndTitle("Add Product");
        setButterKnife();
        initViews();


        if (dataSource.getRoleType().toLowerCase().equalsIgnoreCase("customer")){
            Object o = getIntent().getParcelableExtra("productandbrand");
            category = getIntent().getParcelableExtra("bCategory");
            Log.e(TAG, "onCreate: " + new Gson().toJson(o));

            setProductAndBrand(o);
        }else {

            category = getIntent().getParcelableExtra("bCategory");
            productCategoryList=getIntent().getParcelableArrayListExtra("productCategoryList");
            brandList=getIntent().getParcelableArrayListExtra("brandList");


//            Log.e(TAG, "onCLick: "+new Gson().toJson(productCategoryList) );
//
//            Log.e(TAG, "onCLick: "+new Gson().toJson(brandList) );



            setProductAndBrand(productCategoryList,brandList);
        }




    }


    private void initViews() {


        productCategoryAdapter = new ProductCategoryAdapter(this, new ArrayList<ProductCategory>());
        mProductCategory.setAdapter(productCategoryAdapter);

        brandCategoryAdapter = new BrandCategoryAdapter(this, new ArrayList<Brand>());
        mBrand.setAdapter(brandCategoryAdapter);

        mProductInfoLayout.setVisibility(View.GONE);

    }


    private void setProductAndBrand(List<ProductCategory> p,List<Brand> b) {

        if (p != null && b!=null && p.size()>0 && b.size()>0) {

            productCategoryAdapter.upadetProduct(p);
            brandCategoryAdapter.updateBrand(b);


        }else {

            Log.e(TAG, "setProductAndBrand: something went wrong or null object ");
        }


    }


    private void setProductAndBrand(Object o) {

        if (o != null && o instanceof Mobile) {

            Mobile mobile = (Mobile) o;

            productCategoryAdapter.upadetProduct(mobile.getProductCategories());
            brandCategoryAdapter.updateBrand(mobile.getBrands());


        } else if (o != null && o instanceof Ac) {

            Ac ac = (Ac) o;
            productCategoryAdapter.upadetProduct(ac.getProductCategories());
            brandCategoryAdapter.updateBrand(ac.getBrands());
        } else {

            Log.e(TAG, "setProductAndBrand: something went wrong or null object ");
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_cancel_menu, menu);

        this.menu = menu;
        showHideMenu(false);
        return super.onCreateOptionsMenu(menu);
    }

    private void showHideMenu(boolean isShow) {

        this.menu.findItem(R.id.save).setVisible(isShow);
        this.menu.findItem(R.id.cancel).setVisible(isShow);
//           supportInvalidateOptionsMenu();

    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//
//
//        return super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.save:

                saveProducts();
                return true;

            case R.id.cancel:
                showToast("Cancel");

                super.onBackPressed();

                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private void saveProducts() {
        int quantity = mQuantity.getQuantity();

        if (productInfo!=null) {

            if (quantity <= mQuantity.getMaxQuantity()) {

                ProductCategory productCategory = productCategoryAdapter.getItem(mProductCategory.getSelectedItemPosition());
                Brand brand = brandCategoryAdapter.getItem(mBrand.getSelectedItemPosition());



                String sku = mSKU.getText().toString();
                String model_name = mModelNumber.getText().toString();
                Double price = Double.valueOf(mPrice.getText().toString());

                Double dealer_price = Double.valueOf(mDealePrice.getText().toString());
                Double subTotal = Double.valueOf(mSubtotal.getText().toString());

                Products products = new Products();

                products.setSearchProductName(mProductSearch.getText().toString());

                products.setProductCategory(productCategory);
                products.setBrand(brand);

                products.setProductCategorySelectedPosition(mProductCategory.getSelectedItemPosition());

                products.setBrandSelectedPosition(mBrand.getSelectedItemPosition());

                products.setProduct_id(productInfo.getData().getId());

//            products.setProductCategory(productCategory);
                products.setProduct_category_name(productCategory.getCategoryName());
                products.setProduct_category_id(productCategory.getId());

//            products.setBrand(brand);
                products.setBrand_name(brand.getBrandName());
                products.setBrand_id(brand.getId());

                products.setSku(sku);
                products.setModel_name(model_name);
                products.setPrice(price);
                products.setQuantity(quantity);
                products.setDealer_price(dealer_price);
                products.setSub_total(subTotal);

                products.setMaxQuantity(mQuantity.getMaxQuantity());
                Intent intent = new Intent();
                intent.putExtra("products", products);
                intent.putExtra("isUpdated",false);
                setResult(AddOrderForCustomerActivity.KEY_REQUEST, intent);
                finish();

                showToast("Product is added");

            } else {
                showToast("Please reduce the quantity ");
            }
        }else showToast("There is no product info, Please try again");


    }


    @OnClick({R.id.product_search, R.id.get_product_info})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.product_search:
                searchProduct();
                break;

            case R.id.get_product_info:

                if (mProductSearch.getText().length() > 0) {

                    getProductDetails(mProductSearch.getText().toString());

                } else {

                    showToast("There is no product ");
                }

                break;

        }

    }


    private void getProductDetails(String values) {


        Map<String, String> token = dataSource.getAuthendicate();

        Map<String, Object> params = new HashMap<>();

        params.put("product", values);

        showLoading();

        dataSource.getProductInfo(token, new JSONObject(params).toString(), this);


    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);

        productInfo = (ProductInfo) object;

        if (productInfo.getSuccess()) {

            mProductInfoLayout.setVisibility(View.VISIBLE);
            showHideMenu(true);
            ProductInfo.Data data = productInfo.getData();

            mSKU.setText(data.getSku());
            mModelNumber.setText(data.getModelNumber());
            mPrice.setText(data.getPrice());
            mDealePrice.setText(data.getStarRatingPrice());
            mSubtotal.setText(data.getPrice());

            final float price;

            if (category.getCategoryName().toLowerCase().equalsIgnoreCase("mobile")) {
                price = Float.parseFloat(data.getStarRatingPrice());
            } else {
                price = Float.parseFloat(data.getPrice());
            }
            mSubtotal.setText(price+"");
            mQuantity.setMaxQuantity(Integer.parseInt(data.getQuantity()));
            mQuantity.setQuantity(1);
            mQuantity.setmQuantityCallBack(new ProductQuantity.QuantityCallBack() {
                @Override
                public void quantityCount(int q) {


                    float subtotal = q * price;

                    mSubtotal.setText(subtotal + "");


                }
            });

        } else {

            showToast("Something went wrong, Please try again");

        }


    }

    private void searchProduct() {

        if (productCategoryAdapter.getCount() > 0 && brandCategoryAdapter.getCount() > 0) {

            final ProductCategory productCategory = productCategoryAdapter.getItem(mProductCategory.getSelectedItemPosition());

            final Brand brand = brandCategoryAdapter.getItem(mBrand.getSelectedItemPosition());


            final Map<String, String> token = dataSource.getAuthendicate();

            final SimpleSearchDialogCompat<ProductSearch.Datum> searchDialog =
                    new SimpleSearchDialogCompat(AddProductActivity.this, "Search...",
                            "What are you looking for...?", null, new ArrayList(),
                            new SearchResultListener<Searchable>() {
                                @Override
                                public void onSelected(BaseSearchDialogCompat dialog,
                                                       Searchable item, int position) {
//                                    Toast.makeText(AddProductActivity.this, item.getTitle(),
//                                            Toast.LENGTH_SHORT).show();

                                    mProductSearch.setText(item.getTitle());

                                    dialog.dismiss();
                                }
                            });
            BaseFilter apiFilter = new BaseFilter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    doBeforeFiltering();
                    FilterResults results = new FilterResults();
                    results.values = new ArrayList<ProductSearch.Datum>();
                    results.count = 0;
                    try {

                        Map<String, Object> searchParams = new HashMap<>();

                        searchParams.put("term", charSequence);
                        searchParams.put("category", category.getId());
                        searchParams.put("subcategory", productCategory.getId());
                        searchParams.put("brand", brand.getId());

                        ProductSearch users = dataSource.searchProductDirect(token, new JSONObject(searchParams).toString())
                                .execute()
                                .body();
                        if (users != null) {
                            results.values = users.getData();
                            results.count = users.getData().size();
                        }
                        return results;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    if (filterResults != null) {
                        ArrayList<ProductSearch.Datum> filtered = (ArrayList<ProductSearch.Datum>) filterResults.values;
                        if (filtered != null)
                            searchDialog.getFilterResultListener().onFilter(filtered);
                        doAfterFiltering();
                    }
                }
            };


            searchDialog.setFilter(apiFilter);
            searchDialog.show();

        } else {

            showDialog("Error", "Something went wrong \nThere is no product or brand available");

        }


    }
}
