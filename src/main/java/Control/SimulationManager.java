package Control;

import GUI.SimulationFrame;
import Modelation.Server;
import Modelation.Task;

import java.util.ArrayList;
import java.util.Random;

public class SimulationManager implements Runnable {
    private int timeLimit;
    private int maxProcTime;
    private int minProcTime;
    public int nrClients;
    private Strategy strategy;
    private int avgWaitingTime;

    static SimulationFrame window;

    private int currentTime;

    private Random rnd = new Random();

    public Scheduler scheduler;

    private ArrayList<Task> tasks = new ArrayList<Task>();
    private ArrayList<Task> toRemove = new ArrayList<Task>();

    public SimulationManager(int timeLimit, int maxProcTime, int minProcTime, int nrClients, int nrServers, String strategy){
        this.timeLimit = timeLimit;
        this.maxProcTime = maxProcTime;
        this.minProcTime = minProcTime;
        this.nrClients = nrClients;
        this.strategy = Strategy.valueOf(strategy);
        this.scheduler = new Scheduler(nrServers);
        this.currentTime = 0;
    }

    public void generateRandomTasks(int n){
        for(int i = 0; i < n; i++){
            this.tasks.add(new Task(rnd.nextInt(this.timeLimit), rnd.nextInt(this.maxProcTime - this.minProcTime) + this.minProcTime, i));
        }
    }

    public void run() {
        while(this.currentTime < this.timeLimit){
            for(Task t : this.tasks){
                if(t.getArrivalTime() == currentTime){
                    scheduler.addTask(t, this.strategy, tasks.indexOf(t));
                    this.toRemove.add(t);
                }
            }
            for(Task t : toRemove){
                tasks.remove(t);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            avgWaitingTime = 0;
            for(Server s: scheduler.getServers()){
                avgWaitingTime += s.getWaitingTime();
            }
            window.display(scheduler.getServers(), avgWaitingTime/scheduler.getServers().size(), currentTime);
            currentTime++;
            if(currentTime == this.timeLimit){
                int max = 0;
                for(Server s: scheduler.getServers()){
                    if(s.getWaitingTime() > max) {
                        max = s.getWaitingTime();
                        avgWaitingTime += s.getWaitingTime();
                    }
                }
                timeLimit += max;
            }
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        window = new SimulationFrame();
    }
}
