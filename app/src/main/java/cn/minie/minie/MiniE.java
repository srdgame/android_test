package cn.minie.minie;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MiniE extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_e);
        hideNavBar();
        hideSoftKeyboard();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        hideNavBar();
        hideSoftKeyboard();
    }

    public void showTests(View v){
        Intent i = new Intent(this, TestPage.class);
        startActivity(i);
    }

    public void showApps(View v){
//        Intent i = new Intent(this, AppsListActivity.class);
//        startActivity(i);
    }

    public void onDialInput(View v) {
        TextView input = (TextView) findViewById(R.id.dialInput);

        String tag = (String)v.getTag();
        if (tag.length() == 1) {
            input.append(tag);
        }
        if (tag.equals("call")) {
            input.setText("");
            input.setHint(getResources().getString(R.string.calling));
        }
        if (tag.equals("back")) {
            String inputText = input.getText().toString();
            if (inputText.length() == 1) {
                input.setHint(getResources().getString(R.string.input_room_number));
            }

            if (inputText.length() > 0) {
                input.setText(inputText.substring(0, inputText.length() - 1));
            }
        }
        if (tag.equals("clear")) {
            input.setText("");
            input.setHint(getResources().getString(R.string.input_room_number));
        }
        String inputText = input.getText().toString();
        if (inputText.length() != 0) {
            ListView pList = (ListView) findViewById(R.id.personList);
        }
    }
    private void hideNavBar() {
        if (Build.VERSION.SDK_INT >= 19) {
            View v = getWindow().getDecorView();
            v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }
}