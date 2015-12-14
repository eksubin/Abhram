package com.e_com.subin.boo_photo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.DropBoxManager;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;


public class MainActivity3 extends ActionBarActivity {

    final static private String APP_KEY = "vpsw8svp5soxwty";
    final static private String APP_SECRET = "jqmoqmvda82n6pd";
    private DropboxAPI<AndroidAuthSession> mDBApi;
    private DropboxAPI.UploadRequest mRequest;
    File file;
    File file2;
    OutputStream outputStream,outputStream2;
    ImageView myview;
    private ProgressDialog progressDialog;
    RelativeLayout boo;
    int i =0;
    TextView info;
    Bitmap bJPGcompress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity3);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        mDBApi.getSession().startOAuth2Authentication(MainActivity3.this);
        myview = (ImageView) findViewById(R.id.cool_view);
        boo = (RelativeLayout) findViewById(R.id.blah);
        info = (TextView)findViewById(R.id.info);
        info.setText("Tap To transfer Data");
        boo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i == 0)                                                                                                                                                                                                                                                                                                  {
                    progressDialog = ProgressDialog.show(MainActivity3.this, "", "Loading");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            file = new File("/mnt/sdcard/d.jpg");
                            file2 = new File("/mnt/sdcard/"+new Random()+".jpg");
                            try {
                                outputStream = new FileOutputStream(file);
                                outputStream2 = new FileOutputStream(file2);
                                DropboxAPI.DropboxFileInfo info = mDBApi.getFile("/a.jpg", null, outputStream, null);
                                DropboxAPI.DropboxFileInfo info2 = mDBApi.getFile("/a.jpg", null, outputStream2, null);
                                messageHandler.sendEmptyMessage(0);
                                mDBApi.delete("/a.jpg");
                                i=1;
                            } catch (FileNotFoundException | DropboxException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
                }
                else
                    Toast.makeText(getApplicationContext(),"Dude stop clicking",Toast.LENGTH_SHORT).show();

                boo.setClickable(false);
                info.setText("");
            }

            private Handler messageHandler = new Handler() {
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/d.jp" +
                            "g");
                    myview.setRotation(325);
                    myview.setImageBitmap(bitmap);
                    progressDialog.dismiss();
                }
            };

        });
    }

    protected void onResume() {
        super.onResume();

        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                mDBApi.getSession().finishAuthentication();

                String accessToken = mDBApi.getSession().getOAuth2AccessToken();
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
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
            Intent intent= new Intent(getApplicationContext(),about.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


}

