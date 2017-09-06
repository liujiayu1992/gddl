package com.zhiren.zidy;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.zhiren.common.ExtensionFilter;
import com.zhiren.common.FileNameFilter;
import com.zhiren.common.Xml2DB;

public class TCRImport {

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFileChooser   chooser   =   new   JFileChooser();
		FileFilter xmlft = new ExtensionFilter("XML", ".xml");
		chooser.addChoosableFileFilter(xmlft);
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);//HERE   
        int   returnVal   =   chooser.showOpenDialog(chooser);   
        if(returnVal   ==   JFileChooser.APPROVE_OPTION)   { 
        	Date begin = new Date();
        	System.out.println(begin + "  ������̿�ʼ��");
        	File chooserFile = chooser.getSelectedFile();
    		TransFile(chooserFile);
    		Date end = new Date();
        	System.out.println(end + "  ������̽���������ʱ " + (end.getTime() - begin.getTime()) + " ���롣");
        }
        System.exit(0);
        System.out.print("���˳�");
	}
	
	public static void TransFile(File file){
		if(file.isFile()){
			FileNameFilter fnf = new FileNameFilter("xml");
			if(fnf.accept(file.getParentFile(), file.getName())){
				long begin  = (new Date()).getTime();
				TransForm(file);
				long end = (new Date()).getTime();
				System.out.println("���� ->" + file.getName() + " ��ʱ:" + (end - begin) + " ����;");
			}
    		return;
		}
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for(int i = 0; i < files.length ; i ++){
				TransFile(files[i]);
			}
		}
		return;
	}
	
	public static void TransForm(File xmlFile){
		Xml2DB xd = new Xml2DB(xmlFile);
    	xd.Transform();
	}

}
