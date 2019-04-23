package com.example.aedinger16woche26;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class TopicItem implements Parcelable, Serializable {

    private String id;
    private String topicItemName;
    private String topicItemAnswer;
    private int useCounter;
    private boolean selected;

    public TopicItem(String id, String topicItemName, String topicItemAnswer, int useCounter) {
        this.id = id;
        this.topicItemName = topicItemName;
        this.topicItemAnswer = topicItemAnswer;
        this.useCounter = useCounter;
    }

    public TopicItem(Parcel in){
        id = in.readString();
        topicItemName = in.readString();
        topicItemAnswer = in.readString();
        useCounter = in.readInt();
    }

    public void addOneToUseCounter(){
        useCounter++;
    }

    public boolean getSelected(){
        return selected;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopicItemName() {
        return topicItemName;
    }

    public void setTopicItemName(String topicItemName) {
        this.topicItemName = topicItemName;
    }

    public int getUseCounter() {
        return useCounter;
    }

    public void setUseCounter(int useCounter) {
        this.useCounter = useCounter;
    }

    public String getTopicItemAnswer() {
        return topicItemAnswer;
    }

    public void setTopicItemAnswer(String topicItemAnswer) {
        this.topicItemAnswer = topicItemAnswer;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(topicItemName);
        parcel.writeString(topicItemAnswer);
        parcel.writeInt(useCounter);
    }

    public static final Parcelable.Creator<TopicItem> CREATOR = new Parcelable.Creator<TopicItem>(){

        @Override
        public TopicItem createFromParcel(Parcel parcel) {
            return new TopicItem(parcel);
        }

        @Override
        public TopicItem[] newArray(int size) {
            return new TopicItem[size];
        }
    };

}
