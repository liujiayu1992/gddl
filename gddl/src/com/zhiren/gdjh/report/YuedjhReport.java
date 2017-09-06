package com.zhiren.gdjh.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.webservice.InterCom_dt;
/*
 * ���ߣ����
 * ʱ�䣺2012-10-19
 * �����������ƻ��ھ���
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-11-14
 * �������������ӵ�λ����
 */
/*
 * ���ߣ� ��ʤ��
 * ʱ�䣺2013-03-01
 * �����������ۺ͵�����ú���ۺ�˰�Ͳ���˰������ú���� �������������
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-04-10
 * ���������ӳ�������ύ���ܡ�
 * 		���Ӷ��ύ�����Ƿ�ʹ�ó���������̽����жϡ�
 * 		�������˹��ܣ�������ʱֱ�ӻ��˳������̺͹�˾�ύ���̡�
 */
public class YuedjhReport extends BasePage implements PageValidateListener {

	private String msg = "";

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
		_pageLink = "";
	}


	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _TijiaoChick = false;

	public void TijiaoButton(IRequestCycle cycle) {
		_TijiaoChick = true;
	}
	
	private boolean _HuitChick = false;

	public void HuitButton(IRequestCycle cycle) {
		_HuitChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
		if(_TijiaoChick){
			_TijiaoChick=false;
			Tij();
		}
		if(_HuitChick){
			_HuitChick=false;
			Huit();
		}
	}
	
	private String UPDZhuangtSQL(String zhuangt){
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
		String curdate = "to_date('" + intyear + "-" + intMonth+ "-01','yyyy-mm-dd')";
		
		String diancxxb_id=this.getTreeid();
		
		String liuczt="";
		if(zhuangt.equals("0")&& MainGlobal.getXitxx_item("�ƻ�", "���������������",diancxxb_id, "��").equals("��")){
			liuczt= ", CG.LIUCZTB_ID=0 ";
		}
		
		String sqlChk="BEGIN\n" +
			"  UPDATE YUEDJH_CAIG CG\n" + 
			"     SET CG.ZHUANGT = "+zhuangt+"\n" + 
			"   WHERE CG.RIQ = "+curdate+"\n" + 
			"     AND CG.DIANCXXB_ID =  "+diancxxb_id+";\n" + 
			"  UPDATE YUEDJH_ZHIB CG\n" + 
			"     SET CG.ZHUANGT = "+zhuangt+" " + liuczt+
			"   WHERE CG.RIQ = "+curdate+"\n" + 
			"     AND CG.DIANCXXB_ID =  "+diancxxb_id+";\n" + 
			"END;";
		return sqlChk;
	}
	
	public void Tij(){
		JDBCcon con = new JDBCcon();

		if (MainGlobal.getXitxx_item("�ƻ�", "�Ƿ��������������","0", "��").equals("��")) {
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
			String curdate = "to_date('" + intyear + "-" + intMonth+ "-01','yyyy-mm-dd')";
			
			String diancxxb_id=this.getTreeid();
			
//			ȡ��ָ��ID
			String sql="  select id from YUEDJH_ZHIB CG\n" + 
			"   WHERE CG.RIQ = "+curdate+"\n" + 
			"     AND CG.DIANCXXB_ID =  "+diancxxb_id;
			ResultSetList rsl = new ResultSetList();
			rsl= con.getResultSetList(sql);
			long ID=0;
			while(rsl.next()){
				ID=rsl.getLong("ID");
			}
//			ȡ������ID
			long liucb_id=0;
			sql="SELECT LC.ID  FROM LIUCB LC, LIUCLBB LB  WHERE LC.LIUCLBB_ID = LB.ID AND LB.MINGC = '�ƻ�'";
			rsl= con.getResultSetList(sql);
			while(rsl.next()){
				liucb_id=rsl.getLong("ID");
			}
			rsl.close();
//			�����������
			Liuc.tij("YUEDJH_ZHIB", ID, ((Visit) getPage().getVisit()).getRenyID(), "", liucb_id);
			setMsg("�ύ�������");
		}else{
			con.getUpdate(UPDZhuangtSQL("1"));
			setMsg("�ύ�������");
		}
		
		con.Close();
	}
	
//	���˷���
	public void Huit() {
		JDBCcon con = new JDBCcon();
		String sql=UPDZhuangtSQL("0");

//		ʹ�õ糧�����б��ػ��˵��ж�
		String diancxxb_id = getTreeid();
		if (MainGlobal.getXitxx_item("�±��ϴ�", "�Ƿ������ػ���",diancxxb_id, "��").equals("��")) {
			// ����Ǳ��ػ��˵Ļ� ֱ���ڱ��ؿ���������ύ״̬��
			int num = con.getUpdate(sql);
			if (num != -1) {
				setMsg("�������ݻ��˳ɹ���");
			} else {
				setMsg("�������ݻ���ʧ�ܣ�");
			}
			con.Close();
		} else {
			// �����Զ�̻��˵Ļ����ȸ���Զ�̻���״̬���ٸ��±��ػ���״̬��
			InterCom_dt dt = new InterCom_dt();
			String[] sqls = new String[] { sql };
			String[] answer = dt.sqlExe(diancxxb_id, sqls, true);
			if (answer[0].equals("true")) {
				int num = con.getUpdate(sql);
				if (num == -1) {
					setMsg("�������ݻ��˷����쳣��");
				} else {
					setMsg("���ݻ��˳ɹ���");
				}
			} else {
				setMsg("�������ݷ����쳣��");
			}
		}
	}

	// ����չʾ
	public String getPrintTable() {
		return getYuedjh_caig()+getZhib();
	}

	private String getYuedjh_caig() {
		_CurrentPage = 1;
		_AllPages = 1;

		JDBCcon cn = new JDBCcon();

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
		// �����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String curdate = "to_date('" + intyear + "-" + intMonth+ "-01','yyyy-mm-dd')";

		String strSQL =		"SELECT\n" +
						"GONGYSB_ID,\n" + 
						"MEIKXXB_ID,\n" + 
						"JIHKJB_ID,\n" + 
						"PINZB_ID,\n" + 
						"FAZ_ID,\n" + 
						"JIH_SL,\n" + 
						"JIH_REZ,\n" + 
						"JIH_LIUF,\n" + 
						"JIH_HFF,\n" + 
						"JIH_MEIJ,\n" + 
						"JIH_MEIJBHS,\n" + 
						"JIH_YUNJ,\n" + 
						"JIH_YUNJBHS,\n" + 
						"JIH_ZAF,\n" + 
						"JIH_ZAFBHS,\n" + 
						"(JIH_MEIJ+JIH_YUNJ+JIH_ZAF) JIH_DAOCJ,\n" + 
						"(JIH_MEIJBHS+JIH_YUNJBHS+JIH_ZAFBHS) JIH_DAOCJBHS,\n" + 
						" DECODE(JIH_REZ, 0,0, ROUND((JIH_MEIJ+JIH_YUNJ+JIH_ZAF) * 29.271 / JIH_REZ, 2)) JIH_DAOCBMDJ,\n" + 
						" DECODE(JIH_REZ,0,0,ROUND((JIH_MEIJBHS+JIH_YUNJBHS+JIH_ZAFBHS) *29.271 / JIH_REZ,2)) JIH_DAOCBMDJBHS\n" + 
						"FROM(SELECT decode(grouping(gys.mingc),1,'<b>�ܼ�</b>',GYS.MINGC)        GONGYSB_ID,\n" +
		"       MK.MINGC        MEIKXXB_ID, J.MINGC JIHKJB_ID,\n" + 
		"       PZ.MINGC        PINZB_ID,\n" + 
		"       CZ.MINGC        FAZ_ID,\n" + 
		"       sum(JIH_SL) JIH_SL,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_REZ*JIH_SL) /sum(JIH_SL)),2) JIH_REZ,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_LIUF*JIH_SL) /sum(JIH_SL)),2) JIH_LIUF,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_HFF*JIH_SL) /sum(JIH_SL)),2)JIH_HFF ,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_MEIJ*JIH_SL) /sum(JIH_SL)),2) JIH_MEIJ,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_MEIJBHS*JIH_SL) /sum(JIH_SL)),2) JIH_MEIJBHS,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_YUNJ*JIH_SL) /sum(JIH_SL)),2) JIH_YUNJ,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_YUNJBHS*JIH_SL) /sum(JIH_SL)),2)JIH_YUNJBHS ,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_ZAF*JIH_SL) /sum(JIH_SL)),2)JIH_ZAF ,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_ZAFBHS*JIH_SL) /sum(JIH_SL)),2) JIH_ZAFBHS,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_DAOCJ*JIH_SL) /sum(JIH_SL)),2) JIH_DAOCJ,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_DAOCJBHS*JIH_SL) /sum(JIH_SL)),2) JIH_DAOCJBHS,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_DAOCBMDJ*JIH_SL) /sum(JIH_SL)),2)JIH_DAOCBMDJ ,\n" + 
		"       round(decode(sum(JIH_SL),0,0,sum(JIH_DAOCBMDJBHS*JIH_SL) /sum(JIH_SL)),2) JIH_DAOCBMDJBHS\n" + 
		"  FROM YUEDJH_CAIG CG, GONGYSB GYS, MEIKXXB MK, PINZB PZ, CHEZXXB CZ,JIHKJB J\n" + 
		" WHERE CG.GONGYSB_ID = GYS.ID AND CG.JIHKJB_ID=J.ID\n" + 
		"   AND CG.MEIKXXB_ID = MK.ID\n" + 
		"   AND CG.PINZB_ID = PZ.ID\n" + 
		"   AND CG.FAZ_ID = CZ.ID\n" + 
		"   AND DIANCXXB_ID = "+this.getTreeid()+"\n" + 
		"   AND RIQ = "+curdate+"\n" + 
		"   GROUP BY ROLLUP ((GYS.MINGC,MK.MINGC,J.MINGC,PZ.MINGC,CZ.MINGC)))";
		
		java.sql.ResultSet rs = cn.getResultSet(strSQL);
		Table tb = new Table(rs, 3, 0, 5);
		rt.setBody(tb);

		ArrHeader = new String[3][19];
		ArrHeader[0] = (new String[] { "������λ","ú��λ","�ƻ��ھ�", "Ʒ��","��վ","�ɹ���","��ֵ","���","�ӷ���","�����","�����<br>(����˰)","�˷�","�˷�<br>(����˰)","�ӷ�","�ӷ�<br>(����˰)","������","������<br>(����˰)","����<br>��ú����","����<br>��ú����<br>(����˰)"});
		ArrHeader[1] = (new String[] { "������λ", "ú��λ","�ƻ��ھ�", "Ʒ��", "��վ", "��","�׽�/ǧ��","%","%","Ԫ/��","Ԫ/��","Ԫ/��","Ԫ/��","Ԫ/��","Ԫ/��","Ԫ/��","Ԫ/��","Ԫ/��","Ԫ/��"});
		ArrHeader[2] = (new String[] { "��", "1","2", "3", "4","5","6","7","8","9","10","11","12","13","14","15","16","17","18"});
		
		ArrWidth = (new int[] { 150,150,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60 });
		
		String rptitle="��һ��"+intyear+"��"+intMonth+"��ú̿�ɹ��ƻ�(���С��ӷѡ���ָú̿������ͬ��Լ������ú����˷�һͬ����ķ��ã������װ����վ̨�����ӵȷ��ã�һƱ����ĳ���)";
		rt.setTitle("", ArrWidth);
		
		rt.title.setCellValue(2, 1, "��λ���ƣ�"+((IDropDownModel) getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
		rt.title.setCellFont(2, 1, "", 12, true);
		rt.title.setCellAlign(2, 1, Table.ALIGN_LEFT);
		
		rt.title.setCellValue(3, 1, rptitle);
		rt.title.setCellFont(3, 1, "", 12, true);
		rt.title.setCellAlign(3, 1, Table.ALIGN_LEFT);
		rt.title.mergeRowCells(3);
		
		rt.body.setWidth(ArrWidth);
		rt.body.ShowZero = true;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
//		�ϲ���ͷ
//		rt.body.mergeRow(1);
		rt.body.merge(1, 1, 2, 1);
		rt.body.merge(1, 2, 2, 2);
		rt.body.merge(1, 3, 2, 3);
		rt.body.merge(1, 4, 2, 4);
		rt.body.merge(1, 5, 2, 5);
		
//		���ö��뷽ʽ
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0)
			_CurrentPage = 0;
		cn.Close();
		return rt.getAllPagesHtml();
	}
	
	private String getZhib() {
		_CurrentPage = 1;
		_AllPages = 1;

		JDBCcon cn = new JDBCcon();

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
		// �����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String curdate = "to_date('" + intyear + "-" + intMonth+ "-01','yyyy-mm-dd')";

		String strSQL ="SELECT DECODE(0, 0, '�ϼ�') QIB,\n" +
			"       FADL,\n" + 
			"       GONGDMH,\n" + 
			"       FADCYDL,\n" + 
			"       FADBML,\n" + 
			"       GONGRL,\n" + 
			"       GONGRMH,\n" + 
			"       GONGRBML,\n" + 
			"       BIAOMLHJ,\n" + 
			"       HAOYYML,\n" + 
			"       RLZHBMDJ,\n" + 
			"       SHANGYMKC,\n" + 
			"       SHANGYMKCDJ,\n" + 
			"       SHANGYMKCRZ,\n" + 
			"       YUEMKCJHZ,\n" + 
			"       YUEMKCRZ\n" + 
			"  FROM YUEDJH_ZHIB\n" + 
			" WHERE RIQ = "+curdate+"\n" + 
			"   AND DIANCXXB_ID = "+this.getTreeid();
		
		java.sql.ResultSet rs = cn.getResultSet(strSQL);
		Table tb = new Table(rs, 3, 0, 1);
		rt.setBody(tb);

		ArrHeader = new String[3][16];
		ArrHeader[0] = (new String[] { "�ڱ�", "������", "����ú��", "���糧�õ���", "�����ú��","������","����ú��","���ȱ�ú��","��ú���ϼ�","����ԭú��","��¯�ۺϱ�ú����","����ĩ���","����ĩ��浥��","����ĩ�����ֵ","��ĩ���ƻ�ֵ","��ĩ�����ֵ"});
		ArrHeader[1] = (new String[] { "�ڱ�", "��ǧ��ʱ","��/ǧ��ʱ", "%", "��","�򼪽�","ǧ��/����","��","��","��","Ԫ/��","��","Ԫ/��","�׽�/ǧ��","��","�׽�/ǧ��"});
		ArrHeader[2] = (new String[] { "��", "1","2", "3", "4","5","6","7","8","9","10","11","12","13","14","15"});
		
		ArrWidth = (new int[] { 80,80,70,90,80,80,70,80,80,80,100,80,90,90,90,80});
		String rptitle="���:"+intyear+"��"+intMonth+"�����ָ��Ԥ��";
		rt.setTitle("", ArrWidth);
		rt.title.setCellValue(3, 1, rptitle);
		rt.title.setCellFont(3, 1, "", 12, true);
		rt.title.setCellAlign(3, 1, Table.ALIGN_LEFT);
		rt.title.mergeRowCells(3);
		
		rt.body.setWidth(ArrWidth);
		rt.body.ShowZero = true;
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
//		�ϲ���ͷ
		rt.body.mergeRow(1);
		rt.body.merge(1, 1, 2, 1);
//		���ö��뷽ʽ
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		
		rt.createDefautlFooter(ArrWidth);
		
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0)
			_CurrentPage = 0;
		cn.Close();
		return rt.getAllPagesHtml();

	}

	// ���
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
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 12) {
				_nianf = _nianf + 1;
			}
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged = false;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2011; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	/**
	 * �·�
	 */
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
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 12) {
				_yuef = 1;
			} else {
				_yuef = _yuef + 1;
			}
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public boolean yuefchanged = false;

	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			yuefchanged = true;
		}
		_YuefValue = Value;
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// ***************************�����ʼ����***************************//
	private int _CurrentPage = -1;

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}

	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}

	// ҳ���ж�����
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

	// ******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
//			ͨ���������Ƿ���Ի��˽�������
			if(cycle.getRequestContext().getParameter("lx") != null){
				visit.setString1(cycle.getRequestContext().getParameter("lx"));
			}
			visit.setList1(null);
			setNianfValue(null);
			setYuefValue(null);
			getNianfModels();
			getYuefModels();
			getIDiancmcModels();
			this.setTreeid(null);
			setToolbar(null);
		}
		getToolBars();
	}

	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		// ��λ
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(),
				"-1".equals(getTreeid()) ? null : getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getIDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		// ���
		tb1.addText(new ToolbarText("���:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		// �·�
		tb1.addText(new ToolbarText("�·�:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "ˢ��",
				"function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Refurbish);
		tb1.addItem(tb);
		
		String msg=MainGlobal.getExtMessageShow("���ڴ�������,���Ժ�...", "������...", 5000);
		ToolbarButton sbbt = new ToolbarButton(null, "�ύ",
		"function(){document.getElementById('Tijiao').click();\n "+msg+"}");
		sbbt.setIcon(SysConstant.Btn_Icon_SelSubmit);
//		���ϵͳ����δ�ύ�����ݣ���ô�ύ��ť�ɼ�
		if(DataChk()){
			tb1.addItem(sbbt);
		}else{
			Visit visit = (Visit) getPage().getVisit();
			if(visit.getString1()!=null && !visit.getString1().equals("") &&visit.getString1().equals("return")){
				ToolbarButton sbht = new ToolbarButton(null, "����",
				"function(){document.getElementById('Huit').click();\n "+msg+"}");
				sbht.setIcon(SysConstant.Btn_Icon_Return);
				tb1.addItem(sbht);
			}
		}
		
		setToolbar(tb1);
	}
	
//	���ϵͳ���Ƿ����δ�ύ������
	private boolean DataChk(){
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
		String curdate = "to_date('" + intyear + "-" + intMonth+ "-01','yyyy-mm-dd')";
		
		String diancxxb_id=this.getTreeid();
		JDBCcon cn = new JDBCcon();

		String sqlChk="SELECT ID\n" +
					"  FROM YUEDJH_CAIG CG\n" + 
					" WHERE CG.RIQ = "+curdate+"\n" + 
					"   AND CG.DIANCXXB_ID = "+diancxxb_id+"\n" + 
					"   AND CG.ZHUANGT = 0\n" + 
					"UNION ALL\n" + 
					"SELECT ID\n" + 
					"  FROM YUEDJH_ZHIB CG\n" + 
					" WHERE CG.RIQ = "+curdate+"\n" + 
					"   AND CG.DIANCXXB_ID = "+diancxxb_id+"\n" + 
					"   AND CG.ZHUANGT = 0";
//		�ж��Ƿ����ƻ�����������̣����������ֻ���ύ״̬�����״̬��Ϊ0ʱ�����ݲ�Ϊδ�������
		if (MainGlobal.getXitxx_item("�ƻ�", "�Ƿ��������������","0", "��").equals("��")) {
			sqlChk=
				"SELECT ID\n" + 
				"  FROM YUEDJH_ZHIB CG\n" + 
				" WHERE CG.RIQ = "+curdate+"\n" + 
				"   AND CG.DIANCXXB_ID = "+diancxxb_id+"\n" + 
				"   AND CG.ZHUANGT = 0 AND CG.LIUCZTB_ID=0";
		}
		
		boolean statu=cn.getHasIt(sqlChk);
		cn.Close();
		return statu;
	}

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

	// �糧��
	// �糧����
	public boolean _diancmcchange = false;
	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if (_DiancmcValue == null) {
			_DiancmcValue = (IDropDownBean) getIDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {
		String sql = "";
		sql = "select d.id,d.mingc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
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