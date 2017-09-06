package com.zhiren.jt.meiysgl.rezcb.rezcb;

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
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
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

public class Rezcext extends BasePage implements PageValidateListener {
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}

	private Date WeekFirstDate(Date date) {// �õ���ǰ����������
		String week = "";
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		cal.setTime(date);
		int weekDayIndex = cal.get(Calendar.DAY_OF_WEEK);

		if (weekDayIndex >= 4) {
			cal.add(Calendar.DAY_OF_MONTH, -(weekDayIndex - 4));
		} else {
			cal.add(Calendar.DAY_OF_MONTH, -(weekDayIndex + 3));
		}
		return cal.getTime();
	}

	private Date WeekLastDate(Date date) {// �õ���ǰ����������
		String week = "";
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		cal.setTime(date);
		int weekDayIndex = cal.get(Calendar.DAY_OF_WEEK);

		if (weekDayIndex >= 4) {
			cal.add(Calendar.DAY_OF_MONTH, -(weekDayIndex - 4));
		} else {
			cal.add(Calendar.DAY_OF_MONTH, -(weekDayIndex + 3));
		}
		cal.add(Calendar.DAY_OF_MONTH, 6);
		return cal.getTime();
	}

	
	private boolean hasData(){
		JDBCcon con = new JDBCcon();
		boolean blnHasData=false;
		String strRiQ=getRiqi();
		String strWeekFirstDate=DateUtil.FormatDate(WeekFirstDate(DateUtil.getDate(strRiQ)));
		String strWeekLastDate=DateUtil.FormatDate(WeekLastDate(DateUtil.getDate(strRiQ)));
		
		ResultSetList rsl = con.getResultSetList("select id from rezcb where diancxxb_id=" +this.getTreeid() +
				" and riq>=to_date('"+strWeekFirstDate+"','yyyy-mm-dd')"+
				" and riq<=to_date('"+strWeekLastDate+"','yyyy-mm-dd')");

		if (rsl.next()){
			blnHasData=true;
		}
		
		return blnHasData;
	}
	
	private String getSelectDiancName( ){
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select mingc from diancxxb where id="+this.getTreeid());
		if (rsl.next()){
			return rsl.getString("mingc");
		}
		return "";
	}
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String riqTiaoj=this.getRiqi();
		if(riqTiaoj==null||riqTiaoj.equals("")){
			riqTiaoj=DateUtil.FormatDate(new Date());
			
		}
		String strdiancTreeID = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strdiancTreeID=this.getTreeid();;
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strdiancTreeID = this.getTreeid();
			
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strdiancTreeID=this.getTreeid();
			 
		}
		
		String strWeekFirstDate=DateUtil.FormatDate(WeekFirstDate(DateUtil.getDate(getRiqi())));
		String strWeekLastDate=DateUtil.FormatDate(WeekLastDate(DateUtil.getDate(getRiqi())));
		String riqWhere=" and r.riq>=to_date('"+strWeekFirstDate+"','yyyy-mm-dd')"+
						" and r.riq<=to_date('"+strWeekLastDate+"','yyyy-mm-dd')";
		String chaxun =
			"select nvl(rz.id,0) as id,\n"
				+ "       decode(grouping(dcriq.riq), 1, '�ϼ�', to_char(dcriq.riq, 'yyyy-mm-dd')) as riq,\n"
				+ "       dcriq.mingc as diancxxb_id,\n"
				+ "       nvl(sum(rz.rucsl),0) as rucsl,\n"
				+ "       nvl(decode(sum(rz.rucsl), 0,0,round(sum(rz.rucsl * rz.rucrl) / sum(rz.rucsl), 2)),0) as rucrl,\n"
				+ "       nvl(decode(sum(rz.rucsl),0,0,round(sum(rz.rucsl * rz.rucsf) / sum(rz.rucsl), 2)),0) as rucsf,\n"
				+ "       nvl(sum(rz.rulsl),0) as rulsl,\n"
				+ "       nvl(decode(sum(rz.rulsl), 0,0,round(sum(rz.rulsl * rz.rulrl) / sum(rz.rulsl), 2)),0) as rulrl,\n"
				+ "       nvl(decode(sum(rz.rulsl),0,0,round(sum(rz.rulsl * rz.rulsf) / sum(rz.rulsl), 2)),0) as rulsf,\n"
				+ "       nvl((decode(sum(rz.rucsl),0,0,round(sum(rz.rucsl * rz.rucrl) / sum(rz.rucsl), 2)) -\n"
				+ "           decode(sum(rz.rulsl), 0,0,round(sum(rz.rulsl * rz.rulrl) / sum(rz.rulsl), 2))),0) as rezctzq,\n"
				+ "       nvl((round((decode(sum(rz.rucsl),0,0,round(sum(rz.rucsl * rz.rucrl) / sum(rz.rucsl), 2)) -\n"
				+ "             decode(sum(rz.rulsl),0, 0,round(sum(rz.rulsl * rz.rulrl) / sum(rz.rulsl), 2))) * 1000 /4.1816,0)),0) as rezctzqdk,\n"
				+ "       nvl((ROUND(decode(sum(rz.rucsl),0,0,round(sum(rz.rucsl * rz.rucrl) / sum(rz.rucsl), 2)) -\n"
				+ "             decode(sum(rz.rulsl), 0,0,round(sum(rz.rulsl * rz.rulrl) / sum(rz.rulsl), 2)) *\n"
				+ "             (100 -decode(sum(rz.rucsl), 0,0,round(sum(rz.rucsl * rz.rucsf) / sum(rz.rucsl), 2))) /\n"
				+ "             (100 -decode(sum(rz.rulsl),0, 0,round(sum(rz.rulsl * rz.rulsf) / sum(rz.rulsl), 2))),2)),0) as rezctzh,\n"
				+ "      nvl(( ROUND(ROUND(decode(sum(rz.rucsl), 0, 0,round(sum(rz.rucsl * rz.rucrl) / sum(rz.rucsl), 2)) -\n"
				+ "                   decode(sum(rz.rulsl), 0,0, round(sum(rz.rulsl * rz.rulrl) / sum(rz.rulsl), 2)) *\n"
				+ "                   (100 -decode(sum(rz.rucsl),0,0,round(sum(rz.rucsl * rz.rucsf) / sum(rz.rucsl), 2))) /\n"
				+ "                   (100 -decode(sum(rz.rulsl),0, 0, round(sum(rz.rulsl * rz.rulsf) / sum(rz.rulsl), 2))), 2) * 1000 / 4.1816,0)),0) as rezctzhdk,\n"
				+ "       rz.beiz ,rz.chaocfx,rz.chaocfxry  from\n"
				+ "  (select rq.riq,d.id as diancxxb_id,d.mingc from\n"
				+ "    (select rownum as id,to_date('"+strWeekFirstDate+"','yyyy-mm-dd')+ rownum-1  as riq from all_objects   where rownum<=7) rq,\n"
				+ "    ( select id ,mingc from diancxxb where id="+strdiancTreeID+")  d) dcriq,\n"
				+ " ( select d.id as diancid, d.mingc,r.id,r.riq, r.rucsl,r.rucrl,r.rucsf,r.rulsl,r.rulrl,r.rulsf,r.rezctzq,r.rezctzh,r.beiz,r.chaocfx,r.chaocfxry\n"
				+ "     from rezcb r, diancxxb d\n"
				+ "    where r.diancxxb_id = d.id\n"
				+ "      and r.diancxxb_id ="+strdiancTreeID+"\n"
				+ "      "+ riqWhere+") rz\n"
				+ " where dcriq.riq=rz.riq(+)\n"
				+ "   and dcriq.diancxxb_id=rz.diancid(+)\n"
				+ "   group by rollup((rz.id,dcriq.riq, dcriq.mingc, rz.beiz,rz.chaocfx,rz.chaocfxry))\n"
				+ "   order by dcriq.riq";
 
		//System.out.println(chaxun);
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);

		egu.getColumn("riq").setHeader("����");
		egu.getColumn("diancxxb_id").setHeader("��λ");
		egu.getColumn("rucsl").setHeader("�볧<br>����(t)");
		egu.getColumn("rucrl").setHeader("�볧����<br>(MJ/kg)");
		egu.getColumn("rucsf").setHeader("�볧ˮ��<br>Mt(%)");
		egu.getColumn("rulsl").setHeader("��¯<br>����(t)");
		egu.getColumn("rulrl").setHeader("��¯����<br>(MJ/kg)");
		egu.getColumn("rulsf").setHeader("��¯ˮ��<br>Mt(%)");
		egu.getColumn("rezctzq").setHeader("ˮ�ֵ���<br>ǰ��ֵ��<br>(MJ/kg)");
		egu.getColumn("rezctzqdk").setHeader("ˮ�ֵ���<br>ǰ��<br>(��/����)");
		egu.getColumn("rezctzh").setHeader("ˮ�ֵ���<br>����ֵ��<br>(MJ/kg)");
		egu.getColumn("rezctzhdk").setHeader("ˮ�ֵ���<br>���<br>(��/����)");
		
		
		egu.getColumn("rezctzqdk").setUpdate(false);
		egu.getColumn("rezctzhdk").setUpdate(false);
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("riq").setSortable(false);
		
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
		
		
	
		/*Date datDateFirst=WeekFirstDate(DateUtil.getDate(getRiqi()));
		String strDiancName=getSelectDiancName();
		if (!hasData()){
			String[][] data=new String[8][13];
			for (int i=0;i<8;i++){
				data[i][0]="0";
				data[i][1]=DateUtil.FormatDate(DateUtil.AddDate(datDateFirst, i, DateUtil.AddType_intDay));
				data[i][2]=strDiancName;
				for (int j=3;j<12;j++){
					data[i][j]="0";
				}
				data[i][12]="";
			}
			data[7][1]="�ϼ�";
			egu.setData(data);
		}*/
		
		
		// *****************************************����Ĭ��ֵ****************************
		egu.getColumn("riq").setDefaultValue(this.getRiqi());
		//System.out.println(this.getRiqi());
		
		//*************************������*****************************************88
		//�糧������
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select id,mingc from diancxxb order by mingc"));
		
		
		
		// ������
		egu.addTbarText("����:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");// ���÷ָ���
		// �糧��
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		
		egu.addTbarText("��λ:");
		egu.addTbarTreeBtn("diancTree");

		
		//---------------ҳ��js�ļ��㿪ʼ------------------------------------------
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit',function(e){");
			//������ֵ������ǰ�͵�����
			sb.append("e.record.set('REZCTZQ',Round((parseFloat(e.record.get('RUCRL')==''?0:e.record.get('RUCRL'))-parseFloat(e.record.get('RULRL')==''?0:e.record.get('RULRL'))),2)  );");
			sb.append("e.record.set('REZCTZQDK',Round((parseFloat(e.record.get('RUCRL')==''?0:e.record.get('RUCRL'))-parseFloat(e.record.get('RULRL')==''?0:e.record.get('RULRL')))*1000/4.1816,0)  );");
			sb.append("e.record.set('REZCTZH',Round((parseFloat(e.record.get('RUCRL')==''?0:e.record.get('RUCRL'))-parseFloat(e.record.get('RULRL')==''?0:e.record.get('RULRL'))*(100-parseFloat(e.record.get('RUCSF')==''?0:e.record.get('RUCSF')))/(100-parseFloat(e.record.get('RULSF')==''?0:e.record.get('RULSF')))),2));");
			sb.append("e.record.set('REZCTZHDK',Round((parseFloat(e.record.get('REZCTZH')==''?0:e.record.get('REZCTZH'))*1000/4.1816),0)  );");
			//����������볧����
			sb.append("rec = gridDiv_ds.getAt(7); if(e.field=='RUCSL'){ rec.set('RUCSL', eval(rec.get('RUCSL')||0) + eval(e.value||0) - eval(e.originalValue||0)); countRucrl();countRucsf();}");
			//�����������¯����
			sb.append(" if(e.field=='RULSL'){ rec.set('RULSL', eval(rec.get('RULSL')||0) + eval(e.value||0) - eval(e.originalValue||0));countRulrl();countRulsf();}");
			
			sb.append("if(e.field=='RUCRL'){ countRucrl();}");//��Ȩ��������е��볧����
			//��Ȩ�����볧�����ķ���,��ҳ������и�ʽ���Ĵ�js����
			sb.append("function countRucrl(){").append("var slsum = 0;").append("var slrlsum = 0;").append("for(i=0;i<7;i++){").append("reci = gridDiv_ds.getAt(i);");
			sb.append("sl = eval(reci.get('RUCSL')||0);").append("rl = eval(reci.get('RUCRL')||0);").append("if(sl==0||rl==0){").append("continue;").append("}");
			sb.append("slrlsum += sl*rl;").append("slsum += sl;").append("}").append("rectotal = gridDiv_ds.getAt(7);").append("if(slsum != 0){");
			sb.append("rectotal.set('RUCRL',Round(slrlsum / slsum,2) );").append("}").append("};");
			
			
			sb.append("if(e.field=='RUCSF'){ countRucsf();}");//��Ȩ��������е��볧ˮ��
			sb.append("function countRucsf(){").append("var slsum = 0;").append("var slsfsum = 0;").append("for(i=0;i<7;i++){").append("reci = gridDiv_ds.getAt(i);").append("sl = eval(reci.get('RUCSL')||0);").append("sf = eval(reci.get('RUCSF')||0);").append("if(sl==0||sf==0){").append("continue;");
			sb.append("}").append("slsfsum += sl*sf;").append("slsum += sl;").append("}").append("rectotal = gridDiv_ds.getAt(7);").append("if(slsum != 0){").append("rectotal.set('RUCSF',Round(slsfsum / slsum,2) );").append("}").append("};");
			

			sb.append("if(e.field=='RULRL'){ countRulrl();}");//��Ȩ��������е���¯����
			sb.append("function countRulrl(){").append("var slsum = 0;").append("var slrlsum = 0;").append("for(i=0;i<7;i++){").append("reci = gridDiv_ds.getAt(i);").append("sl = eval(reci.get('RULSL')||0);").append("rl = eval(reci.get('RULRL')||0);").append("if(sl==0||rl==0){");
			sb.append("continue;").append("}").append("slrlsum += sl*rl;").append("slsum += sl;").append("}").append("rectotal = gridDiv_ds.getAt(7);").append("if(slsum != 0){").append("rectotal.set('RULRL',Round(slrlsum / slsum,2) );");
			sb.append("}").append("};");
			
			
			sb.append("if(e.field=='RULSF'){ countRulsf();}");//��Ȩ��������е���¯ˮ��
			sb.append("function countRulsf(){").append("var slsum = 0;").append("var slsfsum = 0;").append("for(i=0;i<7;i++){").append("reci = gridDiv_ds.getAt(i);").append("sl = eval(reci.get('RULSL')||0);").append("sf = eval(reci.get('RULSF')||0);").append("if(sl==0||sf==0){").append("continue;");
			sb.append("}").append("slsfsum += sl*sf;").append("slsum += sl;").append("}").append("rectotal = gridDiv_ds.getAt(7);").append("if(slsum != 0){").append("rectotal.set('RULSF',Round(slsfsum / slsum,2) );").append("}").append("};");
			
			
			
		sb.append("});");
		
		//�趨�ڼ��в��ɱ༭
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		sb.append("if (e.row==7){e.cancel=true;}");//�趨��8�в��ɱ༭,�����Ǵ�0��ʼ��
		sb.append(" if(e.field=='DIANCXXB_ID'){ e.cancel=true;}");//�趨�糧�в��ɱ༭
		sb.append("});");
		
		
		//�趨��������Ϊ�ϼƵ�ʱ��,���в�����
		sb.append("function gridDiv_save(record){if(record.get('RIQ')=='�ϼ�') return 'continue';}");
		
		
		
		egu.addOtherScript(sb.toString());
		
		
		//---------------ҳ��js�������--------------------------
		
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, "DeleteButton");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		
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
			//getSelectData();
		}
		
			getSelectData();
		
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
		return "for(i=0;i<res.getCount()-1;i++){"
				+ " resa=res.getAt(i);"
				+ " resa.set('CHAOCFX',document.getElementById('My_opinion').value);"
				+ " resa.set('CHAOCFXRY',document.getElementById('Histry_opinion').value);"
				+ " }";
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
