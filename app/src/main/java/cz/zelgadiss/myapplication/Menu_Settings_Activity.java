package cz.zelgadiss.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;
import io.ghyeok.stickyswitch.widget.StickySwitch;


public class Menu_Settings_Activity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkAppTheme);
        }
        else
            setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu_settings);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Настройки");
        setSupportActionBar(toolbar);

        StickySwitch stickySwitch = (StickySwitch) findViewById(R.id.sticky_switch);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            stickySwitch.setDirection(StickySwitch.Direction.RIGHT);
        }
        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(@NotNull StickySwitch.Direction direction, @NotNull String text){
                if(direction.name() == "RIGHT") {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    recreate();
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    recreate();
                }

            }

        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        MenuItem mi_profile = menu.findItem(R.id.menuProfile);
        MenuItem mi_home = menu.findItem(R.id.menuHome);
        if (currentUser == null){

            mi_profile.setVisible(false);
            Button button_login =  findViewById(R.id.buttonLogout);
            button_login.setVisibility(View.INVISIBLE);
        }

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            mi_home.setIcon(R.drawable.ic_home_white_24dp);
            mi_profile.setIcon(R.drawable.ic_person_white_24dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuAbout:
                Intent intent_about = new Intent(Menu_Settings_Activity.this, Menu_About_Activity.class);
                startActivity(intent_about);
                finish();
                break;

            case R.id.menuSettings:

                Intent intent = new Intent(Menu_Settings_Activity.this, Menu_Settings_Activity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menuHome:
                Intent intent_home = new Intent(Menu_Settings_Activity.this, MainActivity.class);
                startActivity(intent_home);
                finish();
                break;

            case R.id.menuProfile:
                Intent intent_profile = new Intent(Menu_Settings_Activity.this, ProfileActivity.class);
                startActivity(intent_profile);
                finish();

                break;


        }
        return true;

    }


    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonLogout:
                Intent intent_login = new Intent(Menu_Settings_Activity.this, LoginActivity.class);
                intent_login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent_login);
                finish();
                FirebaseAuth.getInstance().signOut();
        }
    }
}

