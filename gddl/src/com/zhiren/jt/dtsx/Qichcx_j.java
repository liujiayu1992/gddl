package com.zhiren.jt.dtsx;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/**
 * 
 * @author huochaoyuan
 *��������������ʵʱ���������ѯ
 *������һ��һ�ƣ��ֿ����䵥λ��ú����״̬�����Ա��ж����ʽ��ѯ����ѯ������ʱ�侫ȷ�����Ӳ�ѯ��
 *��Դ�����ã� Qichcx_j_sx&lx=Qichcx_all(����ϸ����)/Qichcx_all_hz(�������ݣ�̨����ʽ)
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-03-21
 * ���������������ʼ����BUG
 */
public class Qichcx_j extends BasePage {
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
	private String mstrReportName = "";
	private String QICHCX_ALL="Qichcx_all";//���ؼ�¼(����ʱ��)
	private String QICHCX_ALL_HZ="Qichcx_all_hz";//����̨��(����ʱ��)
	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
if (mstrReportName.equals(QICHCX_ALL)) {
		return getJianjd_all();
	}else if (mstrReportName.equals(QICHCX_ALL_HZ)) {
		return getJianjd_all_hz();
	}else {
		return "�޴˱���";
	}
		
	}


	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}

	private String getJianjd_all() {
		int intHuzStartRow = 0;
		ResultSet rs;
		JDBCcon con = new JDBCcon();
		String changbq = "";
		String changbc = "";
		Visit visit=(Visit) getPage().getVisit();
		String str = "";
//		if (visit.isJTUser()) {
//			str = "";
//		} else {
//			if (visit.isGSUser()) {
//				str = "and (d.id = " + getTreeid() + " or d.fuid = "
//						+ getTreeid() + ")";
//			} else {
//				str = "and d.id = " + getTreeid() + "";
//			}
//		}
//		
//		int treejib = this.getDiancTreeJib();
//		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
//			str = "";
//		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
//			str = "and (d.id = " + getTreeid() + " or d.fuid = "
//					+ getTreeid() + ")";
//		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
//			str = "and d.id = " + getTreeid() + "";
//		}
	//	System.out.println(getTreeid());
		String yunsdwsql="";
		String meicsql="";
		String xiecfsql="";
		String chephsql="";
		String gmsql="";
		String renysql="";
		String zhuangtsql="";
		String shij="";
        if(!getLeixSelectValue().getValue().equals("*���䵥λ")){
        	yunsdwsql="and c.yunsdwb_id="+getLeixSelectValue().getId()+"\n";
        }
        if(!getDiancmcValue().getValue().equals("*ж����ʽ")){
        	xiecfsql="and c.xiecfsb_id="+getDiancmcValue().getId()+"\n";
        }
        if(!getGongysDropDownValue().getValue().equals("*ú��")){
        	meicsql="and c.meicb_id in (select id from meicb where mingc like '%"+getGongysDropDownValue().getValue()+"%' or beiz like '%"+getGongysDropDownValue().getValue()+"%')\n";
        }
   //     System.out.println(getCheph());
        if(getCheph()!=null&&!getCheph().equals("")){
        	chephsql="and c.cheph like '%"+_cheph+"%'\n";
        }
   //     System.out.println(getTreeid());
        if(getTreeid()!=null&&!getTreeid().equals("0")&&!getTreeid().equals("")){
        	gmsql="and (f.gongysb_id="+getTreeid()+" or f.meikxxb_id="+getTreeid()+" )\n";
        }
        if(!getRenyValue().getValue().equals("*��Ա")){
        	renysql="and (c.zhongcjjy like '%"+getRenyValue().getValue()+"%' or c.qingcjjy like '%"+getRenyValue().getValue()+"%')\n";
        }
//        if(!getZhuangtValue().getValue().equals("*״̬")){
//        	if(getZhuangtValue().getValue().equals("���")){
//        		zhuangtsql="and c.qingcsj is not null\n";
//        	}else zhuangtsql="and c.qingcsj is null\n";
//        	
//        }
        if(getZhuangtValue().getValue().equals("���")){
        		zhuangtsql="and c.qingcsj is not null\n";
    			shij="to_char(c.qingcsj, 'yyyy-mm-dd hh24:mi') >= '"+getBeginriqDate()+" "+getBhValue().getValue()+":"+getBsValue().getValue()+"'\n" + 
    			"and to_char(c.qingcsj, 'yyyy-mm-dd hh24:mi') <= '"+getEndriqDate()+" "+getEhValue().getValue()+":"+getEsValue().getValue()+"'\n";
    			
        		
        	}else {
        		zhuangtsql="and c.qingcsj is null\n";
				shij="to_char(c.zhongcsj, 'yyyy-mm-dd hh24:mi') >= '"+getBeginriqDate()+" "+getBhValue().getValue()+":"+getBsValue().getValue()+"'\n" + 
				"and to_char(c.zhongcsj, 'yyyy-mm-dd hh24:mi') <= '"+getEndriqDate()+" "+getEhValue().getValue()+":"+getEsValue().getValue()+"'\n";			
			
        	}

		String sql = 

			"select rownum as xuh ,\n" +
			"      g.mingc as gongys,\n" + 
			"       m.mingc as meik,\n" + 
			"       y.mingc as yunsdw,\n" + 
			"       mc.mingc as meic,\n" + 
			"       shuj.cheph as cheph,\n" + 
			"       shuj.maoz as maoz,\n" + 
			"       shuj.piz as piz,\n" + 
			"       shuj.jingz as jingz,\n" + 
			"       shuj.biaoz as biaoz,\n" + 
			"       shuj.koud as koud,\n" + 
			"       x.mingc as xiemfs,\n" + 
			"       shuj.shij as shij,\n"+
			"       shuj.reny as reny,\n"+
			"       shuj.beiz as beiz\n"+
			"  from (select c.zhongcsj,\n" + 
			"               f.gongysb_id,\n" + 
			"               f.meikxxb_id,\n" + 
			"               c.cheph,\n" + 
			"               c.yunsdwb_id,\n" + 
			"               c.meicb_id,\n" + 
			"               c.xiecfsb_id,\n" + 
			"               c.maoz,\n" + 
			"               c.piaojh,\n"+
			"               c.piz,\n" + 
			"               c.beiz,\n"+
			"               (c.maoz - c.piz-c.koud-c.kouz-c.kous) as jingz,\n" + 
			"               c.biaoz,\n" + 
			"               (c.koud+c.kous+c.kouz) as koud,\n" + 
			"               c.yingk,\n" + 
			"               c.yuns,\n" + 
			"               c.zhongchh||to_char(c.zhongcsj, 'yyyy-mm-dd hh24:mi:ss')||'<p>'||c.qingchh||to_char(c.qingcsj, 'yyyy-mm-dd hh24:mi:ss') as shij,\n" + 
			"               c.zhongcjjy || '/' || c.qingcjjy as reny,\n" + 
			"               c.zhongchh || '/' || c.qingchh as hengqh\n" + 
			"          from chepb c, fahb f\n" + 
			"         where " +shij+
			"           and c.fahb_id = f.id and f.yunsfsb_id=2\n" + yunsdwsql+meicsql+xiecfsql+gmsql+renysql+zhuangtsql+
			"           order by f.gongysb_id,f.meikxxb_id,c.zhongcsj desc\n" + 
			"           ) shuj,\n" + 
			"       gongysb g,\n" + 
			"       meikxxb m,\n" + 
			"       yunsdwb y,\n" + 
			"       meicb mc,\n" + 
			"       xiecfsb x\n" + 
			" where shuj.gongysb_id = g.id(+)\n" + 
			"   and shuj.meikxxb_id = m.id(+)\n" + 
			"   and shuj.yunsdwb_id = y.id(+)\n" + 
			"   and shuj.meicb_id = mc.id(+)\n" + 
			"   and shuj.xiecfsb_id = x.id(+)";


		rs = con.getResultSet(new StringBuffer(sql),
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		String ArrHeader[][] = new String[1][15];
		ArrHeader[0] = new String[] { "���", "������λ", "ú��λ", "���䵥λ", "ú��","����",	
				"ë��", "Ƥ��", "����","Ʊ��","�۶�","ж����ʽ","����ʱ��","���Ա","��ע"};
		int ArrWidth[] = new int[] { 20, 90, 90,80, 50, 60, 60, 60, 60,60, 50,60,140,80,50};
		rt.setTitle("��   ��   ��   ��   ��   ��   ¼<p>"+visit.getDiancmc(),
				//+((getChangbValue().getId() > 0)?"("+getChangbValue().getValue()+")":"")+"", 
				ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitleLeft("�������ڣ�" 
				+ getBeginriqDate()+"��"+getEndriqDate()
				, 3);
		rt.setDefaultTitleRight("��λ����", 3);
		rt.setBody(new Table(rs, 1, 0, 1));

		// ����
		rt.body.setWidth(ArrWidth);

		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		//		
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.ShowZero = true;
//		rt.body.setPageRows(43);
		//rt.body.mergeFixedCols();// �ϲ��б���
		//rt.body.mergeFixedRow();//�ϲ��б���
//		rt.body.setCells(2, 1, rt.body.getRows(), 3, Table.PER_ALIGN,
//				Table.ALIGN_LEFT);
//		rt.body.setCells(2, 1, rt.body.getRows(), 3, Table.PER_VALIGN,
//				Table.VALIGN_TOP);
//		rt.body.setCells(2, 2, rt.body.getRows(), 3, Table.PER_ALIGN,
//				Table.ALIGN_CENTER);
		rt.body.ShowZero = false;

		// �����Ǽӱ�
		ResultSet hz;
		String sql_hz = "";
		sql_hz =


			"select rownum as xuh,\n" +
			"decode(g.mingc,null,'����',g.mingc) as gongys,\n" + 
			"decode(g.mingc,null,'����',decode(m.mingc,null,'�ܼ�',m.mingc)) as meik,\n" + 
			"decode(g.mingc,null,'����',decode(m.mingc,null,'�ܼ�',decode(y.mingc,null,'�ϼ�',y.mingc))) as yunsdw,\n" + 
			"decode(g.mingc,null,'����',decode(m.mingc,null,'�ܼ�',decode(y.mingc,null,'�ϼ�',decode(mc.mingc,null,'С��',mc.mingc)))) as meic,\n" + 
			"z.maoz as maoz,\n" + 
			"z.piz as piz,\n" +  
			"z.jingz as jingz,\n" + 
			"z.biaoz as biaoz,\n" + 
			"z.koud as koud,\n" + 
			"z.ches as ches,\n" + 
			"'' as a,\n"+
			"'' as b,\n"+
			"'' as e,\n"+
			"'' as c\n"+
			"from\n" + 
			"(select   decode(grouping(f.gongysb_id),1,5,decode(grouping(meikxxb_id),1,4,decode(grouping(yunsdwb_id),1,3,decode(grouping(meicb_id),1,2,1)))) as xuh,\n" + 
			"               f.gongysb_id,\n" + 
			"               f.meikxxb_id,\n" + 
			"               c.yunsdwb_id,\n" + 
			"               c.meicb_id,\n" + 
			"               sum(c.maoz) as maoz,\n" + 
			"               sum(c.piz) as piz,\n" + 
			"               (sum(c.maoz) - sum(c.piz)-sum(c.koud)-sum(c.kouz)-sum(c.kous)) as jingz,\n" + 
			"               sum(c.biaoz) as biaoz,\n" + 
			"               (sum(c.koud)+sum(c.kouz)+sum(c.kous)) as koud,\n" + 
			"               sum(c.yingk) as yingk,\n" + 
			"               sum(c.yuns) as yuns,\n" + 
			"               count(c.fahb_id) as ches,\n"+
			"grouping(f.gongysb_id) as gx,\n" +
			"grouping(f.meikxxb_id) as mx,\n" + 
			"grouping(c.yunsdwb_id) as yx,\n" + 
			"grouping(c.meicb_id) as mcx\n"+
			"          from chepb c, fahb f\n" + 
			"         where "+shij+
			"           and c.fahb_id = f.id and f.yunsfsb_id=2\n" + yunsdwsql+meicsql+xiecfsql+gmsql+renysql+zhuangtsql+
			"           group by rollup (f.gongysb_id,f.meikxxb_id,c.yunsdwb_id,c.meicb_id)\n" + 
			"           )z,gongysb g,meikxxb m,yunsdwb y,meicb mc\n" + 
			"           where z.gongysb_id=g.id(+)\n" + 
			"           and z.meikxxb_id=m.id(+)\n" + 
			"           and z.yunsdwb_id=y.id(+)\n" + 
			"           and z.meicb_id=mc.id(+)\n" + 
			" order by z.gx,g.mingc,z.mx,m.mingc,z.yx,y.mingc,z.mcx,mc.mingc";


		hz = con.getResultSet(new StringBuffer(sql_hz),
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rt.body.getPageEndRow(rt.body.getPages());
		// ���ӻ�������
		String ArrHeaderHZ[][] = new String[2][15];
		ArrHeaderHZ[0] = new String[] { "", "", "", "", "", "", "", "","","", "", "","","",""};
		ArrHeaderHZ[1] = new String[] { "���", "������λ", "ú��λ",
				"���䵥λ","ú��","ë��","Ƥ��","����","Ʊ��","�۶�","����","","","",""};
		intHuzStartRow = rt.body.getRows();
		rt.body.AddTableData(ArrHeaderHZ, hz);
		// ���ñ����е�λ��
		int ArrTitltePos[][] = new int[2][2];
		ArrTitltePos[0][0] = 1;
		ArrTitltePos[0][1] = 1;
		ArrTitltePos[1][0] = intHuzStartRow + 2;
		ArrTitltePos[1][1] = intHuzStartRow + 2;

		rt.body.setTableTitlePos(ArrTitltePos);

		// ���û�����ǰ�Ŀհ�
		rt.body.setBorder(0, 0, 2, 1);
		rt.body.setCells(1, 1, rt.body.getRows(), 1, Table.PER_BORDER_LEFT, 2);
		rt.body.setCells(1, rt.body.getCols(), rt.body.getRows(), rt.body
				.getCols(), Table.PER_BORDER_RIGHT, 2);

		if ((intHuzStartRow + 1 - rt.body.getFixedRows())
				% rt.body.getPageRows() == 0) {
			rt.body.setCellBorder(intHuzStartRow + 1, 1, 0, 0, 1, 2);
		} else {
			rt.body.setCellBorder(intHuzStartRow + 1, 1, 0, 0, 1, 2);
		}

		// ���û��ܱ�Ķ���
		rt.body.setRowCells(intHuzStartRow + 2, Table.PER_ALIGN,
				Table.ALIGN_CENTER);
		rt.body.setCells(intHuzStartRow + 3, 2, rt.body.getRows(), rt.body
				.getCols(), Table.PER_ALIGN, Table.ALIGN_RIGHT);

		// rt.body.setRowCells(intHuzStartRow+2, Table.PER_BORDER_TOP, 2);

		// ���úϲ�
		rt.body.mergeRowCells(intHuzStartRow + 1);
		//rt.body.mergeRowCells(intHuzStartRow + 2);
		rt.body.setRowHeight(intHuzStartRow + 1, 3);
//		for (int i = intHuzStartRow + 2; i <= rt.body.getRows(); i++) {
//			rt.body.mergeCell(i, 6, i, 7);
//			rt.body.mergeCell(i, 8, i, 9);
//			rt.body.mergeCell(i, 10, i, 11);
//			rt.body.mergeCell(i, 12, i, 13);
//		}
		for (int i = 1; i < 6; i++) {
			rt.body.setColCells(i, Table.PER_ALIGN, Table.ALIGN_LEFT);
		}
		try {
			hz.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		ResultSet ry;
//		String jjysql = "select 'a'as jianjy from dual\n";
//		String jianjy = "";
//		ry = con.getResultSet(new StringBuffer(jjysql),
//				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//
//		try {
//			while (ry.next()) {
//				jianjy = ry.getString("Jianjy");
//			}
//			ry.close();
//		} catch (SQLException e1) {
//			// TODO �Զ����� catch ��
//			e1.printStackTrace();
//		}
//		String str1 = "2,3";// ����ȡ�ļ��У��˴���ȡ���ǵ�2�к͵�3��
//		int bodyRow = rt.body.getRows();// ȡ�ж�����
//		String[] list = str1.split(",");// Regex�ֳ�����
//		for (int j = 0; j < list.length; j++) {
//			int num = Integer.parseInt(list[j]);// һ��һ��������ͬ��Ϊ��ֵ
//			String cellValue = rt.body.getCellValue(1, num);// ȡ��Ӧ�õ���һ�е�ֵ
//			for (int i = 2; i <= bodyRow; i++) {// i���ó�2�ӵڶ���ȡֵ
//				if (rt.body.getCellValue(i, num).equals(cellValue)) {// ��ͬ����Ϊ��
//					rt.body.setCellValue(i, num, " ");
//				} else {
//					cellValue = rt.body.getCellValue(i, num);// ��ͬ�ı�����Ϊ����һ���ԽǵıȽ�
//				}
//			}
//
//		}
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3,
				"�����ӡ����:" + DateUtil.FormatDate(new Date()), 0);
		rt.setDefautlFooter(4, 2, "���ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 3, "�Ʊ�" + visit.getRenymc(), Table.ALIGN_LEFT);
		// rt.setDefautlFooter(14, 2, Table.PAGENUMBER_CHINA, 2);
		try {
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();

		rt.body.setRowHeight(21);
		return rt.getAllPagesHtml();
	}
	
	private String getJianjd_all_hz() {
		int intHuzStartRow = 0;
		ResultSet rs;
		JDBCcon con = new JDBCcon();
		String changbq = "";
		String changbc = "";
		Visit visit=(Visit) getPage().getVisit();
		String str = "";
//		if (visit.isJTUser()) {
//			str = "";
//		} else {
//			if (visit.isGSUser()) {
//				str = "and (d.id = " + getTreeid() + " or d.fuid = "
//						+ getTreeid() + ")";
//			} else {
//				str = "and d.id = " + getTreeid() + "";
//			}
//		}
//		
//		int treejib = this.getDiancTreeJib();
//		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
//			str = "";
//		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
//			str = "and (d.id = " + getTreeid() + " or d.fuid = "
//					+ getTreeid() + ")";
//		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
//			str = "and d.id = " + getTreeid() + "";
//		}
	//	System.out.println(getTreeid());
		String yunsdwsql="";
		String meicsql="";
		String xiecfsql="";
		String chephsql="";
		String gmsql="";
		String renysql="";
		String zhuangtsql="";
		String shij="";
        if(!getLeixSelectValue().getValue().equals("*���䵥λ")){
        	yunsdwsql="and c.yunsdwb_id="+getLeixSelectValue().getId()+"\n";
        }
        if(!getDiancmcValue().getValue().equals("*ж����ʽ")){
        	xiecfsql="and c.xiecfsb_id="+getDiancmcValue().getId()+"\n";
        }
        if(!getGongysDropDownValue().getValue().equals("*ú��")){
        	meicsql="and c.meicb_id in (select id from meicb where mingc like '%"+getGongysDropDownValue().getValue()+"%' or beiz like '%"+getGongysDropDownValue().getValue()+"%')\n";
        }
   //     System.out.println(getCheph());
        if(getCheph()!=null&&!getCheph().equals("")){
        	chephsql="and c.cheph like '%"+_cheph+"%'\n";
        }
   //     System.out.println(getTreeid());
        if(getTreeid()!=null&&!getTreeid().equals("0")&&!getTreeid().equals("")){
        	gmsql="and (f.gongysb_id="+getTreeid()+" or f.meikxxb_id="+getTreeid()+" )\n";
        }
        if(!getRenyValue().getValue().equals("*��Ա")){
        	renysql="and (c.zhongcjjy like '%"+getRenyValue().getValue()+"%' or c.qingcjjy like '%"+getRenyValue().getValue()+"%')\n";
        }
//        if(!getZhuangtValue().getValue().equals("*״̬")){
//        	if(getZhuangtValue().getValue().equals("���")){
//        		zhuangtsql="and c.qingcsj is not null\n";
//        	}else zhuangtsql="and c.qingcsj is null\n";
//        	
//        }
        if(getZhuangtValue().getValue().equals("���")){
        		zhuangtsql="and c.qingcsj is not null\n";
    			shij="to_char(c.qingcsj, 'yyyy-mm-dd hh24:mi') >= '"+getBeginriqDate()+" "+getBhValue().getValue()+":"+getBsValue().getValue()+"'\n" + 
    			"and to_char(c.qingcsj, 'yyyy-mm-dd hh24:mi') <= '"+getEndriqDate()+" "+getEhValue().getValue()+":"+getEsValue().getValue()+"'\n";
    			
        		
        	}else {
        		zhuangtsql="and c.qingcsj is null\n";
				shij="to_char(c.zhongcsj, 'yyyy-mm-dd hh24:mi') >= '"+getBeginriqDate()+" "+getBhValue().getValue()+":"+getBsValue().getValue()+"'\n" + 
				"and to_char(c.zhongcsj, 'yyyy-mm-dd hh24:mi') <= '"+getEndriqDate()+" "+getEhValue().getValue()+":"+getEsValue().getValue()+"'\n";			
			
        	}

		String sql = 

			"select a.gys,a.mk,a.ysdw,a.ches,a.maoz,a.piz,a.jingz,a.biaoz,a.yuns,a.yingk,a.zongkd\n" +
			"from (\n" + 
			"select\n" + 
			"grouping (g.mingc) as g,\n" + 
			"grouping (m.mingc) as m,\n" + 
			"grouping (y.mingc) as y,\n" + 
			"decode(g.mingc,null,'�ܼ�',g.mingc) as gys,\n" + 
			"decode(g.mingc,null,'�ܼ�',decode(m.mingc,null,'�ϼ�',m.mingc)) as mk,\n" + 
			"decode(g.mingc,null,'�ܼ�',decode(m.mingc,null,'�ϼ�',decode(y.mingc,null,'С��',y.mingc))) as ysdw,\n" + 
			"sum(c.ches) as ches,\n"+
			"sum(c.maoz) as maoz,\n" + 
			"sum(c.piz) as piz,\n" + 
			"sum(c.jingz) as jingz,\n" + 
			"sum(c.biaoz) as biaoz,\n" + 
			"sum(c.yuns) as yuns,\n" + 
			"sum(c.yingk) as yingk,\n" + 
			"sum(c.zongkd) as zongkd\n" + 
			"from\n" + 
			"(select c.fahb_id as fid,\n" + 
			"       c.yunsdwb_id as yunsdwb_id,\n" + 
			"       count(c.fahb_id) as ches,\n"+
			"       sum(c.maoz) as maoz,\n" + 
			"       sum(c.piz) as piz,\n" + 
			"       sum(c.maoz - c.piz - c.koud - c.kous - c.kouz) as jingz,\n" + 
			"       sum(c.biaoz) as biaoz,\n" + 
			"       sum(c.yuns) as yuns,\n" + 
			"       sum(c.yingk) as yingk,\n" + 
			"       sum(c.zongkd) as zongkd\n" + 
			"  from chepb c\n" + 
			" where " +shij+yunsdwsql+xiecfsql+meicsql+renysql+zhuangtsql+         
			" group by c.fahb_id, c.yunsdwb_id\n" + 
			" )c,fahb f,yunsdwb y,gongysb g,meikxxb m\n" + 
			" where c.fid=f.id\n" + 
			" and f.yunsfsb_id=2\n"+
			" and c.yunsdwb_id=y.id(+)\n" + 
			" and f.gongysb_id=g.id(+)\n" + 
			" and f.meikxxb_id=m.id(+)\n" + gmsql+
			" group by rollup ( g.mingc,m.mingc,y.mingc)\n" + 
			") a\n" + 
			"order by a.g desc,a.gys,a.m asc,a.mk,a.y asc,a.ysdw";




		rs = con.getResultSet(new StringBuffer(sql),
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		String ArrHeader[][] = new String[1][11];
		ArrHeader[0] = new String[] {"������λ", "ú��λ", "���䵥λ","����",
				"ë��", "Ƥ��", "����","Ʊ��","����","ӯ��","�ܿ���"};
		int ArrWidth[] = new int[] {120, 120,80,50, 60, 60, 60, 60, 60,60, 60};
		rt.setTitle("��   ��   ��   ��   ��   ̨   ��<p>"+visit.getDiancmc(),
				//+((getChangbValue().getId() > 0)?"("+getChangbValue().getValue()+")":"")+"", 
				ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.setDefaultTitleLeft("�������ڣ�" 
				+ getBeginriqDate()+"��"+getEndriqDate()
				, 3);
		rt.setDefaultTitleRight("��λ����", 3);
		rt.setBody(new Table(rs, 1, 0, 2));

		// ����
		rt.body.setWidth(ArrWidth);

		//		
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.ShowZero = true;
//		rt.body.setPageRows(43);
		rt.body.mergeFixedCols();// �ϲ��б���
		rt.body.mergeFixedRow();//�ϲ��б���
		for(int i = 2; i<= rt.body.getRows() ; i++){
			rt.body.setCells(i, 1, i, 3, Table.PER_ALIGN, Table.ALIGN_CENTER);
			rt.body.merge(i, 1, i, 3);
		}
//		rt.body.mergeCell(2, 1, rt.body.getRows(), 3);
//		rt.body.setCells(2, 1, rt.body.getRows(), 3, Table.PER_ALIGN,
//				Table.ALIGN_LEFT);
//		rt.body.setCells(2, 1, rt.body.getRows(), 3, Table.PER_VALIGN,
//				Table.VALIGN_TOP);
//		rt.body.setCells(2, 2, rt.body.getRows(), 3, Table.PER_ALIGN,
//				Table.ALIGN_CENTER);
		rt.body.ShowZero = false;

		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3,
				"�����ӡ����:" + DateUtil.FormatDate(new Date()), 0);
		rt.setDefautlFooter(4, 2, "���ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(8, 3, "�Ʊ�" + visit.getRenymc(), Table.ALIGN_LEFT);
		// rt.setDefautlFooter(14, 2, Table.PAGENUMBER_CHINA, 2);
		try {
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();

		rt.body.setRowHeight(21);
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
	
	private void getToolbars(){
		Visit visit = (Visit) getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		//����
		tb1.addText(new ToolbarText("��������:"));
		DateField df = new DateField();
		df.setValue(this.getBeginriqDate());
		df.Binding("BeginTime","");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setWidth(60);
		tb1.addField(df);
		
		tb1.addText(new ToolbarText("-"));
		
		//tb1.addText(new ToolbarText("��Ա:"));
		ComboBox cb6 = new ComboBox();
		cb6.setTransform("BH");
		cb6.setEditable(true);
		cb6.setWidth(35);
		tb1.addField(cb6);
		tb1.addText(new ToolbarText("-"));
		
		ComboBox cb8 = new ComboBox();
		cb8.setTransform("BS");
		cb8.setEditable(true);
		cb8.setWidth(35);
		tb1.addField(cb8);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("��"));
		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("EndTime","");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setWidth(60);
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
		
		//tb1.addText(new ToolbarText("��Ա:"));
		ComboBox cb7 = new ComboBox();
		cb7.setTransform("EH");
		cb7.setEditable(true);
		cb7.setWidth(35);
		tb1.addField(cb7);
		tb1.addText(new ToolbarText("-"));
		
		ComboBox cb9 = new ComboBox();
		cb9.setTransform("ES");
		cb9.setEditable(true);
		cb9.setWidth(35);
		tb1.addField(cb9);
		tb1.addText(new ToolbarText("-"));

//		tb1.addText(new ToolbarText("ͳ�ƿھ�:"));
//		ComboBox cb1 = new ComboBox();
//		cb1.setTransform("LeixSelect");
//		cb1.setWidth(80);
//		tb1.addField(cb1);
//		tb1.addText(new ToolbarText("-"));

		long diancid=visit.getDiancxxb_id();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk,"diancTree",""+diancid,null,null,null);
		visit.setDefaultTree(dt);
		
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setValue("��ѡ��ú��");
		tf.setWidth(70);
		//dt.getTree().getSelectedNodeid()
		//tf.setValue(dt.getTree().getSelectedNodeid());
		//tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
				
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		//tb1.addText(new ToolbarText("������λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
//		tb1.addText(new ToolbarText("��Ӧ��:"));
//		ComboBox cb2 = new ComboBox();
//		cb2.setTransform("GongysDropDown");
//		cb2.setEditable(true);
//		tb1.addField(cb2);
		
		tb1.addText(new ToolbarText("-"));
		
	//	tb1.addText(new ToolbarText("���䵥λ:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("LeixSelect");
		cb1.setWidth(80);
		tb1.addField(cb1);
		
		tb1.addText(new ToolbarText("-"));
		
	//	tb1.addText(new ToolbarText("ú��:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("GongysDropDown");
		cb2.setEditable(true);
		cb2.setWidth(60);
		tb1.addField(cb2);
		
		tb1.addText(new ToolbarText("-"));
		
	//	tb1.addText(new ToolbarText("��ж��ʽ:"));
		ComboBox cb3 = new ComboBox();
		cb3.setTransform("DanwSelect");
		cb3.setEditable(true);
		cb3.setWidth(80);
		tb1.addField(cb3);
		
		tb1.addText(new ToolbarText("-"));
	//	tb1.addText(new ToolbarText("״̬:"));
		ComboBox cb4 = new ComboBox();
		cb4.setTransform("ZHUANGT");
		cb4.setEditable(true);
		cb4.setWidth(60);
		tb1.addField(cb4);
		tb1.addText(new ToolbarText("-"));
		
	//	tb1.addText(new ToolbarText("��Ա:"));
		ComboBox cb5 = new ComboBox();
		cb5.setTransform("RENY");
		cb5.setEditable(true);
		cb5.setWidth(60);
		tb1.addField(cb5);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setboolean1(false);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			visit.setDropDownBean6(null);
			visit.setProSelectionModel6(null);
			visit.setDropDownBean7(null);
			visit.setProSelectionModel7(null);
			visit.setDropDownBean8(null);
			visit.setProSelectionModel8(null);
			visit.setDropDownBean9(null);
			visit.setProSelectionModel9(null);
			visit.setDefaultTree(null);
			setBeginriqDate(null);
			setEndriqDate(null);
			setTreeid(null);
			setCheph(null);
		}
		//setTreeid(null);
		if (cycle.getRequestContext().getParameters("lx") != null) {
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName = visit.getString1();
		} else {
			if (visit.getString1().equals("")) {
				mstrReportName = visit.getString1();
			}

		}
		getToolbars();
		blnIsBegin = true;

	}

    // ��Ӧ��
	
    public IDropDownBean getGongysDropDownValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean2()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)getGongysDropDownModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean2();
    }
    public void setGongysDropDownValue(IDropDownBean Value) {
//    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean2()!=null){
//	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean2().getId()){
//	    		((Visit) getPage().getVisit()).setBooleanData1(true);
//	    	}else{
//	    		((Visit) getPage().getVisit()).setBooleanData1(false);
//	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean2(Value);
//    	}
    }
    public void setGongysDropDownModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public IPropertySelectionModel getGongysDropDownModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
            getGongysDropDownModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }
    public void getGongysDropDownModels() {
    	String sql="select id,mingc from meicb union select rownum+20 as id,beiz as mingc from (select distinct beiz from meicb) ";
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"*ú��")) ;
        return ;
    } 

	// ����
    public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
				Calendar stra=Calendar.getInstance();
//				stra.set(DateUtil.getYear(new Date()), 0, 1);
				stra.setTime(new Date());
				stra.add(Calendar.DATE,-1);
				((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}
	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));

		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		((Visit) getPage().getVisit()).setString5(value);
	}
   
    //����
    public IDropDownBean getLeixSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getLeixSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean4();
    }
    public void setLeixSelectValue(IDropDownBean Value) {

	    	((Visit) getPage().getVisit()).setDropDownBean4(Value);

    }
    public void setLeixSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel4(value);
    }

    public IPropertySelectionModel getLeixSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
            getLeixSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }
    public void getLeixSelectModels() {
    	String sql="select id,mingc from yunsdwb";
     //   ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"ȫ��")) ;
        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(sql,"*���䵥λ")) ;
        return ;
    }

    private String treeid;
	public String getTreeid() {
		return treeid;
	}
	public void setTreeid(String a) {
		treeid=a;
	}
	
	private String _cheph;
	
	public String getCheph(){
		return _cheph;
	}
	
	public void setCheph(String a){
		_cheph=a;
	}
    public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	�糧����
    public IDropDownBean getDiancmcValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean5()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean)getDiancmcModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean5();
    }
    public void setDiancmcValue(IDropDownBean Value) {

	    	((Visit) getPage().getVisit()).setDropDownBean5(Value);

    }
    public void setDiancmcModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel5(value);
    }

    public IPropertySelectionModel getDiancmcModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
            getDiancmcModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel5();
    }
    public void getDiancmcModels() {
    	String sql="select id,mingc from xiecfsb";
     //   ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"ȫ��")) ;
        ((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(sql,"*ж����ʽ")) ;
        return ;
    }
//״̬
    public IDropDownBean getZhuangtValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean6()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean6((IDropDownBean)getZhuangtModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean6();
    }
    public void setZhuangtValue(IDropDownBean Value) {

	    	((Visit) getPage().getVisit()).setDropDownBean6(Value);

    }
    public void setZhuangtModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel6(value);
    }

    public IPropertySelectionModel getZhuangtModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
            getZhuangtModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel6();
    }
    public void getZhuangtModels() {
    	  List list=new ArrayList();
//          list.add(new IDropDownBean(1,"*״̬"));
          list.add(new IDropDownBean(1,"���"));
          list.add(new IDropDownBean(2,"δ���"));
          ((Visit) getPage().getVisit()).setProSelectionModel6(new IDropDownModel(list)) ;
        return ;
    }

//��Ա   
    public IDropDownBean getRenyValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean7()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean7((IDropDownBean)getRenyModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean7();
    }
    public void setRenyValue(IDropDownBean Value) {

	    	((Visit) getPage().getVisit()).setDropDownBean7(Value);

    }
    public void setRenyModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel7(value);
    }

    public IPropertySelectionModel getRenyModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {
            getRenyModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel7();
    }
    public void getRenyModels() {
    	String sql="select id,quanc from renyxxb where bum='��������'";
     //   ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"ȫ��")) ;
       
    	((Visit) getPage().getVisit()).setProSelectionModel7(new IDropDownModel(sql,"*��Ա")) ;
        return ;
    }
//bh
 
    public IDropDownBean getBhValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean8()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean8((IDropDownBean)getBhModel().getOption(20));
   	}
    	
       return  ((Visit) getPage().getVisit()).getDropDownBean8();
    }
    public void setBhValue(IDropDownBean Value) {

	    	((Visit) getPage().getVisit()).setDropDownBean8(Value);

    }
    public void setBhModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel8(value);
    }

    public IPropertySelectionModel getBhModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
            getBhModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel8();
    }
    public void getBhModels() {
    	
    	List gm = new ArrayList();
		for (int i = 0; i < 24; i++){
			if(i<10){
			gm.add(new IDropDownBean(String.valueOf(i), "0"+String.valueOf(i)));}else
		    gm.add(new IDropDownBean(String.valueOf(i), String.valueOf(i)));
     //   ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"ȫ��")) ;
		}
    	((Visit) getPage().getVisit()).setProSelectionModel8(new IDropDownModel(gm)) ;
        return ;
    }
    
//eh

    public IDropDownBean getEhValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean9()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean9((IDropDownBean)getEhModel().getOption(20));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean9();
    }
    public void setEhValue(IDropDownBean Value) {

	    	((Visit) getPage().getVisit()).setDropDownBean9(Value);

    }
    public void setEhModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel9(value);
    }

    public IPropertySelectionModel getEhModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel9() == null) {
            getEhModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel9();
    }
    public void getEhModels() {
    	List gm = new ArrayList();
		for (int i = 0; i < 24; i++){
				if(i<10){
				gm.add(new IDropDownBean(String.valueOf(i), "0"+String.valueOf(i)));}else
			    gm.add(new IDropDownBean(String.valueOf(i), String.valueOf(i)));
		}
    	((Visit) getPage().getVisit()).setProSelectionModel9(new IDropDownModel(gm)) ;
        return ;
    }
//  bs

    public IDropDownBean getBsValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean10()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean10((IDropDownBean)getBsModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean10();
    }
    public void setBsValue(IDropDownBean Value) {

	    	((Visit) getPage().getVisit()).setDropDownBean10(Value);

    }
    public void setBsModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel10(value);
    }

    public IPropertySelectionModel getBsModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {
            getBsModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel10();
    }
    public void getBsModels() {
    	List gm = new ArrayList();
		for (int i = 0; i < 60; i++){
				if(i<10){
				gm.add(new IDropDownBean(String.valueOf(i), "0"+String.valueOf(i)));}else
			    gm.add(new IDropDownBean(String.valueOf(i), String.valueOf(i)));
		}
    	((Visit) getPage().getVisit()).setProSelectionModel10(new IDropDownModel(gm)) ;
        return ;
    }
//  es

    public IDropDownBean getEsValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean11()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean11((IDropDownBean)getEsModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean11();
    }
    public void setEsValue(IDropDownBean Value) {

	    	((Visit) getPage().getVisit()).setDropDownBean11(Value);

    }
    public void setEsModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel11(value);
    }

    public IPropertySelectionModel getEsModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel11() == null) {
            getEsModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel11();
    }
    public void getEsModels() {
    	List gm = new ArrayList();
		for (int i = 0; i < 60; i++){
				if(i<10){
				gm.add(new IDropDownBean(String.valueOf(i), "0"+String.valueOf(i)));}else
			    gm.add(new IDropDownBean(String.valueOf(i), String.valueOf(i)));
		}
    	((Visit) getPage().getVisit()).setProSelectionModel11(new IDropDownModel(gm)) ;
        return ;
    }

	//
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}
