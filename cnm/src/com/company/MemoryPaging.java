
package com.company;

import java.util.*;

public class MemoryPaging {
    static LinkedList<Integer> pageInMemoryFIFO = new LinkedList<>();
    static LinkedList<Integer> pageInMemoryLRU = new LinkedList<>();
    static LinkedList<Integer> pageInMemoryOPT = new LinkedList<>();
    static int memoryBlock = 0;

    static int missingPageFIFO = 0;
    static int missingPageLRU = 0;
    static int missingPageOPT = 0;
    static int timesCount = 0;




    public static void main(String[] args) {
        int[] instructionsArray = new int[321];

        memoryBlock = 3;
//        System.out.print("内存块数: ");
//        memoryBlock = new Scanner(System.in).nextInt();
//        while (!checkAllDone(instructionsArray)){
        int j = 0;
        for (int i = 0; i < 64; i++) {
            int m = (int) (Math.random() * 320);
            instructionsArray[j] = m + 1;
            ++j;
            int m1 = (int) (Math.random() * (m - 1));

//            instructionExecute(m1);
            instructionsArray[j] = m1;
            ++j;
//            instructionExecute(m1+1);
            instructionsArray[j] = m1+1;
            ++j;
            int m2 = (int) (Math.random() * (320 - (m1 + 2)) + m1 + 2);


//            instructionExecute(m2);
            instructionsArray[j] = m2;
            ++j;
            instructionsArray[j] = m2+1;
            ++j;


        }
//        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 1; i < 321; i++) {
            System.out.printf("%d\t",instructionsArray[i]);
            if (i % 5 == 0) {
                System.out.print("\n");
            }
//            if (map.containsKey(instructionsArray[i]/10)) {
//                map.put((instructionsArray[i]/10), map.get(instructionsArray[i]/10) + 1);
//            } else {
//                map.put((instructionsArray[i]/10), 1);
//            }
        }
//        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
//
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//
//        }



        for (int i = 1; i < 321; i++) {
            instructionExecute(instructionsArray[i], instructionsArray,i);
        }


//        System.out.println("Times of missing page:" + missingPageFIFO);
        System.out.printf("FIFO缺页率: %f", ((float) missingPageFIFO / (float) timesCount) * 100);
        System.out.println("%");
        System.out.println("FIFO缺页次数: "+missingPageFIFO );
//        System.out.println("Times of missing page:" + missingPageLRU);
        System.out.printf("LRU缺页率: %f", ((float) missingPageLRU / (float) timesCount) * 100);
        System.out.println("%");
        System.out.println("LRU缺页次数: "+missingPageLRU );
        System.out.printf("OPT缺页率: %f", ((float) missingPageOPT / (float) timesCount) * 100);
        System.out.println("%");
        System.out.println("OPT缺页次数: "+missingPageOPT );

    }

    static void instructionExecute(int value, int[] array, int index) {
        int pageIndex = value / 10;


        System.out.println("------当前执行第" + index + "条指令 " + value + " at Page " + pageIndex);
        FIFO(pageIndex, pageInMemoryFIFO);
        LRU(pageIndex, pageInMemoryLRU);
//        OPT(pageIndex,pageInMemoryOPT,map);
        TrueOPT(pageIndex,pageInMemoryOPT,array,index);
        timesCount++;

    }

    static void FIFO(int pageIndex, LinkedList<Integer> pageInMemory) {
        if (!pageInMemory.contains(pageIndex)){
            missingPageFIFO++;
            if (pageInMemory.size() == memoryBlock) {
                // starting first in first out

                System.out.println("FIFO Swap out " + pageInMemory.pollFirst());

                System.out.println("FIFO Swap in " + pageIndex);
                //
//                pageInMemory.pollFirst();



            } else {
                System.out.println("FIFO Loaded page: " + pageIndex);

            }
            pageInMemory.offer(pageIndex);
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

        System.out.print(", notRecentlyUsed: ");
        for (Integer integer : notRecentlyUsed) {
            System.out.printf("%d ", integer);
        }
        System.out.print("\n");
    }

    static void OPT(int pageIndex, LinkedList<Integer> pageInMemory,Map<Integer,Integer> map) {
        if (!pageInMemory.contains(pageIndex)) {
            missingPageOPT++;
            if (pageInMemory.size() == memoryBlock) {
                int lessOpportunity = pageInMemory.peekFirst();
                for (int i : pageInMemory) {
                    if (map.get(i) < map.get(lessOpportunity)) {
                        lessOpportunity = i;
                    }
                }
                System.out.println("OPT swap in " + pageIndex);
                System.out.println("OPT swap out " + lessOpportunity);
                pageInMemory.removeFirstOccurrence(lessOpportunity);


            }
            pageInMemory.offer(pageIndex);
        }
        System.out.print("OPT Page in memory: ");
        for (Integer integer : pageInMemory) {
            System.out.printf("%d ", integer);
        }
        System.out.print("\n");

    }

    static void TrueOPT(int pageIndex, LinkedList<Integer> pageInMemory,int[] instructionsArray,int currentIndex) {
        if (!pageInMemory.contains(pageIndex)) {
            missingPageOPT++;
            if (pageInMemory.size() == memoryBlock) {
                int swapOut = Integer.MIN_VALUE;
                for (int page : pageInMemory) {
                    int tmp = nextIndex(currentIndex, instructionsArray, page);
                    int cmp;
                    if (swapOut != Integer.MIN_VALUE) {
                        cmp = nextIndex(currentIndex, instructionsArray, swapOut);
                    } else {
                        cmp = tmp;
                    }
                    System.out.println("page: " + page + ", nextIndex: " + tmp + ", swapOut: " + swapOut);
                    if ( tmp >= cmp) {
                        swapOut = page;
                    }
                }

                System.out.println("OPT swap out " + swapOut);
                System.out.println("OPT swap in " + pageIndex);

                int swapOutIndex = pageInMemory.indexOf(swapOut);
                pageInMemory.add(swapOutIndex, pageIndex);
                pageInMemory.removeFirstOccurrence(swapOut);


            } else {
                pageInMemory.offer(pageIndex);
            }
        }
        System.out.print("OPT Page in memory: ");
        for (Integer integer : pageInMemory) {
            System.out.printf("%d ", integer);
        }
        System.out.print("\n");
    }

    static int nextIndex(int currentIndex, int[] array,int value) {

        if (currentIndex + 1 == array.length) {
            return Integer.MAX_VALUE;
        }
        for (int i = currentIndex +1; i < array.length; i++) {
            if (array[i] / 10 == value) {
                return i - currentIndex;
            }
        }
        return Integer.MAX_VALUE;
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

