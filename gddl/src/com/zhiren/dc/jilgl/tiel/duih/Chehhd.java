package com.zhiren.dc.jilgl.tiel.duih;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.Locale;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Chehhd extends BasePage {
//	界面用户提示
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
		setTbmsg(null);
	}
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
//	衡单下拉框
	public IDropDownBean getHengdValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean1()==null) {
			if(getHengdModel().getOptionCount()>0) {
				setHengdValue((IDropDownBean)getHengdModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	public void setHengdValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean1(value);
	}
	
	public IPropertySelectionModel getHengdModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel1()==null) {
			setHengdModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel1();
	}
	public void setHengdModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel1(value);
	}
	
	public void setHengdModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		File filepath = new File(visit.getXitwjjwz()+"/shul/jianjwj");
		File[] files = filepath.listFiles();
		List hengdList = new ArrayList();
		if(files != null)
			for (int i = files.length - 1; i >= 0; i--) {
				if(files[i].isFile()
						&& files[i].getName().length()==15
						&& files[i].getName().indexOf(".")==-1) {
					hengdList.add(new IDropDownBean(i,files[i].getName()));
				}
			}
		setHengdModel(new IDropDownModel(hengdList));
	}
//	未检斤文件的方法
	public SortMode getSort() {
		return SortMode.USER;
	}

	public IPropertySelectionModel getChepModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			setChepModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setChepModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public List getChepSelected() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setChepSelected(List ChepSelect) {
		((Visit) getPage().getVisit()).setList1(ChepSelect);
	}

	private void setChepModels() {
		setChepSelected(null);
		Visit v = (Visit) getPage().getVisit();
		List _ChepList = new ArrayList();
		JDBCcon con = new JDBCcon();
		String sql = "select zhi from xitxxb where mingc='显示录入时间' "
				+ "and leib='数量' and diancxxb_id=" + v.getDiancxxb_id();
		ResultSetList rs = con.getResultSetList(sql);
		boolean isHidden = true;
		if (rs.next()) {
			isHidden = ("否".equals(rs.getString("zhi")));
		}
		
		sql=" select * from xitxxb where mingc='车号核对可选框显示序号' and zhi='是' " +
				"  and leib='数量' and zhuangt=1 and diancxxb_id="+v.getDiancxxb_id();
		
		rs=con.getResultSetList(sql);
		
		boolean ShowXuh=false;
		if(rs.next()){
			ShowXuh=true;
		}
		
		
		if (isHidden) {
			if(ShowXuh){
				sql = "select id,rownum||'->'||cheph from chepb where chebb_id< "+SysConstant.CHEB_QC+" and hedbz="+SysConstant.HEDBZ_TJ;
			}else{
				sql = "select id,cheph from chepb where chebb_id< "+SysConstant.CHEB_QC+" and hedbz="+SysConstant.HEDBZ_TJ;
			}
			
		} else {
			if(ShowXuh){
				sql = "select id,rownum||'->'||cheph||','||to_char(lursj,'yyyy-mm-dd hh24:mi:ss') as cheph from chepb where chebb_id< "+SysConstant.CHEB_QC+" and hedbz="+SysConstant.HEDBZ_TJ;
			}else{
				sql = "select id,cheph||','||to_char(lursj,'yyyy-mm-dd hh24:mi:ss') as cheph from chepb where chebb_id< "+SysConstant.CHEB_QC+" and hedbz="+SysConstant.HEDBZ_TJ;
			}
			
		}
		rs = con.getResultSetList(sql);
		while (rs.next()) {
			_ChepList.add(new IDropDownBean(rs.getLong("id"), rs
					.getString("cheph")));
		}
		con.Close();
		setChepModel(new IDropDownModel(_ChepList));
	}
//	取得对应文件名的数据并返回List
	private List getDataList() {
		Visit visit = (Visit)this.getPage().getVisit();
		File filepath = new File(visit.getXitwjjwz()+"/shul/jianjwj");
		if(getHengdValue() == null) {
			WriteLog.writeErrorLog(ErrorMessage.Chehhd002);
//			setMsg(ErrorMessage.Chehhd002);
			return null;
		}
		File file = new File(filepath,getHengdValue().getValue());
//		if(!file.exists()) {
//			setHengdValue(null);
//		}
		FileReader fr;
		BufferedReader br;
		BigInteger bi;
		BigInteger keygen;
		String bufferStr;
		String strarr[];
		StringBuffer sb;
		char c;
		long asclong;
		List list = new ArrayList();
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
			WriteLog.writeErrorLog(ErrorMessage.Chehhd003);
			setMsg(ErrorMessage.Chehhd003);
			return null;
		}
		br = new BufferedReader(fr);
		try {
			keygen = BigInteger.valueOf(Long.parseLong(file.getName()
					.substring(0, 8)));
			while ((bufferStr = br.readLine()) != null) {
				if("".equals(bufferStr)) {
					continue;
				}
				sb = new StringBuffer();
				strarr = bufferStr.split("  ");
				for (int k = 0; k < strarr.length; k++) {
					bi = new BigInteger(strarr[k].trim());
					asclong = bi.xor(keygen).longValue();
					c = (char) asclong;
					sb.append(c);
				}
				String strGuohxx[] = sb.toString().trim().split(",",4);
				String maoz = "".equals(strGuohxx[0])?"":String.valueOf(CustomMaths.div(Double.parseDouble(strGuohxx[0]),
						1000.0));
				String piz = "".equals(strGuohxx[1])?"":String.valueOf(CustomMaths.div(Double.parseDouble(strGuohxx[1]),
						1000.0));
				String sud = strGuohxx[2];
				String cheph = strGuohxx[3];
				list.add(new Guohxx(maoz,piz,sud,cheph));
			}
		} catch (NumberFormatException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
			WriteLog.writeErrorLog(ErrorMessage.Chehhd004);
			setMsg(ErrorMessage.Chehhd004);
			return null;
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
			WriteLog.writeErrorLog(ErrorMessage.Chehhd005);
			setMsg(ErrorMessage.Chehhd005);
			return null;
		}finally {
				try {
					br.close();
					fr.close();
				} catch (IOException e) {
					// TODO 自动生成 catch 块
					e.printStackTrace();
				}
		}
		return list;
	}
//	衡单List记录
	public List getHengdList() {
		if(((Visit)this.getPage().getVisit()).getList2() == null) {
			setHengdList(new ArrayList());
		}
		return ((Visit)this.getPage().getVisit()).getList2();
	}
	public void setHengdList(List list)	{
		((Visit)this.getPage().getVisit()).setList2(list);
	}
//	刷新衡单列表
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("衡单名:"));
		
		ComboBox hengdcb = new ComboBox();
		hengdcb.setTransform("HengdSelect");
		hengdcb.setWidth(130);
		hengdcb.setListeners("select:function(own,rec,index){Ext.getDom('HengdSelect').selectedIndex=index}");
		tb1.addField(hengdcb);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton rbtn = new ToolbarButton(null,"刷新","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		ToolbarButton okbtn = new ToolbarButton(null,"对号","function(){document.getElementById('DuihButton').click();}");
		okbtn.setIcon(SysConstant.Btn_Icon_SelSubmit);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addItem(okbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv");
		egu.addColumn(new GridColumn(GridColumn.ColType_Rownum));
		GridColumn gridc1 = new GridColumn(0,"maoz",Locale.maoz_chepb, 70);
		egu.addColumn(gridc1);
		GridColumn gridc2 = new GridColumn(0,"piz",Locale.piz_chepb, 70);
		egu.addColumn(gridc2);
		GridColumn gridc3 = new GridColumn(0,"sud",Locale.ches_chepb, 70);
		egu.addColumn(gridc3);
		GridColumn gridc4 = new GridColumn(0,"cheph",Locale.cheph_chepb, 90);
		egu.addColumn(gridc4);
		egu.setWidth(346);
		//egu.setHeight("bodyHeight - 30");
		
		egu.addPaging(0);
		List list = getDataList();
		if(list == null) {
			String[][] data = {{"","","",""}};
			egu.setData(data);
			setExtGrid(egu);
			return;
		}
		if(list.isEmpty()) {
			WriteLog.writeErrorLog(ErrorMessage.Chehhd001);
			setMsg(ErrorMessage.Chehhd001);
			String[][] data = {{"","","",""}};
			egu.setData(data);
			setExtGrid(egu);
			return;
		}
		String[][] data = new String[list.size()][4];
		for(int i=0 ; i<list.size() ; i++) {
			data[i][0] = ((Guohxx)list.get(i))._maoz;
			data[i][1] = ((Guohxx)list.get(i))._piz;
			data[i][2] = ((Guohxx)list.get(i))._sud;
			data[i][3] = ((Guohxx)list.get(i))._cheph;
		}
		setHengdList(list);
		egu.setData(data);
		setExtGrid(egu);
//		if(getHengdValue().getValue().lastIndexOf("z")==14) {
//			setMsg("这个文件是您补录的吧？里面一共 "+list.size()+" 车。");
//		}else {
//			setMsg("这个文件是正常的检斤文件，一共 "+list.size()+" 车。");
//		}
	}
//	表格使用的方法
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}
//	工具栏使用的方法
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
		return getToolbar().getRenderScript();
	}
//	页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setboolean1(false);
			visit.setString1(null);
			setHengdValue(null);
			visit.setboolean6(false);
			setHengdModel(null);
			setChepModel(null);
			setChepSelected(null);
			setHengdList(null);
			setHengdType(0);
			getSelectData();
		}
		
//		if(visit.getboolean1()) {
//			visit.setboolean1(false);
//			getSelectData();
//		}
		
		if(visit.getboolean6()){
			visit.setboolean6(false);
			AddXuh();
			
		}
		
		getSelectData();
	}
	
	private void AddXuh(){
		

		Visit visit=(Visit)this.getPage().getVisit();
		visit.setboolean6(false);//
		
		List list=visit.getList6();
		
		if(getChepSelected()==null){
			return ;
		}
		for(int i=0;i<getChepSelected().size();i++){
			IDropDownBean be=(IDropDownBean)getChepSelected().get(i);
			be.setValue((String)list.get(i)+"->"+be.getValue());
		}
		
		
	}
//	按钮的监听事件
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _DuihChick = false;
	public void DuihButton(IRequestCycle cycle) {
		_DuihChick = true;
	}
	
	private void Duih(IRequestCycle cycle) {
		

		
		if(getHengdValue() == null) {
			setMsg("检斤文件呢？");
			return;
		}
		if(getHengdList().isEmpty()) {
			setMsg("似乎您忘了刷新？");
			return;
		}
		if(getChepModel().getOptionCount()==0) {
			setMsg("您在开玩笑吗？根本就没有未过衡的车皮。");
			return;
		}
		if(getChepSelected()==null || getChepSelected().isEmpty()) {
			if(getHengdType()>2) {
				setMsg("这个文件中没有车号信息，所以请您手动选择车号！");
				return;
			}
		}else {
			if(getChepSelected().size() != getHengdList().size()) {
				setMsg("衡单上共有 "+getHengdList().size()+" 车，您选择了 "+getChepSelected().size()+" 车。" );
				return;
			}
		}
		setHengdType(getHengdType());
		cycle.activate("Duih");
	}
//	判断衡单是属于哪一种类型
	public int getHengdType() {
		String cheph = ((Guohxx)getHengdList().get(0))._cheph;
		
		double maoz = Double.parseDouble(((Guohxx)getHengdList().get(0))._maoz);
		String strpiz = ((Guohxx)getHengdList().get(0))._piz;
		double piz = Double.parseDouble(strpiz==null||"".equals(strpiz)?"0":strpiz);
		if(cheph==null || "".equals(cheph)) {
			if(maoz>0 && piz >0) 
				return SysConstant.Hengd_Manual_all;
			else
				if(piz ==0)
					return SysConstant.Hengd_Manual_maoz;
				else
					return SysConstant.Hengd_Manual_piz;
		}else {
			if(maoz>0 && piz >0) 
				return SysConstant.Hengd_Auto_all;
			else
				if(piz ==0)
					return SysConstant.Hengd_Auto_maoz;
				else
					return SysConstant.Hengd_Auto_piz;
		}
	}
	public void setHengdType(int type) {
		((Visit) getPage().getVisit()).setInt1(type);
	}
//	表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
			setChepModels();
		}
		if (_DuihChick) {
			_DuihChick = false;
			Duih(cycle);
		}
	}
//	页面登陆验证
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
}
