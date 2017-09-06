package com.zhiren.common;

import java.io.File;
import java.io.FilenameFilter;
/*
 * 2009-04-16
 * ����
 * ����filenamefilter��ǰ׺����
 */
public class FileNameFilter implements FilenameFilter {
	String extendName;
	String prefixName;
	public FileNameFilter(String extendname) {
		// TODO �Զ����ɹ��캯�����
		extendName = extendname;
	}
	public FileNameFilter(String prefixname,String extendname) {
		// TODO �Զ����ɹ��캯�����
		prefixName = prefixname;
		extendName = extendname;
	}

	public boolean accept(File dir, String name) {
		// TODO �Զ����ɷ������
		if(prefixName !=null && !"".equals(prefixName)
				&& extendName != null && !"".equals(extendName)){
			return name.startsWith(prefixName) && name.endsWith("."+extendName);
		}else
			if(prefixName !=null && !"".equals(prefixName)){
				return name.startsWith(prefixName);
			}else
				if(extendName != null && !"".equals(extendName)){
					return name.endsWith("."+extendName);
				}else
					return name.indexOf(".") == -1;		
	}

}
