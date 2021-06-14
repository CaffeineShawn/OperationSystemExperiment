package com.CaffeineShawn;

import java.io.*;
import java.nio.charset.StandardCharsets;


// 仅作为进程的自动输入
public class PCBReader {
	public static void main(String[] args) {
		String filePath = "src/com/company/TestExample.txt";
		readTXT(filePath);
	}

	public static PCB[] readTXT(String filePath) {
		PCB[] pcbArray = null;

		try {
			File file = new File(filePath);
			if (file.isFile() && file.exists()) {
				InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

				int sizeOfPcbArray = Integer.parseInt(bufferedReader.readLine());
				System.out.println("共有" + sizeOfPcbArray + "个进程");
				pcbArray = new PCB[sizeOfPcbArray];

				String sizeOfTimeSlice = bufferedReader.readLine();
				System.out.println("时间片长度为" + sizeOfTimeSlice);
				RoundRobin.timeSlice = Integer.parseInt(sizeOfTimeSlice);

				for (int i = 0; i < sizeOfPcbArray; i++) {

					String[] pcbData = bufferedReader.readLine().split(" ");
					String name = pcbData[0];
					int burstTime = Integer.parseInt(pcbData[1]);
					int arriveTime = Integer.parseInt(pcbData[2]);
					pcbArray[i] = new PCB(name, burstTime, arriveTime);
					System.out.println(pcbArray[i].name + " " + pcbArray[i].burstTime + " " + pcbArray[i].arriveTime +"\n");
				}
				bufferedReader.close();
			} else {
				System.out.println("File not exists.");
			}
		} catch (IOException e) {
			System.out.println("File read error.");
		}
		return pcbArray;
	}
}
