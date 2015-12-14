package com.e_com.subin.boo_photo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivity2 extends ActionBarActivity  {

    final static private String APP_KEY = "vpsw8svp5soxwty";
    final static private String APP_SECRET = "jqmoqmvda82n6pd";
    private DropboxAPI<AndroidAuthSession> mDBApi;
    private DropboxAPI.UploadRequest mRequest;
    private static final int SELECT_IMAGE =1;
    Uri selectedImage;
    TextView user_msg,warn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        mDBApi.getSession().startOAuth2Authentication(MainActivity2.this);
        user_msg = (TextView)findViewById(R.id.textView);
        warn = (TextView)findViewById(R.id.warning);
        user_msg.setText("Please click to Open The gallery");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK&&requestCode==SELECT_IMAGE)
        {
            selectedImage = data.getData();
            String path = getPath(selectedImage);
            start(path);
        }
        warn.setText("!!!!!!!!!!!!!!!!Warning!!!!!!!!!!!!!!!!");
    user_msg.setText(" Data stored in your finger tip\n         Don't put it in mouth");

    }

    public String getPath(Uri uri)
    {
        String[] filepathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri,filepathColumn,null,null,null);
        cursor.moveToFirst();
        int ColumnIndex = cursor.getColumnIndex(filepathColumn[0]);
        return cursor.getString(ColumnIndex);
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

    public void start(final String path) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(path);

                try {
                    FileInputStream inputStream = new FileInputStream(file);
                    DropboxAPI.Entry response = mDBApi.putFile("/a.jpg", inputStream,
                            file.length(), null, null);
                } catch (FileNotFoundException | DropboxException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }


    public void upload_1(View view) {

        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, SELECT_IMAGE);
    }
}
