package com.zhiren.common;

import java.io.File;
import java.io.FilenameFilter;
/*
 * 2009-04-16
 * 王磊
 * 增加filenamefilter的前缀过滤
 */
public class FileNameFilter implements FilenameFilter {
	String extendName;
	String prefixName;
	public FileNameFilter(String extendname) {
		// TODO 自动生成构造函数存根
		extendName = extendname;
	}
	public FileNameFilter(String prefixname,String extendname) {
		// TODO 自动生成构造函数存根
		prefixName = prefixname;
		extendName = extendname;
	}

	public boolean accept(File dir, String name) {
		// TODO 自动生成方法存根
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
