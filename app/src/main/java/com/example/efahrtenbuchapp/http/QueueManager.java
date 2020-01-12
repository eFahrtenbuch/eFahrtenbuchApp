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

    /**
     * Privater Konstruktor damit von außen nicht zugegriffen werden kann
     * (Singleton)
     */
    private QueueManager(){
        queueMap = new HashMap<>();
    }

    public static synchronized final QueueManager getInstance(){
        if(instance == null){
            instance = new QueueManager();
        }
        return instance;
    }

    /**
     * Liefert die bereits registrierte RequestQue für den Context zurück,
     * falls keine vorhanden ist, wird eine neue registriert.
     * @param context der Context für den die RequestQue
     * @return die RequestQue
     */
    public RequestQueue getOrCreateQueueForContext(Context context){
        if(!queueMap.containsKey(context)){
            queueMap.put(context, Volley.newRequestQueue(context));
        }
        return queueMap.get(context);
    }
}

