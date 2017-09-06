package com.zhiren.jt.zdt.shengcdy.jizyxqk;

/**
 * ������ϸ��ѯ
 * @author xzy
 */
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
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Tingjjzmxbreport extends BasePage {
	
	private String jizbh="";
	private String diancxxb_id="";
	private String beginriq="";
	private String endriq="";
	
	public boolean getRaw() {
		return true;
	}

	public void submit(IRequestCycle cycle) {
		
	}
	
	public String getBiaotmc() {
		String biaotmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc = "select  zhi from xitxxb where mingc='������ⵥλ����'";
		ResultSet rs = cn.getResultSet(sql_biaotmc);
		try {
			while (rs.next()) {
				biaotmc = rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return biaotmc;
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

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getPrintData();
	}

	public Date getBeginriqDate() {
		if (((Visit)getPage().getVisit()).getDate1()==null){
			((Visit)getPage().getVisit()).setDate1(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay));
		}
		return ((Visit)getPage().getVisit()).getDate1();
	}
	
	
	private String getPrintData(){
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("");
		
		sbsql.append("select dc.mingc,decode(grouping(jz.jizbh),1,'С��',jz.jizbh) as jizbh, \n");
		sbsql.append(" 	sum(jz.jizurl) as jizrl, \n");
		sbsql.append(" 	decode(grouping(rownum),1,max(rownum),rownum) as cis, \n");
		sbsql.append(" 	decode(grouping((to_char(yx.kaisrq,'yyyy-mm-dd')||'��'||to_char(yx.jiesrq,'yyyy-mm-dd'))),1,to_char(sum(yx.jiesrq-yx.kaisrq+1)),(to_char(yx.kaisrq,'yyyy-mm-dd')||'��'||to_char(yx.jiesrq,'yyyy-mm-dd'))) as tians, \n");
		sbsql.append(" 	sum(yx.tingjyxdl) as tingjyxdl \n");
		sbsql.append("from jizyxqkb yx, diancxxb dc, jizb jz \n");
		sbsql.append("where yx.diancxxb_id = dc.id and yx.jizb_id = jz.id and dc.id = jz.diancxxb_id and dc.id="+diancxxb_id+" \n");
		sbsql.append(" and yx.kaisrq>=to_date( '"+beginriq+"','yyyy-mm-dd') and yx.kaisrq<=to_date( '"+endriq+"','yyyy-mm-dd') \n");
		sbsql.append(" and yx.shebzt = 'ȱúͣ��' and jz.id='"+jizbh+"' \n");
		sbsql.append("group by rollup(dc.mingc,jz.jizbh,rownum,(to_char(yx.kaisrq,'yyyy-mm-dd')||'��'||to_char(yx.jiesrq,'yyyy-mm-dd')))\n");
		sbsql.append("having not (grouping(jizbh) || grouping((to_char(yx.kaisrq,'yyyy-mm-dd')||'��'||to_char(yx.jiesrq,'yyyy-mm-dd')))) =1 and grouping(mingc)=0\n");
		sbsql.append("order by grouping(dc.mingc) desc,max(dc.xuh),grouping(jz.jizbh) desc,max(jz.xuh)  \n");
		
		
		ResultSet rs = con.getResultSet(sbsql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		String ArrHeader[][]=new String[1][6];
		ArrHeader[0]=new String[] {"��λ","������","��������(MW)","ȱúͣ������","ȱúͣ��ʱ��","Ӱ�����(ǧ��ʱ)"};

		int ArrWidth[]=new int[] {150,70,80,90,90,90};		

		Table bt=new Table(rs,1,0,2);
		rt.setBody(bt);
		bt.setColAlign(2, Table.ALIGN_CENTER);
		
		rt.body.setPageRows(22);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedRow();//�ϲ���
		rt.body.mergeFixedCols();//�Ͳ���
		rt.setTitle("������ϸ", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);

		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "��ӡ����:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}
	
	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}

	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}

		blnIsBegin = true;
		//��������
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			visit.setString9((cycle.getRequestContext().getParameters("lx")[0]));
			jizbh = visit.getString9();
        }else{
        	if(!visit.getString1().equals("")) {
        		jizbh = visit.getString9();
            }
        }
		//�糧ID
		if(cycle.getRequestContext().getParameters("diancxxb_id") !=null) {
			visit.setString10((cycle.getRequestContext().getParameters("diancxxb_id")[0]));
			diancxxb_id = visit.getString10();
        }else{
        	if(!visit.getString1().equals("")) {
        		diancxxb_id = visit.getString10();
            }
        }
		//��ʼ����
		if(cycle.getRequestContext().getParameters("beginriq") !=null) {
			visit.setString11((cycle.getRequestContext().getParameters("beginriq")[0]));
			beginriq = visit.getString11();
        }else{
        	if(!visit.getString1().equals("")) {
        		beginriq = visit.getString11();
            }
        }
		//��������
		if(cycle.getRequestContext().getParameters("endriq") !=null) {
			visit.setString12((cycle.getRequestContext().getParameters("endriq")[0]));
			endriq = visit.getString12();
        }else{
        	if(!visit.getString1().equals("")) {
        		endriq = visit.getString12();
            }
        }
	}

}