package com.example.loginauth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class BMIActivity extends AppCompatActivity {

    // Declare UI elements
    EditText weightEditText;
    EditText heightEditText;
    Button calculateBMIButton;
    TextView bmiResultText;

    EditText ageEditText;
    Button calculateWaterIntakeButton;
    TextView waterIntakeResultText;

    Button fetchResultsButton;
    TextView previousResultsText;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        db = FirebaseFirestore.getInstance();

        // Initialize UI elements for BMI
        weightEditText = findViewById(R.id.editTextWeight);
        heightEditText = findViewById(R.id.editTextHeight);
        calculateBMIButton = findViewById(R.id.calculateBMIButton);
        bmiResultText = findViewById(R.id.bmiResult);

        // Initialize UI elements for Daily Water Intake
        ageEditText = findViewById(R.id.editTextWeightWaterIntake);
        calculateWaterIntakeButton = findViewById(R.id.calculateWaterIntakeButton);
        waterIntakeResultText = findViewById(R.id.waterIntakeResult);

        // Initialize UI elements for fetching previous results
        fetchResultsButton = findViewById(R.id.fetchResultsButton);
        previousResultsText = findViewById(R.id.previousResultsText);

        // Set up BMI calculation
        calculateBMIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMI();
            }
        });

        // Set up Daily Water Intake calculation
        calculateWaterIntakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateWaterIntake();
            }
        });

        // Set up fetching previous results
        fetchResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchPreviousResults();
            }
        });
    }

    private void calculateBMI() {
        // Get user input for weight and height
        double weight = Double.parseDouble(weightEditText.getText().toString());
        double height = Double.parseDouble(heightEditText.getText().toString()) / 100; // Convert height to meters

        // Calculate BMI
        double bmi = weight / (height * height);

        // Display the result
        bmiResultText.setText("Your BMI: " + bmi);

        // Save BMI result to Firestore under "Users/calculations" document
        saveResultToFirestore("bmi", bmi);
    }

    private void calculateWaterIntake() {
        // Get user input for age
        int age = Integer.parseInt(ageEditText.getText().toString());

        // Calculate Daily Water Intake (a simple example, you may use a more accurate formula)
        double waterIntake = age * 0.03; // This is just a placeholder formula, you might want to use a more accurate one

        // Display the result
        waterIntakeResultText.setText("Your Daily Water Intake: " + waterIntake + " liters");

        // Save water intake result to Firestore
        saveResultToFirestore("waterIntake", waterIntake);
    }

    private void saveResultToFirestore(String resultType, double result) {
        // Create a map to store the result
        Map<String, Object> resultData = new HashMap<>();
        resultData.put("result", result);

        // Save the result to Firestore
        db.collection("calculations")
                .document(resultType)
                .set(resultData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BMIActivity.this, "Result saved to Firestore", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(BMIActivity.this, "Failed to save result to Firestore", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchPreviousResults() {
        // Fetch previous results from Firestore and display them
        db.collection("calculations")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            previousResultsText.setText("No previous results found.");
                        } else {
                            StringBuilder resultsBuilder = new StringBuilder();
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                String resultType = document.getId();
                                double result = document.getDouble("result");
                                resultsBuilder.append(resultType).append(": ").append(result).append("\n");
                            }
                            previousResultsText.setText(resultsBuilder.toString());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(BMIActivity.this, "Failed to fetch previous results from Firestore", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
