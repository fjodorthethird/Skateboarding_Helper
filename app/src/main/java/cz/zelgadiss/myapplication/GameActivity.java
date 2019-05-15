package cz.zelgadiss.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GameActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    ImageView iconEncy;
    Button buttonRoll;
    EditText editPointsPlayer1;
    EditText editPointsPlayer2;
    ImageView iconDecreasePlayer1;
    ImageView iconDecreasePlayer2;
    ImageView iconIncreasePlayer1;
    ImageView iconIncreasePlayer2;
    TextView diceOne;
    TextView diceTwo;
    TextView diceThree;
    TextView diceFour;
    TextView namePlayer1;
    TextView namePlayer2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkAppTheme);
        }
        else
            setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Game of SKATE");
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        iconEncy = findViewById(R.id.ency_icon);
        buttonRoll = findViewById(R.id.button_roll);
        editPointsPlayer1 = findViewById(R.id.edit_points_player1);
        editPointsPlayer2 = findViewById(R.id.edit_points_player2);
        iconDecreasePlayer1 = findViewById(R.id.ic_decrease_player1);
        iconIncreasePlayer1 = findViewById(R.id.ic_increase_player1);
        iconDecreasePlayer2 = findViewById(R.id.ic_decrease_player2);
        iconIncreasePlayer2 = findViewById(R.id.ic_increase_player2);
        diceOne = findViewById(R.id.dice_1);
        diceTwo = findViewById(R.id.dice_2);
        diceThree = findViewById(R.id.dice_3);
        diceFour = findViewById(R.id.dice_4);
        namePlayer1 = findViewById(R.id.edit_name_player1);
        namePlayer2 = findViewById(R.id.edit_name_player2);

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
                Intent intent_about = new Intent(GameActivity.this, Menu_About_Activity.class);
                startActivity(intent_about);

                break;

            case R.id.menuSettings:
                Intent intent_settings = new Intent(GameActivity.this, Menu_Settings_Activity.class);
                startActivity(intent_settings);

                break;

            case R.id.menuProfile:
                Intent intent_profile = new Intent(GameActivity.this, ProfileActivity.class);
                startActivity(intent_profile);

                break;

            case R.id.menuHome:
                Intent intent_home = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intent_home);

                break;


        }
        return true;

    }

    public void onClick(View view) {

    }
}
