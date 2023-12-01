package com.example.coursework_2023_DDT;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework_2023_DDT.data.ObservationEntity;
import com.example.coursework_2023_DDT.databinding.ListObservationBinding;

import java.util.List;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ObservationViewHolder> {
    @NonNull
    @Override
    public ObservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_observation, parent, false);
        return new ObservationAdapter.ObservationViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ObservationViewHolder holder, int position) {
        ObservationEntity e = observationList.get(position);
        holder.bindData(e);
    }
    @Override
    public int getItemCount() {
        return observationList.size();
    }

    public interface ListObservationListener{
        void onItemClick(ObservationEntity observationId);
    }
    public class ObservationViewHolder extends RecyclerView.ViewHolder{

        private final ListObservationBinding itemViewBinding;

        public ObservationViewHolder(View itemView) {
            super(itemView);
            itemViewBinding = ListObservationBinding.bind(itemView);
        }

        public void bindData(ObservationEntity tData){
            itemViewBinding.observationName.setText(tData.getType());
            itemViewBinding.getRoot().setOnClickListener(
                    v -> {
                        listener.onItemClick(tData);
                    }
            );
        }
    }

    private List<ObservationEntity> observationList;
    private ListObservationListener listener;

    public ObservationAdapter(List<ObservationEntity> observationList, ListObservationListener listener) {
        this.observationList = observationList;
        this.listener = listener;
    }

}
