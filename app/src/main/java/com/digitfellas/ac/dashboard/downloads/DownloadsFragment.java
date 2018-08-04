package com.digitfellas.ac.dashboard.downloads;


import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;

import com.digitfellas.ac.BuildConfig;
import com.digitfellas.ac.R;
import com.digitfellas.ac.base.BaseAdapter;
import com.digitfellas.ac.base.BaseFragment;
import com.digitfellas.ac.customview.CustomMessageView;
import com.digitfellas.ac.dashboard.anouncement.AnouncementAdapter;
import com.digitfellas.ac.dashboard.anouncement.AnouncementFragment;
import com.digitfellas.ac.dashboard.webview.WebViewActivity;
import com.digitfellas.ac.data.listener.DataListener;
import com.digitfellas.ac.data.model.response.Anouncement;
import com.digitfellas.ac.data.model.response.Downloads;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadsFragment extends BaseFragment implements BaseAdapter.OnItemClick<Downloads.Datum> {

    private static final String TAG = "DownloadsFragment";

    @BindView(R.id.downloads_listview)
    CustomMessageView mDownloadListview;

    private DownloadsAdapter mDownloadsAdapter;

    public DownloadsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_downloads, container, false);
        setButterKnife(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
    }


    private void initViews() {

        mDownloadListview.setLayoutManager(new LinearLayoutManager(getContext()));
        mDownloadListview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mDownloadsAdapter = new DownloadsAdapter(this, new ArrayList<Downloads.Datum>());
        mDownloadListview.setAdapter(mDownloadsAdapter);

        mDownloadListview.showContent();

        loadDownloads();

    }


    private void loadDownloads() {


        showLoading();

        dataSource.loadDownloads(dataSource.getAuthendicate(), new DataListener() {
            @Override
            public void onSuccess(Object object) {

                DownloadsFragment.super.onSuccess(object);

                Downloads downloads = (Downloads) object;

                if (downloads.getSuccess()) {

                    List<Downloads.Datum> datumList = downloads.getData();

                    if (datumList != null && datumList.size() > 0) {

                        mDownloadsAdapter.updateList(datumList);

                        Log.e(TAG, "onSuccess: " + new Gson().toJson(datumList));

                    } else {

                        noAnnouncement("No Data", "There is no downloads available");

                    }

                } else {
                    noAnnouncement("No Data", "There is no downloads available");
                }

            }

            @Override
            public void onFail(Throwable throwable) {
                DownloadsFragment.super.onFail(throwable);

                noAnnouncement("Error", "Something went wrong \nPlease try again");
            }

            @Override
            public void onNetworkFailure() {

                DownloadsFragment.super.onNetworkFailure();

                noAnnouncement("No Internet", "There is no internet");

            }
        });


    }

    private void noAnnouncement(String title, String des) {

        mDownloadListview.setMessageTitle(title);
        mDownloadListview.setMessageDescription(des);

        mDownloadListview.showMessage();

    }


    @Override
    public void OnItemClickListener(View view, Downloads.Datum datum, int postition) {

//        Log.e(TAG, "OnItemClickListener: " + new Gson().toJson(datum));
//
        String url = datum.getFile();
//
//        String ex = url.substring(url.length() - 4);
//
//        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + datum.getName() + ex;
//        File imageFile = new File(path);
//
//        Log.e(TAG, "OnItemClickListener: " + imageFile.toString());
//
////        String path=getActivity().getExternalCacheDir();
////
////
//        if (!imageFile.exists()) {
//            DownloadManager downloadmanager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
//            Uri uri = Uri.parse(url);
//            DownloadManager.Request request = new DownloadManager.Request(uri);
//            request.setTitle(datum.getName());
//            request.setDescription("Downloading");
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//            request.setDestinationUri(Uri.parse("file://" + imageFile.toString()));
//            downloadmanager.enqueue(request);
//        } else {
//
//            Intent intent = new Intent();
//            intent.setAction(android.content.Intent.ACTION_VIEW);
//            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider",imageFile);
////            intent.setDataAndType(Uri.fromFile(imageFile), getMimeType(imageFile.getAbsolutePath()));
//            intent.setDataAndType(uri,getMimeType(imageFile.getAbsolutePath()));
//            startActivity(intent);
//        }


        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        } catch (ActivityNotFoundException e) {


            Intent intent = new Intent(getContext(), WebViewActivity.class);

            intent.putExtra("url", url);

            startActivity(intent);


//            showToast("There is no application to handle it");

//
//            String ex = url.substring(url.length() - 4);
//
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + datum.getName() + ex;
//            File imageFile = new File(path);
//
//            Log.e(TAG, "OnItemClickListener: " + imageFile.toString());

//        String path=getActivity().getExternalCacheDir();
//
//
//            if (!imageFile.exists()) {
//                DownloadManager downloadmanager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
//                Uri uri = Uri.parse(url);
//                DownloadManager.Request request = new DownloadManager.Request(uri);
//                request.setTitle(datum.getName());
//                request.setDescription("Downloading");
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                request.setDestinationUri(Uri.parse("file://" + imageFile.toString()));
//                downloadmanager.enqueue(request);
//            } else {
//                try {
//                    Intent intent = new Intent();
//                    intent.setAction(android.content.Intent.ACTION_VIEW);
//                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", imageFile);
////            intent.setDataAndType(Uri.fromFile(imageFile), getMimeType(imageFile.getAbsolutePath()));
//                    intent.setDataAndType(uri, getMimeType(imageFile.getAbsolutePath()));
//                    startActivity(intent);
//                } catch (ActivityNotFoundException ee) {
//                    showToast("There is no application to handle it");
//                }
//            }

        }


//        showLoading();
//        dataSource.downloadFileWithDynamicUrlAsync(url).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                hideLoading();
//
//                if (response.isSuccessful()){
//
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//hideLoading();
//            }
//        });
//


    }

    private String getMimeType(String url) {
        String parts[] = url.split("\\.");
        String extension = parts[parts.length - 1];
        String type = null;
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }
}
