package com.example.coursework_2023_DDT;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.coursework_2023_DDT.data.HikeDB;
import com.example.coursework_2023_DDT.data.HikeEntity;
import com.example.coursework_2023_DDT.databinding.FragmentMainBinding;

public class MainFragment extends Fragment implements HikeAdapter.ListHikeListener{
    private FragmentMainBinding binding;
    private HikeAdapter adapter;
    HikeDB hikeDB;
    SearchView searchView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        AppCompatActivity app = (AppCompatActivity)getActivity();
        ActionBar ab = app.getSupportActionBar();
        ab.setHomeButtonEnabled(false);
        ab.setDisplayShowHomeEnabled(false);
        ab.setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);
        binding = FragmentMainBinding.inflate(inflater, container,  false);
        hikeDB = new HikeDB(getContext());
        hikeDB.hikeList.setValue(hikeDB.getAll());
        RecyclerView rv = binding.recyclerView;
        rv.setHasFixedSize(true);  //each row has equal size regardless of its content
        rv.addItemDecoration(new DividerItemDecoration(
                getContext(),
                (new LinearLayoutManager(getContext())).getOrientation())
        );
        binding.DeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteAllClick(v);
            }
        });
        hikeDB.hikeList.observe(
                getViewLifecycleOwner(),
                hikeList -> {
                    adapter = new HikeAdapter(hikeList, this);
                    binding.recyclerView.setAdapter(adapter);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                }
        );
        binding.fabAddnewHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundletest = new Bundle();
                bundletest.putString("hike_id", Convention.NEWSS_HIKE_IDSS);
                bundletest.putString("name", Convention.NEWSS_HIKE_IDSS);
                bundletest.putString("destination", Convention.NEWSS_HIKE_IDSS);
                bundletest.putString("date_hike", Convention.NEWSS_HIKE_IDSS);
                bundletest.putInt("participant", 1);
                bundletest.putString("description", Convention.NEWSS_HIKE_IDSS);
                bundletest.putString("parking", Convention.NEWSS_HIKE_IDSS);
                bundletest.putString("length", Convention.NEWSS_HIKE_IDSS);
                bundletest.putString("level", Convention.NEWSS_HIKE_IDSS);
                bundletest.putString("location", Convention.NEWSS_HIKE_IDSS);

                Navigation.findNavController(getView()).navigate(R.id.editorFragment, bundletest);
            }
        });

        return binding.getRoot();
    }

    public void onDeleteAllClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm deletion all");
        builder.setMessage("Are you sure you want to delete all Hike?");
        builder.setPositiveButton("Delete All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hikeDB.deleteAll();
                hikeDB.hikeList.setValue(hikeDB.getAll());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(this.getClass().getName(), "On Resume");
        hikeDB.getAll();
    }
    @Override
    public void onItemClick(HikeEntity hikeInput) {
        HikeEntity hike = hikeDB.getByID(hikeInput.getId());
        Bundle bundletest = new Bundle();
        bundletest.putString("hike_id", hike.getId());
        bundletest.putString("name",hike.getName());
        bundletest.putString("destination",hike.getDestination());
        bundletest.putString("date_hike",hike.getDate());
        bundletest.putInt("participant", hike.getParticipant());
        bundletest.putString("description",hike.getDescription());
        bundletest.putString("parking",hike.getParking());
        bundletest.putString("location", hike.getLocation());
        bundletest.putString("length", hike.getLength());
        bundletest.putString("level", hike.getLevel());
        Navigation.findNavController(getView()).navigate(R.id.editorFragment, bundletest);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                hikeDB.search(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                hikeDB.search(newText);
                return false;
            }
        });
    }
}