package com.zhiren.dc.jilgl.shujdr;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
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
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ����:tzf
 * ʱ��:2009-12-06
 * �޸�����:ú�� ��Ӧ�� �ھ� ����ʵ��  ���� ��Ӧ�̶�Ӧ�ĺ�ͬ�е糧������ĵ糧ֵ
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-16
 * �������������ݵ���ʱ��һ�����Ʒֳ��Ķ��Ĵ���
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-10-28
 * �������޸Ĺ�Ӧ������������leix����
 */
/*
 * ���ߣ����ܱ�
 * ʱ�䣺2009-09-27 10��20
 * ������ˢ��sql����������������,��Ҫ�Ǽ�������������
 */

/*
 * ���ߣ�����
 * ʱ�䣺2009-08-26 10��20
 * �������޸ĳ���combo��ȡֵ��ʽΪSQLȡֵ
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-08-22 13��57
 * �����������س�ʱ�䡢�ᳵʱ�䡢�ᳵ���Ա�ֶ���ʾ ����Gridϵͳ��������
 */
/*
 * ����:tzf
 * ʱ��:2009-06-17
 * �޸�����:���ڻ��� ���� �����ֶ�
 */
/*
 * ����:tzf
 * ʱ��:2009-06-17
 * �޸�����:����  �����滻 ����
 */
/*
 * ����:���
 * ʱ��:2013-06-25
 * �޸�����:ʹ�ò���MainGlobal.getXitxx_item("����", "���ݵ�����ϸ�鿴�����Ƿ��ܱ���", "0", "��")�Ա��水ť�Ƿ���ʾ���п���
 */
public class Shujdr extends BasePage implements PageValidateListener {
//	�����û���ʾ
//	ϵͳ�������ùؼ���
	private static final String customKey = "Shujdrcp"; 
	
	private static final int JINGZWS=3;//�����ֶα���λ��
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
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
	
	private boolean _ReturnChick = false;
	
	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
	
	private void Return(IRequestCycle cycle) {
		cycle.activate("Shujdrhz");
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
		String mokmc="";
		ResultSetList delrsl = getExtGrid().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			
			if(!(mokmc==null)&&!mokmc.equals("")){
				String id = delrsl.getString("id");
				//ɾ��ʱ������־
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_DEL,mokmc,
						"chepbtmp",id);
			}
			sql.append("delete from ").append(" chepbtmp ").append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}
		delrsl.close();
		ResultSetList mdrsl =  getExtGrid().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if ("0".equals(mdrsl.getString("ID"))) {
				sql.append("insert into ").append(" chepbtmp ").append("(id");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					sql.append(",").append(mdrsl.getColumnNames()[i]);
					sql2.append(",").append(
							getValueSql(getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
									mdrsl.getString(i)));
				}
				sql.append(") values(").append(sql2).append(");\n");
			} else {
				if(!(mokmc==null)&&!mokmc.equals("")){
					String id = mdrsl.getString("id");
					//����ʱ������־
					MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
							SysConstant.RizOpType_UP,mokmc,
							"chepbtmp",id);
				}
				sql.append("update ").append("chepbtmp").append(" set ");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					if(mdrsl.getColumnNames()[i].equals("JINGZ")){
						//��Ϊ���ݿ����޾����ֶ�,���Բ�����
					}else{
						sql.append(mdrsl.getColumnNames()[i]).append(" = ");
						sql.append(
								getValueSql(getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
										mdrsl.getString(i))).append(",");
					}
					
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(
						";\n");
			}
		}
		mdrsl.close();
		sql.append("end;");
		int flag = con.getUpdate(sql.toString());
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
			return value == null || "".equals(value) ? "null" : value;
		} else {
			return value;
		}
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
		if (_ReturnChick) {
			_ReturnChick = false;
			Return(cycle);
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String jingzStr="";
		jingzStr=" round_new(c.maoz-c.piz-c.koud-c.kous-c.kouz ,"+JINGZWS+")as jingz ,";
		
		StringBuffer sb = new StringBuffer();
		String dcsql = "c.diancxxb_id,";
		if(visit.isFencb()){
			dcsql = "d.mingc as diancxxb_id,";
		}
		if(visit.getDiancmc().equals("��������")){
			sb.append("select c.id, "+dcsql+" c.gongysmc, c.meikdwmc, c.faz,\n");
			sb.append("c.pinz, c.jihkj, c.fahrq, c.daohrq, c.jianjfs,c.chebb_id,\n");
			sb.append("c.chec,caiybh, to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss') zhongcsj,\n");
			sb.append(" c.zhongcjjy, to_char(c.qingcsj,'yyyy-mm-dd hh24:mi:ss') qingcsj,\n");
			sb.append(" c.qingcjjy, c.cheph, c.maoz, c.piz,"+jingzStr+" c.biaoz,c.yuns,c.yingk,\n");
			sb.append("c.koud, c.kous, c.kouz, c.sanfsl, c.daoz,yuandz,c.yuanshdw,\n");
			sb.append("c.meikdwmc yuanmkdw, c.yunsdw yunsdw, c.daozch, c.beiz ,c.fahbtmp_id from chepbtmp c where ");
			sb.append(" daohrq = to_date('"+visit.getString15()+"','yyyy-mm-dd') and fahbtmp_id = "+visit.getLong1()+ Jilcz.filterDcid(visit, "c") + " and fahb_id = 0");
		}else{
			sb.append("select c.id, "+dcsql+" c.gongysmc, c.meikdwmc, c.faz,\n");
			sb.append("c.pinz, c.jihkj, c.fahrq, c.daohrq, c.jianjfs,c.chebb_id,\n");
			sb.append("c.chec,caiybh, to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss') zhongcsj,\n");
			sb.append(" c.zhongcjjy, to_char(c.qingcsj,'yyyy-mm-dd hh24:mi:ss') qingcsj,\n");
			sb.append(" c.qingcjjy, c.cheph, c.maoz, c.piz,"+jingzStr+" c.biaoz,c.yuns,c.yingk,\n");
			sb.append("c.koud, c.kous, c.kouz, c.sanfsl, c.daoz,yuandz,c.yuanshdw,\n");
			sb.append("c.meikdwmc yuanmkdw, nvl(c.yunsdw,' ') yunsdw, c.daozch, c.beiz ,c.fahbtmp_id from chepbtmp c, diancxxb d where c.diancxxb_id = d.id and ");
			sb.append(" fahbtmp_id = "+visit.getLong1()+ Jilcz.filterDcid(visit, "c") + " and fahb_id = 0");
		}
		
		
		ResultSetList rsl = con.getResultSetList(sb.toString());
		
		int count=0;
		if(rsl!=null){
			count=rsl.getRows();
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl, customKey);
		egu.setTableName("chepbtmp");
		
//		egu.getColumn("diancxxb_id").setHidden(true);
//		egu.getColumn("diancxxb_id").editor = null;
		if(visit.isFencb()) {
			egu.getColumn("diancxxb_id").setHeader("�糧��λ");
			ComboBox dc= new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(dc);
			dc.setEditable(true);
			String dcsb="select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id();
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(dcsb));
//			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
			egu.getColumn("diancxxb_id").setWidth(70);
			egu.getColumn("diancxxb_id").returnId = true;
		}else {
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").editor = null;
		}
		egu.getColumn("gongysmc").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysmc").setEditor(null);
		egu.getColumn("meikdwmc").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikdwmc").setEditor(null);
		egu.getColumn("faz").setHeader(Locale.faz_id_fahb);
		egu.getColumn("faz").setWidth(65);
		egu.getColumn("faz").setEditor(null);
		egu.getColumn("pinz").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinz").setWidth(50);
		egu.getColumn("pinz").setEditor(null);
		egu.getColumn("jihkj").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkj").setWidth(65);
		egu.getColumn("jihkj").setEditor(null);
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("fahrq").setWidth(70);
		
		String fah = "select zhi from xitxxb where mingc = '���ݵ���ɷ�༭��������' and leib='����' and zhuangt = 1 and diancxxb_id = " 
		+ visit.getDiancxxb_id();
		ResultSetList rs = con.getResultSetList(fah);
		if(rs.next()){
			if(!"��".equals(rs.getString("zhi"))){
				egu.getColumn("fahrq").setEditor(null);
			}
		}
		
		
		egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
		egu.getColumn("daohrq").setWidth(70);
		String daohrq="select zhi from xitxxb where mingc = '���ݵ���ɷ�༭��������' and leib='����' and zhuangt = 1 and diancxxb_id = " 
			+ visit.getDiancxxb_id();
		rs=con.getResultSetList(daohrq);
		if(rs.next()){
			if(!"��".equals(rs.getString("zhi"))){
				egu.getColumn("daohrq").setEditor(null);
			}
		}else{
			egu.getColumn("daohrq").setEditor(null);
		}
		
		egu.getColumn("jianjfs").setHeader(Locale.jianjfs_chepb);
		egu.getColumn("jianjfs").setWidth(60);
		egu.getColumn("jianjfs").setEditor(null);
		egu.getColumn("chebb_id").setHeader(Locale.chebb_id_chepb);
		egu.getColumn("chebb_id").setWidth(60);
		egu.getColumn("chebb_id").setEditor(null);
		egu.getColumn("chebb_id").returnId=false;
		egu.getColumn("chebb_id").setHidden(true);
		egu.getColumn("chec").setHeader(Locale.chec_fahb);
		egu.getColumn("chec").setWidth(60);
//		egu.getColumn("chec").setEditor(null);
		egu.getColumn("caiybh").setHeader(Locale.caiybm_caiyb);
		egu.getColumn("zhongcsj").setHeader(Locale.zhongcsj_chepb);
		egu.getColumn("zhongcsj").setWidth(80);
		egu.getColumn("zhongcsj").setEditor(null);
		egu.getColumn("zhongcsj").update = false;
		egu.getColumn("zhongcsj").setHidden(true);
		egu.getColumn("zhongcjjy").setHeader(Locale.zhongcjjy_chepb);
		egu.getColumn("zhongcjjy").setWidth(80);
		egu.getColumn("zhongcjjy").setEditor(null);
		egu.getColumn("qingcsj").setHeader(Locale.qingcsj_chepb);
		egu.getColumn("qingcsj").setWidth(80);
		egu.getColumn("qingcsj").setEditor(null);
		egu.getColumn("qingcsj").update = false;
		egu.getColumn("qingcsj").setHidden(true);
		egu.getColumn("qingcjjy").setHeader(Locale.qingcjjy_chepb);
		egu.getColumn("qingcjjy").setWidth(80);
		egu.getColumn("qingcjjy").setEditor(null);
		egu.getColumn("qingcjjy").setHidden(true);
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		//�𳵿��Ա༭��Ƥ��
		if(!(this.getLeix()!=null && this.getLeix().equals("HY"))){
			egu.getColumn("cheph").setEditor(null);
		}
		
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("maoz").setWidth(65);
	
		egu.getColumn("piz").setHeader(Locale.piz_chepb);
		egu.getColumn("piz").setWidth(65);
		
		egu.getColumn("jingz").setHeader(Locale.jingz_chepb);
		egu.getColumn("jingz").setWidth(65);
		egu.getColumn("jingz").setEditor(null);
		//����ú�Ƿ���Ա༭ë��,Ƥ��,Ĭ�ϲ����Ա༭
		if(this.getLeix()!=null && this.getLeix().equals("HY")){
			String MPJ="select zhi from xitxxb where mingc = '�����ݵ���ɷ�༭ë��Ƥ��' and leib='����' and zhuangt = 1 and diancxxb_id = " 
				+ visit.getDiancxxb_id();
			rs=con.getResultSetList(MPJ);
			if(rs.next()){
				if(!"��".equals(rs.getString("zhi"))){
					egu.getColumn("maoz").setEditor(null);
					egu.getColumn("piz").setEditor(null);
					
				}
			}else{
				egu.getColumn("maoz").setEditor(null);
				egu.getColumn("piz").setEditor(null);
				
			}
		}else{//���˲�����༭ë��,Ƥ��,����
			egu.getColumn("maoz").setEditor(null);
			egu.getColumn("piz").setEditor(null);
			
		}
		
		egu.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
		egu.getColumn("biaoz").setWidth(65);
		egu.getColumn("koud").setHeader(Locale.koud_chepb);
		egu.getColumn("koud").setDefaultValue("0");//����Ĭ��ֵ
		egu.getColumn("koud").setWidth(60);
		egu.getColumn("kous").setHeader(Locale.kous_chepb);
		egu.getColumn("kous").setDefaultValue("0");
		egu.getColumn("kous").setWidth(60);
		egu.getColumn("kouz").setHeader(Locale.kouz_chepb);
		egu.getColumn("kouz").setDefaultValue("0");
		egu.getColumn("kouz").setWidth(60);
		egu.getColumn("sanfsl").setHeader(Locale.sanfsl_chepb);
		egu.getColumn("sanfsl").setDefaultValue("0");
		egu.getColumn("sanfsl").setWidth(60);
		egu.getColumn("daoz").setHeader(Locale.daoz_id_fahb);
		egu.getColumn("daoz").setWidth(65);
		egu.getColumn("yuandz").setHeader(Locale.yuandz_id_fahb);
		egu.getColumn("yuandz").setWidth(65);
		egu.getColumn("yuanshdw").setHeader(Locale.yuanshdwb_id_fahb);
		egu.getColumn("yuanmkdw").setHeader(Locale.yuanmkdw_chepb);
		
		egu.getColumn("yunsdw").setHeader(Locale.yunsdw_id_chepb);
		egu.getColumn("daozch").setHeader(Locale.daozch_chepb);
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu.getColumn("fahbtmp_id").setHidden(true);
		egu.getColumn("fahbtmp_id").setEditor(null);
		
		
		
		//���ù�Ӧ��������
		ComboBox c8 = new ComboBox();
		egu.getColumn("gongysmc").setEditor(c8);
		c8.setEditable(true);
		String gyssb = "select id,mingc from gongysb where leix=1 order by mingc";
		egu.getColumn("gongysmc").setComboEditor(egu.gridId,
				new IDropDownModel(gyssb));
		egu.getColumn("gongysmc").returnId=false;
		//����ú��λ������
		ComboBox c9 = new ComboBox();
		egu.getColumn("meikdwmc").setEditor(c9);
		c9.setEditable(true);
		c9.setListeners("select:function(own,rec,index){gridDiv_grid.getSelectionModel().getSelected().set('YUANMKDW',own.getRawValue())}");
		String mksb = "select id,mingc from meikxxb order by mingc";
		egu.getColumn("meikdwmc").setComboEditor(egu.gridId,
				new IDropDownModel(mksb));
		egu.getColumn("meikdwmc").returnId=false;
		//���÷�վ������
		ComboBox c0 = new ComboBox();
		egu.getColumn("faz").setEditor(c0);
		c0.setEditable(true);
		String Fazsb = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("faz").setComboEditor(egu.gridId,
				new IDropDownModel(Fazsb));
		egu.getColumn("faz").returnId=false;

		//		���õ�վ������
		ComboBox c1 = new ComboBox();
		egu.getColumn("daoz").setEditor(c1);
		c1.setEditable(true);
		String daozsb = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("daoz").setComboEditor(egu.gridId,
				new IDropDownModel(daozsb));
		egu.getColumn("daoz").returnId=false;
		//����Ʒ��������
		ComboBox c2 = new ComboBox();
		egu.getColumn("pinz").setEditor(c2);
		c2.setEditable(true);
		String pinzsb = SysConstant.SQL_Pinz_mei;
		egu.getColumn("pinz").setComboEditor(egu.gridId,
				new IDropDownModel(pinzsb));
		egu.getColumn("pinz").returnId=false;
		//���ÿھ�������
		ComboBox c3 = new ComboBox();
		egu.getColumn("jihkj").setEditor(c3);
		c3.setEditable(true);
		String jihkjsb = SysConstant.SQL_Kouj;
		egu.getColumn("jihkj").setComboEditor(egu.gridId,
				new IDropDownModel(jihkjsb));
		egu.getColumn("jihkj").returnId=false;
		//���ü�﷽ʽ������
		List l = new ArrayList();
		l.add(new IDropDownBean(0, "����"));
		l.add(new IDropDownBean(1, "���"));
		ComboBox c4 = new ComboBox();
		egu.getColumn("jianjfs").setEditor(c4);
		c4.setEditable(true);
		egu.getColumn("jianjfs").setComboEditor(egu.gridId,
				new IDropDownModel(l));
		egu.getColumn("jianjfs").returnId=false;

		//���ó���������
		ComboBox c5 = new ComboBox();
		egu.getColumn("chebb_id").setEditor(c5);
		c5.setEditable(true);
		egu.getColumn("chebb_id").setComboEditor(egu.gridId,
				new IDropDownModel(SysConstant.SQL_Cheb));

		//����ԭ��վ������
		ComboBox c6 = new ComboBox();
		egu.getColumn("yuandz").setEditor(c6);
		c6.setEditable(true);
		String Yuandzsb = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("yuandz").setComboEditor(egu.gridId,
				new IDropDownModel(Yuandzsb));
		egu.getColumn("yuandz").setDefaultValue(visit.getDaoz());
		egu.getColumn("yuandz").returnId=false;
		
		//����ԭ�ջ���λ������
		ComboBox c7 = new ComboBox();
		egu.getColumn("yuanshdw").setEditor(c7);
		c7.setEditable(true);//���ÿ�����
		String sql = "select id,mingc from vwyuanshdw order by mingc";
		egu.getColumn("yuanshdw").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
		egu.getColumn("yuanshdw").returnId=false;

		//�������䵥λ������
		ComboBox comb = new ComboBox();
		egu.getColumn("yunsdw").setEditor(comb);
		comb.setEditable(true);
		String yunsdwsb = "select id,mingc from yunsdwb where diancxxb_id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		egu.getColumn("yunsdw").setComboEditor(egu.gridId,
				new IDropDownModel(yunsdwsb));
		egu.getColumn("yunsdw").returnId=false;
		
		//����GRID�Ƿ���Ա༭
		boolean canEditData=false;
		if (canEditData){
			egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		}else{
			egu.setGridType(ExtGridUtil.Gridstyle_Read );
		}
		//����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
		//����ÿҳ��ʾ����
		egu.addPaging(0);

		if(MainGlobal.getXitxx_item("����", "���ݵ�����ϸ�鿴�����Ƿ��ܱ���", "0", "��").equals("��")){
			egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		}
		
		GridButton bc = new GridButton("����","function(){ " +
				" document.getElementById('ReturnButton').click();}");
		bc.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(bc);

		StringBuffer sbjs = new StringBuffer();
//		sbjs.append("gridDiv_grid.on('afteredit',function(e){");
		sbjs
		.append("if(" +
				" e.field=='DIANCXXB_ID')" +
				"{ " +
		"var ches =gridDiv_ds.getAt(e.row).get('DIANCXXB_ID')" +
		";"+
//		"if(e.row==1||e.row==2){gridDiv_ds.getAt(e.row).set('VALUE',Round_new(ches,2));}else{}"+
		"for (var i=0;i<("+count+"-e.row);i++){ gridDiv_ds.getAt(e.row+i).set('DIANCXXB_ID',ches);}" +
		"}");
//		sbjs.append("});");
//		egu.addOtherScript(sbjs.toString());
		
		if(this.getLeix()!=null && this.getLeix().equals("HY")){
			
			
			
			String dhsql=" select *   from  xitxxb where mingc='�����ݵ���鿴�����滻' and zhi='��' and leib='����' and zhuangt=1 ";
			
			ResultSetList rt=con.getResultSetList(dhsql);
			
			boolean isDefaultChecked=false;
			if(rt.next()){
				isDefaultChecked=true;
			}
			
			Checkbox cbselectlike=new Checkbox();
			
			cbselectlike.setChecked(isDefaultChecked);
			cbselectlike.setId("SelectLike");
			egu.addToolbarItem(cbselectlike.getScript());
			egu.addTbarText("�����滻");
			egu.addOtherScript("gridDiv_grid.on('afteredit',function(e){if(!SelectLike.checked){return;};for(i=e.row;i<gridDiv_ds.getCount();i++){gridDiv_ds.getAt(i).set(e.field,e.value);}});\n");
		}else if(this.getLeix()!=null && this.getLeix().equals("QY")){
			

			
			String dhsql=" select *   from  xitxxb where mingc='�������ݵ���鿴�����滻' and zhi='��' and leib='����' and zhuangt=1";
			
			ResultSetList rt=con.getResultSetList(dhsql);
			
			if(rt.next()){
				Checkbox cbselectlike=new Checkbox();
				
//				cbselectlike.setChecked(isDefaultChecked);
				cbselectlike.setId("SelectLike");
				egu.addToolbarItem(cbselectlike.getScript());
				egu.addTbarText("�����滻");
				egu.addOtherScript("gridDiv_grid.on('afteredit',function(e){if(!SelectLike.checked){return;};for(i=e.row;i<gridDiv_ds.getCount();i++){gridDiv_ds.getAt(i).set(e.field,e.value);}"+sbjs.toString()+"});\n");
			}else{
				
				egu.addOtherScript("gridDiv_grid.on('afteredit',function(e){"+sbjs.toString()+"});\n");
				
			}
		}
		
		
		if(this.getLeix()!=null && this.getLeix().equals("HY")){
			
			rsl=con.getResultSetList("select  * from xitxxb where mingc='��·���ݵ���鿴������ʽ' and leib='����' and zhuangt=1 and  zhi='��' and diancxxb_id="+visit.getDiancxxb_id());
			if(rsl.next()){
				
				
				egu.getColumn("gongysmc").setEditor(null);
				egu.getColumn("meikdwmc").setEditor(null);
				egu.getColumn("faz").setEditor(null);
				egu.getColumn("jihkj").setEditor(null);
				
				
				egu
				.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){ "
						+ "row = irow; \n"
						+ "if('GONGYSMC' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
						+ "gongysTree_window.show();}});\n");
				
				
				
				
				DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk_cz_kj,
						"gongysTree", "" + visit.getDiancxxb_id(), null, null, null);
				Toolbar bbar = dt.getTree().getTreePanel().getBbar();
				bbar.deleteItem();
				StringBuffer handler = new StringBuffer();
				handler
						.append("function() { \n")
						.append(
								"var cks = gongysTree_treePanel.getSelectionModel().getSelectedNode(); var dcmc=null; \n")
						.append("if(cks==null){gongysTree_window.hide();return;} \n")
						.append(
								"rec = gridDiv_grid.getSelectionModel().getSelected(); \n")
						.append("if(cks.getDepth() == 4){ \n")
						.append(
								"rec.set('GONGYSMC', cks.parentNode.parentNode.parentNode.text);\n")
						.append(
								"rec.set('MEIKDWMC', cks.parentNode.parentNode.text);\n")
						.append(
								"rec.set('YUANMKDW', cks.parentNode.parentNode.text);\n")
						.append(
								"rec.set('FAZ', cks.parentNode.text);rec.set('JIHKJ', cks.text);\n" +
								" eval(' dcmc=gysmkhtTree'+getAfValue(cks.parentNode.parentNode.parentNode.id)+'.firstChild;'); \n" +
								" if(dcmc!=null){rec.set('DIANCXXB_ID',dcmc.text);}\n")
						.append("}else if(cks.getDepth() == 3){\n")
						.append(
								"rec.set('GONGYSMC', cks.parentNode.parentNode.text);\n")
						.append("rec.set('MEIKDWMC', cks.parentNode.text);\n")
						.append("rec.set('YUANMKDW', cks.parentNode.text);\n").append(
								"rec.set('FAZ', cks.text);\n")
						.append(" eval(' dcmc=gysmkhtTree'+getAfValue(cks.parentNode.parentNode.id)+'.firstChild;'); \n" +
								" if(dcmc!=null){rec.set('DIANCXXB_ID',dcmc.text);}\n").append(
								"}else if(cks.getDepth() == 2){\n").append(
								"rec.set('GONGYSMC', cks.parentNode.text);\n")
						.append("rec.set('MEIKDWMC', cks.text);\n").append(
								"rec.set('YUANMKDW', cks.text);\n").append(	" eval(' dcmc=gysmkhtTree'+getAfValue(cks.parentNode.id)+'.firstChild;'); \n" +
								" if(dcmc!=null){rec.set('DIANCXXB_ID',dcmc.text);}\n").append(
								"}else if(cks.getDepth() == 1){\n").append(
								"rec.set('GONGYSMC', cks.text); \n" +
								" eval(' dcmc=gysmkhtTree'+getAfValue(cks.id)+'.firstChild;'); if(dcmc!=null){rec.set('DIANCXXB_ID',dcmc.text);} }\n")
								.append(" replaceRecTree(gridDiv_ds,cks,dcmc);\n"+
								"gongysTree_window.hide();\n").append("return;")
						.append("}");
				ToolbarButton btn = new ToolbarButton(null, "ȷ��", handler.toString());
				bbar.addItem(btn);
				visit.setDefaultTree(dt);
				
				
				//��Ӧ��ú���ͬ��
				 String gys_mk_het_sql = "select wid id,mingc,jib,level ll,fuid from\n" +
					"\t\t\t(select 0 wid,0 id, 'ȫ��' mingc, -1 fuid, 1 jib from dual union\n" + 
					"\t\t\tselect wid,id,mingc,fuid,jib from\n" + 
					"      vwgongysmk_het where diancxxb_id = dcid)\n" + 
					"\t\t\twhere jib = level and CONNECT_BY_ISCYCLE = 0\n" + 
					"\t\t\tconnect by NOCYCLE fuid = prior id order SIBLINGS by fuid";
				
				DefaultTree dcgystree = new DefaultTree();
				dcgystree.setTree_window(gys_mk_het_sql,
						"gysmkhtTree", "" + visit.getDiancxxb_id(), null, null, null);
				Toolbar bbar_dcgys = dcgystree.getTree().getTreePanel().getBbar();
				bbar_dcgys.deleteItem();
				ToolbarButton gystb=new ToolbarButton(null, "ȷ��", "function(){}");
				bbar_dcgys.addItem(gystb);
				this.setDefaultTree(dcgystree);
				
				
			}
		}

		
		egu.setDefaultsortable(false);
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
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	public String getTreeScript1() {
		if(((Visit) this.getPage().getVisit()).getDefaultTree()==null){
			return "";
		}
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	
	private DefaultTree dftree2;
	public DefaultTree getDefaultTree() {
		return dftree2;
	}
	public void setDefaultTree(DefaultTree dftree2) {
		this.dftree2 = dftree2;
	}
	
	public String getTreeScript2() {
		if(getDefaultTree()==null){
			return "";
		}
		return  getDefaultTree().getScript();
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
			getSelectData();
		}
	}
	
	public String getLeix() {
		return ((Visit) this.getPage().getVisit()).getString2();
	}

	public void setLeix(String leix) {
		((Visit) this.getPage().getVisit()).setString2(leix);
	}

}