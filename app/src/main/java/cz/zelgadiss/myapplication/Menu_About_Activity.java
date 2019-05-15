package cz.zelgadiss.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Menu_About_Activity extends AppCompatActivity {

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

        setContentView(R.layout.activity_menu_about);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("О приложении");
        setSupportActionBar(toolbar);
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

            mi_home.setIcon(R.drawable.ic_home_white_24dp);

            mi_profile.setIcon(R.drawable.ic_person_white_24dp);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menuAbout:
                Intent intent_about = new Intent(Menu_About_Activity.this, Menu_About_Activity.class);
                startActivity(intent_about);
                finish();
                break;

            case R.id.menuSettings:
                Intent intent_settings = new Intent(Menu_About_Activity.this, Menu_Settings_Activity.class);
                startActivity(intent_settings);
                finish();
                break;

            case R.id.menuHome:
                Intent intent_home = new Intent(Menu_About_Activity.this, MainActivity.class);
                startActivity(intent_home);
                finish();
                break;

            case R.id.menuProfile:
                Intent intent_profile = new Intent(Menu_About_Activity.this, ProfileActivity.class);
                startActivity(intent_profile);
                finish();

                break;
        }
        return true;

    }
    public void ic_about_samsung(View view)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://myitschool.ru"));
        startActivity(browserIntent);
    }

}
