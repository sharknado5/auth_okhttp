package com.lou.auth_okhttp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;

public class UserAreaAct extends AppCompatActivity {

    //private static String user_url = "https://app.dev.it.si/alchemy/api/users/harvey/books";
    private static String response;
    private static Example[] objMeta;
    //Button btn_show;


    //private static Example[] objExample;
    //private static Map<String, Object> objMap;
    //private static List<Example> objList = new ArrayList<>();

    //public String test = "Wiele";

    //final TextView txt_test = (TextView) findViewById(R.id.textView_test);

    //private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        Log.d("UserArea:", "UserArea Entered");

        final TextView txt_welcome = (TextView) findViewById(R.id.textView_welcome);
        final TextView txt_test = (TextView) findViewById(R.id.textView_test);
        Button btn_show = (Button) findViewById(R.id.btn_show);

        MainActivity mainActivity = null;
        try {
            mainActivity = new MainActivity();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        final OkHttpClient client = mainActivity.getClient();
        final LoginInfo objLogin = mainActivity.getObj1();

        txt_welcome.setText("Welcome Mr. " + objLogin.surname);
        Log.d("UserArea:Username", mainActivity.finalUser);

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLICKEDY CLICK", "CLICKED");
                //loadContent(client, objLogin.user.username);
            }
        });

        loadContent(client, objLogin.user.username);

    }

    private void updateList(final Example[] objMeta1) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < objMeta1.length; i++) {
                    Log.d("####BOOK_ID", objMeta1[i].metadata.bookID);
                    if (objMeta1[i].metadata.title == null || objMeta1[i].metadata.title == "") {
                        Log.d("###BOOK_TITLE", "NULL OU BUL");
                    }
                    Log.d("###BOOK_TITLE", objMeta1[i].metadata.title);

                    //String[] textArr = {"One", "Two", "Three", "Four"};

                    ListView listView = (ListView) findViewById(R.id.list_books);

                    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int j = 0; j < objMeta1.length; j++) {
                        if (objMeta1[j].metadata.title == "" || objMeta1[j].metadata.title == null) {
                            list.add(putData(objMeta1[j].metadata.bookID, "(Title not available)"));
                        }
                        else {
                            list.add(putData(objMeta1[j].metadata.bookID, objMeta1[j].metadata.title));
                        }

                        //list.add(objMeta1[j].metadata.bookID);
                        //list.add(objMeta1[j].metadata.title);
                        /*map = new HashMap<String, String>();
                        map.put("BookID: ", objMeta1[j].metadata.bookID + "\n");
                        map.put("Title: ", objMeta1[j].metadata.title);
                        list.add(map);*/
                    }
                    //ArrayAdapter adapter = new ArrayAdapter(UserAreaAct.this, android.R.layout.simple_list_item_1, list);
                    //listView.setAdapter(adapter);
                    String[] from = {"Title: ", "BookID: "};
                    int[] to = {android.R.id.text1, android.R.id.text2};
                    SimpleAdapter adapter = new SimpleAdapter(UserAreaAct.this, list, android.R.layout.simple_list_item_2, from, to);
                    listView.setAdapter(adapter);




                    /*RelativeLayout relativeLayout = new RelativeLayout(UserAreaAct.this);
                    setContentView(relativeLayout);
                    for (int j = 0; j < textArr.length; j++) {
                        TextView textView = new TextView(UserAreaAct.this);
                        textView.setText(textArr[j]);
                        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT);
                        relativeLayout.addView(textView);
                    }*/

                    /*LinearLayout linearLayout = new LinearLayout(UserAreaAct.this);
                    setContentView(linearLayout);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    for (int j = 0; j < textArr.length; j++) {
                        TextView textView = new TextView(UserAreaAct.this);
                        textView.setText(textArr[j]);
                        linearLayout.addView(textView);
                    }*/
                }
            }
        });
    }

    private HashMap<String, String> putData(String id, String title) {
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("Title: ", title);
        item.put("BookID: ", id);
        return item;
    }

    public Example[] getObjMeta() {
        return this.objMeta;
    }

    public void loadContent(final OkHttpClient client, final String username) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                //Toast.makeText()
                try {
                    response = ApiCall.GET(client, RequestBuilder.buildUrl(username) /*user_url*/);
                    Log.d("USER_AREA:LoadContent", response);

                    Gson gson = new Gson();
                    objMeta = gson.fromJson(response, Example[].class);

                    updateList(objMeta);

                    /*for (int i = 0; i < objMeta.length; i++) {
                        Log.d("###########BOOK_ID", objMeta[i].metadata.bookID);
                        if (objMeta[i].metadata.title == null || objMeta[i].metadata.title == "") {
                            Log.d("#########BOOK_TITLE", "NULL OU BUL");
                        }
                        Log.d("###########BOOK_TITLE", objMeta[i].metadata.title);
                    }*/
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public void loadContent2(final OkHttpClient client, final String username) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String resp = null;
                try {
                    resp = ApiCall.GET(client, RequestBuilder.buildUrl(username));
                    Log.d("USER_AREA:LoadContent", resp);
                    return resp;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return resp;
            }
        };
    }
}
