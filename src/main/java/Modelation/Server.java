package Modelation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    public List<Task> tasks;
    public AtomicInteger waitingTime;

    public Server(){
        waitingTime = new AtomicInteger(0);
        this.tasks = Collections.synchronizedList(new ArrayList<Task>());
    }

    public void addTask(Task t){
        this.tasks.add(t);
        this.waitingTime.addAndGet(t.getProcessTime());
    }

    public int getNrTasks(){
        return this.tasks.size();
    }

    public int getWaitingTime(){
        return this.waitingTime.intValue();
    }

    public void run() {
        while(true){
            Thread t = null;
            if(this.tasks.size() > 0) {
                Task task = this.tasks.get(0);
                t = new Thread(task);
                t.start();
                AtomicInteger proctime = new AtomicInteger(task.getProcessTime());
                while(task.getProcessTime() > 0){
                    if(proctime.intValue() != task.getProcessTime()){
                        waitingTime.decrementAndGet();
                        proctime.set(task.getProcessTime());
                    }
                }
                this.tasks.remove(task);
                if(this.tasks.size() == 0){
                    waitingTime.set(0);
                }
                System.out.println("Removed task: " + task.getID());
            }
        }
    }
}
