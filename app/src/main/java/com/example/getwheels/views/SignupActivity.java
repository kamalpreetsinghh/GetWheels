package com.example.getwheels.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.getwheels.R;
import com.example.getwheels.databinding.ActivityLoginBinding;
import com.example.getwheels.databinding.ActivitySignupBinding;
import com.example.getwheels.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getCanonicalName();
    private ActivitySignupBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding =ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.binding.createAccountButton.setOnClickListener(view -> {
            this.validateData();
        });
    }

    private void validateData() {
        boolean validData = true;

        User user = new User();
        user.setFirstName(this.binding.editTextFirstName.getText().toString());
        user.setLastName(this.binding.editTextLastName.getText().toString());
        user.setEmail(this.binding.editTextSignupEmail.getText().toString());
        user.setPassword(this.binding.editTextSignupPassword.getText().toString());

        EditText confirmPassword = this.binding.editTextSignupConfirmPassword;

        if (user.getFirstName().isEmpty()) {
            this.binding.editTextFirstName.setError("First Name cannot be left empty");
            validData = false;
        }

        if (user.getLastName().isEmpty()) {
            this.binding.editTextLastName.setError("Last Name cannot be left empty");
            validData = false;
        }

        if (user.getEmail().isEmpty()) {
            this.binding.editTextSignupEmail.setError("Email cannot be left empty");
            validData = false;
        }

        if (user.getPassword().isEmpty()) {
            this.binding.editTextSignupPassword.setError("Password cannot be left empty");
            validData = false;
        }

        if(confirmPassword.getText().toString().isEmpty()) {
            confirmPassword.setError("Confirm Password cannot be left empty");
            validData = false;
        }

        if(!user.getPassword().equals(confirmPassword.getText().toString())) {
            confirmPassword.setError("Confirm Password does not match with the Password");
            validData = false;
        }

        if (validData) {
            this.createAccount(user);
        } else {
            Toast.makeText(this, "Please enter correct inputs", Toast.LENGTH_SHORT).show();
        }

    }

    private void createAccount(User user) {
    // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            openCarsListActivity();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void openCarsListActivity() {
        Intent intent = new Intent(this, CarsListActivity.class);
        startActivity(intent);
    }

}