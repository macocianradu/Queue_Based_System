package GUI;

import Control.SimulationManager;
import Modelation.Server;
import Modelation.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SimulationFrame extends JFrame{
    private JPanel panel;
    private int width = 640;
    private int height = 480;
    private JButton start = new JButton("Start");
    private JButton exit = new JButton("Exit");
    private JTextField servers = new JTextField();
    private JTextField clients = new JTextField();
    private JTextField maxTime = new JTextField();
    private JTextField minTime = new JTextField();
    private JTextField timeLimit = new JTextField();
    private JTextField strategy = new JTextField();
    private GridBagConstraints c = new GridBagConstraints();
    private JLabel error = new JLabel();
    private JLabel[] waitingTime;
    private JLabel[][] tasks;
    private JLabel avgWaitingTime;
    private JLabel currentTime;

    public SimulationFrame(){
        this.panel = new JPanel(new GridBagLayout());
        setStartScreen(this.panel);
        this.add(this.panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                start();
            }
        });
    }

    private void setStartScreen(JPanel panel){
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.75;
        c.weighty = 0.75;
        c.insets = new Insets(0, 20, 0, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new Label("Nr of Servers: "), c);
        c.gridx = 2;
        c.insets = new Insets(0, 0, 0, 20);
        panel.add(new Label("Nr of Clients: "), c);
        c.gridy = 1;
        panel.add(clients, c);
        c.gridx = 0;
        c.insets = new Insets(0, 20, 0, 0);
        panel.add(servers, c);
        c.gridy = 2;
        panel.add(new Label("Min Time: "), c);
        c.gridx = 2;
        c.insets = new Insets(0, 0, 0, 20);
        panel.add(new Label("Max Time: "), c);
        c.gridy = 3;
        panel.add(maxTime, c);
        c.gridx = 0;
        c.insets = new Insets(0, 20, 0, 0);
        panel.add(minTime, c);
        c.gridy = 4;
        panel.add(new Label("Time Limit: "), c);
        c.gridx = 2;
        c.insets = new Insets(0, 0, 0, 20);
        panel.add(new Label("Strategy: "), c);
        c.gridy = 5;
        panel.add(strategy, c);
        c.gridx = 0;
        c.insets = new Insets(0, 20, 0, 0);
        panel.add(timeLimit, c);
        c.gridy = 6;
        panel.add(exit, c);
        c.gridx = 2;
        c.insets = new Insets(0, 0, 0, 20);
        panel.add(start, c);
        c.gridx = 1;
        c.insets = new Insets(0, 0, 0, 0);
        for(int i = 0; i <= 6; i ++){
            c.gridy = i;
            panel.add(new Label(), c);
        }
        c.gridy = 7;
        c.gridx = 1;
        panel.add(error, c);
    }

    private void start(){
        if(!this.valid()) {
            return;
        }
        panel.removeAll();
        panel.validate();
        panel.repaint();

        SimulationManager manager = new SimulationManager(Integer.valueOf(timeLimit.getText()), Integer.valueOf(maxTime.getText()), Integer.valueOf(minTime.getText()), Integer.valueOf(clients.getText()), Integer.valueOf(servers.getText()), strategy.getText());
        manager.generateRandomTasks(manager.nrClients);
        waitingTime = new JLabel[manager.scheduler.getServers().size()];
        tasks = new JLabel[manager.scheduler.getServers().size()][4];
        for(int i = 0; i < waitingTime.length; i++){
            waitingTime[i] = new JLabel();
            for(int j = 0; j <= 3; j++){
                tasks[i][j] = new JLabel();
            }
        }
        Thread t = new Thread(manager);
        setDisplay(manager.scheduler.getServers());
        t.start();
    }

    private boolean valid(){
        if(servers.getText().length() == 0 || clients.getText().length() == 0 || maxTime.getText().length() == 0 || minTime.getText().length() == 0 || timeLimit.getText().length() == 0 || strategy.getText().length() == 0){
                error.setForeground(Color.red);
                error.setText("All fields must be filled");
                return false;
            }
            else if(Integer.valueOf(maxTime.getText()) < Integer.valueOf(minTime.getText())) {
                error.setForeground(Color.red);
                error.setText("Max time must be greater than min time");
                return false;
            }
            else if(!(strategy.getText().equals("queue") || strategy.getText().equals("time"))){
                error.setForeground(Color.red);
                error.setText("Strategy must be 'time' or 'queue'");
                return false;
            }
        return true;
    }

    private void setDisplay(ArrayList<Server> servers){
        c.weighty = 0.5;
        c.weightx = 0.5;
        c.anchor = GridBagConstraints.PAGE_START;
        for(int i = 0; i < servers.size(); i++){
            c.gridx = i;
            c.gridy = 0;
            panel.add(new Label("Server: " + i), c);
            c.gridy = 1;
            panel.add(waitingTime[i], c);
            for(int j = 2; j <= 5; j++){
                c.gridx = i;
                c.gridy = j;
                panel.add(tasks[i][j-2], c);
            }
        }
        c.gridy = 6;
        c.gridx = 0;
        avgWaitingTime = new JLabel();
        panel.add(avgWaitingTime, c);
        c.gridx = servers.size()/2;
        currentTime = new JLabel();
        panel.add(currentTime, c);
        panel.validate();
        panel.repaint();
    }

    public void display(ArrayList<Server> servers, int avgTime, int time){
        for(int i = 0; i < waitingTime.length; i++){
            for(int j = 0; j <= 3; j++){
                tasks[i][j].setText("");
            }
        }
        c.gridx = -1;
        for(Server s : servers){
            c.gridx++;
            c.gridy = 1;
            waitingTime[c.gridx].setText("Waiting Time: " + s.getWaitingTime());
            for(Task t: s.tasks){
                if(c.gridy < 5) {
                    c.gridy++;
                    tasks[c.gridx][c.gridy - 2].setText("Task" + t.getID() + ": " + t.getProcessTime());
                }
            }
        }
        avgWaitingTime.setText("Average waiting time: " + avgTime);
        currentTime.setText("Current time: " + time);
        panel.validate();
        panel.repaint();
    }
}
