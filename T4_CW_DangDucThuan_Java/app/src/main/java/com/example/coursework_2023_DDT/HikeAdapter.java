package com.example.coursework_2023_DDT;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework_2023_DDT.data.HikeEntity;
import com.example.coursework_2023_DDT.databinding.ListHikeBinding;

import java.util.List;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.HikeViewHolder> {

    public interface ListHikeListener{
        void onItemClick(HikeEntity hikeId);
    }

    private List<HikeEntity> hikes;
    private ListHikeListener listener;
    public HikeAdapter(List<HikeEntity> hikeList, ListHikeListener listener) {
        this.hikes = hikeList;
        this.listener = listener;
    }
    @NonNull
    @Override
    public HikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_hike, parent, false);
        return new HikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HikeViewHolder holder, int position) {
        HikeEntity tData = hikes.get(position);
        holder.bindData(tData);
    }

    @Override
    public int getItemCount() {
        return hikes.size();
    }

    public class HikeViewHolder extends RecyclerView.ViewHolder{

        private final ListHikeBinding itemViewBinding;

        public HikeViewHolder(View itemView) {
            super(itemView);
            itemViewBinding = ListHikeBinding.bind(itemView);
        }

        public void bindData(HikeEntity tData){
            itemViewBinding.HikeName.setText(tData.getName() + "\n" + tData.getDate());
            itemViewBinding.getRoot().setOnClickListener(
                    v -> {
                        listener.onItemClick(tData);
                    }
            );
        }
    }



}
