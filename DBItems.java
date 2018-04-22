package seclass.grocerylistmanager;

/**
 * Created by A.Miller on 11/14/2017.
 */

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.widget.Toast;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class DBItems implements Serializable{

    //HashMap to hold Item Database, <Name,Type>
    private HashMap<String,String> item_database;

    //Static Reference to database for Singleton Patter
    private static DBItems database_instance;

    //Database Name
    private static final String database_name = "Grocery_List_Database.txt";

    private DBItems(Context context){

            load_database(context);

            if(database_instance == null){
                 item_database = new HashMap<String,String>();
                 initialize_database();
            }
        }

    public static DBItems get_instance(Context context){

        if(database_instance == null)
            return database_instance = new DBItems(context);
        else
            return database_instance;
    }

    public void add_item(String item_name,String item_type,Context current_context){
        item_database.put(item_name,item_type);
        save_database(current_context);
        Toast.makeText(current_context.getApplicationContext(),"Item successfully added to database.", Toast.LENGTH_SHORT).show();
    }

    public boolean search_by_type(String type){

        for(String i: item_database.values()){
            if(type.equals(i)){
                return true;
            }
        }
        return false;
    }
    public boolean search_by_name(String name){

        for(String i: item_database.keySet()){
            if(name.equals(i)){
                return true;
                }
            }
            return false;
    }

    public List<String> getAll_names(){

        ArrayList<String> all_names = new ArrayList<>();

        for(String i: item_database.keySet()){
            all_names.add(i);
            }


        return all_names;
    }

    public List<String> getAll_types(){

        ArrayList<String> all_types = new ArrayList<>();

        for(String i: item_database.values()){
            all_types.add(i);
        }

        Set<String> duplicate_remover = new HashSet<>();

        duplicate_remover.addAll(all_types);

        all_types.clear();

        all_types.addAll(duplicate_remover);

        return all_types;
    }

    public void save_database(Context context){

        try {
            FileOutputStream file_out = context.getApplicationContext().openFileOutput(database_name, MODE_PRIVATE);

            ObjectOutputStream out = new ObjectOutputStream(file_out);

            out.writeObject(this);
            out.close();

            file_out.close();

        } catch (IOException e) {
            Toast.makeText(context.getApplicationContext(),"Save failed,IO issue.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    public void load_database(Context context){

        try {
            FileInputStream file_in = context.getApplicationContext().openFileInput(database_name);
            ObjectInputStream in = new ObjectInputStream(file_in);

            database_instance = (DBItems) in.readObject();
            item_database = database_instance.getItem_database();

            in.close();
            file_in.close();

        }catch (FileNotFoundException f){
            Toast.makeText(context.getApplicationContext(), "Default database in use.", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {
            Toast.makeText(context.getApplicationContext(), "Database load failed, default database in use.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;

        } catch (ClassNotFoundException c) {
            Toast.makeText(context.getApplicationContext(),"Database load failed, class not found.", Toast.LENGTH_SHORT).show();
            c.printStackTrace();
            return;
        }
    }

    public void initialize_database(){

        String b = "beverage";
        String ba = "baked Goods";
        String d = "dairy";
        String v = "vegetable";
        String f = "fruit";
        String m = "meat";
        String c = "cannned goods";
        String con = "condiments";
        String s = "snacks";

        item_database.put("coffee",b);
        item_database.put("tea",b);
        item_database.put("water",b);
        item_database.put("grape juice",b);
        item_database.put("soda", b);

        item_database.put("bagels",ba);
        item_database.put("cake", ba);
        item_database.put("garlic bread",ba);
        item_database.put("english muffin", ba);

        item_database.put("butter",d);
        item_database.put("cheese",d);
        item_database.put("milk",d);
        item_database.put("eggs",d);
        item_database.put("yogurt", d);

        item_database.put("asparagus", v);
        item_database.put("broccoli",v);
        item_database.put("avocado",v);
        item_database.put("cabbage",v);
        item_database.put("carrots", v);

        item_database.put("apples", f);
        item_database.put("mango", f);
        item_database.put("orange", f);
        item_database.put("peach",f);

        item_database.put("chicken", m);
        item_database.put("steak" , m);
        item_database.put("pork" , m);
        item_database.put("shrimp", m);
    }

    public HashMap<String,String> getItem_database(){
        return this.item_database;
    }


}