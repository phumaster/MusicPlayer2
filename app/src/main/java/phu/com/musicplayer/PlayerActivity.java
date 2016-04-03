package phu.com.musicplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtName;
    Button btnPre, btnPause, btnNext, btnSeekToPre, btnSeekToNext;
    SeekBar seekBar;
    MediaPlayer media = new MediaPlayer();
    int position;
    ArrayList<File> songList;
    Thread updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle i = getIntent().getExtras();
        position = i.getInt("position");
        songList = (ArrayList) i.getParcelableArrayList("songList");
        txtName = (TextView) findViewById(R.id.txtName);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        btnPre = (Button) findViewById(R.id.btnPre);
        btnPause = (Button) findViewById(R.id.btnPause);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnSeekToNext = (Button) findViewById(R.id.btnSeekToNext);
        btnSeekToPre = (Button) findViewById(R.id.btnSeekToPre);

        btnPre.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnSeekToNext.setOnClickListener(this);
        btnSeekToPre.setOnClickListener(this);

        updateSeekBar = new Thread() {
            @Override
            public void run() {
                int totalDuration = media.getDuration();
                int currentPosition = 0;
                while (currentPosition < totalDuration) {
                    try {
                        sleep(300);
                        currentPosition = media.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //super.run();
            }
        };


        try {
            media.setDataSource(songList.get(position).getPath());
            txtName.setText(songList.get(position).getName());
            media.prepare();
            media.start();
            updateSeekBar.start();
            seekBar.setMax(media.getDuration());

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    media.seekTo(seekBar.getProgress());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnPause:
                if (media.isPlaying()) {
                    media.pause();
                } else {
                    media.start();
                }
                break;
            case R.id.btnPre:
                media.stop();
                media.release();
                try {
                    position = (position - 1) % songList.size();
                    if (0 > (position - 1)) {
                        position = songList.size() - 1;
                    } else {
                        position -= 1;
                    }
                    media.setDataSource(songList.get(position).getPath());
                    txtName.setText(songList.get(position).getName());
                    media.prepare();
                    media.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnNext:
                media.stop();
                media.release();
                try {
                    position = (position + 1) % songList.size();
                    media.setDataSource(songList.get(position).getPath());
                    txtName.setText(songList.get(position).getName());
                    media.prepare();
                    media.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnSeekToNext:
                media.seekTo(media.getCurrentPosition() + 5000);
                break;
            case R.id.btnSeekToPre:
                media.seekTo(media.getCurrentPosition() - 5000);
                break;
        }
    }
}
