package com.zhiren.pub.reny;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DataBassUtil;
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
import com.zhiren.main.validate.Validate;

public class Renyxxfj extends BasePage implements PageValidateListener {
//	进行页面提示信息的设置
	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
	    _msg = _value;
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
//  页面变化记录
    private String Change;
    public String getChange() {
    	return Change;
    }
    public void setChange(String change) {
    	Change = change;
    }
    
//  页面变化记录
    private String theKey;
    private boolean Key=false;
    public String gettheKey() {
    	return theKey;
    }
    public void settheKey(String theKey) {
    	this.theKey = theKey;
    }
    
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}
	
	public String getGridHtml() {
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}
	
	public String getTreeHtml() {
		if (getTree() == null){
			return "";
		}else {
			return getTree().getWindowTreeHtml(this);
		}

	}

	public String getTreeScript() {
		if (getTree() == null) {
			return "";
		}else {
			return getTree().getWindowTreeScript();
		}
	}
	
	
	private String treeid = "";

	public String getTreeid() {

		if (treeid.equals("")) {

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
	

//	按钮事件处理

    private boolean _SaveChick = false;
    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }
    
    private boolean _PowerChick = false;
    public void PowerButton(IRequestCycle cycle) {
        _PowerChick = true;
    }
    
    private boolean _ReSetPwdChick = false;
    public void ReSetPwdButton(IRequestCycle cycle) {
    	_ReSetPwdChick = true;
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
    	if (_ReSetPwdChick) {
    		_ReSetPwdChick = false;
    		ReSetPwd();
        }
    	if (_PowerChick) {
    		_PowerChick = false;
    		Power(cycle);
        }
    	if (_ChazChick) {
    		_ChazChick = false;
    		Key=true;
        }
    }

//  取得组数据
    public void getSelectData() {
    	Visit visit = (Visit) getPage().getVisit();
    	String sql=new String();
    	if(Key){
    		sql = "select id,mingc,quanc,xingb,getrenyfz(id) zu,bum,zhiw,decode(zhuangt,1,'是','否') zhuangt,yiddh,guddh,chuanz,youzbm,email,lianxdz " +
		     " from  renyxxb where diancxxb_id = " + getTreeid() +"" +
		     		" and (mingc like '%" + gettheKey() + "%' or quanc like '%" + gettheKey() + "%')" ;
    		Key=false;
    	}else{
    		sql = "select id,mingc,quanc,xingb,getrenyfz(id) zu,bum,zhiw,decode(zhuangt,1,'是','否') zhuangt,yiddh,guddh,chuanz,youzbm,email,lianxdz " +
    			     " from  renyxxb where diancxxb_id = "
    					+ getTreeid() +" order by mingc";
    	}
		JDBCcon con = new JDBCcon();
    	ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		egu.setTableName("renyxxb");
		egu.setWidth("bodyWidth");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.getColumn("id").setHeader("编号");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor=null;
		egu.getColumn("mingc").setHeader("用户名");
		egu.getColumn("quanc").setHeader("姓名");
		egu.getColumn("xingb").setHeader("性别");
		egu.getColumn("bum").setHeader("部门");
		egu.getColumn("zu").setHeader("权限组");
		egu.getColumn("zu").setEditor(null);
		egu.getColumn("zu").setUpdate(false);
		egu.getColumn("zhiw").setHeader("职位");
		egu.getColumn("zhuangt").setHeader("是否可登陆");
		egu.getColumn("yiddh").setHeader("移动电话");
		egu.getColumn("guddh").setHeader("固定电话");
		egu.getColumn("chuanz").setHeader("传真");
		egu.getColumn("youzbm").setHeader("邮政编码");
		egu.getColumn("email").setHeader("Email");
		egu.getColumn("lianxdz").setHeader("联系地址");
		
		egu.getColumn("xingb").setDefaultValue( "男");
		egu.getColumn("zhuangt").setDefaultValue("是");
		
		ComboBox combSex= new ComboBox();
		egu.getColumn("xingb").setEditor(combSex);
		combSex.setEditable(true);
		List lSex = new ArrayList();
		lSex.add(new IDropDownBean(0, "男"));
		lSex.add(new IDropDownBean(1, "女"));
		egu.getColumn("xingb").setComboEditor(egu.gridId, new IDropDownModel(lSex));
		egu.getColumn("xingb").returnId = false;
		
		ComboBox combisLogin = new ComboBox();
		egu.getColumn("zhuangt").setEditor(combisLogin);
		combisLogin.setEditable(true);
		List lisLogin = new ArrayList();
		lisLogin.add(new IDropDownBean(0, "否"));
		lisLogin.add(new IDropDownBean(1, "是"));
		egu.getColumn("zhuangt").setComboEditor(egu.gridId, new IDropDownModel(lisLogin));
		//是否显示选择电厂树
		if (visit.isFencb()) {
			egu.addTbarText("单位名称:");
			ExtTreeUtil etu = new ExtTreeUtil("diancTree",
					ExtTreeUtil.treeWindowType_Dianc,
					((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
			setTree(etu);
			egu.addTbarTreeBtn("diancTree");
			egu.addTbarText("-");
		}
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		GridButton gSave =  new GridButton(GridButton.ButtonType_Save,"gridDiv",egu.getGridColumns(),"SaveButton");
		egu.addTbarBtn(gSave);
		egu.addToolbarButton(GridButton.ButtonType_Cancel, null);
		
		String sFn = 	"if(gridDiv_sm.getSelected() == null){"
						+"	 Ext.MessageBox.alert('提示信息','请选中一个人员重置密码');"
						+"	 return;"
						+"}"
						+"var grid_rcd = gridDiv_sm.getSelected();"
						+"if(grid_rcd.get('ID') == '0'){"
						+"	 Ext.MessageBox.alert('提示信息','在重置密码之前请先保存!');"
						+"	 return;"
						+"}"
						+"Ext.MessageBox.confirm('提示信息','重置密码后密码将更新为与用户名相同,确认码?',function(btn){"
						+"	 if(btn == 'yes'){"
						+"		    grid_history = grid_rcd.get('ID') + ',' + grid_rcd.get('MINGC');"
						+"			var Cobj = document.getElementById('CHANGE');"
						+"			Cobj.value = grid_history;"
						+"			document.getElementById('ReSetPwdButton').click();"
						+"	       	}"
						+"	  })";
		
		String sPwHandler = "function(){"
					+"var grid_Mrcd = gridDiv_ds.getModifiedRecords();"
					+"if(grid_Mrcd.length > 0){"
					+"	Ext.MessageBox.confirm('提示信息', '重置密码后您所做的更改将不被保存,是否继续?', function(btn){"
					+"		if(btn == 'yes'){"
					+ sFn 
					+		"}"
					+"	});"
					+"}else{"
					+ sFn
					+"}"
					+"}";
					
		egu.addTbarBtn(new GridButton("重置密码",sPwHandler));
		
		String sPowerHandler = "function(){"
					+"if(gridDiv_sm.getSelected()== null){"
		        	+"	Ext.MessageBox.alert('提示信息','请选中一个人员设置分组');"
		        	+"	return;"
		        	+"}"
		        	+"var grid_rcd = gridDiv_sm.getSelected();"
		        	+"if(grid_rcd.get('ID') == '0'){"
		        	+"	Ext.MessageBox.alert('提示信息','在设置分组之前请先保存!');"
		        	+"	return;"
		        	+"}"
		        	+"grid_history = grid_rcd.get('ID');"
					+"var Cobj = document.getElementById('CHANGE');"
					+"Cobj.value = grid_history;"
					+"document.getElementById('PowerButton').click();"
					+"}";
		
		egu.addTbarBtn(new GridButton("设置分组",sPowerHandler));
		String sPrintHandler = "function(){"
						+" var url = 'http://'+document.location.host+document.location.pathname;" 
						+" var end = url.indexOf(';');url = url.substring(0,end);" 
						+" url = url + '?service=page/' + 'Renyxxreport&lx=rezc'; window.open(url,'newWin');}";
		
		egu.addTbarBtn(new GridButton("打印",sPrintHandler));
		egu.addTbarText("-");
		egu.addTbarText("输入用户名或姓名：");
		TextField theKey = new TextField();
		theKey.setWidth(80);
		theKey.setId("theKey_text");
		theKey.setListeners("specialkey:function(thi,e){if (e.getKey()==13){var objkey = document.getElementById('theKey');objkey.value = theKey_text.getValue();document.getElementById('ChazButton').click();}}\n");
		egu.addToolbarItem(theKey.getScript());

		String Chaz = "function(){var objkey = document.getElementById('theKey');objkey.value=theKey_text.getValue();document.getElementById('ChazButton').click();}";
		GridButton chaz = new GridButton("查找", Chaz, SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(chaz);

		
		setExtGrid(egu);

    }
//	保存分组的改动
	private void Save() {
		if(getChange()==null || "".equals(getChange())) {
//			提示信息
			setMsg("修改为空!");
			return ;
		}
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String id = "";
		String mingc = "";
		String quanc = "";
		String xingb = "";
		String bum   = "";
		String zhiw  = "";
		String zhuangt = "";
		String yiddh = "";
		String guddh = "";
		String chuanz = "";
		String youzbm = "";
		String Email = "";
		String lianxdz = "";
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		while(rsl.next()){
			id = rsl.getString("id");
			mingc = rsl.getString("mingc");
			quanc = rsl.getString("quanc");
			xingb = rsl.getString("xingb");
			bum = rsl.getString("bum");
			zhiw = rsl.getString("zhiw");
			zhuangt = rsl.getString("zhuangt").equals("是") ? "1" : "0";
			yiddh = rsl.getString("yiddh");
			guddh = rsl.getString("guddh");
			chuanz = rsl.getString("chuanz");
			youzbm = rsl.getString("youzbm");
			Email = rsl.getString("Email");
			lianxdz = rsl.getString("lianxdz");
			if(id.equals("0")) {
				if(Validate.UserExists(mingc,"0")) {
					setMsg("用户 "+mingc+" 已存在!");
					continue;
				}
				id = MainGlobal.getNewID(visit.getDiancxxb_id());
				String sql = "insert into renyxxb(id, mingc, quanc, mim, xingb,bum,zhiw,yiddh,guddh,chuanz,lianxdz,youzbm,email, diancxxb_id, zhuangt) values("+id+",'"
				+ mingc + "','"+quanc+"',empty_blob(),'"+xingb+"','"+bum+"','"+zhiw+"','"+yiddh+"','"+guddh+"','"+chuanz+"','"+lianxdz+"','"+youzbm+"','"+Email+"',"+ getTreeid() +","+zhuangt+")";
				con.getInsert(sql);
				DataBassUtil dbu = new DataBassUtil();
				try {
					dbu.UpdateBlob("renyxxb", "mim", Long.parseLong(id), mingc,true);
				}catch(Exception e) {
					e.printStackTrace();
					continue;
				}
			}else {
				if(Validate.UserExists(mingc,id)) {
					setMsg("用户 "+mingc+" 已存在!");
					continue;
				}
				String sql = "update renyxxb set mingc='"+mingc+"',quanc='"+quanc
				+"',xingb='"+xingb+"',bum='"+bum+"',zhiw='"+zhiw+"',zhuangt='"+zhuangt
				+"',yiddh='"+yiddh+"',guddh='"+guddh+"',chuanz='"+chuanz+"',lianxdz='"+lianxdz
				+"',youzbm='"+youzbm+"',email='"+Email+"' where id =" + id;
				con.getUpdate(sql);
			}
		}
		
		rsl = getExtGrid().getDeleteResultSet(getChange());
		while (rsl.next()) {
			id = rsl.getString("id"); 
			String sql = "delete from renyxxb where id =" + id;
			con.getDelete(sql);
		}
	}
//	设置分组
	private void Power(IRequestCycle cycle) {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("请选中一个人员设置分组!");
			return;
		}
		Visit visit = (Visit) getPage().getVisit();
		visit.setString1(getChange());
		cycle.activate("Renyz");
	}
//	重置密码
	private void ReSetPwd() {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("请选中一个人员重置密码!");
			return;
		}
		String c[]  = getChange().split(",");
		long id = Long.parseLong(c[0]);
		String mingc = c[1];
		if(id == 0) {
			return ;
		}else {
			DataBassUtil dbu = new DataBassUtil();
			try {
				dbu.UpdateBlob("renyxxb", "mim", id, mingc,true);
//				setMsg(dbu.GetStrBlob("renyxxb", "mim", id,true));
			}catch(Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setTreeid("");
		}
		init();
	} 
	
	private void init() {
		setExtGrid(null);
		setTree(null);
		getSelectData();
	}
//	页面判定方法
    public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}
}
