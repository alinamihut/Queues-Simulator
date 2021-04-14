package model;

import model.Server;
import model.Task;

import java.util.ArrayList;

public interface Strategy {
    public void addTask (ArrayList<Server> servers, Task t);
    public int getWaitingTime (Server s);
}
