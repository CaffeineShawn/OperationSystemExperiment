package com.CaffeineShawn;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class RoundRobin {

    static int currentTime = 0;
    static int timeSlice = 0;


    public static void main(String[] args) throws IOException {
        PCB[] pcbArray;
        Scanner sc = new Scanner(System.in);

        System.out.print("Input 'Manual' to input manually or use txt input: ");
        String modeSelection = sc.nextLine();

        if (modeSelection.equals("Manual")) {
            File savedTestcase = new File("src/com/company/savedTestcase.txt");

            FileOutputStream outputStream = new FileOutputStream(savedTestcase, true);

            System.out.print("输入进程数:");
            int sizeofPCB = sc.nextInt();



            System.out.print("输入时间片长:");
            timeSlice = sc.nextInt();

            String basicData = sizeofPCB + "\n" + timeSlice + "\n";
            outputStream.write(basicData.getBytes(StandardCharsets.UTF_8));


            pcbArray = new PCB[sizeofPCB];
            for (int i = 0; i < sizeofPCB; i++) {
                System.out.print("输入当前进程名、需求执行时间、到达时间:");
                pcbArray[i] = new PCB(sc.next(), sc.nextInt(), sc.nextInt());
                String pcbData = pcbArray[i].name + " " + pcbArray[i].burstTime + " " + pcbArray[i].requiredTime + "\n";
                outputStream.write(pcbData.getBytes(StandardCharsets.UTF_8));
            }
            String enter = "\n";
            outputStream.write(enter.getBytes(StandardCharsets.UTF_8));
            outputStream.close();

        } else {
            pcbArray = PCBReader.readTXT("src/com/company/TestExample.txt");
        }

        // 将进程以先来后到的方式进行排序
        Arrays.sort(pcbArray);

        // 开始执行进程并分配时间片
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


        while (true) {


            if (readyQueue.isEmpty() && waitQueue.isEmpty()) {
                // 执行完毕
                return true;
            }

            // 当前时刻是否有进程到达
            boolean newArrival = checkIfArrived(readyQueue, waitQueue);

            if ( !newArrival && readyQueue.isEmpty()) {
                RoundRobin.currentTime++;
                // 处理机idle, 当前时间+1等待下一个进程到来
            } else if (newArrival) {
                // 分配新的时间片的标志
                newSlice = true;
            }



            while (newSlice) {
                // 分配当前时间片
                localTimeQuantum = TimeQuantum;
                if (!readyQueue.isEmpty()) {
                    // 就绪队列有进程, 从就绪队列队首抽取进程分配时间片开始执行
                    currentPCB = readyQueue.poll();

                    // 判断是不是在时间片内完成
                    int difference = currentPCB.burstTime - localTimeQuantum;
                    if (difference <= 0) {
                        // 时间片内完成, 循环执行
                        while (currentPCB.burstTime > 0) {
                            localTimeQuantum--;
                            currentPCB.burstTime--;
                            if (currentPCB.burstTime == 0) {
                                currentPCB.finishedTime = RoundRobin.currentTime +1;
                            }

                            // 将执行过程输出到命令行
                            log(currentPCB, localTimeQuantum);

                            // 时间增加
                            RoundRobin.currentTime++;

                            // 执行同时不忘检查有没有新进程到达
                            if (!waitQueue.isEmpty()) {
                                checkIfArrived(readyQueue, waitQueue);
                            }
                        }

                    } else {
                        while (localTimeQuantum > 0) {
                            // 在时间片内循环执行并输出
                            localTimeQuantum--;
                            currentPCB.burstTime--;

                            // 将执行过程输出到命令行
                            log(currentPCB, localTimeQuantum);

                            // 执行同时不忘检查有没有新进程到达
                            RoundRobin.currentTime++;
                            if (!waitQueue.isEmpty()) {
                                checkIfArrived(readyQueue, waitQueue);
                            }
                        }
                        // 将未执行完进程放回到就绪队列的末尾
                        readyQueue.offer(currentPCB);

                    }
                }

                else {
                    // 就绪队列没有进程
                    RoundRobin.currentTime++;
                    newSlice = false;
                    // 回到上一级查询等待队列
                }

            }
        }
    }

    static boolean checkIfArrived(Queue<PCB> readyQueue, Queue<PCB> waitQueue) {
        boolean newArrival = false;
        if (!waitQueue.isEmpty()) {
            // 有没有到的
            while (waitQueue.peek().arriveTime <= RoundRobin.currentTime){
                // 从等待队列中取出，放入到就绪队列中
                readyQueue.offer(waitQueue.poll());
                newArrival = true;
                if (waitQueue.isEmpty()) {
                    break;
                }
            }
        }
        return newArrival;
    }

    // 简单拼个字符串
    static void log(PCB currentPCB, int TimeQuantum) {
        System.out.println("当前时间:"+ RoundRobin.currentTime + ",当前进程:" + currentPCB.name + ",当前进程burstTime:" + currentPCB.burstTime + ",当前TimeQuantum:" + TimeQuantum );
    }

    // 调度的入口
    static void processEntry(PCB[] pcbArray, int TimeQuantum) {
        Queue<PCB> readyQueue = new LinkedList<>();
        Queue<PCB> waitQueue = new LinkedList<>();
        float sum = 0;

        // 初始化等待队列
        for (PCB pcb : pcbArray) {
            waitQueue.offer(pcb);
        }

        // 执行
        if (processExecute(readyQueue, waitQueue, TimeQuantum)) {
            System.out.println("Done.");
            // 输出进程的执行情况
            for (PCB pcb : pcbArray) {
                System.out.println("当前进程:" + pcb.name + ",当前进程finishedTime:" + pcb.finishedTime);
                sum += (float) pcb.finishedTime/ (float) pcb.requiredTime/ pcbArray.length ;
            }
            System.out.println("平均带权周转时间为:"+ sum);
        }
    }


}






