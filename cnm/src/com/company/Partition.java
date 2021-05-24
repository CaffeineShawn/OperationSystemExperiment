package com.company;

<<<<<<< Updated upstream
import java.util.LinkedList;
import java.util.ListIterator;
=======
import sun.awt.image.ImageWatched;

import java.util.LinkedList;
>>>>>>> Stashed changes
import java.util.Scanner;

public class Partition {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
<<<<<<< Updated upstream
        System.out.print("请输入分区数:");
//        Partition[] partitions = new Partition[sc.nextInt()];
        int numOfPartitions = sc.nextInt();
=======
//        System.out.print("请输入分区数:");
//        Partition[] partitions = new Partition[sc.nextInt()];
//       int numOfPartitions = sc.nextInt();
>>>>>>> Stashed changes

        System.out.print("请输入分区的起始地址:");
        int startAddress = sc.nextInt();
        Partition firstPartition;
        Partition currentPartition;

        LinkedList<Partition> partitionLinkedlist = new LinkedList<>();
<<<<<<< Updated upstream

        for (int i = 0; i < numOfPartitions; i++) {
            System.out.print("请输入分区" + i + "的大小:");
            //partitions[i] = new Partition(sc.nextInt(),i);
            partitionLinkedlist.add(new Partition(sc.nextInt(), i));
            if (i == 0) {
                //partitions[i].address = startAddress;
                partitionLinkedlist.getLast().address = startAddress;

            } else {

                //partitions[i].address = partitions[i].partitionSize + partitions[i - 1].address;
                //partitions[i - 1].nextPartition = partitions[i];
                partitionLinkedlist.getLast().address = partitionLinkedlist.get(i - 1).partitionSize + partitionLinkedlist.get(i - 1).address;

            }
=======
        System.out.print("请输入分区的大小:");
        partitionLinkedlist.add(new Partition(sc.nextInt(),startAddress));
//        for (int i = 0; i < numOfPartitions; i++) {
//            System.out.print("请输入分区" + i + "的大小:");
//            //partitions[i] = new Partition(sc.nextInt(),i);
//            partitionLinkedlist.add(new Partition(sc.nextInt(), i));
//            if (i == 0) {
//                //partitions[i].address = startAddress;
//                partitionLinkedlist.getLast().address = startAddress;
//
//            } else {
//
//                //partitions[i].address = partitions[i].partitionSize + partitions[i - 1].address;
//                //partitions[i - 1].nextPartition = partitions[i];
//                partitionLinkedlist.getLast().address = partitionLinkedlist.get(i - 1).partitionSize + partitionLinkedlist.get(i - 1).address;
//
//            }
//        }

//        System.out.print("请输入进程数:");
//        Process[] processes = new Process[sc.nextInt()];
//
//        for (int i = 0; i < processes.length; i++) {
//            System.out.print("请输入进程"+ i + "需要的空间大小:");
//            processes[i] = new Process(sc.nextInt(),i);
//        }

        //Process.firstFitPartition(partitionLinkedlist, processes);

//        for (Process process : processes) {
//            System.out.println(process.toString());
//        }
        LinkedList<Process> processes = new LinkedList<>();

        for (Partition partition : partitionLinkedlist) {
            System.out.println(partition.toString());
>>>>>>> Stashed changes
        }

        int processId = 0;

<<<<<<< Updated upstream
        for (int i = 0; i < processes.length; i++) {
            System.out.print("请输入进程"+ i + "需要的空间大小:");
            processes[i] = new Process(sc.nextInt(),i);
        }

        //Process.firstFitPartition(partitionLinkedlist, processes);

        for (Process process : processes) {
            System.out.println(process.toString());
        }

        for (Partition partition : partitionLinkedlist) {
            System.out.println(partition.toString());
        }
        System.out.println("1 for alloc, 2 for release, 3 for exit");
        int processId = 0;
        int selection = sc.nextInt();
        boolean looping = true;
        while (looping){
            switch (selection) {
                case 1: {
                    Process newProcess = new Process(sc.nextInt(), processId++);
                    Process.firstFitPartition(partitionLinkedlist, newProcess);
                    break;
                }
                case 2: {
                    System.out.print("需要释放的进程号为:");
                    Process.releasePartition(partitionLinkedlist, processes[sc.nextInt()]);
                    break;
                }
                case 3: {
                    looping = false;
                    break;
                }
            }
        }

=======
        boolean looping = true;
        while (looping){
            System.out.print("1 for alloc, 2 for release, 3 for exit:");
            int selection = sc.nextInt();
            switch (selection) {
                case 1: {
                    System.out.print("请输入进程"+ processId + "需要的空间大小:");
                    Process newProcess = new Process(sc.nextInt(), processId++);
                    processes.add(newProcess);
                    Process.firstFitPartition(partitionLinkedlist, newProcess);

                    break;
                }
                case 2: {
                    System.out.print("需要释放的进程号为:");
                    Process.releasePartition(partitionLinkedlist, processes.get(sc.nextInt()));
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

>>>>>>> Stashed changes
    }

    int partitionSize;
    int startAddress;
    int processId = -1;
    boolean assigned = false;




    Partition(int partitionSize,int startAddress) {
        this.partitionSize = partitionSize;

        this.startAddress = startAddress;

    }

    @Override
    public String toString() {

        String res =
                "当前分区起始地址:" + this.startAddress + ",当前分区大小:" + this.partitionSize;
        if (this.assigned == true) {
            res = res + ",当前分区进程号:" + this.processId;
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

//        ListIterator<Student> listIterator=students.listIterator();
//        while(listIterator.hasNext()){
//            System.out.println(listIterator.next());
//        }



<<<<<<< Updated upstream
            boolean assignSuccess = false;
            System.out.println("Current process:" + processIndex);
            //for (partitionIndex = 0; partitionIndex != partitions.length; partitionIndex++) {

            for (Partition currentPartition : partitionLinkedList) {
                System.out.println("Current partition:" + currentPartition.id);
=======
        boolean assignSuccess = false;
//        System.out.println("Current process:" + processIndex);
        //for (partitionIndex = 0; partitionIndex != partitions.length; partitionIndex++) {

        for (Partition currentPartition : partitionLinkedList) {
            System.out.println();
>>>>>>> Stashed changes
//                if (partitions[partitionIndex].availableSpace >= processes[processIndex].requiredSpace) {
//                    partitions[partitionIndex].availableSpace -= processes[processIndex].requiredSpace;
//                    processes[processIndex].assignedPartition = partitionIndex;
//                    processIndex++;
//                    assignSuccess = true;
//                    break;
//
//                }

<<<<<<< Updated upstream
                if (currentPartition.availableSpace >= process.requiredSpace) {
                    currentPartition.availableSpace -= process.requiredSpace;
                    process.assignedPartition = currentPartition.id;
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
=======
            if (currentPartition.partitionSize >= process.requiredSpace && currentPartition.assigned == false) {
//                Partition newPartition = new Partition(process.requiredSpace,currentPartition.startAddress);
//                assignSuccess = true;
//                newPartition.assigned = true;
//                newPartition.processId = process.id;
//                currentPartition.startAddress = newPartition.startAddress + newPartition.partitionSize;
//                currentPartition.partitionSize -= newPartition.partitionSize;
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
>>>>>>> Stashed changes

    }

<<<<<<< Updated upstream
=======
//    @Override
//    public String toString() {
//        return "进程" + this.id + ":当前占用的空间大小为:" + this.requiredSpace + ",当前分配的分区号为:" + this.assignedPartition;
//    }

>>>>>>> Stashed changes
    static void releasePartition(LinkedList<Partition> partitionLinkedList, Process process) {
        Scanner sc = new Scanner(System.in);
        System.out.print(process.toString() + ",需要释放的空间大小为:");
        int releaseSpace = sc.nextInt();
        //partitions[process.assignedPartition].availableSpace += releaseSpace;

<<<<<<< Updated upstream
        partitionLinkedList.get(process.assignedPartition).availableSpace += releaseSpace;
=======
        partitionLinkedList.get(process.assignedPartition).partitionSize += releaseSpace;
>>>>>>> Stashed changes
        if (releaseSpace == process.requiredSpace) {
            process.assignedPartition = -1;
        }

//        for (Partition partition : partitions) {
//            System.out.println(partition.toString());
//        }
        for (Partition partition : partitionLinkedList) {
            System.out.println(partition.toString());
        }

    }



}

