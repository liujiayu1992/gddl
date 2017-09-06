package com.zhiren.pub.shujdb;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.zhiren.common.ext.ExtTreeUtil;
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
import com.zhiren.webservice.InterCom;

public class Shujdb extends BasePage {
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

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		//if (mstrReportName.equals(REPORT_NAME_Hetltj)) {
		return getRiz();
		//} else {
		//	return "无此报表";
		//}
	}
	// 合同量分厂分矿分矿分厂统计报表
	private String getRiz() {
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		String sql="select dc.id diancxxb_id,dianczhb.jiekzhb_id,dc.mingc\n" +
		"from (\n" + 
		"select *\n" + 
		"from diancxxb\n" + 
		"where diancxxb.id in(\n" + 
		"select diancxxb.fuid\n" + 
		"from dianczhb,diancxxb\n" + 
		"where  dianczhb.diancxxb_id=diancxxb.id\n" + 
		"union\n" + 
		"select dianczhb.diancxxb_id\n" + 
		"from dianczhb\n" + 
		"union\n" + 
		"select id\n" + 
		"from diancxxb\n" + 
		"where diancxxb.jib=1)\n" + 
		" start with id="+getTreeid()+"\n" + 
		" connect by fuid=prior id\n" + 
		" )dc,dianczhb where dc.id=dianczhb.diancxxb_id";
		ResultSetList rs=con.getResultSetList(sql);
		ArrHeader = new String[1][7];
		ArrWidth = new int[] {100,65,250,250,50,50,80};//任务名称", "标识","操作","执行状态","执行时间","错误代码","类别","错误消息"
		ArrHeader[0] = new String[] { "单位","任务","集团数据","厂级数据","错误","正确","不可访问"};
		//定义行数组
		//定义list
		List list=new ArrayList();
		int row=0;
		while(rs.next()){//选择单位下属电厂实体（n）
			String diancxxb_id=rs.getString(0);//diancxxb_id
			String zhuanghb_id=rs.getString(1);//jiekzhb_id
			String diancmc=rs.getString(2);//电厂名称
			String renwWhere="";
			if(getrenwValue().getId()!=-1){
				renwWhere=" and renwmcb.id="+getrenwValue().getId();
			}
			String sql1=
			"select Sql_CJ,SQL_JT,renwms\n" +
			"from shujdbb,renwmcb\n" + 
			"where diancxxb_id=(\n" + 
			"select nvl(max(diancxxb_id),0)diancxxb_id\n" + 
			"from shujdbb\n" + 
			"where diancxxb_id="+diancxxb_id+") and shujdbb.renwmc=renwmcb.renw "+renwWhere;//增加任务筛选
			JDBCcon con1=new JDBCcon();
			ResultSet rs1=con1.getResultSet(sql1);
			try {
				while(rs1.next()){//每个电厂的下属任务
					row++;
					String renwms=rs1.getString("renwms");
					String Sql_CJ=rs1.getString("Sql_CJ");
					String SQL_JT=rs1.getString("SQL_JT");
					// 取集团数据 规格:(日期、电厂),只能返回一个值
					SQL_JT=SQL_JT.replaceFirst("%%","'"+getRiq1()+"'");//增加日期
					SQL_JT=SQL_JT.replaceFirst("%%",diancxxb_id);//增加电厂id
					JDBCcon con2=new JDBCcon();
					ResultSet rs2=con2.getResultSet(SQL_JT);
					String jitsj="";
					if(rs2.next()){
						jitsj=rs2.getString(1)==null?"":rs2.getString(1);
					}
					//取厂级数据
					Sql_CJ=Sql_CJ.replaceFirst("%%","'"+getRiq1()+"'");//增加日期
					Sql_CJ=Sql_CJ.replaceFirst("%%",diancxxb_id);//增加电厂id
					String changjsj=InterCom.getSqlString(zhuanghb_id,Sql_CJ);
					String[] rowDate=new String[7];
					rowDate[0]=diancmc;
					rowDate[1]=renwms;
					rowDate[2]=jitsj;
					rowDate[3]=changjsj;
					String zhuangt="";
					if(changjsj.equals("出错啦")){
						zhuangt="Y";
						rowDate[4]="";
						rowDate[5]="";
						rowDate[6]=zhuangt;
					}else if( jitsj.equals(changjsj)){
						zhuangt="Y";
						rowDate[4]="";
						rowDate[5]=zhuangt;
						rowDate[6]="";
					}else{
						zhuangt="Y";
						rowDate[4]=zhuangt;
						rowDate[5]="";
						rowDate[6]="";
					}
					list.add(rowDate);
					con2.Close();
				 }
				rt.setBody(new Table(row+1+1, 7));//增加一行标题一个合计
				//增加到报表
				for(int j=0;j<list.size();j++){//从第二行开始
					rt.body.setCellValue(j+2, 1, ((String[])list.get(j))[0]);//单位
					rt.body.setCellValue(j+2, 2, ((String[])list.get(j))[1]);//任务
					rt.body.setCellValue(j+2, 3, ((String[])list.get(j))[2]);//集团数据
					rt.body.setCellValue(j+2, 4, ((String[])list.get(j))[3]);//厂级数据
					rt.body.setCellValue(j+2, 5, ((String[])list.get(j))[4]);//任务
					rt.body.setCellValue(j+2, 6, ((String[])list.get(j))[5]);//集团数据
					rt.body.setCellValue(j+2, 7, ((String[])list.get(j))[6]);//厂级数据
				}
//				合计
				int count_er=0;
				int count_ri=0;
				int count_net=0;
				for(int i=2;i<row+2;i++){
					count_er=rt.body.getCellValue(i, 5).equals("Y")?count_er+1:count_er;
					count_ri=rt.body.getCellValue(i, 6).equals("Y")?count_ri+1:count_ri;
					count_net=rt.body.getCellValue(i, 7).equals("Y")?count_net+1:count_net;
				}
				rt.body.setCellValue(row+2, 1, "合计");//错误
				rt.body.setCellValue(row+2, 5, String.valueOf(count_er));//错误
				rt.body.setCellValue(row+2, 6, String.valueOf(count_ri));//正确
				rt.body.setCellValue(row+2, 7, String.valueOf(count_net));//网络或其他不可访问错误
				
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
				con1.Close();
				con.Close();
			}
		}
		//
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.setTitle("数	据	对	比", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 3, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 4, "日期:" +getRiq1(),Table.ALIGN_RIGHT);
		rt.body.setPageRows(21);
		rt.body.mergeCol(1);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 7, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}
	
	private boolean ShangcButton = false;

	public void ShangcButton(IRequestCycle cycle) {
		ShangcButton = true;
	}
	private boolean shousButton = false;

	public void shousButton(IRequestCycle cycle) {
		shousButton = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (shousButton) {
			blnIsBegin = true;
		}
		if (ShangcButton) {
			
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
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDefaultTree(null);
			blnIsBegin = false;
		}
		
		
		getToolbars();
	}
		public IPropertySelectionModel getrenwModel() {
			if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
				getrenwModels();
			}
			return ((Visit)getPage().getVisit()).getProSelectionModel1();
		}
		
		public IDropDownBean getrenwValue() {
			if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
				((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getrenwModel().getOption(0));
			}
			return ((Visit)getPage().getVisit()).getDropDownBean1();
		}
		
		public void setrenwValue(IDropDownBean Value) {
			((Visit)getPage().getVisit()).setDropDownBean1(Value);
		}

		public IPropertySelectionModel getrenwModels() {
			List list=new ArrayList();
			list.add(new IDropDownBean(-1,"全部") );
			String sql = 

				"select renwmcb.id,renwmcb.renwms\n" +
				"from shujdbb,renwmcb\n" + 
				"where shujdbb.renwmc=renwmcb.renw order by id";

			setrenwModel(new IDropDownModel(list,sql)) ;
			return ((Visit) getPage().getVisit()).getProSelectionModel1();
		}

		public void setrenwModel(IPropertySelectionModel _value) {
			((Visit)getPage().getVisit()).setProSelectionModel1(_value);
		}
		//电厂名称
		private IPropertySelectionModel _IDiancModel;
		public IPropertySelectionModel getDiancmcModel() {
			if (((Visit)getPage().getVisit()).getProSelectionModel2() == null) {
				getDiancmcModels();
			}
			return ((Visit)getPage().getVisit()).getProSelectionModel2();
		}
		
		public IDropDownBean getDiancmcValue() {
			if(((Visit)getPage().getVisit()).getDropDownBean2()==null){
				((Visit)getPage().getVisit()).setDropDownBean2((IDropDownBean)getDiancmcModel().getOption(0));
			}
			return ((Visit)getPage().getVisit()).getDropDownBean2();
		}
		
		public void setDiancmcValue(IDropDownBean Value) {
			((Visit)getPage().getVisit()).setDropDownBean2(Value);
		}

		public IPropertySelectionModel getDiancmcModels() {
			String sql="select id,mingc\n" +
			"from diancxxb\n" ;
			 setDiancmcModel(new IDropDownModel(sql)) ;
			 return ((Visit) getPage().getVisit()).getProSelectionModel2();
		}

		public void setDiancmcModel(IPropertySelectionModel _value) {
			((Visit)getPage().getVisit()).setProSelectionModel2(_value);
		}
		private String riq1;
		public String getRiq1(){
			if(riq1==null||riq1.equals("")){
				riq1=DateUtil.FormatDate(new Date());
			}
			return riq1;
		}
		public void setRiq1(String value){
			riq1=value;
		}

   //ext
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
		public String getTreeScript() {
			System.out.print(((Visit) this.getPage().getVisit()).getDefaultTree().getScript());
			return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
			
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
		private void getToolbars(){
			Visit visit = (Visit) getPage().getVisit();
			Toolbar tb1 = new Toolbar("tbdiv");
			//签订日期
			tb1.addText(new ToolbarText("日期:"));
			DateField df = new DateField();
			df.setValue(this.getRiq1());
			df.Binding("riq","");// 与html页中的id绑定,并自动刷新
			df.setWidth(80);
			tb1.addField(df);
			//单位
			String sql=
				"select id,mingc,level jib\n" +
				"from diancxxb\n" + 
				"where diancxxb.id in(\n" + 
				"select diancxxb.fuid\n" + 
				"from dianczhb,diancxxb\n" + 
				"where  dianczhb.diancxxb_id=diancxxb.id\n" + 
				"union\n" + 
				"select dianczhb.diancxxb_id\n" + 
				"from dianczhb\n" + 
				"union\n" + 
				"select id\n" + 
				"from diancxxb\n" + 
				"where diancxxb.jib=1)\n" + 
				" start with id=100\n" + 
				" connect by fuid=prior id";

			DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"",null,getTreeid());
			dt.setTree_window(sql,"diancTree",""+visit.getDiancxxb_id(),"",null,getTreeid());
			visit.setDefaultTree(dt);
			TextField tf = new TextField();
			tf.setId("diancTree_text");
			tf.setWidth(100);
			tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
			
			ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
			tb2.setIcon("ext/resources/images/list-items.gif");
			tb2.setCls("x-btn-icon");
			tb2.setMinWidth(20);
			
			tb1.addText(new ToolbarText("单位:"));
			tb1.addField(tf);
			tb1.addItem(tb2);
			
			ComboBox comb1=new ComboBox();
			comb1.setWidth(100);
			comb1.setTransform("renwSelect");
			comb1.setLazyRender(true);//动态绑定
			tb1.addField(comb1);
			
			ToolbarButton tbb = new ToolbarButton(null,"刷新","function(){document.all.item('shous').click();}");
			tb1.addItem(tbb);
			setToolbar(tb1);
		}
}
