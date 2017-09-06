package com.zhiren.dc.rulgl.feihlzjc;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*����:���ܱ�
 *����:2010-4-26 11:20:15
 *����:���ӷɻҺ�̼��ƽ��ֵ���Զ�����
 * 
 */
public class Feihlzjccx extends BasePage {
	public boolean getRaw() {
		return true;
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

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getSelectData();
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}

	private String getSelectData() {
		JDBCcon con = new JDBCcon();
		
		String sql = "select mingc from jizxxb where id = " + getJizb_id();
		ResultSetList rsl = con.getResultSetList(sql);
		String jiz = "";
		if (rsl.next()) {
			jiz = rsl.getString("mingc");
		}
		//��������
		String jiandzq="";
		if(jiz.equals("#7�ɻ�¯��")||jiz.equals("#8�ɻ�¯��")){
			jiandzq="ÿ��һ��";
		}else{
			jiandzq="ÿ��һ��";
		}
		
		String fenxsj = this.getRiqi();
		if (fenxsj == null || fenxsj.equals("")) {
			fenxsj = DateUtil.FormatDate(new Date());
		}
		String fenxtime = fenxsj.substring(0, 4) + "��" + fenxsj.substring(5, 7) + "��" + fenxsj.substring(8, 10) + "��";
		
		sql = "select to_char(quysj,'yyyy-mm-dd') as quysj, beiz ,lury,shenhyyj ,shenhyej   from feihlzb where diancxxb_id = " + getTreeid() 
				+ " and jizxxb_id = " + getJizb_id() + " and fenxsj = to_date('" + fenxsj + "','yyyy-mm-dd')";
		rsl = con.getResultSetList(sql);
		String quysj = "";
		String lury="";
		String shenhyyj="";
		String shenhyej="";
		StringBuffer beiz = new StringBuffer();
		while (rsl.next()) {
			quysj = rsl.getString("quysj");
			beiz.append(rsl.getString("beiz"));
			lury=rsl.getString("lury");
			shenhyyj=rsl.getString("shenhyyj");
			shenhyej=rsl.getString("shenhyej");
		}
		
		String quytime = "";
		if (!quysj.equals("") && quysj != null) {
			quytime = quysj.substring(0, 4) + "��" + quysj.substring(5, 7) + "��" + quysj.substring(8, 10) + "��";
		}
		
		sql = "select mingc, kerw from feihlzb fh, huidxxb hd where diancxxb_id = \n" + getTreeid() 
				+ " and jizxxb_id = " + getJizb_id() + " and fenxsj = to_date('" + fenxsj + "','yyyy-mm-dd') " +
				"and fh.huidxxb_id = hd.id order by hd.xuh";
		rsl = con.getResultSetList(sql);
		String ArrHeader[][] = new String[16][5];
		ArrHeader[0] = new String[]{"ȡ��ʱ��", "ȡ��ʱ��", "", "", quytime};
		ArrHeader[1] = new String[]{"����ʱ��", "����ʱ��", "", "", fenxtime};
		ArrHeader[2] = new String[]{"�춨����", "��������", "", "", jiandzq};
		ArrHeader[3] = new String[]{"��������", "��������", "", "", jiz};
		ArrHeader[4] = new String[]{"ȡ���ص�", "ȡ���ص�", "", "", "��ȼ��(%)"};
		int t = 5;
		int hangs=rsl.getRows();
		float Huid2=0.0f;
		float Huid5=0.0f;
		float Huid8=0.0f;
		float Huid11=0.0f;
		float Huid14=0.0f;
		float Huid17=0.0f;
		String FeihhtlA="";
		String FeihhtlB="";
		if(jiz.equals("#7�ɻ�¯��")||jiz.equals("#8�ɻ�¯��")){//#7,#8���鲻�ü���ɻҺ�̼��ƽ��ֵ
			while (rsl.next()) {
				ArrHeader[t] = new String[]{rsl.getString("mingc"), rsl.getString("mingc"), "", "", rsl.getString("kerw")};
				t ++;
			}			
		}else{
			while (rsl.next()) {
				
				if(t==hangs+4){//�����һ�е���һ�м���"�ɻҺ�̼��ƽ��ֵA/B"��ָ��
					if(Huid2==0.0||Huid5==0.0||Huid8==0.0||Huid11==0.0||Huid14==0.0||Huid17==0.0){
						//���Ҷ�2���Ҷ�5���Ҷ�8���Ҷ�11���Ҷ�14���Ҷ�17����������һ��Ϊ0.0ʱ,������ɻҺ�̼��
						FeihhtlA="";
						FeihhtlB="";
					}else{
						double A=Huid2*0.8+Huid8*0.15+Huid14*0.05;
						double B=Huid5*0.8+Huid11*0.15+Huid17*0.05;
						BigDecimal a = new BigDecimal(A); 
						BigDecimal b = new BigDecimal(B); 
						float f1 = a.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
						float f2 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
						FeihhtlA=String.valueOf(f1);
						FeihhtlB=String.valueOf(f2);
					}
					
					
					if(jiz.equals("#1�ɻ�¯��")||jiz.equals("#3�ɻ�¯��")||jiz.equals("#5�ɻ�¯��")){
						ArrHeader[t] = new String[]{"�ɻҺ�̼��ƽ��ֵA/B", "�ɻҺ�̼��ƽ��ֵA/B", "", "", FeihhtlA+"/"+FeihhtlB};
					}else if(jiz.equals("#2�ɻ�¯��")||jiz.equals("#4�ɻ�¯��")||jiz.equals("#6�ɻ�¯��")){
						ArrHeader[t] = new String[]{"�ɻҺ�̼��ƽ��ֵA/B", "�ɻҺ�̼��ƽ��ֵA/B", "", "", FeihhtlB+"/"+FeihhtlA};
					}
					
					t=t+1;
					ArrHeader[t] = new String[]{rsl.getString("mingc"), rsl.getString("mingc"), "", "", rsl.getString("kerw")};
					
				}else{
					ArrHeader[t] = new String[]{rsl.getString("mingc"), rsl.getString("mingc"), "", "", rsl.getString("kerw")};
					if(rsl.getString("mingc").equals("#2�Ҷ�")){
						Huid2=Float.parseFloat(rsl.getString("kerw"));
					}else if(rsl.getString("mingc").equals("#5�Ҷ�")){
						Huid5=Float.parseFloat(rsl.getString("kerw"));
					}else if(rsl.getString("mingc").equals("#8�Ҷ�")){
						Huid8=Float.parseFloat(rsl.getString("kerw"));
					}else if(rsl.getString("mingc").equals("#11�Ҷ�")){
						Huid11=Float.parseFloat(rsl.getString("kerw"));
					}else if(rsl.getString("mingc").equals("#14�Ҷ�")){
						Huid14=Float.parseFloat(rsl.getString("kerw"));
					}else if(rsl.getString("mingc").equals("#17�Ҷ�")){
						Huid17=Float.parseFloat(rsl.getString("kerw"));
					}
					t ++;
				}
				
			}	
		}
			
		
		if (t + 4 < 16) {
			for (int i = t + 1; i < 15; i ++) {
				ArrHeader[i] = new String[]{"", "", "", "", ""};
			}
		}
		
		ArrHeader[15] = new String[]{"��ע", "��ע", beiz.toString(), beiz.toString(), beiz.toString()};
		
		int[] ArrWidth = new int[]{60,60,60,60,300};
		
		Report rt = new Report();

		rt.setBody(new Table(ArrHeader,0,0,0));
		
		rt.getArrWidth(ArrWidth, Report.PAPER_A4_WIDTH);
		rt.body.setWidth(ArrWidth);
		rt.setTitle("�� �� �� �� �� �ҡ�¯ �� �� ȼ �� �� �� �� ��", ArrWidth);
		rt.body.setPageRows(21);
		
		for (int i = 1; i <= 15; i ++) {
			rt.body.mergeCell(i, 1, i, 4);
			rt.body.setRowHeight(i, 45);			
		}
		
		rt.body.setRowHeight(16, 150);		;
		rt.body.mergeRow(16);
		
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		
		rt.setDefaultTitleRight(fenxtime, 5);
		rt.title.setFontSize(13);
		rt.body.setFontSize(13);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "��׼:"+shenhyej, Table.ALIGN_LEFT);
		rt.setDefautlFooter(3, 2, "���:"+shenhyyj, Table.ALIGN_RIGHT);
		rt.setDefautlFooter(5, 1, "����:"+lury, Table.ALIGN_RIGHT);
		rt.footer.setFontSize(13);
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		rsl.close();
		con.Close();
		return rt.getAllPagesHtml();

	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}

	private void getToolbars() {
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("����ʱ��:"));
		DateField df = new DateField();
		df.setValue(getRiqi());
		df.Binding("RIQI", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("RIQI");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("����:"));
		ComboBox jiz = new ComboBox();
		jiz.setTransform("JIZB_ID");
		jiz.setListeners("select:function(){document.Form0.submit();}");
		jiz.setLazyRender(true);
		jiz.setEditable(false);
		tb1.addField(jiz);
		tb1.addText(new ToolbarText("-"));
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(),
				"-1".equals(getTreeid()) ? null : getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(120);
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

		ToolbarButton tb = new ToolbarButton(null, "ˢ��",
				"function(){document.Form0.submit();}");

		tb1.addItem(tb);
		setToolbar(tb1);
	}

    //��¼Jizxxb��ID
	public long getJizb_id() {
		return getJizValue().getId();
	}
	
	// ���ڿؼ�
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
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);

			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);

			visit.setDefaultTree(null);
			setTreeid(null);
			this.setRiqi(null);
		}
		getToolbars();
		blnIsBegin = true;
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

    //����������
	public IDropDownBean getJizValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			if (getJizModel().getOptionCount()>0){
				((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getJizModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setJizValue(IDropDownBean Value) {
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setJizModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getJizModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getIJizModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public void getIJizModels() {		
		String sql = "select id, mingc from jizxxb order by id";
		((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql));
	}
	
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

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}
