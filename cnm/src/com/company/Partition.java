package com.company;

import java.util.Scanner;

public class Partition {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.print("请输入分区数:");
        Partition[] partitions = new Partition[sc.nextInt()];

        System.out.print("请输入分区的起始地址:");
        int startAddress = sc.nextInt();


        for (int i = 0; i < partitions.length; i++) {
            System.out.print("请输入分区" + i + "的大小:");
            partitions[i] = new Partition(sc.nextInt(),i);
            if (i == 0) {
                partitions[i].address = startAddress;
            } else {
                partitions[i].address = partitions[i].partitionSize + partitions[i - 1].address;
                partitions[i - 1].nextPartition = partitions[i];
            }
        }

        System.out.print("请输入进程数:");
        Process[] processes = new Process[sc.nextInt()];

        for (int i = 0; i < processes.length; i++) {
            System.out.print("请输入进程"+ i + "需要的空间大小:");
            processes[i] = new Process(sc.nextInt(),i);
        }

        Process.firstFitPartition(partitions, processes);

        for (int i = 0; i < processes.length; i++) {
            System.out.println(processes[i].toString());
        }

        for (Partition partition : partitions) {
            System.out.println(partition.toString());
        }
        System.out.print("需要释放的进程号为:");
        Process.releasePartition(partitions,processes[sc.nextInt()]);
    }

    int partitionSize;
    int availableSpace;
    int address;
    int id;
    Partition nextPartition;




    Partition(int partitionSize, int id) {
        this.partitionSize = partitionSize;
        this.availableSpace = partitionSize;
        this.id = id;
        nextPartition = null;
    }

    @Override
    public String toString() {

        return  "当前分区号:" +
                this.id +
                ",当前分区起始地址:" +
                this.address +
                ",当前分区大小:" +
                this.partitionSize +
                ",当前分区空闲空间:" +
                this.availableSpace;
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

    static void firstFitPartition(Partition[] partitions, Process[] processes) {
        int partitionIndex;
        int processIndex;
        processIndex = 0;

        while (processIndex != processes.length) {
            boolean assignSuccess = false;
            System.out.println("Current process:" + processIndex);
            for (partitionIndex = 0; partitionIndex != partitions.length; partitionIndex++) {
                System.out.println("Current partition:" + partitionIndex);
                if (partitions[partitionIndex].availableSpace >= processes[processIndex].requiredSpace) {
                    partitions[partitionIndex].availableSpace -= processes[processIndex].requiredSpace;
                    processes[processIndex].assignedPartition = partitionIndex;
                    processIndex++;
                    assignSuccess = true;
                    break;

                }
            }
            if (!assignSuccess) {
                System.out.println("Could not assign Process:" + processIndex + ", skipping...");
                processIndex++;
            }
        }
    }

    @Override
    public String toString() {
        return "进程" + this.id + ":当前占用的空间大小为:" + this.requiredSpace + ",当前分配的分区号为:" + this.assignedPartition;
    }

    static void releasePartition(Partition[] partitions, Process process) {
        Scanner sc = new Scanner(System.in);
        System.out.print(process.toString() + ",需要释放的空间大小为:");
        int releaseSpace = sc.nextInt();
        partitions[process.assignedPartition].availableSpace += releaseSpace;
        if (releaseSpace == process.requiredSpace) {
            process.assignedPartition = -1;
        }

        for (Partition partition : partitions) {
            System.out.println(partition.toString());
        }

    }



}
