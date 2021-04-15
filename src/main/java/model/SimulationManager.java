package model;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class SimulationManager implements Runnable{

    public int timeLimit;
    public int maxProcessingTime;
    public int minProcessingTime;
    public int numberOfServers;
    public int numberOfClients;
    public int maxArrivalTime;
    public int minArrivalTime;
    public SelectionPolicy selectionPolicy;
    private Scheduler scheduler;
    private ArrayList<Task> generatedTasks= new ArrayList<>();
    public FileWriter fw;
    public int peakHour=0;
    public int maxClients=0;
    public int totalServiceTime=0;
    public float averageWaitingTime=0;
    private int processedClients=0;
    public TextArea txtLog;
    public SimulationManager(int timeLimit, int minArrivalTime, int maxArrivalTime, int maxProcessingTime, int minProcessingTime, int numberOfServers, int numberOfClients, SelectionPolicy selectionPolicy, TextArea txtLog) {
        this.timeLimit = timeLimit;
        this.maxArrivalTime=maxArrivalTime;
        this.minArrivalTime=minArrivalTime;
        this.maxProcessingTime = maxProcessingTime;
        this.minProcessingTime = minProcessingTime;
        this.numberOfServers = numberOfServers;
        this.numberOfClients = numberOfClients;
        this.selectionPolicy = selectionPolicy;
        generateNRandomTasks();
        this.txtLog=txtLog;
        scheduler = new Scheduler(numberOfServers,numberOfClients);
        scheduler.changeStrategy(selectionPolicy);
    }


    public static Comparator<Task> taskComparator = Comparator.comparingInt(Task::getArrivalTime);


    private void generateNRandomTasks(){
       for (int i=0;i<numberOfClients;i++) {
           int arrivalTime = (int) ((Math.random() * (maxArrivalTime - minArrivalTime)) + minArrivalTime);
           int processingTime = (int) ((Math.random() * (maxProcessingTime - minProcessingTime)) + minProcessingTime);
           Task newTask = new Task(i + 1, arrivalTime, processingTime);
           generatedTasks.add(newTask);
       }
           generatedTasks.sort(taskComparator);

           for (Task t:generatedTasks){
               System.out.println("task " + t.getID() + "arrival" + t.getArrivalTime() + "processing " + t.getProcessingTime() );
           }

       }

       public FileWriter createFile() throws IOException {
           File file = new File("log2.txt");
       file.createNewFile();
       FileWriter fw = new FileWriter(file);

       return fw;
       }

    public int getNbOfClientsAtTimeT() {
        int totalClients = 0;
        for (Server s: scheduler.getServers()){
            totalClients= totalClients + s.getClients().length;
        }
        return totalClients;
    }
    public void appendText(String string) {
        txtLog.appendText(string);
    }
   @Override
   public void run(){
       try {
           fw=createFile();
           fw.write("START OF SIMULATION \n");
           Platform.runLater(()->txtLog.appendText("START OF SIMULATION"));
       } catch (IOException e) {
           e.printStackTrace();
       }
       int currentTime=0;
        while (currentTime< timeLimit){
            try {
                fw.write("TIME: " + currentTime);
                System.out.println(" \n TIME: " + currentTime);
                fw.write("\n");
                int finalCurrentTime = currentTime;
                Platform.runLater(()->txtLog.appendText(" \n TIME: " + finalCurrentTime + "\n"));

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fw.write("Waiting clients: ");
                Platform.runLater(()->txtLog.appendText("Waiting clients: "));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (generatedTasks.size()!=0) {
                for (Task t : generatedTasks) {
                    if (t.getArrivalTime() >currentTime) {
                        try {
                            fw.write(t.toString());
                            Platform.runLater(()->txtLog.appendText(t.toString()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    if (t.getArrivalTime() == currentTime) {
                        scheduler.dispatchTask(t);
                        processedClients++;
                        totalServiceTime = totalServiceTime + t.getProcessingTime();
                    }
                    if (getNbOfClientsAtTimeT()>maxClients){
                        maxClients=getNbOfClientsAtTimeT();
                        peakHour = currentTime;
                    }
                   averageWaitingTime = (float) Scheduler.waitingTime/numberOfClients;
                }
            }
            try {
                fw.write("\n");
                fw.write(scheduler.toString());
                fw.write("\n");
                Platform.runLater(()->txtLog.appendText("\n" + scheduler.toString() + "\n"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            currentTime++;
            try {

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
       for (Server s: scheduler.getServers()){
           s.stop();
       }

       try {
           fw.write("------------------------------------------ \n");
           fw.write("Average service time:" + (float)totalServiceTime/ numberOfClients);
           fw.write(" \n");
           fw.write("Average waiting time:" + averageWaitingTime);
           fw.write(" \n");
           fw.write("Peak Hour: " + peakHour + " with " + maxClients + " clients in the queues");
           fw.write(" \n");
           fw.write("END OF SIMULATION");
           fw.flush();
           fw.close();
           Platform.runLater(()->txtLog.appendText("\nEND OF SIMULATION\n"));
           Platform.runLater(()->txtLog.appendText("----------------------------- \n"));
           Platform.runLater(()->txtLog.appendText("Average service time:" + (float)totalServiceTime/ numberOfClients));
           Platform.runLater(()->txtLog.appendText("\n"));
           Platform.runLater(()->txtLog.appendText("Average waiting time:" + averageWaitingTime));
           Platform.runLater(()->txtLog.appendText("\n"));
           Platform.runLater(()->txtLog.appendText("Peak Hour: " + peakHour + " with " + maxClients + " clients in the queues"));
           Platform.runLater(()->txtLog.appendText("\n"));

       } catch (IOException e) {
           e.printStackTrace();
       }

   }




}
