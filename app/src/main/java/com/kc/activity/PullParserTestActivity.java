package com.kc.activity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kc.bean.Student;
import com.kc.myasapp.R;
import com.kc.util.ParserByPull;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PullParserTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_parser_test);

        AssetManager manager = getAssets();
        InputStream inputStream = null;
        try {
            inputStream = manager.open("students.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Student> list = ParserByPull.getStudents(inputStream);
        int i = 0;
        for (Student student: list){
            i++;
            Log.e("******","student:"+i+"\n"+
            "id="+student.getId()+",name="+student.getName()+",sex="+ student.getSex()+"age="+student.getAge());
        }
    }
}
