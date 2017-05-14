package com.kc.activity;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kc.base.MyApp;
import com.kc.myasapp.R;
import com.kc.util.Mail;

import java.io.File;
import java.io.IOException;

public class MailTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_test);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                String fileName = "mailTxt.txt";
                String filePath = dir+File.separator+ fileName;
                File file = new File(filePath);
                if (!file.exists()){
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Mail mail = new Mail("successfulpeter@163.com", "Kcinwyyx1");
                mail.setTo(new String[]{"successfulpeter@163.com"});
                mail.setSubject("我是主题哦");
                mail.setBody("我是内容");
                mail.setFrom("successfulpeter@163.com");
                try {
                    mail.addAttachment(filePath);
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyApp.showToast("添加附件异常");
                        }
                    });
                }

                try {
                    if (mail.send()){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyApp.showToast("邮件发送成功");
                            }
                        });
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyApp.showToast("邮件发送失败");
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyApp.showToast("邮件发送异常");
                        }
                    });
                }
            }
        }).start();

    }
}
