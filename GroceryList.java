package seclass.grocerylistmanager;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GroceryList implements Serializable{

    //Private Fields
    private String list_name;
    private final List<Item> item_list = new ArrayList<Item>();;
    private final List<String> names_of_all_items = new ArrayList<String>();
    private DBItems database_ref;
    private User user;

    //Constructor

    public GroceryList(String list_name){
        setList_name(list_name);
    }

    //Core functions of class

    public boolean add_item(String item_name,String item_type,int quantity,Context current_context){

        database_ref = DBItems.get_instance(current_context);
        user = User._get_user_interface(current_context);

        if (!item_name.matches(".*\\w.*")||!item_type.matches(".*\\w.*")|| quantity == 0)
            return false;

		/*Check against database, add to database if item does not exist*/
		if(!database_ref.search_by_name(item_name)||!database_ref.search_by_type(item_type)){
            Toast.makeText(current_context.getApplicationContext(),"Item does not exists in the database, it will now be added. Please try again", Toast.LENGTH_SHORT).show();
            database_ref.add_item(item_name,item_type,current_context);
		    return false;
		}

        for(final Item i:item_list){

            if(i.getItemName().equals(item_name)){
                i.setQuantity(i.getQuantity()+quantity);

                if (i.isChecked_off())
                    names_of_all_items.set(item_list.indexOf(i),"Item Name: "+ item_name.toUpperCase() + ", Current Quantity: "+i.getQuantity()+" - Marked");
                else
                    names_of_all_items.set(item_list.indexOf(i),"Item Name: "+ item_name.toUpperCase() + ", Current Quantity: "+i.getQuantity());

                if (!database_ref.getItem_database().get(item_name).equals(item_type))
                    Toast.makeText(current_context.getApplicationContext(),"The name does not match the type.....but we know what you mean!", Toast.LENGTH_SHORT).show();

                    user.save_all_lists(current_context);

                    return true;
            }
        }


        if (!database_ref.getItem_database().get(item_name).equals(item_type)){

            Toast.makeText(current_context.getApplicationContext(),"The name does not match the type.....but we know what you mean!", Toast.LENGTH_SHORT).show();

            item_list.add(new Item(item_name,database_ref.getItem_database().get(item_name),quantity));

            names_of_all_items.add("Item Name: "+ item_name.toUpperCase() + ", Current Quantity: "+quantity);

            user.save_all_lists(current_context);

            return true;
        }

        item_list.add(new Item(item_name,database_ref.getItem_database().get(item_name),quantity));

        names_of_all_items.add("Item Name: "+ item_name.toUpperCase() + ", Current Quantity: "+quantity);

        user.save_all_lists(current_context);

        return true;
    }

    public void delete_item(String name_of_item_to_remove){

        if(name_of_item_to_remove == null){
            return;
        }

        for(final Item i:item_list){
            if(i.getItemName().equals(name_of_item_to_remove)){
                names_of_all_items.remove(item_list.indexOf(i));
                item_list.remove(i);
                return;
            }
        }
    }

    public void update_quantity(String name_of_item_quantity_to_change, int new_quantity){

        for(final Item i:item_list){
            if(i.getItemName().equals(name_of_item_quantity_to_change)){
                i.setQuantity(new_quantity);
				names_of_all_items.set(names_of_all_items.indexOf(i),"Item Name: "+ name_of_item_quantity_to_change.toUpperCase() + ", Current Quantity: "+new_quantity);
                return;
            }
        }
    }

    public void check_off(String checked_item,Context context){

        user = User._get_user_interface(context);

        for(final Item i:item_list){
            if(i.getItemName().equals(checked_item)){

                i.setChecked_off(!i.isChecked_off());

                if(i.isChecked_off())
                    names_of_all_items.set(item_list.indexOf(i),"Item Name: "+ checked_item.toUpperCase() + ", Current Quantity: "+i.getQuantity()+" - Marked");
                else
                    names_of_all_items.set(item_list.indexOf(i),"Item Name: "+ checked_item.toUpperCase() + ", Current Quantity: "+i.getQuantity());

                user.save_all_lists(context);
                return;
            }
        }
    }

    public void remove_all_checks(Context context){

        user = User._get_user_interface(context);

        for(final Item i:item_list){
            i.setChecked_off(false);
            names_of_all_items.set(item_list.indexOf(i),"Item Name: "+ i.getItemName().toUpperCase() + ", Current Quantity: "+i.getQuantity());
        }

        user.save_all_lists(context);
    }

    public void delete_checked_items(Context context){

        if (item_list.size() == 0){
            return;
        }

        user = User._get_user_interface(context);

        Iterator<Item> i = item_list.iterator();
        Iterator<String> s = names_of_all_items.iterator();

        while (i.hasNext()) {

            Item current_item = i.next();
            String current_name = s.next();

            if(current_item.isChecked_off()){
                s.remove();
                i.remove();
           }
        }

        user.save_all_lists(context);
    }

    public String getList_name() {
        return this.list_name;
    }

    public void setList_name(String new_list_name) {this.list_name = new_list_name;}

    public List<String> getNames_of_all_items(){return names_of_all_items;}

    public List<Item> getItem_list(){
        return item_list;
    }
}
