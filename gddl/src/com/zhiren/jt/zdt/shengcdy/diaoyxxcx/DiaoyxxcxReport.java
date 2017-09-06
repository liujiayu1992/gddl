////2008-08-05 chh 
//�޸����� ��ȼ�Ϲ�˾���û����Բ鿴������
//		   ��ϸ������ʾ

package com.zhiren.jt.zdt.shengcdy.diaoyxxcx;

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
import com.zhiren.common.SysConstant;
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

public class DiaoyxxcxReport extends BasePage {
//	public final static String LX_FC="fc";
//	public final static String LX_FK="fk";
//	public final static String LX_FKFC="fkfc";
//	public final static String LX_FCFK="fcfk";
//	public final static String LX_QP="qp";
	
	private String leix="fc";
	
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
		
		int intLen=0;
		String lx=getLeix();
		intLen=lx.indexOf(",");
		String diancid="";
		String leix="";
		if (intLen>0){
			String [] pa=lx.split(",");
			if (pa.length==2){
				leix=pa[0];//����
				diancid="" +pa[1];//�糧ID
			}else{
				diancid="-1";
			}
		}else{
			return "";
		}
		
		if(leix.equals("1")){//װ��Ԥ��
			diancid=" and diancxxb_id="+diancid;
			return getQibb(diancid);
		}else if(leix.equals("0")){//��վԤ��
			return "";
		}else if(leix.equals("2")){//ȡ���ſ�
			diancid=" and dc.id="+diancid;
			return getQuzpk(diancid);
		}
		return "";
	}

	public String getLeix(){
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getString9().equals("")) {
			return visit.getString9();
        } 
		return ""; 
	}

	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}
	
	private String getQuzpk(String diancid){//ȡ���ſ� leix��2
		
		
		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		Date datEnd=((Visit)getPage().getVisit()).getDate2();
		
		String date=DateUtil.FormatDate(datEnd);
		
		String riq=OraDate(datEnd);//��ǰ����
		String friq=OraDate(DateUtil.getFirstDayOfMonth(datEnd));
//		String riq1=FormatDate(_BeginriqValue);
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="ȡ���ſ���ϸ";
		int iFixedRows=0;//�̶��к�
		int iCol=0;//����
		
		//danwmc=getTreeDiancmc(this.getTreeid());
		//��������
				titlename=titlename+"";
			strSQL=
				"select dc.mingc,to_char(q.riq,'yyyy-mm-dd') as riq,q.quz6,q.paik6,q.daix6,q.quz18,q.paik18,q.daix18,q.CHANGCKC,\n" +//q.changtcs,
				"            q.changtcs,q.JIAOJZC,q.DAOZC,q.changtcs,q.changtsj\n" + 
				"       from quzpkb q,diancxxb dc\n" + 
				"       where q.diancxxb_id=dc.id "+diancid+" and q.riq>="+friq+" and q.riq<="+riq+" order by riq ";

		
		 ArrHeader =new String[2][14];
		 ArrHeader[0]=new String[] {"�糧","����","(ǰ��)18ʱ---(����)6ʱ","(ǰ��)18ʱ---(����)6ʱ","(ǰ��)18ʱ---(����)6ʱ","(ǰ��)18ʱ---(����)18ʱ","(ǰ��)18ʱ---(����)18ʱ","(ǰ��)18ʱ---(����)18ʱ","���Ӽ�¼","���Ӽ�¼","���Ӽ�¼","���Ӽ�¼","��ͣʱ��","��ͣʱ��"};
		 ArrHeader[1]=new String[] {"�糧","����","ȡ��","�ſ�","��ж","ȡ��","�ſ�","��ж","����ճ�","�����س�","����վ��","վ��","��ͣ����","ƽ��ʱ��"};

		 ArrWidth =new int[] {150,100,60,60,60,60,60,60,60,60,60,60,60,60};


			ResultSet rs = cn.getResultSet(strSQL);


		
			
			// ����
			rt.setBody(new Table(rs,2, 0, 1));

			rt.setTitle(titlename, ArrWidth);
//			rt.setTitle(intyear+"��"+intMonth+"��"+titlename, ArrWidth);
			rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+((Visit) getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(5, 4, FormatDate(DateUtil.getDate(date)), Table.ALIGN_CENTER);
			rt.setDefaultTitle(13, 2, "��λ:��", Table.ALIGN_RIGHT);
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(18);
			rt.body.setHeaderData(ArrHeader);// ��ͷ����
			rt.body.mergeFixedRow();

			rt.body.mergeFixedCols();
			rt.body.ShowZero = true;
			//ҳ�� 
			
			  rt.createDefautlFooter(ArrWidth);
			  rt.setDefautlFooter(1,3,"�Ʊ�ʱ��:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			  rt.setDefautlFooter(6,2,"���",Table.ALIGN_CENTER);
			  rt.setDefautlFooter(13,2,"�Ʊ�:"+((Visit)getPage().getVisit()).getRenymc(),Table.ALIGN_RIGHT);
//			  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
			 
			
			
			//����ҳ��
//		    rt.createDefautlFooter(ArrWidth);
			
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
			return rt.getAllPagesHtml();
		
	}
	
	private String getQibb(String diancid) {
		JDBCcon conn = new JDBCcon();
		ChessboardTable cd = new ChessboardTable();
		JDBCcon cn = new JDBCcon();
		Date datEnd=((Visit)getPage().getVisit()).getDate2();
		
		String date=DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(datEnd))+"-"+DateUtil.FormatDate(datEnd);
		
		String riq=OraDate(datEnd);//��ǰ����
		String friq=OraDate(DateUtil.getFirstDayOfMonth(datEnd));
		String gongysfuid = "(select fuid from gongysb where id="
				+ getGongysDropDownValue().getId() + "\n)";
		long fuid = 0;
		ResultSet rs1 = conn.getResultSet(gongysfuid);
		try {
			if (rs1.next()) {
				fuid = rs1.getLong("fuid");
			}
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		ArrWidth = new int[] { 150, 40, 80, 80, 80, 80, 80, 80 };

//		String gongystj = "";
//		if (getGongysDropDownValue().getId() == -1) {
//			gongystj = ",(select xgys.id as id,xgys.xuh,decode(xgys.fuid,0,xgys.mingc,dgys.mingc) as mingc\n"
//					+ "  from gongysb xgys,gongysb dgys\n"
//					+ "  where xgys.fuid=dgys.id(+))gys";
//
//		} else {
//			gongystj = ",(select gys.id,gys.xuh,gys.mingc from gongysb gys  where (gys.id="
//					+ getGongysDropDownValue().getId()
//					+ " or  gys.fuid="
//					+ getGongysDropDownValue().getId() + "))gys ";
//
//		}
//		
		//�õ������ݣ�ʱ��
		StringBuffer strRow = new StringBuffer();
		strRow.append("select 'С��' as ʱ�� from dual union all \n");
		strRow.append("(select to_char("+friq+"+rownum-1,'yyyy-mm-dd') as riq \n");
		strRow.append(" from all_objects");
		strRow.append(" where rownum<=("+riq+"-"+friq+"+1)) \n");
		
		//�õ������ݣ�������
		StringBuffer strCol = new StringBuffer();
		
		//--ʵ��װ��
		strCol.append("select decode(grouping(gys.mingc),1,'С��',gys.mingc) as ��Ӧ�� \n");
		strCol.append("from zhuangcyb,gongysb gys where gongysb_id=gys.id "+diancid+" \n");
		//strCol.append("and riq>="+friq+"\n");
		strCol.append("and riq>="+friq+" and riq<="+riq+"\n");
		strCol.append("group by rollup (gys.mingc) order by grouping(gys.mingc) desc,gys.mingc \n");
//		

//		strCol.append("select decode(grouping(g.mingc), 1, 'С��', g.mingc) as ��Ӧ�� from\n");
//		strCol.append("(select distinct gongysb_id\n");
//		strCol.append("  from zhuangcyb\n");
//		strCol.append(" where riq >= "+friq+"\n");
//		strCol.append("   and riq <= "+riq+"\n");
//		strCol.append("   "+diancid+"\n");
//		strCol.append("union\n");
//		strCol.append("select distinct gongysb_id\n");
//		strCol.append("  from fahb\n");
//		strCol.append(" where daohrq >= "+friq+"\n");
//		strCol.append("   and daohrq <= "+riq+"\n");
//		strCol.append("   "+diancid+") z,gongysb g\n");
//		strCol.append("where z.gongysb_id=g.id\n");
//		strCol.append("\n");
//		strCol.append(" group by rollup(g.mingc)\n");
//		strCol.append(" order by grouping(g.mingc) desc, g.mingc");

		

		// �õ�ȫ������ ���̱�
		StringBuffer sbsql = new StringBuffer();
// sbsql.append("select grouping(to_char(riq.riq,'yyyy-mm-dd')) as
// rowjb,grouping(gys.mingc) as coljb,\n");
// sbsql.append("
// decode(grouping(to_char(riq.riq,'yyyy-mm-dd')),0,to_char(riq.riq,'yyyy-mm-dd'),'С��')
// as ʱ��, \n");
//		sbsql.append("    decode(grouping(gys.mingc),0,gys.mingc,2,'С��','С��') as ��Ӧ��,  \n");
//		sbsql.append("    sum(zc.zuorzc)  as ʵ��װ��, sum(zc.jinrzc)  as ʵ�ʵ����� \n");
//		sbsql.append("from zhuangcyb zc,gongysb gys,(select "+friq+"+rownum as riq from all_objects \n");
//		sbsql.append(" where rownum<=("+riq+"-"+friq+")) riq \n");
//		sbsql.append("   where zc.gongysb_id=gys.id and zc.riq=riq.riq \n").append(diancid);
//		sbsql.append("   group by cube(to_char(riq.riq,'yyyy-mm-dd'),gys.mingc)  \n");
//		sbsql.append("	 order by grouping(to_char(riq.riq,'yyyy-mm-dd')) desc,to_char(riq.riq,'yyyy-mm-dd'),  grouping(gys.mingc) desc,gys.mingc \n");
		
		
		sbsql.append("select grouping(to_char(riq.riq, 'yyyy-mm-dd')) as rowjb, \n");
		sbsql.append("              grouping(gys.mingc) as coljb, \n");
		sbsql.append("       decode(grouping(to_char(riq.riq, 'yyyy-mm-dd')), \n");
		sbsql.append("              0, \n");
		sbsql.append("              to_char(riq.riq, 'yyyy-mm-dd'), \n");
		sbsql.append("              'С��') as ʱ��, \n");
		sbsql.append("       decode(grouping(gys.mingc), 0, gys.mingc, 2, 'С��', 'С��') as ��Ӧ��, \n");
		sbsql.append("       sum(sj.zuorzc) as ʵ��װ�� \n");
//		sbsql.append("       sum(sj.ches) as ʵ�ʵ����� \n");
		sbsql.append("       from (select zc.gongysb_id,(zc.riq-1) as riq,sum(nvl(zc.zuorzc,0)) zuorzc \n");
		sbsql.append("       	from zhuangcyb zc \n");
		sbsql.append("          where zc.riq >= "+friq+"+1 and zc.riq <= "+riq+"+1 \n").append(diancid);
		sbsql.append("          group by zc.gongysb_id,zc.riq) sj,gongysb gys, \n");
		sbsql.append("       (select "+friq+"+ rownum-1 as riq \n");
		sbsql.append("          from all_objects \n");
		sbsql.append("         where rownum <= ("+riq+"-"+friq+")+1) riq \n");
		sbsql.append("      where riq.riq = sj.riq(+) and sj.gongysb_id = gys.id \n");
		sbsql.append("      group by cube(to_char(riq.riq, 'yyyy-mm-dd'), gys.mingc) \n");
		sbsql.append("      order by grouping(to_char(riq.riq, 'yyyy-mm-dd')) desc, \n");
		sbsql.append("          to_char(riq.riq, 'yyyy-mm-dd'),grouping(gys.mingc) desc,gys.mingc \n");
		
		
		System.out.println(sbsql);
		
		cd.setRowNames("ʱ��");
		cd.setColNames("��Ӧ��");
		cd.setDataNames("ʵ��װ��");
		cd.setDataOnRow(false);
		cd.setRowToCol(false);
		cd.setData(strRow.toString(), strCol.toString(), sbsql.toString());
		ArrWidth = new int[cd.DataTable.getCols()];
		int cols = ArrWidth.length;
		for (int i = 1; i < ArrWidth.length; i++) {
			ArrWidth[i] = 80;
		}
		ArrWidth[0] = 80;
		ArrWidth[1] = 80;
		rt.setBody(cd.DataTable);
		rt.body.setWidth(ArrWidth);
		rt.body.mergeFixedRowCol();
		rt.body.ShowZero = true;
		rt.setTitle("ú��װ����ϸ", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.body.setCellValue(1, 0, "����");
		rt.body.setCellValue(1, 1, "����");
		for (int j = 1; j < cols + 1; j++) {
			if (j == 2) {
				rt.body.setColAlign(j, Table.ALIGN_CENTER);
			}
			if (j > 2) {
				rt.body.setColAlign(j, Table.ALIGN_RIGHT);
			}
		}
		rt.setDefaultTitle(1, 2, "�Ʊ�λ:"+ ((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, cols-4, date, Table.ALIGN_CENTER);
		rt.body.setPageRows(21);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3,"��ӡ����:"+ FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))), Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 1, "��λ:������", Table.ALIGN_RIGHT);
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		conn.Close();
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
		if(cycle.getRequestContext().getParameters("lx") !=null) {
			visit.setString9((cycle.getRequestContext().getParameters("lx")[0]));
			leix = visit.getString9();
        }else{
        	if(!visit.getString1().equals("")) {
        		leix = visit.getString9();
            }
        }
	}

	// ��Ӧ��
	public IDropDownBean getGongysDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getGongysDropDownModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setGongysDropDownValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean2(Value);
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
		String sql = "select id,mingc\n" + "from gongysb\n";
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, "ȫ��"));
		return;
	}

	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) this.getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (treeid != null) {
			if (!treeid.equals(this.treeid)) {
				((TextField) getToolbar().getItem("diancTree_text"))
						.setValue(((IDropDownModel) getDiancmcModel())
								.getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
						.setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	// �糧����
	private IPropertySelectionModel _IDiancModel;

	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	private boolean _DiancmcChange = false;

	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getDiancmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == Value) {
			_DiancmcChange = false;
		} else {
			_DiancmcChange = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	// �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib(String DiancTreeJib) {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		
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

	// �õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getTreeDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return IDropDownDiancmc;
	}

	//
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}