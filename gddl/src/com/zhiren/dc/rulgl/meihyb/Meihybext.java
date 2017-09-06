package com.zhiren.dc.rulgl.meihyb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;
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
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
//import com.zhiren.dc.diaoygl.AutoCreateShouhcrb;
import com.zhiren.dc.huaygl.Compute;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ���ߣ����
 * ʱ�䣺2012-10-31
 * ��������������bug������ʱ��Ӧ����DIANCXXB_ID
 */
/*
 * ���ߣ���ʤ��
 * ʱ�䣺2012-07-18
 * ��������������bug
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-04-17
 * ������ʹ�ò����ж��Ƿ���ʾ������ť��Ĭ��ֵΪ��(����ʾ)
 */
/*
 * ���ߣ�����
 * ʱ�䣺2010-06-09
 * �����������涯��ʱrulmzlb�е�meilû�д���ֵ��meil���볧��¯��ֵ�������Ȩֵ
 */
/*
 * ���ߣ�liuy
 * ʱ�䣺2010-04-26
 * ����������ʱrulmzlb��shenhzt�ֶ�Ĭ��Ϊ0
 */
/*
 * ���ߣ�liht
 * ʱ�䣺2010-03-31
 * �������޸�������־ʱ����ID����ȷ����ʧ�ܵ����⣻��rulmzlb������zhiyr�������ˣ��ֶ�
 */
/*
 * ���ߣ�����
 * ʱ�䣺2010-01-16
 * �������޸ĺ����ȵ��ȡ�����������л���ͣ���������Ϊnull �������㲻��ȷ
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-24
 * �������޸���ͷ�糧ȡ�������� ���Ҷ����ݽ���ȡ��
 */
/*
 * ����:tzf
 * ʱ��:2009-10-27
 * �޸�����:���밴ť�����в���zbbh��Ϊ�ж�����
 */
/*
 * ����:tzf
 * ʱ��:2009-10-27
 * �޸�����:���밴ť�з����иı�ȡ��������������
 */
/*
 * ����:tzf
 * ʱ��:2009-10-27
 * �޸�����:���밴ť�з����г�ȥ�жϱ�����������
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
 * ���ߣ�����
 * ʱ�䣺2009-10-20
 * �������޸ĺ����ȵ����ݿ�ȡ��ʱδ��rulmzlb.meil�ֶ�����볧��¯��ֵ���ѯ������������
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-09-22
 * ����������ú����ȡ��
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-07-30 11��35
 * ������������¯ú���������Զ�����
 * 		insert into xitxxb(id,xuh,diancxxb_id,mingc,zhi,danw,leib,zhuangt,beiz)
 * 		values(23288902,1,229,'��¯ú�������ܼ���','��','','��¯',1,'')
 */
/*
 * ����
 * 2009-05-13
 * �糧����TreeIdδ��ʼ����BUG
 */
/*
 * ly
 * �޸�ʱ�䣺2009-04-27
 * �޸����ݣ����ӵ糧Tree
 */
/*
 * 2009-05-18
 * ����
 * �޸��Զ������պĴ�ʱ����ʱ������Ĵ���
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-07-02 14��13
 * �������޸ĵ糧���޸ĵ糧ʱˢ��ҳ��û�����¼������ѡ��糧���ɹ�������
 */
/*
 * ���ߣ�zsj
 * ʱ�䣺2010-04-02
 * �������޸���ͷ�ĵ��빦�ܣ�
 *				  1.����ʱ��Ϊ�����������9#,��10#��,����������ĺ������ݵ�������.Ŀǰ����Ҫ�����������������ݶ�������
                  2.����ĺ������ݶ�Ҫ����2λС��.
 */

/*
 *���ߣ�ww
 *ʱ�䣺2010-06-03 
 *����: �޸�һ�������·ֳ���ѡ����¯����
 *		
 *	   ��Ӳ������ã��жϱ���ʱ���ú����������Ӧ����¯������Ϣ�򲻸���rulmzlb
 */
/*
 * ���ߣ���衻�
 * ʱ�䣺2010��6��7
 * �������޸�shous()�����жԳ�����Ϊ0�Ĵ���
 */
/*
 * ���ߣ����
 * ʱ�䣺2013��3��14
 * �������޸ı��淽����
 */
/*
 * ���ߣ����
 * ʱ�䣺2013��6��27
 * ������ʹ�ô������"N"�ж��Ƿ�ɱ༭��ɾ��������
 */
public class Meihybext extends BasePage implements PageValidateListener {
//	�ͻ��˵���Ϣ��
	private String msg = "";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
//	ҳ���ʼ��(ÿ��ˢ�¶�ִ��)
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
//	 ���ڿؼ�
	private String riqi;
	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			JDBCcon con = new JDBCcon();
			int zhi=0;
			String sql = "select zhi from xitxxb where  leib='��¯' and mingc ='ú����Ĭ������' and zhuangt =1 ";
			ResultSetList rsl=con.getResultSetList(sql);
			while(rsl.next()){
			   zhi=rsl.getInt("zhi");	
			}
			riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),+zhi,DateUtil.AddType_intDay));
			con.Close();
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		this.riqi = riqi;
	}

	private void Save() {

		if (getChange() == null || "".equals(getChange())) {
			setMsg("û����Ҫ����ļ�¼��");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);

		StringBuffer sb = new StringBuffer();
		StringBuffer sbdl = new StringBuffer();

		// ɾ������
		ResultSetList delrsl = getExtGrid().getDeleteResultSet(getChange());
		//�ȼ���ж�ɾ����¯ú��������
		
		if (delrsl.getRows() != 0) {
			sbdl.append("begin\n");
			while (delrsl.next()) {
				String id = delrsl.getString("id");
				//ɾ��ʱ������־
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Meihy,
						"meihyb",id);
				String delsql="";
				delsql+=" delete from  meihyb  where id ="+delrsl.getString("id")+";	\n";
				//ɾ��ʱ������־
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Meihy,
						"rulmzlb",delrsl.getString("rulmzlb_id"));
				delsql+=" delete from  rulmzlb where id ="+delrsl.getString("rulmzlb_id")+"; \n";
				//�ж���¯ú�������Ƿ��Ѿ����ڻ������ݣ�������ڲ���ɾ��

				String sql="select qnet_ar\n" +
				"from rulmzlb\n" + 
				"where id="+delrsl.getString("rulmzlb_id");
				ResultSet rs=con.getResultSet(sql);
				try {
					if(rs.next()){
						double qnet_ar= rs.getDouble("qnet_ar");
						if(qnet_ar!=0){//���ڻ���ֵ
							setMsg("��¯����ֵ����ֵ�Ѿ����ڣ�����ɾ��ֻ���޸ġ�");
//							con.rollBack();
							return;
						}else{
							sbdl.append(delsql);
						}
					}
				} catch (SQLException e) {
					// TODO �Զ����� catch ��
					e.printStackTrace();
				}
			}
			sbdl.append("end;");
			con.getDelete(sbdl.toString());
			con.commit();
		}

		// ��������
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult
							+ "Meihybext.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}

		if (rsl.getRows() == 0) {
			return;
		}
//		String riq1 = getRiqi();
		long diancxxb_id = 0;

		sb.append("begin\n");
		String rulrq = "";
		String strDate = "";
		
		//���������Ƿ��Զ�����¯���ڴ�1��
		boolean StrFenxrq=false;
		ResultSetList Riqrsl=con.getResultSetList("select zhi from xitxxb x where x.mingc='��¯������������Ƿ����¯���ڴ�1��' and leib='��¯' and x.zhuangt=1");
		if(Riqrsl.next()){
			StrFenxrq=true;
		}else{
			StrFenxrq=false;
		}
		Riqrsl.close();
		
		while (rsl.next()) {
			diancxxb_id = Long.parseLong(getTreeid());//visit.getDiancxxb_id();
			String rulmzlb_id = MainGlobal.getNewID(diancxxb_id);// �õ�rulmzlb_id

			String fenxrq ="";
			if(StrFenxrq){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try{
					Date Date_riq=DateUtil.AddDate(sdf.parse(rsl.getString("rulrq")),1,DateUtil.AddType_intDay);
					String Str_riq=DateUtil.FormatDate(Date_riq);
					fenxrq= DateUtil.FormatOracleDate(Str_riq);
				}catch(Exception  e){
					
				}
			}else{
				fenxrq= DateUtil.FormatOracleDate(rsl.getString("rulrq"));
			}
			
			long rulbzb_id=(getExtGrid().getColumn("rulbzb_id").combo).getBeanId(rsl.getString("rulbzb_id"));
			long jizfzb_id=(getExtGrid().getColumn("jizfzb_id").combo).getBeanId(rsl.getString("jizfzb_id"));
			rulrq = DateUtil.FormatOracleDate(rsl.getString("rulrq"));
			strDate = rsl.getString("rulrq");
			
			//�ж���������Ѿ�¼�벻����rulmzlb
			boolean isHasHy = false;
			String sql = " select * from xitxxb where mingc='��¯ú�����Ƿ���' and leib='��¯' and zhuangt=1 and zhi='��' ";
			ResultSetList rss=con.getResultSetList(sql);
			if(rss.next()){
				isHasHy = con.getHasIt("select * from rulmzlb where rulrq=to_date('" 
						+ rsl.getString("RULRQ") + "','yyyy-mm-dd') and rulbzb_id=" + rulbzb_id + " and jizfzb_id=" + jizfzb_id);
			}

			if ("0".equals(rsl.getString("id"))) {
				
				if (!isHasHy) {
					sb.append("insert into rulmzlb (id,diancxxb_id,fenxrq,rulbzb_id,jizfzb_id,meil,rulrq,lursj,shenhzt) values (\n")
					.append(rulmzlb_id).append(",").append(diancxxb_id)
					.append(",").append(fenxrq).append("+1,").append(rulbzb_id).append(",").append(jizfzb_id)
					.append(",").append(rsl.getDouble("FADHY")+rsl.getDouble("GONGRHY")+rsl.getDouble("QITY"))
					.append(",").append(rulrq).append(",").append(rulrq)
					.append(",0);\n");
				}
				sb.append("insert into meihyb \n");
				sb.append("(ID,RULRQ,DIANCXXB_ID,RULMZLB_ID,RULBZB_ID,JIZFZB_ID,FADHY,GONGRHY,QITY,FEISCY,BEIZ,LURY,LURSJ,zhiyr,SHENHZT) \n");
				sb.append("values (getnewid(").append(diancxxb_id).append("),to_date('");
				sb.append(rsl.getString("RULRQ")).append("','yyyy-mm-dd'),").append(diancxxb_id).append(",");
				sb.append(rulmzlb_id).append(",").append(rulbzb_id).append(",").append(jizfzb_id);
				sb.append(",").append(rsl.getString("FADHY")).append(",").append(rsl.getString("GONGRHY"));
				sb.append(",").append(rsl.getString("QITY")).append(",").append(rsl.getString("FEISCY"));
				sb.append(",'").append(rsl.getString("BEIZ")).append("','").append(rsl.getString("LURY"));
				sb.append("',to_date('").append(rsl.getString("LURSJ"))
				  .append("','yyyy-mm-dd'),'").append(rsl.getString("zhiyr")).append("',");
				sb.append(rsl.getString("SHENHZT")).append("); \n");

			} else {
				String id = rsl.getString("id");
				//�޸�ʱ������־
				MainGlobal.LogOperation(con,diancxxb_id,visit.getRenymc(),
						SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Meihy,
						"rulmzlb",rsl.getString("rulmzlb_id"));
				
				if (!isHasHy) {
					sb.append("update rulmzlb set fenxrq=").append(fenxrq).append(",rulbzb_id=").append(rulbzb_id);
					sb.append(",meil="+(rsl.getDouble("FADHY")+rsl.getDouble("GONGRHY")+rsl.getDouble("QITY")) + "\n");
					sb.append(",jizfzb_id=").append(jizfzb_id).append(",rulrq=")
					  .append(rulrq).append(",lursj=")
					  .append(rulrq).append(",")
					  .append("shenhzt=0").append(" where id=");
					sb.append(rsl.getString("rulmzlb_id")).append(";\n");
				}

				//�޸�ʱ������־
				MainGlobal.LogOperation(con,diancxxb_id,visit.getRenymc(),
						SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Meihy,
						"meihyb",id);
				sb.append("update meihyb set RULRQ=to_date('"+rsl.getString("RULRQ")+"','yyyy-mm-dd'),\n");
				sb.append(" RULMZLB_ID="+rsl.getString("RULMZLB_ID")).append(",\n");
				sb.append(" RULBZB_ID="+rulbzb_id+",\n");
				sb.append(" JIZFZB_ID="+jizfzb_id+",\n");
				sb.append(" FADHY="+rsl.getString("FADHY")+",\n");
				sb.append(" GONGRHY="+rsl.getString("GONGRHY")+",\n")
				  .append(" QITY="+rsl.getString("QITY")+",\n")
				  .append(" FEISCY="+rsl.getString("FEISCY")+",\n")
				  .append(" BEIZ='"+rsl.getString("BEIZ")+"',\n")
				  .append(" LURY='"+rsl.getString("LURY")+"',\n" )
			      .append(" LURSJ=to_date('"+rsl.getString("LURSJ")+"','yyyy-mm-dd')\n");
				sb.append(" where id=").append(rsl.getString("id")).append(";\n");

			}
		}
		sb.append("end;");
		con.getInsert(sb.toString());
//		AutoCreateShouhcrb.Create(con,diancxxb_id,DateUtil.getDate(strDate));
		String sql = "select * from xitxxb where zhi ='��' and leib='��¯' and mingc ='��¯ú�������ܼ���' and zhuangt =1 ";
		if(con.getHasIt(sql)){
			Compute.UpdateRulmzl(con, DateUtil.FormatOracleDate(strDate), String.valueOf(diancxxb_id));
		}
		con.commit();
		con.Close();
		setMsg(ErrorMessage.SaveSuccessMessage);

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
	
	private boolean jiesButton = false;

	public void jiesButton(IRequestCycle cycle) {
		jiesButton = true;
	}
	
	private boolean _DaorChick;
	
	public void DaorButton(IRequestCycle cycle){
		_DaorChick=true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
		}
		if(shousButton){
			shousButton = false;
			shous();
		}
		if(jiesButton){
			jiesButton = false;
			jies();
		}
		if(_DaorChick){
			_DaorChick=false;
			Daor();
		}
		getSelectData();
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
		ResultSetList rsl2=null;
		String dcidStr=getFenc(con);
		
		String insertSql=" delete from RULMZLB where id in ( select RULMZLB_ID from meihyb where diancxxb_id in ("+dcidStr+") and rulrq="+DateUtil.FormatOracleDate(this.getRiqi())+" );\n";
		insertSql+=" delete from meihyb where diancxxb_id in ("+dcidStr+") and rulrq="+DateUtil.FormatOracleDate(this.getRiqi())+";\n";
		String sql=" select * from xitxxb where mingc='��¯ú���õ������ݱ�' and leib='��¯'  and zhuangt=1";
		ResultSetList rsl=con.getResultSetList(sql);
		
		String tableName="jhgl_rlsj";
		if(rsl.next()){
			tableName=rsl.getString("zhi");
		}
		
		String DefaultShjb = "";		//
		String Str = "5";	//SQL�������  ֻ�г���Ӧ���״̬������
		String SQL = "select mingc from xitxxb where leib = 'ú����' and zhuangt = 1 and diancxxb_id = "
			+ visit.getDiancxxb_id();
		ResultSetList rs = con.getResultSetList(SQL);
		if (rs.next()) {
			DefaultShjb = rs.getString("mingc");
		}
		rs.close();
		if (!DefaultShjb.equals("�����")) {
			Str = "2";
		} 
		rs.close();
	
		
		
		if(tableName!=null ){//��� ��ͷ
			
			sql=" select j.id,nvl('#','')||REGEXP_SUBSTR(j.mingc,'[0-9]+') mingc,j.diancxxb_id from jizfzb j where j.diancxxb_id in ("+dcidStr+") and leib='��¯����' order by xuh asc ";
			rsl=con.getResultSetList(sql);
			
			while(rsl.next()){
				
				String mingc=rsl.getString("mingc");
				String jizid=rsl.getString("id");
				String dc_id=rsl.getString("diancxxb_id");
//				�ֶζ��壺dt_rq�����ڣ���ct_zbbh��ָ�꣩��am_brsz��ָ��ֵ��
				String sql2=" select dt_rq,ct_zbbh,ct_zbmc,am_brsz,am_bysz from "+tableName+"" +
						"  where dt_rq="+DateUtil.FormatOracleDate(this.getRiqi())+" and nvl('#','')||REGEXP_SUBSTR(ct_zbbh,'[0-9]+')='"+mingc+"' " +
						" and ct_zbbh like'%ml%' order by ct_zbbh desc";
				
				rsl2=con.getResultSetList(sql2);
				
				String fadh="";	//�������
				String gongrh="";	//���Ⱥ���
				boolean flag=false;
				while(rsl2.next()){
					
					String zbbm=rsl2.getString("ct_zbbh");
					if(zbbm.toLowerCase().indexOf("fdhytrml")!=-1 || zbbm.toLowerCase().indexOf("sml")!=-1
							|| zbbm.toLowerCase().indexOf("fdtrml")!= -1){
						fadh=String.valueOf(CustomMaths.Round_new(rsl2.getDouble("am_brsz"),2));
					}
					if(zbbm.toLowerCase().indexOf("grhytrml")!=-1 || zbbm.toLowerCase().indexOf("grtrml")!=-1){
						gongrh=String.valueOf(CustomMaths.Round_new(rsl2.getDouble("am_brsz"),2));
					}
					
					flag=true;
				}
				
				if(flag){//�û������������Ҫ���뵽meihyb��
					
					if(fadh==null || fadh.equals("")) fadh="0";
					if(gongrh==null || gongrh.equals("")) gongrh="0";
					
//					fadh=Double.parseDouble(fadh)*10+"";
//					gongrh=Double.parseDouble(gongrh)*10+"";
					
					String meizlb_id=MainGlobal.getNewID(visit.getDiancxxb_id());
					
			
					insertSql+=" insert into rulmzlb (id,diancxxb_id,fenxrq,rulbzb_id,jizfzb_id,rulrq,lursj,shenhzt) values \n" +
							"  ("+meizlb_id+","+dc_id+","+DateUtil.FormatOracleDate(this.getRiqi())+"+1,232196556,"+jizid+","+DateUtil.FormatOracleDate(this.getRiqi())+"," +
									" sysdate, 0);\n";
					insertSql+=" insert into meihyb(id,rulrq,diancxxb_id,RULMZLB_ID,RULBZB_ID,JIZFZB_ID,FADHY,GONGRHY,QITY,FEISCY,BEIZ,LURY,LURSJ,SHENHZT) \n" +
							" values(getnewid("+dc_id+"),"+DateUtil.FormatOracleDate(this.getRiqi())+","+dc_id+","+meizlb_id+"," +
									" 232196556,"+jizid+","+fadh+","+gongrh+",0,0,'','"+visit.getRenymc()+"',sysdate,"+Str+");\n";
				}
			}
			
			insertSql=" begin \n"+insertSql+" end ;";
			
			con.getUpdate(insertSql);
			rsl.close();
			if(rsl2!=null){
				
				rsl2.close();
			}
			con.Close();
		}
		
	}
	private void jies(){
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String sql="select *\n" +
		"from meihyb\n" + 
		"where to_char(meihyb.rulrq,'yyyy-mm-dd')='"+getRiqi()+"'";
		ResultSetList rs = con.getResultSetList(sql);
		String rulmzlb_id = "0";
		try{
			if(rs.next()){//�����¯���ô���
				setMsg("ú�����Ѿ����ڣ���ɾ�����ٲ�����");
			}else{
				int flag=0;
				con.setAutoCommit(true);
//				����rulmzlb��meihyb
				rulmzlb_id= MainGlobal.getNewID(visit.getDiancxxb_id());
			   sql="insert into rulmzlb (id,diancxxb_id,fenxrq,rulbzb_id,jizfzb_id,rulrq,lursj,shenhzt) values (\n"+
			   rulmzlb_id+","+visit.getDiancxxb_id()+",to_date('"+getRiqi()+"','yyyy-mm-dd')+1,(select id from rulbzb where mingc='ȫ��'),"+
			   "(select id from JIZFZB where mingc='ȫ������'),"+"to_date('"+getRiqi()+"','yyyy-mm-dd'),sysdate,0)";
			   flag= con.getInsert(sql);
			   if(flag==-1){
				   con.rollBack();
				   return;
			   }
			   sql=
				   "insert into meihyb(id,rulrq,diancxxb_id,rulmzlb_id,rulbzb_id,jizfzb_id,fadhy,gongrhy,shenhzt)(\n" +
				   "select getnewid("+visit.getDiancxxb_id()+")id,to_date('"+getRiqi()+"','yyyy-mm-dd')riq,"+visit.getDiancxxb_id()+" diancxxb_id,"+rulmzlb_id+" RULMZLB_ID,\n" + 
				   "(select id from rulbzb where mingc='ȫ��')rulbzb_id,\n" + 
				   "(select id from JIZFZB where mingc='ȫ������')JIZFZB_ID,\n" + 
				   "b.fadhy,b.gongrhy,5 shenhzt\n" + 
				   "from (\n" + 
				   "     select nvl(sum(fadhy),0)fadhy,nvl(sum(gongrhy),0)gongrhy\n" + 
				   "    from (\n" + 
				   "    select plan_data fadhy,0 gongrhy\n" + 
				   "    from zhiren@tj t\n" + 
				   "    where t.plan_xh='90041' and t.plan_date = to_date('"+getRiqi()+"', 'yyyy-mm-dd')--һ��ƽ�ⷢ�����Ȼú��\n" + 
				   "    union\n" + 
				   "    select 0 fadhy,plan_data gongrhy\n" + 
				   "    from zhiren@tj t\n" + 
				   "    where t.plan_xh='90054' and t.plan_date = to_date('"+getRiqi()+"', 'yyyy-mm-dd')--һ��ƽ�⹩����Ȼú��\n" + 
				   "    )\n" + 
				   ")b\n" + 
				   " )";
			   flag=con.getInsert(sql);
			   if(flag==-1){
				   con.rollBack();
				   return;
			   }
			   	con.commit();
			}
		}catch(Exception e){
			e.printStackTrace();
			con.rollBack();
		}finally{
			con.Close();
		}
		
	}
	private void shous(){
		JDBCcon con=new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String sql="select *\n" +
		"from meihyb\n" + 
		"where to_char(meihyb.rulrq,'yyyy-mm-dd')='"+getRiqi()+"'";
		ResultSetList rs=con.getResultSetList(sql);
		if(rs.getRows() > 0){
			setMsg("ú�����Ѿ����ڣ���ɾ�����ٲ�����");
			return;
		}else{
//			��¯ú������
			String rulmzlb_id= MainGlobal.getNewID(visit.getDiancxxb_id());
			String riq = DateUtil.FormatOracleDate(getRiqi());
			String jizfzbid = "2018453";
			String rulbzbid = "20168046";
			String jiz = "201503048";
			double fadl = 0;
			double gongdl = 0;
			double shangwdl = 0;
//			double gongrl = 0;
			double fadfhl = 0;
			double mt = 0.0;
			double v = 0.0;
			double a = 0.0;
			double qnet_ar = 0.0;
			double fadmh = 0.0;
			double gongrmh = 0.0;
			sql = "select rq,(sml910+sml123) as haoyl,round(decode(nvl(sml910+sml123,0),0,0,(nvl(qsf910*sml910,0)+nvl(qsf123*sml123,0))\n" +
				"/nvl(sml910+sml123,0)),2) as quansf,\n" + 
				"round(decode(nvl(sml910+sml123,0),0,0,(nvl(hff910*sml910,0)+nvl(hff123*sml123,0))/nvl(sml910+sml123,0)),2) as huiff,\n" + 
				"round(decode(nvl(sml910+sml123,0),0,0,(nvl(hf910*sml910,0)+nvl(hf123*sml123,0))/nvl(sml910+sml123,0)),2) as huif\n" + 
				"from hdsc_yxhxbz_new@hdsc where bc=4 and rq = '"+getRiqi()+"' order by rq desc";
			rs = con.getResultSetList(sql);
			if(rs.next()){
				mt = rs.getDouble("quansf");
				v  = rs.getDouble("huiff");
				a  = rs.getDouble("huif");
			}
			rs.close();
			sql = 
				"select (ss.fadl+zy.fadl+gf.fadl+hz.fadl) as fadl,(ss.gongdl+zy.gongdl+gf.gongdl+hz.gongdl) as gongdl,\n" +
				"(ss.shangwdl+zy.shangwdl+gf.shangwdl+hz.shangwdl) as shangwdl,\n" + 
				"round(decode((ss.a156+zy.a156+gf.a156+hz.a156),0,0,(ss.a163+zy.a163+gf.a163+hz.a163)/(ss.a156+zy.a156+gf.a156+hz.a156)),2) as fadfhl,\n" + 
				"(ss.gongrl+zy.gongrl+gf.gongrl+hz.gongrl) as gongrl,(ss.meil+zy.meil+gf.meil+hz.meil) as meihy,\n" + 
				"(ss.meil+zy.meil+gf.meil+hz.meil)-(ss.grmh+zy.grmh+gf.grmh+hz.grmh) as fdmh,\n" + 
				"(ss.grmh+zy.grmh+gf.grmh+hz.grmh) as grmh,\n" + 
				"round(decode((ss.meil+zy.meil+gf.meil+hz.meil),0,0,(ss.meil*ss.diwr+zy.meil*zy.diwr+gf.meil*gf.diwr+hz.meil*hz.diwr)/(ss.meil+zy.meil+gf.meil+hz.meil))/1000,3) as diwr\n" + 
				"\n" + 
				"from\n" + 
				"\n" + 
				"(select round(a168/10000,2) as fadl,a163,a156,\n" + 
				"decode(a334,0,0,round(a406*29271/a334,0)) grmh,round((a168-a179)/10000,2) as gongdl,round(a198/10000,3) as shangwdl,\n" + 
				"round(a400) as gongrl,round(a164,2) as fadfhl,round(a334) as diwr,a37 as meil\n" + 
				"from hdrd.rzhbview@hdjh where targdate="+riq+" and eqpgrpcd=6) ss,\n" + 
				"\n" + 
				"(select round(a168/10000,2) as fadl,a163,a156,\n" + 
				"decode(a334,0,0,round(a406*29271/a334,0)) grmh,round((a168-a179)/10000,2) as gongdl,round(a198/10000,3) as shangwdl,\n" + 
				"round(a628) as gongrl,round(a164,2) as fadfhl,round(a334) as diwr,a622 as meil\n" + 
				"from hdrd.rzhbview@hdjh where targdate="+riq+" and eqpgrpcd=7) zy,\n" + 
				"\n" + 
				"(select round(a168/10000,2) as fadl,a163,a156,\n" + 
				"decode(a334,0,0,round(a406*29271/a334,0)) grmh,round((a168-a179)/10000,2) as gongdl,round(a198/10000,3) as shangwdl,\n" + 
				"round(a400) as gongrl,round(a164,2) as fadfhl,round(a334) as diwr,a37 as meil\n" + 
				"from hdrd.rzhbview@hdjh where targdate="+riq+" and eqpgrpcd=8) gf,\n" + 
				"\n" + 
				"(select a.fadl,a.fadl-b.diancydl as gongdl,a.a163,a.a156,\n" + 
				"decode(b.diwr,0,0,round(a.a406*29271/b.diwr,0)) as grmh,b.shangwdl,a.gongrl,a.fadfhl,b.diwr,b.meil from\n" + 
				"(select round(a168/10000,3) as fadl,a163,a156,a406,\n" + 
				"round(a400) as gongrl,round(a164,2) as fadfhl,round(a514) as diwr,a37 as meil\n" + 
				"from hdrd.rzhbview@hdjh where targdate="+riq+" and eqpgrpcd=3) a,\n" + 
				"(select round(a513/10000) diancydl,round(a514) as diwr,round(a619/10000,3) as shangwdl,a580 as meil\n" + 
				"from hdrd.rzhbview@hdjh where targdate="+riq+" and eqpgrpcd=7) b) hz";
			rs = con.getResultSetList(sql);
			if(rs.next()){
				fadl = rs.getDouble("fadl");
				gongdl = rs.getDouble("gongdl");
//				gongrl = rs.getDouble("gongrl");
				shangwdl = rs.getDouble("shangwdl");
				fadfhl = rs.getDouble("fadfhl");
				qnet_ar = rs.getDouble("diwr");
				fadmh = rs.getDouble("fdmh");
				gongrmh = rs.getDouble("grmh");
			}
			sql = "insert into rulmzlb(id,rulrq,fenxrq,diancxxb_id,rulbzb_id," +
				"jizfzb_id,qnet_ar,mt,vad,ad,lursj,lury,meil,shenhzt) values(" + rulmzlb_id +
				"," + riq + "," + riq + "+1,306," +
				rulbzbid + "," + jizfzbid + "," + qnet_ar + "," + mt + "," +
				v + "," + a + ",sysdate,'"+visit.getRenymc()+"',"+(fadmh+gongrmh)+",0)";
			con.getInsert(sql);
			sql = "insert into meihyb(id,rulrq,diancxxb_id,rulmzlb_id,rulbzb_id," +
			"jizfzb_id,fadhy,gongrhy,lursj,lury) values(getnewid(306)," + riq + ",306," + rulmzlb_id + "," +
			rulbzbid + "," + jizfzbid + ",0,0,sysdate,'" +
			visit.getRenymc() + "')";
			con.getInsert(sql);
			sql = "INSERT INTO riscsjb(ID,diancxxb_id,riq,jizb_id,fadl,gongdl,fadfhl,shangwdl)\n"+
				  "VALUES(getnewid(306),306,"+riq+","+jiz +
				  ","+fadl+","+gongdl+","+fadfhl+","+shangwdl+")";
			con.getInsert(sql);
		}
		
		rs.close();
		con.Close();
		
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String rulrq = DateUtil.FormatOracleDate(this.getRiqi());	//��¯���� 
		String DefaultShzt = "5";		//Ĭ�����״̬  �ڲ�����ϵͳ���õ�ʱ��Ĭ��Ϊ�����
		String DefaultShjb = "";		//
		String Str = "m.shenhzt = 5";	//SQL�������  ֻ�г���Ӧ���״̬������
		String SQL = "select mingc from xitxxb where leib = 'ú����' and zhuangt = 1 and diancxxb_id = "
				+ visit.getDiancxxb_id();
		ResultSetList rs = con.getResultSetList(SQL);
		if (rs.next()) {
			DefaultShjb = rs.getString("mingc");
		}
		rs.close();
		if (!DefaultShjb.equals("�����")) {
			DefaultShzt = "1";
			Str = "(m.shenhzt = 0 or m.shenhzt = 2)";
		} 
		//�糧Treeˢ������
		String diancxxb_id="";
		if(getDiancTreeJib()==1){
			diancxxb_id = "";
		}else if(getDiancTreeJib()==2){
			diancxxb_id = "and (d.id = " + this.getTreeid() + " or d.fuid = " + this.getTreeid() + ")\n";
		}else if(getDiancTreeJib()==3){
			if(visit.isFencb()){
				diancxxb_id = "and (d.id = " + this.getTreeid() + " or d.fuid = " + this.getTreeid() + ")\n";
			}else{
				diancxxb_id = "and (d.id = " + this.getTreeid() + ")\n";
			}
		}
		
		String chaxun = "select m.id,m.diancxxb_id, m.rulrq,m.rulmzlb_id, rb.mingc as rulbzb_id,j.mingc as jizfzb_id,\n"
				+ "  m.fadhy,m.gongrhy,m.qity,m.feiscy,m.zhiyr,m.beiz,m.lury,m.lursj,m.shenhzt\n"
				+ "  from meihyb m, diancxxb d, rulmzlb r, rulbzb rb, jizfzb j\n"
				+ " where m.diancxxb_id = d.id(+)\n"
				+ "   and m.rulmzlb_id = r.id(+)\n"
				+ "   and m.rulbzb_id = rb.id(+)\n"
				+ "   and m.jizfzb_id = j.id(+)\n"
				+ "   and m.rulrq = "+ rulrq + "\n"

				+ diancxxb_id
				+ "   and " + Str
		        +"  order by m.rulrq,rb.xuh,j.xuh";

		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("meihyb");
		egu.setWidth("bodyWidth");
		egu.getColumn("diancxxb_id").setHeader("��λ");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("rulrq").setHeader("��������");
		egu.getColumn("rulrq").setEditor(null);
		egu.getColumn("rulmzlb_id").setHidden(true);
		egu.getColumn("rulmzlb_id").setEditor(null);
		egu.getColumn("rulbzb_id").setHeader("��¯����");
		egu.getColumn("jizfzb_id").setHeader("��¯����");
		egu.getColumn("jizfzb_id").setWidth(110);
		egu.getColumn("fadhy").setHeader("�������(��)");
		egu.getColumn("gongrhy").setHeader("���Ⱥ���(��)");
		egu.getColumn("qity").setHeader("������(��)");
		egu.getColumn("feiscy").setHeader("��������(��)");
		egu.getColumn("zhiyr").setHeader("������");
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("lury").setHeader("¼��Ա");
		egu.getColumn("lury").setHidden(true);
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("lursj").setHeader("¼��ʱ��");
		egu.getColumn("lursj").setHidden(true);
		egu.getColumn("lursj").setEditor(null);
		egu.getColumn("shenhzt").setHeader("״̬");
		egu.getColumn("shenhzt").setHidden(true);
		egu.getColumn("shenhzt").setEditor(null);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(25);// ���÷�ҳ
		// *****************************************����Ĭ��ֵ****************************
		egu.getColumn("diancxxb_id").setDefaultValue(
				"" + visit.getDiancxxb_id());
		egu.getColumn("rulrq").setDefaultValue(this.getRiqi());
		egu.getColumn("lury").setDefaultValue(visit.getRenymc());
		egu.getColumn("lursj").setDefaultValue(DateUtil.FormatDate(new Date()));
		egu.getColumn("rulmzlb_id").setDefaultValue("0");
		egu.getColumn("fadhy").setDefaultValue("0");
		egu.getColumn("gongrhy").setDefaultValue("0");
		egu.getColumn("qity").setDefaultValue("0");
		egu.getColumn("feiscy").setDefaultValue("0");
		egu.getColumn("shenhzt").setDefaultValue(DefaultShzt);

		// ������������¯����
		ComboBox cb_banz = new ComboBox();
		egu.getColumn("rulbzb_id").setEditor(cb_banz);
		cb_banz.setEditable(true);
		String rulbzb_idSql = "select r.id,r.mingc from rulbzb r,diancxxb d  where r.diancxxb_id=d.id(+)"
			+ "and (d.fuid="+getTreeid()+" or d.id = "+getTreeid()+")   order by r.xuh";
		egu.getColumn("rulbzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(rulbzb_idSql));
		egu.getColumn("rulbzb_id").setReturnId(true);
		//���Ƿ�����¯�Ƚ���,��������¯����,����ר��Ϊ���Ƿ�����¯�������Ĭ��ֵ
		if(visit.getDiancxxb_id()==264){//264�����Ƿ���ĵ糧id
			egu.getColumn("rulbzb_id").setDefaultValue("ȫ��");
		}
		// ������������¯����
		ComboBox cb_jiz = new ComboBox();
		egu.getColumn("jizfzb_id").setEditor(cb_jiz);
		cb_jiz.setEditable(true);
		String cb_jizSql = "select j.id,j.mingc from jizfzb j,diancxxb d where j.diancxxb_id=d.id(+) " +
				"and (d.fuid="+getTreeid()+" or d.id = "+getTreeid()+")   order by j.xuh";
		egu.getColumn("jizfzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(cb_jizSql));
		egu.getColumn("jizfzb_id").setReturnId(true);
		
        // ����������������
		ComboBox zhiyr = new ComboBox();
		egu.getColumn("zhiyr").setEditor(zhiyr);
		zhiyr.setEditable(true);
		String cb_zhiyr = "select id, quanc from renyxxb where bum = '����'";
		egu.getColumn("zhiyr").setComboEditor(egu.gridId,
				new IDropDownModel(cb_zhiyr));
		egu.getColumn("zhiyr").editor.setAllowBlank(true);

		// ������
		egu.addTbarText("����:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq");
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("-");
		
//		 �糧��
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarText("-");// ���÷ָ���
		egu.addTbarText("�糧��");
		egu.addTbarTreeBtn("diancTree");

		egu.addTbarText("-");

		// ************************************************************
		// ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){").append(
				"document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
		// ʹ�ò����ж��Ƿ���ʾ������ť
		if (MainGlobal.getXitxx_item("����", "��¯ú��ʾ��ť", this.getTreeid(), "��").equals("��")) {
			egu.addToolbarButton(GridButton.ButtonType_Insert, null);
			if(visit.getboolean1()){
				egu.addToolbarButton(GridButton.ButtonType_Delete, null);
			}else{
				egu.addOtherScript("gridDiv_grid.addListener('beforeedit',function(e){"
						+ "if(e.record.get('ID')!='0'){"
						+ " e.cancel=true;" + "}" + "});");
			}
			egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		}
		
		String sql="select * from xitxxb where mingc='��¯�ӿ���ʾ��ť' and zhi='��' and zhuangt=1";
		ResultSetList rs1 = con.getResultSetList(sql);
		if(rs1.next()){
			egu.addToolbarItem("{"+new GridButton("��������","function(){document.getElementById('jiesButton').click();}").getScript()+"}");
		}
		
	
		
		if(MainGlobal.getXitxx_item("���õ���", "���纪���ӿڰ�ť", "0", "��").equals("��")){
//			egu.addToolbarButton(GridButton.."��ȡ����", "shouqsj");
			egu.addToolbarItem("{"+new GridButton("��ȡ����","function(){document.getElementById('shousButton').click();}").getScript()+"}");
		}
		
		//��ӵ��밴ť
		sql=" select * from xitxxb where mingc='��¯ú������ӵ��밴ť' and leib='��¯' and zhuangt=1 and zhi='��' ";
		rs1=con.getResultSetList(sql);
		if(rs1.next()){
			GridButton daor=new GridButton("����","function(){ Ext.MessageBox.confirm('��ʾ��Ϣ','ȷ��Ҫ���ǵ���ʱ���������?',function(btn){if(btn=='yes'){document.all.DaorButton.click();} });}");
			egu.addTbarBtn(daor);
		}
		
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
			visit.setboolean1(true);
			if(cycle.getRequestContext().getParameter("lx") != null && cycle.getRequestContext().getParameter("lx").equals("N")){
				visit.setboolean1(false);
			}
			setRiqi(null);
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			getSelectData();
		}
	}

	

	public static void UpdateRulzlID(String riq, long diancxxb_id) {
		StringBuffer sb = new StringBuffer();
		sb
				.append("update meihyb h ")
				.append("set rulmzlb_id = ( \n")
				.append(
						"select nvl(max(id),0) from rulmzlb z where z.rulrq = h.rulrq \n")
				.append(
						"and z.diancxxb_id = h.diancxxb_id and z.rulbzb_id = h.rulbzb_id \n")
				.append("and z.jizfzb_id = h.jizfzb_id ) where h.rulrq = ")
				.append(DateUtil.FormatOracleDate(riq)).append(
						" and h.diancxxb_id=").append(diancxxb_id);
		JDBCcon con = new JDBCcon();
		con.getUpdate(sb.toString());
		con.Close();
	}

	
//	 �õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getIDropDownDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return IDropDownDiancmc;
	}


	boolean treechange = false;

//	private String treeid;

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
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
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}

		return jib;
	}

}