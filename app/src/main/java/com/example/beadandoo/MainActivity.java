package com.example.beadandoo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_Tag = MainActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;

    EditText userNameET;
    EditText passwordET;
    Button loginButton;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userNameET = findViewById(R.id.editTextUserName);
        passwordET = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.loginButton);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

        Log.i(LOG_Tag, "onCreate");


    }

    public void login(View view) {
        Animation shake =  AnimationUtils.loadAnimation(this,R.anim.shake);
        String userName = userNameET.getText().toString();
        String password = passwordET.getText().toString();

        // Log.i(LOG_Tag,"Bejelentkezet: "+ userName + ",jelszó: "+password);

        mAuth.signInWithEmailAndPassword(userName, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_Tag, "bejelentkezés sikeres volt");
                    startShopping();
                } else {
                    Log.d(LOG_Tag, "bejelentkezés nem sikerült");
                    loginButton.startAnimation(shake);
                    Toast.makeText(MainActivity.this, "bejelentkezés nem sikerült" + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    private void startShopping() {
        Intent intent = new Intent(this, ShopActivity.class);
        startActivity(intent);
    }

    public void register(View view) {
        Intent intent = new Intent(this, registrationActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_Tag, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_Tag, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_Tag, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName", userNameET.getText().toString());
        editor.putString("password", passwordET.getText().toString());
        editor.apply();

        Log.i(LOG_Tag, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_Tag, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_Tag, "onRestart");
    }

    public void guestlogin(View view) {
        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_Tag, "bejelentkezés anonim volt");
                    startShopping();
                } else {
                    Log.d(LOG_Tag, "névtelen bejelentkezés nem sikerült");
                    Toast.makeText(MainActivity.this, "névtelen bejelentkezés nem sikerült" + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}