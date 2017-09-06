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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ���ߣ����
 * ���ڣ�2011-12-20
 * ������ȡ��ȫ�������Զ�ˢ�¹��ܣ��û����ֶ����ˢ�°�ť�ſ�ˢ������
 */
/*
 * ���ߣ����
 * ���ڣ�2012-02-15
 * ������ȡ���±��޸���������ͳ�ƿھ��ķ�����
 */
/*
 * ���ߣ�LIP
 * ���ڣ�2012-03-14
 * �����������������ݴ洢�����б䣬ͬ���Ƴ������Σ��ı�SQL��������
 */
/*
 * ���ߣ�LIP
 * ���ڣ�2012-03-14
 * �����������������ݴ洢�����б䣬ͬ���Ƴ������Σ��ı�SQL��������
 */
/*
 * ���ߣ�LIP
 * ���ڣ�2012-03-14
 * �������кĴ�ϼƣ������޸ĸ�ҳ
 */
/*
 * ���ߣ���ʤ��
 * ���ڣ�2012-03-15
 * ����������ɾ������
 */
/*
 * ���ߣ���ʤ��
 * ���ڣ�2013-01-11
 * �������������治�ɱ༭����ʾ��ʽ��
 *				����,Ʊ��������Сֵ����,��Сֵ����0.
 */
public class Yueslb_szs extends BasePage implements PageValidateListener {
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

	private void Save() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		StringBuffer sql = new StringBuffer();
		sql.delete(0, sql.length());
		sql.append("begin \n");
		while (rsl.next()) {
			if ("0".equals(rsl.getString("YID")) || "".equals(rsl.getString("YID"))) {
				sql.append("insert into yueslb("
								+ "id,fenx,yuetjkjb_id,"
								+ "jingz,biaoz,yingd,kuid,yuns,zongkd,jianjl,ructzl," 
								+ "yingdzje,kuidzje,suopsl,suopje"
								+ ")values("
								+ rsl.getString("ID") + ",'"+rsl.getString("fenx")+"'," + rsl.getString("yuetjkjb_id") + ","
								+ rsl.getDouble("jingz") + ","
								+ rsl.getDouble("biaoz") + ","
								+ rsl.getDouble("yingd") + ","
								+ rsl.getDouble("kuid") + ","
								+ rsl.getDouble("yuns") + ","
								+ rsl.getDouble("zongkd") + ","
								+ rsl.getDouble("jianjl") + ","
								+ rsl.getDouble("ructzl") + ","
								+ rsl.getDouble("yingdzje") + ","
								+ rsl.getDouble("kuidzje") + ","
								+ rsl.getDouble("suopsl") + ","
								+ rsl.getDouble("suopje") + ""
								+ ");\n");
				// }
			} else {
				sql.append("update yueslb set " +
						" jingz=" + rsl.getDouble("jingz") + 
						",biaoz=" + rsl.getDouble("biaoz") + 
						",yingd=" + rsl.getDouble("yingd") + 
						",kuid=" + rsl.getDouble("kuid") + 
						",yuns=" + rsl.getDouble("yuns") + 
						",zongkd=" + rsl.getDouble("zongkd") + 
						",jianjl=" + rsl.getDouble("jianjl") + 
						",ructzl=" + rsl.getDouble("ructzl") + 
						",yingdzje=" + rsl.getDouble("yingdzje") + 
						",kuidzje=" + rsl.getDouble("kuidzje") + 
						",suopsl=" + rsl.getDouble("suopsl") + 
						",suopje=" + rsl.getDouble("suopje") + 
						" where id=" + rsl.getLong("id") + ";\n");
			}
		}
		sql.append("end;");
		int flag = con.getUpdate(sql.toString());
//		System.out.println(flag);
		if(flag!=-1){
			rsl.beforefirst();
			sql.setLength(0);
			while(rsl.next()){
				if("�ۼ�".equals(rsl.getString("fenx")) && getIsSelectLike()){
					String sq = "select\n" +

						"sum(jingz) jingz,\n" +
						"sum(biaoz) biaoz,\n" +
						"sum(yingd) yingd,\n" +
						"sum(kuid) kuid,\n" +
						"sum(yuns) yuns,\n" +
						"sum(zongkd) zongkd,\n" +
						"sum(jianjl) jianjl,\n" +
						"sum(ructzl) ructzl,\n" +
						"sum(yingdzje) yingdzje,\n" +
						"sum(kuidzje) kuidzje,\n" +
						"sum(suopsl) suopsl,\n" +
						"sum(suopje) suopje\n" +
						
						"  from yueslb y,yuetjkjb yt,(select gongysb_id,jihkjb_iD,pinzb_id,yunsfsb_id from yuetjkjb where id= " + rsl.getString("YUETJKJB_ID") + ")yt2\n" + 
						" where y.yuetjkjb_id=yt.id\n" + 
						"	and yt.gongysb_id=yt2.gongysb_id\n" +
						"	and yt.jihkjb_id=yt2.jihkjb_id\n" +
						"	and yt.pinzb_id=yt2.pinzb_id\n" +
						"	and yt.yunsfsb_id=yt2.yunsfsb_id\n" +
						"   and yt.riq>=to_date('"+ getNianf() + "-01-01" + "','yyyy-mm-dd')\n" + 
						"   and yt.riq<=to_date('"+ getNianf() + "-" + getYuef() + "-01" + "','yyyy-mm-dd')\n" + 
						"   and y.fenx='" + SysConstant.Fenx_Beny + "'\n" + 
						"   and yt.diancxxb_id=" + getTreeid();
					ResultSetList rs = con.getResultSetList(sq);
					rs.next();
					sql.append("update yueslb set " +
							" jingz=" + rs.getDouble("jingz") + 
							",biaoz=" + rs.getDouble("biaoz") + 
							",yingd=" + rs.getDouble("yingd") + 
							",kuid=" + rs.getDouble("kuid") + 
							",yuns=" + rs.getDouble("yuns") + 
							",zongkd=" + rs.getDouble("zongkd") + 
							",jianjl=" + rs.getDouble("jianjl") + 
							",ructzl=" + rs.getDouble("ructzl") + 
							",yingdzje=" + rs.getDouble("yingdzje") + 
							",kuidzje=" + rs.getDouble("kuidzje") + 
							",suopsl=" + rs.getDouble("suopsl") + 
							",suopje=" + rs.getDouble("suopje") + 
							" where id=" + rsl.getLong("id") + ";\n");
				}
			}
			if(sql.length()!=0){
				flag = con.getUpdate("begin\n" + sql.toString() + "\n end;");
				if(flag!=-1){
					setMsg("����ɹ�!");
				}else{
					setMsg("����ɹ�,�ۼ�ֵ����ʧ��!");
				}
			}else{
				setMsg("����ɹ�!");
			}
		}else{
			setMsg("����ʧ��");
		} 
		rsl.close();
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	private boolean _DelClick = false;

	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
//			getSelectData();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
//			getSelectData();
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
			String strSql = "delete from yueslb where yuetjkjb_id in (select id from yuetjkjb where riq="
				+ CurrODate
				+ " and diancxxb_id="
				+ getTreeid()
				+ ")";
			int flag = con.getDelete(strSql);
			if(flag == -1) {
				WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail+"SQL:"+strSql);
				setMsg("ɾ�������з�������");
			}else {
				setMsg(CurrZnDate+"�����ݱ��ɹ�ɾ����");
			}
			con.Close();
		}

	
	/**
	 * @param con
	 * @return   true:���ϴ�״̬�� �����޸����� false:δ�ϴ�״̬�� �����޸�����
	 */
	private boolean getZhangt(JDBCcon con){
//		Visit visit=(Visit)getPage().getVisit();
		String CurrODate =  DateUtil.FormatOracleDate(getNianf() + "-"
			+ getYuef() + "-01");
		String sql=
			"select s.zhuangt zhuangt\n" +
			"  from yueslb s, yuetjkjb k\n" + 
			" where s.yuetjkjb_id = k.id\n" + 
			"   and k.diancxxb_id = "+getTreeid()+"\n" + 
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
		String strDate = getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-01";
		String sql = 
			"select y.id yid,decode(y.id,null,getnewid(" + getTreeid() + "),y.id) id,s.gmingc,s.jmingc,s.pmingc,s.ysmingc,s.fenx,s.yuetjkjb_id,\n" +
			"             jingz,biaoz,yingd,kuid,yuns,zongkd,jianjl,ructzl,\n" + 
			"             yingdzje,kuidzje,suopsl,suopje\n" + 
			"  from (\n" + 
			"      select x.fenx,y.id yuetjkjb_id,g.mingc gmingc,j.mingc jmingc,p.mingc pmingc,ys.mingc ysmingc \n" + 
			"        from yuetjkjb y,\n" + 
			"             (select decode(0,0,'"+SysConstant.Fenx_Beny+"') fenx\n" + 
			"                from dual\n" + 
			"              union\n" + 
			"              select decode(0,0,'"+SysConstant.Fenx_Leij+"') fenx from dual) x,\n" + 
			"             gongysb g,\n" + 
			"             jihkjb j,\n" + 
			"             pinzb p,\n" + 
			"             yunsfsb ys\n" + 
			"       where y.riq = to_date('" + strDate + "','yyyy-mm-dd')\n" + 
			"         and y.diancxxb_id="+getTreeid()+"\n" + 
			"         and y.gongysb_id=g.id\n" + 
			"         and y.jihkjb_id=j.id\n" + 
			"         and y.pinzb_id=p.id\n" + 
			"         and y.yunsfsb_id=ys.id\n" + 
			"      ) s,yueslb y\n" + 
			"      where y.yuetjkjb_id(+)=s.yuetjkjb_id\n" + 
			"        and y.fenx(+)=s.fenx\n" + 
			"      order by jmingc desc, gmingc, pmingc, y.id, s.yuetjkjb_id, s.fenx";
		
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		// //���ñ��������ڱ���
		egu.setTableName("yuezlb");
		egu.setWidth("bodyWidth");
		egu.addPaging(0);
		egu.getColumn("yuetjkjb_id").setHidden(true);
		
		egu.getColumn("yid").setHidden(true);
		egu.getColumn("yid").setHeader("yid");
		egu.getColumn("yid").setEditor(null);

		egu.getColumn("jingz").setHeader(Locale.jingz_fahb);
		egu.getColumn("jingz").setWidth(60);
		((NumberField)egu.getColumn("jingz").editor).setDecimalPrecision(0);
		egu.getColumn("jingz").editor.setMinValue("0");
		
		egu.getColumn("biaoz").setHeader(Locale.biaoz_fahb);
		egu.getColumn("biaoz").setWidth(60);
		((NumberField)egu.getColumn("biaoz").editor).setDecimalPrecision(0);
		egu.getColumn("biaoz").editor.setMinValue("0");
		
		egu.getColumn("yingd").setHeader(Locale.yingd_fahb);
		egu.getColumn("yingd").setWidth(60);
		((NumberField)egu.getColumn("yingd").editor).setDecimalPrecision(0);
		egu.getColumn("kuid").setHeader(Locale.kuid_fahb);
		egu.getColumn("kuid").setWidth(60);
		((NumberField)egu.getColumn("kuid").editor).setDecimalPrecision(0);
		egu.getColumn("yuns").setHeader(Locale.yuns_fahb);
		egu.getColumn("yuns").setWidth(60);
		((NumberField)egu.getColumn("yuns").editor).setDecimalPrecision(0);
		egu.getColumn("zongkd").setHeader(Locale.zongkd_fahb);
		egu.getColumn("zongkd").setWidth(60);
		((NumberField)egu.getColumn("zongkd").editor).setDecimalPrecision(0);
		egu.getColumn("jianjl").setHeader(Locale.MRtp_jianjl);
		egu.getColumn("jianjl").setWidth(60);
		((NumberField)egu.getColumn("jianjl").editor).setDecimalPrecision(0);
		egu.getColumn("ructzl").setHeader(Locale.MRtp_ructzl);
		egu.getColumn("ructzl").setWidth(90);
		egu.getColumn("yingdzje").setHeader(Locale.MRtp_yingdzje);
		egu.getColumn("yingdzje").setWidth(90);
		egu.getColumn("kuidzje").setHeader(Locale.MRtp_kuidzje);
		egu.getColumn("kuidzje").setWidth(90);
		egu.getColumn("suopsl").setHeader(Locale.MRtp_suopsl);
		egu.getColumn("suopsl").setWidth(80);
		egu.getColumn("suopje").setHeader(Locale.MRtp_suopje);
		egu.getColumn("suopje").setWidth(80);
		
		egu.getColumn("gmingc").setHeader("������λ");
		egu.getColumn("gmingc").setEditor(null);
		egu.getColumn("gmingc").setWidth(80);
		egu.getColumn("gmingc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("jmingc").setHeader("�ƻ��ھ�");
		egu.getColumn("jmingc").setEditor(null);
		egu.getColumn("jmingc").setWidth(70);
		egu.getColumn("jmingc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("pmingc").setHeader("Ʒ��");
		egu.getColumn("pmingc").setEditor(null);
		egu.getColumn("pmingc").setWidth(70);
		egu.getColumn("pmingc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("ysmingc").setHeader("����");
		egu.getColumn("ysmingc").setEditor(null);
		egu.getColumn("ysmingc").setWidth(60);
		egu.getColumn("ysmingc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("fenx").setHeader("����");
		egu.getColumn("fenx").setWidth(60);
		egu.getColumn("fenx").setEditor(null);
		egu.getColumn("fenx").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.setDefaultsortable(false);
		// /���ð�ť
		egu.addTbarText("���");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(50);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
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
		
		
		// �糧��
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// ���÷ָ���
		// ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){").append(
					MainGlobal.getExtMessageBox("'����ˢ��'+Ext.getDom('NianfDropDown').value+'��'+Ext.getDom('YuefDropDown').value+'�µ�����,���Ժ�'",
					true)).append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		// ɾ����ť
//		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		boolean isLocked = isLocked(con);
//		�ж������Ƿ��Ѿ��ϴ� ������ϴ� �����޸� ɾ�� �������
		if(getZhangt(con)){
			setMsg("�����Ѿ��ϴ���������ϵ�ϼ���λ����֮����ܲ�����");
		}else{
			// �ж������Ƿ�����
			if (isLocked) {
				setMsg("������Ĵ�ϼ������Ѿ����ɣ�<br>����ɾ����Ӧ���ݺ��ٲ�����");
			}else{
				
				//ɾ��
				GridButton gbd = new GridButton("ɾ��", getBtnHandlerScript("DelButton"));
				gbd.setIcon(SysConstant.Btn_Icon_Delete);
				egu.addTbarBtn(gbd);
				
				// ���水ť
				egu.addToolbarButton(GridButton.ButtonType_SaveAll, "SaveButton",MainGlobal.getExtMessageShow("���ڱ�������,���Ժ�...", "������...", 200));

				Checkbox cb=new Checkbox();
				cb.setId("SelectLike");
				cb.setListeners("check:function(own,checked){if(checked){document.all.SelectLike.value='true'}else{document.all.SelectLike.value='false'}}");
				egu.addToolbarItem(cb.getScript());
				egu.addTbarText("�Ƿ��Զ������ۼ�ֵ");
			}
		}
		
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
			visit.setShifsh(true);
			setYuefValue(null);
			setNianfValue(null);
			getYuefModels();
			getNianfModels();
			setRiq();
			setTreeid(null);
		}
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
    //
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
		// System.out.println("jib:" + DiancTreeJib);
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
	
	
	public boolean isLocked(JDBCcon con) {
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
				+ getYuef() + "-01");
		String chk_sql="SELECT ID\n" +
		"  FROM YUESHCHJB\n" + 
		" WHERE RIQ = "+CurrODate+"\n" + 
		"   AND DIANCXXB_ID = "+getTreeid()+"\n" + 
		"UNION\n" + 
		"SELECT Z.ID\n" + 
		"  FROM YUEZLB Z, YUETJKJB T\n" + 
		" WHERE Z.YUETJKJB_ID = T.ID\n" + 
		"   AND RIQ = "+CurrODate+"\n" + 
		"   AND DIANCXXB_ID = "+getTreeid()+"";

		return con.getHasIt(chk_sql);
	}


}
