package com.zhiren.jt.meiysgl.rucsl;

import java.io.Serializable;

import java.util.Date;

public class Rucslbean implements Serializable {
	
    private static final long serialVersionUID = -5207426641277654754L;
    
    private boolean flag;
    private int xuh;// ���
    private int id;// id
    private String diancxxb_id;// �糧��Ϣ��_id
    private String fahdwb_id;// ������λ
    private String meikxxb_id;// ú��λ
    private double meijb_id;//ú�۱�id
    private Date fahrq;// ��������
    private Date daohrq;// ��������
    private String chec;// ����
    private String faz_id;// ��վ
    private String daoz_id;// ��վ
    private String jihkjb_id;// �ƻ��ھ�
    private double maoz;//ë��
    private double piz;// Ƥ��
    private double jingz;//����
    private double biaoz;// ����(��)
    private double yingk;//ӯ��
    private double yuns;//����;
    private double yunsl;//������
    private double koud;//�۶�;
    private double koussl;//��ˮ����
    private double kouz;//����
    private double meigzzl;//ú��վ����
    private double ches;//����
    private double tiaozbz;//������־
    private Date   ruzrq;//��������;
    private String yansbh;//���ձ��
    private double zhilb_id;//������
    private double jiesb_id; //�����
    private double lie_id;//��id
    private String yunsfsb_id;//���䷽ʽ
    private String ranlpzb_id;// ȼ��Ʒ��
    private double yuandz_id;//ԭ��վid
    private String yuanshdwb_id;// ԭ�ջ���λ
    private double kuangfzlb_id;//��������
    private double kuangfjs_id;//�󷽽���
    private double shenhb_shenhid;//��˱�
    private String beiz;//��ע
    
  
    public Rucslbean() {
		super();
		// TODO �Զ����ɹ��캯�����
	}

    public Rucslbean(int xuh) {
		this.xuh=xuh;
		
	}



	public String toString() {

        StringBuffer buffer = new StringBuffer("");

        buffer.append(xuh);
        buffer.append(',');
        buffer.append(diancxxb_id);
        buffer.append(',');
        buffer.append(fahdwb_id);
        buffer.append(',');
        buffer.append(meikxxb_id);
        buffer.append(',');
        buffer.append(meijb_id);
        buffer.append(',');
        buffer.append(fahrq);
        buffer.append(',');
        buffer.append(daohrq);
        buffer.append(',');
        buffer.append(chec);
        buffer.append(',');
        buffer.append(faz_id);
        buffer.append(',');
        buffer.append(daoz_id);
        buffer.append(',');
        buffer.append(jihkjb_id);
        buffer.append(',');
        buffer.append(maoz);
        buffer.append(',');
        buffer.append(piz);
        buffer.append(',');
        buffer.append(jingz);
        buffer.append(',');
        buffer.append(biaoz);
        buffer.append(',');
        buffer.append(yingk);
        buffer.append(',');
        buffer.append(yuns);
        buffer.append(',');
        buffer.append(yunsl);
        buffer.append(',');
        buffer.append(koud);
        buffer.append(',');
        buffer.append(koussl);
        buffer.append(',');
        buffer.append(kouz);
        buffer.append(',');
        buffer.append(meigzzl);
        buffer.append(',');
        buffer.append(ches);
        buffer.append(',');
        buffer.append(tiaozbz);
        buffer.append(',');
        buffer.append(ruzrq);
        buffer.append(',');
        buffer.append(yansbh);
        buffer.append(',');
        buffer.append(zhilb_id);
        buffer.append(',');
        buffer.append(jiesb_id);
        buffer.append(',');
        buffer.append(lie_id);
        buffer.append(',');
        buffer.append(yunsfsb_id);
        buffer.append(',');
        buffer.append(ranlpzb_id);
        buffer.append(',');
        buffer.append(yuandz_id);
        buffer.append(',');
        buffer.append(yuanshdwb_id);
        buffer.append(',');
        buffer.append(kuangfzlb_id);
        buffer.append(',');
        buffer.append(kuangfjs_id);
        buffer.append(',');
        buffer.append(shenhb_shenhid);
        buffer.append(',');
        buffer.append(beiz);
      

        return buffer.toString();
    }





	public static long getSerialVersionUID() {
		return serialVersionUID;
	}





	public String getBeiz() {
		return beiz;
	}





	public void setBeiz(String beiz) {
		this.beiz = beiz;
	}





	public double getBiaoz() {
		return biaoz;
	}





	public void setBiaoz(double biaoz) {
		this.biaoz = biaoz;
	}





	public String getChec() {
		return chec;
	}





	public void setChec(String chec) {
		this.chec = chec;
	}





	public double getChes() {
		return ches;
	}





	public void setChes(double ches) {
		this.ches = ches;
	}





	public Date getDaohrq() {
		return daohrq;
	}





	public void setDaohrq(Date daohrq) {
		this.daohrq = daohrq;
	}





	public String getDaoz_id() {
		return daoz_id;
	}





	public void setDaoz_id(String daoz_id) {
		this.daoz_id = daoz_id;
	}





	public String  getDiancxxb_id() {
		return diancxxb_id;
	}





	public void setDiancxxb_id(String diancxxb_id) {
		this.diancxxb_id = diancxxb_id;
	}





	public String getFahdwb_id() {
		return fahdwb_id;
	}





	public void setFahdwb_id(String fahdwb_id) {
		this.fahdwb_id = fahdwb_id;
	}





	public Date getFahrq() {
		return fahrq;
	}





	public void setFahrq(Date fahrq) {
		this.fahrq = fahrq;
	}





	public String getFaz_id() {
		return faz_id;
	}





	public void setFaz_id(String faz_id) {
		this.faz_id = faz_id;
	}





	public int getId() {
		return id;
	}





	public void setId(int id) {
		this.id = id;
	}





	public double getJiesb_id() {
		return jiesb_id;
	}





	public void setJiesb_id(double jiesb_id) {
		this.jiesb_id = jiesb_id;
	}





	public String getJihkjb_id() {
		return jihkjb_id;
	}





	public void setJihkjb_id(String jihkjb_id) {
		this.jihkjb_id = jihkjb_id;
	}





	public double getJingz() {
		return jingz;
	}





	public void setJingz(double jingz) {
		this.jingz = jingz;
	}





	public double getKoud() {
		return koud;
	}





	public void setKoud(double koud) {
		this.koud = koud;
	}





	public double getKoussl() {
		return koussl;
	}





	public void setKoussl(double koussl) {
		this.koussl = koussl;
	}





	public double getKouz() {
		return kouz;
	}





	public void setKouz(double kouz) {
		this.kouz = kouz;
	}





	public double getKuangfjs_id() {
		return kuangfjs_id;
	}





	public void setKuangfjs_id(double kuangfjs_id) {
		this.kuangfjs_id = kuangfjs_id;
	}





	public double getKuangfzlb_id() {
		return kuangfzlb_id;
	}





	public void setKuangfzlb_id(double kuangfzlb_id) {
		this.kuangfzlb_id = kuangfzlb_id;
	}





	public double getLie_id() {
		return lie_id;
	}





	public void setLie_id(double lie_id) {
		this.lie_id = lie_id;
	}





	public boolean getFlag() {
		return flag;
	}





	public void setFlag(boolean m_flag) {
		this.flag = m_flag;
	}





	public double getMaoz() {
		return maoz;
	}





	public void setMaoz(double maoz) {
		this.maoz = maoz;
	}





	public double getMeigzzl() {
		return meigzzl;
	}





	public void setMeigzzl(double meigzzl) {
		this.meigzzl = meigzzl;
	}





	public double getMeijb_id() {
		return meijb_id;
	}





	public void setMeijb_id(double meijb_id) {
		this.meijb_id = meijb_id;
	}





	public String getMeikxxb_id() {
		return meikxxb_id;
	}





	public void setMeikxxb_id(String meikxxb_id) {
		this.meikxxb_id = meikxxb_id;
	}





	public double getPiz() {
		return piz;
	}





	public void setPiz(double piz) {
		this.piz = piz;
	}





	public String getRanlpzb_id() {
		return ranlpzb_id;
	}





	public void setRanlpzb_id(String ranlpzb_id) {
		this.ranlpzb_id = ranlpzb_id;
	}





	public Date getRuzrq() {
		return ruzrq;
	}





	public void setRuzrq(Date ruzrq) {
		this.ruzrq = ruzrq;
	}





	public double getShenhb_shenhid() {
		return shenhb_shenhid;
	}





	public void setShenhb_shenhid(double shenhb_shenhid) {
		this.shenhb_shenhid = shenhb_shenhid;
	}





	public double getTiaozbz() {
		return tiaozbz;
	}





	public void setTiaozbz(double tiaozbz) {
		this.tiaozbz = tiaozbz;
	}





	public int getXuh() {
		return xuh;
	}





	public void setXuh(int xuh) {
		this.xuh = xuh;
	}





	public String getYansbh() {
		return yansbh;
	}





	public void setYansbh(String yansbh) {
		this.yansbh = yansbh;
	}





	public double getYingk() {
		return yingk;
	}





	public void setYingk(double yingk) {
		this.yingk = yingk;
	}





	public double getYuandz_id() {
		return yuandz_id;
	}





	public void setYuandz_id(double yuandz_id) {
		this.yuandz_id = yuandz_id;
	}





	public String getYuanshdwb_id() {
		return yuanshdwb_id;
	}





	public void setYuanshdwb_id(String yuanshdwb_id) {
		this.yuanshdwb_id = yuanshdwb_id;
	}





	public double getYuns() {
		return yuns;
	}





	public void setYuns(double yuns) {
		this.yuns = yuns;
	}





	




	public double getYunsl() {
		return yunsl;
	}





	public void setYunsl(double yunsl) {
		this.yunsl = yunsl;
	}





	public double getZhilb_id() {
		return zhilb_id;
	}





	public void setZhilb_id(double zhilb_id) {
		this.zhilb_id = zhilb_id;
	}





	public Rucslbean(int xuh, int id, String diancxxb_id, String fahdwb_id, String meikxxb_id, double meijb_id, Date fahrq, Date daohrq, String chec, String faz_id, String daoz_id, String jihkjb_id, double maoz, double piz, double jingz, double biaoz, double yingk, double yuns, double yunsl, double koud, double koussl, double kouz, double meigzzl, double ches, double tiaozbz, Date ruzrq, String yansbh, double zhilb_id, double jiesb_id, double lie_id, String yunsfs, String ranlpzb_id, double yuandz_id, String yuanshdwb_id, double kuangfzlb_id, double kuangfjs_id, double shenhb_shenhid, String beiz) {
		super();
		this.xuh = xuh;
		this.id = id;
		this.diancxxb_id = diancxxb_id;
		this.fahdwb_id = fahdwb_id;
		this.meikxxb_id = meikxxb_id;
		this.meijb_id = meijb_id;
		this.fahrq = fahrq;
		this.daohrq = daohrq;
		this.chec = chec;
		this.faz_id = faz_id;
		this.daoz_id = daoz_id;
		this.jihkjb_id = jihkjb_id;
		this.maoz = maoz;
		this.piz = piz;
		this.jingz = jingz;
		this.biaoz = biaoz;
		this.yingk = yingk;
		this.yuns = yuns;
		this.yunsl = yunsl;
		this.koud = koud;
		this.koussl = koussl;
		this.kouz = kouz;
		this.meigzzl = meigzzl;
		this.ches = ches;
		this.tiaozbz = tiaozbz;
		this.ruzrq = ruzrq;
		this.yansbh = yansbh;
		this.zhilb_id = zhilb_id;
		this.jiesb_id = jiesb_id;
		this.lie_id = lie_id;
		this.yunsfsb_id = yunsfs;
		this.ranlpzb_id = ranlpzb_id;
		this.yuandz_id = yuandz_id;
		this.yuanshdwb_id = yuanshdwb_id;
		this.kuangfzlb_id = kuangfzlb_id;
		this.kuangfjs_id = kuangfjs_id;
		this.shenhb_shenhid = shenhb_shenhid;
		this.beiz = beiz;
	}

	public String getYunsfsb_id() {
		return yunsfsb_id;
	}

	public void setYunsfsb_id(String yunsfsb_id) {
		this.yunsfsb_id = yunsfsb_id;
	}



}
