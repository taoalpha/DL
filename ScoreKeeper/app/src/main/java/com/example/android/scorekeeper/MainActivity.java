package com.example.android.scorekeeper;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.id.message;
import static com.example.android.scorekeeper.R.id.result;
import static com.example.android.scorekeeper.R.id.winA;
import static com.example.android.scorekeeper.R.id.winB;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    int scoreA = 0;
    int scoreB = 0;
    int index = 0;
    int winA = 0;
    int winB = 0;
    String message = "";
    String winner="";

    /******************
     * Description: method for start button, it will change the value of scores to 0
     * @param view
     */
    public void start(View view) {
        scoreA=0;
        scoreB=0;
        index = index + 1;
        displayScoreA();
        displayScoreB();
    }
    /******************
     * Description: method for reset button, it will set all values to be 0 or null.
     * @param view
     */
    public void reset(View view) {
        scoreA = 0;
        scoreB = 0;
        index = 0;
        displayScoreA();
        displayScoreB();
        message="";
        winner="";
        winA = 0;
        winB = 0;
        displayWinA();
        displayWinB();
        TextView result = (TextView) findViewById(R.id.result);
        result.setText(message);
    }
/*****************
 * Description: method for 3 point button. It will increase score by 3 and display it in score TextView for team A
 */
    public void IncreaseTeamABy3(View view) {
        scoreA = scoreA + 3;
        displayScoreA();
    }
    /*****************
     * Description: method for 2 point button. It will increase score by 2 and display it in score TextView for team A.
     */
    public void IncreaseTeamABy2(View view) {
        scoreA = scoreA + 2;
        displayScoreA();
    }
    /*****************
     * Description: method for 1 point button. It will increase score by 1 and display it in score TextView for team A.
     */
    public void IncreaseTeamABy1(View view) {
        scoreA = scoreA + 1;
        displayScoreA();
    }
    /*****************
     * Description: method for 3 point button. It will increase scoreB by 3 and display it in score TextView for team B.
     */
    public void IncreaseTeamBBy3(View view) {
        scoreB = scoreB + 3;
        displayScoreB();
    }
    /*****************
     * Description: method for 2 point button. It will increase scoreB by 2 and display it in score TextView for team B.
     */
    public void IncreaseTeamBBy2(View view) {
        scoreB = scoreB + 2;
        displayScoreB();
    }
    /*****************
     * Description: method for 1 point button. It will increase scoreB by 1 and display it in score TextView for team B.
     */
    public void IncreaseTeamBBy1(View view) {
        scoreB = scoreB + 1;
        displayScoreB();
    }

    /*************
     * Description: This method will be fore complete button. It will change and save the results of each game.
     * @param view
     */
    public void complete(View view) {
        EditText scoreA_name = (EditText) findViewById(R.id.teamA);
        String nameA = scoreA_name.getText().toString();
        EditText scoreB_name = (EditText) findViewById(R.id.teamB);
        String nameB = scoreB_name.getText().toString();
        message = message + "\nGame" + index + ": " + nameA + "-" + scoreA + " vs " + nameB + "-" + scoreB;
        TextView result = (TextView) findViewById(R.id.result);
        if (scoreA > scoreB) {
            winA = winA + 1;
        } else {
            winB = winB + 1;
        }
        displayWinA();
        displayWinB();
        if (winA == 4) {
            winner = nameA;
            message += "\nWinner: " + nameA;
            Context context = getApplicationContext();
            CharSequence text = nameA + " win the game! Wants to share the news with your friend ?";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        if (winB == 4) {
            winner = nameB;
            message += "\nWinner: " + nameB;
            Context context = getApplicationContext();
            CharSequence text = nameB + " win the game! Wants to share the news with your friend ?";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        result.setText(message);
    }

    /*************
     * Description: This method will be for share button. It will set an intent to trigger mail app to send winner result
     */

    public void share(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "winner is " + winner);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "winner is" + winner);
        sendIntent.setType("text/plain");
        if (sendIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(sendIntent);
        }
    }
    /*************
     * Description: It will display current number of games Team A has wined.
     */
    private void displayWinA() {
        TextView winA_text = (TextView) findViewById(R.id.winA);
        winA_text.setText(String.valueOf(winA));
    }
    /*************
     * Description: It will display current number of games Team B has wined.
     */
    private void displayWinB() {
        TextView winB_text = (TextView) findViewById(R.id.winB);
        winB_text.setText(String.valueOf(winB));
    }

     /* Description: It will display current score in team B score field.
     */
    private void displayScoreA() {
        TextView scoreA_text = (TextView) findViewById(R.id.scoreA);
        scoreA_text.setText(String.valueOf(scoreA));
    }
    /*************
     * Description: It will display current score in team B score field.
     */
    private void displayScoreB() {
        TextView scoreB_text = (TextView) findViewById(R.id.scoreB);
        scoreB_text.setText(String.valueOf(scoreB));
    }

}
