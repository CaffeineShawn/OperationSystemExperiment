
package com.CaffeineShawn;

import java.util.*;

public class MemoryPaging {
    // 三种页面调度算法中内存块数以链表组织
    static LinkedList<Integer> pageInMemoryFIFO = new LinkedList<>();
    static LinkedList<Integer> pageInMemoryLRU = new LinkedList<>();
    static LinkedList<Integer> pageInMemoryOPT = new LinkedList<>();
    static int memoryBlock = 0;

    // 初始化缺页次数和当前时间
    static int missingPageFIFO = 0;
    static int missingPageLRU = 0;
    static int missingPageOPT = 0;
    static int timesCount = 0;




    public static void main(String[] args) {
        // 随机数组
        int[] instructionsArray = new int[321];

        // 用户输入内存块数
        memoryBlock = 4;
        System.out.print("内存块数: ");
        memoryBlock = new Scanner(System.in).nextInt();

        // 依照要求生成随机数指令序列
        int j = 0;
        for (int i = 0; i < 64; i++) {

            int m = (int) (Math.random() * 320);
            instructionsArray[j] = m + 1;
            ++j;

            int m1 = (int) (Math.random() * (m - 1));
            instructionsArray[j] = m1;
            ++j;
            instructionsArray[j] = m1+1;
            ++j;

            int m2 = (int) (Math.random() * (320 - (m1 + 2)) + m1 + 2);
            instructionsArray[j] = m2;
            ++j;
            instructionsArray[j] = m2+1;
            ++j;

        }

        // 显示指令序列
        for (int i = 1; i < 321; i++) {
            System.out.printf("%d\t",instructionsArray[i]);
            if (i % 5 == 0) {
                System.out.print("\n");
            }
        }



        // 调用指令序列,同时使用三种算法进行页面调度
        for (int i = 1; i < 321; i++) {
            instructionExecute(instructionsArray[i], instructionsArray,i);
        }

        // 算法执行完成，输出三种算法的结果
        System.out.printf("FIFO缺页率: %f", ((float) missingPageFIFO / (float) timesCount) * 100);
        System.out.println("%");
        System.out.println("FIFO缺页次数: "+missingPageFIFO );
        System.out.printf("LRU缺页率: %f", ((float) missingPageLRU / (float) timesCount) * 100);
        System.out.println("%");
        System.out.println("LRU缺页次数: "+missingPageLRU );
        System.out.printf("OPT缺页率: %f", ((float) missingPageOPT / (float) timesCount) * 100);
        System.out.println("%");
        System.out.println("OPT缺页次数: "+missingPageOPT );
        System.out.print("输入任意值结束:");
        String end = new Scanner(System.in).next();
    }
    // 完成从地址到页面的变换，使用三种页面调度算法
    static void instructionExecute(int value, int[] array, int index) {
        int pageIndex = value / 10;
        System.out.println("------当前执行第" + index + "条指令 " + value + " at Page " + pageIndex);
        FIFO(pageIndex, pageInMemoryFIFO);
        LRU(pageIndex, pageInMemoryLRU);
        OPT(pageIndex,pageInMemoryOPT,array,index);
        timesCount++;

    }

    static void FIFO(int pageIndex, LinkedList<Integer> pageInMemory) {
        if (!pageInMemory.contains(pageIndex)){
            // 简单统计一下
            missingPageFIFO++;
            if (pageInMemory.size() == memoryBlock) {
                // 执行FIFO
                // 换出队首
                System.out.println("FIFO Swap out " + pageInMemory.pollFirst());
                // 换入新页面到队尾
                System.out.println("FIFO Swap in " + pageIndex);
            } else {
                System.out.println("FIFO Loaded page: " + pageIndex);
            }
            // 直接装入
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
            // 简单统计一下
            missingPageLRU++;
            if (pageInMemory.size() == memoryBlock) {
                // 执行LRU
                // 换出最近未使用的页面（最近使用队列的队首）
                System.out.print("LRU Swap out ");
                int swapOut = notRecentlyUsed.pollFirst();
                System.out.println(swapOut);
                // 查询要换出的页面下标
                int swapOutIndex = pageInMemory.indexOf(swapOut);
                // 向内存块中换入将要新的页面，并将最近未使用的页面换出
                pageInMemory.add(swapOutIndex, pageIndex);
                pageInMemory.removeFirstOccurrence(swapOut);
                notRecentlyUsed.offer(pageIndex);
                System.out.println("LRU swap in " +pageIndex);

            } else  {
                // 直接换入，并将新页面号加入最近未使用队列的队尾
                System.out.println("LRU Loaded page: " + pageIndex);
                pageInMemory.offer(pageIndex);
                notRecentlyUsed.offer(pageIndex);

            }
        } else {
            // 无需换页，页面在内存中，需要更新在最近使用队列中的位置
            // 先删除原来存在的地方，后插入到队尾
            while (notRecentlyUsed.contains(pageIndex)) {
                notRecentlyUsed.removeFirstOccurrence(pageIndex);
            }
            notRecentlyUsed.offer(pageIndex);

        }

        // 显示内存块中的页面
        System.out.print("LRU Page in memory: ");
        for (Integer integer : pageInMemory) {
            System.out.printf("%d ", integer);
        }

        // 显示最近未使用队列
        System.out.print(", notRecentlyUsed: ");
        for (Integer integer : notRecentlyUsed) {
            System.out.printf("%d ", integer);
        }
        System.out.print("\n");

    }
//    基于使用统计次数的OPT，已废弃
//    static void OPT(int pageIndex, LinkedList<Integer> pageInMemory,Map<Integer,Integer> map) {
//        if (!pageInMemory.contains(pageIndex)) {
//            missingPageOPT++;
//            if (pageInMemory.size() == memoryBlock) {
//                int lessOpportunity = pageInMemory.peekFirst();
//                for (int i : pageInMemory) {
//                    if (map.get(i) < map.get(lessOpportunity)) {
//                        lessOpportunity = i;
//                    }
//                }
//                System.out.println("OPT swap in " + pageIndex);
//                System.out.println("OPT swap out " + lessOpportunity);
//                pageInMemory.removeFirstOccurrence(lessOpportunity);
//
//
//            }
//            pageInMemory.offer(pageIndex);
//        }
//        System.out.print("OPT Page in memory: ");
//        for (Integer integer : pageInMemory) {
//            System.out.printf("%d ", integer);
//        }
//        System.out.print("\n");
//
//    }

    static void OPT(int pageIndex, LinkedList<Integer> pageInMemory, int[] instructionsArray, int currentIndex) {
        if (!pageInMemory.contains(pageIndex)) {
            // 简单统计一下
            missingPageOPT++;
            if (pageInMemory.size() == memoryBlock) {
                // 初始化换出的页面号，此时没有实际指向
                int swapOut = Integer.MIN_VALUE;
                for (int page : pageInMemory) {
                    // 从随机数序列中查找内存中页面的下一次执行时间与当前的差值，差值最大者换出，若相等则换出最后一个
                    int tmp = nextAppearance(currentIndex, instructionsArray, page);
                    int cmp;
                    if (swapOut != Integer.MIN_VALUE) {
                        // 若swapOut为实际页面，则进行比较
                        cmp = nextAppearance(currentIndex, instructionsArray, swapOut);
                    } else {
                        // 否则直接复制值
                        cmp = tmp;
                    }
                    System.out.println("page: " + page + ", nextIndex: " + tmp + ", swapOut: " + swapOut);
                    if ( tmp >= cmp) {
                        // 更新换出页面号
                        swapOut = page;
                    }
                }
                // log
                System.out.println("OPT swap out " + swapOut);
                System.out.println("OPT swap in " + pageIndex);

                // 换入操作
                int swapOutIndex = pageInMemory.indexOf(swapOut);
                pageInMemory.add(swapOutIndex, pageIndex);
                pageInMemory.removeFirstOccurrence(swapOut);


            } else {
                pageInMemory.offer(pageIndex);
            }
        }
        // 输出
        System.out.print("OPT Page in memory: ");
        for (Integer integer : pageInMemory) {
            System.out.printf("%d ", integer);
        }
        System.out.print("\n");
    }

    static int nextAppearance(int currentIndex, int[] array, int page) {
        // 从随机数数组结合当前是第几次执行页面调度查找当前页面在随机数序列中下一次出现的情况并返回差值
        // 若返回最大值则为找不到，可以换出
        if (currentIndex + 1 == array.length) {

            return Integer.MAX_VALUE;
        }
        for (int i = currentIndex +1; i < array.length; i++) {
            // 无聊的页面换算
            if (array[i] / 10 == page) {
                return i - currentIndex;
            }
        }
        return Integer.MAX_VALUE;
    }




}

