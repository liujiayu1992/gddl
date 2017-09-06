package com.zhiren.dc.huocfcj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Date;
import java.util.Random;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Huocfcj extends BasePage implements PageValidateListener {

	private static final String xit_mingc="��Ƥ������ֵ";//xitxxb�������ֶ�
	private static final String xit_leib="����";//xitxxb������ֶ�
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String Filename;
	
	public void setFilename(String filename){
		Filename=filename;
	}
	public String getFilename(){
		return Filename;
	}
	
	
	private String Pzljz;//Ƥ���ٽ�ֵ
	
	public void setPzljz(String pzljz){
		this.Pzljz=pzljz;
	}
	
	public String getPzljz(){
		
		String sql=" select x.zhi from xitxxb x  where x.mingc='"+this.xit_mingc+"' and x.leib='"+this.xit_leib+"' and  x.zhuangt=1";
		
		String zhi="35";//Ĭ��Ϊ35��
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=con.getResultSetList(sql);
		
		if(rsl.next()){
			zhi=rsl.getString("zhi");
		}
		return " ljz="+zhi+";";
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
		this.setFilename("");
		this.Pzljz="";
		
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {

		if (_SaveChick) {
			_SaveChick = false;
			Save();
			
		}
	}
	//����ļ�����
//	private String getFileName(){
//		String name="";
//		
//		Date date=new Date();
//		
//		name=DateUtil.Formatdate("yyyyMMddHHmmss", date);
//		return name;
//	}
//	
	
	private boolean Save_server(String filename){

		Visit visit=(Visit)this.getPage().getVisit();
		
		if(this.getChange()==null || this.getChange().equals("")){
			this.setMsg("û�����ݣ��޷�����!");
		}
		File filepath = new File(visit.getXitwjjwz()+"/shul/jianjwj");
		
		if(!filepath.exists()){
			filepath.mkdirs();
		}
		
		String fileName=filename;
		String strkeygen = fileName.substring(0,8);
		BigInteger keygen = BigInteger.valueOf(Long.parseLong(strkeygen));
		BigInteger bi;
		
		File file = new File(filepath,fileName+"A"); 
		FileWriter fw=null;
		PrintWriter pw=null;
		try{
			file.createNewFile();
			fw=new FileWriter(file);
		    pw=new PrintWriter(fw);
//			System.out.println(this.getChange());
			String[] rawRec=this.getChange().split(";");//�õ�ÿ�еļ�¼
			for(int i=0;i<rawRec.length;i++){
				String[] colRec=rawRec[i].split(",");//�õ�ÿ�еļ�¼ֵ  ë�� Ƥ��  �ٶ�  ��Ƥ��
				String str=Double.valueOf(colRec[1]).doubleValue()*1000+","+Double.valueOf(colRec[2]).doubleValue()*1000+","+colRec[3]+","+colRec[0];
				String sm="";
				
				for(int j=0;j<str.length();j++){
					bi=BigInteger.valueOf((long)str.charAt(j));
					
					sm+=" "+bi.xor(keygen).longValue()+" ";
				}
				
				pw.println(sm);
				
			}
			
			pw.close();
			fw.close();
			
			return true;
			
		}catch(Exception e){
			e.printStackTrace();
//			����ʧ�ܣ�ɾ�����½����ļ�
			file.delete();
			return false;
			
		}
		
		
		
	
	}

	//���� ����ɹ����   �ͻ����ļ������ݴ��ļ��� �� ������ʽ�ļ� 
	private boolean test(){
		Random rd=new Random();
		int a=rd.nextInt();
		System.out.println(a+"********");
		if(a%2==0){
			return true;
		}
		return false;
	}
	
	public void Save(){
		
//		String filename=this.getFileName();
		String filename=this.getFilename();
		
		boolean t1=this.Save_server(filename);
		
	//	t1=this.test();
		
		if(t1==false){
			this.setMsg("�ļ��޷�����!");
			return ;
		}
		
		//Ϊ�� ˵������ɹ�������ͻ��ı����ļ������Ƶ�temp�ݴ棬ɾ��������ʽ�ļ�
		this.setFilename("");
		this.setMsg("�ļ�����ɹ�!");
	}
	
	//ȡ���ݴ��ڱ���ָ��·���µ��ļ�����
	private String initData;
	
	public String getInitData(){
		
		String s=" gridDiv_data=[ ";
		String filepath="D:/zhiren/huocfcj/temp/huocfcj_temp";
		File file=new File(filepath);
//		File file = new File(visit.getXitwjjwz()+"/huocfcj/temp/huocfcj_temp");
		int flag=0;
		if(file.exists()){
			
			try {
				FileReader fr=new FileReader(file);
				BufferedReader br=new BufferedReader(fr);
				
				while(true){
					String readLine=br.readLine();
					if(readLine==null){//�Ѿ����� �˳�
						
						if(flag==0){
							return "";
						}
						break;
					}
					flag++;
					String[] Str_temp=readLine.split(",");
					
					s+="[";
					for(int i=0;i<Str_temp.length;i++){
						s+="'"+Str_temp[i]+"'";
						if(i!=Str_temp.length-1){
							s+=",";
						}
					}
					s+="],";
					
				}
				
				s=s.substring(0,s.lastIndexOf(","))+"];";
				return s;
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}else{
			return "";
		}
		
	}
	
	
	
	public void beginResponse(IMarkupWriter writer,IRequestCycle cycle){
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
		}
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

