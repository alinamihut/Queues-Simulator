package model;

import java.util.ArrayList;

public class Scheduler {

    private ArrayList<Server> servers = new ArrayList<>();
    private int maxNrServers;
    private int maxTasksPerServer;
    private Strategy strategy;
    public static int waitingTime;

    public Scheduler (int maxNrServers, int maxTasksPerServer){
        for (int i=0;i<maxNrServers;i++){
            Server server = new Server(i+1);
            Thread threadQ = new Thread (server);
            servers.add(server);
            threadQ.start();

        }
    }

    @Override
    public String toString() {
        String str = "";
        for (Server s: servers){
            str = str +"\n Queue" + s.getQIndex() + ": ";
            if (s.getTasksQ().size()==0){
               str = str + "empty";
            }
            else{
                for (Task t: s.getTasksQ()) {
                    str = str + t.toString();
                }
            }
        }

        return str;
    }
    public static void computeWaitingTimeForServerS(Server s){
        waitingTime=waitingTime+ s.getWaitingPeriod().intValue();
    }
    public int getWaitingTime(){
        return waitingTime;
    }

    public void changeStrategy (SelectionPolicy policy){
        if (policy == SelectionPolicy.SHORTEST_QUEUE){
            strategy = new ConcreteStrategyQueue();
        }
        if (policy == SelectionPolicy.SHORTEST_TIME){
            strategy = new ConcreteStrategyTime();
        }
    }

    public void dispatchTask (Task t){
            strategy.addTask(servers,t);
    }

    public ArrayList<Server> getServers(){
        return servers;
    }

}
