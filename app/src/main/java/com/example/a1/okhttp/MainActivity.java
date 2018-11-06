package com.example.a1.okhttp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    String synString = null;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                tvShowok.setText(synString);
            }
            return false;
        }
    });

    @Bind(R.id.tv_init)
    TextView tvInit;
    @Bind(R.id.bt_asyn_get)
    Button btAsynGet;

    @Bind(R.id.bt_syn_get)
    Button btSynGet;

    @Bind(R.id.tv_showok)
    TextView tvShowok;
    @Bind(R.id.bt_asyn_post)
    Button btAsynPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.bt_asyn_get)
    public void onBtAsynGetClicked() {
        getAsyncHttp();
    }

    @OnClick(R.id.bt_syn_get)
    public void onBtSynGetClicked() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synString = getSyncHttp();

                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();
    }

    @OnClick(R.id.bt_asyn_post)
    public void onViewClicked() {
        postAsynHttp();
    }


    private void getAsyncHttp() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://www.baidu.com")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvShowok.setText("no internet ,so failed!");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvShowok.setText(string);
                    }
                });
            }
        });
    }


    private String getSyncHttp() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://www.baidu.com")
                .build();
        Call call = null;
        call = okHttpClient.newCall(request);
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            return "response error 1!";
        }
        if (response.isSuccessful()) {
            try {
                return response.body().string();
            } catch (IOException e) {
                return "response error 2!";
            }
        } else {
            return "response error! 3";
        }
    }

    private void postAsynHttp() {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBoty = new FormBody.Builder()
                .add("tag1", "con1")
                .add("tag2", "con2")
                .add("tag3", "con3")
                .build();
        Request request = new Request.Builder()
                .url("http://www.baidu.com")
                .post(formBoty)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvShowok.setText("no internet ,so failed!");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String string = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvShowok.setText(string);
                    }
                });
            }
        });
    }


}
