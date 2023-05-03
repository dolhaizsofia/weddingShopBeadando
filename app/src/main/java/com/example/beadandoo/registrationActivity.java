package com.example.beadandoo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.checker.nullness.qual.NonNull;

public class registrationActivity extends AppCompatActivity {
    private static final String LOG_Tag =registrationActivity.class.getName();
    private static final String PREF_KEY=MainActivity.class.getPackage().toString();
    private static final int SECRET_KEY=99;
    EditText editTextUserName;
    EditText userEmail;
    EditText editTextPassword;
    EditText editTextPasswordAgain;
    EditText addressEditText;
    RadioGroup accountTypeGroup;

    private SharedPreferences preferences;
    private  FirebaseAuth  mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

       // Bundle bundle=getIntent().getExtras();
       // bundle.getInt("SECRET_KEY");
        int secret_key=getIntent().getIntExtra("SECRET_KEY",0);

        if (secret_key!=99){
            finish();

        }

        editTextUserName= findViewById(R.id.editTextUserName);
        userEmail= findViewById(R.id.userEmail);
        editTextPassword= findViewById(R.id.editTextPassword);
        editTextPasswordAgain = findViewById(R.id.editTextPasswordAgain);
        addressEditText=findViewById(R.id.addressEditText);
        accountTypeGroup=findViewById(R.id.accountTypeGroup);
        accountTypeGroup.check(R.id.buyerRadioButten);

        preferences=getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String userName=preferences.getString("userName","");
        String password=preferences.getString("password","");

        editTextUserName.setText(userName);
        editTextPassword.setText(password);
        editTextPasswordAgain.setText(password);

        mAuth= FirebaseAuth.getInstance();

        Log.i(LOG_Tag,"onCreate");
    }

    public void register(View view) {
        String userName = editTextUserName.getText().toString();
        String email=userEmail.getText().toString();
        String password =editTextPassword.getText().toString();
        String passwordAgain =editTextPasswordAgain.getText().toString();

        if (!password.equals(passwordAgain)){
            Log.e(LOG_Tag,"Nem egyezik a jelszó és a megerősítése");
            return;

        }
        String address=addressEditText.getText().toString();

        int checkedId= accountTypeGroup.getCheckedRadioButtonId();
        RadioButton radioButton =accountTypeGroup.findViewById(checkedId);
        String accountType=radioButton.getText().toString();


        Log.i(LOG_Tag,"Regisztrált: "+ userName + ",email: "+email);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
           @Override
           public void onComplete(@NonNull Task<AuthResult> task){
               if (task.isSuccessful()){
                   Log.d(LOG_Tag,"user created successfully");
                   startShopping();
               }else{
                   Log.d(LOG_Tag,"user wasnt crated successfully");
                   Toast.makeText(registrationActivity.this,"user wasnt crated successfully"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
               }
            }
        });


    }

    public void megsem(View view) {
        finish();
    }

    private void startShopping(/*felhasználó adatai*/){
        Intent intent = new Intent(this,ShopActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_Tag,"onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_Tag,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_Tag,"onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_Tag,"onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_Tag,"onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_Tag,"onRestart");
    }
}
