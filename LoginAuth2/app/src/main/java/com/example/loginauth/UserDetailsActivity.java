package com.example.loginauth;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UserDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        // Retrieve user email from the intent
        String userEmail = getIntent().getStringExtra("userEmail");

        // Example: Display user email in a TextView
        TextView userEmailTextView = findViewById(R.id.userEmailTextView);
        userEmailTextView.setText("User Email: " + userEmail);

        // Retrieve and display diet recommendations
        retrieveAndDisplayDietRecommendations(userEmail);
    }

    private void retrieveAndDisplayDietRecommendations(String userEmail) {
        // TODO: Implement logic to retrieve diet recommendations from Firebase based on the user's email
        // Update the corresponding TextView with the diet recommendations
        // Example: Update the diet recommendations TextView
        TextView dietRecommendationsTextView = findViewById(R.id.dietRecommendationsTextView);

        // Replace "getDietRecommendationsByEmail" with the actual method to retrieve diet recommendations
        String dietRecommendations = getDietRecommendationsByEmail(userEmail);
        dietRecommendationsTextView.setText("Diet Recommendations: " + dietRecommendations);
    }

    // TODO: Implement the actual method to retrieve diet recommendations from Firebase
    private String getDietRecommendationsByEmail(String userEmail) {
        // Example: Replace with the actual logic to query Firebase for diet recommendations
        // In a real scenario, you would query the Firebase database to retrieve the diet recommendations for the user
        // based on their email.

        // For demonstration purposes, a static diet recommendation is returned.
        return "Sample diet recommendations for user " + userEmail;
    }
}
