package com.example.applogbook3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.applogbook3.apdater.Adapter;
import com.example.applogbook3.helper.DBHelper;
import com.example.applogbook3.model.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    AlertDialog.Builder dialog;
    List<Data> list = new ArrayList<>();
    Adapter adapter;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(getApplicationContext());
        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, EditorActivity.class);
            startActivity(intent);
        });

        listView = findViewById(R.id.list_item);
        adapter = new Adapter(MainActivity.this, list);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener((adapterView, view, position, l) -> {
            final String id = list.get(position).getId();
            final String name = list.get(position).getName();
            final String date = list.get(position).getDate();
            final String email = list.get(position).getEmail();
            final CharSequence[] dialogItem = {"Edit", "Delete"};
            dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setItems(dialogItem, (dialogInterface, i) -> {
                switch (i) {
                    case 0:
                        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("name", name);
                        intent.putExtra("date", date);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        break;
                    case 1:
                        db.delete(Integer.parseInt(id));
                        list.remove(position); // Remove the item from the list
                        adapter.notifyDataSetChanged();
                        break;
                }
            }).show();
            return false;
        });

        getData();
    }

    private void getData() {
        list.clear(); // Clear the list before adding data
        ArrayList<HashMap<String, String>> rows = db.getAll();
        for (int i = 0; i < rows.size(); i++) {
            String id = rows.get(i).get("id");
            String name = rows.get(i).get("name");
            String email = rows.get(i).get("email");
            String date = rows.get(i).get("date");
            byte[] image = db.getImage(Integer.parseInt(id)); // Get image from database
            Data data = new Data(id, name, email, date, image);
            list.add(data);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(); // Refresh the data when the activity is resumed
    }
}
