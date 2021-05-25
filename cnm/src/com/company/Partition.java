package com.company;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

public class Partition {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("请输入分区的起始地址:");
        int startAddress = sc.nextInt();
        ;
        LinkedList<Partition> partitionLinkedlist = new LinkedList<>();
        LinkedList<Process> processes = new LinkedList<>();

        System.out.print("请输入分区的大小:");
        partitionLinkedlist.add(new Partition(sc.nextInt(),startAddress));

        for (Partition partition : partitionLinkedlist) {
            System.out.println(partition.toString());
        }

        int processId = 1;

        boolean looping = true;
        while (looping){
            System.out.print("1 for alloc, 2 for release, 3 for exit:");
            int selection = sc.nextInt();
            switch (selection) {
                case 1: {
                    System.out.print("请输入作业"+ processId + "需要的空间大小:");
                    Process newProcess = new Process(sc.nextInt(), processId++);
                    processes.add(newProcess);
                    Process.firstFitPartition(partitionLinkedlist, newProcess);

                    break;
                }
                case 2: {
                    System.out.print("需要释放的作业号为:");
                    Process.releasePartition(partitionLinkedlist, processes.get(sc.nextInt()-1));
                    break;
                }
                case 3: {
                    looping = false;
                    break;
                }
            }
            for (Partition partition : partitionLinkedlist) {
                System.out.println(partition.toString());
            }
        }

    }

    int partitionSize;
    int startAddress;
    int processId = -1;
    boolean assigned = false;



    Partition(int partitionSize,int startAddress) {
        this.startAddress = startAddress;
        this.partitionSize = partitionSize;
    }

    @Override
    public String toString() {

        String res =
                "当前分区起始地址:" + this.startAddress + ",当前分区大小:" + this.partitionSize + ",当前分区分配情况:" + (this.assigned ? "assigned" : "free");
        if (this.assigned == true) {
            res = res + ",当前分区作业号:" + this.processId;
        }
        return res;
    }





}



class Process {
    int requiredSpace;
    int assignedPartition = -1;
    int id;


    Process(int requiredSpace, int id) {
        this.id = id;
        this.requiredSpace = requiredSpace;
    }

    static void firstFitPartition(LinkedList<Partition> partitionLinkedList, Process process) {
        int partitionIndex;
        int processIndex;
        processIndex = 0;

        boolean assignSuccess = false;


        for (Partition currentPartition : partitionLinkedList) {
            if (currentPartition.partitionSize >= process.requiredSpace && currentPartition.assigned == false) {

                int currentId = partitionLinkedList.lastIndexOf(currentPartition);
                Partition newPartition = new Partition(currentPartition.partitionSize - process.requiredSpace, currentPartition.startAddress + process.requiredSpace);
                currentPartition.partitionSize = process.requiredSpace;
                currentPartition.assigned = true;
                currentPartition.processId = process.id;
                assignSuccess = true;
                partitionLinkedList.add(currentId +1,newPartition);


                break;

            }
        }
        if (!assignSuccess) {
            System.out.println("Could not assign Process:" + processIndex + ", skipping...");
            processIndex++;
        }

    }

    static void releasePartition(LinkedList<Partition> partitionLinkedList, Process process) {
        Scanner sc = new Scanner(System.in);
        //System.out.print(process.toString() + ",需要释放的空间大小为:");
      //  int releaseSize = sc.nextInt();
        boolean releaseSuccess = false;
        for (Partition currentPartition : partitionLinkedList) {

            if (currentPartition.processId == process.id) {
                System.out.print(currentPartition.toString()  + ",需要释放的空间大小为:");
                int releaseSpace = sc.nextInt();
                if (currentPartition.partitionSize == releaseSpace) {
                    currentPartition.processId = -1;
                    currentPartition.assigned = false;
                } else if (releaseSpace < currentPartition.partitionSize) {
                    int currentId = partitionLinkedList.lastIndexOf(currentPartition);
                    Partition newPartition = new Partition(currentPartition.partitionSize - releaseSpace, currentPartition.startAddress + releaseSpace);
                    currentPartition.partitionSize = releaseSpace;
                    currentPartition.assigned = false;
                    currentPartition.processId = -1;
                    newPartition.processId = process.id;
                    newPartition.assigned = true;
                    //releaseSuccess = true;
                    partitionLinkedList.add(currentId +1,newPartition);
                    if (currentId > 1) {
                        if (partitionLinkedList.get(currentId - 1).assigned == false) {
                            Partition formerPartition = partitionLinkedList.get(currentId - 1);
                            formerPartition.partitionSize += currentPartition.partitionSize;
                            partitionLinkedList.remove(currentId);
                        }
                    }
                    break;
                } else {
                    System.out.println("ERROR: Illegal release size.");
                }

            }
        }

    }



}