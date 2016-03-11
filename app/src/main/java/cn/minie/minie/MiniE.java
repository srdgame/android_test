package cn.minie.minie;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.io.File;

import android_serialport_api.SerialPort;

public class MiniE extends AppCompatActivity implements SmartDoorCallbacks {
    private static final String TAG = "MiniEMainActivity";
    private ThreadSmartDoor door;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_e);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Button btn = (Button) findViewById(R.id.button);
        door = new ThreadSmartDoor(this);

        String ttt = door.tests();
        btn.setText(door.version());
        door.Start();
        door.init();
        String tex = door.getCards();
        EditText editText = (EditText)findViewById(R.id.editText);
        editText.setText(tex);
        try {
            JSONTokener jsonParser = new JSONTokener(tex);
            JSONArray cards = (JSONArray) jsonParser.nextValue();
            for (int i = 0; i < cards.length(); i++){
                Log.d(TAG, "Get Card: " + cards.getString(i));
            }

            int n = door.setCards(tex);
            Log.d(TAG, "Finished setCards returns " + n);
        }
        catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mini_e, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        door.Stop();
        super.onDestroy();
    }

    public void onInit(int code) {

    }
    public void onClose() {

    }
    public void onPunch(String card) {

    }
    public boolean onCode(String code) {
        return false;
    }
    public boolean checkCard(String card) {
        return false;
    }
}
