package cz.zelgadiss.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity   {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkAppTheme);
        }
        else
            setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Главное меню");
        setSupportActionBar(toolbar);





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        MenuItem mi_home = menu.findItem(R.id.menuHome);
        MenuItem mi_profile = menu.findItem(R.id.menuProfile);
        mi_home.setVisible(false);
        if (currentUser == null){

            mi_profile.setVisible(false);
        }


        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){

            mi_profile.setIcon(R.drawable.ic_person_white_24dp);
        }
        return true;
    }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            switch(item.getItemId()){
                case R.id.menuAbout:
                    Intent intent_about = new Intent(MainActivity.this, Menu_About_Activity.class);
                    startActivity(intent_about);

                    break;

                case R.id.menuSettings:
                    Intent intent_settings = new Intent(MainActivity.this, Menu_Settings_Activity.class);
                    startActivity(intent_settings);

                    break;

                case R.id.menuProfile:
                    Intent intent_profile = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent_profile);

                    break;
            }
            return true;

        }

            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.button_activity_main_encyclopedia:
                        Intent intent_ency = new Intent(MainActivity.this, EncyActivity.class);
                        startActivity(intent_ency);
                        Toast.makeText(MainActivity.this, "Идет загрузка данных...", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.button_activity_main_yourprogress:
                        Intent intent_progress = new Intent(MainActivity.this, ProgressActivity.class);
                        startActivity(intent_progress);
                        break;

                    case R.id.button_activity_main_gameofskate:
                        Intent intent_game = new Intent(MainActivity.this, GameActivity.class);
                        startActivity(intent_game);
                        break;



        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Закрыть приложение")
                .setMessage("Закрыть приложение и выйти?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finishAffinity();

                    }

                })
                .setNegativeButton("Нет", null)
                .show();
    }
}


