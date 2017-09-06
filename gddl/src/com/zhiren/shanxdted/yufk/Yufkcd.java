package com.zhiren.shanxdted.yufk;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;

public class Yufkcd extends BasePage implements PageValidateListener {
	
	private String msg = "";
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	
	protected void initialize() {
		msg = "";
	}

	// ������

	public String getRiq1() {
		if (((Visit) getPage().getVisit()).getString1() == null 
					|| ((Visit) getPage().getVisit()).getString1().equals("")) {
			((Visit) getPage().getVisit()).setString1(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setRiq1(String riq) {

		if (((Visit) getPage().getVisit()).getString1()!=null&&!((Visit) getPage().getVisit()).getString1().equals(riq)) {
			((Visit) getPage().getVisit()).setString1(riq);
			((Visit) getPage().getVisit()).setboolean2(true);
		}
	}
	

	public String getRiq2() {
		if (((Visit) getPage().getVisit()).getString2() == null 
					|| ((Visit) getPage().getVisit()).getString2().equals("")) {
			((Visit) getPage().getVisit()).setString2(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setRiq2(String riq2) {

		if (((Visit) getPage().getVisit()).getString2() != null 
					&& !((Visit) getPage().getVisit()).getString2().equals(riq2)) {
			((Visit) getPage().getVisit()).setString2(riq2);
			((Visit) getPage().getVisit()).setboolean2(true);
		}

	}
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
//	west ԭ��Ľ����id
	public String getParameters() {
		return ((Visit) this.getPage().getVisit()).getString3();
	}

	public void setParameters(String value) {
		((Visit) this.getPage().getVisit()).setString3(value);
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();		//
			getSelectData();
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}
	
	private void Save() {
		// TODO �Զ����ɷ������
		Visit visit = (Visit) this.getPage().getVisit();
		this.Save1(getChange(), visit);
	}
	
	public void Save1(String strchange,Visit visit) {
		
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ExtGridUtil egu=getExtGrid_Yfk();
		String strJiesb_id="0";
		String strDiancxxb_id="0";
		String sql="";
		int intBZ=0;
		boolean Flag=false;
		try {
//		�����߼���
//				1���Ƚ�yufklsb�е�jiesb_id��ƥ�����Ϣɾ��
//				2������ʷ��(yufklsb)�����¼
//				3��������yufkb�����
			if(visit.isFencb()){
				
				strDiancxxb_id=String.valueOf(this.getFencbValue().getId());
				if(strDiancxxb_id.equals("-1")){
					strDiancxxb_id=String.valueOf(visit.getDiancxxb_id());
				}
			}else{
				
				strDiancxxb_id=String.valueOf(visit.getDiancxxb_id());
			}
			
			if(!this.getParameters().equals("")){
				
				strJiesb_id=this.getParameters().substring(0,getParameters().indexOf(","));
			}
		
			
				
				ResultSetList mdrsl = egu.getModifyResultSet(strchange);
				while(mdrsl.next()) {
					
					if(Double.parseDouble(mdrsl.getString("yue"))>0){
						double  dblJsje=0;
						double dblShiyje=0;
						String strJs ="select nvl(hansmk,0)+nvl(hansyf,0)-nvl(yufkje,0) as jine from jiesb where id="+strJiesb_id;
						ResultSet rec=con.getResultSet(strJs);
						if (rec.next()){
							dblJsje=rec.getDouble("jine");
						}
						//System.out.println(mdrsl.getDouble("chongd"));
						if (dblJsje>0){
							if (mdrsl.getDouble("chongd")>mdrsl.getDouble("yue")){								
								intBZ=1;
								break;
							}
							if (dblJsje>=mdrsl.getDouble("chongd")){
								dblShiyje=mdrsl.getDouble("chongd");
							}else{								
								intBZ=2;
								break;
							}
								
							sql="insert into yufklsb (id, yufkb_id, jine, jiesb_id, shiysj,caozry, beiz)	\n"
								+" values(getnewid("+strDiancxxb_id+"),"+mdrsl.getString("ID")+","
									+dblShiyje+","+strJiesb_id+",sysdate,'"+visit.getRenymc()+"','"
									+DateUtil.getYear(new Date())+"��,������Ϊ"+Jiesdcz.getJieslxAndJiesbh(strJiesb_id)+",ʹ�ñ��Ϊ"+mdrsl.getString("BIANH")
									+"��Ԥ����"+dblShiyje+"Ԫ')	\n";
							
							if(con.getInsert(sql)>=0){
								

								
								sql="update yufkb set yue=yue-"+String.valueOf(dblShiyje)+"		\n"
									+" where id="+mdrsl.getString("ID");
								
								if(con.getUpdate(sql)>=0){
									sql="update jiesb set yufkje=yufkje+"+String.valueOf(dblShiyje) +" where id="+strJiesb_id;
									if (con.getUpdate(sql)>=0){
										Flag=true;
									}else{
										Flag=false;
										break;
									}
								}else{
									Flag=false;
									break;
								}
							}else{
								Flag=false;
								break;
							}
						}
					}
				}
				
				mdrsl.close();
				
			//zld end	
			if(Flag){
				
				con.commit();
				this.setMsg("����ɹ�!");
				this.setParameters("");
			}else{
				
				con.rollBack();
				if (intBZ==1){
					this.setMsg("��ʾ����ֽ����ڿ�ʹ�����!");
				}else if (intBZ==2){
					this.setMsg("��ʾ����ֽ����ڴ˴ν������!");
				}else{
					this.setMsg("����ʧ��!");
				}
			}
		
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
	}
	
	public String CountYfkye(String Yfkb_id,JDBCcon con){
		
		String yue="0";
		String sql="";
		try{
			
			sql="select nvl(jine-yiyje,0) as yue from yufkb,	\n"
				+ "	(select sum(nvl(jine,0)) as yiyje from yufklsb where yufkb_id="+Yfkb_id+")	\n"
				+ " where yufkb.id="+Yfkb_id;
			
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				yue=rs.getString("yue");
			}
			
			rs.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return yue;
	}

	public void getSelectData() {

		JDBCcon con = new JDBCcon();
		String sql_condition="";
		String sql_fenchangb="";
		if(getGongysValue().getId()>-1){
			
			sql_condition=" and gongysb_id="+getGongysValue().getId()+"		\n";
		}
		if(this.getFencbValue().getId()>-1){
			sql_fenchangb=" and j.diancxxb_id="+this.getFencbValue().getId()+" \n";
		}
		
//		�г��ѽ���δ���˵Ľ��㵥
		
		String sql= " (select j.id,j.gongysb_id, j.bianm as bianm,	\n"//getHtmlAlert('"+MainGlobal.getHomeContext(this)+"','Jiesdcz','lx','dianc,'||j.bianm||'',j.bianm)
				+ " jieslx,g.mingc as gongys,j.shoukdw,	\n"
				+ " j.jiessl,j.shuik,j.buhsmk as jiak,j.hansmk as zongje,j.yufkje,j.hansmk-j.yufkje as keyje	\n" 
				+ " from jiesb j,gongysb g									\n"
				+ " where jiesrq>=to_date('"+getRiq1()+"','yyyy-MM-dd')		\n" 
				+ " and jiesrq<=to_date('"+getRiq2()+"','yyyy-MM-dd')		\n"
				+ " and j.gongysb_id=g.id	and nvl(j.hansmk,0)+nvl(j.hansyf,0)>nvl(j.yufkje,0)	\n" 
				+ sql_condition
				+sql_fenchangb
				+ " and ruzrq is null)";										
  
		ResultSetList rsl=con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("yufkb");
//		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight("bodyHeight");
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("gongysb_id").setHidden(true);
		egu.getColumn("bianm").setHeader("������");		
		egu.getColumn("jieslx").setHidden(true);
		egu.getColumn("jieslx").setEditor(null);
		egu.getColumn("gongys").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("shoukdw").setHeader("�տλ");
		egu.getColumn("jiessl").setHeader("��������");
		egu.getColumn("shuik").setHeader("˰��");
		egu.getColumn("jiak").setHeader("�ۿ�");
		egu.getColumn("zongje").setHeader("�����ܽ��");
		egu.getColumn("yufkje").setHeader("�ѳ�ֽ��");
		egu.getColumn("keyje").setHeader("��ʹ�ý��");
		egu.getColumn("bianm").setWidth(150);
		egu.getColumn("jiessl").setWidth(80);
		egu.getColumn("shuik").setWidth(80);
		egu.getColumn("jiak").setWidth(80);
		egu.getColumn("zongje").setWidth(80);
		
//		�����ı���_End
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		egu.addPaging(25);
		
		if(((Visit) getPage().getVisit()).isFencb()){
			
			egu.addTbarText("-");
			egu.addTbarText("�ֳ���");
			ComboBox comb = new ComboBox();
			comb.setTransform("FencbDropDown");
			comb.setId("Fencb");
			comb.setEditable(false);
			comb.setLazyRender(true);// ��̬��
			comb.setWidth(100);
			comb.setReadOnly(true);
			egu.addToolbarItem(comb.getScript());
			//egu.addOtherScript("Fencb.on('select',function(){document.forms[0].submit();});");
		}
		
		egu.addTbarText("-");
		egu.addTbarText("��������:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq1");
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("-");
		egu.addTbarText("��");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		
		// ������λ
		egu.addTbarText("-");
		egu.addTbarText(Locale.gongysb_id_fahb);
		ComboBox comb4 = new ComboBox();
		comb4.setTransform("GongysDropDown");
		comb4.setId("Gongys");
		comb4.setEditable(false);
		comb4.setLazyRender(true);// ��̬��
		comb4.setWidth(160);
		comb4.setReadOnly(true);
		egu.addToolbarItem(comb4.getScript());
//		egu.addOtherScript("Gongys.on('select',function(){document.forms[0].submit();});");

		if(!((Visit) getPage().getVisit()).isFencb()){//�ֳ��𹤾�����ʾ����,���԰ѽ���������
			// ���㵥���
			egu.addTbarText("-");
			egu.addTbarText("���㵥���");
			ComboBox comb1 = new ComboBox();
			comb1.setTransform("JiesdbhDropDown");
			comb1.setId("Jiesdbh");
			comb1.setEditable(false);
			comb1.setLazyRender(true);// ��̬��
			comb1.setWidth(160);
			comb1.setReadOnly(true);
			egu.addToolbarItem(comb1.getScript());
			egu.addOtherScript("Jiesdbh.on('select',function(){document.forms[0].submit();});");
		}
	

		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefurbishButton");
		
		egu.addOtherScript("gridDiv_grid.on('click',function(){										\n" 
						+ "		var rec = gridDiv_sm.getSelected(); 								\n"
						+ "		if(rec==null){														\n"
						+ " 		return;															\n"
						+ " 	}																	\n"	
						+ " 	document.getElementById('Parameters').value=rec.get('ID')+','+rec.get('GONGYSB_ID');	\n"
						+ "��	document.forms[0].submit();});");
		
		setExtGrid(egu);
		
		con.Close();
	}
	
	
	public void getYfkSelectData() {
//		Ԥ����
		JDBCcon con = new JDBCcon();
//		ҳ��ش��Ĳ���
		String condition=this.getParameters();
		String strJiesb_id="0";
		String strGongysb_id="0";
		if(!condition.equals("")){
			
			strJiesb_id=condition.substring(0,condition.indexOf(","));
			strGongysb_id=condition.substring(condition.indexOf(",")+1);
		}
		
		
//		�г��ѽ���δ���˵Ľ��㵥
		
										
		
		String sql="select id,riq,bianh,yue,0 as chongd from yufkb where yue>0 and gongysb_id="+strGongysb_id+" order by riq,yue";
		ResultSetList rsl=con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv_Yfk", rsl);
		egu.setTableName("yufkb");
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("riq").setEditor(null);
		
		egu.getColumn("bianh").setHeader("Ԥ������");	
		egu.getColumn("bianh").setEditor(null);
		

		
		egu.getColumn("yue").setHeader("���");
		egu.getColumn("yue").setEditor(null);
		egu.getColumn("yue").setWidth(80);
		
		egu.getColumn("chongd").setHeader("���");
		egu.getColumn("chongd").setWidth(80);
		((NumberField)egu.getColumn("chongd").editor).setDecimalPrecision(2);

		
//		�����ı���_End
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(3, new GridColumn(GridColumn.ColType_Check));
		egu.addPaging(0);
		
		StringBuffer sbCondition=new StringBuffer();
		sbCondition.append("	var rec=gridDiv_Yfk_grid.getSelectionModel().getSelections();	\n")
				   .append("	for(i=0;i<rec.length;i++){	\n")
				   .append("		if(rec[i].get('yue')<=0){	\n")
				   .append("			Ext.MessageBox.alert('��ʾ��Ϣ','��<'+(i+1)+'>��Ԥ����ʹ�ý��������˶�!');	\n")
				   .append("			return;	\n")
				   .append("		}	\n")
				   .append("	}	\n");
				   
		egu.addToolbarButton("����",GridButton.ButtonType_SubmitSel_condition,"SaveButton",sbCondition.toString());
		 
		setExtGrid_Yfk(egu);
		
		con.Close();
	}
	
	
//	����
	public boolean _Fencbchange = false;
	public IDropDownBean getFencbValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getFencbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setFencbValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean5()){
			
			((Visit) getPage().getVisit()).setboolean3(true);
			
		}
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public IPropertySelectionModel getFencbModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

			getFencbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void setFencbModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getFencbModels() {

			String sql ="select id,mingc from diancxxb d where d.fuid="+
			((Visit) getPage().getVisit()).getDiancxxb_id()+"order by mingc";


			((Visit) getPage().getVisit())
			.setProSelectionModel5(new IDropDownModel(sql, "��ѡ��"));
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}
	
//������λ
	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setGongysValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean1() != value) {
			
			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean1(value);
		}
	}

	public void setGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {

			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public IPropertySelectionModel getGongysModels() {
		
		String sql ="";
		long Diancxxb_id=0;
		
		if(((Visit) getPage().getVisit()).isFencb()){
			
//			�ֳ���
			Diancxxb_id=getFencbValue().getId();
		}else{
			
//			���ֳ���
			Diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
			sql="(select gongysb.id,gongysb.mingc from jiesb,gongysb 			\n"
				+ " where jiesrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')	\n" 
				+ " 	and jiesrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd')	\n"
				+ "     and jiesb.gongysb_id=gongysb.id							\n"	
				+ "     and gongysb.leix=1 										\n"
				+ " 	and ruzrq is null)	\n"             
				+ " union	\n" 
				+ "(select gongysb.id,gongysb.mingc from jiesyfb,gongysb		\n"	 
				+ " where jiesrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')	\n" 
				+ " 	and jiesrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd')	\n"
				+ "     and jiesyfb.gongysb_id=gongysb_id						\n"
				+ "     and gongysb.leix=1 										\n"
				+ " 	and ruzrq is null)	\n"             
				+ " order by mingc  ";
		  
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, "ȫ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
	
	
//	���㵥���
	public IDropDownBean getJiesdbhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getJiesdbhModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setJiesdbhValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean2()!= value) {

			((Visit) getPage().getVisit()).setDropDownBean2(value);
		}
	}

	public void setJiesdbhModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getJiesdbhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {

			getJiesdbhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getJiesdbhModels() {
		
		String sql_condition="";
		
		if(getGongysValue().getId()>-1){
			
			sql_condition=" and gongysb_id="+getGongysValue().getId();
		}
		
		String sql="(select id,bianm from jiesb 	\n"
					+ " where jiesrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')	\n" 
					+ " 	and jiesrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd')	\n"
					+ " 	and ruzrq is null "+sql_condition+")	\n"             
					+ " union	\n" 
				+ "(select id,bianm from jiesyfb	\n"	 
					+ " where jiesrq>=to_date('"+this.getRiq1()+"','yyyy-MM-dd')	\n" 
					+ " 	and jiesrq<=to_date('"+this.getRiq2()+"','yyyy-MM-dd')	\n"
					+ " 	and ruzrq is null "+sql_condition+")	\n"             
				+ " order by bianm  ";
		  
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql,"ȫ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
//	
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
	
	//Ԥ����
	public ExtGridUtil getExtGrid_Yfk() {
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setExtGrid_Yfk(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
	}

	public String getGridScript_Yfk() {
		return getExtGrid_Yfk().getGridScript();
	}

	public String getGridHtml_Yfk() {
		return getExtGrid_Yfk().getHtml();
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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			
			((Visit) this.getPage().getVisit()).setString1("");	//riq1
			((Visit) this.getPage().getVisit()).setString2("");	//riq2
			((Visit) this.getPage().getVisit()).setString3("");	//jiesb_id,gongysb_id  ,Parameters()

			if(visit.isFencb()){
				
				setFencbValue(null);	//DropDownBean5	
				setFencbModel(null);	//ProSelectionModel5
				getFencbModels();
			}
			
			setGongysValue(null);	//DropDownBean1
			setGongysModel(null);	//ProSelectionModel1
			getGongysModels();
			
			setJiesdbhValue(null);	//DropDownBean2
			setJiesdbhModel(null);	//ProSelectionModel2
			getJiesdbhModels();
			
			((Visit) this.getPage().getVisit()).setboolean1(false);		//��Ӧ��
			((Visit) this.getPage().getVisit()).setboolean2(false);		//����
			
			getSelectData();
		}
		
		if(((Visit) this.getPage().getVisit()).getboolean2()){
			
			((Visit) this.getPage().getVisit()).setboolean2(false);
			getGongysModels();
			getJiesdbhModels();
		}
		
		if(((Visit) this.getPage().getVisit()).getboolean1()){
			
//			��Ӧ�̱䶯ʱҪ���Ľ��㵥���
			((Visit) this.getPage().getVisit()).setboolean1(false);
			getJiesdbhModels();
		}
		
		getYfkSelectData();
	}

}