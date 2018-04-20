package Control;

import Modelation.Server;
import Modelation.Task;

import java.util.ArrayList;

public class Scheduler {
    private ArrayList<Server> servers;

    public ArrayList<Server> getServers(){
        return this.servers;
    }

    public Scheduler(int maxNrServers){
        servers = new ArrayList<Server>();
        for(int i = 0; i < maxNrServers; i++){
            servers.add(new Server());
            new Thread(servers.get(i)).start();
        }
    }

    public void addTask(Task t, Strategy strategy, int i){
        int min = Integer.MAX_VALUE;
        int index = 0;
        if(strategy == Strategy.queue){
            for(Server s: servers){
                if(s.getNrTasks() < min) {
                    min = s.getNrTasks();
                    index = servers.indexOf(s);
                }
            }
        }
        else {
            for (Server s : servers) {
                if (s.getWaitingTime() < min) {
                    min = s.getWaitingTime();
                    index = servers.indexOf(s);
                }
            }
        }
        System.out.println("Added task " + i + " with processing time: " + t.getProcessTime() + " to server: " + index);
        servers.get(index).addTask(t);
    }
}
