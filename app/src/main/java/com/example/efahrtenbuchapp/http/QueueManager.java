package com.example.efahrtenbuchapp.http;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;

/**
 * Managed die RequestQueues mit den jeweils zugehörigen Contexten.
 * Verwendet das Singleton-Design-Pattern damit pro Context immer nur maximal 1 request Que benutzt wird.
 */
public class QueueManager {

    private final HashMap<Context, RequestQueue> queueMap;
    private static QueueManager instance;

    private QueueManager(){
        queueMap = new HashMap<>();
    }

    public static synchronized final QueueManager getInstance(){
        if(instance == null){
            instance = new QueueManager();
        }
        return instance;
    }

    public RequestQueue getOrCreateQueueForContext(Context context){
        if(!queueMap.containsKey(context)){
            queueMap.put(context, Volley.newRequestQueue(context));
        }
        return queueMap.get(context);
    }
}
    /*public void removeQueue(Context context){
        queueMap.remove(context);
    }*/

    /*public RequestQueue getQueue(Context context){
        if(!queueMap.containsKey(context)) return null;
        return queueMap.get(context);
    }*/

