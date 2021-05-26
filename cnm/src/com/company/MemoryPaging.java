package com.company;

import sun.awt.image.ImageWatched;

import java.util.LinkedList;
import java.util.Scanner;

public class MemoryPaging {
    static LinkedList<Integer> pageInMemory = new LinkedList<>();
    static boolean[] instructionsArray = new boolean[320];
    static int missingPage = 0;
    static int timesCount = 0;

    public static void main(String[] args) {
        int modeSelection;
        Scanner sc = new Scanner(System.in);
        System.out.print("1 for FIFO, 2 for LRU, 3 for OPT: ");
        modeSelection = sc.nextInt();

//        while (!checkAllDone(instructionsArray)){
        for (int i = 0; i < 320; i++) {
            int m = (int) (Math.random() * 320);
            if (m + 1 <= 319){
                instructionExecute(instructionsArray,m+1,modeSelection);
            }
            int m1 = (int) (Math.random() * (m - 1));
//            System.out.println("pick: m1  =" + m1);
            instructionExecute(instructionsArray,m1,modeSelection);
//            System.out.println("pick: m1+1=" + (m1 + 1));
            instructionExecute(instructionsArray,m1+1,modeSelection);
            int m2 = (int) (Math.random() * (320 - (m1 + 2)) + m1 + 2);
//            System.out.println("pick: m2  =" + m2);
            instructionsArray[m2] = true;
            if (m2 + 1 <= 319) {
//                System.out.println("pick: m2+1=" + (m2 + 1));
                instructionExecute(instructionsArray,m2+1,modeSelection);
            }


        }

        System.out.println("Times of missing page:" + missingPage);
        System.out.printf("Percentage: %f", ((float) missingPage / (float) timesCount) * 100);
        System.out.println("%");
    }

    static void instructionExecute(boolean[] instructionsArray, int index, int mode) {
        int pageIndex = index / 10;
        System.out.println("Currently executing: " + index + " at Page " + pageIndex);

//        if (!instructionsArray[index]) {
//            instructionsArray[index] = true;
//        } else {
//            System.out.println((index) + " has already been executed.");
//        }
        timesCount++;
        switch (mode) {
            case 1:
                FIFO(pageIndex, pageInMemory);
                break;
            case 2:
                LRU(pageIndex,pageInMemory);
                break;
            case 3:
                System.out.println();
                break;
        }

    }

    static void FIFO(int pageIndex, LinkedList<Integer> pageInMemory) {
        if (!pageInMemory.contains(pageIndex)){
            missingPage++;
            if (pageInMemory.size() == 4) {
                // starting first in first out

                System.out.println("Swap out " + pageInMemory.pollFirst());

                System.out.println("Swap in " + pageIndex);
                pageInMemory.offer(pageIndex);



            } else if (pageInMemory.size() < 4) {
                System.out.println("Loaded page: " + pageIndex);
                pageInMemory.offer(pageIndex);

            }
        }
        System.out.print("Page in memory: ");
        for (Integer integer : pageInMemory) {
            System.out.printf("%d ", integer);
        }
        System.out.print("\n");
    }

    static LinkedList<Integer> notRecentlyUsed = new LinkedList<>();

    static void LRU(int pageIndex, LinkedList<Integer> pageInMemory) {
        if (!pageInMemory.contains(pageIndex)) {
            missingPage++;
            if (pageInMemory.size() == 4) {
                // starting LRU

                System.out.print("Swap out ");
                int swappedOut = notRecentlyUsed.pollFirst();
                while (notRecentlyUsed.contains(swappedOut)) {
                    notRecentlyUsed.removeLastOccurrence(swappedOut);
                }
                System.out.println(swappedOut);
                pageInMemory.remove(pageInMemory.indexOf(swappedOut));
                //System.out.println(swappedOut);
                System.out.println("Swap in " + pageIndex);
                pageInMemory.offer(pageIndex);
                notRecentlyUsed.offer(pageIndex);



            } else if (pageInMemory.size() < 4) {
                System.out.println("Loaded page: " + pageIndex);
                pageInMemory.offer(pageIndex);
                notRecentlyUsed.offer(pageIndex);

            }
        } else {
            notRecentlyUsed.offer(pageIndex);
        }
        System.out.print("Page in memory: ");
        for (Integer integer : pageInMemory) {
            System.out.printf("%d ", integer);
        }
        System.out.print("\n");
        System.out.print("notRecentlyUsed: ");
        for (Integer integer : notRecentlyUsed) {
            System.out.printf("%d ", integer);
        }
        System.out.print("\n");
    }

    static boolean checkAllDone(boolean[] array) {
        for (boolean i : array) {
            if (!i) {
                return false;
            }
        }

        return true;
    }



}


