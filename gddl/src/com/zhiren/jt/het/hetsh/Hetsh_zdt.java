package com.zhiren.jt.het.hetsh;
	import java.sql.ResultSet;
import java.text.SimpleDateFormat;
	import java.util.ArrayList;
import java.util.Calendar;
	import java.util.Date;
	import java.util.List;

import javax.servlet.http.HttpUtils;
	
	import org.apache.tapestry.IMarkupWriter;
	import org.apache.tapestry.IRequestCycle;
	import org.apache.tapestry.form.IPropertySelectionModel;
	import org.apache.tapestry.html.BasePage;
	
import com.zhiren.common.DateUtil;
	import com.zhiren.common.IDropDownBean;
	import com.zhiren.common.IDropDownModel;
	import com.zhiren.common.JDBCcon;
import com.zhiren.common.Liuc;
import com.zhiren.common.Locale;
	import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.Button;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.jt.het.shenhrz.Yijbean;
import com.zhiren.main.Visit;
import com.zhiren.webservice.InterCom;
	public class Hetsh_zdt extends BasePage {
	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean5()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)+ "';";
		} else {
			return "";
		}
	}
	private String riq1;
	private String riq2;
	
	public String getRiq1() {
		if (riq1 == null|| riq1.equals("")) {
			Calendar stra = Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), 0, 1);
			setRiq1(DateUtil.FormatDate(stra.getTime()));
		}
		return riq1;
	}
	public void setRiq1(String riq1) {
		this.riq1 = riq1;
	}
	public String getRiq2() {
		if (riq2 == null|| riq2.equals("")) {
			Calendar stra = Calendar.getInstance();
			setRiq2(DateUtil.FormatDate(stra.getTime()));
		}
		return riq2;
	}
	public void setRiq2(String riq2) {
		this.riq2 = riq2;
	}
	public boolean isQuanxkz(){
		return((Visit) getPage().getVisit()).getboolean4();
	}
	public void setQuanxkz(boolean value){
		((Visit) getPage().getVisit()).setboolean4(value);
	}
	private int _editTableRow = -1;// 编辑框中选中的行
	public int getEditTableRow() {
		return _editTableRow;
	}
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	public String getXiaox(){
		if(((Visit) getPage().getVisit()).getString1()==null){
			((Visit) getPage().getVisit()).setString1("");
		}
		return ((Visit) getPage().getVisit()).getString1();
	}
	public void setXiaox(String xiaox){
		((Visit) getPage().getVisit()).setString1(xiaox);
	}
	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}
	private boolean TijButton = false;
	public void TijButton(IRequestCycle cycle) {
		TijButton = true;
	}
	private boolean HuitButton = false;
	public void HuitButton(IRequestCycle cycle) {
		HuitButton = true;
	}
	private boolean chakwbButton = false;
	public void chakwbButton(IRequestCycle cycle) {
		chakwbButton = true;
	}
	private boolean ShuaxButton = false;
	public void ShuaxButton(IRequestCycle cycle) {
		ShuaxButton = true;
	}
	public void submit(IRequestCycle cycle) {
		if (TijButton) {
			TijButton = false;
			tij();
		}
		if (HuitButton) {
			HuitButton = false;
			huit();
		}
		if (chakwbButton) {
			chakwbButton = false;
			chakwb();
		}
		if (ShuaxButton) {
			ShuaxButton = false;
			Shuax();
		}
	}
	private void Shuax(){
		getSelectData();
	}
	private Hetshbean _EditValue;
	public List getEditValues() {
		if(((Visit) getPage().getVisit()).getList5()==null){
			((Visit) getPage().getVisit()).setList5(new ArrayList());
		}
		return ((Visit) getPage().getVisit()).getList5();
	}
	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList5(editList);
	}
	public Hetshbean getEditValue() {
		return _EditValue;
	}
	public void setEditValue(Hetshbean EditValue) {
		_EditValue = EditValue;
	}
	public void getSelectData() {
		List list=getEditValues();
		String sql="";
		list.clear();
		JDBCcon con=new JDBCcon();
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),this.getTreeid());
		((Visit) getPage().getVisit()).setExtTree1(etu);
		String zhuangtWhere=" and  (case when hetb.liucztb_id=1 then 1 else 0 end)="+getzhuangtValue().getId();
		if(getzhuangtValue().getId()==-1){
			zhuangtWhere="";
		}
		sql="select hetbh,xufdwmc,gongfdwmc,qiandrq,rel,jiag,hetl,jihkj ,leib,id,diancxxb_id,gongysb_id,hetgysbid,liucztbid,leibid,leibztb_id,fid, fbh,shenhzt,'' chak\n" +
		"from(\n" + 
		"select gethetcxRel(hetb.id)rel,gethetcxJiag(hetb.id)jiag,hetb.hetbh,hetb.xufdwmc,hetb.gongfdwmc,sum(hetslb.hetl)hetl,to_char(hetb.qiandrq,'YYYY-MM-DD')qiandrq,jihkjb.mingc jihkj,leibztb.mingc weiz,decode(hetb.leib,0,'电厂采购',1,'区域销售',2,'区域采购')leib,hetb.id\n" + 
		",hetb.diancxxb_id,hetb.gongysb_id,hetb.hetgysbid,hetb.liucztb_id liucztbid,hetb.leib leibid,decode(leibztb.id,null,hetb.liucztb_id  ,leibztb.id) leibztb_id,b.id fid,b.hetbh fbh\n" + 
		",decode(hetb.liucztb_id,0,'未审核','已审核')shenhzt "+
		"from hetb,hetslb,jihkjb,liucztb,leibztb,hetb b\n" + 
		"where  hetb.diancxxb_id||hetb.fuid=b.id(+)and hetb.liucztb_id=0 and hetb.id=hetslb.hetb_id(+) and hetb.jihkjb_id=jihkjb.id and liucztb.leibztb_id=leibztb.id(+) and hetb.liucztb_id=liucztb.id(+)\n" + zhuangtWhere+
		" group by  hetb.hetbh,hetb.xufdwmc,hetb.gongfdwmc,hetb.qiandrq,jihkjb.mingc,leibztb.mingc,hetb.id,hetb.leib\n" + 
		",hetb.diancxxb_id,hetb.gongysb_id,hetb.hetgysbid,hetb.liucztb_id,hetb.leib,leibztb.id,b.id ,b.hetbh\n" + 
		")a\n" + 
		"where a.qiandrq>='"+this.getRiq1()+"' and a.qiandrq<='"+this.getRiq2()+"' and a.diancxxb_id in (select id\n" + 
		"from(\n" + 
		"select id from diancxxb\n" + 
		"start with (fuid="+getTreeid()+" or shangjgsid="+getTreeid()+")\n" + 
		"connect by fuid=prior id\n" + 
		")\n" + 
		"union\n" + 
		"select id\n" + 
		"from diancxxb\n" + 
		"where id="+getTreeid()+")  order by xufdwmc,gongfdwmc";
 			 ResultSetList rs=con.getResultSetList(sql);
			//查询的sql没有完全写好呢
			 String str=
		       		" var url = 'http://'+document.location.host+document.location.pathname;"+
		            "var end = url.indexOf(';');"+
					"url = url.substring(0,end);"+
		       	    "url = url + '?service=page/' + 'Shenhrz&hetb_id='+record.data['ID'];";
			 String str1=
		       		" var url = 'http://'+document.location.host+document.location.pathname;"+
		            "var end = url.indexOf(';');"+
					"url = url.substring(0,end);"+
		       	    "url = url + '?service=page/' + 'GongysReport&gongysb_id='+record.data['GONGYSB_ID'];";
			 ExtGridUtil egu = new ExtGridUtil("SelectFrmDiv", rs);
//			 egu.setGridType(ExtGridUtil.Gridstyle_Read);
				egu.getColumn("id").setHidden(true);
				egu.getColumn("gongysb_id").setHidden(true);
				egu.getColumn("liucztbid").setHidden(true);
				egu.getColumn("jihkj").setHidden(true);
				egu.getColumn("leib").setHidden(true);
				egu.getColumn("diancxxb_id").setHidden(true);
				egu.getColumn("hetgysbid").setHidden(true);
				egu.getColumn("leibid").setHidden(true);
				egu.getColumn("leibztb_id").setHidden(true);
				egu.getColumn("fid").setHidden(true);
				egu.getColumn("fbh").setHidden(true);
				
				egu.getColumn("hetbh").setHeader("合同编号");
				egu.getColumn("gongfdwmc").setHeader("供方单位名称");
				egu.getColumn("gongfdwmc").setRenderer("function(value,p,record){"+str1+
						"return \"<a href=# onclick=window.open('\"+url+\"','newWin')>\"+value+\"</a>\"}"
				);
				egu.getColumn("xufdwmc").setHeader("需方单位名称");
				egu.getColumn("qiandrq").setHeader("签订日期");
				egu.getColumn("hetl").setHeader("合同量");
				egu.getColumn("rel").setHeader("热量");
				egu.getColumn("jiag").setHeader("价格");
				egu.getColumn("shenhzt").setHeader("状态");
				egu.getColumn("chak").setHeader("");
//				
				
				
				egu.getColumn("chak").setRenderer(
						"function(value,p,record){" +str+
						"return \"<a href=# onclick=window.open('\"+url+\"','newWin')>查看</a>\"}"
				);
				egu.addTbarText("单位:");
				egu.addTbarTreeBtn("diancTree");
				egu.addTbarText("-");
				egu.addTbarText("签订日期:");
				DateField df = new DateField();
				df.setValue(this.getRiq1());
				df.Binding("riq1", "");// 与html页中的id绑定,并自动刷新
				df.setWidth(80);
				egu.addToolbarItem(df.getScript());

				DateField df1 = new DateField();
				df1.setValue(this.getRiq2());
				df1.Binding("riq2", "");// 与html页中的id绑定,并自动刷新
				df1.setWidth(80);
				egu.addToolbarItem(df1.getScript());
				
				egu.addTbarText("-") ;
				egu.addTbarText("状态:");
				ComboBox cbzt = new ComboBox();
				cbzt.setWidth(80);
				cbzt.setTransform("weizSelect");
				egu.addToolbarItem(cbzt.getScript());
				egu.addToolbarItem("{"+new GridButton("刷新","function(){document.getElementById('shuaxButton').click();}").getScript()+"}");
				egu.addTbarText("-");
				egu.addToolbarItem("{"+new GridButton("提交","function(){document.getElementById('tijButton').click();}").getScript()+"}");
				egu.addTbarText("-");
				egu.addToolbarItem("{"+new GridButton("回退","function(){document.getElementById('huitButton').click();}").getScript()+"}");
				egu.addOtherScript("SelectFrmDiv_sm.on('rowselect',function(own,row,record){document.all.item('EditTableRow').value=row;});");
				
			((Visit) this.getPage().getVisit()).setExtGrid1(egu);
	}
	
	/**1,提交
	 * 2,回退
	 */
	private void tij(){
		ExtGridUtil ExtGrid1=((Visit) this.getPage().getVisit()).getExtGrid1();
		if(getEditTableRow()!=-1){
//更新电厂
//			String sqla[]={"update hetxxb set hetxxb.caozlx='确定' where '"+
//					ExtGrid1.griddata[getEditTableRow()][10]+"'||id='"+ExtGrid1.griddata[getEditTableRow()][9]+"'"};
			
			String sqla[]={"update hetxxb set shenhbz=2 where '"+
					ExtGrid1.griddata[getEditTableRow()][10]+"'||id='"+ExtGrid1.griddata[getEditTableRow()][9]+"'"};
			InterCom sender=new InterCom();
			String res[]=sender.sqlExe(ExtGrid1.griddata[getEditTableRow()][10],sqla, true);
//如果厂级提交成功，更新集团
			if(res[0].equals("true")){
				JDBCcon con=new JDBCcon();
				String sql="";
				sql="update hetb\n" + 
				"set liucztb_id=1\n" + 
				"where id="+ExtGrid1.griddata[getEditTableRow()][9];
				con.getUpdate(sql);
				getSelectData();
//如果不成功给出提示				
			}else{
				setMsg("无法更新该电厂合同状态！错误："+res[0]);
			}
			
		}
	}
	private void huit(){
		ExtGridUtil ExtGrid1=((Visit) this.getPage().getVisit()).getExtGrid1();
		if(getEditTableRow()!=-1){
//更新电厂
//			String sqla[]={"update hetxxb set hetxxb.caozlx=null,citjs=null,bentjs=null where '"+
//					ExtGrid1.griddata[getEditTableRow()][10]+"'||id='"+ExtGrid1.griddata[getEditTableRow()][9]+"'"};
			
			String sqla[]={"update hetxxb set shenhbz=0  where '"+
					ExtGrid1.griddata[getEditTableRow()][10]+"'||id='"+ExtGrid1.griddata[getEditTableRow()][9]+"'"};
			InterCom sender=new InterCom();
			String res[]=sender.sqlExe(ExtGrid1.griddata[getEditTableRow()][10],sqla, true);
//如果厂级回退成功，删除集团
			if(res[0].equals("true")){
				JDBCcon con=new JDBCcon();
				String sql="";
				sql="delete hetb\n" + 
				"where id="+ExtGrid1.griddata[getEditTableRow()][9];
				con.getUpdate(sql);
				getSelectData();
//如果不成功给出提示				
			}else{
				setMsg("无法更新该电厂合同状态！错误："+res[0]);
			}
			
		}
	}
	private void chakwb(){
		
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean5(true);
			return;
		} else {
			visit.setboolean5(false);
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			((Visit) getPage().getVisit()).setDropDownBean1(null);
			((Visit) getPage().getVisit()).setProSelectionModel1(null);
			((Visit) getPage().getVisit()).setDropDownBean2(null);
			((Visit) getPage().getVisit()).setProSelectionModel2(null);
			((Visit) getPage().getVisit()).setDropDownBean3(null);
			((Visit) getPage().getVisit()).setProSelectionModel3(null);
			((Visit) getPage().getVisit()).setDropDownBean6(null);
			((Visit) getPage().getVisit()).setProSelectionModel6(null);
			
			setXiaox(null);
			setTreeid("");
//			getSelectData();
			visit.setboolean4(true);
			
		}
//		if(((Visit) getPage().getVisit()).getboolean1()||((Visit) getPage().getVisit()).getboolean2()||((Visit) getPage().getVisit()).getboolean3()){//如果合同位置改变
//			//1, 位置2, 年份3, 单位
//			if(((Visit) getPage().getVisit()).getboolean1()==true){
//				if(getweizSelectValue().getId()==1){
//					visit.setboolean4(true);
//				}else{
//					visit.setboolean4(false);
//				}
//			}
//			getSelectData();
//		}
		getSelectData();
	}
	private String FormatDate(Date _date) {
		if (_date == null) {
			return DateUtil.Formatdate("yyyy-MM-dd", new Date());
		}
		return DateUtil.Formatdate("yyyy-MM-dd", _date);
	}
	private String getProperValue(IPropertySelectionModel _selectModel,
			long value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();
		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getId() == value) {
				return ((IDropDownBean) _selectModel.getOption(i)).getValue();
			}
		}
		return null;
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
	//单位
	public IDropDownBean getdanwSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean1()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean1((IDropDownBean)getdanwSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean1();
    }
 
    public void setdanwSelectValue(IDropDownBean Value) {
    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean1()!=null){
	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean1().getId()){
	    		((Visit) getPage().getVisit()).setboolean3(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean3(false);
	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean1(Value);
    	}
    }
    public void setdanwSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel1(value);
    }

    public IPropertySelectionModel getdanwSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
            getdanwSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel1();
    }

    public void getdanwSelectModels() {
        String sql = 
        	"select id,mingc,jib\n" +
        	"from(\n" + 
        	" select id,mingc,0 as jib\n" + 
        	" from diancxxb\n" + 
        	" where id="+ ((Visit) getPage().getVisit()).getDiancxxb_id()+"\n" + 
        	" union\n" + 
        	" select *\n" + 
        	" from(\n" + 
        	" select id,mingc,level as jib\n" + 
        	"  from diancxxb\n" + 
        	" start with fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"\n" + 
        	" connect by fuid=prior id\n" + 
        	" order SIBLINGS by  xuh)\n" + 
        	" )\n" + 
        	" order by jib";
        List dropdownlist = new ArrayList();
		JDBCcon con = new JDBCcon();
		try {
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String mc = rs.getString("mingc");
				int jib=rs.getInt("jib");
				String nbsp=String.valueOf((char)0xA0);
				for(int i=0;i<jib;i++){
					mc=nbsp+nbsp+nbsp+nbsp+mc;
				}
				dropdownlist.add(new IDropDownBean(id, mc));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
        ((Visit) getPage().getVisit()).setProSelectionModel1(new IDropDownModel(dropdownlist)) ;
        return ;
    }
    //位置
    public IDropDownBean getweizSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean2()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)getweizSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean2();
    }
 
    public void setweizSelectValue(IDropDownBean Value) {
    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean2()!=null){
	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean2().getId()){
	    		((Visit) getPage().getVisit()).setboolean1(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean1(false);
	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean2(Value);
    	}
    }
    public void setweizSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public IPropertySelectionModel getweizSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
            getweizSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }

    public void getweizSelectModels() {
        List list=new ArrayList();
        list.add(new IDropDownBean(1,"我的任务"));
        list.add(new IDropDownBean(2,"流程中"));
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(list)) ;
        return ;
    }
    //年份
	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit()).setDropDownBean3(getIDropDownBean(getNianfModel(),new Date().getYear()+1900));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}
	public void setNianfValue(IDropDownBean Value) {
		if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean3()!=null){
			if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean3().getId()){
	    		((Visit) getPage().getVisit()).setboolean2(true);
	    	}else{
	    		((Visit) getPage().getVisit()).setboolean2(false);
	    	}
			 ((Visit) getPage().getVisit()).setDropDownBean3(Value);
		}
	}
	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
    public void getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = DateUtil.getYear(new Date())-2; i <= DateUtil.getYear(new Date())+2; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		 ((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(listNianf)) ;
	}
    private IDropDownBean getIDropDownBean(IPropertySelectionModel model,long id) {
        int OprionCount;
        OprionCount = model.getOptionCount();
        for (int i = 0; i < OprionCount; i++) {
            if (((IDropDownBean) model.getOption(i)).getId() == id) {
                return (IDropDownBean) model.getOption(i);
            }
        }
        return null;
    }
    //ext代码
	public String getGridHtml() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1().getHtml();
	}
	public String getGridScript() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1().getGridScript();
	}
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
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
//	我的意见
	public void setMy_opinion(String value){
		
		((Visit) getPage().getVisit()).setString1(value);
	}
	
	public String getMy_opinion(){
		
		return ((Visit) getPage().getVisit()).getString1();
	}
	
	//历史意见
	public void setHistry_opinion(String value){
		
		((Visit) getPage().getVisit()).setString2(value);
	}
	
	public String getHistry_opinion(){
		
		return ((Visit) getPage().getVisit()).getString2();
	}
//	位置
    public IDropDownBean getzhuangtValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean6()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean6((IDropDownBean)getzhuangtModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean6();
    }
    public void setzhuangtValue(IDropDownBean Value) {
    	((Visit) getPage().getVisit()).setDropDownBean6(Value);
    }
    public void setzhuangtModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel6(value);
    }
    public IPropertySelectionModel getzhuangtModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
        	getzhuangtModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel6();
    }
    public void getzhuangtModels() {
        List list=new ArrayList();
        list.add(new IDropDownBean(-1,"全部"));
        list.add(new IDropDownBean(0,"未审核"));
        list.add(new IDropDownBean(1,"已审核"));
        ((Visit) getPage().getVisit()).setProSelectionModel6(new IDropDownModel(list)) ;
        return ;
    }
}
