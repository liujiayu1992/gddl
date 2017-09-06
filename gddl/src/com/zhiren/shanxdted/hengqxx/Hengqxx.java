package com.zhiren.shanxdted.hengqxx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.tree.Tree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;;

public class Hengqxx extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public String stripNonValidXMLCharacters(String in) {
	    StringBuffer out = new StringBuffer(); 
	    char current;

	    for (int i = 0; i < in.length(); i++) {
	        current = in.charAt(i); 
	                                
	        if ((current == 0x9) || (current == 0xA) || (current == 0xD)
	                || ((current >= 0x20) && (current <= 0xD7FF))
	                || ((current >= 0xE000) && (current <= 0xFFFD))
	                || ((current >= 0x10000) && (current <= 0x10FFFF))) {
	            out.append(current);
	        } else {
	        	out.append("@");
	        	out.append(getBASE64(String.valueOf(current)));
	        }
	    }
	    return out.toString();
	}
	
	public String Base64ToStr(String s) {
		int iPos = -1;
		if (s==null) return null;
		while((iPos=s.indexOf("@"))!=-1) {
			s = s.substring(0, iPos) 
				+ getFromBASE64(s.substring(iPos+1, iPos+5))
				+ s.substring(iPos+5);
		}
		return s;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
//		visit.getExtGrid1().Save(getChange(), visit);
		ExtGridUtil egu = visit.getExtGrid1();
		StringBuffer sb = new StringBuffer("begin \n");
		JDBCcon con = new JDBCcon();
		String sChange = getChange();
		String tableName = "Hengqxxb";
		//ת��XML�еķǷ��ַ��������Ŀ�ʼ�ַ�
		if (sChange!=null && !"".equals(sChange)) {
			sChange = stripNonValidXMLCharacters(sChange);
		}
		ResultSetList dersl = egu.getDeleteResultSet(sChange);
		while (dersl.next()) {
			if(!(egu.mokmc==null)&&!egu.mokmc.equals("")){
				String id = dersl.getString("id");
				//ɾ��ʱ������־
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_DEL,egu.mokmc,
						tableName,id);
			}
			sb.append("delete from ").append(tableName).append(" where id =")
					.append(dersl.getString(0)).append(";\n");
		}
		dersl.close();
		ResultSetList mdrsl = egu.getModifyResultSet(sChange);
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			if ("0".equals(mdrsl.getString("ID"))) {
				sb.append("insert into ").append(tableName).append("(id");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					sb.append(",").append(mdrsl.getColumnNames()[i]);

					sql2.append(",").append(
						egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),
								Base64ToStr(mdrsl.getString(i))));

				}
				sb.append(") values(").append(sql2).append(");\n");
			} else {
				if(!(egu.mokmc==null)&&!egu.mokmc.equals("")){
					String id = mdrsl.getString("id");
					//����ʱ������־
					MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
							SysConstant.RizOpType_UP,egu.mokmc,
							tableName,id);
				}
				sb.append("update ").append(tableName).append(" set ");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					sb.append(mdrsl.getColumnNames()[i]).append(" = ");
					sb.append(
							egu.getValueSql(egu.getColumn(mdrsl.getColumnNames()[i]),
									Base64ToStr(mdrsl.getString(i)))).append(",");
				}
				sb.deleteCharAt(sb.length() - 1);
				sb.append(" where id =").append(mdrsl.getString("ID")).append(
						";\n");
			}
		}
		mdrsl.close();
		sb.append("end;");
		int flag = con.getUpdate(sb.toString());
		con.Close();
	}
	
	private boolean _RefreshChick = false;
	
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String str = "";
		String sql="";
		
		String toaijsql=" select * from renyzqxb qx,zuxxb z,renyxxb r where qx.zuxxb_id=z.id and qx.renyxxb_id=r.id\n" +
		" and z.mingc='hengqxxpz' and r.id="+visit.getRenyID();//zuxxb���������

		ResultSetList rs=con.getResultSetList(toaijsql);
		
		boolean flag=false;//��
		
		if(rs.next()){
			flag=true;//ҳ���Ƿ�ɱ༭
		}
		
		//��λ����������
		String dcSql = "select id,mingc from diancxxb where id=" + visit.getDiancxxb_id();
		if (visit.isFencb()) {
			ResultSetList rss = con.getResultSetList("select * from diancxxb where fuid=" + visit.getDiancxxb_id());
			if (rss.next()) {
				dcSql = "select id,mingc from diancxxb where fuid=" + visit.getDiancxxb_id() + "order by xuh";
			} 
		}
		
		String dcid=" and d.id="+this.getTreeid()+" ";
		
	    ResultSetList hasfenc=con.getResultSetList(" select * from diancxxb where fuid="+this.getTreeid());
	    
	    if(hasfenc.next()){
	    	dcid=" ";
	    }
	    
	    String zidyQZ = "";
	    if("304".equals(this.getTreeid())){
	    	zidyQZ = ",5,'�ֹ��к�'";
	    }
		
		sql=
			"SELECT H.ID,\n" +
			"       H.XUH,\n" + 
			"       D.MINGC DIANCXXB_ID,\n" + 
			"       H.IP,\n" + 
			"       decode(H.ZCQC,-1,'��ѡ��',0,'���',1,'�غ�',2,'����',3,'����',4,'����'"+zidyQZ+") AS ZCQC,\n" + 
			"       H.QICHH,\n" + 
			"       H.QICHLJCX,\n" + 
			"       H.HENGQPORT,\n" + 
			"       H.STRBEGIN,\n" + 
			"       H.shujw,\n" + 
			"       decode(H.DUQFS,-1,'��ѡ��',0,'��������',1,'��������') AS DUQFS,\n" + 			
			"       H.CARDTYPE,\n" + 
			"       H.CARDPORT,\n" + 
			"       decode(H.CAIYFS,-1,'��ѡ��',0,'����',1,'�˲�',2,'����(������)') as CAIYFS,\n" +
			"       decode(H.ISFENY,-1,'��ѡ��',0,'��','��') AS ISFENY,\n" + 
			"       H.FENYDS,\n" + 
			"       decode(H.HETGL,-1,'��ѡ��',0,'��','��') AS HETGL,\n" + 
			"       H.MAXWEIGHT,\n" +
			"       H.BEIZ\n" + 
			"  FROM HENGQXXB H, DIANCXXB D\n" + 
			" WHERE H.DIANCXXB_ID = D.ID\n" + 
			" " +dcid+
			" ORDER BY D.MINGC, H.XUH";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);	
		egu.addPaging(24);
		egu.setTableName("hengqxxb");
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setHeader("id");
		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("xuh").setWidth(40);
		egu.getColumn("diancxxb_id").setHeader("��λ����");
		egu.getColumn("ip").setHeader("IP��ַ");
		egu.getColumn("zcqc").setHeader("����");
		egu.getColumn("zcqc").setWidth(80);
		egu.getColumn("QICHH").setHeader("���");
		egu.getColumn("QICHH").setWidth(60);
		egu.getColumn("QICHLJCX").setHeader("��������");
		egu.getColumn("hengqport").setHeader("�����˿ں�");
		egu.getColumn("hengqport").setWidth(60);
		egu.getColumn("strbegin").setHeader("��ʼ�ַ���");
		egu.getColumn("shujw").setHeader("��Ч����");
		egu.getColumn("shujw").setWidth(60);
		egu.getColumn("duqfs").setHeader("��ȡ��ʽ");
		egu.getColumn("duqfs").setDefaultValue("��ѡ��");
		egu.getColumn("CARDTYPE").setHeader("����������");
		egu.getColumn("CARDTYPE").setDefaultValue("��ѡ��");
		egu.getColumn("cardport").setHeader("�������˿ں�");

		egu.getColumn("CAIYFS").setHeader("������ʽ");
		egu.getColumn("CAIYFS").setDefaultValue("����");
		egu.getColumn("ISFENY").setHeader("�Ƿ����");
		egu.getColumn("ISFENY").setDefaultValue("��");
		egu.getColumn("ISFENY").setWidth(60);
		egu.getColumn("FENYDS").setHeader("��������");
		egu.getColumn("FENYDS").setDefaultValue("0");
		egu.getColumn("FENYDS").setWidth(60);
		egu.getColumn("HETGL").setHeader("�Ƿ������ͬ");
		egu.getColumn("HETGL").setDefaultValue("��");
		egu.getColumn("HETGL").setWidth(80);
		egu.getColumn("MAXWEIGHT").setHeader("������");
		egu.getColumn("MAXWEIGHT").setDefaultValue("0");
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("beiz").setWidth(100);
			
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(dcSql));				
		egu.getColumn("diancxxb_id").setReturnId(true);
		
		//�س����ᳵ������
		List l = new ArrayList();
		l.add(new IDropDownBean(-1, "��ѡ��"));
		l.add(new IDropDownBean(0, "���"));
		l.add(new IDropDownBean(1, "�غ�"));
		l.add(new IDropDownBean(2, "����"));
		l.add(new IDropDownBean(3, "����"));
		l.add(new IDropDownBean(4, "����"));
		
		if("304".equals(this.getTreeid())){
			l.add(new IDropDownBean(5, "�ֹ��к�"));
	    }

		egu.getColumn("zcqc").setEditor(new ComboBox());
		egu.getColumn("zcqc").setComboEditor(egu.gridId, new IDropDownModel(l));
		egu.getColumn("zcqc").setReturnId(true);
		egu.getColumn("zcqc").setDefaultValue("");
		//��ȡ��ʽ
		List l1 = new ArrayList();
		l1.add(new IDropDownBean(-1,"��ѡ��"));
		l1.add(new IDropDownBean(0,"��������"));
		l1.add(new IDropDownBean(1,"��������"));
		egu.getColumn("duqfs").setEditor(new ComboBox());
		egu.getColumn("duqfs").setReturnId(true);
		egu.getColumn("duqfs").setComboEditor(egu.gridId, new IDropDownModel(l1));
		//������ʽ
		List lc = new ArrayList();
		lc.add(new IDropDownBean(-1,"��ѡ��"));
		lc.add(new IDropDownBean(0,"����"));
		lc.add(new IDropDownBean(1,"�˲�"));
		lc.add(new IDropDownBean(2,"����(������)"));
		egu.getColumn("caiyfs").setEditor(new ComboBox());
		egu.getColumn("caiyfs").setReturnId(true);
		egu.getColumn("caiyfs").setComboEditor(egu.gridId, new IDropDownModel(lc));
		//�Ƿ����
		List l2 = new ArrayList();
		l2.add(new IDropDownBean(-1,"��ѡ��"));
		l2.add(new IDropDownBean(1,"��"));
		l2.add(new IDropDownBean(0,"��"));
		egu.getColumn("isfeny").setEditor(new ComboBox());
		egu.getColumn("isfeny").setReturnId(true);
		egu.getColumn("isfeny").setComboEditor(egu.gridId, new IDropDownModel(l2));
		//�Ƿ��ܺ�ͬ����
		List l3 = new ArrayList();
		l3.add(new IDropDownBean(-1,"��ѡ��"));
		l3.add(new IDropDownBean(1,"��"));
		l3.add(new IDropDownBean(0,"��"));
		egu.getColumn("HETGL").setEditor(new ComboBox());
		egu.getColumn("HETGL").setReturnId(true);
		egu.getColumn("HETGL").setComboEditor(egu.gridId, new IDropDownModel(l3));
		//����������
		List l4 = new ArrayList();
		l4.add(new IDropDownBean(-1,"��ѡ��"));
		l4.add(new IDropDownBean(0,"white"));
		l4.add(new IDropDownBean(1,"black"));
		egu.getColumn("CARDTYPE").setEditor(new ComboBox());
		egu.getColumn("CARDTYPE").setComboEditor(egu.gridId, new IDropDownModel(l4));
		egu.getColumn("CARDTYPE").setReturnId(false);
		
		// �糧��
		egu.addTbarText("��λ����:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		//��ť
		egu.addToolbarButton(GridButton.ButtonType_Refresh,"RefreshButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		if (!flag) {
			egu.addOtherScript("gridDiv_grid.addListener('beforeedit',function(e){" +
					"" +
					"if(!(e.field=='ZCQC' || e.field=='CAIYFS')){\n" +
					"	 e.cancel = true;\n" +
					"\n}" +
					"" +
					"\n});");
		}

		setExtGrid(egu);
		con.Close();
	}
	
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
			this.setTreeid(visit.getDiancxxb_id()+"");
			getSelectData();
		}
		getSelectData();
	}
	
//	 �� s ���� BASE64 ���� 
	public  String getBASE64(String s) { 
		if (s == null) return null; 
		return (new sun.misc.BASE64Encoder()).encode( s.getBytes() ); 
	} 
	

//	 �� BASE64 ������ַ��� s ���н��� 
	public  String getFromBASE64(String s) { 
		if (s == null) return null; 
			BASE64Decoder decoder = new BASE64Decoder(); 
		try { 
			byte[] b = decoder.decodeBuffer(s); 
			return new String(b); 
		} catch (Exception e) { 
			return null; 
		} 
	}
	
//	boolean treechange = false;
//
//	private String treeid = "";
//
//	public String getTreeid() {
//
//		if (treeid.equals("")) {
//
//			treeid = String.valueOf(((Visit) getPage().getVisit())
//					.getDiancxxb_id());
//		}
//		return treeid;
//	}

//	public void setTreeid(String treeid) {
//
//		if (!this.treeid.equals(treeid)) {
//
//			((Visit) getPage().getVisit()).setboolean3(true);
//			this.treeid = treeid;
//		}
//	}
//
//	public ExtTreeUtil getTree() {
//		return ((Visit) this.getPage().getVisit()).getExtTree1();
//	}
//
//	public void setTree(ExtTreeUtil etu) {
//		((Visit) this.getPage().getVisit()).setExtTree1(etu);
//	}
//
//	public String getTreeHtml() {
//		return getTree().getWindowTreeHtml(this);
//	}
//
//	public String getTreeScript() {
//		return getTree().getWindowTreeScript();
//	}
//

}
