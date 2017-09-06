package com.zhiren.common;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.tapestry.IPage;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:tzf
 * 时间:2010-01-20
 * 内容:流程衔接动作处理类
 */
public class Liucxjcl extends BasePage implements PageValidateListener {
	
	public static Class getLoadClass(String className){//得到需要加载的类
		
		try {
			
			URL url=new URL(getLoadClassPath(className));
			ClassLoader load=new URLClassLoader(new URL[]{url},Thread.currentThread().getContextClassLoader());
			Class c=Class.forName(className,false,load);
			return c;
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return  Liucxjcl.class;
	}
	
	public static Object getLoadObject(String className){//得到需要加载的对象
		
		try {
			
			URL url=new URL(getLoadClassPath(className));
			ClassLoader load=new URLClassLoader(new URL[]{url},Thread.currentThread().getContextClassLoader());
			Class c=Class.forName(className,false,load);
			return c.newInstance();
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} 
		
		return null;
	}
	
	public static void active(JDBCcon con,String TableName,long TableID,String xianjdz,String xianjl){//加载类，执行所需的方法
	
		Class c=getLoadClass(xianjl);
		
		try {
			Method method=c.getDeclaredMethod(xianjdz, new Class[]{JDBCcon.class,String.class,String.class});
			
			int modifier=method.getModifiers();
			
			if(Modifier.isStatic(modifier)){//静态方法
				method.invoke(null, new Object[]{con,TableName,TableID+""});
			}else{
				Object target=getLoadObject(xianjl);
				method.invoke(target, new Object[]{con,TableName,TableID+""});
				
			}
		
			
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} 
		
	}
	
	private void quxsh(JDBCcon con,String TableName,String TableID){//发货表取消审核
		
		StringBuffer bf=new StringBuffer(" begin \n");
		bf.append(" update fahb set hedbz="+SysConstant.HEDBZ_YJJ+" where id="+TableID+"; \n");
		bf.append(" update chepb set hedbz="+SysConstant.HEDBZ_YJJ+" where fahb_id="+TableID+";\n");
		bf.append(" end; ");
		
		con.getUpdate(bf.toString());
		
	}
	
	//测试的方法
	private void tijfh(JDBCcon con,String TableName,String TableID){
//		System.out.println(MainGlobal.getAbsolutePath().getPath()+"***"+MainGlobal.getAbsolutePath().getAbsolutePath());
		Visit visit = (Visit)getPage().getVisit();
		con.getInsert("insert into aaa values(getNewID(" + visit.getDiancxxb_id() + "), 'tt', '')");
	}
	
	public static String getLoadClassPath(String classPath){
		
		String path="file:"+MainGlobal.getAbsolutePath().getPath()+"\\"+classPath.replaceAll("[.]", "\\\\")+".class";
//		System.out.println(path);
		return path;
	}

	private void UpdateZhillsbZT(JDBCcon con,String TableName,String TableID){//质量临时表状态
	
		String bf=" update "+TableName+" set zhuangt=3 where id="+TableID;
		
		con.getUpdate(bf.toString());
		
	}

	public void pageValidate(PageEvent arg0) {
		String PageName = arg0.getPage().getPageName();
		String ValPageName = Login.ValidateLogin(arg0.getPage());
		if (!PageName.equals(ValPageName)) {
			ValPageName = Login.ValidateAdmin(arg0.getPage());
			if (!PageName.equals(ValPageName)) {
				IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}
	}
}
