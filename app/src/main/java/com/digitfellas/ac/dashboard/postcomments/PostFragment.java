package com.digitfellas.ac.dashboard.postcomments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.digitfellas.ac.R;
import com.digitfellas.ac.data.DataRepository;
import com.digitfellas.ac.data.DataSource;
import com.digitfellas.ac.data.listener.DataListener;
import com.digitfellas.ac.data.pref.Pref;
import com.digitfellas.ac.data.pref.PreferencesHelper;
import com.digitfellas.ac.data.remote.ApiService;
import com.digitfellas.ac.data.remote.RemoteDataSourceHelper;
import com.digitfellas.ac.data.retrofitclient.ApiEndPoint;
import com.digitfellas.ac.data.retrofitclient.RetrofitClient;
import com.digitfellas.ac.utility.ProgressUtils;
import com.digitfellas.ac.utility.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends BottomSheetDialogFragment implements DataListener {

    private Pref pref;
    private RetrofitClient retrofitClient;
    private ApiService apiService;
    private RemoteDataSourceHelper remoteDataSource;
    public DataSource dataSource;

    @BindView(R.id.comments)
    AppCompatEditText comments;
    @BindView(R.id.post_comments)
    Button postComments;
    @BindView(R.id.cancel)
    Button cancel;
    Unbinder unbinder;

    private CommentsCallBack commentsCallBack;

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = PreferencesHelper.getPreferencesInstance(getContext());

        retrofitClient = RetrofitClient.getRetrofitClientInstance(ApiEndPoint.BASE_URL);

        apiService = retrofitClient.getRetrofit().create(ApiService.class);

        remoteDataSource = new RemoteDataSourceHelper(apiService);

        dataSource = new DataRepository(getContext(), remoteDataSource, pref);
    }


    public static PostFragment getInstance(int id, String type) {

        PostFragment postFragment = new PostFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putString("type", type);

        postFragment.setArguments(bundle);

        return postFragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public void setListener(CommentsCallBack listener) {
        this.commentsCallBack = listener;
    }


    @OnClick({R.id.post_comments, R.id.cancel})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.post_comments:

                postComments();

                break;

            case R.id.cancel:

                dismiss();

                break;
        }

    }


    private void postComments() {


        if (validate()) {

            if (getArguments() != null) {

                int id = getArguments().getInt("id");

                String type = getArguments().getString("type");

                Map<String, Object> params = new HashMap<>();

                params.put("type_id", id);
                params.put("type", type);
                params.put("comment", comments.getText().toString());

                ProgressUtils.showProgress(getContext(), "Loading");

                dataSource.postComments(dataSource.getAuthendicate(), new JSONObject(params).toString(), this);

            } else
                Toast.makeText(getContext(), "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getContext(), "Please fill comments before post", Toast.LENGTH_SHORT).show();

    }


    private boolean validate() {

        return comments.getText().length() > 0;

    }

    @Override
    public void onSuccess(Object object) {

        ProgressUtils.hideProgress();

        if (commentsCallBack != null) commentsCallBack.onSuccess();

        dismiss();
    }

    @Override
    public void onFail(Throwable throwable) {
        ProgressUtils.hideProgress();
        if (commentsCallBack != null) commentsCallBack.onFail();

        dismiss();
    }

    @Override
    public void onNetworkFailure() {

        ProgressUtils.hideProgress();
        dismiss();
        Toast.makeText(getContext(), "There is no internet", Toast.LENGTH_SHORT).show();

    }


    public interface CommentsCallBack {

        void onSuccess();

        void onFail();

    }

}
