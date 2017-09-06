package com.zhiren.dc.monthReport;

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

import com.zhiren.common.CustomMaths;
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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Zhilkdcx extends BasePage implements PageValidateListener {

	private static final String ITEM_ONE = "*"; //�ƻ��ھ��ϼ�ǰ��ӵķ��ţ����ת��Ϊ��д�������
	
	private static final String ITEM_TWO = "#"; //ͳ�䡢�ط�С��ǰ��ӵķ��ţ����ת��ΪСд�������
	
	//	�����û���ʾ
	private String msg="";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO �Զ����ɷ������
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
	
	private int _CurrentPage = -1;
	public int getCurrentPage() {
		return _CurrentPage;
	}
	public void setCurrentPage(int _value) {
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	public int getAllPages() {
		return _AllPages;
	}
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	
//	������
	private String briq;

	public String getBRiq() {
		if (briq==null && "".equals(briq)){
			briq = DateUtil.FormatDate(new Date());
		}
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	

//	ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	public boolean getRaw() {
		return true;
	}


    
//  ���ѡ������ڵ�Ķ�Ӧ�ĵ糧����   
    private String getMingc(String id){ 
		JDBCcon con=new JDBCcon();
		String mingc=null;
		String sql="select mingc from diancxxb where id = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=rsl.getString("mingc");
			
		}
		rsl.close();
		return mingc;
	}
    
//  �жϵ糧Tree����ѡ�糧ʱ�����ӵ糧   
    private boolean hasDianc(String id){ 
		JDBCcon con=new JDBCcon();
		boolean mingc= false;
		String sql="select mingc from diancxxb where fuid = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=true;
		}
		rsl.close();
		return mingc;
	}
    


    

	
//	ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("��ֹ����:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfb.setId("guohrqb");
		tb1.addField(dfb);
	
		tb1.addText(new ToolbarText("-"));

		

		
//		�糧Tree
		DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid_dc());
		setTree_dc(dt1);
		TextField tf1 = new TextField();
		tf1.setId("diancTree_text");
		tf1.setWidth(100);
		tf1.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid_dc() == null || "".equals(getTreeid_dc()) ? "-1"
						: getTreeid_dc())));

		ToolbarButton tb3 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb3.setIcon("ext/resources/images/list-items.gif");
		tb3.setCls("x-btn-icon");
		tb3.setMinWidth(20);
		
		tb1.addText(new ToolbarText("�糧:"));
		tb1.addField(tf1);
		tb1.addItem(tb3);
		

		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
//		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
//		tb1.addFill();
//		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		
		setToolbar(tb1);
	}
		
	
//	�õ���λȫ��
	public String getTianzdwQuanc(JDBCcon cn,long gongsxxbID) {
		String _TianzdwQuanc = "";

		try {
			ResultSet rs = cn
					.getResultSet(" select quanc from diancxxb where id="
							+ gongsxxbID);
			while (rs.next()) {
				_TianzdwQuanc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
		return _TianzdwQuanc;
	}
	
	private  String getTianzdwQuanc(JDBCcon cn){
		return getTianzdwQuanc(cn,Long.valueOf(this.getTreeid_dc()).longValue());
	}
	
	private String getYMD(String date,String biaoz){
		
		if(date==null) date=this.getBRiq();
		
		if(biaoz.equals("Y")){//�������
			
			return DateUtil.Formatdate("yyyy", DateUtil.getDate(date));
		}else if(biaoz.equals("M")){//�����·�
			return DateUtil.Formatdate("MM", DateUtil.getDate(date));
		}else {//��������
			return DateUtil.Formatdate("dd", DateUtil.getDate(date));
		}
	}
	
	private String getYMD(String biaoz){
		return getYMD(null,biaoz);
	}
	
	
	private String getBaseSql(JDBCcon cn,String maxRiq,String CurrODate,String EndDate,String diancxxb_id,String biaoz){
	
		
		String result="";
		
		String sql = 
			 "select "
			+ "t.diancxxb_id, \n"
			+ "t.dqid as gongysb_id,t.pinzb_id,t.jihkjb_id,t.yunsfsb_id from\n"
			+ "(select f.diancxxb_id,m.meikdq_id as DQID,f.pinzb_id,f.jihkjb_id,f.yunsfsb_id\n"
			+ " from fahb f, meikxxb m\n"
			+ "where f.daohrq >= "
			+ CurrODate
			+ "\n"
			+ "and f.daohrq < = "+EndDate+"\n"
			+ "and f.meikxxb_id = m.id and f.diancxxb_id = "
			+ diancxxb_id
			+ "\n"
			+ "union\n"
			+ "select f.diancxxb_id,m.meikdq_id,f.pinzb_id,f.jihkjb_id,f.yunsfsb_id\n"
			+ " from jiesb j, fahb f, meikxxb m\n"
			+ "where f.jiesb_id = j.id and f.meikxxb_id = m.id\n"
			+ "and j.ruzrq >="
			+ CurrODate
			+ "\n"
			+ "and j.ruzrq <="+EndDate+"\n"
			+ "and f.diancxxb_id = "
			+ diancxxb_id
			+ "\n"
			+ "union\n"
			+ "select y.diancxxb_id,y.gongysb_id,y.pinzb_id,y.jihkjb_id,y.yunsfsb_id\n"
			+ "from yuetjkjb y,yuehcb h where y.id = h.yuetjkjb_id and y.riq = add_months("
//			���ڿ���Ƿ����0�����������ʱ��Ϊ ��ȡ����0
			+ CurrODate
			+ ",-1)\n"
			+ "and y.diancxxb_id = "
			+ diancxxb_id
			+ "\n"
			+ "minus\n"
			+ "select y.diancxxb_id,y.gongysb_id,y.pinzb_id,y.jihkjb_id,y.yunsfsb_id\n"
			+ "from yuetjkjb y where y.riq ="
			+ CurrODate
			+ "\n"
			+ "and y.diancxxb_id = " + diancxxb_id + ") t \n";
		
		if( maxRiq!=null )
		sql+=" union select diancxxb_id,gongysb_id,pinzb_id,jihkjb_id,yunsfsb_id from  yuetjkjb where riq="+DateUtil.FormatOracleDate(maxRiq);
		
		
		ResultSetList rs=cn.getResultSetList(sql);
		
		
		boolean flag=false;
		
		while(rs.next()){
			
			flag=true;
			
			long gongysb_id = rs.getLong("gongysb_id");
			long jihkjb_id = rs.getLong("jihkjb_id");
			long pinzb_id = rs.getLong("pinzb_id");
			long yunsfsb_id = rs.getLong("yunsfsb_id");
			
			
			
			
			String type=MainGlobal.getXitxx_item("����", "���㵥������λ", String.valueOf(diancxxb_id), "ZGDT");
			String model = MainGlobal.getXitxx_item("�±�", "�±���λ", String
					.valueOf(diancxxb_id), "ZGDT");
			 if(type.equals("JZRD")||model.equals("DTGJ")){

				 sql = " select\n" + 
				 "a.dqid, a.jihkjb_id, a.pinzb_id, a.yunsfsb_id,sum(a.laimsl) laimsl,\n" + 
				 "sum(zhijbfml) zhijbfml,sum(zhijbfje) zhijbfje,decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.qnet_ar)/sum(a.laimsl),2)) as qnet_ar,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.aar)/sum(a.laimsl),2)) as aar,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.ad)/sum(a.laimsl),2)) as ad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.vdaf)/sum(a.laimsl),2)) as vdaf,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.mt)/sum(a.laimsl),1)) as mt,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.stad)/sum(a.laimsl),2)) as stad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.aad)/sum(a.laimsl),2)) as aad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.mad)/sum(a.laimsl),2)) as mad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.qbad)/sum(a.laimsl),2)) as qbad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.had)/sum(a.laimsl),2)) as had,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.vad)/sum(a.laimsl),2)) as vad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.fcad)/sum(a.laimsl),2)) as fcad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.std)/sum(a.laimsl),2)) as std,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.qgrad)/sum(a.laimsl),2)) as qgrad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.hdaf)/sum(a.laimsl),2)) as hdaf,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.qgrad_daf)/sum(a.laimsl),2)) as qgrad_daf,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.sdaf)/sum(a.laimsl),2)) as sdaf,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.var)/sum(a.laimsl),2)) as var,\n" + 
				 "\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kqnet_ar)/sum(a.laimsl),2)) as kqnet_ar,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kaar)/sum(a.laimsl),2)) as kaar,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kad)/sum(a.laimsl),2)) as kad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kvdaf)/sum(a.laimsl),2)) as kvdaf,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kmt)/sum(a.laimsl),1)) as kmt,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kstad)/sum(a.laimsl),2)) as kstad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kaad)/sum(a.laimsl),2)) as kaad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kmad)/sum(a.laimsl),2)) as kmad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kqbad)/sum(a.laimsl),2)) as kqbad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.khad)/sum(a.laimsl),2)) as khad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kvad)/sum(a.laimsl),2)) as kvad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kfcad)/sum(a.laimsl),2)) as kfcad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kstd)/sum(a.laimsl),2)) as kstd,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kqgrad)/sum(a.laimsl),2)) as kqgrad,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.khdaf)/sum(a.laimsl),2)) as khdaf,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kqgrad_daf)/sum(a.laimsl),2)) as kqgrad_daf,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.ksdaf)/sum(a.laimsl),2)) as ksdaf,\n" + 
				 "decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.kvar)/sum(a.laimsl),2)) as kvar\n" + 
				 "from (\n" + 
				 "select g.meikdq_id dqid, f.jihkjb_id, f.pinzb_id, f.yunsfsb_id,\n" + 
				 "  f.laimsl,z.*,k.qnet_ar kqnet_ar,0 kaar,0 kad,\n" + 
				 "  0 kvdaf, 0 kmt, 0 kstad, 0 kaad,\n" + 
				 "  0 kmad, 0 kqbad, 0 khad, 0 kvad,\n" + 
				 "  0 kfcad, k.std kstd, 0 kqgrad, 0 khdaf,\n" + 
				 "  0 kqgrad_daf, 0 ksdaf, 0 kvar,\n" + 
				 "getzhijbf(f.id,1) zhijbfml,\n" + 
				 "getzhijbf(f.id,2)*getzhijbf(f.id,1) zhijbfje\n" + 
				 "from fahb f, zhilb z, (\n" + 
				 "select hetb_id,sum(xiax)qnet_ar,sum(std)std\n" + 
				 "from(\n" + 
				 "select hetb_id,decode(danwb.bianm,'ǧ��ǧ��',round_new(xiax*0.0041816,3),xiax)xiax ,0 std\n" + 
				 "from hetzlb,danwb\n" + 
				 "where hetzlb.danwb_id=danwb.id and hetzlb.zhibb_id=2\n" + 
				 "union\n" + 
				 "select hetb_id,0 ,shangx\n" + 
				 "from hetzlb\n" + 
				 "where  hetzlb.zhibb_id=3)\n" + 
				 "group by hetb_id\n" + 
				 ") k, meikxxb g\n" + 
				 "where f.zhilb_id = z.id\n" + 
				 "and f.hetb_id = k.hetb_id(+) and f.meikxxb_id = g.id\n" + 
				 "and f.diancxxb_id = " + diancxxb_id + " and g.meikdq_id =" + gongysb_id +
					" and f.jihkjb_id = " + jihkjb_id + " and f.pinzb_id =" + pinzb_id +
					" and f.daohrq >= " +CurrODate +
					" and f.daohrq <= "+EndDate+"\n" +
					" and f.yunsfsb_id =" + yunsfsb_id +
					") a\n" + 
					"group by a.dqid, a.jihkjb_id, a.pinzb_id, a.yunsfsb_id\n" + 
					"";

			 }else{
				 sql = 
						"select\n" +
						"a.dqid, a.jihkjb_id, a.pinzb_id, a.yunsfsb_id,sum(a.laimsl) laimsl,\n" + 
						"sum(zhijbfml) zhijbfml,sum(zhijbfje) zhijbfje," + 
						
						
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.qnet_ar)/sum(a.laimsl),2)) as qnet_ar,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.aar)/sum(a.laimsl),2)) as aar,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.ad)/sum(a.laimsl),2)) as ad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.vdaf)/sum(a.laimsl),2)) as vdaf,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.mt)/sum(a.laimsl),1)) as mt,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.stad)/sum(a.laimsl),2)) as stad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.aad)/sum(a.laimsl),2)) as aad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.mad)/sum(a.laimsl),2)) as mad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.qbad)/sum(a.laimsl),2)) as qbad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.had)/sum(a.laimsl),2)) as had,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.vad)/sum(a.laimsl),2)) as vad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.fcad)/sum(a.laimsl),2)) as fcad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.std)/sum(a.laimsl),2)) as std,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.qgrad)/sum(a.laimsl),2)) as qgrad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.hdaf)/sum(a.laimsl),2)) as hdaf,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.qgrad_daf)/sum(a.laimsl),2)) as qgrad_daf,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.sdaf)/sum(a.laimsl),2)) as sdaf,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*a.var)/sum(a.laimsl),2)) as var,\n" + 
						"\n" + 

						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kqnet_ar,null,a.qnet_ar,a.kqnet_ar))/sum(a.laimsl),2)) as kqnet_ar,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kaar,null,a.aar,a.kaar))/sum(a.laimsl),2)) as kaar,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kad,null,a.ad,a.kad))/sum(a.laimsl),2)) as kad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kvdaf,null,a.vdaf,a.kvdaf))/sum(a.laimsl),2)) as kvdaf,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kmt,null,a.mt,a.kmt))/sum(a.laimsl),1)) as kmt,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kstad,null,a.stad,a.kstad))/sum(a.laimsl),2)) as kstad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kaad,null,a.aad,a.kaad))/sum(a.laimsl),2)) as kaad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kmad,null,a.mad,a.kmad))/sum(a.laimsl),2)) as kmad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kqbad,null,a.qbad,a.kqbad))/sum(a.laimsl),2)) as kqbad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.khad,null,a.had,a.khad))/sum(a.laimsl),2)) as khad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kvad,null,a.vad,a.kvad))/sum(a.laimsl),2)) as kvad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kfcad,null,a.fcad,a.kfcad))/sum(a.laimsl),2)) as kfcad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kstd,null,a.std,a.kstd))/sum(a.laimsl),2)) as kstd,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kqgrad,null,a.qgrad,a.kqgrad))/sum(a.laimsl),2)) as kqgrad,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.khdaf,null,a.hdaf,a.khdaf))/sum(a.laimsl),2)) as khdaf,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kqgrad_daf,null,a.qgrad_daf,a.kqgrad_daf))/sum(a.laimsl),2)) as kqgrad_daf,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.ksdaf,null,a.sdaf,a.ksdaf))/sum(a.laimsl),2)) as ksdaf,\n" + 
						"decode(sum(a.laimsl),0,0,round(sum(a.laimsl*decode(a.kvar,null,a.var,a.kvar))/sum(a.laimsl),2)) as kvar\n" + 
//						end
						"from (select g.meikdq_id dqid, f.jihkjb_id, f.pinzb_id, f.yunsfsb_id,\n" + 
						"  f.laimsl,z.*,k.qnet_ar kqnet_ar, k.aar kaar, k.ad kad,\n" + 
						"  k.vdaf kvdaf, k.mt kmt, k.stad kstad, k.aad kaad,\n" + 
						"  k.mad kmad, k.qbad kqbad, k.had khad, k.vad kvad,\n" + 
						"  k.fcad kfcad, k.std kstd, k.qgrad kqgrad, k.hdaf khdaf,\n" + 
						"  k.qgrad_daf kqgrad_daf, k.sdaf ksdaf, k.var kvar,\n" + 
						"  nvl(case when (r.relzj + r.liuzj + r.huifzj + r.shuifzj+r.huiffzj+r.huirdzj<0)\n" + 
						"then f.biaoz end,0 ) zhijbfml,\n" + 
						"  abs(case when r.relzj + r.liuzj + r.huifzj + r.shuifzj+r.huiffzj+r.huirdzj<0\n" + 
						"then (r.relzj + r.liuzj + r.huifzj + r.shuifzj)*f.biaoz\n" + 
						"end) zhijbfje\n" + 
					
						
						"from fahb f, zhilb z, ruccb r, kuangfzlb k, meikxxb g\n" + 
						"where f.zhilb_id = z.id and f.ruccbb_id = r.id\n" + 
						"and f.kuangfzlb_id = k.id(+) and f.meikxxb_id = g.id\n" + 
						"and f.diancxxb_id = " + diancxxb_id + " and g.meikdq_id =" + gongysb_id +
						" and f.jihkjb_id = " + jihkjb_id + " and f.pinzb_id =" + pinzb_id +
						" and f.daohrq >= " +CurrODate + "\n" +
						" and f.daohrq <= "+EndDate+" \n" +
						" and f.yunsfsb_id =" + yunsfsb_id +
						") a\n" + 
						"group by a.dqid, a.jihkjb_id, a.pinzb_id, a.yunsfsb_id\n" + 
						"";
			 }
			ResultSetList datars = cn.getResultSetList(sql);
			
			double mt=0.0;double ad=0.0;double aad=0.0;double vdaf=0.0;
			double had=0.0;double qnet_ar=0.0;double std=0.0;
			
			double laimslby=0.0; double laimsllj=0.0;
			if(datars.next()){
				
				mt = datars.getDouble("mt"); 
				ad = datars.getDouble("ad");
				aad = datars.getDouble("aad"); 
				vdaf = datars.getDouble("vdaf");
				had = datars.getDouble("had"); 
				qnet_ar = datars.getDouble("qnet_ar");
				std = datars.getDouble("std"); 
				
			}
			
			
			sql =
				"select nvl(sum(relzjje + quanszjje + huifzjje + huiffzjje),0) zhejje,\n" +
				"round(nvl(sum(liuspsl),0),0) liuspsl,nvl(sum(liuspje),0) liuspje\n" + 
				"from (select j.id,\n" + 
				"nvl(sum(case when z.bianm = 'Qnetar' and s.zhejje <0 then abs(s.zhejje) end),0) relzjje,\n" + 
				"nvl(sum(case when z.bianm = 'Mt' and s.zhejje <0 then abs(s.zhejje) end),0) quanszjje,\n" + 
				"nvl(sum(case when z.bianm = 'Aar' and s.zhejje <0 then abs(s.zhejje) end),0) huifzjje,\n" + 
				"nvl(sum(case when z.bianm = 'Vdaf' and s.zhejje <0 then abs(s.zhejje) end),0) huiffzjje,\n" + 
				"nvl(sum(case when z.bianm = 'Std' and s.zhejje <0 then abs(j.jiessl) end),0) liuspsl,\n" + 
				"nvl(sum(case when z.bianm = 'Std' and s.zhejje <0 then abs(s.zhejje) end),0) liuspje\n" + 
				"from jiesb j, jieszbsjb s, zhibb z\n" + 
				"where j.id = s.jiesdid and s.zhibb_id = z.id\n" + 
				"and j.ruzrq >= " +CurrODate + "\n" + 
				"and j.ruzrq <="+EndDate+"\n" + 
				"group by j.id) js, (select distinct f.jiesb_id from fahb f, meikxxb g\n" + 
				"where f.meikxxb_id = g.id\n" + 
				" and f.diancxxb_id =" + diancxxb_id +
				" and g.meikdq_id = " + gongysb_id +
				" and f.jihkjb_id = " + jihkjb_id +
				" and f.yunsfsb_id = " + yunsfsb_id +
				" and f.pinzb_id = " + pinzb_id +
				" ) fh where fh.jiesb_id = js.id";
			datars = cn.getResultSetList(sql);
			
			double suopje=0.0;double liuspsl=0.0;double liuspje=0.0;
			if(datars.next()){
				suopje = datars.getDouble("zhejje");
				liuspsl = datars.getDouble("liuspsl");
				liuspje = datars.getDouble("liuspje");
			}
			datars.close();
			
			double jincml=0.0;
			
			jincml=this.getJingcsl(cn, gongysb_id, jihkjb_id, pinzb_id, yunsfsb_id, CurrODate, EndDate,"����");	
			
			if(biaoz==null || biaoz.equals("����"))
			result+="\n select "+gongysb_id+" gongysb_id,"+jihkjb_id+" jihkjb_id,"+
				  pinzb_id+" pinzb_id,"+yunsfsb_id+" yunsfsb_id,"+diancxxb_id+" diancxxb_id,nvl('����','') fx,"+jincml+" jincml," +
				  		mt+" mt," +ad+" ad,"+aad+" aad,"+vdaf+" vdaf,"+had+" had,"+qnet_ar+" qnet_ar,"+std+" std,"+suopje+" suopje"+
				  		" from dual union";
			
			
	
		String LastODate="add_months("+CurrODate+",-1) ";
			
		sql = "select s.laimsl,zl.* from yuetjkjb tj,yuezlb zl,yueslb s  " +
			"where s.yuetjkjb_id=tj.id and s.fenx = zl.fenx " +
			" and zl.yuetjkjb_id=tj.id and tj.riq="
		+ LastODate
		+ " and tj.diancxxb_id="
		+ diancxxb_id
		+ "\n"
		+ " and tj.gongysb_id="
		+ gongysb_id
		+ " and tj.pinzb_id="
		+ pinzb_id
		+ " and tj.jihkjb_id="
		+ jihkjb_id
		+ " and tj.yunsfsb_id="
		+ yunsfsb_id
		+ " and zl.fenx='�ۼ�'";
	datars = cn.getResultSetList(sql);
	if (datars == null) {
		WriteLog.writeErrorLog(this.getClass().getName() + "\n"
				+ ErrorMessage.NullResult + "\n��������SQL:" + sql);
		setMsg(ErrorMessage.NullResult);
		cn.rollBack();
		cn.Close();
		return "";
	}
	
	
	if(datars.next()){
		
		
		laimsllj = datars.getDouble("laimsl");
		
		if(laimslby + laimsllj != 0){
		qnet_ar = CustomMaths.Round_new(CustomMaths.div(qnet_ar*laimslby 
				+ datars.getDouble("qnet_ar")*laimsllj,laimslby+laimsllj), 2); 
		ad = CustomMaths.Round_new(CustomMaths.div(ad*laimslby 
				+ datars.getDouble("ad")*laimsllj,laimslby+laimsllj), 2); 
		vdaf = CustomMaths.Round_new(CustomMaths.div(vdaf*laimslby 
				+ datars.getDouble("vdaf")*laimsllj,laimslby+laimsllj), 2); 
		mt = CustomMaths.Round_new(CustomMaths.div(mt*laimslby 
				+ datars.getDouble("mt")*laimsllj,laimslby+laimsllj), 1); 
		aad = CustomMaths.Round_new(CustomMaths.div(aad*laimslby 
				+ datars.getDouble("aad")*laimsllj,laimslby+laimsllj), 2); 
		had = CustomMaths.Round_new(CustomMaths.div(had*laimslby 
				+ datars.getDouble("had")*laimsllj,laimslby+laimsllj), 2); 
		std = CustomMaths.Round_new(CustomMaths.div(std*laimslby 
				+ datars.getDouble("std")*laimsllj,laimslby+laimsllj), 2);
		
		suopje = suopje + datars.getDouble("suopje");//suppje
		liuspsl = liuspsl + datars.getDouble("lsuopsl");
		liuspje = liuspje + datars.getDouble("lsuopje");
		}
		
	}
	
	jincml=this.getJingcsl(cn, gongysb_id, jihkjb_id, pinzb_id, yunsfsb_id, CurrODate, EndDate,"�ۼ�");	
	
	if(biaoz==null || biaoz.equals("�ۼ�"))
	result+="\n  select "+gongysb_id+" gongysb_id,"+jihkjb_id+" jihkjb_id,"+
	  pinzb_id+" pinzb_id,"+yunsfsb_id+" yunsfsb_id,"+diancxxb_id+" diancxxb_id,nvl('�ۼ�','') fx,"+jincml+" jincml," +
	  		mt+" mt," +ad+" ad,"+aad+" aad,"+vdaf+" vdaf,"+had+" had,"+qnet_ar+" qnet_ar,"+std+" std,"+suopje+" suopje"+
	  		" from dual union";
	
			
	}//while
		
		
		if(flag){//˵��  ����û���¼ƻ�ͳ�ƿھ�����
//			
			//����ֵΪ0
			
//			double mt=0.0;double ad=0.0;double aad=0.0;double vdaf=0.0;
//			double had=0.0;double qnet_ar=0.0;double std=0.0;
			
//			if(biaoz==null || biaoz.equals("����")){
//				result+="\n select "+0+" gongysb_id,"+0+" jihkjb_id,"+0+" jihkjb_id," +
//				  0+" pinzb_id,"+0+" yunsfsb_id,"+diancxxb_id+" diancxxb_id,'����' fenx,0 jincml," +
//				  		mt+" mt," +ad+" ad,"+aad+" aad,"+vdaf+" vdaf,"+had+" had,"+qnet_ar+" qnet_ar,"+std+" std"+
//				  		" from dual union";
//			}
//			
//			
//			if(biaoz==null || biaoz.equals("�ۼ�")){ 
//				
//			}
		}
		
		if(result==null || result.equals("")) return "";
		
		return result;
		
	}
	public String getPrintTable(){

		JDBCcon cn = new JDBCcon();
		String _Danwqc = getTianzdwQuanc(cn);
		
		
		String CurrODate=DateUtil.FormatOracleDate(this.getYMD("Y")+"-"+this.getYMD("M")+"-01");
		String EndDate=DateUtil.FormatOracleDate(this.getBRiq());
		String diancxxb_id=this.getTreeid_dc();
		
		
		//���µ����һ��
		String lastDay_Month=DateUtil.Formatdate("dd", DateUtil.getLastDayOfMonth(DateUtil.getDate(this.getBRiq())) );
		
		//���ж��������� �� �� ���������� �Ƿ��Ѿ�����������
	
		
	    ResultSetList rsl=cn.getResultSetList(" " +
			          " select max(riq) riq, max(riq) - "+DateUtil.FormatOracleDate(this.getBRiq())+" cz from yuetjkjb  \n" +
			          "");
		      
	    String sql_temp="";
	    if(rsl.next()){
			        
	    	String riq_max=DateUtil.FormatDate(rsl.getDate("riq"));
	    	
	    	if(this.getYMD(riq_max, "Y").equals(this.getYMD("Y"))){
	    		
	    		if(this.getYMD(riq_max, "M").equals(this.getYMD("M"))){
	    			
	    			if(this.getYMD("D").equals(DateUtil.getLastDayOfMonth(this.getBRiq()))){
	    				//�����Ѿ���ȫ����  ֻ��Ҫ���Ҽ���
	    				sql_temp+=this.getBaseSql(CurrODate, diancxxb_id);
	    			}else{
	    				
	    				if(DateUtil.getYear(DateUtil.AddDate(this.getBRiq()+" 00:00:00",-1,DateUtil.AddType_intMonth))==Integer.parseInt(this.getYMD("Y"))){
	    					sql_temp+=this.getBaseSql(cn, DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(this.getBRiq()+" 00:00:00",-1,DateUtil.AddType_intMonth))),CurrODate, EndDate, diancxxb_id,null);
	    				}else{
	    					sql_temp+=this.getBaseSql(cn, null,CurrODate, EndDate, diancxxb_id,null);
	    				}
	    				
	    			}
	    		}else{
	    			
	    			int rq_mx=Integer.parseInt(this.getYMD(riq_max, "M"));
	    			int rq_cu=Integer.parseInt(this.getYMD(this.getBRiq(), "M"));
	    			
	    			if(rq_mx>=rq_cu){//�����Ѿ�����
	    				
	    				if(DateUtil.getYear(DateUtil.AddDate(this.getBRiq()+" 00:00:00",-1,DateUtil.AddType_intMonth))==Integer.parseInt(this.getYMD("Y"))){
	    					sql_temp+=this.getBaseSql(cn, DateUtil.FormatDate(DateUtil.getFirstDayOfMonth(DateUtil.AddDate(this.getBRiq()+" 00:00:00",-1,DateUtil.AddType_intMonth))),CurrODate, EndDate, diancxxb_id,null);
	    				}else{
	    					sql_temp+=this.getBaseSql(cn, null,CurrODate, EndDate, diancxxb_id,null);
	    				}
	    				
	    			}else{
	    				
	    				if(rq_mx+1>=rq_cu){//���һ����
	    					
	    					if(DateUtil.getYear(DateUtil.AddDate(this.getBRiq()+" 00:00:00",-1,DateUtil.AddType_intMonth))==Integer.parseInt(this.getYMD("Y"))){
	    						sql_temp+=this.getBaseSql(cn,riq_max, DateUtil.FormatOracleDate(DateUtil.getFirstDayOfMonth(DateUtil.getDate(this.getBRiq()))), EndDate, diancxxb_id,"����");
		    					sql_temp+=this.getBaseSql(cn,riq_max, DateUtil.FormatOracleDate(DateUtil.AddDate(riq_max+" 00:00:00", 1, DateUtil.AddType_intMonth)), EndDate, diancxxb_id,"�ۼ�");
	    					}else{
	    						sql_temp+=this.getBaseSql(cn,null, DateUtil.FormatOracleDate(DateUtil.getFirstDayOfMonth(DateUtil.getDate(this.getBRiq()))), EndDate, diancxxb_id,"����");
		    					sql_temp+=this.getBaseSql(cn,null, DateUtil.FormatOracleDate(DateUtil.AddDate(riq_max+" 00:00:00", 1, DateUtil.AddType_intMonth)), EndDate, diancxxb_id,"�ۼ�");
	    					}
	    				
	    				}else{
	    					
	    					if(DateUtil.getYear(DateUtil.AddDate(this.getBRiq()+" 00:00:00",-1,DateUtil.AddType_intMonth))==Integer.parseInt(this.getYMD("Y"))){
	    						sql_temp+=this.getBaseSql(cn,riq_max, DateUtil.FormatOracleDate(DateUtil.getFirstDayOfMonth(DateUtil.getDate(this.getBRiq()))), EndDate, diancxxb_id,"����");
		    					sql_temp+=this.getBaseSql(cn,riq_max, DateUtil.FormatOracleDate(DateUtil.AddDate(riq_max+" 00:00:00", 1, DateUtil.AddType_intMonth)), EndDate, diancxxb_id,"�ۼ�");
	    					}else{
	    						sql_temp+=this.getBaseSql(cn,null, DateUtil.FormatOracleDate(DateUtil.getFirstDayOfMonth(DateUtil.getDate(this.getBRiq()))), EndDate, diancxxb_id,"����");
		    					sql_temp+=this.getBaseSql(cn,null, DateUtil.FormatOracleDate(DateUtil.AddDate(riq_max+" 00:00:00", 1, DateUtil.AddType_intMonth)), EndDate, diancxxb_id,"�ۼ�");
	    					}
	    					
	    				}
	    				
	    			}
	    			
	    			
	    		}
	    	}else{
	    		
	    		
	    		if(DateUtil.getYear(DateUtil.AddDate(this.getBRiq()+" 00:00:00",-1,DateUtil.AddType_intMonth))==Integer.parseInt(this.getYMD("Y"))){
	    			sql_temp+=this.getBaseSql(cn,riq_max, DateUtil.FormatOracleDate(DateUtil.getFirstDayOfMonth(DateUtil.getDate(this.getBRiq()))), EndDate, diancxxb_id,"����");
					sql_temp+=this.getBaseSql(cn,riq_max,DateUtil.FormatOracleDate(DateUtil.AddDate(riq_max+" 00:00:00", 1, DateUtil.AddType_intMonth)), EndDate, diancxxb_id,"�ۼ�");
	    		}else{
	    			sql_temp+=this.getBaseSql(cn,null, DateUtil.FormatOracleDate(DateUtil.getFirstDayOfMonth(DateUtil.getDate(this.getBRiq()))), EndDate, diancxxb_id,"����");
					sql_temp+=this.getBaseSql(cn,null,DateUtil.FormatOracleDate(DateUtil.AddDate(riq_max+" 00:00:00", 1, DateUtil.AddType_intMonth)), EndDate, diancxxb_id,"�ۼ�");
	    		}
	    		
	    	}
	    	
		 }
		
	
		sql_temp= sql_temp.substring(0, sql_temp.lastIndexOf("u"));
		
		
	    String sql=
			"SELECT DECODE(GROUPING(JIHKJB.MINGC),\n" +
			"              1,\n" + 
			"              '�ܼ�',\n" + 
			"              DECODE(GROUPING(NVL(GONGYSB.MEITLY,'�ط���')),\n" + 
			"                     1,\n" + 
			"                     '*' || JIHKJB.MINGC || '�ϼ�',\n" + 
			"                     DECODE(GROUPING(SHENGFB.QUANC),\n" + 
			"                            1,\n" + 
			"                            '#' || NVL(GONGYSB.MEITLY,'�ط���') || 'С��',\n" + 
			"                            DECODE(GROUPING(GONGYSB.MINGC),\n" + 
			"                                   1,\n" + 
			"                                   SHENGFB.QUANC,\n" + 
			"                                   GONGYSB.MINGC\n" +
			"                            )\n" +
			"                       )\n" +
			"               )\n" +
			"       ) AS KUANGB,\n" + 
			"     nvl(FX,'') FX,\n" +
			"     SUM(JINCML) AS JINML,\n" + 
			"     SUM(JINCML) AS YANSSL,\n" + 
			"     DECODE(SUM(JINCML),0,0,ROUND_NEW(SUM(JINCML) / SUM(JINCML) * 100, 2)) AS JIANZL,\n" + 
//			"     DECODE(SUM(JINCML),0,0,ROUND(SUM(JINCML * MT_KF) / SUM(JINCML), 1)) AS MT_KF,\n" + 
//			"     DECODE(SUM(JINCML),0,0,ROUND(SUM(JINCML * AAR_KF) / SUM(JINCML), 2)) AS AAR_KF,\n" + 
//			"     DECODE(SUM(JINCML),0,0,ROUND(SUM(JINCML * VDAF_KF) / SUM(JINCML), 2)) AS VDAF_KF,\n" + 
//			"     DECODE(SUM(JINCML),0,0,ROUND(SUM(JINCML * QNET_AR_KF) / SUM(JINCML), 2)) AS QNET_AR_KF,\n" + 
//			"     DECODE(SUM(JINCML),0,0,ROUND(SUM(JINCML * STD_KF) / SUM(JINCML), 2)) AS ST_D_KF,\n" + 
			"     DECODE(SUM(JINCML),0,0,ROUND(SUM(JINCML * MT) / SUM(JINCML), 1)) AS MAR,\n" + 
			"     DECODE(SUM(JINCML),0,0,ROUND(SUM(JINCML * AAD) / SUM(JINCML), 2)) AS AAD,\n" + 
			"     DECODE(SUM(JINCML),0,0,ROUND(SUM(JINCML * AD) / SUM(JINCML), 2)) AS AD,\n" + 
			"     DECODE(SUM(JINCML),0,0,ROUND(SUM(JINCML * VDAF) / SUM(JINCML), 2)) AS VDAF,\n" + 
			"     DECODE(SUM(JINCML),0,0,ROUND(SUM(JINCML * QNET_AR) / SUM(JINCML), 2)) AS QNET_AR,\n" + 
			"     DECODE(SUM(JINCML), 0, 0, ROUND(SUM(JINCML * STD) / SUM(JINCML), 2)) AS ST_D \n" + 
//			"     SUM(NVL(ZHIJBFJE, 0)) AS JZ_HEJ,\n" + 
//			"     SUM(NVL(ZHIJBFJE_M, 0)) AS JZ_SHUIF,\n" + 
//			"     SUM(NVL(ZHIJBFJE_A, 0)) AS JZ_HUIF,\n" + 
//			"     SUM(NVL(ZHIJBFJE_V, 0)) AS JZ_HUIFF,\n" + 
//			"     SUM(NVL(ZHIJBFJE_Q, 0)) AS JZ_FARL,\n" + 
//			"     SUM(NVL(ZHIJBFJE_S, 0)) AS JZ_LIUF,\n" + 
//			"     SUM(NVL(ZHIJBFJE_T, 0)) AS JZ_HUIRD,\n" + 
//			"     SUM(NVL(SUOPJE, 0)) AS SUOPJE \n" + 
//			"     0 AS SUOPL\n" + 
			"FROM (" + sql_temp + ")Z,GONGYSB,JIHKJB,PINZB,SHENGFB\n" +
			"WHERE Z.GONGYSB_ID = GONGYSB.ID\n" +
			"   AND Z.JIHKJB_ID = JIHKJB.ID\n" + 
			"   AND Z.PINZB_ID = PINZB.ID\n" + 
			"   AND GONGYSB.SHENGFB_ID = SHENGFB.ID\n" + 
			"GROUP BY ROLLUP(FX,JIHKJB.MINGC,NVL(GONGYSB.MEITLY,'�ط���'),SHENGFB.QUANC,GONGYSB.MINGC)\n" + 
			"HAVING NOT GROUPING(FX) = 1\n" + 
			"ORDER BY JIHKJB.MINGC DESC,NVL(GONGYSB.MEITLY,'�ط���') DESC,SHENGFB.QUANC DESC, GONGYSB.MINGC DESC,FX";
	    
	    
	    ResultSet rs=cn.getResultSet(sql);
		Report rt=new Report();
		 
		//�����ͷ����

		 String ArrHeader[][]=new String[1][11];
		 ArrHeader[0]=new String[] {"���","����","����ú��","��������","������",
				 "Mar<br>(%)","Aad<br>(%)","Ad<br>(%)","Vdaf<br>(%)","Qnet,ar<br>(%)","St,d<br>(%)"};
		 
		 int ArrWidth[]=new int[] {120,59,59,59,59,40,40,40,40,40,40};

		rt.setTitle("�������ۼƲ�ѯ", ArrWidth);

//		rt.setMarginBottom(rt.getMarginBottom()+25);
		
//		����
		rt.setBody(new Table(rs,1,0,3));
		rt.body.setWidth(ArrWidth);

		rt.body.setPageRows(24);
		rt.body.setHeaderData(ArrHeader);//��ͷ����
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.getPages();
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		
		
		for (int i = 6; i <= 11; i++) {
			rt.body.setColFormat(i, "0.00");
		}
		
		
		rt.body.ShowZero=reportShowZero();
		
//		����������
		convertItem(rt.body);
		
		//ҳ��
		rt.createDefautlFooter(ArrWidth);
//		rt.setDefautlFooter(10, 2, "�ֹ��쵼:", Table.ALIGN_CENTER);
//		rt.setDefautlFooter(16, 2, "�Ʊ�:", Table.ALIGN_LEFT);
//		rt.setDefautlFooter(20, 2, "���:", Table.ALIGN_LEFT);
//		rt.setDefautlFooter(rt.footer.getCols() - 1, 2, Table.PAGENUMBER_CHINA,
//				Table.ALIGN_RIGHT);
	
		_CurrentPage=1;
		_AllPages=rt.getPages();
		
		if (_AllPages==0){
			_CurrentPage=0;
		}
		cn.Close();

		return rt.getAllPagesHtml();
		
	
	}
	
	private String getBaseSql(String strDate, String diancxxb_id) {
		return
		"SELECT KJ.GONGYSB_ID,\n" +
		"             KJ.JIHKJB_ID,\n" + 
		"             KJ.PINZB_ID,\n" + 
		"             KJ.YUNSFSB_ID,\n" + 
		"             DIANCXXB_ID,\n" + 
		"             KJ.FENX AS FX,\n" + 
//		"             SL.biaoz AS jincml,\n" + 
		"             SL.jingz AS jincml,\n" + 
		"             ZL.*\n" + 
		"        FROM YUESLB SL,\n" + 
		"             YUEZLB ZL,\n" + 
		"             (SELECT ID,\n" + 
		"                     GONGYSB_ID,\n" + 
		"                     JIHKJB_ID,\n" + 
		"                     PINZB_ID,\n" + 
		"                     YUNSFSB_ID,\n" + 
		"                     DIANCXXB_ID,\n" + 
		"                     NVL('����', '') AS FENX\n" + 
		"                FROM YUETJKJB\n" + 
		"               WHERE RIQ = TO_DATE('" + strDate + "', 'yyyy-mm-dd')\n" + 
		"                 AND DIANCXXB_ID IN (" + diancxxb_id + ")\n" + 
		"              UNION\n" + 
		"              SELECT ID,\n" + 
		"                     GONGYSB_ID,\n" + 
		"                     JIHKJB_ID,\n" + 
		"                     PINZB_ID,\n" + 
		"                     YUNSFSB_ID,\n" + 
		"                     DIANCXXB_ID,\n" + 
		"                     NVL('�ۼ�', '') AS FENX\n" + 
		"                FROM YUETJKJB\n" + 
		"               WHERE RIQ = TO_DATE('" + strDate + "', 'yyyy-mm-dd')\n" + 
		"                 AND DIANCXXB_ID IN (" + diancxxb_id + ")) KJ\n" + 
		"       WHERE KJ.ID = SL.YUETJKJB_ID(+)\n" + 
		"         AND KJ.FENX = SL.FENX(+)\n" + 
		"         AND KJ.ID = ZL.YUETJKJB_ID(+)\n" + 
		"         AND KJ.FENX = ZL.FENX(+)";
		
	}
	
	
	private double getJingcsl(JDBCcon con,long gongysb_id,long jihkjb_id,long pinzb_id,long yunsfsb_id,String CurrODate,String EndDate,String biaoz){
		
		long scale_in = 0;
		long scale_out = 0;
		String sfbiaoz="��";
		String diancxxb_id = this.getTreeid_dc();

	
		String sql = "select zhi from xitxxb where leib = '�±�' and mingc ='�±���ԼС��λ�ڲ�' and zhuangt = 1 and diancxxb_id="
				+ diancxxb_id;
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			scale_in = rsl.getLong("zhi");
		}
		rsl.close();
		
		
		sql = "select zhi from xitxxb where leib = '�±�' and mingc ='�±���ԼС��λ�ⲿ' and zhuangt = 1 and diancxxb_id="
				+ diancxxb_id;
		rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			scale_out = rsl.getLong("zhi");
		}
		rsl.close();
	    sql = "select zhi from xitxxb where leib = '�±�' and mingc ='�±����������Ƿ�ʹ��Ʊ��' and zhuangt = 1 and diancxxb_id="
			+ diancxxb_id;
	    rsl = con.getResultSetList(sql);
	    if(rsl.next()){
	    	sfbiaoz=rsl.getString("zhi");
	    }
		rsl.close();
		String date_c = MainGlobal.getXitxx_item("�±�", "�±�ȡ�����ڲ�", diancxxb_id,
				"0");
		int flag;
		long lngId = 0;
		
		
		
		
		String type = MainGlobal.getXitxx_item("����", "���㵥������λ", String
				.valueOf(diancxxb_id), "ZGDT");
		String model = MainGlobal.getXitxx_item("�±�", "�±���λ", String
				.valueOf(diancxxb_id), "ZGDT");
		if (type.equals("JZRD")||model.equals("DTGJ")) {
			sql = "select\n"
					+ "nvl(max(g.xuh), 0) as xuh,\n"
					+ "sum(round(f.laimsl,0)) as jingz,\n"
					+ "sum(round(f.biaoz,0)) as biaoz,\n"
					+ "sum(round(f.yingd,0)) as yingd,\n"
					+ "sum(round(f.biaoz,0)-round(f.laimsl,0)+round(f.yingd,0)-round(f.yuns,0)) as kuid,\n"
					+ "sum(round(f.yuns,0)) as yuns,\n"
					+ "sum(round(f.koud,0)) as koud,\n"
					+ "sum(round(f.kous,0)) as kous,\n"
					+ "sum(round(f.kouz,0)) as kouz,\n"
					+ "sum(round(f.koum,0)) as koum,\n"
					+ "sum(round(f.zongkd,0)) as zongkd,\n"
					+ "sum(round(f.sanfsl,0)) as sanfsl,\n"
					+ "sum(round(c.guohsl,0)) as jianjsl,\n"
					+ "sum(round(f.laimsl,0)) as laimsl,\n"
					+ "round(sum(zonghj*f.jingz)/sum(f.jingz),2)*sum(round(f.yingd,0))  yingdsp,\n"
					+ "round(sum(zonghj*f.jingz)/sum(f.jingz),2)*(sum(round(f.biaoz,0)-round(f.laimsl,0)+round(f.yingd,0)-round(f.yuns,0)))  kuidsp\n"
					+ "from "
					+ "(select g.fahb_id ,(g.meij+g.meis+g.yunf+g.yunfs+g.zaf+g.fazzf+g.ditf)zonghj\n"
					+ "from guslsb g\n" + "where id in(\n"
					+ "select max(guslsb.id)\n" + "from guslsb\n"
					+ "group by guslsb.fahb_id\n" + "))"
					+ " r, fahb f, meikxxb m, gongysb g,\n" + "\n"
					+ "(select fahb_id, sum(maoz - piz) as guohsl\n"
					+ "from chepb\n"
					+ "where jianjfs = '����' group by fahb_id ) c\n"
					+ "where f.meikxxb_id = m.id\n" + " and m.meikdq_id = g.id and m.meikdq_id = "
					+ gongysb_id + " and f.jihkjb_id =" + jihkjb_id
					+ "and f.pinzb_id = " + pinzb_id
					+ " and f.yunsfsb_id =" + yunsfsb_id
					+ " and f.daohrq >= " + CurrODate
					+ "and f.daohrq <="+EndDate+"\n"
					+ "and f.diancxxb_id = " + diancxxb_id + "\n"
					+ "and f.id = r.fahb_id(+) and f.id = c.fahb_id(+)";// ����������
		} else if(model.equals("Basic")){
			String RName = "round"; // ��Լ��������
			sql = "select\n"
				+ "nvl(max(g.xuh), 0) as xuh,\n"
				+ ""
				+ RName
				+ "(sum("
				+ RName
				+ "(f.jingz,"
				+ scale_in
				+ ")),"
				+ scale_out
				+ ") as jingz,\n"
				+ ""
				+ RName
				+ "(sum("
				+ RName
				+ "(f.biaoz,"
				+ scale_in
				+ ")),"
				+ scale_out
				+ ") as biaoz,\n"
				+ ""
				+ RName
				+ "(sum("
				+ RName
				+ "(f.yingd,"
				+ scale_in
				+ ")),"
				+ scale_out
				+ ") as yingd,\n"
				+ ""
				+ RName
				+ "(sum("
				+ RName
				+ "(f.yingd - f.yingk,"
				+ scale_in
				+ ")),"
				+ scale_out
				+ ") as kuid,\n"
				+ ""
				+ RName
				+ "(sum("
				+ RName
				+ "(f.yuns,"
				+ scale_in
				+ ")),"
				+ scale_out
				+ ") as yuns,\n"
				+ ""
				+ RName
				+ "(sum("
				+ RName
				+ "(f.koud,"
				+ scale_in
				+ ")),"
				+ scale_out
				+ ") as koud,\n"
				+ ""
				+ RName
				+ "(sum("
				+ RName
				+ "(f.kous,"
				+ scale_in
				+ ")),"
				+ scale_out
				+ ") as kous,\n"
				+ ""
				+ RName
				+ "(sum("
				+ RName
				+ "(f.kouz,"
				+ scale_in
				+ ")),"
				+ scale_out
				+ ") as kouz,\n"
				+ ""
				+ RName
				+ "(sum("
				+ RName
				+ "(f.koum,"
				+ scale_in
				+ ")),"
				+ scale_out
				+ ") as koum,\n"
				+ ""
				+ RName
				+ "(sum("
				+ RName
				+ "(f.zongkd,"
				+ scale_in
				+ ")),"
				+ scale_out
				+ ") as zongkd,\n"
				+ ""
				+ RName
				+ "(sum("
				+ RName
				+ "(f.sanfsl,"
				+ scale_in
				+ ")),"
				+ scale_out
				+ ") as sanfsl,\n"
				+ ""
				+ RName
				+ "(sum("
				+ RName
				+ "(c.guohsl,"
				+ scale_in
				+ ")),"
				+ scale_out
				+ ") as jianjsl,\n"
				+ ""
				+ RName
				+ "(sum("
				+ RName
				+ "(f.laimsl,"
				+ scale_in
				+ ")),"
				+ scale_out
				+ ") as laimsl,\n"
				+ ""
				+ RName
				+ "(sum("
				+ RName
				+ "(((nvl(r.meij,0) + nvl(r.meijs,0)) * f.yingd),"
				+ scale_in
				+ ")),"
				+ scale_out
				+ ")  yingdsp,\n"
				+ ""
				+ RName
				+ "(sum("
				+ RName
				+ "(((nvl(r.meij,0) + nvl(r.meijs,0) + nvl(r.yunj,0) + nvl(r.yunjs,0))\n"
				+ "* (f.yingd-f.yingk))," + scale_in + ")),"
				+ scale_out + ")  kuidsp\n"
				+ "from ruccb r, fahb f, meikxxb m, gongysb g,\n" + "\n"
				+ "(select fahb_id, sum(maoz - piz) as guohsl\n"
				+ "from chepb\n"
				+ "where jianjfs = '����' group by fahb_id ) c\n"
				+ "where f.meikxxb_id = m.id\n" + " and m.meikdq_id = "
				+ gongysb_id + " and m.meikdq_id = g.id and f.jihkjb_id =" + jihkjb_id
				+ "and f.pinzb_id = " + pinzb_id
				+ " and f.yunsfsb_id =" + yunsfsb_id
				+ " and f.daohrq >= " + CurrODate 
				+ " \n" + "and f.daohrq <=" + EndDate
				+ " \n" + "and f.diancxxb_id = "
				+ diancxxb_id + "\n"
				+ "and f.ruccbb_id = r.id(+) and f.id = c.fahb_id(+)";// ����������
		} else {
			if(sfbiaoz.equals("��")){	
				String RName = "round_new"; // ��Լ��������
				sql = "select\n"
					+ "nvl(max(g.xuh), 0) as xuh,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(f.laimsl,"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ") as jingz,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(f.biaoz,"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ") as biaoz,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(decode(f.biaoz,f.laimsl,0,f.yingd),"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ") as yingd,\n"
					+ ""
					+ RName
					+ "(sum(decode(f.biaoz,f.laimsl,0,"
					+ RName
					+ "(f.biaoz,"
					+ scale_in
					+ ")"
					+ "-"
					+ RName
					+ "(f.laimsl,"
					+ scale_in
					+ ")+"
					+ RName
					+ "(f.yingd,"
					+ scale_in
					+ ")-"
					+ RName
					+ "(f.yuns,"
					+ scale_in
					+ "))),"
					+ scale_out
					+ ") as kuid,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(decode(f.biaoz,f.laimsl,0,f.yuns),"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ") as yuns,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(f.koud,"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ") as koud,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(f.kous,"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ") as kous,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(f.kouz,"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ") as kouz,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(f.koum,"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ") as koum,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(f.zongkd,"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ") as zongkd,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(f.sanfsl,"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ") as sanfsl,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(c.guohsl,"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ") as jianjsl,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(f.laimsl,"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ") as laimsl,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(((nvl(r.meij,0) + nvl(r.meijs,0)) * f.yingd),"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ")  yingdsp,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(((nvl(r.meij,0) + nvl(r.meijs,0) + nvl(r.yunj,0) + nvl(r.yunjs,0))\n"
					+ "* (f.yingd-f.yingk))," + scale_in + ")),"
					+ scale_out + ")  kuidsp\n"
					+ "from ruccb r, fahb f, meikxxb m, gongysb g,\n" + "\n"
					+ "(select fahb_id, sum(maoz - piz) as guohsl\n"
					+ "from chepb\n"
					+ "where jianjfs = '����' group by fahb_id ) c\n"
					+ "where f.meikxxb_id = m.id\n" + " and m.meikdq_id = "
					+ gongysb_id + " and m.meikdq_id = g.id and f.jihkjb_id =" + jihkjb_id
					+ "and f.pinzb_id = " + pinzb_id
					+ " and f.yunsfsb_id =" + yunsfsb_id
					+ " and f.daohrq >= " + CurrODate 
					+ " \n" + "and f.daohrq <=" + EndDate
					+ " \n" + "and f.diancxxb_id = "
					+ diancxxb_id + "\n"
					+ "and f.ruccbb_id = r.id(+) and f.id = c.fahb_id(+)";// ����������
			}else{
				String RName = "round_new"; // ��Լ��������
				sql = "select\n"
					+ "nvl(max(g.xuh), 0) as xuh,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(f.laimsl,"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ") as jingz,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(f.laimsl,"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ") as biaoz,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(decode(f.laimsl,f.laimsl,0,f.yingd),"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ") as yingd,\n"
					+ ""
					+ RName
					+ "(sum(decode(f.laimsl,f.laimsl,0,"
					+ RName
					+ "(f.biaoz,"
					+ scale_in
					+ ")"
					+ "-"
					+ RName
					+ "(f.laimsl,"
					+ scale_in
					+ ")+"
					+ RName
					+ "(f.yingd,"
					+ scale_in
					+ ")-"
					+ RName
					+ "(f.yuns,"
					+ scale_in
					+ "))),"
					+ scale_out
					+ ") as kuid,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(decode(f.laimsl,f.laimsl,0,f.yuns),"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ") as yuns,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(f.koud,"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ") as koud,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(f.kous,"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ") as kous,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(f.kouz,"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ") as kouz,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(f.koum,"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ") as koum,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(f.zongkd,"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ") as zongkd,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(f.sanfsl,"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ") as sanfsl,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(c.guohsl,"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ") as jianjsl,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(f.laimsl,"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ") as laimsl,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(((nvl(r.meij,0) + nvl(r.meijs,0)) * f.yingd),"
					+ scale_in
					+ ")),"
					+ scale_out
					+ ")  yingdsp,\n"
					+ ""
					+ RName
					+ "(sum("
					+ RName
					+ "(((nvl(r.meij,0) + nvl(r.meijs,0) + nvl(r.yunj,0) + nvl(r.yunjs,0))\n"
					+ "* (f.yingd-f.yingk))," + scale_in + ")),"
					+ scale_out + ")  kuidsp\n"
					+ "from ruccb r, fahb f, meikxxb m, gongysb g,\n" + "\n"
					+ "(select fahb_id, sum(maoz - piz) as guohsl\n"
					+ "from chepb\n"
					+ "where jianjfs = '����' group by fahb_id ) c\n"
					+ "where f.meikxxb_id = m.id\n" + " and m.meikdq_id = "
					+ gongysb_id + " and m.meikdq_id = g.id and f.jihkjb_id =" + jihkjb_id
					+ "and f.pinzb_id = " + pinzb_id
					+ " and f.yunsfsb_id =" + yunsfsb_id
					+ " and f.daohrq >= " + CurrODate
					+ " \n" + "and f.daohrq <=" + EndDate
					+  " \n" + "and f.diancxxb_id = "
					+ diancxxb_id + "\n"
					+ "and f.ruccbb_id = r.id(+) and f.id = c.fahb_id(+)";// ����������
			}
		}
		
		double jingz=0.0; double jingz_lj=0.0;
		
		ResultSetList datars = con.getResultSetList(sql);
		if (datars.next()) {
			jingz = datars.getDouble("jingz");
			jingz_lj = datars.getDouble("jingz");
		}
		
		String LastODate="add_months("+CurrODate+",-1) ";
		
		sql = "select sl.* from yuetjkjb tj,yueslb sl  where sl.yuetjkjb_id=tj.id and riq="
			+ LastODate
			+ " and diancxxb_id="
			+ diancxxb_id
			+ "\n"
			+ " and gongysb_id="
			+ gongysb_id
			+ " and pinzb_id="
			+ pinzb_id
			+ " and jihkjb_id="
			+ jihkjb_id
			+ " and yunsfsb_id=" + yunsfsb_id + " and fenx='�ۼ�'";
	datars = con.getResultSetList(sql);
	
	if (datars.next()) {
		jingz_lj = jingz_lj + datars.getDouble("jingz");
	}
		
	if(biaoz.equals("����")){
		return jingz;
	}else{
		return jingz_lj;
	}
	
	
	}
	
	private void convertItem(Table tb) {
		String tbCell = "";
		String compareCell = "default"; 
		int t = -1;
		int k = 0;
		int j = 0;
		
		for (int i = 1; i< tb.getRows()-1; i++) {
			tbCell = tb.getCellValue(i, 1);
			t = tbCell.indexOf(ITEM_ONE);
			if (t > -1) {
				//��ֹ�����ϲ�����ͬ�����ۼ����k
				if (!compareCell.equals(tbCell)) k++;
				compareCell = tbCell;
				tb.setCellValue(i, 1, getDxValue(k) + "��" + tbCell.substring(t + 1));
				if (k > 1) j = 0;  //��������һ���ƻ��ھ�ʱ��j���㿪ʼ
			}
			t = tbCell.indexOf(ITEM_TWO);
			if (t > -1) {
				if (!compareCell.equals(tbCell)) j++;
				compareCell = tbCell;
				tb.setCellValue(i, 1, j + "��" + tbCell.substring(t + 1));
			}
		}
	}
	
	
	public String getDxValue(int xuh) {
		String reXuh = "";
		String[] dx = { "", "һ", "��", "��", "��", "��", 
				"��", "��", "��", "��", "��", "ʮ" };
		String strXuh = String.valueOf(xuh);
		for (int i = 0; i < strXuh.length(); i++)
			reXuh = reXuh + dx[Integer.parseInt(strXuh.substring(i, i + 1))];

		return reXuh;
	}
	
	private boolean reportShowZero(){
		return ((Visit) getPage().getVisit()).isReportShowZero();
	}
	
	

	

	
//	������ʹ�õķ���

//	-------------------------�糧Tree-----------------------------------------------------------------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid_dc(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	DefaultTree dc ;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc= etu;
	}

	public String getTreeScript1() {
		return getTree_dc().getScript();
	}
	
//	-------------------------�糧Tree END-------------------------------------------------------------
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
		return getToolbar().getRenderScript();
	}
//	ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));

			getDiancmcModels();
			setTreeid_dc(visit.getDiancxxb_id() + "");
			getSelectData();
		}
	}
	
//	��ť�ļ����¼�
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

//	����ť�ύ����
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}
//	ҳ���½��֤
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
}
