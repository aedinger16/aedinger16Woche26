package com.example.aedinger16woche26;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TopicItemActivity extends Activity {

    private List<TopicItem> listTopicItems;
    private String currentTopic;

    private FirebaseFirestore db;
    private final String collectionName = "aedinger16Woche26_TopicItem";

    private Adapter_ListView_TopicItem adapterTopicItemName;
    private List<String> topicItemNameForAdapter;

    private ListView listViewTopicItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_item_activity);

        db = FirebaseFirestore.getInstance();

        listTopicItems = new ArrayList<>();
        topicItemNameForAdapter = new ArrayList<>();
        listViewTopicItem = findViewById(R.id.listViewTopicItem);
        listViewTopicItem.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        currentTopic = getIntent().getExtras().getString("selectedTopic");

        getListTopicItemFirestore();

    }

    public void fabTopicItemPlay(View view){

        List<TopicItem> selectedTopicItems = new ArrayList<>();

        for (TopicItem topicItem : listTopicItems) {
            if(topicItem.getSelected()){
                selectedTopicItems.add(topicItem);
            }
        }

        Intent intent = new Intent(TopicItemActivity.this, ItemActivity.class);
        intent.putParcelableArrayListExtra("selectedTopicItems", (ArrayList<? extends Parcelable>) selectedTopicItems);
        //based on item add info to intent
        startActivity(intent);
    }

    public void fabTopicItemAdd(View view){

        final Dialog dialogAddTopic = new Dialog(this);
        dialogAddTopic.setContentView(R.layout.dialog_add_topic_item);
        dialogAddTopic.setTitle("Add a new Topic Item");

        Window window = dialogAddTopic.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Button buttonAddTopicItem = dialogAddTopic.findViewById(R.id.buttonAddTopicItem);
        EditText editTextTopicItemName = dialogAddTopic.findViewById(R.id.editTextTopicItemName);
        EditText editTextTopicItemAnswer = dialogAddTopic.findViewById(R.id.editTextTopicItemAnswer);

        buttonAddTopicItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                topicItemNameForAdapter.add(editTextTopicItemName.getText().toString());

                String documentName = currentTopic + "_" + editTextTopicItemName.getText();

                TopicItem topicItemToAdd = new TopicItem(documentName, editTextTopicItemName.getText().toString(), editTextTopicItemAnswer.getText().toString(), 0);
                listTopicItems.add(topicItemToAdd);

                db.collection(collectionName)
                        .document(documentName)
                        .set(topicItemToAdd)
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

                Collections.sort(listTopicItems, (c1,c2) -> (c1.getTopicItemName().compareTo(c2.getTopicItemName())));

                adapterTopicItemName = new Adapter_ListView_TopicItem(TopicItemActivity.this, R.layout.listview_adapter_topicitem, listTopicItems);
                listViewTopicItem.setAdapter(adapterTopicItemName);

                dialogAddTopic.dismiss();
            }
        });

        dialogAddTopic.show();
    }

    private void getListTopicItemFirestore(){

        db.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot document : task.getResult()) {
                        Map<String, Object> map = document.getData();

                        TopicItem topicItem = new TopicItem(map.get("id").toString(), map.get("topicItemName").toString(), map.get("topicItemAnswer").toString(), Integer.parseInt(map.get("useCounter").toString()));

                        listTopicItems.add(topicItem);
                    }
                    if (listTopicItems.size() > 0) {

                        topicItemNameForAdapter = new ArrayList<>();

                        for (TopicItem topicItem : listTopicItems) {

                            if(topicItem.getId().contains(currentTopic)) {
                                topicItemNameForAdapter.add(topicItem.getTopicItemName());
                            }
                        }

                        Collections.sort(listTopicItems, (c1,c2) -> (c1.getTopicItemName().compareTo(c2.getTopicItemName())));

                        adapterTopicItemName = new Adapter_ListView_TopicItem(TopicItemActivity.this, R.layout.listview_adapter_topicitem, listTopicItems);
                        listViewTopicItem.setAdapter(adapterTopicItemName);
                    }


                } else {

                }

            }
        });
    }
}



