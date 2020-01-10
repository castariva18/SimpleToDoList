package org.ganymede.simpletotoList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dmax.dialog.SpotsDialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.ganymede.simpletotoList.Model.ToDo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainTodoActivity extends AppCompatActivity {
    List<ToDo> toDoList = new ArrayList<>();
    FirebaseFirestore db;

    RecyclerView listItem;
    RecyclerView.LayoutManager layoutManager;

    FloatingActionButton fab;

    public MaterialEditText title,description,timeline, status; //public-> access to list adapter
    public boolean isUpdate = false; //flag to cek isUpdate or add new
    public String idUpdate = ""; //id of item need update

    ListItemAdapter adapter;
    SpotsDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_todo);

        db = FirebaseFirestore.getInstance();
        dialog = new SpotsDialog(this);
        title  = (MaterialEditText)findViewById(R.id.title);
        description = (MaterialEditText)findViewById(R.id.description);
        timeline = (MaterialEditText)findViewById(R.id.timeline);
        status = (MaterialEditText)findViewById(R.id.status);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add new
                if (TextUtils.isEmpty(title.getText()) || TextUtils.isEmpty(description.getText()) || TextUtils.isEmpty(timeline.getText())|| TextUtils.isEmpty(status.getText())){
                    Toast.makeText(MainTodoActivity.this,"Please insert data", Toast.LENGTH_SHORT).show();
                }
                else if (!isUpdate){
                    setData(title.getText().toString(),description.getText().toString(),timeline.getText().toString(),status.getText().toString());
                }
                else{
                    updateData(title.getText().toString(),description.getText().toString(), timeline.getText().toString(), status.getText().toString());
                    isUpdate = !isUpdate; // reset flag
                }
            }
        });
        listItem = (RecyclerView)findViewById(R.id.listTodo);
        listItem.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listItem.setLayoutManager(layoutManager);

        loadData();//load data from firestore
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals("DELETE"))
            deleteItem(item.getOrder());
        return super.onContextItemSelected(item);
    }

    private void deleteItem(int index) {
        db.collection("ToDoList")
                .document(String.valueOf(toDoList.get(index).getId()))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadData();
                    }
                });
    }
    private void updateData(String title, String status, String description, String timeline) {
        db.collection("ToDoList").document(idUpdate)
                .update("title",title,"description",description,"timeline",timeline,"status",status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainTodoActivity.this,"Updated !", Toast.LENGTH_SHORT).show();
                    }
                });
        db.collection("ToDoList").document(idUpdate)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                        loadData();
                    }
                });
    }

    private void setData(String title,String description, String timeline, String status) {

        String id = UUID.randomUUID().toString();
        Map<String,Object> todo = new HashMap<>();
        todo.put("id",id);
        todo.put("title", title);
        todo.put("description",description);
        todo.put("timeline",timeline);
        todo.put("status",status);

        db.collection("ToDoList").document(id)
                .set(todo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //refresh data
                loadData();
            }
        });

    }

    private void loadData() {
        dialog.show();
        if (toDoList.size() > 0)
            toDoList.clear(); //remove old value
        db.collection("ToDoList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc:task.getResult()){
                            ToDo todo = new ToDo(doc.getString("id"),
                                    doc.getString("title"),
                                    doc.getString("description"),
                                    doc.getString("timeline"),
                                    doc.getString("status"));
                            toDoList.add(todo);
                        }
                        adapter = new ListItemAdapter(MainTodoActivity.this,toDoList);
                        listItem.setAdapter(adapter);
                        dialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainTodoActivity.this,"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}