package com.zhiren.dc.monthReport.tb;
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
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ���ߣ����
 * ���ڣ�2011-12-20
 * ������ȡ��ȫ�������Զ�ˢ�¹��ܣ��û����ֶ����ˢ�°�ť�ſ�ˢ������
 */
/*
 * ���ߣ����
 * ���ڣ�2011-12-23
 * �������޸Ľ�����ʾ��ʽ
 */
/*
 * ���ߣ���ʤ��
 * ���ڣ�2012-03-16
 * ����������ɾ������
 */
public class Yueshchjb_szs extends BasePage implements PageValidateListener {
	public static final String strParam ="strtime";
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
    private boolean _DelClick = false;
	
	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}
	private void Save() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		String sql = new String();
		while (rsl.next()) {
			if("".equals(rsl.getString("ID"))){
				sql += 
					"insert into yueshchjb(id,riq,diancxxb_id,fenx,qickc,shouml,fady,gongry,qith,sunh,diaocl,panyk,shuifctz,runxcs,jitcs,kuc) values(\n" +
					"  " + "getnewid("+getTreeid()+")" +
					", " + DateUtil.FormatOracleDate(getNianf() + "-" + getYuef() + "-01") + "\n" +
					", " + getTreeid() + "\n" +
					",'" + rsl.getString("fenx") + "'\n" +
					", " + rsl.getDouble("qickc") + "\n" +
					", " + rsl.getDouble("shouml") + "\n" +
					", " + rsl.getDouble("fady") + "\n" +
					", " + rsl.getDouble("gongry") + "\n" +
					", " + rsl.getDouble("qith") + "\n" +
					", " + rsl.getDouble("sunh") + "\n" +
					", " + rsl.getDouble("diaocl") + "\n" +
					", " + rsl.getDouble("panyk") + "\n" +
					", " + rsl.getDouble("shuifctz") + "\n" +
					", " + rsl.getDouble("runxcs") + "\n" +
					", " + rsl.getDouble("jitcs") + "\n" +
					", " + rsl.getDouble("kuc") + "\n" +
					
					");";
			}else{
				sql +=
					"update yueshchjb set " + "\n" +
					" qickc = " + rsl.getDouble("qickc") + "\n" +
					",shouml = " + rsl.getDouble("shouml") + "\n" +
					",fady = " + rsl.getDouble("fady") + "\n" +
					",gongry = " + rsl.getDouble("gongry") + "\n" +
					",qith = " + rsl.getDouble("qith") + "\n" +
					",sunh = " + rsl.getDouble("sunh") + "\n" +
					",diaocl = " + rsl.getDouble("diaocl") + "\n" +
					",panyk = " + rsl.getDouble("panyk") + "\n" +
					",shuifctz = " + rsl.getDouble("shuifctz") + "\n" +
					",runxcs = " + rsl.getDouble("runxcs") + "\n" +
					",jitcs = " + rsl.getDouble("jitcs") + "\n" +
					",kuc = " + rsl.getDouble("kuc") + "\n" +
					" where id=" + rsl.getString("id") + ";";
			}
			
		}
		int flag = 0;
		if(sql.length()!=0){
			flag = con.getInsert("begin\n" + sql + "end;");
			if(flag==-1){
				con.Close();
				return;
			}
		}else{
			con.Close();
			return;
		}

		if(getIsSelectLike()){
			sql = "";
			rsl = new ResultSetList();
			
			double bq_qickc = 0;
			double bq_kc = 0;
			{
				sql = "select qickc,kuc from yueshchjb where \n" +
				  "to_char(riq, 'yyyy') = "+getNianf()+" \n" +
				  "and to_char(riq, 'mm') = "+ getYuef() +" \n" +
			      "and fenx='����'" + 
			      "and diancxxb_id="+getTreeid()+"";
				rsl = con.getResultSetList(sql);
				rsl.next();
				bq_qickc = rsl.getDouble("qickc");
				bq_kc = rsl.getDouble("kuc");
			}
			
			sql = 
				"select \n" +
				"       sum(shouml) shouml,\n" + 
				"       sum(fady) fady,\n" + 
				"       sum(gongry) gongry,\n" + 
				"       sum(qith) qith,\n" + 
				"       sum(sunh) sunh,\n" + 
				"       sum(diaocl) diaocl,\n" + 
				"       sum(panyk) panyk,\n" + 
				"       sum(shuifctz) shuifctz,\n" + 
				"       sum(runxcs) runxcs,\n" + 
				"       sum(jitcs) jitcs\n" + 
				"  from yueshchjb\n" + 
				" where fenx = '����'\n" + 
				"   and diancxxb_id = "+getTreeid()+"\n" + 
				"   and to_char(riq, 'yyyy') = " + getNianf() + "\n" +
				"   and riq<=to_date('"+ getNianf() + "-" + getYuef() + "-01" + "','yyyy-mm-dd')\n";
			rsl = con.getResultSetList(sql);
			rsl.next();
			double shouml = rsl.getDouble("shouml");
			double fady = rsl.getDouble("fady");
			double gongry = rsl.getDouble("gongry");
			double qith = rsl.getDouble("qith");
			double sunh = rsl.getDouble("sunh");
			double diaocl = rsl.getDouble("diaocl");
			double panyk = rsl.getDouble("panyk");
			double shuifctz = rsl.getDouble("shuifctz");
			double runxcs = rsl.getDouble("runxcs");
			double jitcs = rsl.getDouble("sunh");//�����ͬ�����
			sql = "update yueshchjb set \n" + 
				  " qickc = " + bq_qickc + "\n" +
				  ",shouml = " + shouml + "\n" +
				  ",fady = " + fady + "\n" +
				  ",gongry = " + gongry + "\n" +
				  ",qith = " + qith + "\n" +
				  ",sunh = " + sunh + "\n" +
				  ",diaocl = " + diaocl + "\n" +
				  ",panyk = " + panyk + "\n" +
				  ",shuifctz = " + shuifctz + "\n" +
				  ",runxcs = " + runxcs + "\n" +
				  ",jitcs = " + jitcs + "\n" +
				  ",kuc = " + bq_kc + "\n" + //(bq_qickc + shouml - fady - gongry - qith - sunh - diaocl + panyk)
				  "where diancxxb_id=" + getTreeid() + " and fenx='�ۼ�' and riq=" + DateUtil.FormatOracleDate(getNianf() + "-" + getYuef() + "-01");
			
		    flag = con.getUpdate(sql);
		    if(flag !=-1){
		    	setMsg("����ɹ�!");
		    }else{
		    	setMsg("����ʧ��!");
		    }
			
		}
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
		}
		setRiq();
	}
	public void DelData() {
		//String diancxxb_id = getTreeid();
		JDBCcon con = new JDBCcon();
		
		String CurrZnDate=getNianf()+"��"+getYuef()+"��";
		String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
		String strSql = "delete from yueshchjb where riq="+CurrODate
		+" and diancxxb_id = "+getTreeid();
		int flag = con.getDelete(strSql);
		if(flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail+"SQL:"+strSql);
			setMsg("ɾ�������з�������");
		}else {
			setMsg(CurrZnDate+"�����ݱ��ɹ�ɾ����");
		}
		con.Close();
	}

	
	
	public String getBtnHandlerScript(String btnName) {
		// ��ť��script
		String cnDate = getNianf() + "��" + getYuef() + "��";
		StringBuffer btnsb = new StringBuffer();
		btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
		btnsb.append("�Ƿ�ɾ��").append(cnDate).append("�����ݣ�");
		btnsb.append("',function(btn){if(btn == 'yes'){").append(
				"document.getElementById('").append(btnName).append(
				"').click()").append("}; // end if \n").append("});}");
		return btnsb.toString();
	}
	
	/**
	 * @param con
	 * @return   true:���ϴ�״̬�� �����޸����� false:δ�ϴ�״̬�� �����޸�����
	 */
	private boolean getZhangt(JDBCcon con){
		String CurrODate =  DateUtil.FormatOracleDate(getNianf() + "-"
			+ getYuef() + "-01");
		String sql=
			"select k.zhuangt zhuangt\n" +
			"  from yueshchjb k\n" + 
			" where  k.diancxxb_id = "+getTreeid()+"\n" + 
			"   and k.riq = "+CurrODate;
		ResultSetList rs=con.getResultSetList(sql);
		boolean zt=true;
		if(con.getHasIt(sql)){
			while(rs.next()){
				if(rs.getInt("zhuangt")==0||rs.getInt("zhuangt")==2){
					zt=false;
				}
			}
		}else{
			zt=false;
		}
		return zt;
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		
		String diancxxb_id = getTreeid();
		String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
		String qickc = "0";
		String shul = "0";	
		String strSql = "select * from yueslb s, yuetjkjb k\n" +
        "where s.yuetjkjb_id = k.id\n" +
        "  and k.riq = "+ CurrODate + "\n" +
        "  and k.diancxxb_id="+diancxxb_id;
		boolean isNotReady = !con.getHasIt(strSql);
		if(isNotReady) {
			setMsg("����ʹ�ñ�ģ��֮ǰ������������������ݵļ��㣡");
		}

		ResultSetList rsl1 = con.getResultSetList(strSql);
		if(rsl1.next()){
			qickc = "" + rsl1.getDouble("kuc");
		}
		
		strSql = 
			"select sum(jingz) shul from yueslb ys,yuetjkjb yt\n" +
			" where yt.id=ys.yuetjkjb_id\n" + 
			"   and yt.riq = "+CurrODate+"\n" + 
			"   and yt.diancxxb_id="+diancxxb_id+"\n" + 
			"   and ys.fenx='"+SysConstant.Fenx_Beny+"'";

		rsl1 = con.getResultSetList(strSql);
		if(rsl1.next()){
			shul = "" + rsl1.getDouble("shul");
		}
		
		strSql=
			"select sj.id,fx.fenx,decode(nvl(sj.qickc,0),0,"+qickc+",qickc) qickc,decode(fx.fenx,'"+SysConstant.Fenx_Beny+"',decode(nvl(sj.shouml,0),0,"+shul+",sj.shouml),sj.shouml) shouml,sj.fady,sj.gongry,sj.qith,sj.sunh,sj.diaocl,sj.panyk,sj.shuifctz,sj.jitcs,sj.kuc, sj.runxcs from (\n" +
			"select id,fenx,qickc,shouml,fady,gongry,qith,sunh,diaocl,panyk,shuifctz,runxcs,jitcs,kuc\n" +
			" from yueshchjb where riq="+CurrODate+" and diancxxb_id="+diancxxb_id+") sj," +
			"(select decode(0,0,'"+SysConstant.Fenx_Beny+"') fenx from dual\n" +
			"  union all\n" + 
			" select decode(0,0,'"+SysConstant.Fenx_Leij+"') fenx from dual) fx\n" + 
			" where sj.fenx(+)=fx.fenx order by fenx";
		ResultSetList rsl = con.getResultSetList(strSql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(0);
		// //���ñ��������ڱ���
		egu.setTableName("yueshchjb");
		// /������ʾ������
		egu.setWidth("bodyWidth");
		egu.getColumn("fenx").setHeader("����");
		egu.getColumn("fenx").setWidth(60);
		egu.getColumn("fenx").setEditor(null);
		egu.getColumn("qickc").setHeader("�ڳ����");
		egu.getColumn("qickc").setWidth(60);
		egu.getColumn("qickc").setEditor(null);
		egu.getColumn("shouml").setHeader("����");
		egu.getColumn("shouml").setWidth(60);
		egu.getColumn("shouml").setEditor(null);
		egu.getColumn("fady").setHeader("������");
		egu.getColumn("fady").setWidth(60);
		egu.getColumn("gongry").setHeader("������");
		egu.getColumn("gongry").setWidth(60);
		egu.getColumn("qith").setHeader("������");
		egu.getColumn("qith").setWidth(60);
		egu.getColumn("sunh").setHeader("ʵ�ʴ���");
		egu.getColumn("sunh").setWidth(60);
		egu.getColumn("diaocl").setHeader("������");
		egu.getColumn("diaocl").setWidth(60);
		egu.getColumn("panyk").setHeader("��ӯ��");
		egu.getColumn("panyk").setWidth(60);
		egu.getColumn("shuifctz").setHeader("ˮ�ֲ����");
		egu.getColumn("shuifctz").setWidth(80);
		egu.getColumn("jitcs").setHeader("���ᴢ��");
		egu.getColumn("jitcs").setWidth(60);
		egu.getColumn("jitcs").setHidden(true);
		egu.getColumn("kuc").setHeader("���");
		egu.getColumn("kuc").setWidth(60);
		
		
		egu.getColumn("runxcs").setHeader("������");
		egu.getColumn("runxcs").setWidth(60);
		
		
		egu.setDefaultsortable(false);  

		egu.addTbarText("���");
		ComboBox comb1=new ComboBox();
		comb1.setWidth(50);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");//���Զ�ˢ�°�
		comb1.setLazyRender(true);//��̬��
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("�·�");
		ComboBox comb2 = new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");// ���Զ�ˢ�°�
		comb2.setLazyRender(true);// ��̬��
		egu.addToolbarItem(comb2.getScript());
//		egu.addOtherScript("YuefDropDown.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// ���÷ָ���
		// ������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// ���÷ָ���
		boolean isLocked = isLocked(con);
//		ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'����ˢ��'+Ext.getDom('NianfDropDown').value+'��'+Ext.getDom('YuefDropDown').value+'�µ�����,���Ժ�'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);

//		�ж������Ƿ��Ѿ��ϴ� ������ϴ� �����޸� ɾ�� �������
		if(getZhangt(con)){
			setMsg("�����Ѿ��ϴ���������ϵ�ϼ���λ����֮����ܲ�����");
		}else{
			if(isLocked) {
				setMsg("�Ĵ������Ѿ����ɣ�<br>����ɾ����Ӧ���ݺ��ٲ�����");
			}else{
//				ɾ����ť
				GridButton gbd = new GridButton("ɾ��",getBtnHandlerScript("DelButton"));
				gbd.setDisabled(isNotReady);
				gbd.setIcon(SysConstant.Btn_Icon_Delete);
				egu.addTbarBtn(gbd);
				
//				���水ť
				GridButton gbs = new GridButton(GridButton.ButtonType_SaveAll,"gridDiv",egu.getGridColumns(),"SaveButton");
				gbs.setDisabled(isNotReady);
				egu.addTbarBtn(gbs);
				
				Checkbox cb=new Checkbox();
				cb.setId("SelectLike");
				cb.setListeners("check:function(own,checked){if(checked){document.all.SelectLike.value='true'}else{document.all.SelectLike.value='false'}}");
				egu.addToolbarItem(cb.getScript());
				egu.addTbarText("�Ƿ��Զ������ۼ�ֵ");
			}
		}
		

//		ǰ̨������
		StringBuffer sb = new StringBuffer();
//		shuifctz,runxcs,jitcs,
		sb.append(
				"gridDiv_grid.on('afteredit',function(e){\n" +
				"  if(e.field=='QICKC'||e.field=='SHOUML'||e.field=='FADY'||e.field=='GONGRY'||e.field=='QITH'||e.field=='SUNH'||e.field=='DIAOCL'||e.field=='PANYK'||e.field=='SHUIFCTZ'||e.field=='RUNXCS'||e.field=='JITCS'){\n" + 
				"    var qickc=0,shouml=0,fady=0,gongry=0,qith=0,sunh=0,diaocl=0,panyk=0,shuifctz=0,runxcs=0,jitcs=0,kuc=0;\n" + 
				"    var i = e.row;\n" + 
				"    qickc = gridDiv_ds.getAt(i).get('QICKC');\n" + 
				"    shouml = gridDiv_ds.getAt(i).get('SHOUML');\n" + 
				"    fady = gridDiv_ds.getAt(i).get('FADY');\n" + 
				"    gongry = gridDiv_ds.getAt(i).get('GONGRY');\n" + 
				"    qith = gridDiv_ds.getAt(i).get('QITH');\n" + 
				"    sunh = gridDiv_ds.getAt(i).get('SUNH');\n" + 
				"    diaocl = gridDiv_ds.getAt(i).get('DIAOCL');\n" + 
				"    panyk = gridDiv_ds.getAt(i).get('PANYK');\n" + 
				"    shuifctz = gridDiv_ds.getAt(i).get('SHUIFCTZ');\n" + 
				"    jitcs = gridDiv_ds.getAt(i).get('SUNH');\n" + 
				"    kuc=eval(qickc||0)+eval(shouml||0)-eval(fady||0)-eval(gongry||0)-eval(qith||0)-eval(sunh||0)-eval(diaocl||0)+eval(panyk||0)+eval(shuifctz||0);\n" + 
				"	 gridDiv_ds.getAt(i).set('JITCS',sunh);" +
				"    gridDiv_ds.getAt(i).set('KUC',kuc);\n" + 
				"  }\n" + 
				"});"
		);
		sb.append("gridDiv_grid.on('beforeedit',function(e){" + 
				"if(e.field=='JITCS'){e.cancel=true;}" + 
				"});"
		);
		
		egu.addOtherScript(sb.toString());
		
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
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
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
			visit.setString1(null);
			visit.setString2(null);
			visit.setString3(null);
			setYuefValue(null);
			setNianfValue(null);
			getYuefModels();
			getNianfModels();
			setRiq();
			setTreeid(null);
		}
		create(getTreeid(),getNianf() + "-" + getYuef() + "-01");
		getSelectData();
	}

	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}

	public String getYuef() {
		int intYuef = Integer.parseInt(((Visit) getPage().getVisit())
				.getString3());
		if (intYuef < 10) {
			return "0" + intYuef;
		} else {
			return ((Visit) getPage().getVisit()).getString3();
		}
	}

	public void setYuef(String value) {
		((Visit) getPage().getVisit()).setString3(value);
	}

	public void setRiq() {
		setNianf(getNianfValue().getValue());
		setYuef(getYuefValue().getValue());
	}
	public boolean isLocked(JDBCcon con) {//�ж������Ƿ�������_����
		String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-"+"01");
		//Visit visit=(Visit)getPage().getVisit();
		return con.getHasIt("select * from yuehcb yh,yuetjkjb yj where yh.yuetjkjb_id=yj.id and " +
				" yj.riq="+CurrODate+"\n" + 
				" and yj.diancxxb_id="+getTreeid());
	}

	// ���������
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
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			_NianfValue = Value;
		}
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

	// �·�������
	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_yuef = 12;
			} else {
				_yuef = _yuef - 1;
			}
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			_YuefValue = Value;
		}
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}

	// �糧��

	// �õ���½�û����ڵ糧���߷ֹ�˾������
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
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return diancmc;
	}

	// �õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getIDropDownDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return IDropDownDiancmc;
	}

	private String treeid;

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		setRiq();
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

	// �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}
		return jib;
	}
	
	public boolean getIsSelectLike(){
		return ((Visit) this.getPage().getVisit()).getboolean8();
	}
	public String getSelectLike(){
		return ((Visit) this.getPage().getVisit()).getString8();
	}
	public void setSelectLike(String value){
		boolean flag = false;
		if("true".equals(value)){
			flag = true;
		}
		((Visit) this.getPage().getVisit()).setboolean8(flag);
	}
	
	public void create(String diancxxb_id, String strDate){
		Visit visit = (Visit) this.getPage().getVisit();
		if(!String.valueOf(visit.getDiancxxb_id()).equals(diancxxb_id)){//������������ͳ�ƿھ�
			JDBCcon con = new JDBCcon();
			Date cd = DateUtil.getDate(strDate);
			String CurrODate = DateUtil.FormatOracleDate(cd);
			String sql = "select count(0) hej from yueshchjb where riq="+CurrODate+" and diancxxb_id="+diancxxb_id;
			ResultSetList rsl = con.getResultSetList(sql);
			String sql2 = "select count(0) hej from yuetjkjb where riq="+CurrODate+" and diancxxb_id="+diancxxb_id;
			ResultSetList rsl2 = con.getResultSetList(sql2);
			rsl.next();
			rsl2.next();
			
			if(rsl.getInt(0)==0 && rsl2.getInt(0)!=0){
				sql = "begin\n";
				sql+= "insert into yueshchjb(id,diancxxb_id,riq,fenx,qickc,shouml,fady,gongry,qith,sunh,diaocl,panyk,kuc) values("
					 +"getnewid("+diancxxb_id+"),"+diancxxb_id+",to_date('"+strDate+"','yyyy-mm-dd'),'" + SysConstant.Fenx_Beny + "',0,0,0,0,0,0,0,0,0);\n";
				sql+="insert into yueshchjb(id,diancxxb_id,riq,fenx,qickc,shouml,fady,gongry,qith,sunh,diaocl,panyk,kuc) values("
					 +"getnewid("+diancxxb_id+"),"+diancxxb_id+",to_date('"+strDate+"','yyyy-mm-dd'),'" + SysConstant.Fenx_Leij + "',0,0,0,0,0,0,0,0,0);\n";
				sql+="end;";
				int flag = con.getInsert(sql);
				if (flag == -1) {
					WriteLog.writeErrorLog(this.getClass().getName() + "\n"
							+ ErrorMessage.InsertYuetjkjFailed + "\nSQL:" + sql);
					setMsg(ErrorMessage.InsertYuetjkjFailed);
					con.rollBack();
					con.Close();
					return;
				}
			}
			con.Close();
		}
	}

}
