package com.olfu.virtualhomeinteriordesigning.activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mykelneds on 10/14/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper mInstance = null;

//    private Context context;


    private DatabaseHelper(Context context) {
        super(context, "furniture_db", null, 3);
    }

    public static DatabaseHelper getInstance(Context mContext) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(mContext);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        createFurniture(db);
        createProject(db);
    }

    private void createProject(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_project(id varchar, name varchar, background varchar)");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_project_resources(id varchar, name varchar, image varchar, x_position real, y_position real, width integer, height integer)");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS tbl_furniture");
        db.execSQL("DROP TABLE IF EXISTS tbl_project");
        db.execSQL("DROP TABLE IF EXISTS tbl_project_resources");

        onCreate(db);
    }

    private void createFurniture(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_furniture(room integer, kind varchar, name varchar, image varchar)");

//        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'','','')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'cabinet','cabinet','cabinet1')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'cabinet','cabinet','cabinet2')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'cabinet','cabinet','cabinet3')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'cabinet','cabinet','cabinet4')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'cabinet','cabinet','cabinet5')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'cabinet','cabinet','cabinet6')");

        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'door','door_design','door_design1')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'door','door_design','door_design2')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'door','door_design','door_design3')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'door','door_design','door_design4')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'door','door_design','door_design5')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'door','door_design','door_design6')");

        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'door','door_glass','door_glass1')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'door','door_glass','door_glass2')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'door','door_glass','door_glass3')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'door','door_glass','door_glass4')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'door','door_glass','door_glass5')");

        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'door','door_plain','door_plain1')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'door','door_plain','door_plain2')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'door','door_plain','door_plain3')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'door','door_plain','door_plain4')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'door','door_plain','door_plain5')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'door','door_plain','door_plain6')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'door','door_plain','door_plain7')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'door','door_plain','door_plain8')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'door','door_plain','door_plain9')");

        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'sofa','sofa_double_s','sofa_double_s1')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'sofa','sofa_double_s','sofa_double_s2')");

        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'sofa','sofa_double_w','sofa_double_w1')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'sofa','sofa_double_w','sofa_double_w2')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'sofa','sofa_double_w','sofa_double_w3')");

        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'sofa','sofa_double','sofa_double1')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'sofa','sofa_double','sofa_double2')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'sofa','sofa_double','sofa_double3')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'sofa','sofa_double','sofa_double4')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'sofa','sofa_double','sofa_double5')");

        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'sofa','sofa_single','sofa_single1')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'sofa','sofa_single','sofa_single2')");

        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'sofa','sofa_triple','sofa_triple1')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'sofa','sofa_triple','sofa_triple2')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'sofa','sofa_triple','sofa_triple3')");

        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'sofa','sofa_triple_w','sofa_triple_w1')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'sofa','sofa_triple_w','sofa_triple_w2')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'sofa','sofa_triple_w','sofa_triple_w3')");

//        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'table','table_glass','table_center_')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'table','table_glass','table_center_glass1')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'table','table_glass','table_center_glass2')");
        db.execSQL("INSERT INTO tbl_furniture(room, kind, name, image) VALUES (0,'table','table_glass','table_center_glass3')");


    }

    private void populateDB(SQLiteDatabase db) {

        db.execSQL("INSERT INTO tbl_door (id, name) VALUES (1, 'd1')");
        db.execSQL("INSERT INTO tbl_door (id, name) VALUES (2, 'd2')");
        db.execSQL("INSERT INTO tbl_door (id, name) VALUES (3, 'd3')");
        db.execSQL("INSERT INTO tbl_door (id, name) VALUES (4, 'd4')");
        db.execSQL("INSERT INTO tbl_door (id, name) VALUES (5, 'd5')");
        db.execSQL("INSERT INTO tbl_door (id, name) VALUES (6, 'd6')");
        db.execSQL("INSERT INTO tbl_door (id, name) VALUES (7, 'd7')");
        db.execSQL("INSERT INTO tbl_door (id, name) VALUES (8, 'd8')");
        db.execSQL("INSERT INTO tbl_door (id, name) VALUES (9, 'd9')");
        db.execSQL("INSERT INTO tbl_door (id, name) VALUES (10, 'd10')");
        db.execSQL("INSERT INTO tbl_door (id, name) VALUES (11, 'd11')");
        db.execSQL("INSERT INTO tbl_door (id, name) VALUES (12, 'd12')");
        db.execSQL("INSERT INTO tbl_door (id, name) VALUES (13, 'd13')");
        db.execSQL("INSERT INTO tbl_door (id, name) VALUES (14, 'd14')");
        db.execSQL("INSERT INTO tbl_door (id, name) VALUES (15, 'd15')");
        db.execSQL("INSERT INTO tbl_door (id, name) VALUES (16, 'd16')");
        db.execSQL("INSERT INTO tbl_door (id, name) VALUES (17, 'd17')");
        db.execSQL("INSERT INTO tbl_door (id, name) VALUES (18, 'd18')");
        db.execSQL("INSERT INTO tbl_door (id, name) VALUES (19, 'd19')");
        db.execSQL("INSERT INTO tbl_door (id, name) VALUES (20, 'd20')");
        db.execSQL("INSERT INTO tbl_door (id, name) VALUES (21, 'd21')");

        db.execSQL("INSERT INTO tbl_sofa (id, name) VALUES (1, 'sofa1')");
        db.execSQL("INSERT INTO tbl_sofa (id, name) VALUES (2, 'sofa2')");
        db.execSQL("INSERT INTO tbl_sofa (id, name) VALUES (3, 'sofa3')");
        db.execSQL("INSERT INTO tbl_sofa (id, name) VALUES (4, 'sofa4')");
        db.execSQL("INSERT INTO tbl_sofa (id, name) VALUES (5, 'sofa5')");
        db.execSQL("INSERT INTO tbl_sofa (id, name) VALUES (6, 'sofa6')");
        db.execSQL("INSERT INTO tbl_sofa (id, name) VALUES (7, 'sofa7')");
        db.execSQL("INSERT INTO tbl_sofa (id, name) VALUES (8, 'sofa8')");
        db.execSQL("INSERT INTO tbl_sofa (id, name) VALUES (9, 'sofa9')");


    }

    public void populateSample(SQLiteDatabase db)  {
        db.execSQL("INSERT INTO tbl_furniture (id, name, type, price, area, brand, prod_image) VALUES ( 1, 'Wooden Cabinet', 'cabinet', '2000', 200, 'Kemper', '/drawable/cab1') ");
        db.execSQL("INSERT INTO tbl_furniture (id, name, type, price, area, brand, prod_image) VALUES ( 2, 'Circular Chair', 'chair', '500', 100, 'Homecrest', '/drawable/ch1') ");
        db.execSQL("INSERT INTO tbl_furniture (id, name, type, price, area, brand, prod_image) VALUES ( 3, 'Ordinary Chair', 'chair', '200', 100, 'Schrock', '/drawable/ch2') ");
        db.execSQL("INSERT INTO tbl_furniture (id, name, type, price, area, brand, prod_image) VALUES ( 4, 'Mocha Sofa Brown', 'sofa', '2500', 400, 'Decora', '/drawable/sofa1') ");
        db.execSQL("INSERT INTO tbl_furniture (id, name, type, price, area, brand, prod_image) VALUES ( 5, 'Mocha Brown Sofa', 'sofa', '2500', 400, 'Decora', '/drawable/sofa2') ");
        db.execSQL("INSERT INTO tbl_furniture (id, name, type, price, area, brand, prod_image) VALUES ( 6, 'Greyish Sofa', 'sofa', '2500', 400, 'Decora', '/drawable/sofa3') ");
        db.execSQL("INSERT INTO tbl_furniture (id, name, type, price, area, brand, prod_image) VALUES ( 7, 'Mini Blue', 'chair', '500', 400, 'Schrock', '/drawable/mini_blue') ");
        db.execSQL("INSERT INTO tbl_furniture (id, name, type, price, area, brand, prod_image) VALUES ( 8, 'Mini Violet', 'chair', '500', 400, 'Decora', '/drawable/mini_violet') ");
        db.execSQL("INSERT INTO tbl_furniture (id, name, type, price, area, brand, prod_image) VALUES ( 9, 'Door 1', 'door', '500', 400, 'Decora', '/drawable/ddd1') ");
        db.execSQL("INSERT INTO tbl_furniture (id, name, type, price, area, brand, prod_image) VALUES ( 10, 'Door 2', 'door', '500', 400, 'Decora', '/drawable/ddd2') ");
        db.execSQL("INSERT INTO tbl_furniture (id, name, type, price, area, brand, prod_image) VALUES ( 11, 'Door 3', 'door', '500', 400, 'Decora', '/drawable/ddd3') ");
        db.execSQL("INSERT INTO tbl_furniture (id, name, type, price, area, brand, prod_image) VALUES ( 12, 'Sofa Black', 'sofa', '2500', 1400, 'Decora', '/drawable/sofa_black') ");
    }

}
