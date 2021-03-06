package com.lou.itsi_authenticator;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lou.auth_okhttp.R;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import okhttp3.OkHttpClient;

public class UserAreaAct extends AppCompatActivity {

    private static String response;
    private static Example[] objBooks;
    //private static Groups[] objGroup;
    //private static Authorize objAuth;
    //private static Ack objAck;
    //private ListView listView, listGroup;
    final Context context = this;
    //public static boolean ack;
    //public static boolean check = false;
    public MainActivity mainActivity;
    //private AQuery aq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        Log.d("UserArea:", "UserArea Entered");



        /*getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.miebooks);*/

        final TextView txt_welcome = (TextView) findViewById(R.id.textView_welcome);
        final Button btn_show = (Button) findViewById(R.id.btn_show);
        final Button btn_groups = (Button) findViewById(R.id.btn_groups);
        final Button btn_logout = (Button) findViewById(R.id.btn_logout);
        final Button btn_api = (Button) findViewById(R.id.btn_api);
        //listView = (ListView) findViewById(R.id.list_books1);

        mainActivity = null;
        try {
            mainActivity = new MainActivity();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final OkHttpClient client = mainActivity.getClient();
        final OkHttpClient client2 = mainActivity.getClient2();

        Log.wtf("USERAREA!@#$%^&*()", "1");

        final LoginInfo objLogin = mainActivity.getObj1();

        getSupportActionBar().setTitle(objLogin.getUser().getFirstname() + "'s User Area");
        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadContent(client, objLogin.getUser().getUsername(), mainActivity.getDns());

        //txt_welcome.setText("Welcome " + objLogin.getUser().getFirstname());
        //Log.d("UserArea:Username", mainActivity.finalUser);

        //Log.d("CLICKEDY CLICK", "CLICKED");

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserAreaAct.this, BooksAct.class);
                UserAreaAct.this.startActivity(intent);
            }
        });

        btn_groups.setOnClickListener(new View.OnClickListener(){
            //boolean show = false;
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserAreaAct.this, GroupsAct.class);
                UserAreaAct.this.startActivity(intent);
            }
        });

        btn_api.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserAreaAct.this, ApiCallAct.class);
                UserAreaAct.this.startActivity(intent);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserAreaAct.this, MainActivity.class);
                //mainActivity.clearLogin();
                finish();
                UserAreaAct.this.startActivity(intent);
            }
        });

    }

    public void loadContent(final OkHttpClient client, final String username, final String dns) {
        new AsyncTask<Void, Void, Example[]>() {
            @Override
            protected Example[] doInBackground(Void... params) {
                try {

                    response = ApiCall.GET(client, RequestBuilder.buildUrl(username, dns));
                    Log.d("USER_AREA:LoadContent", response);

                    if (response.equals("Unauthorized") || response.equals(null) ||
                            response == null || response.equals("")) {
                        showAlert("You are not Authorized\nto view this content");
                    }
                    else {
                        Gson gson = new Gson();
                        objBooks = gson.fromJson(response, Example[].class);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return objBooks;
            }

            /*@Override
            protected void onPostExecute(Example[] books) {
                super.onPostExecute(books);
                if (objMeta != null) {
                    Log.wtf("BOOKS_ACT!@#$%^&*()", "9");
                    //updateList(objMeta);
                    Log.wtf("BOOKS_ACT!@#$%^&*()", "10");
                }
            }*/
        }.execute();
    }

    public Example[] getObjBooks() {
        return this.objBooks;
    }

    private HashMap<String, String> putData(String id, String title) {
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("Title: ", title);
        item.put("BookID: ", id);
        return item;
    }

    private HashMap<String, String> putData2(String id, String title) {
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("Subject: ", title);
        item.put("Name: ", id);
        return item;
    }

    public Example[] getObjMeta() {
        return this.objBooks;
    }

    public void showAlert(String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Result").setCancelable(true);
        //Log.wtf("CHECK CHECK", "CHECK_AUTH");
        alert.setMessage(msg);
        alert.create();
        alert.show();
    }
}
