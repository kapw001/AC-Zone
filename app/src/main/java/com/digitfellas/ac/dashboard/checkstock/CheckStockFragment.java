package com.digitfellas.ac.dashboard.checkstock;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.digitfellas.ac.R;
import com.digitfellas.ac.base.BaseFragment;
import com.digitfellas.ac.customview.TextV;
import com.digitfellas.ac.dashboard.addproduct.AddProductActivity;
import com.digitfellas.ac.data.listener.DataListener;
import com.digitfellas.ac.data.model.request.StockProductInfoReq;
import com.digitfellas.ac.data.model.request.StockProductSearchReq;
import com.digitfellas.ac.data.model.response.ProductSearch;
import com.digitfellas.ac.data.model.response.StockProductInfo;
import com.digitfellas.ac.data.model.response.StockProductSearch;
import com.digitfellas.ac.utility.Utils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseFilter;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckStockFragment extends BaseFragment {


    @BindView(R.id.product_sku_search)
    TextView product_sku_search;

    @BindView(R.id.layout_stock)
    LinearLayout mLinearLayout;
    @BindView(R.id.product)
    TextView mProduct;
    @BindView(R.id.sku)
    TextView mSku;
    @BindView(R.id.stock)
    TextView mStock;

    @BindView(R.id.msg)
    TextView mMsg;

    public CheckStockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_check_stock, container, false);

        setButterKnife(this,view);

        mLinearLayout.setVisibility(View.GONE);
        mMsg.setVisibility(View.VISIBLE);

        return view;
    }


    @OnClick({R.id.product_sku_search,R.id.product_search})
    public void onClick(View v){


        switch (v.getId()){

            case R.id.product_sku_search:
                productSkuSearch();
                break;

            case R.id.product_search:
                productInfo();
                break;


        }



    }



    private void productSkuSearch(){



        final Map<String, String> token = dataSource.getAuthendicate();

        final SimpleSearchDialogCompat<StockProductSearch.Datum> searchDialog =
                new SimpleSearchDialogCompat(getContext(), "Search...",
                        "What are you looking for...?", null, new ArrayList(),
                        new SearchResultListener<Searchable>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog,
                                                   Searchable item, int position) {
//                                    Toast.makeText(AddProductActivity.this, item.getTitle(),
//                                            Toast.LENGTH_SHORT).show();

                                product_sku_search.setText(item.getTitle());

                                dialog.dismiss();
                            }
                        });
        BaseFilter apiFilter = new BaseFilter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                doBeforeFiltering();
                FilterResults results = new FilterResults();
                results.values = new ArrayList<StockProductSearch.Datum>();
                results.count = 0;
                try {

                    StockProductSearchReq stockProductSearch=new StockProductSearchReq();
                    stockProductSearch.term=charSequence.toString();

                    StockProductSearch users = dataSource.stockProductSearch(token,stockProductSearch)
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
                    ArrayList<StockProductSearch.Datum> filtered = (ArrayList<StockProductSearch.Datum>) filterResults.values;
                    if (filtered != null)
                        searchDialog.getFilterResultListener().onFilter(filtered);
                    doAfterFiltering();
                }
            }
        };


        searchDialog.setFilter(apiFilter);
        searchDialog.show();



    }


    private void productInfo(){


        if (product_sku_search.getText()!=null && product_sku_search.getText().length()>0){

            final Map<String, String> token = dataSource.getAuthendicate();

            StockProductInfoReq stockProductInfoReq=new StockProductInfoReq();
            stockProductInfoReq.search=product_sku_search.getText().toString();

            showLoading();
            dataSource.stockProductInfo(token, stockProductInfoReq, new DataListener() {
                @Override
                public void onSuccess(Object object) {
                    CheckStockFragment.super.onSuccess(object);

                    StockProductInfo stockProductInfo= (StockProductInfo) object;

                    if (stockProductInfo.getSuccess()){

                        mLinearLayout.setVisibility(View.VISIBLE);
                        mMsg.setVisibility(View.GONE);

                        StockProductInfo.Datum data=stockProductInfo.getData().get(0);

                        mProduct.setText(data.getProductName()+"");
                        mSku.setText(data.getSku()+"");
                        mStock.setText(data.getQuantity()+"");


                    }else showToast("Something went wrong");


                }

                @Override
                public void onFail(Throwable throwable) {

                    CheckStockFragment.super.onFail(throwable);

                }

                @Override
                public void onNetworkFailure() {
                    CheckStockFragment.super.onNetworkFailure();
                }
            });



        }else showToast("There is no product to search");





    }



}
