package com.example.manishsingla.scarnesdice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;
import java.lang.String;

public class diceActivity extends AppCompatActivity {

      TextView userscore, computerscore;
      ImageView diceimg;
      Button rollbtn,holdbtn,resetbtn;
    private Random random =new Random();
    private Random nextrandom=new Random();
    private int currentuserscore, currentcomputerscore,surecomputerscore;
    private final int MAX_SCORE=100;
    private int diceicons[]={
            R.drawable.dice1, R.drawable.dice2, R.drawable.dice3,
            R.drawable.dice4,R.drawable.dice5, R.drawable.dice6
    };
    private Handler timer=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);
        userscore = (TextView) findViewById(R.id.score1);
        computerscore = (TextView) findViewById(R.id.score2);
        diceimg = (ImageView) findViewById(R.id.dice11);
        rollbtn = (Button) findViewById(R.id.role);
        holdbtn = (Button) findViewById(R.id.hold);
        resetbtn = (Button) findViewById(R.id.reset);

        //event listeners
        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStart();
            }
        });
        rollbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollDice();
            }
        });
        holdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLog("User put a Hold. Computer turn now");
                startComputerTurn();
            }
        });
    }
        Runnable computerTurnRunnable =new Runnable() {
            @Override
            public void run() {
                if(nextrandom.nextInt(10)<7 || currentcomputerscore==0) {
                    boolean b = computerTurn();
                    if (b) {
                        timer.postDelayed(this, 1000);
                    } else {
                        endComputerTurn();
                    }
                }
                else{
                    addLog("Computer put a hold, user turn now");
                    endComputerTurn();
                }
            }
        };

        private void endComputerTurn(){

        updatecomputerscore();
        holdbtn.setEnabled(true);
        rollbtn.setEnabled(true);
    }

        private void startComputerTurn(){
            holdbtn.setEnabled(false);
            rollbtn.setEnabled(false);
            currentcomputerscore=0;
            currentuserscore=0;
            surecomputerscore=Integer.parseInt(computerscore.getText().toString());
            timer.postDelayed(computerTurnRunnable, 500);
        }

        private void updatecomputerscore() {
            computerscore.setText("" + (surecomputerscore + currentcomputerscore));
        }


        private boolean computerTurn() {
            int num;
            num = random.nextInt(6) + 1;
            diceimg.setImageResource(diceicons[num - 1]);
            if (num == 1) {
                currentcomputerscore = 0;
                addLog("computer rolled a 1,reset");
                return false;
            } else {
                addLog("computer rolled a" + num);
                currentcomputerscore += num;
                updatecomputerscore();
                if ((surecomputerscore + currentcomputerscore) >= MAX_SCORE) {
                    endGame("computer");
                    return false;
                }
            }
            return true;
        }

        private void rollDice() {
            int num = random.nextInt(6) + 1;
            int currentscore = Integer.parseInt(userscore.getText().toString());
            diceimg.setImageResource(diceicons[num - 1]);
            if (num == 1) {
                currentscore -= currentuserscore;
                userscore.setText(currentscore + "");
                addLog("user rolled a 1" + num);
                startComputerTurn();
            } else {
                currentscore += num;
                if (currentscore >= MAX_SCORE) {
                    endGame("user");
                    return;
                }
                currentuserscore += num;
                addLog("user rolled a " + num);
                userscore.setText(currentscore + "");
            }
        }

        private void endGame(String winner) {

            (Toast.makeText(this, "Game over." + winner + "won", Toast.LENGTH_LONG)).show();
            onStart();
        }

    @Override
        protected void onStart(){

            super.onStart();
        currentuserscore=0;
        currentcomputerscore=0;
        surecomputerscore=0;
        userscore.setText("0");
        computerscore.setText("0");
        timer.removeCallbacks(computerTurnRunnable);
    }

    private void addLog(String msg){
        ( Toast.makeText(this, msg,Toast.LENGTH_SHORT)).show();
    }


}
