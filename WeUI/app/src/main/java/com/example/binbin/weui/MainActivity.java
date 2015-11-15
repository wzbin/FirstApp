package com.example.binbin.weui;


import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.logging.LogRecord;


public class MainActivity extends AppCompatActivity {

    private ListView listView = null;
    private Button send = null;
    private EditText enter = null;



    ArrayList<ChatMsgEntity> data = new ArrayList<ChatMsgEntity>();

    //ChatMsgViewAdapter adapter = new ChatMsgViewAdapter(MainActivity.this,data);


    //ChatMsgViewAdapter adapter = new ChatMsgViewAdapter(this, data);

    Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle b = msg.getData();

            int type = b.getInt("type");
            String s = b.getString("xinxi").toString();

            // String back = b.getString("you").toString();

            if(type==1)
                data.add(new ChatMsgEntity("me", s, false));

            //Log.d("My",your+"2");
            else
                data.add(new ChatMsgEntity("you", s, true));


            ChatMsgViewAdapter adapter2 = new ChatMsgViewAdapter(MainActivity.this, data);

            listView.setAdapter(adapter2);

            //listView.setAdapter();

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = (ListView) findViewById(R.id.list);
        //ArrayList<ChatMsgEntity> data = new ArrayList<ChatMsgEntity>();
        //data.add(new ChatMsgEntity("me","hehe",false));
        ChatMsgViewAdapter adapter = new ChatMsgViewAdapter(this, data);

        data.add(new ChatMsgEntity("you", "你好", true));

        listView.setAdapter(adapter);
        //将分割线去掉
        listView.setDividerHeight(0);

        enter = (EditText) findViewById(R.id.enter);

        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = enter.getText().toString();

                enter.setText(null);

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(enter.getWindowToken(), 0);

                Task task = new Task(s);

                Thread thread = new Thread(task);

                thread.start();

                String back = task.getBack();

                //Log.d("My", back);

                Message message = new Message();

                Bundle b = new Bundle();

                b.putInt("type",1);
                b.putString("xinxi", s);
                //b.putString("you", back);

                message.setData(b);

                handler.sendMessage(message);


            }
        });


    }

    private ArrayList<ChatMsgEntity> getData() {
        ArrayList<ChatMsgEntity> arrayList = new ArrayList<ChatMsgEntity>();

        for (int i = 1; i < 10; i++) {

            if ((i % 2) == 0) {

                ChatMsgEntity chatMsgEntity = new ChatMsgEntity("me", "hah", false);
                arrayList.add(chatMsgEntity);

            } else {

                ChatMsgEntity chatMsgEntity = new ChatMsgEntity("you", "waaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaawa", true);
                arrayList.add(chatMsgEntity);

            }


        }
        return arrayList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class Task implements Runnable {

        private String s = new String();
        private String back = new String();

        public Task(String s) {

            this.s = s;

        }


        public String getBack() {

            return this.back;

        }


        @Override
        public void run() {


            String APIKEY = "2e29921641b808e1986e20d4fc6e3238";
            String INFO = null;
            try {
                INFO = URLEncoder.encode(s, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String getURL = "http://www.tuling123.com/openapi/api?key=" + APIKEY + "&info=" + INFO;
            URL getUrl = null;
            try {
                getUrl = new URL(getURL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) getUrl.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                connection.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 取得输入流，并使用Reader读取
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            StringBuffer sb = new StringBuffer();
            String line = "";
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 断开连接
            connection.disconnect();

            sb.delete(0, 23);
            sb.delete(sb.length() - 2, sb.length());



            back = sb.toString();

            Message message2 = new Message();

            Bundle b2 = new Bundle();

            b2.putInt("type", 0);
            b2.putString("xinxi",back);

            message2.setData(b2);
            handler.sendMessage(message2);



        }


    }

}