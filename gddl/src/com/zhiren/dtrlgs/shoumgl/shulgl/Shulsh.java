package com.zhiren.dtrlgs.shoumgl.shulgl;

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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.dtrlgs.faygl.faygs.FayzgInfo;
import com.zhiren.dtrlgs.shoumgl.shoumgs.ShoumzgInfo;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Shulsh extends BasePage {
	
	//״̬
	private int zhuangt;
	public int getZhuangt(){
		return zhuangt;
	}
	public void setZhuangt(String zhuangt){
		if("�����".equals(zhuangt)){
			this.zhuangt = 1;
		}else if("δ���".equals(zhuangt)){
			this.zhuangt = 2;
		}else{
			this.zhuangt = 3;
		}
	}

//	 �����û���ʾ
	private String msg = "";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	private String riq;
	public void setRiq(String value) {
		riq = value;
	}
	public String getRiq() {
		if ("".equals(riq) || riq == null) {
			ResultSetList rs = new JDBCcon()
//					.getResultSetList("select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') time from dual");
					.getResultSetList("select to_char(sysdate,'yyyy-mm-dd') time from dual");
			rs.next();
			riq = rs.getString("time");
		}
		return riq;
	}
	
	private String riq1;
	public void setRiq1(String value) {
		riq1 = value;
	}
	public String getRiq1() {
		if (riq1 == null) {
			setRiq1(DateUtil.FormatDate(new Date()));
		}
		return riq1;
	}

	private String riq2;
	public void setRiq2(String value) {
		riq2 = value;
	}
	public String getRiq2() {
		if (riq2 == null) {
			setRiq2(DateUtil.FormatDate(new Date()));
		}
		return riq2;
	}
	
	// ҳ��仯��¼
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
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
	
	
	//gridInwidow
	public ExtGridUtil getExtGridInWindow() {
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setExtGridInWindow(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
	}

	public String getGridInWindowScript() {
		if (getExtGridInWindow() == null) {
			return "";
		}
		return getExtGridInWindow().getGridScript();
	}

	public String getGridInWindowHtml() {
		if (getExtGridInWindow() == null) {
			return "";
		}
		return getExtGridInWindow().getHtml();
	}

	// ��ť
	private boolean _DaorChick = false;//DaorButton
	public void DaorButton(IRequestCycle cycle) {
		_DaorChick = true;
	}
	
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle){
		_RefreshChick = true;
	}
	
	private boolean _ZuofChick = false;
	public void ZuofButton(IRequestCycle cycle){
		_ZuofChick = true;
	}
	

	public void submit(IRequestCycle cycle) {
		if (_DaorChick) {
			_DaorChick = false;
			daor();
			getSelectData();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if (_ZuofChick) {
			_ZuofChick = false;
			zuof();
			getSelectData();
		}
	}
	
	private void daor() {		
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().setMokmc(SysConstant.RizOpMokm_Rlgs_Shujqr);
		if (!(this.getChange().equals("") || this.getChange() == null)) {
			JDBCcon con=new JDBCcon();
			con.setAutoCommit(false);
			StringBuffer sql = new StringBuffer();
			String change[] = this.getChange().split(";");
			List fhlist=new ArrayList();
			List fylist=new ArrayList();
			sql.append("begin \n");
			boolean flag = true;
			for (int i = 0; i < change.length; i++) {
//				System.out.println("����"+i+change[i]);
				if (change[i] == null || "".equals(change[i])) {
					continue;
				}
				String rsstr[] = change[i].split(",");
				
//				rsstr[0]:fahbtmp.id
//				rsstr[1]:fahbtmp.leix_id  ҵ������id
//				rsstr[2]:fahbtmp.fayslb_id ����id
//				rsstr[3]:fahbtmp.fahrq ��������
//				rsstr[4]:fahbtmp.hetb_id//��ú��ͬ����
//				rsstr[6]:fahbtmp.xiaoshetb_id//���ۺ�ͬ����
				rsstr[0]=rsstr[0].substring(0, rsstr[0].length());
//				System.out.println(rsstr[0]);
				if(rsstr.length == 1){//���û��򹴲�û������ҵ�����͵ļ�¼����
					continue;
				}
				long str = (getExtGrid().getColumn("leix_id").combo).getBeanId(rsstr[1])  ;//ҵ������
				String id1 = "";
				String id2 = "";
//				��ú��ͬ
				long goumhtb_id=0;
				 long xiaoshetb_id=0;
				 long keh_diancxxb_id=0;
				try {
					if(rsstr[4]!="")
					goumhtb_id=MainGlobal.getTableId("hetb", "hetbh", rsstr[4]);
//				���ۺ�ͬ
  
					if(rsstr[6]!="") xiaoshetb_id=MainGlobal.getTableId("hetb", "hetbh", rsstr[6]);
					//�ͻ�����
					if(rsstr[7]!="") keh_diancxxb_id =MainGlobal.getTableId("diancxxb","mingc",rsstr[7]); 
				} catch (Exception e) {
					// TODO �Զ����� catch ��
					e.printStackTrace();
				}		
				if(3 == str){//ֱ��	
					ResultSetList rs = con.getResultSetList("select getnewid("+ getTreeid() +") id1,getnewid("+ getTreeid() +") id2 from dual ");
					rs.next();
					id1 = rs.getString("id1");
					id2 = rs.getString("id2");
					rs.close();
					
					sql.append("insert into fahb (id, yuanid, diancxxb_id, gongysb_id, meikxxb_id, pinzb_id, faz_id, daoz_id, jihkjb_id, fahrq, daohrq, hetb_id, zhilb_id, jiesb_id, yunsfsb_id, chec, maoz, piz, jingz, biaoz, yingd, yingk, yuns, yunsl, koud, kous, kouz, koum, zongkd, sanfsl, ches, tiaozbz, yansbhb_id, lie_id, yuandz_id, yuanshdwb_id, kuangfzlb_id, liucb_id, liucztb_id, hedbz, beiz, ruccbb_id, leiid, ditjsbz, ditjsb_id, lieid, laimsl, laimzl, laimkc, guohsj, yewlxb_id, luncxxb_id, neibcg)");
					sql.append(" 	select " + id1 + ",f.yuanid,f.diancxxb_id,f.gongysb_id,f.meikxxb_id,f.pinzb_id,f.faz_id,");
					sql.append("	f.daoz_id,f.jihkjb_id,f.fahrq,f.daohrq,"+goumhtb_id+",f.zhilb_id,f.jiesb_id,f.yunsfsb_id,f.chec,f.maoz,");
					sql.append("	f.piz,f.jingz,f.biaoz,f.yingd,f.yingk,f.yuns,f.yunsl,f.koud,f.kous,f.kouz,f.koum,f.zongkd,f.sanfsl,f.ches,");
					sql.append("	f.tiaozbz,f.yansbhb_id,f.lie_id,f.yuandz_id,f.yuanshdwb_id,f.kuangfzlb_id,f.liucb_id,f.liucztb_id,");
					sql.append("	f.hedbz,f.beiz,f.ruccbb_id,f.leiid,f.ditjsbz,f.ditjsb_id,f.lieid,f.laimsl,f.laimzl,f.laimkc,f.guohsj ");
					sql.append("	," + str + ",l.id luncxxb_id ,nvl((select NEIBCG from gongysb gy where gy.id=f.gongysb_id),1)");
					sql.append(" 	from fahbtmp f,luncxxb l ");
					sql.append(" 	where f.id = " + rsstr[0] + " and f.chuanm=l.mingc(+); \n") ;
					
					sql.append("insert into fayslb(id, diancxxb_id, chec, luncxxb_id, chukkssj, chukjssj, fahrq, faz_id, daoz_id, meil, zhilb_id, shr_diancxxb_id, xiaosjsb_id, lurry, lursj, beiz, pinzb_id, daohrq, yunsfsb_id, gongysb_id, meikxxb_id, hetb_id, kuangfzlb_id, ruccbb_id, neibxs, yewlxb_id, jihkjb_id,querdhrq,keh_diancxxb_id)");
					sql.append("	select " + id2 + ",f.diancxxb_id,f.chec,l.id,f.fahrq,f.fahrq,f.fahrq,f.faz_id,");
					sql.append("	f.daoz_id,f.biaoz,f.zhilb_id,f.diancxxb_id,null," + visit.getRenyID() + ",sysdate,f.beiz,f.pinzb_id,");
					sql.append("	f.daohrq,f.yunsfsb_id,f.gongysb_id,f.meikxxb_id,"+xiaoshetb_id+",f.kuangfzlb_id,f.ruccbb_id,nvl((select NEIBXS from diancxxb where id=f.diancxxb_id),1),3,f.jihkjb_id,"+DateUtil.FormatOracleDate(DateUtil.FormatDate(new Date()))+","+keh_diancxxb_id+" ");
					sql.append("	from fahbtmp f,luncxxb l");
					sql.append("	where f.id = " + rsstr[0] + " and f.chuanm=l.mingc(+);\n");

					fhlist.add(id1);
					fylist.add(id2);
					
//					���ʱ������־
					MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
							SysConstant.RizOpType_UP,visit.getExtGrid1().getMokmc(),
							"fahb",id1);
					
//					
					sql.append("update fahbtmp ");
					sql.append("	set fahb_id = " + id1 + ",fayslb_id= " + id2 + ",leix_id=" + str + " ");
					sql.append("  ,hetb_id="+goumhtb_id+",xiaoshetb_id="+xiaoshetb_id);
					sql.append("	where id = " + rsstr[0] + "; \n");
					
					MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
							SysConstant.RizOpType_UP,visit.getExtGrid1().getMokmc(),
							"fahbtmp",rsstr[0]);
					
					String duimxxb_id = "";
					String qumxxb_id = "";					
					ResultSetList rs1 = con.getResultSetList("select getnewid("+ getTreeid() +") id from dual ");
					ResultSetList rs2 = con.getResultSetList("select getnewid("+ getTreeid() +") id from dual ");
					rs1.next();
					rs2.next();
					duimxxb_id=rs1.getString("id");
					qumxxb_id=rs2.getString("id");
					rs1.close();
					rs2.close();
					

					
					sql.append("insert into duimxxb(id,fahb_id,ruksl,meicb_id,rukkssj,ruksj,caozy)  ");
					sql.append("	select " + duimxxb_id + "," + id1 + ",nvl(f.maoz,0)-nvl(f.piz,0) jingz,m.id,f.daohrq,f.daohrq," + visit.getRenyID() + " ");
					sql.append("	from fahbtmp f,");
					sql.append("		(select id from meicb where diancxxb_id = 199 and mingc='ֱ��ú��') m \n");
					sql.append("	where f.id = " + rsstr[0] + "; \n");
					
					sql.append("insert into duowkcb(id,diancxxb_id,riq,duiqm_id,meicb_id,leix,pinz,fahr,chec,shij,shul,kucl,ruksl,chuksl,ches,biaoz,PANYK,leib) \n");
					sql.append("	select getnewid(f.diancxxb_id),f.diancxxb_id,f.daohrq riq,"+duimxxb_id+",m.id meicb_id,1 leix,p.mingc,g.quanc fahr,f.chec,f.daohrq shij,f.maoz-f.piz jingz,jingz kucl,jingz ruksl,0 chuksl,f.ches,f.biaoz ,f.yingk,f.leix_id \n");
					sql.append("	from fahbtmp f,pinzb p,gongysb g ,");
					sql.append("		(select id from meicb where diancxxb_id = 199 and mingc='ֱ��ú��') m \n");
					sql.append("	where p.id(+)=f.pinzb_id and g.id(+)=f.gongysb_id \n");
					sql.append("	and f.id = " + rsstr[0] + "; \n");
					

					sql.append("insert into qumxxb(id,zhuangcb_id,chuksl,meicb_id,chukkssj,chukjssj,caozy,qumyy) ");
					sql.append("	select "+qumxxb_id+"," + id1 + ",f.maoz-f.piz jingz,m.id,f.daohrq,f.daohrq," + visit.getRenyID() + ",'ֱ��' ");
					sql.append("	from fahbtmp f,(select id from meicb where diancxxb_id = 199 and mingc='ֱ��ú��') m");
					sql.append("	where f.id = " + rsstr[0] + "; \n");
					
					sql.append("insert into duowkcb(id,diancxxb_id,riq,duiqm_id,meicb_id,leix,pinz,fahr,chec,shij,shul,kucl,ruksl,chuksl,ches,biaoz,PANYK,leib) \n");
					sql.append("	select getnewid(f.diancxxb_id),f.diancxxb_id,f.daohrq riq,"+qumxxb_id+",m.id meicb_id,-1 leix,p.mingc,g.quanc fahr,f.chec,f.daohrq shij,f.maoz-f.piz jingz,0 kucl,0 ruksl,jingz chuksl,f.ches,f.biaoz ,f.yingk,f.leix_id \n");
					sql.append("	from fahbtmp f,pinzb p,gongysb g,");
					sql.append("		(select id from meicb where diancxxb_id = 199 and mingc='ֱ��ú��') m \n");
					sql.append("	where p.id(+)=f.pinzb_id and g.id(+)=f.gongysb_id \n");
					sql.append("	and f.id = " + rsstr[0] + "; \n");
					
				}else if(2 == str){//���ؽ���
					   if(rsstr[2].equals("")||rsstr[2]==null){rsstr[2]="0";}
					sql.append("update fayslb ");
					sql.append("	set daohrq = " + DateUtil.FormatOracleDate(rsstr[3]) );
					sql.append(" 	where id = " + rsstr[2] + "; \n");
					
					sql.append("update fahbtmp ");
					sql.append("	set fayslb_id = " + rsstr[2] + ",leix_id=" + str);
					sql.append("  ,xiaoshetb_id="+xiaoshetb_id);
					sql.append(" 	where id = " + rsstr[0] + "; \n\n");
					sql.append("update duowkcb set zaitml=0 where duiqm_id in (select id from qumxxb where zhuangcb_id =" + rsstr[2] + ");\n");
				}else{//����
					flag = false;
				}
			}
			sql.append("end; \n");
			try{
//				System.out.println(sql.toString());
				if(flag){					
					con.getUpdate(sql.toString());
				}
				con.commit();
				
				ShoumzgInfo.CountChengb(Long.parseLong(getTreeid()),fhlist,true);
//				FayzgInfo.CountChengb(Long.parseLong(getTreeid()),fylist,true);
			}catch(Exception e){
				con.rollBack();
			}finally{
				con.Close();
			}
		}
	}
	
	private void zuof() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().setMokmc(SysConstant.RizOpMokm_Rlgs_Shujqr);
		if (!(this.getChange().equals("") || this.getChange() == null)) {
			JDBCcon con=new JDBCcon();
			con.setAutoCommit(false);
			StringBuffer sql = new StringBuffer();
			String change[] = this.getChange().split(";");
			sql.append("begin \n");
			boolean flag = true;
			for (int i = 0; i < change.length; i++) {
				if (change[i] == null || "".equals(change[i])) {
					continue;
				}
				String rsstr[] = change[i].split(",");
				//rsstr[0]:fahbtmp.id
				//rsstr[1]:fahbtmp.leix_id
				//rsstr[2]:fahbtmp.fahb_id
				//rsstr[3]:fahbtmp.fayslb_id
				
				if(i==0 && "".equals(rsstr[0]) && null == rsstr[0]){//break��˵���û�û��ѡ����
					flag = false;
					break;
				}
				System.out.println(getZhuangt());
				if(getZhuangt() == 1){
					
					long str = (getExtGrid().getColumn("leix_id").combo).getBeanId(rsstr[1])  ;//ҵ������				
					if(3 == str){//ֱ��
						sql.append("update fahbtmp set fahb_id=-2,fayslb_id=-2 where id=" + rsstr[0] + "; \n");
						sql.append("delete from fayslb where id="+ rsstr[3] +";\n\n");
						sql.append("delete from fahb where id="+ rsstr[2] +";\n\n");
						sql.append("delete from duowkcb where duiqm_id in (select id from qumxxb where zhuangcb_id="+ rsstr[2] +" union select id from duimxxb where fahb_id="+ rsstr[2] +");\n");
						sql.append("delete from qumxxb where zhuangcb_id="+ rsstr[2] +";\n\n");
						sql.append("delete from duimxxb where fahb_id="+ rsstr[2] +";\n\n");
						
//						ɾ��ʱ������־
						MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
								SysConstant.RizOpType_DEL,visit.getExtGrid1().getMokmc(),
								"fahb",rsstr[2]);
						MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
								SysConstant.RizOpType_DEL,visit.getExtGrid1().getMokmc(),
								"fahbtmp",rsstr[0]);
					}else if(2 == str){//���ؽ���
						sql.append("update fahbtmp set fayslb_id=-2 where id=" + rsstr[0] + "; \n");
						sql.append("update fayslb set daohrq=null where id="+ rsstr[2] +";\n\n");
						sql.append("update duowkcb set zaitml=chuksl where duiqm_id in (select id from qumxxb where zhuangcb_id =" + rsstr[2] + ");\n");
					}else{
						flag = false;
					}
				}else if(getZhuangt() == 2){

					sql.append("update fahbtmp set fahb_id=-2,fayslb_id=-2 where id=" + rsstr[0] + "; \n\n");
				
				}else{
					flag =  false;
				}
			}
			sql.append("end; \n");
			try{
//				System.out.println(sql.toString());
				if(flag){
					con.getUpdate(sql.toString());
				}
				con.commit();
			}catch(Exception e){
				con.rollBack();
			}finally{
				con.Close();
			}
		}
	}
	
	private String WindowsScript;
    public String getWindowsScript(){
    	return this.WindowsScript;
    }
    public void setWindowsScript(String WindowsScript){
    	this.WindowsScript=WindowsScript;
    }
    
    private void GridInWindow(){
    	JDBCcon con=new JDBCcon();
    	
//		String sql = "select min(fahrq) min,max(fahrq) max from fahbtmp where fayslb_id is null";      
//		ResultSetList rs = con.getResultSetList(sql);
//		rs.next();
//		String min = DateUtil.FormatDate(rs.getDate("min"));
//		String max = DateUtil.FormatDate(rs.getDate("max"));
//		System.out.println(min+"=r="+max);
		
		StringBuffer sql1 = new StringBuffer();
		sql1.append("select f.id,d.mingc shr_diancxxb_id,g.mingc gongysb_id,to_char(f.fahrq,'yyyy-mm-dd') fahrq,f.chec,y.mingc yunsfsb_id,l.mingc luncxxb_id,f.meil ");
		sql1.append(" from fayslb f,diancxxb d,luncxxb l,yunsfsb y ,gongysb g ");
		sql1.append(" where  f.fahrq >= to_date('" + getRiq1() + "','yyyy-mm-dd') ");
		sql1.append(" and f.fahrq < to_date('" + getRiq2() + "','yyyy-mm-dd')+1 and f.shr_diancxxb_id="+ getTreeid());
		sql1.append(" and f.shr_diancxxb_id=d.id(+) and f.luncxxb_id=l.id(+) and f.yunsfsb_id=y.id(+) and f.gongysb_id=g.id and f.daohrq is null");
		ResultSetList rsl = con.getResultSetList(sql1.toString());
		con.Close();
    	
		rsl.beforefirst();
		ExtGridUtil egu=new ExtGridUtil("gridDivInwindow",rsl);
		egu.setWidth(400);
		egu.setHeight(300);
		egu.addPaging(rsl.getRows());
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("shr_diancxxb_id").setHeader("�ջ���");
		egu.getColumn("shr_diancxxb_id").setWidth(70);
		
		egu.getColumn("gongysb_id").setHeader("��Ӧ��");
		egu.getColumn("gongysb_id").setWidth(70);
		
		egu.getColumn("fahrq").setHeader("��������");
		egu.getColumn("fahrq").setWidth(70);
		
		egu.getColumn("chec").setHeader("����");
		egu.getColumn("chec").setWidth(50);
		
		egu.getColumn("yunsfsb_id").setHeader("���䷽ʽ");
		egu.getColumn("yunsfsb_id").setWidth(70);
		
		egu.getColumn("luncxxb_id").setHeader("����");
		egu.getColumn("luncxxb_id").setWidth(60);
		
		egu.getColumn("meil").setHeader("ú��");
		egu.getColumn("meil").setWidth(55);		

		this.setExtGridInWindow(egu);

    }
	
	
	private void getSelectData(){
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();

		sql.append("select f.id,d.mingc diancxxb_id,(select mingc from diancxxb where id=d.id) keh_diancxxb_id,sh.mingc yuanshdwb_id,g.mingc gongysb_id,c.mingc faz_id,");
		sql.append("	f.fahrq,f.daohrq,");
		sql.append("	yu.mingc yunsfsb_id,f.chec,f.chuanm,y.mingc leix_id,h.hetbh hetb_id,(select hetbh from hetb where id=f.xiaoshetb_id) xiaoshetb_id,f.ches,f.maoz,f.piz,f.biaoz,nvl(f.maoz,0)-nvl(f.piz,0) jingz,f.yuns,f.yingk,f.fahb_id,f.fayslb_id, \n");
		sql.append("		h.hetbh hetb_id2,f.zhilb_id ");
		sql.append("from fahbtmp f,diancxxb d,diancxxb sh,gongysb g,chezxxb c,yewlxb y,yunsfsb yu, hetb h \n");
		sql.append("where ");	
		sql.append("     f.fahrq >= to_date('"+ getRiq1() + "','yyyy-mm-dd')\n");
		sql.append(" and f.fahrq < to_date('"+ getRiq2() + "','yyyy-mm-dd')+1\n");
		sql.append(" and d.id = "+ getTreeid());
		sql.append(" and f.diancxxb_id=d.id and f.gongysb_id=g.id(+) and f.faz_id=c.id(+) and yu.id(+)=f.yunsfsb_id and h.id(+)=f.hetb_id and shujly='�Զ��ϴ�' ");
		sql.append(" and f.yuanshdwb_id=sh.id ");
		setZhuangt(getZhuangtSelectValue().getValue());
		if(getZhuangt() == 1){
			sql.append(" and f.fayslb_id > 0");//�����
			sql.append(" and y.id=f.leix_id");
		}else if(getZhuangt() == 2){
			sql.append(" and (f.fayslb_id is null or f.fayslb_id=0) ");//δ���
			sql.append(" and y.id(+)=f.leix_id");
		}else{
			sql.append(" and f.fayslb_id = -2");//ɾ��
			sql.append(" and y.id(+)=f.leix_id");
		}
		sql.append(" order by f.id asc");
//		System.out.println("\n"+sql+"\n");
		ResultSetList rsl = con.getResultSetList(sql.toString());
		if(rsl==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);

		//���븴ѡ��
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
		
		egu.getColumn("diancxxb_id").setHeader("�糧����");
		egu.getColumn("diancxxb_id").setWidth(70);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("diancxxb_id").setHidden(true);
		
		egu.getColumn("yuanshdwb_id").setHeader("�ջ���λ");
		egu.getColumn("yuanshdwb_id").setEditor(null);
		egu.getColumn("yuanshdwb_id").setHidden(true);
		
		egu.getColumn("keh_diancxxb_id").setHeader("�ͻ�����");
		ComboBox kehmc=new ComboBox();
		egu.getColumn("keh_diancxxb_id").setEditor(kehmc);
		kehmc.setEditable(true);
		String kehmcsql="select id ,mingc from diancxxb where cangkb_id=1 and jib=3";
		egu.getColumn("keh_diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(kehmcsql));
		//����
		JDBCcon dianccon=new JDBCcon();
		ResultSet rs=dianccon.getResultSet("select * from diancxxb where id="+this.getTreeid());
		try {
			if(rs.next()){
			egu.getColumn("keh_diancxxb_id").setDefaultValue(rs.getString("mingc"));
			}
		} catch (SQLException e1) {
			// TODO �Զ����� catch ��
			e1.printStackTrace();
		}
		
		
		egu.getColumn("gongysb_id").setHeader("��Ӧ������");
		egu.getColumn("gongysb_id").setWidth(80);
		egu.getColumn("gongysb_id").setEditor(null);
		
		egu.getColumn("faz_id").setHeader("��վ");
		egu.getColumn("faz_id").setWidth(55);
		egu.getColumn("faz_id").setEditor(null);
		
		egu.getColumn("fahrq").setHeader("��������");
		egu.getColumn("fahrq").setWidth(65);
		egu.getColumn("fahrq").setEditor(null);
		
		egu.getColumn("daohrq").setHeader("��������");
		egu.getColumn("daohrq").setWidth(65);
		egu.getColumn("daohrq").setEditor(null);
		
		egu.getColumn("yunsfsb_id").setHeader("���䷽ʽ");
		egu.getColumn("yunsfsb_id").setWidth(60);
		egu.getColumn("yunsfsb_id").setEditor(null);
		
		egu.getColumn("chec").setHeader("��/����");
		egu.getColumn("chec").setWidth(55);
		egu.getColumn("chec").setEditor(null);
		
		egu.getColumn("chuanm").setHeader("����");
		egu.getColumn("chuanm").setWidth(55);
		egu.getColumn("chuanm").setEditor(null);
//		��ú��ͬ���
		egu.getColumn("hetb_id").setHeader("��ú��ͬ���");
		String showkdwSql = "select id,hetbh from hetb where leib=2 order by hetbh asc";
		ComboBox c4 = new ComboBox();
		c4.setId("hetb_id_combobox");
		c4.setEditable(true);
		c4.setListWidth(120);
		egu.getColumn("hetb_id").setEditor(c4);
		egu.getColumn("hetb_id").setComboEditor(egu.gridId,
				new IDropDownModel(showkdwSql));
		egu.getColumn("hetb_id").setReturnId(true);
//		���ۺ�ͬ���
		egu.getColumn("xiaoshetb_id").setHeader("���ۺ�ͬ���");
		String xiaoshetb_id_sql="select id,hetbh from hetb where  leib<>2 order by hetbh asc";
		ComboBox xiaoshetb_id_combobox=new ComboBox();
		xiaoshetb_id_combobox.setId("xiaoshetb_id_combobox");
		
		xiaoshetb_id_combobox.setListWidth(120);
		egu.getColumn("xiaoshetb_id").setEditor(xiaoshetb_id_combobox);
		egu.getColumn("xiaoshetb_id").setComboEditor(egu.gridId, new IDropDownModel(xiaoshetb_id_sql));
		egu.getColumn("xiaoshetb_id").setReturnId(true);
		
		
		egu.getColumn("leix_id").setHeader("ҵ������");
		String fazSql = "select xuh,mingc from yewlxb where id<>1 order by xuh asc";
		ComboBox c2 = new ComboBox();
		c2.setEditable(true);
		c2.setId("yewlx_combo");
		c2.setWidth(60);
		StringBuffer leix = new StringBuffer();
		leix.append("select:function(combo,record,index){");
		leix.append("	if(combo.getRawValue()=='���ؽ���'){");
		leix.append("		gridDivInwindow_sm.selectRow(-1);");
		leix.append("		gridDivInwindow_sm.clearSelections();");
		leix.append("		rec = gridDiv_grid.getSelectionModel().getSelected();");
		leix.append("		rd[0] = rec.get('ID');");
		leix.append("		rd[1] = rec.get('YUANSHDWB_ID');");
		leix.append("		rd[2] = rec.get('CHEC');");
		leix.append("		rd[3] = rec.get('CHUANM');");
		leix.append("		rd[4] = rec.get('FAHRQ');");
		leix.append("		rd[5] = rec.get('BIAOZ');");
		leix.append("		rd[6] = rec.get('GONGYSB_ID');");
		leix.append("		rd[7] = rec.get('FAYSLB_ID');");
		leix.append("		var yunsfsb_id=gridDiv_grid.getSelectionModel().getSelected().get('YUNSFSB_ID');");
		leix.append("		for(var i=0;i<gridDivInwindow_grid.getStore().getCount();i++){");
		leix.append("			if(yunsfsb_id == '��·'){");
		leix.append("				if(gridDivInwindow_grid.getStore().getAt(i).get('SHR_DIANCXXB_ID')==rd[1] && gridDivInwindow_grid.getStore().getAt(i).get('CHEC')==rd[2] &&");
		leix.append("					gridDivInwindow_grid.getStore().getAt(i).get('FAHRQ')==rd[4] && gridDivInwindow_grid.getStore().getAt(i).get('GONGYSB_ID')==rd[6] ){");
		leix.append("					rd=new Array();");
		leix.append("					gridDivInwindow_sm.selectRow(i);");
		leix.append("					gridDivInwindow_grid.getStore().getAt(i).set('FHFYTMP_ID',rd[0]);");
		leix.append("					break;");
		leix.append("				}");
		leix.append("				if(gridDivInwindow_grid.getStore().getAt(i).get('ID')==rd[7]){");
		leix.append("					rd=new Array();");
		leix.append("					gridDivInwindow_sm.selectRow(i);");
		leix.append("					gridDivInwindow_grid.getStore().getAt(i).set('FHFYTMP_ID',rd[0]);");
		leix.append("					break;");
		leix.append("				}");
		leix.append("			}else if(yunsfsb_id == '����'){");
		leix.append("				if(gridDivInwindow_grid.getStore().getAt(i).get('SHR_DIANCXXB_ID')==rd[1] && gridDivInwindow_grid.getStore().getAt(i).get('CHEC')==rd[2] &&");
		leix.append("					gridDivInwindow_grid.getStore().getAt(i).get('LUNCXXB_ID')==rd[3] ){");
		leix.append("					rd=new Array();");
		leix.append("					gridDivInwindow_sm.selectRow(i);");
		leix.append("					gridDivInwindow_grid.getStore().getAt(i).set('FHFYTMP_ID',rd[0]);");
		leix.append("					break;");
		leix.append("				}");
		leix.append("				if(gridDivInwindow_grid.getStore().getAt(i).get('ID')==rd[7]){");
		leix.append("					rd=new Array();");
		leix.append("					gridDivInwindow_sm.selectRow(i);");
		leix.append("					gridDivInwindow_grid.getStore().getAt(i).set('FHFYTMP_ID',rd[0]);");
		leix.append("					break;");
		leix.append("				}");
		leix.append("			}else{");
		leix.append("			}");
		leix.append("		}");		
		leix.append("		Jieg_window.show();");
		leix.append("	}");
		leix.append("      if(combo.getRawValue()=='ֱ��'){\n");
		leix.append("        hetb_id_combobox.enable();\n ");
		leix.append("       }else{var rec = gridDiv_sm.getSelected();rec.set('HETB_ID','');/*hetb_id_combobox.disable();*/}");
		leix.append("}");
		c2.setListeners(leix.toString());
		egu.getColumn("leix_id").setWidth(60);
		egu.getColumn("leix_id").setEditor(c2);
		egu.getColumn("leix_id").setComboEditor(egu.gridId,
				new IDropDownModel(fazSql));
		egu.getColumn("leix_id").setReturnId(true);

		if(getZhuangt() == 1 || getZhuangt() == 3){
			egu.getColumn("leix_id").setEditor(null);
		}
				
		egu.getColumn("ches").setHeader("����");
		egu.getColumn("ches").setWidth(60);
		egu.getColumn("ches").setEditor(null);
		
		egu.getColumn("maoz").setHeader("ë��");
		((NumberField)egu.getColumn("maoz").editor).setDecimalPrecision(3);
		egu.getColumn("maoz").setWidth(60);
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("maoz").setHidden(true);
		
		egu.getColumn("piz").setHeader("Ƥ��");
		((NumberField)egu.getColumn("piz").editor).setDecimalPrecision(3);
		egu.getColumn("piz").setWidth(60);
		egu.getColumn("piz").setEditor(null);
		egu.getColumn("piz").setHidden(true);
		
		egu.getColumn("biaoz").setHeader("Ʊ��");
		((NumberField)egu.getColumn("biaoz").editor).setDecimalPrecision(3);
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("biaoz").setEditor(null);
		
		egu.getColumn("jingz").setHeader("����");
		((NumberField)egu.getColumn("jingz").editor).setDecimalPrecision(3);
		egu.getColumn("jingz").setWidth(60);
		egu.getColumn("jingz").setEditor(null);
		
		egu.getColumn("yuns").setHeader("����");
		((NumberField)egu.getColumn("yuns").editor).setDecimalPrecision(3);
		egu.getColumn("yuns").setWidth(60);
		egu.getColumn("yuns").setEditor(null);
		
		egu.getColumn("yingk").setHeader("ӯ��");
		((NumberField)egu.getColumn("yingk").editor).setDecimalPrecision(3);
		egu.getColumn("yingk").setWidth(60);
		egu.getColumn("yingk").setEditor(null);
		
		egu.getColumn("fahb_id").setHeader("fahb_id");
		egu.getColumn("fahb_id").setEditor(null);
		egu.getColumn("fahb_id").setHidden(true);
		
		egu.getColumn("fayslb_id").setHeader("fayslb_id");
		egu.getColumn("fayslb_id").setEditor(null);
		egu.getColumn("fayslb_id").setHidden(true);
		
		egu.getColumn("zhilb_id").setHeader("zhilb_id");
		egu.getColumn("zhilb_id").setEditor(null);
		egu.getColumn("zhilb_id").setHidden(true);
		
		egu.getColumn("hetb_id2").setHeader("ԭ��ͬ���");
		egu.getColumn("hetb_id2").setEditor(null);
		egu.getColumn("hetb_id2").setHidden(true);
		
		
		//������	
		egu.addTbarText("��������:");
		DateField df = new DateField();
		df.setValue(this.getRiq1());
		df.Binding("Riq1", "");// ��htmlҳ�е�id��,���Զ�ˢ��---Form0
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("��");
		DateField df1 = new DateField();
		df1.setValue(this.getRiq2());
		df1.Binding("Riq2", "");// ��htmlҳ�е�id��,���Զ�ˢ��---Form0
		egu.addToolbarItem(df1.getScript());
		
		egu.addTbarText(" ");
		egu.addTbarText("-");
		
//		 ������������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText(" ");
		egu.addTbarText("-");// ���÷ָ���
		
		egu.addTbarText("״̬:");
		ComboBox comb1=new ComboBox();
		comb1.setTransform("ZhuangtDropDown");
		comb1.setId("Zhuangt");
		comb1.setLazyRender(true);//��̬��
		comb1.setWidth(80);
		egu.addToolbarItem(comb1.getScript());
//		egu.addOtherScript("Zhuangt.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// ���÷ָ���
		
		egu.addPaging(1000);//����һҳ������
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		
		
		String daor = "{"//���밴ť
			+ 
			new GridButton(
					"����",
					"function(){ " 
					        
							+ "if(gridDiv_grid.getSelectionModel().getCount()==0){Ext.Msg.alert('��ʾ','��û��ѡ�����ݣ�');return;}"
							+ "Ext.MessageBox.confirm('��ʾ', 'ȷ����ѡ�������ݵ�����', function(btn) { if(btn=='yes'){ if(gridDiv_sm.hasSelection()){ "
							+ " rec = gridDiv_grid.getSelectionModel().getSelections(); "
							+ " 	for(var i=0;i<rec.length;i++){ "
							+"             if(rec[i].get('XIAOSHETB_ID')==''){alert('��'+(i+1)+'�����ۺ�ͬ����Ϊ��');return;}"
							+"             if(rec[i].get('HETB_ID')==''&&rec[i].get('LEIX_ID')=='ֱ��'){alert('��'+(i+1)+'�вɹ���ͬ����Ϊ��');return;}  "
							+"       }"
							+ " 	for(var i=0;i<rec.length;i++){ "
							+ " 		if(i==0){"
							+ " 			document.getElementById('CHANGE').value=rec[i].get('ID')+','+rec[i].get('LEIX_ID')+','+rec[i].get('FAYSLB_ID')+','+rec[i].get('DAOHRQ')+','+rec[i].get('HETB_ID')+','+rec[i].get('HETB_ID2')+','+rec[i].get('XIAOSHETB_ID')+','+rec[i].get('KEH_DIANCXXB_ID')+';';"
							+ " 		}else{ "
							+ "       	document.getElementById('CHANGE').value+=rec[i].get('ID')+','+rec[i].get('LEIX_ID')+','+rec[i].get('FAYSLB_ID')+','+rec[i].get('DAOHRQ')+','+rec[i].get('HETB_ID')+','+rec[i].get('HETB_ID2')+','+rec[i].get('XIAOSHETB_ID')+','+rec[i].get('KEH_DIANCXXB_ID')+';';}}"
							+ " document.getElementById('DaorButton').click();"+MainGlobal.getExtMessageShow("���ڴ�������,���Ժ�...", "������...", 200)+";}}",SysConstant.Btn_Icon_Insert).getScript() 
			+				"})}} ";
		String zuof = "{"//ɾ����ť
			+ 
			new GridButton(
					"ɾ��",
					"function(){ " 
							+ "if(gridDiv_grid.getSelectionModel().getCount()==0){Ext.Msg.alert('��ʾ','��û��ѡ�����ݣ�');return;}"
							+ "Ext.MessageBox.confirm('����', 'ȷ����ѡ��������ɾ����', function(btn) { if(btn=='yes'){ if(gridDiv_sm.hasSelection()){ "
							+ " rec = gridDiv_grid.getSelectionModel().getSelections(); "
							+ " 	for(var i=0;i<rec.length;i++){ "
							+ " 		if(i==0){"
							+ " 			document.getElementById('CHANGE').value=rec[i].get('ID')+','+rec[i].get('LEIX_ID')+','+rec[i].get('FAHB_ID')+','+rec[i].get('FAYSLB_ID')+';';"
							+ " 		}else{ "
							+ "       	document.getElementById('CHANGE').value+=rec[i].get('ID')+','+rec[i].get('LEIX_ID')+','+rec[i].get('FAHB_ID')+','+rec[i].get('FAYSLB_ID')+';';}}"
							+ " document.getElementById('ZuofButton').click();"+MainGlobal.getExtMessageShow("���ڴ�������,���Ժ�...", "������...", 200)+";}}",SysConstant.Btn_Icon_Delete).getScript() 
			+				"})}} ";

		String zuofy = "{"//�����ɾ����ť//if(gridDivInwindow_grid.getSelectionModel().getCount()==0){
			+ 
			new GridButton(
					"ɾ��",
					"function(){ "
							+ "if(gridDiv_grid.getSelectionModel().getCount()==0){Ext.Msg.alert('��ʾ','��û��ѡ�����ݣ�');return;}"
							+ "Ext.MessageBox.confirm('����', '��ѡ����������������ݣ���ȷ�ϣ��Ƿ�ɾ����', function(btn) { if(btn=='yes'){ if(gridDiv_sm.hasSelection()){ "
							+ " rec = gridDiv_grid.getSelectionModel().getSelections(); "
							+ " var zhilb=new String();"
							+ " for(var j=0;j<rec.length;j++){"
							+ " 	if(rec[j].get('ZHILB_ID')!=0){"//�ж�����id�Ƿ�Ϊ0,���Ϊ0�ü�¼û�л������ɾ�������򣬲���ɾ����Ҫ��ɾ������ɾ�������������Ӧ������
							+ " 		zhilb+=(j+1)+'��';"
							+ " 	}"
							+ " }"
							+ " if(zhilb.length>0){"
							+ " 	zhilb=zhilb.substr(0,zhilb.length-1);"
							+ " 	Ext.Msg.alert('����','��'+zhilb+'�У��Ѿ���������Ϣ���޷�ɾ����');"
							+ " 	return;"
							+ " }"
							+ " 	for(var i=0;i<rec.length;i++){ "
							+ " 		if(i==0){"
							+ " 			document.getElementById('CHANGE').value=rec[i].get('ID')+','+rec[i].get('LEIX_ID')+','+rec[i].get('FAHB_ID')+','+rec[i].get('FAYSLB_ID')+';';"
							+ " 		}else{ "
							+ "       	document.getElementById('CHANGE').value+=rec[i].get('ID')+','+rec[i].get('LEIX_ID')+','+rec[i].get('FAHB_ID')+','+rec[i].get('FAYSLB_ID')+';';}}"
							+ " document.getElementById('ZuofButton').click();"+MainGlobal.getExtMessageShow("���ڴ�������,���Ժ�...", "������...", 200)+";}}",SysConstant.Btn_Icon_Delete).getScript() 
			+				"})}} ";
		if(getZhuangt() == 1){
			egu.addTbarText("-");
			egu.addToolbarItem(zuofy);
		}else if(getZhuangt() == 2){
			egu.addTbarText("-");
			egu.addToolbarItem(daor);
			egu.addTbarText("-");
			egu.addToolbarItem(zuof);
		}else{
		
		}
		StringBuffer script = new StringBuffer();
		script.append( "gridDiv_grid.on('afteredit', function(e) {\n") 
				.append("if(e.field=='LEIX_ID' && gridDiv_grid.getSelectionModel().getSelected().get('LEIX_ID')=='ֱ��'){")
				.append("    gridDiv_grid.getSelectionModel().getSelected().set('FAYSLB_ID','');\n}")
				.append("});");
		
		egu.addOtherScript(script.toString());
//		egu.addOtherScript("hetb_id_combobox.disabled=true;\n");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);//����������
		setExtGrid(egu);
		
		con.Close();
		
	}
	
	
	//��
	private String treeid;
	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
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
	

	public IDropDownBean getZhuangtSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getZhuangtSelectModel()
							.getOption(1));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}
	public void setZhuangtSelectValue(IDropDownBean Value) {
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean3()){
			((Visit) getPage().getVisit()).setDropDownBean3(Value);
		}
	}
	public void setZhuangtSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}
	public IPropertySelectionModel getZhuangtSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getZhuangtSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
	public void getZhuangtSelectModels() {
//		StringBuffer sql=new StringBuffer();
//		sql.append("select 1 id,'�����' mingc from dual union all ");
//		sql.append("select 2 id,'δ���' mingc from dual union all ");
//		sql.append("select 3 id,'ɾ  ��' mingc from dual ");
		List list = new ArrayList();
		list.add(new IDropDownBean(1,"�����"));
		list.add(new IDropDownBean(2,"δ���"));
		list.add(new IDropDownBean(3,"ɾ__��"));
		((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(list));
	}
	
	
//	����ҵ�����������˵�
	public IPropertySelectionModel getYewlxModel(){
		if(((Visit)this.getPage().getVisit()).getProSelectionModel1()==null){
			this.setYewlxModel(this.getYewlxModels());
		}
			return ((Visit)this.getPage().getVisit()).getProSelectionModel1();
		
	}
	public void setYewlxModel(IPropertySelectionModel YewlxModel){
		((Visit)this.getPage().getVisit()).setProSelectionModel1(YewlxModel);
	}
	public IDropDownBean getYewlxValue(){
		if(((Visit)this.getPage().getVisit()).getDropDownBean1()==null){
		this.setYewlxValue( (IDropDownBean)this.getYewlxModel().getOption(0));
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	public void setYewlxValue(IDropDownBean YewlxValue){
		((Visit)this.getPage().getVisit()).setDropDownBean11(YewlxValue);
	}
	public IPropertySelectionModel getYewlxModels(){
		return new IDropDownModel("select id,mingc from yewlxb where id<>1");
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setZhuangtSelectValue(null);
			setZhuangtSelectModel(null);
			getZhuangtSelectModels();
			visit.setDefaultTree(null);
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		
		init();
	}

	private void init() {
		GridInWindow();
		getSelectData();
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
	
	
	public static void main(String args[]){
		JDBCcon con=new JDBCcon();
		con.setAutoCommit(false);
		MainGlobal.LogOperation(con,199,"haha",
				SysConstant.RizOpType_DEL,"asdfasdf",
				"fahb",19912288+"");
//		con.rollBack();
		con.Close();
	}
}