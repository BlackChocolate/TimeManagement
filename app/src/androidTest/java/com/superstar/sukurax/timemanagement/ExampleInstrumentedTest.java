package com.superstar.sukurax.timemanagement;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    Integer err;

    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.superstar.sukurax.timemanagement", appContext.getPackageName());


    }
    @Test
    public void testLogin(){
        final String loginUsername="sukurax";
        final String loginPassword="123456";
        err=1;
        AVQuery<AVObject> query = new AVQuery<>("_User");
        query.whereEqualTo("username", loginUsername.trim());
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, AVException e) {
                if (e == null) {
                    // 查询成功，输出计数
                    if(i==0){
                        AVUser user = new AVUser();// 新建 AVUser 对象实例
                        user.setUsername(loginUsername.trim());// 设置用户名
                        user.setPassword(loginPassword.trim());// 设置密码
                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    // 注册成功
                                    err = 1;
                                    Log.d("test","注册成功");
                                } else {
                                    err = 0;
                                    Log.d("test","注册失败:"+e);
                                }
                            }
                        });
                    }if(i==1){
                        AVUser.logInInBackground(loginUsername.trim(), loginPassword.trim(), new LogInCallback<AVUser>() {
                            @Override
                            public void done(AVUser avUser, AVException e) {
                                if (e == null) {
                                    err = 1;
                                    Log.d("test","登录成功");
                                } else {
                                    err = 0;
                                    Log.d("test","登录失败"+e);
                                }
                            }
                        });
                    }
                } else {
                    // 查询失败
                    err = 0;
                    Log.d("test","查询失败"+e);
                }
            }
        });

        assertEquals("1", err.toString());
    }
}
