package com.zhiren.dc.zhuangh.huaybl;

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

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.DatetimeField;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.common.ext.tree.TreeOperation;

import com.zhiren.dc.huaygl.Compute;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ���ߣ�����
 * ʱ�䣺2010-04-02
 * �������������ܣ�1.�÷�������ƹ���ƶ�
 * 				  2.ȥ�����ֶΣ�����ύʱ�Զ�������ֵ
 * 		���÷�Χ����������
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-09-04 14��29
 * ����������������ExtGrid����������������Ĳ���Ԥ֪�Թ�ȥ���˻س����TAB���ķ���
 */
/*
 * ����:���	
 * ʱ��:2009-5-11
 * �޸�����:��֤�Ƿ����� �������
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-06-26
 * ����������xitxxb���ã���Լ��λ��������С��λ Ĭ��Ϊ3λ��������
 * 		mingc	= '��λ������С��λ'
 * 		zhi		= С��λ
 * 		leib 	= '����'
 * 		zhuangt = 1
 * 		diancxxb_id = �糧ID
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-07-27
 * ������������Ҫָ�갴�س���������һ��������Ĺ���
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-08-11 16��53
 * ����������ExtGrid�����ÿ���ʾ������
 */

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
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-09-01
 * ����������ֵ¼���������¯���볧��Had�ӻ���Ԫ�ط�����ȡ�ã������ݻ���ʱ���Զ�ƥ���Ӧ�������ڵ�HadԪ�ص�ֵ��
 * 		 ��������Had���û��ֶ�¼�롣
 */
public class Ruchylr extends BasePage implements PageValidateListener {

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

	public String getKuangm() {
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setKuangm(String kuangm) {
		((Visit) this.getPage().getVisit()).setString1(kuangm);
	}

	public String getBianh() {
		return ((Visit) this.getPage().getVisit()).getString2();
	}

	public void setBianh(String bianh) {
		((Visit) this.getPage().getVisit()).setString2(bianh);
	}

	public String getShul() {
		return ((Visit) this.getPage().getVisit()).getString3();
	}

	public void setShul(String shul) {
		((Visit) this.getPage().getVisit()).setString3(shul);
	}

	public String getHuaysj() {
		return ((Visit) this.getPage().getVisit()).getString4();
	}

	public void setHuaysj(String huaysj) {
		((Visit) this.getPage().getVisit()).setString4(huaysj);
	}

	// �����ֺ���Χ����ȡ�� Mt, Mad��Aad,Vad,Std,Qbad�ķ�Χ
	public ResultSetList getmaxmin(JDBCcon con, long diancxxb_id) {
		ResultSetList shuzhirsl;
		String sql = "select shangx,xiax,mingc from shuzhlfwb "
				+ "where leib = '����' and beiz='����¼��'";
		shuzhirsl = con.getResultSetList(sql);
		return shuzhirsl;
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = "";

		// ��ֵ����Χ
		ResultSetList szhlfw = getmaxmin(con, visit.getDiancxxb_id());
		// ת������ж�Ӧ��������ID��
		String zhuanmlbid = "";
		sql = "select id from zhuanmlb where mingc = '�������'";
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			zhuanmlbid = rsl.getString("id");
		}
		rsl.close();
		if ("".equals(zhuanmlbid)) {
			WriteLog.writeErrorLog(ErrorMessage.ZhuanmlbNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.ZhuanmlbNotFound);
			return;
		}

		String ssql = "select b.id,b.huaybh,to_char(b.huaysj,'YYYY-MM-DD hh24:mi:ss') huaysj,b.mt,b.mad,b.aad,b.vad,b.stad,DECODE(NVL(b.had,0),0,GETYSFX_ZHILLSB(B.ID,'Had'),b.had)HAD,b.qbad,b.t1,b.t2,b.t3,b.t4,b.huayy,b.lury,b.beiz ,b.huaylb from\n"
				+ "(\n"
				+ "select  z.id,zb.bianm as huaybh ,   nvl(z.huaysj,sysdate) huaysj,\n"
				+ "        z.mt,z.mad,z.aad,z.vad,z.stad,z.had,z.qbad,z.t1,z.t2,z.t3,z.t4,z.huayy,z.lury,z.beiz,z.huaylb \n"
				+ " from yangpdhb yb ,zhillsb z,zhuanmb zb,zhuanmlb zlb\n"
				+ "  where yb.zhilblsb_id=z.id\n"
				+ "  and yb.zhilblsb_id=zb.zhillsb_id\n"
				+ "  and zb.zhuanmlb_id=zlb.id\n"
				+ "  and zlb.jib=2\n"
				+ "  and zlb.diancxxb_id="
				+ visit.getDiancxxb_id()
				+ "\n"
				+ "  and z.shenhzt=0\n"
				+ "  ) a,\n"
				+ "  (\n"
				+ "    select z.id,zb.bianm as huaybh ,   nvl(z.huaysj,sysdate) huaysj,\n"
				+ "        z.mt,z.mad,z.aad,z.vad,z.stad,z.had,z.qbad,z.t1,z.t2,z.t3,z.t4,z.huayy,z.lury,z.beiz,z.huaylb \n"
				+ " from yangpdhb yb ,zhillsb z,zhuanmb zb,zhuanmlb zlb\n"
				+ "  where yb.zhilblsb_id=z.id\n"
				+ "  and yb.zhilblsb_id=zb.zhillsb_id\n"
				+ "  and zb.zhuanmlb_id=zlb.id\n"
				+ "  and zlb.jib=3\n"
				+ "  and zlb.diancxxb_id="
				+ visit.getDiancxxb_id()
				+ "\n"
				+ "  and z.shenhzt=0\n"
				+ "  ) b\n"
				+ "  where b.id=a.id(+)\n"
				+ "\n"
				+ "   union\n"
				+ "   select distinct c.zhilb_id as id,c.huaybh,to_char(min(c.huaysj),'YYYY-MM-DD hh24:mi:ss') huaysj,c.mt,c.mad,c.aad,c.vad,c.stad,DECODE(NVL(C.had,0),0,GETYSFX_RULMZLZB(c.zhilb_id,'Had'),C.had)HAD,c.qbad,c.t1,c.t2,c.t3,c.t4,c.huayy,c.lury,c.beiz,c.huaylb \n"
				+ "   from\n"
				+ "  ( select distinct rb.zhuanmbzllsb_id zhilb_id,zb.bianm as huaybh, nvl(rb.FENXRQ,sysdate) huaysj,\n"
				+ "          rb.mt,rb.mad,rb.aad,rb.vad,rb.stad,rb.had,rb.qbad, 0 as  t1,0 as t2,0 as t3,0 as t4   ,rb.huayy,rb.lury,rb.beiz,'��¯' as huaylb\n"
				+ "   from yangpdhb yb,rulmzlzmxb rb,zhuanmb zb,zhuanmlb zlb\n"
				+ "   where yb.zhilblsb_id=rb.zhuanmbzllsb_id\n"
				+ "   and zb.zhillsb_id=rb.zhuanmbzllsb_id\n"
				+ "   and zb.zhuanmlb_id=zlb.id\n"
				+ "   and zlb.jib=3\n"
				+ "   and zlb.diancxxb_id="
				+ visit.getDiancxxb_id()
				+ "\n"
				+ "   and rb.shenhzt=0\n"
				+ "   ) c,\n"
				+ "   (\n"
				+ "        select distinct rb.zhuanmbzllsb_id zhilb_id,zb.bianm as huaybh, nvl(rb.FENXRQ,sysdate) huaysj,\n"
				+ "          rb.mt,rb.mad,rb.aad,rb.vad,rb.stad,rb.had,rb.qbad, 0 as  t1,0 as t2,0 as t3,0 as t4   ,rb.huayy,rb.lury,rb.beiz,'��¯' as huaylb \n"
				+ "   from yangpdhb yb,rulmzlzmxb rb,zhuanmb zb,zhuanmlb zlb\n"
				+ "   where yb.zhilblsb_id=rb.zhuanmbzllsb_id\n"
				+ "   and zb.zhillsb_id=rb.zhuanmbzllsb_id\n"
				+ "   and zb.zhuanmlb_id=zlb.id\n"
				+ "   and zlb.jib=2\n"
				+ "   and zlb.diancxxb_id="
				+ visit.getDiancxxb_id()
				+ "\n"
				+ "   and rb.shenhzt=0\n"
				+ "   ) d\n"
				+ "   where c.zhilb_id=d.zhilb_id(+) group by (c.zhilb_id ,c.huaybh,c.mt,c.mad,c.aad,c.vad,c.stad,c.had,c.qbad,c.t1,c.t2,c.t3,c.t4,c.huayy,c.lury,c.beiz,c.huaylb)";

		
		rsl = con.getResultSetList(ssql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);

		// //���ñ��������ڱ���
		egu.setTableName("zhillsb");
		// ����ҳ�����Ա����ӹ�����
		egu.setWidth("bodyWidth");
		// /������ʾ������
		egu.getColumn("huaybh").setHeader("������");
		egu.getColumn("huaybh").setEditor(null);
		egu.getColumn("huaybh").setWidth(80);
		
		egu.getColumn("huaysj").setCenterHeader("����ʱ��");
		egu.getColumn("huaysj").setWidth(120);
		egu.getColumn("huaysj").editor.setAllowBlank(false);
		DatetimeField datetime = new DatetimeField();
		egu.getColumn("huaysj").setEditor(datetime);
		egu.getColumn("huaysj").setDefaultValue(DateUtil.FormatDateTime(new Date()));
		egu.getColumn("huaysj").setEditor(null);
		egu.getColumn("huaysj").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("mt").setHeader("ȫˮ��Mt(%)");
		egu.getColumn("mt").setWidth(80);
		egu.getColumn("mt").editor.setMaxValue("100");
		egu.getColumn("mt").editor.setMinValue("1");
		egu.getColumn("mt").setColtype(GridColumn.ColType_default);
		egu.getColumn("mt").editor.setAllowBlank(false);
		
		egu.getColumn("mad").setHeader("ˮ��Mad(%)");
		egu.getColumn("mad").setWidth(80);
		egu.getColumn("mad").editor.setMaxValue("100");
		egu.getColumn("mad").editor.setMinValue("1");
		egu.getColumn("mad").setColtype(GridColumn.ColType_default);
		egu.getColumn("mad").editor.setAllowBlank(false);
		
		egu.getColumn("aad").setHeader("�ҷ�Aad(%)");
		egu.getColumn("aad").setWidth(80);
		egu.getColumn("aad").editor.setMaxValue("100");
		egu.getColumn("aad").editor.setMinValue("1");
		egu.getColumn("aad").setColtype(GridColumn.ColType_default);
		egu.getColumn("aad").editor.setAllowBlank(false);
		
		egu.getColumn("vad").setHeader("�ӷ���Vad(%)");
		egu.getColumn("vad").setWidth(90);
		egu.getColumn("vad").editor.setMaxValue("100");
		egu.getColumn("vad").editor.setMinValue("1");
		egu.getColumn("vad").setColtype(GridColumn.ColType_default);
		egu.getColumn("vad").editor.setAllowBlank(false);
		
		egu.getColumn("qbad").setHeader("��Ͳ����Qbad(Mj/Kg)");
		egu.getColumn("qbad").setWidth(100);
		egu.getColumn("qbad").editor.setMaxValue("100");
		egu.getColumn("qbad").editor.setMinValue("1");
		egu.getColumn("qbad").setColtype(GridColumn.ColType_default);
		egu.getColumn("qbad").editor.setAllowBlank(false);
		
		egu.getColumn("stad").setHeader("���" + "Stad" + "(%)");
		egu.getColumn("stad").setWidth(80);
		egu.getColumn("stad").editor.setMaxValue("100");
		egu.getColumn("stad").editor.setMinValue("0.001");
		egu.getColumn("stad").setColtype(GridColumn.ColType_default);
		egu.getColumn("stad").editor.setAllowBlank(false);
		
		egu.getColumn("HAD").setHeader("��Had(%)");
		egu.getColumn("HAD").setWidth(80);
		egu.getColumn("HAD").editor.setAllowBlank(false);
		egu.getColumn("HAD").editor.setMaxValue("100");
		egu.getColumn("HAD").editor.setMinValue("0.001");
//		egu.getColumn("HAD").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("t1").setHeader("T1(��)");
		egu.getColumn("t1").setHidden(true);
		egu.getColumn("t1").setWidth(80);
		egu.getColumn("t1").setDefaultValue("20");
		
		egu.getColumn("t2").setHeader("T2(��)");
		egu.getColumn("t2").setHidden(true);
		egu.getColumn("t2").setWidth(80);
		egu.getColumn("t2").setDefaultValue("20");
		
		egu.getColumn("t3").setHeader("T3(��)");
		egu.getColumn("t3").setHidden(true);
		egu.getColumn("t3").setWidth(80);
		egu.getColumn("t3").setDefaultValue("20");
		
		egu.getColumn("t4").setHeader("T4(��)");
		egu.getColumn("t4").setHidden(true);
		egu.getColumn("t4").setWidth(80);
		egu.getColumn("t4").setDefaultValue("20");
		
		egu.getColumn("huayy").setCenterHeader("����Ա");
		egu.getColumn("huayy").setWidth(150);
		egu.getColumn("huayy").setEditor(null);
		
		egu.getColumn("lury").setHeader("����¼��Ա");
		egu.getColumn("lury").setWidth(80);
		egu.getColumn("lury").setEditor(null);
		
		egu.getColumn("beiz").setHeader("���鱸ע");
		egu.getColumn("beiz").setWidth(80);
		
		egu.getColumn("huaylb").setHeader("�������");
		egu.getColumn("huaylb").setEditor(null);


//		 ��������û����õķ�Χ����ô���ǳ���Ĭ��
		while (szhlfw.next()) {
			GridColumn gc = egu.getColumn(szhlfw.getString("mingc"));
			if (gc != null) {
				gc.editor.setMaxValue(szhlfw.getString("shangx"));
				gc.editor.setMinValue(szhlfw.getString("xiax"));
			}
		}

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

		egu.addTbarText("��λ����:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");

		egu.addTbarText("-");

		String treepanel =
			"var navtree = new Ext.tree.TreePanel({\n" +
			"\t    autoScroll:true,\n" + 
			"\t    rootVisible:false,\n" + 
			"\t    width: 200,\n" + 
			"\t    autoHeight:true," +
			"\t   \troot:navTree0,\n" + 
			"    \tlisteners : {\n" + 
			"    \t\t'dblclick':function(node,e){\n" + 
			"    \t\t\tif(node.getDepth() == 3){\n" + 
			"    \t\t\t\tvar Cobj = document.getElementById('CHANGE');\n" + 
			"        \t\t\tCobj.value = node.id;\n" + 
			"    \t\t\t}else{\n" + 
			"    \t\t\t\te.cancel = true;\n" + 
			"    \t\t\t}\n" + 
			"    \t\t},'checkchange': function(node,checked){\n" + 
			"\t\t\t\t\t/*if(checked){\n" + 
			"\t\t\t\t\t\taddNode(node);\n" + 
			"\t\t\t\t\t}else{\n" + 
			"\t\t\t\t\t\tsubNode(node);\n" + 
			"\t\t\t\t\t}*/\n" + 
			"\t\t\t\t\tnode.expand();\n" + 
			"\t\t\t\t\tnode.attributes.checked = checked;\n" + 
			"\t\t\t\t\tnode.eachChild(function(child) {\n" + 
			"\t\t\t\t\t\tif(child.attributes.checked != checked){\n" + 
			"\t\t\t\t\t\t\t/*if(checked){\n" + 
			"\t\t\t\t\t\t\t\taddNode(child);\n" + 
			"\t\t\t\t\t\t\t}else{\n" + 
			"\t\t\t\t\t\t\t\tsubNode(child);\n" + 
			"\t\t\t\t\t\t\t}*/\n" + 
			"\t\t\t\t\t\t\tchild.ui.toggleCheck(checked);\n" + 
			"\t\t\t            \tchild.attributes.checked = checked;\n" + 
			"\t\t\t            \tchild.fireEvent('checkchange', child, checked);\n" + 
			"\t\t\t\t\t\t}\n" + 
			"\t\t\t\t\t});\n" + 
			"\t\t\t\t}\n" + 
			"    \t},\n" + 
			"\t   \ttbar:[{text:'ȷ��',handler:function(){\n" + 
			"        var cs = navtree.getChecked();\n" + 
			"        rec = gridDiv_grid.getSelectionModel().getSelected();\n"+
			"        var tmp=\"\";\n" + 
			"        var tmp2='';\n" + 
			"         if(cs==null){win.hide(this);return;}\n"+
			"        else{for(var i = 0; i< cs.length; i++) {\n" + 
			"        \tif(cs[i].isLeaf()){\n" + 
			"\t\t\t\ttmp = cs[i].text;\n" + 
			"        \t\ttmp2=(tmp2?tmp2+',':'')+tmp;\n" + 
			"\n" + 
			"           }\n" + 
			"        }\n" + 
			"        rec.set('HUAYY',tmp2);\n" + 
			"        win.hide(this);\n"+
			"\n" + 
			"        }}}]\n" + 
			"\t});";
		
		String Strtmpfunction = 
			" win = new Ext.Window({\n" + 
			" title: '������Ա',\n " + 
			"            closable:true,closeAction:'hide',\n" + 
			"            width:200,\n" + 
			"            autoHeight:true,\n" + 
			"            border:0,\n" + 
			"            plain:true,\n" + 
			"            items: [navtree]\n" + 
			" });\n";
		

		GridButton refurbish = new GridButton("ˢ��","function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		
		egu.addToolbarButton("�ύ", GridButton.ButtonType_SubmitSel,"SaveButton", null, null);
		
		GridButton Huit = new GridButton("���",	getfunction("HuitButton", "stad"));
		Huit.setIcon(SysConstant.Btn_Icon_SelSubmit);
		egu.addTbarBtn(Huit);
		GridButton zuofei = new GridButton("����", getfunction("ZuofButton",	"stad"));
		zuofei.setIcon(SysConstant.Btn_Icon_SelSubmit);
		egu.addTbarBtn(zuofei);
		
		
		String Scrpit="gridDiv_grid.on('beforeedit',function(e){\n"+
						"if(e.field=='HAD'&& e.record.get('HUAYLB')!='������'){e.cancel=true;}\n"+
						"});\n";
		String changeColor=" var girdcount=0;\n"+
							" gridDiv_ds.each(function(r){\n"+
							"     if(r.get('HUAYLB')!='������'){\n"+
							"         gridDiv_grid.getView().getCell(girdcount, 10).style.backgroundColor='#E3E3E3';\n"+
							"     }\n"+
							"     girdcount=girdcount+1;\n"+
							" });\n";
		
		egu.addOtherScript("gridDiv_grid.on('cellclick',function(own,irow,icol, e){ "
			+ "row = irow; \n"
			+ "if('HUAYY' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
			+"if(!win){"+treepanel+Strtmpfunction+"}"
			+"win.show(this);}});\n"+Scrpit+changeColor);

		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));

		// ��֤�Ƿ����� �������
		String sqltmp = "select * from xitxxb where mingc='����ֵ¼����ʾ��Ʒ���' and zhi='����'";
		ResultSetList rsl2 = con.getResultSetList(sqltmp);
		if (rsl2.next()) {
			egu.getColumn("huaylb").setHidden(true);
		}

		egu.addTbarText("-");
		egu.addTbarText("��ǰ��¼��:"+visit.getRenymc());

		setExtGrid(egu);
		rsl2.close();
		con.Close();
	}

	public String getfunction(String binder, String s) {
		String handler = "function(){\n"
				+ "var Mrcd = gridDiv_grid.getSelectionModel().getSelections();\n"
				+ "for(i = 0; i< Mrcd.length; i++){\n"
				+ "gridDiv_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace('<','&lt;').replace('>','&gt;')+ '</ID>'+ '<HUAYBH update=\"true\">' + Mrcd[i].get('HUAYBH').replace('<','&lt;').replace('>','&gt;')+ '</HUAYBH>'+ '<HUAYSJ update=\"true\">' + ('object' != typeof(Mrcd[i].get('HUAYSJ'))?Mrcd[i].get('HUAYSJ'):Mrcd[i].get('HUAYSJ').dateFormat('Y-m-d'))+ '</HUAYSJ>'+ '<MT update=\"true\">' + Mrcd[i].get('MT')+ '</MT>'+ '<MAD update=\"true\">' + Mrcd[i].get('MAD')+ '</MAD>'+ '<AAD update=\"true\">' + Mrcd[i].get('AAD')+ '</AAD>'+ '<VAD update=\"true\">' + Mrcd[i].get('VAD')+ '</VAD>'+ '<"
				+ s.toUpperCase()
				+ " update=\"true\">' + Mrcd[i].get('"
				+ s.toUpperCase()
				+ "')+ '</"
				+ s.toUpperCase()
				+ ">'+ '<QBAD update=\"true\">' + Mrcd[i].get('QBAD')+ '</QBAD>'+ '<T1 update=\"true\">' + Mrcd[i].get('T1')+ '</T1>'+ '<T2 update=\"true\">' + Mrcd[i].get('T2')+ '</T2>'+ '<T3 update=\"true\">' + Mrcd[i].get('T3')+ '</T3>'+ '<T4 update=\"true\">' + Mrcd[i].get('T4')+ '</T4>'+  '<LURY update=\"true\">' + Mrcd[i].get('LURY').replace('<','&lt;').replace('>','&gt;')+ '</LURY>'+ '<BEIZ update=\"true\">' +'<HUAYY update=\"true)\">' + Mrcd[i].get('HUAYY').replace('<','&lt;').replace('>','&gt;')+ '</HUAYY>'+ Mrcd[i].get('BEIZ').replace('<','&lt;').replace('>','&gt;')+ '</BEIZ>'+ '<HUAYLB update=\"true\">' + Mrcd[i].get('HUAYLB').replace('<','&lt;').replace('>','&gt;')+ '</HUAYLB>' + '</result>' ; \n"
				+ "}\n"//
				+ "if(gridDiv_history==''){ \n"
				+ "Ext.MessageBox.alert('��ʾ��Ϣ','û��ѡ��������Ϣ');\n"
				+ "}else{\n"
				+ "    var Cobj = document.getElementById('CHANGE');\n"
				+ "    Cobj.value = '<result>'+gridDiv_history+'</result>';\n"
				+ "    document.getElementById('"
				+ binder
				+ "').click();\n"
				+ "    Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n"
				+ "}\n" + "}";
		return handler;
	}
	
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);
	}

	private void Huit() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save2(getChange(), visit);
	}

	private void Zuof() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save3(getChange(), visit);
	}

	private int Judgment(String value) {
		int v = 0;
		if (value.equals(null) || value.equals("")) {
			v = 0;
		} else {
			v = Integer.parseInt(value);
		}
		return v;
	}
	
	public String getNavigetion(){
		return ((Visit)this.getPage().getVisit()).getString7();
	}
	
	public void setNavigetion(String nav){
		((Visit)this.getPage().getVisit()).setString7(nav);
	}
	
	public void initNavigation(){
		Visit visit = (Visit) getPage().getVisit();
		setNavigetion("");
		
//		���������Ĳ�ѯSQL
		String sql=
			"select 0 id,'��' as mingc,1 jib,-1 fuid,0 checked from dual\n" +
			"union\n" + 
			"select r.id, r.quanc  as mingc,2 jib,0 fuid, 0 checked\n" + 
			"  from diancxxb d,renyxxb r\n" + 
			" where\n" + 
			"r.diancxxb_id=d.id\n" + 
			"and r.bum='����' and zhuangt=1 and d.id ="+visit.getDiancxxb_id()+"";
			
		TreeOperation dt = new TreeOperation();
		TreeNode node = dt.getTreeRootNode(sql,true);
		
		String treeNodeScript = dt.getTreeNodeScript("navTree", node);
		setNavigetion(treeNodeScript);
	}

	public void Save1(String strchange, Visit visit) {
		JDBCcon con = new JDBCcon();

		// �жϵ�½��Ա�Ƿ���ǻ���Ա,�����,���Զ���huayy�ֶδ����½��Ա����
		boolean xiansztq = false;
		String sql2 = "select zhi from xitxxb where mingc = '�볧����¼��Ĭ�ϵ�¼��Ա���ǻ���Ա' and zhuangt = 1 and diancxxb_id = "
				+ visit.getDiancxxb_id();
		ResultSetList rsl = con.getResultSetList(sql2);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("��")) {
				xiansztq = true;
			} else {
				xiansztq = false;
			}
		}
		rsl.close();

		int sacle = Compute.getQnet_arScale(con, visit.getDiancxxb_id());
		StringBuffer sql = new StringBuffer();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(strchange);
//		long zhillsbid = 0;
		String msg = "";
		while (mdrsl.next()) {
//			zhillsbid = Long.parseLong(mdrsl.getString("ID"));
			double H = mdrsl.getDouble("Had");
			double S = mdrsl.getDouble("Stad");
			double Mt = mdrsl.getDouble("mt");
			double Mad = mdrsl.getDouble("mad");
			double Aad = mdrsl.getDouble("aad");
			double Vad = mdrsl.getDouble("vad");
			double Qbad = mdrsl.getDouble("qbad");

			String id = mdrsl.getString("id");
			MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit.getRenymc(), SysConstant.RizOpType_UP,
					SysConstant.RizOpMokm_Huayzlu, "zhillsb", id);
			if ("��¯".equals(mdrsl.getString("huaylb"))) {
				//��ѯ���кͱ���������ͬ��caib_id Ȼ���������rulmzmxb
				sql.append("update rulmzlzmxb set ");
				sql.append("fenxrq").append("=")
				   .append("to_date('" + mdrsl.getString("HUAYSJ")+ "','YYYY-MM-DD hh24:mi:ss'),")
				   .append(" shenhzt=4,");
				sql.append(Compute.ComputeValue(Mt, Mad, Aad, Vad, Qbad, "HAD", H, "STAD", S, sacle));

			}else {// �볧��������
				sql.append("update zhillsb set ");
				sql.append("huaysj").append("=")
				   .append("to_date('" + mdrsl.getString("Huaysj")+ "','YYYY-MM-DD hh24:mi:ss'),")
				   .append(" shenhzt=4,");
				sql.append(Compute.ComputeValue(Mt, Mad, Aad, Vad, Qbad, "HAD", H, "STAD", S, sacle));
				sql.append("t1").append("=" + Judgment(mdrsl.getString("t1")) + ",");
				sql.append("t2").append("=" + Judgment(mdrsl.getString("t2")) + ",");
				sql.append("t3").append("=" + Judgment(mdrsl.getString("t3")) + ",");
				sql.append("t4").append("=" + Judgment(mdrsl.getString("t4")) + ",");
			}
			
			// ���ܱ�,�жϻ���ֵ¼��ʱ,��½��Ա�Ƿ���ǻ���Ա
			if (xiansztq) {
				sql.append("huayy").append("='" + visit.getRenymc() + "',");
			} else {
				sql.append("huayy").append(
						"='" + mdrsl.getString("huayy") + "',");
			}
			String caiyb_id=mdrsl.getString("id");
			
			sql.append("lury").append("='" + visit.getRenymc() + "',");
			sql.append("beiz").append("='" + mdrsl.getString("beiz") + "' ");
			if("��¯".equals(mdrsl.getString("huaylb"))){
				sql.append("where zhuanmbzllsb_id =").append(caiyb_id).append("\n");//id
			}else{
				sql.append("where id =").append(mdrsl.getString("ID")).append("\n");//id
			}
			con.getUpdate(sql.toString());

			if("��¯".equals(mdrsl.getString("huaylb"))){
				String rul_log=" select * from rulmzlzmxb where zhuanmbzllsb_id="+caiyb_id;
				StringBuffer log= new StringBuffer ("begin \n");
				ResultSetList rsl_log = con.getResultSetList(rul_log);
				while(rsl_log.next()){
					//��¯ԭʼֵ����
					String sql_log=" select * from rulmzlzmxb_log where id="+rsl_log.getString("id");
					ResultSetList rs_log = con.getResultSetList(sql_log);
					
					if(rs_log.getRows()>0){
						
					}else if(rs_log.getRows()==0){
						//û�����ݣ�����һ������
						
						log.append(" insert into rulmzlzmxb_log (ID,RULMZLZB_ID,MIAOS,MEIZBH,MEIZMC,MEICBH,MEIL,MEICMC,MEIDMC,JIZLH,JIZLMS,MEICH,MEICMS," +
								"CAIYJBH,CAIYJMS,BEIZ,CAIYB_ID,CAIYY,AAD,MAD,QBAD,HAD,VAD,FCAD,STD,QGRAD,HDAF,SDAF,VAR,HUAYY,LURY,SHENHZT,LURSJ,BIANM,HAR,QGRD,QGRAD_DAF,HAOYBH,SHANGMKSSJ,ZHUANGT,SHENHRY," +
								"QUERRY,QUERSJ,HAOZT,RULMZLB_ID,RULRQ,FENXRQ,DIANCXXB_ID,RULBZB_ID,JIZFZB_ID,QNET_AR,AAR,AD,VDAF,MT,STAD,YANGGH,ZHUANMBZLLSB_ID) values(").append(rsl_log.getLong("id")).append(",")
						.append(rsl_log.getLong("RULMZLZB_ID")).append(",'").append(rsl_log.getString("MIAOS"))
						.append("','").append(rsl_log.getString("MEIZBH")).append("','")
						.append(rsl_log.getString("MEIZMC")).append("','").append(rsl_log.getString("MEICBH")).append("',")
						.append(rsl_log.getString("MEIL")).append(",'")
						.append(rsl_log.getString("MEICMC")).append("','").append(rsl_log.getString("MEIDMC")).append("','")
						.append(rsl_log.getString("JIZLH")).append("','")
						.append(rsl_log.getString("JIZLMS")).append("','").append(rsl_log.getString("MEICH")).append("','")
						.append(rsl_log.getString("MEICMS")).append("','")
						.append(rsl_log.getString("CAIYJBH")).append("','").append(rsl_log.getString("CAIYJMS")).append("','")
						.append(rsl_log.getString("BEIZ")).append("',")
						.append(rsl_log.getLong("CAIYB_ID")).append(",'").append(rsl_log.getString("CAIYY")).append("',")
						.append(rsl_log.getDouble("AAD")).append(",")
						.append(rsl_log.getDouble("MAD")).append(",").append(rsl_log.getDouble("QBAD")).append(",")
						.append(rsl_log.getDouble("HAD")).append(",")
						.append(rsl_log.getDouble("VAD")).append(",").append(rsl_log.getDouble("FCAD")).append(",")
								.append(rsl_log.getDouble("STD")).append(",")
						.append(rsl_log.getDouble("QGRAD")).append(",").append(rsl_log.getDouble("HDAF")).append(",")
								.append(rsl_log.getDouble("SDAF")).append(",")
						.append(rsl_log.getDouble("VAR")).append(",'").append(rsl_log.getString("HUAYY")).append("','")
								.append(rsl_log.getString("LURY")).append("',")
						.append(rsl_log.getString("SHENHZT")).append(",").append("sysdate")
						.append(",'")
								.append(rsl_log.getString("BIANM")).append("',")
						.append(rsl_log.getDouble("HAR")).append(",").append(rsl_log.getDouble("QGRD")).append(",")
								.append(rsl_log.getDouble("QGRAD_DAF")).append(",'")
						.append(rsl_log.getString("HAOYBH")).append("',to_date('").append(rsl_log.getDateTimeString("SHANGMKSSJ"))
								.append("','yyyy-mm-dd hh24:mi:ss'),'")
						.append(rsl_log.getString("ZHUANGT")).append("','").append(rsl_log.getString("SHENHRY")).append("','")
								.append(rsl_log.getString("QUERRY")).append("',to_date('")
						.append(rsl_log.getDateTimeString("QUERSJ")).append("','yyyy-mm-dd hh24:mi:ss'),").append(rsl_log.getString("HAOZT")).append(",")
								.append(rsl_log.getLong("RULMZLB_ID")).append(",to_date('")
						.append(rsl_log.getDateTimeString("RULRQ")).append("','yyyy-mm-dd hh24:mi:ss'),to_date('")
						.append(rsl_log.getDateTimeString("FENXRQ")).append("','yyyy-mm-dd hh24:mi:ss'),")
								.append(rsl_log.getLong("DIANCXXB_ID")).append(",")
						.append(rsl_log.getLong("RULBZB_ID")).append(",").append(rsl_log.getLong("JIZFZB_ID"))
								.append(",").append(rsl_log.getDouble("QNET_AR")).append(",")
						.append(rsl_log.getDouble("AAR")).append(",").append(rsl_log.getDouble("AD")).append(",")
								.append(rsl_log.getDouble("VDAF")).append(",")
						.append(rsl_log.getDouble("MT")).append(",").append(rsl_log.getDouble("STAD")).append(",'")
								.append(rsl_log.getString("YANGGH")).append("',")
						.append(rsl_log.getLong("ZHUANMBZLLSB_ID")).append(")").append(";\n");
					}
				}
				log.append("end;");
				if(log.length()>13){
					con.getInsert(log.toString());
				}
			}
			sql.delete(0, sql.length());
		}
		setMsg(msg);
		con.Close();
	}

	public void Save2(String strchange, Visit visit) {
		JDBCcon con = new JDBCcon();
		String tableName = "zhillsb";
		String huaysj = "huaysj";
		StringBuffer sql = new StringBuffer("begin \n");
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			if (mdrsl.getString("huaylb").equals("��¯")) {
				tableName = "rulmzlzmxb";
				huaysj = "rulrq";
			}

			String id = mdrsl.getString("id");
			MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit
					.getRenymc(), SysConstant.RizOpType_UP,
					SysConstant.RizOpMokm_Huayzlu, "zhillsb", id);
			sql.append("update ").append(tableName).append(" set ");
			sql.append(huaysj).append("= null,");
			sql.append("qnet_ar").append("=0,");
			sql.append("aar").append("=0,");
			sql.append("ad").append("=0,");
			sql.append("vdaf").append("=0,");
			sql.append("mt").append("=0,");
			sql.append("stad").append("=0,");
			sql.append("aad").append("=0,");
			sql.append("mad").append("=0,");
			sql.append("qbad").append("=0,");
			sql.append("had").append("=0,");
			sql.append("vad").append("=0,");
			sql.append("fcad").append("=0,");
			sql.append("std").append("=0,");
			sql.append("qgrad").append("=0,");
			sql.append("hdaf").append("=0,");
			sql.append("qgrad_daf").append("=0,");
			sql.append("sdaf").append("=0,");
			if (mdrsl.getString("huaylb").equals("��¯")) {

				// û��t1-t4
			} else {// /�볧�Լ�������
				sql.append("t1").append("=0,");
				sql.append("t2").append("=0,");
				sql.append("t3").append("=0,");
				sql.append("t4").append("=0,");
			}
			sql.append("shenhzt").append("=0,");
			sql.append("huayy").append("=null,");
			sql.append("lury").append("=null,");
			sql.append("beiz").append("=null ");
			if(mdrsl.getString("huaylb").equals("��¯")){
				sql.append("where zhuanmbzllsb_id =").append(mdrsl.getString("id")).append(";\n");
			}else{
				sql.append("where id =").append(mdrsl.getString("ID")).append(";\n");
			}
		}
		sql.append(" end;");
		con.getUpdate(sql.toString());
	}

	public void Save3(String strchange, Visit visit) {
		String tableName = "zhillsb";

		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			if (mdrsl.getString("huaylb").equals("��¯")) {
				tableName = "rulmzlzmxb";
			}

			String id = mdrsl.getString("id");
			MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit
					.getRenymc(), SysConstant.RizOpType_UP,
					SysConstant.RizOpMokm_Huayzlu, "zhillsb", id);
			sql.append("update ").append(tableName).append(" set ");
			sql.append("shenhzt").append("=-1 ");
			if(mdrsl.getString("huaylb").equals("��¯")){
				sql.append("where zhuanmbzllsb_id =").append(mdrsl.getString("id"))
				.append(";\n");
			}else{
				sql.append("where id =").append(mdrsl.getString("ID"))
				.append(";\n");
			}
		}
		sql.append(" end;");
		con.getUpdate(sql.toString());
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _HuitChick = false;

	public void HuitButton(IRequestCycle cycle) {
		_HuitChick = true;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _ZuofChick = false;

	public void ZuofButton(IRequestCycle cycle) {
		_ZuofChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_HuitChick) {
			_HuitChick = false;
			Huit();
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
		if (_ZuofChick) {
			_ZuofChick = false;
			Zuof();
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

	private String treeid = "";

	public String getTreeid() {
		if (treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
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
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}
		return jib;
	}

	public String getTbarScript() {
		StringBuffer gridScript = new StringBuffer();
		gridScript.append(getTbar().length() > 0 ? getToolbarScript() + ","
				: "");
		return gridScript.toString();
	}

	public String tbars;

	public String getTbar() {
		if (tbars == null) {
			tbars = "";
		}
		return tbars;
	}

	public void setTbar(String tbars) {
		this.tbars = tbars;
	}

	private String getToolbarScript() {
		StringBuffer tbarScript = new StringBuffer();
		tbarScript.append("tbar: [");
		tbarScript.append(getTbar());
		tbarScript.deleteCharAt(tbarScript.length() - 1);
		tbarScript.append("]");
		return tbarScript.toString();
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
		}
		initNavigation();
		getSelectData();
	}

}