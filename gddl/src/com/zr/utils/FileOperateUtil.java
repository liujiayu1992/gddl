package com.zr.utils;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;


/**
 * @date 2012-5-5 下午12:05:57
 */
public class FileOperateUtil {

    // 判断文件是否存在
    public static void judeFileExists(File file) {

        if (file.exists()) {
            System.out.println("file exists");
        } else {
            System.out.println("file not exists, create it ...");
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    // 判断文件夹是否存在
    public static void judeDirExists(File file) {

        if (file.exists()) {
            if (file.isDirectory()) {
                System.out.println("dir exists");
            } else {
                System.out.println("the same name file exists, can not create dir");
            }
        } else {
            System.out.println("dir not exists, create it ...");
            file.mkdir();
        }

    }
    public static void main(String[] args) {
        File file = new File("d:\\test_file.txt");
        FileOperateUtil.judeFileExists(file);

        File dir = new File("d:\\test_dir");
        FileOperateUtil.judeDirExists(dir);
    }
}
