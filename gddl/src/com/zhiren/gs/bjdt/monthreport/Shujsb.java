package com.zhiren.gs.bjdt.monthreport;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.*;
import com.zhiren.common.tools.FtpCreatTxt;
import com.zhiren.common.tools.FtpUpload;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;


public class Shujsb extends BasePage implements PageValidateListener {
//	�����û���ʾ
	private String msg1="";
	public String getMsg() {
		return msg1;
	}
	public void setMsg(String msg) {
		this.msg1 = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null); 
	}
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	private boolean _ShangbClick = false;
	
	public void ShangbButton(IRequestCycle cycle) {
		_ShangbClick = true;
	}
	
//	��ť�¼�����

    
    private boolean _DiaorszChick = false;
    public void DiaorszButton(IRequestCycle cycle) {
    	_DiaorszChick = true;
    }
    private boolean _GonghdwszChick = false;
    public void GonghdwszButton(IRequestCycle cycle) {
    	_GonghdwszChick = true;
    }
    private boolean _ZhongnghdwszChick = false;
    public void ZhongnghdwszButton(IRequestCycle cycle) {
    	_ZhongnghdwszChick = true;
    }
    private boolean _DanwmcszChick = false;
    public void DanwmcszButton(IRequestCycle cycle) {
    	_DanwmcszChick = true;
    }
	public void submit(IRequestCycle cycle) {
//		Visit visit = (Visit) this.getPage().getVisit();

		if (_Refreshclick) {
			_Refreshclick = false;
			setRiq();
			getSelectData();
		}
		if (_ShangbClick) {
			_ShangbClick = false;
			Shangb();
			getSettings();
			getUploadFtp(Tablename);
			getSelectData();
		}
		
	}
	//**************************�ϱ�����*********************************//
	private void getSettings(){
		JDBCcon cn = new JDBCcon();
		try {
			ResultSet rs;
			rs=cn.getResultSet("select zhi from xitxxb where mingc='�ϼ��������û���'");
			while (rs.next()) {
				if (rs.getString("zhi")!=null ){
					ZhongNyh=rs.getString("zhi");
				};
			}
			rs.close();
			
			rs=cn.getResultSet("select zhi from xitxxb where mingc='�ϼ�����������'");
			while (rs.next()) {
				if (rs.getString("zhi")!=null ){
					ZhongNmm=rs.getString("zhi");
				};
			}
			rs.close();
			
			rs=cn.getResultSet("select zhi from xitxxb where mingc='�ϼ�������IP'");
			while (rs.next()) {
				if (rs.getString("zhi")!=null ){
					ZhongNip=rs.getString("zhi");
				};
			}
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.closeRs();
			cn.Close();
		}
	}
	
//	private String ZhongNyh="administrator";
//	private String ZhongNmm="980904";
//	private String ZhongNip="10.66.3.193";
	private String ZhongNyh="111700";
	private String ZhongNmm="111700";
	private String ZhongNip="219.141.254.214";
//	---------------------------------------------------------------------------
	private String Tablename="";//�ϴ��ļ����ļ���
	StringBuffer msg=new StringBuffer();//��¼�����ϴ��Ƿ�ɹ�
	
	
	public void Shangb(){				//�ϱ���ťִ�еĶ���

//		 ����������ݺ��·�������
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		
		String UploadFilename;
		String TxtName="c://Shujsc/";//�õ������ļ���
        if(!(new File("c://Shujsc")).isDirectory()){
            (new File("c://Shujsc")).mkdir();
        }
		
		Visit visit = (Visit) getPage().getVisit();

		JDBCcon con = new JDBCcon();
		ResultSet rrs=con.getResultSet("select bianm as diancbm ,mingc from diancxxb where id="+this.getTreeid());
		String diancbm="";
		try {
			if(rrs.next()){
			diancbm = rrs.getString("diancbm");
			}
			rrs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		StringBuffer tableName=new StringBuffer();
		con.setAutoCommit(false);
//		int flag=0;

		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());

		while (rsl.next()) {
			if(rsl.getString("baobmc").equals("����16_1��")){ 
		        
//				M������
//			    A�����ۼ�
				TxtName=getStrName(6,intMonth,diancbm,"M");//(�糧����,�ϱ���,�·�,����)
				msg.append(getdiaor16(diancbm,TxtName,"����",intyear,intMonth)+"\n");
				tableName.append(TxtName+",");
				TxtName=getStrName(6,intMonth,diancbm,"A");//(�糧����,�ϱ���,�·�,����)
				msg.append(getdiaor16(diancbm,TxtName,"�ۼ�",intyear,intMonth)+"\n");
				tableName.append(TxtName+",");
				
			}
		
			if(rsl.getString("baobmc").equals("��ȼ01��")){ 
			        
//				M������
//			    A�����ۼ�
				TxtName=getStrName(1,intMonth,diancbm,"M");//(�糧����,�ϱ���,�·�,����)
				msg.append(getdiaor01(diancbm,TxtName,"����",intyear,intMonth)+"\n");
				tableName.append(TxtName+",");
				TxtName=getStrName(1,intMonth,diancbm,"A");//(�糧����,�ϱ���,�·�,����)
				msg.append(getdiaor01(diancbm,TxtName,"�ۼ�",intyear,intMonth)+"\n");
				tableName.append(TxtName+",");
				
			}
			if(rsl.getString("baobmc").equals("��ȼ02��")){ 
//				M������
//			    A�����ۼ�
				TxtName=getStrName(2,intMonth,diancbm,"M");//(�糧����,�ϱ���,�·�,����)
				msg.append(getdiaor02(diancbm,TxtName,"����",intyear,intMonth)+"\n");
				tableName.append(TxtName+",");
				TxtName=getStrName(2,intMonth,diancbm,"A");//(�糧����,�ϱ���,�·�,����)
				msg.append(getdiaor02(diancbm,TxtName,"�ۼ�",intyear,intMonth)+"\n");
				tableName.append(TxtName+",");
			
			}
			if(rsl.getString("baobmc").equals("��ȼ03��")){ 
		        
//				M������
//			    A�����ۼ�
				TxtName=getStrName(3,intMonth,diancbm,"M");//(�糧����,�ϱ���,�·�,����)
				msg.append(getdiaor03(diancbm,TxtName,"����",intyear,intMonth)+"\n");
				tableName.append(TxtName+",");
				TxtName=getStrName(3,intMonth,diancbm,"A");//(�糧����,�ϱ���,�·�,����)
				msg.append(getdiaor03(diancbm,TxtName,"�ۼ�",intyear,intMonth)+"\n");
				tableName.append(TxtName+",");
				
			}if(rsl.getString("baobmc").equals("��ȼ04��")){ 
			        
//				M������
//			    A�����ۼ�
				TxtName=getStrName(4,intMonth,diancbm,"M");//(�糧����,�ϱ���,�·�,����)
				msg.append(getdiaor04(diancbm,TxtName,"����",intyear,intMonth)+"\n");
				tableName.append(TxtName+",");
				TxtName=getStrName(4,intMonth,diancbm,"A");//(�糧����,�ϱ���,�·�,����)
				msg.append(getdiaor04(diancbm,TxtName,"�ۼ�",intyear,intMonth)+"\n");
				tableName.append(TxtName+",");
				
			}if(rsl.getString("baobmc").equals("��ȼ08_1��")){ 
			        
//				M������
//			    A�����ۼ�
				TxtName=getStrName(8,intMonth,diancbm,"M");//(�糧����,�ϱ���,�·�,����)
				msg.append(getdiaor08(diancbm,TxtName,"����",intyear,intMonth)+"\n");
				tableName.append(TxtName+",");
				TxtName=getStrName(8,intMonth,diancbm,"A");//(�糧����,�ϱ���,�·�,����)
				msg.append(getdiaor08(diancbm,TxtName,"�ۼ�",intyear,intMonth)+"\n");
				tableName.append(TxtName+",");
				
			}
			if(rsl.getString("baobmc").equals("��ȼ08��")){
				//��д
			}
			
		}
	
		Tablename=tableName.toString();
		con.Close();
		msg.append("***************TxT�ļ����ɽ���***************"+"\n");
		
	}
	
	public String getdiaor16(String diancbm,String TxtName,String leix,long nianf,long yuef){
		if(diancbm==""){
			return "diaor16��"+leix+"û�����ϴ�����!";
		}else{
			String dc="";
			if(diancbm.equals("111700")){
				dc="";
			}else{
				dc=" and dc.bianm='"+diancbm+"'";
			}
			String diaor16bb="";
			StringBuffer sql= new StringBuffer();
			JDBCcon JDBCcon = new JDBCcon();
			FtpCreatTxt ct =new FtpCreatTxt();
			ct.CreatTxt("c://Shujsc/"+TxtName+".txt");//�����ļ�
			try {
	//			��Ȼ16
				String dr_16diancbm="";
				String dr_16kouj="";
				String dr_16bianm="";
				double dr_16shangyjc=0;
				double dr_16kuangfgyl=0;
				double dr_16kuid=0;
				double dr_16farl=0;
				double dr_16huif=0;
				double dr_16shuif=0;
				double dr_16huiff=0;
				double dr_16qitsrl=0;
				double dr_16shijhylhj=0;
				double dr_16shijhylfdy=0;
				double dr_16shijhylgry=0;
				double dr_16shijhylqty=0;
				double dr_16shijhylzcsh=0;
				double dr_16diaocl=0;
				double dr_16panypk=0;
				double dr_16yuemjc=0;

				StringBuffer diao16=new StringBuffer();
				
//				2009-01-13 lzq �ĳɷֳ��ֿ���ܵ�
//				sql.append("select dc.diancbm,'10' as kouj,mk.bianm,d.shangyjc,d.kuangfgyl,");
//				sql.append(" (d.kuid+d.yuns) as kuid,d.farl,d.huif,d.shuif,d.huiff,d.qitsrl,d.shijhylhj,d.shijhylfdy,");
//				sql.append(" d.shijhylgry,round(d.shijhylqty) as shijhylqty,d.shijhylzcsh,d.diaocl,d.panypk,d.yuemjc from diaor16bb d,diancxxb dc,meikdqb mk");
//				sql.append(" where d.diancxxb_id=dc.id and d.meikdqb_id=mk.id ");
//				sql.append(" and d.tongjrq=to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd') and d.dangyjlj='"+leix+"' "+dc);
				
				sql.append("select dc.bianm diancbm,'10' as kouj,dq.bianm bianm,sum(shangyjc) as shangyjc,\n" );
				sql.append("       sum(kuangfgyl) as kuangfgyl, sum(kuid+yuns) as kuid,\n" );
				sql.append("       (case when sum(kuangfgyl)>0 then round(sum(kuangfgyl * farl) /sum(kuangfgyl),2) else 0 end) as farl,\n" ); 
				sql.append("       (case when sum(kuangfgyl)>0 then round(sum(kuangfgyl * huif) /sum(kuangfgyl),2) else 0 end) as huif,\n" ); 
				sql.append("       (case when sum(kuangfgyl)>0 then round(sum(kuangfgyl * shuif) /sum(kuangfgyl),2) else 0 end) as shuif,\n" ); 
				sql.append("       (case when sum(kuangfgyl)>0 then round(sum(kuangfgyl * huiff) /sum(kuangfgyl),2) else 0 end) as huiff,\n" );
				sql.append("       sum(qitsrl) as qitsrl,sum(shijhylhj) as shijhylhj,sum(shijhylfdy) as shijhylfdy,\n" );
				sql.append("       sum(shijhylgry) as shijhylgry,sum(round(shijhylqty)) as shijhylqty,\n" );
				sql.append("       sum(SHIJHYLZCSH) as SHIJHYLZCSH,sum(diaocl) as diaocl,sum(panypk) as panypk,sum(yuemjc) as yuemjc\n" ); 
				sql.append("  from diaor16bb dr,gongysb dq,diancxxb dc\n" );
				sql.append(" where dr.riq=to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd')  and gongysb_id=dq.id(+)\n" ); 
				sql.append("   and dc.id=dr.diancxxb_id and dr.fenx='"+leix+"'  "+dc+"\n" );
				sql.append(" group by (dc.bianm,dq.bianm)\n" );
				sql.append(" order by dc.bianm,dq.bianm");

				
				ResultSetList rs = JDBCcon.getResultSetList(sql.toString());
				
				if(!rs.next()){
					 new File("c://Shujsc/"+TxtName+".txt").delete();//ɾ���ļ�
					 diaor16bb="����diaor16bb����ʧ��,û���ҵ�diaor16bb"+nianf+"��"+yuef+"�µ�'"+leix+"'��Ҫ����"+"\n";
				}else{
					rs.beforefirst();
					for (int i=0;rs.next();i++){
						diaor16bb="";
						diao16.setLength(0);
						dr_16diancbm=rs.getString("diancbm");
						dr_16kouj=rs.getString("kouj");
						dr_16bianm=rs.getString("bianm");
						dr_16shangyjc=rs.getDouble("shangyjc");
						dr_16kuangfgyl=rs.getDouble("kuangfgyl");
						dr_16kuid=rs.getDouble("kuid");
						dr_16farl=rs.getDouble("farl");
						dr_16huif=rs.getDouble("huif");
						dr_16shuif=rs.getDouble("shuif");
						dr_16huiff=rs.getDouble("huiff");
						dr_16qitsrl=rs.getDouble("qitsrl");
						dr_16shijhylhj=rs.getDouble("shijhylhj");
						dr_16shijhylfdy=rs.getDouble("shijhylfdy");
						dr_16shijhylgry=rs.getDouble("shijhylgry");
						dr_16shijhylqty=rs.getDouble("shijhylqty");
						dr_16shijhylzcsh=rs.getDouble("shijhylzcsh");
						dr_16diaocl=rs.getDouble("shijhylzcsh");
						dr_16panypk=rs.getDouble("panypk");
						dr_16yuemjc=rs.getDouble("yuemjc");
						
						diao16.append(getStr(6,dr_16diancbm));
						diao16.append(getStr(2,dr_16kouj));
						diao16.append(getStr(6,dr_16bianm));
						diao16.append(getNum(8,0,String.valueOf(Math.round(dr_16shangyjc))));
						diao16.append(getNum(10,0,String.valueOf(Math.round(dr_16kuangfgyl))));
						diao16.append(getNum(8,0,String.valueOf(Math.round(dr_16kuid))));
						diao16.append(getNum(6,2,String.valueOf(dr_16farl)));
						diao16.append(getNum(6,2,String.valueOf(dr_16huif)));
						diao16.append(getNum(6,2,String.valueOf(dr_16shuif)));
						diao16.append(getNum(6,2,String.valueOf(dr_16huiff)));
						diao16.append(getNum(8,0,String.valueOf(Math.round(dr_16qitsrl))));
						diao16.append(getNum(10,0,String.valueOf(Math.round(dr_16shijhylhj))));
						diao16.append(getNum(10,0,String.valueOf(Math.round(dr_16shijhylfdy))));
						diao16.append(getNum(8,0,String.valueOf(Math.round(dr_16shijhylgry))));
						diao16.append(getNum(8,0,String.valueOf(Math.round(dr_16shijhylqty))));
						diao16.append(getNum(8,0,String.valueOf(Math.round(dr_16shijhylzcsh))));
						diao16.append(getNum(8,0,String.valueOf(Math.round(dr_16diaocl))));
						diao16.append(getNum(8,0,String.valueOf(Math.round(dr_16panypk))));
						diao16.append(getNum(8,0,String.valueOf(Math.round(dr_16yuemjc))));
						//��������TXT��
						diaor16bb=diao16.toString();//�õ�һ����
						ct.aLine(diaor16bb);//д��������
					}
					diaor16bb="����diaor16bb"+nianf+"��"+yuef+"�µ�'"+leix+"'���ݳɹ�!"+"\n";
				}
				ct.finish();//�ر��������������ִӻ���д���ļ�
				rs.close();
				
			}catch (Exception e) {
				diaor16bb="����diaor16bb�������쳣,��������ʧ��!"+"\n";
				e.printStackTrace();
			} finally {
				JDBCcon.Close();
			}
			return diaor16bb;
		}
	}
	
	public String getdiaor01(String diancbm,String TxtName,String leix,long nianf,long yuef){
		if(diancbm==""){
			return "diaor01��"+leix+"û�����ϴ�����!";
		}else{
			String dc="";
			if(diancbm.equals("111700")){
				dc="";
			}else{
				dc=" and dc.bianm='"+diancbm+"'";
			}
			String diaor01bb="";
			StringBuffer sql= new StringBuffer();
			JDBCcon JDBCcon = new JDBCcon();
			FtpCreatTxt ct =new FtpCreatTxt();
			ct.CreatTxt("c://Shujsc/"+TxtName+".txt");//�����ļ�
			try {
	//			��Ȼ01
				String dr01_diancbm="";
				double dr01_fadsbrl =0;
				double dr01_meitsg =0;
				double dr01_meithyhj =0;
				double dr01_meithyfd =0;
				double dr01_meithygr =0;
				double dr01_meithyqt =0;
				double dr01_meithysh =0;
				double dr01_meitkc =0;
				double dr01_shiysg =0;
				double dr01_shiyhyhj =0;
				double dr01_shiyhyfd =0;
				double dr01_shiyhygr =0;
				double dr01_shiyhyqt =0;
				double dr01_shiyhysh =0;
				double dr01_shiykc =0;
				double dr01_fadl =0;
				double dr01_gongrl =0;
				double dr01_biaozmhfd =0;
				double dr01_biaozmhgr =0;
				double dr01_tianrmhfd =0;
				double dr01_tianrmhgr =0;
				double dr01_biaozmlfd =0;
				double dr01_biaozmlgr =0;
				double dr01_zonghrl =0;
				double dr01_zonghm =0;
				
				StringBuffer diao01=new StringBuffer();
				sql.append("select dc.bianm diancbm,d.fadsbrl,d.meitsg,d.meithyhj,d.meithyfd,d.meithygr,d.meithyqt,\n");
				sql.append(" d.meithysh,d.meitkc,d.shiysg,d.shiyhyhj,d.shiyhyfd,d.shiyhygr,d.shiyhyqt,\n");
				sql.append(" d.shiyhysh,d.shiykc,d.fadl,d.gongrl,d.biaozmhfd,d.biaozmhgr,d.tianrmhfd,d.tianrmhgr,\n");
				sql.append(" d.biaozmlfd,d.biaozmlgr,d.zonghrl,d.zonghm from diaor01bb d,diancxxb dc where d.diancxxb_id=dc.id\n");
				sql.append(" and d.riq=to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd') and d.fenx='"+leix+"' "+dc+"\n");
				ResultSetList rs = JDBCcon.getResultSetList(sql.toString());
				if(!rs.next()){
					 new File("c://Shujsc/"+TxtName+".txt").delete();//ɾ���ļ�
					 diaor01bb="����diaor01bb����ʧ��,û���ҵ�diaor01bb"+nianf+"��"+yuef+"�µ�'"+leix+"'��Ҫ����"+"\n";
				}else{
				        rs.beforefirst();
					for (int i=0;rs.next();i++){
						diao01.setLength(0);
						diaor01bb="";
						dr01_diancbm=rs.getString("diancbm");
						dr01_fadsbrl =rs.getDouble("fadsbrl");
						dr01_meitsg =rs.getDouble("meitsg");
						dr01_meithyhj =rs.getDouble("meithyhj");
						dr01_meithyfd =rs.getDouble("meithyfd");
						dr01_meithygr =rs.getDouble("meithygr");
						dr01_meithyqt =rs.getDouble("meithyqt");
						dr01_meithysh =rs.getDouble("meithysh");
						dr01_meitkc =rs.getDouble("meitkc");
						if(leix.equals("�ۼ�")){
							dr01_meitkc=0;
						}
						dr01_shiysg =rs.getDouble("shiysg");
						dr01_shiyhyhj =rs.getDouble("shiyhyhj");
						dr01_shiyhyfd =rs.getDouble("shiyhyfd");
						dr01_shiyhygr =rs.getDouble("shiyhygr");
						dr01_shiyhyqt =rs.getDouble("shiyhyqt");
						dr01_shiyhysh =rs.getDouble("shiyhysh");
						dr01_shiykc =rs.getDouble("shiykc");
						if(leix.equals("�ۼ�")){
							dr01_shiykc=0;
						}
						dr01_fadl =rs.getDouble("fadl");
						dr01_gongrl =rs.getDouble("gongrl");
						dr01_biaozmhfd =rs.getDouble("biaozmhfd");
						dr01_biaozmhgr =rs.getDouble("biaozmhgr");
						dr01_tianrmhfd =rs.getDouble("tianrmhfd");
						dr01_tianrmhgr =rs.getDouble("tianrmhgr");
						dr01_biaozmlfd =rs.getDouble("biaozmlfd");
						dr01_biaozmlgr =rs.getDouble("biaozmlgr");
						dr01_zonghrl =rs.getDouble("zonghrl");
						dr01_zonghm =rs.getDouble("zonghm");
						
						diao01.append(getStr(6,dr01_diancbm));
						diao01.append(getNum(8,0,String.valueOf(Math.round(dr01_fadsbrl))));
						diao01.append(getNum(10,0,String.valueOf(Math.round(dr01_meitsg))));
						diao01.append(getNum(10,0,String.valueOf(Math.round(dr01_meithyhj))));
						diao01.append(getNum(10,0,String.valueOf(Math.round(dr01_meithyfd))));
						diao01.append(getNum(8,0,String.valueOf(Math.round(dr01_meithygr))));
						diao01.append(getNum(8,0,String.valueOf(Math.round(dr01_meithyqt))));
						diao01.append(getNum(8,0,String.valueOf(Math.round(dr01_meithysh))));
						diao01.append(getNum(8,0,String.valueOf(Math.round(dr01_meitkc))));
						diao01.append(getNum(8,0,String.valueOf(Math.round(dr01_shiysg))));
						diao01.append(getNum(8,0,String.valueOf(Math.round(dr01_shiyhyhj))));
						diao01.append(getNum(8,0,String.valueOf(Math.round(dr01_shiyhyfd))));
						diao01.append(getNum(6,0,String.valueOf(Math.round(dr01_shiyhygr))));
						diao01.append(getNum(6,0,String.valueOf(Math.round(dr01_shiyhyqt))));
						diao01.append(getNum(6,0,String.valueOf(Math.round(dr01_shiyhysh))));
						diao01.append(getNum(8,0,String.valueOf(Math.round(dr01_shiykc))));
						diao01.append(getNum(12,2,String.valueOf(dr01_fadl)));
						diao01.append(getNum(10,0,String.valueOf(Math.round(dr01_gongrl))));
						diao01.append(getNum(4,0,String.valueOf(Math.round(dr01_biaozmhfd))));
						diao01.append(getNum(6,2,String.valueOf(dr01_biaozmhgr)));
						diao01.append(getNum(4,0,String.valueOf(Math.round(dr01_tianrmhfd))));
						diao01.append(getNum(6,2,String.valueOf(dr01_tianrmhgr)));
						diao01.append(getNum(10,0,String.valueOf(Math.round(dr01_biaozmlfd))));
						diao01.append(getNum(8,0,String.valueOf(Math.round(dr01_biaozmlgr))));
						diao01.append(getNum(6,2,String.valueOf(dr01_zonghrl)));
						diao01.append(getNum(6,2,String.valueOf(dr01_zonghm)));
						
						//��������TXT��
						diaor01bb=diao01.toString();//�õ�һ����
						
						ct.aLine(diaor01bb);//д��������
					}
					ct.finish();//�ر��������������ִӻ���д���ļ�
					rs.close();
					diaor01bb="����diaor01bb"+nianf+"��"+yuef+"�µ�'"+leix+"'���ݳɹ�!"+"\n";
				}
			}catch (Exception e) {
				diaor01bb="����diaor01bb�������쳣!"+"\n";
				e.printStackTrace();
			} finally {
				JDBCcon.Close();
			}
			return diaor01bb;
		}
	}
	
	public String getdiaor02(String diancbm,String TxtName,String leix,long nianf,long yuef){
		if(diancbm==""){
			return "diaor02��"+leix+"û�����ϴ�����!";
		}else{
			String dc="";
			if(diancbm.equals("111700")){
				dc="";
			}else{
				dc=" and dc.bianm='"+diancbm+"'";
			}
			String diaor02bb="";
//			StringBuffer sql= new StringBuffer();
			JDBCcon JDBCcon = new JDBCcon();
			FtpCreatTxt ct =new FtpCreatTxt();
			ct.CreatTxt("c://Shujsc/"+TxtName+".txt");//�����ļ�
			try {
	//			��Ȼ16
				String dr_02diancbm="";
				String dr_02kouj="";
				String dr_02bianm="";
				double dr_02jih=0;
				double dr_02shij=0;
				
				if(leix.equals("����")){//����	
					StringBuffer diao02=new StringBuffer();
					String sql="";
					sql="select dc.bianm diancbm,'10' as kouj,dq.bianm bianm,jb.jih,sb.shis\n" +
					"  from diancxxb dc,gongysb dq,dianclbb lx,dianckjpxb px,\n" + 
					"       (select diancxxb_id,gongysb_id from diaor16bb\n" + 
					"         where riq = to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd') and fenx = '�ۼ�'\n" + 
					"        union\n" + 
					"        select jh.diancxxb_id,fu.id as gongysb_id from niancgjhb jh,gongysb gy,gongysb fu \n" + 
					"         where riq = to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd') and jh.gongysb_id=gy.id and substr(gy.bianm,0,6)=fu.bianm ) a,\n" + 
					"       (select diancxxb_id,gongysb_id,sum(nvl(hej,0)*10000) as jih from niancgjhb jh\n" + 
					"         where riq = to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd')\n" + 
					"         group by(diancxxb_id,gongysb_id)) jb,\n" + 
					"       (select diancxxb_id,gongysb_id,sum(nvl(hej,0)*10000) as jih from niancgjhb jh\n" + 
					"         where riq> = to_date('"+nianf+"-01-01', 'yyyy-mm-dd')  and riq<=to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd')\n" + 
					"         group by(diancxxb_id,gongysb_id)) jl,\n" + 
					"       (select diancxxb_id,gongysb_id, sum(kuangfgyl) as shis from diaor16bb\n" + 
					"         where riq = to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd') and fenx = '����'\n" + 
					"         group by diancxxb_id,gongysb_id) sb,\n" + 
					"       (select diancxxb_id,gongysb_id, sum(kuangfgyl) as shis from diaor16bb\n" + 
					"         where riq = to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd') and fenx = '�ۼ�'\n" + 
					"         group by diancxxb_id,gongysb_id) sl\n" + 
					" where dc.id = a.diancxxb_id and dq.id = a.gongysb_id and dc.dianclbb_id=lx.id and dc.id=px.diancxxb_id\n" + 
					"   and a.diancxxb_id = sb.diancxxb_id(+) and a.diancxxb_id = sl.diancxxb_id(+)\n" + 
					"   and a.diancxxb_id = jb.diancxxb_id(+) and a.diancxxb_id = jl.diancxxb_id(+)\n" + 
					"   and a.gongysb_id = sb.gongysb_id(+) and a.gongysb_id = sl.gongysb_id(+)\n" + 
					"   and a.gongysb_id = jb.gongysb_id(+) and a.gongysb_id = jl.gongysb_id(+)and px.kouj='�±�'"+dc;
					
					ResultSetList rs = JDBCcon.getResultSetList(sql);
					
					if(!rs.next()){
						 new File("c://Shujsc/"+TxtName+".txt").delete();//ɾ���ļ�
						 diaor02bb="����diaor02bb����ʧ��,û���ҵ�diaor02bb"+nianf+"��"+yuef+"�µ�'"+leix+"'��Ҫ����"+"\n";
					}else{
						rs.beforefirst();
						for (int i=0;rs.next();i++){
							diaor02bb="";
							diao02.setLength(0);
							dr_02diancbm=rs.getString("diancbm");
							dr_02kouj=rs.getString("kouj");
							dr_02bianm=rs.getString("bianm");
							dr_02jih=rs.getDouble("jih");
							dr_02shij=rs.getDouble("shis");

							//��������TXT��
							diao02.append(getStr(6,dr_02diancbm));
							diao02.append(getStr(2,dr_02kouj));
							diao02.append(getStr(6,dr_02bianm));
							diao02.append(getNum(10,0,String.valueOf(Math.round(dr_02jih))));
							diao02.append(getNum(10,0,String.valueOf(Math.round(dr_02shij))));
							
							diaor02bb=diao02.toString();//�õ�һ����
							ct.aLine(diaor02bb);//д��������
						}
						diaor02bb="����diaor02bb"+nianf+"��"+yuef+"�µ�'"+leix+"'���ݳɹ�!"+"\n";
					}
					ct.finish();//�ر��������������ִӻ���д���ļ�
					rs.close();
				}else{//�ۼ�
					StringBuffer diao02=new StringBuffer();
					String sql="";
					sql="select dc.bianm diancbm,'10' as kouj,dq.bianm bianm,jl.jih,sl.shis\n" +
					"  from diancxxb dc,gongysb dq,dianclbb lx,dianckjpxb px,\n" + 
					"       (select diancxxb_id,gongysb_id from diaor16bb\n" + 
					"         where riq = to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd') and fenx = '�ۼ�'\n" + 
					"        union\n" + 
					"        select jh.diancxxb_id,jh.gongysb_id from niancgjhb jh\n" + 
					"         where riq = to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd')) a,\n" + 
					"       (select diancxxb_id,gongysb_id,sum(nvl(hej,0)*10000) as jih from niancgjhb jh\n" + 
					"         where riq = to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd')\n" + 
					"         group by(diancxxb_id,gongysb_id)) jb,\n" + 
					"       (select diancxxb_id,gongysb_id,sum(nvl(hej,0)*10000) as jih from niancgjhb jh\n" + 
					"         where riq> = to_date('"+nianf+"-01-01', 'yyyy-mm-dd')  and riq<=to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd')\n" + 
					"         group by(diancxxb_id,gongysb_id)) jl,\n" + 
					"       (select diancxxb_id,gongysb_id, sum(kuangfgyl) as shis from diaor16bb\n" + 
					"         where  riq= to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd') and fenx = '����'\n" + 
					"         group by diancxxb_id,gongysb_id) sb,\n" + 
					"       (select diancxxb_id,gongysb_id, sum(kuangfgyl) as shis from diaor16bb\n" + 
					"         where riq = to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd') and fenx = '�ۼ�'\n" + 
					"         group by diancxxb_id,gongysb_id) sl\n" + 
					" where dc.id = a.diancxxb_id and dq.id = a.gongysb_id and dc.dianclbb_id=lx.id and dc.id=px.diancxxb_id\n" + 
					"   and a.diancxxb_id = sb.diancxxb_id(+) and a.diancxxb_id = sl.diancxxb_id(+)\n" + 
					"   and a.diancxxb_id = jb.diancxxb_id(+) and a.diancxxb_id = jl.diancxxb_id(+)\n" + 
					"   and a.gongysb_id = sb.gongysb_id(+) and a.gongysb_id = sl.gongysb_id(+)\n" + 
					"   and a.gongysb_id = jb.gongysb_id(+) and a.gongysb_id = jl.gongysb_id(+)and px.kouj='�±�'"+dc;
					
					ResultSet rs = JDBCcon.getResultSet(sql);
					
					if(!rs.next()){
						 new File("c://Shujsc/"+TxtName+".txt").delete();//ɾ���ļ�
						 diaor02bb="����diaor02bb����ʧ��,û���ҵ�diaor02bb"+nianf+"��"+yuef+"�µ�'"+leix+"'��Ҫ����"+"\n";
					}else{
						rs.beforeFirst();
						for (int i=0;rs.next();i++){
							diaor02bb="";
							diao02.setLength(0);
							dr_02diancbm=rs.getString("diancbm");
							dr_02kouj=rs.getString("kouj");
							dr_02bianm=rs.getString("bianm");
							dr_02jih=rs.getDouble("jih");
							dr_02shij=rs.getDouble("shis");

							//��������TXT��
							diao02.append(getStr(6,dr_02diancbm));
							diao02.append(getStr(2,dr_02kouj));
							diao02.append(getStr(6,dr_02bianm));
							diao02.append(getNum(10,0,String.valueOf(Math.round(dr_02jih))));
							diao02.append(getNum(10,0,String.valueOf(Math.round(dr_02shij))));
							
							diaor02bb=diao02.toString();//�õ�һ����
							ct.aLine(diaor02bb);//д��������
						}
						diaor02bb="����diaor02bb"+nianf+"��"+yuef+"�µ�'"+leix+"'���ݳɹ�!"+"\n";
					}
					ct.finish();//�ر��������������ִӻ���д���ļ�
					rs.close();
				}
			}catch (Exception e) {
				diaor02bb="����diaor02bb�������쳣,��������ʧ��!"+"\n";
				e.printStackTrace();
			} finally {
				JDBCcon.Close();
			}
			return diaor02bb;
		}
	}
	
	public String getdiaor03(String diancbm,String TxtName,String leix,long nianf,long yuef){
		if(diancbm==""){
			return "diaor03��"+leix+"û�����ϴ�����!";
		}else{
			String dc="";
			if(diancbm.equals("111700")){
				dc="";
			}else{
				dc=" and dc.bianm='"+diancbm+"'";
			}
			String diaor03bb="";
			StringBuffer sql= new StringBuffer();
			JDBCcon JDBCcon = new JDBCcon();
			FtpCreatTxt ct =new FtpCreatTxt();
			ct.CreatTxt("c://Shujsc/"+TxtName+".txt");//�����ļ�
			try {
	//			��Ȼ03
				String dr03_diancbm="";
				String dr03_bianm="";
				double dr03_jincsl =0;
				double dr03_choucsl =0;
				double dr03_zhanjcm =0;
				double dr03_guoh =0;
				double dr03_jianc =0;
				double dr03_yingdsl =0;
				double dr03_yingdzje =0;
				double dr03_kuid =0;
				double dr03_kuidzje =0;
				double dr03_suopje =0;
				
				StringBuffer diao03=new StringBuffer();
//				sql.append("select dc.diancbm,mk.bianm,d.jincsl,d.choucsl,d.zhanjcm,d.guoh,d.jianc,d.yingdsl,");
//				sql.append(" d.yingdzje,d.kuid,d.kuidzje,d.suopje from diaor03bb d,diancxxb dc,meikdqb mk ");
//				sql.append(" where d.diancxxb_id=dc.id and d.meikdqb_id=mk.id");
//				sql.append(" and d.tongjrq=to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd') and d.dangyjlj='"+leix+"' "+dc);
				
				
				sql.append("select dc.bianm as diancbm,gongysb.bianm as bianm,\n");
				sql.append("       sum(jincsl) as jincsl, sum(choucsl) as choucsl,\n");
				sql.append("       decode(sum(jincsl),0,0,round(sum(choucsl)/sum(jincsl)*100,2)) as zhanjcm ,\n");
				sql.append("       (case when sum(jincsl)>0 then round(sum(jincsl * guoh) /sum(jincsl),2) else 0 end) as guoh,\n");
				sql.append("       100-(case when sum(jincsl)>0 then round(sum(jincsl * guoh) /sum(jincsl),2) else 0 end) as jianc,\n"); 
				sql.append("       sum(yingdsl) as yingdsl,  sum(yingdzje) as yingdzje,\n");
				sql.append("       sum(kuid) as kuid, sum(kuidzje) as kuidzje,sum(suopje) as suopje\n");
				sql.append("  from diaor03bb,gongysb,diancxxb dc\n");
				sql.append(" where dc.id=diaor03bb.diancxxb_id and riq=to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd')\n");
				sql.append("   and gongysb_id=gongysb.id(+) and diaor03bb.fenx='"+leix+"'  "+dc+"  \n" );
				sql.append("group by (dc.bianm,gongysb.bianm) order by dc.bianm,gongysb.bianm");

				ResultSet rs = JDBCcon.getResultSet(sql.toString());
				if(!rs.next()){
					 new File("c://Shujsc/"+TxtName+".txt").delete();//ɾ���ļ�
					 diaor03bb="����diaor03bb����ʧ��,û���ҵ�diaor03bb"+nianf+"��"+yuef+"�µ�'"+leix+"'��Ҫ����"+"\n";
				}else{
					rs.beforeFirst();
					for (int i=0;rs.next();i++){
						diao03.setLength(0);
						diaor03bb="";
						dr03_diancbm=rs.getString("diancbm");
						dr03_bianm=rs.getString("bianm");
						dr03_jincsl =rs.getDouble("jincsl");
						dr03_choucsl =rs.getDouble("choucsl");
						dr03_zhanjcm =rs.getDouble("zhanjcm");
						dr03_guoh =rs.getDouble("guoh");
						dr03_jianc =rs.getDouble("jianc");
						dr03_yingdsl =rs.getDouble("yingdsl");
						dr03_yingdzje =rs.getDouble("yingdzje");
						dr03_kuid =rs.getDouble("kuid");
						dr03_kuidzje =rs.getDouble("kuidzje");
						dr03_suopje =rs.getDouble("suopje");
						
						diao03.append(getStr(6,dr03_diancbm));
						diao03.append(getStr(6,dr03_bianm));
						diao03.append(getNum(10,0,String.valueOf(Math.round(dr03_jincsl))));
						diao03.append(getNum(10,0,String.valueOf(Math.round(dr03_choucsl))));
						diao03.append(getNum(6,2,String.valueOf(dr03_zhanjcm)));
						diao03.append(getNum(6,2,String.valueOf(dr03_guoh)));
						diao03.append(getNum(6,2,String.valueOf(dr03_jianc)));
						diao03.append(getNum(10,0,String.valueOf(Math.round(dr03_yingdsl))));
						diao03.append(getNum(10,0,String.valueOf(Math.round(dr03_yingdzje))));
						diao03.append(getNum(10,0,String.valueOf(Math.round(dr03_kuid))));
						diao03.append(getNum(10,0,String.valueOf(Math.round(dr03_kuidzje))));
						diao03.append(getNum(10,0,String.valueOf(Math.round(dr03_suopje))));
						
						//��������TXT��
						diaor03bb=diao03.toString();//�õ�һ����
						
						ct.aLine(diaor03bb);//д��������
					}
					ct.finish();//�ر��������������ִӻ���д���ļ�
					rs.close();
					diaor03bb="����diaor03bb"+nianf+"��"+yuef+"�µ�'"+leix+"'���ݳɹ�!"+"\n";
				}
			}catch (Exception e) {
				diaor03bb="����diaor03bb�������쳣!"+"\n";
				e.printStackTrace();
			} finally {
				JDBCcon.Close();
			}
			return diaor03bb;
		}
	}
	
	public String getdiaor04(String diancbm,String TxtName,String leix,long nianf,long yuef){
		if(diancbm==""){
			return "diaor04��"+leix+"û�����ϴ�����!";
		}else{
			String dc="";
			if(diancbm.equals("111700")){
				dc="";
			}else{
				dc=" and dc.bianm='"+diancbm+"'";
			}
			String diaor04bb="";
			StringBuffer sql= new StringBuffer();
			JDBCcon JDBCcon = new JDBCcon();
			FtpCreatTxt ct =new FtpCreatTxt();
			ct.CreatTxt("c://Shujsc/"+TxtName+".txt");//�����ļ�
			try {
	//			��Ȼ04
				String dr04_diancbm="";
				String dr04_bianm="";
				double dr04_jincsl =0;
				double dr04_yanssl =0;
				double dr04_jianzl =0;
				double dr04_kuangffrl =0;
				double dr04_kuangfdj =0;
				double dr04_kuangfsf =0;
				double dr04_kuangfhf =0;
				double dr04_kuangfhff =0;
				double dr04_kuangflf =0;
				double dr04_changffrl =0;
				double dr04_changfdj =0;
				double dr04_changfsf =0;
				double dr04_changfhf =0;
				double dr04_changfhff =0;
				double dr04_changflf =0;
				double dr04_dengjc =0;
				double dr04_rejc =0;
				double dr04_bufl =0;
				double dr04_danjc =0;
				double dr04_zongje =0;
				double dr04_suopje=0;
				
				
				StringBuffer diao04=new StringBuffer();
//				sql.append("select dc.diancbm,mk.bianm,d.jincsl,d.yanssl,d.jianzl,d.kuangffrl,d.kuangfdj,");
//				sql.append(" d.kuangfsf,d.kuangfhf,d.kuangfhff,d.kuangflf,d.changffrl,d.changfdj,d.changfsf,");
//				sql.append(" d.changfhf,d.changfhff,d.changflf,d.dengjc,d.rejc,d.bufl,d.danjc,d.zongje,d.suopje");
//				sql.append(" from diaor04bb d,diancxxb dc,meikdqb mk where d.diancxxb_id=dc.id and d.meikdqb_id=mk.id");
//				sql.append(" and d.tongjrq=to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd') and d.dangyjlj='"+leix+"' "+dc);
				
				
				sql.append("select  diancbm, bianm, JINCSL,YANSSL,\n");
				sql.append("    decode(JINCSL,0,0,round(YANSSL/JINCSL*100,2)) as jianzl,\n"); 
				sql.append("    decode(KUANGFFRLsl,0,0,round(KUANGFFRL/KUANGFFRLsl,2)) as kuangffrl,\n"); 
				sql.append("    dengji(decode(KUANGFFRLsl,0,0,round(KUANGFFRL/KUANGFFRLsl,2))) as kuangfdj,\n"); 
				sql.append("    decode(KUANGFSFsl,0, 0,round(KUANGFSF/KUANGFSFsl,2)) as kuangfsf,\n"); 
				sql.append("    decode(KUANGFHFsl,0,0 ,round(KUANGFHF/KUANGFHFsl,2)) as kuangfhf,\n"); 
				sql.append("    decode(KUANGFHFFsl,0,0,round(KUANGFHFF/KUANGFHFFsl,2)) as kuangfhff,\n"); 
				sql.append("    decode(KUANGFLFsl,0,0,round(KUANGFLF/KUANGFLFsl,2)) as kuangflf,\n");
				sql.append("    decode(JINCSL,0,0,round(FARL/JINCSL,2)) as changffrl,\n"); 
				sql.append("    dengji(decode(JINCSL,0,0,round(FARL/JINCSL,2))) as changfdj,\n"); 
				sql.append("    decode(JINCSL,0,0,round(SHUIF/JINCSL,2)) as changfsf,\n"); 
				sql.append("    decode(JINCSL,0,0,round(HUIF/JINCSL,2)) as changfhf,\n"); 
				sql.append("    decode(JINCSL,0,0,round(HUIFF/JINCSL,2)) as changfhff,\n"); 
				sql.append("    decode(JINCSL,0,0,round(LIUF/JINCSL,2)) as changflf,\n"); 
				sql.append("    (dengji(decode(JINCSL,0,0,round(FARL/JINCSL,2)))-dengji(decode(KUANGFFRLsl,0,0,round(KUANGFFRL/KUANGFFRLsl,2))))*2 as dengjc,\n"); 
				sql.append("    decode(JINCSL,0,0,round(FARL/JINCSL,2))-decode(KUANGFFRLsl,0,0,round(KUANGFFRL/KUANGFFRLsl,2)) as rejc,\n");
				sql.append("    decode(JINCSL,0,0,round(BUFL/JINCSL,2)) as bufl,\n"); 
				sql.append("    decode(BUFL,0,0,round(DANJC/BUFL,2)) as danjc,\n"); 
				sql.append("    ZONGJE as zongje,SUOPJE as suopje\n"); 
				sql.append("from (\n"); 
				sql.append("     select dc.bianm as diancbm,q.bianm as bianm,sum(JINCSL) as JINCSL,sum(YANSSL) as YANSSL ,\n"); 
				sql.append("        sum(JINCSL * KUANGFFRL) as KUANGFFRL,sum(decode(nvl(KUANGFFRL,0),0,0,JINCSL)) as KUANGFFRLsl,\n");
				sql.append("        sum(JINCSL * KUANGFSF)as KUANGFSF,   sum(decode(nvl(KUANGFSF,0),0,0,JINCSL))  as KUANGFSFsl,\n");
				sql.append("        sum(JINCSL * KUANGFHF) as KUANGFHF,  sum(decode(nvl(KUANGFHF,0),0,0,JINCSL))  as KUANGFHFsl,\n"); 
				sql.append("        sum(JINCSL * KUANGFHFF) as KUANGFHFF,sum(decode(nvl(KUANGFHFF,0),0,0,JINCSL)) as KUANGFHFFsl,\n");
				sql.append("        sum(JINCSL * KUANGFLF) as KUANGFLF,  sum(decode(nvl(KUANGFLF,0),0,0,JINCSL))  as KUANGFLFsl,\n"); 
				sql.append("        sum(JINCSL * CHANGFFRL) as FARL,sum(JINCSL * CHANGFSF) as SHUIF,sum(JINCSL * CHANGFHF) as HUIF,\n");
				sql.append("        sum(JINCSL * CHANGFHFF) as HUIFF,sum(JINCSL * CHANGFLF) as LIUF, sum(JINCSL * BUFL) as BUFL,\n"); 
				sql.append("        sum(JINCSL * DANJC*BUFL) as DANJC,sum(ZONGJE) as ZONGJE,sum(SUOPJE) as SUOPJE,\n");
				sql.append("        sum(RELSP) as RELSP,sum(KUIKSPSL) as KUIKSPSL,sum(LIUSP) as LIUSP,sum(LIUSPSL)  as LIUSPSL\n"); 
				sql.append("   from diaor04bb b,gongysb q,diancxxb dc\n"); 
				sql.append("  where riq=to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd') and gongysb_id=q.id(+)\n"); 
				sql.append("    and dc.id=b.diancxxb_id and b.fenx='"+leix+"'  "+dc+"  \n");
				sql.append("   group by (dc.bianm,q.bianm) order by dc.bianm,q.bianm )");
				
				
//				System.out.println(sql.toString());
				ResultSet rs = JDBCcon.getResultSet(sql.toString());
				if(!rs.next()){
					 new File("c://Shujsc/"+TxtName+".txt").delete();//ɾ���ļ�
					 diaor04bb="����diaor04bb����ʧ��,û���ҵ�diaor04bb"+nianf+"��"+yuef+"�µ�'"+leix+"'��Ҫ����"+"\n";
				}else{
				  rs.beforeFirst();
					for (int i=0;rs.next();i++){
						diao04.setLength(0);
						diaor04bb="";
						dr04_diancbm=rs.getString("diancbm");
						dr04_bianm=rs.getString("bianm");
						dr04_jincsl =rs.getDouble("jincsl");
						dr04_yanssl =rs.getDouble("yanssl");
						dr04_jianzl =rs.getDouble("jianzl");
						dr04_kuangffrl =rs.getDouble("kuangffrl");
						dr04_kuangfdj =rs.getDouble("kuangfdj");
						dr04_kuangfsf =rs.getDouble("kuangfsf");
						dr04_kuangfhf =rs.getDouble("kuangfhf");
						dr04_kuangfhff =rs.getDouble("kuangfhff");
						dr04_kuangflf =rs.getDouble("kuangflf");
						dr04_changffrl =rs.getDouble("changffrl");
						dr04_changfdj =rs.getDouble("changfdj");
						dr04_changfsf =rs.getDouble("changfsf");
						dr04_changfhf =rs.getDouble("changfhf");
						dr04_changfhff =rs.getDouble("changfhff");
						dr04_changflf =rs.getDouble("changflf");
						dr04_dengjc =rs.getDouble("dengjc");
						dr04_rejc =rs.getDouble("rejc");
						dr04_bufl =rs.getDouble("bufl");
						dr04_danjc =rs.getDouble("danjc");
						dr04_zongje =rs.getDouble("zongje");
						dr04_suopje=rs.getDouble("suopje");
						
						diao04.append(getStr(6,dr04_diancbm));
						diao04.append(getStr(6,dr04_bianm));
						diao04.append(getNum(10,0,String.valueOf(Math.round(dr04_jincsl))));
						diao04.append(getNum(10,0,String.valueOf(Math.round(dr04_yanssl))));
						diao04.append(getNum(6,2,String.valueOf(dr04_jianzl)));
						diao04.append(getNum(5,2,String.valueOf(dr04_kuangffrl)));
						diao04.append(getNum(4,1,String.valueOf(dr04_kuangfdj)));
						diao04.append(getNum(5,2,String.valueOf(dr04_kuangfsf)));
						diao04.append(getNum(5,2,String.valueOf(dr04_kuangfhf)));
						diao04.append(getNum(5,2,String.valueOf(dr04_kuangfhff)));
						diao04.append(getNum(5,2,String.valueOf(dr04_kuangflf)));
						diao04.append(getNum(5,2,String.valueOf(dr04_changffrl)));
						diao04.append(getNum(4,1,String.valueOf(dr04_changfdj)));
						diao04.append(getNum(5,2,String.valueOf(dr04_changfsf)));
						diao04.append(getNum(5,2,String.valueOf(dr04_changfhf)));
						diao04.append(getNum(5,2,String.valueOf(dr04_changfhff)));
						diao04.append(getNum(5,2,String.valueOf(dr04_changflf)));
						diao04.append(getNum(5,1,String.valueOf(dr04_dengjc)));
						diao04.append(getNum(6,2,String.valueOf(dr04_rejc)));
						diao04.append(getNum(6,2,String.valueOf(dr04_bufl)));
						diao04.append(getNum(6,2,String.valueOf(dr04_danjc)));
						diao04.append(getNum(10,0,String.valueOf(Math.round(dr04_zongje))));
						diao04.append(getNum(10,0,String.valueOf(Math.round(dr04_suopje))));
						
						//��������TXT��
						diaor04bb=diao04.toString();//�õ�һ����
//						System.out.println(diaor04bb);
						ct.aLine(diaor04bb);//д��������
					}
					ct.finish();//�ر��������������ִӻ���д���ļ�
					rs.close();
					diaor04bb="����diaor04bb"+nianf+"��"+yuef+"�µ�'"+leix+"'���ݳɹ�!"+"\n";
				}
			}catch (Exception e) {
				diaor04bb="����diaor04bb�������쳣!"+"\n";
				e.printStackTrace();
			} finally {
				JDBCcon.Close();
			}
			return diaor04bb;
		}
	}
	
	public String getdiaor08(String diancbm,String TxtName,String leix,long nianf,long yuef){
		if(diancbm==""){
			return "diaor08��"+leix+"û�����ϴ�����!";
		}else{
			String dc="";
			if(diancbm.equals("111700")){
				dc="";
			}else{
				dc=" and dc.bianm='"+diancbm+"'";
			}
			String diaor08bb="";
			StringBuffer sql= new StringBuffer();
			JDBCcon JDBCcon = new JDBCcon();
			FtpCreatTxt ct =new FtpCreatTxt();
			ct.CreatTxt("c://Shujsc/"+TxtName+".txt");//�����ļ�
			try {
	//			��Ȼ08
				String dr08_diancbm="";
				String dr08_kouj="";
				String dr08_bianm="";
				double dr08_meil =0;
				double dr08_daoczhj =0;
				double dr08_kuangj =0;
				double dr08_zengzse =0;
				double dr08_jiaohqyzf =0;
				double dr08_tielyf =0;
				double dr08_tieyse =0;
				double dr08_tielzf =0;
				double dr08_shuillyf =0;
				double dr08_shuiyf =0;
				double dr08_shuiyse =0;
				double dr08_shuiyzf =0;
				double dr08_qiyf =0;
				double dr08_gangzf =0;
				double dr08_daozzf =0;
				double dr08_qitfy =0;
				double dr08_sunhl =0;
				double dr08_sunhzhje =0;
				double dr08_rez =0;
				double dr08_biaomdj=0;
				
//				2010-03-01 lzq ���˰�ʺ�����˰��
				double dr08_shuil =0;
				double dr08_qiyse=0;
				
				
				StringBuffer diao08=new StringBuffer();
//				sql.append("select dc.diancbm,'10' as kouj,mk.bianm,d.meil,d.daoczhj,d.kuangj,d.zengzse,");
//				sql.append(" d.jiaohqyzf,d.tielyf,d.tieyse,d.tielzf,d.shuillyf,d.shuiyf,d.shuiyse,d.shuiyzf,");
//				sql.append(" d.qiyf,d.gangzf,d.daozzf,d.qitfy,d.sunhl,d.sunhzhje,d.rez,d.biaomdj");
//				sql.append("  from diaor08bb d,diancxxb dc,meikdqb mk");
//				sql.append("  where d.diancxxb_id=dc.id and d.meikdqb_id=mk.id");
//				sql.append(" and d.tongjrq=to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd') and d.dangyjlj='"+leix+"' "+dc);
				
				
				sql.append("select a.�糧 as diancbm, '10' as kouj,a.ú�� as bianm,a.ú�� as meil,\n");
				sql.append("       ���+����ǰ���ӷ�+��·�˷�+��·�ӷ�+��վ�ӷ�+��������+ˮ�˷�+ˮ���ӷ�+���˷�+���ӷ� as daoczhj,\n"); 
				sql.append("    \t a.��� as kuangj,a.��ֵ˰�� as zengzse,a.����ǰ���ӷ� jiaohqyzf,a.��·�˷� tielyf,a.����˰�� tieyse,a.��·�ӷ� tielzf,\n");
				sql.append("       a.shuillyf,a.ˮ�˷� shuiyf,a.ˮ��˰�� as shuiyse,a.ˮ���ӷ� as shuiyzf,a.���˷� as qiyf,a.���ӷ� as gangzf,\n"); 
				sql.append("       a.��վ�ӷ� as daozzf,a.�������� as qitfy,a.sunhl,a.sunhzhje,a.��ֵ as rez,a.biaomdj,a.˰�� as shuil,a.����˰�� as qiyse \n"); 
				sql.append("  from (\n");
				sql.append("    SELECT dc.bianm as �糧,dq.bianm ú��, sum(meil) as ú��,\n"); 
				sql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*kuangj)/sum(meil),2) end as ���,\n"); 
				sql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*zengzse)/sum(meil),2) end as ��ֵ˰��,\n"); 
				sql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*jiaohqyzf)/sum(meil),2) end as ����ǰ���ӷ�,\n");
				sql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*tielyf)/sum(meil),2) end as ��·�˷�,\n"); 
				sql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*tieyse)/sum(meil),2) end as ����˰��,\n");
				sql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*tielzf)/sum(meil),2) end as ��·�ӷ�,\n"); 
				sql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*shuillyf)/sum(meil),2) end as shuillyf,\n");
				sql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*shuiyf)/sum(meil),2) end as ˮ�˷�,\n"); 
				sql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*shuiyse)/sum(meil),2) end as ˮ��˰��,\n"); 
				sql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*shuiyzf)/sum(meil),2) end as ˮ���ӷ�,\n"); 
				sql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*qiyf)/sum(meil),2) end as ���˷�,\n"); 
				sql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*gangzf)/sum(meil),2) end as ���ӷ�,\n"); 
				sql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*daozzf)/sum(meil),2) end as ��վ�ӷ�,\n"); 
				sql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*qitfy)/sum(meil),2) end as ��������,\n"); 
				sql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*rez)/sum(meil),2) end as ��ֵ,\n"); 
				sql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*sunhl)/sum(meil),2) end as sunhl,\n"); 
				sql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*sunhzhje)/sum(meil),2) end as sunhzhje,\n"); 
				sql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*biaomdj)/sum(meil),2) end as biaomdj, \n"); 

				sql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*getshuil(to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd')))/sum(meil),2) end as ˰��,\n"); 
				sql.append("           case when sum(meil)=0 or sum(meil) is null then 0 else round(sum(meil*qiyse)/sum(meil),2) end as ����˰�� \n");
				
				sql.append("      From diaor08bb dr,gongysb dq,diancxxb dc\n"); 
				sql.append("     where riq =to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd') and dr.diancxxb_id=dc.id and dr.gongysb_id=dq.id and dr.fenx='"+leix+"' "+dc+" \n"); 
				sql.append("     group by (dc.bianm,dq.bianm)  order by dc.bianm,dq.bianm ) a");

				
				ResultSet rs = JDBCcon.getResultSet(sql.toString());
				if(!rs.next()){
					 new File("c://Shujsc/"+TxtName+".txt").delete();//ɾ���ļ�
					 diaor08bb="����diaor08bb����ʧ��,û���ҵ�diaor08bb"+nianf+"��"+yuef+"�µ�'"+leix+"'��Ҫ����"+"\n";
				}else{
				     rs.beforeFirst();
					for (int i=0;rs.next();i++){
						diao08.setLength(0);
						diaor08bb="";
						dr08_diancbm=rs.getString("diancbm");
						dr08_kouj=rs.getString("kouj");
						dr08_bianm=rs.getString("bianm");
						dr08_meil =rs.getDouble("meil");
						dr08_daoczhj =rs.getDouble("daoczhj");
						dr08_kuangj =rs.getDouble("kuangj");
						dr08_zengzse =rs.getDouble("zengzse");
						dr08_jiaohqyzf =rs.getDouble("jiaohqyzf");
						dr08_tielyf =rs.getDouble("tielyf");
						dr08_tieyse =rs.getDouble("tieyse");
						dr08_tielzf =rs.getDouble("tielzf");
						dr08_shuillyf =rs.getDouble("shuillyf");
						dr08_shuiyf =rs.getDouble("shuiyf");
						dr08_shuiyse =rs.getDouble("shuiyse");
						dr08_shuiyzf =rs.getDouble("shuiyzf");
						dr08_qiyf =rs.getDouble("qiyf");
						dr08_gangzf =rs.getDouble("gangzf");
						dr08_daozzf =rs.getDouble("daozzf");
						dr08_qitfy =rs.getDouble("qitfy");
						dr08_sunhl =rs.getDouble("sunhl");
						dr08_sunhzhje =rs.getDouble("sunhzhje");
						dr08_rez =rs.getDouble("rez");
						dr08_biaomdj=rs.getDouble("biaomdj");
						
						
						dr08_shuil =rs.getDouble("shuil");
						dr08_qiyse=rs.getDouble("qiyse");
						
						diao08.append(getStr(6,dr08_diancbm));
						diao08.append(getStr(2,dr08_kouj));
						diao08.append(getStr(6,dr08_bianm));
						diao08.append(getNum(10,0,String.valueOf(Math.round(dr08_meil))));
						
						if(dr08_daoczhj>=1000){
							dr08_daoczhj = Math.round(dr08_daoczhj*10)/10.0;
							diao08.append(getNum(6,1,String.valueOf(dr08_daoczhj)));
						}else{
							diao08.append(getNum(6,2,String.valueOf(dr08_daoczhj)));
						}
						if(dr08_kuangj>=1000){
							dr08_kuangj = Math.round(dr08_kuangj*10)/10.0;
							diao08.append(getNum(6,1,String.valueOf(dr08_kuangj)));
						}else{
							diao08.append(getNum(6,2,String.valueOf(dr08_kuangj)));
						}
//						diao08.append(getNum(6,2,String.valueOf(dr08_daoczhj)));
//						diao08.append(getNum(6,2,String.valueOf(dr08_kuangj)));
						diao08.append(getNum(6,2,String.valueOf(dr08_zengzse)));
						diao08.append(getNum(6,2,String.valueOf(dr08_jiaohqyzf)));
						diao08.append(getNum(6,2,String.valueOf(dr08_tielyf)));
						diao08.append(getNum(6,2,String.valueOf(dr08_tieyse)));
						diao08.append(getNum(6,2,String.valueOf(dr08_tielzf)));
						diao08.append(getNum(6,2,String.valueOf(dr08_shuillyf)));
						diao08.append(getNum(6,2,String.valueOf(dr08_shuiyf)));
						diao08.append(getNum(6,2,String.valueOf(dr08_shuiyse)));
						diao08.append(getNum(6,2,String.valueOf(dr08_shuiyzf)));
						diao08.append(getNum(6,2,String.valueOf(dr08_qiyf)));
						diao08.append(getNum(6,2,String.valueOf(dr08_gangzf)));
						diao08.append(getNum(6,2,String.valueOf(dr08_daozzf)));
						diao08.append(getNum(6,2,String.valueOf(dr08_qitfy)));
						diao08.append(getNum(6,2,String.valueOf(dr08_sunhl)));
						diao08.append(getNum(6,2,String.valueOf(dr08_sunhzhje)));
						diao08.append(getNum(6,2,String.valueOf(dr08_rez)));
						
						if(dr08_biaomdj>=1000){
							dr08_biaomdj = Math.round(dr08_biaomdj*10)/10.0;
							diao08.append(getNum(6,1,String.valueOf(dr08_biaomdj)));
						}else{
							diao08.append(getNum(6,2,String.valueOf(dr08_biaomdj)));
						}
						
						diao08.append(getNum(6,2,String.valueOf(dr08_shuil)));
						diao08.append(getNum(6,2,String.valueOf(dr08_qiyse)));
						
						//��������TXT��
						diaor08bb=diao08.toString();//�õ�һ����
						
						ct.aLine(diaor08bb);//д��������
					}
					ct.finish();//�ر��������������ִӻ���д���ļ�
					rs.close();
					diaor08bb="����diaor08bb"+nianf+"��"+yuef+"�µ�'"+leix+"'���ݳɹ�!"+"\n";
				}
			}catch (Exception e) {
				diaor08bb="����diaor08bb�������쳣!"+"\n";
				e.printStackTrace();
			} finally {
				JDBCcon.Close();
			}
			return diaor08bb;
		}
	}
	
	
	private void getUploadFtp(String tableName){//�ϴ�Ftp
		if(tableName==""){
			setMsg("û�п��ϴ��ļ�");
		}else{
			try{
				String upload="";
				String[] shangbbm=tableName.split(",");
				FtpUpload fu = new FtpUpload();
				//------------����fpt�������ϴ��ļ�-----------//
//				if(getFtpServer().equals("")){
//					upload="�ϴ�ʧ��,������Ftp��ַ!"+"\n";
//					setMsg("������Ftp��ַ��Ŀ¼!");
//				}else{
//					FTP����
					String ip="";
					String mul="/";
					String username="";
					String password="";
					ip=ZhongNip;
//					mul=ZhongNmm;
					if(mul.equals("/")){
						mul = "";
					}else{
						mul = "/"+mul+"/";
					}
//					if (getNim()){
//						username="administrator";
//						password="980904";
//					}else{
//						if(username.equals("") || password.equals("")){
//							setMsg("��¼��Ftp�û���������!");
//						}else{
							username=ZhongNyh;
							password=ZhongNmm;
							fu.connectServer(ip, username, password, mul);
							for (int i=0;i<shangbbm.length;i++){
								String filename="C://Shujsc//"+shangbbm[i]+ ".txt";
								upload=shangbbm[i]+fu.upload(filename,shangbbm[i]+ ".txt"+"\n");//�ϴ��ļ� 
								msg.append(upload+"\n"); 
							}
							fu.closeConnect(); 
//						}
//					}
//				}
			} catch (Exception e) {
				msg.append("Ftp����ʧ����ȷ��Ftp����!"+"\n");
				e.printStackTrace();
			} finally {
				// con.close();
			}
		}
		msg.append("***************TxT�ļ��ϴ�����***************"+"\n");
//		setFanhz(msg.toString());
		setMsg("�ϴ����!");
	}
//	*********************************�����ϴ������ֶδ� ***************************************************//
	private String getStrName(long TN,long yuef,String str,String leix){//�õ��ϴ��ļ���(�ϱ���,�·�,�糧����,����)
		/*�ӿ��ļ�����˵��:�ӿ��ļ�����8λ�ַ������:1019308M
		��1λ�����������
		 6����16-1��
		 1��ȼ01��
		 2��ȼ02��
		 3��ȼ03��
		 4��ȼ04��
		 8��ȼ08��
		 0��ȼ08�±�
		��2-3λ�����·�
		��4-7λ����糧
		    �ɵ糧����ĵ�1λ�͵糧��������3λ����
		��8λ�������ۼ�
		    M������
		    A�����ۼ�*/
		StringBuffer Str_zf = new StringBuffer();

		Str_zf.append(TN);//�õ���һλNΪ��������(1,2,3,4,6,8,z)
		Str_zf.append(getNianfValue().getValue().substring(2));
		if(yuef==10 || yuef==11 || yuef==12){//�õ���2,3λ
			Str_zf.append(yuef);
		}else{
			Str_zf.append("0"+yuef);
		}
//		�õ��糧����
		if(str==null && str.equals("")){
				Str_zf.append("0000");
		}else{
			Str_zf.append(str.substring(0,1));
			Str_zf.append(str.substring(str.length()-3,str.length()));
		}
		Str_zf.append(leix);

		return Str_zf.toString();
	}
	private String getStr(int weis,String str){
		StringBuffer Str_zf = new StringBuffer();
		if(str==null || str.equals("")){
			for (int i=0;i<weis;i++){
				Str_zf.append(" ");
			}
		}else{
			char[] Str=str.toCharArray();
			int Str_lenght=Str.length;
			
			for (int j=0;j<Str_lenght;j++){
				String Strs=""+Str[j];
				Str_zf.append(Strs);
			}
			int cha=0;
			if (Str_lenght!=weis){
				cha=weis-Str_lenght;
				for (int i=0;i<cha;i++){
					Str_zf.append(" ");
				}
			}
		}
		return Str_zf.toString();
	}
	
	private String getNum(int weis,int xiaos,String Number){//�õ�λ����������
		StringBuffer Str_zf = new StringBuffer();
		String str="";
		str=Number;
		if(str.equals("") ){
			for (int j=0;j<weis-xiaos-2;j++){
				String Strs="";
				Str_zf.append(Strs);
			}
			Str_zf.append(0.);
			for (int j=0;j<xiaos;j++){
				Str_zf.append(0);
			}
		}else{
			int zhengsw=0; 
			if(xiaos!=0){//��С��λ��
				String[] c=str.split("\\.");
				String strs1=c[0];//����λ
				char[] Str1=strs1.toCharArray();//����λ
				String Strs2=c[1];//С��λ
				char[] Str2=Strs2.toCharArray();//С��λ
				//¼������λ
				zhengsw=weis-xiaos-1;
				if (Str1.length!=zhengsw){
					int cha=zhengsw-Str1.length;
					for (int i=0;i<cha;i++){
						Str_zf.append(" ");
					}
				}
				for (int j=0;j<Str1.length;j++){
					String Strs=""+Str1[j];
					Str_zf.append(Strs);
				}
				//¼��С��λ
				Str_zf.append(".");
				if(Str2.length!=xiaos){
					for (int j=0;j<Str2.length;j++){
						String Strs=""+Str2[j];
						Str_zf.append(Strs);
					}
					for (int j=0;j<xiaos-Str2.length;j++){
						Str_zf.append(0);
					}
				}else{
					for (int j=0;j<Str2.length;j++){
						String Strs=""+Str2[j];
						Str_zf.append(Strs);
					}
				}
			}else{//����С��λ
				char[] Str=str.toCharArray();
				int Str_lenght=Str.length;
				int cha=0;
				if (Str_lenght!=weis){//���ո�
					cha=weis-Str_lenght;
					for (int i=0;i<cha;i++){
						Str_zf.append(" ");
					}
				}
				for (int j=0;j<Str_lenght;j++){//¼������
					String Strs=""+Str[j];
					Str_zf.append(Strs);
				}
			}
		}

		return Str_zf.toString();
	}
//�������
	public void getSelectData() {
		ResultSetList rsl=null;
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		
		String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");

	
//	---------------------------------------------------------------------	
		String context = MainGlobal.getHomeContext(this);
		String chaolj="";

//		chaolj="get_Zhuangt("+getTreeid()+","+CurrODate+",sjb.shujbmc) as zhuangt \n";

		if (rsl == null) {
 			String strSql=
				"select sjb.id as id,sjb.xuh as xuh,sjb.baobmc as baobmc \n"+//+",\n" +
//					   chaolj+
				"from yuebbdysjb sjb\n" +
				"where sjb.zhuangt=2\n"+
				"order by xuh";

			
 			rsl = con.getResultSetList(strSql);
		}
		
		rsl.beforefirst();
		boolean showBtn = false;
		if(rsl.next()){
			rsl.beforefirst();
			showBtn = false;
		}else{
			showBtn = true;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //���ñ��������ڱ���
		egu.setTableName("yuebshsjb");
		// /������ʾ������
		egu.setWidth("bodyWidth");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		// ���ö�ѡ��
		egu.addColumn(1, new GridColumn(GridColumn.ColType_Check));
		egu.getColumn(1).setAlign("middle");
		
		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("xuh").setWidth(50);
		egu.getColumn("xuh").setHidden(true);
		egu.getColumn("baobmc").setHeader("��������");
		egu.getColumn("baobmc").setWidth(300);
		egu.getColumn("baobmc").setEditor(null);
//		egu.getColumn("zhuangt").setHeader("״̬");
//		egu.getColumn("zhuangt").setWidth(100);
//		egu.getColumn("zhuangt").setEditor(null);

		 
		// /���ð�ť
		egu.addTbarText("��λ��");
		//��ǰ�û������糧
		long diancxxb_id=((Visit)this.getPage().getVisit()).getDiancxxb_id();
	     ExtTreeUtil etu=new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,
	    		    diancxxb_id,this.getTreeid());
	    this.setTree(etu);
	    //����糧ѡ���
		egu.addTbarTreeBtn(etu.treeId);
		egu.addTbarText("���");
		ComboBox comb1=new ComboBox();
		comb1.setWidth(60);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");//���Զ�ˢ�°�
		comb1.setLazyRender(true);//��̬��
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("�·�");
		ComboBox comb2=new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");//���Զ�ˢ�°�
		comb2.setLazyRender(true);//��̬��
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");

//		ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'����ˢ��'+Ext.getDom('NianfDropDown').value+'��'+Ext.getDom('YuefDropDown').value+'�µ�����,���Ժ�'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
	
//		�ϱ���ť
			egu.addToolbarButton("�ϱ�", GridButton.ButtonType_SubmitSel, "ShangbButton", "", SysConstant.Btn_Icon_SelSubmit);
		
/*//		��ȼ����
			egu.addToolbarItem("{"+new GridButton("��ȼ����","function(){"+
					
					"document.getElementById('DiaorszButton').click();" +
					"}").getScript()+"}");
			
//		cpi01������λ����
			egu.addToolbarItem("{"+new GridButton("cpi01������λ����","function(){"+
					
					"document.getElementById('GonghdwszButton').click();" +
					"}").getScript()+"}");
			
//			���ܹ�����λ����
			egu.addToolbarItem("{"+new GridButton("���ܹ�����λ����","function(){"+
					
					"document.getElementById('ZhongnghdwszButton').click();" +
					"}").getScript()+"}");
			
//			�糧��������
			egu.addToolbarItem("{"+new GridButton("�糧��������","function(){"+
					
					"document.getElementById('DanwmcszButton').click();" +
					"}").getScript()+"}");*/

		setExtGrid(egu);
		con.Close();
	}
	

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	
//#######################################################	
//	��ȼ����
	private void Diaorsz(IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		visit.setString4(getChange());
		cycle.activate("Shenhsz");
	}
//	cpi01������λ����
	private void Gonghdwsz(IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		visit.setString5(getChange());
		cycle.activate("Gonghdwsz");
	}
//	���ܹ�����λ����
	private void Zhongnghdwsz(IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		visit.setString6(getChange());
		cycle.activate("Zhongnghdw");
	}
//	cpi01�糧��������
	private void Danwmcsz(IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		visit.setString7(getChange());
		cycle.activate("Danwmcsz");
	}
//#######################################################	
	
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString1(null);
			visit.setString2(null);
			visit.setString3(null);
			visit.setString4(null);
			visit.setString5(null);
			visit.setString6(null);
			visit.setString7(null);
			setYuefValue(null);
			setNianfValue(null);
			this.setTreeid(null);
			this.getYuefModels();
			this.getNianfModels();

			setRiq();

		}
		getSelectData();
	}
	
	
	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString1();
	}
	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	
	public String getYuef() {
		int intYuef = Integer.parseInt(((Visit) getPage().getVisit()).getString3());
		if (intYuef<10){
			return "0"+intYuef;
		}else{
			return ((Visit) getPage().getVisit()).getString3();
		}
	}
	public void setYuef(String value) {
		((Visit) getPage().getVisit()).setString3(value);
	}
	
	public void setRiq() {
		setNianf(getNianfValue().getValue());
		setYuef(getYuefValue().getValue());
	}
//�糧��

	private String treeid;


	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}

	 // ���������
    private static IPropertySelectionModel _NianfModel;
    public IPropertySelectionModel getNianfModel() {
        if (_NianfModel == null) {
            getNianfModels();
        }
        return _NianfModel;
    }
    
	private IDropDownBean _NianfValue;
	
    public IDropDownBean getNianfValue() {
        if (_NianfValue == null) {
            int _nianf = DateUtil.getYear(new Date());
            int _yuef = DateUtil.getMonth(new Date());
            if (_yuef == 1) {
                _nianf = _nianf - 1;
            }
            for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
                Object obj = getNianfModel().getOption(i);
                if (_nianf == ((IDropDownBean) obj).getId()) {
                    _NianfValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _NianfValue;
    }
	
    public void setNianfValue(IDropDownBean Value) {
    	if  (_NianfValue!=Value){
    		_NianfValue = Value;
    	}
    }

    public IPropertySelectionModel getNianfModels() {
        List listNianf = new ArrayList();
        int i;
        for (i = 2004; i <= DateUtil.getYear(new Date())+1; i++) {
            listNianf.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _NianfModel = new IDropDownModel(listNianf);
        return _NianfModel;
    }

    public void setNianfModel(IPropertySelectionModel _value) {
        _NianfModel = _value;
    }

	// �·�������
	private static IPropertySelectionModel _YuefModel;
	
	public IPropertySelectionModel getYuefModel() {
	    if (_YuefModel == null) {
	        getYuefModels();
	    }
	    return _YuefModel;
	}
	
	private IDropDownBean _YuefValue;
	
	public IDropDownBean getYuefValue() {
	    if (_YuefValue == null) {
	        int _yuef = DateUtil.getMonth(new Date());
	        if (_yuef == 1) {
	            _yuef = 12;
	        } else {
	            _yuef = _yuef - 1;
	        }
	        for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
	            Object obj = getYuefModel().getOption(i);
	            if (_yuef == ((IDropDownBean) obj).getId()) {
	                _YuefValue = (IDropDownBean) obj;
	                break;
	            }
	        }
	    }
	    return _YuefValue;
	}
	
	public void setYuefValue(IDropDownBean Value) {
    	if  (_YuefValue!=Value){
    		_YuefValue = Value;
    	}
	}

    public IPropertySelectionModel getYuefModels() {
        List listYuef = new ArrayList();
        for (int i = 1; i < 13; i++) {
            listYuef.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _YuefModel = new IDropDownModel(listYuef);
        return _YuefModel;
    }

    public void setYuefModel(IPropertySelectionModel _value) {
        _YuefModel = _value;
    }

}
