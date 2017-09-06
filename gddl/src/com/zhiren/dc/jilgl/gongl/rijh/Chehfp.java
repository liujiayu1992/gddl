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
 * ����
 * ���ӳ��ŵ����䷽ʽ����
 */
public class Chehfp extends BasePage implements PageValidateListener {
	// �����û���ʾ
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

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
//	Toolbar�ϵĴ�����ʾ
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
//			����
			_OpeningChick=false;
			SaveOpening();
			getSelectData();
		} else if(_AchieveChick){
//			���
			_AchieveChick=false;
			SaveAchieve();
			getSelectData();
		}
	}
	
	 private void GotoRijh(IRequestCycle cycle) {
		// TODO �Զ����ɷ������
		
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
		boolean blnFlag=false;		//��ʶ�Ƿ��и�����Ϣ
//		�����߼�
//		1���ж�Ҫ���µĳ���CHELZT��ZHUANGT�����CHELZT��Ϊ��˵��������Ѿ��ڱ�ļƻ��б������ˣ�
//			����ZHUANGT������ó���������״̬�ģ��򲻸��¸ó�zhuangt
//		2�����ɳ���������������Ҫ���³���update���
//		3�����ӿ������ж�
		StringBuffer EroMsg=new StringBuffer("");
		StringBuffer sql = new StringBuffer("begin \n");
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			
			if(!mdrsl.getString("CHELZT").equals("")
				||mdrsl.getString("ZHUANGT").equals("������")){
//				˵��������Ѿ��ڸüƻ��ձ���ļƻ��������˻����ڱ��ƻ����Ѿ���������
				
				if(!mdrsl.getString("CHELZT").equals("")){
//					˵��������ڱ�ļƻ����Ѿ���������
					strJhrq=mdrsl.getString("CHELZT").substring(0,mdrsl.getString("CHELZT").indexOf(','));
					strJhmc=mdrsl.getString("CHELZT").substring(mdrsl.getString("CHELZT").indexOf(',')+1);
					EroMsg.append("����<"+mdrsl.getString("CHEPH")+">�Ѿ���"+strJhrq+"�յļƻ�<"+strJhmc+">������;").append("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");
				}				
			}else{
//				˵�������û�б����ù�
				sql.append("update ").append(tableName).append(" set zhuangt=1");
				sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
				blnFlag=true;
			}
		} 
		mdrsl.close();
		sql.append("end;");
		if(blnFlag){
				
			if(con.getUpdate(sql.toString())>=0){
			
				setMsg("�����ɹ���");
			}else{
				
				setMsg("����ʧ�ܣ�");
			}	
		}else{
			setMsg("�����ɹ���");
		}
		setErroMsg(EroMsg.toString());
		con.Close();
	}
	
	private void Achieve(String strchange, Visit visit){
		
		JDBCcon con = new JDBCcon();
		String tableName="qicrjhcpb";
		boolean blnFlag=false;		//��ʶ�Ƿ��и�����Ϣ
//		�����߼�
//		1���ȵõ�Ҫ���µĳ���zhuangt����ǡ�����ɡ�����ԣ���������update���
//		2�����ӿ������ж�
		StringBuffer sql = new StringBuffer("begin \n");
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			
			if(!mdrsl.getString("ZHUANGT").equals("�����")){
						
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
			
				setMsg("�����ɹ���");
			}else{
				
				setMsg("����ʧ�ܣ�");
			}	
		}else{
			setMsg("�����ɹ���");
		}
		con.Close();
	}

	private void Save1(String strchange, Visit visit){
		 
		JDBCcon con = new JDBCcon();
		String tableName="qicrjhcpb";
		long Rijhb_id=((Visit) getPage().getVisit()).getLong2();
		long Yunsdw_id=((Visit) getPage().getVisit()).getLong3();
	
//		�����߼�
//		1��ɾ��qicrjhcpb������
//		2����ѡ���������Ϊ�¼�¼����
		
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
//						���������һ�����Ƿ��ظ�
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
			
			setMsg("����ɹ���");
		}else{
			setMsg("����ʧ�ܣ�");
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
			
//			�������ռƻ����ɳ�
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
			
//			��������������״̬
			sb.append(" select gl.id,cl.id as chelxxb_id,jh.mingc as jihmc,ysdw.mingc as yunsdw,cl.cheph,gl.yunmcs,				\n")
			  .append("		decode(gl.zhuangt,0,'δ����',1,'������',2,'�����') as zhuangt,getQicrjh_Chelzt(jh.id,cl.id) as chelzt	\n")
			  .append("		from qicrjhb jh,qicrjhcpb gl,chelxxb cl,yunsdwb ysdw												\n")
			  .append("			where jh.id=gl.qicrjhb_id and gl.chelxxb_id=cl.id												\n") 
	          .append("   			and cl.yunsdwb_id=ysdw.id  and jh.id="+Rijhb_id+" and ysdw.id="+Yunsdw_id+"					\n")
	          .append("		order by zhuangt,cheph	");
		}
		
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
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
		
//		��������
		egu.getColumn("jihmc").setHeader("�ƻ�����");
		egu.getColumn("yunsdw").setHeader(Locale.yunsdw_id_chepb);
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("yunmcs").setHeader("��ú����");
		
//		����������
		egu.getColumn("id").setHidden(true);
		egu.getColumn("chelxxb_id").setHidden(true);
		
//		���ò��ɱ༭
		egu.getColumn("id").setEditor(null);
		egu.getColumn("chelxxb_id").setEditor(null);
		egu.getColumn("jihmc").setEditor(null);
		egu.getColumn("yunsdw").setEditor(null);
		egu.getColumn("cheph").setEditor(null);
		
		
//		����Toolbar��ť	
		GridButton gReturn = new GridButton("����",
				"function(){document.getElementById('ReturnButton').click();}");
		gReturn.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(gReturn);
		
		if(((Visit) getPage().getVisit()).getString4().equals("Rjh")){
//			�������ռƻ����ɳ�
			egu.addTbarText("-");
			egu.addToolbarButton("����",GridButton.ButtonType_SubmitSel, "SaveButton");
			
//			��������
			egu.addTbarText("-");
			egu.addTbarText("��������");
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
//			��������������״̬
			egu.getColumn("yunmcs").setEditor(null);
			egu.getColumn("zhuangt").setHeader("״̬");
			egu.getColumn("zhuangt").setEditor(null);
			egu.getColumn("zhuangt").setRenderer("renderHdzt");
			
//			��ֵΪ�ƻ��ոó��������ƻ��е�״̬�������Ϊ�գ�
//			�ͱ����ó��������ƻ����Ѿ����ã����ֵΪ�Ǹ��ƻ������ں�����
			egu.getColumn("chelzt").setHidden(true);
			egu.getColumn("chelzt").setEditor(null);
			
//			��������
			egu.addTbarText("-");
			egu.addTbarText("��������");
			TextField tfsearchcph=new TextField();
			tfsearchcph.setId("T_SEARCH_CHEPH");
			tfsearchcph.setWidth(90);
			tfsearchcph.setListeners("specialkey:Searchcheph");
			egu.addToolbarItem(tfsearchcph.getScript());
			
			egu.addTbarText("&nbsp&nbsp");
			egu.addTbarText("-");
			egu.addToolbarButton("����",GridButton.ButtonType_SubmitSel, "OpeningButton");
			egu.addTbarText("&nbsp&nbsp");
			egu.addTbarText("-");
			egu.addToolbarButton("���",GridButton.ButtonType_SubmitSel, "AchieveButton");
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
			.append(" 		Ext.MessageBox.alert('��ʾ��Ϣ','û�ж�Ӧ�ĳ��ţ�');	\n")
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

	// ����
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
				.setProSelectionModel1(new IDropDownModel(sql,"��ѡ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
	
	//��Ӧ��Model
	
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
	
	//��Ӧ��Model_end
	
//	ú��Model
	
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
	
//	ú��Model_end
	
//	���䵥λModel
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
	
//	���䵥λModel_end
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			init();
		}
	}
	
	private void init() {

//		�ϸ�ҳ�����
//		((Visit) getPage().getVisit()).setLong1(0);			//�糧��Ϣ��id
//		((Visit) getPage().getVisit()).setLong2(0);			//�ռƻ�id
//		((Visit) getPage().getVisit()).setLong3(0);			//���䵥λ��id
//		((Visit) getPage().getVisit()).setString3("");		//�ƻ�����
//		((Visit) getPage().getVisit()).setString4("");		//��Rijhҳ�洫�ݸ�Chehfpҳ��Ĳ���,������Ϊ�ջ�Tzclzt
															//	���������Ƿ��䳵�Ż��ǳ���״̬�ĵ���
		
		setYunsdwModel(null);		//4
		getYunsdwModels();			//4
		
		getSelectData();
	}
}
