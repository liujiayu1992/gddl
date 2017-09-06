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
 * ����:tzf
 * ʱ��:2010-01-20
 * ����:�����νӶ���������
 */
public class Liucxjcl extends BasePage implements PageValidateListener {
	
	public static Class getLoadClass(String className){//�õ���Ҫ���ص���
		
		try {
			
			URL url=new URL(getLoadClassPath(className));
			ClassLoader load=new URLClassLoader(new URL[]{url},Thread.currentThread().getContextClassLoader());
			Class c=Class.forName(className,false,load);
			return c;
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		return  Liucxjcl.class;
	}
	
	public static Object getLoadObject(String className){//�õ���Ҫ���صĶ���
		
		try {
			
			URL url=new URL(getLoadClassPath(className));
			ClassLoader load=new URLClassLoader(new URL[]{url},Thread.currentThread().getContextClassLoader());
			Class c=Class.forName(className,false,load);
			return c.newInstance();
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} 
		
		return null;
	}
	
	public static void active(JDBCcon con,String TableName,long TableID,String xianjdz,String xianjl){//�����ִ࣬������ķ���
	
		Class c=getLoadClass(xianjl);
		
		try {
			Method method=c.getDeclaredMethod(xianjdz, new Class[]{JDBCcon.class,String.class,String.class});
			
			int modifier=method.getModifiers();
			
			if(Modifier.isStatic(modifier)){//��̬����
				method.invoke(null, new Object[]{con,TableName,TableID+""});
			}else{
				Object target=getLoadObject(xianjl);
				method.invoke(target, new Object[]{con,TableName,TableID+""});
				
			}
		
			
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} 
		
	}
	
	private void quxsh(JDBCcon con,String TableName,String TableID){//������ȡ�����
		
		StringBuffer bf=new StringBuffer(" begin \n");
		bf.append(" update fahb set hedbz="+SysConstant.HEDBZ_YJJ+" where id="+TableID+"; \n");
		bf.append(" update chepb set hedbz="+SysConstant.HEDBZ_YJJ+" where fahb_id="+TableID+";\n");
		bf.append(" end; ");
		
		con.getUpdate(bf.toString());
		
	}
	
	//���Եķ���
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

	private void UpdateZhillsbZT(JDBCcon con,String TableName,String TableID){//������ʱ��״̬
	
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
