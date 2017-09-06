package com.zhiren.jt.zdt.yansgl.rucmslys;

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
	import com.zhiren.common.ResultSetList;
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

	public class Rucmslfcmx_zgdt extends BasePage {
		public final static String LX_FC="fc";
		public final static String LX_FK="fk";
		public final static String LX_FKFC="fkfc";
		public final static String LX_FCFK="fcfk";
		public final static String LX_QP="qp";
		
		private String leix="fc";
		
		public boolean getRaw() {
			return true;
		}

		public void submit(IRequestCycle cycle) {
			
		}
		
		public String getBiaotmc() {
			String biaotmc = "";
			JDBCcon cn = new JDBCcon();
			String sql_biaotmc = "select  zhi from xitxxb where mingc='报表标题单位名称'";
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
			return getPrintDataTz();
		}

		public String getLeix(){
			Visit visit = (Visit) getPage().getVisit();
			if(!visit.getString9().equals("")) {
				return visit.getString9();
	        } 
			return ""; 
		}
		
		private String getCondtion(){
			String strCondtion="";
			int intLen=0;
			Date datStart=((Visit)getPage().getVisit()).getDate1();
			Date datEnd=((Visit)getPage().getVisit()).getDate2();
			
			long lngYunsfsId=-1;
			if  (((Visit) getPage().getVisit()).getDropDownBean4()!=null){
				lngYunsfsId= ((Visit) getPage().getVisit()).getDropDownBean4().getId();
			}
			
			long lngJihkjId= -1;
			if  (((Visit) getPage().getVisit()).getDropDownBean4()!=null){
				lngJihkjId= ((Visit) getPage().getVisit()).getDropDownBean4().getId();
			}
			
			String strDiancxxb_id=((Visit) getPage().getVisit()).getString2();
			int jib=getDiancTreeJib(strDiancxxb_id);
			String strGongys_id=((Visit) getPage().getVisit()).getString3();
			
			String strDate1=DateUtil.FormatDate(datStart);//日期字符
			String strDate2=DateUtil.FormatDate(datEnd);//日期字符
			
			strCondtion="and fh.daohrq>=to_date('"+strDate1+"','yyyy-mm-dd') \n" +
				"and fh.daohrq<=to_date('"+strDate2+"','yyyy-mm-dd') \n" ;
			
			if (lngYunsfsId!=-1){
				strCondtion=strCondtion+" and fh.yunsfsb_id=" +lngYunsfsId;
			}
			
			if (lngJihkjId!=-1){
				strCondtion=strCondtion+" and fh.jihkjb_id=" +lngJihkjId;
			}
			
			String lx=getLeix();
			intLen=lx.indexOf(",");
			if (intLen>0){
				String [] pa=lx.split(",");
				if (pa.length==2){
					strCondtion=strCondtion+" and dc.id=" +pa[0];
					strCondtion=strCondtion+" and y.dqid=" +pa[1];
				}else{
					strCondtion=" and dc.id=-1";
				}
			}else{
				if (!strGongys_id.equals("-1")){
					strCondtion=strCondtion+" and y.dqid=" +strGongys_id;
				}
				if (jib==2){
					strCondtion=strCondtion+" and (dc.fgsid=" +strDiancxxb_id +" or dc.rlgsid="+strDiancxxb_id +")"; 
				}else if (jib==3){
					strCondtion=strCondtion+" and dc.id=" +strDiancxxb_id;
				}else if (jib==-1){
					strCondtion=strCondtion+" and dc.id=" +strDiancxxb_id;
				}
			}
			return strCondtion;
		}
		
		private String getPrintDataTz(){
			StringBuffer sbsql = new StringBuffer();
		
			sbsql.append("select CHEPH,QINGCSJ,ZHONGCSJ,MAOZ,PIZ,BIAOZ,YINGK,YUNS,ZONGKD,JIANJFS, \n");
			sbsql.append("decode(BULSJ,1,'是','否') BULSJ, \n");
			sbsql.append("to_char(chepb.LURSJ, 'YYYY-MM-DD HH24:MI:SS') LURSJ,LURY \n");
			sbsql.append("from chepb where fahb_id="+leix);
			 
			 
			_CurrentPage = 1;
			_AllPages = 1;
			JDBCcon cn = new JDBCcon();
			
			String strTitle="";
			String titleName="入厂煤数量明细"+strTitle;
			
			// 报表表头定义
			Report rt = new Report();
			ResultSet rs = cn.getResultSet(sbsql.toString());
			
			
			String ArrHeader[][]=new String[1][11];
			ArrHeader[0]=new String[] {"车号/船号","轻车时间","重车时间","毛重<br>(吨)","皮重<br>(吨)","票重<br>(吨)",
					"盈亏吨<br>(吨)","运损<br>(吨)","扣吨<br>(吨)","检斤方式","是否为补录","录入时间","录入人员"};
			int ArrWidth[]=new int[] {100,80,80,60,60,60,60,60,60,60,60,80,60};
			
			Date datStart=((Visit)getPage().getVisit()).getDate1();
			Date datEnd=((Visit)getPage().getVisit()).getDate2();
			
			rt.setBody(new Table(rs, 1, 0, 1));
			rt.setTitle(titleName, ArrWidth);
			rt.setDefaultTitle(1, 3, "制表单位:"+ ((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
			rt.setDefaultTitle(4,  4,  DateUtil.Formatdate("yyyy年MM月dd日", datStart)+"至"+DateUtil.Formatdate("yyyy年MM月dd日", datEnd), Table.ALIGN_CENTER);
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(48);
			rt.body.setUseDefaultCss(true);
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			rt.body.ShowZero = true;
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 3, "打印日期:" + DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);

/*			if(rt.body.getRows()>2){
				rt.body.mergeCell(rt.body.getRows(), 1, rt.body.getRows(), 3);
				rt.body.setCellAlign(rt.body.getRows(), 1, Table.ALIGN_CENTER);
			}*/
			
			// 设置页数
			_CurrentPage = 1;
			_AllPages = rt.body.getPages();
			if (_AllPages == 0) {
				_CurrentPage = 0;
			}
			cn.Close();
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
			return DateUtil.Formatdate("yyyy年MM月dd日", _date);
		}

		public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
			Visit visit = (Visit) getPage().getVisit();
			if (visit.getRenyID() == -1) {
				visit.setboolean1(true);
				return;
			}

			blnIsBegin = true;
			if(cycle.getRequestContext().getParameters("fahb_id") !=null) {
				visit.setString10("");
				visit.setString10((cycle.getRequestContext().getParameters("fahb_id")[0]));
				leix = visit.getString10();
	        }else{
	        	if(!visit.getString10().equals("")) {
	        		leix = visit.getString10();
	            }
	        }
		}

		// 供应商
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
					.setProSelectionModel2(new IDropDownModel(sql, "全部"));
			return;
		}

		// 类型
		public IDropDownBean getLeixSelectValue() {
			if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
				((Visit) getPage().getVisit())
						.setDropDownBean4((IDropDownBean) getLeixSelectModel()
								.getOption(0));
			}
			return ((Visit) getPage().getVisit()).getDropDownBean4();
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
			List list = new ArrayList();
			list.add(new IDropDownBean(1, "分厂"));
			list.add(new IDropDownBean(2, "分矿"));
			list.add(new IDropDownBean(3, "棋盘表"));
			((Visit) getPage().getVisit())
					.setProSelectionModel4(new IDropDownModel(list));
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

		// 电厂名称
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

		// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
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

		private String getLaimlField(){
			JDBCcon con = new JDBCcon();
			String laiml = SysConstant.LaimField;
			ResultSetList rs = con.getResultSetList("select * from xitxxb where mingc = '使用集团' and zhuangt = 1 and zhi = '中国大唐'");
			if(rs.next()){
				laiml = "sum(round_new(fh.laimsl,"+((Visit) getPage().getVisit()).getShuldec()+"))";
			}
			rs.close();
			return laiml;
		}
		
		// 得到电厂树下拉框的电厂名称或者分公司,集团的名称
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