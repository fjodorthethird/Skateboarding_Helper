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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

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
    EditText namePlayer1;
    EditText namePlayer2;
    TextView textRandomTrick;
    int player1Counter;
    int player2Counter;

    String dice1Array[] = {"Nollie", "Ollie", "Fakie", "Switch", "Ollie", ""};
    String dice2Array[] = {"Kickflip", "Heelflip", "Kickflip", "Heelflip", "", ""};
    String dice3Array[] = {"Frontside", "Backside", "Frontside", "Backside", "", ""};
    String dice4Array[] = {"Shuvit", "Big Spin", "360 Shuvit", "180", "360", ""};
    String randomTrick[] = new String[4];


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
        textRandomTrick = findViewById(R.id.random_trick);

        player1Counter = 0;
        player2Counter = 0;


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


        switch (view.getId()){

            case R.id.button_roll:
                diceOne.setText(getRandom(dice1Array));
                randomTrick[0] = diceOne.getText().toString() + " ";
                diceTwo.setText(getRandom(dice2Array));
                randomTrick[1] = diceTwo.getText().toString() + " ";
                diceThree.setText(getRandom(dice3Array));
                randomTrick[2] = diceThree.getText().toString() + " ";
                diceFour.setText(getRandom(dice4Array));
                randomTrick[3] = diceFour.getText().toString() + " ";

                textRandomTrick.setText(randomTrick[0] +  randomTrick[1] +  randomTrick[2] + randomTrick[3]);

            case R.id.ic_decrease_player1:
                if (player1Counter > 0){

                    player1Counter--;
                    getPoints(player1Counter, editPointsPlayer1);

                }
                break;


            case R.id.ic_increase_player1:
                if (player1Counter < 5){

                    player1Counter++;
                    getPoints(player1Counter, editPointsPlayer1);

                }
                break;


            case R.id.ic_decrease_player2:
                if (player2Counter > 0){

                    player2Counter--;
                    getPoints(player2Counter, editPointsPlayer2);

                }
                break;


            case R.id.ic_increase_player2:
                if (player2Counter < 5){

                    player2Counter++;
                    getPoints(player2Counter, editPointsPlayer2);
                }
                break;

            case R.id.ency_icon:
                String str;
                str = textRandomTrick.getText().toString();
                Intent intent = new Intent(GameActivity.this, EncyActivity.class);
                intent.putExtra("trickname", str);
                startActivity(intent);

        }

    }

    public static String getRandom(String[] array) {

        int rnd = new Random().nextInt(array.length);
        return array[rnd];

    }

     public void getPoints(int counter, EditText playerPoints){

        if (counter == 0) playerPoints.getText().clear();
        if (counter == 1) {

            playerPoints.getText().clear();
            playerPoints.setText("S");
        }
        if (counter == 2){
            playerPoints.getText().clear();
            playerPoints.setText("S K");
        }
        if (counter == 3) {
            playerPoints.getText().clear();
            playerPoints.setText("S K A");
        }
        if (counter == 4){
            playerPoints.getText().clear();
            playerPoints.setText("S K A T");
        }
        if (counter == 5) {
            playerPoints.getText().clear();
            playerPoints.setText("S K A T E");
        }

    }


}
