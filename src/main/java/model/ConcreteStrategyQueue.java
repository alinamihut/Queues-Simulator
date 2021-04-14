package model;

import model.Server;
import model.Strategy;
import model.Task;

import java.util.ArrayList;

public class ConcreteStrategyQueue implements Strategy {
    @Override
    public void addTask(ArrayList<Server> servers, Task t) {
        Server shortestQueueServer= servers.get(0);
        for (Server s: servers){
            if (s.getClients().length < shortestQueueServer.getClients().length)
                shortestQueueServer=s;
        }
        shortestQueueServer.addTask(t);
        Scheduler.computeWaitingTimeForServerS(shortestQueueServer);
        System.out.println("Client" + t.getID() + "sent to queue" + shortestQueueServer.getQIndex());
    }

}
