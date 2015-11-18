package simongame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity
{
    ImageView blue;
    ImageView green;
    ImageView red;
    ImageView orange;
    TextView score;
    TextView high_score;
    TextView round;
    Button start;
    Button mainMenu;
    String[] colors = new String[]{"R","G","B","O"};
    Editable player;
    ArrayList<String> seq;
    ArrayList<String> userSeq;
    ArrayList<String> playerNames;
    ArrayList<String> highscoreList;
    private SoundPool soundPool;
    private int[] soundIds;
    private int GREEN = 0;
    private int RED = 1;
    private int ORANGE = 2;
    private int BLUE = 3;
    private int LOSE_SOUND = 4;
    private int speakerStream;
    int hs_changed = 0;
    int gameDelay = 1000;
    int colorScheme = 0;

    SharedPreferences sharedPref;
    SharedPreferences sharedPref2;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor2;


    int count = 0;
    int roundNo = 0;
    int userCount;
    int hs;
    static final int DELAY  = 1000;

    private Menu settings;
    private AlertDialog speedDialog;
    private AlertDialog colorDialog;
    private AlertDialog soundDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        highscoreList = new ArrayList<>();

        final Intent i = new Intent(getBaseContext(), simongame.MenuActivity.class);

        mainMenu = (Button) findViewById(R.id.main_menu);
        start = (Button) findViewById(R.id.start);
        blue = (ImageView) findViewById(R.id.blue);
        green = (ImageView) findViewById(R.id.green);
        red = (ImageView) findViewById(R.id.red);
        orange = (ImageView) findViewById(R.id.orange);

        soundIds = new int[5];
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        soundIds[GREEN] = soundPool.load(this, R.raw.green_long, 1);
        soundIds[RED] = soundPool.load(this, R.raw.red_long, 1);
        soundIds[ORANGE] = soundPool.load(this, R.raw.orange_long, 1);
        soundIds[BLUE] = soundPool.load(this, R.raw.blue_long, 1);
        soundIds[LOSE_SOUND] = soundPool.load(this, R.raw.lose, 1);

        sharedPref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPref2 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPref.edit();
        editor2 = sharedPref2.edit();




        blue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (count > 0)
                {
                    // doStream(soundIds[BLUE]);
                    userSeq.add("B");
                    userCount++;
                    validateUserSeq();
                }
            }
        });

        green.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(count > 0)
                {
                    //doStream(soundIds[GREEN]);
                    userSeq.add("G");
                    userCount++;
                    validateUserSeq();
                }
            }
        });

        red.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(count > 0)
                {
                   // doStream(soundIds[RED]);
                    userSeq.add("R");
                    userCount++;
                    validateUserSeq();
                }
            }
        });

        orange.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(count > 0)
                {
                    //doStream(soundIds[ORANGE]);
                    userSeq.add("O");
                    userCount++;
                    validateUserSeq();
                }
            }
        });

        round = (TextView) findViewById(R.id.round);
        round.setText("Round: "+roundNo);

        score = (TextView) findViewById(R.id.score);
        score.setText("Score: 0");

        high_score = (TextView) findViewById(R.id.high_score);
        hs = sharedPref.getInt("highscore", 0);
        high_score.setText("High Score: "+hs);

        start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                seq = new ArrayList<>();
                userSeq = new ArrayList<>();
                count = 1;
                userCount = 0;
                roundNo++;
                round.setText("Round: " + roundNo);
                generatSeq();
            }
        });

        mainMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            startActivity(i);
            }
        });
    }

    private void validateUserSeq()
    {
        if(userSeq.get(userCount -1) == seq.get(userCount -1) )
        {
            if(userCount == count)
            {
                score.setText("Score: " + count);
                if(count>hs)
                {
                    hs=count;
                    high_score.setText("High Score: "+ hs);
                    editor.putInt("highscore", hs);
                    editor.commit();
                    hs_changed = 1;
                }

                generatSeq();
                userCount = 0;
                count++;
            }

        }
        else
        {
            doStream(soundIds[LOSE_SOUND]);
            Toast.makeText(this,"You Lose",Toast.LENGTH_SHORT).show();
            if(hs_changed == 1)
            {

                Toast.makeText(this, "Congratulations! You have a new high score: " + hs, Toast.LENGTH_LONG).show();

               /* final EditText input = new EditText(this);

                new AlertDialog.Builder(this)
                        .setTitle("New High Score")
                        .setMessage("Enter your name")
                        .setView(input)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                player = input.getText();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                // Do nothing.
                            }
                        }).show();*/

                if(highscoreList.size() <= 5)
                {
                    highscoreList.add(Integer.toString(hs));/*+" , player name: "+player);*/
                    Collections.sort(highscoreList);
                }
                else
                {
                    highscoreList.remove(highscoreList.size()-1);
                    highscoreList.add(Integer.toString(hs));/*+" , player name: "+player);*/
                    Collections.sort(highscoreList);
                }

                Set<String> set = new HashSet<String>();
                set.addAll(highscoreList);

                editor2.putStringSet("highscore_list", (Set<String>) set);
                editor2.commit();


                hs_changed = 0;
            }


            score.setText("Score: 0");
            count = 0;
            userCount = 0;
            seq.clear();
            userSeq.clear();
        }
    }

    private void generatSeq()
    {
        final Handler handler = new Handler();
        userSeq.clear();
        seq.add(getRandColor());
        int delayCount = gameDelay;
        for (final String color :seq)
        {
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    lightColors(color);
                }
            }, delayCount);
            delayCount += gameDelay*2;
        }
    }

    private void lightColors(String color)
    {
        final Handler handler = new Handler();
        int delayCount =gameDelay;
        if(colorScheme == 0)
        {
            switch (color)
            {
                case "R":
                    red.setBackground(new ColorDrawable(getResources().getColor(R.color.light_red)));
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            red.setBackground(new ColorDrawable(getResources().getColor(R.color.red)));
                        }
                    }, delayCount);
                    break;
                case "B":
                    blue.setBackground(new ColorDrawable(getResources().getColor(R.color.light_blue)));
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            blue.setBackground(new ColorDrawable(getResources().getColor(R.color.blue)));
                        }
                    }, delayCount);
                    break;
                case "G":
                    green.setBackground(new ColorDrawable(getResources().getColor(R.color.light_green)));
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            green.setBackground(new ColorDrawable(getResources().getColor(R.color.green)));
                        }
                    }, delayCount);
                    break;
                case "O":
                    orange.setBackground(new ColorDrawable(getResources().getColor(R.color.light_orange)));
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            orange.setBackground(new ColorDrawable(getResources().getColor(R.color.orange)));
                        }
                    }, delayCount);
                    break;
            }
        }
        else if(colorScheme == 1)
        {
            switch (color)
            {
                case "R":
                    red.setBackground(new ColorDrawable(getResources().getColor(R.color.light_black)));
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            red.setBackground(new ColorDrawable(getResources().getColor(R.color.black)));
                        }
                    }, delayCount);
                    break;
                case "B":
                    blue.setBackground(new ColorDrawable(getResources().getColor(R.color.light_blue)));
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            blue.setBackground(new ColorDrawable(getResources().getColor(R.color.blue)));
                        }
                    }, delayCount);
                    break;
                case "G":
                    green.setBackground(new ColorDrawable(getResources().getColor(R.color.light_green)));
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            green.setBackground(new ColorDrawable(getResources().getColor(R.color.green)));
                        }
                    }, delayCount);
                    break;
                case "O":
                    orange.setBackground(new ColorDrawable(getResources().getColor(R.color.light_violet)));
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            orange.setBackground(new ColorDrawable(getResources().getColor(R.color.violet)));
                        }
                    }, delayCount);
                    break;
            }

        }
    }

    private String getRandColor()
    {
        Random rand = new Random();
        return colors[rand.nextInt(4)];
    }

    public void setSpeed(int opt)
    {
        if(opt == 1)
            gameDelay = 1000;
        else if(opt == 2)
            gameDelay = 500;
        else
            gameDelay = 250;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        AlertDialog.Builder builder;
        switch (id)
        {
            case 1:
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.gameSpeed_setting);
                builder.setSingleChoiceItems(R.array.gameSpeed_choices, 2, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        setSpeed(whichButton + 1);
                        //levelDisplay.setText(String.valueOf(whichButton + 1));
                    }
                });
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        speedDialog.dismiss();
                    }
                });
                speedDialog = builder.create();
                return speedDialog;
            case 2:
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.colors_setting);
                builder.setSingleChoiceItems(R.array.color_choices, 0, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        colorScheme = whichButton;
                        changeColors(colorScheme);
                       // model.setGame(whichButton + 1);
                    }
                });
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        colorDialog.dismiss();
                    }
                });
                colorDialog = builder.create();
                return colorDialog;
            default: return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.gameSpeed:
                showDialog(1);
                return true;
            case R.id.colors:
                showDialog(2);
                return true;
            case R.id.clearHS:
                hs = 0;
                editor.putInt("highscore", hs);
                editor.commit();
                high_score.setText("High Score: "+ hs);
        }
        return false;
    }

    public void changeColors(int opt)
    {
        if(opt == 1)
        {
            red.setBackground(new ColorDrawable(getResources().getColor(R.color.black)));
            orange.setBackground(new ColorDrawable(getResources().getColor(R.color.violet)));
        }
        else if(opt == 0)
        {
            red.setBackground(new ColorDrawable(getResources().getColor(R.color.red)));
            orange.setBackground(new ColorDrawable(getResources().getColor(R.color.orange)));
        }
    }

    public void doStream (final int soundId)
    {
        if (soundId != 0)
        {
            if (speakerStream !=0)
            {
                soundPool.stop(speakerStream);
            }
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                speakerStream = soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        }, 500);
    }

    public int getHighScore()
    {
        return hs;
    }
}