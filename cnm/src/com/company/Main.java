package com.company;

import org.omg.CosNaming.BindingIterator;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {

    static int currentTime = 0;
    static int timeSlice;
    static int offset = 1;
    static Queue<PCB> waitQueue = new LinkedList<>();
    static Queue<PCB> readyQueue = new LinkedList<>();


    public static void main(String[] args) {
	// write your code here
        Scanner sc = new Scanner(System.in);

        System.out.print("输入进程数:");
        int sizeofPCB = sc.nextInt();


        System.out.print("输入时间片长:");
        timeSlice = sc.nextInt();

        PCB[] pcbs = new PCB[sizeofPCB];
        for (int i = 0; i < sizeofPCB; i++) {
            System.out.print("输入当前进程名、需求执行时间、到达时间:");
            pcbs[i] = new PCB(sc.next(), sc.nextInt(), sc.nextInt());
        }
        Arrays.sort(pcbs);
        for (PCB i : pcbs) {
//            System.out.println(i.name);
            waitQueue.add(i);
        }
        assert waitQueue.peek() != null;
        currentTime = waitQueue.peek().arriveTime;
        PCB.run(readyQueue,waitQueue,null);



    }



}


class PCB implements Comparable<PCB> {
    public String name;
    public int requiredTime;
    public int arriveTime;
    public int responseTime;
    public boolean moreSlice;
    public boolean arrived;
    static PCB tmp = null;

    @Override
    public int compareTo(PCB other) {
        return this.arriveTime - other.arriveTime;
    }

//    @Override
//    public String toString() {
//        return ("进程名:" + this.name +  "\n进程需求时间:" + this.requiredTime + "\n进程到达时间:" +  this.arriveTime);
//
//    }

    PCB(String name, int requiredTime, int arriveTime) {
        this.name = name;
        this.requiredTime = requiredTime;
        this.arriveTime = arriveTime;
        this.moreSlice = true;
        this.responseTime = 0;
        if (arriveTime != 0) {
            this.arrived = false;
        } else {
            this.arrived = true;
        }

    }

    static boolean hasArriveOrNot(PCB currentPCB) {
        if (Main.currentTime >= currentPCB.arriveTime) {
            return true;
        } else {
            return false;
        }
    }


    static boolean execute(Queue<PCB> readyQueue,Queue<PCB> waitQueue,PCB currentPCB) {
        boolean newTimeSlice = false;
        if (Main.currentTime == 0) {
            newTimeSlice = true;
        }
        if (newTimeSlice) {//新的时间片
            newTimeSlice = false;
            currentPCB = readyQueue.poll();
            if (readyQueue.isEmpty() && !waitQueue.isEmpty()){
                while (hasArriveOrNot(waitQueue.peek())) {
                    readyQueue.offer(waitQueue.poll());//补充就绪队列
                    if (waitQueue.isEmpty()) break;
                }
            }
            if (waitQueue.isEmpty() && readyQueue.isEmpty() && currentPCB == null) {
                return false;
            }

        } else if (currentPCB == null) {
            System.out.println("finished");
        }
        //System.out.println(currentPCB.name);
        currentPCB = readyQueue.poll();

        //execute
        currentPCB.requiredTime--;
        if (currentPCB.requiredTime == 0) {
            currentPCB.moreSlice = false;//提前完成
            Main.offset += Main.currentTime % Main.timeSlice;
            Main.offset %= Main.timeSlice;

            newTimeSlice = true;
        } else {

            readyQueue.offer(currentPCB);
        }
        System.out.printf("%d:%s\n",Main.currentTime,currentPCB.name);
        Main.currentTime++;
        //如果不切换时间片的话指向currentPCB的指针会丢失

        if (!newTimeSlice){
            tmp = currentPCB;
        }
        return true;
    }

    static void run(Queue<PCB> readyQueue,Queue<PCB> waitQueue,PCB currentPCB) {

        while (true) {
            if (readyQueue.isEmpty() && Main.waitQueue.isEmpty()) {
                System.out.println("Finished:All process executed.");
                break;
            } else {
                currentPCB = tmp;
                boolean flag = execute(readyQueue, waitQueue, currentPCB);
                if (!flag) return;
                }
            }
        }
    }






