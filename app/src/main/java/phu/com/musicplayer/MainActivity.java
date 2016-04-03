package phu.com.musicplayer;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;//tiep di
    ArrayList<File> songList;
    ArrayList<SongDetail> songDetails = new ArrayList<SongDetail>();
    ListView lvSong;
    MediaMetadataRetriever m = new MediaMetadataRetriever();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        getToolbar();

        songList = this.getAllSong(Environment.getExternalStorageDirectory());
        if(songList.size() <= 0) {
            Toast.makeText(MainActivity.this, "No music file", Toast.LENGTH_SHORT).show();
        }else{
            // have song
            int count = songList.size();

            String[] items1 = new String[songList.size()];
            for(int i = 0; i < count; i++) {
                m.setDataSource(songList.get(i).getPath().toString());

                String name = m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                String album = m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                String artist = m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                String bitRate = m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);

                songDetails.add(new SongDetail(name, artist, album, bitRate));
                items1[i] = songList.get(i).getName().toString();
            }

            lvSong = (ListView) findViewById(R.id.lvSong);

            ArrayAdapter<String> lvAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, items1);
            lvSong.setAdapter(lvAdapter);

            lvSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent switchPlayer = new Intent(MainActivity.this, PlayerActivity.class);
                    switchPlayer.putExtra("position", position).putExtra("songList", songList);
                    startActivity(switchPlayer);
                }
            });
        }

    }

    private ArrayList<File> getAllSong(File directory) {
        File[] allFile = directory.listFiles();
        ArrayList<File> al = new ArrayList<File>();
        for(File singleFile : allFile) {
            if(singleFile.isDirectory() && !singleFile.isHidden()) {
                al.addAll(this.getAllSong(singleFile));
            }else{
                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav") || singleFile.getName().endsWith(".flac")) {
                    al.add(singleFile);
                }
            }
        }
        return al;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void getToolbar(){
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
