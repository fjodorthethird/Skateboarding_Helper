package cz.zelgadiss.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;


public class EncyActivity extends AppCompatActivity {



    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference ref;
    ArrayList<Trick> list;
    RecyclerView recyclerView;

    EditText editSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ency);
        ref = FirebaseDatabase.getInstance().getReference().child("Tricks");
        recyclerView = findViewById(R.id.rv);
        //searchView = findViewById(R.id.searchView);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        editSearch = findViewById(R.id.edit_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Энциклопедия");
        setSupportActionBar(toolbar);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkAppTheme);
            editSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_white_24dp, 0, 0, 0);
        } else
            setTheme(R.style.AppTheme);

        Intent intent = getIntent();
        String str = intent.getStringExtra("trickname");
        editSearch.setText(str);


        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search(s.toString());
            }
        });


    }


    @Override
    protected void onStart(){
        super.onStart();
        if(ref != null){
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        list = new ArrayList<>();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            list.add(ds.getValue(Trick.class));
                        }
                        AdapterClass adapterClass =  new AdapterClass(list);
                        recyclerView.setAdapter(adapterClass);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(EncyActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    private void search(String str){
        ArrayList<Trick> myList = new ArrayList<>();
        for(Trick object : list){
            if((object.getDescription().toLowerCase().contains(str.toLowerCase())) || (object.getName().toLowerCase().contains(str.toLowerCase()))){
                myList.add(object);
            }
        }
        AdapterClass adapterClass = new AdapterClass(myList);
        recyclerView.setAdapter(adapterClass);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        MenuItem mi_profile = menu.findItem(R.id.menuProfile);
        MenuItem mi_home = menu.findItem(R.id.menuHome);
        if (currentUser == null){

            mi_profile.setVisible(false);
        }
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            mi_profile.setIcon(R.drawable.ic_person_white_24dp);
            mi_home.setIcon(R.drawable.ic_home_white_24dp);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menuAbout:
                Intent intent_about = new Intent(EncyActivity.this, Menu_About_Activity.class);
                startActivity(intent_about);

                break;

            case R.id.menuSettings:
                Intent intent_settings = new Intent(EncyActivity.this, Menu_Settings_Activity.class);
                startActivity(intent_settings);

                break;

            case R.id.menuProfile:
                Intent intent_profile = new Intent(EncyActivity.this, ProfileActivity.class);
                startActivity(intent_profile);

                break;

            case R.id.menuHome:
                Intent intent_home = new Intent(EncyActivity.this, MainActivity.class);
                startActivity(intent_home);

                break;


        }
        return true;

    }

}

