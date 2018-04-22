package seclass.grocerylistmanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListDisplay extends AppCompatActivity {

    String list_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_display);

        final User the_user;
        the_user = User._get_user_interface(getApplicationContext());

        //Getting list name from previous activity and initilizing adapter with what should be its proper values
        Bundle name_holder = this.getIntent().getExtras();
        list_name = list_name = name_holder.getString("Name of List");

        if(name_holder !=null)
            list_name = name_holder.getString("Name of List");


        final GroceryList current_list = the_user.choose_list(list_name);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.grocerylist_layout, current_list.getNames_of_all_items());

        ListView listView =  findViewById(R.id.itemListView);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Item model = current_list.getItem_list().get(i);

                current_list.check_off(model.getItemName(),getApplicationContext());

                adapter.notifyDataSetChanged();
            }
        });

        Button unselectAll = findViewById(R.id.SelectAllBTN);
        unselectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                current_list.remove_all_checks(getApplicationContext());
                adapter.notifyDataSetChanged();
            }
        });

        Button backToList = (Button) findViewById(R.id.backbutton2);
        backToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListDisplay.this, Lists.class);
                startActivity(intent);
            }
        });

        Button deleteSelected = (Button) findViewById(R.id.deleteItemsBTN);
        deleteSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                current_list.delete_checked_items(getApplicationContext());
                adapter.notifyDataSetChanged();

            }
        });

    }

    public void buttonOpenAddItem(View view)
    {
        Intent intent = new Intent(ListDisplay.this, AddItem.class);
        intent.putExtra("Name of List", list_name);
        startActivity(intent);
    }
}

