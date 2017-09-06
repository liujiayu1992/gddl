package com.zhiren.jt.zdt.shengcdy.jizyxqk;

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
import com.zhiren.common.IDropDownBean;
	import com.zhiren.common.IDropDownModel;
	import com.zhiren.common.JDBCcon;
	import com.zhiren.common.MainGlobal;
	import com.zhiren.common.ResultSetList;
	import com.zhiren.common.ErrorMessage;
import com.zhiren.common.SysConstant;
	import com.zhiren.common.ext.ExtGridUtil;
	import com.zhiren.common.ext.ExtTreeUtil;
	import com.zhiren.common.ext.GridButton;
	import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarText;
	import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
	import com.zhiren.common.ext.form.TextField;
	import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

	public class Jizyxqk extends BasePage implements PageValidateListener {
		private String msg = "";

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = MainGlobal.getExtMessageBox(msg, false);
			;
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


		// 页面变化记录
		private String Change;

		public String getChange() {
			return Change;
		}

		public void setChange(String change) {
			Change = change;
		}

		public int getDataColumnCount() {
			int count = 0;
			for (int c = 0; c < getExtGrid().getGridColumns().size(); c++) {
				if (((GridColumn) getExtGrid().getGridColumns().get(c)).coltype == GridColumn.ColType_default) {
					count++;
				}
			}
			return count;
		}

//		日期控件
		boolean riqichange=false;
		private String riqi;
		public String getRiqi() {
			if(riqi==null||riqi.equals("")){
				riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),0,DateUtil.AddType_intDay));
			}
			return riqi;
		}
		public void setRiqi(String riqi) {
			
			if(this.riqi!=null &&!this.riqi.equals(riqi)){
				this.riqi = riqi;
				riqichange=true;
			}
			 
		}
		
		public String getValueSql(GridColumn gc, String value) {
			if ("string".equals(gc.datatype)) {
				if (gc.combo != null) {
					if (gc.returnId) {
						return "" + gc.combo.getBeanId(value);
					} else {
						return "'" + value + "'";
					}
				} else {
					return "'" + value + "'";
				}

			} else if ("date".equals(gc.datatype)) {
				return "to_date('" + value + "','yyyy-mm-dd')";
			} else {
				return value;
			}
		}
		
		private void Delete(){
			JDBCcon con = new JDBCcon();

			ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
			StringBuffer sb = new StringBuffer("begin \n");
			while (rsl.next()) {
				sb.append("delete from jizyxqkb where id ="+rsl.getString("id")+";");
			}
			sb.append("end;");
			con.getDelete(sb.toString());
			con.Close();
		}

		private void Save() {
			
			Visit visit = (Visit) getPage().getVisit();
			JDBCcon con = new JDBCcon();
			con.setAutoCommit(false);
			int flag =0;

			ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
			
			long id;
			StringBuffer sb = new StringBuffer("begin \n");
			while (rsl.next()) {
				if(!rsl.getString("shebzt").equals("运行"))
				{
					//如果状态不是运行
					if(rsl.getString("id").equals("0")){
						//如果ID为0插入一条记录
					id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
					sb.append("insert into jizyxqkb(id,diancxxb_id,jizb_id,shebzt,tingjyxdl,kaisrq,jiesrq,shuom)values");
					sb.append("("+id+","+rsl.getString("diancid")+","+rsl.getString("jizb_id")+",'"+rsl.getString("shebzt")+"',"+rsl.getString("tingjyxdl"));
					sb.append(",to_date('"+rsl.getString("kaisrq")+" "+rsl.getString("kaissj")+"','yyyy-mm-dd hh24')");
					sb.append(",to_date('"+rsl.getString("jiesrq")+" "+rsl.getString("jiessj")+"','yyyy-mm-dd hh24')");
					sb.append(",'"+rsl.getString("shuom")+"');").append("\n");
					}
					else{
						//如果ID不为0修改此记录
					String biaoid = rsl.getString("id");
//					更改操作时新增日志
					MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
							SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Jizyxzt,
							"jizyxqkb",biaoid+"");
					sb.append("update jizyxqkb set shebzt = '"+rsl.getString("shebzt")+"',kaisrq=to_date('"+rsl.getString("kaisrq")+" "+rsl.getString("kaissj")+"','yyyy-mm-dd hh24'),jiesrq=to_date('"+rsl.getString("jiesrq")+" "+rsl.getString("jiessj")+"','yyyy-mm-dd hh24'),tingjyxdl='"+rsl.getString("tingjyxdl")+"',shuom='"+rsl.getString("shuom")+"' ");
					sb.append(" where jizb_id = "+rsl.getString("jizb_id")+";\n");
					
					}
				}
				else{
					//如果状态是运行
					if(rsl.getString("id").equals("0")){
						//如果ID为0不做任何操作
					}
					else{
						//如果ID不为0则删除这条状态为运行的记录
					sb.append("delete from jizyxqkb where id ="+rsl.getString("id")+";");
					}
					
				}
			}
			sb.append("end;");
			flag=con.getUpdate(sb.toString());
			if (flag!=-1){//保存成功
				this.setMsg(ErrorMessage.SaveSuccessMessage);
			}
			con.commit();
			con.Close();
			
		}
		
		private boolean _DeleteChick = false;

		public void DeleteButton(IRequestCycle cycle) {
			_DeleteChick = true;
		}

		private boolean _SaveChick = false;

		public void SaveButton(IRequestCycle cycle) {
			_SaveChick = true;
		}

		public void submit(IRequestCycle cycle) {
			if (_SaveChick) {
				_SaveChick = false;
				Save();
				getSelectData();
			}
			if (_DeleteChick){
				_DeleteChick = false;
				Delete();
				getSelectData();
			}
		}

		public void getSelectData() {

			JDBCcon con = new JDBCcon();

			String str = "";
			int treejib = this.getDiancTreeJib();
			if (treejib == 1) {// 选集团时刷新出所有的电厂
				str = "";
			} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
//				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
//						+ getTreeid() + ")";
				str = " and  (dc.fuid = "+ getTreeid() + " or dc.shangjgsid= "+this.getTreeid()+")";
			} else if (treejib == 3) {// 选电厂只刷新出该电厂
				str = "and dc.id = " + getTreeid() + "";
			}
			
//			long kaissj = this.getKaissjValue().getId();
//			long jiessj = this.getJiessjValue().getId();
			String sql = "select nvl(yx.id,0) as id,dc.id as diancid,dc.mingc as diancxxb_id,jizid as jizb_id,dc.jizbh as jizbh,dc.jizurl,\n"
						+ "      nvl(yx.shebzt,'运行') as  shebzt,nvl(yx.tingjyxdl,0) as tingjyxdl,\n"
						+ "      nvl(kaisrq,to_date('"+this.getRiqi()+" "+"','yyyy-mm-dd ')) as  kaisrq,nvl(to_char(kaisrq,'hh24'),0) as kaissj,\n"
						+ "      nvl(jiesrq,to_date('"+this.getRiqi()+" "+"','yyyy-mm-dd ')) as  jiesrq,nvl(to_char(jiesrq,'hh24'),0) as jiessj,yx.shuom\n"
						+ "  from (select yx.* from jizyxqkb yx,diancxxb dc " 
						+ "where yx.kaisrq<=to_date('"+this.getRiqi()+"','yyyy-mm-dd ') " 
						+ "and yx.jiesrq>=to_date('"+this.getRiqi()+"','yyyy-mm-dd ') and yx.diancxxb_id=dc.id "+str+" ) yx,\n"
						+ "       (select dc.mingc,dc.xuh as xuh1,jz.jizbh,jz.jizurl, jz.xuh as xuh2, dc.id,jz.id as jizid from diancxxb dc,jizb jz\n"
						+ "              where dc.id=jz.diancxxb_id "+str+" ) dc\n"
						+ " where yx.diancxxb_id(+)=dc.id and dc.jizid=yx.jizb_id(+) order by dc.xuh1,dc.xuh2";


			ResultSetList rsl = con.getResultSetList(sql);
							
			ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
			egu.setTableName("jizyxqkb");
//			egu.setWidth(990);
			egu.setWidth("bodyWidth");
			egu.getColumn("id").setHeader("JIZYXQKB_ID");
			egu.getColumn("id").setEditor(null);
			egu.getColumn("id").setHidden(true);
			egu.getColumn("diancid").setHeader("DIANCXXB_ID");
			egu.getColumn("diancid").setEditor(null);
			egu.getColumn("diancid").setHidden(true);
			egu.getColumn("diancxxb_id").setHeader("单位名称");
			egu.getColumn("diancxxb_id").setEditor(null);
			
			egu.getColumn("jizb_id").setHeader("jizb_id");
			egu.getColumn("jizb_id").setHidden(true);
			egu.getColumn("jizb_id").setEditor(null);
			egu.getColumn("jizbh").setHeader("机组编号");
			egu.getColumn("jizbh").setEditor(null);
			egu.getColumn("jizbh").setUpdate(false);
			
			egu.getColumn("jizurl").setHeader("机组容量(MW)");
			egu.getColumn("jizurl").setEditor(null);
			egu.getColumn("jizurl").setUpdate(false);
			
			egu.getColumn("shebzt").setHeader("设备状态");
			egu.getColumn("tingjyxdl").setHeader("停机影响电量");
			
			egu.getColumn("kaisrq").setHeader("开始日期");
			egu.getColumn("kaissj").setHeader("开始时间");
			egu.getColumn("kaissj").setDefaultValue("0");
			egu.getColumn("jiesrq").setHeader("结束日期");
			egu.getColumn("jiessj").setHeader("结束时间");
			egu.getColumn("jiessj").setDefaultValue("0");

			egu.getColumn("shuom").setHeader("说明");
			
//			egu.getColumn("diancxxb_id").setWidth(200);
//			egu.getColumn("shuom").setWidth(300);
			
			egu.getColumn("id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
			egu.getColumn("jizb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
			egu.getColumn("diancxxb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
			egu.getColumn("jizbh").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
			egu.getColumn("jizurl").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
			egu.setGridType(ExtGridUtil.Gridstyle_Edit);
			
			egu.addPaging(25);
			egu.getColumn("kaisrq").setDefaultValue(this.getRiqi());
			//单位名称下拉框
			//电厂下拉框
			int treejib2 = this.getDiancTreeJib();
			if (treejib2 == 1) {// 选集团时刷新出所有的电厂
				egu.getColumn("diancxxb_id").setEditor(new ComboBox());
				egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
				egu.getColumn("diancxxb_id").setReturnId(true);
			} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
				egu.getColumn("diancxxb_id").setEditor(new ComboBox());
				egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
						new IDropDownModel("select id,mingc from diancxxb where fuid="+getTreeid()+" order by mingc"));
				egu.getColumn("diancxxb_id").setReturnId(true);
			} else if (treejib == 3) {// 选电厂只刷新出该电厂
				egu.getColumn("diancxxb_id").setEditor(new ComboBox());
				egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
						new IDropDownModel("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc"));
				ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc");			
				String mingc="";
				if(r.next()){
					mingc=r.getString("mingc");
				}
				egu.getColumn("diancxxb_id").setHidden(true);
				egu.getColumn("diancxxb_id").setDefaultValue(mingc);
			}	
//			---------------页面js的计算开始------------------------------------------
			StringBuffer sb = new StringBuffer();
			sb.append("gridDiv_grid.on('beforeedit',function(e){");
			sb.append(" if(e.field=='DIANCXXB_ID'){ e.cancel=true;}");//电厂列不允许编辑
			sb.append("});");
			egu.addOtherScript(sb.toString());
			
//			List l = new ArrayList();
//			l.add(new IDropDownBean(0, "运行"));
//			l.add(new IDropDownBean(1, "备用"));
//			l.add(new IDropDownBean(2, "检修"));
//			l.add(new IDropDownBean(3, "缺煤停机"));
			egu.getColumn("shebzt").setEditor(new ComboBox());
			String cmcSql = "select id,mingc from jizyxztb";
			egu.getColumn("shebzt").setComboEditor(egu.gridId,
					new IDropDownModel(cmcSql));
			egu.getColumn("shebzt").returnId=false;
			egu.getColumn("shebzt").setDefaultValue("运行");
//			------------------------------------------------


			
			List l1 = new ArrayList();
			l1.add(new IDropDownBean(0, "0"));
			l1.add(new IDropDownBean(1, "1"));
			l1.add(new IDropDownBean(2, "2"));
			l1.add(new IDropDownBean(3, "3"));
			l1.add(new IDropDownBean(4, "4"));
			l1.add(new IDropDownBean(5, "5"));
			l1.add(new IDropDownBean(6, "6"));
			l1.add(new IDropDownBean(7, "7"));
			l1.add(new IDropDownBean(8, "8"));
			l1.add(new IDropDownBean(9, "9"));
			l1.add(new IDropDownBean(10, "10"));
			l1.add(new IDropDownBean(11, "11"));
			l1.add(new IDropDownBean(12, "12"));
			l1.add(new IDropDownBean(13, "13"));
			l1.add(new IDropDownBean(14, "14"));
			l1.add(new IDropDownBean(15, "15"));
			l1.add(new IDropDownBean(16, "16"));
			l1.add(new IDropDownBean(17, "17"));
			l1.add(new IDropDownBean(18, "18"));
			l1.add(new IDropDownBean(19, "19"));
			l1.add(new IDropDownBean(20, "20"));
			l1.add(new IDropDownBean(21, "21"));
			l1.add(new IDropDownBean(22, "22"));
			l1.add(new IDropDownBean(23, "23"));
			egu.getColumn("kaissj").setEditor(new ComboBox());
			egu.getColumn("kaissj").setComboEditor(egu.gridId, new IDropDownModel(l1));
			
			List l2 = new ArrayList();
			l2.add(new IDropDownBean(0, "0"));
			l2.add(new IDropDownBean(1, "1"));
			l2.add(new IDropDownBean(2, "2"));
			l2.add(new IDropDownBean(3, "3"));
			l2.add(new IDropDownBean(4, "4"));
			l2.add(new IDropDownBean(5, "5"));
			l2.add(new IDropDownBean(6, "6"));
			l2.add(new IDropDownBean(7, "7"));
			l2.add(new IDropDownBean(8, "8"));
			l2.add(new IDropDownBean(9, "9"));
			l2.add(new IDropDownBean(10, "10"));
			l2.add(new IDropDownBean(11, "11"));
			l2.add(new IDropDownBean(12, "12"));
			l2.add(new IDropDownBean(13, "13"));
			l2.add(new IDropDownBean(14, "14"));
			l2.add(new IDropDownBean(15, "15"));
			l2.add(new IDropDownBean(16, "16"));
			l2.add(new IDropDownBean(17, "17"));
			l2.add(new IDropDownBean(18, "18"));
			l2.add(new IDropDownBean(19, "19"));
			l2.add(new IDropDownBean(20, "20"));
			l2.add(new IDropDownBean(21, "21"));
			l2.add(new IDropDownBean(22, "22"));
			l2.add(new IDropDownBean(23, "23"));
			egu.getColumn("jiessj").setEditor(new ComboBox());
			egu.getColumn("jiessj").setComboEditor(egu.gridId, new IDropDownModel(l2));
//			 工具栏
			egu.addTbarText("日期:");
			DateField df = new DateField();
			df.setValue(this.getRiqi());
			df.Binding("RIQI","forms[0]");// 与html页中的id绑定,并自动刷新
			egu.addToolbarItem(df.getScript());
			egu.addTbarText("-");
			
//			egu.addTbarText("开始时间:");
//			ComboBox comb1=new ComboBox();
//			comb1.setTransform("Kaissj");
//			comb1.setId("Kaissj");//和自动刷新绑定
//			comb1.setLazyRender(true);//动态绑定
//			comb1.setWidth(150);
//			egu.addToolbarItem(comb1.getScript());
//			
//			
//			egu.addTbarText("结束时间:");
//			ComboBox comb2=new ComboBox();
//			comb2.setTransform("Jiessj");
//			comb2.setId("Jiessj");//和自动刷新绑定
//			comb2.setLazyRender(true);//动态绑定
//			comb2.setWidth(150);
//			egu.addToolbarItem(comb2.getScript());
//			// 电厂树
			egu.addTbarText("单位名称:");
			ExtTreeUtil etu = new ExtTreeUtil("diancTree",
					ExtTreeUtil.treeWindowType_Dianc,
					((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
			setTree(etu);
			egu.addTbarTreeBtn("diancTree");

			egu.addTbarText("-");
			egu.addToolbarButton(GridButton.ButtonType_SaveAll, "SaveButton");
			
//			String s = " var url = 'http://'+document.location.host+document.location.pathname;"
//					+ "var end = url.indexOf(';');"
//					+ "url = url.substring(0,end);"
//					+ "url = url + '?service=page/' + 'Jizyxqkreport&lx=rezc';"
//					+ " window.open(url,'newWin');";
//			egu.addToolbarItem("{"
//					+ new GridButton("打印", "function (){" + s + "}").getScript()
//					+ "}");
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
			return getExtGrid().getGridScript();
		}

		public String getGridHtml() {
			return getExtGrid().getHtml();
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

		public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
			Visit visit = (Visit) getPage().getVisit();
			if (!visit.getActivePageName().toString().equals(
					this.getPageName().toString())) {
				// 在此添加，在页面第一次加载时需要置为空的变量或方法
				visit.setActivePageName(getPageName().toString());
				visit.setList1(null);
				this.setRiqi(null);
				setTreeid(null);
			}
			getSelectData();
		}

	
		boolean treechange = false;

		private String treeid = "";

		public String getTreeid() {

			if (treeid==null||treeid.equals("")) {

				treeid = String.valueOf(((Visit) getPage().getVisit())
						.getDiancxxb_id());
			}
			return treeid;
		}

		public void setTreeid(String treeid) {

			if (!this.treeid.equals(treeid)) {

				((Visit) getPage().getVisit()).setboolean3(true);
				this.treeid = treeid;
			}
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

		// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
		public int getDiancTreeJib() {
			JDBCcon con = new JDBCcon();
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
		
		
//		public boolean _KaissjChange = false;
//
//		private IDropDownBean _KaissjValue;
//
//		public IDropDownBean getKaissjValue() {
//			if (_KaissjValue == null) {
//				_KaissjValue = (IDropDownBean) getIKaissjModels().getOption(0);
//			}
//			return _KaissjValue;
//		}
//
//		public void setKaissjValue(IDropDownBean Value) {
//			long id = -2;
//			if (_KaissjValue != null) {
//				id = _KaissjValue.getId();
//			}
//			if (Value != null) {
//				if (Value.getId() != id) {
//					_KaissjChange = true;
//				} else {
//					_KaissjChange = false;
//				}
//			}
//			_KaissjValue = Value;
//		}
//
//		private IPropertySelectionModel _IKaissjModel;
//
//		public void setIKaissjModel(IPropertySelectionModel value) {
//			_IKaissjModel = value;
//		}
//
//		public IPropertySelectionModel getIKaissjModel() {
//			if (_IKaissjModel == null) {
//				getIKaissjModels();
//			}
//			return _IKaissjModel;
//		}
//
//		public IPropertySelectionModel getIKaissjModels() {
//			JDBCcon con = new JDBCcon();
//			try {
//
//				List l = new ArrayList();
//				l.add(new IDropDownBean(0, "0"));
//				l.add(new IDropDownBean(1, "1"));
//				l.add(new IDropDownBean(2, "2"));
//				l.add(new IDropDownBean(3, "3"));
//				l.add(new IDropDownBean(4, "4"));
//				l.add(new IDropDownBean(5, "5"));
//				l.add(new IDropDownBean(6, "6"));
//				l.add(new IDropDownBean(7, "7"));
//				l.add(new IDropDownBean(8, "8"));
//				l.add(new IDropDownBean(9, "9"));
//				l.add(new IDropDownBean(10, "10"));
//				l.add(new IDropDownBean(11, "11"));
//				l.add(new IDropDownBean(12, "12"));
//				l.add(new IDropDownBean(13, "13"));
//				l.add(new IDropDownBean(14, "14"));
//				l.add(new IDropDownBean(15, "15"));
//				l.add(new IDropDownBean(16, "16"));
//				l.add(new IDropDownBean(17, "17"));
//				l.add(new IDropDownBean(18, "18"));
//				l.add(new IDropDownBean(19, "19"));
//				l.add(new IDropDownBean(20, "20"));
//				l.add(new IDropDownBean(21, "21"));
//				l.add(new IDropDownBean(22, "22"));
//				l.add(new IDropDownBean(23, "23"));
//				_IKaissjModel = new IDropDownModel(l);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				con.Close();
//			}
//			return _IKaissjModel;
//		}
//		
//		
//		public boolean _JiessjChange = false;
//
//		private IDropDownBean _JiessjValue;
//
//		public IDropDownBean getJiessjValue() {
//			if (_JiessjValue == null) {
//				_JiessjValue = (IDropDownBean) getIJiessjModels().getOption(0);
//			}
//			return _JiessjValue;
//		}
//
//		public void setJiessjValue(IDropDownBean Value) {
//			long id = -2;
//			if (_JiessjValue != null) {
//				id = _JiessjValue.getId();
//			}
//			if (Value != null) {
//				if (Value.getId() != id) {
//					_JiessjChange = true;
//				} else {
//					_JiessjChange = false;
//				}
//			}
//			_JiessjValue = Value;
//		}
//
//		private IPropertySelectionModel _IJiessjModel;
//
//		public void setIJiessjModel(IPropertySelectionModel value) {
//			_IJiessjModel = value;
//		}
//
//		public IPropertySelectionModel getIJiessjModel() {
//			if (_IJiessjModel == null) {
//				getIJiessjModels();
//			}
//			return _IJiessjModel;
//		}
//
//		public IPropertySelectionModel getIJiessjModels() {
//			JDBCcon con = new JDBCcon();
//			try {
//
//				List l = new ArrayList();
//				l.add(new IDropDownBean(0, "0"));
//				l.add(new IDropDownBean(1, "1"));
//				l.add(new IDropDownBean(2, "2"));
//				l.add(new IDropDownBean(3, "3"));
//				l.add(new IDropDownBean(4, "4"));
//				l.add(new IDropDownBean(5, "5"));
//				l.add(new IDropDownBean(6, "6"));
//				l.add(new IDropDownBean(7, "7"));
//				l.add(new IDropDownBean(8, "8"));
//				l.add(new IDropDownBean(9, "9"));
//				l.add(new IDropDownBean(10, "10"));
//				l.add(new IDropDownBean(11, "11"));
//				l.add(new IDropDownBean(12, "12"));
//				l.add(new IDropDownBean(13, "13"));
//				l.add(new IDropDownBean(14, "14"));
//				l.add(new IDropDownBean(15, "15"));
//				l.add(new IDropDownBean(16, "16"));
//				l.add(new IDropDownBean(17, "17"));
//				l.add(new IDropDownBean(18, "18"));
//				l.add(new IDropDownBean(19, "19"));
//				l.add(new IDropDownBean(20, "20"));
//				l.add(new IDropDownBean(21, "21"));
//				l.add(new IDropDownBean(22, "22"));
//				l.add(new IDropDownBean(23, "23"));
//				_IJiessjModel = new IDropDownModel(l);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				con.Close();
//			}
//			return _IJiessjModel;
//		}

	}