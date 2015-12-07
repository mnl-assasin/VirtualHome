package com.olfu.virtualhomeinteriordesigning.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.olfu.virtualhomeinteriordesigning.R;
import com.olfu.virtualhomeinteriordesigning.UtilsApp;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {

    VideoView myVideoView;
    MediaController mediaControls;

    Button btnNew, btnLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        initVideo();
        initButtons();

    }

    private void initVideo() {
        if (mediaControls == null) {

            mediaControls = new MediaController(this.getApplicationContext());
        }
        myVideoView = (VideoView) findViewById(R.id.videoView);
        myVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.homevid));
        myVideoView.requestFocus();

        //we also set an setOnPreparedListener in order to know when the video file is ready for playback

        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                myVideoView.seekTo(0);
                mediaPlayer.setLooping(true);
                myVideoView.start();
            }
        });
    }

    private void initButtons() {

        btnNew = (Button) findViewById(R.id.btnNew);
        btnNew.setOnClickListener(this);

        btnLoad = (Button) findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btnNew) {
            startActivity(new Intent(this, CameraActivity.class));
//            startActivity(new Intent(this, Designer.class));
        }

        if (v == btnLoad) {
            dialogProjects();
        }
    }

    private void dialogProjects() {

        final ArrayList<ProjectItem> item = readDB("tbl_project");
        final ArrayList<String> projects = new ArrayList<>();

        for (int ctr = 0; ctr < item.size(); ctr++) {
            projects.add(item.get(ctr).getName());
        }

        ListView listView = new ListView(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, projects);
        listView.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(listView);
        builder.setTitle("Choose a project");

        final AlertDialog dialog = builder.create();
        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UtilsApp.toast("ITEM CLICK " + position + " NAME " + projects.get(position));
                UtilsApp.toast(item.get(position).getId() + " : " + item.get(position).getBackground());
                loadProject(item.get(position).getId(), item.get(position).getBackground());
                dialog.dismiss();
            }
        });


    }

    private void loadProject(String id, String background) {

        Bundle extras = new Bundle();
        extras.putString("root", "main");
        extras.putString("id", id);
        extras.putString("background", background);

        Log.d("TAG", background);
        startActivity(new Intent(this, Designer2.class).putExtras(extras));
        finish();


    }

    private ArrayList<ProjectItem> readDB(String TABLE_NAME) {

        ArrayList<ProjectItem> source = new ArrayList<>();

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            source.add(new ProjectItem(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
        }

        return source;

    }

    public class ProjectItem {

        String id, name, background;

        public ProjectItem(String id, String name, String background) {
            this.id = id;
            this.name = name;
            this.background = background;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getBackground() {
            return background;
        }
    }


}
