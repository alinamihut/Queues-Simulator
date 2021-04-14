package model;

import model.Server;
import model.Strategy;
import model.Task;

import java.util.ArrayList;

public class ConcreteStrategyTime implements Strategy {
    @Override
    public void addTask(ArrayList<Server> servers, Task t) {
        Server shortestTimeServer= servers.get(0);
        for (Server s: servers){
            if (s.getWaitingPeriod().intValue() < shortestTimeServer.getWaitingPeriod().intValue())
                shortestTimeServer=s;
        }
        shortestTimeServer.addTask(t);
        Scheduler.computeWaitingTimeForServerS(shortestTimeServer);
        System.out.println("Client" + t.getID() + "sent to queue" + shortestTimeServer.getQIndex());
    }



}
