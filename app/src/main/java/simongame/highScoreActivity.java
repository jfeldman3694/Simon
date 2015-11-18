package simongame;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;
import java.util.StringTokenizer;

public class highScoreActivity extends AppCompatActivity
{

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    ArrayList<String> leadership;
    Set<String> importHS;
    ListView hs_leader;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        leadership = new ArrayList<>();

        sharedPref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        /*getSharedPreferences("mypref", 0);*/
        editor= sharedPref.edit();

        importHS = sharedPref.getStringSet("highscore_list", null);
        if(!importHS.isEmpty())
        {

            for(String s : importHS)
            {
                leadership.add(s);
            }
        }


        String[] scores = new String[5];

        for(int i=0; i<leadership.size(); i++)
        {
            scores[i] = leadership.get(i);

        }
        if(leadership.size() < 5)
        {
            int size = leadership.size();
            for(int i=size; i<5; i++)
            {
                scores[i] = "-";

            }
        }
        int check  = 0;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, scores);

        ListView list = (ListView) findViewById(R.id.listView);

        list.setAdapter(adapter);
        /*check = sharedPref.getInt("highscore", 0);
        Toast.makeText(this, leadership.get(0).toString(), Toast.LENGTH_LONG).show();*/



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_high_score, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}