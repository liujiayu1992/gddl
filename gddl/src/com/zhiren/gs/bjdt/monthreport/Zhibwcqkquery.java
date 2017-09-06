package com.zhiren.gs.bjdt.monthreport;

import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.common.SysConstant;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.common.filejx.FileDropDownModel;
import com.zhiren.common.filejx.FileJx;
import com.zhiren.common.filejx.FilePathRead;

public class Zhibwcqkquery extends BasePage {
	public boolean getRaw() {
		return true;
	}
	private String userName="";
	
	public void setUserName(String value) {
		userName=((Visit) getPage().getVisit()).getRenymc();
	}
	public String getUserName() {
		return userName;
	}
	
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// ����

	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// ��˾
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// �糧
	}
	
	private boolean reportShowZero(){
		return ((Visit) getPage().getVisit()).isReportShowZero();
	}
	
	private int _CurrentPage = -1;
	
	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	
	public int getAllPages() {
		return _AllPages;
	}
	
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	
//	***************������Ϣ��******************//
	private String _msg;

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	private String RT_DR="zhibwcqk";
	
	
	private String mstrReportName="";
	
	public String getTianzdwQuanc(){
		return getTianzdwQuanc(getDiancxxbId());
	}
	
	public String getDiancxxbId(){
		
		return getTreeid();
	}
	
	public boolean isJTUser(){
		return ((Visit)getPage().getVisit()).isJTUser();
	}
	
//	 �õ���λȫ��
	public String getTianzdwQuanc(String gongsxxbID) {
		String _TianzdwQuanc = "";
		JDBCcon cn = new JDBCcon();

		try {
			ResultSet rs = cn
					.getResultSet(" select quanc from diancxxb where id="
							+ gongsxxbID);
			while (rs.next()) {
				_TianzdwQuanc = rs.getString("quanc");
			}
			if (_TianzdwQuanc.equals("��������ȼ�����޹�˾")) {
				_TianzdwQuanc = "���ƹ��ʷ���ɷ����޹�˾ȼ�Ϲ���";
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return _TianzdwQuanc;
	}
	
	private boolean blnIsBegin = false;
//	private String leix="";
	public String getPrintTable(){
		setMsg(null);
		if(!blnIsBegin){
			return "";
		}
		blnIsBegin=false;
		
//		if (mstrReportName.equals(RT_DR)){

			return getZhibwcqk();
//		}
//		else{	
//			return "�޴˱���";
		}
//	}
	
	private String getDiancCondition(){
		JDBCcon cn = new JDBCcon();
		String diancxxb_id=getTreeid();
		String condition ="";
		ResultSet rs=cn.getResultSet("select jib,id,fuid from diancxxb where id=" +diancxxb_id);
		try {
			if (rs.next()){
				if( rs.getLong("jib")==SysConstant.JIB_JT){
					condition="1";
				
				}else if(rs.getLong("jib")==SysConstant.JIB_GS){
					condition="2";
				}else {
					condition="3";
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return condition;
	}
	//    �޸���   ---------------------------------------------------
//	private String getGongysCondition(){
//		if (getDiancmcValue().getId()==-1){
//			return "";
//		}else{
//			return " and dq.id=" +getDiancmcValue().getId();
//		}
//	}
	
	private String getZhibwcqk(){
		String _Danwqc=getTianzdwQuanc();
		JDBCcon cn = new JDBCcon();
		
		if(getDiancCondition().equals("2")){
			_Danwqc = "���ƹ��ʷ���ɷ����޹�˾ȼ�Ϲ���";
		}
		
//		long lngDiancId=getGongsId();//�糧��Ϣ��id
		String strDate=getNianfValue().getValue() +"-"+getYuefValue().getValue()+"-01";
		String strMonth=getNianfValue().getValue()+"��"+getYuefValue().getValue()+"��";
		
		String Data[][] = new String[34][4];
		
		StringBuffer sbsql = new StringBuffer(); 
		String where = "";
		try{
//			if(getDiancmcValue()!=null){            
				if(getDiancCondition().equals("3")){   //�жϵ��ǵ糧����ʱ
					where = " and diancxxb_id="+getTreeid();  
					String sql = "select * from zhibwcqkyb where riq=to_date('"+strDate+"','yyyy-mm-dd')"+where;
					ResultSet rs=cn.getResultSet(sql);
					while(rs.next()){
						int xuh = rs.getInt("xuh")-1;
						Data[xuh][0]=rs.getString("benq");
						Data[xuh][1]=rs.getString("tongq");	
						Data[xuh][2]=rs.getString("bianhqk");
						Data[xuh][3]=rs.getString("beiz");
					}
					rs.close();
				}else{
					//��ѯ�Ǳ�עȡ���ֵ
					sbsql.append("select zb.zhibmc,zb.fenx,max(zb.xuh) as xuh,max(zb.beiz) as beiz,sum(zb.benq) as benq,sum(nvl(zb.tongq,0)) as tongq,sum(zb.bianhqk) as bianhqk  \n");
					sbsql.append("  from zhibwcqkyb zb where zb.riq=to_date('"+strDate+"','yyyy-mm-dd') \n");
					sbsql.append("   and zb.zhibmc not in ('����ú������','��¯ú������','������Ȼú����','������ú����','��λȼ�ϳɱ�','����ú��') \n");
					sbsql.append(" group by (zb.zhibmc,zb.fenx) order by max(zb.xuh) \n");
					
					ResultSet rs=cn.getResultSet(sbsql.toString());
					while(rs.next()){
						int xuh = rs.getInt("xuh")-1;
						
						Data[xuh][0]=rs.getString("benq");
						Data[xuh][1]=rs.getString("tongq");	
						Data[xuh][2]=rs.getString("bianhqk");
						Data[xuh][3]=rs.getString("beiz");
					}
					sbsql.setLength(0);
					 
					sbsql.append("select z.fenx,z.zhibmc,max(z.xuh) as xuh,max(z.beiz) as beiz, \n");
					sbsql.append("       decode(sum(ml.benq),0,0,round(sum(z.benq*ml.benq)/sum(ml.benq),2)) as benq, \n");
					sbsql.append("       decode(sum(nvl(ml.tongq,0)),0,0,round(sum(z.tongq*ml.tongq)/sum(ml.tongq),2)) as tongq, \n");
					sbsql.append("       decode(sum(ml.benq),0,0,round(sum(z.benq*ml.benq)/sum(ml.benq),2))-decode(sum(nvl(ml.tongq,0)),0,0,round(sum(z.tongq*ml.tongq)/sum(ml.tongq),2)) as bianhqk \n");
					sbsql.append("   from  \n");
					sbsql.append("(select zb.fenx,zb.benq,zb.tongq,zb.bianhqk from zhibwcqkyb zb  \n");
					sbsql.append("  where zb.riq=to_date('"+strDate+"','yyyy-mm-dd') and zb.zhibmc='������Ȼú��') ml,zhibwcqkyb z  \n");
					sbsql.append("  where ml.fenx=z.fenx and z.riq=to_date('"+strDate+"','yyyy-mm-dd')  \n");
					sbsql.append("    and z.zhibmc in ('����ú������','������Ȼú����') \n");
					sbsql.append("  group by ( z.fenx,z.zhibmc) order by max(z.xuh) \n");
					
					rs=cn.getResultSet(sbsql.toString());
					while(rs.next()){
						int xuh = rs.getInt("xuh")-1;
						
						Data[xuh][0]=rs.getString("benq");
						Data[xuh][1]=rs.getString("tongq");	
						Data[xuh][2]=rs.getString("bianhqk");
						Data[xuh][3]=rs.getString("beiz");
					}
					sbsql.setLength(0);
					
					sbsql.append("select z.fenx,z.zhibmc,max(z.xuh) as xuh,max(z.beiz) as beiz, \n");
					sbsql.append("       decode(sum(ml.benq),0,0,round(sum(z.benq*ml.benq)/sum(ml.benq),2)) as benq, \n");
					sbsql.append("       decode(sum(nvl(ml.tongq,0)),0,0,round(sum(z.tongq*ml.tongq)/sum(ml.tongq),2)) as tongq, \n");
					sbsql.append("       decode(sum(ml.benq),0,0,round(sum(z.benq*ml.benq)/sum(ml.benq),2))-decode(sum(nvl(ml.tongq,0)),0,0,round(sum(z.tongq*ml.tongq)/sum(ml.tongq),2)) as bianhqk \n");
					sbsql.append("   from  \n");
					sbsql.append("(select zb.fenx,zb.benq,zb.tongq,zb.bianhqk from zhibwcqkyb zb  \n");
					sbsql.append("  where zb.riq=to_date('"+strDate+"','yyyy-mm-dd') and zb.zhibmc='������Ȼú��') ml,zhibwcqkyb z  \n");
					sbsql.append("  where ml.fenx=z.fenx and z.riq=to_date('"+strDate+"','yyyy-mm-dd')  \n");
					sbsql.append("    and z.zhibmc in ('��¯ú������') \n");
					sbsql.append("  group by ( z.fenx,z.zhibmc) order by max(z.xuh) \n");
					
					rs=cn.getResultSet(sbsql.toString());
					while(rs.next()){
						int xuh = rs.getInt("xuh")-1;
						
						Data[xuh][0]=rs.getString("benq");
						Data[xuh][1]=rs.getString("tongq");	
						Data[xuh][2]=rs.getString("bianhqk");
						Data[xuh][3]=rs.getString("beiz");
					}
					sbsql.setLength(0);
					
					sbsql.append("select z.fenx,z.zhibmc,max(z.xuh) as xuh,max(z.beiz) as beiz, \n");
					sbsql.append("       decode(sum(ml.benq),0,0,round(sum(z.benq*ml.benq)/sum(ml.benq),2)) as benq, \n");
					sbsql.append("       decode(sum(nvl(ml.tongq,0)),0,0,round(sum(z.tongq*ml.tongq)/sum(ml.tongq),2)) as tongq, \n");
					sbsql.append("       decode(sum(ml.benq),0,0,round(sum(z.benq*ml.benq)/sum(ml.benq),2))-decode(sum(nvl(ml.tongq,0)),0,0,round(sum(z.tongq*ml.tongq)/sum(ml.tongq),2)) as bianhqk \n");
					sbsql.append("   from  \n");
					sbsql.append("(select zb.fenx,zb.benq,zb.tongq,zb.bianhqk from zhibwcqkyb zb  \n");
					sbsql.append("  where zb.riq=to_date('"+strDate+"','yyyy-mm-dd') and zb.zhibmc='��������') ml,zhibwcqkyb z  \n");
					sbsql.append("  where ml.fenx=z.fenx and z.riq=to_date('"+strDate+"','yyyy-mm-dd')  \n");
					sbsql.append("    and z.zhibmc in ('��λȼ�ϳɱ�','����ú��') \n");
					sbsql.append("  group by ( z.fenx,z.zhibmc) order by max(z.xuh) \n");
					
					rs=cn.getResultSet(sbsql.toString());
					while(rs.next()){
						int xuh = rs.getInt("xuh")-1;
						
						Data[xuh][0]=rs.getString("benq");
						Data[xuh][1]=rs.getString("tongq");	
						Data[xuh][2]=rs.getString("bianhqk");
						Data[xuh][3]=rs.getString("beiz");
					}
					sbsql.setLength(0);
	
					sbsql.append("select max(z.xuh) as xuh,z.fenx,max(z.beiz) as beiz, \n");
					sbsql.append("       decode(sum(rl.benq),0,0,round(sum(dj.benq)*29.271/sum(rl.benq),2)) as benq, \n");
					sbsql.append("       decode(sum(nvl(rl.tongq,0)),0,0,round(sum(dj.tongq)*29.271/sum(rl.tongq),2)) as tongq, \n");
					sbsql.append("       decode(sum(rl.benq),0,0,round(sum(dj.benq)*29.271/sum(rl.benq),2))-decode(sum(nvl(rl.tongq,0)),0,0,round(sum(dj.tongq)*29.271/sum(rl.tongq),2)) as bianhqk \n");
					sbsql.append("  from \n");
					sbsql.append("(select z.fenx,z.zhibmc,z.xuh,z.beiz,  \n");
					sbsql.append("       decode(sum(ml.benq),0,0,round(sum(z.benq*ml.benq)/sum(ml.benq),2)) as benq,  \n");
					sbsql.append("       decode(sum(nvl(ml.tongq,0)),0,0,round(sum(z.tongq*ml.tongq)/sum(ml.tongq),2)) as tongq  \n");
//					sbsql.append("       decode(sum(ml.bianhqk),0,0,round(sum(z.bianhqk*ml.bianhqk)/sum(ml.bianhqk),2)) as bianhqk  \n");
					sbsql.append("   from   \n");
					sbsql.append("(select zb.fenx,zb.benq,nvl(zb.tongq,0) as tongq,zb.bianhqk from zhibwcqkyb zb   \n");
					sbsql.append("  where zb.riq=to_date('"+strDate+"','yyyy-mm-dd') and zb.zhibmc='������Ȼú��' ) ml,zhibwcqkyb z   \n");
					sbsql.append("  where ml.fenx=z.fenx and z.riq=to_date('"+strDate+"','yyyy-mm-dd')   \n");
					sbsql.append("    and z.zhibmc in ('������Ȼú����')  \n");
					sbsql.append("  group by ( z.fenx,z.zhibmc,z.xuh,z.beiz) order by z.xuh )dj, \n");
					sbsql.append("  (select z.fenx,z.zhibmc,z.xuh,z.beiz,  \n");
					sbsql.append("       decode(sum(ml.benq),0,0,sum(z.benq*ml.benq)/sum(ml.benq)) as benq,  \n");
					sbsql.append("       decode(sum(nvl(ml.tongq,0)),0,0,sum(z.tongq*ml.tongq)/sum(ml.tongq)) as tongq,  \n");
					sbsql.append("       decode(sum(ml.bianhqk),0,0,sum(z.bianhqk*ml.bianhqk)/sum(ml.bianhqk)) as bianhqk  \n");
					sbsql.append("   from   \n");
					sbsql.append("(select zb.fenx,zb.benq,nvl(zb.tongq,0) as tongq,zb.bianhqk from zhibwcqkyb zb   \n");
					sbsql.append("  where zb.riq=to_date('"+strDate+"','yyyy-mm-dd') and zb.zhibmc='������Ȼú��' ) ml,zhibwcqkyb z   \n");
					sbsql.append("  where ml.fenx=z.fenx and z.riq=to_date('"+strDate+"','yyyy-mm-dd')   \n");
					sbsql.append("    and z.zhibmc in ('����ú������')  \n");
					sbsql.append("  group by ( z.fenx,z.zhibmc,z.xuh,z.beiz) order by z.xuh )rl,zhibwcqkyb z \n");
					sbsql.append("  where dj.fenx=rl.fenx and z.fenx=dj.fenx and z.riq=to_date('"+strDate+"','yyyy-mm-dd') and z.zhibmc='������ú����' \n");
					sbsql.append("  group by (z.fenx) \n");
					
					rs=cn.getResultSet(sbsql.toString());
					while(rs.next()){
						int xuh = rs.getInt("xuh")-1;
						
						Data[xuh][0]=rs.getString("benq");
						Data[xuh][1]=rs.getString("tongq");	
						Data[xuh][2]=rs.getString("bianhqk");
						Data[xuh][3]=rs.getString("beiz");
					}
					rs.close();
//				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		
		
		int ArrWidth[]=new int[] {100,80,60,70,70,70,140};
		String ArrHeader[][]=new String[35][7];
		ArrHeader[0]=new String[] {"ָ������","��λ","���»��ۼ�","����","ͬ��","�仯���","��ע"};
		ArrHeader[1]=new String[] {"������Ȼú��","���","����",Data[0][0],Data[0][1],Data[0][2],Data[0][3]};
		ArrHeader[2]=new String[] {"","","�ۼ�",Data[1][0],Data[1][1],Data[1][2],Data[1][3]};
		
		ArrHeader[3]=new String[] {"����ú������","Mj/kg","����",Data[2][0],Data[2][1],Data[2][2],Data[2][3]};
		ArrHeader[4]=new String[] {"","","�ۼ�",Data[3][0],Data[3][1],Data[3][2],Data[3][3]};
		
		ArrHeader[5]=new String[] {"��¯ú������","Mj/kg","����",Data[4][0],Data[4][1],Data[4][2],Data[4][3]};
		ArrHeader[6]=new String[] {"","","�ۼ�",Data[5][0],Data[5][1],Data[5][2],Data[5][3]};
		
		ArrHeader[7]=new String[] {"������Ȼú����","Ԫ/��","����",Data[6][0],Data[6][1],Data[6][2],Data[6][3]};
		ArrHeader[8]=new String[] {"","","�ۼ�",Data[7][0],Data[7][1],Data[7][2],Data[7][3]};
		
		ArrHeader[9]=new String[] {"������ú����","Ԫ/��","����",Data[8][0],Data[8][1],Data[8][2],Data[8][3]};
		ArrHeader[10]=new String[] {"","","�ۼ�",Data[9][0],Data[9][1],Data[9][2],Data[9][3]};
		
		ArrHeader[11]=new String[] {"��λȼ�ϳɱ�","Ԫ/ǧǧ��ʱ","����",Data[10][0],Data[10][1],Data[10][2],Data[10][3]};
		ArrHeader[12]=new String[] {"","","�ۼ�",Data[11][0],Data[11][1],Data[11][2],Data[11][3]};
		
		ArrHeader[13]=new String[] {"������","��ǧ��ʱ","����",Data[12][0],Data[12][1],Data[12][2],Data[12][3]};
		ArrHeader[14]=new String[] {"","","�ۼ�",Data[13][0],Data[13][1],Data[13][2],Data[13][3]};
		
		ArrHeader[15]=new String[] {"������","ʮ�ڽ���","����",Data[14][0],Data[14][1],Data[14][2],Data[14][3]};
		ArrHeader[16]=new String[] {"","","�ۼ�",Data[15][0],Data[15][1],Data[15][2],Data[15][3]};
		
		ArrHeader[17]=new String[] {"��������","��ǧ��ʱ","����",Data[16][0],Data[16][1],Data[16][2],Data[16][3]};
		ArrHeader[18]=new String[] {"","","�ۼ�",Data[17][0],Data[17][1],Data[17][2],Data[17][3]};
		
		ArrHeader[19]=new String[] {"����ú��","g/kwh","����",Data[18][0],Data[18][1],Data[18][2],Data[18][3]};
		ArrHeader[20]=new String[] {"","","�ۼ�",Data[19][0],Data[19][1],Data[19][2],Data[19][3]};
		
		ArrHeader[21]=new String[] {"������Ȼú��","���","����",Data[20][0],Data[20][1],Data[20][2],Data[20][3]};
		ArrHeader[22]=new String[] {"","","�ۼ�",Data[21][0],Data[21][1],Data[21][2],Data[21][3]};
		
		ArrHeader[23]=new String[] {"���ñ�ú��","���","����",Data[22][0],Data[22][1],Data[22][2],Data[22][3]};
		ArrHeader[24]=new String[] {"","","�ۼ�",Data[23][0],Data[23][1],Data[23][2],Data[23][3]};
		
		ArrHeader[25]=new String[] {"����úӯ����","��","����",Data[24][0],Data[24][1],Data[24][2],Data[24][3]};
		ArrHeader[26]=new String[] {"","","�ۼ�",Data[25][0],Data[25][1],Data[25][2],Data[25][3]};
		
		ArrHeader[27]=new String[] {"����������","��Ԫ","����",Data[26][0],Data[26][1],Data[26][2],Data[26][3]};
		ArrHeader[28]=new String[] {"","","�ۼ�",Data[27][0],Data[27][1],Data[27][2],Data[27][3]};
		
		ArrHeader[29]=new String[] {"����������","��Ԫ","����",Data[28][0],Data[28][1],Data[28][2],Data[28][3]};
		ArrHeader[30]=new String[] {"","","�ۼ�",Data[29][0],Data[29][1],Data[29][2],Data[29][3]};
		
		ArrHeader[31]=new String[] {"����������","��Ԫ","����",Data[30][0],Data[30][1],Data[30][2],Data[30][3]};
		ArrHeader[32]=new String[] {"","","�ۼ�",Data[31][0],Data[31][1],Data[31][2],Data[31][3]};
		
		ArrHeader[33]=new String[] {"ȼ�Ͻ�����","��Ԫ","����",Data[32][0],Data[32][1],Data[32][2],Data[32][3]};
		ArrHeader[34]=new String[] {"","","�ۼ�",Data[33][0],Data[33][1],Data[33][2],Data[33][3]};

//		����ҳTitle
		Report rt=new Report();
		rt.setTitle("���糧ȼ�Ϲ���ָ��������ͳ��",ArrWidth);
		rt.setDefaultTitleLeft("���Ƶ�λ��"+_Danwqc,4);
		rt.setDefaultTitle(5,3,"����:"+strMonth,Table.ALIGN_RIGHT);
//		rt.setBody(new Table(ArrHeader,0,true,Table.ALIGN_CENTER));//����û�õ�table����������ͰѲ�������Ϊ0.
		rt.setBody(new Table(ArrHeader,0,0,0));    
//		�ϲ���Ԫ��
//		rt.body.setPageRows(31);
		rt.body.setRowHeight(20);
		rt.body.setRowHeight(1, 25);
		rt.body.setWidth(ArrWidth);
		rt.body.mergeCell(2,1,3,1);
		rt.body.mergeCell(2,2,3,2);
		rt.body.mergeCell(4,1,5,1);
		rt.body.mergeCell(4,2,5,2);
		rt.body.mergeCell(6,1,7,1);
		rt.body.mergeCell(6,2,7,2);
		rt.body.mergeCell(8,1,9,1);
		rt.body.mergeCell(8,2,9,2);
		rt.body.mergeCell(10,1,11,1);
		rt.body.mergeCell(10,2,11,2);
		rt.body.mergeCell(12,1,13,1);
		rt.body.mergeCell(12,2,13,2);
		rt.body.mergeCell(14,1,15,1);
		rt.body.mergeCell(14,2,15,2);
		rt.body.mergeCell(16,1,17,1);
		rt.body.mergeCell(16,2,17,2);
		rt.body.mergeCell(18,1,19,1);
		rt.body.mergeCell(18,2,19,2);
		rt.body.mergeCell(20,1,21,1);
		rt.body.mergeCell(20,2,21,2);
		rt.body.mergeCell(22,1,23,1);
		rt.body.mergeCell(22,2,23,2);
		rt.body.mergeCell(24,1,25,1);
		rt.body.mergeCell(24,2,25,2);
		rt.body.mergeCell(26,1,27,1);
		rt.body.mergeCell(26,2,27,2);
		rt.body.mergeCell(28,1,29,1);
		rt.body.mergeCell(28,2,29,2);
		rt.body.mergeCell(30,1,31,1);
		rt.body.mergeCell(30,2,31,2);
		rt.body.mergeCell(32,1,33,1);
		rt.body.mergeCell(32,2,33,2);
		rt.body.mergeCell(34,1,35,1);
		rt.body.mergeCell(34,2,35,2);

//		rt.body.setColAlign(4,Table.ALIGN_RIGHT);
//		rt.body.setColAlign(5,Table.ALIGN_RIGHT);
//		rt.body.setColAlign(6,Table.ALIGN_RIGHT);
		for(int i=0;i<rt.body.getCols();i++){
			rt.body.setCellAlign(1,(i+1), Table.ALIGN_CENTER);
		}
		for(int i=2;i<=35;i++){
			rt.body.setCellAlign(i,4, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(i,5, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(i,6, Table.ALIGN_RIGHT);
			rt.body.setCellAlign(i,7, Table.ALIGN_LEFT);
		}
		
		rt.body.ShowZero=false;
		
		//ҳ��
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1,2,"�Ʊ�:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(4,2,"���:",Table.ALIGN_CENTER);
		rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_CHINA,Table.ALIGN_RIGHT );
		
		
		//����ҳ��
		_CurrentPage=1;
		_AllPages=rt.body.getPages();
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	
	private boolean _QueryClick = false;
	
	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	private boolean _CreateChick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateChick = true;
	}
	
	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	
	private boolean _QuerClick = false;
	
	public void QuerButton(IRequestCycle cycle) {
		_QuerClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
			getPrintTable();
		}
		if(_CreateChick){
			_CreateChick=false;
			Create();
		}
		if(_DeleteChick){
			_DeleteChick=false;
			Delete();
		}
		if (_QuerClick) {
			_QuerClick = false;
			QuerPass();
		}
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel10(null);
			visit.setDropDownBean10(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean3(null);
			visit.setString2("");
			visit.setDefaultTree(null);
			getDiancmcModels();
		}
		
		if(getYuefValue()!=null){
			String yuef = getYuefValue().getValue();
			if(yuef.length()==1){
				m_wenjlx = "z0"+yuef;
			}else{
				m_wenjlx = "z"+yuef;
			}
		}
		getDiancWjModels();
		getSelectData();
		blnIsBegin = true;
		
		
	}
	public void getSelectData() {
		Visit visit = (Visit)this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("���:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("�·�:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(60);
		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree"
				,""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("��λ����:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("-"));
		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		String Reny[] = getShangbry();
		int intShujzt = getShujzt();
//		���ϵͳ��Ϣ����û�н��н��������û���Ȩ�����ã�ϵͳĬ�ϵ�½���û����н������ݵ�Ȩ��
		if(Reny.length==0){
			if(isDiancUser()){//�糧�û�
				if(intShujzt==0){//����δȷ��
					tb1.addText(new ToolbarText("-"));
					tb1.addText(new ToolbarText("����״̬:"));
					ComboBox ztcb = new ComboBox();
					ztcb.setTransform("DiancWjDropDown");
					ztcb.setWidth(150);
					tb1.addField(ztcb);
					
					tb1.addText(new ToolbarText("-"));
					ToolbarButton crttb = new ToolbarButton(null,"����","function(){"+MainGlobal.getExtMessageShow("���ڴ�������,���Ժ�...", "������...", 200)+";document.getElementById('CreateButton').click();}");
					tb1.addItem(crttb);
					tb1.addText(new ToolbarText("-"));
					ToolbarButton qrtb = new ToolbarButton(null,"ȷ������","function(){document.getElementById('QuerButton').click();}");
					tb1.addItem(qrtb);
					tb1.addText(new ToolbarText("-"));
					ToolbarButton deltb = new ToolbarButton(null,"ɾ��","function(){if(document.getElementById('DiancWjDropDown').value==-1){Ext.MessageBox.alert('��ʾ', '��ѡ��Ҫɾ���ĸ���λ�����ݣ�');}else{"+this.getConfirm("��ʾ","��ȷ��Ҫɾ����", "DeleteButton")+" }}");
					tb1.addItem(deltb);
				}
			}else{//��˾�û�
				tb1.addText(new ToolbarText("-"));
				tb1.addText(new ToolbarText("����״̬:"));
				ComboBox ztcb = new ComboBox();
				ztcb.setTransform("DiancWjDropDown");
				ztcb.setWidth(150);
				tb1.addField(ztcb);
				
				tb1.addText(new ToolbarText("-"));
				ToolbarButton crttb = new ToolbarButton(null,"����","function(){"+MainGlobal.getExtMessageShow("���ڴ�������,���Ժ�...", "������...", 200)+";document.getElementById('CreateButton').click();}");
				tb1.addItem(crttb);
//				tb1.addText(new ToolbarText("-"));
//				ToolbarButton qrtb = new ToolbarButton(null,"ȡ��ȷ��","function(){document.getElementById('QuerButton').click();}");
//				tb1.addItem(qrtb);
				tb1.addText(new ToolbarText("-"));
				ToolbarButton deltb = new ToolbarButton(null,"ɾ��","function(){if(document.getElementById('DiancWjDropDown').value==-1){Ext.MessageBox.alert('��ʾ', '��ѡ��Ҫɾ���ĸ���λ�����ݣ�');}else{"+this.getConfirm("��ʾ","��ȷ��Ҫɾ����", "DeleteButton")+" }}");
				tb1.addItem(deltb);
				
			}
		}else{//�����ϵͳ��Ϣ�����ҵ�����ص����ã��жϵ�½���û��Ƿ�������Ȩ�޵��û�������ǣ�����ʾ����״̬�͡����ա�����ɾ������ť
			if(isDiancUser()){//�糧�û�
				if(intShujzt==0){//����δȷ��
					for(int i=0;i<Reny.length;i++){
						if(visit.getString1().equals(Reny[i])){//�û���Ȩ��
							tb1.addText(new ToolbarText("-"));
							tb1.addText(new ToolbarText("����״̬:"));
							ComboBox ztcb = new ComboBox();
							ztcb.setTransform("DiancWjDropDown");
							ztcb.setWidth(150);
							tb1.addField(ztcb);
							
							tb1.addText(new ToolbarText("-"));
							ToolbarButton crttb = new ToolbarButton(null,"����","function(){"+MainGlobal.getExtMessageShow("���ڴ�������,���Ժ�...", "������...", 200)+";document.getElementById('CreateButton').click();}");
							tb1.addItem(crttb);
							tb1.addText(new ToolbarText("-"));
							ToolbarButton qrtb = new ToolbarButton(null,"ȷ������","function(){"+MainGlobal.getExtMessageShow("���ڴ�������,���Ժ�...", "������...", 200)+";document.getElementById('QuerButton').click();}");
							tb1.addItem(qrtb);
							tb1.addText(new ToolbarText("-"));
							ToolbarButton deltb = new ToolbarButton(null,"ɾ��","function(){if(document.getElementById('DiancWjDropDown').value==-1){Ext.MessageBox.alert('��ʾ', '��ѡ��Ҫɾ���ĸ���λ�����ݣ�');}else{"+this.getConfirm("��ʾ","��ȷ��Ҫɾ����", "DeleteButton")+" }}");
							tb1.addItem(deltb);
							break;
						}
					}
				}
			}else{//��˾�û�
				for(int i=0;i<Reny.length;i++){
					if(visit.getString1().equals(Reny[i])){//�û���Ȩ��
						tb1.addText(new ToolbarText("-"));
						tb1.addText(new ToolbarText("����״̬:"));
						ComboBox ztcb = new ComboBox();
						ztcb.setTransform("DiancWjDropDown");
						ztcb.setWidth(150);
						tb1.addField(ztcb);
						
						tb1.addText(new ToolbarText("-"));
						ToolbarButton crttb = new ToolbarButton(null,"����","function(){"+MainGlobal.getExtMessageShow("���ڴ�������,���Ժ�...", "������...", 200)+";document.getElementById('CreateButton').click();}");
						tb1.addItem(crttb);
//						tb1.addText(new ToolbarText("-"));
//						ToolbarButton qrtb = new ToolbarButton(null,"ȷ������","function(){document.getElementById('QuerButton').click();}");
//						tb1.addItem(qrtb);
						tb1.addText(new ToolbarText("-"));
						ToolbarButton deltb = new ToolbarButton(null,"ɾ��","function(){if(document.getElementById('DiancWjDropDown').value==-1){Ext.MessageBox.alert('��ʾ', '��ѡ��Ҫɾ���ĸ���λ�����ݣ�');}else{"+this.getConfirm("��ʾ","��ȷ��Ҫɾ����", "DeleteButton")+" }}");
						tb1.addItem(deltb);
						break;
					}
				}
			}
		}
		setToolbar(tb1);
	}
	
	private void QuerPass(){
		
		String tableName = "zhibwcqkyb";
		String strMsg = "";
		JDBCcon con = new JDBCcon();
		long diancid = -1;
		String strDianc = "";
		if(isDiancUser()){
			diancid = ((Visit)getPage().getVisit()).getDiancxxb_id();
			strDianc = " and diancxxb_id="+diancid;
		}else{
			diancid = getDiancWjValue().getId();
			if(diancid==-1){
				strDianc = "";
			}else{
				strDianc = " and diancxxb_id="+diancid;
			}
		}
		String nianf = getNianfValue().getValue();
		String yuef = getYuefValue().getValue();
		
		String sql = "update "+tableName+" set shujqrzt=1 where riq=to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd') "+strDianc;
		int i = con.getUpdate(sql);
		if(i<0){
			strMsg = "ȷ��ʧ�ܣ�";
		}else{
			strMsg = "ȷ�ϳɹ���";
		}
		Caozrz(diancid,"����ȷ��",nianf,yuef,strMsg);
		setMsg(strMsg);
		con.Close();
	}

//	*************ɾ��ǰ��ȷ��*****************
	private String getConfirm(String title ,String content,String tapsetryId){
		return "Ext.MessageBox.confirm('"+title+"', '"+content+"', function(btn) {"+
                " if(btn=='yes'){"+MainGlobal.getExtMessageShow("���ڴ�������,���Ժ�...", "������...", 200)+";document.getElementById('"+tapsetryId+"').click();}else{return;}"+
                "})";

	}
	
	public int getShujzt(){
		String tableName = "zhibwcqkyb";
		int shujqrzt = 0;
		JDBCcon con = new JDBCcon();
		
		long diancid = -1;
		if(isDiancUser()){
			diancid = ((Visit)getPage().getVisit()).getDiancxxb_id();
		}else{
			diancid = getDiancWjValue().getId();
		}
		String nianf = getNianfValue().getValue();
		String yuef = getYuefValue().getValue();
		
		String sql = "select shujqrzt from "+tableName+" where riq=to_date('"+nianf+"-"+yuef+"-01','yyyy-mm-dd') and diancxxb_id="+diancid;
		ResultSetList rs = con.getResultSetList(sql);
		if(rs.next()){
			shujqrzt = rs.getInt("shujqrzt");
		}
		con.Close();
		return shujqrzt;
	}
	
	private void Caozrz(long diancxxb_id,String btnName,String nianf,String yuef,String strMsg){//������־
		
		JDBCcon con = new JDBCcon();
		String tableName = "zhibwcqkyb";
		String strReny = ((Visit)getPage().getVisit()).getRenymc();
		String sql = 
			"insert into yuebczrzb\n" +
			"  (id, diancxxb_id, baobmc, caozlx, caozsj, caoztj, caozry, zhixzt)\n" + 
			"values\n" + 
			"  (xl_yuebczrzb_id.nextval, "+diancxxb_id+", '"+tableName+"', '"+btnName+"', sysdate, '"+diancxxb_id+"�糧"+nianf+"��"+yuef+"��"+tableName+"����', '"+strReny+"', '"+strMsg+"')";
		con.getInsert(sql);
		con.Close();
		
	}

	public String[] getShangbry(){//��ȡ���±��ϴ�Ȩ�޵��û���
		JDBCcon con = new JDBCcon();
		
		String sql = "select xt.zhi from xitxxb xt where xt.mingc='�±��ϱ���Ա'";
		ResultSetList rsl = con.getResultSetList(sql);
		String strUserName[] = new String[rsl.getRows()];
		
		for(int i=0;rsl.next();i++){
			strUserName[i] = rsl.getString("zhi");
		}
		con.Close();
		return strUserName;
	}
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
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
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
	
	
	private String m_wenjlx = "";
	public void setWenjlx(String value){
		m_wenjlx=value;
	}
	public String getWenjlx(){
		return m_wenjlx;
	}
	
	
	private void Create() {
		
		String jiesry = ((Visit)getPage().getVisit()).getRenymc();
		String strFileName = getWenjlx();
		String Strsz="";
		 ArrayList strbf=new ArrayList();
		 ArrayList strbf2=new ArrayList();
		 String sql="";
		 JDBCcon con=new JDBCcon();
		 CreateDataFormat data = new CreateDataFormat();
		 String filepath="";
		 try{
			 String pathsql = "select zhi from xitxxb where mingc='�±������ϴ�·��'";
			 ResultSet rspath = con.getResultSet(pathsql);
			 if(rspath.next()){
				 filepath = rspath.getString("zhi");
			 }
			 rspath.close();
			 
			 if(getDiancWjValue()!=null && getDiancWjValue().getId()!=-1){
				 String diancbm = "";
				 String bmsql = "select bianm from diancxxb where id="+getDiancWjValue().getId();
				 ResultSet rsbm = con.getResultSet(bmsql);
				 if(rsbm.next()){
					 diancbm = rsbm.getString("bianm");
				 }
				 rsbm.close();
				 strFileName=strFileName+diancbm.substring(0,1)+diancbm.substring(3)+"M";
			 }
			 FileJx wjjx=new FileJx();
//			 filepath="c:\\zhibwcqk\\";
			 FilePathRead jx=new FilePathRead(strFileName,filepath,true);
			 strbf=jx.getTxtFileList();//�õ��ļ��б�
			 int x=0;	
		 
			 for(int j=0;j<strbf.size();j++){
				 
				 strbf2=wjjx.TextJx(strbf.get(j).toString());//һ���ļ�
				 String shangcsj = wjjx.getWenjrq(strbf.get(j).toString());
				 for(int i=0;i<strbf2.size();i++){
					 
					 Strsz=strbf2.get(i).toString();
					 String value[] = data.getData(Strsz, strFileName);
					 String fenx = "";
					 long diancxxb_id = getProperId(getIDiancbmModel(),Strsz.substring(0, 6));
					 if(x==0){
						 if(strFileName.substring(0, 1).equals("z")){
							 sql="delete from zhibwcqkyb where diancxxb_id="+diancxxb_id+" and riq=to_date('"+getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-1','yyyy-MM-dd')";
						 }
						 con.getDelete(sql);
						 x++;
					 }
					 if(strFileName.substring(0, 1).equals("z")){
						 if(i%2 != 0){
							 fenx = "'�ۼ�'";
						 }else{
							 fenx = "'����'";
						 }
						 
						 sql="insert into zhibwcqkyb (id, riq, diancxxb_id, xuh, fenx, zhibmc, benq, tongq, bianhqk, diancscsj, beiz,jiessj,jiesry,wenjm) values("
							 +"getnewid("+getProperId(getIDiancbmModel(),value[0])+"),to_date('"+getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-1','yyyy-MM-dd'), "
							 +getProperId(getIDiancbmModel(),value[0])+","+Integer.parseInt(value[1])+","+fenx+","
							 +getZhibmc(Integer.parseInt(value[1]))+","+Float.parseFloat(value[3])+", "+Float.parseFloat(value[4])+", "
							 +Float.parseFloat(value[5])+","
							 +" to_date('"+shangcsj+"','yyyy-mm-dd HH24:mi:ss'),'"+value[6]+"',to_date('"+DateUtil.FormatDateTime(new Date())+"','yyyy-mm-dd HH24:mi:ss'),'"+jiesry+"','"+strFileName+"')";
						 
						 con.getInsert(sql);
					 }
					 
				 }
				 x=0;
			 }                            
		 }catch(Exception e){
		 		e.printStackTrace();
		 }finally{
			 con.Close();
		 }
		 setMsg("ȡ�����!");
		 getDiancWjModels();
	}
	
	private void Delete() {
		JDBCcon con=new JDBCcon();
		String sql="";
		String where="where riq=to_date('"+getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-1','yyyy-MM-dd')";
		if(getDiancWjValue().getId()!=-1){
			where = where+" and diancxxb_id="+getDiancWjValue().getId();
		}
//		 if(leix.substring(0, 1).equals("z")){
		 sql="delete from zhibwcqkyb "+where;
//		 }
		 con.getDelete(sql);
		 con.Close();
		 setMsg("ɾ���ɹ�!");
	}

	private String getZhibmc(int index){
		String zhibmc="";
		if(index==1 || index==2){
			zhibmc="'������Ȼú��'";
		
		}else if(index==3 || index==4){
			zhibmc="'����ú������'";
		
		}else if(index==5 || index==6){
			zhibmc="'��¯ú������'";
		
		}else if(index==7 || index==8){
			zhibmc="'������Ȼú����'";
		
		}else if(index==9 || index==10){
			zhibmc="'������ú����'";
		
		}else if(index==11 || index==12){
			zhibmc="'��λȼ�ϳɱ�'";
		
		}else if(index==13 || index==14){
			zhibmc="'������'";
		
		}else if(index==15 || index==16){
			zhibmc="'������'";
		
		}else if(index==17 || index==18){
			zhibmc="'��������'";
		
		}else if(index==19 || index==20){
			zhibmc="'����ú��'";
		
		}else if(index==21 || index==22){
			zhibmc="'������Ȼú��'";
		
		}else if(index==23 || index==24){
			zhibmc="'���ñ�ú��'";
		
		}else if(index==25 || index==26){
			zhibmc="'����úӯ����'";
		
		}else if(index==27 || index==28){
			zhibmc="'����������'";
		
		}else if(index==29 || index==30){
			zhibmc="'����������'";
		
		}else if(index==31 || index==32){
			zhibmc="'����������'";
		
		}else if(index==33 || index==34){
			zhibmc="'ȼ�Ͻ�����'";
		}
		return zhibmc;
	}
	
	
//	�糧����
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel10() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel10();
	}
	
	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean10()==null){
			((Visit)getPage().getVisit()).setDropDownBean10((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean10();
	}
	
	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean10()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean10(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		 setDiancmcModel(new IDropDownModel(sql)) ;
		 return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel10(_value);
	}
		

//	�糧����1
//	private static IPropertySelectionModel _DiancmcModel;
		public IPropertySelectionModel getDiancWjModel() {
			if (((Visit)getPage().getVisit()).getProSelectionModel11() == null) {
				getDiancWjModels();
			}
			return ((Visit)getPage().getVisit()).getProSelectionModel11();
		}

//		private IDropDownBean _DiancmcValue;
		public IDropDownBean getDiancWjValue() {
			if(((Visit)getPage().getVisit()).getDropDownBean11()==null){
				((Visit)getPage().getVisit()).setDropDownBean11((IDropDownBean)getDiancWjModels().getOption(0));
//				_DiancmcValue=(IDropDownBean)getDiancmcModels().getOption(0);
			}
			return ((Visit)getPage().getVisit()).getDropDownBean11();
		}
		
		private boolean _DiancWjChange=false;
		public void setDiancWjValue(IDropDownBean Value) {
			if (((Visit)getPage().getVisit()).getDropDownBean11()==Value) {
				_DiancWjChange = false;
			}else{
				_DiancWjChange = true;
			}
			((Visit)getPage().getVisit()).setDropDownBean11(Value);
		}

		public IPropertySelectionModel getDiancWjModels() {
			
			String yuef = getYuefValue().getValue();
			String nianf = getNianfValue().getValue();
			String strLeix = getWenjlx();
			String strdate = nianf+"-"+yuef+"-01";
			String strDiancID = "";
			if(isDiancUser()){
				strDiancID = "and id="+((Visit)getPage().getVisit()).getDiancxxb_id();
			}
			
			String sql = "select id,mingc,bianm from diancxxb d where d.jib=3 "+strDiancID+" order by mingc desc";
			JDBCcon con = new JDBCcon();
			String filepath="";
			String pathsql = "select zhi from xitxxb where mingc='�±������ϴ�·��'";
			 try{
				 ResultSet rspath = con.getResultSet(pathsql);
				 if(rspath.next()){
					 filepath = rspath.getString("zhi");
				 }
				 rspath.close();
			 }catch(Exception e){
				 e.printStackTrace();
			 }finally{
				 con.Close();
			 }
//			 filepath = "c:\\zhibwcqk\\";
			 
			 ((Visit)getPage().getVisit()).setProSelectionModel11(new FileDropDownModel(sql,strLeix,filepath,strdate));

			return ((Visit)getPage().getVisit()).getProSelectionModel11();
		}

		public void setDiancWjModel(IPropertySelectionModel _value) {
			((Visit)getPage().getVisit()).setProSelectionModel11(_value);
		}
		

//		�糧����
		public boolean _diancbmchange = false;
		private IDropDownBean _DiancbmValue;

		public IDropDownBean getDiancbmValue() {
			if(_DiancbmValue==null){
				_DiancbmValue=(IDropDownBean)getIDiancbmModels().getOption(0);
			}
			return _DiancbmValue;
		}

		public void setDiancbmValue(IDropDownBean Value) {
			long id = -2;
			if (_DiancbmValue != null) {
				id = _DiancbmValue.getId();
			}
			if (Value != null) {
				if (Value.getId() != id) {
					_diancbmchange = true;
				} else {
					_diancbmchange = false;
				}
			}
			_DiancbmValue = Value;
		}

		private IPropertySelectionModel _IDiancbmModel;

		public void setIDiancbmModel(IPropertySelectionModel value) {
			_IDiancbmModel = value;
		}

		public IPropertySelectionModel getIDiancbmModel() {
			if (_IDiancbmModel == null) {
				getIDiancbmModels();
			}
			return _IDiancbmModel;
		}

		public IPropertySelectionModel getIDiancbmModels() {
			
			String sql="";
			
			sql = "select d.id,d.bianm from diancxxb d order by d.mingc";
			
			_IDiancbmModel = new IDropDownModel(sql);
			return _IDiancbmModel;
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
        for (i = 2003; i <= DateUtil.getYear(new Date())+1; i++) {
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
    
    private long getProperId(IPropertySelectionModel _selectModel, String value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();

		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getValue().equals(
					value)) {
				return ((IDropDownBean) _selectModel.getOption(i)).getId();
			}
		}
		return -1;
	}

	
	public String getcontext() {
			return "";
//		return "var  context='http://"
//				+ this.getRequestCycle().getRequestContext().getServerName()
//				+ ":"
//				+ this.getRequestCycle().getRequestContext().getServerPort()
//				+ ((Visit) getPage().getVisit()).get() + "';";
	}

	public SortMode getSort() {
		return SortMode.USER;
	}
	
	private String _pageLink;

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page����
	protected void initialize() {
		_pageLink = "";
	}
}
