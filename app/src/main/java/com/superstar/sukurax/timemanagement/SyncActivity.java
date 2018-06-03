package com.superstar.sukurax.timemanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVSaveOption;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static com.superstar.sukurax.timemanagement.MainActivity.datebaseHelper;


public class SyncActivity extends Activity {
    ImageView back_toolbar_pic;
    TextView back_toolbar_text,syncInfo;
    SharedPreferences sp;
    android.support.v7.widget.Toolbar back_toolbar;
    Loading_view loading;
    Button syncUp,syncDown;
    String userId,syncStr="同步信息\n";
    Integer syncNeed=0,syncNoNeed=0,syncFalseBefore=0,syncTrue=0,syncFalse=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp= getSharedPreferences("TimeManagement", Context.MODE_PRIVATE);
        switch (sp.getInt("skin_num", 1)){
            case 1:
                setTheme(R.style.AppTheme);
                break;
            case 2:
                setTheme(R.style.AppTheme2);
                break;
            case 3:
                setTheme(R.style.AppTheme3);
                break;
            case 4:
                setTheme(R.style.AppTheme4);
                break;
            case 5:
                setTheme(R.style.AppTheme5);
                break;
            case 6:
                setTheme(R.style.AppTheme6);
                break;
            default:
                break;
        }
    }





    @Override
    protected void onResume() {
        setContentView(R.layout.sync_layout);
        back_toolbar_pic=(ImageView)findViewById(R.id.back_toolbar_pic);
        back_toolbar_text=(TextView) findViewById(R.id.back_toolbar_text);
        back_toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.back_toolbar);
        back_toolbar_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        syncInfo=(TextView)findViewById(R.id.syncInfo);
        syncUp=(Button)findViewById(R.id.syncUp);
        syncDown=(Button)findViewById(R.id.syncDown);
        super.onResume();
        back_toolbar_text.setText("备份/同步");
        switch (sp.getInt("skin_num", 1)){
            case 1:
                back_toolbar.setBackgroundColor(getResources().getColor(R.color.skinColor1_2));
                break;
            case 2:
                back_toolbar.setBackgroundColor(getResources().getColor(R.color.skinColor2_2));
                break;
            case 3:
                back_toolbar.setBackgroundColor(getResources().getColor(R.color.skinColor3_2));
                break;
            case 4:
                back_toolbar.setBackgroundColor(getResources().getColor(R.color.skinColor4_2));
                break;
            case 5:
                back_toolbar.setBackgroundColor(getResources().getColor(R.color.skinColor5_2));
                break;
            case 6:
                back_toolbar.setBackgroundColor(getResources().getColor(R.color.skinColor6_2));
                break;
            default:
                break;
        }
        if(AVUser.getCurrentUser()==null){
            AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(SyncActivity.this);
            normalDialog.setTitle("检测到您未登录");
            normalDialog.setMessage("登录？");
            normalDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent =  new Intent(getApplication(),LoginActivity.class);
                            startActivity(intent);
                        }
                    });
            normalDialog.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SyncActivity.super.onBackPressed();
                        }
                    });
            // 显示
            normalDialog.show();
        }else {
            userId=AVUser.getCurrentUser().getUsername();
        }
        loading = new Loading_view(this, R.style.CustomDialog);


        syncUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loading.show();
                //加载SQLite数据库
                Cursor cursor1=datebaseHelper.getReadableDatabase().rawQuery(
                        "select * from task ",new String[]{}
                );
                if (cursor1.moveToFirst()) {
                    do{
                        //统计syncState状态为待增加、增加中、待删除、删除中、待修改、修改中的数据项个数，该个数为此次需要同步的数据项个数。
                        if(cursor1.getString(cursor1.getColumnIndex("syncState")).equals("1") || cursor1.getString(cursor1.getColumnIndex("syncState")).equals("2")
                                || cursor1.getString(cursor1.getColumnIndex("syncState")).equals("4") || cursor1.getString(cursor1.getColumnIndex("syncState")).equals("5")
                                || cursor1.getString(cursor1.getColumnIndex("syncState")).equals("7") || cursor1.getString(cursor1.getColumnIndex("syncState")).equals("8")){
                            syncNeed++;
                        }
                        //统计增加中、删除中、修改中信息。这三个状态代表之前同步中但是返回结果失败，说明没有同步成功，所以需要在接下来的操作中继续同步。
                        if(cursor1.getString(cursor1.getColumnIndex("syncState")).equals("2") || cursor1.getString(cursor1.getColumnIndex("syncState")).equals("5")
                                || cursor1.getString(cursor1.getColumnIndex("syncState")).equals("8") ){
                            syncFalseBefore++;
                        }
                        //将待增加、增加中数据逐条同步到服务端，状态改为增加中，等服务端同步成功将数据状态改为已增加。已增加统计个数加一。
                        if(cursor1.getString(cursor1.getColumnIndex("syncState")).equals("1") || cursor1.getString(cursor1.getColumnIndex("syncState")).equals("2")){
                            String sql="insert into task(task_id,userId,content,type,time,state) values("+cursor1.getString(cursor1.getColumnIndex("_id"))+","+
                                    userId+","+cursor1.getString(cursor1.getColumnIndex("content"))+","+cursor1.getString(cursor1.getColumnIndex("type"))+","+Escaping(cursor1.getString(cursor1.getColumnIndex("time")))
                                    +","+cursor1.getString(cursor1.getColumnIndex("state"))+")";
                            final String _id=cursor1.getString(cursor1.getColumnIndex("_id"));
                            final String content=cursor1.getString(cursor1.getColumnIndex("content"));
                            AVQuery.doCloudQueryInBackground(sql, new CloudQueryCallback<AVCloudQueryResult>() {
                                @Override
                                public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                    // 如果 e 为空，说明保存成功
                                    SQLiteDatabase db = datebaseHelper.getWritableDatabase();
                                    if(e==null){
                                        db.execSQL("update  task set syncState=? where _id=?",new String[]{"3",_id});
                                        syncTrue++;
                                        syncStr+="同步任务成功:"+content+"\n";
                                    }else {
                                        db.execSQL("update  task set syncState=? where _id=?",new String[]{"2",_id});
                                        syncFalse++;
                                        syncStr+="同步任务失败:"+content+"\n";
                                    }
                                    syncInfo.setText(syncStr);

                                }
                            });
                        }
                        //将待修改、修改中数据逐条同步到服务端，状态改为修改中，等服务端同步成功将数据状态改为已修改。已修改统计个数加一。
                        if(cursor1.getString(cursor1.getColumnIndex("syncState")).equals("4") || cursor1.getString(cursor1.getColumnIndex("syncState")).equals("5")){
                            String sql="update task set userId="+userId+" content="+cursor1.getString(cursor1.getColumnIndex("content"))+" type="+cursor1.getString(cursor1.getColumnIndex("type"))+" time=" +
                                    Escaping(cursor1.getString(cursor1.getColumnIndex("time")))+" state="+cursor1.getString(cursor1.getColumnIndex("state"))+" where task_id="+cursor1.getString(cursor1.getColumnIndex("_id"))+" and userId="+userId;
                            final String _id=cursor1.getString(cursor1.getColumnIndex("_id"));
                            final String content=cursor1.getString(cursor1.getColumnIndex("content"));

                            AVQuery.doCloudQueryInBackground(sql, new CloudQueryCallback<AVCloudQueryResult>() {
                                @Override
                                public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                    // 如果 e 为空，说明保存成功
                                    SQLiteDatabase db = datebaseHelper.getWritableDatabase();
                                    if(e==null){
                                        db.execSQL("update  task set syncState=? where _id=?",new String[]{"6",_id});
                                        syncTrue++;
                                        syncStr+="同步任务成功:"+content+"\n";
                                    }else {
                                        db.execSQL("update  task set syncState=? where _id=?",new String[]{"5",_id});
                                        syncFalse++;
                                        syncStr+="同步任务失败:"+content+"\n";
                                    }
                                    syncInfo.setText(syncStr);
                                }
                            });
                        }
                        //将待删除、删除中数据逐条同步到服务端，状态改为删除中，等服务端同步成功将数据状态改为已删除。已删除统计个数加一。
                        if(cursor1.getString(cursor1.getColumnIndex("syncState")).equals("7") || cursor1.getString(cursor1.getColumnIndex("syncState")).equals("8")){
                            String sql="delete from task where task_id="+cursor1.getString(cursor1.getColumnIndex("_id"))+" and userId="+userId;
                            final String _id=cursor1.getString(cursor1.getColumnIndex("_id"));
                            final String content=cursor1.getString(cursor1.getColumnIndex("content"));

                            AVQuery.doCloudQueryInBackground(sql, new CloudQueryCallback<AVCloudQueryResult>() {
                                @Override
                                public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                    // 如果 e 为空，说明保存成功
                                    SQLiteDatabase db = datebaseHelper.getWritableDatabase();
                                    if(e==null){
                                        db.execSQL("delete from  note WHERE _id = ? ",new String[]{_id});                                        syncTrue++;
                                        syncStr+="同步任务成功:"+content+"\n";
                                    }else {
                                        db.execSQL("update  task set syncState=? where _id=?",new String[]{"8",_id});
                                        syncFalse++;
                                        syncStr+="同步任务失败:"+content+"\n";
                                    }
                                    syncInfo.setText(syncStr);
                                }
                            });
                        }
                        //第三、四、五步执行完毕后，将已增加、已删除、已修改数据项状态改为已更新。统计已增加、已删除、已修改数据项数，该个数为此次同步成功的数据项个数。待增加、增加中、待删除、删除中、待修改、修改中余留个数为同步失败数量。
                        //统计同步数据项成功的数量和失败的数量。显示同步失败的数据项的不同syncState状态和个数。

                    }while (cursor1.moveToNext());
                }
                Cursor cursor2=datebaseHelper.getReadableDatabase().rawQuery(
                        "select * from note ",new String[]{}
                );
                if (cursor2.moveToFirst()) {
                    do{
                        if(cursor2.getString(cursor2.getColumnIndex("syncState")).equals("1") || cursor2.getString(cursor2.getColumnIndex("syncState")).equals("2")
                                || cursor2.getString(cursor2.getColumnIndex("syncState")).equals("4") || cursor2.getString(cursor2.getColumnIndex("syncState")).equals("5")
                                || cursor2.getString(cursor2.getColumnIndex("syncState")).equals("7") || cursor2.getString(cursor2.getColumnIndex("syncState")).equals("8")){
                            ++syncNeed;
                        }
                        if(cursor2.getString(cursor2.getColumnIndex("syncState")).equals("2") || cursor2.getString(cursor2.getColumnIndex("syncState")).equals("5")
                                || cursor2.getString(cursor2.getColumnIndex("syncState")).equals("8") ){
                            ++syncFalseBefore;
                        }
                        //将待增加、增加中数据逐条同步到服务端，状态改为增加中，等服务端同步成功将数据状态改为已增加。已增加统计个数加一。
                        if(cursor2.getString(cursor2.getColumnIndex("syncState")).equals("1") || cursor2.getString(cursor2.getColumnIndex("syncState")).equals("2")){
                            String sql="insert into note(note_id,userId,note_content,note_time) values('"+cursor2.getString(cursor2.getColumnIndex("_id"))+"','"+
                                    userId+"','"+cursor2.getString(cursor2.getColumnIndex("note_content"))+"','"+Escaping(cursor2.getString(cursor2.getColumnIndex("note_time")))+"')";
                            final String _id=cursor2.getString(cursor2.getColumnIndex("_id"));
                            final String content=cursor2.getString(cursor2.getColumnIndex("note_content"));
                            AVQuery.doCloudQueryInBackground(sql, new CloudQueryCallback<AVCloudQueryResult>() {
                                @Override
                                public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                    // 如果 e 为空，说明保存成功
                                    SQLiteDatabase db = datebaseHelper.getWritableDatabase();
                                    if(e==null){
                                        db.execSQL("update note set syncState=? where _id=?",new String[]{"3",_id});
                                        ++syncTrue;
                                        syncStr+="同步便签成功:"+content+"\n";
                                    }else {
                                        db.execSQL("update note set syncState=? where _id=?",new String[]{"2",_id});
                                        ++syncFalse;
                                        syncStr+="同步便签失败:"+content+"\n";
                                    }
                                    syncInfo.setText(syncStr);
                                }
                            });
                        }

                        //将待修改、修改中数据逐条同步到服务端，状态改为修改中，等服务端同步成功将数据状态改为已修改。已修改统计个数加一。
                        if(cursor2.getString(cursor2.getColumnIndex("syncState")).equals("4") || cursor2.getString(cursor2.getColumnIndex("syncState")).equals("5")){
                            final String _id=cursor2.getString(cursor2.getColumnIndex("_id"));
                            final String note_content=cursor2.getString(cursor2.getColumnIndex("note_content"));
                            final String note_time=cursor2.getString(cursor2.getColumnIndex("note_time"));

                            AVQuery<AVObject> query = new AVQuery<>("note");
                            query.whereEqualTo("note_id",cursor2.getString(cursor2.getColumnIndex("_id")));
                            query.whereEqualTo("userId",userId);
                            query.getFirstInBackground(new GetCallback<AVObject>() {
                                @Override
                                public void done(final AVObject account, AVException e) {
                                    account.put("note_content",note_content);
                                    account.put("note_time",note_time);

                                    AVSaveOption option = new AVSaveOption();
                                    account.saveInBackground(option, new SaveCallback() {
                                        @Override
                                        public void done(AVException e) {

                                            SQLiteDatabase db = datebaseHelper.getWritableDatabase();
                                            if (e == null) {
                                                db.execSQL("update  note set syncState=? where _id=?",new String[]{"6",_id});
                                                ++syncTrue;
                                                syncStr+="同步便签成功:"+note_content+"\n";
                                            } else {
                                                db.execSQL("update  note set syncState=? where _id=?",new String[]{"5",_id});
                                                ++syncFalse;
                                                syncStr+="同步便签失败:"+note_content+"\n";
                                            }
                                            syncInfo.setText(syncStr);
                                        }
                                    });
                                }
                            });

                        }
                        //将待删除、删除中数据逐条同步到服务端，状态改为删除中，等服务端同步成功将数据状态改为已删除。已删除统计个数加一。
                        if(cursor2.getString(cursor2.getColumnIndex("syncState")).equals("7") || cursor2.getString(cursor2.getColumnIndex("syncState")).equals("8")){
                            final String _id=cursor2.getString(cursor2.getColumnIndex("_id"));
                            final String note_content=cursor2.getString(cursor2.getColumnIndex("note_content"));

                            AVQuery<AVObject> query = new AVQuery<>("note");
                            query.whereEqualTo("note_id",_id);
                            query.whereEqualTo("userId",userId);
                            query.getFirstInBackground(new GetCallback<AVObject>() {
                                @Override
                                public void done(final AVObject account, AVException e) {
                                    account.deleteInBackground();
                                    AVQuery<AVObject> query = new AVQuery<>("note");
                                    query.whereEqualTo("note_id",_id);
                                    query.whereEqualTo("userId",userId);
                                    query.getFirstInBackground(new GetCallback<AVObject>() {
                                        @Override
                                        public void done(final AVObject account, AVException e) {
                                            SQLiteDatabase db = datebaseHelper.getWritableDatabase();
                                            if (account == null) {
                                                db.execSQL("update  note set syncState=? where _id=?", new String[]{"9", _id});
                                                ++syncTrue;
                                                syncStr += "同步便签成功:" + note_content + "\n";
                                            } else {
                                                db.execSQL("update  note set syncState=? where _id=?", new String[]{"8", _id});
                                                ++syncFalse;
                                                syncStr += "同步便签失败:" + note_content + "\n";
                                            }
                                            syncInfo.setText(syncStr);

                                        }
                                    });

                                }
                            });
                        }
                    }while (cursor2.moveToNext());
                }
                //第三、四、五步执行完毕后，将已增加、已删除、已修改数据项状态改为已更新。统计已增加、已删除、已修改数据项数，该个数为此次同步成功的数据项个数。待增加、增加中、待删除、删除中、待修改、修改中余留个数为同步失败数量。
                //统计同步数据项成功的数量和失败的数量。显示同步失败的数据项的不同syncState状态和个数。
                SQLiteDatabase db = datebaseHelper.getWritableDatabase();
                db.execSQL("update  note set syncState=? where _id=?",new String[]{"12","3"});
                db.execSQL("update  note set syncState=? where _id=?",new String[]{"12","6"});
                db.execSQL("update  note set syncState=? where _id=?",new String[]{"12","9"});

                loading.dismiss();
                AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(SyncActivity.this);
                normalDialog.setTitle("同步完成");
                normalDialog.setMessage("同步数量:"+syncNeed+"\n同步成功:"+syncTrue+"\n同步失败:"+syncFalse);
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                // 显示
                normalDialog.show();



            }
        });
        syncDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncNeed=0;
                syncNoNeed=0;
                syncFalseBefore=0;
                syncTrue=0;
                syncFalse=0;
                loading.show();


            }
        });
    }

    public String Escaping(String str){
//        str=str.replaceAll("-","[-]");
//        str=str.replaceAll(":","[:]");
        return str.replace("\\\\","\\\\\\\\");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
