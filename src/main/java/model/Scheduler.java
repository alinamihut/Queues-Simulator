package model;

import java.util.ArrayList;

public class Scheduler {
    private ArrayList<Server> servers = new ArrayList<>();
    private int maxNrServers;
    private int maxTasksPerServer;
    private Strategy strategy;
    int waitingTime;

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
  /*
    public int computeWaitingTime (){
        for (model.Server s: servers) {
          waitingTime = waitingTime + s.getWaitingPeriod().intValue();
        }
        return waitingTime;
    }

   */
    public int computeWaitingTimeForClient (Task t){
        int waitingTime=0;
        Server serverWithTask = servers.get(0);
        for (Server s: servers) {  // find server
            for (Task t1 : s.getTasksQ()) {
                if (t1.getID() == t.getID()) {
                    serverWithTask = s;
                }
            }
        }

        for (Task t1: serverWithTask.getTasksQ())
            if (t1.getArrivalTime() <t.getArrivalTime()){
                waitingTime=waitingTime+t1.getProcessingTime();
            }

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
