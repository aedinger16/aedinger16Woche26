package com.example.aedinger16woche26;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.List;

public class ItemActivity extends Activity {

    private List<TopicItem> selectedItems;

    private TextView textViewName;
    private TextView textViewAnswer;

    private int currentItemIndex = 0;

    private FirebaseFirestore db;
    private final String collectionName = "aedinger16Woche26_TopicItem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_activity);

        selectedItems = getIntent().getParcelableArrayListExtra("selectedTopicItems");
        Collections.shuffle(selectedItems);

        db = FirebaseFirestore.getInstance();

        init();
    }

    public void fabItemCorrect(View view){

        TopicItem currentItem = selectedItems.get(currentItemIndex);
        currentItem.addOneToUseCounter();

        db.collection(collectionName)
                .document(String.valueOf(currentItem.getId()))
                .update("useCounter", currentItem.getUseCounter())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("firestoreDemo.update", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("firestoreDemo.update", "Error updating document", e);
                    }
                });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        currentItemIndex++;

        updateView();
    }

    public void fabItemFalse(View view){

        currentItemIndex++;

        updateView();

    }

    private void updateView(){
        if(currentItemIndex < selectedItems.size()){

            TopicItem nextItem = selectedItems.get(currentItemIndex);

            textViewName.setText(nextItem.getTopicItemName());
            textViewAnswer.setText("Click to show the answer");
        }
        else{
            Intent intent = new Intent(ItemActivity.this, TopicItemActivity.class);
            intent.putExtra("selectedTopic", selectedItems.get(currentItemIndex-1).getId().split("_")[0]);
            //based on item add info to intent
            startActivity(intent);
        }
    }

    private void init(){
        textViewName = findViewById(R.id.textViewItemName);
        textViewAnswer = findViewById(R.id.textViewItemAnswer);

        textViewName.setText(selectedItems.get(currentItemIndex).getTopicItemName());
    }

    public void showAnswer(View view){

        String currentText = textViewAnswer.getText().toString();

        if(currentText.equals("Click to show the answer")){
            textViewAnswer.setText(selectedItems.get(currentItemIndex).getTopicItemAnswer());
        }
        else{
            textViewAnswer.setText("Click to show the answer");
        }

    }
}
