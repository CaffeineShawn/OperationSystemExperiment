
package com.company;

import java.util.*;

public class MemoryPaging {
    static LinkedList<Integer> pageInMemoryFIFO = new LinkedList<>();
    static LinkedList<Integer> pageInMemoryLRU = new LinkedList<>();
    static int memoryBlock = 0;

    static int missingPageFIFO = 0;
    static int missingPageLRU = 0;
    static int timesCount = 0;




    public static void main(String[] args) {
        boolean[] instructionsArrayFIFO = new boolean[320];
        boolean[] instructionsArrayLRU = new boolean[320];
        memoryBlock = 3;
//        System.out.print("内存块数: ");
//        memoryBlock = new Scanner(System.in).nextInt();
//        while (!checkAllDone(instructionsArray)){
        for (int i = 0; i < 64; i++) {
            int m = (int) (Math.random() * 320);
            if (m + 1 <= 319){
                instructionExecute(instructionsArrayFIFO,instructionsArrayLRU,m+1);
            }
            int m1 = (int) (Math.random() * (m - 1));
//            System.out.println("pick: m1  =" + m1);
            instructionExecute(instructionsArrayFIFO,instructionsArrayLRU,m1);

//            System.out.println("pick: m1+1=" + (m1 + 1));
            instructionExecute(instructionsArrayFIFO,instructionsArrayLRU,m1+1);

            int m2 = (int) (Math.random() * (320 - (m1 + 2)) + m1 + 2);
//            System.out.println("pick: m2  =" + m2);

            instructionExecute(instructionsArrayFIFO,instructionsArrayLRU,m2);

            if (m2 + 1 <= 319) {
//                System.out.println("pick: m2+1=" + (m2 + 1));
                instructionExecute(instructionsArrayFIFO,instructionsArrayLRU,m2+1);

            }


        }

//        System.out.println("Times of missing page:" + missingPageFIFO);
        System.out.printf("FIFO缺页率: %f", ((float) missingPageFIFO / (float) timesCount) * 100);
        System.out.println("%");
        System.out.println("FIFO缺页次数: "+missingPageFIFO );
//        System.out.println("Times of missing page:" + missingPageLRU);
        System.out.printf("LRU缺页率: %f", ((float) missingPageLRU / (float) timesCount) * 100);
        System.out.println("%");
        System.out.println("LRU缺页次数: "+missingPageLRU );
    }

    static void instructionExecute(boolean[] FIFO,boolean[] LRU, int index) {
        int pageIndex = index / 10;


        System.out.println("------Currently executing: " + index + " at Page " + pageIndex);
        FIFO(pageIndex, pageInMemoryFIFO);
        LRU(pageIndex, pageInMemoryLRU);


        timesCount++;
//        switch (mode) {
//            case 1:
//                FIFO(pageIndex, pageInMemory);
//                break;
//            case 2:
//                LRU(pageIndex,pageInMemory);
//                break;
//            case 3:
//                System.out.println();
//                break;
//        }

    }

    static void FIFO(int pageIndex, LinkedList<Integer> pageInMemory) {
        if (!pageInMemory.contains(pageIndex)){
            missingPageFIFO++;
            if (pageInMemory.size() == memoryBlock) {
                // starting first in first out

                System.out.println("FIFO Swap out " + pageInMemory.pollFirst());

                System.out.println("FIFO Swap in " + pageIndex);
                pageInMemory.offer(pageIndex);
//
//                pageInMemory.pollFirst();



            } else {
                System.out.println("FIFO Loaded page: " + pageIndex);
                pageInMemory.offer(pageIndex);

            }
        }
        System.out.print("FIFO Page in memory: ");
        for (Integer integer : pageInMemory) {
            System.out.printf("%d ", integer);
        }
        System.out.print("\n");
    }

    static LinkedList<Integer> notRecentlyUsed = new LinkedList<>();

    static void LRU(int pageIndex, LinkedList<Integer> pageInMemory) {
        if (!pageInMemory.contains(pageIndex)) {
            missingPageLRU++;
            if (pageInMemory.size() == memoryBlock) {
                // starting LRU

                System.out.print("LRU Swap out ");
                int swapOut = notRecentlyUsed.pollFirst();
                System.out.println(swapOut);
                int swapOutIndex = pageInMemory.indexOf(swapOut);
                pageInMemory.add(swapOutIndex, pageIndex);
                pageInMemory.removeFirstOccurrence(swapOut);
                notRecentlyUsed.offer(pageIndex);
//                Map<Integer, Integer> map = new TreeMap<>();
//                for (int i : pageInMemory) {
//                    map.put(i, pageInMemory.indexOf(i));
//                }
//                int swappedOut = notRecentlyUsed.peekFirst();
//                for (int i : pageInMemory) {
//                    if (map.get(swappedOut) > map.get(i)) {
//                        swappedOut = i;
//                    }
//                }

//                int swappedOut = notRecentlyUsed.pollFirst();
//                while (notRecentlyUsed.contains(swappedOut)) {
//                    notRecentlyUsed.removeLastOccurrence(swappedOut);
//                }
//                System.out.println(swappedOut);
//                pageInMemory.removeFirstOccurrence(swappedOut);
//                //System.out.println(swappedOut);
//                System.out.println("LRU Swap in " + pageIndex);
//                pageInMemory.addFirst(pageIndex);
//                notRecentlyUsed.offer(pageIndex);

                System.out.println("LRU swap in " +pageIndex);

            } else  {
                System.out.println("LRU Loaded page: " + pageIndex);
                pageInMemory.offer(pageIndex);
                notRecentlyUsed.offer(pageIndex);

            }
        } else {// 无需换页
            while (notRecentlyUsed.contains(pageIndex)) {
                notRecentlyUsed.removeFirstOccurrence(pageIndex);
            }
            notRecentlyUsed.offer(pageIndex);

        }
        System.out.print("LRU Page in memory: ");
        for (Integer integer : pageInMemory) {
            System.out.printf("%d ", integer);
        }
        System.out.print("\n");
        System.out.print("LRU notRecentlyUsed: ");
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

