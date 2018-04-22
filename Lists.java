package seclass.grocerylistmanager;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Lists extends AppCompatActivity {
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        final User the_user;
        the_user = User._get_user_interface(getApplicationContext());

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.grocerylist_layout, the_user.get_grocery_lists_names());

        ListView myGroceryListView = findViewById(R.id.myGroceryListView);

        myGroceryListView.setAdapter(adapter);

        myGroceryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {


                Toast.makeText(getApplicationContext(),"Clicked "+(i+1), Toast.LENGTH_SHORT).show();


                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Lists.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_layout,null);
                final EditText mRename = (EditText) mView.findViewById(R.id.renameEdit);
                Button mRenameList = (Button) mView.findViewById(R.id.renameListbtn);

                mBuilder.setView(mView);
                dialog = mBuilder.create();

                mRenameList.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        String new_name = mRename.getText().toString();

                        if(!new_name.isEmpty()){

                            the_user.rename_list(the_user.choose_list(i),new_name,getApplicationContext());

                            Toast.makeText(getApplicationContext(),"Rename Success ", Toast.LENGTH_SHORT).show();

                            adapter.notifyDataSetChanged();

                        dialog.cancel();
                        }
                    }
                });

                Button mDeleteList = (Button) mView.findViewById(R.id.deleteListBTN);
                mDeleteList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        the_user.delete_list(the_user.choose_list(i),getApplicationContext());

                        adapter.notifyDataSetChanged();

                        Toast.makeText(getApplicationContext(),"Deleted ", Toast.LENGTH_SHORT).show();

                        dialog.cancel();

                    }
                });

                final Button mOpenList = (Button) mView.findViewById(R.id.accessListBTN);
                mOpenList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(getApplicationContext(),"List Opened ", Toast.LENGTH_SHORT).show();

                        Intent list_display = new Intent(getApplicationContext(), ListDisplay.class);

                        list_display.putExtra("Name of List", the_user.choose_list(i));

                        startActivity(list_display);

                    }
                });

                Button mCancel = (Button) mView.findViewById(R.id.cancelbtn);
                mCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

        Button addList = findViewById(R.id.addListButton);
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText listInput = findViewById(R.id.editTextList);

                String newItem = listInput.getText().toString();

                the_user.new_list(newItem,getApplicationContext());

                adapter.notifyDataSetChanged();
            }
        });
    }
}
