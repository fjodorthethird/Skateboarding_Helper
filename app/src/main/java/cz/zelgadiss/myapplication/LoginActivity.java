package cz.zelgadiss.myapplication;

import android.content.Intent;
import android.os.Bundle;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText ETemail;
    private EditText ETpassword;
    private EditText ETpassword_repeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

                } else {
                    // User is signed out

                }

            }
        };

        ETemail = (EditText) findViewById(R.id.login);
        ETpassword = (EditText) findViewById(R.id.password);
        ETpassword_repeat = (EditText) findViewById(R.id.password_repeat);

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            Intent intent_main = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent_main);

        } else {
            // User is signed out

        }
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_enter) {

            enter(ETemail.getText().toString(),ETpassword.getText().toString());
        }
        else if (v.getId() == R.id.button_doregister) {

            Intent intent_register = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent_register);
            finish();
        }
        else if (v.getId() == R.id.button_withoutregister) {
            Intent intent_main = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent_main);
            finish();
        }

    }

    public void enter(String email , final String password)
    {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (task.getResult().getAdditionalUserInfo().isNewUser()){

                        String email = currentUser.getEmail();
                        String uID = currentUser.getUid();
                        HashMap<Object, String> hashMap = new HashMap<>();
                        hashMap.put("email", email);
                        hashMap.put("firstname"," ");
                        hashMap.put("secondname", " ");
                        hashMap.put("country", " ");
                        hashMap.put("image", " ");
                        hashMap.put("uID", uID);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference = database.getReference("Users");
                        reference.child(uID).setValue(hashMap);

                    }

                    Toast.makeText(LoginActivity.this, "Вы успешно вошли", Toast.LENGTH_SHORT).show();
                    Intent intent_main = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent_main);
                    finish();
                }
                else 
                    Toast.makeText(LoginActivity.this, "Авторизация не удалась", Toast.LENGTH_SHORT).show();

            }
        });
    }


}

