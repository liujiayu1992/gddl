package com.zhiren.dc.gdxw.jianj;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dc.huaygl.Caiycl;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ����:wzb
 * ʱ��:2010-12-11 16:36:14
 * �޸�����:����ú���ᵹ
 */

public class Changnbd_gz extends BasePage implements PageValidateListener {
	
	// �����û���ʾ
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

	public IDropDownModel getYunsdwModel() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sql = "select id,mingc from yunsdwb where diancxxb_id="
				+ visit.getDiancxxb_id();
		return new IDropDownModel(sql);
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	//�޸�
	private boolean _UpdateChick = false;

	public void UpdateButton(IRequestCycle cycle) {
		_UpdateChick = true;
	}


//�жϳ������û�л�Ƥ,�������ظ����غ�
	private boolean IsHuiPi(String cheph) {
		boolean issuod = false;
		JDBCcon con = new JDBCcon();
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select cl.piz from changnbdb cl where cl.cheph='");
		sql.append(cheph).append("'");
		ps = con.getPresultSet(sql.toString());
		try {
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getDouble("piz")==0) {
					issuod = true;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
				con.Close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return issuod;
	}
	
	
	private void Save() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û�������κθ���Ŷ��");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(this.getClass().getName() + "\n"
							+ ErrorMessage.NullResult + "\n");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		int flag = 0;
		
		
		
		
		if (rsl.next()) {
			
			if (IsHuiPi(rsl.getString("qiccht_id")+rsl.getString("cheph"))) {
				setMsg("�����������,�ó���δ��Ƥ!");
				return;
			}
			IDropDownModel idm = new IDropDownModel(SysConstant.SQL_Meic);
			IDropDownModel ysdw = new IDropDownModel(SysConstant.SQL_Yunsdw);
			
			long diancxxb_id = 0;
			if (visit.isFencb()) {
				diancxxb_id = (getExtGrid().getColumn("diancxxb_id").combo)
						.getBeanId(rsl.getString("diancxxb_id"));
			} else {
				diancxxb_id = visit.getDiancxxb_id();
			}
			String cheht = rsl.getString("qiccht_id");
			String cheph=rsl.getString("cheph");
			double maoz = rsl.getDouble("maoz");
			long come_meicb_id=idm.getBeanId(rsl.getString("come_meicb_id"));
			long go_meicb_id=idm.getBeanId(rsl.getString("go_meicb_id"));
			long yunsdwb_id=ysdw.getBeanId(rsl.getString("yunsdwb_id"));
			String pinz=rsl.getString("pinz");
			String zhongchh=rsl.getString("zhongchh");
			String zhongcjjy=rsl.getString("zhongcjjy");
			
			
		
			
			String sql="insert into changnbdb(id,diancxxb_id,pinz,come_meicb_id,go_meicb_id,fahrq,daohrq,cheph,maoz,"+
						"zhongcsj,zhongchh,zhongcjjy,yunsdwb_id"+
						") values ( getnewid("+visit.getDiancxxb_id()+"),"+visit.getDiancxxb_id()+",'"+pinz+"',"+come_meicb_id+"," +
						""+go_meicb_id+",to_date(to_char(sysdate,'yyyy-mm-dd'), 'yyyy-mm-dd'),to_date(to_char(sysdate,'yyyy-mm-dd'), 'yyyy-mm-dd')," +
						"'"+cheht+"'||'"+cheph+"',"+maoz+",sysdate,'"+zhongchh+"','"+zhongcjjy+"',"+yunsdwb_id+")";
			flag = con.getInsert(sql);
			if(flag == -1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(this.getClass().getName() + 
						"\nSQL:" + sql +"����changnbdbʧ��!");
				setMsg(this.getClass().getName() + ":����changnbdbʧ��!");
				return;
			}
			
		}
		con.commit();
		con.Close();
		setMsg("ë�ر���ɹ�");
	}
	
	//�޸ı���
	private void UpdateSave() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û�������κθ���Ŷ��");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ResultSetList rsl = getPizGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(this.getClass().getName() + "\n"
							+ ErrorMessage.NullResult + "\n");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		int flag = 0;
		
		
		
		
		if (rsl.next()) {
			
		
			IDropDownModel idm = new IDropDownModel(SysConstant.SQL_Meic);
			IDropDownModel ysdw = new IDropDownModel(SysConstant.SQL_Yunsdw);
			
			
			String id=rsl.getString("id");
			String cheph=rsl.getString("cheph");
			long come_meicb_id=idm.getBeanId(rsl.getString("come_meicb_id"));
			long go_meicb_id=idm.getBeanId(rsl.getString("go_meicb_id"));
			long yunsdwb_id=ysdw.getBeanId(rsl.getString("yunsdwb_id"));
			String pinz=rsl.getString("pinz");
			
			
			String sql="update changnbdb set cheph='"+cheph+"',come_meicb_id="+come_meicb_id+",go_meicb_id="+go_meicb_id+"," +
					"yunsdwb_id="+yunsdwb_id+",pinz='"+pinz+"' where id="+id;
		
			
		
			flag = con.getUpdate(sql);
			if(flag == -1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(this.getClass().getName() + 
						"\nSQL:" + sql +"����changnbdbʧ��!");
				setMsg(this.getClass().getName() + ":����changnbdbʧ��!");
				return;
			}
			
		}
		con.commit();
		con.Close();
		setMsg("�����޸ĳɹ�");
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			init();
		}
		if (_UpdateChick) {
			_UpdateChick = false;
			UpdateSave();
			init();
		}
		
		
	}

	

	public void setChepid(String fahids) {
		((Visit) this.getPage().getVisit()).setString1(fahids);
	}



	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String tempst="";
		JDBCcon con = new JDBCcon();
		String sqldc = "and d.id = " + visit.getDiancxxb_id();
		String dcmcdef = visit.getDiancmc();
		String dcsql = "select id,mingc from diancxxb where fuid="
			+ visit.getDiancxxb_id();
		ResultSetList rsl;
//		�����һ������
		if(visit.isFencb()){
			sqldc = "and d.fuid = " + visit.getDiancxxb_id();
//			ȡ��Ĭ�ϵ糧����
			rsl = con.getResultSetList(dcsql);
			if(rsl.next()){
				dcmcdef = rsl.getString("mingc");
			}
			rsl.close();
		}

		String sql=
			"select id,qiccht_id,cheph,diancxxb_id,maoz,come_meicb_id,\n" +
			"go_meicb_id,yunsdwb_id,pinz,zhongcjjy,zhongchh\n" + 
			"from (\n" + 
			"select 0 as id,qi.mingc as qiccht_id,'' as cheph,bd.diancxxb_id ,\n" + 
			"0 as maoz,p.mingc as pinz,mc1.mingc as come_meicb_id,mc2.mingc as go_meicb_id,\n" + 
			"ys.mingc as yunsdwb_id,'"+visit.getRenymc()+"' as zhongcjjy,'' as zhongchh\n" + 
			"from changnbdb bd,meicb mc1,meicb mc2,qiccht qi,pinzb p,yunsdwb ys\n" + 
			"where bd.come_meicb_id=mc1.id\n" + 
			"and bd.go_meicb_id=mc2.id\n" + 
			"and bd.pinz=p.mingc\n" + 
			"and bd.cheph like '%' || qi.mingc(+) || '%'\n" + 
			"and bd.yunsdwb_id=ys.id(+)\n" + 
			"order by bd.id desc )\n" + 
			"where rownum=1";

		
		
		rsl = con.getResultSetList(sql);
//		���û��ȡ��Ĭ������
		if(!rsl.next()){
			sql="select 0 as id,'' as qiccht_id,'' as cheph,'' as diancxxb_id,0 as maoz,\n" +
				"'' as come_meicb_id,'' as go_meicb_id,'' as yunsdwb_id,'' as pinz,'"+visit.getRenymc()+"' as  zhongcjjy,'' as zhongchh\n" + 
				" from dual";

			rsl = con.getResultSetList(sql);
//			��ȡĬ�ϳ���ͷ
			String tempsql="select mingc from qiccht where zhuangt=1 order by xuh";
			ResultSetList temprsl=con.getResultSetList(tempsql);
			
			if(temprsl.next()){
				tempst=temprsl.getString("mingc");	 
			}
			temprsl.close();

		}else{
			
				rsl.beforefirst();
			
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
//		����GRID�ɱ༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// ����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
//		���ø�grid�����з�ҳ
		egu.addPaging(0);
		
//		��������
		egu.getColumn("qiccht_id").setCenterHeader("��ͷ");
		egu.getColumn("cheph").setCenterHeader("����");
		egu.getColumn("diancxxb_id").setCenterHeader("�糧id");
		egu.getColumn("maoz").setCenterHeader("ë��");
		egu.getColumn("pinz").setCenterHeader("Ʒ��");
		egu.getColumn("come_meicb_id").setCenterHeader("ú̿��Դ");
		egu.getColumn("go_meicb_id").setCenterHeader("жú�ص�");
		egu.getColumn("yunsdwb_id").setCenterHeader("���䵥λ");
		egu.getColumn("zhongcjjy").setCenterHeader("�س����Ա");
		egu.getColumn("zhongchh").setCenterHeader("�س����");
		
		
		
//		�����п�
		egu.getColumn("qiccht_id").setWidth(80);
		egu.getColumn("cheph").setWidth(90);
		egu.getColumn("diancxxb_id").setWidth(110);
		egu.getColumn("maoz").setWidth(80);
		egu.getColumn("pinz").setWidth(90);
		egu.getColumn("come_meicb_id").setWidth(140);
		egu.getColumn("go_meicb_id").setWidth(140);
		egu.getColumn("yunsdwb_id").setWidth(90);
		egu.getColumn("zhongcjjy").setWidth(100);
		egu.getColumn("zhongchh").setWidth(100);
//		�������Ƿ�ɱ༭
		egu.getColumn("id").setEditor(null);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("zhongcjjy").setEditor(null);
		egu.getColumn("zhongchh").setEditor(null);
		
//      ����Ĭ��ֵ
		egu.getColumn("qiccht_id").setDefaultValue(tempst);
		
		
//		�����е�������
		egu.getColumn("maoz").editor.setAllowBlank(false);
		sql = "select * from shuzhlfwb where leib ='����' and mingc = '������ë��' and diancxxb_id = "
				+ visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			egu.getColumn("maoz").editor.setMinValue(rsl.getString("xiax"));
			egu.getColumn("maoz").editor.setMaxValue(rsl.getString("shangx"));
		}
		rsl.close();

//		���÷ֳ���糧������
		if (visit.isFencb()) {
			ComboBox dc = new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(dc);
			dc.setEditable(true);
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
					new IDropDownModel(dcsql));
		} else {
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").setEditor(null);
			egu.getColumn("diancxxb_id").setDefaultValue(visit.getDiancxxb_id()+"");
		}
//		���ó�ͷ������
		ComboBox chet= new ComboBox();
		egu.getColumn("qiccht_id").setEditor(chet); 
		chet.setEditable(true);
		String chetSql="select id,mingc from qiccht  where zhuangt=1 order by xuh ";
		egu.getColumn("qiccht_id").setComboEditor(egu.gridId, new
		IDropDownModel(chetSql));
		
//		������Դú��������
		ComboBox cmc= new ComboBox();
		egu.getColumn("come_meicb_id").setEditor(cmc); 
		cmc.setEditable(true);
		String mcSql="select id,piny || '-' ||mingc from meicb order by xuh";
		egu.getColumn("come_meicb_id").setComboEditor(egu.gridId, new
		IDropDownModel(mcSql));
		
//		����жúú��������
		ComboBox xiemmc= new ComboBox();
		egu.getColumn("go_meicb_id").setEditor(xiemmc); 
		xiemmc.setEditable(true);
		String xiemmcmcSql="select id,piny || '-' ||mingc from meicb order by xuh";
		egu.getColumn("go_meicb_id").setComboEditor(egu.gridId, new
		IDropDownModel(xiemmcmcSql));
		
//		���䵥λ
		ComboBox yuns= new ComboBox();
		egu.getColumn("yunsdwb_id").setEditor(yuns); 
		yuns.setEditable(true);
		String yunsSql="select id,mingc from yunsdwb";
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId, new
		IDropDownModel(yunsSql));
		
//		Ʒ��
		ComboBox pinzmc= new ComboBox();
		egu.getColumn("pinz").setEditor(pinzmc); 
		pinzmc.setEditable(true);
		String pinzSql=" select id,mingc from pinzb  where leib='ú'";
		egu.getColumn("pinz").setComboEditor(egu.gridId, new
		IDropDownModel(pinzSql));
		
		GridButton refurbish = new GridButton("ˢ��",
		"function (){document.getElementById('RefurbishButton').click();}");
			refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addTbarText("-");// ���÷ָ���
		
		GridButton gbs = new GridButton("����ë��",GridButton.ButtonType_Save,
				"gridDiv", egu.getGridColumns(), "SaveButton");
		egu.addTbarBtn(gbs);
		egu.addTbarText("-");// ���÷ָ���
		egu.addTbarText("->");// ���÷ָ���
		
		
		
		
		
		int zhongc=0;
		int kongc=0;
		double jingz=0.0;
		int jus=0;
		
		//���ذ�����
		sql="select count(*) as zhongc from changnbdb c\n" +
			"where c.zhongcsj>=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')\n" + 
			"and c.zhongcsj<to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')+1 \n"+
			"and c.maoz>0\n" + 
			"and c.zhongcsj is not null";
		rsl = con.getResultSetList(sql);
		if (rsl.next()){
			zhongc=rsl.getInt("zhongc");
		}
		//��Ƥ����,����
		sql="select count(*) as huip,sum(c.maoz-c.piz) as jingz from changnbdb c\n" +
			"where c.qingcsj>=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')\n" + 
			"and c.qingcsj<to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')+1 \n"+
			"and c.piz>0\n" + 
			"and c.qingcsj is not null";
		rsl = con.getResultSetList(sql);
		if (rsl.next()){
			kongc=rsl.getInt("huip");
			jingz=rsl.getDouble("jingz");
		}
	
		
		egu.addTbarText(
				"���հᵹ:&nbsp;" +
				"&nbsp;����:"+zhongc+" ��,&nbsp;" +
				"����:"+kongc+" ��,&nbsp;�ᵹ��ú:"+jingz+"��" +
				"&nbsp;");

		
		egu.addTbarText("-");// ���÷ָ���
		egu.addOtherScript("gridDiv_grid.on('rowclick',function(){Mode='sel';DataIndex='MAOZ';  });");
		egu.addOtherScript("gridDiv_grid.on('beforeedit',function(e){ "
				+"if(e.row!=row_maoz_index){ e.cancel=true; } \n"
				+"if(e.field == 'MAOZ'){e.cancel=true;}});\n");
		
		egu.addOtherScript("function  gridDiv_save(rec){if(confirm('�Ƿ񱣴泵��:   '+rec.get('CHEPH') + '     ë��: ' + rec.get('MAOZ')+'        ')){return \"\";}else{return \"return\";}};");
		
		egu.addOtherScript(" gridDiv_grid.on('cellclick',function(grid,rowIndex,columnIndex,e){ \n " +
				" if(columnIndex==3){ \n" +
				" row_maoz_index=rowIndex;\n"+
				" gridDiv_grid.getView().refresh();\n"+
				" gridDiv_grid.getView().getRow(rowIndex).style.backgroundColor=\"red\";} \n"+
				" else { \n" +
//				" gridDiv_grid.getView().focusRow(row_maoz_index); \n" +
				" } \n"+
				" }); \n");
		
		egu.addOtherScript(" gridDiv_grid.addListener('afteredit',function(e){\n " +
				" gridDiv_grid.getView().getRow(e.row).style.backgroundColor=\"red\"; \n" +
				"} ); \n");
		setExtGrid(egu);
		
		
		
		
		/*  Ƥ��grid��ʼ��  */
		
		sql = "select bd.id,bd.cheph,bd.maoz,mc1.mingc as come_meicb_id,mc2.mingc as go_meicb_id,\n" +
			"to_char(bd.zhongcsj,'yyyy-mm-dd hh24:mi:ss') as zhongcsj,ys.mingc as yunsdwb_id,bd.pinz,bd.zhongcjjy,bd.zhongchh\n" + 
			"from changnbdb bd,meicb mc1,meicb mc2,yunsdwb ys\n" + 
			"where bd.come_meicb_id=mc1.id(+)\n" + 
			"and bd.go_meicb_id=mc2.id(+)\n" + 
			"and bd.yunsdwb_id=ys.id(+)\n" + 
			"and bd.qingcsj is null\n" + 
			"order by bd.id desc";

		rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu1 = new ExtGridUtil("gridDivPiz", rsl);
//		����ҳ��ɱ༭
		egu1.setGridType(ExtGridUtil.Gridstyle_Edit);
//		����ҳ����
		egu1.setWidth(Locale.Grid_DefaultWidth);
//		���ø�grid�����з�ҳ
		egu1.addPaging(0);
//		��������
		egu1.getColumn("cheph").setCenterHeader(Locale.cheph_chepb);
		egu1.getColumn("maoz").setCenterHeader(Locale.maoz_chepb);
		egu1.getColumn("come_meicb_id").setCenterHeader("ú̿��Դ");
		egu1.getColumn("go_meicb_id").setCenterHeader("жú�ص�");
		egu1.getColumn("zhongcsj").setCenterHeader("�س�ʱ��");
		egu1.getColumn("yunsdwb_id").setCenterHeader("���䵥λ");
		egu1.getColumn("pinz").setCenterHeader("Ʒ��");
		egu1.getColumn("zhongcjjy").setCenterHeader("�غ�Ա");
		egu1.getColumn("zhongchh").setCenterHeader("�س����");
		
//		�����п�
		egu1.getColumn("cheph").setWidth(110);
		egu1.getColumn("maoz").setWidth(80);
		egu1.getColumn("come_meicb_id").setWidth(150);
		egu1.getColumn("go_meicb_id").setWidth(150);
		egu1.getColumn("zhongcsj").setWidth(180);
		egu1.getColumn("yunsdwb_id").setWidth(100);
		egu1.getColumn("pinz").setWidth(70);
		egu1.getColumn("zhongcjjy").setWidth(90);
		egu1.getColumn("zhongchh").setWidth(90);

//		�������Ƿ�ɱ༭
		//egu1.getColumn("cheph").setEditor(null);
		egu1.getColumn("maoz").setEditor(null);
		//egu1.getColumn("come_meicb_id").setEditor(null);
		//egu1.getColumn("go_meicb_id").setEditor(null);
		egu1.getColumn("zhongcsj").setEditor(null);
		//egu1.getColumn("yunsdwb_id").setEditor(null);
		//egu1.getColumn("pinz").setEditor(null);
		egu1.getColumn("zhongcjjy").setEditor(null);
		egu1.getColumn("zhongchh").setEditor(null);
		
		
		
//		������Դú��������
		ComboBox cmc2= new ComboBox();
		egu1.getColumn("come_meicb_id").setEditor(cmc2); 
		cmc2.setEditable(true);
		String mcSql2="select id,piny || '-' ||mingc from meicb order by xuh";
		egu1.getColumn("come_meicb_id").setComboEditor(egu1.gridId, new
		IDropDownModel(mcSql2));
		
//		����жúú��������
		ComboBox xiemmc2= new ComboBox();
		egu1.getColumn("go_meicb_id").setEditor(xiemmc2); 
		xiemmc2.setEditable(true);
		String xiemmcmcSql2="select id,piny || '-' ||mingc from meicb order by xuh";
		egu1.getColumn("go_meicb_id").setComboEditor(egu1.gridId, new
		IDropDownModel(xiemmcmcSql2));
		
//		���䵥λ
		ComboBox yuns2= new ComboBox();
		egu1.getColumn("yunsdwb_id").setEditor(yuns2); 
		yuns2.setEditable(true);
		String yunsSql2="select id,mingc from yunsdwb";
		egu1.getColumn("yunsdwb_id").setComboEditor(egu1.gridId, new
		IDropDownModel(yunsSql2));
		
//		Ʒ��
		ComboBox pinzmc2= new ComboBox();
		egu1.getColumn("pinz").setEditor(pinzmc2); 
		pinzmc2.setEditable(true);
		String pinzSql2=" select id,mingc from pinzb  where leib='ú'";
		egu1.getColumn("pinz").setComboEditor(egu1.gridId, new
		IDropDownModel(pinzSql2));
		
		
		GridButton PIZ_refurbish = new GridButton("ˢ��",
		"function (){document.getElementById('RefurbishButton').click();}");
			refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu1.addTbarBtn(PIZ_refurbish);
		egu1.addTbarText("-");// ���÷ָ���
		

		GridButton gbs2 = new GridButton("�޸ı���",GridButton.ButtonType_Save,
				"gridDivPiz", egu1.getGridColumns(), "UpdateButton");
		egu1.addTbarBtn(gbs2);
		
		
		//�²�����ݵ�һ�б��
		//egu1.addOtherScript(" if(gridDivPiz_ds.getCount()>=1){ gridDivPiz_grid.getView().getRow(0).style.backgroundColor=\"red\"; }\n");
		setPizGrid(egu1);
		con.Close();
	}


	

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public ExtGridUtil getPizGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setPizGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridScriptPiz() {
		if (getPizGrid() == null) {
			return "";
		}
		return getPizGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	public String getGridPizHtml() {
		if (getPizGrid() == null) {
			return "";
		}
		return getPizGrid().getHtml();
	}

	public DefaultTree getDefaultTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}

	public void setDefaultTree(DefaultTree dftree1) {
		((Visit) this.getPage().getVisit()).setDefaultTree(dftree1);
	}

	public String getTreeScript() {
		if(getDefaultTree()==null){
			return "";
		}
		// System.out.print(((Visit)
		// this.getPage().getVisit()).getDefaultTree().getScript());
		return getDefaultTree().getScript();
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

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setGongysModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setGongysModels() {
		String sql = "select id,mingc from gongysb order by xuh,mingc";
		setGongysModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getMeikModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setMeikModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setMeikModels() {
		String sql = "select id,mingc from meikxxb order by xuh,mingc";
		setMeikModel(new IDropDownModel(sql));
	}

	private void setJianjdmodel() {
		JDBCcon con = new JDBCcon();
		String sql = "select zhi from xitxxb where mingc='�������ﵥģʽ' and zhuangt = 1 and diancxxb_id ="
				+ ((Visit) this.getPage().getVisit()).getDiancxxb_id();
		ResultSetList rsl = con.getResultSetList(sql);
		String model = "";
		if (rsl.next()) {
			model = rsl.getString("zhi");
		}
		rsl.close();
		((Visit) this.getPage().getVisit()).setString15(model);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setString1(null);
			init();
		}
	}

	private void init() {
		setGongysModels();
		setMeikModels();
		setJianjdmodel();
		setExtGrid(null);
		setPizGrid(null);
		setDefaultTree(null);
		getSelectData();
	}
}