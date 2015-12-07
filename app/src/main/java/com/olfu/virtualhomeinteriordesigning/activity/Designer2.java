package com.olfu.virtualhomeinteriordesigning.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.olfu.virtualhomeinteriordesigning.R;
import com.olfu.virtualhomeinteriordesigning.Utilities.RecyclerItemClickListener;
import com.olfu.virtualhomeinteriordesigning.UtilsApp;
import com.olfu.virtualhomeinteriordesigning.adapter.FurnitureAdapter;
import com.olfu.virtualhomeinteriordesigning.model.ImageItem;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Designer2 extends AppCompatActivity implements View.OnClickListener {

    final String TAG = getClass().getSimpleName();
    final String DIALOG_TITLE = "Choose A Furniture";

    RelativeLayout container;
    LinearLayout controlsContainer;

    ImageView backgroundImage;
    Button btnAdd, btnChange, btnCustomize, btnRemove, btnSave;

    AlertDialog dialog;
    AlertDialog.Builder builder;

    ArrayList<CustomImage> listImages;
    ArrayList<ImageItem> listSources;

    boolean controlsVisiblity = true;

    // D A T A B A S E //
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    ContentValues values;
    Cursor cursor;
    // D A T A B A S E //

    // positon of image in Arraylist
    int index = -1;
    String background, root, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designer2);

        initialized();

    }

    private void initialized() {

        dbHelper = DatabaseHelper.getInstance(this);

        initContainer();
        initControls();
        initValues();
        getBundle();
    }

    private void initContainer() {
        // Main Container
        container = (RelativeLayout) findViewById(R.id.container);

        backgroundImage = (ImageView) findViewById(R.id.backgroundImage);

        // Container of controls
        controlsContainer = (LinearLayout) findViewById(R.id.controlsContainer);
        hideControls(container);
    }

    private void initControls() {

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnChange = (Button) findViewById(R.id.btnChange);
        btnChange.setOnClickListener(this);

        btnCustomize = (Button) findViewById(R.id.btnCustomize);
        btnCustomize.setOnClickListener(this);

        btnRemove = (Button) findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(this);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

    }

    private void initValues() {

        listImages = new ArrayList<>();
        listSources = new ArrayList<>();
    }

    private void getBundle() {

        Bundle b = getIntent().getExtras();
        if (b != null) {

            root = b.getString("root", null);
            background = b.getString("background", null);
            Picasso.with(getApplicationContext()).load(Uri.fromFile(new File(background))).into(backgroundImage);
            if (root.equals("main")) {

                id = b.getString("id", null);
                loadProject(id);
            }

        }

    }

    private void loadProject(String id) {
        CustomImage ci;
        db = dbHelper.getReadableDatabase();
        cursor = db.query("tbl_project_resources", null, "id = '" + id + "'", null, null, null, null);
        String source;
        String name, image;
        float xPos, yPos;
        int width, height;
        Log.d(TAG, "Load Project");
        Log.d(TAG, "Counts: " + cursor.getCount());

        while (cursor.moveToNext()) {

            name = cursor.getString(1);
            image = cursor.getString(2);
            xPos = cursor.getFloat(3);
            yPos = cursor.getFloat(4);
            width = cursor.getInt(5);
            height = cursor.getInt(6);
            Log.d(TAG, name + " : " + image);

            ci = new CustomImage(this);
            ci.setX(xPos);
            ci.setY(yPos);
            ci.setLayoutParams(new ViewGroup.LayoutParams(width, height));

            source = "android.resource://" + getPackageName() + "/drawable/" + image;
            Picasso.with(getApplicationContext()).load(source).into(ci);

            listImages.add(ci);
            listSources.add(new ImageItem(name, image));

            ci = listImages.get(listImages.size() - 1);
            ci.setOnClickListener(this);
            ci.setOnTouchListener(ci);
            container.addView(ci);

        }

    }

    @Override
    public void onClick(View v) {

        if (v == btnAdd) {
            releaseIndex();
            showFurnitureChooser();
        }

        if (v == btnChange) {
            showFurnitureChooser();
        }

        if (v == btnCustomize) {
            customFurnitureChooser(getIndex());
        }

        if (v == btnRemove) {
            removeFurniture();
        }

        if (v == btnSave) {
            saveProject();
        }

        for (int ctr = 0; ctr < listImages.size(); ctr++) {
            if (v == listImages.get(ctr)) {
//                listImages.get(ctr).bringToFront();
                v.bringToFront();
                Log.d(TAG, "Postion " + ctr);
                setIndex(ctr);
            }
        }


    }

    private void showFurnitureChooser() {

        String furnitures[] = {"Cabinet", "Door", "Sofa", "Table"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, furnitures);

        final ListView list = new ListView(this);
        list.setAdapter(adapter);

        dialog = dialogCreator(list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String furniture = list.getItemAtPosition(position).toString().toLowerCase();
//                UtilsApp.toast("Furniture " + furniture);
                dialog.dismiss();
                showFurnitures(position);

            }
        });


    }

    private void showFurnitures(final int position) {

        final ArrayList<ImageItem> listFurnitures = populateFurnitures(position);

        View v = getLayoutInflater().inflate(R.layout.dialog_furniture, null);

        dialog = dialogCreator(v);

        RecyclerView rv = (RecyclerView) v.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rv.setAdapter(new FurnitureAdapter(getApplicationContext(), listFurnitures));
        rv.setLayoutManager(llm);

        rv.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                String furniture = listFurnitures.get(position).getImage();

                if (getIndex() == -1) {
                    addFurniture(listFurnitures.get(position));
                } else {
                    changeFurniture(listFurnitures.get(position));
                }
                dialog.dismiss();
            }
        }));


    }


    private void customFurnitureChooser(int position) {

        final ArrayList<ImageItem> listFurnitures = new ArrayList<>();

        db = dbHelper.getReadableDatabase();
        String name = listSources.get(position).getName();

        Cursor cursor = db.query("tbl_furniture", null, "name = '" + name + "'", null, null, null, null);
        Log.d(TAG, "Cursor count: " + cursor.getCount());

        while (cursor.moveToNext()) {

            String sName = cursor.getString(2);
            String image = cursor.getString(3);
            Log.d(TAG, "Name: " + sName + " Image: " + image);
            listFurnitures.add(new ImageItem(sName, image));
        }
        cursor.close();
        db.close();

        //

        View v = getLayoutInflater().inflate(R.layout.dialog_furniture, null);

        dialog = dialogCreator(v);

        RecyclerView rv = (RecyclerView) v.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rv.setAdapter(new FurnitureAdapter(getApplicationContext(), listFurnitures));
        rv.setLayoutManager(llm);

        rv.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                String furniture = listFurnitures.get(position).getImage();

//                if (getIndex() == -1) {
//                    addFurniture(listFurnitures.get(position));
//                } else {
//                    changeFurniture(listFurnitures.get(position));
//                }

                changeFurniture(listFurnitures.get(position));
                dialog.dismiss();
            }
        }));


    }


    private void addFurniture(ImageItem item) {

        String source = "android.resource://" + getPackageName() + "/drawable/" + item.getImage();

        CustomImage ci = new CustomImage(this);
        Picasso.with(getApplicationContext()).load(source).into(ci);

        listImages.add(ci);
        listSources.add(item);

        ci = listImages.get(listImages.size() - 1);
        ci.setOnClickListener(this);
        ci.setOnTouchListener(ci);
        container.addView(ci);


//        listImages.get(listImages.size() - 1).setOnClickListener(this);
//        container.addView(listImages.get(listImages.size() - 1));

    }

    private void changeFurniture(ImageItem item) {

        String source = "android.resource://" + getPackageName() + "/drawable/" + item.getImage();
        CustomImage image = listImages.get(getIndex());
        Picasso.with(getApplicationContext()).load(source).into(image);

        listImages.set(getIndex(), image);
        listSources.set(index, item);

        releaseIndex();
    }

    private void removeFurniture() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete a Furniture");
        builder.setMessage("Do you really want to delete this furniture?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                removeFurniture(getIndex());
                dialog.dismiss();

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }

    private void removeFurniture(int position) {
//        ((ViewManager) custom.get(getIndex()).getParent()).removeView(custom.get(getIndex()));
        ((ViewManager) listImages.get(position).getParent()).removeView(listImages.get(position));
        listImages.remove(position);
        listSources.remove(position);

        Log.d(TAG, "Furniture Count: " + listImages.size() + " Sources Count: " + listSources.size());
    }


    private void saveProject() {


        final EditText txtFile = new EditText(this);
        builder = new AlertDialog.Builder(this);
        builder.setView(txtFile);
        builder.setTitle("Enter project name");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String filename = txtFile.getText().toString();
                // do more
                saveProject(filename);
                dialog.dismiss();
            }
        });
        builder.show();

        UtilsApp.toast("Project Save Succesfully!");


    }

    private void saveProject(String filename) {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String id = filename + timeStamp;

        db = dbHelper.getWritableDatabase();

        values = new ContentValues();

        values.put("id", id);
        values.put("name", filename);
        values.put("background", background);

        db.insert("tbl_project", null, values);

        values = new ContentValues();
        for (int ctr = 0; ctr < listImages.size(); ctr++) {
            String name = listSources.get(ctr).getName();
            String image = listSources.get(ctr).getImage();
            CustomImage ci = listImages.get(ctr);

            ViewGroup.LayoutParams params = ci.getLayoutParams();
            float xPos = ci.getX();
            float yPos = ci.getY();
            int width = params.width;
            int height = params.height;

            values.put("id", id);
            values.put("name", name);
            values.put("image", image);
            values.put("x_position", xPos);
            values.put("y_position", yPos);
            values.put("width", width);
            values.put("height", height);

            db.insert("tbl_project_resources", null, values);


        }
    }

    private ArrayList<ImageItem> populateFurnitures(int position) {

        ArrayList<ImageItem> listFurnitures = new ArrayList<>();

        switch (position) {

            case 0:
                listFurnitures.add(new ImageItem("cabinet", "cabinet1"));
                listFurnitures.add(new ImageItem("cabinet", "cabinet2"));
                listFurnitures.add(new ImageItem("cabinet", "cabinet3"));
                listFurnitures.add(new ImageItem("cabinet", "cabinet4"));
                listFurnitures.add(new ImageItem("cabinet", "cabinet5"));
                listFurnitures.add(new ImageItem("cabinet", "cabinet6"));
                break;

            case 1:
                listFurnitures.add(new ImageItem("door_design", "door_design1"));
                listFurnitures.add(new ImageItem("door_glass", "door_glass1"));
                listFurnitures.add(new ImageItem("door_plain", "door_plain1"));
                break;

            case 2:
                listFurnitures.add(new ImageItem("sofa_double_s", "sofa_double_s1"));
                listFurnitures.add(new ImageItem("sofa_double_w", "sofa_double_w1"));
                listFurnitures.add(new ImageItem("sofa_double", "sofa_double1"));
                listFurnitures.add(new ImageItem("sofa_single", "sofa_single1"));
                listFurnitures.add(new ImageItem("sofa_triple", "sofa_triple1"));
                listFurnitures.add(new ImageItem("sofa_triple_w", "sofa_triple_w1"));
                break;

            case 3:
                listFurnitures.add(new ImageItem("table_glass", "table_center_glass1"));
                listFurnitures.add(new ImageItem("table_glass", "table_center_glass2"));
                listFurnitures.add(new ImageItem("table_glass", "table_center_glass3"));
                break;
        }

        return listFurnitures;

    }

    private AlertDialog dialogCreator(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(DIALOG_TITLE).setView(v);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        return dialog;
    }

    private void hideControls(View view) {

        if (!(view instanceof CustomImage)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
//                    btn.setEnabled(false);
                    releaseIndex();
                    controls();
                    return false;
                }
            });
        }

    }

    private void controls() {

        if (isVisible()) {
            controlsContainer.setVisibility(View.VISIBLE);
        } else {
            controlsContainer.setVisibility(View.INVISIBLE);

        }
        setControlsVisiblity();

    }

    public boolean isVisible() {
        return controlsVisiblity;
    }

    public void setControlsVisiblity() {
//        this.controlsVisiblity = controlsVisiblity;
        controlsVisiblity = !controlsVisiblity;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;

        btnChange.setEnabled(true);
        btnCustomize.setEnabled(true);
        btnRemove.setEnabled(true);
    }

    public void releaseIndex() {
        setIndex(-1);
        btnChange.setEnabled(false);
        btnCustomize.setEnabled(false);
        btnRemove.setEnabled(false);
    }

//    public class ImageProperties {
//        //   db.execSQL("CREATE TABLE IF NOT EXISTS tbl_project_resources(id varchar, name varchar, x_position real, y_position real, width integer, height integer)");
//        String name;
//        float xPos, yPos;
//        int width, height;
//
//        public String getName() {
//            return name;
//        }
//
//        public float getxPos() {
//            return xPos;
//        }
//
//        public float getyPos() {
//            return yPos;
//        }
//
//        public int getWidth() {
//            return width;
//        }
//
//        public int getHeight() {
//            return height;
//        }
//
//        public ImageItem(String name, float xPos, float yPos, int width, int height) {
//
//            this.name = name;
//            this.xPos = xPos;
//            this.yPos = yPos;
//            this.width = width;
//            this.height = height;
//        }
//    }

}
