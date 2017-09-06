package com.zhiren.dc.diaoygl.xiecwh;

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
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.DatetimeField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author yang
 * ʱ�䣺2010-3-19
 * ������ȥ��ú���ֶ�
 */
/**
 * @author yang
 * ʱ�䣺2010-3-26
 * �޸�����: ��ʱ����ͬʱ��ѯ�������ݣ�����ѯʱ�侫ȷ������
 */

public class Rucxcwh extends BasePage implements PageValidateListener {

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, true);
	}

	protected void initialize() {
		super.initialize();
		setMsg(null);
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	// ����
	private void Save() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sql = new StringBuffer(" ");
		// �糧ID
		long diancID = visit.getDiancxxb_id();

		// ��ú��ֵ��ID
		long shangmbzbID = 0;

		// ú����ID��Ĭ��0
		String meicID = "0";

		// ���j=0���ѯ��ú��ֵ��ID,��ѯ��ı��ֵ����whileѭ����ֻ����һ�β�ѯ
		int j = 0;

		// �����ʶ�����Ϊtrue������begin end;�д��ڸ�����䣬���ִ�б���
		boolean okSave = false;
		ResultSetList rsl = this.getExtGrid().getModifyResultSet(
				this.getChange());

		sql.append("begin\n");
		while (rsl.next()) {
			// ж����ʽ��ֱ�ϡ���ж
			int zhis = 0, waix = 0;
			// �ж��޸ļ�¼�Ƿ���ѡ��״̬��������򹹽��������
			if (rsl.getString("zhuangt").equals("��")) {
				okSave = true;
				if (rsl.getString("XIECFS").equals("��ж")) {
					waix = 1;
				} else {
					zhis = 1;
				}
				if (j == 0) {
					// ж����ʼʱ��
					String ks = this.getXCKaissj();

					// ж����ֹʱ��
					String jz = this.getXCJiezsj();

					// �жϿ�ʼ�ͽ�ֹʱ���Ƿ���ȷ
					if (DateUtil.getDateTime(ks).compareTo(
							DateUtil.getDateTime(jz)) > 0) {
						this.setMsg("'ж��ʱ�䷶Χ���󣺽���ʱ��Ҫ���ڵ��ڿ�ʼʱ�䣡'");
						return;
					}
					// ����ж��ʱ�䷶Χ�����ú��ֵ��ID
					String sq = "select t.id from(\n"
							+ "select id,substr(to_char(s.kaissj,'yyyy-mm-dd HH24:mi'),12) k,substr(to_char(s.jiezsj,'yyyy-mm-dd HH24:mi'),12) j from shangmbzb s\n"
							+ ") t\n"
							+ "where to_date(t.k,'HH24:mi:ss')<to_date('"
							+ ks.substring(ks.indexOf(" ") + 1)
							+ "','HH24:mi:ss') and to_date(t.j,'HH24:mi:ss')>to_date('"
							+ jz.substring(jz.indexOf(" ") + 1)
							+ "','HH24:mi:ss')";

					ResultSetList rs = con.getResultSetList(sq);
					if (rs.next()) {
						shangmbzbID = rs.getLong(0);
					}
					rs.close();
					j = 1;
				}

				// String sql1 = "insert into
				// rucxcb(id,chepb_id,shangmbzb_id,waix,zhis,diaody,xieckssj,xiecjzsj,beiz)"
				// + " values(getnewid("
				// + diancID
				// + "),"
				// + rsl.getString("chepb_id")
				// + ","
				// + shangmbzbID
				// + ","
				// + waix
				// + ","
				// + zhis
				// + ",'"
				// + rsl.getString("DIAODY")
				// + "',to_date('"
				// + rsl.getDateTimeString("KAISSJ")
				// + "','yyyy-mm-dd HH24:mi'),to_date('"
				// + rsl.getDateTimeString("JIEZSJ")
				// + "','yyyy-mm-dd HH24:mi'),'"
				// + rsl.getString("BEIZ")
				// + "');\n";
				sql
						.append("insert into rucxcb(id,chepb_id,shangmbzb_id,waix,zhis,diaody,xieckssj,xiecjzsj,beiz)");
				sql.append(" values(getnewid(");
				sql.append(diancID);
				sql.append("),");
				sql.append(rsl.getString("chepb_id"));
				sql.append(",");
				sql.append(shangmbzbID);
				sql.append(",");
				sql.append(waix);
				sql.append(",");
				sql.append(zhis);
				sql.append(",'");
				sql.append(rsl.getString("DIAODY"));
				sql.append("',to_date('");
				sql.append(this.getXCKaissj());
				sql.append("','yyyy-mm-dd HH24:mi:ss'),to_date('");
				sql.append(this.getXCJiezsj());
				sql.append("','yyyy-mm-dd HH24:mi:ss'),'");
				sql.append(rsl.getString("BEIZ"));
				sql.append("');\n");

				// sql.append(sql1);

				// ���ѡ����ж��ú�������������Ӧ��Ƥ��chepbú�������ֶ�meicb_id
				if (!"".equals(rsl.getString("meic"))
						&& "��ж".equals(rsl.getString("XIECFS"))) {
					for (int i = 0; i < rsl.getColumnCount(); i++) {
						if ("MEIC".toUpperCase().equals(
								this.getExtGrid().getColumn(i).dataIndex
										.toUpperCase())) {
							meicID = this.getValueSql(this.getExtGrid()
									.getColumn(i), rsl.getString("meic"));
						}
					}
					// String sql2 = "update chepb c set c.meicb_id=" + meicID
					// + " where c.id=" + rsl.getString("chepb_id") + ";";
					sql.append("update chepb c set c.meicb_id=");
					sql.append(meicID);
					sql.append(" where c.id=");
					sql.append(rsl.getString("chepb_id"));
					sql.append(";\n");
				}
				// sql.append(sql2);
			}
		}
		sql.append("end;");
		
		//begin end�а���sql���
		if (okSave) {
			okSave = false;
			con.getInsert(sql.toString());
		}
		rsl.close();
		con.Close();

	}

	public String getValueSql(GridColumn gc, String value) {
		if ("string".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return "'" + value + "'";
			}

		} else if ("date".equals(gc.datatype)) {
			return "to_date('" + value + "','yyyy-mm-dd')";
		} else if ("float".equals(gc.datatype)) {
			return value == null || "".equals(value) ? "0" : value;
		} else {
			return value;
		}
	}

	// ����
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	// ˢ��
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
		}
		getSelectData();
	}

	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		StringBuffer shead = new StringBuffer();
		shead
				.append(" [  {header:'<table><tr><td width=8 align=center></td></tr></table>', align:'center',rowspan:2},\n");
		shead.append("   {header:'�����볧��Ϣ', colspan:8},\n");
		shead
				.append(" {header:'ж����ʽ<br/>(ֱ��/��ж)', align:'center',rowspan:2}, \n");
		shead.append(" {header:'ú��', align:'center',rowspan:2}, \n");
		shead.append(" {header:'����Ա', align:'center',rowspan:2}, \n");
		shead.append(" {header:'ж��ʱ�䷶Χ<br/>(��/ʱ/��)',colspan:2}, \n");
		shead.append(" {header:'��ע', align:'center',rowspan:2}, \n");
		shead.append(" {header:'��Ƥ���', align:'center',rowspan:2} \n");

		StringBuffer xhead = new StringBuffer();
		xhead.append(" [ {header:'���', align:'center'},\n  ");
		xhead.append("  {header:'״̬', align:'center'},\n ");
		xhead.append("  {header:'������λ', align:'center'},\n ");
		xhead.append("  {header:'��������', align:'center'},\n ");
		xhead.append("  {header:'��վ', align:'center'},\n ");
//		xhead.append("  {header:'ú��', align:'center'},\n ");
		xhead.append("  {header:'Ʊ��', align:'center'},\n ");
		xhead.append("  {header:'�ɵ���', align:'center'},\n ");
		xhead.append("  {header:'�볧ʱ��<br/>(��/ʱ/��)', align:'center'},\n ");
		xhead.append("  {header:'��ʼʱ��', align:'center'},\n ");
		xhead.append("  {header:'����ʱ��', align:'center'}\n ");
		xhead.append(" ] \n ");

		shead.append(" ],\n");

		// ����ʾδѡ��״̬
		//select m.mingc meiz       from meizb m,
		String sql = "select rownum xuh,'��' zhuangt,g.mingc fahdw,c.cheph chelhm,cz.mingc faz,\n"
				+ "       c.biaoz piaoz,c.zhongchh gudh,to_char(c.zhongcsj,'dd-HH24-mi') rucsj,\n"
				+ "       '' xiecfs,'' meic,r.diaody diaody,r.xieckssj kaissj,r.xiecjzsj jiezsj,r.beiz beiz,c.id chepb_id\n"
				+ "from fahb f,chepb c,chezxxb cz,gongysb g,rucxcb r,yunsfsb y\n"
				+ "where f.id=c.fahb_id\n"
				+ "      and f.gongysb_id=g.id\n"
				+ "      and f.faz_id=cz.id\n"
				//+ "      and f.meizb_id=m.id\n"
				+ "		 and f.yunsfsb_id=y.id\n"
				+ "		 and y.id=1\n"
				+ "      and r.chepb_id(+)=c.id\n"
				+ "      and to_char(c.zhongcsj,'yyyy-mm-dd HH24:mi')>="
				+ this.getWanzRiq(this.getRCKSRiq(), this.getRCKSSJValue()
						.getValue())
				+ "\n"
				+ "		 and to_char(c.zhongcsj,'yyyy-mm-dd HH24:mi')<="
				+ this.getWanzRiq(this.getRCJZRiq(), this.getRCJZSJValue()
						.getValue())
				+ "\n "
				+ "		and c.id not in(\n"
				+ "         select r.chepb_id from rucxcb r\n"
				+ "     )\n"
				+ "order by xuh";

		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setTableName("rucxcb");
		egu.addPaging(25);

		// egu.getColumn("zhuangt").setEditor(new Checkbox());
		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("xuh").setEditor(null);
		
		egu.getColumn("zhuangt").setHeader("״̬");
		egu.getColumn("zhuangt").setEditor(null);

		egu.getColumn("fahdw").setHeader("������λ");
		egu.getColumn("fahdw").setEditor(null);

		egu.getColumn("chelhm").setHeader("��������");
		egu.getColumn("chelhm").setEditor(null);

		egu.getColumn("faz").setHeader("��վ");
		egu.getColumn("faz").setEditor(null);

//		egu.getColumn("meiz").setHeader("ú��");
//		egu.getColumn("meiz").setEditor(null);

		egu.getColumn("piaoz").setHeader("Ʊ��");
		egu.getColumn("piaoz").setEditor(null);

		egu.getColumn("gudh").setHeader("�ɵ���");
		egu.getColumn("gudh").setEditor(null);

		egu.getColumn("rucsj").setHeader("�볧ʱ��");
		egu.getColumn("rucsj").setEditor(null);

		egu.getColumn("xiecfs").setHeader("ж����ʽ");
		egu.getColumn("xiecfs").setEditor(new ComboBox());

		// ж����ʽ������
		List xc = new ArrayList();
		xc.add(new IDropDownBean("1", "��ж"));
		xc.add(new IDropDownBean("2", "ֱ��"));
		egu.getColumn("xiecfs").setComboEditor(egu.gridId,
				new IDropDownModel(xc));

		egu.getColumn("meic").setHeader("ú��");
		egu.getColumn("meic").setEditor(new ComboBox());
		egu.getColumn("meic").setComboEditor(egu.gridId,
				new IDropDownModel("select id,mingc from meicb"));

		egu.getColumn("diaody").setHeader("����Ա");
		egu.getColumn("diaody").setEditor(new ComboBox());
		egu.getColumn("diaody").setComboEditor(
				egu.gridId,
				new IDropDownModel(
						"select r.id,r.quanc||'('||r.zhiw||')' from renyxxb r where r.bum='����'",
						""));

		egu.getColumn("kaissj").setHeader("ж����ʼʱ��");
		egu.getColumn("jiezsj").setHeader("ж����ֹʱ��");
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("chepb_id").setHidden(true);
		List list = egu.gridColumns;

		// ���õ�Ԫ����
		for (int i = 0; i < list.size(); i++) {
			GridColumn gc = (GridColumn) list.get(i);
			if (gc.dataIndex.equalsIgnoreCase("fahdw")) {
				gc.setWidth(200);
			} else if (gc.dataIndex.equalsIgnoreCase("xiecsjfw")) {
				gc.setWidth(200);
			} else if (gc.dataIndex.equalsIgnoreCase("zhuangt")
						||gc.dataIndex.equalsIgnoreCase("xuh")) {
				gc.setWidth(50);
			} else {
				gc.setWidth(120);
			}
		}

		// ��ʼʱ�䷶Χѡ��ؼ�
		DatetimeField ks = new DatetimeField();
		ks.setFormat("Y-m-d H:i");
		ks.setMenu("new DatetimeMenu()");
		egu
				.getColumn("kaissj")
				.setRenderer(
						"function(value){ if(value==null || value==''){return '';}else{if('object' != typeof(value)){return value;}else{ks=value;document.getElementById('Kaissj').value=value.dateFormat('Y-m-d H:i:s');return value.dateFormat('d H/i').replace(' ','/');}}}");
		egu.getColumn("kaissj").setEditor(ks);

		// ����ʱ��ѡ��
		DatetimeField js = new DatetimeField();
		js.setFormat("Y-m-d H:i");
		js.setMenu("new DatetimeMenu()");
		egu
				.getColumn("jiezsj")
				.setRenderer(
						"function(value){ if(value==null || value==''){return '';}else{if('object' != typeof(value)){return value;}else{jz=value;document.getElementById('Jiezsj').value=value.dateFormat('Y-m-d H:i:s');return value.dateFormat('d H/i').replace(' ','/');}}}");
		egu.getColumn("jiezsj").setEditor(js);

		String Headers = shead.toString() + xhead.toString();
		egu.setHeaders(Headers);
		egu.setPlugins("new Ext.ux.plugins.XGrid()");

		// �볧��ʼʱ��ѡ��
		egu.addTbarText("��ʼ���ڣ�");
		DateField rucksDF = new DateField();
		rucksDF.setValue(this.getRCKSRiq());
		rucksDF
				.setListeners("change:function(own,newValue,oldValue)"
						+ "{document.getElementById('RCKSRiq').value = newValue.dateFormat('Y-m-d');}");
		egu.addToolbarItem(rucksDF.getScript());
		egu.addTbarText("-");

		// �볧��ֹʱ��ѡ��
		egu.addTbarText("��ֹ���ڣ�");
		DateField rucjzDF = new DateField();
		rucjzDF.setId("df");
		rucjzDF.setValue(this.getRCJZRiq());
		rucjzDF
				.setListeners("change:function(own,newValue,oldValue)"
						+ "{document.getElementById('RCJZRiq').value = newValue.dateFormat('Y-m-d');document.forms[0].submit();}");
		egu.addToolbarItem(rucjzDF.getScript());
		egu
				.addOtherScript("df.on('change', function(o,record,index) {document.forms[0].submit();});");
		egu.addTbarText("-");

		// ��ʼʱ��
		egu.addTbarText("��ʼʱ�䣺");
		ComboBox ksComb = new ComboBox();
		ksComb.setWidth(80);
		ksComb.setTransform("RCKSSJDropDown");
		ksComb.setId("KS");// ���Զ�ˢ�°�
		ksComb.setLazyRender(true);// ��̬��
		ksComb.setEditable(true);
		egu.addToolbarItem(ksComb.getScript());
		egu.addTbarText("-");
		// ��ʼʱ��
		egu.addTbarText("��ֹʱ�䣺");
		ComboBox jzComb = new ComboBox();
		jzComb.setWidth(80);
		jzComb.setTransform("RCJZSJDropDown");
		jzComb.setId("JZ");// ���Զ�ˢ�°�
		jzComb.setLazyRender(true);// ��̬��
		jzComb.setEditable(true);
		egu.addToolbarItem(jzComb.getScript());

		egu.addTbarText("-");
		// ˢ��
		GridButton gbrf = new GridButton(
				"ˢ��",
				"function(){document.getElementById('RefreshButton').click();}",
				SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbrf);

		String handler = "function(){\n"
				+ "if(ks>jz){Ext.MessageBox.alert('��ʾ��Ϣ','ж��ʱ�䷶Χ ��ֹʱ��Ҫ���ڻ���ڿ�ʼʱ�䣡');return;}"
				+ " var gridDivsave_history = '';var Mrcd = gridDiv_ds.getModifiedRecords();\n"
				+ "for(i = 0; i< Mrcd.length; i++){\n"
				+ "if(typeof(gridDiv_save)=='function'){ var revalue = gridDiv_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}}\n"
				+ "if(Mrcd[i].get('ZHUANGT')=='��'){"
				+ "if(Mrcd[i].get('XIECFS') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ж����ʽ ����Ϊ��');return;\n"
				+ "}if(Mrcd[i].get('KAISSJ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ж����ʼʱ�� ����Ϊ��');return;\n"
				+ "}if(Mrcd[i].get('JIEZSJ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ж����ֹʱ�� ����Ϊ��');return;\n"
				+ "}if(Mrcd[i].get('DIAODY') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ����Ա ����Ϊ��');return;\n"
				+ "}if(Mrcd[i].get('XIECFS')=='��ж'&&Mrcd[i].get('MEIC')==''){Ext.MessageBox.alert('��ʾ��Ϣ','��ж��Ҫѡ��ú��');return;}"
				+ "gridDivsave_history += '<result>' + '<sign>U</sign>' + '<ZHUANGT update=\"true\">' + Mrcd[i].get('ZHUANGT')+ '</ZHUANGT>'\n"
				+ "+ '<XIECFS update=\"true\">' + Mrcd[i].get('XIECFS').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</XIECFS>'\n"
				+ "+ '<MEIC update=\"true\">' + Mrcd[i].get('MEIC').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</MEIC>'\n"
				+ "+ '<DIAODY update=\"true\">' + Mrcd[i].get('DIAODY').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</DIAODY>'\n"
				+ "+ '<KAISSJ update=\"true\">' + ('object' != typeof(Mrcd[i].get('KAISSJ'))?Mrcd[i].get('KAISSJ'):Mrcd[i].get('KAISSJ').dateFormat('d/H/i'))+ '</KAISSJ>'\n"
				+ "+ '<JIEZSJ update=\"true\">' + ('object' != typeof(Mrcd[i].get('JIEZSJ'))?Mrcd[i].get('JIEZSJ'):Mrcd[i].get('JIEZSJ').dateFormat('d/H/i'))+ '</JIEZSJ>'\n"
				+ "+ '<BEIZ update=\"true\">' + Mrcd[i].get('BEIZ').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</BEIZ>'\n"
				+ "+ '<CHEPB_ID update=\"true\">' + Mrcd[i].get('CHEPB_ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</CHEPB_ID>'\n"
				+ " + '</result>' ; }}\n"
				+ "if(gridDiv_history=='' && gridDivsave_history==''){\n"
				+ "Ext.MessageBox.alert('��ʾ��Ϣ','û�н��иĶ���û��ѡ��Ҫά���ļ�¼���޷�����');\n"
				+ "}else{\n"
				+ "var Cobj = document.getElementById('CHANGE');Cobj.value = '<result>'+gridDiv_history+gridDivsave_history+'</result>';"
				+ "Ext.MessageBox.confirm('��ʾ��Ϣ','������޷��޸ģ�ȷ�ϱ��棿',function(btn){\n"
				+ "	if(btn=='yes'){\n"
				+ "   document.getElementById('SaveButton').click();Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});"
				+ "	}\n" + "});\n" + "}\n" + "}";

		// ����
		GridButton gbts = new GridButton("����", handler,
				SysConstant.Btn_Icon_Save);
		egu.addTbarBtn(gbts);
		setExtGrid(egu);

		rsl.close();
		con.Close();
	}

	// ����������ڣ�to_date(yyyy-mm-dd h:m:s)
	/**
	 * @param riq
	 *            �����ַ��� yyyy-mm-dd
	 * @param sj
	 *            ʱ���ַ��� h:m:s
	 * @return �������� yyyy-mm-dd h:m:s
	 */
	public String getWanzRiq(String riq, String sj) {
		return "'" + riq + " " + sj + "'";
	}

	// ��ѯ�������볧��ʼ���� yyyy-mm-dd
	private String rcksRiq = "";

	// ��ʼ����ѯ����ʱ���־
	private boolean Flag = false;

	public String getRCKSRiq() {
		return rcksRiq;
	}

	public void setRCKSRiq(String riq) {

		if (!riq.equals(rcksRiq)) {

			Flag = true;
		}
		rcksRiq = riq;
	}

	// ��ѯ�������볧�������� yyyy-mm-dd
	private String rcjzRiq = "";

	public String getRCJZRiq() {
		return rcjzRiq;
	}

	public void setRCJZRiq(String riq) {

		if (!riq.equals(rcjzRiq)) {

			Flag = true;
		}
		rcjzRiq = riq;
	}

	// ж��ʱ�䷶Χ��kaissj jiezsj
	// ��ʼʱ��
	private String kaissj = "";

	public void setXCKaissj(String value) {
		kaissj = value;
	}

	public String getXCKaissj() {
		return kaissj;
	}

	// ж��ʱ�䷶Χ��kaissj jiezsj
	// ����ʱ��
	private String jiezsj = "";

	public void setXCJiezsj(String value) {
		jiezsj = value;
	}

	public String getXCJiezsj() {
		return jiezsj;
	}

	// �볧��ѯ��ʼʱ��������:ʱ����
	private static IPropertySelectionModel _RCKSSJModel;

	public IPropertySelectionModel getRCKSSJModel() {
		if (_RCKSSJModel == null) {
			getRCKSSJModels();
		}
		return _RCKSSJModel;
	}

	private IDropDownBean _RCKSSJValue;

	public IDropDownBean getRCKSSJValue() {
		if (_RCKSSJValue == null) {
			_RCKSSJValue = (IDropDownBean) _RCKSSJModel.getOption(0);
		}
		return _RCKSSJValue;
	}

	public void setRCKSSJValue(IDropDownBean Value) {
		_RCKSSJValue = Value;
	}

	public IPropertySelectionModel getRCKSSJModels() {
		List _list = new ArrayList();
		int i = 0;
		_list.add(new IDropDownBean("-1", "00:00"));
		JDBCcon con = new JDBCcon();

		String sql = "select distinct substr(to_char(c.zhongcsj,'yyyy-mm-dd HH24:mi'),12) mingc\n"
				+ "from chepb c\n"
				+ "where  to_date(substr(to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss'),0,11),'yyyy-mm-dd')>=to_date('"
				+ this.getRCKSRiq()
				+ "','yyyy-mm-dd')\n"
				+ "       and to_date(substr(to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss'),0,11),'yyyy-mm-dd')<=to_date('"
				+ this.getRCJZRiq()
				+ "','yyyy-mm-dd')\n"
				+ " 	and c.id not in(\n"
				+ "			select r.chepb_id from rucxcb r"
				+ "		)" + "order by mingc asc";

		ResultSetList rs = con.getResultSetList(sql);
		while (rs.next()) {
			String id = i + "";
			String mc = rs.getString(0);
			_list.add(new IDropDownBean(id, mc));
			i++;
		}
		_RCKSSJModel = new IDropDownModel(_list);

		rs.close();
		con.Close();
		return _RCKSSJModel;
	}

	public void setRCKSSJModel(IPropertySelectionModel _value) {
		_RCKSSJModel = _value;
	}

	// ��ѯ��ֹʱ��������
	// ��ѯ��ʼʱ��������:ʱ����
	private static IPropertySelectionModel _RCJZSJModel;

	public IPropertySelectionModel getRCJZSJModel() {
		if (_RCJZSJModel == null) {
			getRCJZSJModels();
		}
		return _RCJZSJModel;
	}

	private IDropDownBean _RCJZSJValue;

	public IDropDownBean getRCJZSJValue() {	
		if (_RCJZSJValue == null) {
			_RCJZSJValue = (IDropDownBean) _RCJZSJModel.getOption(0);
		}
		return _RCJZSJValue;
	}

	public void setRCJZSJValue(IDropDownBean Value) {
		_RCJZSJValue = Value;
	}

	public IPropertySelectionModel getRCJZSJModels() {
		int i = 0;
		List _list = new ArrayList();
		_list.add(new IDropDownBean("-1", "23:59"));
		JDBCcon con = new JDBCcon();
	
		// ��ѯʱ�� hh:mm
		String sql = "select distinct substr(to_char(c.zhongcsj,'yyyy-mm-dd HH24:mi'),12) mingc\n"
				+ "from chepb c\n"
				+ "where  to_date(substr(to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss'),0,11),'yyyy-mm-dd')>=to_date('"
				+ this.getRCKSRiq()
				+ "','yyyy-mm-dd')\n"
				+ "       and to_date(substr(to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss'),0,11),'yyyy-mm-dd')<=to_date('"
				+ this.getRCJZRiq()
				+ "','yyyy-mm-dd')\n"
				+ " 	and c.id not in(\n"
				+ "			select r.chepb_id from rucxcb r"
				+ "		)" + "order by mingc asc";

		ResultSetList rs = con.getResultSetList(sql);
		while (rs.next()) {
			String id = i + "";
			String mc = rs.getString(0);
			_list.add(new IDropDownBean(id, mc));
			i++;
		}
		_RCJZSJModel = new IDropDownModel(_list);
		
		rs.close();
		con.Close();
		return _RCJZSJModel;
	}

	public void setRCJZSJModel(IPropertySelectionModel _value) {
		_RCJZSJModel = _value;
	}

	// -----�糧tree
	// private String treeid;
	// private boolean diancFlag=false;
	// public String getTreeid() {
	// return treeid;
	// }
	// public void setTreeid(String treeid) {
	// this.treeid = treeid;
	// }
	// public String getTreeid() {
	// String treeid=((Visit) getPage().getVisit()).getString2();
	// if(treeid==null||treeid.equals("")){
	// ((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit)
	// this.getPage().getVisit()).getDiancxxb_id()));
	// }
	// return ((Visit) getPage().getVisit()).getString2();
	// }
	// public void setTreeid(String treeid) {
	// if(((Visit) getPage().getVisit()).getString2()!=null && !((Visit)
	// getPage().getVisit()).getString2().equals(treeid)){
	// diancFlag=true;
	// }
	// ((Visit) getPage().getVisit()).setString2(treeid);
	// }
	// public ExtTreeUtil getTree() {
	// return ((Visit) this.getPage().getVisit()).getExtTree1();
	// }
	// public void setTree(ExtTreeUtil etu) {
	// ((Visit) this.getPage().getVisit()).setExtTree1(etu);
	// }
	// public String getTreeHtml() {
	// return getTree().getWindowTreeHtml(this);
	// }
	// public String getTreeScript() {
	// return getTree().getWindowTreeScript();
	// }

	// --------------------------------

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
			visit.setActivePageName(getPageName().toString());
			this.getRCJZSJModels();
			this.getRCKSSJModels();
			this.setRCJZRiq(DateUtil.FormatDate(new Date()));
			this.setRCKSRiq(DateUtil.FormatDate(new Date()));
			getSelectData();
		}
		// �޸����ں����³�ʼ��ʱ��������
		if (Flag) {
			Flag = false;
			this.getRCKSSJModels();
			this.getRCJZSJModels();

		}

	}
}
