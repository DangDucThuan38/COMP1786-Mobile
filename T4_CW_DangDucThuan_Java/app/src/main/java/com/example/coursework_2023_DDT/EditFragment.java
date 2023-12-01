package com.example.coursework_2023_DDT;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;

import com.example.coursework_2023_DDT.data.HikeDB;
import com.example.coursework_2023_DDT.data.HikeEntity;
import com.example.coursework_2023_DDT.databinding.FragmentEditorBinding;

import java.util.Calendar;

public class EditFragment extends Fragment {

    private Switch aSwitch;
    private FragmentEditorBinding binding;
    private DatePickerDialog datePickerDialog;
    EditText editTextDate;
    HikeDB hikeDB;
    boolean parkCheck;
    SharedPreferences sharedPreferences;

    @Override
    public void onResume() {
        super.onResume();
        String[] hikeName = getResources().getStringArray(R.array.HikeName);
        binding.autoCompleteTextView.setAdapter(new ArrayAdapter<>(requireContext(),R.layout.dropdown_hikename, hikeName));
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        AppCompatActivity app = (AppCompatActivity)getActivity();
        ActionBar acbar = app.getSupportActionBar();
        acbar.setHomeButtonEnabled(true);
        acbar.setDisplayShowHomeEnabled(true);
        acbar.setDisplayHomeAsUpEnabled(true);
        acbar.setHomeAsUpIndicator(R.drawable.ic_outline_save_24);
        setHasOptionsMenu(true);

        binding = FragmentEditorBinding.inflate(inflater, container, false);

        hikeDB = new HikeDB(getContext());
        String idReceived = getArguments().getString("hike_id");
        Bundle bundleReceived = getArguments();

        if(idReceived != Convention.NEWSS_HIKE_IDSS){
            hikeDB.hike.setValue(hikeDB.getByID(idReceived));
        }

        hikeDB.hike.observe(
                getViewLifecycleOwner(),
                trip ->{
                    binding.autoCompleteTextView.setText(bundleReceived.getString("name"));
                    binding.parkSelected.setText(bundleReceived.getString("parking"));
                    binding.editDestination.setText(bundleReceived.getString("destination"));
                    binding.editDescription.setText(bundleReceived.getString("description"));
                    binding.editDate.setText(bundleReceived.getString("date_hike"));
                    binding.editParticipant.setText(Integer.toString(bundleReceived.getInt("participant")));
                    binding.editLocation.setText(bundleReceived.getString("location"));
                    binding.editLength.setText(bundleReceived.getString("length"));
                    binding.editLevel.setText(bundleReceived.getString("level"));
                    requireActivity().invalidateOptionsMenu();
                }
        );

        binding.parkSelected.setInputType(InputType.TYPE_NULL);
        aSwitch = binding.switchPark;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.parkSelected.setText("Parking: Yes");
                    sharedPreferences.edit().putBoolean("Notification", true).apply();
                } else {
                    binding.parkSelected.setText("Parking: No");
                    sharedPreferences.edit().putBoolean("Notification", false).apply();
                }
            }
        });
        parkCheck = sharedPreferences.getBoolean("Notification", true);
        aSwitch.setChecked(parkCheck);
        editTextDate = binding.editDate;
        editTextDate.setInputType(InputType.TYPE_NULL);
        editTextDate.setText(DateHandling.getTodayDate());
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatePicker();
                showDateDialog();
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
                if (this.validate()) return saveAndReturn() ;
                else return false;
            case R.id.action_delete:
                return deleteAndReturn();
            case R.id.observationDetail:
                return toExpense();
            default: return super.onOptionsItemSelected(item);
        }
    }
    private boolean toExpense() {
        Bundle bundleReceived = getArguments();
        Navigation.findNavController(getView()).navigate(R.id.observationFragment, bundleReceived);
        return true;
    }
    private boolean validate() {
        EditText name = binding.autoCompleteTextView;
        EditText date = binding.editDate;
        EditText destination = binding.editDestination;
        EditText parking= binding.parkSelected;
        EditText participant = binding.editParticipant;
        boolean isValidated = true;

        if (name.getText().toString().equals(Convention.EMPTYSS_STRINGSS)){
            name.setError("Name of the Hike is required");
            isValidated = false;
        }
        if (date.getText().toString().equals(Convention.EMPTYSS_STRINGSS)){
            date.setError("Date of the Hike is required");
            isValidated = false;
        }
        String participantText = participant.getText().toString();
        if (participantText.equals(Convention.EMPTYSS_STRINGSS) || Integer.parseInt(participantText) <= 0){
            participant.setError("Please enter complete information!");
            participant.setText("1");
            isValidated = false;
        }
        if (parking.getText().toString().equals(Convention.EMPTYSS_STRINGSS)){
            parking.setError("Please enter complete information!");
            binding.parkSelected.setText("Parking : No");
            isValidated = false;
        }
        if (destination.getText().toString().equals(Convention.EMPTYSS_STRINGSS)){
            destination.setError("Please enter complete information!");
            isValidated = false;
        }
        return isValidated;
    }

    //Delete
    private boolean deleteAndReturn() {
        Log.i(this.getClass().getName(), "Delete and return");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete this Hike");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hikeDB.delete(hikeDB.hike.getValue().getId());
                Navigation.findNavController(getView()).navigateUp();
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
        if(getArguments().getString("hike_id") != Convention.NEWSS_HIKE_IDSS){
            inflater.inflate(R.menu.hike_delete, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private boolean saveAndReturn() {
        Log.i(this.getClass().getName(), "Saved");

        HikeEntity hike = new HikeEntity();

        hike.setName(binding.autoCompleteTextView.getText().toString());
        hike.setLocation(binding.editLocation.getText().toString());
        hike.setParking(binding.parkSelected.getText().toString());
        hike.setDate(binding.editDate.getText().toString());
        hike.setParticipant(Integer.parseInt(binding.editParticipant.getText().toString()));
        hike.setDestination(binding.editDestination.getText().toString());
        hike.setDescription(binding.editDescription.getText().toString());
        hike.setLength(binding.editLength.getText().toString());
        hike.setLevel(binding.editLevel.getText().toString());


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Information of your Hike:");
        builder.setMessage("Name of the trip: " + binding.autoCompleteTextView.getText().toString() + "\n" +
                "Location: " + binding.editLocation.getText().toString() + "\n" +
                "Parking : " + binding.parkSelected.getText().toString() + "\n" +
                "Date of the Hike: " + binding.editDate.getText().toString() + "\n" +
                "Participant: "  + binding.editParticipant.getText().toString() + "\n" +
                "Destination: " + binding.editDestination.getText().toString() + "\n" +
                "Level: " + binding.editLevel.getText().toString() + "\n" +
                "Length: " + binding.editLength.getText().toString() + "\n" +
                "Description: " + binding.editDescription.getText().toString() + "\n" );
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (getArguments().getString("hike_id") != "0"){
                    hike.setId(getArguments().getString("hike_id"));
                    hikeDB.update(hike);
                }
                else{
                    hikeDB.insert(hike);
                }
                dialog.dismiss();
                Navigation.findNavController(getView()).navigateUp();
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


    //Initiate the date picker
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = DateHandling.makeDateString(dayOfMonth, month, year);
                binding.editDate.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(requireActivity(), style,dateSetListener, year, month, day);
    }

    private void showDateDialog() {
        datePickerDialog.show();
    }

}

