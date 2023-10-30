package com.example.loginauth;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Spinner;

public class BMIActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        final EditText editTextHeight = findViewById(R.id.editTextHeight);
        final EditText editTextWeight = findViewById(R.id.editTextWeight);
        final TextView bmiResult = findViewById(R.id.bmiResult);
        Button calculateBMIButton = findViewById(R.id.calculateBMIButton);

        final EditText editTextAge = findViewById(R.id.editTextAge);
        final RadioGroup radioGroupGender = findViewById(R.id.radioGroupGender);
        final EditText editTextHeightInches = findViewById(R.id.editTextHeightInches);
        final EditText editTextWeightBMR = findViewById(R.id.editTextWeightBMR);
        final TextView bmrResult = findViewById(R.id.bmrResult);
        Button calculateBMRButton = findViewById(R.id.calculateBMRButton);

        // Find views for Daily Water Intake
        final EditText editTextWeightWaterIntake = findViewById(R.id.editTextWeightWaterIntake);
        final TextView waterIntakeResult = findViewById(R.id.waterIntakeResult);
        Button calculateWaterIntakeButton = findViewById(R.id.calculateWaterIntakeButton);

        // Find views for TDEE
        final EditText editTextAgeTDEE = findViewById(R.id.editTextAgeTDEE);
        final RadioGroup radioGroupGenderTDEE = findViewById(R.id.radioGroupGenderTDEE);
        final EditText editTextWeightTDEE = findViewById(R.id.editTextWeightTDEE);
        final EditText editTextHeightTDEE = findViewById(R.id.editTextHeightTDEE);
        final Spinner spinnerActivityLevel = findViewById(R.id.spinnerActivityLevel);
        final TextView tdeeResult = findViewById(R.id.tdeeResult);
        Button calculateTDEEButton = findViewById(R.id.calculateTDEEButton);

        // Define an array of activity levels
        String[] activityLevels = {"Sedentary", "Lightly Active", "Moderately Active", "Very Active", "Extremely Active"};

        // Create an ArrayAdapter for the Spinner
        ArrayAdapter<String> activityLevelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, activityLevels);
        activityLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter to the Spinner
        spinnerActivityLevel.setAdapter(activityLevelAdapter);

        calculateBMIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get input values
                String heightStr = editTextHeight.getText().toString();
                String weightStr = editTextWeight.getText().toString();

                if (heightStr.isEmpty() || weightStr.isEmpty()) {
                    // Handle validation errors, e.g., show a Toast message or an error dialog
                    // Return or inform the user that both height and weight are required.
                } else {
                    try {
                        float height = Float.parseFloat(heightStr);
                        float weight = Float.parseFloat(weightStr);

                        // Perform BMI calculation
                        float bmi = calculateBMI(height, weight);

                        // Display the calculated BMI
                        bmiResult.setText("Your BMI is: " + bmi);

                        // Style the result TextView
                        bmiResult.setTypeface(null, Typeface.BOLD);
                    } catch (NumberFormatException e) {
                        // Handle parsing errors, e.g., show a Toast message or an error dialog
                        // Return or inform the user that height and weight must be valid numbers.
                    }
                }
            }
        });

        calculateBMRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get input values (gender, age, height, weight)
                // Similar validation and error handling can be applied here.
                // Then, perform BMR calculation and display the result.
            }
        });

        calculateWaterIntakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String weightWaterIntakeStr = editTextWeightWaterIntake.getText().toString();

                if (weightWaterIntakeStr.isEmpty()) {
                    // Handle validation errors, e.g., show a Toast message or an error dialog
                    // Return or inform the user that weight for water intake is required.
                } else {
                    try {
                        float weightWaterIntake = Float.parseFloat(weightWaterIntakeStr);
                        float waterIntake = calculateWaterIntake(weightWaterIntake);

                        // Display the calculated daily water intake
                        waterIntakeResult.setText("Your Daily Water Intake is: " + waterIntake + " mL");

                        // Style the result TextView
                        waterIntakeResult.setTypeface(null, Typeface.BOLD);
                    } catch (NumberFormatException e) {
                        // Handle parsing errors, e.g., show a Toast message or an error dialog
                        // Return or inform the user that weight for water intake must be a valid number.
                    }
                }
            }
        });

        calculateTDEEButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get input values (gender, age, height, weight, activity level)
                int selectedGenderId = radioGroupGenderTDEE.getCheckedRadioButtonId();
                RadioButton selectedGender = findViewById(selectedGenderId);
                String gender = selectedGender.getText().toString();

                String ageStr = editTextAgeTDEE.getText().toString();
                String heightStrTDEE = editTextHeightTDEE.getText().toString();
                String weightStrTDEE = editTextWeightTDEE.getText().toString();
                String activityLevel = spinnerActivityLevel.getSelectedItem().toString();

                if (ageStr.isEmpty() || heightStrTDEE.isEmpty() || weightStrTDEE.isEmpty()) {
                    // Handle validation errors, e.g., show a Toast message or an error dialog
                    // Return or inform the user that all fields are required.
                } else {
                    try {
                        int age = Integer.parseInt(ageStr);
                        float height = Float.parseFloat(heightStrTDEE);
                        float weight = Float.parseFloat(weightStrTDEE);

                        // Perform TDEE calculation
                        float tdee = calculateTDEE(gender, age, height, weight, activityLevel);

                        // Display the calculated TDEE in the tdeeResult TextView
                        tdeeResult.setText("Your Total Daily Energy Expenditure (TDEE) is: " + tdee + " calories");

                        // Style the result TextView
                        tdeeResult.setTypeface(null, Typeface.BOLD);
                    } catch (NumberFormatException e) {
                        // Handle parsing errors, e.g., show a Toast message or an error dialog
                        // Return or inform the user that age, height, and weight must be valid numbers.
                    }
                }
            }
        });
    }

    private float calculateTDEE(String gender, int age, float height, float weight, String activityLevel) {
        // Calculate TDEE based on the appropriate formula for the chosen gender
        float bmr;

        if (gender.equals("Male")) {
            bmr = 88.362f + (13.397f * weight) + (4.799f * height) - (5.677f * age);
        } else {
            bmr = 447.593f + (9.247f * weight) + (3.098f * height) - (4.330f * age);
        }

        // Define activity level multipliers based on different activity levels
        float activityMultiplier;

        if (activityLevel.equals("Sedentary")) {
            activityMultiplier = 1.2f;
        } else if (activityLevel.equals("Lightly Active")) {
            activityMultiplier = 1.375f;
        } else {
            activityMultiplier = 1.0f;
        }
        float tdee = bmr * activityMultiplier;

        return tdee;
    }

    private float calculateWaterIntake(float weight) {
        // Calculate daily water intake based on weight (in kilograms)
        // The general recommendation is to drink at least 30-35 mL of water per kilogram of body weight.
        return weight * 35; // You can adjust the multiplier as needed.
    }

    private float calculateBMI(float height, float weight) {
        // Calculate BMI based on height and weight
        float heightInMeters = height / 100; // Convert height to meters
        return weight / (heightInMeters * heightInMeters);
    }
}
