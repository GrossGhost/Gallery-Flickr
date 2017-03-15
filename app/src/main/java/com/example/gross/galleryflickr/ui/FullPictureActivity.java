package com.example.gross.galleryflickr.ui;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gross.galleryflickr.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;


public class FullPictureActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imageView;
    private String folderToSave = Environment.getExternalStorageDirectory().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_pic);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        toolbar.getBackground().setAlpha(50);


        Intent intent = getIntent();

        String url = intent.getExtras().getString("url");

        imageView = (ImageView) findViewById(R.id.full_pic_view);
        Picasso.with(this)
                .load(url)
                .into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_full_pic,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menuSavePic){
            SavePicture(imageView,folderToSave);
        }
        return super.onOptionsItemSelected(item);
    }

    private String SavePicture(ImageView iv, String folderToSave)
    {

        OutputStream fOut = null;
        Calendar c = Calendar.getInstance();

        try {
            File file = new File(folderToSave, "pic_" + c.getTime().toString() +".jpg"); // создать уникальное имя для файла основываясь на дате сохранения
            fOut = new FileOutputStream(file);

            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut); // сохранять картинку в jpeg-формате с 100% сжатия.
            fOut.flush();
            fOut.close();
            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(),  file.getName()); // регистрация в фотоальбоме
        }
        catch (Exception e) // здесь необходим блок отслеживания реальных ошибок и исключений, общий Exception приведен в качестве примера
        {
            return e.getMessage();
        }
        Toast toast = Toast.makeText(getApplicationContext(),
                "SAVED", Toast.LENGTH_SHORT);
        toast.show();
        return "";
    }
}
