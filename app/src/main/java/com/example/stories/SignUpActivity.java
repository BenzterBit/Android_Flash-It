package com.example.stories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    EditText nameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("User Registration");
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.regPassEditText);


    }

    public void registerClicked(View view) {
        mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid()).child("email").setValue(emailEditText.getText().toString());
                    FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid()).child("name").setValue(nameEditText.getText().toString());

                    Login();
                } else {
                    Toast.makeText(SignUpActivity.this, "Registration Failed!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void Login(){
        //move to next activity
        Intent intent = new Intent(this,UserListActivity.class);
        startActivity(intent);
    }
}
