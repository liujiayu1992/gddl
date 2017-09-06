package com.zhiren.jt.dtsx;

/*
 * ���ߣ�huochaoyuan
 * ʱ�䣺2009-09-07
 * ������Ϊ����������������ר�뵥�����ڹ��õ�ת�뵥����̫���������̫�࣬���������Ա���
 * ������һ��һ�Ƶ糧
 * ��Դ�����ã�Zhuanmd_sx&lx=zhongc/daohrq/fahrq&num1=0&num2=1(0:ú�����Ƶ���Ϣ��1��һ�����룻2���������룻3:��������)
 */


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Zhuanmd_sx extends BasePage {
	private boolean bigFontSize = false;

	public boolean getRaw() {
		return true;
	}

	private String userName = "";

	public void setUserName(String value) {
		userName = ((Visit) getPage().getVisit()).getRenymc();
	}

	public String getUserName() {
		return userName;
	}

	// private boolean reportShowZero(){
	// return ((Visit) getPage().getVisit()).isReportShowZero();
	// }

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

	// ***************������Ϣ��******************//
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

	public long getDiancxxbId() {

		return ((Visit) getPage().getVisit()).getDiancxxb_id();
	}

	public boolean isJTUser() {
		return ((Visit) getPage().getVisit()).isJTUser();
	}

	private String Zhongcsj = "zhongc";//�س�����
	
	private String Daohrq="daohrq";//��������
	
	private String Fahrq="fahrq";//��������

	private boolean blnIsBegin = false;

	// private String leix = "";

	private String mstrReportName = "";

	public String getPrintTable() {
		setMsg(null);
		Visit visit=(Visit) getPage().getVisit();
		if (!blnIsBegin) {
			return "";
		}
        
		blnIsBegin = false;

		if (mstrReportName.equals(Zhongcsj)) {
			return getZhuanmad_Zhongcsj(visit.getInt1(),visit.getInt2(),visit.getString4());
		} else if (mstrReportName.equals(Daohrq)) {
			return getZhuanmad_Daohrq(visit.getInt1(),visit.getInt2(),visit.getString4());
		}else if (mstrReportName.equals(Fahrq)) {
			return getZhuanmad_Fahrq(visit.getInt1(),visit.getInt2(),visit.getString4());
		}else {
			return "�޴˱���";
		}
	}

	private String getZhuanmad_Fahrq(int a, int b,String hq) {
		JDBCcon cn = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		int cybmwidth = 150;
		int hybmwidth = 100;
		int fontSize = 9;
		if (bigFontSize) {
			cybmwidth = 300;
			hybmwidth = 300;
			fontSize = 28;
		}
		sb.append("select a.bianm as bianm1,b.bianm as bianm2 \n");
	   if (a == 0) {
			// ����+�𳵳���/�������䵥λ
			sb.append("from\n");

			sb
					.append("(select y.zhilblsb_id as zhillsb_id,j.mingc as bianm,j.mingc as mingc\n");
			sb.append("from\n");
			sb
					.append("("); 
			if(!hq.equals("h")){				
			sb.append("select distinct zhilb_id,(m.mingc||'<p>('||f.chec||','||p.mingc||','||decode(y.mingc,null,'',y.mingc)||')') as mingc\n");
			sb.append("from chepb c, fahb f,meikxxb m,yunsdwb y,pinzb p\n");
			sb.append("where c.fahb_id = f.id\n");
			sb.append("and f.meikxxb_id=m.id\n");
			sb.append("and c.yunsdwb_id=y.id(+)\n");
			sb.append("and f.pinzb_id=p.id(+)\n");
			sb.append("and f.yunsfsb_id !=1\n");
			sb
					.append("and to_date(to_char(f.fahrq, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
							+ getRiqi() + "', 'yyyy-mm-dd')\n");
			}
//			sb
//					.append("or to_date(to_char(c.qingcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =to_date('"
//							+ getRiqi() + "', 'yyyy-mm-dd'))\n");
			if(!hq.equals("q")){
			if(!hq.equals("h")){	
			sb.append("union\n");
			}
			sb
					.append("select distinct zhilb_id,(m.mingc||'<p>('||f.chec||','||p.mingc||','||decode(y.mingc,null,'',y.mingc)||')') as mingc\n");
			sb.append("from chepb c, fahb f,meikxxb m,yunsdwb y,pinzb p\n");
			sb.append("where c.fahb_id = f.id\n");
			sb.append("and f.meikxxb_id=m.id\n");
			sb.append("and c.yunsdwb_id=y.id(+)\n");
			sb.append("and f.meikxxb_id=m.id\n");
			sb.append("and f.pinzb_id=p.id(+)\n");
			sb.append("and f.yunsfsb_id=1\n");
			sb
					.append("and to_date(to_char(f.fahrq, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
							+ getRiqi() + "', 'yyyy-mm-dd')\n");
			}
			sb.append(")j,caiyb c,yangpdhb y\n");
			sb.append("where  j.zhilb_id=c.zhilb_id\n");
			sb.append("and y.caiyb_id=c.id)a\n");

		}  else {
			sb.append("from(\n");
			sb.append("select  z.zhillsb_id,z.bianm,zz.mingc\n");
			sb.append("from\n");

			sb.append("(");
			if(!hq.equals("h")){
			sb.append("select distinct zhilb_id\n");
			sb.append("from chepb c, fahb f\n");
			sb.append("where c.fahb_id = f.id\n");
			sb.append("and f.yunsfsb_id !=1\n");
			sb
					.append("and to_date(to_char(f.fahrq, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
							+ getRiqi() + "', 'yyyy-mm-dd')\n");
			}
//			sb
//					.append("or to_date(to_char(c.qingcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =to_date('"
//							+ getRiqi() + "', 'yyyy-mm-dd'))\n");
			if(!hq.equals("q")){
				if(!hq.equals("h")){	
					sb.append("union\n");
					}
			sb.append("select distinct zhilb_id\n");
			sb.append("from chepb c, fahb f\n");
			sb.append("where c.fahb_id = f.id\n");
			sb.append("and f.yunsfsb_id=1\n");
			sb
					.append("and to_date(to_char(f.fahrq, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
							+ getRiqi() + "', 'yyyy-mm-dd')\n");
			}
			sb.append(") j,\n");
			sb.append("caiyb c,yangpdhb y,zhuanmb z,zhuanmlb zz\n");
			sb.append("where  j.zhilb_id=c.zhilb_id\n");
			sb.append("and y.caiyb_id=c.id\n");
			sb.append("and z.zhillsb_id=y.zhilblsb_id\n");
			sb.append("and z.zhuanmlb_id=zz.id\n");
			sb.append("and zz.jib=" + a + ") a\n");
		}
		sb.append(",\n");
		sb.append("(select  z.zhillsb_id,z.bianm,zz.mingc\n");
		sb.append("from\n");
		sb.append("(");
		if(!hq.equals("h")){
		sb.append("select distinct zhilb_id\n");
		sb.append("from chepb c, fahb f\n");
		sb.append("where c.fahb_id = f.id\n");
		sb.append("and f.yunsfsb_id !=1\n");
		sb
				.append("and to_date(to_char(f.fahrq, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
						+ getRiqi() + "', 'yyyy-mm-dd')\n");
		}
//		sb
//				.append("or to_date(to_char(c.qingcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =to_date('"
//						+ getRiqi() + "', 'yyyy-mm-dd'))\n");
		if(!hq.equals("q")){
			if(!hq.equals("h")){	
				sb.append("union\n");
				}
		sb.append("select distinct zhilb_id\n");
		sb.append("from chepb c, fahb f\n");
		sb.append("where c.fahb_id = f.id\n");
		sb.append("and f.yunsfsb_id=1\n");
		sb
				.append("and to_date(to_char(f.fahrq, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
						+ getRiqi() + "', 'yyyy-mm-dd')\n");
		}
		sb.append(") j,\n");
		sb.append("caiyb c,yangpdhb y,zhuanmb z,zhuanmlb zz\n");
		sb.append("where  j.zhilb_id=c.zhilb_id\n");
		sb.append("and y.caiyb_id=c.id\n");
		sb.append("and z.zhillsb_id=y.zhilblsb_id\n");
		sb.append("and z.zhuanmlb_id=zz.id\n");
		sb.append("and zz.jib=" + b + ") b\n");
		sb.append("where a.zhillsb_id=b.zhillsb_id\n");
		sb.append("order by a.zhillsb_id");

		ResultSet rs = cn.getResultSet(sb.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][2];
		String mingc1 = "";
		String mingc2 = "";
		ResultSet rss = cn.getResultSet("select mingc,jib from zhuanmlb\n"
				+ "union\n" + "select 'ú������' as mingc,0 as jib from dual\n"
				+ "union\n" + "select 'ú������' as mingc,-1 as jib from dual\n"
				+ "union\n" + "select 'ú������' as mingc,-2 as jib from dual");

		try {
			while (rss.next()) {
				if (rss.getInt("jib") == a) {
					mingc1 = rss.getString("mingc");
				}
				if (rss.getInt("jib") == b) {
					mingc2 = rss.getString("mingc");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ArrHeader[0] = new String[] { mingc1, mingc2 };

		int ArrWidth[] = new int[] { cybmwidth, hybmwidth };

		rt.setTitle("ת�뵥", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		// rt.body.mergeFixedRow();
		rt.body.setCells(1, 1, rt.body.getRows(), 2, Table.PER_ALIGN,
				Table.ALIGN_CENTER);

		rt.body.setFontSize(fontSize);

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		// rt.setDefautlFooter(7, 1, "���:", Table.ALIGN_RIGHT);
		// rt.setDefautlFooter(9, 2, "�Ʊ�:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	
	private String getZhuanmad_Zhongcsj(int a, int b,String hq) {
		JDBCcon cn = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		int cybmwidth = 150;
		int hybmwidth = 100;
		int fontSize = 9;
		if (bigFontSize) {
			cybmwidth = 300;
			hybmwidth = 300;
			fontSize = 28;
		}
		sb.append("select a.bianm as bianm1,b.bianm as bianm2 \n");
	   if (a == 0) {
			// ����+�𳵳���/�������䵥λ
			sb.append("from\n");

			sb
					.append("(select y.zhilblsb_id as zhillsb_id,j.mingc as bianm,j.mingc as mingc\n");
			sb.append("from\n");
			sb
					.append("(");
			if(!hq.equals("h")){
			sb.append("select distinct zhilb_id,(m.mingc||'<p>('||f.chec||','||p.mingc||','||decode(y.mingc,null,'',y.mingc)||')') as mingc\n");
			sb.append("from chepb c, fahb f,meikxxb m,yunsdwb y,pinzb p\n");
			sb.append("where c.fahb_id = f.id\n");
			sb.append("and f.meikxxb_id=m.id\n");
			sb.append("and c.yunsdwb_id=y.id(+)\n");
			sb.append("and f.pinzb_id=p.id(+)\n");
			sb.append("and f.yunsfsb_id !=1\n");
			sb
					.append("and to_date(to_char(c.zhongcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
							+ getRiqi() + "', 'yyyy-mm-dd')\n");
			}
//			sb
//					.append("or to_date(to_char(c.qingcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =to_date('"
//							+ getRiqi() + "', 'yyyy-mm-dd'))\n");
			if(!hq.equals("q")){
				if(!hq.equals("h")){	
					sb.append("union\n");
					}
			sb
					.append("select distinct zhilb_id,(m.mingc||'<p>('||f.chec||','||p.mingc||','||decode(y.mingc,null,'',y.mingc)||')') as mingc\n");
			sb.append("from chepb c, fahb f,meikxxb m,yunsdwb y,pinzb p\n");
			sb.append("where c.fahb_id = f.id\n");
			sb.append("and f.meikxxb_id=m.id\n");
			sb.append("and c.yunsdwb_id=y.id(+)\n");
			sb.append("and f.meikxxb_id=m.id\n");
			sb.append("and f.pinzb_id=p.id(+)\n");
			sb.append("and f.yunsfsb_id=1\n");
			sb
					.append("and to_date(to_char(c.lursj, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
							+ getRiqi() + "', 'yyyy-mm-dd')\n");
			}
			sb.append(")j,caiyb c,yangpdhb y\n");
			sb.append("where  j.zhilb_id=c.zhilb_id\n");
			sb.append("and y.caiyb_id=c.id)a\n");

		}  else {
			sb.append("from(\n");
			sb.append("select  z.zhillsb_id,z.bianm,zz.mingc\n");
			sb.append("from\n");

			sb.append("(");
			if(!hq.equals("h")){
			sb.append("select distinct zhilb_id\n");
			sb.append("from chepb c, fahb f\n");
			sb.append("where c.fahb_id = f.id\n");
			sb.append("and f.yunsfsb_id !=1\n");
			sb
					.append("and to_date(to_char(c.zhongcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
							+ getRiqi() + "', 'yyyy-mm-dd')\n");
			}
//			sb
//					.append("or to_date(to_char(c.qingcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =to_date('"
//							+ getRiqi() + "', 'yyyy-mm-dd'))\n");
			if(!hq.equals("q")){
				if(!hq.equals("h")){	
					sb.append("union\n");
					}
			sb.append("select distinct zhilb_id\n");
			sb.append("from chepb c, fahb f\n");
			sb.append("where c.fahb_id = f.id\n");
			sb.append("and f.yunsfsb_id=1\n");
			sb
					.append("and to_date(to_char(c.lursj, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
							+ getRiqi() + "', 'yyyy-mm-dd')\n");
			}
			sb.append(") j,\n");
			sb.append("caiyb c,yangpdhb y,zhuanmb z,zhuanmlb zz\n");
			sb.append("where  j.zhilb_id=c.zhilb_id\n");
			sb.append("and y.caiyb_id=c.id\n");
			sb.append("and z.zhillsb_id=y.zhilblsb_id\n");
			sb.append("and z.zhuanmlb_id=zz.id\n");
			sb.append("and zz.jib=" + a + ") a\n");
		}
		sb.append(",\n");
		sb.append("(select  z.zhillsb_id,z.bianm,zz.mingc\n");
		sb.append("from\n");
		sb.append("(");
		if(!hq.equals("h")){
		sb.append("select distinct zhilb_id\n");
		sb.append("from chepb c, fahb f\n");
		sb.append("where c.fahb_id = f.id\n");
		sb.append("and f.yunsfsb_id !=1\n");
		sb
				.append("and to_date(to_char(c.zhongcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
						+ getRiqi() + "', 'yyyy-mm-dd')\n");
		}
//		sb
//				.append("or to_date(to_char(c.qingcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =to_date('"
//						+ getRiqi() + "', 'yyyy-mm-dd'))\n");
		if(!hq.equals("q")){
			if(!hq.equals("h")){	
				sb.append("union\n");
				}
		sb.append("select distinct zhilb_id\n");
		sb.append("from chepb c, fahb f\n");
		sb.append("where c.fahb_id = f.id\n");
		sb.append("and f.yunsfsb_id=1\n");
		sb
				.append("and to_date(to_char(c.lursj, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
						+ getRiqi() + "', 'yyyy-mm-dd')\n");
		}
		sb.append(") j,\n");
		sb.append("caiyb c,yangpdhb y,zhuanmb z,zhuanmlb zz\n");
		sb.append("where  j.zhilb_id=c.zhilb_id\n");
		sb.append("and y.caiyb_id=c.id\n");
		sb.append("and z.zhillsb_id=y.zhilblsb_id\n");
		sb.append("and z.zhuanmlb_id=zz.id\n");
		sb.append("and zz.jib=" + b + ") b\n");
		sb.append("where a.zhillsb_id=b.zhillsb_id\n");
		sb.append("order by a.zhillsb_id");

		ResultSet rs = cn.getResultSet(sb.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][2];
		String mingc1 = "";
		String mingc2 = "";
		ResultSet rss = cn.getResultSet("select mingc,jib from zhuanmlb\n"
				+ "union\n" + "select 'ú������' as mingc,0 as jib from dual\n"
				+ "union\n" + "select 'ú������' as mingc,-1 as jib from dual\n"
				+ "union\n" + "select 'ú������' as mingc,-2 as jib from dual");

		try {
			while (rss.next()) {
				if (rss.getInt("jib") == a) {
					mingc1 = rss.getString("mingc");
				}
				if (rss.getInt("jib") == b) {
					mingc2 = rss.getString("mingc");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ArrHeader[0] = new String[] { mingc1, mingc2 };

		int ArrWidth[] = new int[] { cybmwidth, hybmwidth };

		rt.setTitle("ת�뵥", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		// rt.body.mergeFixedRow();
		rt.body.setCells(1, 1, rt.body.getRows(), 2, Table.PER_ALIGN,
				Table.ALIGN_CENTER);

		rt.body.setFontSize(fontSize);

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		// rt.setDefautlFooter(7, 1, "���:", Table.ALIGN_RIGHT);
		// rt.setDefautlFooter(9, 2, "�Ʊ�:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	
	private String getZhuanmad_Daohrq(int a, int b,String hq) {
		JDBCcon cn = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		int cybmwidth = 150;
		int hybmwidth = 100;
		int fontSize = 9;
		if (bigFontSize) {
			cybmwidth = 300;
			hybmwidth = 300;
			fontSize = 28;
		}
		sb.append("select a.bianm as bianm1,b.bianm as bianm2 \n");
		 if (a == 0) {
				// ����+�𳵳���/�������䵥λ
				sb.append("from\n");

				sb
						.append("(select y.zhilblsb_id as zhillsb_id,j.mingc as bianm,j.mingc as mingc\n");
				sb.append("from\n");
				sb
						.append("(");
				if(!hq.equals("h")){
				sb.append("select distinct zhilb_id,(m.mingc||'<p>('||f.chec||','||p.mingc||','||decode(y.mingc,null,'',y.mingc)||')') as mingc\n");
				sb.append("from chepb c, fahb f,meikxxb m,yunsdwb y,pinzb p\n");
				sb.append("where c.fahb_id = f.id\n");
				sb.append("and f.meikxxb_id=m.id\n");
				sb.append("and c.yunsdwb_id=y.id(+)\n");
				sb.append("and f.pinzb_id=p.id(+)\n");
				sb.append("and f.yunsfsb_id !=1\n");
				sb
						.append("and to_date(to_char(f.daohrq, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
								+ getRiqi() + "', 'yyyy-mm-dd')\n");
				}
//				sb
//						.append("or to_date(to_char(c.qingcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =to_date('"
//								+ getRiqi() + "', 'yyyy-mm-dd'))\n");
				if(!hq.equals("q")){
					if(!hq.equals("h")){	
						sb.append("union\n");
						}
				sb
						.append("select distinct zhilb_id,(m.mingc||'<p>('||f.chec||','||p.mingc||','||decode(y.mingc,null,'',y.mingc)||')') as mingc\n");
				sb.append("from chepb c, fahb f,meikxxb m,yunsdwb y,pinzb p\n");
				sb.append("where c.fahb_id = f.id\n");
				sb.append("and f.meikxxb_id=m.id\n");
				sb.append("and c.yunsdwb_id=y.id(+)\n");
				sb.append("and f.meikxxb_id=m.id\n");
				sb.append("and f.pinzb_id=p.id(+)\n");
				sb.append("and f.yunsfsb_id=1\n");
				sb
						.append("and to_date(to_char(f.daohrq, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
								+ getRiqi() + "', 'yyyy-mm-dd')\n");
				}
				sb.append(")j,caiyb c,yangpdhb y\n");
				sb.append("where  j.zhilb_id=c.zhilb_id\n");
				sb.append("and y.caiyb_id=c.id)a\n");

			}  else {
				sb.append("from(\n");
				sb.append("select  z.zhillsb_id,z.bianm,zz.mingc\n");
				sb.append("from\n");

				sb.append("(");
				if(!hq.equals("h")){
				sb.append("select distinct zhilb_id\n");
				sb.append("from chepb c, fahb f\n");
				sb.append("where c.fahb_id = f.id\n");
				sb.append("and f.yunsfsb_id !=1\n");
				sb
						.append("and to_date(to_char(f.daohrq, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
								+ getRiqi() + "', 'yyyy-mm-dd')\n");
				}
//				sb
//						.append("or to_date(to_char(c.qingcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =to_date('"
//								+ getRiqi() + "', 'yyyy-mm-dd'))\n");
				if(!hq.equals("q")){
					if(!hq.equals("h")){	
						sb.append("union\n");
						}
				sb.append("select distinct zhilb_id\n");
				sb.append("from chepb c, fahb f\n");
				sb.append("where c.fahb_id = f.id\n");
				sb.append("and f.yunsfsb_id=1\n");
				sb
						.append("and to_date(to_char(f.daohrq, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
								+ getRiqi() + "', 'yyyy-mm-dd')\n");
				}
				sb.append(") j,\n");
				sb.append("caiyb c,yangpdhb y,zhuanmb z,zhuanmlb zz\n");
				sb.append("where  j.zhilb_id=c.zhilb_id\n");
				sb.append("and y.caiyb_id=c.id\n");
				sb.append("and z.zhillsb_id=y.zhilblsb_id\n");
				sb.append("and z.zhuanmlb_id=zz.id\n");
				sb.append("and zz.jib=" + a + ") a\n");
			}
			sb.append(",\n");
			sb.append("(select  z.zhillsb_id,z.bianm,zz.mingc\n");
			sb.append("from\n");
			sb.append("(");
			if(!hq.equals("h")){
			sb.append("select distinct zhilb_id\n");
			sb.append("from chepb c, fahb f\n");
			sb.append("where c.fahb_id = f.id\n");
			sb.append("and f.yunsfsb_id !=1\n");
			sb
					.append("and to_date(to_char(f.daohrq, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
							+ getRiqi() + "', 'yyyy-mm-dd')\n");
	        }
//			sb
//					.append("or to_date(to_char(c.qingcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =to_date('"
//							+ getRiqi() + "', 'yyyy-mm-dd'))\n");
			if(!hq.equals("q")){
				if(!hq.equals("h")){	
					sb.append("union\n");
					}
			sb.append("select distinct zhilb_id\n");
			sb.append("from chepb c, fahb f\n");
			sb.append("where c.fahb_id = f.id\n");
			sb.append("and f.yunsfsb_id=1\n");
			sb
					.append("and to_date(to_char(f.daohrq, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
							+ getRiqi() + "', 'yyyy-mm-dd')\n");
			}
			sb.append(") j,\n");
			sb.append("caiyb c,yangpdhb y,zhuanmb z,zhuanmlb zz\n");
			sb.append("where  j.zhilb_id=c.zhilb_id\n");
			sb.append("and y.caiyb_id=c.id\n");
			sb.append("and z.zhillsb_id=y.zhilblsb_id\n");
			sb.append("and z.zhuanmlb_id=zz.id\n");
			sb.append("and zz.jib=" + b + ") b\n");
			sb.append("where a.zhillsb_id=b.zhillsb_id\n");
			sb.append("order by a.zhillsb_id");


		ResultSet rs = cn.getResultSet(sb.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][2];
		String mingc1 = "";
		String mingc2 = "";
		ResultSet rss = cn.getResultSet("select mingc,jib from zhuanmlb\n"
				+ "union\n" + "select 'ú������' as mingc,0 as jib from dual\n"
				+ "union\n" + "select 'ú������' as mingc,-1 as jib from dual\n"
				+ "union\n" + "select 'ú������' as mingc,-2 as jib from dual");

		try {
			while (rss.next()) {
				if (rss.getInt("jib") == a) {
					mingc1 = rss.getString("mingc");
				}
				if (rss.getInt("jib") == b) {
					mingc2 = rss.getString("mingc");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ArrHeader[0] = new String[] { mingc1, mingc2 };

		int ArrWidth[] = new int[] { cybmwidth, hybmwidth };

		rt.setTitle("ת�뵥", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		// rt.body.mergeFixedRow();
		rt.body.setCells(1, 1, rt.body.getRows(), 2, Table.PER_ALIGN,
				Table.ALIGN_CENTER);

		rt.body.setFontSize(fontSize);

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		// rt.setDefautlFooter(7, 1, "���:", Table.ALIGN_RIGHT);
		// rt.setDefautlFooter(9, 2, "�Ʊ�:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	private boolean _RefurbishClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_RefurbishClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishClick) {

			Refurbish();
			_RefurbishClick = false;
		}

	}

	private void Refurbish() {
		// Ϊ "ˢ��" ��ť��Ӵ������
//		if (mstrReportName.equals(CAIY)) {
//			getCaiymzzym();
//		} else if (mstrReportName.equals(HUAY)) {
//			getZhiybmzhybm();
//		} else if ((mstrReportName).equals(ALL)) {
//			getZhuanmad();
//		}
		getPrintTable();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setString2("");
			visit.setString3("");
			visit.setString4("");
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			
			getSelectData();
		}
		if (cycle.getRequestContext().getParameter("lx") != null) {
			if (!visit.getString1().equals(
					cycle.getRequestContext().getParameter("lx"))) {

				visit.setProSelectionModel10(null);
				visit.setDropDownBean10(null);
				visit.setProSelectionModel5(null);
				visit.setDropDownBean5(null);
				setRiqi(null);

			}
			visit.setString1(cycle.getRequestContext().getParameter("lx"));
			if(cycle.getRequestContext().getParameter("hq")==null){
				visit.setString4("hq");
			}else visit.setString4(cycle.getRequestContext().getParameter("hq"));
			visit.setInt1(Integer.parseInt(cycle.getRequestContext().getParameter("num1")));
			visit.setInt2(Integer.parseInt(cycle.getRequestContext().getParameter("num2")));
			mstrReportName = visit.getString1();
		}else{
				mstrReportName = visit.getString1();
		}
		blnIsBegin = true;
		getSelectData();

	}

	// ������
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}

	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		Visit visit = (Visit) getPage().getVisit();
		getCaiybmModels();
		String rq="��������";
		if(visit.getString1().equals(Zhongcsj)){
			if(visit.getString4().equals("h")){
			rq="�˵�¼������:";
			}else{
				rq="�س�����:";
			}
		}else if(visit.getString1().equals(Daohrq)){
			rq="��������:";
		}else if (visit.getString1().equals(Fahrq)){
			rq="��������:";
		};
		tb1.addText(new ToolbarText(rq));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("RIQI");
		// df.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

//		if (mstrReportName.equals(CAIY)) {
//			// ����
//			tb1.addText(new ToolbarText("��������:"));
//			ComboBox CB = new ComboBox();
//			CB.setTransform("CAIYBM");
//			CB.setWidth(120);
//			CB.setListeners("select:function(){document.Form0.submit();}");
//			CB.setEditable(true);
//			tb1.addField(CB);
//			tb1.addText(new ToolbarText("-"));
//		}

		ToolbarButton tb = new ToolbarButton(null, "ˢ��",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);

		setToolbar(tb1);

	}

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

	public String getcontext() {
		return "";
		// return "var context='http://"
		// + this.getRequestCycle().getRequestContext().getServerName()
		// + ":"
		// + this.getRequestCycle().getRequestContext().getServerPort()
		// + ((Visit) getPage().getVisit()).get() + "';";
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

	// ��������
	public IDropDownBean getCaiybmValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getCaiybmModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setCaiybmValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setCaiybmModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getCaiybmModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getCaiybmModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getCaiybmModels() {

		StringBuffer sbsql = new StringBuffer();
		Visit visit = ((Visit) getPage().getVisit());
		sbsql
				.append("select distinct z.zhillsb_id as id, c.caiybm\n"
						+ "  from (select bianm as caiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 1\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '��������')) c,\n"
						+ "       (select bianm as zhiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '��������')) z,\n"
						+ "       (select distinct f.id, f.diancxxb_id, z.id as zid\n"
						+ "          from zhillsb z, fahb f, chepb c\n"
						+ "         where f.zhilb_id = z.zhilb_id\n"
						+ "           and c.fahb_id = f.id\n"
						+ "           and to_date(to_char(c.lursj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
						+ "               to_date('" + getRiqi()
						+ "', 'yyyy-mm-dd')) s\n"
						+ " where c.zhillsb_id = z.zhillsb_id\n"
						+ "   and c.zhillsb_id = s.zid\n"
						+ "   and z.zhillsb_id = s.zid\n"
						+ Jilcz.filterDcid(visit, "s") + " order by caiybm");

		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sbsql.toString(),
						"������"));
		return;
	}
}
