package com.schneewittchen.rosandroid.widgets.pose;

import android.content.Context;
import android.util.Log;

import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.TransformProvider;
import com.schneewittchen.rosandroid.ui.fragments.map.MapxusViewModel;
import com.schneewittchen.rosandroid.ui.opengl.shape.GoalShape;
import com.schneewittchen.rosandroid.ui.opengl.shape.Shape;
import com.schneewittchen.rosandroid.ui.opengl.visualisation.ROSColor;
import com.schneewittchen.rosandroid.ui.opengl.visualisation.VisualizationView;
import com.schneewittchen.rosandroid.ui.views.widgets.SubscriberLayerView;

import org.ros.internal.message.Message;
import org.ros.namespace.GraphName;
import org.ros.rosjava_geometry.FrameTransform;
import org.ros.rosjava_geometry.Transform;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import geometry_msgs.Pose;
import geometry_msgs.PoseStamped;
import geometry_msgs.PoseWithCovarianceStamped;
import nav_msgs.Odometry;
import nav_msgs.Path;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.0
 * @created on 08.03.21
 */
public class PoseView extends SubscriberLayerView {

    public static final String TAG = PoseView.class.getSimpleName();
    private Shape shape;
    public PoseWithCovarianceStamped pose;


    public PoseView(Context context) {
        super(context);
        shape = new GoalShape();
    }

    public void setWidgetEntity(BaseEntity widgetEntity) {
        super.setWidgetEntity(widgetEntity);
    }

    @Override
    public void draw(VisualizationView view, GL10 gl) {
        if (pose == null) return;

        shape.draw(view, gl);
    }

    @Override
    public void onNewMessage(Message message) {
        Log.d(TAG, "Entered the onNewMessage function");
        pose = (PoseWithCovarianceStamped)message;
        Log.d(TAG, "onNewMessage: \n");
        Log.d(TAG, "the pose is:\n");
        Log.d(TAG, String.valueOf(pose.getHeader().getSeq()));
        Log.d(TAG, "\n");
        Log.d(TAG, String.valueOf(pose.getPose().getPose().getPosition().getX()));
        GraphName source = GraphName.of(pose.getHeader().getFrameId());
        frame = source;
        FrameTransform frameTransform = TransformProvider.getInstance().getTree().transform(source, frame);

        if (frameTransform == null) return;

        Transform poseTransform = Transform.fromPoseMessage(pose.getPose().getPose());
        shape.setTransform(frameTransform.getTransform().multiply(poseTransform));
    }

    public void setPose(PoseWithCovarianceStamped message){
         pose = message;
    }
    public PoseWithCovarianceStamped getPose(){return pose;}
}