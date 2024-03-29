package iqiqiya.lanlana.sqlitedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText nameEdt;
    private EditText ageEdt;
    private EditText idEdt;
    private String genderStr = "男";
    private SQLiteDatabase sqLiteDatabase;
    private ListView stuList;
    private String idStr;
    private String nameStr;
    private String ageStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initDatabase();
    }

    private void initUI(){
        //初始化控件
        nameEdt = findViewById(R.id.name_edt);
        ageEdt = findViewById(R.id.age_edt);
        idEdt = findViewById(R.id.id_edt);


        stuList = findViewById(R.id.stu_list);

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

    private void initDatabase(){
        //初始化SQLite数据库
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
        sqLiteDatabase = helper.getReadableDatabase();
        //db.rawQuery()查询  select * from 表名
        //db.execSQL()增删改，创建
    }

    //SQLiteOpenHelper
    //SQLiteDatabase
    public void operate(View v){
        switch (v.getId()){
            case R.id.add_btn:


                nameStr = nameEdt.getText().toString();
                ageStr = ageEdt.getText().toString();

                //第一种写法
                //String add_sql = "insert into info_db (name,age,gender) values ('"+nameStr+"',"+ageStr+",'"+genderStr+"')";
                //db.execSQL(add_sql);

                //第二种
                String add_sql = "insert into test_db (name,age,sex) values (?,?,?)";
                sqLiteDatabase.execSQL(add_sql,new String[]{nameStr, ageStr,genderStr});

                Toast.makeText(MainActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                break;
            case R.id.select_btn:
                //全部查询
                String select_sql = "select * from test_db";

                //按照_id进行查询
                idStr = idEdt.getText().toString();
                if (!idStr.equals("")){
                    select_sql += " where _id="+ idStr;
                }

                //查询结果
                Cursor cursor = sqLiteDatabase.rawQuery(select_sql,null);
                //SimpleCursorAdapter
                //最后一个参数是数据展示
                //因为SimpleCursorAdapter初期定义的问题，所以这里必须要用_id而不是id
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                        this,R.layout.item,cursor,
                        new String[]{"_id","name","age","sex"},
                        new int[]{R.id.id_item,R.id.name_item,R.id.age_item,R.id.gender_item},
                        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
                );
                stuList.setAdapter(adapter);
                break;
            case R.id.delete_btn:
                idStr = idEdt.getText().toString();
                int count = sqLiteDatabase.delete("test_db","_id=?",new String[]{idStr});
                if (count > 0){
                    Toast.makeText(this,"删除成功",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.update_btn:
                idStr = idEdt.getText().toString();
                nameStr = nameEdt.getText().toString();
                ageStr = ageEdt.getText().toString();

                ContentValues values = new ContentValues();

                values.put("name", nameStr);
                values.put("age",ageStr);
                values.put("sex",genderStr);
                int count2 = sqLiteDatabase.update("test_db",values,"_id=?",new String[]{idStr});
                if (count2 > 0){
                    Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
