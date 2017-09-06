package com.zhiren.dc.hesgl.changnzf;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;

/**
 * @author ����
 * 2010-01-27
 * ������װ���ӷѽ���
 */

public class Zcjs extends BasePage {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		msg = "";
	}
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private boolean _SaveChick = false;
	
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _ReturnChick = false;
	
	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			if(save()) {
				cycle.activate("Zhuangcjs");
			}
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			cycle.activate("Zhuangcjs");
		}
	}
	
	public String getShoukdw() {
		return ((Visit) getPage().getVisit()).getString16();
	}

	public void setShoukdw(String value) {
		((Visit) getPage().getVisit()).setString16(value);
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		
		Visit visit = (Visit)this.getPage().getVisit();
		String gysmc = visit.getString1();
		int ches = visit.getInt2();
		double koud = visit.getDouble7();
		double shul = visit.getDouble2();
		double danj = visit.getDouble3();
		double bhszf = visit.getDouble4();
		double zfsk = visit.getDouble5();
		double feiyhj = visit.getDouble6();
		
		String quanc = getShoukdwValue().getValue();
		String kaihyh="";
		String zhangh="";
		ResultSetList rs = con.getResultSetList("select kaihyh,zhangh from shoukdw where id="+getShoukdwValue().getId());
        while(rs.next()){
          kaihyh=rs.getString("kaihyh");
          zhangh=rs.getString("zhangh");
        }
		
		String qisrq = visit.getString8();
		String jiezrq = visit.getString9();
		String beiz = visit.getString10();
		String bianm = visit.getString12();
		
		sbsql.append("select xiangm, zhi from (" +
				"select '������' as xiangm,'"+ bianm +"' as zhi , 1 as num from dual union " +  //���ɱ༭
				"select '��Ӧ������' as xiangm, '"+ gysmc +"' as zhi , 2 as num from dual union " +
				
				"select '����' as xiangm, '"+ ches + "' as zhi, 3 as num from dual union " +
				"select '�۶�' as xiangm, '"+ koud + "' as zhi, 4 as num from dual union " +
				
				"select '��������' as xiangm, '"+ shul + "' as zhi, 5 as num from dual union " +
				"select '����' as xiangm, '"+ danj + "' as zhi, 6 as num from dual union " +
				"select '����˰�ӷ�' as xiangm, '"+ bhszf +"' as zhi, 7 as num from dual union " +
				"select '�ӷ�˰��' as xiangm, '"+ zfsk +"' as zhi, 8 as num from dual union " +
				"select '���úϼ�' as xiangm, '"+ feiyhj +"' as zhi, 9 as num from dual union " +
				
				"select '�տλ' as xiangm, '"+ quanc +"' as zhi, 10 as num from dual union " +
				"select '��������' as xiangm, '"+ kaihyh +"' as zhi, 11 as num from dual union " +
				"select '�˺�'  as xiangm, '"+ zhangh +"' as zhi, 12 as num from dual union " +
				"select '��Ʊ���' as xiangm, '' as zhi, 13 as num from dual union " +
				
				"select '��ʼ����' as xiangm, '"+ qisrq +"' as zhi, 14 as num from dual union " +
				"select '��ֹ����' as xiangm, '"+ jiezrq +"' as zhi, 15 as num from dual union " +
				"select '��ע' as xiangm, '"+ beiz +"' as zhi, 16 as num from dual ) order by num");
		
		ResultSetList rsl = con.getResultSetList(sbsql.toString());
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("dual");
		egu.getColumn("xiangm").setHeader("��Ŀ����");
		egu.getColumn("zhi").setHeader("ֵ");
		egu.getColumn("zhi").setWidth(300);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
//		˰��
		double zhi = visit.getDouble16();
		
		egu.addOtherScript("\n gridDiv_grid.on('beforeedit',function(e){ 							\n" +
			"if(e.field=='ZHI') {																	\n" +
			"	if(e.row==4 || e.row==5) {															\n" +
			"		var temp4 = gridDiv_ds.getAt(4).get('ZHI');										\n" +
			"		var temp5 = gridDiv_ds.getAt(5).get('ZHI');										\n" +
			"		gridDiv_grid.on('afteredit',function(a){										\n" +
			"			if ((/^\\d+(\\.\\d+)?$/.test(gridDiv_ds.getAt(4).get('ZHI'))) " +
			"				&& (/^\\d+(\\.\\d+)?$/.test(gridDiv_ds.getAt(5).get('ZHI')))) {			\n" +
			"				temp4 = gridDiv_ds.getAt(4).get('ZHI');									\n" +
			"				temp5 = gridDiv_ds.getAt(5).get('ZHI');									\n" +
			"				var row4 = eval(gridDiv_ds.getAt(4).get('ZHI'));						\n" +
			"				var row5 = eval(gridDiv_ds.getAt(5).get('ZHI'));						\n" +
			"				var result = row4*row5; result=Round_new(result,2);						\n" +
			
			"				var buhszf = result*(1-"+zhi+");										\n" +
			"				var zafsk = result*"+zhi+";												\n" +
			"				buhszf=Round_new(buhszf,2);	zafsk=Round_new(zafsk,2);					\n" +
			"				gridDiv_ds.getAt(gridDiv_ds.getCount()-8).set('ZHI',result.toString());	\n" +
			"				gridDiv_ds.getAt(gridDiv_ds.getCount()-9).set('ZHI',zafsk.toString());	\n" +
			"				gridDiv_ds.getAt(gridDiv_ds.getCount()-10).set('ZHI',buhszf.toString());\n" +
			"			} else {																	\n" +
			"				Ext.MessageBox.alert('��ʾ��Ϣ','�������ݱ���Ϊ���֣�');					\n" +
//			"				gridDiv_ds.getAt(gridDiv_ds.getCount()-8).set('ZHI',temp5.toString());	\n" +
//			"				gridDiv_ds.getAt(gridDiv_ds.getCount()-9).set('ZHI',temp4.toString());	\n" +
			"			}																			\n" +
			"		}); 																			\n" +
			"	} else if(e.row==1||e.row==12||e.row==15) {																\n" +
			"		e.cancel=false;																	\n" +
			"	} else {																			\n" +
			"		e.cancel=true;																	\n" +
			"	}																					\n" +
			"} else {																				\n" +
			"	e.cancel=true;																		\n" +
			"}																						\n" +
			"});																					\n");
		
		egu.addTbarBtn(new GridButton("����", "function(){document.getElementById('ReturnButton').click();}", 
				SysConstant.Btn_Icon_Return));
		
		egu.addTbarText("-");
		
		egu.addToolbarButton("����", GridButton.ButtonType_SaveAll, "SaveButton");
		egu.addPaging(0);
		setExtGrid(egu);
		
		egu.addTbarText("-");
		
//		�տλ
		egu.addTbarText(Locale.shoukdw_jies);
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("ShoukdwDropDown");
		comb1.setId("Shoukdw");
		comb1.setEditable(false);
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(130);
		comb1.setReadOnly(true);
		egu.addToolbarItem(comb1.getScript());
		egu.addOtherScript("Shoukdw.on('select',function(){document.forms[0].submit();});");
		
		rsl.close();
		con.Close();
	}
	
	//���ܣ�����
	//�߼������α���zafjsb��changnzfb��zhuangcjsglb������zhuangcjsglb�����Ƕ���
	public boolean save() {
		Visit visit = (Visit)this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer istsql = new StringBuffer();
		StringBuffer sbrsl = new StringBuffer();
		StringBuffer czrsl = new StringBuffer();
		istsql.append("begin\n");
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		String bianm = "";
		while(mdrsl.next()) {
			if (mdrsl.getString("XIANGM").equals("��Ӧ������")) {
				sbrsl.append("'").append(mdrsl.getString("ZHI")).append("', ");
				czrsl.append("(select id from gongysb where mingc = '").append(mdrsl.getString("ZHI")).append("'),");
			} else if(mdrsl.getString("XIANGM").equals("����")) {
				sbrsl.append(mdrsl.getString("ZHI")).append(", ");
			} else if(mdrsl.getString("XIANGM").equals("�۶�")) {
				sbrsl.append(mdrsl.getString("ZHI")).append(", ");
			} else if(mdrsl.getString("XIANGM").equals("��������")) {
				sbrsl.append(mdrsl.getString("ZHI")).append(", ");
				czrsl.append(mdrsl.getString("ZHI")).append(", ");
			} else if(mdrsl.getString("XIANGM").equals("����")) {
				sbrsl.append(mdrsl.getString("ZHI")).append(", ");
				czrsl.append(mdrsl.getString("ZHI")).append(", ");
			} else if(mdrsl.getString("XIANGM").equals("����˰�ӷ�")) {
//				visit.setDouble4(Double.parseDouble(mdrsl.getString("ZHI")));
				sbrsl.append(mdrsl.getString("ZHI")).append(", ");
			} else if(mdrsl.getString("XIANGM").equals("�ӷ�˰��")) {
//				visit.setDouble5(Double.parseDouble(mdrsl.getString("ZHI")));
				sbrsl.append(mdrsl.getString("ZHI")).append(", ");
			} else if(mdrsl.getString("XIANGM").equals("���úϼ�")) {
//				visit.setDouble6(Double.parseDouble(mdrsl.getString("ZHI")));
				sbrsl.append(mdrsl.getString("ZHI")).append(", ");
				czrsl.append(mdrsl.getString("ZHI")).append(", ");
			} else if (mdrsl.getString("XIANGM").equals("�տλ")) {
				sbrsl.append(getShoukdwValue().getId()).append(", ");
				czrsl.append(getShoukdwValue().getId()).append(", ");
			} else if (mdrsl.getString("XIANGM").equals("��������")) {
				sbrsl.append("'").append(mdrsl.getString("ZHI")).append("', ");
			} else if (mdrsl.getString("XIANGM").equals("�˺�")) {
				sbrsl.append("'").append(mdrsl.getString("ZHI")).append("', ");
			} else if (mdrsl.getString("XIANGM").equals("��Ʊ���")) {
				sbrsl.append("'").append(mdrsl.getString("ZHI")).append("', ");
			} else if (mdrsl.getString("XIANGM").equals("��ʼ����") || mdrsl.getString("XIANGM").equals("��ֹ����")) {
				sbrsl.append("to_date('").append(mdrsl.getString("ZHI")).append("', 'yyyy-MM-dd'), ");
				czrsl.append("to_date('").append(mdrsl.getString("ZHI")).append("', 'yyyy-MM-dd'), ");
			} else if(mdrsl.getString("XIANGM").equals("��ע")) {
				sbrsl.append("'").append(mdrsl.getString("ZHI")).append("' ");
			} else {
				//sbrsl.append(mdrsl.getString("ZHI")).append(", ");
			}
		}
		
		StringBuffer sbsql2 = new StringBuffer();
		sbsql2.append("select getnewid(").append(visit.getDiancxxb_id()).append(") zf_id, ").append("getnewid(").append(visit.getDiancxxb_id()).append(") cnzf_id ").append(" from dual");
		ResultSetList idrsl = con.getResultSetList(sbsql2.toString());
		String newId_zf = "";
		String newId_cnzf = "";
		while(idrsl.next()) {
			newId_zf = idrsl.getString("zf_id");
			newId_cnzf = idrsl.getString("cnzf_id");
		}
		
		String sltsql = "select bianm from zafjsb where bianm = '"+ bianm +"'";
		ResultSetList sltrsl = con.getResultSetList(sltsql);
		if (sltrsl.next()) {
			this.setMsg("�������Ѿ����ڣ�����������");
			return false;
		} else {
			
			
			
			String ids = visit.getString11();
			istsql.append("insert into zafjsb(id, bianm, riq, diancxxb_id, gongysmc, ches, koud, jiessl, danj, buhszf, zafsk, hej, shoukdw_id, kaihyh, zhangh, fapbh, qiszyfw, jiezzyfw, beiz)");
			istsql.append(" values(")
				.append(newId_zf).append(", '")
				.append(visit.getString12()).append("', ")
				.append("to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd')").append(", ")
				.append(visit.getDiancxxb_id()).append(", ")
				.append(sbrsl.toString()).append("); \n");
			
			istsql.append("insert into changnzfb (id,riq,diancxxb_id,feiyxm_item_id,feiylb_item_id,shuil,zafjsb_id,gongysb_id,shul,danj,feiyhj,shoukdw_id,qiszyfw,jiezzyfw,beiz)");
			istsql.append(" values(")
				.append(newId_cnzf).append(",")
				.append("to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd')").append(",")
				.append(visit.getString12()).append(", ")
				.append("(select id from item where mingc='").append(getShoukdwValue().getValue()).append("'),")
				.append("(select id from item where mingc='����')").append(",")
				.append(MainGlobal.getXitsz("װ����˰��", visit.getDiancxxb_id()+"","0.07")).append(",")
				.append(newId_zf).append(",")
				.append(czrsl.toString()).append("''); \n");
			
			String[] sb = null;
			String[] sb1 = null;
			sb=visit.getString11().split(",");
			String sql_gl = "";
			String ches = "";
			String duns = "";
			String koud = "";
			ResultSetList rsl = null;
			//����������
			for(int i=0;i<sb.length;i++){
				sb1=sb[i].split(";");
				sql_gl = "select count(c.id) as ches,sum(c.maoz - c.piz - c.zongkd) as duns,sum(c.zongkd) as koud " +
						"	from fahb f,chepb c " +
						"	where c.fahb_id=f.id  " +
						"		and f.id=" + sb1[1];
				rsl = con.getResultSetList(sql_gl.toString());
				while(rsl.next()){
					ches=rsl.getString("ches");
					duns=rsl.getString("duns");
					koud=rsl.getString("koud");
				}
				
				istsql.append("insert into zhuangcjsglb values(")
				.append(MainGlobal.getNewID(visit.getDiancxxb_id())).append(",")
				.append(sb1[1]).append(",")
				.append(sb1[0]).append(",")
				.append(newId_cnzf).append(",")
				.append(duns).append(",")
				.append(ches).append(",")
				.append("'');");
			}
			
			istsql.append("end;");
			
			if(istsql.length()>13){
				if(con.getUpdate(istsql.toString())>-1){
					this.setMsg("����ɹ���");
				} else {
					this.setMsg("����ʧ�ܣ�");
					return false;
				}
			}
		}
		mdrsl.close();
		idrsl.close();
		sltrsl.close();
		con.Close();
		return true;
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
	
	//	�տλ
	public IDropDownBean getShoukdwValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getShoukdwModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setShoukdwValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {

			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
	}

	public void setShoukdwModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getShoukdwModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {

			getShoukdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getShoukdwModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		String  sql ="select s.id,s.mingc from shoukdw s,item i where i.mingc = s.mingc  order by decode(mingc,'"+visit.getString7()+"',0,1)";
		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean2(null);
			
			//˵�λ������
			getShoukdwValue();
			setShoukdwValue(null);
			getShoukdwModels();
		}
		getSelectData();
	}
}
