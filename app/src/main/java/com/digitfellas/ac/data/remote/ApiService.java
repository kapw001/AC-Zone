package com.digitfellas.ac.data.remote;


import com.digitfellas.ac.data.model.UserModel;
import com.digitfellas.ac.data.model.request.Deviceinfo;
import com.digitfellas.ac.data.model.request.Login;
import com.digitfellas.ac.data.model.request.OrderRequest;
import com.digitfellas.ac.data.model.request.Register;
import com.digitfellas.ac.data.model.request.StockProductInfoReq;
import com.digitfellas.ac.data.model.request.StockProductSearchReq;
import com.digitfellas.ac.data.model.request.TaskPost;
import com.digitfellas.ac.data.model.response.Anouncement;
import com.digitfellas.ac.data.model.response.AnouncementDetails;
import com.digitfellas.ac.data.model.response.CreateOrderForDealer;
import com.digitfellas.ac.data.model.response.CreateService;
import com.digitfellas.ac.data.model.response.CustomerServices;
import com.digitfellas.ac.data.model.response.CustomerServicesDetails;
import com.digitfellas.ac.data.model.response.Downloads;
import com.digitfellas.ac.data.model.response.LoginResponse;
import com.digitfellas.ac.data.model.response.Logout;
import com.digitfellas.ac.data.model.response.MyOrderResponse;
import com.digitfellas.ac.data.model.response.OrderConfirm;
import com.digitfellas.ac.data.model.response.OrderDetailsInfo;
import com.digitfellas.ac.data.model.response.PostedServicesRes;
import com.digitfellas.ac.data.model.response.ProductInfo;
import com.digitfellas.ac.data.model.response.ProductSearch;
import com.digitfellas.ac.data.model.response.RegisterResponse;
import com.digitfellas.ac.data.model.response.ServiceManServicesDetails;
import com.digitfellas.ac.data.model.response.StockProductInfo;
import com.digitfellas.ac.data.model.response.StockProductSearch;
import com.digitfellas.ac.data.model.response.TaskAdd;
import com.digitfellas.ac.data.model.response.Tasks;
import com.digitfellas.ac.data.model.response.TasksDetails;
import com.digitfellas.ac.data.model.response.UserDetailsResponse;
import com.digitfellas.ac.data.model.response.createorder.CreateOrderResponse;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by yasar on 6/3/18.
 */

public interface ApiService {

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("auth/login")
    Observable<LoginResponse> login(@Body String json);

    @GET("user")
    Observable<UserDetailsResponse> getUserProfile(@HeaderMap Map<String, String> headermap);


    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("auth/signup")
    Observable<RegisterResponse> register(@Body String json);

    @GET("orders/create")
    Observable<CreateOrderResponse> createOrder(@HeaderMap Map<String, String> headermap);

    @GET("orders/create")
    Observable<CreateOrderForDealer> createOrderForDealer(@HeaderMap Map<String, String> headermap);


    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("productsearch")
    Observable<String> searchProduct(@Body String json);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("productsearch")
    Call<ProductSearch> searchProductDirect(@HeaderMap Map<String, String> headermap, @Body String json);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("productsearchinfo")
    Observable<ProductInfo> getProductInfo(@HeaderMap Map<String, String> headermap, @Body String json);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("orders")
    Observable<OrderConfirm> saveCustomerOrder(@HeaderMap Map<String, String> headermap, @Body OrderRequest json);

    @GET("orders")
    Observable<MyOrderResponse> loadMyOrder(@HeaderMap Map<String, String> headermap, @Query("page") String page, @Query("status") String status);

    @GET("orders/{id}")
    Observable<OrderDetailsInfo> getOrderDetails(@HeaderMap Map<String, String> headermap, @Path("id") int id);


    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("productstocksearch")
    Call<StockProductSearch> stockProductSearch(@HeaderMap Map<String, String> headermap, @Body StockProductSearchReq json);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("productstock")
    Observable<StockProductInfo> stockProductInfo(@HeaderMap Map<String, String> headermap, @Body StockProductInfoReq json);

    @GET("announcements")
    Observable<Anouncement> loadAnnouncement(@HeaderMap Map<String, String> headermap);

    @GET("announcements/{id}")
    Observable<AnouncementDetails> loadAnnouncementDetails(@HeaderMap Map<String, String> headermap, @Path("id") int id);

    @GET("downloads")
    Observable<Downloads> loadDownloads(@HeaderMap Map<String, String> headermap);

    @Streaming
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlAsync(@Url String fileUrl);

    @GET("services/create")
    Observable<CreateService> createService(@HeaderMap Map<String, String> headermap);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("services")
    Observable<PostedServicesRes> postService(@HeaderMap Map<String, String> headermap, @Body String json);

    @GET("services")
    Observable<CustomerServices> getAllServicesForCustomer(@HeaderMap Map<String, String> headermap, @Query("page") int page, @Query("status") int status);

    @GET("services/{id}")
    Observable<CustomerServicesDetails> getServiceDetailsForCustomer(@HeaderMap Map<String, String> headermap, @Path("id") int id);

    @GET("services/{id}")
    Observable<ServiceManServicesDetails> getServiceDetailsForServiceMan(@HeaderMap Map<String, String> headermap, @Path("id") int id);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @PUT("services/{id}")
    Observable<String> changeServiceManStatus(@HeaderMap Map<String, String> headermap, @Path("id") int id, @Body String json);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("comment")
    Observable<String> postComments(@HeaderMap Map<String, String> headermap, @Body String json);

    @GET("tasks/create")
    Observable<TaskAdd> getTask(@HeaderMap Map<String, String> headermap);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("tasks")
    Observable<String> postTask(@HeaderMap Map<String, String> headermap, @Body TaskPost json);

    @GET("tasks")
    Observable<Tasks> getAllTask(@HeaderMap Map<String, String> headermap, @Query("page") int page, @Query("status") int status);

    @GET("tasks/{id}")
    Observable<TasksDetails> getTaskDetails(@HeaderMap Map<String, String> headermap, @Path("id") int id);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("deviceinfo")
    Observable<String> postDeviceInfo(@HeaderMap Map<String, String> headermap, @Body Deviceinfo json);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("logout")
    Observable<Logout> logout(@HeaderMap Map<String, String> headermap);


}
