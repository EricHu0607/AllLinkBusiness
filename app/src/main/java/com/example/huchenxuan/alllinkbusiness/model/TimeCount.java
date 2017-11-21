package com.example.huchenxuan.alllinkbusiness.model;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.TextView;

import com.example.huchenxuan.alllinkbusiness.R;

/**
 * Created by huchenxuan on 2017/11/7.
 */

public class TimeCount extends CountDownTimer {
    private Button send;
    public TimeCount(Button send ,long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
    }

    @Override
    public void onFinish() {// 计时完毕时触发
        send.setClickable(true);
    }

    @Override
    public void onTick(long millisUntilFinished) {// 计时过程显示
        send.setClickable(false);
        send.setText(millisUntilFinished / 1000 + "秒");
    }
}