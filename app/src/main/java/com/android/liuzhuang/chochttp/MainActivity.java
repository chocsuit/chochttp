package com.android.liuzhuang.chochttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.liuzhuang.chochttp.presenter.ChocHttpTest;
import com.android.liuzhuang.chochttp.presenter.NetworkPresenter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private NetworkPresenter presenter;
    private TextView responseTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new NetworkPresenter();
        responseTv = (TextView) findViewById(R.id.response);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.okhttp) {
//            presenter.sendByOkHttp();
            ChocHttpTest.main(null);
        } else if (v.getId() == R.id.retrofit) {
            presenter.sendByRetrofit();
        }
    }
}
