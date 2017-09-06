package com.zhiren.hebkhh;

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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zibcgl extends BasePage implements PageValidateListener {
	// �����û���ʾ
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
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

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}

	private boolean _GuolClick = false;

	public void GuolButton(IRequestCycle cycle) {
		_GuolClick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
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
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_GuolClick) {
			_GuolClick = false;
			this.setChehao("");
			getSelectData();
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();
		if (getChehao().equals("")||(getChehao()).equals(null)) {
			sb.append("select z.id,yuanch,z.xuh,cheh,zhizs,chucrq,qiyrq,tingyrq,chex,zaizl,ziz,rongj,huanc,");
			sb.append("d.mingc chanqssdw,decode(baoyzt,1,'����',2,'����',3,'����','δ����') baoyzt,z.beiz from zibcb z, diancxxb d where z.chanqssdw = d.id order by xuh");
		} else {
			sb.append("select id,yuanch,xuh,cheh,zhizs,chucrq,qiyrq,tingyrq,chex,zaizl,ziz,rongj,huanc,");
			sb.append("d.mingc chanqssdw,decode(baoyzt,1,'����',2,'����',3,'����','δ����') baoyzt,z.beiz from zibcb z, diancxxb d\n");
			sb.append(" where z.chanqssdw = d.id and cheh like '%" + this.getChehao() + "%'\n");
			sb.append(" order by xuh");
		}

		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �����Ƿ���Ա༭
		egu.setTableName("zibcb");

		egu.getColumn("yuanch").setHeader("ԭ����");
		egu.getColumn("yuanch").setWidth(60);
		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("xuh").setWidth(50);
		egu.getColumn("cheh").setHeader("����");
		egu.getColumn("cheh").setWidth(60);
		egu.getColumn("zhizs").setHeader("���쳧��");
		egu.getColumn("zhizs").setWidth(100);
		egu.getColumn("chucrq").setHeader("��������");
		egu.getColumn("chucrq").setWidth(80);
		egu.getColumn("qiyrq").setHeader("��������");
		egu.getColumn("qiyrq").setWidth(80);
		egu.getColumn("tingyrq").setHeader("ͣ������");
		egu.getColumn("tingyrq").setWidth(80);
		egu.getColumn("chex").setHeader("����");
		egu.getColumn("chex").setWidth(50);
		egu.getColumn("zaizl").setHeader("����");
		egu.getColumn("zaizl").setWidth(50);
		egu.getColumn("ziz").setHeader("����");
		egu.getColumn("ziz").setWidth(50);
		egu.getColumn("rongj").setHeader("�ݻ�");
		egu.getColumn("rongj").setWidth(50);
		egu.getColumn("huanc").setHeader("����");
		egu.getColumn("huanc").setWidth(50);
		egu.getColumn("chanqssdw").setHeader("��Ȩ������λ");
		egu.getColumn("chanqssdw").setWidth(100);
		egu.getColumn("baoyzt").setHeader("����״̬");
		egu.getColumn("baoyzt").setWidth(100);
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("beiz").setWidth(100);

		List baoylist = new ArrayList();
		baoylist.add(new IDropDownBean(0, "��ѡ��"));
		baoylist.add(new IDropDownBean(1, "����"));
		baoylist.add(new IDropDownBean(2, "����"));
		baoylist.add(new IDropDownBean(3, "����"));
		egu.getColumn("baoyzt").setEditor(new ComboBox());
		egu.getColumn("baoyzt").setComboEditor(egu.gridId,new IDropDownModel(baoylist));
		egu.getColumn("baoyzt").setDefaultValue("��ѡ��");

		egu.getColumn("chanqssdw").setEditor(new ComboBox());
		egu.getColumn("chanqssdw").setComboEditor(egu.gridId,
				new IDropDownModel("select  d.id, d.mingc as chezmc "
						+ " from diancxxb d where fuid=" +visit.getDiancxxb_id()));
		
		egu.getColumn("chucrq").setDefaultValue(DateUtil.FormatDate(new Date()));
		egu.getColumn("qiyrq").setDefaultValue(DateUtil.FormatDate(new Date()));

		egu.getColumn("zaizl").setDefaultValue("0");
		egu.getColumn("ziz").setDefaultValue("0");
		egu.getColumn("rongj").setDefaultValue("0");
		egu.getColumn("huanc").setDefaultValue("0");
		
		
		// ����Grid����
		egu.addPaging(20);

		egu.addTbarText("��������:");
		ComboBox glfs = new ComboBox();
		glfs.setTransform("GuolSelectx");
		glfs.setWidth(130);
		glfs.setId("GuolSelectx");
		glfs.setLazyRender(true);
		glfs.setListeners("select:function(own,rec,index){Ext.getDom('GuolSelectx').selectedIndex=index;document.getElementById('GuolButton').click();"
						+ MainGlobal.getExtMessageShow("��ȴ�", "������", 200) + "}");
		egu.addToolbarItem(glfs.getScript());
		
		if(this.getGuolSelectValue().getId()==1){//���Ų���
			
			TextField theKey = new TextField();
			theKey.setWidth(100);
			theKey.setId("theKey");

			theKey.setListeners("change:function(thi,newva,oldva){ sta='';}\n");
			egu.addToolbarItem(theKey.getScript());
			// ����ext�еĵڶ���egu�����д���gridDiv�����ı������ȵ�һ����Piz������gridDiv----gridDivPiz.
			GridButton chazhao = new GridButton(
					"����/������һ��",
					"function(){\n"
							+ "       if(theKey.getValue().toString()==''){Ext.MessageBox.alert('��ʾ','������ֵ');return;}\n"
							+ "       var len=gridDiv_data.length;\n"
							+ "       var count;\n"
							+ "       if(len%"
							+ egu.getPagSize()
							+ "!=0){\n"
							+ "        count=parseInt(len/"
							+ egu.getPagSize()
							+ ")+1;\n"
							+ "        }else{\n"
							+ "          count=len/"
							+ egu.getPagSize()
							+ ";\n"
							+ "        }\n"
							+ "        for(var i=0;i<count;i++){\n"
							+ "           gridDiv_ds.load({params:{start:i*"
							+ egu.getPagSize()
							+ ", limit:"
							+ egu.getPagSize()
							+ "}});\n"
							+ "           var rec=gridDiv_ds.getRange();\n "
							+ "           for(var j=0;j<rec.length;j++){\n "
							+ "               if(rec[j].get('CHEH').toString().indexOf(theKey.getValue().toString())!=-1 ||rec[j].get('YUANCH').toString().indexOf(theKey.getValue().toString())!=-1){\n"
							+ "                 var nw=[rec[j]]\n"
							+ "                  if(sta.indexOf(rec[j].get('ID').toString())==-1){\n"
							+ "                      gridDiv_sm.selectRecords(nw);\n"
							+ "                      sta+=rec[j].get('ID').toString()+';';\n"
							+ "                       return;\n"
							+ "                  }\n"
							+ "                \n"
							+ "               }\n"
							+ "           }\n"
							+ "        }\n"
							+ "        if(sta==''){\n"
							+ "          Ext.MessageBox.alert('��ʾ','��Ҫ�ҵĳ��Ų�����');\n"
							+ "        }else{\n"
							+ "           sta='';\n"
							+ "           Ext.MessageBox.alert('��ʾ','�����Ѿ�����β');\n"
							+ "         }\n" + "      }\n");
			chazhao.setIcon(SysConstant.Btn_Icon_Search);
			egu.addTbarBtn(chazhao);
			egu.addTbarText("-");
		}else if (this.getGuolSelectValue().getId()==2) {//����ɸѡ
			
			TextField tf = new TextField();
			tf.setWidth(100);
			tf.setValue(getChehao());

			tf.setListeners("change:function(own,n,o){document.getElementById('Chehao').value = n}");
			egu.addToolbarItem(tf.getScript());

			// ˢ�°�ť
			GridButton refurbish = new GridButton(
					GridButton.ButtonType_Refresh, "gridDiv", egu
							.getGridColumns(), "RefurbishButton");
			egu.addTbarBtn(refurbish);
			egu.addTbarText("-");
		} 
			
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String str = " var url = 'http://'+document.location.host+document.location.pathname;"
				+ "var end = url.indexOf(';');"
				+ "url = url.substring(0,end);"
				+ "url = url + '?service=page/' + 'ZibcReport';"
				+ " window.open(url,'newWin');";
		GridButton print = new GridButton("��ӡȫ��", "function (){" + str + "}");
		print.setIcon(SysConstant.Btn_Icon_Print);
		egu.addTbarBtn(print);

		egu.addTbarText("-");

		egu
				.addOtherScript("gridDiv_grid.on('rowdblclick',function(own,row,e){showRizsm(row)});");

		setExtGrid(egu);
		con.Close();
	}

	private String chehao = "";

	public String getChehao() {
		return chehao;
	}

	public void setChehao(String ch) {
		chehao = ch;
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	// ��Ŀ����
	public IDropDownBean getGuolSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {

			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getGuolSelectModel()
							.getOption(0));

		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setGuolSelectValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean10()) {

			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean10(Value);
		}
	}

	public void setGuolSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getGuolSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {
			getGuolSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public void getGuolSelectModels() {
		List list = new ArrayList();
		Visit vi = (Visit) this.getPage().getVisit();
		list.add(new IDropDownBean(1, "���Ų���"));
		list.add(new IDropDownBean(2, "����ɸѡ"));
		vi.setProSelectionModel10(new IDropDownModel(list));
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
}
