package com.example.aedinger16woche26;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TopicActivity extends Activity {

    private ListView listViewItems;
    private FirebaseFirestore db;
    private List<Topic> listTopics = new ArrayList<>();

    private final String collectionName = "aedinger16Woche26_Topics";

    private ArrayAdapter<String> adapterTopicName;
    private List<String> topicNameForAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_activity);

        listViewItems = findViewById(R.id.listViewTopic);
        db = FirebaseFirestore.getInstance();

        getListTopicsFirestore();

        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedTopic = adapterTopicName.getItem(i);

                Intent intent = new Intent(TopicActivity.this, TopicItemActivity.class);
                intent.putExtra("selectedTopic", selectedTopic);
                //based on item add info to intent
                startActivity(intent);
            }
        });
    }

    public void fabAddTopic(View view){

        final Dialog dialogAddTopic = new Dialog(this);
        dialogAddTopic.setContentView(R.layout.dialog_add_topic);
        dialogAddTopic.setTitle("Add a new Topic");

        Button buttonAddTopic = dialogAddTopic.findViewById(R.id.buttonAddTopic);
        EditText editTextAddTopic = dialogAddTopic.findViewById(R.id.editTextAddTopic);

        buttonAddTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Topic topicToAdd = new Topic(listTopics.size()+1, editTextAddTopic.getText().toString());
                listTopics.add(topicToAdd);

                db.collection(collectionName)
                        .document(String.valueOf(topicToAdd.getId()))
                        .set(topicToAdd)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("firestoreDemo.set", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("firestoreDemo.set", "Error writing document", e);
                            }
                        });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                topicNameForAdapter.add(topicToAdd.getTopicName());
                adapterTopicName.notifyDataSetChanged();

                dialogAddTopic.dismiss();
            }
        });

        dialogAddTopic.show();
    }

    private void getListTopicsFirestore(){

        db.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot document : task.getResult()) {
                        Map<String, Object> map = document.getData();

                        Topic topic = new Topic(Integer.parseInt(map.get("id").toString()), (map.get("topicName").toString()));

                        listTopics.add(topic);
                    }
                    if (listTopics.size() > 0) {

                        topicNameForAdapter = new ArrayList<>();

                        for (Topic topic : listTopics) {
                            topicNameForAdapter.add(topic.getTopicName());
                        }

                        adapterTopicName = new ArrayAdapter<String>(TopicActivity.this,android.R.layout.simple_list_item_1, topicNameForAdapter);
                        listViewItems.setAdapter(adapterTopicName);
                    }


                } else {

                }

            }
        });
    }
}
