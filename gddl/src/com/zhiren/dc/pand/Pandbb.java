package com.zhiren.dc.pand;

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

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.common.ext.tree.TreeButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author yinjm
 * �������̵㱨��
 */
/*
 * ���ߣ�������
 * ʱ�䣺2010-01-29 
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */

public class Pandbb extends BasePage implements PageValidateListener {

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
	
	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
//	�̵����������_��ʼ
	public IDropDownBean getPandbmValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null){
			if (getPandbmModel().getOptionCount()>0){
				((Visit)this.getPage().getVisit()).setDropDownBean3((IDropDownBean)getPandbmModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean3();
	}

	public void setPandbmValue(IDropDownBean Value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getPandbmModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel3() == null) {
			getPandbmModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel3();
	}
	
	public void setPandbmModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel3(value);
	}

	public void getPandbmModels() {
		String str = 
			"select pd.id, pd.bianm\n" +
			"  from pandb pd, diancxxb d\n" + 
			" where pd.diancxxb_id = d.id\n" + 
			"   and d.id = "+ this.getTreeid() +"\n" + 
			" order by pd.id desc";
		((Visit)this.getPage().getVisit()).setProSelectionModel3(new IDropDownModel(str,"��ѡ��"));
	}
//	�̵����������_����
	
	private boolean _RefurbishChick = false;
	
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}
	
	public String getPrintTable() {
		
		StringBuffer sb = new StringBuffer();
		sb.append(getPandbg()); 	// ��ȡ�̵�ú��Ϣ
		sb.append(getCanjpdbmhry());// ��ȡ�μ��̵�Ĳ��źͲ�����Ա
		
		_CurrentPage = 1;
		_AllPages = 1;
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		return sb.toString();
	}
	
	public void getSelectData() {
		
		Visit visit = (Visit)this.getPage().getVisit();
		
		Toolbar tbr = new Toolbar("tbdiv");
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win, "diancTree", "" + visit.getDiancxxb_id(), "forms[0]", null, getTreeid());
		setTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid() == null || "".equals(getTreeid()) ? "-1" : getTreeid())));

		
		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		tbr.addText(new ToolbarText("�糧��"));
		tbr.addField(tf);
		tbr.addItem(tb2);
		


		tbr.addText(new ToolbarText("-"));
		
		tbr.addText(new ToolbarText("�̵���룺"));
		ComboBox combPandbm = new ComboBox();
		combPandbm.setWidth(150);
		combPandbm.setTransform("PandbmDropDown");
		combPandbm.setLazyRender(true);
		tbr.addField(combPandbm);
		tbr.addText(new ToolbarText("-"));
		
		ToolbarButton tbrtn = new ToolbarButton(null, "ˢ��",
			"function(){document.getElementById('RefurbishButton').click();}");
		tbrtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		tbr.addItem(tbrtn);
		setToolbar(tbr);
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
	
//	�糧��_��ʼ
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id, mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	private boolean blnDiancChange=false;
	
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		String strDiancID=((Visit) getPage().getVisit()).getString3();
		if (treeid==null){
			blnDiancChange=true;
		} else if(!treeid.equals(strDiancID)){
			blnDiancChange=true;
		}
		
		((Visit) getPage().getVisit()).setString3(treeid);
	}
//	public void setTreeid(String treeid) {
//		((Visit) getPage().getVisit()).setString3(treeid);
//	}

	public DefaultTree getTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}

	public void setTree(DefaultTree etu) {
		((Visit) this.getPage().getVisit()).setDefaultTree(etu);
	}

	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	�糧��_����
	
//	��ȡ�̵�ú��Ϣ
	public String getPandbg() {
		
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
	   
		double[] pandqjhm = getPandqjhm(con);	// ��ȡ�̵�ǰ����ú��Ϣ
		double pandqjml = pandqjhm[0];			// �̵�ǰ��ú��
		double pandqhml = pandqjhm[1];			// �̵�ǰ��ú��			
		
		double[] pandhjhm = getPandhjhm(con);	// ��ȡ�̵�����ú��Ϣ
		double pandhjml = pandhjhm[0];			// �̵���ú��
		double pandhhml = pandhjhm[1];			// �̵���ú��
		double[] pandzmm =  getPandzmm(con);	// ��ȡ�̵�����ú��Ϣ
		
		double zhangmkc = pandzmm[0];			// ������������
//		double panyk = pandzmm[1];				// ��ӯ��
		double fadh = pandzmm[2]+pandzmm[15];				// �����
		double cuns = pandzmm[3];				// ����
		double yuns = pandzmm[4];				// ����
		double gongrh = pandzmm[5]+pandzmm[16];				// ���Ⱥ�
		double shuifc = pandzmm[6];				// ˮ�ֲ����
		double qity = pandzmm[7];				// ������
		double pidcqyhml = pandzmm[8];			// Ƥ����ȫ�º�ú��
		double rucmpjsf = pandzmm[9];			// �볧úƽ��ˮ��
		double rulmpjsf = pandzmm[10];			// ��¯úƽ��ˮ��
		double meidpjsf = pandzmm[11];			// ú��ƽ��ˮ��
		double ripjcml = pandzmm[12];			// ��ƽ����ú��
		double meiccmpjzl = pandzmm[13];		// ú����úƽ������
		double cunsl = pandzmm[14];				// ������
		
		String[][] meic = getMeic(con);			// ��ȡú�����ơ��̵�������ܶȡ��̵�����Ĵ�ú��
		String[][] cunmwz = getCunmwz(con);		// ��ȡ�̵��úλ�����ơ��̵�λ�ô�ú�Ĵ�ú��

		long pdtjcml = getPandtjcml(con);		// ��ȡ�̵�����Ĵ�ú��
		long pdwzcml = getPandwzcml(con);		// ��ȡ�̵�λ�ô�ú�Ĵ�ú��
		
		String ArrHeader[][] = new String[1][8];
		ArrHeader[0] = new String[] {"���","��Ŀ","��λ","����","���","��Ŀ","��λ","����"};
		int ArrWidth[] = new int[] {50,200,50,80,50,200,50,80};
		
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		Table tb = new Table(20 + meic.length + cunmwz.length, 8);
		tb.setWidth(ArrWidth);
		tb.setHeaderData(ArrHeader);
		
		int j = 1;
		for (int i = 2; i < 7; i ++) {
			tb.setCellValue(i, 1, String.valueOf(j ++));
			tb.setCellAlign(i, 1, 1);
		}
		int z = 6;
		for (int i = 7 + meic.length + cunmwz.length; i < 12 + meic.length + cunmwz.length; i ++) {
			tb.setCellValue(i, 1, String.valueOf(z ++));
			tb.setCellAlign(i, 1, 1);
		}
		int k = 14;
		for (int i = 2; i < 8; i ++) {
			tb.setCellValue(i, 5, String.valueOf(k ++));
			tb.setCellAlign(i, 5, 1);
		}
		tb.setCellValue(2, 2, "������������");
		tb.setCellValue(2, 6, "�볧úƽ��ˮ��");
		tb.setCellValue(3, 2, "�̵�ǰ��ú��");
		tb.setCellValue(3, 6, "��¯úƽ��ˮ��");
		tb.setCellValue(4, 2, "�̵�ǰ��ú��");
		tb.setCellValue(4, 6, "ú��ƽ��ˮ��");
		tb.setCellValue(5, 2, "�̵�ʱ����ú��");
		tb.setCellValue(5, 6, "��ƽ����ú��");
		tb.setCellValue(6, 2, "�̵�ϼ�ú��");
		tb.setCellValue(6, 6, "������");
		tb.setCellValue(7, 6, "������");
		tb.setCellValue(8 + meic.length, 5, String.valueOf(20 + meic.length));
		tb.setCellAlign(8 + meic.length, 5, 1);
		tb.setCellValue(8 + meic.length, 6, "ú����úƽ������");
		tb.setCellValue(8 + meic.length, 7, "KJ/Kg");
		tb.setCellAlign(8 + meic.length, 7, 1);
		
		tb.setCellValue(2, 7, "%");
		tb.setCellValue(3, 7, "%");
		tb.setCellValue(4, 7, "%");
		tb.setCellValue(5, 7, "t");
		tb.setCellValue(6, 7, "%");
		tb.setCellValue(7, 7, "%");
		tb.setCellAlign(7, 7, 1);
		
		tb.setCellValue(2, 8, String.valueOf(rucmpjsf));
		tb.setCellValue(3, 8, String.valueOf(rulmpjsf));
		tb.setCellValue(4, 8, String.valueOf(meidpjsf));
		tb.setCellValue(5, 8, String.valueOf(Math.round(ripjcml)));
		tb.setCellValue(6, 8, String.valueOf(cunsl));
		tb.setCellValue(7, 8, String.valueOf(Math.round(yuns / (pandqjml + pandhjml) * 100) / 100.0));
		tb.setCellValue(8 + meic.length, 8, String.valueOf(meiccmpjzl));
		
		for (int i = 2; i < 7; i ++) {
			tb.setCellValue(i, 3, "t");
			tb.setCellAlign(i, 3, 1);
			tb.setCellAlign(i, 7, 1);
		}
		for (int i = 7 + meic.length + cunmwz.length; i < 12 + meic.length + cunmwz.length; i ++) {
			tb.setCellValue(i, 3, "t");
			tb.setCellAlign(i, 3, 1);
		}
		tb.setCellValue(2, 4, String.valueOf(Math.round(zhangmkc)));
		tb.setCellValue(3, 4, String.valueOf(Math.round(pandqjml)));
		tb.setCellValue(4, 4, String.valueOf(Math.round(pandqhml)));
		tb.setCellValue(5, 4, String.valueOf(Math.round(zhangmkc) + Math.round(pandqjml) - Math.round(pandqhml)));
		tb.setCellValue(6, 4, String.valueOf(pdtjcml + pdwzcml));
//		���㵱��ú��ӯ��
		long calc_panyk = (pdtjcml + pdwzcml) - (Math.round(zhangmkc) + Math.round(pandqjml) - Math.round(pandqhml));
		if ((calc_panyk) >= 0) {
			tb.setCellValue(7 + meic.length + cunmwz.length, 4, String.valueOf(calc_panyk));
		} else {
			if (calc_panyk < (Math.round(ripjcml) * 0.005)) {
				tb.setCellValue(7 + meic.length + cunmwz.length, 4, "0");
			} else {
				tb.setCellValue(7 + meic.length + cunmwz.length, 4, String.valueOf(calc_panyk - Math.round(cuns)));
			}
		}
//		tb.setCellValue(7 + meic.length + cunmwz.length, 4, String.valueOf(Math.round(panyk)));
		tb.setCellValue(8 + meic.length + cunmwz.length, 4, String.valueOf(Math.round(pandhjml)));
		tb.setCellValue(9 + meic.length + cunmwz.length, 4, String.valueOf(Math.round(pandhhml)));
		tb.setCellValue(10 + meic.length + cunmwz.length, 4, String.valueOf(Math.round(pandqjml + pandhjml)));
		tb.setCellValue(11 + meic.length + cunmwz.length, 4, String.valueOf(Math.round(pandqhml + pandhhml+cuns+yuns+qity+shuifc)));
		
//		��ʾú�����ơ��̵�������ܶȡ��̵�����Ĵ�ú��
		for (int i = 1; i <= meic.length; i ++) {
			tb.setCellValue(6 + i, 1, "����");
			tb.setCellAlign(6 + i, 1, 1);
			tb.setCellValue(6 + i, 2, meic[i - 1][0]);
			tb.setCellValue(6 + i, 3, "t");
			tb.setCellAlign(6 + i, 3, 1);
			tb.setCellValue(6 + i, 4, meic[i - 1][2]);
			tb.setCellValue(7 + i, 5, String.valueOf(19 + i));
			tb.setCellAlign(7 + i, 5, 1);
			tb.setCellValue(7 + i, 6, meic[i - 1][0]+"�ۺϱ���");
			tb.setCellValue(7 + i, 7, "t/m&sup3;");
			tb.setCellAlign(7 + i, 7, 1);
			tb.setCellValue(7 + i, 8, meic[i - 1][1]);
		}
		
//		��ʾ�̵��úλ�����ơ��̵�λ�ô�ú�Ĵ�ú��
		for (int i = 1; i <= cunmwz.length; i ++) {
			tb.setCellValue(6 + meic.length + i, 1, "����");
			tb.setCellAlign(6 + meic.length + i, 1, 1);
			tb.setCellValue(6 + meic.length + i, 2, cunmwz[i - 1][0]);
			tb.setCellValue(6 + meic.length + i, 3, "t");
			tb.setCellAlign(6 + meic.length + i, 3, 1);
			tb.setCellValue(6 + meic.length + i, 4, cunmwz[i - 1][1]);
		}
		
		tb.setCellValue(7 + meic.length + cunmwz.length, 2, "����ú��ӯ��");
		tb.setCellValue(8 + meic.length + cunmwz.length, 2, "�̵���ú��");
		tb.setCellValue(9 + meic.length + cunmwz.length, 2, "�̵���ú��");
		tb.setCellValue(10 + meic.length + cunmwz.length, 2, "ȫ���ܽ�ú��");
		tb.setCellValue(11 + meic.length + cunmwz.length, 2, "ȫ���ܺ�ú��");
		
		for (int i = 12 + meic.length + cunmwz.length; i < 18 + meic.length + cunmwz.length; i ++) {
			tb.setCellValue(i, 1, "����");
			tb.setCellAlign(i, 1, 1);
			tb.setCellValue(i, 3, "t");
			tb.setCellAlign(i, 3, 1);
		}
		tb.mergeCell(12 + meic.length + cunmwz.length, 1, 17 + meic.length + cunmwz.length, 1);
		
		tb.setCellValue(12 + meic.length + cunmwz.length, 2, "�����ú��");
		tb.setCellValue(13 + meic.length + cunmwz.length, 2, "����ú��");
		tb.setCellValue(14 + meic.length + cunmwz.length, 2, "����ú��");
		tb.setCellValue(15 + meic.length + cunmwz.length, 2, "���Ⱥ�ú��");
		tb.setCellValue(16 + meic.length + cunmwz.length, 2, "ˮ�ֲ������");
		tb.setCellValue(17 + meic.length + cunmwz.length, 2, "������ú��");
		
		tb.setCellValue(12 + meic.length + cunmwz.length, 4, String.valueOf(Math.round(fadh)));
		tb.setCellValue(13 + meic.length + cunmwz.length, 4, String.valueOf(Math.round(cuns)));
		tb.setCellValue(14 + meic.length + cunmwz.length, 4, String.valueOf(Math.round(yuns)));
		tb.setCellValue(15 + meic.length + cunmwz.length, 4, String.valueOf(Math.round(gongrh)));
		tb.setCellValue(16 + meic.length + cunmwz.length, 4, String.valueOf(Math.round(shuifc)));
		tb.setCellValue(17 + meic.length + cunmwz.length, 4, String.valueOf(Math.round(qity)));
		
		int l = 11;
		for (int i = 18 + meic.length + cunmwz.length; i < 21 + meic.length + cunmwz.length; i ++) {
			tb.setCellValue(i, 1, String.valueOf(l ++));
			tb.setCellAlign(i, 1, 1);
			tb.setCellValue(i, 3, "t");
			tb.setCellAlign(i, 3, 1);
		}
		tb.setCellValue(18 + meic.length + cunmwz.length, 2, "Ƥ����ȫ�º�ú��");
		tb.setCellValue(19 + meic.length + cunmwz.length, 2, "��ĩ����ú��");
		tb.setCellValue(20 + meic.length + cunmwz.length, 2, "��ĩʵ�ʿ��ú��");
		
		tb.setCellValue(18 + meic.length + cunmwz.length, 4, String.valueOf(Math.round(pidcqyhml)));
		tb.setCellValue(19 + meic.length + cunmwz.length, 4, String.valueOf(Math.round(zhangmkc) + Math.round(pandqjml + pandhjml) - Math.round(pandqhml + pandhhml)-Math.round(cuns)));
		tb.setCellValue(20 + meic.length + cunmwz.length, 4, String.valueOf(Math.round(zhangmkc) + Math.round(pandqjml + pandhjml) - Math.round(pandqhml + pandhhml)-Math.round(cuns)));
		
		tb.setColAlign(4, 2);
		tb.setColAlign(8, 2);
		tb.ShowZero = true;
		if (meic.length != 0) {
			if (cunmwz.length != 0) {
				tb.mergeCell(7, 1, 6 + meic.length + cunmwz.length, 1);
			} else {
				tb.merge(7, 1, 6 + meic.length, 1);
			}
		} else {
			if (cunmwz.length != 0) {
				tb.merge(7, 1, 6 + cunmwz.length, 1);
			}
		}
		tb.merge(7 + meic.length + 2, 5, 20 + meic.length + cunmwz.length, 8);
		tb.setCellValue(7 + meic.length + 2, 5, "��ע��<br>&nbsp;&nbsp;&nbsp;&nbsp;" + getBeiz(con));
		tb.setCellVAlign(7 + meic.length + 2, 5, 1);
//		tb.setPageRows(50);
//		tb.mergeFixedCols();
		
		rt.setBody(tb);
		rt.body.setPageRows(rt.PAPER_ROWS);
//		���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
//		rt.body.mergeFixedCols();
		rt.setTitle("�� �� �� ��", ArrWidth);
		rt.setDefaultTitle(1, 4, "�Ʊ�λ��"+((Visit)this.getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
		rt.setDefaultTitle(6, 3, "�Ʊ����ڣ�"+DateUtil.Formatdate("yyyy��MM��dd��", new Date()), Table.ALIGN_RIGHT);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "��Ӫ���ܾ���", Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 2, "�������ܾ���", Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 1, "�Ʊ��ˣ�", Table.ALIGN_RIGHT);
		
		if(rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		
		con.Close();
		return rt.getAllPagesHtml();
	}

//	��ȡ�μ��̵�Ĳ��źͲ�����Ա
	public String getCanjpdbmhry() {
		
		Report rt = new Report();
		
		JDBCcon con = new JDBCcon();
		String renyxx = "select bum, reny from pandbmryzzb where pandb_id = "+ getPandbmValue().getId() +" group by (bum, reny) order by bum";
		ResultSetList rsl = con.getResultSetList(renyxx);
		
		Table tb = new Table(8, 8);
		int ArrWidth[] = new int[] {50,200,50,80,50,200,50,80};
		tb.setWidth(ArrWidth);
		
		StringBuffer jingyglb = new StringBuffer("��Ӫ������");
		StringBuffer shengcrlb = new StringBuffer("����ȼ�ϲ���");
		StringBuffer caiwb = new StringBuffer("��&nbsp;&nbsp;��&nbsp;&nbsp;����");
		StringBuffer shebb = new StringBuffer("��&nbsp;&nbsp;��&nbsp;&nbsp;����");
		StringBuffer ranlglb = new StringBuffer("ȼ�Ϲ�����");
		StringBuffer jiansb = new StringBuffer("��&nbsp;&nbsp;��&nbsp;&nbsp;����");
//		���漸��int�ͱ�������������ʾ����Ա���Ƹ�����5�ı���ʱ���л���
		int jingyglb_count = 1;
		int shengcrlb_count = 1;
		int caiwb_count = 1;
		int shebb_count = 1;
		int ranlglb_count = 1;
		int jiansb_count = 1;
		while(rsl.next()) {
			if (rsl.getString("bum").equals("��Ӫ����")) {
				if (jingyglb_count % 6 != 0) {
					jingyglb.append("&nbsp;").append(rsl.getString("reny")).append("&nbsp;");
				} else {
					jingyglb.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					jingyglb.append("&nbsp;").append(rsl.getString("reny")).append("&nbsp;");
				}
				jingyglb_count ++;
			}
			if (rsl.getString("bum").equals("����ȼ�ϲ���")) {
				if (shengcrlb_count % 6 != 0) {
					shengcrlb.append("&nbsp;").append(rsl.getString("reny")).append("&nbsp;");
				} else {
					shengcrlb.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					shengcrlb.append("&nbsp;").append(rsl.getString("reny")).append("&nbsp;");
				}
				shengcrlb_count ++;
			}
			if (rsl.getString("bum").equals("����")) {
				if (caiwb_count % 6 != 0) {
					caiwb.append("&nbsp;").append(rsl.getString("reny")).append("&nbsp;");
				} else {
					caiwb.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					caiwb.append("&nbsp;").append(rsl.getString("reny")).append("&nbsp;");
				}
				caiwb_count ++;
			}
			if (rsl.getString("bum").equals("�豸��")) {
				if (shebb_count % 6 != 0) {
					shebb.append("&nbsp;").append(rsl.getString("reny")).append("&nbsp;");
				} else {
					shebb.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					shebb.append("&nbsp;").append(rsl.getString("reny")).append("&nbsp;");
				}
				shebb_count ++;
			}
			
			if (rsl.getString("bum").equals("ȼ�ܲ�")) {
				if (ranlglb_count % 6 != 0) {
					ranlglb.append("&nbsp;").append(rsl.getString("reny")).append("&nbsp;");
				} else {
					ranlglb.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					ranlglb.append("&nbsp;").append(rsl.getString("reny")).append("&nbsp;");
				}
				ranlglb_count ++;
			}
			if (rsl.getString("bum").equals("����")) {
				if (jiansb_count % 6 != 0) {
					jiansb.append("&nbsp;").append(rsl.getString("reny")).append("&nbsp;");
				} else {
					jiansb.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					jiansb.append("&nbsp;").append(rsl.getString("reny")).append("&nbsp;");
				}
				jiansb_count ++;
			}
		}
		tb.mergeCell(1, 1, 1, 8);
		tb.mergeCell(2, 1, 2, 8);
		tb.mergeCell(3, 1, 3, 8);
		tb.mergeCell(5, 1, 5, 8);
		tb.setCellValue(2, 1, "�μ��̵���Ա��");
		
		tb.setColVAlign(2, 1);
		tb.setColVAlign(6, 1);
		
		tb.mergeCell(4, 2, 4, 4);
		tb.setCellValue(4, 2, jingyglb.toString());
		tb.mergeCell(4, 6, 4, 8);
		tb.setCellValue(4, 6, shengcrlb.toString());
		
		tb.mergeCell(6, 2, 6, 4);
		tb.setCellValue(6, 2, caiwb.toString());
		tb.mergeCell(6, 6, 6, 8);
		tb.setCellValue(6, 6, shebb.toString());
		
		tb.mergeCell(8, 2, 8, 4);
		tb.setCellValue(8, 2, ranlglb.toString());
		tb.mergeCell(8, 6, 8, 8);
		tb.setCellValue(8, 6, jiansb.toString());
		
		rt.setBody(tb);
		rt.body.setBorder(0, 0, 0, 0);
		rt.body.setRowCells(1, Table.PER_BORDER_BOTTOM, 0);
		rt.body.setRowCells(2, Table.PER_BORDER_BOTTOM, 0);
		rt.body.setRowCells(3, Table.PER_BORDER_BOTTOM, 0);
		rt.body.setRowCells(4, Table.PER_BORDER_BOTTOM, 0);
		rt.body.setRowCells(5, Table.PER_BORDER_BOTTOM, 0);
		rt.body.setRowCells(6, Table.PER_BORDER_BOTTOM, 0);
		rt.body.setRowCells(7, Table.PER_BORDER_BOTTOM, 0);
		rt.body.setRowCells(8, Table.PER_BORDER_BOTTOM, 0);
		
		for (int i = 1; i <= 8; i ++) {
			rt.body.setRowCells(i, Table.PER_BORDER_RIGHT, 0);
		}
		
		con.Close();
		return rt.getAllPagesHtml();
	}
	
//	��ȡú�����ơ��̵�������ܶȡ��̵�����Ĵ�ú��
	public String[][] getMeic(JDBCcon con) {
		String[][] meic = new String[0][];
		String str = 
			"select mc.mingc, ptj.mid, ptj.cunml\n" +
			"  from pandtjb ptj, meicb mc\n" + 
			" where ptj.meicb_id = mc.id\n" + 
			"   and ptj.pandb_id = " + getPandbmValue().getId();
		
		ResultSetList rsl = con.getResultSetList(str);
		if (rsl.getRows() != 0) {
			meic = new String[rsl.getRows()][3];
		}
		int i = 0;
		while(rsl.next()) {
			meic[i][0] = rsl.getString("mingc");
			meic[i][1] = String.valueOf(Math.round(rsl.getDouble("mid")*100)/100.0);
			meic[i][2] = String.valueOf(Math.round(rsl.getDouble("cunml")));
			i ++;
		}
		return meic;
	}
	
//	��ȡ�̵��úλ�����ơ��̵�λ�ô�ú�Ĵ�ú��
	public String[][] getCunmwz(JDBCcon con) {
		String[][] cunmwz = new String[0][];
		String str = 
			"select  pdcmwz.mingc, pdwzcm.cunml\n" +
			"  from pandwzcmb pdwzcm, pandcmwz pdcmwz\n" + 
			" where pdwzcm.pandcmwz_id = pdcmwz.id\n" + 
			"   and pdwzcm.pandb_id = " + getPandbmValue().getId();

		ResultSetList rsl = con.getResultSetList(str);
		if (rsl.getRows() != 0) {
			cunmwz = new String[rsl.getRows()][2];
		}
		int i = 0;
		while(rsl.next()) {
			cunmwz[i][0] = rsl.getString("mingc");
			cunmwz[i][1] = String.valueOf(Math.round(rsl.getDouble("cunml")));
			i ++;
		}
		return cunmwz;
	}
	
//	��ȡ�̵�����Ĵ�ú�� 
	public long getPandtjcml(JDBCcon con) {
		long pdtjcml = 0; 
		String str = "select ptj.cunml from pandtjb ptj where ptj.pandb_id = " + getPandbmValue().getId();
		ResultSetList rsl = con.getResultSetList(str);
		while (rsl.next()) {
			pdtjcml += Math.round(rsl.getDouble("cunml"));
		}
		return pdtjcml;
	}
	
//	��ȡ�̵�λ�ô�ú�Ĵ�ú��
	public long getPandwzcml(JDBCcon con) {
		long pdwzcml = 0;
		String str = "select pdwzcm.cunml from pandwzcmb pdwzcm where pdwzcm.pandb_id = " + getPandbmValue().getId();
		ResultSetList rsl = con.getResultSetList(str);
		while (rsl.next()) {
			pdwzcml += Math.round(rsl.getDouble("cunml"));
		}
		return pdwzcml;
	}
	public String getPandRiq(JDBCcon con, String pandBm) throws SQLException  {
		Date riq = null;
		String sRiq = "";
		String sql = "select riq from pandb where bianm='" + pandBm + "'";
		ResultSet rs = con.getResultSet(sql);
		if (rs.next()) {
			riq = rs.getDate("riq");
			sRiq = DateUtil.FormatDate(riq);
		}
		rs.close();
		return sRiq;
	}
	
//���¿��
	public double getShangYKC(JDBCcon con, String riq) throws SQLException {
		Visit visit = (Visit) this.getPage().getVisit();
		double shangykc = 0.0;
		String sql = "select kuc from yueshchjb where riq=first_day(add_months(to_date('" + riq + "','yyyy-mm-dd'),-1))"
				+ " and fenx='����' and diancxxb_id=" + visit.getDiancxxb_id();
		ResultSet rs = con.getResultSet(sql);
		if (rs.next()) {
			shangykc = rs.getDouble("kuc");
		} else {
			riq = String.valueOf((Integer.parseInt(riq.substring(5,7)) - 1));
			setMsg(riq + "�¿��Ϊ0��");
		}
		rs.close();
		return shangykc;
	}
	
//	��ȡ�̵�����ú��Ϣ
	public double[] getPandzmm(JDBCcon con) {
		double[] pandzmm = new double[17];
		String sql = 
			"select pdzmm.shangyzmkc,\n" +
			"       pdzmm.panyk,\n" + 
			"       pdzmm.fadh,\n" + 
			"       pdzmm.cuns,\n" + 
			"       pdzmm.yuns,\n" + 
			"       pdzmm.gongrh,\n" + 
			"       pdzmm.shuifc,\n" + 
			"       pdzmm.qity,\n" + 
			"       pdzmm.pidcqyhml,\n" + 
			"       pdzmm.rucmpjsf,\n" + 
			"       pdzmm.rulmpjsf,\n" + 
			"       pdzmm.meidpjsf,\n" + 
			"       pdzmm.ripjcml,\n" + 
			"       pdzmm.meiccmpjzl,\n" +
			"       pdzmm.cunsl,\n" + 
			"       pdzmm.pandhfdh,\n"+
			"       pdzmm.pandhgrh\n"+
			"  from pandzmm pdzmm\n" + 
			" where pdzmm.pandb_id = " + getPandbmValue().getId();
		
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			int columnCount = rsl.getColumnCount();
			pandzmm = new double[columnCount];
			for (int i = 0; i < columnCount; i++) {
				pandzmm[i] = rsl.getDouble(i);
			}
		}
		return pandzmm;
	}
	
	public String getBeiz(JDBCcon con) {
		String beiz = "";
		String str = "select beiz from pandb where id = " + getPandbmValue().getId();
		ResultSetList rsl = con.getResultSetList(str);
		while (rsl.next()) {
			beiz = rsl.getString("beiz");
		}
		return beiz; 
	}
	
//	��ȡ�̵�ǰ����ú
	public double[] getPandqjhm(JDBCcon con) {
		double[] pandqjhm = new double[2];
		String str = 
			"select sum(shc.dangrgm) jinm, sum(shc.haoyqkdr) haom\n" +
			"  from shouhcrbb shc\n" + 
			" where shc.riq >= (select first_day((select riq from pandb where id = "+ getPandbmValue().getId() +")) from dual)\n" + 
			"   and shc.riq < (select riq from pandb where id = "+ getPandbmValue().getId() +")";
		
		ResultSetList rsl = con.getResultSetList(str);
		while(rsl.next()) {
			pandqjhm[0] = rsl.getDouble("jinm");
			pandqjhm[1] = rsl.getDouble("haom");
		}
		return pandqjhm;
	}
	
//	��ȡ�̵�����ú
	public double[] getPandhjhm(JDBCcon con) {
		double[] pandhjhm = new double[2];
		String str = 
			"select sum(shc.dangrgm) jinm, sum(shc.haoyqkdr) haom\n" +
			"  from shouhcrbb shc\n" + 
			" where shc.riq >= (select riq from pandb where id = "+ getPandbmValue().getId() +")\n" + 
			"   and shc.riq <= (select last_day((select riq from pandb where id = "+ getPandbmValue().getId() +")) from dual)";
		
		ResultSetList rsl = con.getResultSetList(str);
		while(rsl.next()) {
			pandhjhm[0] = rsl.getDouble("jinm");
			pandhjhm[1] = rsl.getDouble("haom");
		}
		return pandhjhm;
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
		if (!visit.getActivePageName().equals(getPageName())) {
			visit.setActivePageName(getPageName());
			visit.setProSelectionModel3(null);
			visit.setDropDownBean3(null);
			this.setTreeid(null);
			setPandbmModel(null);
			setPandbmValue(null);
			//begin��������г�ʼ������
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
				
			getDiancmcModels();
		}
		if(blnDiancChange){
			blnDiancChange=false;
			setPandbmModel(null);
			setPandbmValue(null);
		}
		getSelectData();
	}
}