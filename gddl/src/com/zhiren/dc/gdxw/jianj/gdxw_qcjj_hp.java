package com.zhiren.dc.gdxw.jianj;

import java.sql.PreparedStatement;
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

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dc.huaygl.Caiycl;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/* ����:wzb
 * ����:2010-6-10 9:21:24
 * �޸�����:�޸Ĳ���������ɵķ���,������500��һ������Ϊÿ�춼����
 * 
 */


/*
 * ����:tzf
 * ʱ��:2009-12-28
 * �޸�����:���ĵ�������ʾ��Ϣ������
 */
/*
 * ����:tzf
 * ʱ��:2009-12-27
 * �޸�����:���������� ����ɫ��ǲ���
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-09-23
 * ����:ѡ�����Ͱ��ʱ���ų�(3����)�����Ĳ���Ͱ��
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-09-22 
 * ����������ú��Ա�༭�� alter table chepbtmp add meigy varchar2(30) null
 */

/*
 * ���ߣ����ܱ�
 * ʱ�䣺2009-09-26
 * ������gdxw_cy��������ʱ���ֶ� alter table gdxw_cy add shengcrq date null
 */
/*
 * ���ߣ����ܱ�
 * ʱ�䣺2009-10-16
 * ������gdxw_cy���ӳ����ֶ�,0�Ǵ�,1��С��,��С�����жϱ�׼�Ǿ����Ƿ����45��
 *  alter table gdxw_cy add chex number(1) null;
 */
/**
 * @author Rock
 * @since 2009.09.14
 * @version 1.0
 * @discription �����������������
 */
public class gdxw_qcjj_hp extends BasePage implements PageValidateListener {
	private double mk_kdl_sdz=0;//ú��۶����趨ֵ
	private double mk_kdl_sjz=0;//ú��۶���ʵ��ֵ
	private final static String Customkey = "gdxw_qcjj_hp";
	private  boolean IsUpdateChelPiz=false;//�Ƿ�����˳�����Ϣ���Ƥ����Ϣ
	// �����û���ʾ
	private String msg = "";
	private double chelxxb_cheph_piz=0;
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

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
//	ˢ���������ڰ�
	private String riq;
	public void setRiq(String riq) {
		this.riq = riq;
	}
	public String getRiq() {
		return riq;
	}
	public IDropDownModel getYunsdwModel() {
		Visit visit = (Visit) this.getPage().getVisit();
		String sql = "select id,mingc from yunsdwb where diancxxb_id="
				+ visit.getDiancxxb_id();
		return new IDropDownModel(sql);
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
//	����
	private boolean _JusChick = false;

	public void JusButton(IRequestCycle cycle) {
		_JusChick = true;
	}




	
	//ú�����ִ�С�����в���,�Ծ���40��Ϊ����,���ش���40��Ϊ��,С��40��ΪС��,ֻ�жϴ�С��,���ж�ÿ500����Ϊһ����.
	//Ҳ����˵һ����������˲��ܶ��ٶ�,ֻ�жϳ���,���ж�ú������.
	private int countCaiy_new(JDBCcon con, long diancxxb_id,String meikdwmc,
			double maoz, double piz, double koud,String chepbtmp_id){
		Visit visit = (Visit) this.getPage().getVisit();
		String Qufdxc="select zhi,beiz from xitxxb x where x.mingc='����500�ֲ����Ƿ����ִ�С��' and zhuangt=1 and diancxxb_id="+visit.getDiancxxb_id()+"";
		ResultSetList Daxcrs = con.getResultSetList(Qufdxc); 
		String sql="";
		boolean Bool_Qufdxc=false;//�Ƿ����ִ�С������
		boolean Bool_dac=false;  //�Ƿ��Ǵ�
		if(Daxcrs.next()){
			String Dx_zhi=Daxcrs.getString("zhi");
			double Dx_jingz=Daxcrs.getDouble("beiz");
			//500�ַ����Ƿ����ִ�С��
			if(Dx_zhi.equals("��")){
				Bool_Qufdxc=true;
				String chex="";
				//���maoz-piz-koud>=Dx_jingz ,�Ǵ�,������С��,(Dx_jingz��45��),��chex=0,С��chex=1
				if(maoz-piz-koud>=Dx_jingz){
					Bool_dac=true;
					chex=" and chex=0";
				}else{
					chex=" and chex=1";
				}
				 sql = "select * from gdxw_cy where diancxxb_id = " + diancxxb_id +
				" and meikdwmc = '" + meikdwmc + "' and zhuangt = 1 and to_char(shengcrq,'yyyy-mm-dd')=to_char(sysdate,'yyyy-mm-dd')  "+chex+""; 
			}else{
//				�жϲ���������û��δ�����Ĳ�����Ϣ(zhuangt = 0)
				 sql = "select * from gdxw_cy where diancxxb_id = " + diancxxb_id +
				" and meikdwmc = '" + meikdwmc + "' and zhuangt = 1 and to_char(shengcrq,'yyyy-mm-dd')=to_char(sysdate,'yyyy-mm-dd') ";
			}
		}
		
		
		
 
		ResultSetList rs = con.getResultSetList(sql); 
		String id = "";
		String sql1="";
		int flag = 0;
		if(rs.next()){
//			������������м�¼˵���ó������ô˱��ֱ�Ӹ��������Ϣ����
			id = rs.getString("id");
			double old_maoz = rs.getDouble("maoz");
			double old_piz = rs.getDouble("piz");
			double old_koud = rs.getDouble("koud");
			double new_maoz = CustomMaths.add(old_maoz, maoz);
			double new_piz = CustomMaths.add(old_piz, piz);
			double new_koud = CustomMaths.add(old_koud, koud);
			double new_jingz = CustomMaths.sub(new_maoz, new_piz);
			double new_jingz_koud=CustomMaths.sub(new_jingz, new_koud);
			long zhilb_id=rs.getLong("zhilb_id");
			sql = "update gdxw_cy set maoz = " + new_maoz +
			", piz = " + new_piz + 
			", koud = " + new_koud + 
			", jingz = " + new_jingz_koud +
			",zhiyrq=sysdate"+
			" where id = " + rs.getString("id");

			flag = con.getUpdate(sql);
		
			if(flag == -1){
				WriteLog.writeErrorLog(this.getClass().getName() + 
						"\nSQL:" + sql +"����gdxw_cy��ʧ��!");
				setMsg(this.getClass().getName() + ":����gdxw_cy��ʧ��!");
				return flag;
			}
			
//			����chepbtmp�е�zhilb_id
			sql1="update chepbtmp set zhilb_id="+zhilb_id+",fahbtmp_id="+zhilb_id+" where id="+chepbtmp_id+"";
			flag=con.getUpdate(sql1);
			if(flag == -1){
				WriteLog.writeErrorLog(this.getClass().getName() + 
						"\nSQL:" + sql +"���¸���chepbtmp�е�zhilb_id��ʧ��!");
				setMsg(this.getClass().getName() + ":���¸���chepbtmp�е�zhilb_idʧ��!");
				return flag;
			}
			
		}else{
//			���Ϊ��������
			id = MainGlobal.getNewID(diancxxb_id);
//			����������������
			long zhilb_id = Jilcz.getZhilbid(con, null, new Date(), visit.getDiancxxb_id());
//			���ɲ�����������������
			flag = Caiycl.CreatBianh(con, zhilb_id, visit.getDiancxxb_id());
			if(flag == -1){
				WriteLog.writeErrorLog(this.getClass().getName() + 
						"\n�������ʧ��!");
				setMsg(this.getClass().getName() + ":�������ʧ��!");
				return flag;
			}
			
			
//			����δʹ�õĴ���λ��(�������δʹ�ù���)
			
			/*sql = "select * from cunywzb where id in\n" +
				"(select id from cunywzb where zhuangt = 1\n" + 
				"minus\n" + 
				"select cunywzb_id from caiyb where zhilb_id in\n" + 
				"(select zhilb_id from gdxw_cy where zhuangt = 1 \n" +
				"and (zhiyrq <= sysdate and zhiyrq>= sysdate -1))\n" + 
				") and rownum = 1";*/
			
			sql = "select * from cunywzb where id in\n" +
			"(select id from cunywzb where zhuangt = 1\n" + 
			"minus\n" + 
			"select cunywzb_id from caiyb where zhilb_id in\n" + 
			"(select zhilb_id from gdxw_cy where zhuangt = 1 \n" +
			"and shengcrq>=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')))\n" + 
			"and rownum = 1";
			
			rs = con.getResultSetList(sql);
			if(rs.next()){
//				���²������д���λ��
				sql = "update caiyb set cunywzb_id = " + rs.getString("id") + 
				" where zhilb_id = " + zhilb_id;
//              ����chepbtmp�е�zhilb_id
				 sql1="update chepbtmp set zhilb_id="+zhilb_id+",fahbtmp_id="+zhilb_id+" where id="+chepbtmp_id+"";
				con.getUpdate(sql);
				con.getUpdate(sql1);
			}else{
				WriteLog.writeErrorLog(this.getClass().getName() + 
				"\n����λ������,����Ӵ���λ��!");
				setMsg(this.getClass().getName() + ":����λ������,����Ӵ���λ��!");
				return -1;
			}
//			long 
			double jingz=maoz-piz-koud;
			//�Ƿ����ִ�С������
			if(Bool_Qufdxc){
				//����Ǵ�,chex=0,���������С��chex=1
				if(Bool_dac){
					sql = "insert into gdxw_cy(id, diancxxb_id, meikdwmc, maoz, zhilb_id,zhuangt,piz,koud,jingz,shengcrq,chex,zhiyrq) " +
					"values("+id+"," + diancxxb_id +
					",'" + meikdwmc + "'," + maoz + "," + zhilb_id+ ",1,"+piz+","+koud+","+jingz+",sysdate,0,sysdate)";
				}else{
					sql = "insert into gdxw_cy(id, diancxxb_id, meikdwmc, maoz, zhilb_id,zhuangt,piz,koud,jingz,shengcrq,chex,zhiyrq) " +
					"values("+id+"," + diancxxb_id +
					",'" + meikdwmc + "'," + maoz + "," + zhilb_id+ ",1,"+piz+","+koud+","+jingz+",sysdate,1,sysdate)";
				}
				
			}else{
				sql = "insert into gdxw_cy(id, diancxxb_id, meikdwmc, maoz, zhilb_id,zhuangt,piz,koud,jingz,shengcrq,zhiyrq) " +
				"values("+id+"," + diancxxb_id +
				",'" + meikdwmc + "'," + maoz + "," + zhilb_id+ ",1,"+piz+","+koud+","+jingz+",sysdate,sysdate)";
			}
			
			
			
			
			
			
			
			flag = con.getInsert(sql);
			if(flag == -1){
				WriteLog.writeErrorLog(this.getClass().getName() + 
				"\n����gdxw_cy��Ϣʧ��!");
				setMsg(this.getClass().getName() + ":����gdxw_cy��Ϣʧ��!");
				return flag;
			}
		}
		rs.close();
		return flag;
	}

	private boolean _SavePizChick = false;

	public void SavePizButton(IRequestCycle cycle) {
		_SavePizChick = true;
	}

	//�ж�Ƥ���Ƿ񳬹�ϵͳ���趨�ķ�Χ,ֻ�ж�����,���ж�����,����chelxxb��Ƥ����20��,ֻ�жϵ���20�ֵ������Ƿ񳬹�3%,ʵ����������20���ж�
	private boolean QSuodzt(String cheph,double piz) {
		boolean issuod = false;
		JDBCcon con = new JDBCcon();
		ResultSetList rs=null;
		long xitwcl=0;
		String sql = "";
		sql="select x.zhi from xitxxb x where x.mingc='������ƤƤ����Χ' and zhuangt=1 ";
		 rs = con.getResultSetList(sql); 
		if(rs.next()){
			xitwcl=rs.getLong("zhi");
		}
		sql="select decode(c.piz,0,0,100-Round("+piz+"/c.piz,4)*100) as wucha,c.piz from chelxxb c where c.cheph='"+cheph+"'";
		
		 rs = con.getResultSetList(sql); 
		if (rs.next()) {
				double zhi=Double.parseDouble(rs.getString("wucha"));
				
				if(zhi>xitwcl){
					chelxxb_cheph_piz=rs.getDouble("piz");
					issuod=true;
				}
		}

		rs.close();
		con.Close();

		return issuod;
	}
	//������״̬������쳣��������
	private void InsertSuocztb(String chepbtmpb_id,String cheph,double maoz,double piz){
		JDBCcon con = new JDBCcon();
		ResultSetList rs=null;
		Visit visit = (Visit) this.getPage().getVisit();
		long chelxxb_id=0;
		int flag=0;
		String chelxxbSql="select id from chelxxb where cheph='"+cheph+"'";
		 rs = con.getResultSetList(chelxxbSql); 
		 if(rs.next()){
			  chelxxb_id=rs.getLong("id");
		 }
		 
		 //�ж��Ƿ��ظ�����suocztb��
		 String Ischongf="select * from suocztb s where s.chepbtmp_id="+chepbtmpb_id+"";
		 rs = con.getResultSetList(Ischongf); 
		 if(!rs.next()){
			 //��suocztb���������
			 String newid = MainGlobal.getNewID(visit.getDiancxxb_id());
			 String InserSC="insert into suocztb (id,chelxxb_id,suocsj,suocry,suocyy,zt,chepbtmp_id,maoz,piz) values (" +
			 		""+newid+","+chelxxb_id+",sysdate,'"+visit.getRenymc()+"','Ƥ�س�����Χ',1,"+chepbtmpb_id+","+maoz+","+piz+"" +
			 		") ";
			 flag = con.getInsert(InserSC);
				if(flag == -1){
					WriteLog.writeErrorLog(this.getClass().getName() + 
					"\n����suocztb��Ϣʧ��!");
					setMsg(this.getClass().getName() + ":����suocztb��Ϣʧ��!");
					
				}
		 }
		 
		
		con.Close();	
	}
	
	
	//�жϹ���Ա�Ƿ��Ѿ�����
	private boolean ISJieSuo(String chepbtmp_id ){
		boolean isjies=true;
		ResultSetList rs=null;
		JDBCcon con = new JDBCcon();
		long zt=0;
		String sql="select s.zt from suocztb s where s.chepbtmp_id="+chepbtmp_id+"";
		 rs = con.getResultSetList(sql); 
		if(rs.next()){
			zt=rs.getLong("zt");
			if(zt==2){
				isjies=false;
			}
		}
		con.Close();
		return isjies;
		
	}
	
	private boolean IsChongfgh(String id) {
		boolean ischongfgh = true;
		JDBCcon con = new JDBCcon();
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select  c.qingcsj  from chepbtmp c where c.id=");
		sql.append(id).append("");
		ps = con.getPresultSet(sql.toString());
		try {
			rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getString("qingcsj")==null) {
					ischongfgh = false;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
				con.Close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return ischongfgh;
	}
	
	private void SavePiz() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û�������κθ��ģ�");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ResultSetList rsl = getPizGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "Qincjjlr.SavePiz �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		IDropDownModel idm = new IDropDownModel(SysConstant.SQL_Meic);
		int flag = 0;
		String cheph ="";
		String meikdwmc="";
		double maoz = 0.0;
		double piz =  0.0;
		double biaoz = 0.0;
		double koud =  0.0;
		double jingz =  0.0;
		if (rsl.next()) {
			String id=rsl.getString("id");
			 cheph = rsl.getString("cheph");
			double piz_1=Double.parseDouble(rsl.getString("piz"));
			double maoz_1=Double.parseDouble(rsl.getString("maoz"));
			
			//�ж��Ƿ��ظ�����,��������������������Ѿ�����,�������������Ϊûˢ��,����Ա��ѡ�иó����й������
			if(IsChongfgh(rsl.getString("id"))){
				setMsg("ѡ�񳵺Ŵ���,�ó��Ų�����,��ˢ��!");
				return;
			}
			
			
			//Ƥ���Ƿ񳬹���Χ
			if (QSuodzt(cheph,piz_1)) {
				
				if(ISJieSuo(id)){//�жϹ���Ա�Ƿ��Ѿ���suocztb����
					
//					��suocztb��������
					InsertSuocztb(id,cheph,maoz_1,piz_1);
					setMsg("�ó���ǰƤ��:"+piz_1+"��ϵͳƤ��:"+chelxxb_cheph_piz+"����3%,��ֹ��Ƥ,��֪ͨ����Ա!");
					return;
				}
				
			}
			
			
			long diancxxb_id = 0;
			if (visit.isFencb()) {
				diancxxb_id = (getExtGrid().getColumn("dcmc").combo)
						.getBeanId(rsl.getString("dcmc"));
			} else {
				diancxxb_id = visit.getDiancxxb_id();
			}
			meikdwmc = rsl.getString("meikdwmc");
			
			maoz = rsl.getDouble("maoz");
			piz = rsl.getDouble("piz");
			biaoz = 0.0;
			koud = rsl.getDouble("koud");
			jingz =CustomMaths.sub(CustomMaths.sub(maoz,piz),koud);
			biaoz = maoz - piz - koud;
			String sql = "update chepbtmp set piz =" + piz + ", biaoz = " + biaoz +
			", koud = " + koud + ",  qingchh = '" +
			rsl.getString("qingchh") + "', qingcsj = sysdate, qingcjjy = '" +
			rsl.getString("qingcjjy") + "', meicb_id = " + 
			idm.getBeanId(rsl.getString("meicb_id")) + ",meigy = '" + 
			rsl.getString("meigy") + "',pinz='ԭú',faz='��',daoz='����ú��',jihkj='�г��ɹ�',fahrq=to_date(to_char(sysdate,'yyyy-mm-dd'), 'yyyy-mm-dd')," +
			" daohrq=to_date(to_char(sysdate,'yyyy-mm-dd'), 'yyyy-mm-dd'), chebb_id=3,xiecfs='��ж' where id = " + id;
			flag = con.getUpdate(sql);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog("���³�Ƥ��TMPʧ��!");
				setMsg("���³�Ƥ��TMPʧ��!");
				return;
			}
			flag = countCaiy(con, diancxxb_id, meikdwmc, maoz, piz, koud,id);
			//flag = countCaiy_new(con, diancxxb_id, meikdwmc, maoz, piz, koud,id);
			if(flag == -1) {
				con.rollBack();
				con.Close();
				return;
			}
			// ���³���Ƥ����Ϣ
			SaveChelxx(con,diancxxb_id,cheph,piz);
			
			
			
		}
		con.commit();
//		con.Close();
//		setMsg("����ɹ�������:"+cheph+",ú��:"+meikdwmc+",ë��:"+maoz+",Ƥ��:"+piz+",����:"+jingz+",�۶�:"+koud+"");
		if(IsUpdateChelPiz){
			IsUpdateChelPiz=false;
			setMsg("Ƥ�ر���ɹ�<br>"+cheph+"Ϊ��һ����ú��Ƥ�أ�"+piz+"�Ѿ��ɹ�������������Ϣ��!");
		}else{
			setMsg("Ƥ�ر���ɹ�");
		}
		//�жϵ����ú��Ŀ۶����Ƿ񳬹�ϵͳ�趨ֵ
		boolean meikkdlsd=IsKoudl(con,visit.getDiancxxb_id(),meikdwmc,koud);
		if(meikkdlsd){
			//�۶��ʳ���ϵͳ�趨ֵ,������ú��,��ֹ���ٹ��غ�
			Insert_Meikkdlsdb(con,visit.getDiancxxb_id(),meikdwmc);
		}
		
		con.Close();
		
	}

	public void Insert_Meikkdlsdb(JDBCcon con,long diancxxb_id,String meikdwmc){
			
				//�����ÿ������,��ֹ����غ�,�Ѿ������غ��,�������Ƥ, �������ݴ���meikkdlsdb����
				String TodayIsHaveSuod="select * from meikkdlsdb m where m.meikdwmc='"+meikdwmc+"' and zhuangt=1 and m.suodrq=to_date(to_char(sysdate,'yyyy-mm-dd'), 'yyyy-mm-dd')";
				ResultSetList rsl=con.getResultSetList(TodayIsHaveSuod);
				if(rsl.next()){
					// ��������Ѿ�������,������Ƥ�������ٽ�������.
				}else{
					  String suodsql="insert into meikkdlsdb (id,meikxxb_id,meikdwmc,suodsj,suodr,suodrq,zhuangt) values (" +
		    			""+MainGlobal.getNewID(diancxxb_id)+"," +
		    			"(select max(id) from meikxxb where mingc='"+meikdwmc+"')," +
		    			"'"+meikdwmc+"'," +
		    			"sysdate," +
		    			"'"+((Visit) getPage().getVisit()).getRenymc()+"'," +
		    			"to_date(to_char(sysdate,'yyyy-mm-dd'), 'yyyy-mm-dd'),1" +
		    			") ";
				  con.getUpdate(suodsql);
				  this.setMsg("ú��:----"+meikdwmc+"----,�����ۼƿ۶���Ϊ"+mk_kdl_sjz+",�Ѿ�����ϵͳ�趨ֵ"+mk_kdl_sdz+";<br>-----------ϵͳ�ѽ�ֹ��ú��������غ�!------------");
				}
				
				
			
	
	}
	
	
	//�ж�ú��۶����Ƿ񳬱�,ú��۶���������meikkdlsz����
	public boolean IsKoudl(JDBCcon con,long diancxxb_id,String meikdwmc,double koud){
		
		boolean fanhuizhi=false;
		String koudlSql="select koudl as koudl from meikkdlsz s where s.meikmc='ȫ��'";
		ResultSetList rsl=con.getResultSetList(koudlSql);
		if(rsl.next()){// �����"ȫ��",�Ͱ���ȫ�����趨ֵ���жϿ۶���
			double quanbu_koudl=rsl.getDouble("koudl");
		    String mk_koudl="select koudl as koudl2 from meikkdlsz s where s.meikmc='"+meikdwmc+"' order by s.koudl desc ";
		    rsl=con.getResultSetList(mk_koudl);
		    if(rsl.next()){//�������ȫ��,����ú������,����ú���趨�Ŀ۶���������۶���.
		    	quanbu_koudl=rsl.getDouble("koudl2");
		    }
		    mk_kdl_sdz=quanbu_koudl;//��ȫ�ֱ�����ֵ,Ϊ��������ȡֵ��׼��
		    //sql�жϵ����ú��Ŀ۶��������Ƕ���
		    String pandkoudl="select decode(sum(c.maoz-c.piz-c.koud),0,0,nvl(round(sum(c.koud)*100 / sum(c.maoz - c.piz - c.koud),2), 0)) as koudl3" +
		    		        " from chepbtmp c where c.daohrq=to_date(to_char(sysdate,'yyyy-mm-dd'), 'yyyy-mm-dd') and c.meikdwmc='"+meikdwmc+"'";
		    rsl=con.getResultSetList(pandkoudl);
		    double mk_today_koud=0;
		    if(rsl.next()){
		    	mk_today_koud=rsl.getDouble("koudl3");
		    }
		    mk_kdl_sjz=mk_today_koud;//ȫ�ֱ���,�۶���ʵ��ֵ,Ϊ��������ȡֵ��׼��
		    String suodsql="";
		    if(mk_today_koud>quanbu_koudl){//�ж�ú��ǰ�Ŀ۶�����ϵͳ�趨�Ŀ۶��ʵĴ�С
		    	fanhuizhi=true;
		    }
		}
		
		return fanhuizhi;
	}

	

	//���������Ϣ��ó�Ƥ��Ϊ0�����¸ó���Ƥ����Ϣ
	public void SaveChelxx(JDBCcon con,long diancxxb_id,String cheph,double piz){
		String ChelSql="select piz from chelxxb where cheph='"+cheph+"'";
		ResultSetList rsl=con.getResultSetList(ChelSql);
		if(rsl.next()){
			double oldpiz=rsl.getDouble("piz");
			if(oldpiz==0){
				IsUpdateChelPiz=true;//��¼�Ƿ���³�����Ϣ���Ƥ���ֶ�
				String UpdateChelxxib="update chelxxb set piz="+piz+" where cheph='"+cheph+"'";
				con.getUpdate(UpdateChelxxib);
			}
		}
	}

	

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		
		if (_SavePizChick) {
			_SavePizChick = false;
			SavePiz();
			init();
		}
		if (_JusChick) {
			_JusChick = false;
			Jus();
			init();
		}
	}

	private void Jus() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û�������κθ���Ŷ��");
			return;
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
	
		int flag = 0;
		String sql="update chepbtmp set isjus=1 ,jussj=sysdate,jusry='"+visit.getRenymc()+"' where id="+getChange()+"";
		flag = con.getUpdate(sql);
		if(flag == -1){
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(this.getClass().getName() + 
						"\nSQL:" + sql +"���³�Ƥ��TMPʧ��!");
			setMsg(this.getClass().getName() + ":���³�Ƥ��TMPʧ��!");
			return;
		}
			
		
		con.commit();
		con.Close();
		setMsg("���ճɹ�");
	}


	public void setChepid(String fahids) {
		((Visit) this.getPage().getVisit()).setString1(fahids);
	}



	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sqldc = "and d.id = " + visit.getDiancxxb_id();
		String dcmcdef = visit.getDiancmc();
		String dcsql = "select id,mingc from diancxxb where fuid="
			+ visit.getDiancxxb_id();
		ResultSetList rsl;
//		�����һ������
		if(visit.isFencb()){
			sqldc = "and d.fuid = " + visit.getDiancxxb_id();
//			ȡ��Ĭ�ϵ糧����
			rsl = con.getResultSetList(dcsql);
			if(rsl.next()){
				dcmcdef = rsl.getString("mingc");
			}
			rsl.close();
		}
		
		
/*  Ƥ��grid��ʼ��  */
		
		String sql = "select c.id, c.cheph,c.meikdwmc,  c.maoz, c.piz,\n" +
			" cl.piz as lspz,c.koud, '' meicb_id, meigy,c.zhongcjjy , " +
			"to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss') as zhongcsj,to_char(c.lursj,'yyyy-mm-dd hh24:mi:ss') as lursj," +
			"nvl('"+visit.getRenymc()+"','') qingcjjy," +
			" c.qingchh  from chepbtmp c, chelxxb cl\n" + 
			"where c.zhongcsj is not null " +
			"and c.qingcsj is null  " +
			"  and c.cheph=cl.cheph(+)"+
			"and c.isjus=0 order by zhongcsj";
		rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu1 = new ExtGridUtil("gridDivPiz", rsl);
//		����ҳ��ɱ༭
		egu1.setGridType(ExtGridUtil.Gridstyle_Edit);
//		����ҳ����
		egu1.setWidth(Locale.Grid_DefaultWidth);
//		���ø�grid���з�ҳ
		egu1.addPaging(100);
//		��������
		egu1.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu1.getColumn("meikdwmc").setHeader(Locale.meikxxb_id_fahb);
		egu1.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu1.getColumn("piz").setHeader(Locale.piz_chepb);
		egu1.getColumn("lspz").setHeader("��ʷƤ��");
		egu1.getColumn("lspz").setHidden(true);
		egu1.getColumn("koud").setHeader(Locale.koud_chepb);
		egu1.getColumn("meicb_id").setHeader("���λ��");
		egu1.getColumn("lursj").setCenterHeader("����ʱ��");
		egu1.getColumn("zhongcsj").setCenterHeader("�س�ʱ��");
		egu1.getColumn("meigy").setHeader("��úԱ");
		egu1.getColumn("qingcjjy").setHeader("�պ�Ա");
		egu1.getColumn("qingchh").setHeader("���");
		egu1.getColumn("zhongcjjy").setHeader("�غ�Ա");
	
		
//		�����п�
		egu1.getColumn("meikdwmc").setWidth(150);
		egu1.getColumn("cheph").setWidth(110);
		egu1.getColumn("piz").setWidth(70);
		egu1.getColumn("koud").setWidth(70);
		egu1.getColumn("meigy").setWidth(80);
		egu1.getColumn("zhongcjjy").setWidth(80);
		egu1.getColumn("qingcjjy").setWidth(80);
		egu1.getColumn("qingchh").setWidth(70);
		egu1.getColumn("meicb_id").setWidth(135);
		egu1.getColumn("maoz").setWidth(70);
		egu1.getColumn("lursj").setWidth(200);
		egu1.getColumn("zhongcsj").setWidth(200);
//		����������
		sql = "select * from shuzhlfwb where leib ='����' and mingc = '������Ƥ��' and diancxxb_id = "
			+ visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			egu1.getColumn("piz").editor.setMinValue(rsl.getString("xiax"));
			egu1.getColumn("piz").editor.setMaxValue(rsl.getString("shangx"));
		}
//		�������Ƿ�ɱ༭
		egu1.getColumn("meikdwmc").setEditor(null);
		egu1.getColumn("lursj").setEditor(null);
		egu1.getColumn("zhongcsj").setEditor(null);
		egu1.getColumn("cheph").setEditor(null);
		egu1.getColumn("maoz").setEditor(null);
		egu1.getColumn("lspz").setEditor(null);
		egu1.getColumn("zhongcjjy").setEditor(null);
		egu1.getColumn("qingcjjy").setEditor(null);
		egu1.getColumn("qingchh").setEditor(null);
		egu1.getColumn("qingcjjy").setHidden(true);
//		����ú��������
		ComboBox cmc= new ComboBox();
		egu1.getColumn("meicb_id").setEditor(cmc); 
		cmc.setEditable(true);
		String mcSql="select id,piny || '-' ||mingc from meicb order by xuh";
		egu1.getColumn("meicb_id").setComboEditor(egu1.gridId, new
		IDropDownModel(mcSql));
//      ��úԱ��Ӧ��ú��������
		ComboBox ymz= new ComboBox();
		egu1.getColumn("meigy").setEditor(ymz);
		ymz.setEditable(true);
		String ymy = "select id ,zhiw from renyxxb r where r.bum='��úԱ' order by r.zhiw";
		egu1.getColumn("meigy").setComboEditor(egu1.gridId,new IDropDownModel(ymy));
		

		// ���복�ſ��Բ鵽ģ����Ӧ����Ϣ��-----------------------------------------------------------
		egu1.addTbarText("���복�ţ�");
		TextField theKey = new TextField();
		theKey.setId("theKey");
		theKey.setWidth(110);
		theKey.setListeners("change:function(thi,newva,oldva){  sta='';},specialkey:function(thi,e){if (e.getKey()==13) {chaxun();}}\n");
		egu1.addToolbarItem(theKey.getScript());
		// ����ext�еĵڶ���egu�����д���gridDiv�����ı������ȵ�һ����Piz������gridDiv----gridDivPiz.
		GridButton chazhao = new GridButton("(ģ��)����",
				"function(){chaxun();}");
		chazhao.setIcon(SysConstant.Btn_Icon_Search);
		egu1.addTbarBtn(chazhao);
		egu1.addTbarText("-");

		String otherscript = "var sta=''; function chaxun(){\n"
				+ "       if(theKey.getValue().toString()==''){Ext.MessageBox.alert('��ʾ','������ֵ');return;}\n"
				+ "       var len=gridDivPiz_data.length;\n"
				+ "       var count;\n"
				+ "       if(len%"
				+ egu1.getPagSize()
				+ "!=0){\n"
				+ "        count=parseInt(len/"
				+ egu1.getPagSize()
				+ ")+1;\n"
				+ "        }else{\n"
				+ "          count=len/"
				+ egu1.getPagSize()
				+ ";\n"
				+ "        }\n"
				+ "        for(var i=0;i<count;i++){\n"
				+ "           gridDivPiz_ds.load({params:{start:i*"
				+ egu1.getPagSize()
				+ ", limit:"
				+ egu1.getPagSize()
				+ "}});\n"
				+ "           var rec=gridDivPiz_ds.getRange();\n "
				+ "           for(var j=0;j<rec.length;j++){\n "
				+ "               if(rec[j].get('CHEPH').toString().indexOf(theKey.getValue().toString())!=-1){\n"
				+ "                 var nw=[rec[j]]\n"
				+ "                  if(sta.indexOf(rec[j].get('ID').toString())==-1){\n"
				+ "                      gridDivPiz_sm.selectRecords(nw);\n"
				+ "                      sta+=rec[j].get('ID').toString()+';';\n"
				+ "                       return;\n" + "                  }\n"
				+ "                \n" + "               }\n"
				+ "           }\n" + "        }\n" + "        if(sta==''){\n"
				+ "          Ext.MessageBox.alert('��ʾ','��Ҫ�ҵĳ��Ų�����');\n"
				+ "        }else{\n" + "           sta='';\n"
				+ "           Ext.MessageBox.alert('��ʾ','�����Ѿ�����β');\n"
				+ "         }\n" + "}\n";

		egu1.addOtherScript(otherscript);
		GridButton refurbish = new GridButton("ˢ��",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu1.addTbarBtn(refurbish);
		egu1.addTbarText("-");
		
		GridButton gbs_pz = new GridButton("����Ƥ��",GridButton.ButtonType_Save,
				"gridDivPiz", egu1.getGridColumns(), "SavePizButton");
		egu1.addTbarBtn(gbs_pz);
		egu1.addOtherScript("function  gridDivPiz_save(rec){if(confirm('�Ƿ񱣴� ����: '+rec.get('CHEPH') + '  ú��: ' + rec.get('MEIKDWMC'))){return \"\";}else{return \"return\";}};");
		egu1.addTbarText("-");
		
		egu1.addOtherScript("gridDivPiz_grid.on('rowclick',function(){Mode='sel';DataIndex='PIZ';});");
		egu1.addOtherScript("gridDivPiz_grid.on('beforeedit',function(e){ "
				+"if(e.row!=row_maoz_index){ e.cancel=true; } \n"
				+"if(e.field == 'PIZ'){e.cancel=true;}});\n");
		

		egu1.addTbarText("->");// ���÷ָ���
		
		
		egu1.addTbarText("��ʷƤ�أ�");
		TextField Lispiz = new TextField();
		Lispiz.setId("ls");
		Lispiz.setWidth(80);
		egu1.addToolbarItem(Lispiz.getScript());
		egu1.addTbarText("-");// ���÷ָ���
		
		
//		����
		egu1.addTbarText("-");// ���÷ָ���
		String sPwHandler = "function(){"
			+"if(gridDivPiz_sm.getSelected() == null){"
			+"	 Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ�г��Ž��о���');"
			+"	 return;"
			+"}"
			+"var grid_rcd = gridDivPiz_sm.getSelected();"
		
			+"Ext.MessageBox.confirm('��ʾ��Ϣ','���ճ���:&nbsp;&nbsp;&nbsp;'+ grid_rcd.get('CHEPH')+'&nbsp;&nbsp;&nbsp;ú��:&nbsp;&nbsp;&nbsp;'+grid_rcd.get('MEIKDWMC')+'&nbsp;&nbsp;&nbsp;ȷ����?&nbsp;&nbsp;&nbsp;',function(btn){"
			+"	 if(btn == 'yes'){"
			+"		    grid_history = grid_rcd.get('ID');"
			+"			var Cobj = document.getElementById('CHANGE');"
			+"			Cobj.value = grid_history;"
			+"			document.getElementById('JusButton').click();"
			+"	       	}"
			+"	  })"
			+"}";
		egu1.addTbarBtn(new GridButton("����",sPwHandler));
		egu1.addTbarText("-");// ���÷ָ���
		
		
		egu1.addOtherScript(" gridDivPiz_grid.on('cellclick',function(grid,rowIndex,columnIndex,e){ \n " +
				" if(columnIndex==2){ \n" +
				" row_maoz_index=rowIndex;\n"+
				" theKey.setValue(gridDivPiz_grid.getStore().getAt(rowIndex).get('CHEPH'));\n"+
				" ls.setValue(gridDivPiz_grid.getStore().getAt(rowIndex).get('LSPZ'));\n"+
				" sta='';\n"+
				" gridDivPiz_grid.getView().refresh();\n"+
				" gridDivPiz_grid.getView().getRow(rowIndex).style.backgroundColor=\"red\";} \n"+
				" else { \n" +
//				" gridDiv_grid.getView().focusRow(row_maoz_index); \n" +
				" } \n"+
				" }); \n");
		
		egu1.addOtherScript(" gridDivPiz_grid.addListener('afteredit',function(e){\n " +
				" gridDivPiz_grid.getView().getRow(e.row).style.backgroundColor=\"red\"; \n" +
				"} ); \n");
		
		setPizGrid(egu1);
		
		
		
		
		
/*  Ƥ�ر������grid��ʼ��  */
		
//		���ó�ͷ��Ĭ��ֵ
		 sql = "select rownum as xuh,a.* from (\n"+
			"select c.id, d.mingc dcmc,c.cheph, c.meikdwmc, c.maoz  ,c.piz ,(c.maoz-c.piz-c.koud) as jingz," +
			"c.koud, c.meigy,c.zhongcjjy,c.qingcjjy,mc.mingc as meic, "+
			"to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi') as zhongcsj,to_char(c.qingcsj,'yyyy-mm-dd hh24:mi') as qingcsj,\n" + 
			"to_char(c.lursj,'yyyy-mm-dd hh24:mi') as lursj \n"+
			"from chepbtmp c, diancxxb d, yunsfsb y, meicb mc\n" + 
			"where c.diancxxb_id = d.id \n" + 
			"and c.yunsfs = y.mingc\n" + 
			"and c.meicb_id=mc.id\n" + 
			"and to_char(c.qingcsj,'yyyy-mm-dd')='"+this.getRiq()+"'\n" + 
			"and y.id = " + SysConstant.YUNSFS_QIY + "\n" +
			sqldc + "\n" +
			"order by c.qingcsj  )a order by xuh desc";
		rsl = con.getResultSetList(sql);
//		���û��ȡ��Ĭ������
		if(rsl == null){
			
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl,Customkey);
//		����GRID���ɱ༭
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		// ����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
//		���ø�grid�����з�ҳ
		egu.addPaging(0);
//		��������
		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("dcmc").setHeader(Locale.diancxxb_id_fahb);
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("meikdwmc").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("piz").setHeader(Locale.piz_chepb);
		egu.getColumn("jingz").setHeader(Locale.jingz_chepb);
		egu.getColumn("koud").setHeader(Locale.koud_chepb);
		egu.getColumn("meic").setHeader("���λ��");
		egu.getColumn("meigy").setHeader("��úԱ");
		egu.getColumn("lursj").setHeader("����ʱ��");
		egu.getColumn("zhongcsj").setHeader("����ʱ��");
		egu.getColumn("qingcsj").setHeader("����ʱ��");
		egu.getColumn("zhongcjjy").setHeader("�غ�Ա");
		egu.getColumn("qingcjjy").setHeader("�պ�Ա");
//		�����п�
		egu.getColumn("xuh").setWidth(55);
		egu.getColumn("dcmc").setWidth(70);
		egu.getColumn("meikdwmc").setWidth(160);
		egu.getColumn("cheph").setWidth(110);
		egu.getColumn("maoz").setWidth(70);
		egu.getColumn("piz").setWidth(70);
		egu.getColumn("jingz").setWidth(70);
		egu.getColumn("koud").setWidth(70);
		egu.getColumn("meic").setWidth(130);
		egu.getColumn("meigy").setWidth(80);
		egu.getColumn("lursj").setWidth(200);
		egu.getColumn("zhongcsj").setWidth(200);
		egu.getColumn("qingcsj").setWidth(200);
		egu.getColumn("zhongcjjy").setWidth(80);
		egu.getColumn("qingcjjy").setWidth(80);
//		�������Ƿ�ɱ༭
		egu.getColumn("cheph").setEditor(null);


//		���÷ֳ���糧������
		if (visit.isFencb()) {
			ComboBox dc = new ComboBox();
			egu.getColumn("dcmc").setEditor(dc);
			dc.setEditable(true);
			egu.getColumn("dcmc").setComboEditor(egu.gridId,
					new IDropDownModel(dcsql));
		} else {
			egu.getColumn("dcmc").setHidden(true);
			egu.getColumn("dcmc").setEditor(null);
		}
		int caiy=0;
		int zhongc=0;
		int kongc=0;
		double jingz=0.0;
		int jus=0;
		//��������
		sql="select count(*) as caiy from chepbtmp c\n" +
			"where c.lursj>=to_date('"+this.getRiq()+"','yyyy-mm-dd')\n" + 
			"and c.lursj<to_date('"+this.getRiq()+"','yyyy-mm-dd')+1";
		rsl = con.getResultSetList(sql);
		if (rsl.next()){
			caiy=rsl.getInt("caiy");
		}
		//���ذ�����
		sql="select count(*) as zhongc from chepbtmp c\n" +
			"where c.zhongcsj>=to_date('"+this.getRiq()+"','yyyy-mm-dd')\n" + 
			"and c.zhongcsj<to_date('"+this.getRiq()+"','yyyy-mm-dd')+1 \n"+
			"and c.maoz>0\n" + 
			"and c.zhongcsj is not null";
		rsl = con.getResultSetList(sql);
		if (rsl.next()){
			zhongc=rsl.getInt("zhongc");
		}
		//��Ƥ����,����
		sql="select count(*) as huip,sum(c.maoz-c.piz-c.koud) as jingz from chepbtmp c\n" +
			"where c.qingcsj>=to_date('"+this.getRiq()+"','yyyy-mm-dd')\n" + 
			"and c.qingcsj<to_date('"+this.getRiq()+"','yyyy-mm-dd')+1 \n"+
			"and c.piz>0\n" + 
			"and c.qingcsj is not null";
		rsl = con.getResultSetList(sql);
		if (rsl.next()){
			kongc=rsl.getInt("huip");
			jingz=rsl.getDouble("jingz");
		}
//		���ճ���
		sql="select count(*) as jus from chepbtmp c\n" +
		"where c.jussj>=to_date('"+this.getRiq()+"','yyyy-mm-dd')\n" +
		" and c.jussj<to_date('"+this.getRiq()+"','yyyy-mm-dd')+1 \n"+
		" and c.isjus=1";
		rsl = con.getResultSetList(sql);
		if (rsl.next()){
			jus=rsl.getInt("jus");
		}
		DateField dfRIQ = new DateField();
		dfRIQ.Binding("RIQ", "");
		dfRIQ.setValue(getRiq());
		egu.addToolbarItem(dfRIQ.getScript());
		egu.addTbarText("-");
		egu.addTbarText("-");
		GridButton refurbish1 = new GridButton("ˢ��",
		"function (){document.getElementById('RefurbishButton').click();}");
		refurbish1.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish1);
		egu.addTbarText("-");
		egu.addTbarText("&nbsp;&nbsp;" +
				""+this.getRiq()+":&nbsp;����:"+caiy+"��," +
				"&nbsp;����:"+zhongc+" ��,&nbsp;" +
				"����:"+kongc+" ��,&nbsp;����:"+jus+"��,&nbsp;������ú:"+jingz+"��");
		
		egu.addOtherScript(" if(gridDiv_ds.getCount()>=1){ gridDiv_grid.getView().getRow(0).style.backgroundColor=\"red\"; }\n");
		setExtGrid(egu);
		
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public ExtGridUtil getPizGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setPizGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridScriptPiz() {
		if (getPizGrid() == null) {
			return "";
		}
		return getPizGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	public String getGridPizHtml() {
		if (getPizGrid() == null) {
			return "";
		}
		return getPizGrid().getHtml();
	}

	public DefaultTree getDefaultTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}

	public void setDefaultTree(DefaultTree dftree1) {
		((Visit) this.getPage().getVisit()).setDefaultTree(dftree1);
	}

	public String getTreeScript() {
		if(getDefaultTree()==null){
			return "";
		}
		// System.out.print(((Visit)
		// this.getPage().getVisit()).getDefaultTree().getScript());
		return getDefaultTree().getScript();
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

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setGongysModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setGongysModels() {
		String sql = "select id,mingc from gongysb order by xuh,mingc";
		setGongysModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getMeikModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setMeikModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setMeikModels() {
		String sql = "select id,mingc from meikxxb order by xuh,mingc";
		setMeikModel(new IDropDownModel(sql));
	}

	private void setJianjdmodel() {
		JDBCcon con = new JDBCcon();
		String sql = "select zhi from xitxxb where mingc='�������ﵥģʽ' and zhuangt = 1 and diancxxb_id ="
				+ ((Visit) this.getPage().getVisit()).getDiancxxb_id();
		ResultSetList rsl = con.getResultSetList(sql);
		String model = "";
		if (rsl.next()) {
			model = rsl.getString("zhi");
		}
		rsl.close();
		((Visit) this.getPage().getVisit()).setString15(model);
	}
	//ÿ500���Ƴ�һ�������ɲ�����ŵ�ģʽ,ͬʱҲ�жϳ���,�Ծ���40��Ϊ��׼,����40���Ǵ�,С��40����С��.
	private int countCaiy(JDBCcon con, long diancxxb_id,String meikdwmc,
			double maoz, double piz, double koud,String chepbtmp_id){
		Visit visit = (Visit) this.getPage().getVisit();
		String Qufdxc="select zhi,beiz from xitxxb x where x.mingc='����500�ֲ����Ƿ����ִ�С��' and zhuangt=1 and diancxxb_id="+visit.getDiancxxb_id()+"";
		ResultSetList Daxcrs = con.getResultSetList(Qufdxc); 
		String sql="";
		boolean Bool_Qufdxc=false;//�Ƿ����ִ�С������
		boolean Bool_dac=false;  //�Ƿ��Ǵ�
		if(Daxcrs.next()){
			String Dx_zhi=Daxcrs.getString("zhi");
			double Dx_jingz=Daxcrs.getDouble("beiz");
			//500�ַ����Ƿ����ִ�С��
			if(Dx_zhi.equals("��")){
				Bool_Qufdxc=true;
				String chex="";
				//���maoz-piz-koud>=Dx_jingz ,�Ǵ�,������С��,(Dx_jingz��45��),��chex=0,С��chex=1
				if(maoz-piz-koud>=Dx_jingz){
					Bool_dac=true;
					chex=" and chex=0";
				}else{
					chex=" and chex=1";
				}
				 sql = "select * from gdxw_cy where diancxxb_id = " + diancxxb_id +
				" and meikdwmc = '" + meikdwmc + "' and zhuangt = 0 and to_char(shengcrq,'yyyy-mm-dd')=to_char(sysdate,'yyyy-mm-dd')  "+chex+""; 
			}else{
//				�жϲ���������û��δ�����Ĳ�����Ϣ(zhuangt = 0)
				 sql = "select * from gdxw_cy where diancxxb_id = " + diancxxb_id +
				" and meikdwmc = '" + meikdwmc + "' and zhuangt = 0 and to_char(shengcrq,'yyyy-mm-dd')=to_char(sysdate,'yyyy-mm-dd')";
			}
		}
		
		long meikxxb_id=0;
		String GetMeikxxb_id_Sql="select nvl(max(id),0) as id from meikxxb where mingc='"+meikdwmc+"'";
		ResultSetList rs_meikxxb_id = con.getResultSetList(GetMeikxxb_id_Sql);
		if(rs_meikxxb_id.next()){
			meikxxb_id=rs_meikxxb_id.getLong("id");
		}
		rs_meikxxb_id.close();
		
 
		ResultSetList rs = con.getResultSetList(sql); 
		String id = "";
		String sql1="";
		int flag = 0;
		if(rs.next()){
//			������������м�¼˵���ó������ô˱��ֱ�Ӹ��������Ϣ����
			id = rs.getString("id");
			double old_maoz = rs.getDouble("maoz");
			double old_piz = rs.getDouble("piz");
			double old_koud = rs.getDouble("koud");
			double new_maoz = CustomMaths.add(old_maoz, maoz);
			double new_piz = CustomMaths.add(old_piz, piz);
			double new_koud = CustomMaths.add(old_koud, koud);
			double new_jingz = CustomMaths.sub(new_maoz, new_piz);
			double new_jingz_koud=CustomMaths.sub(new_jingz, new_koud);
			long zhilb_id=rs.getLong("zhilb_id");
			sql = "update gdxw_cy set maoz = " + new_maoz +
			", piz = " + new_piz + 
			", koud = " + new_koud + 
			", jingz = " + new_jingz_koud +
			" where id = " + rs.getString("id");

			flag = con.getUpdate(sql);
		
			if(flag == -1){
				WriteLog.writeErrorLog(this.getClass().getName() + 
						"\nSQL:" + sql +"����gdxw_cy��ʧ��!");
				setMsg(this.getClass().getName() + ":����gdxw_cy��ʧ��!");
				return flag;
			}
			
//			����chepbtmp�е�zhilb_id
			sql1="update chepbtmp set zhilb_id="+zhilb_id+",fahbtmp_id="+zhilb_id+" where id="+chepbtmp_id+"";
			flag=con.getUpdate(sql1);
			if(flag == -1){
				WriteLog.writeErrorLog(this.getClass().getName() + 
						"\nSQL:" + sql +"���¸���chepbtmp�е�zhilb_id��ʧ��!");
				setMsg(this.getClass().getName() + ":���¸���chepbtmp�е�zhilb_idʧ��!");
				return flag;
			}
			
		}else{
//			���Ϊ��������
			id = MainGlobal.getNewID(diancxxb_id);
//			����������������
			long zhilb_id = Jilcz.getZhilbid(con, null, new Date(), visit.getDiancxxb_id());
//			���ɲ�����������������
			flag = Caiycl.CreatBianh(con, zhilb_id, visit.getDiancxxb_id(),meikxxb_id,2,2,1);
			if(flag == -1){
				WriteLog.writeErrorLog(this.getClass().getName() + 
						"\n�������ʧ��!");
				setMsg(this.getClass().getName() + ":�������ʧ��!");
				return flag;
			}
			
			
//			����δʹ�õĴ���λ��
			
			sql = /*"select * from cunywzb where id in\n" +
				"(select id from cunywzb where zhuangt = 1\n" + 
				"minus\n" + 
				"select cunywzb_id from caiyb where zhilb_id in\n" + 
				"(select zhilb_id from gdxw_cy where zhuangt = 0 \n" +
				"or (zhiyrq <= sysdate and zhiyrq>= sysdate -3))\n" + 
				") and rownum = 1";
			*/
			
			"select * from cunywzb where id in\n" +
			"(select id from cunywzb where zhuangt = 1\n" + 
			"minus\n" + 
			"select cunywzb_id from caiyb where zhilb_id in\n" + 
			"(select zhilb_id from gdxw_cy where  \n" +
			" shengcrq>=to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')))\n" + 
			"and rownum = 1";
			rs = con.getResultSetList(sql);
			if(rs.next()){
//				���²������д���λ��
				sql = "update caiyb set cunywzb_id = " + rs.getString("id") + 
				" where zhilb_id = " + zhilb_id;
//              ����chepbtmp�е�zhilb_id
				 sql1="update chepbtmp set zhilb_id="+zhilb_id+",fahbtmp_id="+zhilb_id+" where id="+chepbtmp_id+"";
				con.getUpdate(sql);
				con.getUpdate(sql1);
			}else{
				WriteLog.writeErrorLog(this.getClass().getName() + 
				"\n����λ������,����Ӵ���λ��!");
				setMsg(this.getClass().getName() + ":����λ������,����Ӵ���λ��!");
				return -1;
			}
//			long 
			double jingz=maoz-piz-koud;
			//�Ƿ����ִ�С������
			if(Bool_Qufdxc){
				//����Ǵ�,chex=0,���������С��chex=1
				if(Bool_dac){
					sql = "insert into gdxw_cy(id, diancxxb_id, meikdwmc, maoz, zhilb_id,zhuangt,piz,koud,jingz,shengcrq,chex,zhiyrq) " +
					"values("+id+"," + diancxxb_id +
					",'" + meikdwmc + "'," + maoz + "," + zhilb_id+ ",0,"+piz+","+koud+","+jingz+",sysdate,0,sysdate)";
				}else{
					sql = "insert into gdxw_cy(id, diancxxb_id, meikdwmc, maoz, zhilb_id,zhuangt,piz,koud,jingz,shengcrq,chex,zhiyrq) " +
					"values("+id+"," + diancxxb_id +
					",'" + meikdwmc + "'," + maoz + "," + zhilb_id+ ",0,"+piz+","+koud+","+jingz+",sysdate,1,sysdate)";
				}
				
			}else{
				sql = "insert into gdxw_cy(id, diancxxb_id, meikdwmc, maoz, zhilb_id,zhuangt,piz,koud,jingz,shengcrq,chex,zhiyrq) " +
				"values("+id+"," + diancxxb_id +
				",'" + meikdwmc + "'," + maoz + "," + zhilb_id+ ",0,"+piz+","+koud+","+jingz+",sysdate,0,sysdate)";
			}
			
			
			
			
			
			
			
			flag = con.getInsert(sql);
			if(flag == -1){
				WriteLog.writeErrorLog(this.getClass().getName() + 
				"\n����gdxw_cy��Ϣʧ��!");
				setMsg(this.getClass().getName() + ":����gdxw_cy��Ϣʧ��!");
				return flag;
			}
		}
		rs.close();
		sql = "select zhi,danw from xitxxb where  mingc ='��������������' and zhuangt=1 and leib= '����'";
		 rs = con.getResultSetList(sql);
		double Fenzl = 0;
		String Fenzd = "";
		if(rs.next()){
			Fenzl =rs.getDouble("ZHI");
			Fenzd = rs.getString("DANW");
		}
		//rscy.close();
		sql = "select " + Fenzd + " from gdxw_cy where id = " + id;
		rs = con.getResultSetList(sql);
		if(rs.next()){
			if(rs.getDouble(0) > Fenzl){
				sql = "update gdxw_cy set zhuangt = 1, zhiyrq = sysdate where id = " + id;
				con.getUpdate(sql);
			}
		}
		rs.close();
		return flag;
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			visit.setString1(null);
			init();
		}
	}

	private void init() {
		setGongysModels();
		setMeikModels();
		setJianjdmodel();
		setExtGrid(null);
		setPizGrid(null);
		setDefaultTree(null);
		getSelectData();
	}
}