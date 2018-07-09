package com.example.nettest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    // M:数据源
    private ArrayList<String> mImageList;// 图片数据来源
    private ArrayList<String> mTitleList;
    private ArrayList<String> mDateList;
    private ArrayList<String> mAuthorList;
    private ArrayList<String> mUrlList;// 跳转页面URL

    // V:视图
    private ListView mListView;

    // P:适配器
    private MyAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initData();
        initAdapter();
        initView();
    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                try {
                    // 获取数据的地址
                    URL url = new URL("http://v.juhe.cn/toutiao/index?type=tiyu&key=5465c4c5d60f72c3d756a9f1a9b8437d");

                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();

                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);

                    if (connection.getResponseCode() == 200) {
                        // 获取字节输入流
                        InputStream is = connection.getInputStream();
                        // 转换为字符流
                        InputStreamReader isr = new InputStreamReader(is);

                        // 缓冲字符输入流
                        BufferedReader br = new BufferedReader(isr);

                        // Json数据拼接用的字符串
                        StringBuffer sb = new StringBuffer();
                        // "水瓢"
                        String buffer = new String();

                        while ((buffer = br.readLine()) != null) {

                            // 追加拼接的字符串
                            sb.append(buffer);
                        }
                        // 转换成字符串，打印检测结果是否正常
                        Log.d(TAG, sb.toString());
                        br.close();

                        parseJSON(sb.toString());
                    } else {
                        Log.d(TAG, "Error code:" + connection.getResponseCode());
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.d(TAG, "" + e);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "" + e);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "" + e);
                }
            }

        }.start();

    }

    // 解析Json数据
    private void parseJSON(String data) throws JSONException {

        // 最外层的JSONObject类型

        JSONObject object = new JSONObject(data);

        JSONObject result = object.getJSONObject("result");
        JSONArray array = result.getJSONArray("data");

        for (int i = 0; i < array.length(); i++) {

            JSONObject item = array.getJSONObject(i);

            // String uniquekey = item.getString("uniquekey");
            String title = item.getString("title");
            String date = item.getString("date");
            String category = item.getString("category");
            String author_name = item.getString("author_name");
            String url = item.getString("url");
            String thumbnail_pic_s = item.getString("thumbnail_pic_s");// 图片的链接

            //根据偏好设置，进行显示！！！！！！！！！！！
            if (category.equals("体育")) {
                mTitleList.add(title);
                mDateList.add(date);
                mImageList.add(thumbnail_pic_s);
                mAuthorList.add(author_name);
                mUrlList.add(url);
            }
        }
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 跳转传值，在下个页面打开相应的网页
                Intent intent = new Intent(Main2Activity.this,
                        MainActivity.class);
                intent.putExtra("url", mUrlList.get(position));
                startActivity(intent);
            }
        });

    }
    private void initAdapter() {
        // 第一个参数：上下文
        // 第二个参数：单个Item的布局
        mAdapter = new MyAdapter(this, mImageList, mTitleList, mDateList,
                mAuthorList);
    }

    public void getInputStreamFromNet(final String imageUrl,
                                      final ImageView image) {
        new Thread() {
            @Override
            public void run() {
                // 基础参数对象

                BasicHttpParams params = new BasicHttpParams();
                // 使用静态方法

                // 设置连接时长
                // 第一个参数：BasicHttpParams对象
                // 第二个参数：设置时间，默认60秒。设置为10秒
                HttpConnectionParams.setConnectionTimeout(params, 10000);
                HttpConnectionParams.setSoTimeout(params, 10000);

                // 获得HttpClient对象
                HttpClient client = new DefaultHttpClient(params);

                // 实例化get请求对象
                HttpGet get = new HttpGet(imageUrl);
                try {

                    // 执行get请求，获得返回的数据封装
                    HttpResponse response = client.execute(get);
                    // 判断状态码
                    if (response.getStatusLine().getStatusCode() == 200) {
                        InputStream is = response.getEntity().getContent();

                        // 转Bitmap或者文件存储
                        final Bitmap bitmap = BitmapFactory.decodeStream(is);

                        runOnUiThread(new Runnable() {// 需继承Activity类
                            @Override
                            public void run() {
                                image.setImageBitmap(bitmap);

                            }
                        });

                        is.close();

                    } else {
                        Log.d(TAG, "Error code:"
                                + response.getStatusLine().getStatusCode());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "" + e);
                }
            }
        }.start();

    }
}
