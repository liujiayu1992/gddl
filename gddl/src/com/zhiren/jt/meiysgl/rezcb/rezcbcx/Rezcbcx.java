package com.zhiren.jt.meiysgl.rezcb.rezcbcx;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.DateUtil;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import org.apache.tapestry.contrib.palette.SortMode;

public class Rezcbcx extends BasePage implements PageValidateListener{

	// �ж��Ƿ��Ǽ����û�
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// ����

	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// ��˾
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// �糧
	}
	public void pageValidate(PageEvent arg0) {
	    	String PageName = arg0.getPage().getPageName();
	    	String ValPageName = Login.ValidateLogin(arg0.getPage());
	    	if (!PageName.equals(ValPageName)) {
	    		ValPageName = Login.ValidateAdmin(arg0.getPage());
	    		if(!PageName.equals(ValPageName)) {
	    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
	        		throw new PageRedirectException(ipage);
	    		}
			}
		}
	// �糧�û�����ʱ��ʾ�糧����
	public String getDiancName() {
		JDBCcon con = new JDBCcon();
		ResultSet rs;
		String diancmc = "";
		// long diancID = -1;
		String sql = "select dc.id,dc.quanc as mingc from diancxxb dc where dc.id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		try {
			rs = con.getResultSet(sql);
			while (rs.next()) {
				// diancID = rs.getLong("id");
				diancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return diancmc;
	}

	private String OraDate(Date _date) {
		if (_date == null) {
			return "to_date('" + DateUtil.Formatdate("yyyy-MM-dd", new Date())
					+ "','yyyy-mm-dd')";
		}
		return "to_date('" + DateUtil.Formatdate("yyyy-MM-dd", _date)
				+ "','yyyy-mm-dd')";
	}

	// ��ʼ����
	private Date _BeginriqValue = new Date();

	private boolean _BeginriqChange = false;

	public Date getBeginriqDate() {
		if (_BeginriqValue == null) {
			_BeginriqValue = new Date();
		}
		return _BeginriqValue;
	}

	public void setBeginriqDate(Date _value) {
		if (DateUtil.Formatdate("yyyy-MM-dd", _BeginriqValue).equals(
				DateUtil.Formatdate("yyyy-MM-dd", _value))) {
			_BeginriqChange = false;
		} else {
			_BeginriqValue = _value;
			_BeginriqChange = true;
		}
	}

	// ��ʼ����
	private Date _EndriqValue = new Date();

	private boolean _EndriqChange = false;

	public Date getEndriqDate() {
		if (_EndriqValue == null) {
			_EndriqValue = new Date();
		}
		return _EndriqValue;
	}

	public void setEndriqDate(Date _value) {
		if (_EndriqValue.equals(_value)) {
			_EndriqChange = false;
		} else {
			_EndriqValue = _value;
			_EndriqChange = true;
		}
	}

	private String _msg;

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
	}

	private void Refurbish() {
		// Ϊ "ˢ��" ��ť��Ӵ������
		isBegin = true;
		// getSelectData();
	}

	// ******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			((Visit) getPage().getVisit()).setList1(null);
			((Visit) getPage().getVisit()).getList1();
			_BeginriqValue = new Date();
			_EndriqValue = new Date();
			setBaoblxValue(null);
			getIBaoblxModels();
			isBegin = true;
			this.setTreeid(null);
			visit.setList1(null);
			this.getSelectData();
			
		}
		if (_Baoblxchange) {
			_Baoblxchange = false;
			Refurbish();
		}
		if (_BeginriqChange) {
			_BeginriqChange = false;
			Refurbish();
		}
		if (_EndriqChange) {
			_EndriqChange = false;
			Refurbish();
		}
		getToolBars();
		Refurbish();
	}

	private String RT_HET = "Rezcbcx";

	private String mstrReportName = "Rezcbcx";

	public String getPrintTable() {
		if (!isBegin) {
			return "";
		}
		isBegin = false;
		if (mstrReportName.equals(RT_HET)) {
			return getSelectData();
		} else {
			return "�޴˱���";
		}
	}

	public int getZhuangt() {
		return 1;
	}

	private int intZhuangt = 0;

	public void setZhuangt(int _value) {
		intZhuangt = 1;
	}

	private String Week(Date date) {// �õ���ǰ����������
		String week = "";
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		cal.setTime(date);
		int day = cal.get(Calendar.DATE);
		int weekname = cal.get(Calendar.DAY_OF_WEEK);

		String beginweek = "";
		String endweek = "";
		// GetDate dg = new GetDate();
		switch (weekname) {

		case 4: // ������
			beginweek = "r.riq>=" + OraDate(date) + "";
			endweek = "r.riq<=" + OraDate(date) + "+6";
			break;
		case 5: // ������
			beginweek = "r.riq>=" + OraDate(date) + "-1";
			endweek = "r.riq<=" + OraDate(date) + "+5";
			break;
		case 6: // ������
			beginweek = "r.riq>=" + OraDate(date) + "-2";
			endweek = "r.riq<=" + OraDate(date) + "+4";
			break;
		case 7: // ������
			beginweek = "r.riq>=" + OraDate(date) + "-3";
			endweek = "r.riq<=" + OraDate(date) + "+3";
			break;
		case 1: // ������
			beginweek = "r.riq>=" + OraDate(date) + "-4";
			endweek = "r.riq<=" + OraDate(date) + "+2";
			break;
		case 2: // ����һ
			beginweek = "r.riq>=" + OraDate(date) + "-5";
			endweek = "r.riq<=" + OraDate(date) + "+1";
			break;
		case 3: // ���ڶ�
			beginweek = "r.riq>=" + OraDate(date) + "-6";
			endweek = "r.riq<=" + OraDate(date) + "";
			break;

		default:
			break;
		}
		week = " and " + beginweek + " and " + endweek;
		return week;
	}

	private Date WeekFistDate(Date date) {// �õ���ǰ����������
		
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		cal.setTime(date);
		int weekDayIndex = cal.get(Calendar.DAY_OF_WEEK);

		if (weekDayIndex >= 4) {
			cal.add(Calendar.DAY_OF_MONTH, -(weekDayIndex - 4));
		} else {
			cal.add(Calendar.DAY_OF_MONTH, -(weekDayIndex + 3));
		}
		return cal.getTime();
	}

	private Date WeekLastDate(Date date) {// �õ���ǰ����������
		
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		cal.setTime(date);
		int weekDayIndex = cal.get(Calendar.DAY_OF_WEEK);

		if (weekDayIndex >= 4) {
			cal.add(Calendar.DAY_OF_MONTH, -(weekDayIndex - 4));
		} else {
			cal.add(Calendar.DAY_OF_MONTH, -(weekDayIndex + 3));
		}
		cal.add(Calendar.DAY_OF_MONTH, 6);
		return cal.getTime();
	}

	// String beginweek="";
	// String endweek="";
	// // GetDate dg = new GetDate();
	// switch (weekname) {
	//		
	// case 4: //������
	// beginweek=""+OraDate(date)+"";
	// endweek="r.riq<="+OraDate(date)+"+6";
	// break;
	// case 5: //������
	// beginweek="r.riq>="+OraDate(date)+"-1";
	// endweek="r.riq<="+OraDate(date)+"+5";
	// break;
	// case 6: //������
	// beginweek="r.riq>="+OraDate(date)+"-2";
	// endweek="r.riq<="+OraDate(date)+"+4";
	// break;
	// case 7: //������
	// beginweek="r.riq>="+OraDate(date)+"-3";
	// endweek="r.riq<="+OraDate(date)+"+3";
	// break;
	// case 1: //������
	// beginweek="r.riq>="+OraDate(date)+"-4";
	// endweek="r.riq<="+OraDate(date)+"+2";
	// break;
	// case 2: //����һ
	// beginweek="r.riq>="+OraDate(date)+"-5";
	// endweek="r.riq<="+OraDate(date)+"+1";
	// break;
	// case 3: //���ڶ�
	// beginweek="r.riq>="+OraDate(date)+"-6";
	// endweek="r.riq<="+OraDate(date)+"";
	// break;
	//			
	// default: break;
	// }
	// week=" and "+beginweek+" and "+endweek;
	// return week;
	// }

	private boolean isBegin = false;

	private String getSelectData() {
		String strSQL = "";
		_CurrentPage = 1;
		_AllPages = 1;

		JDBCcon cn = new JDBCcon();

		// �����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String ArrFormat[] = null;
		String titlename = "��ֵ��";
		int iFixedRows = 0;// �̶��к�
		int iCol = 0;// ����
		String riq = "";
		String riqlj="";
		String titledate = "";// ��������
		
		int year = 0;
		int month = 0;
		boolean zongh=false;
		if (getBaoblxValue().getValue().equals("�±�")) {   //�±������Ϊ�����¡��ۼƺ��ۺϡ�
			

			
			if (getLeixValue().getValue().equals("����")){  //�����жϵ����µ���������getLeixValue
				riq = " r.riq>=first_day(" + OraDate(getBeginriqDate())
				+ ") and r.riq<=Last_day(" + OraDate(getBeginriqDate())
				+ ")";
				year = DateUtil.getYear(getBeginriqDate());
				month = DateUtil.getMonth(getBeginriqDate());
				titledate = year + "��" + month + "��";
			}else if (getLeixValue().getValue().equals("�ۼ�")){
				riq=" r.riq>=to_date('"+DateUtil.getYear(getBeginriqDate())+"-01-01','yyyy-mm-dd') and r.riq<=Last_day("+OraDate(getBeginriqDate())+")";
				year = DateUtil.getYear(getBeginriqDate());
				month = DateUtil.getMonth(getBeginriqDate());
				titledate = year + "��" + month + "��";
			}else{ //���ۺϵĲ�ѯ
				riq = " r.riq>=first_day(" + OraDate(getBeginriqDate())
				+ ") and r.riq<=Last_day(" + OraDate(getBeginriqDate())
				+ ")";
				riqlj=" r.riq>=to_date('"+DateUtil.getYear(getBeginriqDate())+"-01-01','yyyy-mm-dd') and r.riq<=Last_day("+OraDate(getBeginriqDate())+")";
				zongh=true;    //֤������������жϡ�
			}
				

			
		} else if (getBaoblxValue().getValue().equals("�ܱ�")) {
			riq = " r.riq>=" + OraDate(WeekFistDate(getBeginriqDate()))
					+ " and r.riq<=" + OraDate(WeekLastDate(getBeginriqDate()))
					+ "";
			titledate = FormatDate(WeekFistDate(getBeginriqDate())) + "��"
					+ FormatDate(WeekLastDate(getBeginriqDate()));
		} else {// ��ʱ���
			riq = " r.riq>=" + OraDate(getBeginriqDate()) + " and r.riq<="
					+ OraDate(getEndriqDate());
			titledate = FormatDate(getBeginriqDate()) + "��"
					+ FormatDate(getEndriqDate());
		}
		
     

		String strdiancid = "";
		
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strdiancid=" ";
			
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strdiancid = "  and dc.fuid=  " +this.getTreeid();
			
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strdiancid=" and dc.id= " +this.getTreeid();
			 
		}else if (jib==-1){
			strdiancid=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		
		
		// ��������
		if(jib==1){//���Ǽ���ʱ,���շֹ�˾����
			strSQL ="select diancmc,\n"
				+ "       rucsl,\n"
				+ "       rucrl,\n"
				+ "       rucsf,\n"
				+ "       rulsl,\n"
				+ "       rulrl,\n"
				+ "       rulsf,\n"
				+ "       case\n"
				+ "         when rulsl = 0 or rucsl = 0 then\n"
				+ "          '-'\n"
				+ "         else\n"
				+ "          to_char(rucrl - rulrl)\n"
				+ "       end as rezctzq,\n"
				+ "       case\n"
				+ "         when rulsl = 0 or rucsl = 0 then\n"
				+ "          '-'\n"
				+ "         else\n"
				+ "          to_char(round((rucrl - rulrl) * 1000 / 4.1816, 0))\n"
				+ "       end as rezctzqdk,\n"
				+ "       case\n"
				+ "         when rulsl = 0 or rucsl = 0 then\n"
				+ "          '-'\n"
				+ "         else\n"
				+ "          to_char(round(rucrl - rulrl * (100 - rucsf) / (100 - rulsf), 2))\n"
				+ "       end as rezctzh,\n"
				+ "       case\n"
				+ "         when rulsl = 0 or rucsl = 0 then\n"
				+ "          '-'\n"
				+ "         else\n"
				+ "          to_char(round(round(rucrl - rulrl * (100 - rucsf) / (100 - rulsf),\n"
				+ "                              2) * 1000 / 4.1816,\n"
				+ "                        0))\n"
				+ "       end as rezctzhdk,\n"
				+ "       beiz\n"
				+ "  from (select decode(grouping(dc.leix) + grouping(dc.jianc),\n"
				+ "                      2,\n"
				+ "                      '"+getTreeDiancmc(this.getTreeid())+"',\n"
				+ "                      1,\n"
				+ "                      '��' || dc.leix || 'С��',\n"
				+ "                      max(dc.jianc)) as diancmc,\n"
				+ "               sum(r.rucsl) as rucsl,\n"
				+ "               decode(grouping(dc.jianc),\n"
				+ "                      1,\n"
				+ "                      decode(sum(decode(sl.rulsl, 0, 0, r.rucsl)),\n"
				+ "                             0,\n"
				+ "                             0,\n"
				+ "                             round(sum(decode(sl.rulsl, 0, 0, r.rucsl) *\n"
				+ "                                       r.rucrl) /\n"
				+ "                                   sum(decode(sl.rulsl, 0, 0, r.rucsl)),\n"
				+ "                                   2)),\n"
				+ "                      round(decode(sum(r.rucsl),\n"
				+ "                                   0,\n"
				+ "                                   0,\n"
				+ "                                   sum(r.rucsl * r.rucrl) / sum(r.rucsl)),\n"
				+ "                            2)) as rucrl,\n"
				+ "               decode(grouping(dc.jianc),\n"
				+ "                      1,\n"
				+ "                      decode(sum(decode(sl.rulsl, 0, 0, r.rucsl)),\n"
				+ "                             0,\n"
				+ "                             0,\n"
				+ "                             round(sum(decode(sl.rulsl, 0, 0, r.rucsl) *\n"
				+ "                                       r.rucsf) /\n"
				+ "                                   sum(decode(sl.rulsl, 0, 0, r.rucsl)),\n"
				+ "                                   2)),\n"
				+ "                      round(decode(sum(r.rucsl),\n"
				+ "                                   0,\n"
				+ "                                   0,\n"
				+ "                                   sum(r.rucsl * r.rucsf) / sum(r.rucsl)),\n"
				+ "                            2)) as rucsf,\n"
				+ "               sum(r.rulsl) as rulsl,\n"
				+ "               decode(grouping(dc.jianc),\n"
				+ "                      1,\n"
				+ "                      decode(sum(decode(sl.rucsl, 0, 0, r.rulsl)),\n"
				+ "                             0,\n"
				+ "                             0,\n"
				+ "                             round(sum(decode(sl.rucsl, 0, 0, r.rulsl) *\n"
				+ "                                       r.rulrl) /\n"
				+ "                                   sum(decode(sl.rucsl, 0, 0, r.rulsl)),\n"
				+ "                                   2)),\n"
				+ "                      round(decode(sum(r.rulsl),\n"
				+ "                                   0,\n"
				+ "                                   0,\n"
				+ "                                   sum(r.rulsl * r.rulrl) / sum(r.rulsl)),\n"
				+ "                            2)) as rulrl,\n"
				+ "               decode(grouping(dc.jianc),\n"
				+ "                      1,\n"
				+ "                      decode(sum(decode(sl.rucsl, 0, 0, r.rulsl)),\n"
				+ "                             0,\n"
				+ "                             0,\n"
				+ "                             round(sum(decode(sl.rucsl, 0, 0, r.rulsl) *\n"
				+ "                                       r.rulsf) /\n"
				+ "                                   sum(decode(sl.rucsl, 0, 0, r.rulsl)),\n"
				+ "                                   2)),\n"
				+ "                      round(decode(sum(r.rulsl),\n"
				+ "                                   0,\n"
				+ "                                   0,\n"
				+ "                                   sum(r.rulsl * r.rulsf) / sum(r.rulsl)),\n"
				+ "                            2)) as rulsf,\n"
				+ "               decode(grouping(dc.jianc), 1, '', max(r.beiz)) as beiz\n"
				+ "          from (select r.* from rezcb r  where "+riq+")r,\n"
				+ "               dianckjpxb px,\n"
				+ "               (select s.diancxxb_id,\n"
				+ "                       decode(s.rulsl, 0, 0, s.rucsl) as rucsl,\n"
				+ "                       decode(s.rucsl, 0, 0, s.rulsl) as rulsl\n"
				+ "                  from (select r.diancxxb_id,\n"
				+ "                               sum(r.rucsl) as rucsl,\n"
				+ "                               sum(r.rulsl) as rulsl\n"
				+ "                          from rezcb r\n"
				+ "                         where "+riq+"\n"
				+ "                         group by r.diancxxb_id) s) sl,\n"
				+ "               (select d.id,d.fuid,d.mingc as jianc,df.mingc as leix ,df.xuh  from diancxxb d ,diancxxb df where  d.jib=3 and d.fuid=df.id(+)) dc\n"
				+ "         where \n"
				+ "            r.diancxxb_id(+) = dc.id\n"
				+ "           and sl.diancxxb_id(+) = dc.id\n"
				+ "           and dc.id = px.diancxxb_id(+) "+strdiancid+"\n"
				+ "           and px.kouj = '�±�'\n"
				+ "         group by rollup(dc.leix, dc.jianc)\n"
				+ "         order by grouping(dc.leix) desc,\n"
				+ "                  max(dc.xuh),\n"
				+ "                  grouping(dc.jianc) desc,\n"
				+ "                  max(px.xuh))";
			
			ArrHeader = new String[2][12];
			ArrHeader[0] = new String[] { "��λ", "�볧ú", "�볧ú", "�볧ú", "��¯ú", "��¯ú",
					"��¯ú", "ˮ�ֵ���ǰ��ֵ��", "ˮ�ֵ���ǰ��ֵ��", "ˮ�ֵ�������ֵ��", "ˮ�ֵ�������ֵ��", "��ע" };
			ArrHeader[1] = new String[] { "��λ", "����(t)", "Qnet,ar(MJ/kg)", "Mt(%)",
					"����(t)", "Qnet,ar(MJ/kg)", "Mt(%)", "mj/kg", "��/����", "mj/kg",
					"��/����", "��ע" };

			ArrWidth = new int[] { 130, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50 };
			ArrFormat = new String[] { "", "0", "0.00", "0.00", "0", "0.00", "0.00", "0.00", "0", "0.00", "0", "" };
			iFixedRows = 1;
			iCol = 10;

		}else if(jib==2||(jib==3&&!getBaoblxValue().getValue().equals("�ܱ�"))){
			
	
				strSQL ="select diancmc,\n"
						+ "       rucsl,\n"
						+ "       rucrl,\n"
						+ "       rucsf,\n"
						+ "       rulsl,\n"
						+ "       rulrl,\n"
						+ "       rulsf,\n"
						+ "       case\n"
						+ "         when rulsl = 0 or rucsl = 0 then\n"
						+ "          '-'\n"
						+ "         else\n"
						+ "          to_char(rucrl - rulrl)\n"
						+ "       end as rezctzq,\n"
						+ "       case\n"
						+ "         when rulsl = 0 or rucsl = 0 then\n"
						+ "          '-'\n"
						+ "         else\n"
						+ "          to_char(round((rucrl - rulrl) * 1000 / 4.1816, 0))\n"
						+ "       end as rezctzqdk,\n"
						+ "       case\n"
						+ "         when rulsl = 0 or rucsl = 0 then\n"
						+ "          '-'\n"
						+ "         else\n"
						+ "          to_char(round(rucrl - rulrl * (100 - rucsf) / (100 - rulsf), 2))\n"
						+ "       end as rezctzh,\n"
						+ "       case\n"
						+ "         when rulsl = 0 or rucsl = 0 then\n"
						+ "          '-'\n"
						+ "         else\n"
						+ "          to_char(round(round(rucrl - rulrl * (100 - rucsf) / (100 - rulsf),\n"
						+ "                              2) * 1000 / 4.1816,\n"
						+ "                        0))\n"
						+ "       end as rezctzhdk,\n"
						+ "       beiz\n"
						+ "  from (select decode(grouping(dc.leix) + grouping(dc.jianc),\n"
						+ "                      2,\n"
						+ "                      '"+getTreeDiancmc(this.getTreeid())+"',\n"
						+ "                      1,\n"
						+ "                      '��' || dc.leix || 'С��',\n"
						+ "                      max(dc.jianc)) as diancmc,\n"
						+ "               sum(r.rucsl) as rucsl,\n"
						+ "               decode(grouping(dc.jianc),\n"
						+ "                      1,\n"
						+ "                      decode(sum(decode(sl.rulsl, 0, 0, r.rucsl)),\n"
						+ "                             0,\n"
						+ "                             0,\n"
						+ "                             round(sum(decode(sl.rulsl, 0, 0, r.rucsl) *\n"
						+ "                                       r.rucrl) /\n"
						+ "                                   sum(decode(sl.rulsl, 0, 0, r.rucsl)),\n"
						+ "                                   2)),\n"
						+ "                      round(decode(sum(r.rucsl),\n"
						+ "                                   0,\n"
						+ "                                   0,\n"
						+ "                                   sum(r.rucsl * r.rucrl) / sum(r.rucsl)),\n"
						+ "                            2)) as rucrl,\n"
						+ "               decode(grouping(dc.jianc),\n"
						+ "                      1,\n"
						+ "                      decode(sum(decode(sl.rulsl, 0, 0, r.rucsl)),\n"
						+ "                             0,\n"
						+ "                             0,\n"
						+ "                             round(sum(decode(sl.rulsl, 0, 0, r.rucsl) *\n"
						+ "                                       r.rucsf) /\n"
						+ "                                   sum(decode(sl.rulsl, 0, 0, r.rucsl)),\n"
						+ "                                   2)),\n"
						+ "                      round(decode(sum(r.rucsl),\n"
						+ "                                   0,\n"
						+ "                                   0,\n"
						+ "                                   sum(r.rucsl * r.rucsf) / sum(r.rucsl)),\n"
						+ "                            2)) as rucsf,\n"
						+ "               sum(r.rulsl) as rulsl,\n"
						+ "               decode(grouping(dc.jianc),\n"
						+ "                      1,\n"
						+ "                      decode(sum(decode(sl.rucsl, 0, 0, r.rulsl)),\n"
						+ "                             0,\n"
						+ "                             0,\n"
						+ "                             round(sum(decode(sl.rucsl, 0, 0, r.rulsl) *\n"
						+ "                                       r.rulrl) /\n"
						+ "                                   sum(decode(sl.rucsl, 0, 0, r.rulsl)),\n"
						+ "                                   2)),\n"
						+ "                      round(decode(sum(r.rulsl),\n"
						+ "                                   0,\n"
						+ "                                   0,\n"
						+ "                                   sum(r.rulsl * r.rulrl) / sum(r.rulsl)),\n"
						+ "                            2)) as rulrl,\n"
						+ "               decode(grouping(dc.jianc),\n"
						+ "                      1,\n"
						+ "                      decode(sum(decode(sl.rucsl, 0, 0, r.rulsl)),\n"
						+ "                             0,\n"
						+ "                             0,\n"
						+ "                             round(sum(decode(sl.rucsl, 0, 0, r.rulsl) *\n"
						+ "                                       r.rulsf) /\n"
						+ "                                   sum(decode(sl.rucsl, 0, 0, r.rulsl)),\n"
						+ "                                   2)),\n"
						+ "                      round(decode(sum(r.rulsl),\n"
						+ "                                   0,\n"
						+ "                                   0,\n"
						+ "                                   sum(r.rulsl * r.rulsf) / sum(r.rulsl)),\n"
						+ "                            2)) as rulsf,\n"
						+ "               decode(grouping(dc.jianc), 1, '', max(r.beiz)) as beiz\n"
						+ "          from  (select r.* from rezcb r  where "+riq+")r,\n"
						+ "               dianckjpxb px,\n"
						+ "               (select s.diancxxb_id,\n"
						+ "                       decode(s.rulsl, 0, 0, s.rucsl) as rucsl,\n"
						+ "                       decode(s.rucsl, 0, 0, s.rulsl) as rulsl\n"
						+ "                  from (select r.diancxxb_id,\n"
						+ "                               sum(r.rucsl) as rucsl,\n"
						+ "                               sum(r.rulsl) as rulsl\n"
						+ "                          from rezcb r\n"
						+ "                         where "+riq+"\n"
						+ "                         group by r.diancxxb_id) s) sl,\n"
						+ "               (select dc.id,dc.fuid, dc.mingc as jianc, dl.mingc as leix, dl.xuh\n"
						+ "                  from diancxxb dc, dianclbb dl\n"
						+ "                 where dc.dianclbb_id = dl.id) dc\n"
						+ "         where \n"
						+ "            r.diancxxb_id(+) = dc.id\n"
						+ "           and sl.diancxxb_id(+) = dc.id\n"
						+ "           and dc.id = px.diancxxb_id(+) "+strdiancid+"\n"
						
						+ "           and px.kouj = '�±�'\n"
						+ "         group by rollup(dc.leix, dc.jianc)\n"
						+ "         order by grouping(dc.leix) desc,\n"
						+ "                  max(dc.xuh),\n"
						+ "                  grouping(dc.jianc) desc,\n"
						+ "                  max(px.xuh))";
				ArrHeader = new String[2][12];
				ArrHeader[0] = new String[] { "��λ", "�볧ú", "�볧ú", "�볧ú", "��¯ú", "��¯ú",
						"��¯ú", "ˮ�ֵ���ǰ��ֵ��", "ˮ�ֵ���ǰ��ֵ��", "ˮ�ֵ�������ֵ��", "ˮ�ֵ�������ֵ��", "��ע" };
				ArrHeader[1] = new String[] { "��λ", "����(t)", "Qnet,ar(MJ/kg)", "Mt(%)",
						"����(t)", "Qnet,ar(MJ/kg)", "Mt(%)", "mj/kg", "��/����", "mj/kg",
						"��/����", "��ע" };

				ArrWidth = new int[] { 130, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50 };
				ArrFormat = new String[] { "", "0", "0.00", "0.00", "0",
						"0.00", "0.00", "0.00", "0", "0.00", "0", "" };
				iFixedRows = 1;
				iCol = 10;

		       }else if(jib==3&&getBaoblxValue().getValue().equals("�ܱ�")){
		    strSQL=" select diancmc,\n"+
		           "to_char(riq,'yyyy-mm-dd'),\n"+
		          "rucsl,\n"+
		          "rucrl,\n"+
		          "rucsf,\n"+
		          "rulsl,\n"+
		          "rulrl,\n"+
		          "rulsf,\n"+
		          "case\n"+
		          "  when rulsl = 0 or rucsl = 0 then\n"+
		          "   '-'\n"+
		           " else\n"+
		           "  to_char(rucrl - rulrl)\n"+
		          "end as rezctzq,\n"+
		          "case\n"+
		          "  when rulsl = 0 or rucsl = 0 then\n"+
		          "   '-'\n"+
		           " else\n"+
		            " to_char(round((rucrl - rulrl) * 1000 / 4.1816, 0))\n"+
		         " end as rezctzqdk,\n"+
		         " case\n"+
		          "  when rulsl = 0 or rucsl = 0 then\n"+
		           "  '-'\n"+
		           " else\n"+
		            " to_char(round(rucrl - rulrl * (100 - rucsf) / (100 - rulsf), 2))\n"+
		         " end as rezctzh,\n"+
		          "case\n"+
		          "  when rulsl = 0 or rucsl = 0 then\n"+
		           "  '-'\n"+
		           " else\n"+
		           "  to_char(round(round(rucrl - rulrl * (100 - rucsf) / (100 - rulsf),\n"+
		           "                      2) * 1000 / 4.1816,\n"+
		           "                0))\n"+
		         " end as rezctzhdk,\n"+
		         " beiz\n"+
		        
		     "from (\n"+
		     "select grouping(r.riq),r.riq, \n"+
		      "       decode(grouping(r.riq),\n"+
		       "                  1,\n"+
		        "                 max(dc.jianc)||'�ܼ�',\n"+
		         "                max(dc.jianc)) as diancmc,\n"+
		         "         sum(r.rucsl) as rucsl,\n"+
		         "         decode(grouping(r.riq),\n"+
		    		   "               1,\n"+
		    		   "               decode(sum(decode(sl.rulsl, 0, 0, r.rucsl)),\n"+
		    		   "                       0,\n"+
		    		   "                0,\n"+
		    		   "                 round(sum(decode(sl.rulsl, 0, 0, r.rucsl) *\n"+
		    		   "                           r.rucrl) /\n"+
		    		   "                       sum(decode(sl.rulsl, 0, 0, r.rucsl)),\n"+
		    		   "                       2)),\n"+
		    		   "          round(decode(sum(r.rucsl),\n"+
		    		   "                       0,\n"+
		    		   "                       0,\n"+
		    		   "                        sum(r.rucsl * r.rucrl) / sum(r.rucsl)),\n"+
		    		   "                  2)) as rucrl,\n"+
		    		   "    decode(grouping(r.riq),\n"+
		    		   "            1,\n"+
		    		   "            decode(sum(decode(sl.rulsl, 0, 0, r.rucsl)),\n"+
		    		   "                   0,\n"+
		    		   "                   0,\n"+
		    		   "                  round(sum(decode(sl.rulsl, 0, 0, r.rucsl) *\n"+
		    		   "                             r.rucsf) /\n"+
		    		   "                         sum(decode(sl.rulsl, 0, 0, r.rucsl)),\n"+
		    		   "                         2)),\n"+
		    		   "            round(decode(sum(r.rucsl),\n"+
		    		   "                         0,\n"+
		    		   "                         0,\n"+
		    		   "                         sum(r.rucsl * r.rucsf) / sum(r.rucsl)),\n"+
		    		   "                  2)) as rucsf,\n"+
		    		   "     sum(r.rulsl) as rulsl,\n"+
		    		   "      decode(grouping(r.riq),\n"+
		    		   "            1,\n"+
		    		   "             decode(sum(decode(sl.rucsl, 0, 0, r.rulsl)),\n"+
		    		   "                   0,\n"+
		    		   "                   0,\n"+
		    		   "                   round(sum(decode(sl.rucsl, 0, 0, r.rulsl) *\n"+
		    		   "                            r.rulrl) /\n"+
		    		   "                         sum(decode(sl.rucsl, 0, 0, r.rulsl)),\n"+
		    		   "                         2)),\n"+
		    		   "            round(decode(sum(r.rulsl),\n"+
		    		   "                         0,\n"+
		    		   "                         0,\n"+
		    		   "                         sum(r.rulsl * r.rulrl) / sum(r.rulsl)),\n"+
		    		   "                  2)) as rulrl,\n"+
		    		   "     decode(grouping(r.riq),\n"+
		    		   "            1,\n"+
		    		   "            decode(sum(decode(sl.rucsl, 0, 0, r.rulsl)),\n"+
		    		   "                   0,\n"+
		    		   "                  0,\n"+
		    		   "                   round(sum(decode(sl.rucsl, 0, 0, r.rulsl) *\n"+
		    		   "                             r.rulsf) /\n"+
		    		   "                         sum(decode(sl.rucsl, 0, 0, r.rulsl)),\n"+
		    		   "                         2)),\n"+
		    		   "            round(decode(sum(r.rulsl),\n"+
		    		   "                         0,\n"+
		    		   "                         0,\n"+
		    		   "                         sum(r.rulsl * r.rulsf) / sum(r.rulsl)),\n"+
		    		   "                  2)) as rulsf,\n"+
		    		   "     decode(grouping(r.riq), 1, '', max(r.beiz)) as beiz\n"+
		    		   "   from rezcb r,\n"+
		    		   "    dianckjpxb px,\n"+
		               "     (select s.diancxxb_id,\n"+
		    		   "             decode(s.rulsl, 0, 0, s.rucsl) as rucsl,\n"+
		    		   "             decode(s.rucsl, 0, 0, s.rulsl) as rulsl\n"+
		    		   "        from (select r.diancxxb_id,\n"+
		    		   "                     sum(r.rucsl) as rucsl,\n"+
		    		   "                     sum(r.rulsl) as rulsl\n"+
		    		   "                from rezcb r\n"+
		    		   "               where  "+riq+"\n"+
		    		   "               group by r.diancxxb_id) s) sl,\n"+
		    		   "     (select dc.id,dc.fuid, dc.mingc as jianc, dl.mingc as leix, dl.xuh\n"+
		    		   "         from diancxxb dc, dianclbb dl\n"+
		    		   "       where dc.dianclbb_id = dl.id) dc\n"+
		    		   "   where  "+riq+"\n"+
		    		   "   and r.diancxxb_id = dc.id\n"+
		    		   "   and sl.diancxxb_id = dc.id\n"+
		    		   "  and dc.id = px.diancxxb_id(+)   "+strdiancid+" \n"+
		    		   "  and px.kouj = '�±�'\n"+
		    		   "   group by rollup(r.riq)\n"+
		    		   "   order by \n"+
		    		   "        grouping(r.riq) desc,\n"+
		    		   "        r.riq\n"+
		                 "    )";
			ArrHeader = new String[2][13];
			ArrHeader[0] = new String[] { "��λ","����", "�볧ú", "�볧ú", "�볧ú", "��¯ú", "��¯ú",
					"��¯ú", "ˮ�ֵ���ǰ��ֵ��", "ˮ�ֵ���ǰ��ֵ��", "ˮ�ֵ�������ֵ��", "ˮ�ֵ�������ֵ��", "��ע" };
			ArrHeader[1] = new String[] { "��λ","����", "����(t)", "Qnet,ar(MJ/kg)", "Mt(%)",
					"����(t)", "Qnet,ar(MJ/kg)", "Mt(%)", "mj/kg", "��/����", "mj/kg",
					"��/����", "��ע" };

			ArrWidth = new int[] { 130,70, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50 };
			ArrFormat = new String[] { "","", "0", "0.00", "0.00", "0",
					"0.00", "0.00", "0.00", "0", "0.00", "0", "" };
			iFixedRows = 1;
			iCol = 10;
		       }
		//////////////////////////////////////////////////////////
		if(zongh){
			zongh=false;
			strSQL = 
				"select diancmc,decode(1,1,fenx,'') as fenx,rucsl,rucrl,rucsf,rulsl,rulrl,rulsf, rezctzq,rezctzqdk,rezctzh,rezctzhdk,beiz from\n" +
				" (select diancmc,fenx,rownum as xuh,rucsl,rucrl,rucsf,rulsl,rulrl,rulsf,\n" + 
				"  case when rulsl=0 or rucsl=0 then '-' else to_char(rucrl-rulrl) end as rezctzq,\n" + 
				"  case when rulsl=0 or rucsl=0 then '-' else to_char(round((rucrl-rulrl)*1000/4.1816,0)) end as rezctzqdk,\n" + 
				"  case when rulsl=0 or rucsl=0 then '-' else to_char(round(rucrl-rulrl*(100-rucsf)/(100-rulsf),2)) end as rezctzh,\n" + 
				"  case when rulsl=0 or rucsl=0 then '-' else to_char(round(round(rucrl-rulrl*(100-rucsf)/(100-rulsf),2)*1000/4.1816,0)) end as rezctzhdk,beiz\n" + 
				" from (\n" + 
				"select  decode(grouping(dc.leix)+grouping(dc.mingc),2,'"+getTreeDiancmc(this.getTreeid())+"',1,'��'||dc.leix||'С��',max(dc.mingc)) as diancmc,\n" + 
				"  min(dc.fenx) as fenx,sum(r.rucsl) as rucsl,\n" + 
				"  decode(grouping(dc.mingc),1,decode(sum(decode(sl.rulsl,0,0,r.rucsl)),0,0,\n" + 
				"                                    round(sum(decode(sl.rulsl,0,0,r.rucsl)*r.rucrl)/sum(decode(sl.rulsl,0,0,r.rucsl)),2)),\n" + 
				"                              round(decode(sum(r.rucsl),0,0,sum(r.rucsl*r.rucrl)/sum(r.rucsl)),2)) as rucrl,\n" + 
				"  decode(grouping(dc.mingc),1,decode(sum(decode(sl.rulsl,0,0,r.rucsl)),0,0,\n" + 
				"                                    round(sum(decode(sl.rulsl,0,0,r.rucsl)*r.rucsf)/sum(decode(sl.rulsl,0,0,r.rucsl)),2)),\n" + 
				"                              round(decode(sum(r.rucsl),0,0,sum(r.rucsl*r.rucsf)/sum(r.rucsl)),2)) as rucsf,\n" + 
				"  sum(r.rulsl) as rulsl,\n" + 
				"  decode(grouping(dc.mingc),1,decode(sum(decode(sl.rucsl,0,0,r.rulsl)),0,0,\n" + 
				"                                   round(sum(decode(sl.rucsl,0,0,r.rulsl)*r.rulrl)/sum(decode(sl.rucsl,0,0,r.rulsl)),2)),\n" + 
				"                              round(decode(sum(r.rulsl),0,0,sum(r.rulsl*r.rulrl)/sum(r.rulsl)),2)) as rulrl,\n" + 
				"  decode(grouping(dc.mingc),1,decode(sum(decode(sl.rucsl,0,0,r.rulsl)),0,0,\n" + 
				"                                   round(sum(decode(sl.rucsl,0,0,r.rulsl)*r.rulsf)/sum(decode(sl.rucsl,0,0,r.rulsl)),2)),\n" + 
				"                              round(decode(sum(r.rulsl),0,0,sum(r.rulsl*r.rulsf)/sum(r.rulsl)),2)) as rulsf,\n" + 
				"  decode(grouping(dc.mingc),1,'',max(r.beiz)) as beiz\n" + 
				"  from ((select r.diancxxb_id,r.rucrl,r.rucsf,r.rucsl,r.rulsl,r.rulrl,r.rulsf,r.beiz\n" + 
				"        from rezcb r where "+riq+" ) ) r,dianckjpxb px,\n" + 
				"(select s.diancxxb_id,decode(s.rulsl,0,0,s.rucsl) as rucsl, decode(s.rucsl,0,0,s.rulsl) as rulsl from\n" + 
				"(select r.diancxxb_id,sum(r.rucsl) as rucsl,sum(r.rulsl) as rulsl from rezcb r\n" + 
				"  where  "+riq+"\n" + 
				"   group by r.diancxxb_id) s) sl,\n" + 
				"  (select distinct dc.id,dc.mingc,fx.fenx,dl.mingc as leix,dl.xuh from diancxxb dc,dianclbb dl,rezcb r,\n" + 
				"       (select decode(1,1,'����','') as fenx from dual union select decode(1,1,'�ۼ�','') as fenx from dual) fx\n" + 
				"       where dc.dianclbb_id=dl.id and r.diancxxb_id=dc.id "+strdiancid+" and  " +
				" "+riqlj+" ) dc  where dc.fenx='����'\n" + 
				"       and r.diancxxb_id(+)=dc.id and sl.diancxxb_id(+)=dc.id and dc.id=px.diancxxb_id(+) and px.kouj='�±�'\n" + 
				"       group by rollup (dc.leix,dc.mingc)\n" + 
				"  order by grouping(dc.leix) desc,max(dc.xuh),grouping(dc.mingc) desc,max(px.xuh)) union\n" + 
				
				"select diancmc,fenx,rownum as xuh,rucsl,rucrl,rucsf,rulsl,rulrl,rulsf,\n" + 
				"  case when rulsl=0 or rucsl=0 then '-' else to_char(rucrl-rulrl) end as rezctzq,\n" + 
				"   case when rulsl=0 or rucsl=0 then '-' else to_char(round((rucrl-rulrl)*1000/4.1816,0)) end as rezctzqdk,\n" + 
				"   case when rulsl=0 or rucsl=0 then '-' else to_char(round(rucrl-rulrl*(100-rucsf)/(100-rulsf),2)) end as rezctzh,\n" + 
				"   case when rulsl=0 or rucsl=0 then '-' else to_char(round(round(rucrl-rulrl*(100-rucsf)/(100-rulsf),2)*1000/4.1816,0)) end as rezctzhdk,beiz\n" + 
				" from (\n" + 
				"select  decode(grouping(dc.leix)+grouping(dc.mingc),2,'"+getTreeDiancmc(this.getTreeid())+"',1,'��'||dc.leix||'С��',max(dc.mingc)) as diancmc,\n" + 
				"  max(dc.fenx) as fenx,sum(r.rucsl) as rucsl,\n" + 
				"  decode(grouping(dc.mingc),1,decode(sum(decode(sl.rulsl,0,0,r.rucsl)),0,0,\n" + 
				"                                    round(sum(decode(sl.rulsl,0,0,r.rucsl)*r.rucrl)/sum(decode(sl.rulsl,0,0,r.rucsl)),2)),\n" + 
				"                              round(decode(sum(r.rucsl),0,0,sum(r.rucsl*r.rucrl)/sum(r.rucsl)),2)) as rucrl,\n" + 
				"  decode(grouping(dc.mingc),1,decode(sum(decode(sl.rulsl,0,0,r.rucsl)),0,0,\n" + 
				"                                    round(sum(decode(sl.rulsl,0,0,r.rucsl)*r.rucsf)/sum(decode(sl.rulsl,0,0,r.rucsl)),2)),\n" + 
				"                              round(decode(sum(r.rucsl),0,0,sum(r.rucsl*r.rucsf)/sum(r.rucsl)),2)) as rucsf,\n" + 
				"  sum(r.rulsl) as rulsl,\n" + 
				"  decode(grouping(dc.mingc),1,decode(sum(decode(sl.rucsl,0,0,r.rulsl)),0,0,\n" + 
				"                                   round(sum(decode(sl.rucsl,0,0,r.rulsl)*r.rulrl)/sum(decode(sl.rucsl,0,0,r.rulsl)),2)),\n" + 
				"                              round(decode(sum(r.rulsl),0,0,sum(r.rulsl*r.rulrl)/sum(r.rulsl)),2)) as rulrl,\n" + 
				"  decode(grouping(dc.mingc),1,decode(sum(decode(sl.rucsl,0,0,r.rulsl)),0,0,\n" + 
				"                                   round(sum(decode(sl.rucsl,0,0,r.rulsl)*r.rulsf)/sum(decode(sl.rucsl,0,0,r.rulsl)),2)),\n" + 
				"                              round(decode(sum(r.rulsl),0,0,sum(r.rulsl*r.rulsf)/sum(r.rulsl)),2)) as rulsf,\n" + 
				"  decode(grouping(dc.mingc),1,'',max(r.beiz)) as beiz\n" + 
				"  from rezcb r,dianckjpxb px,\n" + 
				"(select s.diancxxb_id,decode(s.rulsl,0,0,s.rucsl) as rucsl, decode(s.rucsl,0,0,s.rulsl) as rulsl from\n" + 
				"(select r.diancxxb_id,sum(r.rucsl) as rucsl,sum(r.rulsl) as rulsl from rezcb r\n" + 
				"  where  "+riqlj+"\n" + 
				"  group by r.diancxxb_id) s) sl,\n" + 
				" (select distinct dc.id,dc.mingc,fx.fenx,dl.mingc as leix,dl.xuh from diancxxb dc,dianclbb dl,rezcb r,\n" + 
				"       (select decode(1,1,'����','') as fenx from dual union select decode(1,1,'�ۼ�','') as fenx from dual ) fx\n" + 
				"       where dc.dianclbb_id=dl.id and r.diancxxb_id=dc.id  "+strdiancid+" and   " +
				""+riqlj+" ) dc  where dc.fenx='�ۼ�'\n" + 
				"       and  "+riqlj+"\n" + 
				"       and r.diancxxb_id(+)=dc.id and sl.diancxxb_id(+)=dc.id and dc.id=px.diancxxb_id(+) and px.kouj='�±�'\n" + 
				"       group by rollup (dc.leix,dc.mingc)\n" + 
				"  order by grouping(dc.leix) desc,max(dc.xuh),grouping(dc.mingc) desc,max(px.xuh))  order by xuh,fenx\n" + 
				"  )";

			
			ArrHeader=new String[2][13];
			ArrHeader[0]=new String[] {"��λ","��λ","�볧ú","�볧ú","�볧ú","��¯ú","��¯ú","��¯ú","ˮ�ֵ���ǰ<br>��ֵ��","ˮ�ֵ���ǰ<br>��ֵ��","ˮ�ֵ�����<br>��ֵ��","ˮ�ֵ�����<br>��ֵ��","��ע"};
			ArrHeader[1]=new String[] {"��λ","��Ŀ","����(t)","Qnet,ar(MJ/kg)","Mt(%)","����(t)","Qnet,ar(MJ/kg)","Mt(%)","mj/kg","��/����","mj/kg","��/����","��ע"};
			ArrWidth=new int[] {90,40,60,50,40,60,50,40,40,40,40,40,100};
			ArrFormat=new String[]{"","","0","0.00","0.00","0","0.00","0.00","0.00","0","0.00","0",""};
			iCol=10;
			iFixedRows = 2;

		}
//		System.out.print(strSQL);
		// ֱ���ֳ��ֿ����
//		ArrHeader = new String[2][12];
//		ArrHeader[0] = new String[] { "��λ", "�볧ú", "�볧ú", "�볧ú", "��¯ú", "��¯ú",
//				"��¯ú", "ˮ�ֵ���ǰ��ֵ��", "ˮ�ֵ���ǰ��ֵ��", "ˮ�ֵ�������ֵ��", "ˮ�ֵ�������ֵ��", "��ע" };
//		ArrHeader[1] = new String[] { "��λ", "����(t)", "Qnet,ar(MJ/kg)", "Mt(%)",
//				"����(t)", "Qnet,ar(MJ/kg)", "Mt(%)", "mj/kg", "��/����", "mj/kg",
//				"��/����", "��ע" };
//
//		ArrWidth = new int[] { 130, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50 };
//		String ArrFormat[] = new String[] { "", "0", "0.00", "0.00", "0",
//				"0.00", "0.00", "0.00", "0", "0.00", "0", "" };
//		iCol = 10;
		
		
//		 System.out.println(strSQL);
//		iFixedRows = 1;
		ResultSet rs = cn.getResultSet(strSQL);

		// ����
		rt.setBody(new Table(rs, 2, 0, iFixedRows));

		rt.setTitle(titledate + titlename, ArrWidth);
		  Visit visit=((Visit) getPage().getVisit());
		   String zhibdw=this.getDiancName();
		if(zhibdw.equals("��������ȼ�����޹�˾")&&visit.getRenyjb()==2){
			zhibdw="���ƹ��ʷ���ɷ����޹�˾ȼ�Ϲ���";
		}
		rt.setDefaultTitle(1, 5, "�Ʊ�λ:"+zhibdw, Table.ALIGN_LEFT);
		//rt.setDefaultTitle(iCol, 3, "�Ʊ�����:" + FormatDate(getBeginriqDate()),Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(36);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.setColFormat(ArrFormat);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(10,3,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
		 
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		// System.out.println(rt.getAllPagesHtml());
		return rt.getAllPagesHtml();
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

	// Page����
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}



	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
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

	public Date getYesterday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	public Date getMonthFirstday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
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

	// �󱨱�����
	public boolean _Baoblxchange = false;

	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if (_BaoblxValue == null) {
			_BaoblxValue = (IDropDownBean) getIBaoblxModels().getOption(0);
		}
		return _BaoblxValue;
	}

	public void setBaoblxValue(IDropDownBean Value) {
		long id = -2;
		if (_BaoblxValue != null) {
			id = _BaoblxValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Baoblxchange = true;
			} else {
				_Baoblxchange = false;
			}
		}
		_BaoblxValue = Value;
	}

	private IPropertySelectionModel _IBaoblxModel;

	public void setIBaoblxModel(IPropertySelectionModel value) {
		_IBaoblxModel = value;
	}

	public IPropertySelectionModel getIBaoblxModel() {
		if (_IBaoblxModel == null) {
			getIBaoblxModels();
		}
		return _IBaoblxModel;
	}

	public IPropertySelectionModel getIBaoblxModels() {
		JDBCcon con = new JDBCcon();
		try {
			List fahdwList = new ArrayList();
			fahdwList.add(new IDropDownBean(0, "�ܱ�"));
			fahdwList.add(new IDropDownBean(1, "�±�"));
			fahdwList.add(new IDropDownBean(2, "��ʱ��β�ѯ"));

			_IBaoblxModel = new IDropDownModel(fahdwList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IBaoblxModel;
	}
	
	
//	 �±�����(�¼ӵ�)  ----------------------------------------------------------------------------
	public boolean _Leixchange = false;

	private IDropDownBean _LeixValue;

	public IDropDownBean getLeixValue() {
		if (_LeixValue == null) {
			_LeixValue = (IDropDownBean) getILeixModels().getOption(0);
		}
		return _LeixValue;
	}

	public void setLeixValue(IDropDownBean Value) {
		long id = -2;
		if (_LeixValue != null) {
			id = _LeixValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Leixchange = true;
			} else {
				_Leixchange = false;
			}
		}
		_LeixValue = Value;
	}

	private IPropertySelectionModel _ILeixModel;

	public void setILeixModel(IPropertySelectionModel value) {
		_ILeixModel = value;
	}

	public IPropertySelectionModel getILeixModel() {
		if (_ILeixModel == null) {
			getILeixModels();
		}
		return _ILeixModel;
	}

	public IPropertySelectionModel getILeixModels() {
		JDBCcon con = new JDBCcon();
		try {
			List leixList = new ArrayList();
			leixList.add(new IDropDownBean(0, "����"));
			leixList.add(new IDropDownBean(1, "�ۼ�"));
			leixList.add(new IDropDownBean(2, "�ۺ�"));

			_ILeixModel = new IDropDownModel(leixList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _ILeixModel;
	}
	//--------------------------------------------------------------------------------------------

	// �ֹ�˾������
	private boolean _fengschange = false;

	public IDropDownBean getFengsValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getFengsModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setFengsValue(IDropDownBean Value) {
		if (getFengsValue().getId() != Value.getId()) {
			_fengschange = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public IPropertySelectionModel getFengsModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getFengsModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setDiancxxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public void getFengsModels() {
		String sql;
		sql = "select id ,mingc from diancxxb where jib=2 order by id";
		setDiancxxModel(new IDropDownModel(sql));
	}

	// �����û��õĺͷֹ�˾����������ĵ糧������
	public boolean _diancmcchange1 = false;

	private IDropDownBean _DiancmcValue1;

	public IDropDownBean getDiancmcValue1() {
		if (_DiancmcValue1 == null) {
			_DiancmcValue1 = (IDropDownBean) getIDiancmcModel1s().getOption(0);
		}
		return _DiancmcValue1;
	}

	public void setDiancmcValue1(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue1 != null) {
			id = _DiancmcValue1.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange1 = true;
			} else {
				_diancmcchange1 = false;
			}
		}
		_DiancmcValue1 = Value;
	}

	private IPropertySelectionModel _IDiancmcModel1;

	public void setIDiancmcModel1(IPropertySelectionModel value) {
		_IDiancmcModel1 = value;
	}

	public IPropertySelectionModel getIDiancmcModel1() {
		if (_IDiancmcModel1 == null) {
			getIDiancmcModel1s();
		}
		return _IDiancmcModel1;
	}

	public IPropertySelectionModel getIDiancmcModel1s() {

		String sql = "";
		long fenggsId = this.getFengsValue().getId();
		sql = "select d.id,d.mingc from diancxxb d where d.fuid=" + fenggsId
				+ " order by d.mingc desc";

		_IDiancmcModel1 = new IDropDownModel(sql);
		return _IDiancmcModel1;
	}

	// /����

	// �ֹ�˾�û�����ʱ��ʾ�ĵ糧����������
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

	/*public IPropertySelectionModel getIDiancmcModels() {

		String sql = "";
		long diancId = ((Visit) getPage().getVisit()).getDiancxxb_id();
		sql = "select d.id,d.mingc from diancxxb d where d.fuid=" + diancId
				+ " order by d.mingc desc";
		// System.out.println(sql);

		_IDiancmcModel = new IDropDownModel(sql);
		return _IDiancmcModel;
	}*/

	public void getIDiancmcModels() {
		
		String sql="";
		sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);
		

	}
	
	
//	�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
//	�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getTreeDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

	}
	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("����:"));
		DateField df = new DateField();
		df.setValue(DateUtil.FormatDate(this.getBeginriqDate()));
		df.Binding("riqDateSelect","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setWidth(100);
		//df.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		
		//-----------------------------------------------
		if (this.getBaoblxValue().getValue().equals("��ʱ��β�ѯ")) {

			tb1.addText(new ToolbarText("��"));
			DateField df1 = new DateField();
			df1.setValue(DateUtil.FormatDate(this.getEndriqDate()));
			df1.Binding("riqEndSelect", "forms[0]");
			df1.setWidth(100);
			tb1.addField(df1);
			tb1.addText(new ToolbarText("-"));

		}
		//--------------------------------------------------
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		

		tb1.addText(new ToolbarText("����:"));
		ComboBox leix = new ComboBox();
		leix.setTransform("BaoblxDropDown");
		
		leix.setWidth(100);
		leix.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(leix);
		tb1.addText(new ToolbarText("-"));
		
//		������±�������ʱ�������µĿ�----------------------------------------------------
		if(this.getBaoblxValue().getValue().equals("�±�")){
			
			tb1.addText(new ToolbarText("�ھ�:"));
			ComboBox cblx = new ComboBox();
			cblx.setTransform("LeixDropDown");
			cblx.setWidth(100);
			cblx.setListeners("select:function(){document.Form0.submit();}");
			tb1.addField(cblx);
			tb1.addText(new ToolbarText("-"));
		}
		
		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
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

	private String treeid;

	/*public String getTreeid() {
		if (treeid == null || "".equals(treeid)) {
			return "-1";
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}*/
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
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