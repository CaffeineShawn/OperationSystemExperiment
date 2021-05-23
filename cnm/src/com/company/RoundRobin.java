package com.company;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class RoundRobin {

    static int currentTime = 0;
    static int timeSlice = 0;


    public static void main(String[] args) {
        PCB[] pcbArray;
        Scanner sc = new Scanner(System.in);

        System.out.println("Manual or Auto(read from txt)?");
        String modeSelection = sc.next();

        if (modeSelection == "Manual") {
            System.out.print("输入进程数:");
            int sizeofPCB = sc.nextInt();


            System.out.print("输入时间片长:");
            timeSlice = sc.nextInt();

            pcbArray = new PCB[sizeofPCB];
            for (int i = 0; i < sizeofPCB; i++) {
                System.out.print("输入当前进程名、需求执行时间、到达时间:");
                pcbArray[i] = new PCB(sc.next(), sc.nextInt(), sc.nextInt());
            }

        } else {
            pcbArray = PCBReader.readTXT("src/com/company/testExample.txt");
        }
        Arrays.sort(pcbArray);

        PCB.processEntry(pcbArray,timeSlice);


    }



}


class PCB implements Comparable<PCB> {
    public String name;
    public int burstTime;
    public int requiredTime;
    public int arriveTime;
    public int finishedTime = -1;

    @Override
    public int compareTo(PCB other) {
        return this.arriveTime - other.arriveTime;
    }



    PCB(String name, int burstTime, int arriveTime) {
        this.name = name;
        this.burstTime = burstTime;
        this.requiredTime = burstTime;
        this.arriveTime = arriveTime;
    }


    static boolean processExecute(Queue<PCB> readyQueue, Queue<PCB> waitQueue, int TimeQuantum) {
        boolean newSlice = false;
        PCB currentPCB;
        int localTimeQuantum;


        while (true){
            if (readyQueue.isEmpty() && waitQueue.isEmpty()) {
                return true;
            }
            boolean newArrival = checkIfArrived(readyQueue, waitQueue);
            if ( !newArrival && readyQueue.isEmpty()) {
                RoundRobin.currentTime++;    // 处理机idle}
            } else if (newArrival) {
                newSlice = true;
            }



            while (newSlice) {
                localTimeQuantum = TimeQuantum;
                if (!readyQueue.isEmpty()) { // 就绪队列有进程
                    currentPCB = readyQueue.poll();
                    int difference = currentPCB.burstTime - localTimeQuantum;
                    if (difference <= 0) {
                        while (currentPCB.burstTime > 0) {
                            localTimeQuantum--;
                            currentPCB.burstTime--;
                            if (currentPCB.burstTime == 0) {
                                currentPCB.finishedTime = RoundRobin.currentTime +1;
                            }
                            log(currentPCB, localTimeQuantum);

                            RoundRobin.currentTime++;
                            if (!waitQueue.isEmpty()) {
                                checkIfArrived(readyQueue, waitQueue);
                            }
                        }

                    } else {
                        while (localTimeQuantum > 0) {
                            localTimeQuantum--;
                            currentPCB.burstTime--;
                            log(currentPCB, localTimeQuantum);

                            RoundRobin.currentTime++;
                            if (!waitQueue.isEmpty()) {
                                checkIfArrived(readyQueue, waitQueue);
                            }
                        }
                        readyQueue.offer(currentPCB);

                    }
                    newSlice = true;
                }

                else { // 就绪队列没有进程
                    RoundRobin.currentTime++;
                    newSlice = false;// 回到上一级查询等待队列
                }

            }
        }
    }

    static boolean checkIfArrived(Queue<PCB> readyQueue, Queue<PCB> waitQueue) {
        boolean newArrival = false;
        if (!waitQueue.isEmpty()) {

            while (waitQueue.peek().arriveTime <= RoundRobin.currentTime){
                readyQueue.offer(waitQueue.poll());
                newArrival = true;
                if (waitQueue.isEmpty()) {
                    break;
                }
            }
        }
        return newArrival;
    }

    static void log(PCB currentPCB, int TimeQuantum) {
        System.out.println("当前时间:"+ RoundRobin.currentTime + ",当前进程:" + currentPCB.name + ",当前进程burstTime:" + currentPCB.burstTime + ",当前TimeQuantum:" + TimeQuantum );
    }


    static void processEntry(PCB[] pcbArray, int TimeQuantum) {
        Queue<PCB> readyQueue = new LinkedList<>();
        Queue<PCB> waitQueue = new LinkedList<>();
        float sum = 0;

        for (PCB pcb : pcbArray) {
            waitQueue.offer(pcb);
        }

        if (processExecute(readyQueue, waitQueue, TimeQuantum)) {
            System.out.println("Done.");
            for (PCB pcb : pcbArray) {
                System.out.println("当前进程:" + pcb.name + ",当前进程finishedTime:" + pcb.finishedTime);
                sum += (float) pcb.finishedTime/ (float) pcb.requiredTime/ pcbArray.length ;
            }
            System.out.println("平均带权周转时间为:"+ sum);
        }
    }


}






