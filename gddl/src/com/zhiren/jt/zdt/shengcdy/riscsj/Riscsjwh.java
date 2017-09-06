package com.zhiren.jt.zdt.shengcdy.riscsj;

import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.Locale;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ���ߣ�����
 * ʱ�䣺2009-10-31
 * �������޸�ˢ��ʱһ������ѡ���ܳ�ˢ�·ֳ�����
 */
/*
 * ����:tzf
 * ʱ��:2009-10-27
 * �޸�����:���밴ť�����в���zbbh��Ϊ�ж�����
 */
/*
 * ����:tzf
 * ʱ��:2009-10-26
 * �޸�����:���밴ť����Ϊ���ݿ��ַ�����Ĳ�һ�����ֵ���jhgl_rlsj������ct_zbbhȥ���жϡ�
 */
/*
 * ����:tzf
 * ʱ��:2009-10-20
 * �޸�����:���ӵ��밴ť
 */
/*
 * ����:zsj
 * ʱ��:2010-04-02
 * �޸�����:�����ͷ�糧���ı�Ľӿڱ�ṹ
 * �ѷ������͹�����ҲҪ�������ǵ�ϵͳ,Ŀǰ�������Ѿ��ܵ���,��������û�е������.
 */


/*����:wzb
 *����:2010-6-2 
 *�޸�����:�����ʱ��ȡ������������ֵ,������sql����,�޸�����272��
 * ��and ct_zbbh like'%dl%' �ĳ� and (ct_zbbh like '%dl%'  or ct_zbbh like '%dwgrl%')
 * 
 */

/*
 * ����:���
 * ����:2012-3-12
 * �޸�����:ע������ʱ�Զ������ձ��Ĳ�����
 */
/*
 * ����:���
 * ����:2012-11-29
 * �޸�����:������ú���в����ݲ����ж��Ƿ���ʾ��Ĭ��Ϊ����ʾ
 * 	MainGlobal.getXitxx_item("����������", "�Ƿ����غ�ú����Ϣ", "0", "��")
 */
/*
 * ����:���
 * ����:2013-03-25
 * �޸�����:���������������������������ĵ�λ���Ϊ��ǧ��ʱ�����������淽����
 * 			��ʼ���糧��
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-07-31
 * ����������ϵͳ�������ù���
 */
public class Riscsjwh extends BasePage implements PageValidateListener {
//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
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
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û����Ҫ����ļ�¼��");
			return;
		}
		StringBuffer sb=new StringBuffer();
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		int flag=0;
		String id = "";
		ResultSetList rssb=getExtGrid().getDeleteResultSet(getChange());
		if(rssb==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Fahxg.Save �� getExtGrid().getDeleteResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while(rssb.next()){
			id=rssb.getString("id");
			//ɾ��ʱ������־
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Riscsj,
					"riscsjb",id+"");
			String sql="delete from riscsjb where id="+id;
			flag=con.getDelete(sql);
			if(flag==-1){
				con.rollBack();
				con.Close();
				
			}
			con.commit();
		}
		
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Riscsjwh.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			con.Close();
			return;
		}
		
		sb.append("begin ");
//		String riq = "";
		while(rsl.next()) {
			id=rsl.getString("id");
			if(id.equals("0")){
//				riq = rsl.getString("riq");
				sb.append("insert into riscsjb (id,diancxxb_id,riq,jizb_id,HAOML,fadl,gongdl,shangwdl,gongrl,fadfhl)");
				sb.append("values (getnewid(").append(getTreeid()).append("),").append(getTreeid()).append(",").append(DateUtil.FormatOracleDate(rsl.getString("riq"))).append(",'");
				sb.append((getExtGrid().getColumn("jizb_id").combo).getBeanId(rsl.getString("jizb_id"))).append("',").append(rsl.getDouble("HAOML")).append(",").append(rsl.getDouble("fadl")).append("*10000,");
				sb.append(rsl.getDouble("gongdl")).append("*10000,").append(rsl.getDouble("shangwdl")).append("*10000,").append(rsl.getDouble("gongrl")).append(",").append(rsl.getDouble("fadfhl")).append(");") ;
//				con.getInsert(sb.toString());
			}else{
				//����ʱ������־
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Riscsj,
						"riscsjb",id+"");
				sb.append("update riscsjb set jizb_id=").append((getExtGrid().getColumn("jizb_id").combo).getBeanId(rsl.getString("jizb_id"))).append(",fadl=");
				sb.append(rsl.getDouble("fadl")).append("*10000, HAOML= "+rsl.getDouble("HAOML")+", gongrl=").append(rsl.getDouble("gongrl")).append(",gongdl=").append(rsl.getDouble("gongdl")).append("*10000,shangwdl=").append(rsl.getDouble("shangwdl")).append("*10000,fadfhl=");
				sb.append(rsl.getDouble("fadfhl")).append(" where id=").append(id).append(";");
//				con.getUpdate(sb.toString());
			}
		}
		sb.append("end;");
		flag = con.getInsert(sb.toString());
		if(flag==-1){
			con.rollBack();
			con.Close();
			
		}
		if (flag!=-1){//����ɹ�
			setMsg(ErrorMessage.SaveSuccessMessage);
		}
//		AutoCreateShouhcrb.Create(con,visit.getDiancxxb_id(),DateUtil.getDate(riq));
		con.commit();
		con.Close();
		//setMsg("����ɹ�");
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	private boolean shousButton = false;
	public void shousButton(IRequestCycle cycle) {
		shousButton = true;
	}
	
	private boolean _DaorChick;
	
	public void DaorButton(IRequestCycle cycle){
		_DaorChick=true;
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
		if(shousButton){
			shousButton = false;
			shous();
			getSelectData();
		}
		if(_DaorChick){
			_DaorChick=false;
			Daor();
			getSelectData();
		}
	}
	
	private String getFenc(JDBCcon con){
		String sql=" select * from diancxxb where fuid="+this.getTreeid();
		ResultSetList rsl=con.getResultSetList(sql);
		String s="";
		while(rsl.next()){
			s+=rsl.getString("id")+",";
		}
		if(s.equals("")){
			s=this.getTreeid()+",";
		}
		
		return s.substring(0,s.lastIndexOf(","));
	}
	
	private void Daor(){
	    
		Visit visit=(Visit)this.getPage().getVisit();
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=null;
		ResultSetList rsl2=null;
		String dcidStr=getFenc(con);
		if(visit.getDiancxxb_id()==232){	//��ͷ�糧
			String insertSql=" delete from riscsjb where riq="+DateUtil.FormatOracleDate(this.getRiqi())+" and diancxxb_id in ("+dcidStr+") ;";
			String sql=" select * from xitxxb where mingc='���������ݵ������ݱ�' and leib='����'  and zhuangt=1";
			rsl=con.getResultSetList(sql);
			
			String tableName="jhgl_rlsj";
			if(rsl.next()){
				tableName=rsl.getString("zhi");
			}
			
			if(tableName!=null){//��� ��ͷ
				
				sql=" select j.id, nvl('#','')||REGEXP_SUBSTR(j.jizbh,'[0-9]+') mingc,j.diancxxb_id from jizb j where j.diancxxb_id in ("+dcidStr+")  order by xuh asc ";
				rsl=con.getResultSetList(sql);
				
				while(rsl.next()){
					
					String mingc=rsl.getString("mingc");
					String jizid=rsl.getString("id");
					String dc_id=rsl.getString("diancxxb_id");
//					�ֶζ��壺dt_rq�����ڣ���ct_zbbh��ָ�꣩��am_brsz��ָ��ֵ��
					String sql2=" select dt_rq,ct_zbbh,ct_zbmc,am_brsz,am_bysz from " +tableName+" "+
					"  where dt_rq="+DateUtil.FormatOracleDate(this.getRiqi())+" and nvl('#','')||REGEXP_SUBSTR(ct_zbbh,'[0-9]+')='"+mingc+"' " +
					//" and ct_zbbh like'%dl%' "; ����:���ܱ�,����ԭ��,��Ϊ���ܹ�ȡ����������ֵ
					"and (ct_zbbh like '%dl%'  or ct_zbbh like '%dwgrl%')";
					
					rsl2=con.getResultSetList(sql2);
					
					String fadh="";//������
					String gongdh="";//������
					String fadcydh="";//���糧�õ���
					String gongrh="";
					boolean flag=false;
					while(rsl2.next()){
						
						String zbbm=rsl2.getString("ct_zbbh");
						if(zbbm.toLowerCase().indexOf("fdl")!=-1){
							
							fadh=String.valueOf(CustomMaths.Round_new(rsl2.getDouble("am_brsz")*10000, 0));
						}
						if(zbbm.toLowerCase().indexOf("gdl")!=-1){
							
							gongdh=String.valueOf(CustomMaths.Round_new(rsl2.getDouble("am_brsz")*10000, 0));
						}
						if(zbbm.toLowerCase().indexOf("fdcydl")!=-1){
							fadcydh=rsl2.getString("am_brsz");
						}
						if(zbbm.toLowerCase().indexOf("grl")!=-1 
								|| zbbm.toLowerCase().indexOf("dwgrl")!=-1){
							gongrh=rsl2.getString("am_brsz");
						}
						
						flag=true;
					}
					
					if(flag){//�û������������Ҫ���뵽meihyb��
						
						if(fadh==null || fadh.equals("")) fadh="0";
						if(gongrh==null || gongrh.equals("")) gongrh="0";
						if(fadcydh==null || fadcydh.equals("")) fadcydh="0";
						if(gongdh==null || gongdh.equals("")) gongdh="0";
						
						if(gongdh.equals("0")){//������Ϊ0   �÷�����-���糧�õ���
							gongdh=(Double.parseDouble(fadh)-Double.parseDouble(fadcydh))+"";
						}
						
						insertSql+=" insert into riscsjb (id,diancxxb_id,riq,jizb_id,fadl,gongdl,gongrl,fadfhl,shangwdl) values \n" +
							"  (getnewid("+getTreeid()+"),"+dc_id+","+DateUtil.FormatOracleDate(this.getRiqi())+"," +
							jizid+","+fadh+","+gongdh+","+gongrh+",0"+",0"+");\n" ;
						
					}
				}
				
				insertSql=" begin \n"+insertSql+" end ;";
				
				con.getUpdate(insertSql);
			}
		}else if(visit.getDiancxxb_id()==229){//�����ȵ糧�������ݽӿ�
			JDBCcon sqlserver =null;
			String insertSql=" delete from riscsjb where riq="+DateUtil.FormatOracleDate(this.getRiqi())+" and diancxxb_id in ("+this.getTreeid()+") ;";
			String tableName="scrbb";
			String sql="";
			if(tableName!=null){//
				sql=" select j.id, j.jizbh,j.diancxxb_id from jizb j where j.diancxxb_id in ("+this.getTreeid()+")  order by xuh asc ";
				rsl=con.getResultSetList(sql);
				
				while(rsl.next()){
					String mingc=rsl.getString("jizbh");
					String jizid=rsl.getString("id");
					String dc_id=rsl.getString("diancxxb_id");
					if(mingc.equals("��Դ����")){		
						sqlserver=new JDBCcon(JDBCcon.ConnectionType_ODBC,"",
								"jdbc:odbc:hymis","scrb","rbgl");
						String sql2=" select rq,fdldr,swdldr,swdldr,round(grldr,0) grldr from " +tableName+" "+
						"  where rq="+DateUtil.FormatOracleDate(this.getRiqi());
						
						rsl2=sqlserver.getResultSetList(sql2);
						
						String fadh="";//������
						String gongdh="";//������
						String shangwdl="";//��������
						String gongrh="";
						boolean flag=false;
						while(rsl2.next()){
								fadh=rsl2.getString("fdldr");
								gongdh=rsl2.getString("swdldr");
								shangwdl=rsl2.getString("swdldr");
								gongrh=rsl2.getString("grldr");
						
							flag=true;
						}
						sqlserver.Close();
						if(flag){//�û������������Ҫ���뵽meihyb��
							
							if(fadh==null || fadh.equals("")) fadh="0";
							if(gongrh==null || gongrh.equals("")) gongrh="0";
							if(shangwdl==null || shangwdl.equals("")) shangwdl="0";
							if(gongdh==null || gongdh.equals("")) gongdh="0";
							
							if(gongdh.equals("0")){//������Ϊ0   ������=��������
								gongdh=Double.parseDouble(shangwdl)+"";
							}
							
							insertSql+=" insert into riscsjb (id,diancxxb_id,riq,jizb_id,fadl,gongdl,gongrl,fadfhl,shangwdl) values \n" +
							"  (getnewid("+getTreeid()+"),"+dc_id+","+DateUtil.FormatOracleDate(this.getRiqi())+"," +
							jizid+","+CustomMaths.round(Double.parseDouble(fadh), 0)+","+CustomMaths.round(Double.parseDouble(gongdh),0)+","+gongrh+","+CustomMaths.round((Double.parseDouble(fadh)/(250000*24))*100,2)+","+shangwdl+");\n" ;
							
						}
						
					}else if(mingc.equals("��������")){
						
						sqlserver=new JDBCcon(JDBCcon.ConnectionType_ODBC,"",
								"jdbc:odbc:ecmis","xcjhtj","rbgl");
						String sql2=" select rq,fdldr,swdldr,swdldr,round(grldr,0) grldr from "+tableName+" "+
						"  where rq="+DateUtil.FormatOracleDate(this.getRiqi());
						
						rsl2=sqlserver.getResultSetList(sql2);
						
						String fadh="";//������
						String gongdh="";//������
						String shangwdl="";//��������
						String gongrh="";
						boolean flag=false;
						while(rsl2.next()){
								fadh=rsl2.getString("fdldr");
								gongdh=rsl2.getString("swdldr");
								shangwdl=rsl2.getString("swdldr");
								gongrh=rsl2.getString("grldr");
						
							flag=true;
						}
						
						if(flag){//�û������������Ҫ���뵽meihyb��
							
							if(fadh==null || fadh.equals("")) fadh="0";
							if(gongrh==null || gongrh.equals("")) gongrh="0";
							if(shangwdl==null || shangwdl.equals("")) shangwdl="0";
							if(gongdh==null || gongdh.equals("")) gongdh="0";
							
							if(gongdh.equals("0")){//������Ϊ0   ������=��������
								gongdh=Double.parseDouble(shangwdl)+"";
							}
							
							insertSql+=" insert into riscsjb (id,diancxxb_id,riq,jizb_id,fadl,gongdl,gongrl,fadfhl,shangwdl) values \n" +
							"  (getnewid("+getTreeid()+"),"+dc_id+","+DateUtil.FormatOracleDate(this.getRiqi())+"," +
							jizid+","+CustomMaths.round(Double.parseDouble(fadh), 0)+","+CustomMaths.round(Double.parseDouble(gongdh),0)+","+gongrh+","+CustomMaths.round((Double.parseDouble(fadh)/(400000*24))*100,2)+","+shangwdl+");\n" ;
							
						}
					}
					
					
				}
				
				insertSql=" begin \n"+insertSql+" end ;";
				
				con.getUpdate(insertSql);
			}
		}
		
		if(rsl!=null){
			
			rsl.close();
		}
		if(rsl2!=null){
			
			rsl2.close();
		}
		con.Close();
	}
	private void shous(){
		JDBCcon con=new JDBCcon();
//		Visit visit = (Visit) getPage().getVisit();
		String sql="select *\n" +
		"from riscsjb\n" + 
		"where to_char(riscsjb.riq,'yyyy-mm-dd')='"+getRiqi()+"'";
		ResultSet rs=con.getResultSet(sql);
		try{
			if(rs.next()){//�����¯���ô���
				setMsg("������Ϣ�Ѿ����ڣ���ɾ�����ٲ�����");
			}else{
				sql="insert into riscsjb(id,diancxxb_id,riq,jizb_id,fadl,gongdl,gongrl,fadfhl,shangwdl)(\n" + 
				"select getnewid("+getTreeid()+")id,"+getTreeid()+" diancxxb_id,to_date('"+getRiqi()+"','yyyy-mm-dd')riq,\n" + 
				"decode(jizh,'#1',(select id from jizb where jizbh='#1'),(select id from jizb where jizbh='#2'))jizbh,\n" + 
				"nvl(fadl,0)*1000,nvl(gongdl,0)*1000,0,nvl(fuhl,0),nvl(shangwdl,0)*1000\n" + 
				"from(\n" + 
				"select '#1'jizh,sum(fadl1)fadl,sum(shangwdl)shangwdl,sum(gongdl)gongdl,sum(fuhl)fuhl\n" + 
				"from(\n" + 
				"    select plan_xh,plan_data fadl1,0 shangwdl,0 gongdl,0 fuhl\n" + 
				"    from zhiren@tj t\n" + 
				"    where t.plan_xh='10001' and t.plan_date = to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
				"    union\n" + 
				"    select plan_xh,0 fadl1,plan_data shangwdl,0 gongdl,0 fuhl\n" + 
				"    from zhiren@tj t\n" + 
				"    where t.plan_xh='10006' and t.plan_date = to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
				"    union\n" + 
				"    select plan_xh,0 fadl1,0 shangwdl,plan_data gongdl,0 fuhl\n" + 
				"    from zhiren@tj t\n" + 
				"    where t.plan_xh='10007' and t.plan_date = to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
				"    union\n" + 
				"    select plan_xh,0 fadl1,0 shangwdl,0 gongdl,plan_data fuhl\n" + 
				"    from zhiren@tj t\n" + 
				"    where t.plan_xh='10028' and t.plan_date = to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
				" )\n" + 
				" union\n" + 
				" select '#2'jizh,sum(fadl1),sum(shangwdl),sum(gongdl),sum(fuhl)\n" + 
				"from(\n" + 
				"    select plan_xh,plan_data fadl1,0 shangwdl,0 gongdl,0 fuhl\n" + 
				"    from zhiren@tj t\n" + 
				"    where t.plan_xh='20001' and t.plan_date = to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
				"    union\n" + 
				"    select plan_xh,0 fadl1,plan_data shangwdl,0 gongdl,0 fuhl\n" + 
				"    from zhiren@tj t\n" + 
				"    where t.plan_xh='20006' and t.plan_date = to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
				"    union\n" + 
				"    select plan_xh,0 fadl1,0 shangwdl,plan_data gongdl,0 fuhl\n" + 
				"    from zhiren@tj t\n" + 
				"    where t.plan_xh='20007' and t.plan_date = to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
				"    union\n" + 
				"    select plan_xh,0 fadl1,0 shangwdl,0 gongdl,plan_data fuhl\n" + 
				"    from zhiren@tj t\n" + 
				"    where t.plan_xh='20028' and t.plan_date = to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
				" )))";
				con.getInsert(sql); 
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
	}
	
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon(); 
		String riqTiaoj=this.getRiqi();
		if(riqTiaoj==null||riqTiaoj.equals("")){
			riqTiaoj=DateUtil.FormatDate(new Date());
		}
		Visit visit = ((Visit) getPage().getVisit());
//		String riq = DateUtil.FormatDate(new Date());
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select r.id,r.diancxxb_id,r.riq,j.jizbh as jizb_id,r.HAOML,round(r.fadl/10000,4) fadl,round(r.gongdl/10000,4) gongdl,round(r.shangwdl/10000,4) shangwdl,r.gongrl,r.fadfhl \n");
		sbsql.append("from riscsjb r,jizb j,diancxxb d \n");
		sbsql.append("where r.jizb_id=j.id and riq=to_date('"+riqTiaoj+"','yyyy-mm-dd') \n");
		sbsql.append("and r.diancxxb_id=d.id and (d.fuid="+getTreeid()+" or d.id = "+getTreeid()+") order by id");
		
		ResultSetList rsl = con.getResultSetList(sbsql.toString());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sbsql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl,"Riscsjwh");
		egu.setTableName("riscsjb");
		if(visit.isFencb()) {
			ComboBox dc= new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(dc);
			dc.setEditable(true);
			String dcSql="select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id();
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(dcSql));
			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
			egu.getColumn("diancxxb_id").setWidth(70);
		}else {
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").editor = null;
		}
		egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
		egu.getColumn("diancxxb_id").setWidth(80);
		egu.getColumn("diancxxb_id").setDefaultValue(visit.getDiancxxb_id()+"");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("riq").setHeader("����");
		egu.getColumn("riq").setWidth(100);
		egu.getColumn("riq").setDefaultValue(riqTiaoj);
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("jizb_id").setHeader("������");
		egu.getColumn("jizb_id").setWidth(100);
		egu.getColumn("HAOML").setHeader("��ú��(��)");
		egu.getColumn("HAOML").setWidth(100);
		egu.getColumn("HAOML").setDefaultValue("0");
		if(MainGlobal.getXitxx_item("����������", "�Ƿ����غ�ú����Ϣ", "0", "��").equals("��")){
			egu.getColumn("HAOML").setHidden(true);
		}
		egu.getColumn("fadl").setHeader("������<br>(��ǧ��ʱ)");
		egu.getColumn("fadl").setWidth(80);
		((NumberField)egu.getColumn("fadl").editor).setDecimalPrecision(4);
		egu.getColumn("gongdl").setHeader("������<br>(��ǧ��ʱ)");
		egu.getColumn("gongdl").setWidth(80);
		((NumberField)egu.getColumn("gongdl").editor).setDecimalPrecision(4);
		egu.getColumn("shangwdl").setHeader("��������<br>(��ǧ��ʱ)");
		egu.getColumn("shangwdl").setWidth(80);
		((NumberField)egu.getColumn("shangwdl").editor).setDecimalPrecision(4);
		egu.getColumn("gongrl").setHeader("������<br>(����)");
		egu.getColumn("gongrl").setWidth(80);
		egu.getColumn("fadfhl").setHeader("����<br>������(%)");
		egu.getColumn("fadfhl").setWidth(100);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setWidth(Locale.Grid_DefaultWidth);
//		 ���û���������
		ComboBox c1 = new ComboBox();
		egu.getColumn("jizb_id").setEditor(c1);
		c1.setEditable(true);
		String Sql = "select id, jizbh from jizb where diancxxb_id in (select id from diancxxb d where (d.fuid="+getTreeid()+" or d.id = "+getTreeid()+"))";
		egu.getColumn("jizb_id").setComboEditor(egu.gridId,new IDropDownModel(Sql));
		
//		 ������
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// ���÷ָ���
		
		egu.addTbarText("����:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI","");// ��htmlҳ�е�id��,���Զ�ˢ��
		egu.addToolbarItem(df.getScript());
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
		//egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		if(MainGlobal.getXitxx_item("���õ���", "���õ���ȡ����", "0", "��").equals("��")){
//			egu.addToolbarButton(GridButton.."��ȡ����", "shouqsj");
			egu.addToolbarItem("{"+new GridButton("��ȡ����","function(){document.getElementById('shousButton').click();}").getScript()+"}");
		}
		
//		��ӵ��밴ť
		String sql=" select * from xitxxb where mingc='������������ӵ��밴ť' and leib='����' and zhuangt=1 and zhi='��' ";
		ResultSetList rs1=con.getResultSetList(sql);
		if(rs1.next()){
			GridButton daor=new GridButton("����","function(){ Ext.MessageBox.confirm('��ʾ��Ϣ','ȷ��Ҫ���ǵ���ʱ���������?',function(btn){if(btn=='yes'){document.all.DaorButton.click();} });}");
			egu.addTbarBtn(daor);
		}
		rs1.close();
		
		
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
		if(getExtGrid() == null) {
			return "";
		}
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

//	��
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
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setTbmsg(null);
//			treeid = String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
			setTreeid(null);
		}
		getSelectData();
	}
}