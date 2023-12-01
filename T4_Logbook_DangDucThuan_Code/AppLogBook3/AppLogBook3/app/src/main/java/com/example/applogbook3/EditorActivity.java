package com.example.applogbook3;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.applogbook3.helper.DBHelper;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;

public class EditorActivity extends AppCompatActivity {
    private EditText edtname, edtemail, edtdate;
    private Button btnSave, selectImg;
    private ImageView imgView;
    private DBHelper db = new DBHelper(this);

    private String id, name, email, date;
    private byte[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        edtname = findViewById(R.id.edt_name);
        edtemail = findViewById(R.id.edt_email);
        edtdate = findViewById(R.id.edt_date);
        btnSave = findViewById(R.id.btn_save);
        imgView = findViewById(R.id.iv_profile_image);
        selectImg = findViewById(R.id.btn_select_image);

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        date = getIntent().getStringExtra("date");
        image = getIntent().getByteArrayExtra("image");

        if (id == null || id.equals("")) {
            setTitle("Add");
        } else {
            setTitle("Edit");
            edtname.setText(name);
            edtdate.setText(date);
            edtemail.setText(email);
            if (image != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                imgView.setImageBitmap(bitmap);
            }
        }

        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryForImage();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (id == null || id.equals("")) {
                        save();
                    } else {
                        edit();
                    }
                } catch (Exception ex) {
                    if (ex != null) {
                        Log.e("Saving", ex.getMessage());
                    } else {
                        Log.e("Saving", "An exception occurred, but the exception object is null.");
                    }
                }
            }
        });
    }

    private void openGalleryForImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            try {
                // Get the selected image and display it
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgView.setImageBitmap(bitmap);

                // Convert the bitmap to a byte array for storage
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                image = stream.toByteArray();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void save() {
        String nameValue = edtname.getText().toString();
        String emailValue = edtemail.getText().toString();
        String dateValue = edtdate.getText().toString();

        if (nameValue.isEmpty() || emailValue.isEmpty() || dateValue.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            db.insert(nameValue, emailValue, dateValue, image);
            finish();
        }
    }

    private void edit() {
        String nameValue = edtname.getText().toString();
        String emailValue = edtemail.getText().toString();
        String dateValue = edtdate.getText().toString();

        if (nameValue.isEmpty() || emailValue.isEmpty() || dateValue.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            db.update(Integer.parseInt(id), nameValue, emailValue, dateValue, image);
            finish();
        }
    }

    public void showDatePicker(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                edtdate.setText(selectedDate);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
        datePickerDialog.show();
    }
}
