package cz.zelgadiss.myapplication;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText ETemail;
    private EditText ETpassword;
    private EditText ETpassword_repeat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

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

        findViewById(R.id.button_register).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
         if (view.getId() == R.id.button_register) {

            registration(ETemail.getText().toString(),ETpassword.getText().toString());
        }
         else if(view.getId() == R.id.button_withoutregister) {
             Intent intent_main = new Intent(RegistrationActivity.this, MainActivity.class);
             startActivity(intent_main);
             finish();
         }

    }

    public void registration (String email , String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful() && ETpassword.getText().toString().equals(ETpassword_repeat.getText().toString()))
                {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    String email = currentUser.getEmail();
                    String uID = currentUser.getUid();
                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("email", email);
                    hashMap.put("firstname", "");
                    hashMap.put("secondname", " ");
                    hashMap.put("country", " ");
                    hashMap.put("image", " ");
                    hashMap.put("uID", uID);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("Users");
                    reference.child(uID).setValue(hashMap);
                    Toast.makeText(RegistrationActivity.this, "Вы успешно зарегистрировались", Toast.LENGTH_SHORT).show();
                    Intent intent_login = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(intent_login);
                    finish();
                }
                else
                    Toast.makeText(RegistrationActivity.this, "Регистрация не удалась", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
