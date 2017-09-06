package com.zhiren.jt.jihgl.ranlxyjh;

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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Liuc;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 
*/

public class Ranlxyjhwh extends BasePage implements PageValidateListener {

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}
	
	protected void initialize() {
		msg = "";
	}

	// ���������ȡ��
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}
	}

	boolean riq2change = false;

	private String riq2;

	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setRiq2(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
		}

	}

	// ҳ��仯��¼
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

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	private boolean _FuzsqChick = false;

	public void FuzsqButton(IRequestCycle cycle) {
		_FuzsqChick = true;
	}

	private boolean _CreateChick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateChick = true;
	}
    //�ύ��ִ�еķ���
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			//���淽��
			_SaveChick = false;
			Save();
			getSelectData();
		}

		if (_CreateChick) {
			_CreateChick = false;
			// ����
			create();
			getSelectData();
		}
		if (_RefreshChick) {
			_RefreshChick =false;
			getSelectData();
		}
		if (_FuzsqChick) {
			// ����ͬ��
			_FuzsqChick=false;
			fuzsq();
			getSelectData();
		}
	}



	// --------------------------------------------------------------------------------------------------------------------
	private boolean tijsh;// �Ƿ���� �ύ����������˹���

	public boolean isTijsh() {

		if (!"��ѡ��".equals(getLiucmc())) {
			tijsh = true;
		}
		return tijsh;
	}

	public IDropDownBean getmobmcSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getmobmcSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setmobmcSelectValue(IDropDownBean Value) {
		if (Value != null
				&& ((Visit) getPage().getVisit()).getDropDownBean1() != null) {
			if (Value.getId() != ((Visit) getPage().getVisit())
					.getDropDownBean1().getId()) {
				((Visit) getPage().getVisit()).setboolean1(true);
			} else {
				((Visit) getPage().getVisit()).setboolean1(false);
			}
			((Visit) getPage().getVisit()).setDropDownBean1(Value);

		}
	}

	public IPropertySelectionModel getmobmcSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getmobmcSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void getmobmcSelectModels() {

		String sql = "";

		sql = "select id,mingc " + "from ranlxyjhzbb " + "where diancxxb_id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id();

		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, "�½�ģ��"));
		return;
	}

	// ���ݱ���ɹ���ҳ����ʾ��Ϣ
	private String baoccgMsg;

	public String getBaoccgMsg() {
		return baoccgMsg;
	}

	public void setBaoccgMsg(String baoccgMsg) {
		this.baoccgMsg = baoccgMsg;
	}

	public String _liucmc;

	public void setLiucmc(String _value) {
		_liucmc = _value;
	}

	public String getLiucmc() {
		if (_liucmc == null) {
			_liucmc = "";
		}
		return _liucmc;
	}

	// ����ͬ�ڹ���
	private void fuzsq() {
		JDBCcon con = new JDBCcon();
		int nian=Integer.parseInt(getNianfValue().getValue());
		String sy="";
		long ranlxyjhzbb_id=0;
		String sql = 
			"select rhb.id as id, item.xuh as xuh ,item.mingc as mingc,ranlxyjhzbb_id,ZHIBMC_ITEM_ID,item.beiz as beiz,\n"
			+ "rhb.value,rhb.y1 y1,rhb.y2 y2 ,rhb.y3 y3,rhb.y4 y4,rhb.y5 y5,rhb.y6 y6,rhb.y7 y7,rhb.y8 y8,rhb.y9 y9,rhb.y10 y10,rhb.y11 y11 ,rhb.y12 y12 \n"
			+ "from item item,itemsort its,ranlxyjhb rhb,ranlxyjhzbb rzb\n"
			+ "where  rhb.ranlxyjhzbb_id=rzb.id and \n"
			+" item.itemsortid=its.id and \n"
			+ " rhb.ZHIBMC_ITEM_ID=item.id  and to_char(rzb.nianf,'yyyy') = '"
			+ (nian-1)
			+ "' and rzb.Diancxxb_Id ="
			+ getTreeid()
			+ "  order by item.xuh, item.mingc ";
		ResultSetList rsli = con.getResultSetList(sql);
		long dd=0;
		StringBuffer ab=new StringBuffer();
		ab.append("begin \n");

		while (rsli.next()) {//���ȥ���Ƿ������ݣ������������ô����ɾ������Ĳ��Ҹ���ȥ��ģ�û����ô��ʾͬ�����ݲ��㡣
//			zhi = rsli.getString("ZHI");
			String sq = 
				"select rzb.id as id, item.xuh as xuh ,item.mingc as mingc,item.beiz as beiz,\n"
				+ "rhb.value,rhb.y1 y1,rhb.y2 y2 ,rhb.y3 y3,rhb.y4 y4,rhb.y5 y5,rhb.y6 y6,rhb.y7 y7,rhb.y8 y8,rhb.y9 y9,rhb.y10 y10,rhb.y11 y11 ,rhb.y12 y12 \n"
				+ "from item item,itemsort its,ranlxyjhb rhb,ranlxyjhzbb rzb\n"
				+ "where  rhb.ranlxyjhzbb_id=rzb.id and \n"
				+" item.itemsortid=its.id and \n"
				+ " rhb.ZHIBMC_ITEM_ID=item.id  and to_char(rzb.nianf,'yyyy') = '"
				+ (nian)
				+ "' and rzb.Diancxxb_Id ="
				+ getTreeid()
				+ "  order by item.xuh, item.mingc ";
			ResultSetList rsql = con.getResultSetList(sq);// ȡ����������ݣ������������ô��ɾ����û�оͲ�����
			if (rsql.next()){
				dd=rsql.getLong("id");
			}else{
				String ssy="select id from ranlxyjhzbb where diancxxb_id="+getTreeid()+"and to_char(nianf,'yyyy') = '"
				+ (nian)+"'\n";
				ResultSetList rslis = con.getResultSetList(ssy);
				sy="insert into ranlxyjhzbb (id,diancxxb_id,nianf) values(getnewId(" + getTreeid() + ")," +
						getTreeid() +","+"to_date('"+getNianfValue()+"','yyyy')"
						+")\n";
				if(!rslis.next()){
					ResultSetList	rsi = con.getResultSetList(sy);
				}
			}
			
		
			if(rsql.getRows()>0){
				String zhi = "delete  from  ranlxyjhb r where  r.ranlxyjhzbb_id ="+dd;
				ResultSetList	rsi = con.getResultSetList(zhi);
			}
			String ssy="select id from ranlxyjhzbb where diancxxb_id="+getTreeid()+"and to_char(nianf,'yyyy') = '"
			+ (nian)+"'\n";
			ResultSetList rslis = con.getResultSetList(ssy);
			while(rslis.next()){
				ranlxyjhzbb_id=rslis.getLong("id");
			}
			ab.append("insert into ranlxyjhb (ID,ranlxyjhzbb_id,ZHIBMC_ITEM_ID,value,y1,y2,y3,y4,y5,y6,y7,y8,y9,y10,y11,y12)" +
					" values("+"getnewId(" + getTreeid() + "),"+ranlxyjhzbb_id+","+rsli.getInt("ZHIBMC_ITEM_ID")+","
					+rsli.getInt("value")+","+rsli.getInt("y1")+","+rsli.getInt("y2")+","+rsli.getInt("y3")+","+rsli.getInt("y4")+","
					+rsli.getInt("y5")+","+rsli.getInt("y6")+","+rsli.getInt("y7")+","+rsli.getInt("y8")+","
					+rsli.getInt("y9")+","+rsli.getInt("y10")+","+rsli.getInt("y11")+","+rsli.getInt("y12")
					+") ;\n");
			rsql.close();
		}
		ab.append("end;");
		if(ab.length()>13){
			con.getInsert(ab.toString());
		}
	
		if(rsli.getRows()==0){
			setMsg("ͬ�����ݲ��� ���� ����˾���ܸ���!");
		}
		
		rsli.close();
		// ����Ӧ���������ģ����ж�Ĭ�������Ѿ��趨�ˣ���ô���Բ��������򷵻���Ϣ
		con.Close();
	}

	// --------------------------------------------------------------------------------------------------------------------------

	public void create() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		JDBCcon co = new JDBCcon();
		long yuef = getYuefValue().getId();
		long nianf = getNianfValue().getId();
		String riq = "";
		if (String.valueOf(yuef).length() == 1) {
			riq = String.valueOf(nianf) 
//			+ "0" + String.valueOf(yuef) + "01"
			;
		} else {
			riq = String.valueOf(nianf)
//			+ String.valueOf(yuef) + "01"
			;
		}
		// �ж��Ƿ�ȫ���мƻ�
		String dianc_sql = "select d.jib as jib from diancxxb d where d.id= "+getTreeid();
		ResultSetList dianc_rsl = con.getResultSetList(dianc_sql);
		String diancpp="";
		while(dianc_rsl.next()){
			diancpp=dianc_rsl.getString("jib");
		}
		if ("3".equals(diancpp) ) {//����ǵ糧����

			// �˴��Ĳ�ѯ��Ҫȡ�ñ������������
			String sql = "select rzb.id, rhb.ranlxyjhzbb_id,rzb.diancxxb_id  from ranlxyjhzbb rzb,ranlxyjhb rhb where rhb.ranlxyjhzbb_id =rzb.id and \n"
					+"rzb.diancxxb_id="+ getTreeid()
					+ " and rzb.nianf = "
					+ "to_date("
					+ riq
					+ ",'yyyy')";
			ResultSetList rsl = co.getResultSetList(sql);
			long idd = 0;
			long iidd = 0;
			int pand=rsl.getRows();//ȡ�ü�¼��
			long f_id = 0;
			if (rsl.next()) {//��������ݣ��������������ݣ���ȡ�������������ID
				idd = rsl.getLong("ranlxyjhzbb_id");
				f_id =rsl.getLong("diancxxb_id");
			}
			rsl.close();
			co.Close();
			
//			if (rsl.getRows() == 0) {//���ȡ��������0 ��ô

				StringBuffer sql_insert = new StringBuffer("begin \n");
				StringBuffer sql_insert1 = new StringBuffer("begin \n");
				//�˴�������û������ֻҪ״ֵ̬����Ҫ����ô��Ҫִ�в������
				if(pand>0){//ȡ���ܹ��ж��ٷ���Ҫ������ݣ�����0����Ҫɾ������Ϊ���������ݾ�Ҫ����ԭ������
					
					sql_insert.append("delete from ranlxyjhb where ranlxyjhzbb_id=").append( idd ).append(
							" ;\n");
					sql_insert.append("");
				}else{
					sql_insert1.append("insert into ranlxyjhzbb (id,diancxxb_id,nianf) values(getnewId(" + getTreeid() + ")," +
							getTreeid() +","+"to_date('"+getNianfValue()+"','yyyy')"
							+");\n");
					sql_insert1.append("end;");
					con.getInsert(sql_insert1.toString());
				}
				String sss="select id from ranlxyjhzbb where diancxxb_id="+getTreeid()+" and to_char(nianf,'yyyy')='"+getNianfValue()+"'";
				ResultSetList rssl=co.getResultSetList(sss);
				while (rssl.next() && rssl.getRows() == 1) {
                iidd=rssl.getLong("id");//�ܱ�
			}
				rssl.close();
				if(iidd!=0){//�ܱ� ������
					String s="select im.id as id,im.bianm as bianm from item im,itemsort it where it.bianm='RANLXYJHZB'"+" and im.itemsortid=it.id ";
					ResultSetList rss=co.getResultSetList(s);
					while(rss.next()){
						
						sql_insert
						.append("insert  into ")
						.append(
						"ranlxyjhb (ID,ranlxyjhzbb_id,ZHIBMC_ITEM_ID,value,y1,y2,y3,y4,y5,y6,y7,y8,y9,y10,y11,y12)")
						.append(" VALUES ( ").append(
								"getnewId(" + getTreeid() + "),").append(
										iidd).append(",").append(
												rss.getInt("id")+"," + "0,0,0,0,0,0,0,0,0,0,0,0,0" + " );\n");
						
					}
					sql_insert.append("end;");
					if (sql_insert.length() > 13) {
						con.getInsert(sql_insert.toString());
					}
					rss.close();
				}
								
				
				con.Close();
				//��ʾ��������ɹ�
				setMsg(String.valueOf(nianf) + "��"  + "�����ݳɹ����ɣ�");
				// }else{
				// setMsg("���������ݽ�����ԭ�����ݣ�");
			}else{
				
				//���û��ȡ������
				setMsg("����˾�����ܵ�������!");
//			}
		}
//	else {
		con.Close();
//		getSelectData();
		}
//	}

	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}

	public String getYuef() {
		int intYuef = Integer.parseInt(((Visit) getPage().getVisit())
				.getString2());
		if (intYuef < 10) {
			return "0" + intYuef;
		} else {
			return ((Visit) getPage().getVisit()).getString2();
		}
	}

	public void setYuef(String value) {
		((Visit) getPage().getVisit()).setString2(value);
	}
	private boolean _QuedChick = false;

	public void QuedButton(IRequestCycle cycle) {
		_QuedChick = true;
	}

	public boolean isLiucmcDp() {
		return ((Visit) getPage().getVisit()).getboolean6();
	}

	public void setLiucmcDp(boolean editable) {
		((Visit) getPage().getVisit()).setboolean6(editable);
	}

	

	private void Save() {

		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		// ����insert��update�ķ���
		StringBuffer sql_selete = new StringBuffer("begin \n");
		//ȡ�ñ仯����Ŀ
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(
				getChange());// ��˵�ǲ�����д����ʶ��ext .replaceAll("&nbsp;", "")������û�������������⡣
		while (mdrsl.next()) {
			if ("0".equals(mdrsl.getString("ID"))) {
            //�˴��������Ĳ�������������û�����������Բ�������
			} else {
				//ֻ�Ǹ��²���

				if (!(visit.getExtGrid1().mokmc == null)
						&& !visit.getExtGrid1().mokmc.equals("")) {
					String id = mdrsl.getString("id");
					// ����ʱ������־
					MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit
							.getRenymc(), SysConstant.RizOpType_UP, visit
							.getExtGrid1().mokmc,
							visit.getExtGrid1().tableName, id);
				}

				sql_selete.append("update ").append(
						visit.getExtGrid1().tableName).append(
						" set value=" + mdrsl.getString("value")+", y1="+mdrsl.getString("y1")+", y2="+mdrsl.getString("y2")
						+", y3="+mdrsl.getString("y3")+", y4="+mdrsl.getString("y4")+", y5="+mdrsl.getString("y5")
						+", y6="+mdrsl.getString("y6")+", y7="+mdrsl.getString("y7")+", y8="+mdrsl.getString("y8")
						+", y9="+mdrsl.getString("y9")+", y10="+mdrsl.getString("y10")
						+", y11="+mdrsl.getString("y11")+", y12="+mdrsl.getString("y12")
						);

				sql_selete.append(" where id = ").append(mdrsl.getString("ID"))
						.append(";\n");

			}
		}
		mdrsl.close();
		sql_selete.append("end;");
		if (sql_selete.length() > 13) {

			con.getUpdate(sql_selete.toString());
		}
		con.Close();
	}

	public String getBtnHandlerScript(String btnName) {
		// ��ť��script����Ӧ�������ɲ���
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getNianfValue() + "��" + getYuefValue() + "��";
		btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
		if (btnName.endsWith("CreateButton")) {
			btnsb.append("���������ݽ�����").append("���Ѵ����ݣ��Ƿ������");
		} 
		if(btnName.endsWith("FuzsqButton")) {
			btnsb.append("�Ѿ��������ݽ�������").append("���Ƿ������");
			// btnsb.append("�Ƿ�ɾ��").append(cnDate).append("�����ݣ�");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){").append(
				"document.getElementById('").append(btnName).append(
				"').click()").append("}; // end if \n").append("});}");
		return btnsb.toString();
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String str = "";
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = " and jib=3 ";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			str = " and dc.fuid = " + getTreeid() + "";
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = " and dc.id = " + getTreeid() + "";
		}

		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		// ���·���1��ʱ����ʾ01,
		String StrMonth = "";
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}

		String strdate = intyear + "-" + StrMonth + "-01";

		String titdate = intyear + "��" + StrMonth + "��";

		String gys = "";
//		if (getGongysValue().getValue().equals("��ѡ��")) {
//			gys = " ";
//		} else {
//			gys = " and gy.id=" + getGongysValue().getId();
//		}
		String context = MainGlobal.getHomeContext(this);
		long yuefen = getYuefValue().getId();
		long nianfen = getNianfValue().getId();
		String nian = "";
		if (String.valueOf(yuefen).length() == 1) {
			nian = String.valueOf(nianfen) 
//			+ "0" + String.valueOf(yuefen)
//					+ "01"
					;
		} else {
			nian = String.valueOf(nianfen) 
//			+ String.valueOf(yuefen) + "01"
			;
		}
        //ά��ҳ�����ʾ��Ŀ
		int sum=0;
		int itt =0;
		int itcou=0;
		int ic=0;
		String sql1 =

		"select rhb.id as id ,decode(item.xuh,8,'����',"
				+ "9,'����',10,'����',11,'����',12,'����',15,'����',16,'����',"
				+ "17,'����',18,'����',19,'����',20,'����',21,'����',22,'����',23,"
				+ "'����',24,'����',25,'����',26,'����',27,'����',28,'����',29,'����',30,'����',31,'����'"
				+ ",32,'����',33,'����',34,'����',35,'����',36,'����',37,'����',38,'����',39,'����',40,'����',13,8,14,9,41,10,42,11,item.xuh) xuh,item.mingc as mingc,item.beiz as beiz,\n"
				+ "rhb.value,rhb.y1 y1,rhb.y2 y2 ,rhb.y3 y3,rhb.y4 y4,rhb.y5 y5,rhb.y6 y6,rhb.y7 y7,rhb.y8 y8,rhb.y9 y9,rhb.y10 y10,rhb.y11 y11 ,rhb.y12 y12 \n"
				+ "from item item,itemsort its,ranlxyjhb rhb,ranlxyjhzbb rzb\n"
				+ "where  rhb.ranlxyjhzbb_id=rzb.id and \n"
				+ " item.itemsortid=its.id and \n"
				+ " rhb.ZHIBMC_ITEM_ID=item.id  and to_char(rzb.nianf,'yyyy') = '"
				+ nian + "' and rzb.Diancxxb_Id =" + getTreeid()
				+ " and instr(item.diancxxbs_id,to_char(rzb.diancxxb_id))>0 \n"
				+ "  order by item.xuh, item.mingc ";
//        System.out.println(sql1);
		String sql2 =

			"select rhb.id as id ,item.xuh xuh,item.mingc as mingc,item.beiz as beiz,\n"
					+ "rhb.value,rhb.y1 y1,rhb.y2 y2 ,rhb.y3 y3,rhb.y4 y4,rhb.y5 y5,rhb.y6 y6,rhb.y7 y7,rhb.y8 y8,rhb.y9 y9,rhb.y10 y10,rhb.y11 y11 ,rhb.y12 y12 \n"
					+ "from item item,itemsort its,ranlxyjhb rhb,ranlxyjhzbb rzb\n"
					+ "where  rhb.ranlxyjhzbb_id=rzb.id and \n"
					+" item.itemsortid=its.id and item.xuh between 15 and 40 and\n"
					+ " rhb.ZHIBMC_ITEM_ID=item.id  and to_char(rzb.nianf,'yyyy') = '"
					+ nian
					+ "' and rzb.Diancxxb_Id ="
					+ getTreeid()
					+" and instr(item.diancxxbs_id,to_char(rzb.diancxxb_id))>0 \n"
					+ "  order by item.xuh, item.mingc ";
		ResultSetList rsl = con.getResultSetList(sql1);
		ResultSetList rsl1 = con.getResultSetList(sql2);
		StringBuffer  sbu=new StringBuffer("");
		StringBuffer  sbu1=new StringBuffer("");
		StringBuffer  sbhang=new StringBuffer("");

		int jis=0;	//ҳ����ʵ�к�
		int count_ous=0;	//��¼һ���������ж��ٸ�ż��
		long hangc=0;//�в�
		sbu.append("");//
		StringBuffer  sqo=new StringBuffer("");
		boolean flag=false;
		
//		�볧ú�ɹ��ƻ�
		while(rsl1.next()){//ȡ�����мƻ��ھ�
			long hang=rsl1.getLong("xuh");
			//�����������ﹹ��js�ű�����ô˼���ǣ�����һ����¼���з��࣬�����ǲ���ģ�1-14��������Ǳ仯�ģ�14-n������һ��Ӧ��������
			
			
			///----------------------------///
			
			
			if(hang%2==0){//˵����ż��,ż��ʱ��������Ҫ֪�������ĸ�����ģ�ҲҪ����һ��js�����js�Ǻ������Ĳ�������Ӱ��
				//��ô�Ͱ�hang-1����Ϊ���ɱ༭��
				
//				1���������ʽ
//				2������ż���ж������е�������͹�ʽ
				
				jis++;
				count_ous++;
				setHengsgs(visit,13+jis);
				
				setOuszxqhgs(visit,13+jis,count_ous);
				
				sbhang.append("||e.row=="+(hang-2)+"");//���ɱ༭
				
				}else{//����������ʵ�������ʼ�����ģ�������Ҫ��������������ʱ�����һ��js�����js��С�Ƶ�
					
					jis++;	//�к� ����Ϊ14
					flag=true;
					count_ous=0;
					setHengsgs(visit,13+jis);
					setShushjgs(visit, 13+jis);
					setFinish_Ouszxqhgs(visit);
					
//					1���������ʽ
//					2����¼�к�
//					3������13�кϼƵ��㷨
					
				}
		}
		this.setFinish_Ouszxqhgs(visit);
		setFinish_Shushjgs(visit);
//		visit.getStringBuffer27().append(
		visit.getStringBuffer1().append(visit.getStringBuffer15()).append(visit.getStringBuffer2());	//�����������������й�ʽ
//		System.out.println(visit.getStringBuffer1());
		
		rsl1.close();
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("ranlxyjhb");
		egu.setWidth("bodyWidth");

		egu.getColumn("id").setHidden(true);
		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("xuh").setEditor(null);
		egu.getColumn("mingc").setHeader("ָ������");
		egu.getColumn("mingc").setEditor(null);
		egu.getColumn("beiz").setHeader("��λ");
		egu.getColumn("beiz").setEditor(null);
		// �˴�Ҳ���жϣ��ж��·��Ƿ��������˾��趨Ϊ���ɱ༭
		egu.getColumn("value").setHeader("�ϼ�");
//		egu.getColumn("value").setEditor(null);
		egu.getColumn("value").setRenderer("renderMotif");
		((NumberField)egu.getColumn("y1").editor).setDecimalPrecision(4);
		egu.getColumn("y1").setHeader("һ�·�");
		egu.getColumn("y1").setRenderer("renderMoti");
		((NumberField)egu.getColumn("y2").editor).setDecimalPrecision(4);
		egu.getColumn("y2").setHeader("���·�");
		egu.getColumn("y2").setRenderer("renderMoti");
		((NumberField)egu.getColumn("y3").editor).setDecimalPrecision(4);
		egu.getColumn("y3").setHeader("���·�");
		egu.getColumn("y3").setRenderer("renderMoti");
		((NumberField)egu.getColumn("y4").editor).setDecimalPrecision(4);
		egu.getColumn("y4").setHeader("���·�");
		egu.getColumn("y4").setRenderer("renderMoti");
		((NumberField)egu.getColumn("y5").editor).setDecimalPrecision(4);
		egu.getColumn("y5").setHeader("���·�");
		egu.getColumn("y5").setRenderer("renderMoti");
		((NumberField)egu.getColumn("y6").editor).setDecimalPrecision(4);
		egu.getColumn("y6").setHeader("���·�");
		egu.getColumn("y6").setRenderer("renderMoti");
		((NumberField)egu.getColumn("y7").editor).setDecimalPrecision(4);
		egu.getColumn("y7").setHeader("���·�");
		egu.getColumn("y7").setRenderer("renderMoti");
		((NumberField)egu.getColumn("y8").editor).setDecimalPrecision(4);
		egu.getColumn("y8").setHeader("���·�");
		egu.getColumn("y8").setRenderer("renderMoti");
		((NumberField)egu.getColumn("y9").editor).setDecimalPrecision(4);
		egu.getColumn("y9").setHeader("���·�");
		egu.getColumn("y9").setRenderer("renderMoti");
		((NumberField)egu.getColumn("y10").editor).setDecimalPrecision(4);
		egu.getColumn("y10").setHeader("ʮ�·�");
		egu.getColumn("y10").setRenderer("renderMoti");
		((NumberField)egu.getColumn("y11").editor).setDecimalPrecision(4);
		egu.getColumn("y11").setHeader("ʮһ�·�");
		egu.getColumn("y11").setRenderer("renderMoti");
		((NumberField)egu.getColumn("y12").editor).setDecimalPrecision(4);
		egu.getColumn("y12").setHeader("ʮ���·�");
		egu.getColumn("y12").setRenderer("renderMoti");
		
		
		egu.getColumn("xuh").setWidth(40);
		egu.getColumn("mingc").setWidth(110);
		egu.getColumn("beiz").setWidth(65);
		egu.getColumn("value").setWidth(60);
		egu.getColumn("y1").setWidth(57);
		egu.getColumn("y2").setWidth(57);
		egu.getColumn("y3").setWidth(57);
		egu.getColumn("y4").setWidth(57);
		egu.getColumn("y5").setWidth(57);
		egu.getColumn("y6").setWidth(57);
		egu.getColumn("y7").setWidth(57);
		egu.getColumn("y8").setWidth(57);
		egu.getColumn("y9").setWidth(57);
		egu.getColumn("y10").setWidth(57);
		egu.getColumn("y11").setWidth(57);
		egu.getColumn("y12").setWidth(57);
//		 egu.getColumn("value").setRenderer("function(value,metadata,rec,rowIndex){if(rowIndex==4||rowIndex==5||rowIndex==6||rowIndex==8||rowIndex==11){metadata.css='tdTextext1';}return value;}");
		// egu.getColumn("value").setEditor(null);//���ɱ༭
		// �˴���ע���ǿ���Ҫ�ж���Щ�ܹ��༭�Ͳ��ܱ༭�ġ�
		// �˴�Ӧ���з����жϣ����û�����ݾͻ�ȫ����ʾ�������������ô����ʾ��һ���·�

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);

		egu.addPaging(25);
		egu.addTbarText("���:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// ���Զ�ˢ�°�nianf.setListeners("select:function(){document.Form0.submit();}");
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(60);
		comb1.setListeners("select:function(){document.Form0.submit();}");
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("-");// ���÷ָ���
		String shangqjhxql_sql = "";
		String shangqjhxql_nianyue = "";
		String shangqjhxql_yuefen = String.valueOf(yuefen - 1);
		if (shangqjhxql_yuefen.length() == 1) {
			shangqjhxql_nianyue = String.valueOf(nianfen) + "0"
					+ shangqjhxql_yuefen + "01";
		} else {
			shangqjhxql_nianyue = String.valueOf(nianfen)
					+ String.valueOf(yuefen) + "01";
		}
		String shangqjhxql = "";


		egu.addTbarText(shangqjhxql);

		// �糧��
		egu.addTbarText("��λ����:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		// ˢ��
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){").append(
				"document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		egu.addTbarText("-");
		StringBuffer bao = new StringBuffer();
		bao.append("function (){").append(
				"document.getElementById('SaveButton').click();}");
		StringBuffer shengc = new StringBuffer();
		shengc.append("function (){").append(
				"document.getElementById('CreateButton').click();}");

		GridButton kf = new GridButton("����",
				getBtnHandlerScript("CreateButton"));// getBtnHandlerScript("CreateButton")
		kf.setIcon(SysConstant.Btn_Icon_Create);

		// �˴����ж���Ҫ�϶��Ƿ�Ϊ��ť�����ã���Ҫ�ҵ�״ֵ̬���Ƿ��ûҵĲ���


			egu.addTbarBtn(kf);
			egu.addTbarText("-");
			egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");

			egu.addTbarText("-");
			egu
					.addToolbarItem("{"
							+ new GridButton("��������",
									
									getBtnHandlerScript("FuzsqButton"))
									.getScript() + "}");
//		}

		// ---------------ҳ��js�ļ��㿪ʼ------------------------------------------
		StringBuffer sb = new StringBuffer();

		sb.append("gridDiv_grid.on('beforeedit',function(e){");

		// �趨ĳһ�в��ܱ༭
		sb
				.append("if(e.row==6||e.row==7||e.row==8||e.row==13"+sbhang.toString()+"){e.cancel=true;}");//

		sb.append("});");

		sb.append("gridDiv_grid.on('afteredit',function(e){");
		//2,3,7,8,9,10,11,12,14,15,16,17 �����ܼƵ� ��Ӧ1,2,6,7,8,9,10,11,13,14,15,16
//		sb
//		.append(" rec = gridDiv_ds.getAt(4); recfdl = gridDiv_ds.getAt(0);recfdmh = gridDiv_ds.getAt(1);");
//		�볧ú̿�ɹ��ƻ�
		sb.append("if(e.row>=14&&e.row<=(13+"+jis+")){	\n" +
				""+visit.getStringBuffer1()+"\n}");
//		System.out.println(visit.getStringBuffer1());
//		System.out.println(sb.toString());
//		sb.append(visit.getStringBuffer1());
		sb
		.append("if((e.row==1||e.row==2||e.row==7||e.row==6||e.row==9||e.row==10||e.row==11||e.row==8" +
				"||e.row==14||e.row==15||e.row==16||e.row>=13) && e.column>=6)" +
				"{ " +
		"var ches =eval(gridDiv_ds.getAt(e.row).get('Y1'))+eval(gridDiv_ds.getAt(e.row).get('Y2'))+eval(gridDiv_ds.getAt(e.row).get('Y3'))" +
		"+eval(gridDiv_ds.getAt(e.row).get('Y4'))+eval(gridDiv_ds.getAt(e.row).get('Y5'))+eval(gridDiv_ds.getAt(e.row).get('Y6'))" +
		"+eval(gridDiv_ds.getAt(e.row).get('Y7'))+eval(gridDiv_ds.getAt(e.row).get('Y8'))+eval(gridDiv_ds.getAt(e.row).get('Y9'))" +
		"+eval(gridDiv_ds.getAt(e.row).get('Y10'))+eval(gridDiv_ds.getAt(e.row).get('Y11'))+eval(gridDiv_ds.getAt(e.row).get('Y12'))" +
		";"+
//		"if(e.row==1||e.row==2){gridDiv_ds.getAt(e.row).set('VALUE',Round_new(ches,2));}else{}"+
		"gridDiv_ds.getAt(e.row).set('VALUE',Round_new(ches,4));" +
		"}");
		
		

//		1,����ƽ�������,6,13,18
		sb
		.append("if((e.row==0||e.row==12||e.row==13||e.row==(14+"+jis+") ) && e.column>=6){"+
	 
	" var s =0;"+
	" var che =eval(gridDiv_ds.getAt(e.row).get('Y1'))+eval(gridDiv_ds.getAt(e.row).get('Y2'))+eval(gridDiv_ds.getAt(e.row).get('Y3'))+eval(gridDiv_ds.getAt(e.row).get('Y4'))+eval(gridDiv_ds.getAt(e.row).get('Y5'))+eval(gridDiv_ds.getAt(e.row).get('Y6'))+eval(gridDiv_ds.getAt(e.row).get('Y7'))+eval(gridDiv_ds.getAt(e.row).get('Y8'))+eval(gridDiv_ds.getAt(e.row).get('Y9'))+eval(gridDiv_ds.getAt(e.row).get('Y10'))+eval(gridDiv_ds.getAt(e.row).get('Y11'))+eval(gridDiv_ds.getAt(e.row).get('Y12'));"+
 
 
	" for(var i=6;i<=17;i++){ "+
	 	
	 	 	 
		" if(eval(gridDiv_ds.getAt(e.row).get('Y'+(i-5)+''))=='NaN'||eval(gridDiv_ds.getAt(e.row).get('Y'+(i-5)+''))==0){"+
		 		
		 		
		" }else{"+
		 "	s++;	"+ 	
		" }"+
	 "}"+
	 "if(e.row==4||e.row==5){gridDiv_ds.getAt(e.row).set('VALUE',Round_new(eval(eval(che)/s),2)); }else{"+
"gridDiv_ds.getAt(e.row).set('VALUE',Round_new(eval(eval(che)/s),4));}"+

"}"
);
		//�����仯��7 ��8��6
		sb
		.append("if(e.row==1||e.row==3||e.row==12" +
				")" +
				"{ " +
		"var ches1 =eval(gridDiv_ds.getAt(1).get('Y1'))*eval(gridDiv_ds.getAt(3).get('Y1')/100*29271/eval(gridDiv_ds.getAt(12).get('Y1'))/10000)" +";"+
		"var ches2 =eval(gridDiv_ds.getAt(1).get('Y2'))*eval(gridDiv_ds.getAt(3).get('Y2')/100*29271/eval(gridDiv_ds.getAt(12).get('Y2'))/10000)" +";"+
		"var ches3 =eval(gridDiv_ds.getAt(1).get('Y3'))*eval(gridDiv_ds.getAt(3).get('Y3')/100*29271/eval(gridDiv_ds.getAt(12).get('Y3'))/10000)" +";"+
		"var ches4 =eval(gridDiv_ds.getAt(1).get('Y4'))*eval(gridDiv_ds.getAt(3).get('Y4')/100*29271/eval(gridDiv_ds.getAt(12).get('Y4'))/10000)" +";"+
		"var ches5 =eval(gridDiv_ds.getAt(1).get('Y5'))*eval(gridDiv_ds.getAt(3).get('Y5')/100*29271/eval(gridDiv_ds.getAt(12).get('Y5'))/10000)" +";"+
		"var ches6 =eval(gridDiv_ds.getAt(1).get('Y6'))*eval(gridDiv_ds.getAt(3).get('Y6')/100*29271/eval(gridDiv_ds.getAt(12).get('Y6'))/10000)" +";"+
		"var ches7 =eval(gridDiv_ds.getAt(1).get('Y7'))*eval(gridDiv_ds.getAt(3).get('Y7')/100*29271/eval(gridDiv_ds.getAt(12).get('Y7'))/10000)" +";"+
		"var ches8 =eval(gridDiv_ds.getAt(1).get('Y8'))*eval(gridDiv_ds.getAt(3).get('Y8')/100*29271/eval(gridDiv_ds.getAt(12).get('Y8'))/10000)" +";"+
		"var ches9 =eval(gridDiv_ds.getAt(1).get('Y9'))*eval(gridDiv_ds.getAt(3).get('Y9')/100*29271/eval(gridDiv_ds.getAt(12).get('Y9'))/10000)" +";"+
		"var ches10 =eval(gridDiv_ds.getAt(1).get('Y10'))*eval(gridDiv_ds.getAt(3).get('Y10')/100*29271/eval(gridDiv_ds.getAt(12).get('Y10'))/10000)" +";"+
		"var ches11 =eval(gridDiv_ds.getAt(1).get('Y11'))*eval(gridDiv_ds.getAt(3).get('Y11')/100*29271/eval(gridDiv_ds.getAt(12).get('Y11'))/10000)" +";"+
		"var ches12 =eval(gridDiv_ds.getAt(1).get('Y12'))*eval(gridDiv_ds.getAt(3).get('Y12')/100*29271/eval(gridDiv_ds.getAt(12).get('Y12'))/10000)" +";"+
		
		"if( String(ches1)=='NaN'||String(ches1)=='Infinity'){ gridDiv_ds.getAt(7).set('Y1',Round_new(0,4));ches1=0; }else{gridDiv_ds.getAt(7).set('Y1',Round_new(ches1,4)); }"+
		"if( String(ches2)=='NaN'||String(ches2)=='Infinity'){ gridDiv_ds.getAt(7).set('Y2',Round_new(0,4));ches2=0;  }else{gridDiv_ds.getAt(7).set('Y2',Round_new(ches2,4)); }"+
		"if( String(ches3)=='NaN'||String(ches3)=='Infinity'){ gridDiv_ds.getAt(7).set('Y3',Round_new(0,4));ches3=0;  }else{gridDiv_ds.getAt(7).set('Y3',Round_new(ches3,4)); }"+
		"if( String(ches4)=='NaN'||String(ches4)=='Infinity'){ gridDiv_ds.getAt(7).set('Y4',Round_new(0,4));ches4=0;  }else{gridDiv_ds.getAt(7).set('Y4',Round_new(ches4,4)); }"+
		"if( String(ches5)=='NaN'||String(ches5)=='Infinity'){ gridDiv_ds.getAt(7).set('Y5',Round_new(0,4));ches5=0;  }else{gridDiv_ds.getAt(7).set('Y5',Round_new(ches5,4)); }"+
		"if( String(ches6)=='NaN'||String(ches6)=='Infinity'){ gridDiv_ds.getAt(7).set('Y6',Round_new(0,4));ches6=0;  }else{gridDiv_ds.getAt(7).set('Y6',Round_new(ches6,4)); }"+
		"if( String(ches7)=='NaN'||String(ches7)=='Infinity'){ gridDiv_ds.getAt(7).set('Y7',Round_new(0,4));ches7=0;  }else{gridDiv_ds.getAt(7).set('Y7',Round_new(ches7,4)); }"+
		"if( String(ches8)=='NaN'||String(ches8)=='Infinity'){ gridDiv_ds.getAt(7).set('Y8',Round_new(0,4));ches8=0;  }else{gridDiv_ds.getAt(7).set('Y8',Round_new(ches8,4)); }"+
		"if( String(ches9)=='NaN'||String(ches9)=='Infinity'){ gridDiv_ds.getAt(7).set('Y9',Round_new(0,4));ches9=0;  }else{gridDiv_ds.getAt(7).set('Y9',Round_new(ches9,4)); }"+
		"if( String(ches10)=='NaN'||String(ches10)=='Infinity'){ gridDiv_ds.getAt(7).set('Y10',Round_new(0,4));ches10=0;  }else{gridDiv_ds.getAt(7).set('Y10',Round_new(ches10,4)); }"+
		"if( String(ches11)=='NaN'||String(ches11)=='Infinity'){ gridDiv_ds.getAt(7).set('Y11',Round_new(0,4));ches11=0;  }else{gridDiv_ds.getAt(7).set('Y11',Round_new(ches11,4)); }"+
		"if( String(ches12)=='NaN'||String(ches12)=='Infinity'){ gridDiv_ds.getAt(7).set('Y12',Round_new(0,4));ches12=0;  }else{gridDiv_ds.getAt(7).set('Y12',Round_new(ches12,4)); }"+
		
		
		"gridDiv_ds.getAt(7).set('VALUE',Round_new(eval(ches1+ches2+ches3+ches4+ches5+ches6+ches7+ches8+ches9+ches10+ches11+ches12),4));" +
		"}");
		
		sb
		.append("if(e.row==2||e.row==5||e.row==12" +
				")" +
				"{ " +
		"var ch1 =eval(gridDiv_ds.getAt(2).get('Y1'))*eval(gridDiv_ds.getAt(5).get('Y1')/1000*29271/eval(gridDiv_ds.getAt(12).get('Y1'))/10000)" +";"+
		"var ch2 =eval(gridDiv_ds.getAt(2).get('Y2'))*eval(gridDiv_ds.getAt(5).get('Y2')/1000*29271/eval(gridDiv_ds.getAt(12).get('Y2'))/10000)" +";"+
		"var ch3 =eval(gridDiv_ds.getAt(2).get('Y3'))*eval(gridDiv_ds.getAt(5).get('Y3')/1000*29271/eval(gridDiv_ds.getAt(12).get('Y3'))/10000)" +";"+
		"var ch4 =eval(gridDiv_ds.getAt(2).get('Y4'))*eval(gridDiv_ds.getAt(5).get('Y4')/1000*29271/eval(gridDiv_ds.getAt(12).get('Y4'))/10000)" +";"+
		"var ch5 =eval(gridDiv_ds.getAt(2).get('Y5'))*eval(gridDiv_ds.getAt(5).get('Y5')/1000*29271/eval(gridDiv_ds.getAt(12).get('Y5'))/10000)" +";"+
		"var ch6 =eval(gridDiv_ds.getAt(2).get('Y6'))*eval(gridDiv_ds.getAt(5).get('Y6')/1000*29271/eval(gridDiv_ds.getAt(12).get('Y6'))/10000)" +";"+
		"var ch7 =eval(gridDiv_ds.getAt(2).get('Y7'))*eval(gridDiv_ds.getAt(5).get('Y7')/1000*29271/eval(gridDiv_ds.getAt(12).get('Y7'))/10000)" +";"+
		"var ch8 =eval(gridDiv_ds.getAt(2).get('Y8'))*eval(gridDiv_ds.getAt(5).get('Y8')/1000*29271/eval(gridDiv_ds.getAt(12).get('Y8'))/10000)" +";"+
		"var ch9 =eval(gridDiv_ds.getAt(2).get('Y9'))*eval(gridDiv_ds.getAt(5).get('Y9')/1000*29271/eval(gridDiv_ds.getAt(12).get('Y9'))/10000)" +";"+
		"var ch10 =eval(gridDiv_ds.getAt(2).get('Y10'))*eval(gridDiv_ds.getAt(5).get('Y10')/1000*29271/eval(gridDiv_ds.getAt(12).get('Y10'))/10000)" +";"+
		"var ch11 =eval(gridDiv_ds.getAt(2).get('Y11'))*eval(gridDiv_ds.getAt(5).get('Y11')/1000*29271/eval(gridDiv_ds.getAt(12).get('Y11'))/10000)" +";"+
		"var ch12 =eval(gridDiv_ds.getAt(2).get('Y12'))*eval(gridDiv_ds.getAt(5).get('Y12')/1000*29271/eval(gridDiv_ds.getAt(12).get('Y12'))/10000)" +";"+
		"if( String(ch1)=='NaN'||String(ch1)=='Infinity'){ gridDiv_ds.getAt(8).set('Y1',Round_new(0,4));ch1=0; }else{gridDiv_ds.getAt(8).set('Y1',Round_new(ch1,4)); }"+
		"if( String(ch2)=='NaN'||String(ch2)=='Infinity'){ gridDiv_ds.getAt(8).set('Y2',Round_new(0,4));ch2=0;  }else{gridDiv_ds.getAt(8).set('Y2',Round_new(ch2,4)); }"+
		"if( String(ch3)=='NaN'||String(ch3)=='Infinity'){ gridDiv_ds.getAt(8).set('Y3',Round_new(0,4));ch3=0;  }else{gridDiv_ds.getAt(8).set('Y3',Round_new(ch3,4)); }"+
		"if( String(ch4)=='NaN'||String(ch4)=='Infinity'){ gridDiv_ds.getAt(8).set('Y4',Round_new(0,4));ch4=0;  }else{gridDiv_ds.getAt(8).set('Y4',Round_new(ch4,4)); }"+
		"if( String(ch5)=='NaN'||String(ch5)=='Infinity'){ gridDiv_ds.getAt(8).set('Y5',Round_new(0,4));ch5=0;  }else{gridDiv_ds.getAt(8).set('Y5',Round_new(ch5,4)); }"+
		"if( String(ch6)=='NaN'||String(ch6)=='Infinity'){ gridDiv_ds.getAt(8).set('Y6',Round_new(0,4));ch6=0;  }else{gridDiv_ds.getAt(8).set('Y6',Round_new(ch6,4)); }"+
		"if( String(ch7)=='NaN'||String(ch7)=='Infinity'){ gridDiv_ds.getAt(8).set('Y7',Round_new(0,4));ch7=0;  }else{gridDiv_ds.getAt(8).set('Y7',Round_new(ch7,4)); }"+
		"if( String(ch8)=='NaN'||String(ch8)=='Infinity'){ gridDiv_ds.getAt(8).set('Y8',Round_new(0,4));ch8=0;  }else{gridDiv_ds.getAt(8).set('Y8',Round_new(ch8,4)); }"+
		"if( String(ch9)=='NaN'||String(ch9)=='Infinity'){ gridDiv_ds.getAt(8).set('Y9',Round_new(0,4));ch9=0;  }else{gridDiv_ds.getAt(8).set('Y9',Round_new(ch9,4)); }"+
		"if( String(ch10)=='NaN'||String(ch10)=='Infinity'){ gridDiv_ds.getAt(8).set('Y10',Round_new(0,4));ch10=0;  }else{gridDiv_ds.getAt(8).set('Y10',Round_new(ch10,4)); }"+
		"if( String(ch11)=='NaN'||String(ch11)=='Infinity'){ gridDiv_ds.getAt(8).set('Y11',Round_new(0,4));ch11=0;  }else{gridDiv_ds.getAt(8).set('Y11',Round_new(ch11,4)); }"+
		"if( String(ch12)=='NaN'||String(ch12)=='Infinity'){ gridDiv_ds.getAt(8).set('Y12',Round_new(0,4));ch12=0;  }else{gridDiv_ds.getAt(8).set('Y12',Round_new(ch12,4)); }"+
		"gridDiv_ds.getAt(8).set('VALUE',Round_new(eval(ch1+ch2+ch3+ch4+ch5+ch6+ch7+ch8+ch9+ch10+ch11+ch12),4));" +
		"}");
		
		// ��¯����ԭú������
		sb
		.append("if(e.row==1||e.row==2||e.row==3||e.row==5||e.row==7||e.row==8||e.row==9||e.row==10||e.row==11||e.row==12" +
				")" +
				"{ " +
		"var che1 =eval(gridDiv_ds.getAt(7).get('Y1'))+eval(gridDiv_ds.getAt(8).get('Y1'))+eval(gridDiv_ds.getAt(9).get('Y1'))+eval(gridDiv_ds.getAt(10).get('Y1'))+eval(gridDiv_ds.getAt(11).get('Y1'))" +";"+
		"var che2 =eval(gridDiv_ds.getAt(7).get('Y2'))+eval(gridDiv_ds.getAt(8).get('Y2'))+eval(gridDiv_ds.getAt(9).get('Y2'))+eval(gridDiv_ds.getAt(10).get('Y2'))+eval(gridDiv_ds.getAt(11).get('Y2'))" +";"+
		"var che3 =eval(gridDiv_ds.getAt(7).get('Y3'))+eval(gridDiv_ds.getAt(8).get('Y3'))+eval(gridDiv_ds.getAt(9).get('Y3'))+eval(gridDiv_ds.getAt(10).get('Y3'))+eval(gridDiv_ds.getAt(11).get('Y3'))" +";"+
		"var che4 =eval(gridDiv_ds.getAt(7).get('Y4'))+eval(gridDiv_ds.getAt(8).get('Y4'))+eval(gridDiv_ds.getAt(9).get('Y4'))+eval(gridDiv_ds.getAt(10).get('Y4'))+eval(gridDiv_ds.getAt(11).get('Y4'))" +";"+
		"var che5 =eval(gridDiv_ds.getAt(7).get('Y5'))+eval(gridDiv_ds.getAt(8).get('Y5'))+eval(gridDiv_ds.getAt(9).get('Y5'))+eval(gridDiv_ds.getAt(10).get('Y5'))+eval(gridDiv_ds.getAt(11).get('Y5'))" +";"+
		"var che6 =eval(gridDiv_ds.getAt(7).get('Y6'))+eval(gridDiv_ds.getAt(8).get('Y6'))+eval(gridDiv_ds.getAt(9).get('Y6'))+eval(gridDiv_ds.getAt(10).get('Y6'))+eval(gridDiv_ds.getAt(11).get('Y6'))" +";"+
		"var che7 =eval(gridDiv_ds.getAt(7).get('Y7'))+eval(gridDiv_ds.getAt(8).get('Y7'))+eval(gridDiv_ds.getAt(9).get('Y7'))+eval(gridDiv_ds.getAt(10).get('Y7'))+eval(gridDiv_ds.getAt(11).get('Y7'))" +";"+
		"var che8 =eval(gridDiv_ds.getAt(7).get('Y8'))+eval(gridDiv_ds.getAt(8).get('Y8'))+eval(gridDiv_ds.getAt(9).get('Y8'))+eval(gridDiv_ds.getAt(10).get('Y8'))+eval(gridDiv_ds.getAt(11).get('Y8'))" +";"+
		"var che9 =eval(gridDiv_ds.getAt(7).get('Y9'))+eval(gridDiv_ds.getAt(8).get('Y9'))+eval(gridDiv_ds.getAt(9).get('Y9'))+eval(gridDiv_ds.getAt(10).get('Y9'))+eval(gridDiv_ds.getAt(11).get('Y9'))" +";"+
		"var che10 =eval(gridDiv_ds.getAt(7).get('Y10'))+eval(gridDiv_ds.getAt(8).get('Y10'))+eval(gridDiv_ds.getAt(9).get('Y10'))+eval(gridDiv_ds.getAt(10).get('Y10'))+eval(gridDiv_ds.getAt(11).get('Y10'))" +";"+
		"var che11 =eval(gridDiv_ds.getAt(7).get('Y11'))+eval(gridDiv_ds.getAt(8).get('Y11'))+eval(gridDiv_ds.getAt(9).get('Y11'))+eval(gridDiv_ds.getAt(10).get('Y11'))+eval(gridDiv_ds.getAt(11).get('Y11'))" +";"+
		"var che12 =eval(gridDiv_ds.getAt(7).get('Y12'))+eval(gridDiv_ds.getAt(8).get('Y12'))+eval(gridDiv_ds.getAt(9).get('Y12'))+eval(gridDiv_ds.getAt(10).get('Y12'))+eval(gridDiv_ds.getAt(11).get('Y12'))" +";"+
		"if( String(che1)=='NaN'||String(che1)=='Infinity'){ gridDiv_ds.getAt(6).set('Y1',Round_new(0,4));che1=0; }else{gridDiv_ds.getAt(6).set('Y1',Round_new(che1,4)); }"+
		"if( String(che2)=='NaN'||String(che2)=='Infinity'){ gridDiv_ds.getAt(6).set('Y2',Round_new(0,4));che2=0; }else{gridDiv_ds.getAt(6).set('Y2',Round_new(che2,4)); }"+
		"if( String(che3)=='NaN'||String(che3)=='Infinity'){ gridDiv_ds.getAt(6).set('Y3',Round_new(0,4));che3=0; }else{gridDiv_ds.getAt(6).set('Y3',Round_new(che3,4)); }"+
		"if( String(che4)=='NaN'||String(che4)=='Infinity'){ gridDiv_ds.getAt(6).set('Y4',Round_new(0,4)); che4=0;}else{gridDiv_ds.getAt(6).set('Y4',Round_new(che4,4)); }"+
		"if( String(che5)=='NaN'||String(che5)=='Infinity'){ gridDiv_ds.getAt(6).set('Y5',Round_new(0,4));che5=0; }else{gridDiv_ds.getAt(6).set('Y5',Round_new(che5,4)); }"+
		"if( String(che6)=='NaN'||String(che6)=='Infinity'){ gridDiv_ds.getAt(6).set('Y6',Round_new(0,4));che6=0; }else{gridDiv_ds.getAt(6).set('Y6',Round_new(che6,4)); }"+
		"if( String(che7)=='NaN'||String(che7)=='Infinity'){ gridDiv_ds.getAt(6).set('Y7',Round_new(0,4));che7=0; }else{gridDiv_ds.getAt(6).set('Y7',Round_new(che7,4)); }"+
		"if( String(che8)=='NaN'||String(che8)=='Infinity'){ gridDiv_ds.getAt(6).set('Y8',Round_new(0,4));che8=0; }else{gridDiv_ds.getAt(6).set('Y8',Round_new(che8,4)); }"+
		"if( String(che9)=='NaN'||String(che9)=='Infinity'){ gridDiv_ds.getAt(6).set('Y9',Round_new(0,4));che9=0; }else{gridDiv_ds.getAt(6).set('Y9',Round_new(che9,4)); }"+
		"if( String(che10)=='NaN'||String(che10)=='Infinity'){ gridDiv_ds.getAt(6).set('Y10',Round_new(0,4));che10=0; }else{gridDiv_ds.getAt(6).set('Y10',Round_new(che10,4)); }"+
		"if( String(che11)=='NaN'||String(che11)=='Infinity'){ gridDiv_ds.getAt(6).set('Y11',Round_new(0,4));che11=0; }else{gridDiv_ds.getAt(6).set('Y11',Round_new(che11,4)); }"+
		"if( String(che12)=='NaN'||String(che12)=='Infinity'){ gridDiv_ds.getAt(6).set('Y12',Round_new(0,4));che12=0; }else{gridDiv_ds.getAt(6).set('Y12',Round_new(che12,4)); }"+
		
		"gridDiv_ds.getAt(6).set('VALUE',Round_new(eval(che1+che2+che3+che4+che5+che6+che7+che8+che9+che10+che11+che12),4));" +
		"}");
//		
	
//		19ȡ��12�·ݵ�
		
		sb
		.append("if((e.row>=(15+"+jis+")&&e.column>=6)" +
				"){" +
				"var chesq12 =eval(gridDiv_ds.getAt(e.row).get('Y12'))" +";"+
				"var chesq11 =eval(gridDiv_ds.getAt(e.row).get('Y11'))" +";"+
				"var chesq10 =eval(gridDiv_ds.getAt(e.row).get('Y10'))" +";"+
				"var chesq9 =eval(gridDiv_ds.getAt(e.row).get('Y9'))" +";"+
				"var chesq8 =eval(gridDiv_ds.getAt(e.row).get('Y8'))" +";"+
				"var chesq7 =eval(gridDiv_ds.getAt(e.row).get('Y7'))" +";"+
				"var chesq6 =eval(gridDiv_ds.getAt(e.row).get('Y6'))" +";"+
				"var chesq5 =eval(gridDiv_ds.getAt(e.row).get('Y5'))" +";"+
				"var chesq4 =eval(gridDiv_ds.getAt(e.row).get('Y4'))" +";"+
				"var chesq3 =eval(gridDiv_ds.getAt(e.row).get('Y3'))" +";"+
				"var chesq2 =eval(gridDiv_ds.getAt(e.row).get('Y2'))" +";"+
				"var chesq1 =eval(gridDiv_ds.getAt(e.row).get('Y1'))" +";"+
//				"if(chesq12==0&&chesq11!=0){gridDiv_ds.getAt(e.row).set('VALUE',chesq11)}"+
//				"if(chesq12==0&&chesq11==0&&chesq10!=0){gridDiv_ds.getAt(e.row).set('VALUE',chesq10)}"+
//				"if(chesq12==0&&chesq11==0&&chesq10==0&&chesq9!=0){gridDiv_ds.getAt(e.row).set('VALUE',chesq9)}"+
//				"if(chesq12==0&&chesq11==0&&chesq10==0&&chesq9==0&&chesq8!=0){gridDiv_ds.getAt(e.row).set('VALUE',chesq8)}"+
//				"if(chesq12==0&&chesq11==0&&chesq10==0&&chesq9==0&&chesq8==0&&chesq7!=0){gridDiv_ds.getAt(e.row).set('VALUE',chesq7)}"+
//				"if(chesq12==0&&chesq11==0&&chesq10==0&&chesq9==0&&chesq8==0&&chesq7==0&&chesq6!=0){gridDiv_ds.getAt(e.row).set('VALUE',chesq6)}"+
//				"if(chesq12==0&&chesq11==0&&chesq10==0&&chesq9==0&&chesq8==0&&chesq7==0&&chesq6==0&&chesq5!=0){gridDiv_ds.getAt(e.row).set('VALUE',chesq5)}"+
//				"if(chesq12==0&&chesq11==0&&chesq10==0&&chesq9==0&&chesq8==0&&chesq7==0&&chesq6==0&&chesq5==0&&chesq4!=0){gridDiv_ds.getAt(e.row).set('VALUE',chesq4)}"+
//				"if(chesq12==0&&chesq11==0&&chesq10==0&&chesq9==0&&chesq8==0&&chesq7==0&&chesq6==0&&chesq5==0&&chesq4==0&&chesq3!=0){gridDiv_ds.getAt(e.row).set('VALUE',chesq3)}"+
//				"if(chesq12==0&&chesq11==0&&chesq10==0&&chesq9==0&&chesq8==0&&chesq7==0&&chesq6==0&&chesq5==0&&chesq4==0&&chesq3==0&&chesq2!=0){gridDiv_ds.getAt(e.row).set('VALUE',chesq2)}"+
//				"if(chesq12==0&&chesq11==0&&chesq10==0&&chesq9==0&&chesq8==0&&chesq7==0&&chesq6==0&&chesq5==0&&chesq4==0&&chesq3==0&&chesq2==0&&chesq2!=0){gridDiv_ds.getAt(e.row).set('VALUE',chesq1)}"+
//				"if(chesq12==0&&chesq11==0&&chesq10==0&&chesq9==0&&chesq8==0&&chesq7==0&&chesq6==0&&chesq5==0&&chesq4==0&&chesq3==0&&chesq2==0&&chesq1==0){gridDiv_ds.getAt(e.row).set('VALUE',chesq12)}"+
//				"else{gridDiv_ds.getAt(e.row).set('VALUE',chesq12);}" +
				"gridDiv_ds.getAt(e.row).set('VALUE',chesq12);"+
				
		"}");
		
		//
		
		// ��Ҫȡ��D13����ֵ--shangqjhxql

		sb.append("});");
		egu.addOtherScript(sb.toString());
		// ---------------ҳ��js�������--------------------------
		//�˴����������û������Ŀ������������(���½Կ�)
//		egu.defaultsortable=false;
		egu.setDefaultsortable(false);
		setExtGrid(egu);
//		ann_rsl.close();
		con.Close();
	}

	// �������� DropDownBean8
	// �������� ProSelectionModel8������Ĭ�����̵Ĺ����Լ�����״̬����
	public IDropDownBean getLiucmcValue() {
		String sql="";
		sql = "select zhi from xitxxb where mingc='���繩�ȼƻ�������Ĭ������' and leib='�ƻ�'";// �ƻ�
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
			JDBCcon con = new JDBCcon();
			ResultSetList rsl = con.getResultSetList(sql);
			if (rsl.next()) {
				for (int i = 0; i < ((IDropDownModel)getILiucmcModel()).getOptionCount(); i ++) {
					if (((IDropDownModel)getILiucmcModel()).getLabel(i).equals(rsl.getString("zhi"))) {
						((Visit) getPage().getVisit()).setDropDownBean8((IDropDownBean)getILiucmcModel().getOption(i));
						break;
					}
				}
			} else {
				((Visit) getPage().getVisit()).setDropDownBean8((IDropDownBean)getILiucmcModel().getOption(0));
			
		}
			rsl.close();
			con.Close();
		}
		
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	//
	public void setLiucmcValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean8() != value) {

			((Visit) getPage().getVisit()).setDropDownBean8(value);
		}
	}

	//
	public void setILiucmcModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}

	public IPropertySelectionModel getILiucmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {

			getILiucmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	public IPropertySelectionModel getILiucmcModels() {

		String sql = "";
		sql = "select liucb.id,liucb.mingc from liucb,liuclbb where liucb.liuclbb_id=liuclbb.id and liuclbb.mingc='�ƻ�' order by mingc";// �ƻ�
		String sql_xians = "";

		// �˴���Ҫ�ж�_____��Ҫ�������ֵ��ô��Ҫ��ҳ�����������ݿ��д��ڵ�ֵ��û������������ʾ����ѡ��
		((Visit) getPage().getVisit())
				.setProSelectionModel8(new IDropDownModel(sql, "��ѡ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	// �������� end

	

	// ----------------------
	// -----------------

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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.isFencb();
			setTreeid(null);
			visit.getStringBuffer1().setLength(0);	//����ʽ
//			����ʽ_begin
			visit.getStringBuffer2().setLength(0);
			visit.getStringBuffer3().setLength(0);
			visit.getStringBuffer4().setLength(0);
			visit.getStringBuffer5().setLength(0);
			visit.getStringBuffer6().setLength(0);
			visit.getStringBuffer7().setLength(0);
			visit.getStringBuffer8().setLength(0);
			visit.getStringBuffer9().setLength(0);
			visit.getStringBuffer10().setLength(0);
			visit.getStringBuffer11().setLength(0);
			visit.getStringBuffer12().setLength(0);
			visit.getStringBuffer13().setLength(0);
			
			visit.getStringBuffer14().setLength(0);
			visit.getStringBuffer15().setLength(0);
			visit.getStringBuffer16().setLength(0);
			visit.getStringBuffer17().setLength(0);
			visit.getStringBuffer18().setLength(0);
			visit.getStringBuffer19().setLength(0);
			visit.getStringBuffer20().setLength(0);
			visit.getStringBuffer21().setLength(0);
			visit.getStringBuffer22().setLength(0);
			visit.getStringBuffer23().setLength(0);
			visit.getStringBuffer24().setLength(0);
			visit.getStringBuffer25().setLength(0);
			visit.getStringBuffer26().setLength(0);
			visit.getStringBuffer27().setLength(0);
			visit.getStringBuffer28().setLength(0);
			
//			����ʽ_end
			
			this.setNianfValue(null);
			this.setNianfModel(null);
			

		}
		clearBuffer( visit);
		getSelectData();

	}

	public void clearBuffer(Visit visit)
	{
		visit.getStringBuffer1().setLength(0);	//����ʽ
//		����ʽ_begin
		visit.getStringBuffer2().setLength(0);
		visit.getStringBuffer3().setLength(0);
		visit.getStringBuffer4().setLength(0);
		visit.getStringBuffer5().setLength(0);
		visit.getStringBuffer6().setLength(0);
		visit.getStringBuffer7().setLength(0);
		visit.getStringBuffer8().setLength(0);
		visit.getStringBuffer9().setLength(0);
		visit.getStringBuffer10().setLength(0);
		visit.getStringBuffer11().setLength(0);
		visit.getStringBuffer12().setLength(0);
		visit.getStringBuffer13().setLength(0);
		
		visit.getStringBuffer14().setLength(0);
		visit.getStringBuffer15().setLength(0);
		visit.getStringBuffer16().setLength(0);
		visit.getStringBuffer17().setLength(0);
		visit.getStringBuffer18().setLength(0);
		visit.getStringBuffer19().setLength(0);
		visit.getStringBuffer20().setLength(0);
		visit.getStringBuffer21().setLength(0);
		visit.getStringBuffer22().setLength(0);
		visit.getStringBuffer23().setLength(0);
		visit.getStringBuffer24().setLength(0);
		visit.getStringBuffer25().setLength(0);
		visit.getStringBuffer26().setLength(0);
		visit.getStringBuffer27().setLength(0);
		visit.getStringBuffer28().setLength(0);
	}
	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	boolean treechange = false;

	private String treeid = "";

	public String getTreeid() {

		if (treeid == null || treeid.equals("")) {

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
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}

//	 ���
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
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
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

	// �·�
	public boolean Changeyuef = false;

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
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (_YuefValue!= null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		_YuefValue = Value;
		
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"��ѡ��"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}
	
	private void setHengsgs(Visit visit,long jis){
//		������㹫ʽ
		
		visit.getStringBuffer1().append("if(e.row=="+(jis)+"){	\n")
			.append("gridDiv_ds.getAt("+jis+").set('VALUE',");
			
		
		for(int i=1;i<=12;i++){
			
			visit.getStringBuffer1()
				.append("eval(gridDiv_ds.getAt("+(jis)+").get('Y"+i+"'))").append("+");
		}
		visit.getStringBuffer1()
			.deleteCharAt(visit.getStringBuffer1().length()-1).append(")}; \n");
	}
	
	private void setShushjgs(Visit visit,long jis){
//		����13�У��ܼ��У���������������㹫ʽ
//		��12��StringBuffer ��¼12���µ�����ֵ �� StringBuffer2~13
		
		if(visit.getStringBuffer2().length()==0){
//			��һ�ι������㹫ʽ
			visit.getStringBuffer27().append("gridDiv_ds.getAt(13).set('VALUE',").append("eval(gridDiv_ds.getAt("+jis+").get('VALUE'))").append("+");
			
			visit.getStringBuffer2().append("gridDiv_ds.getAt(13).set('Y1',").append("eval(gridDiv_ds.getAt("+jis+").get('Y1'))").append("+");
			
			visit.getStringBuffer3().append("gridDiv_ds.getAt(13).set('Y2',").append("eval(gridDiv_ds.getAt("+jis+").get('Y2'))").append("+");
			
			visit.getStringBuffer4().append("gridDiv_ds.getAt(13).set('Y3',").append("eval(gridDiv_ds.getAt("+jis+").get('Y3'))").append("+");
			
			visit.getStringBuffer5().append("gridDiv_ds.getAt(13).set('Y4',").append("eval(gridDiv_ds.getAt("+jis+").get('Y4'))").append("+");
			
			visit.getStringBuffer6().append("gridDiv_ds.getAt(13).set('Y5',").append("eval(gridDiv_ds.getAt("+jis+").get('Y5'))").append("+");
			
			visit.getStringBuffer7().append("gridDiv_ds.getAt(13).set('Y6',").append("eval(gridDiv_ds.getAt("+jis+").get('Y6'))").append("+");
			
			visit.getStringBuffer8().append("gridDiv_ds.getAt(13).set('Y7',").append("eval(gridDiv_ds.getAt("+jis+").get('Y7'))").append("+");
			
			visit.getStringBuffer9().append("gridDiv_ds.getAt(13).set('Y8',").append("eval(gridDiv_ds.getAt("+jis+").get('Y8'))").append("+");
			
			visit.getStringBuffer10().append("gridDiv_ds.getAt(13).set('Y9',").append("eval(gridDiv_ds.getAt("+jis+").get('Y9'))").append("+");
			
			visit.getStringBuffer11().append("gridDiv_ds.getAt(13).set('Y10',").append("eval(gridDiv_ds.getAt("+jis+").get('Y10'))").append("+");
			
			visit.getStringBuffer12().append("gridDiv_ds.getAt(13).set('Y11',").append("eval(gridDiv_ds.getAt("+jis+").get('Y11'))").append("+");
			
			visit.getStringBuffer13().append("gridDiv_ds.getAt(13).set('Y12',").append("eval(gridDiv_ds.getAt("+jis+").get('Y12'))").append("+");
			
		}else{
			visit.getStringBuffer27().append("eval(gridDiv_ds.getAt("+jis+").get('VALUE'))").append("+");
			
			
			visit.getStringBuffer2().append("eval(gridDiv_ds.getAt("+jis+").get('Y1'))").append("+");
			
			visit.getStringBuffer3().append("eval(gridDiv_ds.getAt("+jis+").get('Y2'))").append("+");
			
			visit.getStringBuffer4().append("eval(gridDiv_ds.getAt("+jis+").get('Y3'))").append("+");
			
			visit.getStringBuffer5().append("eval(gridDiv_ds.getAt("+jis+").get('Y4'))").append("+");
			
			visit.getStringBuffer6().append("eval(gridDiv_ds.getAt("+jis+").get('Y5'))").append("+");
			
			visit.getStringBuffer7().append("eval(gridDiv_ds.getAt("+jis+").get('Y6'))").append("+");
			
			visit.getStringBuffer8().append("eval(gridDiv_ds.getAt("+jis+").get('Y7'))").append("+");
			
			visit.getStringBuffer9().append("eval(gridDiv_ds.getAt("+jis+").get('Y8'))").append("+");
			
			visit.getStringBuffer10().append("eval(gridDiv_ds.getAt("+jis+").get('Y9'))").append("+");
			
			visit.getStringBuffer11().append("eval(gridDiv_ds.getAt("+jis+").get('Y10'))").append("+");
			
			visit.getStringBuffer12().append("eval(gridDiv_ds.getAt("+jis+").get('Y11'))").append("+");
			
			visit.getStringBuffer13().append("eval(gridDiv_ds.getAt("+jis+").get('Y12'))").append("+");
		}
	}
	
	private void setOuszxqhgs(Visit visit,long jis,int count_ous){
//		����ż������ж���һ����������е���͹�ʽ
//		�βΣ�visit��jis�кţ�count_ous��¼ÿһ�������ж�Ӧ��ż���еĸ���
		
		if(visit.getStringBuffer14().length()==0){
//			����һ����������������һ�ι�������ʽ
			visit.getStringBuffer28().append("gridDiv_ds.getAt("+(jis-count_ous)+").set('VALUE',").append("eval(gridDiv_ds.getAt("+jis+").get('VALUE'))").append("+");
			
			visit.getStringBuffer14().append("gridDiv_ds.getAt("+(jis-count_ous)+").set('Y1',").append("eval(gridDiv_ds.getAt("+jis+").get('Y1'))").append("+");
			visit.getStringBuffer16().append("gridDiv_ds.getAt("+(jis-count_ous)+").set('Y2',").append("eval(gridDiv_ds.getAt("+jis+").get('Y2'))").append("+");
			visit.getStringBuffer17().append("gridDiv_ds.getAt("+(jis-count_ous)+").set('Y3',").append("eval(gridDiv_ds.getAt("+jis+").get('Y3'))").append("+");
			visit.getStringBuffer18().append("gridDiv_ds.getAt("+(jis-count_ous)+").set('Y4',").append("eval(gridDiv_ds.getAt("+jis+").get('Y4'))").append("+");
			visit.getStringBuffer19().append("gridDiv_ds.getAt("+(jis-count_ous)+").set('Y5',").append("eval(gridDiv_ds.getAt("+jis+").get('Y5'))").append("+");
			visit.getStringBuffer20().append("gridDiv_ds.getAt("+(jis-count_ous)+").set('Y6',").append("eval(gridDiv_ds.getAt("+jis+").get('Y6'))").append("+");
			visit.getStringBuffer21().append("gridDiv_ds.getAt("+(jis-count_ous)+").set('Y7',").append("eval(gridDiv_ds.getAt("+jis+").get('Y7'))").append("+");
			visit.getStringBuffer22().append("gridDiv_ds.getAt("+(jis-count_ous)+").set('Y8',").append("eval(gridDiv_ds.getAt("+jis+").get('Y8'))").append("+");
			visit.getStringBuffer23().append("gridDiv_ds.getAt("+(jis-count_ous)+").set('Y9',").append("eval(gridDiv_ds.getAt("+jis+").get('Y9'))").append("+");
			visit.getStringBuffer24().append("gridDiv_ds.getAt("+(jis-count_ous)+").set('Y10',").append("eval(gridDiv_ds.getAt("+jis+").get('Y10'))").append("+");
			visit.getStringBuffer25().append("gridDiv_ds.getAt("+(jis-count_ous)+").set('Y11',").append("eval(gridDiv_ds.getAt("+jis+").get('Y11'))").append("+");
			visit.getStringBuffer26().append("gridDiv_ds.getAt("+(jis-count_ous)+").set('Y12',").append("eval(gridDiv_ds.getAt("+jis+").get('Y12'))").append("+");
		
		}else{
			visit.getStringBuffer28().append("eval(gridDiv_ds.getAt("+jis+").get('VALUE'))").append("+");
			
			visit.getStringBuffer14().append("eval(gridDiv_ds.getAt("+jis+").get('Y1'))").append("+");
			visit.getStringBuffer16().append("eval(gridDiv_ds.getAt("+jis+").get('Y2'))").append("+");
			visit.getStringBuffer17().append("eval(gridDiv_ds.getAt("+jis+").get('Y3'))").append("+");
			visit.getStringBuffer18().append("eval(gridDiv_ds.getAt("+jis+").get('Y4'))").append("+");
			visit.getStringBuffer19().append("eval(gridDiv_ds.getAt("+jis+").get('Y5'))").append("+");
			visit.getStringBuffer20().append("eval(gridDiv_ds.getAt("+jis+").get('Y6'))").append("+");
			visit.getStringBuffer21().append("eval(gridDiv_ds.getAt("+jis+").get('Y7'))").append("+");
			visit.getStringBuffer22().append("eval(gridDiv_ds.getAt("+jis+").get('Y8'))").append("+");
			visit.getStringBuffer23().append("eval(gridDiv_ds.getAt("+jis+").get('Y9'))").append("+");
			visit.getStringBuffer24().append("eval(gridDiv_ds.getAt("+jis+").get('Y10'))").append("+");
			visit.getStringBuffer25().append("eval(gridDiv_ds.getAt("+jis+").get('Y11'))").append("+");
			visit.getStringBuffer26().append("eval(gridDiv_ds.getAt("+jis+").get('Y12'))").append("+");
			
		}
		
//		����.....
	}
	
	private void setFinish_Ouszxqhgs(Visit visit){
		
//		���ż���ж����������㹫ʽ,����¼��һ�������е�ż���м��㹫ʽ�������������
//		����ֵΪStringBuffer15
		
		if(visit.getStringBuffer14().length()>0){
			
			visit.getStringBuffer14().deleteCharAt(visit.getStringBuffer14().length()-1).append(");	\n");
			
			visit.getStringBuffer15().append(visit.getStringBuffer14());
			
			visit.getStringBuffer14().setLength(0);
			
			visit.getStringBuffer16().deleteCharAt(visit.getStringBuffer16().length()-1).append(");	\n");
			
			visit.getStringBuffer15().append(visit.getStringBuffer16());
			
			visit.getStringBuffer16().setLength(0);
			
			visit.getStringBuffer17().deleteCharAt(visit.getStringBuffer17().length()-1).append(");	\n");
			
			visit.getStringBuffer15().append(visit.getStringBuffer17());
			
			visit.getStringBuffer17().setLength(0);
			
			visit.getStringBuffer18().deleteCharAt(visit.getStringBuffer18().length()-1).append(");	\n");
			
			visit.getStringBuffer15().append(visit.getStringBuffer18());
			
			visit.getStringBuffer18().setLength(0);
			
			visit.getStringBuffer19().deleteCharAt(visit.getStringBuffer19().length()-1).append(");	\n");
			
			visit.getStringBuffer15().append(visit.getStringBuffer19());
			
			visit.getStringBuffer19().setLength(0);
			
			visit.getStringBuffer20().deleteCharAt(visit.getStringBuffer20().length()-1).append(");	\n");
			
			visit.getStringBuffer15().append(visit.getStringBuffer20());
			
			visit.getStringBuffer20().setLength(0);
			
			visit.getStringBuffer21().deleteCharAt(visit.getStringBuffer21().length()-1).append(");	\n");
			
			visit.getStringBuffer15().append(visit.getStringBuffer21());
			
			visit.getStringBuffer21().setLength(0);
			
//			visit.getStringBuffer21().deleteCharAt(visit.getStringBuffer21().length()-1).append(");	\n");
//			
//			visit.getStringBuffer15().append(visit.getStringBuffer21());
//			
//			visit.getStringBuffer21().setLength(0);
			
			visit.getStringBuffer22().deleteCharAt(visit.getStringBuffer22().length()-1).append(");	\n");
			
			visit.getStringBuffer15().append(visit.getStringBuffer22());
			
			visit.getStringBuffer22().setLength(0);
			
			visit.getStringBuffer23().deleteCharAt(visit.getStringBuffer23().length()-1).append(");	\n");
			
			visit.getStringBuffer15().append(visit.getStringBuffer23());
			
			visit.getStringBuffer23().setLength(0);
			
			visit.getStringBuffer24().deleteCharAt(visit.getStringBuffer24().length()-1).append(");	\n");
			
			visit.getStringBuffer15().append(visit.getStringBuffer24());
			
			visit.getStringBuffer24().setLength(0);
			
			visit.getStringBuffer25().deleteCharAt(visit.getStringBuffer25().length()-1).append(");	\n");
			
			visit.getStringBuffer15().append(visit.getStringBuffer25());
			
			visit.getStringBuffer25().setLength(0);
			
			visit.getStringBuffer26().deleteCharAt(visit.getStringBuffer26().length()-1).append(");	\n");
			
			visit.getStringBuffer15().append(visit.getStringBuffer26());
			
			visit.getStringBuffer26().setLength(0);
			
			visit.getStringBuffer28().deleteCharAt(visit.getStringBuffer28().length()-1).append(");	\n");
			
			visit.getStringBuffer15().append(visit.getStringBuffer28());
			
			visit.getStringBuffer28().setLength(0);
			
			
		}
	}
	
	private void setFinish_Shushjgs(Visit visit){
		
//		��� ����13�У��ܼ��У���������������㹫ʽ
//		����ֵ  visit.getStringBuffer2()
		if(visit.getStringBuffer2().length()>0){
//			˵������������
			visit.getStringBuffer2().deleteCharAt(visit.getStringBuffer2().length()-1).append(");	\n");
			
			visit.getStringBuffer3().deleteCharAt(visit.getStringBuffer3().length()-1).append(");	\n");
			visit.getStringBuffer2().append(visit.getStringBuffer3());
			
			visit.getStringBuffer4().deleteCharAt(visit.getStringBuffer4().length()-1).append(");	\n");
			visit.getStringBuffer2().append(visit.getStringBuffer4());
			
			visit.getStringBuffer5().deleteCharAt(visit.getStringBuffer5().length()-1).append(");	\n");
			visit.getStringBuffer2().append(visit.getStringBuffer5());
			
			visit.getStringBuffer6().deleteCharAt(visit.getStringBuffer6().length()-1).append(");	\n");
			visit.getStringBuffer2().append(visit.getStringBuffer6());
			
			visit.getStringBuffer7().deleteCharAt(visit.getStringBuffer7().length()-1).append(");	\n");
			visit.getStringBuffer2().append(visit.getStringBuffer7());
			
			visit.getStringBuffer8().deleteCharAt(visit.getStringBuffer8().length()-1).append(");	\n");
			visit.getStringBuffer2().append(visit.getStringBuffer8());
			
			visit.getStringBuffer9().deleteCharAt(visit.getStringBuffer9().length()-1).append(");	\n");
			visit.getStringBuffer2().append(visit.getStringBuffer9());

			visit.getStringBuffer10().deleteCharAt(visit.getStringBuffer10().length()-1).append(");	\n");
			visit.getStringBuffer2().append(visit.getStringBuffer10());
			
			visit.getStringBuffer11().deleteCharAt(visit.getStringBuffer11().length()-1).append(");	\n");
			visit.getStringBuffer2().append(visit.getStringBuffer11());
			
			visit.getStringBuffer12().deleteCharAt(visit.getStringBuffer12().length()-1).append(");	\n");
			visit.getStringBuffer2().append(visit.getStringBuffer12());
			
			visit.getStringBuffer13().deleteCharAt(visit.getStringBuffer13().length()-1).append(");	\n");
			visit.getStringBuffer2().append(visit.getStringBuffer13());
			
			visit.getStringBuffer27().deleteCharAt(visit.getStringBuffer27().length()-1).append(");	\n");
			visit.getStringBuffer2().append(visit.getStringBuffer27());
		}
	}
}