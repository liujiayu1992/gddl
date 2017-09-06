package com.zhiren.jt.jihgl.niancgjh;

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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public  class Niancgjhext extends BasePage implements PageValidateListener {
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
		setTbmsg(null);
	}

	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
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
		
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		int flag =0;
		
		ResultSetList rsl;
		rsl=visit.getExtGrid1().getModifyResultSet(getChange());

		double  htl=0;
		String strDate="";
		String strYear="";
		String strYearEnd="";
		//������»�����
		while (rsl.next()){
			strYear=getNianfValue().getValue() +"-01-01";
			strYearEnd=getNianfValue().getValue() +"-12-01";
			String aa ="delete from niancgjhb " +
			"where (riq>=to_date('"+strYear +"','yyyy-mm-dd') and riq<=to_date('"+strYearEnd +"','yyyy-mm-dd'))" +
			"and gongysb_id=getGongysId('"+rsl.getString("gongysmc")+"') " +
			"and diancxxb_id=getDiancId('"+ rsl.getString("mingc")+"')";
			flag=con.getDelete(aa);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
						+ aa);
				setMsg(ErrorMessage.DeleteDatabaseFail);
				return;
			}
			
			for (int i=1;i<=12;i++){
				htl=rsl.getDouble("Y"+i);
				if (htl!=0){
					strDate=getNianfValue().getValue() +"-"+ i +"-01";
					String bb ="insert into niancgjhb(id,gongysb_id,diancxxb_id,hej,riq) values(" +
					"getNewId(getDiancId('"+ rsl.getString("mingc")+"')),"+
					"getGongysId('"+rsl.getString("gongysmc")+"'),"+
					"getDiancId('"+ rsl.getString("mingc")+"'),"+
					htl+"," +
					"to_date('"+strDate+"','yyyy-mm-dd'))";
					con.getInsert(bb);
					if (flag == -1) {
						con.rollBack();
						con.Close();
						WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
								+ bb);
						setMsg(ErrorMessage.InsertDatabaseFail);
						return;
					}
					
				}
			}
		}
		//ɾ������
		rsl=visit.getExtGrid1().getDeleteResultSet(getChange());
		while (rsl.next()){
			//System.out.println("����ʱ����ɾ��");
			strYear=getNianfValue().getValue() +"-01-01";
			strYearEnd=getNianfValue().getValue() +"-12-01";
			String cc ="delete from niancgjhb " +
			"where (riq>=to_date('"+strYear +"','yyyy-mm-dd') and riq<=to_date('"+strYearEnd +"','yyyy-mm-dd'))" +
			"and gongysb_id=getGongysId('"+rsl.getString("gongysmc")+"') " +
			"and diancxxb_id=getDiancId('"+ rsl.getString("mingc")+"')";
			con.getDelete(cc);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
						+ cc);
				setMsg(ErrorMessage.DeleteDatabaseFail);
				return;
			}
			
		}
		if (flag!=-1){
			setMsg(ErrorMessage.SaveSuccessMessage);
		}
		con.commit();
		con.Close();
		
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _CopyButton = false;
	public void CopyButton(IRequestCycle cycle) {
		_CopyButton = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if(_CopyButton){
			_CopyButton=false;
			CoypLastYearData();
		}
	}
	
	public void CoypLastYearData(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		// ����������ݺ��·�������
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		intyear=intyear-1;
		//��Ӧ������
		long gongysID=this.getMeikdqmcValue().getId();
		
		//
		String strdiancTreeID = "";
		
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strdiancTreeID="";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strdiancTreeID = " and d.fuid= " +this.getTreeid();
			
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strdiancTreeID=" and d.id= " +this.getTreeid();
			
		}
		
		String copyData = "select n.*\n"
				+ "  from niancgjhb n, diancxxb d,gongysb g\n"
				+ " where n.diancxxb_id = d.id(+)\n"
				+ "   and n.gongysb_id=g.id(+)\n"
				+ "   "+strdiancTreeID+"\n"
				+ "   and g.id="+gongysID+"\n" 
				+ "   and to_char(n.riq, 'yyyy') = '"+intyear+"'";

		//System.out.println("����ȥ�������:"+copyData);
		ResultSetList rslcopy = con.getResultSetList(copyData);
		while(rslcopy.next()){
			
			long gongysb_id=rslcopy.getLong("gongysb_id");
			long diancxxb_id=rslcopy.getLong("diancxxb_id");
			double hej=rslcopy.getDouble("hej");
			Date riq=rslcopy.getDate("riq");
			int year=DateUtil.getYear(riq);
			int yue=DateUtil.getMonth(riq);
			int day=DateUtil.getDay(riq);
			
			String strriq=year+1+"-"+yue+"-"+day;
			String _id = MainGlobal.getNewID(((Visit) getPage()
					.getVisit()).getDiancxxb_id());
			con.getInsert("insert into niancgjhb(id,gongysb_id,diancxxb_id,hej,riq) values(" +
					_id+","+gongysb_id +","+ diancxxb_id+","+hej+","+"to_date('"+strriq+"','yyyy-mm-dd'))");
					
		}
		
		con.Close();
		
		
	}


	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		// ����������ݺ��·�������
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		
		//��Ӧ������
		long gongysID=this.getMeikdqmcValue().getId();
		
		//
		String strdiancTreeID = "";
		String strdiancTreeID2="";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strdiancTreeID="";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strdiancTreeID = " and dc.fuid= " +this.getTreeid();
			strdiancTreeID2=" and fuid=" +this.getTreeid();
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strdiancTreeID=" and dc.id= " +this.getTreeid();
			strdiancTreeID2=" and id=" +this.getTreeid();
		}
		String chaxun = "select dcmk.diancid as id,dcmk.gongysmc as gongysmc,dcmk.mingc,\n"
				+ "  sum(decode(to_char(riq,'mm'),'01',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'02',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'03',nvl( n.hej,0),0)+\n"
				+ "  decode(to_char(riq,'mm'),'04',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'05',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'06',nvl( n.hej,0),0)+\n"
				+ "  decode(to_char(riq,'mm'),'07',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'08',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'09',nvl( n.hej,0),0)+\n"
				+ "  decode(to_char(riq,'mm'),'10',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'11',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'12',nvl( n.hej,0),0)) as quann,\n"
				+ "  sum(decode(to_char(riq,'mm'),'01',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'02',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'03',nvl( n.hej,0),0)) as j1,\n"
				+ "  sum(decode(to_char(riq,'mm'),'01',nvl( n.hej,0),0)) as y1,\n"
				+ "  sum(decode(to_char(riq,'mm'),'02',nvl( n.hej,0),0)) as y2,\n"
				+ "  sum(decode(to_char(riq,'mm'),'03',nvl( n.hej,0),0)) as y3,\n"
				+ "  sum(decode(to_char(riq,'mm'),'04',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'05',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'06',nvl( n.hej,0),0)) as j2,\n"
				+ "  sum(decode(to_char(riq,'mm'),'04',nvl( n.hej,0),0)) as y4,\n"
				+ "  sum(decode(to_char(riq,'mm'),'05',nvl( n.hej,0),0)) as y5,\n"
				+ "  sum(decode(to_char(riq,'mm'),'06',nvl( n.hej,0),0)) as y6,\n"
				+ "  sum(decode(to_char(riq,'mm'),'07',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'08',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'09',nvl( n.hej,0),0)) as j3,\n"
				+ "  sum(decode(to_char(riq,'mm'),'07',nvl( n.hej,0),0)) as y7,\n"
				+ "  sum(decode(to_char(riq,'mm'),'08',nvl( n.hej,0),0)) as y8,\n"
				+ "  sum(decode(to_char(riq,'mm'),'09',nvl( n.hej,0),0)) as y9,\n"
				+ "  sum(decode(to_char(riq,'mm'),'10',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'11',nvl( n.hej,0),0)+decode(to_char(riq,'mm'),'12',nvl( n.hej,0),0)) as j4,\n"
				+ "  sum(decode(to_char(riq,'mm'),'10',nvl( n.hej,0),0)) as y10,\n"
				+ "  sum(decode(to_char(riq,'mm'),'11',nvl( n.hej,0),0)) as y11,\n"
				+ "  sum(decode(to_char(riq,'mm'),'12',nvl( n.hej,0),0)) as y12\n"
				+ " from\n"
				+ " (select dc.mingc,dc.id as diancid,dq.mingc as gongysmc,dq.id  as diqid\n"
				+ " from (select id,mingc from diancxxb  where jib=3 "+strdiancTreeID2+") dc,\n"
				+ "      (select id,mingc from gongysb where id="+gongysID+") dq) dcmk,\n"
				+ " (select riq, hej,g.id as diqid,dc.id as diancid\n"
				+ "         from niancgjhb n, gongysb g, diancxxb dc\n"
				+ "         where n.gongysb_id = g.id\n"
				+ "               and n.diancxxb_id = dc.id\n"
				+ "               "+strdiancTreeID+"\n"
				+ "               and to_char(n.riq, 'yyyy') = '"+intyear+"'\n"
				+ "               and gongysb_id = "+gongysID+") n\n"
				+ "  where dcmk.diancid=n.diancid(+)\n"
				+ "        and dcmk.diqid=n.diqid(+)\n"
				+ "  group by dcmk.diancid,dcmk.gongysmc ,dcmk.mingc"
				+ "  order by dcmk.diancid";
	
		
		//System.out.println(chaxun);
		//System.out.println("----------------------------------------");
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("niancgjhb");
		egu.setWidth("bodyWidth");
		egu.getColumn("id").setHidden(true);

		egu.getColumn("gongysmc").setHeader("��Ӧ������");
		egu.getColumn("mingc").setHeader("��λ");
		egu.getColumn("quann").setHeader("ȫ��ϼ�");
		egu.getColumn("j1").setHeader("һ����");
		egu.getColumn("y1").setHeader("һ��");
		egu.getColumn("y2").setHeader("����");
		egu.getColumn("y3").setHeader("����");
		egu.getColumn("j2").setHeader("������");
		egu.getColumn("y4").setHeader("����");
		egu.getColumn("y5").setHeader("����");
		egu.getColumn("y6").setHeader("����");
		egu.getColumn("j3").setHeader("������");
		egu.getColumn("y7").setHeader("����");
		egu.getColumn("y8").setHeader("����");
		egu.getColumn("y9").setHeader("����");
		egu.getColumn("j4").setHeader("�ļ���");
		egu.getColumn("y10").setHeader("ʮ��");
		egu.getColumn("y11").setHeader("ʮһ��");
		egu.getColumn("y12").setHeader("ʮ����");
		
		egu.getColumn("quann").setEditor(null);
		egu.getColumn("j1").setEditor(null);
		egu.getColumn("j2").setEditor(null);
		egu.getColumn("j3").setEditor(null);
		egu.getColumn("j4").setEditor(null);
		
		egu.getColumn("j1").setHidden(true);
		egu.getColumn("j2").setHidden(true);
		egu.getColumn("j3").setHidden(true);
		egu.getColumn("j4").setHidden(true);
		//ѭ���趨�еĿ��,���趨С��λ��
		for( int i=1;i<=12;i++){
			egu.getColumn("y"+i).setWidth(45);
			((NumberField)egu.getColumn("y"+i).editor).setDecimalPrecision(2);
		}
		egu.getColumn("j1").setWidth(50);
		egu.getColumn("j2").setWidth(50);
		egu.getColumn("j3").setWidth(50);
		egu.getColumn("j4").setWidth(50);
		
//		�趨���ɱ༭�е���ɫ
		egu.getColumn("j1").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("j2").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("j3").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("j4").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(23);// ���÷�ҳ

		// *****************************************����Ĭ��ֵ****************************
		
		egu.getColumn("mingc").setDefaultValue(
				this.getIDropDownDiancmc(this.getTreeid()));
		// ���ù�Ӧ�̵�Ĭ��ֵ
		egu.getColumn("gongysmc").setDefaultValue(
				this.getMeikdqmcValue().getValue());
		// *************************������*****************************************88
		// �糧������
		egu.getColumn("mingc").setEditor(new ComboBox());
		egu.getColumn("mingc").setComboEditor(egu.gridId,new IDropDownModel("select id,mingc from diancxxb order by mingc"));
		// ���ù�Ӧ�̵�������
		egu.getColumn("gongysmc").setEditor(new ComboBox());
		String GongysSql = "select id,mingc from gongysb where leix=1 and zhuangt=1 order by mingc";
		egu.getColumn("gongysmc").setComboEditor(egu.gridId,new IDropDownModel(GongysSql));

	

		// ********************������************************************************
		egu.addTbarText("���:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		// ������
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), this.getTreeid());
		setTree(etu);
		egu.addTbarText("-");// ���÷ָ���
		
		egu.addTbarText("��λ:");
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		egu.addTbarText("��Ӧ��:");
		ComboBox comb3 = new ComboBox();
		comb3.setTransform("MeikmcDropDown");
		comb3.setId("gongys");
		comb3.setEditable(true);
		comb3.setLazyRender(true);// ��̬��
		comb3.setWidth(130);
		egu.addToolbarItem(comb3.getScript());

		// �趨�������������Զ�ˢ��
		egu.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});gongys.on('select',function(){document.forms[0].submit();});");
		//egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addToolbarItem("{"+new GridButton("����ȥ��ƻ�","function(){document.getElementById('CopyButton').click();}").getScript()+"}");
		egu.addTbarText("->");
		egu.addTbarText("<font color=\"#EE0000\">��λ:���</font>");
		
		//---------------ҳ��js�ļ��㿪ʼ------------------------------------------
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit',function(e){");
			
			sb.append("e.record.set('J1',parseFloat(e.record.get('Y1')==''?0:e.record.get('Y1'))+parseFloat(e.record.get('Y2')==''?0:e.record.get('Y2'))+parseFloat(e.record.get('Y3')==''?0:e.record.get('Y3')));");
			sb.append("e.record.set('J2',parseFloat(e.record.get('Y4')==''?0:e.record.get('Y4'))+parseFloat(e.record.get('Y5')==''?0:e.record.get('Y5'))+parseFloat(e.record.get('Y6')==''?0:e.record.get('Y6')));");
			sb.append("e.record.set('J3',parseFloat(e.record.get('Y7')==''?0:e.record.get('Y7'))+parseFloat(e.record.get('Y8')==''?0:e.record.get('Y8'))+parseFloat(e.record.get('Y9')==''?0:e.record.get('Y9')));");
			sb.append("e.record.set('J4',parseFloat(e.record.get('Y10')==''?0:e.record.get('Y10'))+parseFloat(e.record.get('Y11')==''?0:e.record.get('Y11'))+parseFloat(e.record.get('Y12')==''?0:e.record.get('Y12')));");
			sb.append("e.record.set('QUANN',parseFloat(e.record.get('J1')==''?0:e.record.get('J1'))+parseFloat(e.record.get('J2')==''?0:e.record.get('J2'))+parseFloat(e.record.get('J3')==''?0:e.record.get('J3'))+parseFloat(e.record.get('J4')==''?0:e.record.get('J4')));");
			
		sb.append("});");
		
		
		
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		//sb.append("if (e.row==7){e.cancel=true;}");//�趨��8�в��ɱ༭,�����Ǵ�0��ʼ��
		sb.append(" if(e.field=='GONGYSMC'){ e.cancel=true;}");//�趨��Ӧ���в��ɱ༭
		sb.append(" if(e.field=='MINGC'){ e.cancel=true;}");//�趨�糧��Ϣ�в��ɱ༭
		sb.append("});");
		
		egu.addOtherScript(sb.toString());
		//---------------ҳ��js�������--------------------------
		

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
			this.setNianfValue(null);
			this.getNianfModels();
			this.setTreeid(null);
			setMeikdqmcValue(null);
			getIMeikdqmcModels();
			//getSelectData();
		}
		//if(nianfchanged||_meikdqmcchange){
			getSelectData();
		//}
		
	}
//	 ���
	private static IPropertySelectionModel _NianfModel;
	
	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	
//	 �������
	public boolean _meikdqmcchange = false;

	private IDropDownBean _MeikdqmcValue;

	public IDropDownBean getMeikdqmcValue() {
		if (_MeikdqmcValue == null) {
			_MeikdqmcValue = (IDropDownBean) getIMeikdqmcModels().getOption(0);
		}
		return _MeikdqmcValue;
	}

	public void setMeikdqmcValue(IDropDownBean Value) {
		long id = -2;
		if (_MeikdqmcValue != null) {
			id = _MeikdqmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_meikdqmcchange = true;
			} else {
				_meikdqmcchange = false;
			}
		}
		_MeikdqmcValue = Value;
	}

	private IPropertySelectionModel _IMeikdqmcModel;

	public void setIMeikdqmcModel(IPropertySelectionModel value) {
		_IMeikdqmcModel = value;
	}

	public IPropertySelectionModel getIMeikdqmcModel() {
		if (_IMeikdqmcModel == null) {
			getIMeikdqmcModels();
		}
		return _IMeikdqmcModel;
	}

	public IPropertySelectionModel getIMeikdqmcModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select id,mingc from gongysb  where leix=1 and zhuangt=1 order by mingc";
			_IMeikdqmcModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IMeikdqmcModel;
	}
//	 �õ���½�û����ڵ糧���߷ֹ�˾������
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		cn.Close();
		return diancmc;

	}
	//�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getIDropDownDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

	}
	
	//�õ��糧��Ĭ�ϵ�վ
	public String getDiancDaoz(){
		String daoz = "";
		String treeid=this.getTreeid();
		if(treeid==null||treeid.equals("")){
			treeid="1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = "+treeid+"");

			ResultSet rs = con.getResultSet(sql.toString());
			
			while (rs.next()) {
				 daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
		return daoz;
	}
	
	
	private String treeid;
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
//	�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
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
	
}
