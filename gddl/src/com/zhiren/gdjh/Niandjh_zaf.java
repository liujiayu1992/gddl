package com.zhiren.gdjh;

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
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ���ߣ����
 * ʱ�䣺2012-10-18
 * �������������Ƽƻ���ť�����������û����������ʾ�ð�ť��������ʾ��
 * 		�����ܳ�����������ơ�
 */

/*
 * ���ߣ���ʤ��
 * ʱ�䣺2012-11-19
 * �������Ա������ƽ��б�����ڽ���������������ɽ���δ��ɽ��ĺϼ�ֵ�ҽ����Զ�����
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-11-19
 * ���÷�Χ������������������糧
 * ������������ƻ�ָ���б�ú���ۺ���¯�ۺϱ�ú���۵ļ��㹫ʽ��
 */
public  class Niandjh_zaf extends BasePage implements PageValidateListener {
	
	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
	    _msg = MainGlobal.getExtMessageBox(_value,false);
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
//-------------------------------------------------------------  save()  -----------------------------

	private boolean isParentDc(JDBCcon con){
		String sql = "select * from diancxxb where fuid = " + this.getTreeid();
		return con.getHasIt(sql);
	}
	
	private void Save() {
  		Visit visit = (Visit) this.getPage().getVisit();
 		int flag=visit.getExtGrid1().Save(getChange(), visit);
 		if(flag!=-1){
 			setMsg(ErrorMessage.SaveSuccessMessage);
 		}
 		JDBCcon con = new JDBCcon();
// 		ÿ�α���ʱ���¼���ָ���е���ز���
 		countBMDJ(con,this.getTreeid(),"to_date('"+this.getNianfValue().getValue()+"-01-01','yyyy-mm-dd')");
 		con.Close();
 	}
//	���ƹ���
	private void CopyData() {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(true);
		String strDate=getNianfValue().getValue() +"-01-01";
		
		String CopySql=
			"INSERT INTO NIANDJH_ZAF\n" +
			"(ID,DIANCXXB_ID,RIQ,TBRQ,ZAFMC,YUCJE,YUCSM,SHIJWCJE,YUJWCJE,YUJWCSM,ZHUANGT)\n" + 
			"\n" + 
			"(SELECT GETNEWID("+this.getTreeid()+"),DIANCXXB_ID,ADD_MONTHS(RIQ, 12) RIQ,TBRQ,ZAFMC,\n" + 
			"YUCJE,YUCSM,SHIJWCJE,YUJWCJE,YUJWCSM,0 ZHUANGT\n" + 
			"   FROM NIANDJH_ZAF\n" + 
			"  WHERE RIQ = ADD_MONTHS(DATE '"+strDate+"', -12)\n" + 
			"    AND DIANCXXB_ID = "+this.getTreeid()+")";

		con.getInsert(CopySql);
// 		ÿ�α���ʱ���¼���ָ���е���ز���
 		countBMDJ(con,this.getTreeid(),"to_date('"+strDate+"','yyyy-mm-dd')");
 		con.Close();
		setMsg("���Ʋ�����ɣ�");
	}	
	
//	ÿ�α���ʱ���¼���ָ���е���ز���
	private int countBMDJ(JDBCcon con, String diancxxb_id, String CurrODate) {
		String upsql = 
			"SELECT MEIZBMDJ,\n" +
			"       ZAFJE,\n" + 
			"       ROUND(DECODE(BIAOMLHJ,0,0,(MEIZBML * MEIZBMDJ + RANYL * RANYDJ + ZAFJE) / BIAOMLHJ),2) RULZHBMDJ\n" + 
			"  FROM (SELECT NVL(CGJH.JIH_REZ, 0) JIH_REZ,\n" + 
			"               NVL(CGJH.JIH_DAOCJ, 0) JIH_DAOCJ,\n" + 
			"               NVL(ZAF.ZAFJE, 0) ZAFJE,\n" + 
			"               NVL(ZHIB.RUCRLRZC, 0) RUCRLRZC,\n" + 
			"               NVL(ZHIB.MEIZBML, 0) MEIZBML,\n" + 
			"               NVL(ZHIB.RANYL, 0) RANYL,\n" + 
			"               NVL(ZHIB.RANYDJ, 0) RANYDJ,\n" + 
			"               NVL(ZHIB.BIAOMLHJ, 0) BIAOMLHJ,\n" + 
			"               ROUND(DECODE((NVL(JIH_REZ, 0) - NVL(RUCRLRZC, 0)),0,0,NVL(JIH_DAOCJ, 0) * 29.2712 /(NVL(JIH_REZ, 0) - NVL(RUCRLRZC, 0))),2) MEIZBMDJ\n" + 
			"          FROM (SELECT SUM(JIH_SL) JIHSL,\n" + 
			"                       DECODE(SUM(JIH_SL),0,0,SUM(JIH_SL * JIH_REZ) / SUM(JIH_SL)) JIH_REZ,\n" + 
			"                       DECODE(SUM(JIH_SL),0,0,SUM(JIH_SL * (JIH_MEIJBHS+JIH_YUNJBHS+JIH_ZAFBHS+JIH_QITBHS)) / SUM(JIH_SL)) JIH_DAOCJ\n" + 
			"                  FROM NIANDJH_CAIG\n" + 
			"                 WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"                   AND RIQ = "+CurrODate+") CGJH,\n" + 
			"               (SELECT SUM(YUCJE) ZAFJE\n" + 
			"                  FROM NIANDJH_ZAF\n" + 
			"                 WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"                   AND TRUNC(RIQ, 'yyyy') = "+CurrODate+") ZAF,\n" + 
			"               (SELECT RUCRLRZC, MEIZBML, RANYL, RANYDJ, BIAOMLHJ\n" + 
			"                  FROM NIANDJH_ZHIB\n" + 
			"                 WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"                   AND RIQ = "+CurrODate+") ZHIB) SJ";
		
		ResultSetList rsl = con.getResultSetList(upsql);
		
		String updateSql="";
		if(rsl.next()) {
			double MEIZBMDJ=rsl.getDouble("MEIZBMDJ"); 
			double QITFY=rsl.getDouble("ZAFJE"); 
			double RULZHBMDJ=rsl.getDouble("RULZHBMDJ"); 
			updateSql = "update niandjh_zhib set MEIZBMDJ="+MEIZBMDJ+", QITFY="+QITFY+", RLZHBMDJ="+RULZHBMDJ+" where riq = " + CurrODate + " and diancxxb_id=" + diancxxb_id;
		}
		rsl.close();
		if(updateSql.length()>1){
			con.getUpdate(updateSql);
			return 1;
		}else{
			return -1;
		}
		
	}
	
//	---------------------------------------------save end--------------------------------------------------
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}


	private boolean _Refreshclick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	private boolean _Cpyclick = false;

	public void CpyButton(IRequestCycle cycle) {
		_Cpyclick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
		}
		if(_Cpyclick){
			_Cpyclick=false;
			CopyData();
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String sql=
			"SELECT ID,\n" +
			"       DIANCXXB_ID,\n" + 
			"       RIQ,TBRQ,\n" + 
			"       ZAFMC,\n" + 
			"       YUCJE,\n" + 
			"       YUCSM,\n" +
			" (SHIJWCJE+YUJWCJE) AS shnyjwcje,\n" + 
			"       SHIJWCJE,\n" + 
			"       YUJWCJE,\n" + 
			"       YUJWCSM,\n" + 
			"       ZHUANGT\n" + 
			"  FROM NIANDJH_ZAF\n" + 
			" WHERE DIANCXXB_ID = "+this.getTreeid()+"\n" + 
			"   AND RIQ = TO_DATE('"+this.getNianfValue().getValue()+"-01-01','yyyy-mm-dd')\n" +
			"	ORDER BY RIQ,ID";

		ResultSetList rsl = con.getResultSetList(sql);
			
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("NIANDJH_ZAF");
		egu.setWidth("bodyWidth");

		String riq=DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(new Date()));
		egu.getColumn("diancxxb_id").setHeader("�糧����");
		egu.getColumn("diancxxb_id").setDefaultValue(this.getTreeid());
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("diancxxb_id").setHidden(true);
		
		egu.getColumn("RIQ").setHeader("����");
		egu.getColumn("RIQ").setEditor(null);
		egu.getColumn("RIQ").setHidden(true);
		egu.getColumn("RIQ").setDefaultValue(this.getNianfValue().getValue()+"-01-01");
		egu.getColumn("RIQ").setWidth(50);
		
		egu.getColumn("TBRQ").setHeader("�����");
		egu.getColumn("TBRQ").setEditor(null);
		egu.getColumn("TBRQ").setHidden(true);
		egu.getColumn("TBRQ").setDefaultValue(riq);
		egu.getColumn("TBRQ").setWidth(50);
		
		egu.getColumn("ZAFMC").setCenterHeader("��������");
		egu.getColumn("ZAFMC").setWidth(150);
		egu.getColumn("ZAFMC").editor.setAllowBlank(false);
		
		egu.getColumn("YUCJE").setCenterHeader((this.getNianfValue().getValue())+"��<br>Ԥ����<br>(Ԫ)");
		egu.getColumn("YUCJE").setWidth(100);
		egu.getColumn("YUCJE").setDefaultValue("0");
		egu.getColumn("YUCJE").editor.setAllowBlank(false);

		egu.getColumn("YUCSM").setCenterHeader("Ԥ����ȷ���˵��");
		egu.getColumn("YUCSM").setWidth(250);
		egu.getColumn("YUCSM").editor.setAllowBlank(false);
		
		egu.getColumn("shnyjwcje").setCenterHeader("����Ԥ��<br>��ɽ��<br>(Ԫ)");
		egu.getColumn("shnyjwcje").setDefaultValue("0");
		egu.getColumn("shnyjwcje").setWidth(100);
		egu.getColumn("shnyjwcje").setEditor(null);
		egu.getColumn("shnyjwcje").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("shnyjwcje").setUpdate(false);
		egu.getColumn("SHIJWCJE").setCenterHeader("��������ɽ��<br>(Ԫ)");
		egu.getColumn("SHIJWCJE").setDefaultValue("0");
		egu.getColumn("SHIJWCJE").setWidth(100);
		
		egu.getColumn("YUJWCJE").setCenterHeader("����δ��ɽ��<br>(Ԫ)");
		egu.getColumn("YUJWCJE").setDefaultValue("0");
		egu.getColumn("YUJWCJE").setWidth(100);
		
		egu.getColumn("YUJWCSM").setCenterHeader("����������˵��");
		egu.getColumn("YUJWCSM").setWidth(250);
		
		egu.getColumn("zhuangt").setHeader("״̬");
		egu.getColumn("zhuangt").setDefaultValue("0");
		egu.getColumn("zhuangt").setHidden(true);
		egu.getColumn("zhuangt").setEditor(null);
		
//		�ӷ�����������
		String cbosql="SELECT ID, MINGC\n" +
			"  FROM ITEM\n" + 
			" WHERE ITEMSORTID IN (SELECT ID FROM ITEMSORT WHERE BIANM = 'ZAFMC')\n" + 
			" ORDER BY XUH";
		egu.getColumn("ZAFMC").setEditor(new ComboBox());
		egu.getColumn("ZAFMC").setComboEditor(egu.gridId, new IDropDownModel(cbosql));
		egu.getColumn("ZAFMC").setReturnId(false);//���ˣ�ҳ����ʾ���֣�������ʾ���֡�
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(0);
		egu.setDefaultsortable(false);//�趨ҳ�治�Զ�����
		
//		������
		egu.addTbarText("Ԥ�����:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NianfDropdown");
		comb1.setId("NianfDropdown");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(80);
		egu.addToolbarItem(comb1.getScript());
		egu.addTbarText("-");
//		�糧��
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarText("-");// ���÷ָ���
		egu.addTbarText("��λ:");
		egu.addTbarTreeBtn("diancTree");
		
//		ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'����ˢ������,���Ժ�'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
//		grid����ƥ�䷽��
		String script="gridDiv_grid.on('afteredit',function(e)\n"+
				"{\n"+
			"rec = e.record;\n"+
			"var zafmc=rec.get('ZAFMC');\n"+
			"if(e.field=='ZAFMC')\n"+
			"{\n"+
			"	for (var i=0;i<zafxx.length;i++ ){\n"+
			"		if(zafxx[i][0]==zafmc){\n"+
			"			rec.set('SHIJWCJE',zafxx[i][1]);\n"+
			"			}\n"+
			"	}	\n"+
			"}\n"+
			"	 var shjwcje=parseFloat(e.record.get('SHIJWCJE')==''?0:e.record.get('SHIJWCJE'));\n"+
			"	var yujwcje=parseFloat(e.record.get('YUJWCJE')==''?0:e.record.get('YUJWCJE'));\n"+
			"	var shnyjwcje=shjwcje+yujwcje;\n"+
			"	 e.record.set('SHNYJWCJE',shnyjwcje);\n"+
			"});\n";
		
		egu.addOtherScript(script);
		
		
		
		
//		 �ж������Ƿ��Ѿ��ϴ� ������ϴ� �����޸� ɾ�� �������
		if (getZhangt(con)) {
			if(visit.getDiancxxb_id()==112){
				egu.addTbarText("-");
				egu.addToolbarButton(GridButton.ButtonType_Insert, null);
				egu.addToolbarButton(GridButton.ButtonType_Delete, null);
				egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
			}else{
				setMsg("�����Ѿ��ϴ���������ϵ�ϼ���λ����֮����ܲ�����");
			}
		} else {
			if(visit.isFencb() && isParentDc(con) && MainGlobal.getXitxx_item("�ƻ�ģ��", "�ֳ����ܳ���ʾ��ť", this.getTreeid(), "��").equals("��")){
				
			}else{
				egu.addTbarText("-");
				egu.addToolbarButton(GridButton.ButtonType_Insert, null);
				egu.addToolbarButton(GridButton.ButtonType_Delete, null);
				egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
//				���������û����������ʾ���ư�ť
				if(!con.getHasIt(sql)){
					String handler="function (){document.getElementById('CpyButton').click();"+
					"Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...'," +
					"progressText:'������...',width:300,wait:true,waitConfig: " +
					"{interval:200},icon:Ext.MessageBox.INFO});}";
					GridButton cpy = new GridButton("��������ȼƻ�", handler);
					cpy.setIcon(SysConstant.Btn_Icon_Copy);
					egu.addTbarBtn(cpy);
				}
			}
		}
		setExtGrid(egu);
		con.Close();
	}
	
	/**
	 * @param con
	 * @return true:���ϴ�״̬�� �����޸����� false:δ�ϴ�״̬�� �����޸�����
	 */
	private boolean getZhangt(JDBCcon con) {
		String CurrODate = DateUtil.FormatOracleDate(this.getNianfValue().getValue() + "-01-01");
		String sql = "select s.zhuangt zhuangt\n" + "  from NIANDJH_ZAF s\n"
				+ " where  s.diancxxb_id = " + getTreeid() + "\n"
				+ "   and s.riq = " + CurrODate;
		ResultSetList rs = con.getResultSetList(sql);
		boolean zt = true;
		if (con.getHasIt(sql)) {
			while (rs.next()) {
				if (rs.getInt("zhuangt") == 0 || rs.getInt("zhuangt") == 2) {
					zt = false;
				}
			}
		} else {
			zt = false;
		}
		return zt;
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
	
	public void setZafxx(){
		Visit visit = (Visit) getPage().getVisit();
		String riq=DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(new Date()));
		String rslSQL="SELECT ZAFLX.MINGC, NVL(ZAFJE.JINE, 0) ZAFJE\n" +
			"  FROM (SELECT XUH, MINGC\n" + 
			"          FROM ITEM\n" + 
			"         WHERE ITEMSORTID IN (SELECT ID FROM ITEMSORT WHERE BIANM = 'ZAFMC')) ZAFLX,\n" + 
			"       (SELECT SUM(JINE) JINE, MINGC\n" + 
			"          FROM ZAFB\n" + 
			"         WHERE RIQ BETWEEN TRUNC(DATE '"+riq+"', 'yyyy') AND DATE'"+riq+"'\n" + 
			"         GROUP BY MINGC) ZAFJE\n" + 
			" WHERE ZAFLX.MINGC = ZAFJE.MINGC(+)\n" + 
			" ORDER BY ZAFLX.XUH";
		
		JDBCcon con = new JDBCcon();
		ResultSetList list=con.getResultSetList(rslSQL.toString());
		String rsl="var zafxx=[ ";
		while(list.next()){
			String ZAFMC=list.getString("MINGC");
			String ZAFJE=list.getString("ZAFJE");
			rsl+="[\""+ZAFMC+"\",\""+ZAFJE+"\"],";
		}
		rsl=rsl.substring(0, rsl.length()-1);
		rsl+="];";
		list.close();
		con.Close();
		visit.setString3(rsl);
	}
	
	public String getZafxx(){
		Visit visit = (Visit) getPage().getVisit();
		if(visit.getString3()==null ||visit.getString3().equals("")){
			setZafxx();
		}
		return visit.getString3();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
//			 �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			this.setTreeid(null);
			getTreeid();
			this.setNianfValue(null);
			this.getNianfModels();
			setZafxx();
		}
		getSelectData();

	}
	
//	���������
	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			int _nianf = DateUtil.getYear(new Date())+1;
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					((Visit) getPage().getVisit()).setDropDownBean1((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setNianfValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setNianfModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2009; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit()).setProSelectionModel1(new IDropDownModel(listNianf));
	}
	

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

}
