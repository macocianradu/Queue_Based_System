package Modelation;

public class Task implements Runnable {
    private int arrivalTime;
    private int processTime;
    private int id;

    public Task(int arrivalTime, int processTime, int id){
        this.processTime = processTime;
        this.arrivalTime = arrivalTime;
        this.id = id;
    }

    public int getID(){
        return this.id;
    }

    public void run() {
        while(this.processTime > 0) {
            this.processTime--;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getProcessTime(){
        return this.processTime;
    }

    public int getArrivalTime(){
        return this.arrivalTime;
    }
}
