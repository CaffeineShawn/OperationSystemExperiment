package com.CaffeineShawn;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


// 仅作为进程的自动输入
public class PCBReader {
	public static void main(String[] args) throws IOException {
		Resource resource = new Resource();
		String filePath = resource.getResource();
		readTXT();
	}

	public static PCB[] readTXT() {
		PCB[] pcbArray = null;

		try {


				Resource resource = new Resource();
				InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(resource.getClass().getResourceAsStream("testExample.txt")), StandardCharsets.UTF_8);
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
				inputStreamReader.close();


		} catch (IOException e) {
			System.out.println("File read error.");
		}
		return pcbArray;
	}
}
