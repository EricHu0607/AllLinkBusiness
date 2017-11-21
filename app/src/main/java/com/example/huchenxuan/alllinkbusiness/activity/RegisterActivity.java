package com.example.huchenxuan.alllinkbusiness.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.huchenxuan.alllinkbusiness.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.huchenxuan.alllinkbusiness.model.RegexUtils.*;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = LoginInterfaceActivity.class.getSimpleName();
    private int retCode;
    private TimeCount time;

    //    定义界面中的按钮
    private Button btnRegister;
    private Button btnGetVerifyCode;
    //    定义界面中的编辑框
    private EditText inputPhoneNumber;
    private EditText inputVerificationCode;
    private EditText inputPassword;
    private EditText inputVerifyPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

        btnGetVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phoneNumber = inputPhoneNumber.getText().toString();
                if (judagePhoneNumber(phoneNumber)) {
                    return;
                }

                RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);


                String url = new String("http://101.200.47.1:8080/alllink/seller2/sendMessage");

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        url, listenerVerificationCode, errorListenerVerificationCode) {
                    @Override
                    protected Map<String,String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<String, String>();
                        map.put("phoneNumber",phoneNumber);
                        map.put("verificationCode","");
                        return map;
                    }
                };
                requestQueue.add(stringRequest);

                time = new TimeCount(60000, 1000);
                time.start();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phoneNumber = inputPhoneNumber.getText().toString();
                final String password = inputPassword.getText().toString();
                final String verificationCode = inputVerificationCode.getText().toString();
                final String verifyPassword = inputVerifyPassword.getText().toString();

                if (judagePhoneNumber(phoneNumber)) {
                    return;
                }
                if (judgePassword(password, verifyPassword)) {
                    return;
                }
                if (verificationCode.length() != 6) {
                    Toast.makeText(RegisterActivity.this,"验证码为6位数!",Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);

//                String url = new String("http://101.200.47.1:9999/test/test.php");
//                String url = new String("http://10.82.60.115:8080/alllink/seller/add");
                String url = new String("http://101.200.47.1:8080/alllink/seller2/add");

                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        url, listenerRegister, errorListenerRegister) {
                    @Override
                    protected Map<String,String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<String, String>();
                        map.put("Content-Type","test");
                        map.put("phoneNumber",phoneNumber);
                        map.put("verificationCode",verificationCode);
                        map.put("password",password);
                        return map;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
    }

    /**
     * @param password
     * @param verifyPassword
     * @return
     */
    private boolean judgePassword(String password, String verifyPassword) {
        if (password.length()<6 || password.length()>20) {
            Toast.makeText(RegisterActivity.this, "密码为6-20位！",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        if (!isPassword(password)) {
            Toast.makeText(RegisterActivity.this, "密码需要由英文数字组成！",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        if (!password.equals(verifyPassword)) {
            Toast.makeText(RegisterActivity.this, "两次密码不同，请重新输入！",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    /**
     * @param phoneNumber
     * @return
     */
    private boolean judagePhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(RegisterActivity.this, "手机号不能为空！",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        if (phoneNumber.length() != 11) {
            Toast.makeText(RegisterActivity.this, "手机号必须为11位！",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        if ( !isMobileExact(phoneNumber) ) {
                Toast.makeText(RegisterActivity.this, "请输入正确的手机号！",
                        Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void init() {
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnGetVerifyCode = (Button) findViewById(R.id.btnGetVerifyCode);
        inputPhoneNumber = (EditText) findViewById(R.id.inputPhoneNumber);
        inputVerificationCode = (EditText) findViewById(R.id.inputVerificationCode);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        inputVerifyPassword = (EditText) findViewById(R.id.inputVerifyPassword);
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            btnGetVerifyCode.setText("重新获取验证码");
            btnGetVerifyCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            btnGetVerifyCode.setClickable(false);
            btnGetVerifyCode.setText(millisUntilFinished / 1000 + "秒");
        }
    }


    Response.Listener<String> listenerRegister = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            Log.e(TAG, s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                retCode = jsonObject.getInt("code");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (retCode == 1) {
                Toast.makeText(RegisterActivity.this,"注册成功!",Toast.LENGTH_SHORT).show();
//                                    登录成功，启动MainActivity
                Intent intentBusiness = new Intent(RegisterActivity.this,
                        MainActivity.class);
                startActivity(intentBusiness);
//                                    结束该Activity
                finish();
            } else {
                Toast.makeText(RegisterActivity.this,"注册失败!",Toast.LENGTH_SHORT).show();
            }
        }
    };

    Response.Listener<String> listenerVerificationCode = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            Log.e(TAG, s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                retCode = jsonObject.getInt("code");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (retCode == 1) {
                Toast.makeText(RegisterActivity.this,"验证码已发送!",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterActivity.this,"验证码发送失败!",Toast.LENGTH_SHORT).show();
            }
        }
    };


    Response.ErrorListener errorListenerRegister = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.e(TAG, volleyError.getMessage(), volleyError);
            Toast.makeText(RegisterActivity.this,"连接服务器失败",Toast.LENGTH_SHORT).show();
        }
    };

    Response.ErrorListener errorListenerVerificationCode = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.e(TAG, volleyError.getMessage(), volleyError);
            Toast.makeText(RegisterActivity.this,"连接服务器失败",Toast.LENGTH_SHORT).show();
        }
    };
}
