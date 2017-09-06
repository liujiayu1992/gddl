package com.zhiren.dc.huopfytj;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author yinjm
 * ������Feiytj.java(����ͳ��)
 */
/*
 * �޸����ݣ�1���Ż���ͷ�ͱ����SQL��ѯ��䡣
 *    2���޸�ҳü��ҳ�����ݺͶ��뷽ʽ��
 *    3���޸�Feiytj.page�ļ���Feiytj.html�ļ������ҳ������һ���߿�����󻯵������Լ�ҳ����ʾ����ȷ�����⡣
 * �޸�ʱ�䣺2009-09-25
 * �޸��ˣ�������
 */

public class Feiytj extends BasePage {
	
	private String _msg;

	public void setMsg(String _value) {
		this._msg = MainGlobal.getExtMessageBox(_value, false);
	}
	
	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	protected void initialize() {
		this._msg = "";
	}
	
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
	
	public boolean getRaw() {
		return true;
	}
	
//	����ʼ����
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	
//	�󶨽�������
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
	}
	
	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
	public String getPrintTable() {
		if (getSelectData().equals("û�����ݣ�")) {
			return "û�����ݣ�";
		}
		return getSelectData();
	}
	
	private boolean _RefurbishChick = false;
	
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}
	
	public String getSelectData() {
		
		JDBCcon con = new JDBCcon();
		
		Toolbar tbr = new Toolbar("tbdiv");
		tbr.addText(new ToolbarText("��ѯ���ڣ�"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");
		tbr.addField(dfb);
		tbr.addText(new ToolbarText(" �� "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");
		tbr.addField(dfe);
		tbr.addText(new ToolbarText("-"));
		
		ToolbarButton tbrtn = new ToolbarButton(null, "��ѯ", "function(){document.getElementById('RefurbishButton').click();}");
		tbrtn.setIcon(SysConstant.Btn_Icon_Search);
		tbr.addItem(tbrtn);
		tbr.addFill();
		setToolbar(tbr);
		
		Report rt = new Report();

		int iColIndex = 0;
		String strInnerSum = "";
		String strOuterSum = "";
		
//		��̬��ͷSQL��ѯ���
		String tableHeader = 
			"select item.shuib,\n" +
			"    decode(item.gfymc, 1, item.mingc, item.mingc || item.shuib) as mingc,\n" + 
			"    item.mingc as yuansmc\n" + 
			"from (select grouping(fyxm.shuib) gshuib,\n" + 
			"             grouping(fymc.mingc) gfymc,\n" + 
			"             decode(grouping(fyxm.shuib), 1, 3, fyxm.shuib) as shuib,\n" + 
			"             decode(grouping(fyxm.shuib), 1, 3, 3 - fyxm.shuib) as shuibsx,\n" + 
			"             decode(grouping(fymc.mingc), 1, decode(fyxm.shuib, 1, '��˰С��', 0, '����˰С��', '�ܼƽ��'), fymc.mingc) as mingc\n" + 
			"       from feiyxmb fyxm, feiymcb fymc\n" + 
			"       where fyxm.feiymcb_id = fymc.id\n" + 
			"       group by rollup(fyxm.shuib, fymc.mingc)) item\n" + 
			"order by item.shuibsx, item.gfymc, item.mingc";

		ResultSetList rslHeader = con.getResultSetList(tableHeader);
		
		if (rslHeader.getRows() == 0) {
			return "û�����ݣ�";
		}
		
		String ArrHeader[][] = new String[2][5 + rslHeader.getRows()];
		int ArrWidth[] = new int[5 + rslHeader.getRows()];
		while(rslHeader.next()) {
			if (rslHeader.getString("mingc").equals("��˰С��")){
				strInnerSum = strInnerSum + "sum(decode(fyxm.shuib, 1, fy.zhi)) as ��˰С��, \n";
			}else if(rslHeader.getString("mingc").equals("����˰С��")){
				strInnerSum = strInnerSum + "sum(decode(fyxm.shuib, 0, fy.zhi)) as ����˰С��, \n";
			}else if(rslHeader.getString("mingc").equals("�ܼƽ��")){
				strInnerSum = strInnerSum + "sum(fy.zhi) as �ܼƽ�� \n";
			}else{
				strInnerSum = strInnerSum + "sum(decode(fymc.mingc || fyxm.shuib, '" + rslHeader.getString("mingc")+"', fy.zhi))  as " + rslHeader.getString("mingc") + ", \n";
			}
			
			if (rslHeader.getString("mingc").equals("�ܼƽ��")){
				strOuterSum=strOuterSum +"nvl(sum(fy." + rslHeader.getString("mingc") + "),0) as " + rslHeader.getString("mingc") + "\n";
			}else{
				strOuterSum=strOuterSum +"nvl(sum(fy." + rslHeader.getString("mingc") + "),0) as " + rslHeader.getString("mingc") + ", \n";
			}
			
			if (rslHeader.getInt("shuib") == 1){
				ArrHeader[0][5 + iColIndex] = "��˰";
			}else if(rslHeader.getInt("shuib") == 0){
				ArrHeader[0][5 + iColIndex] = "����˰";
			}else{
				ArrHeader[0][5 + iColIndex] = "�ܼƽ��";
			}
			
			ArrHeader[1][5 + iColIndex] = rslHeader.getString("yuansmc");
			ArrWidth[5 + iColIndex] = 65;
			iColIndex ++;
		}
		
//		����SQL��ѯ���
		String tableBody = 
			"select\n" +
			"    decode(grouping(gys.mingc) + grouping(mkxx.mingc) + grouping(fh.daohrq), 3, '�ܼ�', 2, '�ϼ�', 1, 'С��', to_char(daohrq, 'yyyy-mm-dd')) as ����,\n" + 
			"    gys.mingc gysmc,\n" + 
			"    mkxx.mingc mkmc,\n" + 
			"    sum(fh.ches) as ches,\n" + 
			"    sum(fh.biaoz) as biaoz,\n" + 
				 strOuterSum +
			"from (select fh.id,\n" + 
				   	   strInnerSum +
			"      from fahb    fh,\n" + 
			"          chepb   cp,\n" + 
			"          danjcpb djcp,\n" + 
			"          yunfdjb yfdj,\n" + 
			"          feiyb   fy,\n" + 
			"          feiyxmb fyxm,\n" + 
			"          feiymcb fymc\n" + 
			"      where fh.id = cp.fahb_id\n" + 
			"          and cp.id = djcp.chepb_id\n" + 
			"          and djcp.yunfdjb_id = yfdj.id\n" + 
			"          and yfdj.feiylbb_id = 3\n" + 
			"          and yfdj.feiyb_id = fy.feiyb_id\n" + 
			"          and fy.feiyxmb_id = fyxm.id\n" + 
			"          and fyxm.feiymcb_id = fymc.id\n" + 
			"          and fh.fahrq between to_date('"+ getBRiq() +"', 'yyyy-MM-dd') and to_date('"+ getERiq() +"', 'yyyy-MM-dd')\n" + 
			"       group by fh.id) fy,\n" + 
			"       fahb fh,\n" + 
			"       gongysb gys,\n" + 
			"       meikxxb mkxx\n" + 
			"where fh.fahrq between to_date('"+ getBRiq() +"', 'yyyy-MM-dd') and to_date('"+ getERiq() +"', 'yyyy-MM-dd')\n" + 
			"    and fy.id = fh.id\n" + 
			"    and fh.gongysb_id = gys.id\n" + 
			"    and fh.meikxxb_id = mkxx.id\n" + 
			"group by rollup(gys.mingc, mkxx.mingc, fh.daohrq)\n" + 
			"order by grouping(fh.daohrq),\n" + 
			"    grouping(mkxx.mingc),\n" + 
			"    grouping(gys.mingc),\n" + 
			"    fh.daohrq,\n" + 
			"    gys.mingc,\n" + 
			"    mkxx.mingc";

		ResultSetList rsData = con.getResultSetList(tableBody);
		
//		�̶���ͷ
		ArrHeader[0][0] = "����";
		ArrHeader[0][1] = "������λ";
		ArrHeader[0][2] = "ú��λ";
		ArrHeader[0][3] = "����";
		ArrHeader[0][4] = "Ʊ��";
		
		ArrHeader[1][0] = "����";
		ArrHeader[1][1] = "������λ";
		ArrHeader[1][2] = "ú��λ";
		ArrHeader[1][3] = "����";
		ArrHeader[1][4] = "Ʊ��";
		
		ArrWidth[0] = 80;
		ArrWidth[1] = 120;
		ArrWidth[2] = 120;
		ArrWidth[3] = 40;
		ArrWidth[4] = 50;
		
		rt.setTitle("�� �� �� ѯ",ArrWidth);
		rt.setBody(new Table(rsData,2,0,1));
		
		rt.body.setPageRows(25);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		if (rsData.getRows() > 0) {
			rt.body.mergeCell(rsData.getRows() + 2, 1, rsData.getRows() + 2, 3);
		}
		rt.body.mergeFixedRow();
		rt.body.ShowZero = true;
		
		rt.setDefaultTitle(1, 3, "�Ʊ�λ��" + ((Visit)this.getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(5 + rslHeader.getRows() - 3, 4, "��ѯ���ڣ�" + getBRiq() + " �� " + getERiq(), Table.ALIGN_RIGHT);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()), Table.ALIGN_LEFT);
		rt.setDefautlFooter((5 + rslHeader.getRows())/2, 2, "��ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(3 + rslHeader.getRows(), 2, "�Ʊ�", Table.ALIGN_LEFT);
		rt.body.setColCells(1, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setColCells(2, Table.PER_ALIGN, Table.ALIGN_LEFT);
		rt.body.setColCells(3, Table.PER_ALIGN, Table.ALIGN_LEFT);
		
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		if(rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		con.Close();
		return rt.getAllPagesHtml();
	}
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
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().equals(getPageName())) {
			visit.setActivePageName(getPageName());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
		}
		getSelectData();
	}
}