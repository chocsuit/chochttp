package com.android.liuzhuang.chochttp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.liuzhuang.chochttp.presenter.ChocHttpTest;
import com.android.liuzhuang.chochttp.presenter.NetworkPresenter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NetworkPresenter.NetworkCallback {

    private NetworkPresenter presenter;
    private TextView responseTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new NetworkPresenter();
        presenter.setCallback(this);
        responseTv = (TextView) findViewById(R.id.response);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.choc) {
            presenter.sendByChoc();
        }
    }

    @Override
    public void onSuccess(String response) {
        responseTv.setTextColor(Color.parseColor("#999999"));
        responseTv.setText(response);
    }

    @Override
    public void onError(String error) {
        responseTv.setTextColor(Color.RED);
        responseTv.setText(error);
    }
}
