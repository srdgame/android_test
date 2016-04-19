package cn.minie.minie;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONTokener;

import cn.minie.aidl.IAppMgrInterface;

public class TestPage extends AppCompatActivity implements SmartDoorCallbacks {

    private static final String TAG = "MiniETestPageActivity";
    private ThreadSmartDoor door;
    private UpdateManager mUpdateManager;

    IAppMgrInterface mService;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IAppMgrInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_page);
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

        mUpdateManager = new UpdateManager(this);
        door = new ThreadSmartDoor(this);

        Button btn = (Button) findViewById(R.id.btnText);
        btn.setText(door.version());

        int r = door.init("/dev/ttySAC1", 9600, 0);
        if (r == 0) {
            Log.d(TAG, "Door connection initialized!");

            door.Start();
            String tex = door.getCards();
            TextView log = (TextView) findViewById(R.id.textLog);
            log.append("Cards JSON:" + tex);
            try {
                JSONTokener jsonParser = new JSONTokener(tex);
                JSONArray cards = (JSONArray) jsonParser.nextValue();
                for (int i = 0; i < cards.length(); i++) {
                    Log.d(TAG, "Get Card: " + cards.getString(i));
                    log.append("Get Card: " + cards.getString(i));
                }

                int n = door.setCards(tex);
                Log.d(TAG, "Finished setCards returns " + n);
                log.append("Finished setCards returns " + n);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Intent i = new Intent("cn.minie.daemon.AppMgr");
        i.setPackage("cn.minie.daemon");
        bindService(i, mConnection, Context.BIND_AUTO_CREATE);
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
    protected void onStop() {
        super.onStop();
        if (mService != null) {
            unbindService(mConnection);
        }
    }
    @Override
    protected void onDestroy() {
        door.Stop();
        super.onDestroy();
    }

    /**
     * Controls handlers
     */

    /**
     * Test Button click
     * @param v
     */
    public void onTestJNI(View v){
        try {
            String ttt = door.tests();
            TextView log = (TextView) findViewById(R.id.textLog);
            log.append(ttt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onDaemonInstall(View v){
        try {
            //String fp = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/test.apk";
            String fp = "/sdcard/Download/test.apk";
            int result = mService.Install(fp);
            result = result + 100;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onDaemonLaunch(View v){
        try {
            String pn = "com.tortoisekungfu.shoudiantong";
            String an = "com.tortoisekungfu.shoudiantong.MainActivity";
            int result = mService.Start(pn, an);
//                    Intent intent = new Intent(Intent.ACTION_MAIN);
//                    intent.setComponent(new ComponentName(pn,an));
//                    startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onDaemonRemoteInstall(View v) {
        try {
            int result = mService.InstallRemote("http://172.16.2.38:8081/test.apk");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onTestUpdate(View v){
        try {
            mUpdateManager.DownloadApk("http://172.16.2.38:8081/test.apk", "test.apk");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Implement the SmartDoorCallbacks
     * @param code
     */
    public void onInit(int code) {
       // Log.d(TAG, "onInit: " + code);
    }
    public void onClose() {
       // Log.d(TAG, "onClose: ");

    }
    public void onPunch(String card, int err) {
       // Log.d(TAG, "onPunch: " + card + " " + err);

    }
    public boolean onCode(int code) {
       // Log.d(TAG, "onCode: " + code);

        return false;
    }
    public boolean checkCard(String card) {

      //  Log.d(TAG, "checkCard: " + card);
        return false;
    }
}
