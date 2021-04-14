package controller;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.SelectionPolicy;
import model.SimulationManager;
import view.UserInterface;

public class Controller {
    public TextField tfNumberClients;
    public TextField tfNumberQueues;
    public TextField tfArrTimeMax;
    public TextField tfArrTimeMin;
    public TextField tfSimulationInterval;
    public TextField tfServiceTimeMin;
    public TextField tfServiceTimeMax;
    public TextField tfTime;
    public Button buttonStart;
    public Button buttonStop;
    public TextField tfStrategy;

    private UserInterface userInterface;
    private int nrOfClients;
    private int nrOfQueues;
    private int simulationDuration;
    private int minArrivalTime;
    private int maxArrivalTime;
    private int minServiceTime;
    private int maxServiceTime;
    private String strategy;
    private SelectionPolicy selectionPolicy;
    public int getNrOfClients() {
        return nrOfClients;
    }

    public void setNrOfClients(int nrOfClients) {
        this.nrOfClients = nrOfClients;
    }

    public int getNrOfQueues() {
        return nrOfQueues;
    }

    public void setNrOfQueues(int nrOfQueues) {
        this.nrOfQueues = nrOfQueues;
    }

    public int getSimulationDuration() {
        return simulationDuration;
    }

    public void setSimulationDuration(int simulationDuration) {
        this.simulationDuration = simulationDuration;
    }

    public int getMinArrivalTime() {
        return minArrivalTime;
    }

    public void setMinArrivalTime(int minArrivalTime) {
        this.minArrivalTime = minArrivalTime;
    }

    public int getMaxArrivalTime() {
        return maxArrivalTime;
    }

    public void setMaxArrivalTime(int maxArrivalTime) {
        this.maxArrivalTime = maxArrivalTime;
    }

    public int getMinServiceTime() {
        return minServiceTime;
    }

    public void setMinServiceTime(int minServiceTime) {
        this.minServiceTime = minServiceTime;
    }

    public int getMaxServiceTime() {
        return maxServiceTime;
    }

    public void setMaxServiceTime(int maxServiceTime) {
        this.maxServiceTime = maxServiceTime;
    }

    public TextField getTfTime() {
        return tfTime;
    }

    public void getInputData() {
        try {
            nrOfClients = Integer.parseInt(tfNumberClients.getText());
            nrOfQueues = Integer.parseInt(tfNumberQueues.getText());
            simulationDuration = Integer.parseInt(tfSimulationInterval.getText());
            minArrivalTime = Integer.parseInt(tfArrTimeMin.getText());
            maxArrivalTime = Integer.parseInt(tfArrTimeMax.getText());
            minServiceTime = Integer.parseInt(tfServiceTimeMin.getText());
            maxServiceTime = Integer.parseInt(tfServiceTimeMax.getText());
            strategy=tfStrategy.getText();
            if (nrOfClients <= 0 || nrOfQueues <= 0 || simulationDuration <= 0 || minArrivalTime < 0 ||
                    maxArrivalTime <= 0 || maxArrivalTime < minArrivalTime || minServiceTime < 0 || maxServiceTime <= 0
                    || maxServiceTime < minServiceTime  ) {
                UserInterface.showAlertForInvalidData();
            } else {
                System.out.println("clients " + nrOfClients + "queues " + nrOfQueues);
                System.out.println("t arrival min" + minArrivalTime + "max" + maxArrivalTime);
                System.out.println("t service min" + minServiceTime + "max" + maxServiceTime);
                System.out.println("duration " + simulationDuration);

                setNrOfClients(nrOfClients);
                setNrOfQueues(nrOfQueues);
                setSimulationDuration(simulationDuration);
                setMinArrivalTime(minArrivalTime);
                setMaxArrivalTime(maxArrivalTime);
                setMinServiceTime(minServiceTime);
                setMaxServiceTime(maxServiceTime);
            }
        } catch (Exception e) {
            UserInterface.showAlertForInvalidData();
        }
        if (!strategy.equals("SHORTEST_QUEUE") && !strategy.equals("SHORTEST_TIME")){
            UserInterface.showAlertForInvalidData();
        }
        else{
            if (strategy.equals("SHORTEST_TIME")){
                selectionPolicy = SelectionPolicy.SHORTEST_TIME;
            }
            else {
                selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;
            }
        }


    }

    public void initialiseSimulation (){
        getInputData();
        SimulationManager simulationManager = new SimulationManager(simulationDuration,minArrivalTime,maxArrivalTime,maxServiceTime,minServiceTime,nrOfQueues,nrOfClients,selectionPolicy);
        Thread t = new Thread(simulationManager);
        t.start();

    }





}
