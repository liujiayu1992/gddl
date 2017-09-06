package com.zhiren.main.validate;

import com.zhiren.common.JDBCcon;

public class Validate {
//	判断该用户是否存在
	public static boolean UserExists(String UserName,String id) {
		JDBCcon con = new JDBCcon();
		return con.getHasIt("select * from renyxxb where mingc='"+UserName+"' and id !="+id);
	}
}
