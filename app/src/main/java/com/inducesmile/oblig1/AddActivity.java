package com.inducesmile.oblig1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inducesmile.oblig1.database.AppDatabase;
import com.inducesmile.oblig1.database.QuizItem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddActivity extends AppCompatActivity {
    private ImageView cameraImage = null;
    private ArrayList<QuizItem> addDatabase = MainActivity.quizData;
    private EditText savePicName = null;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int RESULT_LOAD_RESULT = 2;
    private Button cam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        cam = (Button) findViewById(R.id.button_camera);
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quiz:
                Intent intentQuiz = new Intent(AddActivity.this, QuizActivity.class);
                startActivity(intentQuiz);
                return true;

            case R.id.home:
                Intent intentAdd = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intentAdd);
                return true;

            case R.id.database:
                Intent intentDatabase = new Intent(AddActivity.this, DatabaseActivity.class);
                startActivity(intentDatabase);
                return true;

            case R.id.preferences:
                Intent intentPref = new Intent(AddActivity.this, Preferences.class);
                startActivity(intentPref);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cameraImage = (ImageView) findViewById(R.id.camera_imageView);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            cameraImage.setImageBitmap(imageBitmap);

        } else if (requestCode == RESULT_LOAD_RESULT && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            cameraImage.setImageBitmap(imageBitmap);
        }

    }


    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void savePic(View view) {
        QuizActivity.hideKeyboard(this);
        if (cameraImage != null) {
            BitmapDrawable drawable = (BitmapDrawable) cameraImage.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
           Log.i("Bitten", ""+bitmap.getByteCount());
            if (bitmap.getByteCount() > 2000000){
                compressImage(bitmap);
                Log.i("storring1", ""+compressImage(bitmap));
            }
            savePicName = (EditText) findViewById(R.id.save_editText);
            Log.i("Status", "" + savePicName.getText().toString());
            if (!savePicName.getText().toString().equals("")) {
                byte[] bytes = DbBitmapUtility.getBytes(compressImage(bitmap));
               Log.i("storring ", ""+bytes.length);



                QuizItem newItem = new QuizItem(0, savePicName.getText().toString(), bytes);

                Asyncclass asyncTask = new Asyncclass(getApplicationContext(), newItem);
                        asyncTask.execute();

                addDatabase.add(newItem);
                Toast.makeText(this, "Bildet ble lagret", Toast.LENGTH_SHORT).show();
                savePicName.setText("");
            } else {
                Toast.makeText(this, "Vennligst gi bildet ett navn", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Du må først velge ett bilde", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadExistingPhoto(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_RESULT);

    }

    private static class Asyncclass extends AsyncTask<Void, Void, Void> {

        private Context context;
        QuizItem quizItem;

        Asyncclass (Context context, QuizItem quizItem){
            this.context = context;
            this.quizItem = quizItem;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AppDatabase db = Room.databaseBuilder(context,
                    AppDatabase.class, "quiz").build();
            db.quizDao().addItem(quizItem);
            return null;
        }
    }

    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 10, baos);//Compression quality, here 100 means no compression, the storage of compressed data to baos
        int options = 90;
        while (baos.toByteArray().length / 1024 > 400) {  //Loop if compressed picture is greater than 400kb, than to compression
            baos.reset();//Reset baos is empty baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//The compression options%, storing the compressed data to the baos
            options -= 10;//Every time reduced by 10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//The storage of compressed data in the baos to ByteArrayInputStream
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//The ByteArrayInputStream data generation
        return bitmap;
    }
}
