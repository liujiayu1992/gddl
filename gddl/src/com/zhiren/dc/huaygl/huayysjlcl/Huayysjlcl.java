package com.zhiren.dc.huaygl.huayysjlcl;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * ����:���ܱ�
 * ʱ��:2010-8-5
 * ����:1�� ����ȷ��ʱ������ú���Ĵ�����
 *      2. �������״̬
 *      3���޸���ֵ��Ԫ�ط�������ȡֵ���������
 *      4.�޸Ļ�������С��������
 */


/*
 * ���ߣ�������
 * ʱ�䣺2009-08-15
 * �������޸����ݼ��㲻��ȷ����
 * 
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-08-14
 * �������޸�С����Լ
 * 
 */
/*
 * 2009-05-13
 * ����
 * �޸ĸ���������ʱ�����״̬Ϊ3��ʶ����ֱ�ӵ�һ�����
 */
/*
 * ����:tzf
 * ʱ��:2009-4-27
 * ����:����xitxxb�����ã���huaygyfxb��huaylfb��huayrlb�ֱ�ȡ����ʾ��ȷ�� �� ����.
 */
public class Huayysjlcl extends BasePage implements PageValidateListener {

	private final static String yuansbglly="ԭʼ��������Դ";
	private final static String yuansbgqly="ԭʼ��������Դ";
	private final static String yuansfx="Ԫ�ط���";
	private final static String shougwh="�ֹ�ά��";
	private final static String zidjs="�Զ�����";
	private final static String caijsj="�ɼ�����";
	private final static String _liu="��";
	private final static String _qing="��";
	private final static String huaybm="�������";
	private final static String huayysjl_bianm="HYYSJL";//itemsort���� ���ڵı���
	// ������Ŀ�� ��Ӧ ����Ŀ ����
//	private final static String quansf="ȫˮ��";
//	private final static String shuif="ˮ��";
//	private final static String huiff="�ӷ���";
//	private final static String huif="�ҷ�";
//	private final static String quanl="ȫ��";
//	private final static String farl="������";
	
	
	private final static String round_count="###.00";//�Զ�����ʱ  ������С���� β��
	public boolean getRaw() {
		return true;
	}

	
	
	
	
	// �����û���ʾ
	private String msg = "";

	public String getMsg() {
		return msg;
	}
	private String Markbh = "true"; // ��Ǳ���������Ƿ�ѡ��
	
	public String getMarkbh() {
		return Markbh;
	}
	public void setMarkbh(String markbh) {
		Markbh = markbh;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
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

	public String getPrintTable() {
		return getHuaybgd();
	}
	
	
	
	//�� xitxxb �� ȡ�� ԭʼ��������Դ
	private String getLiuly(JDBCcon con){
		Visit visit=(Visit)this.getPage().getVisit();
		String zhi="";
		String sql=" select zhi from xitxxb where leib='����' and zhuangt = 1 and mingc='"+yuansbglly+"' and diancxxb_id="+visit.getDiancxxb_id();
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			zhi=rsl.getString("zhi");
		}
		rsl.close();
		return zhi;
		
	}
	
//	�� xitxxb �� ȡ�� ԭʼ��������Դ
	private String getQingly(JDBCcon con){
		Visit visit=(Visit)this.getPage().getVisit();
		String zhi="";
		String sql=" select zhi from xitxxb where leib='����' and zhuangt = 1 and mingc='"+yuansbgqly+"' and diancxxb_id="+visit.getDiancxxb_id();
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			zhi=rsl.getString("zhi");
		}
		rsl.close();
		return zhi;
		
	}
	
	private double Round_New(double value){
		double rv=0;
		DecimalFormat dc=new DecimalFormat(round_count);
		String s=dc.format(value);
		try{
			rv=Double.valueOf(s).doubleValue();
		}catch(Exception e){
			return 0;
		}
		
		return rv;
	}
	
	private double Round_New(double value,String pattern){
		double rv=0;
		DecimalFormat dc=new DecimalFormat(pattern);
		String s=dc.format(value);
		
		try{
			rv=Double.valueOf(s).doubleValue();
		}catch(Exception e){
			return 0;
		}
		
		return rv;
	}
	
	//�Զ�����ʱ  �õ��ոɻ���� ֵ
	private String getKonggjq_v(JDBCcon con,String bianm){
		String value="";
		StringBuffer bf=new StringBuffer();
		bf.append(" select z.mt,z.mad,z.aad,z.vad,z.stad,z.qbad,z.had  \n");
		bf.append("  from zhillsb z, zhuanmb m, zhuanmlb l \n");
		bf.append(" where z.id = m.zhillsb_id and m.zhuanmlb_id = l.id \n");
		bf.append(" and l.mingc = '�������' and m.bianm='").append(bianm).append("' \n");
		
		ResultSetList rsl=con.getResultSetList(bf.toString());
		double Mt_v=0;
		double Mad_v=0;
		double Aad_v=0;
		double Vad_v=0;
		double Stad_v=0;
		double Qbad_v=0;
		double Had_v=0;
		if(rsl.next()){
			Mt_v=rsl.getDouble("mt");
			Mad_v=rsl.getDouble("mad");
			Aad_v=rsl.getDouble("aad");
			Vad_v=rsl.getDouble("vad");
			Stad_v=rsl.getDouble("stad");
			Qbad_v=rsl.getDouble("qbad");
			Had_v=rsl.getDouble("had");
			
		}
		double Qnetar_v=0;
		double Qgrd_v=0;
		//�����Զ����� ����
		if(Mt_v>0 && Mad_v>0 && Aad_v>0 && Vad_v>0 && Stad_v>0 && Qbad_v>0 ){
			
			double Aar_v = Round_New(Aad_v*(100-Mt_v)/(100-Mad_v));
			double Ad_v  = Round_New(Aar_v*100/(100-Aad_v));
			double Var_v = Round_New(Vad_v*(100-Mt_v)/(100-Mad_v));
			double Vd_v  = Round_New(Vad_v*100/(100-Mad_v));
			double Vdaf_v = Round_New(Vad_v*100/(100-Vd_v));
			double FCar_v = Round_New(100-Mt_v-Aar_v-Var_v);
			double FCad_v = Round_New(100-Mad_v-Aad_v-Vad_v);
			double FCd_v = Round_New(100-Ad_v-Vd_v);
			double Star_v = Round_New(Stad_v*(100-Mt_v)/(100-Mad_v));
			double Std_v = Round_New(Stad_v*100/(100-Mad_v));
			double Qgrad_v = 0.0;
			Had_v = Round_New((Vd_v*100/(100-Ad_v)*0.074+2.16)*(100-Mad_v-Aad_v)/100);
			
			
			double Hdaf_v = Round_New(Had_v * 100 / (100 - Mad_v - Aad_v));
			if(Qbad_v <= 16.7){
				Qgrad_v = Round_New((Qbad_v*1000-94.1*Stad_v-Qbad_v)/1000);
			}else{
				 if(Qbad_v > 16.7 && Qbad_v <= 25.1){
					Qgrad_v = Round_New((Qbad_v*1000-94.1*Stad_v-1.2*Qbad_v)/1000);
				 }else{
					Qgrad_v = Round_New((Qbad_v*1000-94.1*Stad_v-1.6*Qbad_v)/1000);
				 }
			}
			Qgrd_v  = Round_New(Qgrad_v*100/(100-Mad_v));
			
			Qnetar_v  = Round_New(((Qgrad_v*1000-206*Had_v)*(100-Aar_v)/(100-Mad_v)-23*Mt_v)/1000,"###.000");
			
		}else{
			Had_v=0;
		}
		
		value=Had_v+"";
		return value;
	}
	
	
	
	private void setArrayValue(String ArrHeader[][], boolean isOnlyQuery){
		Visit visit = (Visit) getPage().getVisit();
		
		JDBCcon con = new JDBCcon();
		
		boolean VadIsCount=false;//�ӷ����������ƽ���ģ�Vad��ҳ�����¼��㣬�����ô�������ֵ,���Vad���������ģ�ҳ�治���¼���
		String SqlVadIsCount="select h.shebxh\n" +
			"  from huaygyfxb h\n" + 
			" where h.bianm = '"+this.getBianmValue().getValue()+"'\n" + 
			"   and h.riq >= to_date('"+this.getRiq()+"', 'yyyy-mm-dd')\n" + 
			"   and h.riq <= to_date('"+this.getEriq()+"', 'yyyy-mm-dd')\n" + 
			"   and h.fenxxmb_id = 107";
		
		ResultSetList rsl=con.getResultSetList(SqlVadIsCount);
		if(rsl.next()){
			String shebxh=rsl.getString("shebxh");
			if(shebxh.equals("��ƽ")){
				VadIsCount=true;
			}
		}
		rsl.close();
		
		
		
		String wend_shid=""; //�¶�ʪ��  Ϊ��
		String caiyl="";//������  Ϊ��
		String zhiyr="";//������  Ϊ��
		
		StringBuffer bf=new StringBuffer();
		bf.append(" select max(zhillsbid) as zhillsbid,sum(jingz) as jingz,bianh,kuangb,faz,pinz,huaysj,max(chec) as chec,max(caiysj) as caiysj,meikdm from ");
		bf.append(" (select z.id as zhillsbid,f.jingz,nvl(m.bianm,'') bianh,nvl(mk.mingc,'') kuangb,nvl(c.mingc,'') faz,nvl(p.mingc,'') pinz,nvl(to_char(z.huaysj,'yyyy-mm-dd'),'') huaysj,nvl(f.chec,'') chec,nvl(to_char(cai.caiyrq,'yyyy-mm-dd'), '') caiysj \n");
		bf.append(",decode(cai.meikdm,null,' ',cai.meikdm) as meikdm from meikxxb mk,chezxxb c,pinzb p,zhillsb z,  \n");
		bf.append(" (select distinct fa.diancxxb_id,fa.zhilb_id, fa.meikxxb_id,max(fa.chec) as chec,fa.faz_id,fa.pinzb_id,sum(jingz) as jingz from fahb fa group by fa.diancxxb_id,fa.zhilb_id, fa.meikxxb_id,fa.faz_id,fa.pinzb_id) f, zhuanmb m, caiyb cai,zhuanmlb l \n");
		bf.append(" where f.zhilb_id=z.zhilb_id  and f.meikxxb_id=mk.id and cai.zhilb_id = z.zhilb_id \n");
		bf.append(" and f.faz_id=c.id and f.pinzb_id=p.id  \n");
		bf.append(" and z.id = m.zhillsb_id and m.zhuanmlb_id = l.id  and l.mingc = '"+huaybm+"' \n");
		bf.append(" and m.bianm='").append(this.getBianmValue().getValue()).append("' \n");
		bf.append(" and f.diancxxb_id=").append(visit.getDiancxxb_id()).append(" \n");
		bf.append(" union select z.id as zhillsbid,0 as jingz,z.beiz as bianh,decode(q.meikmc,null,'',q.meikmc) as kuangb,'' as faz,'' as pinz,nvl(to_char(z.huaysj,'yyyy-mm-dd'),'') huaysj,'' as chec,decode(q.caiyrq,null,'',to_char(q.caiyrq,'yyyy-mm-dd')) caiysj,decode(q.meikmc,null,'',q.meikmc) as meikdm\n");
		bf.append(" from zhillsb z,qitycyb q where z.id=q.zhillsb_id(+) and z.beiz='").append(this.getBianmValue().getValue()).append("'");
		bf.append(") group  by bianh,kuangb,faz,pinz,huaysj,meikdm   order by caiysj desc ");
		 rsl=con.getResultSetList(bf.toString());
		
		if(rsl.next()){
//			�ж��Ƿ���ʾ�������ʾ����
			boolean isKuangb = MainGlobal.getXitxx_item("����", "���鱨�浥��ʾ���", 
					String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()),
					"��").equals("��");
						
			boolean isMeikdm = MainGlobal.getXitxx_item("����", "���鱨�浥��ʾ������", 
					String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()),
					"��").equals("��");
			if (isKuangb){
				ArrHeader[0][1] = rsl.getString("kuangb"); // ú��λ����
				ArrHeader[0][3] = rsl.getString("faz"); // ��վ(��)
			}else{
				if(isMeikdm){
					ArrHeader[0][1]=rsl.getString("meikdm");
						
				}else{
					ArrHeader[0][1] ="";
				}
				ArrHeader[0][3]="";
			}
			
			
			/*   //�� ������ ���� ����� had��ֵ  ��ֵ ����
			if(this.getQingly().equals(yuansfx)){//�� ��Դ ��Ԫ�ط���
				if(this.getQingpz()){//�� ������ȷ
					ArrHeader[0][7] = this.getQingjz(); // ��
				}else{
					ArrHeader[0][7] = "";//���ò���ȷ   Ϊ��
				}
			}else{ //�Զ�����
				ArrHeader[0][7] =this.getKonggjq_v(con, this.getBianmValue().getValue()) ;
			}
			*/
			
			boolean isShowjingz = MainGlobal.getXitxx_item("����", "����ԭʼ���浥��ʾ����", 
					String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()),
					"��").equals("��");
			if(isShowjingz){
				ArrHeader[0][9] = String.valueOf(rsl.getDouble("jingz")); // ú��
			}
			
			ArrHeader[1][1] = rsl.getString("pinz"); // Ʒ��
			ArrHeader[1][3] = rsl.getString("bianh"); // ���
			if (!rsl.getString("huaysj").equals("")){
				ArrHeader[1][7] = rsl.getString("huaysj"); // ��������
			}else{
				ArrHeader[1][7] = getEriq();
			}
			
			String sql="select r.quanc from yangpdhb y,caiyryglb c,renyxxb r where y.id=c.yangpdhb_id and c.renyxxb_id=r.id "
				+ " and y.zhilblsb_id="+rsl.getLong("zhillsbid");
			String caiyry="";
			ResultSetList rscyry=con.getResultSetList(sql);
			while (rscyry.next()){
				if (caiyry.equals("")){
					caiyry=rscyry.getString("quanc");
				}else{
					caiyry=caiyry+","+rscyry.getString("quanc");
				}
			}
			rscyry.close();
			//������Ա
			String zhiyry="";
			
			sql="select r.quanc from yangpdhb y,zhiyryglb c,renyxxb r where y.id=c.yangpdhb_id and c.renyxxb_id=r.id "
				+ " and y.zhilblsb_id="+rsl.getLong("zhillsbid");
			ResultSetList rszyry=con.getResultSetList(sql);
			while (rszyry.next()){
				if (zhiyry.equals("")){
					zhiyry=rszyry.getString("quanc");
				}else{
					zhiyry=zhiyry+","+rszyry.getString("quanc");
				}
			}
			rszyry.close();
			
			
			if (caiyry.equals("")){
				rscyry=con.getResultSetList("select decode(q.caiyry,null,'',q.caiyry) caiyry,decode(q.zhiyry,null,'',q.zhiyry) as zhiyry from zhillsb z,qitycyb q where z.id=q.zhillsb_id(+) and z.beiz='"+this.getBianmValue().getValue()+"'");
				if (rscyry.next()){
					caiyry=rscyry.getString("caiyry");
					zhiyry=rscyry.getString("zhiyry");
				}
			}else{
				//zhiyry=caiyry;
			}
			
			ArrHeader[1][9] = caiyry; // ������Ա
			ArrHeader[2][1] = rsl.getString("chec"); // ����
			ArrHeader[2][3] = wend_shid; // ʪ��
			
			if(!rsl.getString("caiysj").equals("")){
				ArrHeader[2][7] = rsl.getString("caiysj"); // ����ʱ��
			}else{
				ArrHeader[2][7] = "";
			}
			
			ArrHeader[2][9] = zhiyry; // ������
		}
		rsl.close();
		Table tb = new Table(1, 1);
		
		
		//  ��ҵ����  ȫˮ��
		//wzb ���������ж�,ȡʱ��ε�ǰ7��,������,��ֹ����ظ�,�����糧�����ŵ��²��ظ�,�¸��¾��п����ظ�.
		bf.setLength(0);
		bf.append(" select h.huayz,h.cis,h.qimbh,h.qimzl,h.shenhy,h.huayy,\n");
		bf.append(" h.qimzl+h.meiyzl qishizl,h.meiyzl shiyzl,h.honghzzl,h.zongzl,\n");
		bf.append(" h.honghzzl1 jiancxsyhzzl,shenhzt,h.shebxh \n");
		bf.append(" from huaygyfxb h,item f,itemsort i \n");
		bf.append(" where h.fenxxmb_id=f.id and f.bianm='").append(Huayysjlsh.Mt).append("' \n");
		bf.append(" and h.bianm='").append(this.getBianmValue().getValue()).append("' \n");
		bf.append(" and h.diancxxb_id=").append(visit.getDiancxxb_id());
		bf.append("  and f.itemsortid=i.itemsortid and i.bianm='").append(huayysjl_bianm).append("' \n");
		bf.append("  and h.riq>=to_date('"+getRiq()+"','yyyy-mm-dd')-7 ").append(" \n");
		bf.append("  and h.riq<=to_date('"+getEriq()+"','yyyy-mm-dd')+7").append(" \n");
		bf.append(" and h.shenhzt>=1 order by h.cis");
		rsl=con.getResultSetList(bf.toString());
	
		int count=rsl.getRows();//���ؼ�¼����  ƽ��ֵ��
		//rsl.beforefirst();
		if(count==0){//��ֵ   ��ʾ����
			if(this.getBianmValue().getId()!=-1 && this.getMsg().equals("")){//  �ų� ��ѡ��  ʱ  ��ʾ����Ϣ 
				this.setMsg("��ȡȫˮ������ʧ�ܣ�����!");//���ð�ť
			}
			ToolbarButton tbn=(ToolbarButton)this.getToolbar().getItem("commit_id");
			tbn.setDisabled(true);
			
			ToolbarButton tbn1=(ToolbarButton)this.getToolbar().getItem("cancel_id");
			tbn1.setDisabled(true);
		}else if(count>4){//ҳ�����ݳ���4��,��������,ҳ��ᱨ��,�����ڴ��ж�
			if(this.getBianmValue().getId()!=-1 && this.getMsg().equals("")){
				this.setMsg(this.getBianmValue().getValue()+" ȫˮ�����ݳ���4��������!");
			}
			ToolbarButton tbn=(ToolbarButton)this.getToolbar().getItem("commit_id");//���ð�ť
			tbn.setDisabled(true);
			
			ToolbarButton tbn1=(ToolbarButton)this.getToolbar().getItem("cancel_id");
			tbn1.setDisabled(true);
			con.Close();
			return;
		}
		
		double quansf_sum=0;//ȫˮ�� ��ֵ
		String shenhry="";//�����Ա
		String huayry="";//������Ա
		int colsnum =1;
		while(rsl.next()){
			////rsl.getInt("cis")
			int initRow = 4;
			int initCol = 1 + colsnum;
			double ganzhjszl=0;//�����ú����������
			if(rsl.getString("shebxh").equals("SDTGA300")){
				ArrHeader[initRow++][initCol] = rsl.getString("qimbh");//����ƿ��
				ArrHeader[initRow++][initCol] = tb.format(rsl//����ƿ����
						.getString("qimzl"), "0.0000");
				ArrHeader[initRow++][initCol] = tb.format(rsl//ʽ������+����ƿ����
						.getString("qishizl"), "0.0000");
				ArrHeader[initRow++][initCol] = tb.format(rsl//ʽ������
						.getString("shiyzl"), "0.0000");
				ArrHeader[initRow++][initCol] = tb.format(rsl//��ɺ�������
						.getString("honghzzl"), "0.0000");
				ArrHeader[initRow++][initCol] = tb.format(rsl//��������������
						.getString("jiancxsyhzzl"), "0.0000");
				
				ganzhjszl=rsl.getDouble("qishizl")-rsl.getDouble("honghzzl");
				ArrHeader[initRow++][initCol] = tb.format(ganzhjszl+"", "0.0000");
			}else{
				ArrHeader[initRow++][initCol] = rsl.getString("qimbh");//����ƿ��
				ArrHeader[initRow++][initCol] = tb.format(rsl//����ƿ����
						.getString("qimzl"), "0.00");
				ArrHeader[initRow++][initCol] = tb.format(rsl//ʽ������+����ƿ����
						.getString("qishizl"), "0.00");
				ArrHeader[initRow++][initCol] = tb.format(rsl//ʽ������
						.getString("shiyzl"), "0.00");
				ArrHeader[initRow++][initCol] = tb.format(rsl//��ɺ�������
						.getString("honghzzl"), "0.00");
				ArrHeader[initRow++][initCol] = tb.format(rsl//��������������
						.getString("jiancxsyhzzl"), "0.00");
				
				ganzhjszl=rsl.getDouble("qishizl")-rsl.getDouble("honghzzl");
				ArrHeader[initRow++][initCol] = tb.format(ganzhjszl+"", "0.00");
			}
			
			
			//double quansMT=tb.Round_New(ganzhjszl/rsl.getDouble("shiyzl")*100,1);//ȫˮ��Mt
			double quansMT=tb.Round_New(rsl.getDouble("huayz"),2);//ȫˮ��Mt
			ArrHeader[initRow++][initCol] = tb.format(quansMT+"","0.00");
			quansf_sum+=Double.valueOf(tb.format(quansMT+"","0.00")).doubleValue();
			
			if(rsl.getString("shenhy")!=null){
				shenhry=rsl.getString("shenhy");
			}
			if(rsl.getString("huayy")!=null){
				huayry=rsl.getString("huayy");
			}
			colsnum=colsnum+1;
		}
		double pingjz=0;
		quansf_sum=Double.valueOf(tb.format(quansf_sum+"","0.00")).doubleValue();
		if(count==0){
			pingjz=0;
		}else{
			pingjz=tb.Round_New(quansf_sum/count,1);
		}
		
		ArrHeader[12][2] = tb.format(pingjz+"","0.0");
		
		ArrHeader[13][1]+=shenhry;
		ArrHeader[13][2]+=huayry;
		
		rsl.close();
		
		
		
		//��ҵ���� ˮ��
		//wzb ���������ж�,ȡʱ��ε�ǰ7��,������,��ֹ����ظ�,�����糧�����ŵ��²��ظ�,�¸��¾��п����ظ�.
		bf.setLength(0);
		bf.append(" select h.huayz,h.cis,h.qimbh,h.qimzl,h.shenhy,h.huayy,\n");
		bf.append(" h.qimzl+h.meiyzl qishizl,h.meiyzl shiyzl,h.honghzzl,h.zongzl,\n");
		bf.append(" h.honghzzl1 jiancxsyhzzl \n");
		bf.append(" from huaygyfxb h,item f,itemsort i \n");
		bf.append(" where h.fenxxmb_id=f.id and f.bianm='").append(Huayysjlsh.Mad).append("' \n");
		bf.append(" and h.bianm='").append(this.getBianmValue().getValue()).append("' \n");
		bf.append(" and h.diancxxb_id=").append(visit.getDiancxxb_id());
		bf.append("  and f.itemsortid=i.itemsortid and i.bianm='").append(huayysjl_bianm).append("' \n");
		bf.append("  and h.riq>=to_date('"+getRiq()+"','yyyy-mm-dd')-7 ").append(" \n");
		bf.append("  and h.riq<=to_date('"+getEriq()+"','yyyy-mm-dd')+7").append(" \n");
		bf.append(" and  h.shenhzt>=1 order by h.cis");
		
		rsl=con.getResultSetList(bf.toString());
	
		count=rsl.getRows();//���ؼ�¼����  ƽ��ֵ��
		if(count==0){//��ֵ   ��ʾ����
			
			if(this.getBianmValue().getId()!=-1 && this.getMsg().equals("")){//  �ų� ��ѡ��  ʱ  ��ʾ����Ϣ 
				this.setMsg("��ȡ�ոɻ�ˮ������ʧ�ܣ�����!");//���ð�ť
			}
			
			ToolbarButton tbn=(ToolbarButton)this.getToolbar().getItem("commit_id");
			tbn.setDisabled(true);
			
			ToolbarButton tbn1=(ToolbarButton)this.getToolbar().getItem("cancel_id");
			tbn1.setDisabled(true);
		}else if(count>4){//ҳ�����ݳ���4��,��������,ҳ��ᱨ��,�����ڴ��ж�
			if(this.getBianmValue().getId()!=-1 && this.getMsg().equals("")){
				this.setMsg(this.getBianmValue().getValue()+" �ոɻ�ˮ�����ݳ���4��������!");
			}
			ToolbarButton tbn=(ToolbarButton)this.getToolbar().getItem("commit_id");//���ð�ť
			tbn.setDisabled(true);
			
			ToolbarButton tbn1=(ToolbarButton)this.getToolbar().getItem("cancel_id");
			tbn1.setDisabled(true);
			con.Close();
			return;
		}
		
		double shuif_sum=0;//ˮ����ֵ
				shenhry="";
				huayry="";
		colsnum =1;		
		while(rsl.next()){
			//int colsnum = count;//rsl.getInt("cis")
			int initRow = 4;
			int initCol = 7 + colsnum;
			ArrHeader[initRow++][initCol] = rsl.getString("qimbh");//����ƿ��
			ArrHeader[initRow++][initCol] = tb.format(rsl//����ƿ����
					.getString("qimzl"), "0.0000");
			ArrHeader[initRow++][initCol] = tb.format(rsl//ʽ������+����ƿ����
					.getString("qishizl"), "0.0000");
			ArrHeader[initRow++][initCol] = tb.format(rsl//ʽ������
					.getString("shiyzl"), "0.0000");
			ArrHeader[initRow++][initCol] = tb.format(rsl//��ɺ�������
					.getString("honghzzl"), "0.0000");
			ArrHeader[initRow++][initCol] = tb.format(rsl//��������������
					.getString("jiancxsyhzzl"), "0.0000");
			
			double ganzhjszl=0;//�����ú����������
			ganzhjszl=rsl.getDouble("qishizl")-rsl.getDouble("honghzzl");
			ArrHeader[initRow++][initCol] = tb.format(ganzhjszl+"", "0.0000");
			
			//double quansMT=tb.Round_New(ganzhjszl/rsl.getDouble("shiyzl")*100,2);//ˮ��
			double quansMT=tb.Round_New(rsl.getDouble("huayz"),2);//ˮ��
			ArrHeader[initRow++][initCol] = tb.format(quansMT+"","0.00");
			shuif_sum+=Double.valueOf(tb.format(quansMT+"","0.00")).doubleValue();
			
			if(rsl.getString("shenhy")!=null){
				shenhry=rsl.getString("shenhy");
			}
			if(rsl.getString("huayy")!=null){
				huayry=rsl.getString("huayy");
			}
			colsnum=colsnum+1;
		}
		double pingjz_shuif=0;
		shuif_sum=Double.valueOf(tb.format(shuif_sum+"","0.00")).doubleValue();
		if(count==0){
			pingjz_shuif=0;
		}else{
			pingjz_shuif=tb.Round_New(shuif_sum/count,2);
		}
		
		ArrHeader[12][8] = tb.format(pingjz_shuif+"","0.00");
		
		ArrHeader[13][7]+=shenhry;
		ArrHeader[13][8]+=huayry;
		
		rsl.close();
//��ҵ����  �ҷ�
		//wzb ���������ж�,ȡʱ��ε�ǰ7��,������,��ֹ����ظ�,�����糧�����ŵ��²��ظ�,�¸��¾��п����ظ�.
		bf.setLength(0);
		bf.append(" select h.huayz,h.cis,h.qimbh,h.qimzl,h.shenhy,h.huayy,\n");
		bf.append(" h.qimzl+h.meiyzl qishizl,h.meiyzl shiyzl,h.honghzzl,h.zongzl,\n");
		bf.append(" h.honghzzl1 jiancxsyhzzl \n");
		bf.append(" from huaygyfxb h,item f,itemsort i \n");
		bf.append(" where h.fenxxmb_id=f.id and f.bianm='").append(Huayysjlsh.Aad).append("' \n");
		bf.append(" and h.bianm='").append(this.getBianmValue().getValue()).append("' \n");
		bf.append(" and h.diancxxb_id=").append(visit.getDiancxxb_id());
		bf.append("  and f.itemsortid=i.itemsortid and i.bianm='").append(huayysjl_bianm).append("' \n");
		bf.append("  and h.riq>=to_date('"+getRiq()+"','yyyy-mm-dd')-7 ").append(" \n");
		bf.append("  and h.riq<=to_date('"+getEriq()+"','yyyy-mm-dd')+7").append(" \n");
		bf.append(" and h.shenhzt>=1 order by h.cis");
		
		rsl=con.getResultSetList(bf.toString());
		
		count=rsl.getRows();//���ؼ�¼����  ƽ��ֵ��
		if(count==0){//��ֵ   ��ʾ����
			if(this.getBianmValue().getId()!=-1 && this.getMsg().equals("")){//  �ų� ��ѡ��  ʱ  ��ʾ����Ϣ 
				this.setMsg("��ȡ�ҷ�����ʧ�ܣ�����!");//���ð�ť
			}
			ToolbarButton tbn=(ToolbarButton)this.getToolbar().getItem("commit_id");
			tbn.setDisabled(true);
			
			ToolbarButton tbn1=(ToolbarButton)this.getToolbar().getItem("cancel_id");
			tbn1.setDisabled(true);
		}else if(count>4){//ҳ�����ݳ���4��,��������,ҳ��ᱨ��,�����ڴ��ж�
			if(this.getBianmValue().getId()!=-1 && this.getMsg().equals("")){
				this.setMsg(this.getBianmValue().getValue()+" �ҷ����ݳ���4��������!");
			}
			ToolbarButton tbn=(ToolbarButton)this.getToolbar().getItem("commit_id");//���ð�ť
			tbn.setDisabled(true);
			
			ToolbarButton tbn1=(ToolbarButton)this.getToolbar().getItem("cancel_id");
			tbn1.setDisabled(true);
			con.Close();
			return;
		}
		
		double huif_sum=0;//�ҷ���ֵ
				shenhry="";
				huayry="";
				colsnum =1;		
		while(rsl.next()){
			//int colsnum = count;//rsl.getInt("cis");
			int initRow = 14;
			int initCol = 7 + colsnum;
			ArrHeader[initRow++][initCol] = rsl.getString("qimbh");//����ƿ��
			ArrHeader[initRow++][initCol] = tb.format(rsl//����ƿ����
					.getString("qimzl"), "0.0000");
			ArrHeader[initRow++][initCol] = tb.format(rsl//ʽ������+����ƿ����
					.getString("qishizl"), "0.0000");
			ArrHeader[initRow++][initCol] = tb.format(rsl//ʽ������
					.getString("shiyzl"), "0.0000");
			ArrHeader[initRow++][initCol] = tb.format(rsl//��ɺ�������
					.getString("honghzzl"), "0.0000");
			ArrHeader[initRow++][initCol] = tb.format(rsl//��������������
					.getString("jiancxsyhzzl"), "0.0000");
			
			double ganzhjszl=0;//�����ú����������
			ganzhjszl=rsl.getDouble("honghzzl")-rsl.getDouble("qimzl");//   ������ ����
			ArrHeader[initRow++][initCol] = tb.format(ganzhjszl+"", "0.0000");
			
			//double quansMT=tb.Round_New(ganzhjszl/rsl.getDouble("shiyzl")*100,2);//  �ҷ�  Aad
			double quansMT=tb.Round_New(rsl.getDouble("huayz"),2);//  �ҷ�  Aad
			ArrHeader[initRow++][initCol] = tb.format(quansMT+"","0.00");
			huif_sum+=Double.valueOf(tb.format(quansMT+"","0.00")).doubleValue();
			
			if(rsl.getString("shenhy")!=null){
				shenhry=rsl.getString("shenhy");
			}
			if(rsl.getString("huayy")!=null){
				huayry=rsl.getString("huayy");
			}
			colsnum =colsnum +1;
		}
		huif_sum=Double.valueOf(tb.format(huif_sum+"","0.00")).doubleValue();
		double pingjz_huif=0;
		if(count==0){
			pingjz_huif=0;
		}else{
			pingjz_huif=tb.Round_New(huif_sum/count,2);
		}
		
		ArrHeader[22][8] = tb.format(pingjz_huif+"","0.00");
		
		ArrHeader[23][7]+=shenhry;
		ArrHeader[23][8]+=huayry;
		
		rsl.close();
		
		
		//��ҵ����  �ӷ���
//		wzb ���������ж�,ȡʱ��ε�ǰ7��,������,��ֹ����ظ�,�����糧�����ŵ��²��ظ�,�¸��¾��п����ظ�.
		bf.setLength(0);
		bf.append(" select h.huayz,h.cis,h.qimbh,h.qimzl,h.shenhy,h.huayy,\n");
		bf.append(" h.qimzl+h.meiyzl qishizl,h.meiyzl shiyzl,h.honghzzl,h.zongzl,\n");
		bf.append(" h.honghzzl1 jiancxsyhzzl \n");
		bf.append(" from huaygyfxb h,item f,itemsort i \n");
		bf.append(" where h.fenxxmb_id=f.id and f.bianm='").append(Huayysjlsh.Vad).append("' \n");
		bf.append(" and h.bianm='").append(this.getBianmValue().getValue()).append("' \n");
		bf.append(" and h.diancxxb_id=").append(visit.getDiancxxb_id());
		bf.append("  and f.itemsortid=i.itemsortid and i.bianm='").append(huayysjl_bianm).append("' \n");
		bf.append("  and h.riq>=to_date('"+getRiq()+"','yyyy-mm-dd')-7 ").append(" \n");
		bf.append("  and h.riq<=to_date('"+getEriq()+"','yyyy-mm-dd')+7").append(" \n");
		bf.append(" and h.shenhzt>=1 order by h.cis");
		
		rsl=con.getResultSetList(bf.toString());
		
		count=rsl.getRows();//���ؼ�¼����  ƽ��ֵ��
		if(count==0){//��ֵ   ��ʾ����
			if(this.getBianmValue().getId()!=-1 && this.getMsg().equals("")){//  �ų� ��ѡ��  ʱ  ��ʾ����Ϣ 
				this.setMsg("��ȡ�ӷ�������ʧ�ܣ�����!");//���ð�ť
			}
			ToolbarButton tbn=(ToolbarButton)this.getToolbar().getItem("commit_id");
			tbn.setDisabled(true);
			
			ToolbarButton tbn1=(ToolbarButton)this.getToolbar().getItem("cancel_id");
			tbn1.setDisabled(true);
		}else if(count>4){//ҳ�����ݳ���4��,��������,ҳ��ᱨ��,�����ڴ��ж�
			if(this.getBianmValue().getId()!=-1 && this.getMsg().equals("")){
				this.setMsg(this.getBianmValue().getValue()+" �ӷ������ݳ���4��������!");
			}
			ToolbarButton tbn=(ToolbarButton)this.getToolbar().getItem("commit_id");//���ð�ť
			tbn.setDisabled(true);
			
			ToolbarButton tbn1=(ToolbarButton)this.getToolbar().getItem("cancel_id");
			tbn1.setDisabled(true);
			con.Close();
			return;
		}
		
		double huiff_sum=0;//�ӷ�����ֵ
				shenhry="";
				huayry="";
		colsnum =1;		
		while(rsl.next()){
			//int colsnum =count;// rsl.getInt("cis");
			int initRow = 14;
			int initCol = 1 + colsnum;
			ArrHeader[initRow++][initCol] = rsl.getString("qimbh");//������
			ArrHeader[initRow++][initCol] = tb.format(rsl//��������
					.getString("qimzl"), "0.0000");
			ArrHeader[initRow++][initCol] = tb.format(rsl//ʽ������+��������
					.getString("qishizl"), "0.0000");
			ArrHeader[initRow++][initCol] = tb.format(rsl//ʽ������
					.getString("shiyzl"), "0.0000");
			ArrHeader[initRow++][initCol] = tb.format(rsl//��ɺ�������
					.getString("honghzzl"), "0.0000");
//			ArrHeader[initRow++][initCol] = tb.format(rsl//��������������
//					.getString("jiancxsyhzzl"), "0.0");
			
			double ganzhjszl=0;//�����ú����������
			ganzhjszl=rsl.getDouble("qishizl")-rsl.getDouble("honghzzl");
			ArrHeader[initRow++][initCol] = tb.format(ganzhjszl+"", "0.0000");
			
			double quansMT=0.0d;
			if(VadIsCount){//�ӷ�����ͨ����ƽ���ģ�vad���¼���
				 quansMT=tb.Round_New(ganzhjszl/rsl.getDouble("shiyzl")*100-pingjz_shuif,2);//�ӷ���  -ˮ�ֵľ�ֵ
			}else{
				 quansMT=tb.Round_New(rsl.getDouble("huayz"),2);
			}
			
			
			ArrHeader[initRow++][initCol] = tb.format(quansMT+"","0.00");
			huiff_sum+=Double.valueOf(tb.format(quansMT+"","0.00")).doubleValue();
			
			if(rsl.getString("shenhy")!=null){
				shenhry=rsl.getString("shenhy");
			}
			if(rsl.getString("huayy")!=null){
				huayry=rsl.getString("huayy");
			}
			colsnum =colsnum+1;
		}
		huiff_sum=Double.valueOf(tb.format(huiff_sum+"","0.00")).doubleValue();
		double pingjz_huiff=0;
		if(count==0){
			pingjz_huiff=0;
		}
		else{
			pingjz_huiff=tb.Round_New(huiff_sum/count,2);
		}
		
		
		ArrHeader[21][2] = tb.format(pingjz_huiff+"","0.00");
		
		double Vdaf_v=this.getVdaf_v(pingjz_huiff, pingjz_shuif,pingjz_huif);
		ArrHeader[22][2] = tb.format(Vdaf_v+"", "0.00");
//			
		ArrHeader[23][1]+=shenhry;
		ArrHeader[23][2]+=huayry;
		
		rsl.close();
		
		
		
		
		
		//  ��ֱ�  ȫ��
		
		double Stad_v=0;
		double Std_v=0;
		String sign="";
		if(this.getLiuly().equals(yuansfx)){//  ϵͳ���� �� Ԫ�ط���
			if(this.getLiupz()){//������ ��ȷ
				
			    sign=this.getYuansSign(con, _liu, visit.getDiancxxb_id()+"");
				
				if(sign.toUpperCase().indexOf("STAD")!=-1){//ϵͳ���õ��� stad
					
					if(this.getBianmValue().getId()!=-1){//  �� �� ��ѡ�� ʱ  ��Ҫ��ʾ
						ArrHeader[29][2]=tb.format(this.getLiujz(), "0.00");
						Stad_v=Double.valueOf(this.getLiujz()).doubleValue();
						Std_v=this.getStd_v(Double.valueOf(this.getLiujz()).doubleValue(), pingjz_shuif);
						ArrHeader[30][2]=tb.format(Std_v+"", "0.00");
					}
					
				}
				if(sign.toUpperCase().indexOf("STD")!=-1){//ϵͳ���õ��� std
					if(this.getBianmValue().getId()!=-1){
						Stad_v=this.getStad_v(Double.valueOf(this.getLiujz()).doubleValue(), pingjz_shuif);
						ArrHeader[29][2]=tb.format(Stad_v+"", "0.00");
						Std_v=Double.valueOf(this.getLiujz()).doubleValue();
						ArrHeader[30][2]=tb.format(this.getLiujz(), "0.00");
					}
					
				}
			}else{
				
			}
		}else{//�Զ�����  ȡֵ  ��ֵ
			
//			wzb ���������ж�,ȡʱ��ε�ǰ7��,������,��ֹ����ظ�,�����糧�����ŵ��²��ظ�,�¸��¾��п����ظ�.
			bf.setLength(0);
			bf.append(" select h.cis,h.liuf, h.qimbh,h.qimzl,h.qimzl+h.meiyzl qishizl,\n");
			bf.append(" h.meiyzl shiyzl,h.shenhy,h.huayy \n");
			bf.append(" from huaylfb h,item f,itemsort i \n");
			bf.append(" where h.fenxxmb_id=f.id and f.bianm='").append(Huayysjlsh.Stad).append("' \n");
			bf.append(" and h.bianm='").append(this.getBianmValue().getValue()).append("' \n");
			bf.append(" and h.diancxxb_id=").append(visit.getDiancxxb_id());
			bf.append("  and f.itemsortid=i.itemsortid and i.bianm='").append(huayysjl_bianm).append("' \n");
			bf.append("  and h.riq>=to_date('"+getRiq()+"','yyyy-mm-dd')-7 ").append(" \n");
			bf.append("  and h.riq<=to_date('"+getEriq()+"','yyyy-mm-dd')+7").append(" \n");
			bf.append(" and h.shenhzt>=1 order by h.cis");
			
			rsl=con.getResultSetList(bf.toString());
			
			count=rsl.getRows();//���ؼ�¼����  ƽ��ֵ��
			if(count==0){//��ֵ   ��ʾ����
				if(this.getBianmValue().getId()!=-1 && this.getMsg().equals("")){//  �ų� ��ѡ��  ʱ  ��ʾ����Ϣ 
					this.setMsg("��ȡ�������ʧ�ܣ�����!");//���ð�ť
				}
				
				ToolbarButton tbn=(ToolbarButton)this.getToolbar().getItem("commit_id");
				tbn.setDisabled(true);
				
				ToolbarButton tbn1=(ToolbarButton)this.getToolbar().getItem("cancel_id");
				tbn1.setDisabled(true);
			}else if(count>4){//ҳ�����ݳ���4��,��������,ҳ��ᱨ��,�����ڴ��ж�
				if(this.getBianmValue().getId()!=-1 && this.getMsg().equals("")){
					this.setMsg(this.getBianmValue().getValue()+" ������ݳ���4��������!");
				}
				ToolbarButton tbn=(ToolbarButton)this.getToolbar().getItem("commit_id");//���ð�ť
				tbn.setDisabled(true);
				
				ToolbarButton tbn1=(ToolbarButton)this.getToolbar().getItem("cancel_id");
				tbn1.setDisabled(true);
				con.Close();
				return;
			}
			
			double quanl_sum=0;//ȫ����ֵ
					shenhry="";
					huayry="";
			colsnum =1;		
			while(rsl.next()){
				//int colsnum =count;// rsl.getInt("cis");
				int initRow = 24;
				int initCol = 1 + colsnum;
				ArrHeader[initRow++][initCol] = rsl.getString("qimbh");//����ƿ��
				if (rsl.getDouble("qimzl")>0){
					ArrHeader[initRow++][initCol] = tb.format(rsl//����ƿ����
							.getString("qimzl"), "0.0000");
				}else{
					ArrHeader[initRow++][initCol]="";
				}
				if (rsl.getDouble("qimzl")>0){
					ArrHeader[initRow++][initCol] = tb.format(rsl//ʽ������+����ƿ����
							.getString("qishizl"), "0.0000");
				}else{
					ArrHeader[initRow++][initCol]="";
				}
				
				ArrHeader[initRow++][initCol] = tb.format(rsl//ʽ������
						.getString("shiyzl"), "0.0000");
		
				
				double liuf=tb.Round_New(rsl.getDouble("liuf"),2);
				quanl_sum+=liuf;
				
				ArrHeader[initRow++][initCol] = tb.format(liuf+"", "0.00");
				
				if(rsl.getString("shenhy")!=null){
					shenhry=rsl.getString("shenhy");
				}
				if(rsl.getString("huayy")!=null){
					huayry=rsl.getString("huayy");
				}
				colsnum =colsnum+1;
			}
			quanl_sum=Double.valueOf(tb.format(quanl_sum+"","0.00")).doubleValue();
			double pingjz_quanl=0;
			if(count==0){
				pingjz_quanl=0;
			}else{
				pingjz_quanl=tb.Round_New(quanl_sum/count,2);
			}
			
			Stad_v=pingjz_quanl;
			ArrHeader[29][2] = tb.format(pingjz_quanl+"","0.00");
			Std_v=this.getStd_v(pingjz_quanl, pingjz_shuif);
			ArrHeader[30][2] = tb.format(this.getStd_v(pingjz_quanl, pingjz_shuif)+"","0.00");
			              
			ArrHeader[31][1]+=shenhry;
			ArrHeader[31][2]+=huayry;
			
			rsl.close();
			
			
		}
		
		
		
		//������ 
	
//		wzb ���������ж�,ȡʱ��ε�ǰ7��,������,��ֹ����ظ�,�����糧�����ŵ��²��ظ�,�¸��¾��п����ظ�.
		bf.setLength(0);
		bf.append(" select h.cis,h.qimzl,h.farl,h.meiyzl shiyzl,h.shenhy,h.huayy \n");
		bf.append(" from huayrlb h,item f,itemsort i  \n");
		bf.append(" where h.fenxxmb_id=f.id and f.bianm='").append(Huayysjlsh.Qbad).append("' \n");
		bf.append(" and h.bianm='").append(this.getBianmValue().getValue()).append("' \n");
		bf.append(" and h.diancxxb_id=").append(visit.getDiancxxb_id());
		bf.append("  and f.itemsortid=i.itemsortid and i.bianm='").append(huayysjl_bianm).append("' \n");
		bf.append("  and h.riq>=to_date('"+getRiq()+"','yyyy-mm-dd')-7 ").append(" \n");
		bf.append("  and h.riq<=to_date('"+getEriq()+"','yyyy-mm-dd')+7").append(" \n");
		bf.append(" and h.shenhzt>=1 order by h.cis");
		
		rsl=con.getResultSetList(bf.toString());
		
		count=rsl.getRows();//���ؼ�¼����  ƽ��ֵ��
		if(count==0){//��ֵ   ��ʾ����
			if(this.getBianmValue().getId()!=-1 && this.getMsg().equals("")){//  �ų� ��ѡ��  ʱ  ��ʾ����Ϣ 
				this.setMsg("��ȡ��Ͳ������ʧ�ܣ�����!");//���ð�ť
			}
			ToolbarButton tbn=(ToolbarButton)this.getToolbar().getItem("commit_id");
			tbn.setDisabled(true);
			
			ToolbarButton tbn1=(ToolbarButton)this.getToolbar().getItem("cancel_id");
			tbn1.setDisabled(true);
		}else if(count>4){//ҳ�����ݳ���4��,��������,ҳ��ᱨ��,�����ڴ��ж�
			if(this.getBianmValue().getId()!=-1 && this.getMsg().equals("")){
				this.setMsg(this.getBianmValue().getValue()+" ��Ͳ�����ݳ���4��������!");
			}
			ToolbarButton tbn=(ToolbarButton)this.getToolbar().getItem("commit_id");//���ð�ť
			tbn.setDisabled(true);
			
			ToolbarButton tbn1=(ToolbarButton)this.getToolbar().getItem("cancel_id");
			tbn1.setDisabled(true);
			con.Close();
			return;
		}
		
		double farl_sum=0;//�ҷ���ֵ
				shenhry="";
				huayry="";
		colsnum =1;		
		while(rsl.next()){
			//int colsnum =count;// rsl.getInt("cis");
			int initRow = 24;
			int initCol = 7 + colsnum;
		
			if (rsl.getDouble("qimzl")>0){
				ArrHeader[initRow++][initCol] = tb.format(rsl//��������
						.getString("qimzl"), "0.0000");
			}else{
				ArrHeader[initRow++][initCol] = "";
			}
			ArrHeader[initRow++][initCol]="";//
			ArrHeader[initRow++][initCol] = tb.format(rsl//��ƽ ʽ������
					.getString("shiyzl"), "0.0000");
			
			ArrHeader[initRow++][initCol]=tb.format(rsl.getString("farl"), "0");
			farl_sum+=Double.valueOf(tb.format(rsl.getDouble("farl")+"","0")).doubleValue();
			
			
			if(rsl.getString("shenhy")!=null){
				shenhry=rsl.getString("shenhy");
			}
			if(rsl.getString("huayy")!=null){
				huayry=rsl.getString("huayy");
			}
			colsnum =colsnum+1;
		}
		farl_sum=Double.valueOf(tb.format(farl_sum+"","0")).doubleValue();
		double pingjz_farl=0;
		if(count==0){
			pingjz_farl=0;
		}else{
			pingjz_farl=tb.Round_New(farl_sum/count,0);
		}
		
		
		ArrHeader[28][8] = tb.format(pingjz_farl+"","0");
		double Qgrad=tb.Round_New(this.getQgrad_v(pingjz_farl/1000, Stad_v),0);
		ArrHeader[29][8] = tb.format(Qgrad+"","0");
		
		//�õ� xitxxb �� ��ķ���  had�� hdaf��
		String sig_had=this.getYuansSign(con, _qing, visit.getDiancxxb_id()+"");
		
		double Had_v=0;
		double Hdaf_v=0;
		double Qnetar=0;
		if(this.getBianmValue().getId()!=-1){
			
			if(sig_had.toUpperCase().equals("HAD")){
				Had_v=this.getHad_v(con,pingjz_huif, pingjz_shuif, pingjz_huiff, pingjz,sig_had,this.getBianmValue().getValue());
				Hdaf_v=this.getHdaf_v_ByHad(pingjz_huiff, pingjz_shuif, Had_v);
			}else if(sig_had.toUpperCase().equals("HDAF")){
				Hdaf_v=this.getHad_v(con,pingjz_huif, pingjz_shuif, pingjz_huiff, pingjz,sig_had,this.getBianmValue().getValue());
				Had_v=this.getHad_v_ByHdaf(pingjz_huif, pingjz_shuif, Hdaf_v);
			}
			
			ArrHeader[0][7] = tb.format(Had_v+"","0.00");
			
			Qnetar=this.getQnetar_v(Qgrad/1000, pingjz_huif, pingjz, Had_v, pingjz_shuif);
			ArrHeader[30][8] = tb.format(Qnetar+"", "0.00");             
		}
		
		ArrHeader[31][7]+=shenhry;
		ArrHeader[31][8]+=huayry;
		
		rsl.close();
		
		con.Close();
//--------------------- ͨ�����㹫ʽ  ����ֵ  �Ƿ���ȷ	�ж�����	-------------
		
		
		boolean t1=false;
		boolean t2=false;
		if(this.getLiuly().equals(yuansfx)){//  ϵͳ���� �� Ԫ�ط���  ��
			
			if(Stad_v<=0){
				t1=true;//�� ��ֵ  Ҳ��Ҫ �ж� �Ƿ�����
			}
			
			
			
		}else{   //�ɼ�����  Ҳ��Ҫ�жϣ�
			
		}
		
		if(this.getQingly().equals(yuansfx)){ //  ϵͳ���� �� Ԫ�ط���  ��
			
			if(Had_v<=0){
				t2=true;//��� ֵ  Ҳ��Ҫ�ж� �Ƿ�����
			}
			
		}else{
			
		}
		
		
	
	if(pingjz<=0 || pingjz_shuif<=0 || pingjz_huiff<=0 || pingjz_huif<=0  || pingjz_farl<=0 || t1 || t2)
	{
		//���� ���� û������ ���
		
		if(!this.getQingly().equals(yuansfx)){ //  ���� Ԫ�ط���  Ҫ���
			
			Had_v=0;
			ArrHeader[0][7] = "";
		}else{
			if(sig_had.toUpperCase().equals("HDAF")){
				Had_v=0;
				ArrHeader[0][7] = "";
			}else{
				Hdaf_v=0;
				}
		}
		
		if(!this.getLiuly().equals(yuansfx)){//  ���� Ԫ�ط���  Ҫ���
			Stad_v=0;
		}else{
			if(sign.toUpperCase().equals("STD")){
				Stad_v=0;
				ArrHeader[29][2]="";
			}else{
				Std_v=0;
				ArrHeader[30][2]= "";//Std
			}
		}
		Vdaf_v=0;
		ArrHeader[22][2] = "";//vdaf
		
		Qgrad=0;
		ArrHeader[29][8] = "";//Qgrad
		Qnetar=0;
		ArrHeader[30][8] = "";//Qnetar
		
	
		
		this.setAar_v(0);
		this.setAd_v(0);
		this.setFcad_v(0);
		this.setQgrd_v(0);
		
	}else{		
		this.setAar_v(tb.Round_New(pingjz_huif*(100-pingjz)/(100-pingjz_shuif),2));
		this.setAd_v(tb.Round_New(pingjz_huif*100/(100-pingjz_shuif),2));
		this.setFcad_v(tb.Round_New(100-pingjz_shuif-pingjz_huif-pingjz_huiff,2));
		this.setQgrd_v(tb.Round_New(Qgrad*100/(100-pingjz_shuif)/1000,3));
	}
		

		
		
		//--------��ż���Ҫ�õı���ֵ
		
	this.setMt_v(pingjz);
	this.setMad_v(pingjz_shuif);
	this.setVad_v(pingjz_huiff);
	this.setAad_v(pingjz_huif);
	this.setStad_v(Stad_v);
	this.setQbad_v(pingjz_farl/1000);
	this.setHad_v(Had_v);
	this.setHdaf_v(Hdaf_v);
	
	this.setVdaf_v(Vdaf_v);
	this.setStd_v(Std_v);
	this.setQgrad_v(Qgrad/1000);
	this.setQnetar_v(Qnetar);
	
		
	
	}
	
	//���� Had
	
	private double getHad_v(JDBCcon con,double Aad_v,double Mad_v,double Vad_v,double Mt_v,String sign,String bianm ){
		Table tb = new Table(1, 1);
		double had=0;
		double hdaf=0;
		String sql="";
		if(this.getQingly(con).equals(yuansfx)){//��Ԫ�ط���
			
			if(this.getQingpz()){
				if(sign.toUpperCase().indexOf("HAD")!=-1){
					had=tb.Round_New(Double.valueOf(this.getQingjz()).doubleValue(),2);
					hdaf=tb.Round_New(had * 100 / (100 - Mad_v - Aad_v),2);
				}else{ //ϵͳ�����õ��� hdaf���ݴ� ����� had
					hdaf=tb.Round_New(Double.valueOf(this.getQingjz()).doubleValue(),2);
					had=tb.Round_New(Double.valueOf(this.getQingjz()).doubleValue()*(100 - Mad_v - Aad_v)/100,2);
				}
				
			}
		}else if (this.getQingly(con).equals(shougwh)){
			try{
				sql="select cai.had from zhuanmb m, caiyb cai,zhuanmlb l,zhillsb z"
					+ " where z.id = m.zhillsb_id and m.zhuanmlb_id = l.id  and l.mingc = '"+huaybm+"'"
					+ " and cai.zhilb_id=z.zhilb_id and m.bianm='"+bianm+"'"
					+ " union select nvl(q.had,0) as had from zhillsb z,qitycyb q where z.id=q.zhillsb_id(+) and z.beiz='"+bianm+"'";
				ResultSet rs=con.getResultSet(sql);
				if (rs.next()){
					had=tb.Round_New(rs.getDouble("had"),2);
					hdaf=tb.Round_New(had * 100 / (100 - Mad_v - Aad_v),2);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}else{//�Զ�����
			
			if(Aad_v<=0 || Mad_v<=0 || Vad_v<=0 || Mt_v<=0 ){
				return 0;
			}
			
			double Aar_v = tb.Round_New(Aad_v*(100-Mt_v)/(100-Mad_v),2);
			double Vd_v  = tb.Round_New(Vad_v*100/(100-Mad_v),2);
			double Ad_v  = tb.Round_New(Aar_v*100/(100-Aad_v),2);
			had=tb.Round_New((Vd_v*100/(100-Ad_v)*0.074+2.16)*(100-Mad_v-Aad_v)/100,2);
			hdaf=tb.Round_New(had * 100 / (100 - Mad_v - Aad_v),2);
		}
		if(sign.toUpperCase().indexOf("HDAF")!=-1){
			return hdaf;
		}
		return had;
	}
	
	private double getHdaf_v_ByHad(double Aad_v,double Mad_v,double Had_v){
		Table tb = new Table(1, 1);
		double hdaf=tb.Round_New(Had_v * 100 / (100 - Mad_v - Aad_v),2);
		
		return hdaf;
	}
	
	private double getHad_v_ByHdaf(double Aad_v,double Mad_v,double Hdaf_v){
		Table tb = new Table(1, 1);
		double had=tb.Round_New(Hdaf_v* (100 - Mad_v - Aad_v)/100,2);
		return had;
	}
	//����  Qnetar
	
	private double getQnetar_v(double Qgrad_v,double Aad_v,double Mt_v,double Had_v,double Mad_v){
		Table tb = new Table(1, 1);
		if(Qgrad_v<=0 || Aad_v<=0 || Mt_v<=0 || Had_v<=0 ||  Mad_v<=0){
			return 0;
		}
		double qne=0;
		double Aar_v=tb.Round_New(Aad_v*(100-Mt_v)/(100-Mad_v),2);
	
		qne  = tb.Round_New(((Qgrad_v-0.206*Had_v)*(100-Mt_v)/(100-Mad_v)-0.023*Mt_v),3);
		qne  = tb.Round_New(qne,2);
		return qne;
	}
	//���� Qgrad
	private double getQgrad_v(double Qbad_v,double Stad_v){
		double qgr_v=0;
		Table tb = new Table(1, 1);
		if(Qbad_v <= 16.700){
			qgr_v = tb.Round_New((Qbad_v-0.0941*Stad_v-0.001*Qbad_v)*1000,0);
		}else{
			 if(Qbad_v > 16.700 && Qbad_v <= 25.100){
				 qgr_v = tb.Round_New((Qbad_v-0.0941*Stad_v-0.0012*Qbad_v)*1000,0);
			 }else{
				 qgr_v = tb.Round_New((Qbad_v-0.0941*Stad_v-0.0016*Qbad_v)*1000,0);
			 }
		}
		
		return qgr_v;
	}
	// ���� stad��ֵ
	private double getStad_v(double Std_v,double Mad_v){
		double stad_v=0;
		Table tb = new Table(1, 1);
		stad_v=tb.Round_New(Std_v*(100-Mad_v)/100,2);
		return stad_v;
	}
	//���� std��ֵ
	private double getStd_v(double Stad_v,double Mad_v){
		
		double std_v=0;
		Table tb = new Table(1, 1);
		std_v=tb.Round_New(Stad_v*100/(100-Mad_v),2);
		
		return std_v;
	}
	
	//���㹫ʽ ����  �����޻һ��ӷ���Vdaf
	private double getVdaf_v(double Vad_v,double Mad_v,double Aad_v){
		double vdaf_v=0;
		Table tb = new Table(1, 1);
		double Vd_v  = tb.Round_New(Vad_v*100/(100-Mad_v),2);
		vdaf_v=tb.Round_New(Vad_v*100/(100-Aad_v-Mad_v),2);
		
		return vdaf_v;
	}
	
	//------------------------����  ����ֵ------------
	
	private void setMt_v(double value){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble4(value);
	}
	
	private double getMt_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble4();
	}
	private void setMad_v(double value){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble5(value);
	}
	private double getMad_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble5();
	}
	private void setVad_v(double value){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble6(value);
	}
	private double getVad_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble6();
	}
	private void setAad_v(double value){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble7(value);
	}
	private double getAad_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble7();
	}
	private void setStad_v(double value){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble8(value);
	}
	private double getStad_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble8();
	}
	private void setQbad_v(double value){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble9(value);
	}
	private double getQbad_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble9();
	}
	private void setHad_v(double value){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble10(value);
	}
	private double getHad_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble10();
	}
	private void setStd_v(double value){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble11(value);
	}
	private double getStd_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble11();
	}
	private void setQgrad_v(double value){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble12(value);
	}
	private double getQgrad_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble12();
	}
	private void setQnetar_v(double value){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble13(value);
	}
	private double getQnetar_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble13();
	}
	private void setVdaf_v(double value){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble14(value);
	}
	private double getVdaf_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble14();
	}
	
	private void setAar_v(double Aar_v){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble16(Aar_v);
	}
	private double getAar_v(){
		
		
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble16();
	}
	
	private void setAd_v(double Ad_v){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble17(Ad_v);
	}
	private double getAd_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		
		return visit.getDouble17();
	}
	
	private void setFcad_v(double Fcad_v){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble18(Fcad_v);
	}
	private double getFcad_v(){
		Visit visit=(Visit)this.getPage().getVisit();
		
		return visit.getDouble18();
	}
	
	private void setHdaf_v(double value){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble15(value);
	}
	private double getHdaf_v(){

//		double value= Round_New(this.getHad_v() * 100 / (100 - this.getMad_v() - this.getAad_v()));
		Visit visit=(Visit)this.getPage().getVisit();
		
		return visit.getDouble15();
	}
	
	private void setQgrd_v(double Qgrd_v){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setDouble19(Qgrd_v);
	}
	//Qgrd_v
	private double getQgrd_v(){
//		double value=Round_New(this.getQgrad_v()*100/(100-this.getMad_v()));
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getDouble19();
	}
	
	private double  getSdaf_v(double Std,double Ad){
		double value=0;
		Table tb=new Table(1,1);
		value=tb.Round_New(Std*100/(100-Ad), 2);
		return value;
	}
	
	private double getQgrad_daf_v(double Qgrad,double Aad,double Mad){
		double dblQgrdaf=0;
		Table tb=new Table(1,1);
		dblQgrdaf=tb.Round_New(Qgrad*100/(100-Aad-Mad), 2);
		return dblQgrdaf;
	}
	private String getVar_v(){
		return "''";
	}
	private String getQbrad_v(){
		return "''";
	}
	private double  getHar_v(double Had,double Mt,double Mad){
		double dblHar=0;
		Table tb=new Table(1,1);
		dblHar=tb.Round_New(Had*(100-Mt)/(100-Mad), 2);
		return dblHar;
	}
	
	
	//-----------------------
	// ȼ�ϲɹ���ָ���������ձ�
	private String getHuaybgd() {
		
		
		Visit visit=(Visit)this.getPage().getVisit();
		
		if(!visit.getboolean5() || !visit.getboolean6()){//�� �� ��  û��������ȷ ҳ�治����ʾ
			this.setMsg("Ԫ�ط�����Ŀ�����ֵ���ò���ȷ!");
//			return "";
		}
		String rltitle1 = "��������+��������(g)";
		String rltitle2 = "��������(g)";
//		if (isRelbalance()) {
//			rltitle1 = "��ƽ ��������(g)";
//			rltitle2 = "�Զ� ��������(g)";
//		}
		String ArrHeader[][] = new String[33][12];
		ArrHeader[0] = new String[] { "�� ��", "", "��վ(��)", "", "", "", "�ոɻ���",
				"", "ú��(t)", "", "", "" };
		ArrHeader[1] = new String[] { "Ʒ ��", "", "�� ��", "", "", "", "��������", "",
				"������", "", "", "" };
		ArrHeader[2] = new String[] { "����/����", "", "�¶�ʪ��", "", "", "", "��������", "",
				"������", "", "", "" };
		ArrHeader[3] = new String[] { "", "", "��һ��", "�ڶ���", "������", "���Ĵ�", "",
				"", "��һ��", "�ڶ���", "������", "���Ĵ�" };
		ArrHeader[4] = new String[] { "ȫˮ��Mt", "����ƿ(��)��", "", "", "", "",
				"ˮ��Mad", "����ƿ��", "", "", "", "" };
		ArrHeader[5] = new String[] { "ȫˮ��Mt", "����ƿ(��)����(g)", "", "", "", "",
				"ˮ��Mad", "����ƿ����(g)", "", "", "", "" };
		ArrHeader[6] = new String[] { "ȫˮ��Mt", "����ƿ(��)����+��������(g)", "", "", "",
				"", "ˮ��Mad", "����ƿ����+��������(g)", "", "", "", "" };
		ArrHeader[7] = new String[] { "ȫˮ��Mt", "��������m(g)", "", "", "", "",
				"ˮ��Mad", "��������m(g)", "", "", "", "" };
		ArrHeader[8] = new String[] { "ȫˮ��Mt", "��ɺ�������(g)", "", "", "", "",
				"ˮ��Mad", "��ɺ�������(g)", "", "", "", "" };
		ArrHeader[9] = new String[] { "ȫˮ��Mt", "����������������(g)", "", "", "", "",
				"ˮ��Mad", "����������������(g)", "", "", "", "" };
		ArrHeader[10] = new String[] { "ȫˮ��Mt", "�����ú�����ٵ�����m1(g)", "", "", "",
				"", "ˮ��Mad", "�����ú�����ٵ�����m1(g)", "", "", "", "" };
		ArrHeader[11] = new String[] { "ȫˮ��Mt", "ȫˮ��Mt=m1/m*100(%)", "", "",
				"", "", "ˮ��Mad", "ˮ��Mad=m1/m*100(%)", "", "", "", "" };
		ArrHeader[12] = new String[] { "ȫˮ��Mt", "ƽ��ֵMt(%)", "", "", "", "",
				"ˮ��Mad", "ƽ��ֵMad(%)", "", "", "", "" };
		ArrHeader[13] = new String[] { "ȫˮ��Mt", "���:", "����:", "����:", "����:",
				"����:", "ˮ��Mad", "���:", "����:", "����:", "����:", "����:" };
		ArrHeader[14] = new String[] { "�ӷ���Vad", "������", "", "", "", "",
				"�ҷ�Aad", "�����", "", "", "", "" };
		ArrHeader[15] = new String[] { "�ӷ���Vad", "��������(g)", "", "", "", "",
				"�ҷ�Aad", "��������(g)", "", "", "", "" };
		ArrHeader[16] = new String[] { "�ӷ���Vad", "��������+��������(g)", "", "", "",
				"", "�ҷ�Aad", "��������+��������(g)", "", "", "", "" };
		ArrHeader[17] = new String[] { "�ӷ���Vad", "��������m(g)", "", "", "", "",
				"�ҷ�Aad", "��������m(g)", "", "", "", "" };
		ArrHeader[18] = new String[] { "�ӷ���Vad", "���Ⱥ�������(g)", "", "", "", "",
				"�ҷ�Aad", "���Ⱥ�������(g)", "", "", "", "" };
		ArrHeader[19] = new String[] { "�ӷ���Vad", "ú�����Ⱥ���ٵ�����m1(g)", "", "", "",
				"", "�ҷ�Aad", "����������������(g)", "", "", "", "" };
		ArrHeader[20] = new String[] { "�ӷ���Vad", "�ӷ���Vad=m1/m*100-Mad(%)", "",
				"", "", "", "�ҷ�Aad", "�����������m1(g)", "", "", "", "" };
		ArrHeader[21] = new String[] { "�ӷ���Vad", "ƽ��ֵVad(%)", "", "", "", "",
				"�ҷ�Aad", "�ҷ�Aad(%)", "", "", "", "" };
		ArrHeader[22] = new String[] { "�ӷ���Vad", "ƽ��ֵVdaf(%)", "", "", "", "",
				"�ҷ�Aad", "ƽ��ֵ(%)", "", "", "", "" };
		ArrHeader[23] = new String[] { "�ӷ���Vad", "���:", "����:", "����:", "����:",
				"����:", "�ҷ�Aad", "���:", "����:", "����:", "����:", "����:" };
		ArrHeader[24] = new String[] { "ȫ��", "�����", "", "", "", "", "������",
				"��������(g)", "", "", "", "" };
		ArrHeader[25] = new String[] { "ȫ��", "��������(g)", "", "", "", "", "������",
				rltitle1, "", "", "", "" };
		ArrHeader[26] = new String[] { "ȫ��", "��������+��������(g)", "", "", "", "",
				"������", rltitle2, "", "", "", "" };
		ArrHeader[27] = new String[] { "ȫ��", "��������(g)", "", "", "", "", "������",
				"Qb,ad(J/g)", "", "", "", "" };
		ArrHeader[28] = new String[] { "ȫ��", "ȫ��St,ad(%)", "", "", "", "",
				"������", "ƽ��ֵQb,ad(J/g)", "", "", "", "" };
		ArrHeader[29] = new String[] { "ȫ��", "ƽ��ֵSt,ad(%)", "", "", "", "",
				"������", "Qgr,ad(J/g)", "", "", "", "" };
		ArrHeader[30] = new String[] { "ȫ��", "ƽ��ֵSt,d(%)", "", "", "", "",
				"������", "Qnet,ar(MJ/kg)", "", "", "", "" };
		ArrHeader[31] = new String[] { "ȫ��", "���:", "����:", "����:", "����:", "����:",
				"������", "���:", "����:", "����:", "����:", "����:" };
		ArrHeader[32] = new String[] { "", "", "", "", "", "", "", "", "", "",
				"", "" };
		int ArrWidth[] = new int[] { 55, 180, 51, 51, 51, 51, 55, 170, 51, 51,
				51, 51 };
		/*
		 * �����鸳ֵ
		 */
		String strRcRl="";
		if (visit.getString12().equals("RC")) {//�볧����  Ҫ��ӡ������
			setArrayValue(ArrHeader, true);
			strRcRl="�볧ú";
		} else {//��¯����ʱ Ҫ��ӡ������
			
		}

		// ��ֵ����

		Report rt = new Report();
		rt.setTitle(visit.getDiancqc()+"<br>"+strRcRl+"����ԭʼ��¼", ArrWidth);
		
		rt.setBody(new Table(33, 12));
		// rt.setDefaultTitleLeft("���Ƶ�λ��", 1);
		// rt.setDefaultTitleLeft("����", 1);
		// rt.setDefaultTitleRight("��ţ�", 1);
		//rt.setBody(new Table(ArrHeader, 0, false, Table.ALIGN_LEFT));
		rt.body.setWidth(ArrWidth);
		rt.body.setRowHeight(30);
		rt.body.setRowHeight(33, 150);
		
		
		//��ֵ
		
		for (int i = 0; i < 33; i++) {
			for (int j = 0; j < 12; j++) {
				if (ArrHeader[i][j] == null || ArrHeader[i][j].length() == 0) {
					ArrHeader[i][j] = "";
				}
				rt.body.setCellValue(i+1 , j+1 , ArrHeader[i][j]);
			}
		}
		
		// �ϲ���Ԫ��
		rt.body.mergeCell(1, 4, 1, 6);
		rt.body.mergeCell(1, 10, 1, 12);
		rt.body.mergeCell(2, 4, 2, 6);
		rt.body.mergeCell(2, 10, 2, 12);
		rt.body.mergeCell(3, 4, 3, 6);
		rt.body.mergeCell(3, 10, 3, 12);
		rt.body.mergeCell(5, 1, 14, 1);
		rt.body.mergeCell(5, 7, 14, 7);
		rt.body.mergeCell(13, 3, 13, 6);
		rt.body.mergeCell(13, 9, 13, 12);
		rt.body.mergeCell(14, 3, 14, 6);
		rt.body.mergeCell(14, 9, 14, 12);
		rt.body.mergeCell(15, 1, 24, 1);
		rt.body.mergeCell(15, 7, 24, 7);
		rt.body.mergeCell(22, 3, 22, 6);
		rt.body.mergeCell(23, 3, 23, 6);
		rt.body.mergeCell(23, 9, 23, 12);
		rt.body.mergeCell(24, 3, 24, 6);
		rt.body.mergeCell(24, 9, 24, 12);
		rt.body.mergeCell(25, 1, 32, 1);
		rt.body.mergeCell(25, 7, 32, 7);
		rt.body.mergeCell(29, 9, 29, 12);
		rt.body.mergeCell(30, 3, 30, 6);
		rt.body.mergeCell(30, 9, 30, 12);
		rt.body.mergeCell(31, 3, 31, 6);
		rt.body.mergeCell(31, 9, 31, 12);
		rt.body.mergeCell(32, 3, 32, 6);
		rt.body.mergeCell(32, 9, 32, 12);
		rt.body.mergeCell(33, 1, 33, 6);
		rt.body.mergeCell(33, 7, 33, 12);
		
//		rt.body.setCells(6, 3, 12, 6, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(5, 3, 12, 6, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(13, 3, 13, 6, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
//		rt.body.setCells(6, 9, 12, 11, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(5, 9, 12, 12, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(13, 9, 13, 12, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
//		rt.body.setCells(16, 3, 21, 6, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(15, 3, 21, 6, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(22, 3, 22, 6, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setCells(23, 3, 23, 6, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
//		rt.body.setCells(16, 9, 22, 11, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(15, 9, 22, 12, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(23, 9, 23, 12, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
		
//		rt.body.setCells(28, 3, 29, 6, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(27, 3, 29, 6, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(28, 3, 29, 6, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
		rt.body.setCells(25, 3, 30, 6, Table.PER_ALIGN, Table.ALIGN_RIGHT);
//		rt.body.setCells(26, 9, 28, 11, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(25, 9, 28, 12, Table.PER_ALIGN, Table.ALIGN_RIGHT);
	//	rt.body.setCells(26, 9, 28, 11, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
		//-----------
		rt.body.setCells(30, 3, 30, 6, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setCells(30, 9, 30, 12, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setCells(29, 3, 29, 6, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setCells(29, 9, 29, 12, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
		rt.body.setCells(31, 3, 31, 6, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setCells(31, 9, 31, 12, Table.PER_ALIGN, Table.ALIGN_CENTER);
		return rt.getHtml();
	
	}

	// ��ť�ļ����¼�
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	//ȷ��
	private boolean _SaveButton=false;
	
	public void SaveButton(IRequestCycle cycle){
		_SaveButton=true;
	}
	//����
	private boolean _CancelButton=false;
	
	public void CancelButton(IRequestCycle cycle){
		_CancelButton=true;
	}
	// ������ť�ύ����
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if(_SaveButton){
			_SaveButton=false;
			this.BT_Commit();
		}
		if(_CancelButton){
			_CancelButton=false;
			this.BT_Cancel();
		}
	}
	
	//ȷ��
	private void BT_Commit(){
		JDBCcon con=new JDBCcon();
		Visit visit=(Visit)this.getPage().getVisit();
		String diancxxb_id=visit.getDiancxxb_id()+"";
		String huayy=visit.getRenymc();
		StringBuffer bf=new StringBuffer(" begin \n");
		
		
		//ȡ��������һλ,����Ƿ�����ĸC������ĸX
		String Ruchybm=this.getBianmValue().getValue().substring(0,1);
		
		boolean ChoucyBm = MainGlobal.getXitxx_item("����", "�볧����������ĸC��ͷ�Ƿ��ǳ����", 
				String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()),
				"��").equals("��");
		boolean KuangcyBm = MainGlobal.getXitxx_item("����", "�볧����������ĸX��ͷ�Ƿ��ǿ����", 
				String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()),
				"��").equals("��");
		boolean HuocyBm = MainGlobal.getXitxx_item("����", "�볧����������ĸH��ͷ�Ƿ��ǻ�����", 
				String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()),
				"��").equals("��");
		
		bf.append("   update  huaygyfxb  set shenhzt=1  where shenhzt=2 and bianm='").append(this.getBianmValue().getValue()).append("' \n").append(" and diancxxb_id=").append(diancxxb_id).append(";\n");
		bf.append("   update huaylfb set shenhzt=1  where shenhzt=2 and bianm='").append(this.getBianmValue().getValue()).append("' \n").append(" and diancxxb_id=").append(diancxxb_id).append(";\n");
		bf.append("   update huayrlb set shenhzt=1  where shenhzt=2 and bianm='").append(this.getBianmValue().getValue()).append("' \n").append(" and diancxxb_id=").append(diancxxb_id).append(";\n");

		//����������ĸC��ͷ,�����ǳ����,����choucyb��
		if(ChoucyBm&&Ruchybm.equals("C")){
			bf.append("   update choucyb set shenhzt=3 \n")
			.append(" ,huaysj=").append(DateUtil.FormatOracleDate(getEriq())).append("\n")
			.append(" ,QNET_AR=").append(this.getQnetar_v()).append(" \n")
			.append(" ,AAR=").append(this.getAar_v()).append(" \n")
			.append(" ,AD=").append(this.getAd_v()).append(" \n")
			.append(" ,VDAF=").append(this.getVdaf_v()).append(" \n")
			.append(" ,MT=").append(this.getMt_v()).append("\n")
			.append(" ,STAD=").append(this.getStad_v()).append(" \n")
			.append(" ,AAD=").append(this.getAad_v()).append(" \n")
			.append(" ,MAD=").append(this.getMad_v()).append(" \n")
			.append(" ,QBAD=").append(this.getQbad_v()).append(" \n")
			.append(" ,HAD=").append(this.getHad_v()).append(" \n")
			.append(" ,VAD=").append(this.getVad_v()).append(" \n")
			.append(" ,FCAD=").append(this.getFcad_v()).append(" \n")
			.append(" ,STD=").append(this.getStd_v()).append(" \n")
			.append(" ,QGRAD=").append(this.getQgrad_v()).append(" \n")
			.append(" ,HDAF=").append(this.getHdaf_v()).append(" \n")
			.append(" ,QGRD=").append(this.getQgrd_v()).append(" \n")
			.append(" ,SDAF=").append(this.getSdaf_v(this.getStd_v(),this.getStd_v())).append(" \n")
			.append(" ,QGRAD_DAF=").append(this.getQgrad_daf_v(this.getQgrad_v(),this.getAad_v(),this.getMad_v())).append(" \n")
			.append(" ,HAR=").append(this.getHar_v(this.getHad_v(),this.getMt_v(),this.getMad_v())).append(" \n")
			.append(" ,VAR=").append(this.getVar_v()).append(" \n")
			.append(" ,QBRAD=").append(this.getQbrad_v()).append(" \n")
			.append(" ,huayy='").append(huayy).append("' \n")
			.append(" ,lury='").append(huayy).append("' \n")
			.append(" where id in (")
			//max(c.id),��Ϊ�˷�ֹ���������ظ�,�ظ�ʱȡid���ı��,Ҳ������������ӵı��.
			.append(" select max(c.id) from choucyb c where c.huaybh='"+this.getBianmValue().getValue()+"'  \n")
			.append(" ); \n");
			
		}else if(KuangcyBm&&Ruchybm.equals("X")){//����������ĸX��ͷ,�����ǿ����,����kuangcyb��
			
			bf.append("   update kuangcyb set shenhzt=3 \n")
			.append(" ,huaysj=").append(DateUtil.FormatOracleDate(getEriq())).append("\n")
			.append(" ,QNET_AR=").append(this.getQnetar_v()).append(" \n")
			.append(" ,AAR=").append(this.getAar_v()).append(" \n")
			.append(" ,AD=").append(this.getAd_v()).append(" \n")
			.append(" ,VDAF=").append(this.getVdaf_v()).append(" \n")
			.append(" ,MT=").append(this.getMt_v()).append("\n")
			.append(" ,STAD=").append(this.getStad_v()).append(" \n")
			.append(" ,AAD=").append(this.getAad_v()).append(" \n")
			.append(" ,MAD=").append(this.getMad_v()).append(" \n")
			.append(" ,QBAD=").append(this.getQbad_v()).append(" \n")
			.append(" ,HAD=").append(this.getHad_v()).append(" \n")
			.append(" ,VAD=").append(this.getVad_v()).append(" \n")
			.append(" ,FCAD=").append(this.getFcad_v()).append(" \n")
			.append(" ,STD=").append(this.getStd_v()).append(" \n")
			.append(" ,QGRAD=").append(this.getQgrad_v()).append(" \n")
			.append(" ,HDAF=").append(this.getHdaf_v()).append(" \n")
			.append(" ,QGRD=").append(this.getQgrd_v()).append(" \n")
			.append(" ,SDAF=").append(this.getSdaf_v(this.getStd_v(),this.getStd_v())).append(" \n")
			.append(" ,QGRAD_DAF=").append(this.getQgrad_daf_v(this.getQgrad_v(),this.getAad_v(),this.getMad_v())).append(" \n")
			.append(" ,HAR=").append(this.getHar_v(this.getHad_v(),this.getMt_v(),this.getMad_v())).append(" \n")
			.append(" ,VAR=").append(this.getVar_v()).append(" \n")
			.append(" ,QBRAD=").append(this.getQbrad_v()).append(" \n")
			.append(" ,huayy='").append(huayy).append("' \n")
			.append(" ,lury='").append(huayy).append("' \n")
			.append(" where id in (")
			//max(c.id),��Ϊ�˷�ֹ����ظ�,������¶�������.
			.append(" select max(c.id) from kuangcyb c where c.huaybh='"+this.getBianmValue().getValue()+"'  \n")
			.append(" ); \n");
			
			
			
		}else if(HuocyBm&&Ruchybm.equals("H")){//����������ĸH��ͷ,�����ǻ�����,����meicyb��
			
			bf.append("   update meicyb set shenhzt=3 \n")
			.append(" ,huaysj=").append(DateUtil.FormatOracleDate(getEriq())).append("\n")
			.append(" ,QNET_AR=").append(this.getQnetar_v()).append(" \n")
			.append(" ,AAR=").append(this.getAar_v()).append(" \n")
			.append(" ,AD=").append(this.getAd_v()).append(" \n")
			.append(" ,VDAF=").append(this.getVdaf_v()).append(" \n")
			.append(" ,MT=").append(this.getMt_v()).append("\n")
			.append(" ,STAD=").append(this.getStad_v()).append(" \n")
			.append(" ,AAD=").append(this.getAad_v()).append(" \n")
			.append(" ,MAD=").append(this.getMad_v()).append(" \n")
			.append(" ,QBAD=").append(this.getQbad_v()).append(" \n")
			.append(" ,HAD=").append(this.getHad_v()).append(" \n")
			.append(" ,VAD=").append(this.getVad_v()).append(" \n")
			.append(" ,FCAD=").append(this.getFcad_v()).append(" \n")
			.append(" ,STD=").append(this.getStd_v()).append(" \n")
			.append(" ,QGRAD=").append(this.getQgrad_v()).append(" \n")
			.append(" ,HDAF=").append(this.getHdaf_v()).append(" \n")
			.append(" ,QGRD=").append(this.getQgrd_v()).append(" \n")
			.append(" ,SDAF=").append(this.getSdaf_v(this.getStd_v(),this.getStd_v())).append(" \n")
			.append(" ,QGRAD_DAF=").append(this.getQgrad_daf_v(this.getQgrad_v(),this.getAad_v(),this.getMad_v())).append(" \n")
			.append(" ,HAR=").append(this.getHar_v(this.getHad_v(),this.getMt_v(),this.getMad_v())).append(" \n")
			.append(" ,VAR=").append(this.getVar_v()).append(" \n")
			.append(" ,QBRAD=").append(this.getQbrad_v()).append(" \n")
			.append(" ,huayy='").append(huayy).append("' \n")
			.append(" ,lury='").append(huayy).append("' \n")
			.append(" where id in (")
			//max(c.id),��Ϊ�˷�ֹ����ظ�,������¶�������.
			.append(" select max(c.id) from meicyb c where c.huaybh='"+this.getBianmValue().getValue()+"'  \n")
			.append(" ); \n");
			
			
			
		}else{//�������볧��
			 bf.append("   update zhillsb set shenhzt=3 \n")
			.append(" ,huaysj=").append(DateUtil.FormatOracleDate(getEriq())).append("\n")
			.append(" ,QNET_AR=").append(this.getQnetar_v()).append(" \n")
			.append(" ,AAR=").append(this.getAar_v()).append(" \n")
			.append(" ,AD=").append(this.getAd_v()).append(" \n")
			.append(" ,VDAF=").append(this.getVdaf_v()).append(" \n")
			.append(" ,MT=").append(this.getMt_v()).append("\n")
			.append(" ,STAD=").append(this.getStad_v()).append(" \n")
			.append(" ,AAD=").append(this.getAad_v()).append(" \n")
			.append(" ,MAD=").append(this.getMad_v()).append(" \n")
			.append(" ,QBAD=").append(this.getQbad_v()).append(" \n")
			.append(" ,HAD=").append(this.getHad_v()).append(" \n")
			.append(" ,VAD=").append(this.getVad_v()).append(" \n")
			.append(" ,FCAD=").append(this.getFcad_v()).append(" \n")
			.append(" ,STD=").append(this.getStd_v()).append(" \n")
			.append(" ,QGRAD=").append(this.getQgrad_v()).append(" \n")
			.append(" ,HDAF=").append(this.getHdaf_v()).append(" \n")
			.append(" ,QGRD=").append(this.getQgrd_v()).append(" \n")
			.append(" ,SDAF=").append(this.getSdaf_v(this.getStd_v(),this.getStd_v())).append(" \n")
			.append(" ,QGRAD_DAF=").append(this.getQgrad_daf_v(this.getQgrad_v(),this.getAad_v(),this.getMad_v())).append(" \n")
			.append(" ,HAR=").append(this.getHar_v(this.getHad_v(),this.getMt_v(),this.getMad_v())).append(" \n")
			.append(" ,VAR=").append(this.getVar_v()).append(" \n")
			.append(" ,QBRAD=").append(this.getQbrad_v()).append(" \n")
			.append(" ,huayy='").append(huayy).append("' \n")
			.append(" ,lury='").append(huayy).append("' \n")
			.append(" where id in (")
			//max(z.id),��Ϊ�˷�ֹ����ظ�,�������ǰ�ı��һ�����
			.append(" select max(z.id) from zhillsb z, zhuanmb m, zhuanmlb l \n")
			.append(" where z.id = m.zhillsb_id and m.zhuanmlb_id = l.id \n")
			.append(" and l.mingc = '"+huaybm+"' and m.bianm='").append(this.getBianmValue().getValue()).append("'")
			.append(" union select id from zhillsb where beiz='").append(this.getBianmValue().getValue()).append("'")
			.append(" ); \n");
		}
		
		
		
	bf.append(" end; ");
		int a=con.getUpdate(bf.toString());
		if(a==-1){
			this.setMsg("���ݲ���ʧ��!");
		}else{
			//������˳ɹ�,ˢ�±��������,ʹ�Ѿ���˹��ı�Ŵ�����������ʧ.
			this.getBianmModels();
			this.setMsg("���ݲ����ɹ�!");
		}
		
	}
	//ȡ��
	private void BT_Cancel(){
		JDBCcon con=new JDBCcon();
		Visit visit=(Visit)this.getPage().getVisit();
		ResultSet rs=null;
		String diancxxb_id=visit.getDiancxxb_id()+"";
		try{
			//��max(id),ֻ������������Ļ�����,��ֹ�������ظ�.
			String sql="select shenhzt  from zhillsb where id in ("
				+ " select max(z.id) from zhillsb z, zhuanmb m, zhuanmlb l \n"
				+ " where z.id = m.zhillsb_id and m.zhuanmlb_id = l.id \n"
				+" and l.mingc = '"+huaybm+"' and m.bianm='"+this.getBianmValue().getValue()+"')";
			rs=con.getResultSet(sql);
			if (rs.next()){
				if (rs.getInt("shenhzt")==7){
					this.setMsg("�����Ѿ�������ˣ�����������!");
					return;
				}
			}
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
		StringBuffer bf=new StringBuffer(" begin \n");
		
		bf.append("   update  huaygyfxb  set shenhzt=2  where shenhzt=1 and bianm='").append(this.getBianmValue().getValue()).append("' \n").append(" and diancxxb_id=").append(diancxxb_id).append(";\n");
		bf.append("   update huaylfb set shenhzt=2  where shenhzt=1 and bianm='").append(this.getBianmValue().getValue()).append("' \n").append(" and diancxxb_id=").append(diancxxb_id).append(";\n");
		bf.append("   update huayrlb set shenhzt=2  where shenhzt=1 and bianm='").append(this.getBianmValue().getValue()).append("' \n").append(" and diancxxb_id=").append(diancxxb_id).append(";\n");
//		bf.append("   update zhillsb set shenhzt=1  where bianm='").append(this.getBianmValue().getValue()).append("' \n").append(" and diancxxb_id=").append(diancxxb_id).append(";\n");
		bf.append("   update zhillsb set shenhzt=0 \n")
		.append(" ,QNET_AR=").append("''").append(" \n")
		.append(" ,AAR=").append("''").append(" \n")
		.append(" ,AD=").append("''").append(" \n")
		.append(" ,VDAF=").append("''").append(" \n")
		.append(" ,MT=").append("''").append("\n")
		.append(" ,STAD=").append("''").append(" \n")
		.append(" ,AAD=").append("''").append(" \n")
		.append(" ,MAD=").append("''").append(" \n")
		.append(" ,QBAD=").append("''").append(" \n")
		.append(" ,HAD=").append("''").append(" \n")
		.append(" ,VAD=").append("''").append(" \n")
		.append(" ,FCAD=").append("''").append(" \n")
		.append(" ,STD=").append("''").append(" \n")
		.append(" ,QGRAD=").append("''").append(" \n")
		.append(" ,HDAF=").append("''").append(" \n")
		.append(" ,QGRD=").append("''").append(" \n")
		.append(" ,SDAF=").append("''").append(" \n")
		.append(" ,QGRAD_DAF=").append("''").append(" \n")
		.append(" ,HAR=").append("''").append(" \n")
		.append(" ,VAR=").append("''").append(" \n")
		.append(" ,QBRAD=").append("''").append(" \n")
		.append(" where id in (")
		//max(z.id),��ֹ�������ظ�,����ʷ����ͬ�Ļ�����,һ����.
		.append(" select max(z.id) from zhillsb z, zhuanmb m, zhuanmlb l \n")
		.append(" where z.id = m.zhillsb_id and m.zhuanmlb_id = l.id \n")
		.append(" and l.mingc = '"+huaybm+"' and m.bianm='").append(this.getBianmValue().getValue()).append("'")
		.append(" union select id from zhillsb where beiz='").append(this.getBianmValue().getValue()).append("'")
		.append(" );\n");
		
		bf.append(" end; ");
		int a=con.getUpdate(bf.toString());
		if(a==-1){
			this.setMsg("���ݲ���ʧ��!");
		}else{
			this.setMsg("���ݲ����ɹ�!");
		}
		
	}
	//ȡ�� ���� ��� �� �볧������   �볧������ �� zhi  
	private  String getYuansSign(JDBCcon con, String Yuans, String diancxxb_id){
		String sign ="";
		Visit visit=(Visit)this.getPage().getVisit();
		String s="";
		if(visit.getString12().equals("RC")){
			s="�볧";
		}else{
			s="��¯";
		}
		String sql = "select zhi from xitxxb where mingc = '"+s+"����"+Yuans+"'\n"
		+ " and zhuangt = 1 and diancxxb_id = " + diancxxb_id+" and leib='"+s+"'";
		
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			sign = rsl.getString("zhi");
		}
		rsl.close();
		return sign;
	}
	//���� xitxxb �� �� �� �� ��zhi��ȡ�� yuansfxb�ж�Ӧ��zhi
	private String getyuansfxb(JDBCcon con, String Yuans, String diancxxb_id){
		
		
		//ȡ��������һλ,����Ƿ�����ĸC������ĸX
		String Ruchybm=this.getBianmValue().getValue().substring(0,1);
		
		boolean ChoucyBm = MainGlobal.getXitxx_item("����", "�볧����������ĸC��ͷ�Ƿ��ǳ����", 
				String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()),
				"��").equals("��");
		boolean KuangcyBm = MainGlobal.getXitxx_item("����", "�볧����������ĸX��ͷ�Ƿ��ǿ����", 
				String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()),
				"��").equals("��");
		boolean HuocyBm = MainGlobal.getXitxx_item("����", "�볧����������ĸH��ͷ�Ƿ��ǻ�����", 
				String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()),
				"��").equals("��");
		//�жϵ�ǰ�����Ƿ��Ѿ�����˹���  �����˹�����ֵ��zhillsb��ȥ
		String shenhzt=this.getShenhzt(con);
		String zhi="";
		String sign=this.getYuansSign(con, Yuans, diancxxb_id);
		String sql = "";
		
		if(ChoucyBm&&Ruchybm.equals("C")){//  �����糧 ���������
			sql=
				"select fx.zhi\n" +
				"   from yuansfxb fx,\n" + 
				"        yuansxmb fs,\n" + 
				"        (select c.meikxxb_id,c.caiysj as daohrq\n" + 
				"         from choucyb c\n" + 
				"         where c.huaybh='"+this.getBianmValue().getValue()+"') meid\n" + 
				"          where fx.yuansxmb_id = fs.id\n" + 
				"           and fx.zhi != 0\n" + 
				"           and fs.zhuangt = 1\n" + 
				"           and fx.zhuangt = 1\n" + 
				"           and fs.mingc = '"+sign+"'\n" + 
				"           and meid.meikxxb_id = fx.meikxxb_id\n" + 
				"           and meid.daohrq >=fx.riq\n" + 
				"           order by  meid.daohrq desc";

			
			
		}else if(KuangcyBm&&Ruchybm.equals("X")){//�����糧  ���������
			
			sql=
				"select fx.zhi\n" +
				"   from yuansfxb fx,\n" + 
				"        yuansxmb fs,\n" + 
				"        (select c.meikxxb_id,c.caiysj as daohrq\n" + 
				"         from kuangcyb c\n" + 
				"         where c.huaybh='"+this.getBianmValue().getValue()+"') meid\n" + 
				"          where fx.yuansxmb_id = fs.id\n" + 
				"           and fx.zhi != 0\n" + 
				"           and fs.zhuangt = 1\n" + 
				"           and fx.zhuangt = 1\n" + 
				"           and fs.mingc = '"+sign+"'\n" + 
				"           and meid.meikxxb_id = fx.meikxxb_id\n" + 
				"           and meid.daohrq >=fx.riq\n" + 
				"           order by  meid.daohrq desc";

			
			
		}else if(HuocyBm&&Ruchybm.equals("H")){//�����糧  ���������
			
			/*sql=
				"select fx.zhi\n" +
				"   from yuansfxb fx,\n" + 
				"        yuansxmb fs,\n" + 
				"        (select c.meikxxb_id,c.caiysj as daohrq\n" + 
				"         from meicyb c\n" + 
				"         where c.huaybh='"+this.getBianmValue().getValue()+"') meid\n" + 
				"          where fx.yuansxmb_id = fs.id\n" + 
				"           and fx.zhi != 0\n" + 
				"           and fs.zhuangt = 1\n" + 
				"           and fx.zhuangt = 1\n" + 
				"           and fs.mingc = '"+sign+"'\n" + 
				"           and meid.meikxxb_id = fx.meikxxb_id\n" + 
				"           and meid.daohrq >=fx.riq\n" + 
				"           order by  meid.daohrq desc";*/
			
			sql="select zhi from xitxxb x where x.mingc='��������ú����Ĭ����ֵ' and leib='����' and zhuangt=1 ";

			
			
		}else if(shenhzt.equals("1")){//����1��ʱ�����Ѿ���˹���
			sql = 
				"select ls.had as zhi\n" +
				"                              from zhuanmb zm, zhuanmlb lb, zhillsb ls\n" + 
				"                             where zm.zhuanmlb_id = lb.id\n" + 
				"                               and lb.mingc = '�������'\n" + 
				"                               and ls.id = zm.zhillsb_id\n" + 
				"                               and zm.bianm = '"+this.getBianmValue().getValue()+"' order by ls.id desc ";

		}else{//�����볧������
			 sql=
				"select fx.zhi\n" +
				"   from yuansfxb fx,\n" + 
				"        yuansxmb fs,\n" + 
				"        (select meikxxb_id, daohrq\n" + 
				"          from fahb\n" + 
				"         where zhilb_id in (select ls.zhilb_id\n" + 
				"                              from zhuanmb zm, zhuanmlb lb, zhillsb ls\n" + 
				"                             where zm.zhuanmlb_id = lb.id\n" + 
				"                               and lb.mingc = '�������'\n" + 
				"                               and ls.id = zm.zhillsb_id\n" + 
				"                               and zm.bianm = '"+this.getBianmValue().getValue()+"')) meid\n" + 
				"\n" + 
				"         where fx.yuansxmb_id = fs.id\n" + 
				"           and fx.zhi != 0\n" + 
				"           and fs.zhuangt = 1\n" + 
				"           and fx.zhuangt = 1\n" + 
				"           and fs.mingc = '"+sign+"'\n" + 
				"           and meid.meikxxb_id = fx.meikxxb_id\n" + 
				"           and meid.daohrq >=fx.riq\n" + 
				"           order by meid.daohrq  desc";
			 //ȡֵʱ���յ������� ��������,����������ֻ������ظ�������,ȡֵʱ����ȡ��.
		}
		
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			zhi=rsl.getString("zhi");
		}
		rsl.close();
		return zhi;
	
	}

	private String getShenhzt(JDBCcon con){//�õ� ���� ��Ӧ��¼�� ���״̬
		String zhuangt="";
		String sql=" select h.shenhzt from huaygyfxb h where  h.bianm='"+this.getBianmValue().getValue()+"'" 
		+"  and h.riq>=to_date('"+this.getRiq()+"','yyyy-mm-dd')-7  \n"
		+"  and h.riq<=to_date('"+this.getEriq()+"','yyyy-mm-dd')+7";
		//wzb 2010-9-4 17:36  ������������
		ResultSetList rsl=con.getResultSetList(sql);
		
		if(rsl.next()){
			zhuangt=rsl.getString("shenhzt");
		}
		rsl.close();
		return zhuangt;
	}
	
	//�� ��Դ
	private void setLiuly(String liuly){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setString7(liuly);
	}
	private String getLiuly(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getString7();
	}
	//�� ��Դ
	private void setQingly(String qingly){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setString8(qingly);
	}
	private String getQingly(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getString8();
	}
	
	//  Ԫ�ط���ʱ��������� �Ƿ���ȷ
	private void setLiupz(boolean t){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setboolean5(t);
	}
	
	private boolean getLiupz(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getboolean5();
	}
	
	//Ԫ�ط���ʱ  ��������Ƿ���ȷ
	
	private void setQingpz(boolean t){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setboolean6(t);
	}
	
	private boolean getQingpz(){
		Visit visit=(Visit)this.getPage().getVisit();
		return visit.getboolean6();
	}
	
	//Ԫ�ط���ʱ��������ȷ�����ֵ
	private void setLiujz(String s){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setString13(s);
	}
	
	private String getLiujz(){
		Visit visit=(Visit)this.getPage().getVisit();
		if(visit.getString13()==null){
			return "0";
		}
		return visit.getString13();
	}
	
	//Ԫ�ط���ʱ��������ȷ�����ֵ
	private void setQingjz(String s){
		Visit visit=(Visit)this.getPage().getVisit();
		visit.setString14(s);
	}
	
	private String getQingjz(){
		Visit visit=(Visit)this.getPage().getVisit();
		if(visit.getString14()==null){
			return "0";
		}
		return visit.getString14();
	}
	
	
	// ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		JDBCcon con=new JDBCcon();
		String liuly=this.getLiuly(con);//�������Դ
		
		
		String qingly=this.getQingly(con);//�������Դ
		
		
		String diancxxb_id=visit.getDiancxxb_id()+"";
		
		//��ϵͳû������ ���� Ϊ��ʱ  ����Ĭ��ֵ
		if(liuly.equals("")){ 
			liuly=caijsj;
		}
		if(qingly.equals("")){
			qingly=zidjs;
		}
		
		this.setLiuly(liuly);
		this.setQingly(qingly);
		
		
		//visit.setboolean5(true);
		// Ԫ�ط��� ��ϵͳ��ֵ  ������ȷ ����ȷ��ʾ
		this.setLiupz(true);
		//visit.setboolean6(true);
		// Ԫ�ط��� ��ϵͳ��ֵ  ������ȷ ����ȷ��ʾ
		this.setQingpz(true);
		
		
		if(!getBianmValue().getValue().equals("��ѡ��")){
		//���� ��Դ�� Ԫ�ط��� ʱ  Ҫ ͨ�� Ԫ�ط�����  �ж� �Ƿ��д�
			if(liuly.equals(yuansfx)){
				String zhi=this.getyuansfxb(con, _liu, diancxxb_id);
				
				if(zhi!=null && !zhi.equals("")){
				
					//visit.setString13(zhi);
					//��� ���ƽ��ֵ
					this.setLiujz(zhi);
					//visit.setboolean5(true);
					// ��ϵͳ��ֵ  ������ȷ ����ȷ��ʾ
					this.setLiupz(true);
				}else{
					//visit.setboolean5(false);
					this.setLiupz(false);
				}
			}
			if(qingly.equals(yuansfx)){
				String zhi=this.getyuansfxb(con, _qing, diancxxb_id);
				
				if(zhi!=null && !zhi.equals("")){
				
					//visit.setString14(zhi);
					//��� ���ƽ��ֵ
					this.setQingjz(zhi);
					//visit.setboolean6(true);
					// ��ϵͳ��ֵ  ������ȷ ����ȷ��ʾ
					this.setQingpz(true);
				}else{
					//visit.setboolean6(false);
					this.setQingpz(false);
				}
			}
		}
		
//		if (visit.isFencb()) {
//			tb1.addText(new ToolbarText("����:"));
//			ComboBox changbcb = new ComboBox();
//			changbcb.setTransform("ChangbSelect");
//			changbcb.setWidth(130);
//			changbcb
//					.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
//			tb1.addField(changbcb);
//			tb1.addText(new ToolbarText("-"));
//		}
		tb1.addText(new ToolbarText("����:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("huayrq");
		tb1.addField(df);
	
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("��:"));
		DateField df2 = new DateField();
		df2.setValue(getEriq());
		df2.Binding("ERIQ", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		df2.setId("huayrq2");
		tb1.addField(df2);
	
		tb1.addText(new ToolbarText("-"));
		
		
		tb1.addText(new ToolbarText("״̬:"));
		ComboBox GES = new ComboBox();
		GES.setTransform("ShenhztSelect");
		GES.setWidth(100);
		GES.setListeners("select:function(){document.getElementById('Mark_bh').value = 'true'; document.forms[0].submit();}");
		GES.setEditable(false);
		tb1.addField(GES);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("-"));
		
		if(visit.getString12().indexOf("RC")!=-1){  //�볧����  ���
			//�趨�����ſɱ༭,�����Զ�ˢ��
			tb1.addText(new ToolbarText("�������:"));
			ComboBox shij = new ComboBox();
			shij.setTransform("BianmSelect");
			shij.setEditable(true);
			shij.setWidth(130);
			shij.setListeners("select:function(own,rec,index){document.getElementById('Mark_bh').value = 'false'; Ext.getDom('BianmSelect').selectedIndex=index}");
			//shij.setListeners("select:function(){document.forms[0].submit();}");
			tb1.addField(shij);
		}else{//��¯���� ���
			
		}
		
		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "��ѯ",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		rbtn.setId("search_id");
		
		ToolbarButton rbtn1 = new ToolbarButton(null, "ȷ��",
		"function(){document.getElementById('SaveButton').click();}");
		rbtn1.setIcon(SysConstant.Btn_Icon_SelSubmit);
		rbtn1.setId("commit_id");
		
		ToolbarButton rbtn2 = new ToolbarButton(null, "����",
		"function(){document.getElementById('CancelButton').click();}");
		rbtn2.setIcon(SysConstant.Btn_Icon_Cancel);
		rbtn2.setId("cancel_id");
		
		if(!this.getLiupz() || !this.getQingpz()){// Ԫ�ط���  �� �� �� ��һ�� ���ò���ȷ  ��ť���� ����ʾ��Ϣ
		//	rbtn.setDisabled(true);
			rbtn1.setDisabled(true);
			rbtn2.setDisabled(true);
		}
		
		String shenhzt=this.getShenhzt(con);
		if(shenhzt.equals("1")){//ȷ�� ��ť ʧЧ
			rbtn1.setDisabled(true);
		}
		if(shenhzt.equals("2")){//���� ��ťʧЧ
			rbtn2.setDisabled(true);
		}
			
		con.Close();
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addItem(rbtn1);
		tb1.addItem(rbtn2);
		tb1.addFill();
		// tb1.addText(new ToolbarText("<marquee width=300
		// scrollamount=2></marquee>"));
		setToolbar(tb1);
	}

	// ������ʹ�õķ���
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		// if(getTbmsg()!=null) {
		// getToolbar().deleteItem();
		// getToolbar().addText(new ToolbarText("<marquee width=300
		// scrollamount=2>"+getTbmsg()+"</marquee>"));
		// }
		((DateField) getToolbar().getItem("huayrq")).setValue(getRiq());
		
		return getToolbar().getRenderScript();
	}
	
	

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ����ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			
			visit.setString12("RC");//�����ת������ҳ������  Ĭ����  �볡 ���� 
			
			String pageName=this.getPageName().toString();
			if(pageName!=null && pageName.toUpperCase().equals("RL")){
				visit.setString12(pageName);
			}
			
			
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			setEriq(DateUtil.FormatDate(new Date()));
			setChangbValue(null);
			setChangbModel(null);
			setBianmValue(null);
			setBianmModel(null);
			setShenhztModel(null);
			setShenhztValue(null);
			visit.setDouble1(0);
			visit.setDouble2(0);
			visit.setDouble3(0);
			visit.setDouble4(0);
			visit.setDouble5(0);
			visit.setDouble6(0);
			visit.setDouble7(0);
			visit.setDouble8(0);
			visit.setDouble9(0);
			visit.setDouble10(0);
			visit.setDouble11(0);
			visit.setDouble12(0);
			visit.setDouble13(0);
			visit.setDouble14(0);
			visit.setDouble15(0);
			visit.setDouble16(0);
			visit.setDouble17(0);
			visit.setDouble18(0);
			visit.setDouble19(0);
			visit.setDouble20(0);
			
			visit.setString13("");
			visit.setString14("");
			
			visit.setString7("");
			visit.setString8("");
			
			visit.setboolean5(false);
			visit.setboolean6(false);
			this.setMsg("");
			getSelectData();
			getHuaybgd();
			
		}
		if (riqchange) {
			riqchange = false;
			setBianmValue(null);
			setBianmModel(null);
			
		}
		if (getMarkbh().equals("true")) { // �ж����getMarkbh()����"true"����ô���³�ʼ�����������
			this.getBianmModels();
		}
		getSelectData();
		this.setMsg("");
	}

	// �����Ƿ�仯
	private boolean riqchange = false;

	// ������
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		if (this.riq != null) {
			if (!this.riq.equals(riq))
				riqchange = true;
		}
		this.riq = riq;
	}
	
	
//	 ������2
	private String eriq;

	public String getEriq() {
		return eriq;
	}

	public void setEriq(String eriq) {
		if (this.eriq != null) {
			if (!this.eriq.equals(eriq))
				riqchange = true;
		}
		this.eriq = eriq;
	}

	
	
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public IDropDownBean getBianmValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			if (getBianmModel().getOptionCount() > 0) {
				setBianmValue((IDropDownBean) getBianmModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setBianmValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(value);
	}

	public IPropertySelectionModel getBianmModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			getBianmModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setBianmModel(IPropertySelectionModel model) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(model);
	}
	
	private String get_Shenhzt(){
		String shenhzt = "";
		if(getShenhztValue().getValue().equals("δ���")){
			shenhzt = " and h.shenhzt = 2";
		}else if(getShenhztValue().getValue().equals("�����")){
			shenhzt = " and h.shenhzt = 1";
		}
		return shenhzt;
	}
	private void getBianmModels() {
		StringBuffer sb = new StringBuffer();
		Visit visit=(Visit)this.getPage().getVisit();

		sb.append(" select rownum as id,f.bianm  from (");
		sb.append(" select distinct h.bianm from huaygyfxb h where h.riq>=").append(DateUtil.FormatOracleDate(getRiq())).append("\n")
		.append(" and h.riq<=").append(DateUtil.FormatOracleDate(getEriq())).append("\n") 
		.append(" and h.diancxxb_id=").append(visit.getDiancxxb_id()).append("\n")
		  .append(" "+ get_Shenhzt()+"\n");
		sb.append(" ) f order by f.bianm\n");
			
		setBianmModel(new IDropDownModel(sb.toString(), "��ѡ��"));
	}

	// ����������
	public IDropDownBean getChangbValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
			if (getChangbModel().getOptionCount() > 0) {
				setChangbValue((IDropDownBean) getChangbModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean2();
	}

	public void setChangbValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean2(value);
	}

	public IPropertySelectionModel getChangbModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setChangbModel();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setChangbModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
	}

	public void setChangbModel() {
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if (visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="
					+ visit.getDiancxxb_id());
		} else {
			sb.append("select id,mingc from diancxxb where id="
					+ visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}

	public void pageValidate(PageEvent arg0) {
		// TODO �Զ����ɷ������
		
	}
	

	public IDropDownBean getShenhztValue() {											
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {										
			if (getShenhztModel().getOptionCount() > 0) {									
				setShenhztValue((IDropDownBean) getShenhztModel().getOption(0));								
			}else{									
				setShenhztValue(new IDropDownBean());								
			}									
		}										
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();										
	}											
												
	public void setShenhztValue(IDropDownBean value) {											
		((Visit) this.getPage().getVisit()).setDropDownBean3(value);										
	}											
												
	public IPropertySelectionModel getShenhztModel() {											
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {										
			setShenhztModels();									
		}										
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();										
	}											
												
	public void setShenhztModel(IPropertySelectionModel value) {											
		((Visit) this.getPage().getVisit()).setProSelectionModel3(value);										
	}											
												
	public void setShenhztModels() {											
		List riqlist = new ArrayList();										
		riqlist.add(new IDropDownBean(0,"δ���"));										
		riqlist.add(new IDropDownBean(1,"�����"));										
		setShenhztModel(new IDropDownModel(riqlist));										
	}	
}