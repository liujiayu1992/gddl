package com.zr.utils;

import sun.net.TelnetOutputStream;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by zhiyu on 2017/3/11.
 */
public class FtpUpload1 extends FTPClientUtil {
    private static String path;
    public void connectServer(String ip, String username, String password, String filepath) {
        serverCfg.put(FTPClientUtil.SERVER_IP, ip.substring(0,ip.indexOf(":")));
        serverCfg.put(FTPClientUtil.SERVER_PORT, Integer.parseInt(ip.substring(ip.indexOf(":"))));
        serverCfg.put(FTPClientUtil.USER_NAME, username);
        serverCfg.put(FTPClientUtil.PASSWORD, password);
        this.path=filepath;
    }
    public static String upload(String localname, String remotename) {
        try {
            java.io.File file_in = new java.io.File(localname);
            return upload(file_in,path);
        } catch (IOException ex) {
            System.out.println("not upload");
            System.out.println(ex);
            return "ÉÏ´«Ê§°Ü";
        }
    }
    public void closeConnect() {

    }
}
