package com.zhiren.main.validate;

import com.zhiren.common.JDBCcon;

public class Validate {
//	�жϸ��û��Ƿ����
	public static boolean UserExists(String UserName,String id) {
		JDBCcon con = new JDBCcon();
		return con.getHasIt("select * from renyxxb where mingc='"+UserName+"' and id !="+id);
	}
}
