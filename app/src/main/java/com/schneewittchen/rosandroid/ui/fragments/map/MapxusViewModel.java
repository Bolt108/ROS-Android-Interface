package com.schneewittchen.rosandroid.ui.fragments.map;

import android.app.Application;
import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.schneewittchen.rosandroid.domain.RosDomain;
//import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.RosData;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.SubNode;
import com.schneewittchen.rosandroid.widgets.pose.PoseEntity;
//import com.schneewittchen.rosandroid.model.repositories.rosRepo.message.Topic;
//import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;
//import com.schneewittchen.rosandroid.viewmodel.VizViewModel;

import org.ros.internal.message.Message;
//import com.schneewittchen.rosandroid.widgets.pose.PoseView;
//import java.util.List;

import java.util.List;

import geometry_msgs.PoseWithCovarianceStamped;

public class MapxusViewModel extends AndroidViewModel implements SubNode.NodeListener {

    private static final String TAG = MapxusViewModel.class.getSimpleName();
    private final RosDomain rosDomain;
    private double[] darr = new double[2];
    private SubNode subNode;

    public MapxusViewModel(@NonNull Application application) {
        super(application);

        rosDomain = RosDomain.getInstance(application);

        subNode = new SubNode(this);
        subNode.setWidget(new PoseEntity());
        Topic topic = new Topic("slovlp_ekf_info", "geometry_msgs.PoseWithCovarianceStamped");
        subNode.setTopic(topic);

    }

    public LiveData<RosData> getData() {
        return this.rosDomain.getData();
    }


    public void onNewData(RosData data) {
        Log.d("mapfragment", "get ros data: " + data);
        Message message = data.getMessage();
        Log.d(TAG, String.valueOf(message));
        onNewMessage(data);
        //PoseWithCovarianceStamped pose = (PoseWithCovarianceStamped) message;
        //Log.d("Pose X Coordinate: ", String.valueOf(pose.getPose().getPose().getPosition().getX()));
        // do sth according to the topic and messages
        // switch () {...}
    }

    @Override
    public void onNewMessage(RosData data) {
        Log.d(TAG, "data topic: " + data.getTopic().name);
        darr = newMessage(data.getMessage());

    }

    public double[] newMessage(Message message){
        PoseWithCovarianceStamped pose = (PoseWithCovarianceStamped) message;
        darr[0] = pose.getPose().getPose().getPosition().getX();
        darr[1] = pose.getPose().getPose().getPosition().getY();
        return darr;
    }

    public double[] getLocArray(){
        return darr;
    }

    public void publishData(BaseData data) {
        rosDomain.publishData(data);
    }

    public void updateWidget(BaseEntity widget) {
        rosDomain.updateWidget(null, widget);
    }

    public LiveData<List<BaseEntity>> getCurrentWidgets() {
        return rosDomain.getCurrentWidgets();
    }
}
