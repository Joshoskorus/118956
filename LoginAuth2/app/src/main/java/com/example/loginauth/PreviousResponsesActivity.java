package com.example.loginauth;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PreviousResponsesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_responses);

        // Initialize UI elements
        TextView previousResponsesText = findViewById(R.id.previousResponsesText);

        // Fetch and display previous responses
        fetchPreviousResponses(previousResponsesText);
    }

    private void fetchPreviousResponses(final TextView previousResponsesText) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Diet")
                .document("Recommendations")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String previousResponse = documentSnapshot.getString("result");
                            // Display previous response in previousResponsesText
                            previousResponsesText.setText(previousResponse);
                        } else {
                            // No previous response found
                            // Handle this case, for example, by displaying a message
                            previousResponsesText.setText("No previous response found.");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // Handle failure to fetch previous responses
                        previousResponsesText.setText("Failed to fetch previous response from Firestore");
                    }
                });
    }
}
