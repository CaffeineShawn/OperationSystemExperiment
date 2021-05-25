package com.company;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

public class Partition implements Comparable<Partition> {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int algorithmSelection = 0;
        while (algorithmSelection != 1  && algorithmSelection != 2) {
            System.out.print("1 for first-fit and 2 for best-fit:");
            algorithmSelection = sc.nextInt();
        }
        System.out.println("You have chosen:" + (algorithmSelection - 1 == 0 ? "first-fit" : "best-fit") );

        System.out.print("请输入分区的起始地址:");
        int startAddress = sc.nextInt();
        LinkedList<Partition> partitionLinkedList = new LinkedList<>();
        LinkedList<Process> processes = new LinkedList<>();

        System.out.print("请输入分区的大小:");
        partitionLinkedList.add(new Partition(sc.nextInt(), startAddress));

        for (Partition partition : partitionLinkedList) {
            System.out.println(partition.toString());
        }


        int processId = 1;

        boolean looping = true;
        while (looping) {
            System.out.print("1 for alloc, 2 for release, 3 for exit:");
            int selection = sc.nextInt();
            switch (selection) {
                case 1: {
                    System.out.print("请输入作业" + processId + "需要的空间大小:");
                    Process newProcess = new Process(sc.nextInt(), processId++);
                    processes.add(newProcess);
                    if (algorithmSelection == 1) {
                        FirstFitPartition(partitionLinkedList, newProcess);
                    } else {
                        BestFitPartition(partitionLinkedList,newProcess);
                    }


                    break;
                }
                case 2: {
                    System.out.print("需要释放的作业号为:");
                    releasePartition(partitionLinkedList, processes.get(sc.nextInt() - 1));
                    break;
                }
                case 3: {
                    looping = false;
                    break;
                }
            }
            MergeFreePartition(partitionLinkedList);
            MergeFreePartition(partitionLinkedList);// in case of merging the latter one :)
            for (Partition partition : partitionLinkedList) {
                System.out.println(partition.toString());
            }
        }

    }

    int partitionSize;
    int startAddress;
    int processId = -1;
    boolean assigned = false;


    Partition(int partitionSize, int startAddress) {
        this.startAddress = startAddress;
        this.partitionSize = partitionSize;
    }

    @Override
    public String toString() {

        String res =
                "当前分区起始地址:" + this.startAddress + ",当前分区大小:" + this.partitionSize + ",当前分区分配情况:" + (this.assigned ? "assigned" : "free");
        if (this.assigned) {
            res = res + ",当前分区作业号:" + this.processId;
        }
        return res;
    }

    @Override
    public int compareTo(Partition o) {
        return -this.partitionSize + o.partitionSize;
    }


    static void MergeFreePartition(LinkedList<Partition> partitionLinkedList) {
        Partition formerPart;
        ListIterator<Partition> listIterator = partitionLinkedList.listIterator();

        formerPart = listIterator.next();
        while (listIterator.hasNext()) {

            Partition currentPart = listIterator.next();
            if (!formerPart.assigned && !currentPart.assigned) {
                formerPart.partitionSize += currentPart.partitionSize;

                partitionLinkedList.remove(currentPart);
                break;

            }

            formerPart = currentPart;
        }
    }

    static void FirstFitPartition(LinkedList<Partition> partitionLinkedList, Process process) {

        boolean assignSuccess = false;


        for (Partition currentPartition : partitionLinkedList) {
            if (currentPartition.partitionSize >= process.requiredSpace && !currentPartition.assigned) {

                int currentId = partitionLinkedList.lastIndexOf(currentPartition);
                Partition newPartition = new Partition(currentPartition.partitionSize - process.requiredSpace, currentPartition.startAddress + process.requiredSpace);
                currentPartition.partitionSize = process.requiredSpace;
                currentPartition.assigned = true;
                currentPartition.processId = process.id;
                assignSuccess = true;
                partitionLinkedList.add(currentId + 1, newPartition);


                break;

            }
        }
        if (!assignSuccess) {
            System.out.println("Could not assign 作业" + process.id + ", skipping...");

        }

    }

    static void BestFitPartition(LinkedList<Partition> partitionLinkedList, Process process) {
        Partition bestFitPartition = null;
        int bestFitIndex = -1;
        //partitionLinkedList.sort(Partition::compareTo);
        // Search for the best-fit partition.
        for (Partition currentPartition : partitionLinkedList) {
            //if ((currentPartition.partitionSize - process.requiredSpace <= bestFitPartition.partitionSize - process.requiredSpace) && !currentPartition.assigned) {
            if (!currentPartition.assigned){
                if (bestFitPartition == null) {
                    if (process.requiredSpace <= currentPartition.partitionSize) {
                        bestFitPartition = currentPartition;
                        bestFitIndex = partitionLinkedList.indexOf(currentPartition);
                    }
                } else if ((currentPartition.partitionSize - process.requiredSpace <= bestFitPartition.partitionSize - process.requiredSpace)) {
                    bestFitPartition = currentPartition;
                    bestFitIndex = partitionLinkedList.indexOf(currentPartition);
                }
            }



        }


        if (bestFitPartition == null) {
            System.out.println("Could not assign 作业" + process.id + ", skipping...");
        } else {
            Partition newPartition = new Partition(bestFitPartition.partitionSize - process.requiredSpace, bestFitPartition.startAddress + process.requiredSpace);
            bestFitPartition.partitionSize = process.requiredSpace;
            bestFitPartition.assigned = true;
            bestFitPartition.processId = process.id;

            partitionLinkedList.add(bestFitIndex + 1, newPartition);
        }

    }

    static void releasePartition(LinkedList<Partition> partitionLinkedList, Process process) {
        Scanner sc = new Scanner(System.in);
        for (Partition currentPartition : partitionLinkedList) {

            if (currentPartition.processId == process.id) {
                System.out.print(currentPartition + ",需要释放的空间大小为:");
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
                    partitionLinkedList.add(currentId + 1, newPartition);

                    break;
                } else {
                    System.out.println("ERROR: Illegal release size.");
                }

            }
        }

    }


}



class Process {
    int requiredSpace;
    int id;


    Process(int requiredSpace, int id) {
        this.id = id;
        this.requiredSpace = requiredSpace;
    }



    



}