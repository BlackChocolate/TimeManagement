package com.superstar.sukurax.timemanagement;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class EditActivity extends Activity{
    ImageView back;
    TextView back_text,edit_text;
    RadioGroup edit_radioG;
    DatePicker edit_date;
    TimePicker edit_time;
    Button edit_cancel,edit_confirm;
    RadioButton edit_radioBt1,edit_radioBt2,edit_radioBt3,edit_radioBt4;
    private Calendar cl;
    private int year,month,day,hour,minute;
    String date,time;
    SharedPreferences sp;
    Toolbar  back_toolbar;
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

        setContentView(R.layout.edit_task);

        back=(ImageView)findViewById(R.id.back_toolbar_pic);
        back_text=(TextView)findViewById(R.id.back_toolbar_text);
        back_toolbar=(Toolbar)findViewById(R.id.back_toolbar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        edit_text=(TextView)findViewById(R.id.edit_text);
        edit_radioG=(RadioGroup)findViewById(R.id.edit_radioG);
        edit_date=(DatePicker)findViewById(R.id.date_picker);
        edit_time=(TimePicker)findViewById(R.id.time_picker);
        edit_cancel=(Button)findViewById(R.id.edit_cancel);
        edit_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        edit_confirm=(Button)findViewById(R.id.edit_confirm);

        edit_radioBt1=(RadioButton)findViewById(R.id.edit_radioBt1);
        edit_radioBt2=(RadioButton)findViewById(R.id.edit_radioBt2);
        edit_radioBt3=(RadioButton)findViewById(R.id.edit_radioBt3);
        edit_radioBt4=(RadioButton)findViewById(R.id.edit_radioBt4);

        cl= Calendar.getInstance();
        year=cl.get(Calendar.YEAR);
        month=cl.get(Calendar.MONTH)+1;
        day=cl.get(Calendar.DAY_OF_MONTH);
        hour=cl.get(Calendar.HOUR_OF_DAY);
        minute=cl.get(Calendar.MINUTE);


        edit_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer type = 0;
                if(edit_text.getText().toString().equals("")){
                    Toast.makeText(EditActivity.this,"任务未填写", Toast.LENGTH_SHORT).show();
                }else if(edit_text.getText().toString().length()>=11){
                    Toast.makeText(EditActivity.this,"任务过长，请简略叙述", Toast.LENGTH_SHORT).show();
                }else if(edit_radioG.getCheckedRadioButtonId()!=R.id.edit_radioBt1 & edit_radioG.getCheckedRadioButtonId()!=R.id.edit_radioBt2& edit_radioG.getCheckedRadioButtonId()!=R.id.edit_radioBt3 & edit_radioG.getCheckedRadioButtonId()!=R.id.edit_radioBt4){
                    Toast.makeText(EditActivity.this,"未选择类型", Toast.LENGTH_SHORT).show();
                }else {
                    switch (edit_radioG.getCheckedRadioButtonId()){
                        case R.id.edit_radioBt1:
                            type=1;
                            break;
                        case R.id.edit_radioBt2:
                            type=2;
                            break;
                        case R.id.edit_radioBt3:
                            type=3;
                            break;
                        case R.id.edit_radioBt4:
                            type=4;
                            break;
                        default:
                            Toast.makeText(EditActivity.this, "类型错误", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    SQLiteDatabase db = MainActivity.datebaseHelper.getWritableDatabase();
                    db.execSQL("insert into task (content,type,time,state) values(?,?,?,?)",new String[]{edit_text.getText().toString(), type.toString(),date+" "+time,"0"});

                    Intent intent =  new Intent(getApplication(),MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }


            }



        });


        edit_date.init(year, cl.get(Calendar.MONTH), day, new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                // TODO Auto-generated method stub
                setTitle(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                date=(year-2000)+"-"+(monthOfYear+1)+"-"+dayOfMonth;
            }
        });
        edit_time.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // TODO Auto-generated method stub
                setTitle(hourOfDay+":"+minute);
                time=hourOfDay+":"+minute;
            }
        });
//弹出类型时间选择器
//        new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                // TODO Auto-generated method stub
//                setTitle(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
//            }
//        }, year,cl.get(Calendar.MONTH), day).show();
//
//
//        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
//
//            @Override
//            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                // TODO Auto-generated method stub
//                setTitle(hourOfDay+":"+minute);
//            }
//        }, hour, minute, true).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        back_text.setText("设置任务");

        Calendar calendar = Calendar.getInstance();
        date=(calendar.get(Calendar.YEAR)-2000)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
        time=calendar.get(Calendar.HOUR_OF_DAY)+"："+calendar.get(Calendar.MINUTE);

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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
