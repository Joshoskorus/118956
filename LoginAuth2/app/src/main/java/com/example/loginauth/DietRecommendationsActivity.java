package com.example.loginauth;



import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.BuildConfig;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.loginauth.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.collect.HashBiMap;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;


public class DietRecommendationsActivity extends AppCompatActivity {

    // Declare UI elements
    EditText allergiesEditText;
    EditText healthGoalsEditText;
    EditText dietaryPreferencesEditText;
    Button submitButton;
    TextView dietRecommendationsText;

    EditText diagnosedDiseaseEditText;

    Button exportPdfButton;

    Button exportToPdf;

    FirebaseFirestore db;




    // Define your OpenAI API key
   // private static final String OPENAI_API_KEY = "ac46f8e8e6a24dd09be028b98c9e5f4b";
    //private static final String OPENAI_API_URL = "https://api.openai.com/v1/completions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_recommendations);

        db= FirebaseFirestore.getInstance();

        // Initialize UI elements
        allergiesEditText = findViewById(R.id.allergiesEditText);
        healthGoalsEditText = findViewById(R.id.healthGoalsEditText);
        dietaryPreferencesEditText = findViewById(R.id.dietaryPreferencesEditText);
        submitButton = findViewById(R.id.submitButton);
        dietRecommendationsText = findViewById(R.id.dietRecommendationsText);
        exportPdfButton = findViewById(R.id.exportPdfButton);
        diagnosedDiseaseEditText=findViewById(R.id.diagnosedDiseaseEditText);


        // Set onClickListener for the exportPdfButton


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input from each field
                String allergiesInput = allergiesEditText.getText().toString();
                String healthGoalsInput = healthGoalsEditText.getText().toString();
                String dietaryPreferencesInput = dietaryPreferencesEditText.getText().toString();
                String diagInput = diagnosedDiseaseEditText.getText().toString();
                String question= "Allergies: " + allergiesInput + "\nHealth Goals: " + healthGoalsInput + "\nDietary Preferences: " + dietaryPreferencesInput +"\nDiagnosedDisease: " + diagInput+
                        "\nRecommend a very short and brief  suitable diet for me in point form less than 5 lines .";


                sendRequest(question);

                Snackbar.make(findViewById(android.R.id.content), "Just a moment, we are processing your diet", Snackbar.LENGTH_LONG).show();

                // Call the OpenAI API to generate diet recommendations

            }
        });

        //  button to navigate to PreviousResponsesActivity
        // button to navigate to PreviousResponsesActivity
        Button viewPreviousResponsesButton = findViewById(R.id.viewPreviousResponsesButton);
        viewPreviousResponsesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to fetch and display previous responses
                fetchPreviousResponses();

                // Create an Intent to start PreviousResponsesActivity
                Intent intent = new Intent(DietRecommendationsActivity.this, PreviousResponsesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void exportToPdf() {
        // Get the diet recommendations text
        String dietRecommendations = dietRecommendationsText.getText().toString();

        // Step 3: Ensure the directory exists
        File pdfDirectory = getPublicStorageDirectory("DietPDFs");
        if (pdfDirectory != null) {
            // Step 4: Create a PDF file
            File pdfFile = createPdfFile(pdfDirectory, "DietRecommendations.pdf");

            // Step 5: Write text to PDF
            writeTextToPdf(pdfFile, dietRecommendations);

            // Inform the user that the PDF has been exported
            Toast.makeText(this, "Diet recommendations exported to PDF", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to create directory", Toast.LENGTH_SHORT).show();
        }
    }

    // Step 3: Ensure the directory exists
    private File getPublicStorageDirectory(String directoryName) {
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), directoryName);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                // Handle directory creation failure
                return null;
            }
        }
        return directory;
    }

    // Step 4: Create a PDF file
    private File createPdfFile(File directory, String fileName) {
        return new File(directory, fileName);
    }

    // Step 5: Write text to PDF
    private void writeTextToPdf(File pdfFile, String text) {
        try (PdfWriter pdfWriter = new PdfWriter(new FileOutputStream(pdfFile))) {
            // Create a PDF document
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);

            // Add the diet recommendations text to the PDF
            document.add(new Paragraph("Diet Recommendations:\n\n" + text));

            // Close the document
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to export diet recommendations to PDF", Toast.LENGTH_SHORT).show();
        }
    }


    public void sendRequest(String request){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.openai.com/v1/completions";

        JSONObject jsonObject =new JSONObject();
        try {
            jsonObject.put("model", "gpt-3.5-turbo-instruct-0914");
            jsonObject.put("prompt", request);
            jsonObject.put("max_tokens",  800);






// Request a string response from the provided URL.
       JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String answer = null;
                        try {
                            answer = response.getJSONArray("choices").getJSONObject(0).getString("text");

                            //Save the result to Firestore
                            saveResultToFirestore(answer);

                            // Display the first 500 characters of the response string.
                            dietRecommendationsText.setText(answer);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dietRecommendationsText.setText("That didn't work!");
            }

        }){
           @Override
           public Map<String, String> getHeaders() throws AuthFailureError {
               HashMap<String,String> map =new HashMap<>();
               map.put("Content-Type", "application/json");
              map.put("Authorization", " Bearer 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");

               return map;
           }
       };

       jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
           @Override
           public int getCurrentTimeout() {
               return 60000;
           }

           @Override
           public int getCurrentRetryCount() {
               return 6;
           }

           @Override
           public void retry(VolleyError error) throws VolleyError {

           }
       });

/// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }



    }


    private void saveResultToFirestore(String result) {
        // Create a map to store the result
        Map<String, Object> resultData = new HashMap<>();
        resultData.put("result", result);

        // Save the result to Firestore under "dietRecommendations/recommendations" document
        db.collection("Diet")
                .document("Recommendations")
                .set(resultData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Successfully saved to Firestore
                        // You can add any additional logic or feedback here
                        Toast.makeText(DietRecommendationsActivity.this, "Result saved to Firestore", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // Failed to save to Firestore
                        // You can add any error handling logic here
                        Toast.makeText(DietRecommendationsActivity.this, "Failed to save result to Firestore", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchPreviousResponses() {
        // Fetch previous responses from Firestore and display them
        db.collection("Diet")
                .document("Recommendations")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String previousResponse = documentSnapshot.getString("result");
                            // Display previous response in dietRecommendationsText
                            dietRecommendationsText.setText(previousResponse);
                        } else {
                            // No previous response found
                            // Handle this case, for example, by displaying a message
                            dietRecommendationsText.setText("No previous response found.");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // Handle failure to fetch previous responses
                        Toast.makeText(DietRecommendationsActivity.this, "Failed to fetch previous response from Firestore", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    }









