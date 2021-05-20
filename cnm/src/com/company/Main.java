package com.company;



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

        PCB.run(readyQueue,waitQueue,null);

        float res = 0;

        for (PCB i : pcbs) {
            System.out.println(i.name + "的周转时间为:" + i.turnAroundTime + ",服务时间为:" + i.requiredTime);
            res += (float) i.turnAroundTime / (float) i.requiredTime / (float) pcbs.length;
            System.out.printf(res + "\t");
        }
        System.out.println(res);
    }



}


class PCB implements Comparable<PCB> {
    public String name;
    public int requiredTimeRemaining;
    public int arriveTime;
    public int responseTime;
    public boolean moreSlice;
    public boolean arrived;
    public int turnAroundTime;
    public int requiredTime;
    static PCB tmp = null;
    static boolean newTimeSlice = false;


    @Override
    public int compareTo(PCB other) {
        return this.arriveTime - other.arriveTime;
    }

    @Override
    public String toString() {
        return ("进程名:" + this.name +  "\n进程完成时间:" + this.turnAroundTime + "\n进程到达时间:" +  this.arriveTime);

    }

    PCB(String name, int requiredTimeRemaining, int arriveTime) {
        this.name = name;
        this.requiredTimeRemaining = requiredTimeRemaining;
        this.arriveTime = arriveTime;
        this.moreSlice = true;
        this.responseTime = 0;
        this.arrived = arriveTime == 0;
        this.requiredTime = requiredTimeRemaining;

    }

    static boolean hasArriveOrNot(PCB currentPCB) {
        return Main.currentTime >= currentPCB.arriveTime;
    }


    static boolean execute(Queue<PCB> readyQueue, Queue<PCB> waitQueue, PCB currentPCB) {
        if(!waitQueue.isEmpty()){
            if (hasArriveOrNot(waitQueue.peek()) == false && readyQueue.isEmpty()) {
                Main.currentTime = waitQueue.peek().arriveTime;
                Main.offset += Main.currentTime % Main.timeSlice;
                Main.offset %= Main.timeSlice;
                newTimeSlice = true;
            }
        }


        if (newTimeSlice || Main.currentTime == 0) {//新的时间片


            if (!waitQueue.isEmpty()) {
                while (hasArriveOrNot(waitQueue.peek())) {
                    readyQueue.offer(waitQueue.poll());//补充就绪队列
                    if (waitQueue.isEmpty()) break;
                }
            }

            newTimeSlice = false;
            currentPCB = readyQueue.poll();
            if (waitQueue.isEmpty() && readyQueue.isEmpty() && currentPCB == null) {
                return false;
            }

        } else if (currentPCB == null) {
           // System.out.println("finished");
            return true;
        }
        //System.out.println(currentPCB.name);


        //execute
        currentPCB.requiredTimeRemaining--;
        if (currentPCB.requiredTimeRemaining <= 0) {
            currentPCB.moreSlice = false;//提前完成
            currentPCB.turnAroundTime = Main.currentTime +1;
            Main.offset += Main.currentTime % Main.timeSlice;
            Main.offset %= Main.timeSlice;

            newTimeSlice = true;
        } else if ((Main.currentTime + Main.offset) % Main.timeSlice == 0) {
            //正常进入新的时间片
            System.out.println(currentPCB.name + "-Added to the readyQueue");
            readyQueue.offer(currentPCB);
            newTimeSlice = true;
        }
        System.out.printf("currentTime:%d\t%s\tremaining:%d\tnewTimeSlice:%b\n", Main.currentTime,currentPCB.name,currentPCB.requiredTimeRemaining,newTimeSlice);
        for (PCB p : readyQueue) {
            p.toString();
        }
        Main.currentTime++;
        System.out.println("currentTime:" +Main.currentTime);
        if (!waitQueue.isEmpty()) {
            while (hasArriveOrNot(waitQueue.peek())) {
                System.out.println(waitQueue.peek().name +"-Added to the readyQueue");
                readyQueue.offer(waitQueue.poll());//补充就绪队列
                if (waitQueue.isEmpty()) break;
            }
        }
        //如果不切换时间片的话指向currentPCB的指针会丢失

        if (!newTimeSlice) {
            tmp = currentPCB;
        }
        return true;
    }

    static void run(Queue<PCB> readyQueue, Queue<PCB> waitQueue, PCB currentPCB) {

        while (true) {
            if (readyQueue.isEmpty() && Main.waitQueue.isEmpty() && currentPCB.requiredTimeRemaining != 0 ) {
                System.out.println("Finished:All process executed.");
                break;
            } else {
                currentPCB = tmp;
                boolean flag = execute(readyQueue, waitQueue, currentPCB);
                if (!flag) {
                    System.out.println("Finished:All process executed.");
                    return;
                }
            }
        }
    }
}






