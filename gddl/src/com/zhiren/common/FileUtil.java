package com.zhiren.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class FileUtil {
	public static String getSuffix(String fileName){
		int index = fileName.lastIndexOf(".");
		return fileName.substring(index);
	}
	/**
	 * @param existed Դ�ļ�
	 * @param moveTo �ƶ�����·��
	 * @return �Ƿ�ɹ�
	 */
	public static boolean move(File existed,String moveTo){
		File mt = new File(moveTo,existed.getName());
		boolean success = existed.renameTo(mt);
		return success;
	}
	/**
	 * 
	 * @param existed
	 * @param copyTo
	 * @return
	 */
	public static void copy(File existed,String copyTo) throws IOException{
		File cpt = new File(copyTo);
		if(!cpt.exists()){
			cpt.mkdirs();
		}
		File cptf = new File(cpt,existed.getName());
		cptf.delete();
		cptf.createNewFile();
		InputStream in = new FileInputStream(existed);
		OutputStream out = new FileOutputStream(cptf);
		 
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0)
		{
		    out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

    /**
     * ɾ���ļ����������ļ����ļ���
     *
     * @param fileName
     *            Ҫɾ�����ļ���
     * @return ɾ���ɹ�����true�����򷵻�false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("ɾ���ļ�ʧ��:" + fileName + "�����ڣ�");
            return false;
        } else {
            if (file.isFile())
                return deleteFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    /**
     * ɾ�������ļ�
     *
     * @param fileName
     *            Ҫɾ�����ļ����ļ���
     * @return �����ļ�ɾ���ɹ�����true�����򷵻�false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // ����ļ�·������Ӧ���ļ����ڣ�������һ���ļ�����ֱ��ɾ��
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("ɾ�������ļ�" + fileName + "�ɹ���");
                return true;
            } else {
                System.out.println("ɾ�������ļ�" + fileName + "ʧ�ܣ�");
                return false;
            }
        } else {
            System.out.println("ɾ�������ļ�ʧ�ܣ�" + fileName + "�����ڣ�");
            return false;
        }
    }

    /**
     * ɾ��Ŀ¼��Ŀ¼�µ��ļ�
     *
     * @param dir
     *            Ҫɾ����Ŀ¼���ļ�·��
     * @return Ŀ¼ɾ���ɹ�����true�����򷵻�false
     */
    public static boolean deleteDirectory(String dir) {
        // ���dir�����ļ��ָ�����β���Զ�����ļ��ָ���
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // ���dir��Ӧ���ļ������ڣ����߲���һ��Ŀ¼�����˳�
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("ɾ��Ŀ¼ʧ�ܣ�" + dir + "�����ڣ�");
            return false;
        }
        boolean flag = true;
        // ɾ���ļ����е������ļ�������Ŀ¼
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // ɾ�����ļ�
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // ɾ����Ŀ¼
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("ɾ��Ŀ¼ʧ�ܣ�");
            return false;
        }
        // ɾ����ǰĿ¼
        if (dirFile.delete()) {
            System.out.println("ɾ��Ŀ¼" + dir + "�ɹ���");
            return true;
        } else {
            return false;
        }
    }



}
