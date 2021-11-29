package com.schneewittchen.rosandroid.ui.fragments.map;


import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;

import geometry_msgs.PoseWithCovarianceStamped;

/**
 * A simple {@link Subscriber} {@link NodeMain}.
 *
 * @author damonkohler@google.com (Damon Kohler)
 */
public class PoseSubscriber extends AbstractNodeMain {

    private double darr[];

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("PoseSubscriber");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        Subscriber<geometry_msgs.PoseWithCovarianceStamped> subscriber = connectedNode.newSubscriber("PoseSubscriber", PoseWithCovarianceStamped._TYPE);
        subscriber.addMessageListener(message -> {
            darr[0] = message.getPose().getPose().getPosition().getX();
            darr[1] = message.getPose().getPose().getPosition().getY();
        });
    }

    public double[] getDarr(){
        return darr;
    }
}