package com.zhiren.dc.huaygl.rucrlrzc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Rucrlrzc extends BasePage implements PageValidateListener {
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	// ҳ��仯��¼
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

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {

		_RefurbishChick = true;
	}
	private boolean _CreateClick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;

			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
		}
	}

	
	public void CreateData() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String riq=this.getRiqi();
		//�볧�����������
		String ruc = "select nvl(sum(round(f.laimsl,0)),0) as rucsl,\n"
				+ "nvl(round_new(decode(sum(f.laimsl),0,0,sum(f.laimsl*z.qnet_ar)/sum(f.laimsl)),2),0) as rucrl,\n"
				+ "nvl(round_new(decode(sum(f.laimsl),0,0,sum(f.laimsl*z.mt)/sum(f.laimsl)),1),0) as rucsf\n"
				+ "from fahb f ,zhilb z\n"
				+ "where f.daohrq=to_date('"+riq+"','yyyy-mm-dd')\n"
				+ "and f.zhilb_id=z.id(+)\n" 
				+ "and f.diancxxb_id="+this.getTreeid()+" \n"
				+ "and z.shenhzt=1";
		ResultSetList rsl = con.getResultSetList(ruc);
		double rucsl=0.0;
		double rucrl=0.0;
		double rucsf=0.0;
		if(rsl.next()){
			rucsl=rsl.getDouble("rucsl");
			rucrl=rsl.getDouble("rucrl");
			rucsf=rsl.getDouble("rucsf");
		}
		//��¯����������
		String rul = "select nvl(sum(hy.fadhy+hy.gongrhy),0) as rulsl,\n"
				+ " nvl(round_new(decode(sum(hy.fadhy+hy.gongrhy),0,0,sum((hy.fadhy+hy.gongrhy)*zl.qnet_ar)/sum(hy.fadhy+hy.gongrhy)),2),0) as rulrl,\n"
				+ " nvl(round_new(decode(sum(hy.fadhy+hy.gongrhy),0,0,sum((hy.fadhy+hy.gongrhy)*zl.mt)/sum(hy.fadhy+hy.gongrhy)),1),0) as rulsf\n"
				+ " from  meihyb hy,rulmzlb zl\n"
				+ " where hy.rulmzlb_id=zl.id(+)\n"
				+ " and hy.rulrq=to_date('"+riq+"','yyyy-mm-dd')\n"
				+ "and hy.diancxxb_id="+this.getTreeid()+"\n "
				+ " and zl.shenhzt=3";

		
		rsl = con.getResultSetList(rul);
		double rulsl=0.0;
		double rulrl=0.0;
		double rulsf=0.0;
		if(rsl.next()){
			rulsl=rsl.getDouble("rulsl");
			rulrl=rsl.getDouble("rulrl");
			rulsf=rsl.getDouble("rulsf");
		}
		
		double rezctzq=rucrl-rulrl;
		double rezctzh=rucrl-rulrl*(100-rucsf)/(100-rulsf);
		String insetRezcb="insert into rezcb (id,riq,diancxxb_id,rucsl,rucrl,rucsf,rulsl,rulrl,rulsf" +
				",rezctzq,rezctzh) values (" +
				""+MainGlobal.getNewID(visit.getDiancxxb_id())+"," +
				" to_date('"+riq+"','yyyy-mm-dd')," +
				" "+this.getTreeid()+"," +
				" "+rucsl+","+rucrl+","+rucsf+","+rulsl+","+rulrl+","+rulsf+","+rezctzq+",round_new("+rezctzh+",2))";
		String deleteRezc="delete rezcb r where r.riq=to_date('"+riq+"','yyyy-mm-dd') and diancxxb_id="+this.getTreeid()+"";
		int DeFlag=con.getDelete(deleteRezc);
		int flag=con.getInsert(insetRezcb);
		if(flag==-1){
			rsl.close();
			con.Close();
			WriteLog.writeErrorLog(this.getClass().getName() + 
					"\nSQL:" + insetRezcb +"����rezcb��ʧ��!");
			setMsg(this.getClass().getName() + ":����rezcb��ʧ��!");
			
		}else{
			rsl.close();
			con.Close();
			this.setMsg(riq+" �������ɳɹ�!");
		}
		
		
	}
	


	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String strdiancTreeID = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strdiancTreeID="";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strdiancTreeID ="and dc.fuid="+ this.getTreeid();
			
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strdiancTreeID=" and dc.id="+this.getTreeid();
			 
		}
		
		String FirstDate=getRiqi();
		if(FirstDate==null||FirstDate.equals("")){
			FirstDate=DateUtil.FormatDate(new Date());
		}
		
		String chaxun =

			"select r.id,r.riq,dc.mingc as diancxxb_id,r.rucsl,r.rucrl,r.rucsf,r.rulsl,\n" +
			"  r.rulrl,r.rulsf,r.rezctzq,round_new(r.rezctzq*1000/4.1816,0) as rezctzqdk,\n" + 
			"  r.rezctzh,round_new(r.rezctzh*1000/4.1816,0) as rezctzhdk,\n" + 
			"  r.beiz,r.chaocfx,r.chaocfxry\n" + 
			" from rezcb  r,diancxxb dc\n" + 
			" where r.riq=to_date('"+FirstDate+"','yyyy-mm-dd')\n" + 
			" and r.diancxxb_id=dc.id\n" + 
			" "+strdiancTreeID+"";

 
		//System.out.println(chaxun);
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);

		egu.getColumn("riq").setHeader("����");
		egu.getColumn("diancxxb_id").setHeader("��λ");
		egu.getColumn("rucsl").setHeader("�볧����<br>(��)");
		egu.getColumn("rucrl").setHeader("�볧����<br>(MJ/kg)");
		egu.getColumn("rucsf").setHeader("�볧ˮ��<br>Mt(%)");
		egu.getColumn("rulsl").setHeader("��¯����<br>(��)");
		egu.getColumn("rulrl").setHeader("��¯����<br>(MJ/kg)");
		egu.getColumn("rulsf").setHeader("��¯ˮ��<br>Mt(%)");
		egu.getColumn("rezctzq").setHeader("ˮ�ֵ���ǰ<br>��ֵ��<br>(MJ/kg)");
		egu.getColumn("rezctzqdk").setHeader("ˮ�ֵ���ǰ<br>ǰ��ֵ��<br>(kcal/kg)");
		egu.getColumn("rezctzh").setHeader("ˮ�ֵ�����<br>��ֵ��<br>(MJ/kg)");
		egu.getColumn("rezctzhdk").setHeader("ˮ�ֵ�����<br>��ֵ��<br>(kcal/kg)");
		egu.getColumn("beiz").setHeader("��ע");
		
		egu.getColumn("rezctzqdk").setUpdate(false);
		egu.getColumn("rezctzhdk").setUpdate(false);
		
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("riq").setSortable(false);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("rezctzq").setEditor(null);
		egu.getColumn("rezctzqdk").setEditor(null);
		egu.getColumn("rezctzh").setEditor(null);
		egu.getColumn("rezctzhdk").setEditor(null);
		
		egu.getColumn("chaocfx").setHidden(true);
		egu.getColumn("chaocfxry").setHidden(true);
		
		//�趨�еĳ�ʼ���
		egu.getColumn("riq").setWidth(80);
		egu.getColumn("diancxxb_id").setWidth(120);
		egu.getColumn("rucsl").setWidth(60);
		egu.getColumn("rucrl").setWidth(65);
		egu.getColumn("rucsf").setWidth(65);
		egu.getColumn("rulsl").setWidth(65);
		egu.getColumn("rulrl").setWidth(65);
		egu.getColumn("rulsf").setWidth(65);
		egu.getColumn("rezctzq").setWidth(70);
		egu.getColumn("rezctzqdk").setWidth(70);
		egu.getColumn("rezctzh").setWidth(70);
		egu.getColumn("rezctzhdk").setWidth(70);
		egu.getColumn("beiz").setWidth(100);
		
		//�趨�е�С��λ
		((NumberField)egu.getColumn("rucrl").editor).setDecimalPrecision(2);
		((NumberField)egu.getColumn("rucsf").editor).setDecimalPrecision(2);
		((NumberField)egu.getColumn("rulrl").editor).setDecimalPrecision(2);
		((NumberField)egu.getColumn("rulsf").editor).setDecimalPrecision(2);
		
//		�趨���ɱ༭�е���ɫ
		egu.getColumn("rezctzq").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("rezctzqdk").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("rezctzh").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("rezctzhdk").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		
		egu.setTableName("rezcb");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(100);
		egu.setWidth(1000);
		egu.setDefaultsortable(false);//�趨ҳ�治�Զ�����
		egu.getColumn("riq").setDataType(GridColumn.DataType_Date);    
		
		
	
		
		// *****************************************����Ĭ��ֵ****************************
		egu.getColumn("riq").setDefaultValue(this.getRiqi());
		//System.out.println(this.getRiqi());
		
		//*************************������*****************************************88
		//�糧������
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select id,mingc from diancxxb order by mingc"));
		
//		 �糧��
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		
		egu.addTbarText("��λ:");
		egu.addTbarTreeBtn("diancTree");
		
		// ������
		egu.addTbarText("����:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");// ���÷ָ���
		

		
		//---------------ҳ��js�ļ��㿪ʼ------------------------------------------
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit',function(e){");
			//������ֵ������ǰ�͵�����
			sb.append("e.record.set('REZCTZQ',Round((parseFloat(e.record.get('RUCRL')==''?0:e.record.get('RUCRL'))-parseFloat(e.record.get('RULRL')==''?0:e.record.get('RULRL'))),2)  );");
			sb.append("e.record.set('REZCTZQDK',Round((parseFloat(e.record.get('RUCRL')==''?0:e.record.get('RUCRL'))-parseFloat(e.record.get('RULRL')==''?0:e.record.get('RULRL')))*1000/4.1816,0)  );");
			sb.append("e.record.set('REZCTZH',Round((parseFloat(e.record.get('RUCRL')==''?0:e.record.get('RUCRL'))-parseFloat(e.record.get('RULRL')==''?0:e.record.get('RULRL'))*(100-parseFloat(e.record.get('RUCSF')==''?0:e.record.get('RUCSF')))/(100-parseFloat(e.record.get('RULSF')==''?0:e.record.get('RULSF')))),2));");
			sb.append("e.record.set('REZCTZHDK',Round((parseFloat(e.record.get('REZCTZH')==''?0:e.record.get('REZCTZH'))*1000/4.1816),0)  );");
			
			
			
		sb.append("});");
//		�趨�ڼ��в��ɱ༭
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		
		sb.append(" if(e.field=='DIANCXXB_ID'){ e.cancel=true;}");//�趨�糧�в��ɱ༭
		sb.append("});");
		
		
		
		
		egu.addOtherScript(sb.toString());
		
		
		//---------------ҳ��js�������--------------------------
		
		egu.addTbarText("-");
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);
		egu.addTbarText("-");// ���÷ָ���
//		 ���ɰ�ť
		GridButton gbc = new GridButton("����",
				getBtnHandlerScript("CreateButton"));
		
		gbc.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbc);
		egu.addTbarText("-");// ���÷ָ���
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarText("-");// ���÷ָ���
		
		egu.addToolbarItem("{"+new GridButton("�������","function(){ " +
				"if(gridDiv_sm.isSelected(7)){ " +
				"Ext.MessageBox.alert('��ʾ','��ѡ��������������ݽ��г������!'); " +
				"return;} " +
				"if(gridDiv_sm.hasSelection()){ " +
				" if(false){ " +
				" document.getElementById('DivMy_opinion').className = 'x-hidden';}" +
				"	window_panel.show(); " +
				"  rec = gridDiv_grid.getSelectionModel().getSelections(); " +
				" document.getElementById('My_opinion').value='';" +
				" document.getElementById('Histry_opinion').value='';" +
				" var strmyp=''; " +
				" for(var i=0;i<rec.length;i++){ " +
				" if(strmyp.substring(rec[i].get('CHAOCFX'))>-1){ " +
				" if(strmyp==''){ strmyp=rec[i].get('CHAOCFX');}else{ strmyp+=','+rec[i].get('CHAOCFX');}}" +
				" var strtmp=rec[i].get('CHAOCFXRY');" +
				" document.getElementById('Histry_opinion').value+=strtmp+'\\n';} document.getElementById('My_opinion').value=strmyp;"+
				" }else{ "+
				" 	Ext.MessageBox.alert('��ʾ','����ѡ���������������!');} "+
				"}").getScript()+"}");
		
		egu.addTbarText("-");// ���÷ָ���
		
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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			this.setTreeid(null);
			this.setRiqi(null);
			this.setRiqi(DateUtil.FormatDate(new Date()));
			this.setMsg("");
			//getSelectData();
		}
		
			getSelectData();
		
	}


	boolean treechange=false;
	private String treeid;
//	public String getTreeid() {
//		if(treeid==null||treeid.equals("")){
//			treeid=String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
//		}
//		return treeid;
//	}
//	public void setTreeid(String treeid) {
//		this.treeid = treeid;
//		treechange=true;
//	}
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

	//���ڿؼ�
	boolean riqichange=false;
	private String riqi;
	public String getRiqi() {
		if(riqi==null||riqi.equals("")){
			riqi=DateUtil.FormatDate(new Date());
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		
		if(this.riqi!=null &&!this.riqi.equals(riqi)){
			this.riqi = riqi;
			riqichange=true;
		}
		
		
	}
	//�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
	
	
//	�������
	public void setMy_opinion(String value){
		
		((Visit) getPage().getVisit()).setString1(value);
	}
	
	public String getMy_opinion(){
		return ((Visit) getPage().getVisit()).getString1();
	}
	
	//���������Ա
	public void setHistry_opinion(String value){
		
		((Visit) getPage().getVisit()).setString2(value);
	}
	
	public String getHistry_opinion(){
		return ((Visit) getPage().getVisit()).getString2();
	}
	

	public String getQuedButtonScript() {
		//Ϊ����������ȷ����ť���js����
		return "for(i=0;i<=res.getCount()-1;i++){"
				+ " resa=res.getAt(i);"
				+ " resa.set('CHAOCFX',document.getElementById('My_opinion').value);"
				+ " resa.set('CHAOCFXRY',document.getElementById('Histry_opinion').value);"
				+ " }";
	}
	public String getBtnHandlerScript(String btnName) {
		// ��ť��script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = this.getRiqi();
		btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
		if (btnName.endsWith("CreateButton")) {
			btnsb.append("���������ݽ�����").append(cnDate).append("���Ѵ����ݣ��Ƿ������");
		} 
		btnsb.append("',function(btn){if(btn == 'yes'){").append(
				"document.getElementById('").append(btnName).append(
				"').click()").append("}; // end if \n").append("});}");
		return btnsb.toString();
	}
	
	  //frontpage�����������м�Ȩ������js���� 
	/*function countRucrl(){ 
		  var slsum = 0;
		  var  slrlsum = 0;
		  for(i=0;i<7;i++){
			  reci = gridDiv_ds.getAt(i);
			  sl =eval(reci.get('RUCSL')||0); 
			  rl = eval(reci.get('RUCRL')||0);
			  if(sl==0||rl==0){ 
				  continue; 
			  }
			  slrlsum += sl*rl;
			  slsum += sl; 
		  }
		  rectotal =gridDiv_ds.getAt(7); 
		  if(slsum != 0){ 
			  rectotal.set('RUCRL',Round(slrlsum /slsum,2) );
		  } 
	
	  
		  if(e.field=='RUCRL'){
			  countRucrl();
		  }
	 }*/
	 
	
}
