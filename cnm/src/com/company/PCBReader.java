package com.company;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PCBReader {
	public static void main(String[] args) {
		String filePath = "src/com/company/1.txt";
		readTXT(filePath);
	}

	public static PCB[] readTXT(String filePath) {
		PCB[] pcbs = null;

		try {
			File file = new File(filePath);
			if (file.isFile() && file.exists()) {
				InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

//				String lineTxt;
//				while ((lineTxt = bufferedReader.readLine()) != null) {
//					System.out.println(lineTxt);
//				}
				int sizeOfPcbArray = Integer.parseInt(bufferedReader.readLine());
				System.out.println("共有" + sizeOfPcbArray + "个进程");
				pcbs = new PCB[sizeOfPcbArray];

				String sizeOfTimeSlice = bufferedReader.readLine();
				System.out.println("时间片长度为" + sizeOfTimeSlice);
				RoundRobin.timeSlice = Integer.parseInt(sizeOfTimeSlice);

				for (int i = 0; i < sizeOfPcbArray; i++) {

					String[] pcbData = bufferedReader.readLine().split(" ");
					String name = pcbData[0];
					int burstTime = Integer.parseInt(pcbData[1]);
					int arriveTime = Integer.parseInt(pcbData[2]);
					pcbs[i] = new PCB(name, burstTime, arriveTime);
					System.out.println(pcbs[i].name + " " + pcbs[i].burstTime + " " + pcbs[i].arriveTime);
				}
				bufferedReader.close();
			} else {
				System.out.println("File not exists.");
			}
		} catch (IOException e) {
			System.out.println("File read error.");
		}
		return pcbs;
	}
}
