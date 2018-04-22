package seclass.grocerylistmanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.List;
import java.util.ArrayList;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.Toast;

public class AddItem extends AppCompatActivity implements OnItemSelectedListener {

    String[] numbers={"0","1","2","3","4","5", "6", "7","8","9"};
    String list_name,item;
    List<String> arrName;
    List<String> arrType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        AutoCompleteTextView autocompleteType;
        AutoCompleteTextView autocompleteName;

        final User the_user = User._get_user_interface(getApplicationContext());
        final DBItems database_ref = DBItems.get_instance(getApplicationContext());

        arrName = database_ref.getAll_names();
        arrType = database_ref.getAll_types();

        Bundle name_holder = this.getIntent().getExtras();
        list_name = name_holder.getString("Name of List");
        final GroceryList current_list = the_user.choose_list(list_name);

        //***************************************************************************************
        /* Implementing the autocomplete text entry boxes for the Add Item Page */

        autocompleteType = (AutoCompleteTextView)
                findViewById(R.id.TypeEntry);

        ArrayAdapter<String> adapterType = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, arrType);

        autocompleteType.setThreshold(2);
        autocompleteType.setAdapter(adapterType);

        autocompleteName = (AutoCompleteTextView)
                findViewById(R.id.NameEntry);

        ArrayAdapter<String> adapterName = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, arrName);

        autocompleteName.setThreshold(2);
        autocompleteName.setAdapter(adapterName);


        //***************************************************************************************
        /* Quantity implementation */

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.QtyEntry);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,numbers);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        //See bottom section for implementation of spinner selection, which I THINK goes down there,
        //outside of the initial onCreate method


        //***************************************************************************************
        /* Add to list button implementation */

        Button addListItem = (Button) findViewById(R.id.AddListItemButton);
        addListItem.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                // Implement add item to the list here
                EditText type_text = findViewById(R.id.TypeEntry);
                EditText name_text = findViewById(R.id.NameEntry);

                String name = name_text.getText().toString().toLowerCase();

                String type = type_text.getText().toString().toLowerCase();

                if(!current_list.add_item(name,type,Integer.parseInt(item),getApplicationContext())){
                    return;
                }

//              onBackPressed();

                Intent intent = new Intent(AddItem.this, ListDisplay.class);
                intent.putExtra("Name of List", list_name);
                startActivity(intent);
            }
        });
    }

    // spinner implementation methods
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // On selecting a spinner item
         item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0){}



}

