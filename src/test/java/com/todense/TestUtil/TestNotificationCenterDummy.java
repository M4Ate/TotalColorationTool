package com.todense.TestUtil;

import com.todense.model.graph.Graph;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import de.saxsys.mvvmfx.utils.notifications.NotificationObserver;

import java.util.ArrayList;
import java.util.List;

public class TestNotificationCenterDummy implements NotificationCenter {

    Graph graphToPublish;

    List<String> messagesToPublish = new ArrayList<>();

    @Override
    public void subscribe(String s, NotificationObserver notificationObserver) {

    }

    @Override
    public void unsubscribe(String s, NotificationObserver notificationObserver) {

    }

    @Override
    public void unsubscribe(NotificationObserver notificationObserver) {

    }

    @Override
    public void publish(String s, Object... objects) {
        this.messagesToPublish.add(s);

        for (Object object : objects) {
            if (object instanceof String) {
                this.messagesToPublish.add((String) object);
            } else if( object instanceof Graph) {
                this.graphToPublish = (Graph) object;
            }
        }
    }

    @Override
    public void publish(Object o, String s, Object[] objects) {
        this.graphToPublish = (Graph) objects[0];
        this.messagesToPublish.add(s);
    }

    @Override
    public void subscribe(Object o, String s, NotificationObserver notificationObserver) {

    }

    @Override
    public void unsubscribe(Object o, String s, NotificationObserver notificationObserver) {

    }

    @Override
    public void unsubscribe(Object o, NotificationObserver notificationObserver) {

    }

    public Graph getGraphToPublish() {
        return this.graphToPublish;
    }

    public void getGraphToPublish(Graph graphToPublish) {
        this.graphToPublish = graphToPublish;
    }

    public List<String> getMessagesToPublish() {
        return messagesToPublish;
    }
}
