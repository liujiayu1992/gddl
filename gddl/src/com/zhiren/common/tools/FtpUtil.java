package com.zhiren.common.tools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import sun.net.*;
import sun.net.ftp.FtpClient;

/**
 * @author lee_scs
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */

public class FtpUtil {

    private static FtpClient ftpClient;

    /**
     * connectServer
     * ����ftp������
     * @throws java.io.IOException
     * @param path �ļ��У��մ����Ŀ¼
     * @param password ����
     * @param user   ��½�û�
     * @param server ��������ַ
     */
    public void connectServer(String server, String user, String password,  String path)
            throws IOException
    {
        // server��FTP��������IP��ַ��user:��¼FTP���������û���
        // password����¼FTP���������û����Ŀ��path��FTP�������ϵ�·��
        ftpClient = new FtpClient();
        ftpClient.openServer(server);
        ftpClient.login(user, password);
        //path��ftp��������Ŀ¼����Ŀ¼
        if (path.length() != 0)  ftpClient.cd(path);
        //��2�����ϴ�������
        ftpClient.binary();
    }

    /**
     * upload
     * �ϴ��ļ�
     * @throws java.lang.Exception
     * @return -1 �ļ�������
     *          -2 �ļ�����Ϊ��
     *          >0 �ɹ��ϴ��������ļ��Ĵ�С
     * @param newname �ϴ�������ļ���
     * @param filename �ϴ����ļ�
     */
    public long upload(String filename,String newname) throws Exception
    {
        long result = 0;
        TelnetOutputStream os = null;
        FileInputStream is = null;
        try {
            java.io.File file_in = new java.io.File(filename);
            if (!file_in.exists()) return -1;
            if (file_in.length()==0) return -2;
            os = ftpClient.put(newname);
            result = file_in.length();
            is = new FileInputStream(file_in);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = is.read(bytes)) != -1) {
                os.write(bytes, 0, c);
            }
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
        return result;
    }
    /**
     * upload
     * @throws java.lang.Exception
     * @return
     * @param filename
     */
    public long upload(String filename)
            throws Exception
    {
        String newname = "";
        if (filename.indexOf("/")>-1)
        {
            newname = filename.substring(filename.lastIndexOf("/")+1);
        }else
        {
            newname = filename;
        }
        return upload(filename,newname);
    }

    /**
     *  download
     *  ��ftp�����ļ�������
     * @throws java.lang.Exception
     * @return
     * @param newfilename �������ɵ��ļ���
     * @param filename �������ϵ��ļ���
     */
    public long download(String filename,String newfilename)
            throws Exception
    {
        long result = 0;
        TelnetInputStream is = null;
        FileOutputStream os = null;
        try
        {
            is = ftpClient.get(filename);
            java.io.File outfile = new java.io.File(newfilename);
            os = new FileOutputStream(outfile);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = is.read(bytes)) != -1) {
                os.write(bytes, 0, c);
                result = result + c;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
        return result;
    }
    /**
     * ȡ��ĳ��Ŀ¼�µ������ļ��б�
     *
     */
    public List getFileList(String path)
    {
        List list = new ArrayList();
        try
        {
//	 DataInputStream dis = new  DataInputStream(ftpClient.nameList(path)); 
            BufferedReader  dis = new  BufferedReader(new InputStreamReader(ftpClient.nameList(path)));
            String filename = "";
            while((filename=dis.readLine())!=null)
            {
                list.add(filename);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * closeServer
     * �Ͽ���ftp������������
     * @throws java.io.IOException
     */
    public void closeServer()
            throws IOException
    {
        try
        {
            if (ftpClient != null)
            {
                ftpClient.closeServer();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String [] args) throws Exception
    {
// FtpUtil ftp = new FtpUtil(); 
// 
// try { 
//      //����ftp������ 
//      ftp.connectServer("10.66.2.222", "administrator", "980904", "/xzy"); 
//      ftpClient.sendServer("mkdir /xzy/a\n");
//      
//     /* if(!(new File("c://Shujsc").isDirectory())){//�ж��ļ����Ƿ����
//			new File("c://Shujsc").mkdir(); //�½��ļ���
//	  }
//      
//      *//**  �ϴ��ļ��� info2 �ļ����� *//* 
//      System.out.println("filesize:"+ftp.upload("f:/download/Install.exe")+"�ֽ�"); 
//      *//** ȡ��info2�ļ����µ������ļ��б�,�����ص� E���� *//* 
//      List list = ftp.getFileList("."); 
//      for (int i=0;i<list.size();i++) 
//      { 
//         String filename = (String)list.get(i); 
//         System.out.println(filename); 
//         ftp.download(filename,"E:/"+filename); 
//      } */
// } catch (Exception e) { 
//    /// 
// }finally 
// { 
//    ftp.closeServer(); 
// } 
        Date date= new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, -3);
        c.get(Calendar.DATE);
//	System.out.println(date-1);
        System.out.println(c.get(Calendar.DAY_OF_MONTH)+","+c.get(Calendar.DATE));



    }
}

