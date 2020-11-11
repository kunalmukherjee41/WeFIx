package com.example.wefix;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.example.wefix.Api.RetrofitClient;
import com.example.wefix.model.UserResponse;
import com.example.wefix.storage.SharedPrefManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button login;
    String userID;

    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        TextView create_account = findViewById(R.id.create_account);
        TextView forgot_password = findViewById(R.id.forgot_password);

        //goto create activity
        create_account.setOnClickListener(
                v -> startActivity(new Intent(LoginActivity.this, CreateUserActivity.class))
        );

        //check email password and goto home activity
        login.setOnClickListener(
                v -> {
                    login.setBackgroundColor(getResources().getColor(R.color.btn));
                    userLogin();
                }
        );

    }

    public void ShowHidePass(View view) {

        if (view.getId() == R.id.show_pass_btn) {
            if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.password_hide_asset);
                //Show Password
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.password_visible_asset);
                //Hide Password
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    //on user login
    private void userLogin() {

        progressBar = new ProgressDialog(this);
        progressBar.show();
        progressBar.setContentView(R.layout.progress_dialog);
        Objects.requireNonNull(progressBar.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        String txt_username = email.getText().toString().trim();
        String txt_password = password.getText().toString().trim();
        if (TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_username)) {
            Toast.makeText(LoginActivity.this, "Fill Both Requirements", Toast.LENGTH_LONG).show();
            login.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
            progressBar.dismiss();

//        } else if (!Patterns.EMAIL_ADDRESS.matcher(txt_username).matches()) {
//            Toast.makeText(LoginActivity.this, "Provided a valid Email Address", Toast.LENGTH_LONG).show();
//            login.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
//            progressBar.dismiss();

        } else {

            Call<UserResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .userLogin(txt_username, txt_password);

            call.enqueue(new Callback<UserResponse>() {
                             @Override
                             public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                                 assert response.body() != null;
                                 if (!response.body().getError()) {
                                     assert response.body() != null;
                                     UserResponse userResponse = response.body();
                                     SharedPrefManager.getInstance(LoginActivity.this).saveUser(userResponse.getUser());

                                     Toast.makeText(LoginActivity.this, userResponse.getMessage(), Toast.LENGTH_LONG).show();
//                                     Intent intent = new Intent(LoginActivity.this, DisplayActivity.class);
//                                     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                     startActivity(intent);
                                     firebaseLogin(txt_username, txt_password);
                                     email.setText("");

                                 } else {
                                     Toast.makeText(LoginActivity.this, "Password Is Invalid", Toast.LENGTH_LONG).show();
                                 }
                                 login.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                                 password.setText("");
                                 login.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));
                                 progressBar.dismiss();

                             }

                             @Override
                             public void onFailure(Call<UserResponse> call, Throwable t) {
                                 Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                 password.setText("");
                                 progressBar.dismiss();
                                 login.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_btn, null));

                             }
                         }
            );

        }
    }

    //check user previously login or not
    @Override
    protected void onStart() {
        super.onStart();
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, DisplayActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progressBar.dismiss();
    }

    private void firebaseLogin(String txt_email, String txt_password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(txt_email, txt_password)
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                assert firebaseUser != null;
                                userID = firebaseUser.getUid();

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("email", txt_email);
                                hashMap.put("id", userID);
                                reference.setValue(hashMap).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Intent intent = new Intent(LoginActivity.this, DisplayActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        progressBar.dismiss();
                                    }
                                });
                            } else {
                                auth.signInWithEmailAndPassword(txt_email, txt_password)
                                        .addOnCompleteListener(
                                                task12 -> {
                                                    Intent intent = new Intent(LoginActivity.this, DisplayActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    progressBar.dismiss();
                                                }
                                        );
                            }
                        }
                );

    }

}
