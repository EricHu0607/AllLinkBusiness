package com.example.huchenxuan.alllinkbusiness.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

/**
 * Created by Administrator on 2017/11/1.
 */

public class LoginInterfaceActivity extends AppCompatActivity {

    private static final String TAG = LoginInterfaceActivity.class.getSimpleName();
//    定义界面中的按钮
    private Button bnLogin;
//    定义界面中的文本框
    private TextView register;
    private TextView forgetPassword;
//    定义界面中的编辑框
    private EditText inputUsername;
    private EditText inputPassword;
//    定义返回码
    private int retCode;

    Response.Listener<String> listener = new Response.Listener<String>() {
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
//                                    登录成功，启动MainActivity
                Intent intentBusiness = new Intent(LoginInterfaceActivity.this,
                    MainActivity.class);
                startActivity(intentBusiness);
//                                    结束该Activity
                finish();
            } else {
                Toast.makeText(LoginInterfaceActivity.this,"用户名或密码错误!",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_interface);
//        获取界面中的按钮
        bnLogin = (Button) findViewById(R.id.btnLogin);
//        获取界面中的文本框
        register = (TextView) findViewById(R.id.register);
        forgetPassword =(TextView) findViewById(R.id.forgetPassword);
//        获取界面中的编辑框
        inputUsername = (EditText) findViewById(R.id.edit_username);
        inputPassword = (EditText) findViewById(R.id.edit_password);

        bnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phoneNumber = inputUsername.getText().toString();
                final String passwordStr = inputPassword.getText().toString();

                if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(passwordStr)) {
                    Toast.makeText(LoginInterfaceActivity.this, "账号密码不能为空！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phoneNumber.length() != 11) {
                    Toast.makeText(LoginInterfaceActivity.this, "手机号必须为11位！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestQueue requestQueue = Volley.newRequestQueue(LoginInterfaceActivity.this);

//                String url = new String("http://101.200.47.1:9999/test/test.php");
                String url = new String("http://10.82.60.115:8080/alllink/seller2/login");
//                String url = new String("http://101.200.47.1:8080/alllink/seller/login");


                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        url, listener, errorListener) {
                    @Override
                    protected Map<String,String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<String, String>();
                        map.put("phoneNumber", phoneNumber);
                        map.put("password",passwordStr);
                        return map;
                    }
                };
                requestQueue.add(stringRequest);

/*                {
//                    登录成功，启动MainActivity
                    Intent intentBusiness = new Intent(LoginInterfaceActivity.this,
                            MainActivity.class);
                    startActivity(intentBusiness);
//                    结束该Activity
                    finish();
                }*/
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegister = new Intent(LoginInterfaceActivity.this,
                        RegisterActivity.class);
                startActivity(intentRegister);
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForgetPassword = new Intent(LoginInterfaceActivity.this,
                        ForgetPasswordActivity.class);
                startActivity(intentForgetPassword);
            }
        });
    }



    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.e(TAG, volleyError.getMessage(), volleyError);
            Toast.makeText(LoginInterfaceActivity.this,"连接服务器失败",Toast.LENGTH_SHORT).show();

        }
    };
}
