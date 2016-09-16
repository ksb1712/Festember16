package com.mygdx.game;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class QR extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        TextView welcomeText = (TextView) findViewById(R.id.welcomeText);
        welcomeText.setText(welcomeText.getText().toString() + Utilities.username);
        if (Utilities.qr_status == 2) {
            ImageView qrCodeImage = (ImageView) findViewById(R.id.qr_code_image);
            qrCodeImage.setVisibility(View.VISIBLE);
            Bitmap bitmap = new SaveImage(Utilities.username, null).loadFromCacheFile();
            if (bitmap == null) {

                //TODO api call
            }
            else {
                qrCodeImage.setImageBitmap(bitmap);
            }
        }
    }

    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            //Logout
/*
            SharedPreferences.Editor editor = Utilities.prefs.edit();
            editor.putInt("status", 0);
            editor.putString("user_name", null);
            editor.putString("user_pass", null);
            editor.apply();
*/
            Utilities.username = Utilities.username = null;
//            Intent intent = new Intent(QRCodeWelcomePage.this, QRCodeLogin.class);
//            startActivity(intent);
//            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (Utilities.qr_status != 2)
            super.onBackPressed();
        else finish();
    }




}
