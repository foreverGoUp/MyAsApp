package com.kc.util;

import android.util.Log;
import android.util.Xml;

import com.kc.bean.Student;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/11.
 */
public class ParserByPull {

    public static List<Student> getStudents(InputStream in){
        List<Student> list = null;
        Student student = null;

        try {
            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
//        XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in,"UTF-8");
            int eventType = parser.getEventType();

            while (eventType!=XmlPullParser.END_DOCUMENT){
                Log.e("ParserByPull","START_DOCUMENT---"+parser.getName());
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        list = new ArrayList<>();
                        Log.e("ParserByPull","START_DOCUMENT---"+parser.getName());

                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equals("student")){
                            student = new Student();
                            student.setId(parser.getAttributeValue(0));
                        }else if (student!=null){
                            if (name.equals("name")){
                                student.setName(parser.nextText());
                            }else if (name.equals("age")){
                                student.setAge(Integer.parseInt(parser.nextText()));
                            }else if (name.equals("sex")){
                                student.setSex(parser.nextText());
                            }
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        String endName= parser.getName();
                        if (endName.equals("student")){
                            list.add(student);
                            student =null;
                        }

                        break;
                }
                eventType = parser.next();

            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;


    }
}
