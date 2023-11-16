package com.example.loginauth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference usersRef;
    private List<String> userList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();



        usersRef = FirebaseDatabase.getInstance().getReference("users");
        userList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // Retrieve the list of users from Firebase
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userEmail = userSnapshot.child("email").getValue(String.class);
                    userList.add(userEmail);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminActivity.this, "Failed to retrieve users", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up item click listener to view user details
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedUserEmail = userList.get(position);
                // Start an activity to display user details (you need to implement this)
                Intent intent = new Intent(AdminActivity.this, UserDetailsActivity.class);
                intent.putExtra("userEmail", selectedUserEmail);
                startActivity(intent);
            }
        });

        // Set up item long click listener to delete user
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedUserEmail = userList.get(position);
                // Implement a method to delete the user (you need to implement this)
                deleteUser(selectedUserEmail);
                return true;
            }
        });
    }

    private void deleteUser(String userEmail) {
        // Implement the logic to delete the user from Firebase
        // You need to handle authentication and database deletion appropriately
        // This is a basic example, and you should add error handling and confirmation dialogs
        usersRef.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    userSnapshot.getRef().removeValue();
                }
                Toast.makeText(AdminActivity.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminActivity.this, "Failed to delete user", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
