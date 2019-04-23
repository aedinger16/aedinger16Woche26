package com.example.aedinger16woche26;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

public class Adapter_ListView_TopicItem extends ArrayAdapter<TopicItem> {

    private List<TopicItem> topicItems;
    private Context context;
    private int resource;

    public Adapter_ListView_TopicItem(@NonNull Context context, int resource, List<TopicItem> topicItems) {
        super(context, resource, topicItems);

        this.topicItems = topicItems;
        this.context = context;
        this.resource = resource;
    }

    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent){

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(resource, null, false);

        TextView textViewTopicItemName = view.findViewById(R.id.textViewTopicItemName);
        TextView textViewUseCount = view.findViewById(R.id.textViewUseCount);
        CheckBox checkBoxSelectTopicItem = view.findViewById(R.id.checkBoxSelectTopicItem);

        TopicItem topicItem = topicItems.get(position);

        checkBoxSelectTopicItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                topicItem.setSelected(b);
            }
        });

        textViewTopicItemName.setText(topicItem.getTopicItemName());
        textViewUseCount.setText(String.valueOf(topicItem.getUseCounter()));

        return view;
    }
}
