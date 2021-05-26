package com.company;

public class MemoryPaging {



    public static void main(String[] args) {
        boolean[] instructionsArray = new boolean[320];
        while (!checkAllDone(instructionsArray)){
            int m = (int) (Math.random() * 320);
            if (m + 1 <= 319){
                instructionExecute(instructionsArray,m+1);
            }
            int m1 = (int) (Math.random() * (m - 1));
//            System.out.println("pick: m1  =" + m1);
            instructionExecute(instructionsArray,m1);
//            System.out.println("pick: m1+1=" + (m1 + 1));
            instructionExecute(instructionsArray,m1+1);
            int m2 = (int) (Math.random() * (320 - (m1 + 2)) + m1 + 2);
//            System.out.println("pick: m2  =" + m2);
            instructionsArray[m2] = true;
            if (m2 + 1 <= 319) {
//                System.out.println("pick: m2+1=" + (m2 + 1));
                instructionExecute(instructionsArray,m2+1);
            }
        }

//        for (int i = 0; i < 320; i++) {
//            if (instructionsArray[i]) {
//                System.out.println(i +" is done.");
//            }
//        }
    }

    static boolean checkAllDone(boolean[] array) {
        for (boolean i : array) {
            if (!i) {
                return false;
            }
        }
        System.out.println("All instructions has been executed.");
        return true;
    }

    static int instructionExecute(boolean[] instructionsArray, int index) {
        int pageIndex = index / 10;
        System.out.println("Currently executing: " + index + " at Page " + pageIndex);

        if (instructionsArray[index] == false) {
            instructionsArray[index] = true;
        } else {
            System.out.println((index) + " has already been executed.");
        }
        return pageIndex;
    }

}
