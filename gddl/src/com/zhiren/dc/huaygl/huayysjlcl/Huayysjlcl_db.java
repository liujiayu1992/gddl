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
import com.zhiren.dc.huaygl.Judge;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
�йػ���ԭʼ�������Ϣ
1.	Ĭ�Ͻӿ�ԭʼ��Ŀ����״̬0
2.	ԭʼ��Ŀ��˺�2
3.	ԭʼ��Ŀ���Ϻ�Ϊ-1
4.	ԭʼ����ȷ�Ϻ�1
5.	ԭʼ���泷����2

 * 
 * ���ߣ�chh
 * ʱ�䣺2010-05-03
 * ����������ͨ���������ƣ������Զ���˵Ĺ��̣�������ԭʼ������Ŀ�е�ѡ��ѡȥ��˵Ļ���
 */
/*
 * 
 * 
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
public class Huayysjlcl_db extends BasePage implements PageValidateListener {

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
	
	private final static long SELECT_WEISH=1;//δ��ˣ��������е�ID
	private final static long SELECT_YISH=2;//�����
		
	public static final String Qbad = "Qbad";
	public static final String Stad = "Stad";
	public static final String Mt = "Mt";
	public static final String Mad = "Mad";
	public static final String Aad = "Aad";
	public static final String Vad = "Vad";
	
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
		
		String wend_shid=""; //�¶�ʪ��  Ϊ��
		String caiyl="";//������  Ϊ��
		String zhiyr="";//������  Ϊ��
		
		StringBuffer bf=new StringBuffer();
		bf.append(" select max(zhillsbid) as zhillsbid,sum(jingz) as jingz,bianh,kuangb,faz,pinz,huaysj,max(chec) as chec,max(caiysj) as caiysj,meikdm from ");
		bf.append(" (select z.id as zhillsbid,f.jingz,nvl(m.bianm,'') bianh,nvl(mk.mingc,'') kuangb,nvl(c.mingc,'') faz,nvl(p.mingc,'') pinz,nvl(to_char(z.huaysj,'yyyy-mm-dd'),'') huaysj,nvl(f.chec,'') chec,nvl(to_char(cai.caiyrq,'yyyy-mm-dd'), '') caiysj \n");
		bf.append(",decode(cai.meikdm,null,' ',cai.meikdm) as meikdm from meikxxb mk,chezxxb c,pinzb p,zhillsb z,  \n");
		bf.append(" (select distinct fa.diancxxb_id,fa.zhilb_id, fa.meikxxb_id,max(fa.chec) as chec,fa.faz_id,fa.pinzb_id,sum(maoz-piz) as jingz from fahb fa group by fa.diancxxb_id,fa.zhilb_id, fa.meikxxb_id,fa.faz_id,fa.pinzb_id) f, zhuanmb m, caiyb cai,zhuanmlb l \n");
		bf.append(" where f.zhilb_id=z.zhilb_id  and f.meikxxb_id=mk.id and cai.zhilb_id = z.zhilb_id \n");
		bf.append(" and f.faz_id=c.id and f.pinzb_id=p.id  \n");
		bf.append(" and z.id = m.zhillsb_id and m.zhuanmlb_id = l.id  and l.mingc = '"+huaybm+"' \n");
		bf.append(" and m.bianm='").append(this.getBianmValue().getValue()).append("' \n");
		bf.append(" and f.diancxxb_id=").append(visit.getDiancxxb_id()).append(" \n");
		bf.append(" union select z.id as zhillsbid,0 as jingz,z.beiz as bianh,decode(q.meikmc,null,'',q.meikmc) as kuangb,'' as faz,'' as pinz,nvl(to_char(z.huaysj,'yyyy-mm-dd'),'') huaysj,'' as chec,decode(q.caiyrq,null,'',to_char(q.caiyrq,'yyyy-mm-dd')) caiysj,decode(q.meikmc,null,'',q.meikmc) as meikdm\n");
		bf.append(" from zhillsb z,qitycyb q where z.id=q.zhillsb_id(+) and z.beiz='").append(this.getBianmValue().getValue()).append("'");
		bf.append(") group  by bianh,kuangb,faz,pinz,huaysj,meikdm");
		ResultSetList rsl=con.getResultSetList(bf.toString());
		
		if(rsl.next()){
//			�ж��Ƿ���ʾ�������ʾ����
			boolean isKuangb = MainGlobal.getXitxx_item("����", "���鱨�浥��ʾ���", 
					String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()),
					"��").equals("��");
						
			boolean isMeikdm = MainGlobal.getXitxx_item("����", "���鱨�浥��ʾ������", 
					String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()),
					"��").equals("��");
			if (isKuangb){
				ArrHeader[0][2] = rsl.getString("kuangb"); // ú��λ����
				ArrHeader[0][4] = rsl.getString("faz"); // ��վ(��)
			}else{
				if(isMeikdm){
					ArrHeader[0][2]=rsl.getString("meikdm");
						
				}else{
					ArrHeader[0][2] ="";
				}
				ArrHeader[0][4]="";
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
		
			ArrHeader[0][9] = String.valueOf(rsl.getDouble("jingz")); // ú��
			ArrHeader[1][2] = rsl.getString("pinz"); // Ʒ��
			ArrHeader[1][4] = this.getBianmValue().getValue(); // ���
			if (!rsl.getString("huaysj").equals("")){
				ArrHeader[1][7] = rsl.getString("huaysj"); // ��������
			}else{
				ArrHeader[1][7] = getRiq();
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
			
			ArrHeader[1][7] = caiyry; // ������Ա
			ArrHeader[2][2] = rsl.getString("chec"); // ����
			ArrHeader[2][4] = wend_shid; // ʪ��
			
			if(!rsl.getString("caiysj").equals("")){
				ArrHeader[2][7] = rsl.getString("caiysj"); // ����ʱ��
			}else{
				ArrHeader[2][7] = "";
			}
			
			ArrHeader[2][9] = zhiyry; // ������
		}
		
		if ("".equals(ArrHeader[1][4])) {
			ArrHeader[1][4] = this.getBianmValue().getValue(); // ���
		}
		
		if ("".equals(ArrHeader[1][7])) {
			ArrHeader[1][7] = this.getRiqEnd();
		}
		rsl.close();
		Table tb = new Table(1, 1);
		
		
		//  ��ҵ����  ȫˮ��
		bf.setLength(0);
		bf.append(" select h.huayz,h.cis,h.qimbh,h.qimzl,h.shenhy,h.huayy,\n");
		bf.append(" h.qimzl+h.meiyzl qishizl,h.meiyzl shiyzl,h.honghzzl,h.zongzl,\n");
		bf.append(" h.honghzzl1 jiancxsyhzzl,shenhzt,h.shebxh \n");
		bf.append(" from huaygyfxb h,item f,itemsort i \n");
		bf.append(" where h.fenxxmb_id=f.id and f.bianm='").append(Huayysjlsh.Mt).append("' \n");
		bf.append(" and h.bianm='").append(this.getBianmValue().getValue()).append("' \n");
		bf.append(" and h.diancxxb_id=").append(visit.getDiancxxb_id());
		bf.append("  and f.itemsortid=i.itemsortid and i.bianm='").append(huayysjl_bianm).append("' \n");
		
		//ww��������ж�
		bf.append(" and h.riq>=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')-2) and h.riq<=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')+2) \n");
		
		bf.append(" and h.shenhzt>=1 order by h.cis");
		rsl=con.getResultSetList(bf.toString());
	
		int count=rsl.getRows();//���ؼ�¼����  ƽ��ֵ��
		if (count>2) {
			this.setMsg("ȫˮ����ֵ����2��");
			return;
		}
		//rsl.beforefirst();
		if(count==0){//��ֵ   ��ʾ����
			if(this.getBianmValue().getId()!=-1 && this.getMsg().equals("")){//  �ų� ��ѡ��  ʱ  ��ʾ����Ϣ 
//				this.setMsg("��ȡȫˮ������ʧ�ܣ�����!");//���ð�ť
			}
			ToolbarButton tbn=(ToolbarButton)this.getToolbar().getItem("commit_id");
			tbn.setDisabled(true);
			
			ToolbarButton tbn1=(ToolbarButton)this.getToolbar().getItem("cancel_id");
			tbn1.setDisabled(true);
		}
		
		double quansf_sum=0;//ȫˮ�� ��ֵ
		String shenhry="";//�����Ա
		String huayry="";//������Ա
		int colsnum =1;
		while(rsl.next()){
			////rsl.getInt("cis")
			int initRow = 4;
			int initCol = 2 + colsnum;
			double ganzhjszl=0;//�����ú����������
			if(rsl.getString("shebxh").equals("SDTGA300") || rsl.getDouble("shiyzl")<11){
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
		
		ArrHeader[12][3] = tb.format(pingjz+"","0.0");
		
		ArrHeader[13][2]+=shenhry;
		ArrHeader[13][4]=huayry;
		
		rsl.close();
		
		
		
		//��ҵ���� ˮ��
		
		bf.setLength(0);
		bf.append(" select h.huayz,h.cis,h.qimbh,h.qimzl,h.shenhy,h.huayy,\n");
		bf.append(" h.qimzl+h.meiyzl qishizl,h.meiyzl shiyzl,h.honghzzl,h.zongzl,\n");
		bf.append(" h.honghzzl1 jiancxsyhzzl \n");
		bf.append(" from huaygyfxb h,item f,itemsort i \n");
		bf.append(" where h.fenxxmb_id=f.id and f.bianm='").append(Huayysjlsh.Mad).append("' \n");
		bf.append(" and h.bianm='").append(this.getBianmValue().getValue()).append("' \n");
		bf.append(" and h.diancxxb_id=").append(visit.getDiancxxb_id());
		bf.append("  and f.itemsortid=i.itemsortid and i.bianm='").append(huayysjl_bianm).append("' \n");
		
		//ww��������ж�
		bf.append(" and h.riq>=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')-2) and h.riq<=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')+2) \n");
		bf.append(" and  h.shenhzt>=1 order by h.cis");
		
		rsl=con.getResultSetList(bf.toString());
	
		count=rsl.getRows();//���ؼ�¼����  ƽ��ֵ��
		if (count>2) {
			this.setMsg("ˮ����ֵ����2��");
			return;
		}
		if(count==0){//��ֵ   ��ʾ����
			
			if(this.getBianmValue().getId()!=-1 && this.getMsg().equals("")){//  �ų� ��ѡ��  ʱ  ��ʾ����Ϣ 
//				this.setMsg("��ȡ�ոɻ�ˮ������ʧ�ܣ�����!");//���ð�ť
			}
			
			ToolbarButton tbn=(ToolbarButton)this.getToolbar().getItem("commit_id");
			tbn.setDisabled(true);
			
			ToolbarButton tbn1=(ToolbarButton)this.getToolbar().getItem("cancel_id");
			tbn1.setDisabled(true);
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
		ArrHeader[13][9]=huayry;
		
		rsl.close();
//��ҵ����  �ҷ�
		
		bf.setLength(0);
		bf.append(" select h.huayz,h.cis,h.qimbh,h.qimzl,h.shenhy,h.huayy,\n");
		bf.append(" h.qimzl+h.meiyzl qishizl,h.meiyzl shiyzl,h.honghzzl,h.zongzl,\n");
		bf.append(" h.honghzzl1 jiancxsyhzzl \n");
		bf.append(" from huaygyfxb h,item f,itemsort i \n");
		bf.append(" where h.fenxxmb_id=f.id and f.bianm='").append(Huayysjlsh.Aad).append("' \n");
		bf.append(" and h.bianm='").append(this.getBianmValue().getValue()).append("' \n");
		bf.append(" and h.diancxxb_id=").append(visit.getDiancxxb_id());
		bf.append("  and f.itemsortid=i.itemsortid and i.bianm='").append(huayysjl_bianm).append("' \n");
		
		//ww��������ж�
		bf.append(" and h.riq>=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')-2) and h.riq<=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')+2) \n");
		
		bf.append(" and h.shenhzt>=1 order by h.cis");
		
		rsl=con.getResultSetList(bf.toString());
		
		count=rsl.getRows();//���ؼ�¼����  ƽ��ֵ��
		if (count>2) {
			this.setMsg("�ҷ���ֵ����2��");
			return;
		}
		if(count==0){//��ֵ   ��ʾ����
			if(this.getBianmValue().getId()!=-1 && this.getMsg().equals("")){//  �ų� ��ѡ��  ʱ  ��ʾ����Ϣ 
//				this.setMsg("��ȡ�ҷ�����ʧ�ܣ�����!");//���ð�ť
			}
			ToolbarButton tbn=(ToolbarButton)this.getToolbar().getItem("commit_id");
			tbn.setDisabled(true);
			
			ToolbarButton tbn1=(ToolbarButton)this.getToolbar().getItem("cancel_id");
			tbn1.setDisabled(true);
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
		ArrHeader[23][9]=huayry;
		
		rsl.close();
		
		
		//��ҵ����  �ӷ���
		
		bf.setLength(0);
		bf.append(" select h.huayz,h.cis,h.qimbh,h.qimzl,h.shenhy,h.huayy,\n");
		bf.append(" h.qimzl+h.meiyzl qishizl,h.meiyzl shiyzl,h.honghzzl,h.zongzl,\n");
		bf.append(" h.honghzzl1 jiancxsyhzzl \n");
		bf.append(" from huaygyfxb h,item f,itemsort i \n");
		bf.append(" where h.fenxxmb_id=f.id and f.bianm='").append(Huayysjlsh.Vad).append("' \n");
		bf.append(" and h.bianm='").append(this.getBianmValue().getValue()).append("' \n");
		bf.append(" and h.diancxxb_id=").append(visit.getDiancxxb_id());
		bf.append("  and f.itemsortid=i.itemsortid and i.bianm='").append(huayysjl_bianm).append("' \n");
		//ww��������ж�
		bf.append(" and h.riq>=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')-2) and h.riq<=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')+2) \n");
		bf.append(" and h.shenhzt>=1 order by h.cis");
		
		rsl=con.getResultSetList(bf.toString());
		
		count=rsl.getRows();//���ؼ�¼����  ƽ��ֵ��
		if (count>2) {
			this.setMsg("�ӷ�����ֵ����2��");
			return;
		}
		if(count==0){//��ֵ   ��ʾ����
			if(this.getBianmValue().getId()!=-1 && this.getMsg().equals("")){//  �ų� ��ѡ��  ʱ  ��ʾ����Ϣ 
//				this.setMsg("��ȡ�ӷ�������ʧ�ܣ�����!");//���ð�ť
			}
			ToolbarButton tbn=(ToolbarButton)this.getToolbar().getItem("commit_id");
			tbn.setDisabled(true);
			
			ToolbarButton tbn1=(ToolbarButton)this.getToolbar().getItem("cancel_id");
			tbn1.setDisabled(true);
		}
		
		double huiff_sum=0;//�ӷ�����ֵ
				shenhry="";
				huayry="";
		colsnum =1;		
		while(rsl.next()){
			//int colsnum =count;// rsl.getInt("cis");
			int initRow = 14;
			int initCol = 2 + colsnum;
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
			
			//double quansMT=tb.Round_New(ganzhjszl/rsl.getDouble("shiyzl")*100-pingjz_shuif,2);//�ӷ���  -ˮ�ֵľ�ֵ
			double quansMT=tb.Round_New(rsl.getDouble("huayz"),2);//�ӷ���  -ˮ�ֵľ�ֵ
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
		
		
		ArrHeader[21][3] = tb.format(pingjz_huiff+"","0.00");
		
		double Vdaf_v=this.getVdaf_v(pingjz_huiff, pingjz_shuif,pingjz_huif);
		ArrHeader[22][3] = tb.format(Vdaf_v+"", "0.00");
//			
		ArrHeader[23][2]+=shenhry;
		ArrHeader[23][4]=huayry;
		
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
						ArrHeader[29][3]=tb.format(this.getLiujz(), "0.00");
						Stad_v=Double.valueOf(this.getLiujz()).doubleValue();
						Std_v=this.getStd_v(Double.valueOf(this.getLiujz()).doubleValue(), pingjz_shuif);
						ArrHeader[30][3]=tb.format(Std_v+"", "0.00");
					}
					
				}
				if(sign.toUpperCase().indexOf("STD")!=-1){//ϵͳ���õ��� std
					if(this.getBianmValue().getId()!=-1){
						Stad_v=this.getStad_v(Double.valueOf(this.getLiujz()).doubleValue(), pingjz_shuif);
						ArrHeader[29][3]=tb.format(Stad_v+"", "0.00");
						Std_v=Double.valueOf(this.getLiujz()).doubleValue();
						ArrHeader[30][3]=tb.format(this.getLiujz(), "0.00");
					}
					
				}
			}else{
				
			}
		}else{//�Զ�����  ȡֵ  ��ֵ
			
			
			bf.setLength(0);
			bf.append(" select h.cis,h.liuf, h.qimbh,h.qimzl,h.qimzl+h.meiyzl qishizl,\n");
			bf.append(" h.meiyzl shiyzl,h.shenhy,h.huayy \n");
			bf.append(" from huaylfb h,item f,itemsort i \n");
			bf.append(" where h.fenxxmb_id=f.id and f.bianm='").append(Huayysjlsh.Stad).append("' \n");
			bf.append(" and h.bianm='").append(this.getBianmValue().getValue()).append("' \n");
			bf.append(" and h.diancxxb_id=").append(visit.getDiancxxb_id());
			bf.append("  and f.itemsortid=i.itemsortid and i.bianm='").append(huayysjl_bianm).append("' \n");
			//ww��������ж�
			bf.append(" and h.riq>=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')-2) and h.riq<=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')+2) \n");
			bf.append(" and h.shenhzt>=1 order by h.cis");
			
			rsl=con.getResultSetList(bf.toString());
			
			count=rsl.getRows();//���ؼ�¼����  ƽ��ֵ��
			if (count>2) {
				this.setMsg("ȫ����ֵ����2��");
				return;
			}
			if(count==0){//��ֵ   ��ʾ����
				if(this.getBianmValue().getId()!=-1 && this.getMsg().equals("")){//  �ų� ��ѡ��  ʱ  ��ʾ����Ϣ 
//					this.setMsg("��ȡ�������ʧ�ܣ�����!");//���ð�ť
				}
				ToolbarButton tbn=(ToolbarButton)this.getToolbar().getItem("commit_id");
				tbn.setDisabled(true);
				
				ToolbarButton tbn1=(ToolbarButton)this.getToolbar().getItem("cancel_id");
				tbn1.setDisabled(true);
			}
			
			double quanl_sum=0;//ȫ����ֵ
					shenhry="";
					huayry="";
			colsnum =1;		
			while(rsl.next()){
				//int colsnum =count;// rsl.getInt("cis");
				int initRow = 24;
				int initCol = 2 + colsnum;
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
			ArrHeader[29][3] = tb.format(pingjz_quanl+"","0.00");
			Std_v=this.getStd_v(pingjz_quanl, pingjz_shuif);
			ArrHeader[30][3] = tb.format(this.getStd_v(pingjz_quanl, pingjz_shuif)+"","0.00");
			              
			ArrHeader[31][2]+=shenhry;
			ArrHeader[31][4]=huayry;
			
			rsl.close();
			
			
		}
		
		
		
		//������ 
	
		
		bf.setLength(0);
		bf.append(" select h.cis,h.qimzl,h.farl,h.meiyzl shiyzl,h.shenhy,h.huayy \n");
		bf.append(" from huayrlb h,item f,itemsort i  \n");
		bf.append(" where h.fenxxmb_id=f.id and f.bianm='").append(Huayysjlsh.Qbad).append("' \n");
		bf.append(" and h.bianm='").append(this.getBianmValue().getValue()).append("' \n");
		bf.append(" and h.diancxxb_id=").append(visit.getDiancxxb_id());
		bf.append("  and f.itemsortid=i.itemsortid and i.bianm='").append(huayysjl_bianm).append("' \n");
		//ww��������ж�
		bf.append(" and h.riq>=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')-2) and h.riq<=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')+2) \n");
		bf.append(" and h.shenhzt>=1 order by h.cis");
		
		rsl=con.getResultSetList(bf.toString());
		
		count=rsl.getRows();//���ؼ�¼����  ƽ��ֵ��
		if (count>2) {
			this.setMsg("��������ֵ����2��");
			return;
		}
		if(count==0){//��ֵ   ��ʾ����
			if(this.getBianmValue().getId()!=-1 && this.getMsg().equals("")){//  �ų� ��ѡ��  ʱ  ��ʾ����Ϣ 
//				this.setMsg("��ȡ��Ͳ������ʧ�ܣ�����!");//���ð�ť
			}
			ToolbarButton tbn=(ToolbarButton)this.getToolbar().getItem("commit_id");
			tbn.setDisabled(true);
			
			ToolbarButton tbn1=(ToolbarButton)this.getToolbar().getItem("cancel_id");
			tbn1.setDisabled(true);
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
			
			//ww 2010-05-11 
			//����Ǵ�Ԫ�ط�����ȡ���ݣ�����ú��ȡ���������ֵ 
			if(this.getQingly(con).equals(yuansfx)){
				
				ResultSetList rsHad = con.getResultSetList(
					"select ysf.zhi as zhi from yuansfxb ysf ,yuansxmb xm,\n" +
					"(SELECT meikxxb_id FROM yangpdhb WHERE zhilblsb_id ="+
					"(SELECT zhillsb_id FROM zhuanmb WHERE zhuanmlb_id=100663 AND bianm='" + this.getBianmValue().getValue() + "' AND ROWNUM=1)) m \n" +
					"where ysf.yuansxmb_id=xm.id\n" + 
					"and m.meikxxb_id = ysf.meikxxb_id\n" + 
					"and xm.mingc='" + sig_had + "'\n" + 
					"and ysf.zhuangt = 1\n" + 
					"AND ysf.riq< (\n" +
					"SELECT MAX(riq) AS riq FROM  huaygyfxb  WHERE  bianm= '" + this.getBianmValue().getValue() + "'\n" + 
					"--AND riq BETWEEN to_date('"+ this.getRiq() +"','yyyy-mm-dd') AND to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')\n" + 
					") \n" +
					"order by ysf.riq desc"
					);
				if (rsHad.next()) {
				//���¸�����ֵ
				this.setQingpz(true);
				this.setQingjz(rsHad.getString("zhi"));
				} else {
					//�����ֵ
//					this.setQingpz(false);
					this.setQingjz("0");
				}
			}
			
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
		ArrHeader[31][9]=huayry;
		
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
				ArrHeader[29][3]="";
			}else{
				Std_v=0;
				ArrHeader[30][3]= "";//Std
			}
		}
//		Vdaf_v=0;
//		ArrHeader[22][3] = "";//vdaf
		
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
				
				//2010-05-18  ww 
				//�볧��ȡԪ�ط���
				//�����������п����ֶ�ά����ֵ	
				if ("0".equals(this.getQingjz())) {
					try{
						sql = "select nvl(q.had,0) as had from zhillsb z,qitycyb q where z.id=q.zhillsb_id(+) and z.beiz='"+bianm+"'" 
							+ " and z.huaysj>=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')-2) and z.huaysj<=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')+2) \n";
						ResultSet rs=con.getResultSet(sql);
							if (rs.next()){
								had=tb.Round_New(rs.getDouble("had"),2);
								hdaf=tb.Round_New(had * 100 / (100 - Mad_v - Aad_v),2);
							} 
						}catch(Exception e){
							e.printStackTrace();
						}
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
			if (this.getMsg().equals("")){
				this.setMsg("Ԫ�ط�����Ŀ�����ֵ���ò���ȷ!");
			}
//			return "";
		}
		String rltitle1 = "��������+��������(g)";
		String rltitle2 = "��������(g)";
//		if (isRelbalance()) {
//			rltitle1 = "��ƽ ��������(g)";
//			rltitle2 = "�Զ� ��������(g)";
//		}
		
		String sp = "<br><br><br><br>";
		String ArrHeader[][] = new String[33][10];
		ArrHeader[0] = new String[] { "�� ��","�� ��", "", "��վ(��)", "",  "�ոɻ���","�ոɻ���","", "ú��(t)", "" };
		ArrHeader[1] = new String[] { "Ʒ ��", "Ʒ ��","", "�� ��", "",  "��������", "��������", "","������", ""};
		ArrHeader[2] = new String[] { "����/����", "����/����","", "�¶�ʪ��", "", "��������","��������", "","������", "",  };
		ArrHeader[3] = new String[] { "ȫ"+sp+"ˮ"+sp+"��"+sp+"Mt","", "", "��һ��", "�ڶ���",  "ˮ"+sp+"��"+sp+"Mad","","", "��һ��", "�ڶ���"};
		ArrHeader[4] = new String[] { "ȫ"+sp+"ˮ"+sp+"��"+sp+"Mt","����ƿ(��)��", "����ƿ(��)��", "", "", "ˮ"+sp+"��"+sp+"Mad", "����ƿ��","����ƿ��", "", ""};
		ArrHeader[5] = new String[] { "ȫ"+sp+"ˮ"+sp+"��"+sp+"Mt","����ƿ(��)����(g)", "����ƿ(��)����(g)", "", "", "ˮ"+sp+"��"+sp+"Mad", "����ƿ����(g)","����ƿ����(g)", "", ""};
		ArrHeader[6] = new String[] { "ȫ"+sp+"ˮ"+sp+"��"+sp+"Mt","����ƿ(��)����+��������(g)", "����ƿ(��)����+��������(g)", "", "", "ˮ"+sp+"��"+sp+"Mad", "����ƿ����+��������(g)","����ƿ����+��������(g)", "", ""};
		ArrHeader[7] = new String[] { "ȫ"+sp+"ˮ"+sp+"��"+sp+"Mt","��������m(g)", "��������m(g)", "", "", "ˮ"+sp+"��"+sp+"Mad", "��������m(g)", "��������m(g)", "", ""};
		ArrHeader[8] = new String[] { "ȫ"+sp+"ˮ"+sp+"��"+sp+"Mt","��ɺ�������(g)", "��ɺ�������(g)", "", "","ˮ"+sp+"��"+sp+"Mad", "��ɺ�������(g)","��ɺ�������(g)", "", "" };
		ArrHeader[9] = new String[] { "ȫ"+sp+"ˮ"+sp+"��"+sp+"Mt","����������������(g)", "����������������(g)", "", "","ˮ"+sp+"��"+sp+"Mad", "����������������(g)","����������������(g)", "", "" };
		ArrHeader[10] = new String[] { "ȫ"+sp+"ˮ"+sp+"��"+sp+"Mt","�����ú�����ٵ�����m1(g)", "�����ú�����ٵ�����m1(g)", "", "", "ˮ"+sp+"��"+sp+"Mad", "�����ú�����ٵ�����m1(g)","�����ú�����ٵ�����m1(g)", "", "" };
		ArrHeader[11] = new String[] { "ȫ"+sp+"ˮ"+sp+"��"+sp+"Mt","ȫˮ��Mt=m1/m*100(%)", "ȫˮ��Mt=m1/m*100(%)", "", "", "ˮ"+sp+"��"+sp+"Mad", "ˮ��Mad=m1/m*100(%)","ˮ��Mad=m1/m*100(%)", "", "" };
		ArrHeader[12] = new String[] { "ȫ"+sp+"ˮ"+sp+"��"+sp+"Mt","ƽ��ֵMt(%)", "ƽ��ֵMt(%)", "", "","ˮ"+sp+"��"+sp+"Mad", "ƽ��ֵMad(%)","ƽ��ֵMad(%)", "", "" };
		ArrHeader[13] = new String[] { "ȫ"+sp+"ˮ"+sp+"��"+sp+"Mt","���:", "���:", "����:", "����:", "ˮ"+sp+"��"+sp+"Madd", "���:","���:", "����:", "����:" };
		ArrHeader[14] = new String[] { "��"+sp+"��"+sp+"��"+sp+"Vad","������", "������", "", "", "��"+sp+"��"+sp+"Aad", "�����", "�����", "", ""};
		ArrHeader[15] = new String[] { "��"+sp+"��"+sp+"��"+sp+"Vad","��������(g)", "��������(g)", "", "", "��"+sp+"��"+sp+"Aad", "��������(g)","��������(g)", "", "" };
		ArrHeader[16] = new String[] { "��"+sp+"��"+sp+"��"+sp+"Vad","��������+��������(g)", "��������+��������(g)", "", "", "��"+sp+"��"+sp+"Aad", "��������+��������(g)","��������+��������(g)", "", ""};
		ArrHeader[17] = new String[] { "��"+sp+"��"+sp+"��"+sp+"Vad","��������m(g)", "��������m(g)", "", "", 	"��"+sp+"��"+sp+"Aad", "��������m(g)","��������m(g)", "", ""};
		ArrHeader[18] = new String[] { "��"+sp+"��"+sp+"��"+sp+"Vad","���Ⱥ�������(g)", "���Ⱥ�������(g)", "", "", "��"+sp+"��"+sp+"Aad", "���Ⱥ�������(g)","���Ⱥ�������(g)", "", ""};
		ArrHeader[19] = new String[] { "��"+sp+"��"+sp+"��"+sp+"Vad","ú�����Ⱥ���ٵ�����m1(g)", "ú�����Ⱥ���ٵ�����m1(g)", "", "", "��"+sp+"��"+sp+"Aad", "����������������(g)","����������������(g)", "", ""};
		ArrHeader[20] = new String[] { "��"+sp+"��"+sp+"��"+sp+"Vad","�ӷ���Vad=m1/m*100-Mad(%)", "�ӷ���Vad=m1/m*100-Mad(%)", "","", "��"+sp+"��"+sp+"Aad", "�����������m1(g)","�����������m1(g)", "", "" };
		ArrHeader[21] = new String[] { "��"+sp+"��"+sp+"��"+sp+"Vad","ƽ��ֵVad(%)", "ƽ��ֵVad(%)", "", "", "��"+sp+"��"+sp+"Aad", "�ҷ�Aad(%)","�ҷ�Aad(%)", "", "" };
		ArrHeader[22] = new String[] { "��"+sp+"��"+sp+"��"+sp+"Vad","ƽ��ֵVdaf(%)", "ƽ��ֵVdaf(%)", "", "", 	"��"+sp+"��"+sp+"Aad", "ƽ��ֵ(%)","ƽ��ֵ(%)", "", "" };
		ArrHeader[23] = new String[] { "��"+sp+"��"+sp+"��"+sp+"Vad","���:", "���:", "����:", "����:",  "��"+sp+"��"+sp+"Aad","���:", "���:", "����:", "����:" };
		ArrHeader[24] = new String[] { "ȫ"+sp+"��", "�����", "�����", "", "", "��"+sp+"��"+sp+"��","��������(g)","��������(g)", "", ""};
		ArrHeader[25] = new String[] { "ȫ"+sp+"��", "��������(g)","��������(g)", "", "", "��"+sp+"��"+sp+"��",rltitle1,rltitle1, "", ""};
		ArrHeader[26] = new String[] { "ȫ"+sp+"��", "��������+��������(g)","��������+��������(g)", "", "","��"+sp+"��"+sp+"��", rltitle2,rltitle2, "", ""};
		ArrHeader[27] = new String[] { "ȫ"+sp+"��", "��������(g)","��������(g)", "", "", "��"+sp+"��"+sp+"��","Qb,ad(J/g)","Qb,ad(J/g)", "", "" };
		ArrHeader[28] = new String[] { "ȫ"+sp+"��", "ȫ��St,ad(%)","ȫ��St,ad(%)", "", "", "��"+sp+"��"+sp+"��", "ƽ��ֵQb,ad(J/g)", "ƽ��ֵQb,ad(J/g)", "", "" };
		ArrHeader[29] = new String[] { "ȫ"+sp+"��", "ƽ��ֵSt,ad(%)","ƽ��ֵSt,ad(%)", "", "",	"��"+sp+"��"+sp+"��", "Qgr,ad(J/g)","Qgr,ad(J/g)", "", "" };
		ArrHeader[30] = new String[] { "ȫ"+sp+"��", "ƽ��ֵSt,d(%)","ƽ��ֵSt,d(%)", "", "", 	"��"+sp+"��"+sp+"��", "Qnet,ar(MJ/kg)","Qnet,ar(MJ/kg)", "", "" };
		ArrHeader[31] = new String[] { "ȫ"+sp+"��", "���:","���:", "����:", "����:","��"+sp+"��"+sp+"��", "���:", "���:", "����:", "����:" };
		ArrHeader[32] = new String[] { "ȫ<br>��<br>ԭ<br>ʼ<br>��<br>¼<br>��<br>ӡ<br>��", "", "", "", "", 
				"��<br>��<br>��<br>ԭ<br>ʼ<br>��<br>¼<br>��<br>ӡ<br>��", "", "", "", ""};
		int ArrWidth[] = new int[] { 30,40, 130, 70, 70, 30, 30, 130, 70, 70 };
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
		
		rt.setBody(new Table(33, 10));
		// rt.setDefaultTitleLeft("���Ƶ�λ��", 1);
		// rt.setDefaultTitleLeft("����", 1);
		// rt.setDefaultTitleRight("��ţ�", 1);
		//rt.setBody(new Table(ArrHeader, 0, false, Table.ALIGN_LEFT));
		rt.body.setWidth(ArrWidth);
		rt.body.setRowHeight(22);
		rt.body.setRowHeight(33, 200);
		
		
		//��ֵ
		
		for (int i = 0; i < 33; i++) {
			for (int j = 0; j < 10; j++) {
				if (ArrHeader[i][j] == null || ArrHeader[i][j].length() == 0) {
					ArrHeader[i][j] = "";
				}
				rt.body.setCellValue(i+1 , j+1 , ArrHeader[i][j]);
			}
		}
		
		// �ϲ���Ԫ��
		for (int i=1;i<4;i++) {
			rt.body.mergeCell(i, 1, i, 2);
			rt.body.mergeCell(i, 6, i, 7);
		}
		
		
		rt.body.mergeCell(4, 1, 14, 1);
		rt.body.mergeCell(15, 1, 24, 1);
		rt.body.mergeCell(25, 1, 32, 1);
		
		rt.body.mergeCell(4, 6, 14, 6);
		rt.body.mergeCell(15, 6, 24, 6);
		rt.body.mergeCell(25, 6, 32, 6);
		
		for (int i=4;i<=32;i++) {
			rt.body.mergeCell(i, 2, i, 3);
			rt.body.mergeCell(i, 7, i, 8);
		}
		
		rt.body.mergeCell(13, 4, 13, 5);
		rt.body.mergeCell(14, 4, 14, 5);
		rt.body.mergeCell(13, 9, 13, 10);
		rt.body.mergeCell(14, 9, 14, 10);
		
		rt.body.mergeCell(22, 4, 22, 5);
		rt.body.mergeCell(23, 4, 23, 5);
		rt.body.mergeCell(24, 4, 24, 5);	
		rt.body.mergeCell(23, 9, 23, 10);
		rt.body.mergeCell(24, 9, 24, 10);
		
		rt.body.mergeCell(30, 4, 30, 5);
		rt.body.mergeCell(31, 4, 31, 5);
		rt.body.mergeCell(32, 4, 32, 5);
		
		rt.body.mergeCell(29, 9, 29, 10);
		rt.body.mergeCell(30, 9, 30, 10);
		rt.body.mergeCell(31, 9, 31, 10);
		rt.body.mergeCell(32, 9, 32, 10);
		
		rt.body.mergeCell(33, 3, 33, 5);
		rt.body.mergeCell(33, 8, 33, 10);
		
		
		rt.body.setCellBorderLeft(33, 3, 0);
		rt.body.setCellBorderRight(33, 2, 0);
		rt.body.setCellBorderLeft(33, 8, 0);
		rt.body.setCellBorderRight(33, 7, 0);
		
		rt.body.setCells(1, 1, 4, 10, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		
//		rt.body.setCells(6, 3, 12, 6, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(5, 4, 12, 5, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(13, 4, 13, 5, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
//		rt.body.setCells(6, 9, 12, 11, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(5, 9, 12, 10, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(13, 9, 13, 10, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
//		rt.body.setCells(16, 3, 21, 6, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(15, 4, 21, 5, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(22, 4, 22, 5, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setCells(23, 4, 23, 5, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
//		rt.body.setCells(16, 9, 22, 11, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(15, 9, 22, 10, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(23, 9, 23, 10, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
		
//		rt.body.setCells(28, 3, 29, 6, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(27, 4, 29, 5, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(28, 4, 29, 5, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
		rt.body.setCells(25, 4, 30, 5, Table.PER_ALIGN, Table.ALIGN_RIGHT);
//		rt.body.setCells(26, 9, 28, 11, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(25, 9, 28, 10, Table.PER_ALIGN, Table.ALIGN_RIGHT);
	//	rt.body.setCells(26, 9, 28, 11, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
		//-----------
		rt.body.setCells(30, 4, 30, 5, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setCells(30, 9, 30, 10, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setCells(29, 4, 29, 5, Table.PER_ALIGN, Table.ALIGN_RIGHT);
		rt.body.setCells(29, 9, 29, 10, Table.PER_ALIGN, Table.ALIGN_CENTER);
		
		rt.body.setCells(31, 4, 31, 5, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setCells(31, 9, 31, 10, Table.PER_ALIGN, Table.ALIGN_CENTER);
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
	// ����ť�ύ����
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			
			getSelectData();
		}
		if(_SaveButton){
			_SaveButton=false;
			this.BT_Commit();
			this.setBianmValue(null);
			setBianmModels();
		}
		if(_CancelButton){
			_CancelButton=false;
			this.BT_Cancel();
			setBianmModels();
		}
	}
	
	//ȷ��
	private void BT_Commit(){
		JDBCcon con=new JDBCcon();
		Visit visit=(Visit)this.getPage().getVisit();
		String diancxxb_id=visit.getDiancxxb_id()+"";
		StringBuffer bf=new StringBuffer(" begin \n");
		
		bf.append("   update  huaygyfxb  set shenhzt=1  where shenhzt=2 and bianm='").append(this.getBianmValue().getValue()).append("' \n").append(" and diancxxb_id=").append(diancxxb_id).append(";\n");
		bf.append("   update huaylfb set shenhzt=1  where shenhzt=2 and bianm='").append(this.getBianmValue().getValue()).append("' \n").append(" and diancxxb_id=").append(diancxxb_id).append(";\n");
		bf.append("   update huayrlb set shenhzt=1  where shenhzt=2 and bianm='").append(this.getBianmValue().getValue()).append("' \n").append(" and diancxxb_id=").append(diancxxb_id).append(";\n");
//		bf.append("   update zhillsb set shenhzt=1  where bianm='").append(this.getBianmValue().getValue()).append("' \n").append(" and diancxxb_id=").append(diancxxb_id).append(";\n");
		bf.append("   update zhillsb set shenhzt=3 \n")
		.append(" ,huaysj=").append(DateUtil.FormatOracleDate(getRiq())).append("\n")
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
		.append(" where id in (")
		.append(" select z.id from zhillsb z, zhuanmb m, zhuanmlb l \n")
		.append(" where z.id = m.zhillsb_id and m.zhuanmlb_id = l.id \n")
		.append(" and l.mingc = '"+huaybm+"' and m.bianm='").append(this.getBianmValue().getValue()).append("'")
		.append(" union select id from zhillsb where beiz='").append(this.getBianmValue().getValue()).append("'")
		.append(" ); \n");

		bf.append(" end; ");
		int a=con.getUpdate(bf.toString());
		if(a==-1){
			this.setMsg("���ݲ���ʧ��!");
		}else{
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
			String sql="select shenhzt  from zhillsb where id in ("
				+ " select z.id from zhillsb z, zhuanmb m, zhuanmlb l \n"
				+ " where z.id = m.zhillsb_id and m.zhuanmlb_id = l.id \n"
				+" and l.mingc = '"+huaybm+"' and m.bianm='"+this.getBianmValue().getValue()+"')";
			rs=con.getResultSet(sql);
			if (rs.next()){
				if (rs.getInt("shenhzt")==7){
					this.setMsg("�����Ѿ�������ˣ���������!");
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
		.append(" select z.id from zhillsb z, zhuanmb m, zhuanmlb l \n")
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
		
		String zhi="";
		String sign=this.getYuansSign(con, Yuans, diancxxb_id);
		String sql=" select fx.zhi from yuansfxb fx,yuansxmb fs where fx.yuansxmb_id=fs.id \n"
					+" and fx.zhi!=0 and fs.zhuangt=1 and fx.zhuangt=1 and  fs.mingc='"+sign+"'  ";
		
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			zhi=rsl.getString("zhi");
		}
		rsl.close();
		return zhi;
	}

	private String getShenhzt(JDBCcon con){//�õ� ���� ��Ӧ��¼�� ���״̬
		String zhuangt="";
		String sql=" select h.shenhzt from huaygyfxb h where  h.bianm='"+this.getBianmValue().getValue()+"'";
		
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
//		DateField df = new DateField();
//		df.setValue(getRiq());
//		df.Binding("RIQ", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
//		df.setId("huayrq");
//		tb1.addField(df);
			
//		tb1.addText(new ToolbarText("��"));

		DateField df1 = new DateField();
		df1.setValue(getRiq());
		df1.Binding("RiqEnd", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("huayrqEnd");
		tb1.addField(df1);
		
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("���״̬:"));
		ComboBox zhuangt = new ComboBox();
		zhuangt.setTransform("ZhuangtSelect");
		zhuangt.setWidth(130);
		zhuangt.setListeners("select:function(own,rec,index){Ext.getDom('ZhuangtSelect').selectedIndex=index;Form0.submit();}");
		tb1.addField(zhuangt);
		
		tb1.addText(new ToolbarText("-"));
		
		if(visit.getString12().indexOf("RC")!=-1){  //�볧����  ���
			tb1.addText(new ToolbarText("�������:"));
			ComboBox shij = new ComboBox();
			shij.setTransform("BianmSelect");
			shij.setWidth(130);
			shij
					.setListeners("select:function(own,rec,index){Ext.getDom('BianmSelect').selectedIndex=index;Form0.submit();}");
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
		
		//�ڵõ�����ǰ�����Զ����
		if (getZhuangtValue().getId()==SELECT_WEISH){		
			String strMsg = AutoFix().toString();
			if (!"".equals(strMsg)) {
				this.setMsg(AutoFix().toString());//�����
			}
		}
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
//		((DateField) getToolbar().getItem("huayrq")).setValue(getRiq());
		((DateField) getToolbar().getItem("huayrqEnd")).setValue(getRiqEnd());
		
		return getToolbar().getRenderScript();
	}
	
	
	private int getTtype(String leix){
		int tType=0;
		if(Qbad.equals(leix)){
			tType = Judge.T_TYPE_Qbad;
		}else if(Stad.equals(leix)){
			tType = Judge.T_TYPE_Stad;
		}else if(Mt.equals(leix)){
			tType = Judge.T_TYPE_Mt;
		}else if(Mad.equals(leix)){
			tType = Judge.T_TYPE_Mad;
		}else if(Aad.equals(leix)){
			tType = Judge.T_TYPE_Aad;
		}else if(Vad.equals(leix)){
			tType = Judge.T_TYPE_Vad;
		}
		return tType;
	}
	
	private String getItemName(String leix){
		if(Qbad.equals(leix)){
			return "��Ͳ��";
		}else if(Stad.equals(leix)){
			return "���";
		}else if(Mt.equals(leix)){
			return "ȫˮ��";
		}else if(Mad.equals(leix)){
			return "�ոɻ�ˮ��";
		}else if(Aad.equals(leix)){
			return "�ҷ�";
		}else if(Vad.equals(leix)){
			return "�ӷ���";
		}
		return "";
	}
	
	private StringBuffer  AutoFix(){
		StringBuffer sb=new StringBuffer();
		if (getBianmValue().getId()!=-1){
			sb.append(AutoFixItem (Qbad));
			sb.append(AutoFixItem (Stad));
			sb.append(AutoFixItem (Mt));
			sb.append( AutoFixItem (Mad));
			sb.append(AutoFixItem (Aad));
			sb.append(AutoFixItem (Vad));
		}
		return sb;
	}
	
	private String AutoFixItem(String leix){
		JDBCcon con = new JDBCcon();
		Visit visit=(Visit)this.getPage().getVisit();
		
		String sql="";
		String strTableName="";
		String strResultName="";
		
		int itemDataRows=0;
		int itemType=getTtype(leix);
		con.setAutoCommit(false);
		
		if (itemType==Judge.T_TYPE_Qbad){//����
			sql="select h.id, h.farl from huayrlb h where h.bianm='"+getBianmValue().getValue()
				+"' and h.diancxxb_id="+visit.getDiancxxb_id()
				+" and h.riq>=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')-2) and h.riq<=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')+2) \n"
				+" and shenhzt>-1 and shenhzt<>1";			
			strResultName="farl";
			strTableName="huayrlb";
		}else if(itemType==Judge.T_TYPE_Stad){//���
			sql="select h.id,h.liuf from huaylfb h where h.bianm='"+getBianmValue().getValue()
				+"' and h.diancxxb_id="+visit.getDiancxxb_id()
				+" and h.riq>=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')-2) and h.riq<=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')+2) \n"
				+" and shenhzt>-1 and shenhzt<>1";
			strResultName="liuf";
			strTableName="huaylfb";
		}else {//��ҵ����
			sql="select h.id,huayz ,i.mingc ,i.bianm   from huaygyfxb h, item i, itemsort s"
				+" where h.fenxxmb_id = i.id"  
				+" and i.itemsortid = s.id"
				+"	and h.diancxxb_id="+visit.getDiancxxb_id()
				+" and i.bianm='"+leix+"'" 
				+" and h.bianm='"+getBianmValue().getValue()+"' "
				+" and h.riq>=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')-2) and h.riq<=(to_date('" + this.getRiqEnd() + "','yyyy-mm-dd')+2) \n"
				+" and shenhzt>-1 and shenhzt<>1";;
			strResultName="huayz";
			strTableName="huaygyfxb";
		}
		
		ResultSetList rs =con.getResultSetList(sql);
		double[] array = new double[rs.getRows()];
		itemDataRows=array.length;
		if (itemDataRows==0 ){
			return "��"+getItemName(leix)+"���ݣ�";
		}
		while(rs.next()){
			if("1".equals(rs.getString("shenhzt"))){
				setMsg("�ѽ������ݲ��ܽ�����ˣ�");
				return getItemName(leix)+"�ѽ������ݲ��ܽ������";
			}
			array[rs.getRow()] = rs.getDouble(strResultName);
		}
		
		Judge ju = new Judge();
		
		double[] dbarrjudge = ju.getJudgeData(itemType, array);
		if  (itemDataRows>=2 && dbarrjudge==null){
			return getItemName(leix) + "����";
		}
		
//		if (dbarrjudge==null) {
//			return getItemName(leix) + "ֻ��һ��ֵ";
//		}
		
		for(int i = 0; i < dbarrjudge.length ; i++){
			rs.beforefirst();
			while(rs.next()){
				if(dbarrjudge[i] == rs.getDouble(strResultName)){
					UpdateZt(con,strTableName,rs.getString("id"),2);
					rs.Remove(rs.getRow());
					break;
				}
			}
		}
		rs.beforefirst();
		while(rs.next()){
			UpdateZt(con,strTableName,rs.getString("id"),-1);
		}
		rs.close();
		con.commit();
		con.Close();
		return "";
	}

	private int UpdateZt(JDBCcon con, String tableName, String id, int zt){
		String sql = "update "+tableName+" set shenhzt = "+zt+" where id =" + id;
		return con.getUpdate(sql);
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			
			visit.setString12("RC");//�����ת������ҳ������  Ĭ����  �볡 ���� 
			
			String pageName=this.getPageName().toString();
			if(pageName!=null && pageName.toUpperCase().equals("RL")){
				visit.setString12(pageName);
			}
			
			visit.setActivePageName(getPageName().toString());
//			setRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(),-7,DateUtil.AddType_intDay)));
			setRiq(DateUtil.FormatDate(new Date()));
			setRiqEnd(DateUtil.FormatDate(new Date()));
			
			setZhuangtValue(null);
			setZhuangtModel(null);
			
			setChangbValue(null);
			setChangbModel(null);
			setBianmValue(null);
			setBianmModel(null);
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
			
		}
		if (riqchange || zhuangtChange) {
			riqchange = false;
			zhuangtChange=false;
			setBianmValue(null);
			setBianmModel(null);
		}
		getSelectData();
	}

	// �����Ƿ�仯
	private boolean riqchange = false;

	// ������
	private String riq;

	public String getRiq() {
		if (this.riq==null){
			riq = DateUtil.FormatDate(new Date());
		}
		
		return riq;
	}

	public void setRiq(String riq) {
		if (this.riq != null) {
			if (!this.riq.equals(riq))
				riqchange = true;
		}
		this.riq = riq;
	}

	private String riqEnd;
	public String getRiqEnd() {
		if (riqEnd==null) {
			riqEnd = DateUtil.FormatDate(new Date());
		}
		return riqEnd;
	}

	public void setRiqEnd(String riqEnd) {
		if (this.riqEnd != null) {
			if (!this.riqEnd.equals(riqEnd))
				riqchange = true;
		}
		this.riq = riqEnd;
		this.riqEnd = riqEnd;
	}
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean zhuangtChange=false;
	public IDropDownBean getZhuangtValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getZhuangtModel().getOptionCount() > 0) {
				setZhuangtValue((IDropDownBean) getZhuangtModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setZhuangtValue(IDropDownBean value) {
		if (value==null){
			zhuangtChange=true;
		}else{
			if (value.equals(((Visit) this.getPage().getVisit()).getDropDownBean3())){
				zhuangtChange=false;
			}else{
				zhuangtChange=true;
			}
		}
		((Visit) this.getPage().getVisit()).setDropDownBean3(value);
	}

	public IPropertySelectionModel getZhuangtModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			setZhuangtModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setZhuangtModel(IPropertySelectionModel model) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(model);
	}

	private void setZhuangtModels() {
		List dropdownlist =new ArrayList(); ;
		dropdownlist.add(new IDropDownBean(SELECT_WEISH, "δ���"));
		dropdownlist.add(new IDropDownBean(SELECT_YISH, "�����"));
		setZhuangtModel( new IDropDownModel(dropdownlist));
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
			setBianmModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setBianmModel(IPropertySelectionModel model) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(model);
	}

	private void setBianmModels() {
		StringBuffer sb = new StringBuffer();
		Visit visit=(Visit)this.getPage().getVisit();
		
		if (getZhuangtValue().getId()==SELECT_WEISH){//δ��˵ı���
//			sb.append(" select rownum as id,f.bianm  from (")
//			.append(" select distinct h.bianm from huaygyfxb h ")
//			.append(" where h.riq>=").append(DateUtil.FormatOracleDate(getRiq())).append("\n")
//			.append(" and h.riq<=").append(DateUtil.FormatOracleDate(getRiqEnd())).append("\n")
//			.append(" and h.diancxxb_id=").append(visit.getDiancxxb_id()).append("\n")
//			.append(" and h.shenhzt in (0,2) \n")
//			.append(" ) f order by f.bianm\n");
			
//			sb.append(
//					"SELECT  rownum as id,g.bianm from\n" +
//					"( select DISTINCT h.bianm  from huaygyfxb h\n" + 
//					" where h.riq>=").append(DateUtil.FormatOracleDate(getRiq())).append("-1\n" +
//					"and h.riq<=").append(DateUtil.FormatOracleDate(getRiqEnd())).append("\n" +
//					"and h.diancxxb_id=" + visit.getDiancxxb_id() + "\n" + 
//					"and h.shenhzt in (0,2)\n" + 
//					") g,\n" + 
//					"(select DISTINCT h.bianm  from huayrlb h\n" + 
//					" where h.riq>=").append(DateUtil.FormatOracleDate(getRiq())).append("-1\n" +
//					"and h.riq<=").append(DateUtil.FormatOracleDate(getRiqEnd())).append("\n" +
//					"and h.diancxxb_id=" + visit.getDiancxxb_id() + "\n" + 
//					"and h.shenhzt in (0,2)\n" + 
//					") r\n" +
//					"where r.bianm=g.bianm \n" +
//					"order by bianm"
//			);
			sb.append(
					"SELECT  rownum as id,bianm\n" +
					"from  (\n" + 
					"( select DISTINCT h.bianm  from huaygyfxb h\n" + 
					" where h.riq>=").append(DateUtil.FormatOracleDate(getRiq())).append("\n" + 
					"and h.riq<=").append(DateUtil.FormatOracleDate(getRiqEnd())).append("\n" + 
					"and h.diancxxb_id=" + visit.getDiancxxb_id() + "\n" + 
					"and h.shenhzt in (0,2)\n" + 
					")\n" + 
					"UNION\n" + 
					"(select DISTINCT h.bianm  from huayrlb h\n" + 
					" where h.riq>=").append(DateUtil.FormatOracleDate(getRiq())).append("\n" + 
					"and h.riq<=").append(DateUtil.FormatOracleDate(getRiqEnd())).append("\n" + 
					"and h.diancxxb_id=" + visit.getDiancxxb_id() + "\n" + 
					"and h.shenhzt in (0,2)\n" + 
					")\n" + 
					"UNION\n" + 
					"(select DISTINCT h.bianm  from huaylfb h\n" + 
					" where h.riq=").append(DateUtil.FormatOracleDate(getRiq())).append("\n" + 
					"and h.riq=").append(DateUtil.FormatOracleDate(getRiqEnd())).append("\n" + 
					"and h.diancxxb_id=" + visit.getDiancxxb_id() + "\n" + 
					"AND bianm<>'����'\n" + 
					"and h.shenhzt in (0,2)\n" + 
					")\n" + 
					")\n" + 
					"order by bianm"
					);
			
		}else{//����˵ı���
//			sb.append(" select rownum as id,f.bianm  from (")
//			.append(" select distinct h.bianm from huaygyfxb h ")
//			.append(" where h.riq=").append(DateUtil.FormatOracleDate(getRiq())).append("\n")
//			.append(" and h.diancxxb_id=").append(visit.getDiancxxb_id()).append("\n")
//			.append(" and h.shenhzt in (1) \n")
//			.append(" ) f order by f.bianm\n");
		
//		sb.append(
//				"SELECT rownum as id, g.bianm from\n" +
//				"( select DISTINCT h.bianm as bianm from huaygyfxb h\n" + 
//				" where h.riq>=").append(DateUtil.FormatOracleDate(getRiq())).append("-1\n" +
//				"and h.riq<=").append(DateUtil.FormatOracleDate(getRiqEnd())).append("\n" +
//				"and h.diancxxb_id=" + visit.getDiancxxb_id() + "\n" + 
//				"and h.shenhzt in (1)\n" + 
//				") g ,\n" + 
//				"(\n" + 
//				"select DISTINCT h.bianm as bianm from huayrlb h\n" + 
//				" where h.riq>=").append(DateUtil.FormatOracleDate(getRiq())).append("-1\n" +
//				"and h.riq<=").append(DateUtil.FormatOracleDate(getRiqEnd())).append("\n" +
//				"and h.diancxxb_id=" + visit.getDiancxxb_id() + "\n" + 
//				"and h.shenhzt in (1)\n" + 
//				") r\n" +  
//				"where r.bianm=g.bianm \n" +
//				"order by bianm"
//				);
			sb.append(
				"SELECT  rownum as id,bianm\n" +
				"from  (\n" + 
				"( select DISTINCT h.bianm  from huaygyfxb h\n" + 
				" where h.riq>=").append(DateUtil.FormatOracleDate(getRiq())).append("\n" + 
				"and h.riq<=").append(DateUtil.FormatOracleDate(getRiqEnd())).append("\n" + 
				"and h.diancxxb_id=" + visit.getDiancxxb_id() + "\n" + 
				"and h.shenhzt in (1)\n" + 
				")\n" + 
				"UNION\n" + 
				"(select DISTINCT h.bianm  from huayrlb h\n" + 
				" where h.riq>=").append(DateUtil.FormatOracleDate(getRiq())).append("\n" + 
				"and h.riq<=").append(DateUtil.FormatOracleDate(getRiqEnd())).append("\n" + 
				"and h.diancxxb_id=" + visit.getDiancxxb_id() + "\n" + 
				"and h.shenhzt in (1)\n" + 
				")\n" + 
				"UNION\n" + 
				"(select DISTINCT h.bianm  from huaylfb h\n" + 
				" where h.riq=").append(DateUtil.FormatOracleDate(getRiq())).append("\n" + 
				"and h.riq=").append(DateUtil.FormatOracleDate(getRiqEnd())).append("\n" + 
				"and h.diancxxb_id=" + visit.getDiancxxb_id() + "\n" + 
				"AND bianm<>'����'\n" + 
				"and h.shenhzt in (1)\n" + 
				")\n" + 
				")\n" + 
				"order by bianm"
				);
		}
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

}
