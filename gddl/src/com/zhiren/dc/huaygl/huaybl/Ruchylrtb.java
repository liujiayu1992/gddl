package com.zhiren.dc.huaygl.huaybl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.DatetimeField;
import com.zhiren.dc.huaygl.Compute;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * �޸�ʱ�䣺2009-09-18
 * �޸��ˣ�  ww
 * �޸����ݣ�
 * 			1 ���ӳ����ж�
 * 			�ڻ�ȡԪ�ط���ʱû�м��볧����жϣ��޷���ȡԪ�ط�����Ŀ��ֵ
 INSERT INTO xitxxb VALUES(
 getnewid(diancxbid),
 1,
 diancxbid,
 '�ֳ�ͬ����ֵ��ͬ',
 '��',
 '',
 '����',
 1,
 'ʹ��'
 )
 
 *          2 ��Ӳ���ʱ��,Ĭ�ϲ���ʾ
 INSERT INTO xitxxb VALUES(
 getnewid(diancxbid),
 1,
 diancxbid,
 '����¼����ʾ��������',
 '��',
 '',
 '����',
 1,
 'ʹ��'
 )
 */

/*����:���ܱ�
 * ʱ��:2009-10-26 10:33:14
 * �޸�����:ˢ�¼����������ڵ��ж�,ֻ�������糧�����ַ���,
 *         ��ʱ�������糧������
 * 
 * 
 * 
 */

/* �޸�ʱ��:2010-01-25 
 * ��Ա��liht
 * �޸�����:����ʱ��¼��ʱ��ԭ�������ջ����Ͼ�ȷ��ʱ����
 * 
 * 
 * 
 */
public class Ruchylrtb extends BasePage implements PageValidateListener {
//	 ������
	private String riq1;
	public String getRiq1() {
		if (riq1 == null || riq1.equals("")) {
			setRiq1(DateUtil.FormatDate(new Date()));
		}
		return riq1;
	}
	public void setRiq1(String riq1) {
		this.riq1 = riq1;
	}

	private String riq2;
	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			setRiq2(DateUtil.FormatDate(new Date()));
		}
		return riq2;
	}
	public void setRiq2(String riq2) {
		this.riq2 = riq2;
	}
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

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		String sql = "";
		sql =
			"select nvl(zhilb.id,0)zhilb_id0,fahb.zhilb_id,to_char(fahb.daohrq,'yyyy-mm-dd')daohrq,luncxxb.mingc||'/'||fahb.chec quanm,g.mingc mkmc,pinzb.mingc pinz,fahb.biaoz,\n" + 
			"zhilb.mt,--1\n" + 
			"zhilb.mad,--2\n" + 
			"zhilb.aad,--3\n" + 
			"zhilb.vad,--4\n" + 
			"zhilb.fcad,--5\n" + 
			"zhilb.stad,--6\n" + 
			"zhilb.qnet_ar,--7\n" + 
			"  round_new(zhilb.aad * (100 - zhilb.mt) / (100 - zhilb.mad),2)aar,--31aar�յ���\n" + 
			"  round_new(zhilb.aad  * 100 / (100 - zhilb.mad),2)ad,--32ad �����\n" + 
			"  round_new(zhilb.vad * 100 / (100 - zhilb.mad -  zhilb.aad ),2)vdaf,--41vadf�ո���㼯\n" + 
			"  round_new(zhilb.vad * (100 - zhilb.mt) / (100 - zhilb.mad),2)var,--42var�յ���\n" + 
			"  round_new(zhilb.stad  * 100 / (100 - zhilb.mad),2)std,--61std�����\n" + 
			"  round_new(zhilb.stad * 100 / (100 - zhilb.mad -  zhilb.aad ),2)sdaf,--62stdaf�ո���㼯\n" + 
			"----------------\n" + 
			"zhilb.hdaf,zhilb.had,zhilb.qgrad_daf,zhilb.qgrad,zhilb.qbad\n" + 
			"from fahb,zhilb,luncxxb,meikxxb g,pinzb\n" + 
			"where fahb.pinzb_id=pinzb.id and fahb.zhilb_id=zhilb.id(+) and fahb.luncxxb_id=luncxxb.id and fahb.meikxxb_id=g.id " +
			"and to_char(fahb.daohrq,'yyyy-mm-dd')>='"+getRiq1()+"' and to_char(fahb.daohrq,'yyyy-mm-dd')<='"+getRiq2()+"' order by fahb.id";
	
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //���ñ��������ڱ���
		egu.setTableName("zhilb");
		// ����ҳ�����Ա����ӹ�����
		egu.setWidth("bodyWidth");
//		 ����GRID�Ƿ���Ա༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// /������ʾ������
		egu.getColumn("zhilb_id0").setHeader("����1");
		egu.getColumn("zhilb_id0").hidden=true;
		egu.getColumn("zhilb_id0").setEditor(null);
		egu.getColumn("zhilb_id").setHeader("����2");
		egu.getColumn("zhilb_id").hidden=true;
		egu.getColumn("zhilb_id").setEditor(null);
		egu.getColumn("daohrq").setHeader("��������");
		egu.getColumn("daohrq").setWidth(70);
		egu.getColumn("daohrq").setEditor(null);
		egu.getColumn("quanm").setHeader("����/����");
		egu.getColumn("quanm").setWidth(100);
		egu.getColumn("quanm").setEditor(null);
		egu.getColumn("mkmc").setHeader("���");
		egu.getColumn("mkmc").setWidth(100);
		egu.getColumn("mkmc").setEditor(null);
		egu.getColumn("pinz").setHeader("Ʒ��");
		egu.getColumn("pinz").setWidth(70);
		egu.getColumn("pinz").setEditor(null);
		egu.getColumn("biaoz").setHeader("�˵���");
		egu.getColumn("biaoz").setWidth(70);
		egu.getColumn("biaoz").setEditor(null);
		
		egu.getColumn("mt").setHeader("ȫˮMt(%)");
		egu.getColumn("mt").setWidth(70);
		egu.getColumn("mad").setHeader("�ոɻ�ˮMad(%)");
		egu.getColumn("mad").setWidth(70);
		egu.getColumn("aad").setHeader("�ոɻ��ҷ�Aad(%)");
		egu.getColumn("aad").setWidth(70);
		egu.getColumn("vad").setHeader("�ոɻ��ӷ���Vad(%)");
		egu.getColumn("vad").setWidth(70);
		egu.getColumn("fcad").setHeader("�̶�̼(%)");
		egu.getColumn("fcad").setWidth(70);
		egu.getColumn("stad").setHeader("�ոɻ����(%)");
		egu.getColumn("stad").setWidth(70);
		egu.getColumn("qnet_ar").setHeader("�յ���������(Mj/Kg)");
		egu.getColumn("qnet_ar").setWidth(70);
		//����
		egu.getColumn("aar").setHeader("�յ����ҷ�(%)");
		egu.getColumn("aar").setEditor(null);
		egu.getColumn("ad").setHeader("������ҷ�(%)");
		egu.getColumn("ad").setEditor(null);
		egu.getColumn("vdaf").setHeader("�����޻һ��ӷ���(%)");
		egu.getColumn("vdaf").setEditor(null);
		egu.getColumn("var").setHeader("�յ����ӷ���(%)");
		egu.getColumn("var").setEditor(null);
		egu.getColumn("std").setHeader("��������(%)");
		egu.getColumn("std").setEditor(null);
		egu.getColumn("sdaf").setHeader("�����޻һ����(%)");
		egu.getColumn("sdaf").setEditor(null);
		//��ѡ
		egu.getColumn("hdaf").setHeader("�����޻һ���(%)");
		egu.getColumn("had").setHeader("�ոɻ���(%)");
		egu.getColumn("qgrad_daf").setHeader("�����޻һ���λ��ֵ(Mj/Kg)");
		egu.getColumn("qgrad").setHeader("��λ��ֵ(Mj/Kg)");
		egu.getColumn("qbad").setHeader("��Ͱ����(Mj/Kg)");
		 
//		�������ڲ�ѯ
		egu.addTbarText("��������:");
		DateField df = new DateField();
		df.setValue(this.getRiq1());
		df.Binding("riq1", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq1");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");
		egu.addTbarText("��:");
		DateField df1 = new DateField();
//		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		
		egu.addTbarText("-");
		GridButton refurbish = new GridButton("ˢ��",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
//		GridButton baocButton = new GridButton("����",
//		"function (){document.getElementById('SaveButton').click();}");
//		baocButton.setIcon(SysConstant.Btn_Icon_Save);
//		egu.addTbarBtn(baocButton);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		setExtGrid(egu);
		con.Close();
	}

	public void Save(String strchange) {
		/*zhilb.aad * (100 - zhilb.mt) / (100 - zhilb.mad)aar,--31aar�յ���
		zhilb.aad  * 100 / (100 - zhilb.mad)ad,--32ad �����
		zhilb.vad * 100 / (100 - zhilb.mad -  zhilb.aad )vdaf,--41vadf�ո���㼯
		zhilb.vad * (100 - zhilb.mt) / (100 - zhilb.mad)var,--42var�յ���
		zhilb.stad  * 100 / (100 - zhilb.mad)std,--61std�����
		zhilb.stad * 100 / (100 - zhilb.mad -  zhilb.aad )sdaf,--62stdaf�ո���㼯*/
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql="";
		String strMs="";
//		if(strchange.equals("")){strMs="û���κθ��ģ�";setMsg(strMs);return;};
		ResultSetList cMrs = getExtGrid().getModifyResultSet(strchange);
		while (cMrs.next()) {
			long zhilb_id0 = Long.parseLong(cMrs.getString("zhilb_id0"));
			long zhilb_id = Long.parseLong(cMrs.getString("zhilb_id"));
			String  huaybh = cMrs.getString("daohrq")+cMrs.getString("quanm");
			
			double mt = cMrs.getDouble("mt");
			double mad = cMrs.getDouble("mad");
			double aad = cMrs.getDouble("aad");
			double vad = cMrs.getDouble("vad");
			double fcad = cMrs.getDouble("fcad");
			double stad = cMrs.getDouble("stad");
			double qnet_ar = cMrs.getDouble("qnet_ar");
			
			double aar=CustomMaths.Round_new(aad* (100 - mt) / (100 - mad), 2);
			double ad=CustomMaths.Round_new(aad  * 100 / (100 - mad),2);
			double vdaf=CustomMaths.Round_new(vad * 100 / (100 - mad -  aad ),2);
			double var=CustomMaths.Round_new(vad * (100 - mt) / (100 - mad),2);
			double std=CustomMaths.Round_new(stad  * 100 / (100 - mad),2);
			double sdaf=CustomMaths.Round_new(stad * 100 / (100 - mad -  aad ),2);
			double star=CustomMaths.Round_new(stad* (100 - mt) / (100 - mad),2);
			
			double hdaf = cMrs.getDouble("hdaf");
			double had = cMrs.getDouble("had");
			double qgrad_daf = cMrs.getDouble("qgrad_daf");
			double qgrad = cMrs.getDouble("qgrad");
			double qbad = cMrs.getDouble("qbad");
			if(zhilb_id0==0){//����ǵ�һ��¼�뻯��ֵ
				sql="insert into zhilb\n" +
				"  (id,\n" + 
				"   huaybh,\n" + 
				"   caiyb_id,\n" + 
				"   huaysj,\n" + 
				"   qnet_ar,\n" + 
				"   aar,\n" + 
				"   ad,\n" + 
				"   vdaf,\n" + 
				"   mt,\n" + 
				"   stad,\n" + 
				"   aad,\n" + 
				"   mad,\n" + 
				"   qbad,\n" + 
				"   had,\n" + 
				"   vad,\n" + 
				"   fcad,\n" + 
				"   std,\n" + 
				"   qgrad,\n" + 
				"   hdaf,\n" + 
				"   qgrad_daf,\n" + 
				"   sdaf,\n" + 
				"   var,\n" + 
				"   huayy,\n" + 
				"   lury,\n" + 
				"   shenhzt,\n" + 
				"   liucztb_id,\n" + 
				"   star)\n" + 
				"values\n" + 
				"  ("+zhilb_id+",\n" + 
				"   '"+huaybh+"',\n" + 
				"   0,\n" + 
				"   sysdate,\n" + 
				"   "+qnet_ar+",\n" + 
				"   "+aar+",\n" + 
				"   "+ad+",\n" + 
				"   "+vdaf+",\n" + 
				"   "+mt+",\n" + 
				"   "+stad+",\n" + 
				"   "+aad+",\n" + 
				"   "+mad+",\n" + 
				"   "+qbad+",\n" + 
				"   "+had+",\n" + 
				"   "+vad+",\n" + 
				"   "+fcad+",\n" + 
				"   "+std+",\n" + 
				"   "+qgrad+",\n" + 
				"   "+hdaf+",\n" + 
				"   "+qgrad_daf+",\n" + 
				"   "+sdaf+",\n" + 
				"   "+var+",\n" + 
				"   '"+visit.getRenymc()+"',\n" + 
				"    '"+visit.getRenymc()+"',\n" + 
				"   1,\n" + 
				"   1,\n" + 
				"   "+star+")";
				con.getInsert(sql);

			}else{//���Ѿ����ڻ���ֵ���и���

				sql="update zhilb\n" +
				"   set  "+
				"       qnet_ar = "+qnet_ar+",\n" + 
				"       aar = "+aar+",\n" + 
				"       ad = "+ad+",\n" + 
				"       vdaf = "+vdaf+",\n" + 
				"       mt = "+mt+",\n" + 
				"       stad = "+stad+",\n" + 
				"       aad = "+aad+",\n" + 
				"       mad = "+mad+",\n" + 
				"       qbad = "+qbad+",\n" + 
				"       had = "+had+",\n" + 
				"       vad = "+vad+",\n" + 
				"       fcad = "+fcad+",\n" + 
				"       std = "+std+",\n" + 
				"       qgrad = "+qgrad+",\n" + 
				"       hdaf = "+hdaf+",\n" + 
				"       qgrad_daf = "+qgrad_daf+",\n" + 
				"       sdaf = "+sdaf+",\n" + 
				"       var = "+var+",\n" + 
				"       star = "+star+"\n" + 
				" where id = "+zhilb_id0;
				con.getUpdate(sql);
			}
		}
		con.Close();
		strMs="����ɹ���";
		setMsg(strMs);
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
			Save(getChange());
			getSelectData();
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
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

	public String gridId;

	public int pagsize;

	public int getPagSize() {
		return pagsize;
	}

	public String getGridScriptLoad() {
		StringBuffer gridScript = new StringBuffer();
		gridScript.append(gridId).append("_grid.render();");
		if (getPagSize() > 0) {
			gridScript.append(gridId).append(
					"_ds.load({params:{start:0, limit:").append(getPagSize())
					.append("}});\n");
			gridScript.append(gridId).append(
					"_ds.on('datachanged',function(){ ").append(gridId).append(
					"_history = ''; });");
		} else {
			gridScript.append(gridId).append("_ds.load();");
		}
		return gridScript.toString();
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
			setExtGrid(null);
			visit.setString6(DateUtil.FormatDate(new Date()));
		}
		getSelectData();
	}
}