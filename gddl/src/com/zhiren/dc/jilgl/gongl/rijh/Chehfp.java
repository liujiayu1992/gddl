package com.zhiren.dc.jilgl.gongl.rijh;

import java.sql.ResultSet;
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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 2009-02-19
 * 王磊
 * 增加车号的运输方式过滤
 */
public class Chehfp extends BasePage implements PageValidateListener {
	// 界面用户提示
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
		setErroMsg("");
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
//	Toolbar上的错误提示
	private String ErroMsg;
	public String getErroMsg() {
		return ErroMsg;
	}

	public void setErroMsg(String value) {
		ErroMsg=value;
	}
	
	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _OpeningChick = false;

	public void OpeningButton(IRequestCycle cycle) {
		_OpeningChick = true;
	}
	
	private boolean _AchieveChick = false;

	public void AchieveButton(IRequestCycle cycle) {
		_AchieveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_ReturnChick) {
			_ReturnChick = false;
			
			GotoRijh(cycle);
		} else if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		} else if (_OpeningChick){
//			启用
			_OpeningChick=false;
			SaveOpening();
			getSelectData();
		} else if(_AchieveChick){
//			完成
			_AchieveChick=false;
			SaveAchieve();
			getSelectData();
		}
	}
	
	 private void GotoRijh(IRequestCycle cycle) {
		// TODO 自动生成方法存根
		
			cycle.activate("Rijh");
	}
	 
	private void  SaveOpening(){
		
		Visit visit = (Visit) this.getPage().getVisit();
		Opening(getChange(), visit);
	}
	
	private void  SaveAchieve(){
		
		Visit visit = (Visit) this.getPage().getVisit();
		Achieve(getChange(), visit);
	}

	private void Save() {
		 
		 Visit visit = (Visit) this.getPage().getVisit();
		 Save1(getChange(), visit);
	}
	
	private void Opening(String strchange, Visit visit){
		
		JDBCcon con = new JDBCcon();
		String tableName="qicrjhcpb";
		String strJhrq="";
		String strJhmc="";
		boolean blnFlag=false;		//标识是否有更新信息
//		更新逻辑
//		1、判断要更新的车的CHELZT和ZHUANGT，如果CHELZT不为空说明这个车已经在别的计划中被启用了，
//			否则看ZHUANGT，如果该车是已启用状态的，则不更新该车zhuangt
//		2、生成除上述条件的所有要更新车的update语句
//		3、增加空语句的判断
		StringBuffer EroMsg=new StringBuffer("");
		StringBuffer sql = new StringBuffer("begin \n");
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			
			if(!mdrsl.getString("CHELZT").equals("")
				||mdrsl.getString("ZHUANGT").equals("已启用")){
//				说明这个车已经在该计划日被别的计划中启用了或者在本计划中已经被启用了
				
				if(!mdrsl.getString("CHELZT").equals("")){
//					说明这个车在别的计划中已经被启用了
					strJhrq=mdrsl.getString("CHELZT").substring(0,mdrsl.getString("CHELZT").indexOf(','));
					strJhmc=mdrsl.getString("CHELZT").substring(mdrsl.getString("CHELZT").indexOf(',')+1);
					EroMsg.append("车号<"+mdrsl.getString("CHEPH")+">已经在"+strJhrq+"日的计划<"+strJhmc+">中启用;").append("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");
				}				
			}else{
//				说明这个车没有被启用过
				sql.append("update ").append(tableName).append(" set zhuangt=1");
				sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
				blnFlag=true;
			}
		} 
		mdrsl.close();
		sql.append("end;");
		if(blnFlag){
				
			if(con.getUpdate(sql.toString())>=0){
			
				setMsg("操作成功！");
			}else{
				
				setMsg("操作失败！");
			}	
		}else{
			setMsg("操作成功！");
		}
		setErroMsg(EroMsg.toString());
		con.Close();
	}
	
	private void Achieve(String strchange, Visit visit){
		
		JDBCcon con = new JDBCcon();
		String tableName="qicrjhcpb";
		boolean blnFlag=false;		//标识是否有更新信息
//		更新逻辑
//		1、先得到要更新的车的zhuangt如果是“已完成“则忽略，否则生成update语句
//		2、增加空语句的判断
		StringBuffer sql = new StringBuffer("begin \n");
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			
			if(!mdrsl.getString("ZHUANGT").equals("已完成")){
						
				sql.append("update ").append(tableName).append(" set zhuangt=2")
					.append(",").append("wancsj=sysdate")
					.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
				blnFlag=true;
			}
		} 
		mdrsl.close();
		sql.append("end;");
		if(blnFlag){
				
			if(con.getUpdate(sql.toString())>=0){
			
				setMsg("操作成功！");
			}else{
				
				setMsg("操作失败！");
			}	
		}else{
			setMsg("操作成功！");
		}
		con.Close();
	}

	private void Save1(String strchange, Visit visit){
		 
		JDBCcon con = new JDBCcon();
		String tableName="qicrjhcpb";
		long Rijhb_id=((Visit) getPage().getVisit()).getLong2();
		long Yunsdw_id=((Visit) getPage().getVisit()).getLong3();
	
//		保存逻辑
//		1、删除qicrjhcpb表数据
//		2、将选择的数据作为新纪录插入
		
		StringBuffer sql = new StringBuffer("begin \n");
		sql.append("delete from ").append(tableName).append(" where qicrjhb_id =")
				.append(Rijhb_id).append(";\n");
		
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")")
				.append(",").append(Rijhb_id);
				sql.append("insert into ").append(tableName).append("(id,qicrjhb_id");
				
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					
					if(mdrsl.getColumnNames()[i].equals("CHELXXB_ID")||mdrsl.getColumnNames()[i].equals("YUNMCS")){
//						检查名称在一天中是否重复
						sql.append(",").append(mdrsl.getColumnNames()[i]);
						sql2.append(",").append(getExtGrid().getValueSql(getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
								mdrsl.getString(i)));
					}
				}
				sql.append(") values(").append(sql2).append(");\n");
		} 
		mdrsl.close();
		sql.append("end;");
		if(con.getUpdate(sql.toString())>=0){
			
			setMsg("保存成功！");
		}else{
			setMsg("保存失败！");
		}
		con.Close();
	 }

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		// Visit visit = ((Visit) getPage().getVisit());
		String Rijhmc=((Visit) getPage().getVisit()).getString3();
		long Rijhb_id=((Visit) getPage().getVisit()).getLong2();
		long Yunsdw_id=((Visit) getPage().getVisit()).getLong3();
		
		StringBuffer sb = new StringBuffer();
		
		if(((Visit) getPage().getVisit()).getString4().equals("Rjh")){
			
//			正常的日计划的派车
			sb.append(" select * from 	\n")
			  .append(" (select gl.id,cl.id as chelxxb_id,jh.mingc as jihmc,ysdw.mingc as yunsdw,cl.cheph,gl.yunmcs 		\n")
			  .append("		from qicrjhb jh,qicrjhcpb gl,chelxxb cl,yunsdwb ysdw											\n")
			  .append("			where jh.id=gl.qicrjhb_id and gl.chelxxb_id=cl.id											\n") 
	          .append("   			and cl.yunsdwb_id=ysdw.id  and jh.id="+Rijhb_id+" and ysdw.id="+Yunsdw_id+"	and cl.yunsfsb_id = "+SysConstant.YUNSFS_QIY+"			\n")
	          .append("	union	\n")
	          .append(" select 0 as id,cl.id as chelxxb_id,'"+Rijhmc+"' as jihmc,ysdw.mingc as yunsdw,cl.cheph,0 as yunmcs	\n")
	          .append("		from chelxxb cl,yunsdwb ysdw																	\n")
	          .append("			where ysdw.id=cl.yunsdwb_id and ysdw.id="+Yunsdw_id+"	and cl.yunsfsb_id = "+SysConstant.YUNSFS_QIY+"									\n")
	          .append("			   and cl.id not in (select cl.id															\n") 
	          .append("             	from qicrjhb jh,qicrjhcpb gl,chelxxb cl,yunsdwb ysdw								\n")
	          .append("		                  where jh.id=gl.qicrjhb_id and gl.chelxxb_id=cl.id								\n") 
	          .append("		                        and cl.yunsdwb_id=ysdw.id  and jh.id="+Rijhb_id+" and ysdw.id="+Yunsdw_id+"))	\n")
			  .append(" 	order by id desc,cheph ");
			
		}else if(((Visit) getPage().getVisit()).getString4().equals("Tzclzt")){
			
//			调整车辆的启用状态
			sb.append(" select gl.id,cl.id as chelxxb_id,jh.mingc as jihmc,ysdw.mingc as yunsdw,cl.cheph,gl.yunmcs,				\n")
			  .append("		decode(gl.zhuangt,0,'未启用',1,'已启用',2,'已完成') as zhuangt,getQicrjh_Chelzt(jh.id,cl.id) as chelzt	\n")
			  .append("		from qicrjhb jh,qicrjhcpb gl,chelxxb cl,yunsdwb ysdw												\n")
			  .append("			where jh.id=gl.qicrjhb_id and gl.chelxxb_id=cl.id												\n") 
	          .append("   			and cl.yunsdwb_id=ysdw.id  and jh.id="+Rijhb_id+" and ysdw.id="+Yunsdw_id+"					\n")
	          .append("		order by zhuangt,cheph	");
		}
		
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(3, new GridColumn(GridColumn.ColType_Check));
		egu.addPaging(0);
		
//		设置列名
		egu.getColumn("jihmc").setHeader("计划名称");
		egu.getColumn("yunsdw").setHeader(Locale.yunsdw_id_chepb);
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("yunmcs").setHeader("运煤次数");
		
//		设置隐藏项
		egu.getColumn("id").setHidden(true);
		egu.getColumn("chelxxb_id").setHidden(true);
		
//		设置不可编辑
		egu.getColumn("id").setEditor(null);
		egu.getColumn("chelxxb_id").setEditor(null);
		egu.getColumn("jihmc").setEditor(null);
		egu.getColumn("yunsdw").setEditor(null);
		egu.getColumn("cheph").setEditor(null);
		
		
//		设置Toolbar按钮	
		GridButton gReturn = new GridButton("返回",
				"function(){document.getElementById('ReturnButton').click();}");
		gReturn.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(gReturn);
		
		if(((Visit) getPage().getVisit()).getString4().equals("Rjh")){
//			正常的日计划的派车
			egu.addTbarText("-");
			egu.addToolbarButton("保存",GridButton.ButtonType_SubmitSel, "SaveButton");
			
//			车号搜索
			egu.addTbarText("-");
			egu.addTbarText("车号搜索");
			TextField tfsearchcph=new TextField();
			tfsearchcph.setId("T_SEARCH_CHEPH");
			tfsearchcph.setWidth(90);
			tfsearchcph.setListeners("specialkey:Searchcheph");
			egu.addToolbarItem(tfsearchcph.getScript());
			
			sb.setLength(0);
			sb.append(" var strtmp=''; ")
				.append(" for(var i=0	;i<gridDiv_ds.getCount();i++){		\n")
				.append(" 	if(gridDiv_ds.getAt(i).get('ID')>0){ 			\n")
				.append("		strtmp=strtmp+','+i;}}						\n")
				.append("	gridDiv_sm.selectRows(strtmp.split(','));		\n\n");
			egu.addOtherScript(sb.toString());
			
		}else if(((Visit) getPage().getVisit()).getString4().equals("Tzclzt")){
//			调整车辆的启用状态
			egu.getColumn("yunmcs").setEditor(null);
			egu.getColumn("zhuangt").setHeader("状态");
			egu.getColumn("zhuangt").setEditor(null);
			egu.getColumn("zhuangt").setRenderer("renderHdzt");
			
//			此值为计划日该车在其它计划中的状态，如果不为空，
//			就表明该车在其它计划中已经启用，则该值为那个计划的日期和名称
			egu.getColumn("chelzt").setHidden(true);
			egu.getColumn("chelzt").setEditor(null);
			
//			车号搜索
			egu.addTbarText("-");
			egu.addTbarText("车号搜索");
			TextField tfsearchcph=new TextField();
			tfsearchcph.setId("T_SEARCH_CHEPH");
			tfsearchcph.setWidth(90);
			tfsearchcph.setListeners("specialkey:Searchcheph");
			egu.addToolbarItem(tfsearchcph.getScript());
			
			egu.addTbarText("&nbsp&nbsp");
			egu.addTbarText("-");
			egu.addToolbarButton("启用",GridButton.ButtonType_SubmitSel, "OpeningButton");
			egu.addTbarText("&nbsp&nbsp");
			egu.addTbarText("-");
			egu.addToolbarButton("完成",GridButton.ButtonType_SubmitSel, "AchieveButton");
		}
		
		sb.setLength(0);			
		sb.append("function Searchcheph(){								\n")
			.append("  var cheph='';									\n")
			.append("  var i=-1;										\n")
			.append("  i=gridDiv_ds.findBy(function(rec){				\n")
			.append(" 	if(rec.get('CHEPH')==T_SEARCH_CHEPH.getValue()){ \n")
			.append(" 		return true;								 \n")
			.append("   }												\n")
			.append("}); 												\n")
			.append(" if(i>=0){											\n")
			.append("  		gridDiv_sm.selectRow(i,true);				\n")
			.append(" }else{											\n")
			.append(" 		Ext.MessageBox.alert('提示信息','没有对应的车号！');	\n")
			.append(" }													\n")
			.append("};");
			
		egu.addOtherScript(sb.toString());
		
		if(getErroMsg()!=null&&!getErroMsg().equals("")) {
			egu.addToolbarItem("'->'");
			egu.addToolbarItem("'<marquee width=300 scrollamount=2>"+getErroMsg()+"</marquee>'");
		}
		setExtGrid(egu);
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
		// if(getTbmsg()!=null) {
		// getExtGrid().addToolbarItem("'->'");
		// getExtGrid().addToolbarItem("'<marquee width=300
		// scrollamount=2>"+getTbmsg()+"</marquee>'");
		// }
		return getExtGrid().getGridScript();
	}
	
	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	public String getTreeScript() {
		// System.out.print(((Visit)
		// this.getPage().getVisit()).getDefaultTree().getScript());
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
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

	// 厂别
	public IDropDownBean getChangbValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getChangbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setChangbValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean1()) {

			((Visit) getPage().getVisit()).setDropDownBean1(Value);
		}
	}

	public IPropertySelectionModel getChangbModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {

			getChangbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setChangbModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getChangbModels() {

		String sql = "select id,mingc from diancxxb d where d.fuid="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id()
				+ "order by mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql,"请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
	
	//供应商Model
	
	public IPropertySelectionModel getGongysModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {

			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getGongysModels() {
		
		long Diancxxb_id=0;
		if(((Visit) getPage().getVisit()).isFencb()){
				
			Diancxxb_id=this.getChangbValue().getId();
			
		}else{
			
			Diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
		}

		String sql = "select g.id,g.mingc from gongysb g,gongysdcglb l where 	\n"
				+ " l.gongysb_id=g.id and l.diancxxb_id=	\n"
				+ Diancxxb_id
				+ "order by g.mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
	
	//供应商Model_end
	
//	煤矿Model
	
	public IPropertySelectionModel getMeikdwModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {

			getMeikdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setMeikdwModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getMeikdwModels() {
		
		long Diancxxb_id=0;
		if(((Visit) getPage().getVisit()).isFencb()){
				
			Diancxxb_id=this.getChangbValue().getId();
			
		}else{
			
			Diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
		}

		String sql = "select m.id,m.mingc from gongysb g,meikxxb m,gongysdcglb lg,gongysmkglb lm where g.id=lg.gongysb_id	\n"
				+ " and lm.gongysb_id=g.id and lm.meikxxb_id=m.id and lg.diancxxb_id=	\n"
				+ Diancxxb_id
				+ "order by m.mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
	
//	煤矿Model_end
	
//	运输单位Model
	public IPropertySelectionModel getYunsdwModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {

			getYunsdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setYunsdwModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getYunsdwModels() {
		
		long Diancxxb_id=0;
		if(((Visit) getPage().getVisit()).isFencb()){
				
			Diancxxb_id=this.getChangbValue().getId();
			
		}else{
			
			Diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
		}

		String sql = "select id,mingc from yunsdwb where yunsdwb.diancxxb_id=	\n"
				+ Diancxxb_id
				+ "order by mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}
	
//	运输单位Model_end
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			init();
		}
	}
	
	private void init() {

//		上个页面参数
//		((Visit) getPage().getVisit()).setLong1(0);			//电厂信息表id
//		((Visit) getPage().getVisit()).setLong2(0);			//日计划id
//		((Visit) getPage().getVisit()).setLong3(0);			//运输单位表id
//		((Visit) getPage().getVisit()).setString3("");		//计划名称
//		((Visit) getPage().getVisit()).setString4("");		//由Rijh页面传递给Chehfp页面的参数,参数可为空或Tzclzt
															//	用来区分是分配车号还是车辆状态的调整
		
		setYunsdwModel(null);		//4
		getYunsdwModels();			//4
		
		getSelectData();
	}
}
