package iqiqiya.lanlana.sqlitedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText nameEdt;
    private EditText ageEdt;
    private EditText idEdt;
    private String genderStr = "男";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI(){
        //初始化控件
        nameEdt = findViewById(R.id.name_edt);
        ageEdt = findViewById(R.id.age_edt);
        idEdt = findViewById(R.id.id_edt);

        final RadioGroup genderGp = findViewById(R.id.gender_gp);
        genderGp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.male){
                    genderStr="男";
                }else {
                    genderStr="女";
                }
            }
        });

        //进入界面直接开始动态请求权限
        int permisson = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permisson!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }


    //SQLiteOpenHelper
    //SQLiteDatabase
    public void operate(View v){
        switch (v.getId()){
            case R.id.add_btn:
                //如果只有一个数据库名称，只会存放在私有目录data/data/包名/
                //如果带SD卡路径，那么会在指定路径下
                //最后一个参数是数据库版本
                String path = Environment.getExternalStorageDirectory()+"/test.db";
                SQLiteOpenHelper helper = new SQLiteOpenHelper(this,path,null,1) {
                    @Override
                    public void onCreate(SQLiteDatabase db) {
                        //创建
                        Toast.makeText(MainActivity.this,"数据库创建",Toast.LENGTH_SHORT).show();
                        //数据库不存在就调用onCreate()，所以可以在这里创建表
                        String sql = "create table test_db (_id integer primary key autoincrement," +
                                "name varchar(20),"+"age integer,"+"sex varchar(2))";
                        db.execSQL(sql);
                    }

                    @Override
                    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                        //升级
                        Toast.makeText(MainActivity.this,"数据库升级",Toast.LENGTH_SHORT).show();
                    }
                };
                //打开就是获取数据库对象
                //1.数据库存在就直接打开
                //2.数据库不存在，先创建再打开
                //3.数据库存在，但版本号增加，先调用onUpgrade()进行升级
                SQLiteDatabase db = helper.getReadableDatabase();
                //db.rawQuery()查询  select * from 表名
                //db.execSQL()增删改，创建

                String nameStr = nameEdt.getText().toString();
                String ageStr = ageEdt.getText().toString();

                //第一种写法
                //String add_sql = "insert into info_db (name,age,gender) values ('"+nameStr+"',"+ageStr+",'"+genderStr+"')";
                //db.execSQL(add_sql);

                //第二种
                String add_sql = "insert into test_db (name,age,sex) values (?,?,?)";
                db.execSQL(add_sql,new String[]{nameStr,ageStr,genderStr});

                Toast.makeText(MainActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                break;
            case R.id.select_btn:
                break;
            case R.id.delete_btn:
                break;
            case R.id.update_btn:
                break;
        }
    }
}
