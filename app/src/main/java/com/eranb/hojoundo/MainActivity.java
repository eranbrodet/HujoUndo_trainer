package com.eranb.hojoundo;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "HojoUndo";
    private final int TOTAL = 13;
    private ArrayList<String> order = new ArrayList<String>() {{
        add("hajiki_uke_hiraken_tsuki"); add("hiji_tsuki"); add("koi_no_shippo_uchi_tate_uchi");
        add("koi_no_shippo_uchi_yoko_uchi"); add("mawashi_tsuki"); add("seiken_tsuki");
        add("shomen_geri"); add("shomen_hajiki"); add("sokuto_geri"); add("tenshin_kosoku_geri");
        add("tenshin_shoken_tsuki"); add("tenshin_zenshoku_geri"); add("uke_shuto_uchi_ura_uchi_shoken_tsuki");
    }};
    private MediaPlayer mp;
    private int cur_index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button button = (Button) findViewById(R.id.newNumberButton);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newNumber(button);
            }
        });
    }

    private void newNumber(final Button button){
        if (cur_index == 0) {
            // Re-shuffle every new round
            Log.i(TAG, "Shuffling");
            Collections.shuffle(order);
            Log.d(TAG, "New order "+ order);
        }
        // Get current element and increment
        String cur = order.get(cur_index);
        Log.i(TAG, "New item " + cur + "(" + cur_index + ")");
        cur_index = (cur_index+1) % TOTAL;

        // Set new text
        TextView tv1 = (TextView)findViewById(R.id.number);
        assert tv1 != null;
        tv1.setText(cur.replace("_", " "));

        // play audio corresponding to element
        int res_id = getResources().getIdentifier(cur, "raw", getPackageName());
        mp = MediaPlayer.create(MainActivity.this, res_id);
        mp.start();

        // Disable button (will be re-enabled after play)
        button.setEnabled(false);

        // Set callback for post-play action
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        endPlay(button);
                    }
                });
            }
        });
    }

    private void endPlay(final Button button) {
        mp.release();
        button.setEnabled(true);
    }
}
