package com.example.applogbook3.apdater;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.applogbook3.R;
import com.example.applogbook3.model.Data;

import java.util.List;

public class Adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Data> dataList;

    public Adapter(Activity activity, List<Data> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_users, null);
        }

        TextView name = convertView.findViewById(R.id.edit_name);
        TextView date = convertView.findViewById(R.id.edit_date);
        TextView email = convertView.findViewById(R.id.edit_email);
        ImageView imageView = convertView.findViewById(R.id.imageView); // Thêm ImageView

        Data data = dataList.get(position);
        name.setText(data.getName());
        email.setText(data.getEmail());
        date.setText(data.getDate());

        // Hiển thị ảnh nếu có
        if (data.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data.getImage(), 0, data.getImage().length);
            imageView.setImageBitmap(bitmap);
        }

        return convertView;
    }
}
