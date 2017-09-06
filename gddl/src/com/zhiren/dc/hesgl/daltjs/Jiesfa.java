package com.zhiren.dc.hesgl.daltjs;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Money;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.dc.chengbgl.Chengbcl;
import com.zhiren.dc.hesgl.jiesd.Balances;
import com.zhiren.dc.hesgl.jiesd.Balances_variable;
import com.zhiren.dc.hesgl.jiesd.Dcbalancebean;
import com.zhiren.dc.hesgl.jiesd.Dcbalancezbbean;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author yinjm
 * 2010-08-19
 * ���������㷽��
 */

public class Jiesfa extends BasePage implements PageValidateListener {
	
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
	
	private String jiesdw; // ������㵥λid������һ��diancxxb_id
	
	public String getJiesdw() {
		return jiesdw;
	}

	public void setJiesdw(String jiesdw) {
		this.jiesdw = jiesdw;
	}
	
	private long jieslx;
	
	public long getJieslx() {
		return jieslx;
	}

	public void setJieslx(long jieslx) {
		this.jieslx = jieslx;
	}

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public List getJieszbValues() {
		return ((Visit) getPage().getVisit()).getList2();
	}

	public void setJieszbValues(List editList) {
		((Visit) getPage().getVisit()).setList2(editList);
	}
	
//	����״̬������_��ʼ
	public IDropDownBean getJiesztValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
			if (getJiesztModel().getOptionCount() > 0) {
				setJiesztValue((IDropDownBean) getJiesztModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean2();
	}

	public void setJiesztValue(IDropDownBean LeibValue) {
		
		if (((Visit) this.getPage().getVisit()).getDropDownBean2() != LeibValue && ((Visit) this.getPage().getVisit()).getDropDownBean2() != null) {
			((Visit) this.getPage().getVisit()).setboolean7(true);
		}
		
		((Visit) this.getPage().getVisit()).setDropDownBean2(LeibValue);
	}

	public IPropertySelectionModel getJiesztModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			getJiesztModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setJiesztModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
	}

	public void getJiesztModels() {
		ArrayList list = new ArrayList();
		list.add(new IDropDownBean(0, "δ����"));
		list.add(new IDropDownBean(1, "�ѽ���"));
		setJiesztModel(new IDropDownModel(list));
	}
//	����״̬������_����
	
	
//	��������������_��ʼ
	public IDropDownBean getJieslxValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getJieslxModel().getOptionCount() > 0) {
				setJieslxValue((IDropDownBean) getJieslxModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setJieslxValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(LeibValue);
	}

	public IPropertySelectionModel getJieslxModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			getJieslxModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setJieslxModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
	}

	public void getJieslxModels() {
		String sql = "select lb.id, lb.mingc from feiylbb lb where (lb.leib < 2 or lb.leib = 3) order by lb.id";
		setJieslxModel(new IDropDownModel(sql, "ȫ��"));
	}
//	��������������_����
	
//	���㷽�����������_��ʼ
	public IDropDownBean getJiesbmValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean4() == null) {
			if (getJiesbmModel().getOptionCount() > 0) {
				setJiesbmValue((IDropDownBean) getJiesbmModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean4();
	}

	public void setJiesbmValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean4(LeibValue);
	}

	public IPropertySelectionModel getJiesbmModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel4() == null) {
			getJiesbmModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
	}

	public void setJiesbmModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel4(value);
	}

	public void getJiesbmModels() {
		String sql = 
			"select distinct fa.id, fa.bianm\n" +
			"  from jiesfab fa, jiesfaglb gl\n" + 
			" where fa.id = gl.jiesfab_id\n" +
			"   and fa.shifjs = "+ getJiesztValue().getStrId() +"\n" +
			" order by fa.bianm";
		
		setJiesbmModel(new IDropDownModel(sql, "��ѡ��"));
	}
//	���㷽�����������_����
	
	// ָ������Model
	public IDropDownBean getZhibbmValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getIZhibbmModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setZhibbmValue(IDropDownBean value) {

		((Visit) getPage().getVisit()).setDropDownBean1(value);
	}

	public void setIZhibbmModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getIZhibbmModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getIZhibbmModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public IPropertySelectionModel getIZhibbmModels() {
		String sql = "select id,bianm from zhibb order by bianm";
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, "��ѡ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
	
	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	
//	"����"��ť
	private boolean _JiesClick = false;

	public void JiesButton(IRequestCycle cycle) {
		_JiesClick = true;
	}
	
//	"ɾ��"��ť
	private boolean _DeleteClick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshClick) {
			_RefreshClick = false;
		}
		if (_JiesClick) {
			_JiesClick = false;
			jies();
		}
		if (_DeleteClick) {
			_DeleteClick = false;
			delete();
			((Visit) this.getPage().getVisit()).setboolean7(true);
		}
	}
	
	public void delete() {
		
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		
		String sql = "select fa.jiesdw_id, fa.jieslx from jiesfab fa where fa.id = " + visit.getString10();
		ResultSetList rsl = con.getResultSetList(sql);
		while (rsl.next()) { // ȡ�ý��㷽����Ϣ
			setJiesdw(rsl.getString("jiesdw_id"));
			setJieslx(rsl.getLong("jieslx"));
		}
		
		StringBuffer sbsql = new StringBuffer("begin\n");
		if (getJieslx() == Locale.guotyf_feiylbb_id) { // �����˷�
			
			sbsql.append("update jiesfab fa set fa.shifjs = 0 where fa.id = ").append(getJiesbmValue().getStrId()).append(";\n")
			.append("delete from danjcpb djcp where djcp.yunfjsb_id in (select yf.id from jiesyfb yf where yf.jiesfab_id = ").append(getJiesbmValue().getStrId()).append(");\n")
			.append("delete from yunfdjb dj where dj.id in (select djcp.yunfdjb_id from danjcpb djcp where djcp.yunfjsb_id in (select yf.id from jiesyfb yf where yf.jiesfab_id = ")
			.append(getJiesbmValue().getStrId()).append("));\n")
			.append("delete from jiesyfb yf where yf.jiesfab_id = ").append(getJiesbmValue().getStrId()).append(";\n");
			
		} else { // ú��
			
			sbsql.append("update jiesfab fa set fa.shifjs = 0 where fa.id = ").append(getJiesbmValue().getStrId()).append(";\n")
			.append("update fahb fh set fh.jiesb_id = 0 where fh.id in (select gl.fahb_id from jiesfaglb gl where gl.jiesfab_id = ").append(getJiesbmValue().getStrId()).append(");\n")
			.append("delete danpcjsmxb mx where mx.jiesdid in (select js.id from jiesb js where js.jiesfab_id = ").append(getJiesbmValue().getStrId()).append(");\n")
			.append("delete jieszbsjb sj where sj.jiesdid in (select js.id from jiesb js where js.jiesfab_id = ").append(getJiesbmValue().getStrId()).append(");\n")
			.append("delete jiesb js where js.jiesfab_id = ").append(getJiesbmValue().getStrId()).append(";\n");
			
		}
		
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		rsl.close();
		con.Close();
		setMsg("ɾ���ɹ���");
	}
	
	public void jies() {
		
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		
		String sql = 
			"select fh.gongysb_id,\n" +
			"       gl.hetb_id,\n" + 
			"       fa.jiesdw_id,\n" + 
			"       fa.jieslx,\n" + 
			"       fh.lie_id,\n" + 
			"       gl.fahb_id,\n" + 
			"       gl.jieskd,\n" +
			"       gl.bukyqjk,\n" + 
			"       gl.bukyqyf,\n" +
			"       gl.yunsdwb_id\n" +
			"  from jiesfaglb gl, fahb fh, jiesfab fa\n" + 
			" where gl.jiesfab_id = "+ getJiesbmValue().getStrId() +"\n" + 
			"   and gl.jiesfab_id = fa.id\n" + 
			"   and gl.fahb_id = fh.id\n" + 
			" order by fh.gongysb_id desc, gl.hetb_id, fh.lie_id";
		
		if (getJieslx() == Locale.guotyf_feiylbb_id) {
			
			sql = 
				"select 0 as gongysb_id,\n" +
				"       gl.hetb_id,\n" + 
				"       fa.jiesdw_id,\n" + 
				"       fa.jieslx,\n" + 
				"       fh.lie_id,\n" + 
				"       gl.fahb_id,\n" + 
				"       gl.jieskd,\n" +
				"       gl.bukyqjk,\n" + 
				"       gl.bukyqyf,\n" +
				"       gl.yunsdwb_id\n" +
				"  from jiesfaglb gl, fahb fh, jiesfab fa\n" + 
				" where gl.jiesfab_id = "+ getJiesbmValue().getStrId() +"\n" + 
				"   and gl.jiesfab_id = fa.id\n" + 
				"   and gl.fahb_id = fh.id\n" + 
				" order by gl.yunsdwb_id, fh.gongysb_id desc, gl.hetb_id, fh.lie_id";
		}
		
		ResultSetList rsl = con.getResultSetList(sql);
		
		sql = 
			"select distinct fh.gongysb_id\n" +
			"  from jiesfaglb gl, fahb fh, jiesfab fa\n" + 
			" where gl.jiesfab_id = "+ getJiesbmValue().getStrId() +"\n" + 
			"   and gl.jiesfab_id = fa.id\n" + 
			"   and gl.fahb_id = fh.id";
		
		if (getJieslx() == Locale.guotyf_feiylbb_id) {
			sql = "select distinct gl.yunsdwb_id from jiesfaglb gl where gl.jiesfab_id = " + getJiesbmValue().getStrId();
		}
		
		ResultSetList count = con.getResultSetList(sql);
		String[][] pic = new String[count.getRows()][10];
		
//		�����㷽�������ķ�����Ϣ���ŵ���ά����pic��
		if (getJieslx() == Locale.guotyf_feiylbb_id) { // �����˷�
			
			int i = 0;
			String yunsdwb_id = "";
			String lie_ids = "";
			String fahb_ids = "";
			double jieskd = 0.0;	// ����۶�
			double bukyqjk = 0.0;	// ������ǰ�ۿ�
			double bukyqyf = 0.0;	// ������ǰ�˷�
			while(rsl.next()) {
				if (!yunsdwb_id.equals(rsl.getString("yunsdwb_id"))) {
					yunsdwb_id = rsl.getString("yunsdwb_id");
					pic[i][4] = lie_ids;
					pic[i][5] = fahb_ids;
					pic[i][6] = String.valueOf(jieskd);
					pic[i][7] = String.valueOf(bukyqjk);
					pic[i][8] = String.valueOf(bukyqyf);
					if (rsl.getRow() != 0) {
						i ++;
						lie_ids = "";
						fahb_ids = "";
						jieskd = 0.0;
						bukyqjk = 0.0;
						bukyqyf = 0.0;
					}
					pic[i][0] = rsl.getString("gongysb_id");
					pic[i][1] = rsl.getString("hetb_id");
					pic[i][2] = rsl.getString("jiesdw_id");
					pic[i][3] = rsl.getString("jieslx");
					pic[i][9] = rsl.getString("yunsdwb_id");
					lie_ids += rsl.getString("lie_id")+",";
					fahb_ids += rsl.getString("fahb_id")+",";
					jieskd += rsl.getDouble("jieskd");
					bukyqjk += rsl.getDouble("bukyqjk");
					bukyqyf += rsl.getDouble("bukyqyf");
					if (rsl.getRow() == rsl.getRows() - 1) {
						pic[i][4] = lie_ids;
						pic[i][5] = fahb_ids;
						pic[i][6] = String.valueOf(jieskd);
						pic[i][7] = String.valueOf(bukyqjk);
						pic[i][8] = String.valueOf(bukyqyf);
					}
				} else {
					lie_ids += rsl.getString("lie_id")+",";
					fahb_ids += rsl.getString("fahb_id")+",";
					jieskd += rsl.getDouble("jieskd");
					bukyqjk += rsl.getDouble("bukyqjk");
					bukyqyf += rsl.getDouble("bukyqyf");
					if (rsl.getRow() == rsl.getRows() - 1) {
						pic[i][4] = lie_ids;
						pic[i][5] = fahb_ids;
						pic[i][6] = String.valueOf(jieskd);
						pic[i][7] = String.valueOf(bukyqjk);
						pic[i][8] = String.valueOf(bukyqyf);
					}
				}
			}
			
		} else { // ú��
			
			int i = 0;
			String gongysb_id = "";
			String lie_ids = "";
			String fahb_ids = "";
			double jieskd = 0.0;	// ����۶�
			double bukyqjk = 0.0;	// ������ǰ�ۿ�
			double bukyqyf = 0.0;	// ������ǰ�˷�
			while(rsl.next()) {
				if (!gongysb_id.equals(rsl.getString("gongysb_id"))) {
					gongysb_id = rsl.getString("gongysb_id");
					pic[i][4] = lie_ids;
					pic[i][5] = fahb_ids;
					pic[i][6] = String.valueOf(jieskd);
					pic[i][7] = String.valueOf(bukyqjk);
					pic[i][8] = String.valueOf(bukyqyf);
					if (rsl.getRow() != 0) {
						i ++;
						lie_ids = "";
						fahb_ids = "";
						jieskd = 0.0;
						bukyqjk = 0.0;
						bukyqyf = 0.0;
					}
					pic[i][0] = rsl.getString("gongysb_id");
					pic[i][1] = rsl.getString("hetb_id");
					pic[i][2] = rsl.getString("jiesdw_id");
					pic[i][3] = rsl.getString("jieslx");
					pic[i][9] = rsl.getString("yunsdwb_id");
					lie_ids += rsl.getString("lie_id")+",";
					fahb_ids += rsl.getString("fahb_id")+",";
					jieskd += rsl.getDouble("jieskd");
					bukyqjk += rsl.getDouble("bukyqjk");
					bukyqyf += rsl.getDouble("bukyqyf");
					if (rsl.getRow() == rsl.getRows() - 1) {
						pic[i][4] = lie_ids;
						pic[i][5] = fahb_ids;
						pic[i][6] = String.valueOf(jieskd);
						pic[i][7] = String.valueOf(bukyqjk);
						pic[i][8] = String.valueOf(bukyqyf);
					}
				} else {
					lie_ids += rsl.getString("lie_id")+",";
					fahb_ids += rsl.getString("fahb_id")+",";
					jieskd += rsl.getDouble("jieskd");
					bukyqjk += rsl.getDouble("bukyqjk");
					bukyqyf += rsl.getDouble("bukyqyf");
					if (rsl.getRow() == rsl.getRows() - 1) {
						pic[i][4] = lie_ids;
						pic[i][5] = fahb_ids;
						pic[i][6] = String.valueOf(jieskd);
						pic[i][7] = String.valueOf(bukyqjk);
						pic[i][8] = String.valueOf(bukyqyf);
					}
				}
			}
		}
		
		try {
			
			long mid = 0;
			long myid = 0;
			String mtianzdw = MainGlobal.getTableCol("diancxxb", "quanc", "id",
					String.valueOf(((Visit) getPage().getVisit()).getLong1()));
			String mjiesbh = "";
			String mfahdw = "";
			String mmeikdw="";
			String mfaz = "";
			String mshoukdw = "";
			Date mfahksrq = new Date();
			Date mfahjzrq = new Date();
			Date myansksrq = new Date();
			Date myansjzrq = new Date();
			String mfahrq = "";
			String mkaihyh = "";
			String mpinz = "";
			String myuanshr = mtianzdw;
			String mzhangh = "";
			String mhetsl = "";// ��ͬ����
			double mgongfsl = 0;
			long mches = 0;
			String mxianshr = myuanshr;
			String mfapbh = "";
			String mdaibcc = "";
			String myansbh = "";
			String mduifdd = "";
			String mfukfs = "";
			double mshulzjbz = 0;
			double myanssl = 0;
			double myingksl = 0;
			double mshulzjje = 0;
			String mMjtokcalxsclfs=""; 	//�׽�ת��С������ʽ
			double myunfhsdj = 0;		//�˷Ѻ�˰����

			String mShulzb_ht="";	//����ָ���ͬ
			double mShulzb_yk=0;	//����ָ��ӯ��
			double mShulzb_zdj=0;	//����ָ���۵���
			double mShulzb_zje=0;	//����ָ���۽��
			
			String mQnetar_ht = ""; // ��ͬ����
			double mQnetar_kf = 0; // ��������
			double mQnetar_cf = 0; // ��������
			double mQnetar_js = 0; // ��������
			double mQnetar_yk = 0; // ����ӯ��
			double mQnetar_zdj = 0; // �۵���
			double mQnetar_zje = 0; // ����

			String mStd_ht = ""; // ��ͬ���
			double mStd_kf = 0; // ��������
			double mStd_cf = 0; // ��������
			double mStd_js = 0; // ��������
			double mStd_yk = 0; // ����ӯ��
			double mStd_zdj = 0; // �۵���
			double mStd_zje = 0; // ����

			String mAd_ht = ""; // ��ͬ���
			double mAd_kf = 0; // ��������
			double mAd_cf = 0; // ��������
			double mAd_js = 0; // ��������
			double mAd_yk = 0; // ����ӯ��
			double mAd_zdj = 0; // �۵���
			double mAd_zje = 0; // ����

			String mVdaf_ht = ""; // ��ͬ���
			double mVdaf_kf = 0; // ��������
			double mVdaf_cf = 0; // ��������
			double mVdaf_js = 0; // ��������
			double mVdaf_yk = 0; // ����ӯ��
			double mVdaf_zdj = 0; // �۵���
			double mVdaf_zje = 0; // ����

			String mMt_ht = ""; // ��ͬ���
			double mMt_kf = 0; // ��������
			double mMt_cf = 0; // ��������
			double mMt_js = 0; // ��������
			double mMt_yk = 0; // ����ӯ��
			double mMt_zdj = 0; // �۵���
			double mMt_zje = 0; // ����

			String mQgrad_ht = ""; // ��ͬ���
			double mQgrad_kf = 0; // ��������
			double mQgrad_cf = 0; // ��������
			double mQgrad_js = 0; // ��������
			double mQgrad_yk = 0; // ����ӯ��
			double mQgrad_zdj = 0; // �۵���
			double mQgrad_zje = 0; // ����

			String mQbad_ht = ""; // ��ͬ���
			double mQbad_kf = 0; // ��������
			double mQbad_cf = 0; // ��������
			double mQbad_js = 0; // ��������
			double mQbad_yk = 0; // ����ӯ��
			double mQbad_zdj = 0; // �۵���
			double mQbad_zje = 0; // ����

			String mHad_ht = ""; // ��ͬ���
			double mHad_kf = 0; // ��������
			double mHad_cf = 0; // ��������
			double mHad_js = 0; // ��������
			double mHad_yk = 0; // ����ӯ��
			double mHad_zdj = 0; // �۵���
			double mHad_zje = 0; // ����

			String mStad_ht = ""; // ��ͬ���
			double mStad_kf = 0; // ��������
			double mStad_cf = 0; // ��������
			double mStad_js = 0; // ��������
			double mStad_yk = 0; // ����ӯ��
			double mStad_zdj = 0; // �۵���
			double mStad_zje = 0; // ����
			
			String mStar_ht = ""; // ��ͬ���
			double mStar_kf = 0; // ��������
			double mStar_cf = 0; // ��������
			double mStar_js = 0; // ��������
			double mStar_yk = 0; // ����ӯ��
			double mStar_zdj = 0; // �۵���
			double mStar_zje = 0; // ����

			String mMad_ht = ""; // ��ͬ���
			double mMad_kf = 0; // ��������
			double mMad_cf = 0; // ��������
			double mMad_js = 0; // ��������
			double mMad_yk = 0; // ����ӯ��
			double mMad_zdj = 0; // �۵���
			double mMad_zje = 0; // ����

			String mAar_ht = ""; // ��ͬ���
			double mAar_kf = 0; // ��������
			double mAar_cf = 0; // ��������
			double mAar_js = 0; // ��������
			double mAar_yk = 0; // ����ӯ��
			double mAar_zdj = 0; // �۵���
			double mAar_zje = 0; // ����

			String mAad_ht = ""; // ��ͬ���
			double mAad_kf = 0; // ��������
			double mAad_cf = 0; // ��������
			double mAad_js = 0; // ��������
			double mAad_yk = 0; // ����ӯ��
			double mAad_zdj = 0; // �۵���
			double mAad_zje = 0; // ����

			String mVad_ht = ""; // ��ͬ���
			double mVad_kf = 0; // ��������
			double mVad_cf = 0; // ��������
			double mVad_js = 0; // ��������
			double mVad_yk = 0; // ����ӯ��
			double mVad_zdj = 0; // �۵���
			double mVad_zje = 0; // ����

			String mT2_ht = ""; // ��ͬ���
			double mT2_kf = 0; // ��������
			double mT2_cf = 0; // ��������
			double mT2_js = 0; // ��������
			double mT2_yk = 0; // ����ӯ��
			double mT2_zdj = 0; // �۵���
			double mT2_zje = 0; // ����

			String mYunju_ht = ""; // ��ͬ�˾�
			double mYunju_kf = 0; // �����˾�
			double mYunju_cf = 0; // �����˾�
			double mYunju_js = 0; // �����˾�
			double mYunju_yk = 0; // ����ӯ��
			double mYunju_zdj = 0; // �۵���
			double mYunju_zje = 0; // ����
			
			double mYunfjsdj_mk = 0;	//�˷ѽ��㵥��(jiesb)
			double mYunzfhj_mk = 0;		//���ӷѺϼƣ�jiesb��
			double mBuhsyf_mk = 0;		//����˰�˷ѣ�jiesb��
			double mYunfjsl_mk = 0;		//�˷ѽ�������(jiesb)	

			long mhetb_id = 0;
			double mjiessl = 0;
			double myunfjsl = 0;
			double mkoud_js = 0; // ����ʱ�Ŀ۶�
			double mbuhsdj = 0;
			double mjiakje = 0;
			double mbukyqjk = 0;
			double mjiakhj = 0;
			double mjiaksl = 0.13;
			double mjiaksk = 0;
			double mjiasje = 0;
			double mtielyf = 0;
			double mtielzf = 0;
			double mkuangqyf = 0;
			double mkuangqzf = 0;
			double mkuangqsk = 0;
			double mkuangqjk = 0;
			double mjiesslcy = 0;
			double mbukyqyzf = 0;
			double mjiskc = 0;
			double mbuhsyf = 0;
			double myunfsl = 0.07;
			double myunfsk = 0;
			double myunzfhj = 0;
			double mhej = 0;
			String mdaxhj = "";
			String mmeikhjdx = "";
			String myunzfhjdx = "";
			String mbeiz = "";
			String mranlbmjbr = ((Visit) getPage().getVisit()).getRenymc();
			Date mranlbmjbrq = new Date();
			double mkuidjf = 0;
			double mjingz = 0;
			Date mjiesrq = new Date();
			String mchangcwjbr = "";
			Date mchangcwjbrq = null;
			Date mruzrq = null;
			String mjieszxjbr = "";
			Date mjieszxjbrq = null;
			String mgongsrlbjbr = ((Visit) getPage().getVisit()).getRenymc();
			Date mgongsrlbjbrq = new Date();
			double mhetjg = 0;
			long mjieslx = ((Visit) getPage().getVisit()).getLong2();
			double myuns = 0;
			String myunsfs = "";
			long myunsfsb_id = 0;
			String mdiancjsbs = "";
			mdiancjsbs = getDiancjsbs(((Visit) getPage().getVisit()).getLong1());
			String mstrJieszb = "";
			String mstrHejdxh= "";
			String mErroMessage = "";
			double myingd = 0;
			double mkuid = 0;
			String myunju = ""; // ��������˾�varchar2��

			// ���е����ν���ʱ��Ҫ��ÿһ�����εĽ��������������������danpcjsmxb�У���ʱ�Ͳ�����id
			// ����ʱҪ�ж��������id������о�һ��Ҫ�����id
			long mMeikjsb_id = 0;
			long mYunfjsb_id = 0;
			long mJihkjb_id = 0;
			
			double mfengsjj=0;	//�ֹ�˾�Ӽ�
			double mjiajqdj=0;	//�Ӽ�ǰ����
			int mjijlx=0;		//�������ͣ�0����˰��1������˰��
			
			double mkuidjfyf_je=0;	//���־ܸ��˷ѽ��
			double mkuidjfzf_je=0;	//���־ܸ��ӷѽ��
			double mchaokdl=0;		//��/������
			String mChaodorKuid="";	//��/���ֱ�ʶ��CD,KD,""��
			
			boolean flag = true;
			for (int k = 0; k < pic.length; k ++) {
				
				Balances bls = new Balances();
				Balances_variable bsv = new Balances_variable(); // Balances����
				bsv.setBukjk(Double.parseDouble(pic[k][7]));
				bsv.setBukyzf(Double.parseDouble(pic[k][8]));
				bls.setBsv(bsv);
				
				bls.getBalanceData(pic[k][4].substring(0, pic[k][4].lastIndexOf(",")),
						Long.parseLong(pic[k][2]),
						Long.parseLong(pic[k][3]),
						Long.parseLong(pic[k][0]),
						Long.parseLong(pic[k][1]),
						"��", "", 
						Double.parseDouble(pic[k][6]),
						Double.parseDouble(pic[k][9]),
						0, 0.00,"", visit,0.00);
				
				visit.setString15(pic[k][2]); // ��¼���㵥λ���õ�λ�����Ƿֹ�˾id��Ҳ�����ǵ糧id
				visit.setLong1(Long.parseLong(pic[k][2])); // ��¼�糧id
				visit.setLong2(Long.parseLong(pic[k][3])); // ��¼��������
				visit.setLong9(Long.parseLong(pic[k][9])); // ��¼yunsdwb_id
				visit.setString13(pic[k][5].substring(0, pic[k][5].lastIndexOf(","))); // ��¼fahb_id
				
				bsv = bls.getBsv();
				mErroMessage = bsv.getErroInfo();
				mjiesbh = bsv.getJiesbh();
				
				mfahdw = bsv.getFahdw();
				mmeikdw = bsv.getMeikdw();
				mfaz = bsv.getFaz();
				mshoukdw = bsv.getShoukdw();
				mfahksrq = bsv.getFahksrq();
				mfahjzrq = bsv.getFahjzrq();
				mfahrq = bsv.getFahrq();
				myansksrq = bsv.getYansksrq();
				myansjzrq = bsv.getYansjsrq();
				mkaihyh = bsv.getKaihyh();
				mpinz = bsv.getRanlpz();
				mzhangh = bsv.getZhangH();
				mhetsl = String.valueOf(bsv.getHetml());
				mgongfsl = bsv.getGongfsl();
				mches = bsv.getChes();
				myansbh = bsv.getYansbh();
				mshulzjbz = bsv.getHansmj();
				myanssl = bsv.getYanssl();
				myingksl = bsv.getYingksl();
				mshulzjje = bsv.getShul_zje();
				mhetb_id = bsv.getHetb_Id();
				mdaibcc = bsv.getDaibcc();
				mkoud_js = bsv.getKoud_js();
				myunsfsb_id = bsv.getYunsfsb_id();
				myunju = bsv.getYunju_jsbz();
//				�ӼۺͲ���˰���۴���
				mfengsjj=bsv.getFengsjj();
				mjiajqdj=bsv.getJiajqdj();
				mjijlx=bsv.getJijlx();
				mMjtokcalxsclfs=bsv.getMj_to_kcal_xsclfs();//�׽�ת��С������ʽ
				myunfhsdj=bsv.getYunfjsdj();	//�˷Ѻ�������
				this.setMeikhsdjblxsw(bsv.getMeikhsdjblxsw());	//ú�˰���۱���С��λ
				this.setYunfhsdjblxsw(bsv.getYunfhsdjblxsw());	//�˷Ѻ�˰���۱���С��λ
				mbukyqjk = bsv.getBukjk();
				mbukyqyzf = bsv.getBukyzf();

//				ָ��_Begin
//				��������ָ��_Begin
				mShulzb_ht=bsv.getShul_ht();
				mShulzb_yk=bsv.getShul_yk();
				mShulzb_zdj=bsv.getShulzb_zdj();
				mShulzb_zje=bsv.getShulzb_zje();
//				��������ָ��_End
				
				mQnetar_ht = bsv.getQnetar_ht();
				mQnetar_kf = bsv.getQnetar_kf();
				mQnetar_cf = bsv.getQnetar_cf();
				mQnetar_js = bsv.getQnetar_js();
				mQnetar_yk = bsv.getQnetar_yk();
				mQnetar_zdj = bsv.getQnetar_zdj();
				mQnetar_zje = bsv.getQnetar_zje();

				mAd_ht = bsv.getAd_ht();
				mAd_kf = bsv.getAd_kf();
				mAd_cf = bsv.getAd_cf();
				mAd_js = bsv.getAd_js();
				mAd_yk = bsv.getAd_yk();
				mAd_zdj = bsv.getAd_zdj();
				mAd_zje = bsv.getAd_zje();

				mStd_ht = bsv.getStd_ht();
				mStd_kf = bsv.getStd_kf();
				mStd_cf = bsv.getStd_cf();
				mStd_js = bsv.getStd_js();
				mStd_yk = bsv.getStd_yk();
				mStd_zdj = bsv.getStd_zdj();
				mStd_zje = bsv.getStd_zje();

				mVdaf_ht = bsv.getVdaf_ht();
				mVdaf_kf = bsv.getVdaf_kf();
				mVdaf_cf = bsv.getVdaf_cf();
				mVdaf_js = bsv.getVdaf_js();
				mVdaf_yk = bsv.getVdaf_yk();
				mVdaf_zdj = bsv.getVdaf_zdj();
				mVdaf_zje = bsv.getVdaf_zje();

				mMt_ht = bsv.getMt_ht();
				mMt_kf = bsv.getMt_kf();
				mMt_cf = bsv.getMt_cf();
				mMt_js = bsv.getMt_js();
				mMt_yk = bsv.getMt_yk();
				mMt_zdj = bsv.getMt_zdj();
				mMt_zje = bsv.getMt_zje();

				mQgrad_ht = bsv.getQgrad_ht();
				mQgrad_kf = bsv.getQgrad_kf();
				mQgrad_cf = bsv.getQgrad_cf();
				mQgrad_js = bsv.getQgrad_js();
				mQgrad_yk = bsv.getQgrad_yk();
				mQgrad_zdj = bsv.getQgrad_zdj();
				mQgrad_zje = bsv.getQgrad_zje();

				mQbad_ht = bsv.getQbad_ht();
				mQbad_kf = bsv.getQbad_kf();
				mQbad_cf = bsv.getQbad_cf();
				mQbad_js = bsv.getQbad_js();
				mQbad_yk = bsv.getQbad_yk();
				mQbad_zdj = bsv.getQbad_zdj();
				mQbad_zje = bsv.getQbad_zje();

				mHad_ht = bsv.getHad_ht();
				mHad_kf = bsv.getHad_kf();
				mHad_cf = bsv.getHad_cf();
				mHad_js = bsv.getHad_js();
				mHad_yk = bsv.getHad_yk();
				mHad_zdj = bsv.getHad_zdj();
				mHad_zje = bsv.getHad_zje();

				mStad_ht = bsv.getStad_ht();
				mStad_kf = bsv.getStad_kf();
				mStad_cf = bsv.getStad_cf();
				mStad_js = bsv.getStad_js();
				mStad_yk = bsv.getStad_yk();
				mStad_zdj = bsv.getStad_zdj();
				mStad_zje = bsv.getStad_zje();
				
				mStar_ht = bsv.getStar_ht();
				mStar_kf = bsv.getStar_kf();
				mStar_cf = bsv.getStar_cf();
				mStar_js = bsv.getStar_js();
				mStar_yk = bsv.getStar_yk();
				mStar_zdj = bsv.getStar_zdj();
				mStar_zje = bsv.getStar_zje();

				mMad_ht = bsv.getMad_ht();
				mMad_kf = bsv.getMad_kf();
				mMad_cf = bsv.getMad_cf();
				mMad_js = bsv.getMad_js();
				mMad_yk = bsv.getMad_yk();
				mMad_zdj = bsv.getMad_zdj();
				mMad_zje = bsv.getMad_zje();

				mAar_ht = bsv.getAar_ht();
				mAar_kf = bsv.getAar_kf();
				mAar_cf = bsv.getAar_cf();
				mAar_js = bsv.getAar_js();
				mAar_yk = bsv.getAar_yk();
				mAar_zdj = bsv.getAar_zdj();
				mAar_zje = bsv.getAar_zje();

				mAad_ht = bsv.getAad_ht();
				mAad_kf = bsv.getAad_kf();
				mAad_cf = bsv.getAad_cf();
				mAad_js = bsv.getAad_js();
				mAad_yk = bsv.getAad_yk();
				mAad_zdj = bsv.getAad_zdj();
				mAad_zje = bsv.getAad_zje();

				mVad_ht = bsv.getVad_ht();
				mVad_kf = bsv.getVad_kf();
				mVad_cf = bsv.getVad_cf();
				mVad_js = bsv.getVad_js();
				mVad_yk = bsv.getVad_yk();
				mVad_zdj = bsv.getVad_zdj();
				mVad_zje = bsv.getVad_zje();

				mT2_ht = bsv.getT2_ht();
				mT2_kf = bsv.getT2_kf();
				mT2_cf = bsv.getT2_cf();
				mT2_js = bsv.getT2_js();
				mT2_yk = bsv.getT2_yk();
				mT2_zdj = bsv.getT2_zdj();
				mT2_zje = bsv.getT2_zje();

				mYunju_ht = bsv.getYunju_ht(); // ��ͬ�˾�
				mYunju_kf = bsv.getYunju_kf(); // �����˾�
				mYunju_cf = bsv.getYunju_cf(); // �����˾�
				mYunju_js = bsv.getYunju_js(); // �����˾�
				mYunju_yk = bsv.getYunju_yk(); // �˾�ӯ��
				mYunju_zdj = bsv.getYunju_zdj(); // �˾��۵���
				mYunju_zje = bsv.getYunju_zje(); // �۽��

				// ָ��_End
				
//				��ú����е��˷Ѳ���
				
				mYunfjsdj_mk = bsv.getYunfjsdj_mk();
				mYunzfhj_mk = bsv.getYunzfhj_mk();
				mBuhsyf_mk = bsv.getBuhsyf_mk();
				mYunfjsl_mk = bsv.getYunfjsl_mk();
//				��ú����е��˷Ѳ���_end

				mjiessl = bsv.getJiessl();
				myunfjsl = bsv.getYunfjsl();
				mbuhsdj = bsv.getBuhsmj();
				mjiakje = bsv.getJine();
				mjiakhj = bsv.getJiakhj();
				mjiaksl = bsv.getMeiksl();
				mjiaksk = bsv.getJiaksk();
				mjiasje = bsv.getJiashj();
				mtielyf = bsv.getTielyf();
				mtielzf = bsv.getTielzf();
//				�����˷ѡ��ӷ�
				mkuangqyf=bsv.getKuangqyf();
				mkuangqzf=bsv.getKuangqzf();
				
				mjiesslcy = bsv.getJiesslcy();
				mbuhsyf = bsv.getBuhsyf();
				myunfsl = bsv.getYunfsl();
				myunfsk = bsv.getYunfsk();
				myunzfhj = bsv.getYunzfhj();
				mhej = bsv.getHej();
				Money mn = new Money();
				mdaxhj = mn.NumToRMBStr(mhej);
				mmeikhjdx = mn.NumToRMBStr(bsv.getJiashj());
				myunzfhjdx = mn.NumToRMBStr(bsv.getYunzfhj());
				mbeiz = bsv.getBeiz();
				mjingz = bsv.getJingz();
				mhetjg = bsv.getHetmdj();
				myuns = bsv.getYuns();
				myunsfs = bsv.getYunsfs();
				myingd = bsv.getYingd();
				mkuid = bsv.getKuid();
				mJihkjb_id = bsv.getJihkjb_id();
				mMeikjsb_id = bsv.getMeikjsb_id();
				mYunfjsb_id = bsv.getYunfjsb_id();
				
				mkuidjfyf_je = bsv.getKuidjfyf_je();
				mkuidjfzf_je = bsv.getKuidjfzf_je();
				mchaokdl = Math.abs(bsv.getChaokdl());	//��/����������������ʾ
				mChaodorKuid = bsv.getChaodOrKuid();	//��/���ֱ�ʶ
				
				((Visit) getPage().getVisit()).setLong4(bsv.getMeikxxb_Id()); // ú����Ϣ��id
				((Visit) getPage().getVisit()).setLong5(bsv.getFaz_Id()); // ��վid
				((Visit) getPage().getVisit()).setLong6(bsv.getDaoz_Id()); // ��վid
				((Visit) getPage().getVisit()).setDouble1(bsv.getGongfsl()); // ��������
				
				List _editvalues = new ArrayList();
				List _Jieszbvalues = new ArrayList();
				
//				_editvalues.add(new Dcbalancebean(mid, myid, mtianzdw, mjiesbh,
//						mfahdw, mmeikdw, mfaz, myunsfsb_id, mshoukdw, mfahksrq, mfahjzrq,
//						myansksrq, myansjzrq, mkaihyh, mpinz, myuanshr, mzhangh,
//						mhetsl, mgongfsl, mches, mxianshr, mfapbh, mdaibcc,
//						myansbh, mduifdd, mfukfs, mshulzjbz, myanssl, myingksl,
//						myingd, mkuid, mshulzjje, mjiessl, mjiesslcy, myunfjsl,
//						mbuhsdj, mjiakje, mbukyqjk, mjiakhj, mjiaksl, mjiaksk,
//						mjiasje, mtielyf, mtielzf, mkuangqyf, mkuangqzf, mkuangqsk,
//						mkuangqjk, mbukyqyzf, mjiskc, mbuhsyf, myunfsl, myunfsk,
//						myunzfhj, mhej, mmeikhjdx, myunzfhjdx, mdaxhj, mbeiz,
//						mranlbmjbr, mranlbmjbrq, mkuidjf, mjingz, mjiesrq, mfahrq,
//						mchangcwjbr, mchangcwjbrq, mruzrq, mjieszxjbr, mjieszxjbrq,
//						mgongsrlbjbr, mgongsrlbjbrq, mhetjg, mjieslx, myuns,
//						mkoud_js, myunsfs, mdiancjsbs, mhetb_id, myunju,
//						mMeikjsb_id, mYunfjsb_id, mJihkjb_id, mfengsjj, mjiajqdj,
//						mjijlx,mMjtokcalxsclfs,mkuidjfyf_je,mkuidjfzf_je,mchaokdl,
//						mChaodorKuid,myunfhsdj,mYunfjsdj_mk,mYunzfhj_mk,mBuhsyf_mk,
//						mYunfjsl_mk,0));

				_Jieszbvalues.add(new Dcbalancezbbean(mShulzb_ht,mQnetar_ht, mStd_ht, mAd_ht,
						mVdaf_ht, mMt_ht, mQgrad_ht, mQbad_ht, mHad_ht, mStad_ht,
						mMad_ht, mAar_ht, mAad_ht, mVad_ht, mT2_ht, mYunju_ht,mStar_ht,
						mQnetar_kf, mStd_kf, mAd_kf, mVdaf_kf, mMt_kf, mQgrad_kf,
						mQbad_kf, mHad_kf, mStad_kf, mMad_kf, mAar_kf, mAad_kf,
						mVad_kf, mT2_kf, mYunju_kf, mStar_kf,mQnetar_cf, mStd_cf, mAd_cf,
						mVdaf_cf, mMt_cf, mQgrad_cf, mQbad_cf, mHad_cf, mStad_cf,
						mMad_cf, mAar_cf, mAad_cf, mVad_cf, mT2_cf, mYunju_cf,mStar_cf,
						mQnetar_js, mStd_js, mAd_js, mVdaf_js, mMt_js, mQgrad_js,
						mQbad_js, mHad_js, mStad_js, mMad_js, mAar_js, mAad_js,
						mVad_js, mT2_js, mYunju_js, mStar_js, mShulzb_yk, mQnetar_yk, mStd_yk, mAd_yk,
						mVdaf_yk, mMt_yk, mQgrad_yk, mQbad_yk, mHad_yk, mStad_yk,
						mMad_yk, mAar_yk, mAad_yk, mVad_yk, mT2_yk, mYunju_yk,mStar_yk,
						mShulzb_zdj,mQnetar_zdj, mStd_zdj, mAd_zdj, mVdaf_zdj, mMt_zdj,
						mQgrad_zdj, mQbad_zdj, mHad_zdj, mStad_zdj, mMad_zdj,
						mAar_zdj, mAad_zdj, mVad_zdj, mT2_zdj, mYunju_zdj,mStar_zdj,
						mShulzb_zje, mQnetar_zje, mStd_zje, mAd_zje, mVdaf_zje, mMt_zje,
						mQgrad_zje, mQbad_zje, mHad_zje, mStad_zje, mMad_zje,
						mAar_zje, mAad_zje, mVad_zje, mT2_zje, mYunju_zje,
						mStar_zje));
				
				setEditValues(_editvalues);
				this.setJieszbValues(_Jieszbvalues);
				
				if (!mErroMessage.equals("")) {
					this.setMsg(mErroMessage);
					break;
				}
				
				if (Save()) {
					continue; 
				} else {
					flag = false;
					break;
				}
				
			}
			
			if (flag) { // ������㷽���е����з���������ɹ�����ô��ʶ�÷����Ѿ��������
				sql = "update jiesfab fa set fa.shifjs = 1 where fa.id = " + getJiesbmValue().getStrId();
				con.getUpdate(sql);
				visit.setboolean7(true);
			}
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		rsl.close();
		count.close();
		con.Close();
	}
	
	private boolean Save() {
		// ��Ҫ˵�������н����δ��д ��ͬid������״̬id�����̸���id���󷽽���id
		String msg = "";
		long Mkid = 0;// ú��id
		long Yfid = 0;// �˷�id
		boolean Flag = false;
		String table_mk = "jiesb";
		String table_yf = "jiesyfb";
		JDBCcon con =new JDBCcon();
		con.setAutoCommit(false);
		try {

			if (Jiesdcz.CheckHetshzt(((Dcbalancebean) getEditValues().get(0))
					.getHetb_id(),((Visit) getPage().getVisit()).getLong1())) {
				// �жϺ�ͬ�����״̬
				
				if (getEditValues() != null
						&& !getEditValues().isEmpty()
						&& !((Dcbalancebean) getEditValues().get(0))
								.getJiesbh().equals("")) {
					
					if (Jiesdcz.isFengsj(((Visit) getPage().getVisit()).getString15())){
						
//						�Ƿֹ�˾�ɹ�����
						table_mk="kuangfjsmkb";
						table_yf="kuangfjsyfb";
					}
					
					if (Jiesdcz.checkbh(table_mk,table_yf,
							((Dcbalancebean) getEditValues().get(0))
									.getJiesbh(), 0, 0)) {

						if (((Visit) getPage().getVisit()).getLong2() == Locale.liangpjs_feiylbb_id) {// ��Ʊ����

							if (((Dcbalancebean) getEditValues().get(0))
									.getId() == 0) {
								// �糧ú���
								Mkid = SaveDiancjsmkb(con,table_mk,table_yf);

								if (Mkid > 0) {
									// �����糧�����˷ѱ�
									Yfid = SaveDiancjsyfb(Mkid,con,table_yf);

									if (Yfid > 0) {
										// Ҫ�ͻ�����Ϣָ��ģ�鶨һ��zhibb����

										if (SaveZhib(Mkid,con)) {

											Flag = true;
										}
									}
								}
							}

						} else if (((Visit) getPage().getVisit()).getLong2() == Locale.meikjs_feiylbb_id) {
							// ������ú��
							if (((Dcbalancebean) getEditValues().get(0))
									.getId() == 0) {
								// ����ú���
								Mkid = SaveDiancjsmkb(con,table_mk,table_yf);

								if (Mkid > 0) {

									if (this.SaveZhib(Mkid,con)) {

										Flag = true;
									}
								}
							}
						} else {
							// �������˷�
							if (checkXitszDjyf()) {
								// ��ϵͳ��Ϣ������ã�����˾ϵͳ�ܲ��ܵ������˷�
								Yfid = SaveDiancjsyfb(0,con,table_yf);

								if (Yfid > 0) {

									Flag = true;
								}

							} else {

								msg = "��ѡ������˷Ѷ�Ӧ��ú����㵥";
							}
						}
					} else {

						msg = "���㵥����ظ�";
					}
				} else {

					msg = "���ܱ���ս��㵥";
				}

			} else {

				msg = "��ͬδ��˲��ܱ��棡";
			}

			if (Flag) {

				setMsg("��������ɹ���");
				con.commit();
				Chengbcl.CountCb_js(((Visit) getPage().getVisit()).getString15(),Mkid, Yfid);

			} else {

				setMsg(msg + " �������ʧ�ܣ�");
				con.rollBack();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			con.Close();
		}
		return Flag;
	}
	
	private boolean checkXitszDjyf() {
		// TODO �Զ����ɷ������
		// ���ϵͳ�����е�"�ɵ��������˷�"����
		JDBCcon con = new JDBCcon();
		try {
			String zhi = "";

			String sql = "select zhi from xitxxb where mingc='�ɵ��������˷�'";
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {

				zhi = rs.getString("zhi");
			}

			if (zhi.trim().equals("��")) {

				return true;
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}

		return false;
	}
	
	private boolean SaveZhib(long Mkid,JDBCcon con) {
		// ������㵥ָ��
		boolean Flag = false;

		StringBuffer sbsql = new StringBuffer(" begin \n");

		sbsql.append(this.SaveJszbsjb(Mkid, Locale.jiessl_zhibb,
				((Dcbalancebean) getEditValues().get(0)).getHetsl(),
				((Dcbalancebean) getEditValues().get(0)).getGongfsl(),
				((Dcbalancebean) getEditValues().get(0)).getYanssl(),
				((Dcbalancebean) getEditValues().get(0)).getJiessl(),
				((Dcbalancebean) getEditValues().get(0)).getYingksl(),
				((Dcbalancebean) getEditValues().get(0)).getShulzjbz(),
				((Dcbalancebean) getEditValues().get(0)).getShulzjje(), 1));
		
		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getShulzb_ht()
				.equals("")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Shul_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getShulzb_ht(),
					((Dcbalancebean) getEditValues().get(0)).getGongfsl(),
					((Dcbalancebean) getEditValues().get(0)).getYanssl(),
					((Dcbalancebean) getEditValues().get(0)).getJiessl(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getShulzb_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getShulzb_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getShulzb_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getQnetar_ht()
				.equals("")) {

			sbsql
					.append(this
							.SaveJszbsjb(
									Mkid,
									Locale.Qnetar_zhibb,
									MainGlobal
											.Mjkg_to_kcalkg(
													((Dcbalancezbbean) getJieszbValues()
															.get(0))
															.getQnetar_ht(),
													"-", 0),
									MainGlobal
											.Mjkg_to_kcalkg(
													((Dcbalancezbbean) getJieszbValues()
															.get(0))
															.getQnetar_kf(), 0 ,
															((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
									MainGlobal
											.Mjkg_to_kcalkg(
													((Dcbalancezbbean) getJieszbValues()
															.get(0))
															.getQnetar_cf(), 0 ,
															((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
									MainGlobal
											.Mjkg_to_kcalkg(
													((Dcbalancezbbean) getJieszbValues()
															.get(0))
															.getQnetar_js(), 0 ,
															((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
									((Dcbalancezbbean) getJieszbValues().get(0))
											.getQnetar_yk(),
									((Dcbalancezbbean) getJieszbValues().get(0))
											.getQnetar_zdj(),
									((Dcbalancezbbean) getJieszbValues().get(0))
											.getQnetar_zje(), 1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getStd_ht()
				.equals("")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Std_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getStd_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStd_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStd_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStd_js(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStd_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStd_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStd_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getAd_ht().equals("")) {

			sbsql.append(this
					.SaveJszbsjb(Mkid, Locale.Ad_zhibb,
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getAd_ht(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getAd_kf(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getAd_cf(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getAd_js(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getAd_yk(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getAd_zdj(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getAd_zje(), 1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_ht().equals(
				"")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Vdaf_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_js(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVdaf_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getMt_ht().equals("")) {

			sbsql.append(this
					.SaveJszbsjb(Mkid, Locale.Mt_zhibb,
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getMt_ht(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getMt_kf(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getMt_cf(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getMt_js(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getMt_yk(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getMt_zdj(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getMt_zje(), 1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getQgrad_ht().equals(
				"")) {

			sbsql
					.append(this
							.SaveJszbsjb(
									Mkid,
									Locale.Qgrad_zhibb,
									MainGlobal
											.Mjkg_to_kcalkg(
													((Dcbalancezbbean) getJieszbValues()
															.get(0))
															.getQgrad_ht(),
													"-", 0),
									MainGlobal
											.Mjkg_to_kcalkg(
													((Dcbalancezbbean) getJieszbValues()
															.get(0))
															.getQgrad_kf(), 0 ,
															((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
									MainGlobal
											.Mjkg_to_kcalkg(
													((Dcbalancezbbean) getJieszbValues()
															.get(0))
															.getQgrad_cf(), 0 ,
															((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
									MainGlobal
											.Mjkg_to_kcalkg(
													((Dcbalancezbbean) getJieszbValues()
															.get(0))
															.getQgrad_js(), 0 ,
															((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
									((Dcbalancezbbean) getJieszbValues().get(0))
											.getQgrad_yk(),
									((Dcbalancezbbean) getJieszbValues().get(0))
											.getQgrad_zdj(),
									((Dcbalancezbbean) getJieszbValues().get(0))
											.getQgrad_zje(), 1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getQbad_ht().equals(
				"")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Qbad_zhibb, MainGlobal
					.Mjkg_to_kcalkg(
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getQbad_ht(), "-", 0), MainGlobal
					.Mjkg_to_kcalkg(
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getQbad_kf(), 0 ,
									((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()), MainGlobal
					.Mjkg_to_kcalkg(
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getQbad_cf(), 0 ,
									((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()), MainGlobal
					.Mjkg_to_kcalkg(
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getQbad_js(), 0 ,
									((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs()),
					((Dcbalancezbbean) getJieszbValues().get(0)).getQbad_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getQbad_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getQbad_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getHad_ht()
				.equals("")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Had_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getHad_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getHad_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getHad_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getHad_js(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getHad_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getHad_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getHad_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getStad_ht().equals(
				"")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Stad_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getStad_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStad_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStad_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStad_js(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStad_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStad_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getStad_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getMad_ht()
				.equals("")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Mad_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getMad_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getMad_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getMad_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getMad_js(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getMad_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getMad_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getMad_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getAar_ht()
				.equals("")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Aar_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getAar_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAar_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAar_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAar_js(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAar_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAar_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAar_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getAad_ht()
				.equals("")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Aad_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getAad_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAad_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAad_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAad_js(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAad_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAad_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getAad_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getVad_ht()
				.equals("")) {

			sbsql.append(this.SaveJszbsjb(Mkid, Locale.Vad_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0)).getVad_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVad_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVad_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVad_js(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVad_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVad_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0)).getVad_zje(),
					1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getT2_ht().equals("")) {

			sbsql.append(this
					.SaveJszbsjb(Mkid, Locale.T2_zhibb,
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getT2_ht(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getT2_kf(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getT2_cf(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getT2_js(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getT2_yk(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getT2_zdj(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getT2_zje(), 1));
		}

		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getYunju_ht().equals(
				"")) {

			sbsql.append(this
					.SaveJszbsjb(Mkid, Locale.Yunju_zhibb,
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getYunju_ht(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getYunju_kf(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getYunju_cf(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getYunju_js(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getYunju_yk(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getYunju_zdj(),
							((Dcbalancezbbean) getJieszbValues().get(0))
									.getYunju_zje(), 1));
		}
		
		if (!((Dcbalancezbbean) getJieszbValues().get(0)).getStar_ht().equals(
		"")) {

			sbsql.append(this
					.SaveJszbsjb(Mkid, Locale.Star_zhibb,
					((Dcbalancezbbean) getJieszbValues().get(0))
							.getStar_ht(),
					((Dcbalancezbbean) getJieszbValues().get(0))
							.getStar_kf(),
					((Dcbalancezbbean) getJieszbValues().get(0))
							.getStar_cf(),
					((Dcbalancezbbean) getJieszbValues().get(0))
							.getStar_js(),
					((Dcbalancezbbean) getJieszbValues().get(0))
							.getStar_yk(),
					((Dcbalancezbbean) getJieszbValues().get(0))
							.getStar_zdj(),
					((Dcbalancezbbean) getJieszbValues().get(0))
							.getStar_zje(), 1));
		}

		sbsql.append(" end; ");

		if (con.getInsert(sbsql.toString()) >= 0) {

			Flag = true;
		} 
		return Flag;
	}
	
	private String SaveJszbsjb(long Mkid, String mingc, String hetbz,
			double gongf, double changf, double jies, double yingk,
			double zhejbz, double zhejje, int zhuangt) {
		// ������㵥��ָ������
		Visit visit = new Visit();
		String sql = "";
		long Id = 0;
		try {

			Id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getLong1()));

			sql = " insert into jieszbsjb (id, jiesdid, zhibb_id, hetbz, gongf, changf, jies, yingk, zhejbz, zhejje, zhuangt, yansbhb_id) "
					+ " values ("
					+ Id
					+ ", "
					+ Mkid
					+ ", "
					+ getProperId(getIZhibbmModel(), mingc)
					+ ", '"
					+ hetbz
					+ "', "
					+ gongf
					+ ", "
					+ changf
					+ ", "
					+ jies
					+ ", "
					+ yingk
					+ ", "
					+ zhejbz
					+ ", "
					+ zhejje
					+ ","
					+ zhuangt
					+ ","
					+ MainGlobal.getTableId("yansbhb", "bianm",
							((Visit) getPage().getVisit()).getString4())
					+ ");	\n";

		} catch (Exception e) {

			e.printStackTrace();
		} 
		return sql;
	}
	
	private long SaveDiancjsmkb(JDBCcon con, String TableName_mk,String TableName_yf) {
		// �洢ú���
		String sql = "";
		long Id = 0;

		try {

			if (((Dcbalancebean) getEditValues().get(0)).getMeikjsb_id() > 0) {
//				���danpcjsmxb��jiesbid����0˵��danpcjsmxb��������ϢҪ��jiesb�������
//					�ʽ�����idӦȡdanpcjsmxb��jiesbid��
				Id = ((Dcbalancebean) getEditValues().get(0)).getMeikjsb_id();
			} else {

				Id = Long
						.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getLong1()));
			}

			sql = "insert into "+TableName_mk+" (id, diancxxb_id, bianm, gongysb_id, gongysmc, yunsfsb_id, yunj, yingd, kuid, \n" +
					"faz, fahksrq, fahjzrq, meiz, daibch, yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, \n" +
					"zhangh, fapbh, fukfs, duifdd, ches, jiessl, guohl, yuns, koud, jiesslcy, hansdj, bukmk, hansmk, buhsmk, \n" +
					"meikje, shuik, shuil, buhsdj, jieslx, jiesrq, ruzrq, hetb_id, liucztb_id, LIUCGZID, beiz, RANLBMJBR, RANLBMJBRQ ,\n" +
					"jihkjb_id, jiesfab_id, bukjk) "
					+ " values ("
					+ Id
					+ ", "
					+ ((Visit) getPage().getVisit()).getLong1()
					+ ",'"
					+ ((Dcbalancebean) getEditValues().get(0)).getJiesbh()
					+ "',"
					+ MainGlobal
							.getTableId("gongysb", "quanc",
									((Dcbalancebean) getEditValues().get(0))
											.getFahdw())
					+ ", '"
					+ ((Dcbalancebean) getEditValues().get(0)).getFahdw()
					+ "',"
					+ ((Dcbalancebean) getEditValues().get(0)).getYunsfsb_id()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getYunju()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getYingd()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getKuid()
					+ ",'"
					+ ((Dcbalancebean) getEditValues().get(0)).getFaz()
					+ "', to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getFahksrq())
					+ "','yyyy-MM-dd'),to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getFahjzrq())
					+ "','yyyy-MM-dd'),'"
					+ ((Dcbalancebean) getEditValues().get(0)).getPinz()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getDaibcc()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getYuanshr()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getXianshr()
					+ "', to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getYansksrq())
					+ "','yyyy-MM-dd'),to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getYansjzrq())
					+ "','yyyy-MM-dd'),'"
					+ ((Dcbalancebean) getEditValues().get(0)).getYansbh()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getShoukdw()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getKaihyh()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getZhangh()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getFapbh()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getFukfs()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getDuifdd()
					+ "',"
					+ ((Dcbalancebean) getEditValues().get(0)).getChes()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getJiessl()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getJingz()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getYuns()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getKoud_js()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getJiesslcy()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getShulzjbz()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getBukyqjk()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getJiasje()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getJiakhj()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getJiakje()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getJiaksk()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getJiaksl()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getBuhsdj()
					+ ", "
					+ ((Visit) getPage().getVisit()).getLong2()
					+ ", to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getJiesrq())
					+ "','yyyy-MM-dd'),null,"
					+ ((Dcbalancebean) getEditValues().get(0)).getHetb_id()
					+ ",0,0,'"
					+ ((Dcbalancebean) getEditValues().get(0)).getBeiz()
					+ "','"
					+ ((Dcbalancebean) getEditValues().get(0)).getRanlbmjbr()
					+ "',to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getRanlbmjbrq()) + "','yyyy-MM-dd'),"
					+ ((Dcbalancebean) getEditValues().get(0)).getJihkjb_id()
					+ ", "
					+ getJiesbmValue().getStrId()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getBukyqjk()
					+ ")";

			if (con.getInsert(sql) >= 0) {

				if (UpdateJiesb(Id,con,TableName_mk)) {

					((Dcbalancebean) getEditValues().get(0)).setId(Id);

					if (UpdateFahb_Jiesbid(Id,con)) {
						// ���·�������jiesb_id
						if(UpdateJiesyfb_Diancjsmkbid(Id,con,TableName_yf)){
//							����Ƚ������˷ѣ��ٽ���ú����Ҫ��jiesyfb���й���
							return Id;
						}
					}
				}
			} 

		} catch (Exception e) {

			e.printStackTrace();
		}
		return 0;
	}
	
	private  boolean UpdateJiesyfb_Diancjsmkbid(long meikjsb_id,
			JDBCcon con,String TableName_yf){
//	����Ƚ������˷ѣ��ٽ���ú����Ҫ��jiesyfb���й���
	boolean Flag=false;
	Jiesdcz Jscz = new Jiesdcz();
	String where = " diancjsmkb_id";
	if(TableName_yf.equals("kuangfjsyfb")){
//		�����˷ѱ�
		where = " kuangfjsmkb_id";
	}
	
	String sql="update "+TableName_yf+" set "+where+"="+meikjsb_id+" where id in("+
			"select distinct dj.yunfjsb_id\n" +
			"       from fahb f,chepb c,danjcpb dj,yunfdjb yd\n" + 
			"       where f.id=c.fahb_id\n" + 
			"             and c.id=dj.chepb_id\n" + 
			"             and dj.yunfdjb_id=yd.id\n" + 
			"             and yd.feiylbb_id="+Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
					.getLong2())+"\n" + 
			"             and f.jiesb_id in ("+meikjsb_id+"))";
	
	if(con.getUpdate(sql)>=0){
		
		Flag=true;
	}
	return Flag;
}
	
	private long SaveDiancjsyfb(long Meikid,JDBCcon con,String TableName) {
		// �洢�˷ѱ�
		String sql = "";
		long Id = 0;
		try {
			
			String guanlb_id = "diancjsmkb_id";	//����������
			
			if(TableName.equals("kuangfjsyfb")){
				
				guanlb_id = "kuangfjsmkb_id";
			}

			if (((Dcbalancebean) getEditValues().get(0)).getYunfjsb_id() > 0) {

				Id = ((Dcbalancebean) getEditValues().get(0)).getYunfjsb_id();
			} else {

				Id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
						.getVisit()).getLong1()));
			}
			
			String xiecf = String.valueOf(((Visit)getPage().getVisit()).getDouble17());
			if (xiecf.equals("0.0")) {
				xiecf = "null";
			}

			sql = " insert into "+TableName+" (id,diancxxb_id, bianm, gongysb_id, gongysmc, meikxxb_id, "
					+ " meikdwmc, yunsfsb_id, yunj, yingd, kuid, faz, fahksrq, fahjzrq, meiz, daibch, "
					+ " yuanshr, xianshr, yansksrq, yansjzrq, yansbh, shoukdw, kaihyh, zhangh, fapbh, "
					+ " fukfs, duifdd, ches, gongfsl, yanssl, jiessl, yingk, guohl, yuns, koud, jiesslcy, "
					+ " guotyf, guotzf, kuangqyf, kuangqzf, jiskc, hansdj, bukyf, hansyf, buhsyf, shuik, "
					+ " shuil, buhsdj, jieslx, jiesrq, ruzrq, hetb_id, liucztb_id, LIUCGZID, beiz, "
					+ " "+guanlb_id+", RANLBMJBR, RANLBMJBRQ, kuidjfyf,kuidjfzf, zhongchh, xiecf, jiesfab_id, bukyzf)"
					+ " values("
					+ Id
					+ ", "
					+ ((Visit) getPage().getVisit()).getLong1()
					+ ", '"
					+ ((Dcbalancebean) getEditValues().get(0)).getJiesbh()
					+ "', "
					+ MainGlobal
							.getTableId("gongysb", "quanc",
									((Dcbalancebean) getEditValues().get(0))
											.getFahdw())
					+ ", '"
					+ ((Dcbalancebean) getEditValues().get(0)).getFahdw()
					+ "',"
					+ MainGlobal
							.getTableId("meikxxb", "quanc",
									((Dcbalancebean) getEditValues().get(0))
											.getMeikdw())
					+ ",'"
					+ ((Dcbalancebean) getEditValues().get(0)).getMeikdw()
					+ "',"
					+ ((Dcbalancebean) getEditValues().get(0)).getYunsfsb_id()
					+ ",'"
					+ ((Dcbalancebean) getEditValues().get(0)).getYunju()
					+ "',"
					+ ((Dcbalancebean) getEditValues().get(0)).getYingd()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getKuid()
					+ ",'"
					+ ((Dcbalancebean) getEditValues().get(0)).getFaz()
					+ "',to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getFahksrq())
					+ "','yyyy-MM-dd'),to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getFahjzrq())
					+ "','yyyy-MM-dd'),'"
					+ ((Dcbalancebean) getEditValues().get(0)).getPinz()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getDaibcc()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getYuanshr()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getXianshr()
					+ "',to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getYansksrq())
					+ "','yyyy-MM-dd'), to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getYansjzrq())
					+ "','yyyy-MM-dd'), '"
					+ ((Dcbalancebean) getEditValues().get(0)).getYansbh()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getShoukdw()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getKaihyh()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getZhangh()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getFapbh()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getFukfs()
					+ "', '"
					+ ((Dcbalancebean) getEditValues().get(0)).getDuifdd()
					+ "', "
					+ ((Dcbalancebean) getEditValues().get(0)).getChes()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getGongfsl()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getYanssl()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getYunfjsl()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getYingksl()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getJingz()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getYuns()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getKoud_js()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getJiesslcy()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getTielyf()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getTielzf()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getKuangqyf()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getKuangqzf()
					+ ","
					+ (double) Math
							.round((((Dcbalancebean) getEditValues().get(0))
									.getTielzf() + ((Dcbalancebean) getEditValues()
									.get(0)).getKuangqzf()) * 100)
					/ 100
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getYunfhsdj()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getBukyqyzf()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getYunzfhj()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getBuhsyf()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getYunfsk()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getYunfsl()
					+ ", 0, "
					+ ((Visit) getPage().getVisit()).getLong2()
					+ ", to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getJiesrq())
					+ "','yyyy-MM-dd'), null,"
					+ ((Dcbalancebean) getEditValues().get(0)).getHetb_id()
					+ ", 0, 0, '"
					+ ((Dcbalancebean) getEditValues().get(0)).getBeiz()
					+ "',"
					+ GetMeikjsb_id(Meikid)
					+ ",'"
					+ ((Dcbalancebean) getEditValues().get(0)).getRanlbmjbr()
					+ "',to_date('"
					+ this.FormatDate(((Dcbalancebean) getEditValues().get(0))
							.getRanlbmjbrq()) + "','yyyy-MM-dd')" 
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getKuidjfyf()
					+ ","
					+ ((Dcbalancebean) getEditValues().get(0)).getKuidjfzf()
					+ ", '"+ ((Visit)getPage().getVisit()).getString19() +"', "+ xiecf
					+ ", "
					+ getJiesbmValue().getStrId()
					+ ", "
					+ ((Dcbalancebean) getEditValues().get(0)).getBukyqyzf()
					+")";

			if (con.getInsert(sql) >= 0) {

				((Dcbalancebean) getEditValues().get(0)).setYid(Id);
				if (UpdateDanjcpb_Jiesyfbid(Id, con)) {
					
					return Id;
				}
			} 

		} catch (Exception e) {

			e.printStackTrace();
		} 
		return 0;
	}
	
	private boolean UpdateDanjcpb_Jiesyfbid(long JiesyfbId, JDBCcon con) {
		// ���ݳ�Ƥ��yunfjsb_id
		// ����ڽ���ѡ��ҳ����ѡ����Ҫ�˶Ի�Ʊ��ʱ˵����yunfdjb��danjcpb���Ѿ��м�¼
		// ����ڽ���ѡ��ҳ����ѡ�񡰲���Ҫ�˶Ի�Ʊ��ʱ˵����yunfdjb��danjcpb�ж�û�м�¼��Ҫ�����¼�¼
		boolean Flag = false;
		boolean Hedbz = false; // true �Ѻ˶ԡ�false δ�˶�
		Jiesdcz Jscz = new Jiesdcz();
		String yunsdw_contion="";
		String strChepb="chepb";
		String strFinalArray[]=null;	//�����жϲ����Ƿ���Ҫ���в�ִ���
		String where_fahb_continue="";		//�������������
		String where_chep_continue="";		//��Ƥ���������
		String where_chep_daozcpb_continue="";	//��װ��Ƥ��������
		String sql ="";
		
		int IntRow_num=0;
		
		if(((Visit) getPage().getVisit()).getLong9()>-1){
			
			if(((Visit) getPage().getVisit()).getLong2()==Locale.daozyf_feiylbb_id){
//				��װ�˷� 
				yunsdw_contion=" and c.yunsdw='"+((Visit) getPage().getVisit()).getString10()+"'";
			}else{
				
				yunsdw_contion=" and c.yunsdwb_id="+((Visit) getPage().getVisit()).getLong9();
			}
		}
		
		if(((Visit) getPage().getVisit()).getLong2()==Locale.daozyf_feiylbb_id){
//			��װ�˷� 
			
			strChepb="daozcpb";
		}
		
		try {
			
			strFinalArray = Jiesdcz.getFenzzfc(
					((Visit) getPage().getVisit()).getString13(), ",", 1000);	//�õ������ַ���
			
			StringBuffer sbcpid = new StringBuffer();
			
			if(strFinalArray!=null){
//				˵���з���
				where_fahb_continue="";
				for(int i=0;i<strFinalArray.length;i++){
					
					if(i==0){
						
						where_fahb_continue="f.id in ("+strFinalArray[i]+")";
					}else{
						
						where_fahb_continue+=" or f.id in ("+strFinalArray[i]+")";
					}
				}
				
				sql = "select c.id from fahb f,"+strChepb+" c		\n"
					+ " where f.id=c.fahb_id and ("+where_fahb_continue+")	\n"
					+ yunsdw_contion;
				
			}else{
				
				sql = "select c.id from fahb f,"+strChepb+" c		\n"
					+ " where f.id=c.fahb_id and f.id in ("
					+ ((Visit) getPage().getVisit()).getString13() + ")	\n"
					+ yunsdw_contion;
			}

			ResultSetList rsl = con.getResultSetList(sql);
			IntRow_num = rsl.getRows();
			while (rsl.next()) {

				sbcpid.append(rsl.getLong("id")).append(",");
			}
			sbcpid.deleteCharAt(sbcpid.length() - 1);
			
			// �ж�yunfdjb��danjcpb���Ƿ���ڼ�¼���������Hedbz=trueֱ�Ӹ��£����������Hedbz=false;�Ƚ����ݲ�������ٸ���״̬
			
			strFinalArray = null;
			strFinalArray = Jiesdcz.getFenzzfc(
					sbcpid.toString(), ",", 1000);	//�õ������ַ���
			
			
			if(strFinalArray!=null){
//				˵���з���
				where_chep_continue="";
				where_chep_daozcpb_continue="";
				for(int i=0;i<strFinalArray.length;i++){
					
					if(i==0){
						
						where_chep_continue=" danjcpb.chepb_id in ("+strFinalArray[i]+")";
						where_chep_daozcpb_continue=" id in ("+strFinalArray[i]+")";
					}else{
						
						where_chep_continue+=" or danjcpb.chepb_id in ("+strFinalArray[i]+")";
						where_chep_daozcpb_continue+=" or id in ("+strFinalArray[i]+")";
					}
				}
				
				sql = "select yunfdjb.id from yunfdjb,danjcpb where danjcpb.yunfdjb_id=yunfdjb.id 	\n"
					+ " and ("
					+ where_chep_continue
					+ ") and yunfdjb.feiylbb_id="
					+ Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
							.getLong2()) + "";
				
			}else{
				
				sql = "select yunfdjb.id from yunfdjb,danjcpb where danjcpb.yunfdjb_id=yunfdjb.id 	\n"
					+ " and danjcpb.chepb_id in ("
					+ sbcpid.toString()
					+ ") and yunfdjb.feiylbb_id="
					+ Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
							.getLong2()) + "";
			}

			rsl = con.getResultSetList(sql);
			
			if(IntRow_num!=rsl.getRows()){
//				˵����һ����û�н���danjcpb��yunfdjb�Ĳ������
				
				if(!where_chep_continue.equals("")
					||!where_fahb_continue.equals("")	
					){
					
					if(!where_fahb_continue.equals("")&&!where_chep_continue.equals("")){
						
						sql="select c.id from fahb f,"+strChepb+" c		\n"
							+ " where f.id=c.fahb_id and ("+where_fahb_continue+")	\n"
							+ yunsdw_contion 
							+ " minus	\n"
							+ "select danjcpb.chepb_id as id from yunfdjb,danjcpb where danjcpb.yunfdjb_id=yunfdjb.id 	\n"
							+ " and ("
							+ where_chep_continue
							+ ") and yunfdjb.feiylbb_id="
							+ Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
								.getLong2());
					}else if(!where_fahb_continue.equals("")){
						
						sql="select c.id from fahb f,"+strChepb+" c		\n"
							+ " where f.id=c.fahb_id and ("+where_fahb_continue+")	\n"
							+ yunsdw_contion 
							+ " minus	\n"
							+ "select danjcpb.chepb_id as id from yunfdjb,danjcpb where danjcpb.yunfdjb_id=yunfdjb.id 	\n"
							+ " and danjcpb.chepb_id in ("
							+ sbcpid.toString()
							+ ") and yunfdjb.feiylbb_id="
							+ Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
								.getLong2());
						
					}else if(!where_chep_continue.equals("")){
						
						sql="select c.id from fahb f,"+strChepb+" c		\n"
							+ " where f.id=c.fahb_id and f.id in ("
							+((Visit) getPage().getVisit()).getString13()+")	\n"
							+ yunsdw_contion 
							+ " minus	\n"
							+ "select danjcpb.chepb_id as id from yunfdjb,danjcpb where danjcpb.yunfdjb_id=yunfdjb.id 	\n"
							+ " and ("
							+ where_chep_continue
							+ ") and yunfdjb.feiylbb_id="
							+ Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
								.getLong2());
					}
				}else{
					
					sql="select c.id from fahb f,"+strChepb+" c		\n"
						+ " where f.id=c.fahb_id and f.id in ("
						+ ((Visit) getPage().getVisit()).getString13() + ")	\n"
						+ yunsdw_contion 
						+ " minus	\n"
						+ "select danjcpb.chepb_id as id from yunfdjb,danjcpb where danjcpb.yunfdjb_id=yunfdjb.id 	\n"
						+ " and danjcpb.chepb_id in ("
						+ sbcpid.toString()
						+ ") and yunfdjb.feiylbb_id="
						+ Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
								.getLong2());
				}
				
				rsl=con.getResultSetList(sql);
				
				if(rsl.getRows()>0){
					
					StringBuffer sb = new StringBuffer("begin		\n");
					String Yunfdjb_id = "0";
					int i=0;
					while(rsl.next()){
						i++;

						Yunfdjb_id = MainGlobal.getNewID(((Visit) getPage()
								.getVisit()).getLong1());
						sb
								.append(
										"insert into yunfdjb(id, danjbh, feiyb_id, biaoz, zongje, caozy, caozsj, beiz, feiylbb_id, ches)	\n")
								.append(" values		\n")
								.append(
										" ("
												+ Yunfdjb_id
												+ ", '', 0, 0, 0, '"
												+ ((Visit) getPage().getVisit())
														.getRenymc()
												+ "', sysdate, ")
								.append(
										"'����˶Ի�Ʊ�����˷ѽ���', "
												+ Jscz
														.Feiylbb_transition(((Visit) getPage()
																.getVisit())
																.getLong2())
												+ ", 1);	\n");

						sb
								.append(
										"insert into danjcpb(yunfdjb_id, chepb_id, yunfjsb_id, yansbhb_id, jifzl, id)		\n")
								.append(" values		\n").append(
										"("
												+ Yunfdjb_id
												+ ", "
												+ rsl.getString("id")
												+ ", 0, 0, 0, getnewid("
												+ ((Visit) getPage().getVisit())
														.getLong1() + "));	\n");
						
						if(i>=1000&&i%1000==0){
							sb.append(" end;\n");
							
							if(con.getUpdate(sb.toString())<0){
								
								Flag = false;
								return Flag;
							}else{
								
								sb.setLength(0);
								sb.append("begin \n");
							}
							
						}
					
					}
					sb.append("end;");
					if(sb.length()>13){
						
						if(con.getInsert(sb.toString())<0){
							
							Flag = false;
							return Flag;
						}
					}
				}
			}
				
			rsl.close();
			
			String Feiylb_condition="";	//Ϊ�˴�����Ʊ����ʱ��
//									�������˷ѺͿ����˷�һ��˶��˵�����£��ڸ���danjcpb��
//									yunfjsb_idʱ�޶���yunfdjb��feiylbb_idΪ�����˷ѵ�bug��
//									�����˸ñ��������((Visit) getPage().getVisit()).getLong2()Ϊ��Ʊ���㣨1����
//									���������ֵΪ �����˷� or �����˷�
			
			if(((Visit) getPage().getVisit())
					.getLong2()==Locale.liangpjs_feiylbb_id){
				
				Feiylb_condition=Locale.guotyf_feiylbb_id+","+Locale.kuangqyf_feiylbb_id;
			}else{
				
				Feiylb_condition=String.valueOf(Jscz.Feiylbb_transition(((Visit) getPage().getVisit())
						.getLong2()));
			}

			if(!where_chep_continue.equals("")){
				
				sql = "update danjcpb set yunfjsb_id="
					+ JiesyfbId
					+ " where ("+where_chep_continue+") \n"
					+ " and danjcpb.yunfdjb_id in	\n"
					+ " (select yunfdjb.id from yunfdjb,danjcpb where danjcpb.yunfdjb_id=yunfdjb.id	\n"
					+ "	and ("+where_chep_continue+") and yunfdjb.feiylbb_id in ("
					+ Feiylb_condition + ")) ";
				
			}else{
				
				sql = "update danjcpb set yunfjsb_id="
					+ JiesyfbId
					+ " where chepb_id in ("
					+ sbcpid.toString()
					+ ") 	\n"
					+ " and danjcpb.yunfdjb_id in	\n"
					+ " (select yunfdjb.id from yunfdjb,danjcpb where danjcpb.yunfdjb_id=yunfdjb.id	\n"
					+ "	and danjcpb.chepb_id in ("
					+ sbcpid.toString()
					+ ") and yunfdjb.feiylbb_id in ("
					+ Feiylb_condition + ")) ";
			}
			
			if (con.getUpdate(sql) > 0) {

				if(((Visit) getPage().getVisit()).getLong2()==Locale.daozyf_feiylbb_id){
//					��װ�˷� 
					
					if(!where_chep_daozcpb_continue.equals("")){
						
						sql = "update daozcpb set jiesb_id="+JiesyfbId+" where ("+where_chep_daozcpb_continue+")";
					}else{
						
						sql = "update daozcpb set jiesb_id="+JiesyfbId+" where id in ("+sbcpid.toString()+")";
					}
					
					
					if(con.getUpdate(sql)>0){
						
						Flag = true;
					}
				}else{
					
					Flag = true;
				}
				
			} 

		} catch (Exception e) {

			e.printStackTrace();
		}

		return Flag;
	}
	
	private boolean UpdateJiesb(long Jiesb_Id,JDBCcon con,String TableName) {

		String sql;
		try {
			sql = "update "+TableName+" set jiesrl="
					+ MainGlobal.Mjkg_to_kcalkg(
							((Dcbalancezbbean) getJieszbValues()
									.get(0))
									.getQnetar_js(), 0 ,
									((Dcbalancebean) getEditValues().get(0)).getMjtokcalxsclfs())
					+ "," + " jieslf="
					+ ((Dcbalancezbbean) getJieszbValues().get(0)).getStd_js()
					+ "," + " jiesrcrl="
					+ ((Dcbalancezbbean) getJieszbValues().get(0)).getQnetar_cf()
					+ "," + " meikxxb_id="
					+ MainGlobal.getTableId("meikxxb", "quanc", ((Dcbalancebean) getEditValues().get(0)).getMeikdw())
					+ ","+ " meikdwmc='"
					+ ((Dcbalancebean) getEditValues().get(0)).getMeikdw()
					+ "',hetj="
					+ ((Dcbalancebean) getEditValues().get(0)).getHetjg()
					+ ",fengsjj="
					+((Dcbalancebean) getEditValues().get(0)).getFengsjj()
					+",jiajqdj="
					+((Dcbalancebean) getEditValues().get(0)).getJiajqdj()
					+",jijlx="
					+((Dcbalancebean) getEditValues().get(0)).getJijlx()
					+",Yunfhsdj="
					+((Dcbalancebean) getEditValues().get(0)).getYunfjsdj_mk()
					+",hansyf="
					+(((Dcbalancebean) getEditValues().get(0)).getYunzfhj()==0?
							((Dcbalancebean) getEditValues().get(0)).getYunzfhj_mk():
							((Dcbalancebean) getEditValues().get(0)).getYunzfhj())	
					+",Buhsyf="
					+(((Dcbalancebean) getEditValues().get(0)).getBuhsyf()==0?
							((Dcbalancebean) getEditValues().get(0)).getBuhsyf_mk():
							((Dcbalancebean) getEditValues().get(0)).getBuhsyf())	
					+",yunfjsl="
					+((Dcbalancebean) getEditValues().get(0)).getYunfjsl_mk();
			
			if(((Dcbalancebean) getEditValues().get(0)).getJieslx()==Locale.meikjs_feiylbb_id){
				
				sql+= ",kuidjfyf="
					+((Dcbalancebean) getEditValues().get(0)).getKuidjfyf()
					+ ",kuidjfzf="
					+((Dcbalancebean) getEditValues().get(0)).getKuidjfzf();	
			}
			
			if(((Dcbalancebean) getEditValues().get(0)).getChaodOrKuid().equals("CD")){
//				����"����"
				sql+=",chaokdl="+((Dcbalancebean) getEditValues().get(0)).getChaokdl()
					+", chaokdlx='"+((Dcbalancebean) getEditValues().get(0)).getChaodOrKuid()+"'";
			}else if(((Dcbalancebean) getEditValues().get(0)).getChaodOrKuid().equals("KD")){
//				����"����"
				sql+=",chaokdl="+(-((Dcbalancebean) getEditValues().get(0)).getChaokdl())
					+", chaokdlx='"+((Dcbalancebean) getEditValues().get(0)).getChaodOrKuid()+"'";
			}
			
			sql+=" where id=" + Jiesb_Id;
			if (con.getUpdate(sql) >= 0) {

				return true;
			}
		
		} catch (Exception e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}

		return false;
	}
	
	public void getSelectData() {
		
		String fahqssj = "";	// ������ʼʱ��
		String fahjzsj = "";	// ������ֹʱ��
		String where_js = "";	// ú��˷��Ƿ�����SQL����
		
		JDBCcon con = new JDBCcon();
		String sql = "select fa.jiesdw_id, fa.fahqssj, fa.fahjzsj, fa.jieslx from jiesfab fa where fa.id = " + getJiesbmValue().getStrId();
		ResultSetList rsl = con.getResultSetList(sql);
		
		while (rsl.next()) { // ȡ�ý��㷽����Ϣ
			fahqssj = rsl.getDateString("fahqssj");
			fahjzsj = rsl.getDateString("fahjzsj");
			setJiesdw(rsl.getString("jiesdw_id"));
			setJieslx(rsl.getLong("jieslx"));
		}
		
		if (getJieslx() == Locale.guotyf_feiylbb_id) { // �����˷�
			
			if (getJiesztValue().getValue().equals("δ����")) {
				where_js = "                   and (djcp.yunfjsb_id = 0 or djcp.yunfjsb_id is null)\n";
			} else {
				where_js = "                   and (djcp.yunfjsb_id != 0)\n";;
			}
			
		} else { // ú�����
			
			if (getJiesztValue().getValue().equals("δ����")) {
				where_js = "fh.jiesb_id = 0\n";
			} else {
				where_js = "fh.jiesb_id != 0\n";
			}
			
		}
		
		if (getJieslx() == Locale.guotyf_feiylbb_id) { // �����˷�
			
			sql = 
				"select /*grouping(fa.yunsdwmc) ysdw, grouping(fa.meikxxb_id) mk, grouping(fa.fahb_id) fh,*/\n" +
				"       /*decode(grouping(fa.fahb_id), 1, '', max(fa.zhuangt)) zhuangt,*/\n" + 
				"       /*decode(grouping(fa.fahb_id), 1, '', max(fa.js_fahb_id)) js_fahb_id,*/\n" + 
				"       fa.fahb_id,\n" + 
				"       decode(grouping(fa.yunsdwmc), 1, '�ܼ�', fa.yunsdwmc) yunsdwb_id,\n" + 
				"       decode(grouping(fa.yunsdwmc) + grouping(fa.meikxxb_id), 1, '�ϼ�', fa.meikxxb_id) meikxxb_id,\n" + 
				"       decode(grouping(fa.yunsdwmc) + grouping(fa.meikxxb_id) + grouping(fa.fahb_id), 1, 'С��', 2, '', 3, '', max(fa.pinzb_id)) pinzb_id,\n" + 
				"       decode(grouping(fa.yunsdwmc) + grouping(fa.meikxxb_id) + grouping(fa.fahb_id), 1, '', 2, '', 3, '', max(fa.jihkjb_id)) jihkjb_id,\n" + 
				"       decode(grouping(fa.yunsdwmc) + grouping(fa.meikxxb_id) + grouping(fa.fahb_id), 1, to_date(null), 2, '', 3, '', max(fa.fahrq)) fahrq,\n" + 
				"       decode(grouping(fa.yunsdwmc) + grouping(fa.meikxxb_id) + grouping(fa.fahb_id), 1, to_date(null), 2, '', 3, '', max(fa.daohrq)) daohrq,\n" + 
				"       decode(grouping(fa.yunsdwmc) + grouping(fa.meikxxb_id) + grouping(fa.fahb_id), 1, '', 2, '', 3, '', max(fa.hetb_id)) hetb_id,\n" + 
				"       sum(fa.jieskd) jieskd,\n" + 
				"       sum(fa.bukyqyf) bukyqyf,\n" + 
				"       sum(fa.maoz) maoz,\n" + 
				"       sum(fa.piz) piz,\n" + 
				"       sum(fa.biaoz) biaoz,\n" + 
				"       sum(fa.yingd) yingd,\n" + 
				"       sum(fa.yingk) yingk,\n" + 
				"       sum(fa.yuns) yuns,\n" + 
				"       sum(fa.koud) koud,\n" + 
				"       sum(fa.ches) ches\n" + 
				"    from (\n" + 
				"        select /*decode(grouping(xx.fahb_id), 1, '', case when max(js.fahb_id) is null then '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��' else '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��' end) as zhuangt,*/\n" + 
				"               decode(grouping(xx.yunsdwmc) + grouping(xx.meikmc) + grouping(xx.fahb_id), 1, '', 2, '', 3, '', max(js.fahb_id)) js_fahb_id,\n" + 
				"               /*grouping(xx.yunsdwmc) ysdw, grouping(xx.meikmc) mk, grouping(xx.fahb_id) fhid,*/\n" + 
				"               xx.fahb_id,\n" + 
				"               max(xx.yunsdwb_id) yunsdwb_id,\n" + 
				"               decode(grouping(xx.yunsdwmc), 1, '�ܼ�', xx.yunsdwmc) yunsdwmc,\n" + 
				"               decode(grouping(xx.yunsdwmc) + grouping(xx.meikmc), 1, '�ϼ�', xx.meikmc) meikxxb_id,\n" + 
				"               decode(grouping(xx.yunsdwmc) + grouping(xx.meikmc) + grouping(xx.fahb_id), 1, 'С��', 2, '', 3, '', max(xx.pinzb_id)) pinzb_id,\n" + 
				"               decode(grouping(xx.yunsdwmc) + grouping(xx.meikmc) + grouping(xx.fahb_id), 1, '', 2, '', 3, '', max(xx.jihkjb_id)) jihkjb_id,\n" + 
				"               decode(grouping(xx.yunsdwmc) + grouping(xx.meikmc) + grouping(xx.fahb_id), 1, to_date(null), 2, '', 3, '', max(xx.fahrq)) fahrq,\n" + 
				"               decode(grouping(xx.yunsdwmc) + grouping(xx.meikmc) + grouping(xx.fahb_id), 1, to_date(null), 2, '', 3, '', max(xx.daohrq)) daohrq,\n" + 
				"               decode(grouping(xx.yunsdwmc) + grouping(xx.meikmc) + grouping(xx.fahb_id), 1, '', 2, '', 3, '', max(js.hetb_id)) hetb_id,\n" + 
				"               max(js.jieskd) jieskd,\n" + 
				"               max(js.bukyqyf) bukyqyf,\n" + 
				"               sum(xx.maoz) maoz,\n" + 
				"               sum(xx.piz) piz,\n" + 
				"               sum(xx.biaoz) biaoz,\n" + 
				"               sum(xx.yingd) yingd,\n" + 
				"               sum(xx.yingk) yingk,\n" + 
				"               sum(xx.yuns) yuns,\n" + 
				"               sum(xx.koud) koud,\n" + 
				"               count(xx.chepb_id) ches\n" + 
				"          from (select fh.id fahb_id,\n" + 
				"                       ysdw.id yunsdwb_id,\n" + 
				"                       ysdw.mingc yunsdwmc,\n" + 
				"                       mk.mingc meikmc,\n" + 
				"                       pz.mingc pinzb_id,\n" + 
				"                       kj.mingc jihkjb_id,\n" + 
				"                       fh.fahrq,\n" + 
				"                       fh.daohrq,\n" + 
				"                       cp.maoz,\n" + 
				"                       cp.piz,\n" + 
				"                       cp.biaoz,\n" + 
				"                       cp.yingd,\n" + 
				"                       cp.yingk,\n" + 
				"                       cp.yuns,\n" + 
				"                       cp.koud,\n" + 
				"                       cp.id chepb_id\n" + 
				"                  from fahb    fh,\n" + 
				"                       gongysb gys,\n" + 
				"                       meikxxb mk,\n" + 
				"                       pinzb   pz,\n" + 
				"                       chezxxb faz,\n" + 
				"                       chezxxb daoz,\n" + 
				"                       jihkjb  kj,\n" + 
				"                       zhilb   zl,\n" + 
				"                       chepb   cp,\n" + 
				"                       danjcpb djcp,\n" + 
				"                       yunsdwb ysdw\n" + 
				"                 where fh.fahrq >= to_date('"+ fahqssj +"', 'yyyy-mm-dd')\n" + 
				"                   and fh.fahrq <= to_date('"+ fahjzsj +"', 'yyyy-mm-dd')\n" + 
				"                   and fh.liucztb_id = 1\n" + 
				"                   and fh.gongysb_id = gys.id\n" + 
				"                   and fh.meikxxb_id = mk.id\n" + 
				"                   and fh.pinzb_id = pz.id\n" + 
				"                   and fh.faz_id = faz.id\n" + 
				"                   and fh.daoz_id = daoz.id\n" + 
				"                   and fh.jihkjb_id = kj.id\n" + 
				"                   and fh.zhilb_id = zl.id\n" + 
				"                   and cp.fahb_id = fh.id\n" + 
				"                   and djcp.chepb_id(+) = cp.id\n" + 
									where_js + 
				"                   and cp.yunsdwb_id = ysdw.id\n" + 
				"                 order by fh.id, ysdw.mingc, fh.meikxxb_id) xx,\n" + 
				"               (select gl.fahb_id, gl.yunsdwb_id, gl.jieskd, gl.bukyqyf, ysht.hetbh hetb_id\n" + 
				"                  from jiesfaglb gl, hetys ysht\n" + 
				"                 where gl.jiesfab_id = "+ getJiesbmValue().getStrId() +"\n" + 
				"                   and gl.hetb_id = ysht.id(+)\n" + 
				"                 order by gl.fahb_id) js\n" + 
				"         where xx.fahb_id = js.fahb_id\n" + 
				"           and xx.yunsdwb_id = js.yunsdwb_id\n" + 
				"         group by rollup(xx.yunsdwmc, xx.meikmc, xx.fahb_id)\n" + 
				"         having not grouping(xx.fahb_id) = 1) fa\n" + 
				"group by rollup(fa.yunsdwmc, fa.meikxxb_id, fa.fahb_id)";

			
		} else { // ú��
			
			sql = 
				"select fh.id fahb_id,\n" +
				"       /*grouping(gys.mingc) gmc, grouping(mk.mingc) km, grouping(fh.id) gid,*/\n" + 
				"       decode(grouping(gys.mingc), 1, '�ܼ�', gys.mingc) as gongysb_id,\n" + 
				"       decode(grouping(gys.mingc) + grouping(mk.mingc), 1, '�ϼ�', mk.mingc) as meikxxb_id,\n" + 
				"       decode(grouping(mk.mingc) + grouping(fh.id), 1, 'С��', 2, '', max(pz.mingc)) as pinzb_id,\n" + 
				"       decode(grouping(gys.mingc) + grouping(mk.mingc) + grouping(fh.id), 1, '', 2, '', 3, '', max(faz.mingc)) as faz_id,\n" + 
				"       decode(grouping(gys.mingc) + grouping(mk.mingc) + grouping(fh.id), 1, '', 2, '', 3, '', max(daoz.mingc)) as daoz_id,\n" + 
				"       decode(grouping(gys.mingc) + grouping(mk.mingc) + grouping(fh.id), 1, '', 2, '', 3, '', max(kj.mingc)) as jihkjb_id,\n" + 
				"       decode(grouping(gys.mingc) + grouping(mk.mingc) + grouping(fh.id), 1, to_date(null), 2, '', 3, '', max(fh.fahrq)) as fahrq,\n" + 
				"       decode(grouping(gys.mingc) + grouping(mk.mingc) + grouping(fh.id), 1, to_date(null), 2, '', 3, '', max(fh.daohrq)) as daohrq,\n" + 
				"       decode(grouping(gys.mingc) + grouping(mk.mingc) + grouping(fh.id), 1, '', 2, '', 3, '', max(fagl.hetbh)) as hetb_id,\n" + 
				"       sum(fagl.jieskd) as jieskd,\n" + 
				"       sum(fagl.bukyqjk) as bukyqjk,\n" +
				"       sum(fh.maoz) maoz,\n" + 
				"       sum(fh.piz) piz,\n" + 
				"       sum(fh.jingz) jingz,\n" + 
				"       sum(fh.biaoz) biaoz,\n" + 
				"       sum(fh.yingd) yingd,\n" + 
				"       sum(fh.yingk) yingk,\n" + 
				"       sum(fh.yuns) yuns,\n" + 
				"       sum(fh.koud) koud,\n" + 
				"       sum(fh.ches) ches,\n" + 
				"       sum(fh.laimsl) laimsl,\n" + 
				"       round_new(sum(zl.qnet_ar * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 3) qnet_ar,\n" + 
				"       round_new(sum(zl.aar * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) aar,\n" + 
				"       round_new(sum(zl.ad * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) ad,\n" + 
				"       round_new(sum(zl.vdaf * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) vdaf,\n" + 
				"       round_new(sum(zl.mt * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) mt,\n" + 
				"       round_new(sum(zl.stad * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) stad,\n" + 
				"       round_new(sum(zl.aad * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) aad,\n" + 
				"       round_new(sum(zl.mad * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) mad,\n" + 
				"       round_new(sum(zl.qbad * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) qbad,\n" + 
				"       round_new(sum(zl.had * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) had,\n" + 
				"       round_new(sum(zl.vad * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) vad,\n" + 
				"       round_new(sum(zl.fcad * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) fcad,\n" + 
				"       round_new(sum(zl.std * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) std,\n" + 
				"       round_new(sum(zl.qgrad * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) qgrad,\n" + 
				"       round_new(sum(zl.hdaf * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) hdaf,\n" + 
				"       round_new(sum(zl.qgrad_daf * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) qgrad_daf,\n" + 
				"       round_new(sum(zl.sdaf * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) sdaf,\n" + 
				"       round_new(sum(zl.var * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) var,\n" + 
				"       round_new(sum(zl.t1 * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) t1,\n" + 
				"       round_new(sum(zl.t2 * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) t2,\n" + 
				"       round_new(sum(zl.t3 * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) t3,\n" + 
				"       round_new(sum(zl.t4 * fh.laimsl) / decode(sum(fh.laimsl), 0, 1, sum(fh.laimsl)), 2) t4\n" + 
				"  from fahb    fh,\n" + 
				"       gongysb gys,\n" + 
				"       meikxxb mk,\n" + 
				"       pinzb   pz,\n" + 
				"       chezxxb faz,\n" + 
				"       chezxxb daoz,\n" + 
				"       jihkjb  kj,\n" + 
				"       hetb    ht,\n" + 
				"       zhilb   zl,\n" + 
				"       (select gl.fahb_id, gl.jieskd, gl.bukyqjk, ht.hetbh\n" +
				"          from jiesfaglb gl, hetb ht\n" + 
				"         where gl.jiesfab_id = "+ getJiesbmValue().getStrId() +"\n" + 
				"           and gl.hetb_id = ht.id(+)" +") fagl\n" + 
				" where " + where_js + 
				"   and fh.gongysb_id = gys.id\n" + 
				"   and fh.meikxxb_id = mk.id\n" + 
				"   and fh.pinzb_id = pz.id\n" + 
				"   and fh.faz_id = faz.id\n" + 
				"   and fh.daoz_id = daoz.id\n" + 
				"   and fh.jihkjb_id = kj.id\n" + 
				"   and fh.hetb_id = ht.id(+)\n" + 
				"   and fh.zhilb_id = zl.id\n" + 
				"   and fh.id = fagl.fahb_id\n" + 
				" group by rollup (gys.mingc, mk.mingc, fh.id)";
		}
		
		rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setDefaultsortable(false);
		
		egu.getColumn("fahb_id").setHidden(true);
		egu.getColumn("fahb_id").setHeader("FAHB_ID");
		
		if (getJieslx() == Locale.guotyf_feiylbb_id) {
			egu.getColumn("yunsdwb_id").setHeader("���䵥λ");
			egu.getColumn("yunsdwb_id").setEditor(new ComboBox());
			egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId, 
					new IDropDownModel("select ys.id, ys.mingc from yunsdwb ys where ys.diancxxb_id = "+ getJiesdw() +" order by ys.mingc"));
		} else {
			egu.getColumn("gongysb_id").setHeader("��Ӧ��");
		}
		
		egu.getColumn("meikxxb_id").setHeader("ú��");
		egu.getColumn("pinzb_id").setHeader("Ʒ��");
		egu.getColumn("pinzb_id").setWidth(60);
		egu.getColumn("jihkjb_id").setHeader("�ƻ��ھ�");
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("fahrq").setHeader("��������");
		egu.getColumn("fahrq").setWidth(80);
		egu.getColumn("daohrq").setHeader("��������");
		egu.getColumn("daohrq").setWidth(80);
		egu.getColumn("hetb_id").setHeader("��ͬ���");
		egu.getColumn("hetb_id").setWidth(160);
		egu.getColumn("jieskd").setHeader("����۶�");
		egu.getColumn("jieskd").setWidth(60);
		
		if (getJieslx() == Locale.guotyf_feiylbb_id) {
			egu.getColumn("bukyqyf").setHeader("������ǰ�˷�");
			egu.getColumn("bukyqyf").setWidth(90);
		} else {
			egu.getColumn("faz_id").setHeader("��վ");
			egu.getColumn("faz_id").setWidth(90);
			egu.getColumn("daoz_id").setHeader("��վ");
			egu.getColumn("daoz_id").setWidth(90);
			egu.getColumn("bukyqjk").setHeader("������ǰ�ۿ�");
			egu.getColumn("bukyqjk").setWidth(90);
		}
		
		egu.getColumn("maoz").setHeader("ë��");
		egu.getColumn("maoz").setWidth(70);
		egu.getColumn("piz").setHeader("Ƥ��");
		egu.getColumn("piz").setWidth(70);
		egu.getColumn("biaoz").setHeader("Ʊ��");
		egu.getColumn("biaoz").setWidth(70);
		egu.getColumn("yingd").setHeader("ӯ��");
		egu.getColumn("yingd").setWidth(50);
		egu.getColumn("yingk").setHeader("ӯ��");
		egu.getColumn("yingk").setWidth(50);
		egu.getColumn("yuns").setHeader("����");
		egu.getColumn("yuns").setWidth(50);
		egu.getColumn("koud").setHeader("�۶�");
		egu.getColumn("koud").setWidth(50);
		egu.getColumn("ches").setHeader("����");
		egu.getColumn("ches").setWidth(50);
		
		if (getJieslx() == Locale.meikjs_feiylbb_id ) {
			egu.getColumn("jingz").setHeader("����");
			egu.getColumn("jingz").setWidth(70);
			egu.getColumn("laimsl").setHeader("��ú����");
			egu.getColumn("qnet_ar").setHeader("QNET_AR");
			egu.getColumn("qnet_ar").setWidth(60);
			egu.getColumn("aar").setHeader("AAR");
			egu.getColumn("aar").setWidth(50);
			egu.getColumn("ad").setHeader("AD");
			egu.getColumn("ad").setWidth(50);
			egu.getColumn("vdaf").setHeader("VDAF");
			egu.getColumn("vdaf").setWidth(50);
			egu.getColumn("mt").setHeader("MT");
			egu.getColumn("mt").setWidth(50);
			egu.getColumn("stad").setHeader("STAD");
			egu.getColumn("stad").setWidth(50);
			egu.getColumn("aad").setHeader("AAD");
			egu.getColumn("aad").setWidth(50);
			egu.getColumn("mad").setHeader("MAD");
			egu.getColumn("mad").setWidth(50);
			egu.getColumn("qbad").setHeader("QBAD");
			egu.getColumn("qbad").setWidth(50);
			egu.getColumn("had").setHeader("HAD");
			egu.getColumn("had").setWidth(50);
			egu.getColumn("vad").setHeader("VAD");
			egu.getColumn("vad").setWidth(50);
			egu.getColumn("fcad").setHeader("FACD");
			egu.getColumn("fcad").setWidth(50);
			egu.getColumn("fcad").setHidden(true);
			egu.getColumn("std").setHeader("STD");
			egu.getColumn("std").setWidth(50);
			egu.getColumn("qgrad").setHeader("QGRAD");
			egu.getColumn("qgrad").setWidth(50);
			egu.getColumn("qgrad").setHidden(true);
			egu.getColumn("hdaf").setHeader("HDAF");
			egu.getColumn("hdaf").setWidth(50);
			egu.getColumn("hdaf").setHidden(true);
			egu.getColumn("qgrad_daf").setHeader("QGRAD_DAF");
			egu.getColumn("qgrad_daf").setWidth(50);
			egu.getColumn("qgrad_daf").setHidden(true);
			egu.getColumn("sdaf").setHeader("SDAF");
			egu.getColumn("sdaf").setWidth(50);
			egu.getColumn("sdaf").setHidden(true);
			egu.getColumn("var").setHeader("VAR");
			egu.getColumn("var").setWidth(50);
			egu.getColumn("var").setHidden(true);
			egu.getColumn("t1").setHeader("T1");
			egu.getColumn("t1").setWidth(50);
			egu.getColumn("t1").setHidden(true);
			egu.getColumn("t2").setHeader("T2");
			egu.getColumn("t2").setWidth(50);
			egu.getColumn("t2").setHidden(true);
			egu.getColumn("t3").setHeader("T3");
			egu.getColumn("t3").setWidth(50);
			egu.getColumn("t3").setHidden(true);
			egu.getColumn("t4").setHeader("T4");
			egu.getColumn("t4").setWidth(50);
			egu.getColumn("t4").setHidden(true);
		}
		
//		egu.addTbarText("�������ͣ�");
//		ComboBox comb = new ComboBox();
//		comb.setWidth(120);
//		comb.setTransform("Jieslx");
//		comb.setId("jieslx");
//		comb.setLazyRender(true);
//		comb.setEditable(false);
//		egu.addToolbarItem(comb.getScript());
////		egu.addOtherScript("jieslx.on('select',function(){document.forms[0].submit();});");
//		egu.addTbarText("-");
		
		egu.addTbarText("����״̬��");
		ComboBox comb_zt = new ComboBox();
		comb_zt.setWidth(100);
		comb_zt.setTransform("Jieszt");
		comb_zt.setId("jieszt");
		comb_zt.setLazyRender(true);
		comb_zt.setEditable(false);
		egu.addToolbarItem(comb_zt.getScript());
		egu.addOtherScript("jieszt.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		egu.addTbarText("���㷽����ţ�");
		ComboBox comb_bm = new ComboBox();
		comb_bm.setWidth(120);
		comb_bm.setTransform("Jiesbm");
		comb_bm.setId("jiesbm");
		comb_bm.setLazyRender(true);
		comb_bm.setEditable(false);
		egu.addToolbarItem(comb_bm.getScript());
		egu.addOtherScript("jiesbm.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		String hander = 
			"function(){\n" +
			"    if (document.getElementById('jiesbm').value == -1) {\n" + 
			"        Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ�����㷽����ţ�');\n" + 
			"        return;\n" + 
			"    }\n" + 
			"    document.getElementById('RefreshButton').click();\n" + 
			"}";
		GridButton gbt = new GridButton("ˢ��", hander);
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		
		if (getJiesztValue().getValue().equals("δ����")) {
			
			String condition = 
				"function(){\n" +
				"    if (document.getElementById('jiesbm').value == -1) {\n" + 
				"        Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ�����㷽����ţ�');\n" + 
				"        return;\n" + 
				"    }\n" + 
				"    document.getElementById('JiesButton').click();\n" + 
				"    Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...'," +
				"    width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n" +
				"}";
			GridButton jies = new GridButton("����", condition, SysConstant.Btn_Icon_SelSubmit);
			egu.addTbarBtn(jies);
			
		} else {
			
			String condition = 
				"function(){\n" +
				"    if (document.getElementById('jiesbm').value == -1) {\n" + 
				"        Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ�����㷽����ţ�');\n" + 
				"        return;\n" + 
				"    }\n" + 
				"    document.getElementById('deleteButton').click();\n" + 
				"    Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...'," +
				"    width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n" +
				"}";
			GridButton jies = new GridButton("ɾ��", condition, SysConstant.Btn_Icon_Delete);
			egu.addTbarBtn(jies);
			
		}
		
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(0);
		setExtGrid(egu);
		rsl.close();
		con.Close();
	}
	
	private boolean UpdateFahb_Jiesbid(long JiesbId,JDBCcon con) {
		// ���·�����jiesb_id
		boolean Flag = false;
		String sql = "update fahb set jiesb_id=" + JiesbId
				+ " where id in ("
				+ ((Visit) getPage().getVisit()).getString13() + ")";
		if (con.getUpdate(sql) >= 0) {

			Flag = true;
		} 
		return Flag;
	}
	
	private long GetMeikjsb_id(long meikjsb_id) throws SQLException{
//		�����˷�ʱҪ�ͽ������а󶨣�������jiesyfb.diancjsmkb_idΪjiesb.id
//		ԭ������˷ѽ���ķ�������һ��ú����㣬�Ǿ͹�����ú����㡣�������ú����㣬�����й�����
		long Jiesb_id=meikjsb_id;
		if(meikjsb_id>0){
//			�������Ʊ���㣬meikjsb_id>0
			
		}else{
			
			JDBCcon con=new JDBCcon();
			String sql="select distinct nvl(jiesb_id,0) as jiesb_id from fahb where id in ("+((Visit) getPage().getVisit()).getString13()+")";
			ResultSetList rsl=con.getResultSetList(sql);
			if(rsl.getRows()>1){
//				˵����һ���˷ѽ����Ӧ���ú�����˴�����ú����й���
				
			}else if(rsl.getRows()==1){
//				˵���Ƕ�Ӧһ��ú�����
				Jiesb_id=rsl.getLong("jiesb_id");
			}
			rsl.close();
			con.Close();
		}
		
		return Jiesb_id;
	}
	
	private long getProperId(IPropertySelectionModel _selectModel, String value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();

		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getValue().equals(
					value)) {
				return ((IDropDownBean) _selectModel.getOption(i)).getId();
			}
		}
		return -1;
	}
	
	private String FormatDate(Date _date) {
		if (_date == null) {
			return DateUtil.Formatdate("yyyy-MM-dd", new Date());
		}
		return DateUtil.Formatdate("yyyy-MM-dd", _date);
	}
	
	private String getDiancjsbs(long diancxxb_id) {

		JDBCcon con = new JDBCcon();
		String sql = "", diancjsbs = "";

		try {

			sql = "select JIESBDCBS from diancxxb where id=" + diancxxb_id;
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {

				diancjsbs = rs.getString("JIESBDCBS") + "-";
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		return diancjsbs;
	}
	
//	ú�˰���۱���С��λ
	public int getMeikhsdjblxsw(){
		
		return ((Visit) getPage().getVisit()).getInt1();
	}
	
	public void setMeikhsdjblxsw(int value){
		
		((Visit) getPage().getVisit()).setInt1(value);
	}
	
//	�˷Ѻ�˰���۱���С��λ
	public int getYunfhsdjblxsw(){
		
		return ((Visit) getPage().getVisit()).getInt2();
	}
	
	public void setYunfhsdjblxsw(int value){
		
		((Visit) getPage().getVisit()).setInt2(value);
	}
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	public String getGridScript() {
		return getExtGrid().getGridScript();
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setProSelectionModel2(null); // ����״̬������
			visit.setDropDownBean2(null);
			visit.setProSelectionModel3(null); // ��������������
			visit.setDropDownBean3(null);
			visit.setProSelectionModel4(null); // ���㷽�����������
			visit.setDropDownBean4(null);
		}
		if (visit.getboolean7()) {
			getJiesbmModels();
			visit.setboolean7(false);
		}
		getSelectData();
	}
}