package com.zhiren.dc.jilgl.gongl.daoy;

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
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者:tzf
 * 时间:2009-07-15
 * 内容:搬倒机  信息修改
 */
public class Qicdyxg extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false );
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _CancelChick = false;

	public void CancelButton(IRequestCycle cycle) {
		_CancelChick = true;
	}
	
	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if(_DeleteChick){
			_DeleteChick=false;
			delete();
			this.getSelectData();
		}
		if(_CancelChick){
			_CancelChick=false;
			huit();
			this.getSelectData();
		}
	}
	
	private void delete(){
		
		JDBCcon  con=new JDBCcon();
		
		String sql=" delete from qicdyb where  id in ("+this.getParameters()+")";
		int flag=con.getDelete(sql);
		if(flag>=0){
			this.setMsg("数据操作成功!");
		}else{
			this.setMsg("数据操作失败!");
		}
		con.Close();
	}

	private void huit(){
		JDBCcon  con=new JDBCcon();
		String sql=" update qicdyb set piz=0,qingcsj='' where id in("+this.getParameters()+")";
		
		int flag=con.getUpdate(sql);
		if(flag>=0){
			this.setMsg("数据操作成功!");
		}else{
			this.setMsg("数据操作失败!");
		}
	}
	
	private String Parameters;//记录项目ID

	public String getParameters() {
		
		return Parameters;
	}

	public void setParameters(String value) {
		
		Parameters = value;
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		//-----------------------------
		String jingz_sql=" select zhi  from xitxxb where mingc='搬倒车数量净重字段截取保留位数' and leib='数量' and zhuangt=1 and diancxxb_id="+visit.getDiancxxb_id();
		ResultSetList rsl_jz=con.getResultSetList(jingz_sql);
		
		String baol_cu="-1";
		String jingz_cons=" (q.maoz-q.piz) as jingz,";//净重字段 用到的 计算公式
		if(rsl_jz.next()){//判断系统净重  字段 模式   
			baol_cu=rsl_jz.getString("zhi");
			jingz_cons=" trunc((q.maoz-q.piz),"+baol_cu+") as jingz, ";
		}
		//--------------------------------------
		
		String shij=this.getRiq();
		String ora_str="to_date('"+shij+"','yyyy-MM-dd')";
		String sql = 
			"select q.id, q.diancxxb_id, q.cheph,q.qingcsj, m1.mingc yuanmc_id,\n" +
			"m2.mingc xiemc_id, q.maoz,q.piz,"+jingz_cons+" nvl('"+visit.getRenymc()+"','') qingcjjy,q.qingch\n" + 
			"from qicdyb q,meicb m1,meicb m2\n" + 
			"where q.yuanmc_id = m1.id(+) and q.xiemc_id = m2.id(+)\n" + 
			"and q.diancxxb_id = "+this.getTreeid()+" and to_date(to_char(q.qingcsj,'yyyy-MM-dd'),'yyyy-MM-dd')="+ora_str+" order by q.qingcsj asc";

		
//		System.out.println(sql);
		ResultSetList rsl = con
				.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("qicdyb");
		
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHidden(true);
		
		egu.getColumn("cheph").setHeader("车号");
		egu.getColumn("cheph").setEditor(null);
		
		egu.getColumn("qingcsj").setHeader("轻车时间");
		egu.getColumn("qingcsj").setEditor(null);
		
		egu.getColumn("yuanmc_id").setHeader("原煤场");
		egu.getColumn("yuanmc_id").setEditor(null);
		
		egu.getColumn("xiemc_id").setHeader("煤场");
		egu.getColumn("xiemc_id").setEditor(null);
		
		egu.getColumn("maoz").setHeader("毛重");
		egu.getColumn("maoz").setEditor(null);
		
		egu.getColumn("piz").setHeader("皮重");
		egu.getColumn("piz").setEditor(null);
		
		egu.getColumn("jingz").setHeader("净重");
		egu.getColumn("jingz").setEditor(null);
		
		egu.getColumn("qingcjjy").setHeader("轻车员");
		egu.getColumn("qingcjjy").setEditor(null);
		
		egu.getColumn("qingch").setHeader("轻车衡号");
		egu.getColumn("qingch").setEditor(null);
		
		
		egu.addTbarText("轻车时间:");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		df.setId("guohrq");
		egu.addToolbarItem(df.getScript());
		
		
		
		
		egu.addTbarText("-");
		egu.addTbarText("输入车号：");
		 TextField theKey=new TextField();
		 theKey.setId("theKey");
		 theKey.setListeners("change:function(thi,newva,oldva){ sta=new Array;}\n");
		 theKey.setListeners("specialkey:function(field,e){SearCH(field,e);}");
		 egu.addToolbarItem(theKey.getScript());
		GridButton chazhao=new GridButton("（模糊）查找/查找下一个","function(){SearCH(null,null);}");
		chazhao.setIcon(SysConstant.Btn_Icon_Search);
		egu.addTbarBtn(chazhao);
		egu.addTbarText("-");
		
		
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符
		
		GridButton cx=new GridButton("查询","function(){ document.getElementById('RefurbishButton').click();}");
		cx.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(cx);
		
		egu.addTbarText("-");
		
		String str2=
			"   var recs = gridDiv_sm.getSelections(); \n"
	        +"  if(recs!=null && recs.length>0 ){  gridDiv_history='';\n"
	        +"  for(var i=0;i<recs.length;i++){ var rec=recs[i];\n"
	        +"      gridDiv_history += rec.get('ID'); if(i!=recs.length-1) gridDiv_history+=',';\n"
	        +"		}\n"  
	        +"  	document.getElementById('PARAMETERS').value=gridDiv_history; \n"
	        +"      "
	        +"  }else{\n"
	        +"  	Ext.MessageBox.alert('提示信息','请选中一个记录!'); \n"
	        +"  	return;"
	        +"  }"
	        +"";
		
		
		
		GridButton huit=new GridButton("回退","function(){ "+str2+" document.getElementById('CancelButton').click();}");
		huit.setIcon(SysConstant.Btn_Icon_Cancel);
		egu.addTbarBtn(huit);
		
		egu.addTbarText("-");
		
		GridButton del=new GridButton("删除","function(){ "+str2+" document.getElementById('DeleteButton').click();}");
		del.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(del);
		
		
		String chaz="function SearCH(field,e){\n"+
		
		"   if(e!=null && e.getCharCode()!=e.ENTER){return;}\n"+
        "       if(theKey.getValue().toString()==''){Ext.MessageBox.alert('提示','请输入值');return;}\n"+
     "       var len=gridDiv_data.length;\n"+
     "       var count=1;\n"+
//     "       if(len%"+egu.getPagSize()+"!=0){\n"+
//     "        count=parseInt(len/"+egu.getPagSize()+")+1;\n"+
//     "        }else{\n"+
//     "          count=len/"+egu.getPagSize()+";\n"+
//     "        }\n"+
     "        for(var i=0;i<count;i++){\n"+
//     "           gridDiv_ds.load({params:{start:i*"+egu.getPagSize()+", limit:"+egu.getPagSize()+"}});\n"+
     "           var rec=gridDiv_ds.getRange();\n"+
     "           for(var j=0;j<rec.length;j++){\n"+
     "               if(rec[j].get('CHEPH').toString().indexOf(theKey.getValue().toString())!=-1){\n"+
     "                 var nw=[rec[j]]\n"+
     "                  if(sta.indexOf(rec[j].get('ID').toString())==-1){\n"+
     "                      gridDiv_sm.selectRecords(nw);\n"+
     "                      sta[sta.length]=rec[j].get('ID').toString();\n"+
     "						 theKey.focus(true,true);"+
     "                       return;\n"+
     "                  }\n"+
     "                \n"+
     "               }\n"+
     "           }\n"+
     "        }\n"+
     "        if(sta.length==0){\n"+
     "          Ext.MessageBox.alert('提示','你要找的车号不存在');\n"+
     "        }else{\n"+
     "           sta=new Array;\n"+
     "           Ext.MessageBox.alert('提示','查找已经到结尾');\n"+
     "         }\n"+
     "      }\n";
		
		egu.addOtherScript(chaz);
		
		setExtGrid(egu);
		con.Close();
	}
	
//	电厂树
	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
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
	
	
//	绑定日期
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
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
			setRiq(DateUtil.FormatDate(new Date()));
			
			
			
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
			
			getSelectData();
		}
	}
	
	protected void initialize() {
		// TODO 自动生成方法存根
		super.initialize();
		setMsg("");
	}

}
