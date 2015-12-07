package com.olfu.virtualhomeinteriordesigning.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.olfu.virtualhomeinteriordesigning.R;
import com.olfu.virtualhomeinteriordesigning.Utilities.RecyclerItemClickListener;
import com.olfu.virtualhomeinteriordesigning.UtilsApp;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Designer extends Activity implements View.OnClickListener {

    FrameLayout container;
    CustomImage customImage;
    Button btnFurniture, btnDelete, btnSave;

    ArrayList<String> source;
    ArrayList<CustomImage> images;
    ArrayList<String> imageSource;


    ImageView backgroundImage;

    String background, root, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designer);


        initValues();
        initViews();
        getBundle();

    }

    private void getBundle() {

        Bundle b = getIntent().getExtras();
        if (b != null) {

            root = b.getString("root", null);
            background = b.getString("background", null);
            Picasso.with(getApplicationContext()).load(Uri.fromFile(new File(background))).into(backgroundImage);
            if (root.equals("main")) {

                id = b.getString("id", null);
                loadResources(id);
            }
        }

    }

    private void initValues() {
        images = new ArrayList<>();
        imageSource = new ArrayList<>();
    }

    public void initViews() {


        container = (FrameLayout) findViewById(R.id.container);

        backgroundImage = (ImageView) findViewById(R.id.imgBackground);


        btnFurniture = (Button) findViewById(R.id.btnFurniture);
        btnFurniture.setOnClickListener(this);

        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

    }

    private void loadResources(String id) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("tbl_project_resources", null, null, null, null, null, null);
        UtilsApp.toast("ID: + " + id + "Count: " + cursor.getCount());

        String name;
        float xPos, yPos;
        int width, height;

        ArrayList<ImageItem> item = new ArrayList<>();
        Log.d("TAG", id);

        while (cursor.moveToNext()) {

            String cId = cursor.getString(0);
            Log.d("TAG", cId);
            if (cId.equals(id)) {
                Log.d("TAG", "TRUE " + cId);
                name = cursor.getString(1);
                Log.d("TAG", "source " + name);
                xPos = cursor.getFloat(2);
                yPos = cursor.getFloat(3);
                width = cursor.getInt(4);
                height = cursor.getInt(5);

                item.add(new ImageItem(name, xPos, yPos, width, height));
            }
        }


        for (int ctr = 0; ctr < item.size(); ctr++) {
            customImage = new CustomImage(this);
            customImage.setOnTouchListener(customImage);
            customImage.setAdjustViewBounds(true);

//            String source = "android.resource://" + getPackageName() + "/drawable/" + item.get(ctr).getName();
            String source = item.get(ctr).getName();
            Picasso.with(getApplicationContext()).load(source).into(customImage);
            customImage.setX(item.get(ctr).getxPos());
            customImage.setY(item.get(ctr).getyPos());

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(item.get(ctr).getWidth(), item.get(ctr).getHeight());
            customImage.setLayoutParams(params);

            container.addView(customImage);

            images.add(customImage);
            imageSource.add(source);

        }

    }

    @Override
    public void onClick(View v) {
        if (v == btnFurniture) {
//            showFurnitureTypes();
            showDoors();
        }

        if (v == btnSave) {
            saveProject();
        }

        if (v == btnDelete) {
            delete();
        }
    }

    private void showFurnitureTypes() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this.getApplicationContext());
        dialog.setTitle("xcv").show();

    }

    private void showDoors() {

        source = readDB("tbl_door");
        Log.d("TAG", "size" + source.size());
        View v = getLayoutInflater().inflate(R.layout.dialog_furniture, null);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Choose A Furniture");
        dialog.setView(v);

        final AlertDialog alert = dialog.create();
        alert.show();

        RecyclerView rv = (RecyclerView) v.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
//        rv.setAdapter(new FurnitureAdapter(getApplicationContext(), source));
        rv.setLayoutManager(llm);

        rv.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                addFurniture(position);
                alert.dismiss();
            }
        }));
    }

    private void addFurniture(int position) {
        UtilsApp.toast(" XC" + position);

        customImage = new CustomImage(this);
        customImage.setOnTouchListener(customImage);
        customImage.setAdjustViewBounds(true);

        String source = "android.resource://" + getPackageName() + "/drawable/" + this.source.get(position);
        Picasso.with(getApplicationContext()).load(source).into(customImage);
        container.addView(customImage);

        images.add(customImage);
        imageSource.add(source);

    }

    private void saveProject() {

        for (int ctr = 0; ctr < imageSource.size(); ctr++) {
            Log.d("TAG", "Source: " + imageSource.get(ctr) + " : " + images.get(ctr).getX() + " : " + images.get(ctr).getY());
        }

        final EditText fileName = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(fileName);
        builder.setTitle("Enter Project Name");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String filename = fileName.getText().toString();
                // do more
                saveProject(filename);
                dialog.dismiss();
            }
        });
        builder.show();


    }

    private void saveProject(String filename) {
        UtilsApp.toast("BACKGROUND: " + background);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String id = filename + timeStamp;

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("id", id);
        values.put("name", filename);
        values.put("background", background);

        db.insert("tbl_project", null, values);

        UtilsApp.toast("ID " + id + "PROJ NAME: " + filename);


        values = new ContentValues();
        for (int ctr = 0; ctr < images.size(); ctr++) {
            String name = imageSource.get(ctr);
            CustomImage image = images.get(ctr);
            ViewGroup.LayoutParams params = image.getLayoutParams();
            float xPos = image.getX();
            float yPos = image.getY();
            int width = params.width;
            int height = params.height;

            values.put("id", id);
            values.put("name", name);
            values.put("x_position", xPos);
            values.put("y_position", yPos);
            values.put("width", width);
            values.put("height", height);

            db.insert("tbl_project_resources", null, values);
            UtilsApp.toast("ID " + id + " inserted: " + ctr);
        }


    }

    private void delete() {

        ArrayList<String> projects = readDB("tbl_project");

        for (int ctr = 0; ctr < projects.size(); ctr++) {
            Log.d("TAG", "Project:  " + projects.get(ctr) + " " + ctr);
        }

    }

    private ArrayList<String> readDB(String TABLE_NAME) {

        ArrayList<String> source = new ArrayList<>();

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            source.add(cursor.getString(1));
        }

        return source;

    }

    public class ImageItem {
        //   db.execSQL("CREATE TABLE IF NOT EXISTS tbl_project_resources(id varchar, name varchar, x_position real, y_position real, width integer, height integer)");
        String name;
        float xPos, yPos;
        int width, height;

        public String getName() {
            return name;
        }

        public float getxPos() {
            return xPos;
        }

        public float getyPos() {
            return yPos;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public ImageItem(String name, float xPos, float yPos, int width, int height) {

            this.name = name;
            this.xPos = xPos;
            this.yPos = yPos;
            this.width = width;
            this.height = height;
        }
    }

}
