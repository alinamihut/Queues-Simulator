package model;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private LinkedBlockingQueue<Task> tasksQ;
    private AtomicInteger waitingPeriod;
    private int QIndex;
    private boolean isRunning;
    public Server( int index) {
      tasksQ = new LinkedBlockingQueue<>();
      waitingPeriod = new AtomicInteger(0);
      QIndex = index;
      isRunning = true;
    }

    public LinkedBlockingQueue<Task> getTasksQ() {
        return tasksQ;
    }

    public int getQIndex() {
        return QIndex;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }
    public void stop() {
        this.isRunning = false;
    }
    @Override
    public void run() {
        while (this.isRunning) {
            try {

                Task currentTask = getTasksQ().peek();
                //System.out.println("client" + currentTask.getID() + "arival time" + currentTask.getArrivalTime() + "processing time" + currentTask.getProcessingTime());
                if (currentTask !=null) {
                    System.out.println("client" + currentTask.getID() + "processing time" + currentTask.getProcessingTime());
                   // int serviceTime = currentTask.getProcessingTime();
                    currentTask.setProcessingTime(currentTask.getProcessingTime() - 1);
                        if (currentTask.getProcessingTime()==0){
                            tasksQ.take();
                            System.out.println("client" + currentTask.getID() + " leaves the queue");
                            //waitingPeriod.addAndGet(-1);
                        }
                    waitingPeriod.addAndGet(-1);
                    Thread.sleep(1000);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    public void addTask(Task newTask) {
    tasksQ.add(newTask);
    waitingPeriod.addAndGet(newTask.getProcessingTime());
    }

    public Task[] getClients() {
        Task[] tasks = new Task[tasksQ.size()];
        tasksQ.toArray(tasks);
        return tasks;
    }


}
