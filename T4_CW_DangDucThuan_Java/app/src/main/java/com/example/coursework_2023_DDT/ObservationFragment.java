package com.example.coursework_2023_DDT;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.coursework_2023_DDT.data.ObservationDB;
import com.example.coursework_2023_DDT.data.ObservationEntity;
import com.example.coursework_2023_DDT.databinding.FragmentObservationBinding;

public class ObservationFragment extends Fragment implements ObservationAdapter.ListObservationListener{

    private FragmentObservationBinding binding;
    private ObservationAdapter adapter1;
    ObservationDB ObservationDB;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        AppCompatActivity app = (AppCompatActivity)getActivity();
        ActionBar abcrs = app.getSupportActionBar();
        abcrs.setHomeButtonEnabled(false);
        abcrs.setDisplayShowHomeEnabled(false);
        abcrs.setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);

        binding = FragmentObservationBinding.inflate(inflater, container,  false);

        RecyclerView rv2 = binding.recyclerViewExpense;
        rv2.setHasFixedSize(true);  //each row has equal size regardless of its content
        rv2.addItemDecoration(new DividerItemDecoration(
                getContext(),
                (new LinearLayoutManager(getContext())).getOrientation())
        );
        String hikeId = getArguments().getString("hike_id");
        ObservationDB = new ObservationDB(getContext(), hikeId);
        ObservationDB.observationList.setValue(ObservationDB.getAll());

        ObservationDB.observationList.observe(
                getViewLifecycleOwner(),
                observationList -> {
                    adapter1 = new ObservationAdapter(observationList, this);
                    binding.recyclerViewExpense.setAdapter(adapter1);
                    binding.recyclerViewExpense.setLayoutManager(new LinearLayoutManager(getActivity()));
                    if(observationList.size() == 0){
                        rv2.setVisibility(View.GONE);
                    }
                    else{
                        rv2.setVisibility(View.VISIBLE);
                    }
                }

        );
        binding.fabAddObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundletest = new Bundle();
                bundletest.putString("observationID", Convention.NEWSS_HIKE_IDSS);
                bundletest.putString("hike_id", getArguments().getString("hike_id"));

                Navigation.findNavController(getView()).navigate(R.id.ObservationEditorFragment, bundletest);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onItemClick(ObservationEntity expenseInput) {
        Bundle bundletest = new Bundle();
        ObservationEntity observationss = ObservationDB.getByID(expenseInput.getO_ID());
        bundletest.putString("observationID", observationss.getO_ID());
        bundletest.putString("observation_name", observationss.getType());
        bundletest.putString("observation_date", observationss.getDate());
        bundletest.putString("hike_id", observationss.getH_ID());
        bundletest.putString("comment", observationss.getNotes());
        Navigation.findNavController(getView()).navigate(R.id.ObservationEditorFragment, bundletest);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.observation_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_home:
                return backHome();
            default: return super.onOptionsItemSelected(item);}
    }

    private boolean backHome() {
        Navigation.findNavController(getView()).navigate(R.id.mainFragment);
        return true;
    }
}