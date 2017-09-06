package com.zhiren.gangkjy.jiexdy.zhuangcgl;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.dtrlgs.faygl.faygs.FaycbBean;
import com.zhiren.dtrlgs.faygl.faygs.FayzgInfo;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Qumxx extends BasePage implements PageValidateListener {
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
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
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

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private boolean _ReturnClick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveClick) {
			_SaveClick = false;
			if(saveChk()){return;}
		}
		if (_ReturnClick) {
			_ReturnClick = false;
			if(((Visit) getPage().getVisit()).getString2()==null || ((Visit) getPage().getVisit()).getString2().equals("")){
				setMsg("�û���Ϣ����");
				cycle.activate("Welcome");
			}else{
				cycle.activate(((Visit) getPage().getVisit()).getString2());
			}
		}
	}

	private void initGrid() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = 
			"select q.id,\n" +
			"gs.mingc||'-'||m.mingc duow,\n" + 
			"q.chuksl,\n" + 
			"q.chukkssj,\n" + 
			"to_char(q.chukkssj,'hh24') chukksshi,\n" + 
			"to_char(q.chukkssj,'mi') chukksfen,\n" + 
			"q.chukjssj,\n" + 
			"to_char(q.chukjssj,'hh24') chukjsshi,\n" + 
			"to_char(q.chukjssj,'mi') chukjsfen,\n" + 
			"q.caozy,q.zhuangt,q.beiz,fy.zhilb_id \n" + 
			"from qumxxb q, meicb m,duowgsb gs,fayslb fy \n" + 
			"where q.meicb_id = m.id and m.duowgsb_id=gs.id and q.zhuangcb_id=fy.id \n" + 
			"and q.zhuangcb_id = " + visit.getString11();

		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.NullResult + "\nSQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		String chukkssj=DateUtil.FormatDate(new Date());
		String chukh="00";
		String chukf="00";
		String chukjssj=DateUtil.FormatDate(new Date());
		String chukjh="00";
		String chukjf="00";
		String shijSql=
			"select to_char(chukkssj,'yyyy-mm-dd') as chukkssj,\n" +
			"       to_char(chukkssj, 'hh24') as chukh,\n" + 
			"       to_char(chukkssj, 'mi') as chukf,\n" + 
			"       to_char(chukjssj,'yyyy-mm-dd') as chukjssj,\n" + 
			"       to_char(chukjssj, 'hh24') as chukjh,\n" + 
			"       to_char(chukjssj, 'mi') as chukjf \n" +
			"		from fayslb "+
			"  where id="+visit.getString11();
		
//		if(con.getHasIt(shijSql)){
			ResultSetList shijrsl=con.getResultSetList(shijSql);
			while(shijrsl.next()){
				chukkssj=shijrsl.getString("chukkssj");
				chukh=shijrsl.getString("chukh");
				chukf=shijrsl.getString("chukf");
				chukjssj=shijrsl.getString("chukjssj");
				chukjh=shijrsl.getString("chukjh");
				chukjf=shijrsl.getString("chukjf");
			}
			shijrsl.close();
//		}
		
		// �½�grid
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// ����grid�ɱ༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// ����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight");
		// �������ݲ���ҳ
		egu.addPaging(0);
		// ����gridΪ��ѡ
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		// ����grid�б���
		egu.getColumn("duow").setHeader(Local.duow);
		egu.getColumn("chuksl").setHeader(Local.chuksl);
		egu.getColumn("chukkssj").setHeader(Local.chukksrq);
		egu.getColumn("chukksshi").setHeader(Local.shi);
		egu.getColumn("chukksfen").setHeader(Local.fen);
		egu.getColumn("chukjssj").setHeader("�����������");
		egu.getColumn("chukjsshi").setHeader(Local.shi);
		egu.getColumn("chukjsfen").setHeader(Local.fen);
		egu.getColumn("caozy").setHeader(Local.caozy);
		egu.getColumn("caozy").setHidden(true);
		egu.getColumn("caozy").setEditor(null);
		egu.getColumn("zhuangt").setHeader(Local.zhuangt);
		egu.getColumn("zhuangt").setHidden(true);
		egu.getColumn("zhuangt").setEditor(null);
		egu.getColumn("beiz").setHeader(Local.beiz);
		egu.getColumn("zhilb_id").setHeader("ZHILB_ID");
//		�����п��
		egu.getColumn("duow").setWidth(120);
		egu.getColumn("chuksl").setWidth(80);
		egu.getColumn("chukkssj").setWidth(80);
		egu.getColumn("chukksshi").setWidth(40);
		egu.getColumn("chukksfen").setWidth(40);
		egu.getColumn("chukjssj").setWidth(80);
		egu.getColumn("chukjsshi").setWidth(40);
		egu.getColumn("chukjsfen").setWidth(40);
		egu.getColumn("caozy").setWidth(70);
		egu.getColumn("zhuangt").setWidth(50);
		egu.getColumn("beiz").setWidth(300);
		egu.getColumn("zhilb_id").setWidth(50);
		// ����Ĭ��ֵ
		egu.getColumn("chukkssj").setDefaultValue(chukkssj);
		egu.getColumn("chukjssj").setDefaultValue(chukjssj);
		egu.getColumn("chukksshi").setDefaultValue(chukh);
		egu.getColumn("chukksfen").setDefaultValue(chukf);
		egu.getColumn("chukjsshi").setDefaultValue(chukjh);
		egu.getColumn("chukjsfen").setDefaultValue(chukjf);
		egu.getColumn("caozy").setDefaultValue(visit.getRenymc());
		egu.getColumn("zhilb_id").setDefaultValue("0");
		egu.getColumn("zhilb_id").setEditor(null);
		egu.getColumn("zhilb_id").setHidden(true);
		egu.getColumn("zhilb_id").setUpdate(false);
		// ����������������
		// ��λ
		ComboBox duow = new ComboBox();
		egu.getColumn("duow").setEditor(duow);
		duow.setEditable(true);
	 String 	fayslb_zhuangq="";
		      if(visit.getString2().equals("Zhuangclr")){
		    	  fayslb_zhuangq="zhuangcb";
		      }else if(visit.getString2().equals("Fayxxwh")){
		    	  fayslb_zhuangq="fayslb";
		      }
		 sql=	"select d.id,gs.mingc||'-'||d.mingc as mingc from meicb d,duowgsb gs,"+fayslb_zhuangq +" f"+
	       " where d.diancxxb_id =f.diancxxb_id and d.duowgsb_id=gs.id "+
			"    and f.id="+visit.getString11()+
	       " and   d.mingc<>'ֱ��ú��' order by xuh";
		egu.getColumn("duow").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
		// ��⿪ʼʱ
		ComboBox rukkss = new ComboBox();
		egu.getColumn("chukksshi").setEditor(rukkss);
		rukkss.setEditable(true);
		List h = new ArrayList();
		for (int i = 0; i < 24; i++)
			if(i<10){
				h.add(new IDropDownBean(String.valueOf(i), "0"+String.valueOf(i)));
			}else{
			h.add(new IDropDownBean(String.valueOf(i), String.valueOf(i)));}
		egu.getColumn("chukksshi")
				.setComboEditor(egu.gridId, new IDropDownModel(h));
		// ��⿪ʼ��
		ComboBox rukksf = new ComboBox();
		egu.getColumn("chukksfen").setEditor(rukksf);
		rukksf.setEditable(true);
		List m = new ArrayList();
		for (int i = 0; i < 60; i++)
			if(i<10){
				m.add(new IDropDownBean(String.valueOf(i), "0"+String.valueOf(i)));
			}else{
			m.add(new IDropDownBean(String.valueOf(i), String.valueOf(i)));}
		egu.getColumn("chukksfen")
				.setComboEditor(egu.gridId, new IDropDownModel(m));
		// ���ʱ
		ComboBox ruks = new ComboBox();
		egu.getColumn("chukjsshi").setEditor(ruks);
		ruks.setEditable(true);
		List gh = new ArrayList();
		for (int i = 0; i < 24; i++)
			if(i<10){
				gh.add(new IDropDownBean(String.valueOf(i), "0"+String.valueOf(i)));
			}else{
			gh.add(new IDropDownBean(String.valueOf(i), String.valueOf(i)));}
		egu.getColumn("chukjsshi").setComboEditor(egu.gridId,
				new IDropDownModel(gh));
		// ����
		ComboBox rukf = new ComboBox();
		egu.getColumn("chukjsfen").setEditor(rukf);
		rukf.setEditable(true);
		List gm = new ArrayList();
		for (int i = 0; i < 60; i++)
			if(i<10){
				gm.add(new IDropDownBean(String.valueOf(i), "0"+String.valueOf(i)));
			}else{
			gm.add(new IDropDownBean(String.valueOf(i), String.valueOf(i)));}
		egu.getColumn("chukjsfen").setComboEditor(egu.gridId,
				new IDropDownModel(gm));
		// ��Ӱ�ť
		String cond= "for(var i=0;i<Kucls.length;i++){\n"+
           " everyDwKucl=0;\n"+
           " for(var j=0;j<gridDiv_ds.getCount();j++){\n"+
           "             			 var rec_ds=gridDiv_ds.getAt(j);\n"+
            "            			// alert(Kucls[i][0]+'=='+ rec_ds.get('DUOW'));\n"+
             "           			 if(Kucls[i][0]==(''+ rec_ds.get('DUOW'))&&rec_ds.get('ID')=='0'){\n"+
             "             				everyDwKucl+=Number(rec_ds.get('CHUKSL'));\n"+
              "          			 }\n"+
               "      	}\n"+
               "      if(everyDwKucl>Kucls[i][1]){\n"+
                "             alert(Kucls[i][0]+'ֻ�п��'+Kucls[i][1]);\n"+
               "              return;\n"+
               "      }\n"+
        "}";
		GridButton insert = new GridButton(GridButton.ButtonType_Insert,
				"gridDiv", egu.getGridColumns(), "");
		egu.addTbarBtn(insert);
		// ɾ����ť
//		GridButton delete = new GridButton(GridButton.ButtonType_Delete,
//				"gridDiv", egu.getGridColumns(), "");
		String insertcondition="var rec=gridDiv_sm.getSelected();if(eval(rec.get('ZHILB_ID'))>0){Ext.MessageBox.alert('��ʾ��Ϣ','����ɾ����Ӧ�Ĳ����ͻ�����Ϣ��');return;}\n";
		IGridButton delete = new IGridButton(GridButton.ButtonType_Delete,
				"gridDiv", egu.getGridColumns(), "",insertcondition);
		egu.addTbarBtn(delete);
		// ���水ť
		GridButton save = new GridButton(GridButton.ButtonType_Save_condition, "gridDiv",
				egu.getGridColumns(), "SaveButton",cond);
		egu.addTbarBtn(save);
		
//		 ��ϸ���ⰴť
		GridButton Return = new GridButton("����", "ReturnXiemxx");
		Return.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(Return);
           
		egu.addOtherScript("\ngridDiv_grid.on('beforeedit',function(e){if(e.record.get('ID')>0){e.cancel=true;}});\n");
		
		setExtGrid(egu);
		con.Close();

	}
	
	private boolean saveChk(){
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û��δ�ύ�Ķ���");
			return true;
		}
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String shijSql="select \n" +
			"       to_char(chukkssj, 'yyyymmddhh24miss') as chukkssj,\n" + 
			"       to_char(chukjssj, 'yyyymmddhh24miss') as chukjssj \n" + 
			"  from fayslb"+
			"  where id="+visit.getString11();
		
		if(con.getHasIt(shijSql)){
			ResultSetList shijrsl=con.getResultSetList(shijSql);
			long  chukkssj=0;
			long  chukjssj=0;
			while(shijrsl.next()){
				 chukkssj=Long.parseLong(shijrsl.getString("chukkssj"));
				 chukjssj=Long.parseLong(shijrsl.getString("chukjssj"));
			}

			shijrsl.close();
			ResultSetList rs = getExtGrid().getModifyResultSet(getChange());
			while(rs.next()){
				String ks = rs.getString("chukkssj").replaceAll("-", "")  + rs.getString("chukksshi") + rs.getString("chukksfen")+ "00";
				String js = rs.getString("chukjssj").replaceAll("-", "")  + rs.getString("chukjsshi") + rs.getString("chukjsfen") + "00";
				long ksd=Long.parseLong(ks);
				long jsd=Long.parseLong(js);

				if(ksd<chukkssj || jsd>chukjssj ){
					setMsg("��ʼʱ�䳬����Χ");
					con.Close();
					return true;
				}
				if(jsd<chukkssj || jsd>chukjssj){
					setMsg("����ʱ�䳬����Χ");
					con.Close();
					return true;
				}
				if(jsd<=ksd){
					setMsg("����ʱ��С�ڵ��ڿ�ʼʱ��");
					con.Close();
					return true;
				}
				if(rs.getDouble("chuksl")==0){
					setMsg("������������Ϊ0");
					con.Close();
					return true;
				}
			}
		}
		con.Close();
		save();
		initGrid();
		return false;
	}

	private void save() {

		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		List fayslb_idList=new ArrayList();
		con.setAutoCommit(false);
		String sql;
		int flag;
//		����װ������ 
		 String sqlXiem="";
	      if(visit.getString2().equals("Fayxxwh")){
	    	  sqlXiem="select to_char(fahrq,'yyyy-mm-dd') as fahrq, DIANCXXB_ID, p.mingc as pinz,"+
				"g.mingc as fahr,  f.chec as chec from fayslb f,"+
				"vwdianc g, vwpinz p where f.diancxxb_id = g.id "+
				"and f.pinzb_id = p.id and f.id="+visit.getString11();
	      }else if(visit.getString2().equals("Zhuangclr")){
	    	  sqlXiem="select to_char(z.ligsj,'yyyy-mm-dd') as fahrq,z. DIANCXXB_ID, p.mingc as pinz,\n"+
				"g.mingc as fahr,  z.hangc as chec \n"+
                "from zhuangcb z,vwdianc g, vwpinz p\n"+
                "where z.diancxxb_id = g.id \n"+
				"and z.pinzb_id = p.id\n"+
                 "and z.id="+visit.getString11();
	      }
		
		
		ResultSetList rslJingz=con.getResultSetList(sqlXiem);
		String fahrq="";
		String diancxxb_id="";
		String pinz="";
		String fahr="";
		String chec="";
		while(rslJingz.next()){
			fahrq=rslJingz.getString("FAHRQ");
			diancxxb_id=rslJingz.getString("DIANCXXB_ID");
			pinz=rslJingz.getString("PINZ");
			fahr=rslJingz.getString("FAHR");
			chec=rslJingz.getString("CHEC");
		}
		rslJingz.close();
		fayslb_idList.add(new FaycbBean(Long.parseLong(diancxxb_id),Long.parseLong(visit.getString11())));   //����ú�й�
		// ɾ������
		ResultSetList rs = getExtGrid().getDeleteResultSet(getChange());
		 
		sql = "begin \n";
		while (rs.next()) {
			ResultSetList oldKucl=con.getResultSetList("select CHUKSL, to_char(shij,'yyyy-mm-dd hh24:mi:ss') shij, MEICB_ID from duowkcb where DUIQM_ID="+rs.getString("id"));
			String Kuc="";
			String Shj="";
			String meic="";
			while(oldKucl.next()){
				Kuc=oldKucl.getString("CHUKSL");
				Shj=oldKucl.getString("SHIJ");
				meic=oldKucl.getString("MEICB_ID");
			}
			oldKucl.close();
			sql+="update duowkcb set kucl=kucl+"+Kuc+" where MEICB_ID="+meic+" and shij>to_date('"+Shj+"','yyyy-mm-dd hh24:mi:ss');\n";
			
			sql += "delete from qumxxb where id =" + rs.getString("id") + ";\n";
			
			sql+="delete from duowkcb where DUIQM_ID="+ rs.getString("id") + ";\n";
			
		}
			sql+="update fayslb set meil=(nvl((select sum(chuksl) from qumxxb where zhuangcb_id="+visit.getString11()+"),0)) where id="+visit.getString11();
		sql += "end;\n";
		if (rs.getRows() > 0) {
			flag = con.getUpdate(sql);
			if (flag == -1) {
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.DeleteDatabaseFail + "\nSQL:" + sql);
				setMsg(ErrorMessage.DeleteDatabaseFail);
				con.rollBack();
				return;
				}else{
					con.commit();
				}
		}
		
		rs.close();
		// �޸�����
		rs = getExtGrid().getModifyResultSet(getChange());
		sql = "begin \n";
		while (rs.next()) {
			long id = rs.getLong("id");
			String zhuangcb_id =  visit.getString11();
			long meicb_id = getExtGrid().getColumn("duow").combo.getBeanId(rs
					.getString("duow"));
			double chuksl = rs.getDouble("chuksl");
			
			String strriq = rs.getString("chukkssj") + " "
					+ rs.getString("chukksshi") + ":" + rs.getString("chukksfen")
					+ ":00";
			String chukkssj = DateUtil.FormatOracleDateTime(strriq);
			
			strriq = rs.getString("chukjssj") + " " + rs.getString("chukjsshi") + ":"
					+ rs.getString("chukjsfen") + ":00";
			
			String chukjssj = DateUtil.FormatOracleDateTime(strriq);
			int zhuangt = rs.getInt("zhuangt");
			String beiz = rs.getString("beiz");
			//�ж��Ƿ����ظ�ʱ��
			if(con.getHasIt("select id from duowkcb where shij="+chukjssj+" and meicb_id="+meicb_id+" and duiqm_id<>"+id+"\n")){
				this.setMsg("�������ʱ��"+strriq+"�������ظ�");
				con.rollBack();
				return ;
				
			}
			
			String sqlKuc="nvl((select kucl from( select kucl from duowkcb\n" + 
			"  where meicb_id ="+meicb_id+" and diancxxb_id="+diancxxb_id+"  \n" + 
			"  and shij< "+chukjssj+" order by shij desc)\n" + 
			"  where rownum=1),0)-"+chuksl;
			
			if (id == 0) {
				
				String newID=MainGlobal.getNewID(visit.getDiancxxb_id());
				//����װ������ 
				 String 	qumyy="";
			      if(visit.getString2().equals("Zhuangclr")){
			    	  qumyy="װ��";
			      }else if(visit.getString2().equals("Fayxxwh")){
			    	  qumyy="����";
			      }
				sql += "insert into qumxxb(id,zhuangcb_id,meicb_id,chuksl,chukkssj," +
						"chukjssj,caozy,caozsj,zhuangt,beiz,qumyy) values("+newID+"," + zhuangcb_id + "," + meicb_id + "," + chuksl + "," +
						chukkssj + "," + chukjssj + ",'" + visit.getRenymc() + "',sysdate,"+zhuangt+",'" +
						beiz + "','"+qumyy+"');\n";
				
				sql+="insert into duowkcb (ID,DIANCXXB_ID,RIQ,DUIQM_ID,MEICB_ID,LEIX,PINZ,FAHR,"+
				"CHEC,SHIJ,SHUL,KUCL,CHUKSL,LEIB,zaitml) values(getnewid("+visit.getDiancxxb_id()+"),"+diancxxb_id+",to_date('"+fahrq +"','yyyy-mm-dd'),"+newID+
				","+meicb_id+",-1,'"+pinz+"','"+fahr+"','"+chec+"',"+chukjssj+","+chuksl+","+sqlKuc+","+chuksl+",2,"+chuksl+"); \n";
				
				sql+="update duowkcb set kucl=kucl-"+chuksl+" where MEICB_ID="+meicb_id+" and shij>"+chukjssj+";\n";
				
			} else {
				
				ResultSetList oldKucl=con.getResultSetList("select CHUKSL, to_char(shij,'yyyy-mm-dd hh24:mi:ss') shij, MEICB_ID from duowkcb where DUIQM_ID="+id);
				String Chuk="";
				String Shj="";
				String meic="";
				while(oldKucl.next()){
					Chuk=oldKucl.getString("CHUKSL");
					Shj=oldKucl.getString("SHIJ");
					meic=oldKucl.getString("MEICB_ID");
				}
				sql+="update duowkcb set kucl=kucl+"+Chuk+" where MEICB_ID="+meic+" and shij>to_date('"+Shj+"','yyyy-mm-dd hh24:mi:ss'); \n";
				
				sql += "update qumxxb set \n" + " meicb_id = " + meicb_id
						+ ",\n" + " chuksl = " + chuksl + ",\n"
						+ " chukkssj = " + chukkssj + ",\n" + " chukjssj = "
						+ chukjssj + ",\n" + " caozy = '" 
						+ visit.getRenymc() + "',zhuangt="+zhuangt
						+ ", beiz = '" + beiz + "'\n"
						+ " where id=" + id + ";\n";
				
				sql+="update duowkcb set meicb_id="+meicb_id+", shul="+chuksl+",chuksl="+chuksl
				+", shij="+chukjssj+",kucl="+sqlKuc+", zaitml="+chuksl+" where DUIQM_ID="+id+"; \n";
		
				sql+="update duowkcb set kucl=kucl-"+chuksl+" where MEICB_ID="+meic+" and shij>"+chukjssj+"; \n";
				oldKucl.close();
			}
		}
		sql += "end;";
		if (rs.getRows() > 0) {
			flag = con.getUpdate(sql);
			if (flag == -1) {
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.UpdateDatabaseFail + "\nSQL:" + sql);
				setMsg(ErrorMessage.UpdateDatabaseFail);
				con.rollBack();
				return;
			}else{
				con.commit();
			}
		}
		String riqChk="select min(q.chukkssj) as riq from qumxxb q where q.zhuangcb_id="+visit.getString11();
		ResultSetList oldRiq=con.getResultSetList(riqChk);
		while(oldRiq.next()){
			riqChk=oldRiq.getString("riq");
		}
		if(riqChk.equals("") || riqChk.equals(null)){
			setMsg("��������գ���¼�����ݡ�");
			con.Close();
			return;
		}else{
				if(visit.getString2().equals("Fayxxwh")){
					sql="update fayslb set meil=(nvl((select sum(chuksl) from qumxxb where zhuangcb_id="+visit.getString11()+"),0))," +
						" chukkssj=(select min(q.chukkssj) from qumxxb q where ZHUANGCB_ID="+visit.getString11()+"), " +
						" chukjssj=(select max(q.chukjssj) from qumxxb q where ZHUANGCB_ID="+visit.getString11()+") " +
						" where id="+visit.getString11();
					flag = con.getUpdate(sql);
						if (flag == -1) {
							WriteLog.writeErrorLog(this.getClass().getName() + "\n"
									+ ErrorMessage.UpdateDatabaseFail + "\nSQL:" + sql);
							setMsg(ErrorMessage.UpdateDatabaseFail);
							con.rollBack();
							return;
						}else{
							con.commit();
							FayzgInfo.CountChengb(fayslb_idList,true);
						}
				}
//		����װ��¼������װ����
			if(visit.getString2().equals("Zhuangclr")){
				sql="update zhuangcb set zhuangcl=(nvl((select sum(chuksl) from qumxxb where zhuangcb_id="+visit.getString11()+"),0)) where id="+visit.getString11();
				flag = con.getUpdate(sql);
				if (flag == -1) {
					WriteLog.writeErrorLog(this.getClass().getName() + "\n"
							+ ErrorMessage.UpdateDatabaseFail + "\nSQL:" + sql);
					setMsg(ErrorMessage.UpdateDatabaseFail);
					con.rollBack();
					return;
				}else{
					con.commit();
				}
			}
		}	
		rs.close();
		con.Close();
		setMsg(ErrorMessage.SaveSuccessMessage);
	}

	public String getKucls(){
		Visit visit=((Visit)this.getPage().getVisit());
		JDBCcon con=new JDBCcon();
		 
		 String 	fayslb_zhuangq="";
		 String sql="";
	      if(visit.getString2().equals("Zhuangclr")){
	    	  ResultSetList rslf=con.getResultSetList("select zhuangckssj,diancxxb_id from zhuangcb where id="+visit.getString11());
	    	  String zhuangckssj="";
	    	  long diancxxb_id=0;
	    	  if(rslf.next()){
	    		  zhuangckssj=DateUtil.FormatOracleDateTime(rslf.getDate("zhuangckssj"));
	    		  diancxxb_id=rslf.getLong("diancxxb_id");
	    	  }
	    	  sql="  select meicb.mingc,nvl(du.kucl,0) kucl from\n"+
		    	 "(select d. meicb_id ,\n"+
		    	  " max(d.shij) shij \n"+
		    	 "from duowkcb d\n"+ 
		    	 " where d.meicb_id<>(select id from meicb where mingc='ֱ��ú��') and "+zhuangckssj+">shij group by d.meicb_id ) duh,duowkcb du,meicb\n"+
		    	 "where  duh.meicb_id=du.meicb_id(+) and duh.shij=du.shij(+) and meicb.id=duh.meicb_id(+) and meicb.diancxxb_id="+diancxxb_id+"\n";

	    	
	    	  fayslb_zhuangq="zhuangcb";
	      }else if(visit.getString2().equals("Fayxxwh")){
	    	  ResultSetList rslf=con.getResultSetList("select chukkssj,diancxxb_id from fayslb where id="+visit.getString11());
	    	  String chukkssj="";
	    	  long diancxxb_id=0;
	    	  if(rslf.next()){
	    		  chukkssj=DateUtil.FormatOracleDateTime(rslf.getDate("chukkssj"));
	    		  diancxxb_id=rslf.getLong("diancxxb_id");
	    	  }
	    	  sql="  select dg.mingc||'-'||meicb.mingc as mingc,nvl(du.kucl,0) kucl from\n"+
		    	 "(select d. meicb_id ,\n"+
		    	  " max(d.shij) shij \n"+
		    	 "from duowkcb d\n"+ 
		    	 " where d.meicb_id<>50112248  and "+chukkssj+">shij group by d.meicb_id ) duh,duowkcb du,meicb,duowgsb dg\n"+
		    	 "where  duh.meicb_id=du.meicb_id(+) and duh.shij=du.shij(+) and meicb.id=duh.meicb_id(+) and meicb.diancxxb_id="+diancxxb_id+" and dg.id=meicb.duowgsb_id\n";


	    	  fayslb_zhuangq="fayslb";
	      }
	/*	="select meicb.mingc,nvl(du.kucl,0) kucl from\n"+
                   "(select  meicb_id ,max(shij) shij from duowkcb  where meicb_id<>50112248 group by meicb_id ) duh,duowkcb du,meicb,"+fayslb_zhuangq+" fz\n"+
                   "where  duh.meicb_id=du.meicb_id(+) and duh.shij=du.shij(+) and meicb.id=duh.meicb_id(+) and meicb.diancxxb_id=fz.diancxxb_id and fz.id="+visit.getString11()+"\n";*/
		
		String Kucls="var Kucls=[";
		ResultSetList rsl=con.getResultSetList(sql);
		while(rsl.next()){
			Kucls+="['"+rsl.getString("mingc")+"','"+rsl.getString("kucl")+"']";
			if(!(rsl.getRow()+1==rsl.getRows())){
				Kucls+=",";
			}
		}
		Kucls+="]";
          con.Close();
		return Kucls;
	}
	private void init() {
		setExtGrid(null);
		initGrid();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			init();
		}
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
}

class IGridButton extends GridButton{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8018691269575238278L;
	public IGridButton(int btnType,String parentId,List columns,String tapestryBtnId,String condition){
		super( btnType, parentId, columns, tapestryBtnId,condition);
	}
	public String getDeleteScript() {
		StringBuffer record = new StringBuffer();
		record.append("function() {\n");
		record.append( super.condition+"\n");
		record.append("for(i=0;i<"+parentId+"_sm.getSelections().length;i++){\n");
		record.append("	record = "+parentId+"_sm.getSelections()[i];\n");
		
		StringBuffer sb = new StringBuffer();
		//sb.append(b);
		sb.append(parentId).append("_history += '<result>' ")
		.append("+ '<sign>D</sign>' ");
		
		for(int c=0;c<columns.size();c++) {
			if(((GridColumn)columns.get(c)).coltype == GridColumn.ColType_default) {
				GridColumn gc = ((GridColumn)columns.get(c));
				if(gc.update) {
					if("date".equals(gc.datatype)) {
						sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ('object' != typeof(").append("record.get('")
						.append(gc.dataIndex).append("'))?").append("record.get('")
						.append(gc.dataIndex).append("'):").append("record.get('")
						.append(gc.dataIndex).append("').dateFormat('Y-m-d'))")
						.append("+ '</").append(gc.dataIndex).append(">'\n");
					}else {
						sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ").append("record.get('").append(gc.dataIndex).append("')");
						if(!gc.datatype.equals(GridColumn.DataType_Float)) {
							sb.append(".replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')");
						}
						sb.append("+ '</").append(gc.dataIndex).append(">'\n");
					}
				}
			}
		}
		sb.append(" + '</result>' ;");
		record.append(sb);
		
		record.append("	"+parentId+"_ds.remove("+parentId+"_sm.getSelections()[i--]);}}");
		return record.toString();
	}
}