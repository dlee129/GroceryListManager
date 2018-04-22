package seclass.grocerylistmanager;

/**
 * Created by A.Miller on 11/14/2017.
 */
import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class User implements Serializable {

    //Private Static Variable for Singleton Pattern

    private static User user_interface;

    //Private Lists for singleton pattern

    private ArrayList<GroceryList> current_lists;
    private final ArrayList<String> grocery_lists_names = new ArrayList<>();

    //Name of Interface File
    private final String name_of_file = "User_Interface_Data.txt";

    //Singleton Pattern Private Constructor and Instance creator

    private User(Context context){

        load_all_lists(context);

        if(getCurrent_lists() == null){
             current_lists = new ArrayList<>();
             initialize_string_list();
        }

        }


    public static synchronized User _get_user_interface(Context context){

        if(user_interface == null){
            return (user_interface = new User(context));
        }
        else {
            return user_interface;
        }
    }


    // Core Functions of the GroceryList Class


    public void new_list(String name_of_list, Context current_context){

        if (!name_of_list.matches(".*\\w.*")||name_of_list.startsWith(" ")){
            return;
        }

        if (current_lists.size() > 0 ) {

            for(final GroceryList g: current_lists){
                if(g.getList_name().equals(name_of_list)){
                    Toast.makeText(current_context.getApplicationContext(),"List already exists", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        current_lists.add(new GroceryList(name_of_list));
        grocery_lists_names.add(name_of_list);
        save_all_lists(current_context);
    }

    public void rename_list(String old_list_name,String new_list_name,Context context){

        for(final GroceryList g: current_lists){
            if(g.getList_name().equals(old_list_name)){
                g.setList_name(new_list_name);
                grocery_lists_names.set(grocery_lists_names.indexOf(old_list_name),new_list_name);
                save_all_lists(context);
                return;
            }
        }
    }

    public GroceryList choose_list(String list_name){

        for(final GroceryList g: current_lists){
            if(g.getList_name().equals(list_name))
                return current_lists.get(current_lists.indexOf(g));
        }
        return null;
    }

    public String choose_list(int index){
        return current_lists.get(index).getList_name();
    }


    public void delete_list(String list_to_be_deleted,Context context){

        for(final GroceryList g: current_lists){
            if(g.getList_name().equals(list_to_be_deleted)){
                current_lists.remove(current_lists.indexOf(g));
                grocery_lists_names.remove(list_to_be_deleted);
                save_all_lists(context);
                return;
            }
        }
    }

    public void initialize_string_list(){

        for (GroceryList g: current_lists){
            grocery_lists_names.add(g.getList_name());
        }
    }

    public void save_all_lists(Context current_context){

        try {
            FileOutputStream file_out = current_context.getApplicationContext().openFileOutput(name_of_file, MODE_PRIVATE);

            ObjectOutputStream out = new ObjectOutputStream(file_out);

            out.writeObject(this);
            out.close();

            file_out.close();

        } catch (IOException e) {
            Toast.makeText(current_context.getApplicationContext(),"Save failed.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    public void load_all_lists(Context current_context){

        try {

            FileInputStream file_in = current_context.getApplicationContext().openFileInput(name_of_file);
            ObjectInputStream in = new ObjectInputStream(file_in);

            user_interface = (User) in.readObject();
            current_lists = user_interface.getCurrent_lists();
            initialize_string_list();

            in.close();
            file_in.close();

            Toast.makeText(current_context.getApplicationContext(),"Welcome Back.", Toast.LENGTH_SHORT).show();

        }catch (FileNotFoundException f){
            Toast.makeText(current_context.getApplicationContext(), "File not found.", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {
            Toast.makeText(current_context.getApplicationContext(), "Load failed.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;

        } catch (ClassNotFoundException c) {
            Toast.makeText(current_context.getApplicationContext(),"Load failed, class not found.", Toast.LENGTH_SHORT).show();
            c.printStackTrace();
            return;
        }
    }

    public ArrayList<String> get_grocery_lists_names(){
        return grocery_lists_names;
    }

    public ArrayList<GroceryList> getCurrent_lists(){
        return current_lists;
    }
}
