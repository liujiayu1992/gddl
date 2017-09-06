package com.zhiren.shanxdted.hengqfkxxsx;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Meiksx extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String theKey;
	private boolean Key=false;
    public String gettheKey() {
    	return theKey;
    }
    public void settheKey(String theKey) {
    	this.theKey = theKey;
    }
    
    private boolean hasFenc(JDBCcon con){//有分厂返回  true
		String sql=" select * from diancxxb where fuid="+this.getTreeid();
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			return true;
		}
		return false;
	}

	private void Save() {
		StringBuffer sql = new StringBuffer();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		while (rsl.next()) {
			if("".equals(rsl.getString("msid"))){
				sql.append("insert into meiksxb values(\n" +
						"getnewid("+getTreeid()+"),\n" +
						getTreeid()+",\n" +
						rsl.getString("id")+",\n" +
						(getExtGrid().getColumn("hengqsxzt").combo)
						.getBeanId(rsl.getString("hengqsxzt")) + "\n" +
						");");
			}else{
				long hengqsxzt = (getExtGrid().getColumn("hengqsxzt").combo)
						.getBeanId(rsl.getString("hengqsxzt"));
				if(hengqsxzt == 0){
					sql.append("delete meiksxb where id =" + rsl.getString("msid") + ";");
				}else{
					sql.append("update meiksxb set HENGQSXZT = '"+
						(getExtGrid().getColumn("hengqsxzt").combo)
						.getBeanId(rsl.getString("hengqsxzt"))+"' where id ="+rsl.getString("msid") + ";\n");
				}
			}
		}
		int flag = 0;
		if(sql.length()!=0){
			flag = con.getUpdate("begin\n" + sql.toString() + "end;\n");
			if(flag==-1){
				setMsg("设置失败"); 
			}else{
				setMsg("设置成功");
			}
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _ChazChick = false;
    public void ChazButton(IRequestCycle cycle) {
    	_ChazChick = true;
    }

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_ChazChick) {
    		_ChazChick = false;
    		Key=true;
        }
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		String diancxxb_id = getTreeid();
		if(hasFenc(con)){
			diancxxb_id = "=" + diancxxb_id;
		}else{
			diancxxb_id = "(+)=" + diancxxb_id;
		}
		String sql = "";
		if(Key){
    		sql = "select ms.id msid,m.id,ms.diancxxb_id,d.mingc dmingc,m.xuh,m.mingc,m.quanc,decode(ms.hengqsxzt,0,'_',1,'默认使用',2,'正常使用') hengqsxzt \n" +
			      "  from meikxxb m,meiksxb ms,diancxxb d \n" +
				  " where m.id=ms.meikxxb_id(+)\n" +
				  "   and (m.mingc like '%" + gettheKey() + "%' or m.quanc like '%" + gettheKey() + "%')\n" + 
				  "   and ms.diancxxb_id"+diancxxb_id+"\n" +
				  "   and m.zhuangt=1\n" +
				  "   and d.id" + diancxxb_id + "\n" + 
    			  " order by xuh";
    		Key=false;
    	}else{
    		sql = "select ms.id msid,m.id,ms.diancxxb_id,d.mingc dmingc,m.xuh,m.mingc,m.quanc,decode(ms.hengqsxzt,0,'_',1,'默认使用',2,'正常使用') hengqsxzt \n" +
    			  "  from meikxxb m,meiksxb ms,diancxxb d \n" +
    			  " where m.id=ms.meikxxb_id(+)\n" +
    			  "   and ms.diancxxb_id"+diancxxb_id+"\n" +
    			  "   and m.zhuangt=1\n" +
    			  "   and d.id" + diancxxb_id + "\n" + 
    			  " order by xuh";
    	}
		
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("meikxxb");
		
		egu.getColumn("msid").setHeader("msid");
		egu.getColumn("msid").setEditor(null);
		egu.getColumn("msid").setHidden(true);
		
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setEditor(null);
		egu.getColumn("id").setHidden(true);
		
		
		egu.getColumn("diancxxb_id").setHeader("diancxxb_id");
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("diancxxb_id").setHidden(true);
		
		egu.getColumn("dmingc").setHeader("电厂名称");
		egu.getColumn("dmingc").setEditor(null);
		egu.getColumn("dmingc").setUpdate(false);
		egu.getColumn("dmingc").setHidden(true);
		
		
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("xuh").setEditor(null);
		egu.getColumn("xuh").setWidth(50);
		egu.getColumn("xuh").setUpdate(false);
		
		egu.getColumn("mingc").setHeader("简称");
		egu.getColumn("mingc").setEditor(null);
		egu.getColumn("mingc").setWidth(200);
		egu.getColumn("mingc").setUpdate(false);
		egu.getColumn("quanc").setHeader("全称");
		egu.getColumn("quanc").setEditor(null);
		egu.getColumn("quanc").setWidth(300);
		egu.getColumn("quanc").setUpdate(false);
		
		
		egu.getColumn("hengqsxzt").setHeader("使用类别");
		List l = new ArrayList();
		l.add(new IDropDownBean(0, "_"));
		l.add(new IDropDownBean(1, "默认使用"));
		l.add(new IDropDownBean(2, "正常使用"));
		egu.getColumn("hengqsxzt").setEditor(new ComboBox());
		egu.getColumn("hengqsxzt").setComboEditor(egu.gridId, new IDropDownModel(l));
		egu.getColumn("hengqsxzt").setReturnId(false);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		
		egu.addTbarText("-");
		egu.addTbarText("电厂：");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree", ExtTreeUtil.treeWindowType_Dianc, 
			((Visit)this.getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");

		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		
		egu.addTbarText("-");
		egu.addTbarText("输入名称：");
		TextField theKey = new TextField();
		theKey.setWidth(80);
		theKey.setId("theKey_text");
		theKey.setListeners("specialkey:function(thi,e){if (e.getKey()==13){var objkey = document.getElementById('theKey');objkey.value = theKey_text.getValue();document.getElementById('ChazButton').click();}}\n");
		egu.addToolbarItem(theKey.getScript());
		
		String Chaz = "function(){var objkey = document.getElementById('theKey');objkey.value=theKey_text.getValue();document.getElementById('ChazButton').click();}";
		GridButton chaz = new GridButton("模糊查找", Chaz, SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(chaz);
		
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
	
//	设置电厂树_开始
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	
	public void setTreeid(String treeid) {
		if(!treeid.equals(((Visit) getPage().getVisit()).getString2()) || ((Visit) getPage().getVisit()).getString2()==null){
			((Visit) getPage().getVisit()).setString2(treeid);
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
//	设置电厂树_结束

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
			setTreeid("");
		}
		setMsg("");
		getSelectData();
	}
}
