package com.example.loginauth;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private EditText phoneNumberEditText;
    private EditText dateOfBirthEditText;
    private EditText allergiesEditText;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        dateOfBirthEditText = findViewById(R.id.dateOfBirthEditText);
        allergiesEditText = findViewById(R.id.allergiesEditText);
        maleRadioButton = findViewById(R.id.maleRadioButton);
        femaleRadioButton = findViewById(R.id.femaleRadioButton);
        saveButton = findViewById(R.id.saveButton);

        // Populate the year spinner
        Spinner yearSpinner = findViewById(R.id.yearSpinner);
        List<String> years = getYears();
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        // Populate the month spinner
        Spinner monthSpinner = findViewById(R.id.monthSpinner);
        List<String> months = getMonths();
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        // Populate the day spinner
        Spinner daySpinner = findViewById(R.id.daySpinner);
        List<String> days = getDays();
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, days);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getgender = maleRadioButton.isChecked() ? "Male" : "Female";
                String getphoneNumber = phoneNumberEditText.getText().toString();
                String getdateOfBirth = dateOfBirthEditText.getText().toString();
                String getallergies = allergiesEditText.getText().toString();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("gender", getgender);
                hashMap.put("phoneNumber", getphoneNumber);
                hashMap.put("dateOfBirth", getdateOfBirth);
                hashMap.put("allergies", getallergies);

                FirebaseFirestore.getInstance().collection("User")
                        .document("User Data")
                        .set(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ProfileActivity.this, "Data saved Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private List<String> getYears() {
        List<String> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int year = currentYear; year >= 1900; year--) {
            years.add(String.valueOf(year));
        }
        return years;
    }

    private List<String> getMonths() {
        List<String> months = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            String monthString = String.format("%02d", month);
            months.add(monthString);
        }
        return months;
    }

    private List<String> getDays() {
        List<String> days = new ArrayList<>();
        for (int day = 1; day <= 31; day++) {
            String dayString = String.format("%02d", day);
            days.add(dayString);
        }
        return days;
    }
}
