package com.CaffeineShawn;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class Resource {
    public String getResource() throws IOException {
        //查找指定资源的URL
//        URL fileURL = this.getClass().getResource("testExample.txt");
//        System.out.println(fileURL.getFile());
//
//
//        return fileURL.getFile();
        // 打包出来不能读文件，改了一下
        InputStream is=this.getClass().getResourceAsStream("testExample.txt");
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        String s="";
        while((s=br.readLine())!=null)
            System.out.println(s);
        return s;
    }
    public static void main(String[] args) throws IOException {
        Resource res=new Resource();
        res.getResource();
    }

}

