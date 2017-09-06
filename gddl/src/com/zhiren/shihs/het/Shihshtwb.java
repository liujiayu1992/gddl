package com.zhiren.shihs.het;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import bsh.Interpreter;

import com.zhiren.common.DataBassUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.jt.het.hetmb.Fahxxbean;
import com.zhiren.jt.het.hetmb.Hetxxbean;
import com.zhiren.jt.het.hetmb.Zengkkbean;
import com.zhiren.jt.het.hetmb.Zhilyqbean;
import com.zhiren.jt.het.hetmb.jijbean;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Shihshtwb extends BasePage implements PageValidateListener {


	public boolean getRaw() {
		return true;
	}

	private int _CurrentPage = -1;

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}

//	private int _Flag = 0;
//
//	public int getFlag() {
//		JDBCcon con = new JDBCcon();
//		String sql = "SELECT ZHUANGT FROM JICXXB WHERE MINGC = '�����еĳ����Ƿ���ʾ'";
//		ResultSet rs = con.getResultSet(sql);
//		try {
//			if (rs.next()) {
//				_Flag = rs.getInt("ZHUANGT");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return _Flag;
//	}
//
//	public void setFlag(int _value) {
//		_Flag = _value;
//	}

	private String REPORT_NAME_HETDY = "Hetdy";// 

	private String mstrReportName = "";

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
//		blnIsBegin = false;
//		if (mstrReportName.equals(REPORT_NAME_HETDY)) {
		
			return getShihshtdy();
		
			
//		} else {
//			return "�޴˱���";
//		}
	}
//	private void loadBean (Object obj,"Hetxxbean"){
//		
//	}
	
	private String getShihshtdy(){
		
		
		
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		String[] gongs=new String[3];
		try{

			String sql="select id\n" +
			"from (\n" + 
			"select id,leix,decode(mingc,'�����������ַ���',1,'�۸��������ַ���',2,3)xuh,mingc\n" + 
			"from gongsb\n" + 
			"where gongsb.zhuangt=1 and leix='��ͬ'\n" + 
			")\n" + 
			"order by xuh";
			ResultSetList rs0=con.getResultSetList(sql);
			DataBassUtil clob=new DataBassUtil();
			int k=0;
			while(rs0.next()){
				//gongs[k++]=rs0.getString(1);
				gongs[k++]=clob.getClob("gongsb", "gongs", rs0.getLong(0));
				
			}
//��ͬ
		StringBuffer buffer = new StringBuffer("");
		buffer.append("select  shihhtb.id,hetsl,sg.quanc GONGFDWMC,bianh HETBH,dc.quanc XUFDWMC,to_char(qiandrq,'YYYY-MM-DD') QIANDRQ,qianddd,to_char(qisrq,'YYYY-MM-DD') QISRQ,to_char(jiesrq,'YYYY-MM-DD') GUOQRQ,sg.danwdz GONGFDWDZ,sg.faddbr GONGFFDDBR,sg.weitdlr GONGFWTDLR,sg.dianh GONGFDH,sg.chuanz GONGFDBGH,sg.kaihyh GONGFKHYH,sg.zhangh GONGFZH,sg.youzbm GONGFYZBM,sg.shuih GONGFSH,");
		buffer.append("dc.faddbr XUFFDDBR,dc.weitdlr XUFWTDLR,dc.youzbm XUFYZBM,dc.kaihyh XUFKHYH,dc.zhangh XUFZH,dc.diz XUFDWDZ,dc.dianh XUFDH,dc.chuanz XUFDBGH,dc.shuih XUFSH" );
		buffer.append(" from shihhtb,shihgysb sg,diancxxb dc  where shihhtb.shihgysb_id=sg.id and shihhtb.diancxxb_id=dc.id " );
		buffer.append(" and shihhtb.id=" +((Visit) getPage().getVisit()).getLong1() );
		
		
		Hetxxbean bean=new Hetxxbean();
		ResultSet rs=con.getResultSet(buffer);
		String qiandrq="";
		String QISRQ="";
		String GUOQRQ="";
		String jihkjmc="";
		String fahr="";
		String zhibmc="";
		String tiaojmc="";
		String danwmc="";
		String hetsl="";
		String fanw="";
		String jigdw="";
		String jij="";
		if(rs.next()){
			bean.setHetbh(rs.getString("HETBH"));
			bean.setQianddd(rs.getString("QIANDDD"));
			bean.setGONGFDWMC(rs.getString("GONGFDWMC"));
			bean.setXUFDWMC(rs.getString("XUFDWMC"));
			bean.setGONGFDWDZ(rs.getString("GONGFDWDZ"));
			bean.setGONGFDH(rs.getString("GONGFDH"));
			bean.setGONGFFDDBR(rs.getString("GONGFFDDBR"));
			bean.setGONGFWTDLR(rs.getString("GONGFWTDLR"));
			bean.setGONGFDBGH(rs.getString("GONGFDBGH"));
			bean.setGONGFKHYH(rs.getString("GONGFKHYH"));
			bean.setGONGFZH(rs.getString("GONGFZH"));
			bean.setGONGFYZBM(rs.getString("GONGFYZBM"));
			bean.setGongfsh(rs.getString("GONGFSH"));
			bean.setXUFDWDZ(rs.getString("XUFDWDZ"));
			bean.setXUFFDDBR(rs.getString("XUFFDDBR"));
			bean.setXUFWTDLR(rs.getString("XUFWTDLR"));
			bean.setXUFDH(rs.getString("XUFDH"));
			bean.setXUFDBGH(rs.getString("XUFDBGH"));
			bean.setXUFKHYH(rs.getString("XUFKHYH"));
			bean.setXUFZH(rs.getString("XUFZH"));
			bean.setXUFYZBM(rs.getString("XUFYZBM"));
			bean.setXufsh(rs.getString("XUFSH"));
			qiandrq=rs.getString("QIANDRQ");
			QISRQ=rs.getString("QISRQ");
			GUOQRQ=rs.getString("GUOQRQ");
			
			hetsl=rs.getString("HETSL");
			
		
		}

		buffer.setLength(0);
		
		buffer.append("  select dw.mingc danwmc, tj.mingc tiaojmc,zb.mingc zhibmc,sj.jij,jigdw.JIGDW,");
		buffer.append("  decode(tj.mingc,'���ڵ���',sj.xiax,'����',sj.xiax,'С�ڵ���',sj.shangx,'С��',sj.shangx,'����',sj.xiax,'����','['||sj.xiax||'-'||sj.shangx||']') fanw");
		buffer.append("  from danwb dw ,shihhtjg  sj, shihhtb ht,zhibb zb,tiaojb tj, (select danwb.mingc JIGDW from danwb,shihhtjg  where danwb.id=shihhtjg.jijdwid) jigdw   where ht.id=sj.shihhtb_id and  ");
		buffer.append("  sj.danwb_id=dw.id and  sj.zhibb_id=zb.id  and sj.tiaojb_id=tj.id and ht.id="+((Visit) getPage().getVisit()).getLong1());
		
		
	    rs=con.getResultSet(buffer);
	    boolean isShow=rs.next();
	    
	  
	    if(isShow){
	    	
	    	zhibmc=rs.getString("ZHIBMC");
			tiaojmc=rs.getString("TIAOJMC");
			danwmc=rs.getString("DANWMC");
			
			fanw=rs.getString("FANW");
			jigdw=rs.getString("JIGDW");
			jij=rs.getString("JIJ");
	    }
		
		int qittkRows=0;
		
		//////////////////////////////////////////////////
//�����ݡ�ǿ�кϲ����в���
		String[][] ArrHeader = new String[1][18];
		int ArrWidth[]=new int[] {104,32,32,42,42,45,35,35,35,35,35,35,35,35,35,35,35,35};
		rt.setTitle("ʯ �� ʯ �� �� �� ͬ", ArrWidth);
		int iRows=0;
		int iHeadRows=4;
		int iShulRows=1;
		int iZhilRows=1;
		int iJiagRows=0;//zengkkBeans.size();jiagBeans.size()+
		int iQitRows=qittkRows;
		int iGongxfRows=11;
		int iYouxq=1;
		final int HANGJJ=30;//�м��
		iRows=iHeadRows+iShulRows+3+iZhilRows+1+iJiagRows+1+iQitRows+1+iGongxfRows+iYouxq+2;
		if(fahr==""){
			iRows-=1;
		}
		rt.setBody(new Table(iRows,18));
		rt.body.setWidth(ArrWidth);
		
		rt.body.setCellValue(1, 1, "<font size=2><b>������:</b></font>"+bean.getGONGFDWMC());
		rt.body.mergeCell(1, 1, 1, 12);
		rt.body.setCellValue(1, 13,  "<font size=2><b>��ͬ���:</b></font>"+bean.getHetbh());
		rt.body.mergeCell(1, 13, 1, 18);
		
		rt.body.setCellValue(2, 13, "<b><font size=2>ǩ������:</b></font>"+qiandrq);
		rt.body.mergeCell(2, 13, 2, 18);
		
		rt.body.setCellValue(3, 1, "<font size=2><b>������:</b></font>"+bean.getXUFDWMC());
		rt.body.mergeCell(3, 1, 3, 12);
		rt.body.mergeCell(3, 13, 3, 18);
		rt.body.setCellValue(3, 13, "<font size=2><b>ǩ���ص�:</b></font>"+bean.getQianddd());
		rt.body.setRowHeight(3,HANGJJ);
		rt.body.setCellVAlign(3, 1,Table.VALIGN_TOP);
		rt.body.setCellVAlign(3, 13,Table.VALIGN_TOP);
		
		rt.body.setCellValue(4, 1, "&nbsp;&nbsp;&nbsp;&nbsp;���ݡ��л����񹲺͹����ú�ͬ�������������Ʒ������ͬ�����������ع�ƽ��������ԭ�򣬾�˫��Э��һ�£�" +"�������Э�飬ǩ");
		rt.body.mergeCell(4, 1, 4, 18);
		rt.body.setCellValue(5, 1, "����ͬԼ��˫����ͬ���С�");
		rt.body.mergeCell(5, 1, 5, 18);
		
		rt.body.setCellValue(6, 1, "<font size=2><b>һ��ʯ��ʯ��ͬ����</b></font>");
		rt.body.setRowHeight(6,30);
		rt.body.setCellVAlign(6, 1,Table.VALIGN_BOTTOM);
		rt.body.mergeCell(6, 1, 6, 18);
	
		rt.body.merge(7,1, 9,18);
		rt.body.setCellValue(7, 1, "����: "+hetsl+"(��)");
	//	rt.body.mergeCell(7, 10, 9, 18);
	//	rt.body.setCellValue(7,2, hetsl+"(��)");
		//rt.body.mergeCell(7, 10, 9, 18);
	//	rt.body.mergeCell(7,9,7,18);
		
		rt.body.setRowClassName(7, "clearBorder");
	//	rt.body.setRowHeight(8,0);
		
		
		int fahrow=0;//����������
		
		int zhilrowInd=fahrow+iShulRows+9;//����������
		rt.body.setRowHeight(zhilrowInd,HANGJJ);
		rt.body.setCellVAlign(zhilrowInd, 1,Table.VALIGN_BOTTOM);
		rt.body.setCellValue(zhilrowInd, 1, "<font size=2><b>����ʯ��ʯ�۸�");
		rt.body.mergeCell(zhilrowInd, 1, zhilrowInd, 18);
		
		
		int jiagrowInd=iZhilRows+zhilrowInd+1;//�۸�����
		
		String jiagstr=zhibmc+tiaojmc+fanw+danwmc;
		if(!isShow){
			
			jiagstr="û���������";
		}else if(jiagstr.equals("")){
			
			jiagstr+="�۸���: "+(!((jij+jigdw).equals(""))?(jij+jigdw):"û���������");
		}else{
			jiagstr+=","+"�۸���: "+(!((jij+jigdw).equals(""))?(jij+jigdw):"û���������");
		}
		rt.body.setCellValue(zhilrowInd+1, 1,jiagstr);
	
		rt.body.mergeCell(zhilrowInd+1, 1, zhilrowInd+1,18);
		
		int qittkrow=iJiagRows+jiagrowInd+1;//��������������
		
		rt.body.setRowHeight(qittkrow,HANGJJ);
		rt.body.setCellVAlign(qittkrow, 1,Table.VALIGN_BOTTOM);
		rt.body.setCellValue(qittkrow, 1, "<font size=2><b>������������: ��</b></font>");
		rt.body.mergeCell(qittkrow, 1, qittkrow, 18);
		

		int gongxfrow=qittkrow+qittkRows+1;
		
		rt.body.setRowHeight(gongxfrow,HANGJJ);
		rt.body.setCellVAlign(gongxfrow, 1,Table.VALIGN_BOTTOM);
		rt.body.setCellVAlign(gongxfrow, 9,Table.VALIGN_BOTTOM);
		rt.body.setCellVAlign(gongxfrow, 2,Table.VALIGN_BOTTOM);
		rt.body.setCellVAlign(gongxfrow, 12,Table.VALIGN_BOTTOM);
		
		rt.body.setCellValue(gongxfrow, 1, "<font size=2><b>����</b></font>");
		rt.body.mergeCell(gongxfrow, 1, gongxfrow, 8);
		rt.body.setCellValue(gongxfrow, 9, "<font size=2><b>�跽</b></font>");
		rt.body.mergeCell(gongxfrow, 9, gongxfrow, 18);
		rt.body.setCellAlign(gongxfrow, 9, Table.ALIGN_CENTER);
		rt.body.setCellAlign(gongxfrow, 1, Table.ALIGN_CENTER);
		
		rt.body.setCellValue(gongxfrow+1, 1, "��λ����(��):");
		rt.body.setCellValue(gongxfrow+1, 2, bean.getGONGFDWMC());
		rt.body.setCellValue(gongxfrow+1, 9, "��λ����(��):");
		rt.body.setCellValue(gongxfrow+1, 12, bean.getXUFDWMC());
		rt.body.mergeCell(gongxfrow+1, 2, gongxfrow+1, 8);
		rt.body.mergeCell(gongxfrow+1, 12, gongxfrow+1, 18);
		rt.body.mergeCell(gongxfrow+1, 9, gongxfrow+1, 11);
		rt.body.setCellAlign(gongxfrow+1, 9, Table.ALIGN_RIGHT);
		
		
		rt.body.setCellValue(gongxfrow+2, 1, "��λ��ַ:");
		rt.body.setCellValue(gongxfrow+2, 2, bean.getGONGFDWDZ());
		rt.body.setCellValue(gongxfrow+2, 9, "��λ��ַ:");
		rt.body.setCellValue(gongxfrow+2, 12,bean.getXUFDWDZ());
		rt.body.mergeCell(gongxfrow+2, 2, gongxfrow+2, 8);
		rt.body.mergeCell(gongxfrow+2, 12, gongxfrow+2, 18);
		rt.body.mergeCell(gongxfrow+2, 9, gongxfrow+2, 11);
		rt.body.setCellAlign(gongxfrow+2, 9, Table.ALIGN_RIGHT);
		
		
		rt.body.setCellValue(gongxfrow+3, 1, "����������:");
		rt.body.setCellValue(gongxfrow+3, 2, bean.getGONGFFDDBR());
		rt.body.setCellValue(gongxfrow+3, 9, "����������:");
		rt.body.setCellValue(gongxfrow+3, 12, bean.getXUFFDDBR());
		rt.body.mergeCell(gongxfrow+3, 2, gongxfrow+3, 8);
		rt.body.mergeCell(gongxfrow+3, 12, gongxfrow+3, 18);
		rt.body.mergeCell(gongxfrow+3, 9, gongxfrow+3, 11);
		rt.body.setCellAlign(gongxfrow+3, 9, Table.ALIGN_RIGHT);
		
		rt.body.setCellValue(gongxfrow+4, 1, "ί�д�����:");
		rt.body.setCellValue(gongxfrow+4, 2, bean.getGONGFWTDLR());
		rt.body.setCellValue(gongxfrow+4, 9, "ί�д�����:");
		rt.body.setCellValue(gongxfrow+4, 12, bean.getXUFWTDLR());
		rt.body.mergeCell(gongxfrow+4, 2, gongxfrow+4, 8);
		rt.body.mergeCell(gongxfrow+4, 12, gongxfrow+4, 18);
		rt.body.mergeCell(gongxfrow+4, 9, gongxfrow+4, 11);
		rt.body.setCellAlign(gongxfrow+4, 9, Table.ALIGN_RIGHT);
		
		
		rt.body.setCellValue(gongxfrow+5, 1, "�绰:");
		rt.body.setCellValue(gongxfrow+5, 2,bean.getGONGFDH());
		rt.body.setCellValue(gongxfrow+5, 9, "�绰:");
		rt.body.setCellValue(gongxfrow+5, 12, bean.getXUFDH());
		rt.body.mergeCell(gongxfrow+5, 2, gongxfrow+5, 8);
		rt.body.mergeCell(gongxfrow+5, 12, gongxfrow+5, 18);
		rt.body.mergeCell(gongxfrow+5, 9, gongxfrow+5, 11);
		rt.body.setCellAlign(gongxfrow+5, 9, Table.ALIGN_RIGHT);
		
		
		rt.body.setCellValue(gongxfrow+6, 1, "�����:");
		rt.body.setCellValue(gongxfrow+6, 2, bean.getGONGFDBGH());
		rt.body.setCellValue(gongxfrow+6, 9, "�����:");
		rt.body.setCellValue(gongxfrow+6, 12, bean.getXUFDBGH());
		rt.body.mergeCell(gongxfrow+6, 2, gongxfrow+6, 8);
		rt.body.mergeCell(gongxfrow+6, 12, gongxfrow+6, 18);
		rt.body.mergeCell(gongxfrow+6, 9, gongxfrow+6, 11);
		rt.body.setCellAlign(gongxfrow+6, 9, Table.ALIGN_RIGHT);
		
		
		rt.body.setCellValue(gongxfrow+7, 1, "��������:");
		rt.body.setCellValue(gongxfrow+7,2, bean.getGONGFKHYH());
		rt.body.setCellValue(gongxfrow+7, 9, "��������:");
		rt.body.setCellValue(gongxfrow+7, 12, bean.getXUFKHYH());
		rt.body.mergeCell(gongxfrow+7, 2, gongxfrow+7, 8);
		rt.body.mergeCell(gongxfrow+7, 12, gongxfrow+7, 18);
		rt.body.mergeCell(gongxfrow+7, 9, gongxfrow+7, 11);
		rt.body.setCellAlign(gongxfrow+7, 9, Table.ALIGN_RIGHT);
		
		rt.body.setCellValue(gongxfrow+8, 1, "�˺�:");
		rt.body.setCellValue(gongxfrow+8, 2, bean.getGONGFZH());
		rt.body.setCellValue(gongxfrow+8, 9, "�˺�:");
		rt.body.setCellValue(gongxfrow+8, 12, bean.getXUFZH());
		rt.body.mergeCell(gongxfrow+8, 2, gongxfrow+8, 8);
		rt.body.mergeCell(gongxfrow+8, 12, gongxfrow+8, 18);
		rt.body.mergeCell(gongxfrow+8, 9, gongxfrow+8, 11);
		rt.body.setCellAlign(gongxfrow+8, 9, Table.ALIGN_RIGHT);
		
		
		rt.body.setCellValue(gongxfrow+9, 1, "��������:");
		rt.body.setCellValue(gongxfrow+9, 2, bean.getGONGFYZBM());
		rt.body.setCellValue(gongxfrow+9, 9, "��������:");
		rt.body.setCellValue(gongxfrow+9, 12, bean.getXUFYZBM());
		rt.body.mergeCell(gongxfrow+9, 2, gongxfrow+9, 8);
		rt.body.mergeCell(gongxfrow+9, 12, gongxfrow+9, 18);
		rt.body.mergeCell(gongxfrow+9, 9, gongxfrow+9, 11);
		rt.body.setCellAlign(gongxfrow+9, 9, Table.ALIGN_RIGHT);
		
		
		rt.body.setCellValue(gongxfrow+10, 1, "˰��:");
		rt.body.setCellValue(gongxfrow+10, 2, bean.getGongfsh());
		rt.body.setCellValue(gongxfrow+10,9, "˰��:");
		rt.body.setCellValue(gongxfrow+10, 12, bean.getXufsh());
		rt.body.mergeCell(gongxfrow+10, 2, gongxfrow+10, 8);
		rt.body.mergeCell(gongxfrow+10, 12, gongxfrow+10, 18);
		rt.body.mergeCell(gongxfrow+10, 9, gongxfrow+10, 11);
		rt.body.setCellAlign(gongxfrow+10, 9, Table.ALIGN_RIGHT);
		
		rt.body.setRowHeight(gongxfrow+11,HANGJJ);
		rt.body.setCellVAlign(gongxfrow+11, 1,Table.VALIGN_BOTTOM);
		rt.body.setCellValue(gongxfrow+11, 1, "<font size=2><b>��Ч����:"+QISRQ+"��"+GUOQRQ+"</b></font>");
		rt.body.mergeCell(gongxfrow+11, 1, gongxfrow+11, 18);
		rt.body.setCellAlign(gongxfrow+11, 1, Table.ALIGN_LEFT);
//���ñ߿�	
		rt.body.setBorderNone();
		rt.body.setCells(1,1,gongxfrow+11,18,Table.PER_BORDER_BOTTOM,0);
		rt.body.setCells(1,1,gongxfrow+11,18,Table.PER_BORDER_RIGHT,0);
		//7�е�ShulRows+8��������
	
		rt.body.setCellBorderbottom(gongxfrow+1, 2, 1);
		rt.body.setCellBorderbottom(gongxfrow+1, 12, 1);
		rt.body.setCellBorderbottom(gongxfrow+1, 2, 1);
		rt.body.setCellBorderbottom(gongxfrow+1, 12, 1);
		rt.body.setCellBorderbottom(gongxfrow+2, 2, 1);
		rt.body.setCellBorderbottom(gongxfrow+2, 12, 1);
		rt.body.setCellBorderbottom(gongxfrow+3, 2, 1);
		rt.body.setCellBorderbottom(gongxfrow+3, 12, 1);
		rt.body.setCellBorderbottom(gongxfrow+4, 2, 1);
		rt.body.setCellBorderbottom(gongxfrow+4, 12, 1);
		rt.body.setCellBorderbottom(gongxfrow+5, 2, 1);
		rt.body.setCellBorderbottom(gongxfrow+5, 12, 1);
		rt.body.setCellBorderbottom(gongxfrow+5, 2, 1);
		rt.body.setCellBorderbottom(gongxfrow+5, 12, 1);
		rt.body.setCellBorderbottom(gongxfrow+6, 2, 1);
		rt.body.setCellBorderbottom(gongxfrow+6, 12, 1);
		rt.body.setCellBorderbottom(gongxfrow+7, 2, 1);
		rt.body.setCellBorderbottom(gongxfrow+7, 12, 1);
		rt.body.setCellBorderbottom(gongxfrow+8, 2, 1);
		rt.body.setCellBorderbottom(gongxfrow+8, 12, 1);
		rt.body.setCellBorderbottom(gongxfrow+9, 2, 1);
		rt.body.setCellBorderbottom(gongxfrow+9, 12, 1);
		rt.body.setCellBorderbottom(gongxfrow+10, 2, 1);
		rt.body.setCellBorderbottom(gongxfrow+10, 12, 1);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return rt.getAllPagesHtml();
		
	}
	
	


	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
//			visit.setActivePageName(getPageName().toString());
			visit.setList7(null);
			visit.setList8(null);
			visit.setList9(null);
			visit.setList10(null);
		}
		
		
		if (cycle.getRequestContext().getParameter("hetb_id") != null&&!cycle.getRequestContext().getParameter("hetb_id").equals("-1")) {
			visit.setLong1(Long.parseLong(cycle.getRequestContext().getParameter("hetb_id")));
			
		}else{
			blnIsBegin = false;
			return;
		}
		blnIsBegin = true;
	}

	public int getZhuangt() {
		return 1;
	}

	private int intZhuangt=0;
	public void setZhuangt(int _value) {
		intZhuangt=1;
	}
//	��ʽ���������Ӳ�س����Զ������������� ����ʾ������Ϊ
	/**
	 * private String[] getRows(String strValue,int rowSize);
	 */
	private  List getRows(String strValue,int rowSize){
	String[] tmp = strValue.split("\n");
	List result=new ArrayList();
	String TemChars="";
	String TemChar="";
	int j=0;
	int c=0;
	byte[] b=new byte[4];
	try{
		for(int i=0;i<tmp.length;i++){
			if(tmp[i].length()*2<rowSize){//�����Ӳ������ַ�����涨�ַ��Ƚϣ����С���򲻱�Ҫ����
				result.add(tmp[i]);
				continue;
			}else{//���Ӳ������ַ�����涨�ַ����Ƚϣ����������Ҫ����
				for(int k=0;k<tmp[i].length();k++){
					if(c<rowSize){//С�ڹ涨�ַ���ʱ�����ۼ�
						TemChar=tmp[i].substring(k, k+1);
						TemChars+=TemChar;
//						b=TemChar.getBytes("unicode");
//						if(b[3]==-1){//ȫ��
//							c+=2;
//						}else{
//							c++;
//						}
						if(isLetter(TemChar.toCharArray()[0])){
							c++;
						}else{
							c+=2;
						}
					}else{
						result.add(TemChars);
						TemChars="";
						TemChar="";
						c=0;
						k--;
					}
				}
				if(!TemChars.equals("\r")){
					result.add(TemChars);
				}
				TemChars="";
				TemChar="";
				c=0;
			}
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	return result;
}
	 public  boolean isLetter(char c) {
	        int k = 0x80;
	        return c / k == 0 ? true : false;
	 }

	public void pageValidate(PageEvent arg0) {
		// TODO �Զ����ɷ������
		
	}

}
