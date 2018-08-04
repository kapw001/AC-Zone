package com.digitfellas.ac.data.remote;

import com.digitfellas.ac.data.listener.DataListener;
import com.digitfellas.ac.data.model.request.Deviceinfo;
import com.digitfellas.ac.data.model.request.Login;
import com.digitfellas.ac.data.model.request.OrderRequest;
import com.digitfellas.ac.data.model.request.Register;
import com.digitfellas.ac.data.model.request.StockProductInfoReq;
import com.digitfellas.ac.data.model.request.StockProductSearchReq;
import com.digitfellas.ac.data.model.request.TaskPost;
import com.digitfellas.ac.data.model.response.ProductInfo;
import com.digitfellas.ac.data.model.response.ProductSearch;
import com.digitfellas.ac.data.model.response.StockProductSearch;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.HeaderMap;
import retrofit2.http.Url;

/**
 * Created by yasar on 22/3/18.
 */

public interface RemoteDataSource {


    void login(String json, DataListener dataListener);

    void getUserProfile(@HeaderMap Map<String, String> map, DataListener dataListener);

    void register(String json, DataListener dataListener);

    void createOrder(@HeaderMap Map<String, String> map, DataListener dataListener);

    void createOrderForDealer(@HeaderMap Map<String, String> map, DataListener dataListener);

    void searchProduc(String json, DataListener dataListener);

    Call<ProductSearch> searchProductDirect(@HeaderMap Map<String, String> headermap, String json);

    void getProductInfo(@HeaderMap Map<String, String> headermap, String json, DataListener dataListener);

    void saveCustomerOrder(@HeaderMap Map<String, String> headermap, OrderRequest json, DataListener dataListener);

    void loadMyOrder(@HeaderMap Map<String, String> headermap, String page, String status, DataListener dataListener);

    void getOrderDetails(@HeaderMap Map<String, String> headermap, int page, DataListener dataListener);

    Call<StockProductSearch> stockProductSearch(@HeaderMap Map<String, String> headermap, StockProductSearchReq json);

    void stockProductInfo(@HeaderMap Map<String, String> headermap, StockProductInfoReq json, DataListener dataListener);

    void loadAnnouncement(@HeaderMap Map<String, String> headermap, DataListener dataListener);

    void loadAnnouncementDetails(@HeaderMap Map<String, String> headermap, int id, DataListener dataListener);

    void loadDownloads(@HeaderMap Map<String, String> headermap, DataListener dataListener);

    Call<ResponseBody> downloadFileWithDynamicUrlAsync(@Url String fileUrl);

    void createService(@HeaderMap Map<String, String> headermap, DataListener dataListener);

    void postService(@HeaderMap Map<String, String> headermap, String json, DataListener dataListener);

    void getAllServicesForCustomer(@HeaderMap Map<String, String> headermap, int page, int status, DataListener dataListener);

    void getServiceDetailsForCustomer(@HeaderMap Map<String, String> headermap, int id, DataListener dataListener);

    void getServiceDetailsForServiceMan(@HeaderMap Map<String, String> headermap, int id, DataListener dataListener);

    void changeServiceManStatus(@HeaderMap Map<String, String> headermap, int id, String json, DataListener dataListener);

    void postComments(@HeaderMap Map<String, String> headermap, String json, DataListener dataListener);

    void getTask(@HeaderMap Map<String, String> headermap, DataListener dataListener);

    void postTask(@HeaderMap Map<String, String> headermap, TaskPost json, DataListener dataListener);

    void getAllTask(@HeaderMap Map<String, String> headermap, int page, int status, DataListener dataListener);

    void getTaskDetails(@HeaderMap Map<String, String> headermap, int id, DataListener dataListener);

    void postDeviceInfo(@HeaderMap Map<String, String> headermap, Deviceinfo json, DataListener dataListener);

    void logout(@HeaderMap Map<String, String> headermap, DataListener dataListener);

}
