package com.zhiren.pub.kehxx;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Kehxxwh extends BasePage implements PageValidateListener {

	private static int _editTableRow = -1;// 编辑框中选中的行

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}

	private String _msg;

	protected void initialize() {
		_msg = "";
	}

	public void setMsg(String _value) {
		_msg = _value;
	}
//	关键字
//	private String GuanjzTf;
	public void setGuanjzTf(String GuanjzTf){
//		this.GuanjzTf=GuanjzTf;
		((Visit)this.getPage().getVisit()).setString4(GuanjzTf);
	}
	public String getGuanjzTf(){
		return ((Visit)this.getPage().getVisit()).getString4();
	}
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return MainGlobal.getExtMessageBox(_msg, false);
	}
	private String Change ;
	public String getChange() {
		
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
    
	private String _mingc;
	
	public void setMingc(String mingc){
		_mingc = mingc;
	}
	
	public String getMingc(){
		return _mingc;
	}
	
	private String _bianm;
	
	public void setBianm(String bianm){
		_bianm = bianm;
	}
	
	public String getBianm(){
		return _bianm;
	}
	
	
	private void Insert(IRequestCycle cycle) {
		Visit visit = ((Visit) getPage().getVisit());
		String lianpID = "-1";
		visit.setboolean1(false);
		visit.setboolean2(false);
		cycle.getRequestContext().getRequest().setAttribute("id", lianpID);
		visit.setString1(lianpID);
		cycle.activate("GongysChakext");
	}

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
//		List _list = ((Visit) getPage().getVisit()).getList1();
		// ((Gongysbean) _list.get(i)).getXXX();
		getSelectData();
		// setMsg();
	}
	
	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

//	private boolean _firstpagebutton = false;
//
//	private boolean _uppagebutton = false;
//
//	private boolean _downpagebutton = false;
//
//	private boolean _lastpagebutton = false;
//
//	private boolean _gopagebutton = false;

//	public void FirstPageButton(IRequestCycle cycle) {
//		_firstpagebutton = true;
//	}
//
//	public void UpPageButton(IRequestCycle cycle) {
//		_uppagebutton = true;
//	}
//
//	public void DownPageButton(IRequestCycle cycle) {
//		_downpagebutton = true;
//	}
//
//	public void LastPageButton(IRequestCycle cycle) {
//		_lastpagebutton = true;
//	}
//
//	public void GoPageButton(IRequestCycle cycle) {
//		_gopagebutton = true;
//	}
	private boolean _savebutton = false;
	public void SaveButton(IRequestCycle cycle){
		_savebutton=true;
	}
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
		if(_savebutton){
			_savebutton=false;
			Save();
		}
//		if (_firstpagebutton) {
//			ToFirstPage();
//			_firstpagebutton = false;
//		}
//		if (_uppagebutton) {
//			ToUpPage();
//			_uppagebutton = false;
//		}
//		if (_downpagebutton) {
//			ToDownPage();
//			_downpagebutton = false;
//		}
//		if (_lastpagebutton) {
//			ToLastPage();
//			_lastpagebutton = false;
//		}
//
//		if (_gopagebutton) {
//			GoPage();
//			_gopagebutton = false;
//		}
		if (_InsertChick) {
			_InsertChick = false;
			Insert(cycle);
		}
	}
public void Save(){
	Visit visit=(Visit)this.getPage().getVisit();
	//删除
	ResultSetList delrsl=this.getExtGrid().getDeleteResultSet(this.getChange());
	    JDBCcon con=new JDBCcon();
	    while(delrsl.next()){
	    	con.getDelete(" delete from diancxxb where id="+delrsl.getLong("ID"));
	    }
	    con.Close();
	//保存
	ResultSetList rsl=
		this.getExtGrid().getModifyResultSet(this.getChange());
	  JDBCcon conbaoc=new JDBCcon();
	  JDBCcon conupdate=new JDBCcon();
	while(rsl.next()){
		
		String id=MainGlobal.getNewID(visit.getDiancxxb_id());
		int xuh=this.getnextxuh();
		String bianm=this.getnextBianm();
		String mingc=rsl.getString("mingc");
		String quanc=rsl.getString("quanc");
		String piny=rsl.getString("piny");
		long shengfb_id=MainGlobal.getProperId(this.getExtGrid().getColumn("shengfb_id").combo,rsl.getString("shengfb_id"));
		long fuid=199;
		String diz=rsl.getString("diz");
		String jib="3";
		long cangkb_id=1;
		long neibxs=MainGlobal.getProperId(this.getExtGrid().getColumn("neibxs").combo,rsl.getString("neibxs"));
		if(rsl.getLong("id")==0){
	    String sqltt="insert into diancxxb\n"+
	    "(id, xuh, bianm, mingc, quanc, piny, shengfb_id, fuid, diz,  jib, cangkb_id, neibxs)\n"+
	    "values("+id+","+xuh+",'"+bianm+"','"+mingc+"','"+quanc+"','"+piny+"',"+shengfb_id+","+fuid+",'"+diz+"',"+jib+","+cangkb_id+","+neibxs+")\n";
	   if(conbaoc.getInsert(sqltt)<0){
		   this.setMsg("保存失败");
		   return ;
	   };
		}else{
			String sqlupdate="update diancxxb\n"+
         "set \n"+
          "mingc = '"+mingc+"',\n"+
          "quanc = '"+quanc+"',\n"+
          "piny = '"+piny+"',\n"+
          "shengfb_id = "+shengfb_id+",\n"+
          "diz = '"+diz+"',\n"+
          "neibxs = "+neibxs+"\n"+
          "where id = "+rsl.getLong("id");
			if(conupdate.getUpdate(sqlupdate)<0){
				 this.setMsg("保存失败");
				   return ;
			};
		}
	}
	conbaoc.Close();
	conupdate.Close();
	this.setMsg("操作成功");
}
public int getnextxuh(){
	JDBCcon con=new JDBCcon();
     ResultSetList rsl=	con.getResultSetList("select max(xuh)+1 maxxuh from diancxxb ");
      if(rsl.next()){
         return  rsl.getInt("maxxuh");
      }
	return 1;
}
public String getnextBianm(){
	   JDBCcon con=new JDBCcon();
	  ResultSetList rsl=con.getResultSetList("select max(to_number(substr(bianm,4))) maxbianm from diancxxb where bianm like '199%'");
	     int jisbianm=0;
	    if(rsl.next()){
	    	jisbianm= Integer.parseInt(rsl.getString("maxbianm"))+1; 
;
	    }else{
	    	jisbianm=001;
	    }
	    if(jisbianm>99){
	return "199"+jisbianm;
	    }else{
	    	return "1990"+jisbianm;
	    }
}
/*	private Gongysbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Gongysbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Gongysbean EditValue) {
		_EditValue = EditValue;
	}*/
	
//	public void chakSelectAction(IRequestCycle cycle) {
//		Visit visit = ((Visit) getPage().getVisit());
////		List _list = ((Visit) getPage().getVisit()).getList1();
//		Object obj[] = cycle.getServiceParameters();
//		String lianpID = "-1";
//		if (obj.length > 0) {
//			lianpID = (String)obj[0];
//		}
////		int introw = getEditTableRow();
//		boolean chak = true;
//		visit.setboolean1(chak);
//		visit.setboolean2(true);
////		long fuid = ((Gongysbean) _list.get(introw)).getFuid();
//		cycle.getRequestContext().getRequest().setAttribute("id", lianpID);
//		String id = (String) cycle.getRequestContext().getRequest().getAttribute("id");
////		visit.setLong2(fuid);
//		visit.setString1(id);
//		cycle.activate("GongysChakext");
//	}
//	
//	
//	public void xiugSelectAction(IRequestCycle cycle) {
//		Visit visit = ((Visit) getPage().getVisit());
//		Object obj[] = cycle.getServiceParameters();
//		String lianpID = "-1";
//		if (obj.length > 0) {
//			lianpID = (String) obj[0];
//		}
//		boolean xiug = false;
//		visit.setboolean2(true);
//		visit.setboolean1(xiug);
//		cycle.getRequestContext().getRequest().setAttribute("id", lianpID);
//		String id = (String) cycle.getRequestContext().getRequest().getAttribute("id");
//		visit.setString1(id);
//		cycle.activate("GongysChakext");
//	}

//	public void getSelectData(int leix) {
////		Visit visit = (Visit) getPage().getVisit();
//		List _editvalues = new ArrayList();
//		JDBCcon JDBCcon = new JDBCcon();
//		StringBuffer sql = new StringBuffer("");
//		int rowOfPage = this.getRowsOfPage();
//		StringBuffer SQL = new StringBuffer("");
//		boolean hasData = false;
//		try {
//			String Strshengf = "";
//			String Strgongslb = "";
//			String Strmingc = "";
//			String Strbianm = "";
//			if (leix == 0) {// 第一次加载
//				Strshengf = "";
//				Strgongslb = "";
//			} else {
//				if (getShengfValue().getId() == -1) {
//					Strshengf = "";
//				} else {
//					Strshengf = "and shengfb.id =" + getShengfValue().getId();
//				}
//				if (getGongyslbValue().getId() == -1) {
//					Strgongslb = "";
//				} else {
//					if (getGongyslbValue().getId() == 0) {
//						Strgongslb = " and fuid = 0";
//					} else {
//						Strgongslb = " and fuid != 0";
//					}
//				}
//				if(getMingc().equals("")||getMingc().equals(null)){
//					Strmingc = "";
//				}else{
//					Strmingc = " and mingc like '%"+getMingc()+"%'";
//				}
//				if(getBianm().equals("")||getBianm().equals(null)){
//					Strbianm = "";
//				}else{
//					Strbianm = " and bianm like '%"+getBianm()+"%'";
//				}
//			}
//			sql.append("select ceil(rownum/" + rowOfPage
//					+ ") as page,gongysb.id as id,gongysb.xuh as xuh,");
//			sql.append(" gongysb.bianm as bianm," + " gongysb.quanc as quanc,");
//			sql
//					.append(" gongysb.mingc as mingc,"
//							+ " shengfb.quanc as shengf,");
//			sql.append("fuid as fuid,");
//			sql.append(" decode(fuid, 0, '大供应商', '小供应商') as leib");
//			sql.append(" from gongysb, shengfb");
//			sql.append(" where shengfb.id = gongysb.shengfb_id " + Strshengf
//					+ " " + Strgongslb + " "+Strmingc+" "+Strbianm+"");
//			sql.append(" order by gongysb.xuh, gongysb.mingc");
//			setSQL(sql);
//			ResultSet rs = JDBCcon.getResultSet(sql);
//			while (rs.next()) {
//				int mpage = rs.getInt("PAGE");
//				setPages(mpage);
//				hasData = true;
//			}
//			this.setCurrentPage(0);
//			this.setTotalPages(0);
//			this.setGoPage(0);
//			if (hasData) {
//				this.setCurrentPage(1);
//				this.setGoPage(1);
//				SQL.delete(0, SQL.length());
//				SQL.append("select count(*) as pages from (");
//				SQL.append(sql);
//				SQL.append(") t");
//				CountPages(SQL);
//				this.setEditValues(_editvalues);
//				ToFirstPage();
//			} else {
//				this.setEditValues(_editvalues);
//				this.setCurrentPage(1);
//				this.setGoPage(1);
//				this.setTotalPages(1);
//				ToFirstPage();
//			}
//			rs.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			JDBCcon.Close();
//		}
//		setEditTableRow(-1);
//	}
	
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
	
	public String getcontext() {
        return this.getRequestCycle().getRequestContext().getRequest()
                .getScheme()
                + "://"
                + this.getRequestCycle().getRequestContext().getServerName()
                + ":"
                + this.getRequestCycle().getRequestContext().getServerPort()
                + this.getEngine().getContextPath();
    }

	public void getSelectData() {
		Visit visit = ((Visit) getPage().getVisit());
		JDBCcon con = new JDBCcon();
		String context = MainGlobal.getHomeContext(this);
		String tiaoj="";
		String paixu="";
		if (getGuanjzTf() != "" && getGuanjzTf() != null) {
			if (this.getWeizSelectValue().getStrId().equals("1")) {
				tiaoj = " and d.quanc like '%" + getGuanjzTf() + "%'\n";
			} else {
				tiaoj = " and d.bianm like '%" + getGuanjzTf() + "%'\n";
			}
			paixu="order by d.bianm";
		}else{
			paixu=" order by  d.quanc";
		}
		
		String sql="select d.id,d.xuh,d.bianm,d.mingc,d.quanc,d.piny,s.quanc shengfb_id ,d.diz,decode(d.neibxs,1,'外部',0,'内部') neibxs\n"+ 
                  " from diancxxb d, shengfb s\n"+
                  "where s.id=d.shengfb_id and fuid=199 and jib=3 and cangkb_id = 1 "+tiaoj+" "+paixu;
		ResultSetList rsl = con.getResultSetList(sql);
		/*ResultSetList rsl = con
				.getResultSetList("select gongysb.id as id,\n" +
						"       gongysb.xuh as xuh,\n" + 
						"       gongysb.bianm as bianm,\n" + 
						"       gongysb.quanc as quanc,\n" + 
						"       gongysb.mingc as mingc,\n" + 
						"       shengfb.quanc as shengf,\n" + 
						"       fuid as fuid,\n" + 
						"       decode(fuid, 0, '大供应商', '小供应商') as leib,\n" + 
						"       decode(1,1,'查看') as chak,\n" + 
						"       decode(1,1,'修改') as xiug\n" + 
						"  from gongysb, shengfb\n" + 
						" where shengfb.id = gongysb.shengfb_id\n" + tiaoj+
						paixu);*/
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// egu.setTitle("车站信息");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setHeight("570");
		egu.setTableName("diancxxb");
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("mingc").setHeader("名称");
		egu.getColumn("bianm").setHeader("编码");
		egu.getColumn("quanc").setHeader("全称");
		egu.getColumn("shengfb_id").setHeader("省份");
		egu.getColumn("piny").setHeader("拼音");
		egu.getColumn("diz").setHeader("地址");
		egu.getColumn("neibxs").setHeader("销售类别");
		/*egu.getColumn("chak").setRenderer(
						"function(value,p,record){return String.format('<a href="+context+"/app?service=page/{1}&id={2}&zhuangt=1>{0}</a>',value,'GongysChakext',record.data['ID']);}");
		egu
		.getColumn("xiug")
		.setRenderer(
				"function(value,p,record){return String.format('<a href="+context+"/app?service=page/{1}&id={2}&zhuangt=2>{0}</a>',value,'GongysChakext',record.data['ID']);}");
*///		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.getColumn("quanc").setWidth(230);
		egu.getColumn("bianm").editor.allowBlank=true;
		egu.getColumn("bianm").setEditor(null);
		
		egu.getColumn("xuh").editor.allowBlank=true;
		egu.getColumn("xuh").setEditor(null);
		ComboBox shengf=new ComboBox();
		shengf.setEditable(true);
		egu.getColumn("shengfb_id").setEditor(shengf);
	
		egu.getColumn("shengfb_id").setComboEditor(egu.gridId, new IDropDownModel("select id,quanc from shengfb"));
		egu.getColumn("shengfb_id").setReturnId(true);
		
		ComboBox neibxs= new ComboBox();
		egu.getColumn("neibxs").setEditor(neibxs);
		neibxs.setEditable(true);
		List neibxs_list = new ArrayList();
		neibxs_list.add(new IDropDownBean(1, "外部"));
		neibxs_list.add(new IDropDownBean(0, "内部"));
		egu.getColumn("neibxs").setComboEditor(egu.gridId, new IDropDownModel(neibxs_list));
		egu.getColumn("neibxs").setReturnId(true);
		visit.setboolean2(true);
		egu.addPaging(22);
		// egu.getColumn("lujxxb_id").setEditor(new ComboBox());
		// egu.getColumn("lujxxb_id").setComboEditor(egu.gridId, new
		// IDropDownModel("select id,mingc from lujxxb"));
		// List l = new ArrayList();
		// l.add(new IDropDownBean(0,"车站"));
		// l.add(new IDropDownBean(1,"港口"));
		// egu.getColumn("leib").setEditor(new ComboBox());
		// egu.getColumn("leib").setComboEditor(egu.gridId, new
		// IDropDownModel(l));
		// egu.getColumn("leib").setReturnId(false);
		// egu.getColumn("leib").setDefaultValue("车站");
//		egu.addButton(GridButton.ButtonType_Insert, null);
//		egu.addToolbarItem("{"+new GridButton("添加","function(){document.getElementById('InsertButton').click();}").getScript()+"}");
		
		/*String str=
       		" var url = 'http://'+document.location.host+document.location.pathname;"+
            "var end = url.indexOf(';');"+
			"url = url.substring(0,end);"+
       	    "url = url + '?service=page/' + 'GongysReport';" +
       	    " window.open(url,'newWin');";
		String str1=
			"if(gridDiv_sm.getSelected()==null){alert('请选择供应商！');return;}" +
       		" var url = 'http://'+document.location.host+document.location.pathname;"+
            "var end = url.indexOf(';');"+
			"url = url.substring(0,end);"+
       	    "url = url + '?service=page/' + 'GongysReport&gongysb_id='+gridDiv_sm.getSelected().get('ID');" +
       	    " window.open(url,'newWin');";
		
		egu.addToolbarItem("{"+new GridButton("打印供应商列表","function (){"+str+"}").getScript()+"}");
		egu.addToolbarItem("{"+new GridButton("打印供应商明细","function (){"+str1+"}").getScript()+"}");*/
		
		egu.addTbarText("输入关键字：");
		
		 ComboBox chaxunco=new ComboBox();
		 chaxunco.setTransform("WeizSelectx");
		 chaxunco.setWidth(100);
		 chaxunco.setLazyRender(true);
		 chaxunco.setId("chaxunco");
		 egu.addToolbarItem(chaxunco.getScript());
		 TextField theKey=new TextField();
		 theKey.setId("theKey");
		 theKey.setValue(this.getGuanjzTf());
		 theKey.setListeners("change:function(thi,newva,oldva){ document.getElementById('GuanjzTf').value=newva;}\n");
		 egu.addToolbarItem(theKey.getScript());
		GridButton chazhao=new GridButton("查找","function(){\n"+
	               "var mesbox="+MainGlobal.getExtMessageShow("查找中..","请稍等", 200)+
                "       document.getElementById('RefurbishButton').click();\n"+
                "      }\n");
              chazhao.setIcon(SysConstant.Btn_Icon_Search);
		      egu.addTbarBtn(chazhao);
//		egu.addButton(GridButton.ButtonType_Delete, null);
//		egu.addButton(GridButton.ButtonType_Save, "SaveButton");
		      egu.addTbarText("-");
		      egu.addToolbarButton(GridButton.ButtonType_Insert,"");
				egu.addToolbarButton(GridButton.ButtonType_Delete, "");
				egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		setExtGrid(egu);
		con.Close();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			if(!visit.getActivePageName().toString().equals("GongysChakext")){
				this.setGuanjzTf("");
				setWeizSelectValue(null);
				setWeizSelectModel(null);
				
			}
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
//			visit.setProSelectionModel1(null);
			visit.setProSelectionModel2(null);
//			visit.setDropDownBean1(null);
			visit.setDropDownBean2(null);
			visit.setList1(null);
		
			setMingc(null);
			setBianm(null);
//			getChaxunModels();
			getGongyslbModels();
//			getSelectData();
		

		}
		
		getSelectData();
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

//	private String getProperValue(IPropertySelectionModel _selectModel,
//			int value) {
//		int OprionCount;
//		OprionCount = _selectModel.getOptionCount();
//		for (int i = 0; i < OprionCount; i++) {
//			if (((IDropDownBean) _selectModel.getOption(i)).getId() == value) {
//				return ((IDropDownBean) _selectModel.getOption(i)).getValue();
//			}
//		}
//		return null;
//	}
//
//	private long getProperId(IPropertySelectionModel _selectModel, String value) {
//		int OprionCount;
//		OprionCount = _selectModel.getOptionCount();
//
//		for (int i = 0; i < OprionCount; i++) {
//			if (((IDropDownBean) _selectModel.getOption(i)).getValue().equals(
//					value)) {
//				return ((IDropDownBean) _selectModel.getOption(i)).getId();
//			}
//		}
//		return -1;
//	}

//	 查找方式下拉菜单
	public IDropDownBean getWeizSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean9() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean9((IDropDownBean) getWeizSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean9();
	}

	public void setWeizSelectValue(IDropDownBean Value) {
		if (Value != ((Visit) getPage().getVisit()).getDropDownBean9()) {

			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean9(Value);
		}
	}

	public void setWeizSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel9(value);
	}

	public IPropertySelectionModel getWeizSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel9() == null) {
			getWeizSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}

	public void getWeizSelectModels() {
		List list = new ArrayList();
		Visit v=(Visit)this.getPage().getVisit();	
		
	
		list.add(new IDropDownBean(1, "按全称查找"));
		
		list.add(new IDropDownBean(2, "按编码查找"));
		((Visit) getPage().getVisit())
				.setProSelectionModel9(new IDropDownModel(list));
	}
	
	
	
	public IDropDownBean getGongyslbValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getGongyslbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setGongyslbValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getGongyslbModel()
							.getOption(0));
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getGongyslbModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getGongyslbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setGongyslbModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public void getGongyslbModels() {
		String sql = "select lb.*\n"
				+ "  from (select 0 as id, decode(1, 1, '大供应商') as mingc\n"
				+ "          from dual\n"
				+ "        union\n"
				+ "        select 1 as id, decode(1, 1, '小供应商') as mingc from dual) lb";
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, "全部"));
	}

	private int m_pages = 0;

	public int getPages() {
		return m_pages;
	}

	public void setPages(int pages) {
		m_pages = pages;
	}

//	private ResultSet getQueryResult(StringBuffer SQL) {
//		ResultSet rs = null;
//		JDBCcon con = new JDBCcon();
//		rs = con.getResultSet(SQL);
//		return rs;
//	}
//
//	private int getRowsOfPage() {
//		int rowsOfpage = 19;
//		return rowsOfpage;
//	}

	private int m_totalpages = 0;

	public int getTotalPages() {
		return m_totalpages;
	}

	public void setTotalPages(int _value) {
		m_totalpages = _value;
	}

	private int m_currentpage = 0;

	public int getCurrentPage() {
		return m_currentpage;
	}

	public void setCurrentPage(int _value) {
		m_currentpage = _value;
	}

	private int m_page = 1;

	public int getGoPage() {
		return m_page;
	}

	public void setGoPage(int page) {
		m_page = page;
	}

	private StringBuffer m_sql = new StringBuffer("");

	public StringBuffer getSQL() {
		return m_sql;
	}

	public void setSQL(StringBuffer sql) {
		m_sql = sql;
	}



	
}