package com.zhiren.jt.zdt.shengcdy.jiexsbzt;

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
	import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
	import com.zhiren.common.IDropDownModel;
	import com.zhiren.common.JDBCcon;
	import com.zhiren.common.MainGlobal;
	import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
	import com.zhiren.common.ext.ExtGridUtil;
	import com.zhiren.common.ext.ExtTreeUtil;
	import com.zhiren.common.ext.GridButton;
	import com.zhiren.common.ext.GridColumn;
	import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
	import com.zhiren.common.ext.form.TextField;
	import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

	public class Jiexsbzt extends BasePage implements PageValidateListener {
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

		public int getDataColumnCount() {
			int count = 0;
			for (int c = 0; c < getExtGrid().getGridColumns().size(); c++) {
				if (((GridColumn) getExtGrid().getGridColumns().get(c)).coltype == GridColumn.ColType_default) {
					count++;
				}
			}
			return count;
		}

//		���ڿؼ�
		boolean riqichange=false;
		private String riqi;
		public String getRiqi() {
			if(riqi==null||riqi.equals("")){
				riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay));
			}
			return riqi;
		}
		public void setRiqi(String riqi) {
			
			if(this.riqi!=null &&!this.riqi.equals(riqi)){
				this.riqi = riqi;
				riqichange=true;
			}
			 
		}
		
		public String getValueSql(GridColumn gc, String value) {
			if ("string".equals(gc.datatype)) {
				if (gc.combo != null) {
					if (gc.returnId) {
						return "" + gc.combo.getBeanId(value);
					} else {
						return "'" + value + "'";
					}
				} else {
					return "'" + value + "'";
				}

			} else if ("date".equals(gc.datatype)) {
				return "to_date('" + value + "','yyyy-mm-dd')";
			} else {
				return value;
			}
		}

		private void Save() {
			Visit visit = (Visit) this.getPage().getVisit();
//			����ģ�����ƣ���visit.getExtGrid1().Save(getChange(), visit)���ã������޸Ļ�ɾ������ʱ�����־
			visit.getExtGrid1().setMokmc(SysConstant.RizOpMokm_Jiexsbzt);
			int flag = visit.getExtGrid1().Save(getChange(), visit);
			if (flag!=-1){
				setMsg(ErrorMessage.SaveSuccessMessage);
			}
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
		}

		public void getSelectData() {

			JDBCcon con = new JDBCcon();

			String str = "";
			int treejib = this.getDiancTreeJib();
			if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
				str = " and jib=3 ";
			} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
				//str = " and dc.fuid = "+ getTreeid() + "";
				str = " and  (dc.fuid = "+ getTreeid() + " or dc.shangjgsid= "+this.getTreeid()+")";
			} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
				str = " and dc.id = " + getTreeid() + "";
			}
			String sql = "select nvl(yx.id,0) as id,dc.mingc as diancxxb_id,shebxxbid as jiexsbb_id,dc.shebmc,dc.bianh,nvl(yx.shebzt,'����') as shebzt,\n"
						+ "        nvl(riq,to_date('"+this.getRiqi()+ "','yyyy-mm-dd')) as riq,\n"
						+ "        nvl(kaisrq,to_date('"+this.getRiqi()+ "','yyyy-mm-dd')) as kaisrq,\n"
						+ "         nvl(jiesrq,to_date('"+this.getRiqi()+ "','yyyy-mm-dd')) as jiesrq,yx.shuom,yx.img \n"
						+ "  from (select yx.* from shebyxqkb yx,diancxxb dc where yx.kaisrq<=to_date('"+this.getRiqi()+ "','yyyy-mm-dd') and yx.jiesrq>=to_date('"+this.getRiqi()+ "','yyyy-mm-dd') and yx.diancxxb_id=dc.id "+str+" ) yx,\n"
						+ "       (select dc.mingc,dc.xuh as xuh1,sb.bianh,sb.mingc as shebmc, dc.id,sb.id as shebxxbid from diancxxb dc,jiexsbb sb\n"
						+ "              where dc.id=sb.diancxxb_id  "+str+ " ) dc\n"
						+ " where yx.diancxxb_id(+)=dc.id and dc.shebxxbid=yx.jiexsbb_id(+) order by dc.xuh1";

			ResultSetList rsl = con.getResultSetList(sql);
							
			ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
			egu.setTableName("shebyxqkb");
			egu.getColumn("id").setHeader("SHEBYXQKB_ID");
			egu.getColumn("id").setEditor(null);
			egu.getColumn("id").setHidden(true);
			egu.getColumn("diancxxb_id").setHeader("��λ����");
			egu.getColumn("diancxxb_id").setEditor(null);
			
			egu.getColumn("jiexsbb_id").setHeader("jiexsbb_id");
			egu.getColumn("jiexsbb_id").setHidden(true);
			egu.getColumn("jiexsbb_id").setEditor(null);
			
			egu.getColumn("shebmc").setHeader("�豸����");
			egu.getColumn("shebmc").setEditor(null);
			egu.getColumn("shebmc").setUpdate(false);
			egu.getColumn("bianh").setHeader("���");
			egu.getColumn("bianh").setEditor(null);
			egu.getColumn("bianh").setUpdate(false);
			
			egu.getColumn("shebzt").setHeader("�豸״̬");
			egu.getColumn("riq").setHeader("��ǰ����");
			egu.getColumn("riq").setHidden(true);

			egu.getColumn("kaisrq").setHeader("��ʼ����");
			egu.getColumn("jiesrq").setHeader("��������");
			egu.getColumn("shuom").setHeader("˵��");
			
			egu.getColumn("img").setHeader("ͼƬ");
			egu.getColumn("img").setHidden(true);
			
			egu.getColumn("diancxxb_id").setWidth(200);
			egu.getColumn("shuom").setWidth(200);
			
			egu.getColumn("id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
			egu.getColumn("jiexsbb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
			egu.getColumn("diancxxb_id").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
			egu.getColumn("bianh").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
			egu.getColumn("shebmc").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
			
			egu.setGridType(ExtGridUtil.Gridstyle_Edit);
			egu.addPaging(25);
			egu.getColumn("riq").setDefaultValue(this.getRiqi());
			//��λ����������
			//�糧������
			int treejib2 = this.getDiancTreeJib();
			if (treejib2 == 1) {// ѡ����ʱˢ�³����еĵ糧
				egu.getColumn("diancxxb_id").setEditor(new ComboBox());
				egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
				egu.getColumn("diancxxb_id").setReturnId(true);
			} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
				egu.getColumn("diancxxb_id").setEditor(new ComboBox());
				egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
						new IDropDownModel("select id,mingc from diancxxb where fuid="+getTreeid()+" order by mingc"));
				egu.getColumn("diancxxb_id").setReturnId(true);
			} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
				egu.getColumn("diancxxb_id").setEditor(new ComboBox());
				egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
						new IDropDownModel("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc"));
				ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc");			
				String mingc="";
				if(r.next()){
					mingc=r.getString("mingc");
				}
				egu.getColumn("diancxxb_id").setHidden(true);
				egu.getColumn("diancxxb_id").setDefaultValue(mingc);
			}	
//			---------------ҳ��js�ļ��㿪ʼ------------------------------------------
			StringBuffer sb = new StringBuffer();
			sb.append("gridDiv_grid.on('beforeedit',function(e){");
			sb.append(" if(e.field=='DIANCXXB_ID'){ e.cancel=true;}");//�糧�в�����༭
			sb.append("});");
			egu.addOtherScript(sb.toString());
			
			List l = new ArrayList();
			l.add(new IDropDownBean(0, "����"));
			l.add(new IDropDownBean(1, "����"));
			l.add(new IDropDownBean(2, "����"));
			l.add(new IDropDownBean(3, "ͣ��"));
			egu.getColumn("shebzt").setEditor(new ComboBox());
			egu.getColumn("shebzt").setComboEditor(egu.gridId, new IDropDownModel(l));
			egu.getColumn("shebzt").returnId=false;
//			 ������
			egu.addTbarText("����:");
			DateField df = new DateField();
			df.setValue(this.getRiqi());
			df.Binding("RIQI","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
			egu.addToolbarItem(df.getScript());
			// �糧��
			egu.addTbarText("��λ����:");
			ExtTreeUtil etu = new ExtTreeUtil("diancTree",
					ExtTreeUtil.treeWindowType_Dianc,
					((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
			setTree(etu);
			egu.addTbarTreeBtn("diancTree");
			egu.addTbarText("-");
			egu.addToolbarButton(GridButton.ButtonType_Delete, "");
			egu.addTbarText("-");
			egu.addToolbarButton(GridButton.ButtonType_SaveAll, "SaveButton");
			
//			String s = " var url = 'http://'+document.location.host+document.location.pathname;"
//					+ "var end = url.indexOf(';');"
//					+ "url = url.substring(0,end);"
//					+ "url = url + '?service=page/' + 'Jiexsbztreport&lx=rezc';"
//					+ " window.open(url,'newWin');";
//			egu.addToolbarItem("{"
//					+ new GridButton("��ӡ", "function (){" + s + "}").getScript()
//					+ "}");
			setExtGrid(egu);
			con.Close();
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
				this.setRiqi(null);
				setTreeid(null);
			}
			getSelectData();
		}

		

		boolean treechange = false;

		private String treeid = "";

		public String getTreeid() {

			if (treeid==null||treeid.equals("")) {

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

	}