package com.superstar.sukurax.timemanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;

import java.util.Map;

public class LoginActivity extends Activity {
    EditText loginPassword,loginUsername;
    Button loginBtn;
    Loading_view loading;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        loginUsername=(EditText)findViewById(R.id.loginUsername);
        loginPassword=(EditText)findViewById(R.id.loginPassword);
        loginBtn=(Button)findViewById(R.id.loginBtn);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(AVUser.getCurrentUser()!=null){
            AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(LoginActivity.this);
            normalDialog.setTitle("检测到您已登录");
            normalDialog.setMessage("注销登录？");
            normalDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AVUser.getCurrentUser().logOut();
                        }
                    });
            normalDialog.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LoginActivity.super.onBackPressed();
                        }
                    });
            // 显示
            normalDialog.show();
        }

        loading = new Loading_view(this, R.style.CustomDialog);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loginUsername.getText().toString().isEmpty()||loginPassword.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "不能为空!", Toast.LENGTH_SHORT).show();
                }else{
                    loading.show();
                    AVQuery<AVObject> query = new AVQuery<>("_User");
                    query.whereEqualTo("username", loginUsername.getText().toString().trim());
                    query.countInBackground(new CountCallback() {
                        @Override
                        public void done(int i, AVException e) {
                            if (e == null) {
                                // 查询成功，输出计数
                               if(i==0){
                                   AVUser user = new AVUser();// 新建 AVUser 对象实例
                                   user.setUsername(loginUsername.getText().toString().trim());// 设置用户名
                                   user.setPassword(loginPassword.getText().toString().trim());// 设置密码
                                   user.signUpInBackground(new SignUpCallback() {
                                       @Override
                                       public void done(AVException e) {
                                           if (e == null) {
                                               loading.dismiss();
                                               // 注册成功
                                               Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                               Intent intent =  new Intent(getApplication(),MainActivity.class);
                                               startActivity(intent);
                                           } else {
                                               loading.dismiss();
                                               // 失败的原因可能有多种，常见的是用户名已经存在。
                                               Toast.makeText(LoginActivity.this, "注册失败"+e, Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                   });
                               }if(i==1){
                                    AVUser.logInInBackground(loginUsername.getText().toString().trim(), loginPassword.getText().toString().trim(), new LogInCallback<AVUser>() {
                                        @Override
                                        public void done(AVUser avUser, AVException e) {
                                            if (e == null) {
                                                loading.dismiss();
                                                // 注册成功
                                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                                Intent intent =  new Intent(getApplication(),MainActivity.class);
                                                startActivity(intent);
                                            } else {
                                                loading.dismiss();
                                                // 失败的原因可能有多种，常见的是用户名已经存在。
                                                Toast.makeText(LoginActivity.this, "登录失败"+e, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            } else {
                                loading.dismiss();
                                // 查询失败
                                Toast.makeText(LoginActivity.this, "用户名是否唯一查询失败"+e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
