package com.example.coursework_2023_DDT;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.coursework_2023_DDT.data.ObservationDB;
import com.example.coursework_2023_DDT.data.ObservationEntity;
import com.example.coursework_2023_DDT.databinding.FragmentObservationEditBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;

public class ObservationEditFragment extends Fragment {
    private FragmentObservationEditBinding binding;
    private DatePickerDialog datePickerDialog;
    EditText editTextDate;
    ObservationDB observationDB;
    FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    public void onResume() {
        super.onResume();
        String[] observationName = getResources().getStringArray(R.array.ObservationName);
        binding.autoCompleteTextViewObservation.setAdapter(new ArrayAdapter<>(requireContext(), R.layout.dropdown_hikename, observationName));
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        AppCompatActivity app = (AppCompatActivity) getActivity();
        ActionBar acbars = app.getSupportActionBar();
        acbars.setHomeButtonEnabled(true);
        acbars.setDisplayShowHomeEnabled(true);
        acbars.setDisplayHomeAsUpEnabled(true);
        acbars.setHomeAsUpIndicator(R.drawable.ic_outline_save_24);
        setHasOptionsMenu(true);

        binding = FragmentObservationEditBinding.inflate(inflater, container, false);

        String hikeId = getArguments().getString("hike_id");
        Bundle bundleReceived = getArguments();


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        observationDB = new ObservationDB(getContext(), hikeId);
        String observationIdReceived = getArguments().getString("observationID");

        if (!observationIdReceived.equals(Convention.NEWSS_HIKE_IDSS)) {
            observationDB.observation.setValue(observationDB.getByID(observationIdReceived));
        }
        observationDB.observation.observe(
                getViewLifecycleOwner(),
                expense -> {
                    binding.autoCompleteTextViewObservation.setText(bundleReceived.getString("observation_name"));
                    binding.editDateObservation.setText(bundleReceived.getString("observation_date"));
                    binding.comment.setText(bundleReceived.getString("comment"));
                    requireActivity().invalidateOptionsMenu();
                }
        );

        editTextDate = binding.editDateObservation;
        editTextDate.setInputType(InputType.TYPE_NULL);
        editTextDate.setText(DateHandling.getTodayDate());
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatePicker();
                showDateDialog(editTextDate);
            }
        });
        return binding.getRoot();
    }




    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (this.observationValidate()) return observationSaveAndReturn() ;
                else return false;
            case R.id.action_delete_observation:
                return ObservationDeleteAndReturn();
            default: return super.onOptionsItemSelected(item);
        }
    }

    private boolean ObservationDeleteAndReturn() {
        Log.i(this.getClass().getName(), "Delete and return");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete observation of Hike");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                observationDB.delete(observationDB.observation.getValue().getO_ID());
                Navigation.findNavController(getView()).navigateUp();
            }
        });
//        builder.show();
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

        return true;
    }

    private boolean observationSaveAndReturn() {
        Log.i(this.getClass().getName(), "Saved and return");
        ObservationEntity observation = new ObservationEntity();
        observation.setType(binding.autoCompleteTextViewObservation.getText().toString());
        observation.setDate(binding.editDateObservation.getText().toString());
        observation.setNotes(binding.comment.getText().toString());
        observation.setH_ID(getArguments().getString("hike_id"));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Information of Observation of the Hike:");
        builder.setMessage("Type: "+binding.autoCompleteTextViewObservation.getText().toString() + "\n" +
                "Date: "+binding.editDateObservation.getText().toString() + "\n" +
                "Comment: " + binding.comment.getText().toString() + "\n");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (getArguments().getString("observationID") != "0") {
                    observation.setO_ID(getArguments().getString("observationID"));
                    observationDB.update(observation);
                    dialog.dismiss();
                    Navigation.findNavController(getView()).navigateUp();
                }
                else{
                    observationDB.insert(observation);
                    dialog.dismiss();
                    Navigation.findNavController(getView()).navigateUp();
                }
            }
        });
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
        return true;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if(getArguments().getString("observationID") != Convention.NEWSS_HIKE_IDSS){
            inflater.inflate(R.menu.observation_delete, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private boolean observationValidate() {
        EditText name = binding.autoCompleteTextViewObservation;
        EditText date = binding.editDateObservation;
        boolean isValidated = true;

        if (name.getText().toString().equals(Convention.EMPTYSS_STRINGSS)){
            name.setError("Name Observation is required");
            isValidated = false;
        }
        if (date.getText().toString().equals(Convention.EMPTYSS_STRINGSS)){
            date.setError("Date Observation is required");
            isValidated = false;
        }

        return isValidated;
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = DateHandling.makeDateString(dayOfMonth, month, year);
                binding.editDateObservation.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(requireContext(), style, dateSetListener, year, month, day);
    }

    private void showDateDialog(EditText editTextDate) {
        datePickerDialog.show();
    }
}

