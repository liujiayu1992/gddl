package com.zhiren.dc.huaygl.rulhy;

import java.io.Serializable;

import java.util.Date;

public class Rulgyfxbean implements Serializable {
	private long Mt_ph;// 称量瓶号

	private long Mt_pzl;// 称量瓶质量

	private long Mt_pzlsyzl;// 称量瓶质量+式样质量

	private long Mt_syzl;// 式样质量

	private long Mt_hhzzl;// 烘后总质量

	private long Mt_jcxsyhzzl;// 检查性实验后总质量

	private long Mt_qsf;// 全水分

	private long Mt_pjz;// 平均值

	private String Mt_sh;// 审核

	private long Mad_ph;// 称量瓶号

	private long Mad_pzl;// 称量瓶质量

	private long Mad_pzlsyzl;// 称量瓶质量+式样质量

	private long Mad_syzl;// 式样质量

	private long Mad_hhzzl;// 烘后总质量

	private long Mad_jcxsyhzzl;// 检查性实验后总质量

	private long Mad_qsf;// 全水分

	private long Mad_pjz;// 平均值

	private String Mad_sh;// 审核

	private long Vad_ggh;// 坩埚号

	private long Vad_ggzl;// 坩埚质量

	private long Vad_ggzlsyzl;// 坩埚质量+试样质量

	private long Vad_syzl;// 试样质量

	private long Vad_jrhzzl;// 加热后总质量

	private long Vad_myjrhjszl;// 煤样加热后减少的质量

	private long Vad_hffqsf;// 挥发分Vad=m/m*00-Mad(%)

	private long Vad_hff;// 挥发分

	private long Vad_pjz;// 平均值

	private String Vad_sh;// 审核

	private long Aad_hmh;// 灰皿号

	private long Aad_hmzl;// 灰皿质量

	private long Aad_hmzlsyzl;// 灰皿质量+试样质量

	private long Aad_syzl;// 试样质量

	private long Aad_jrhzzl;// 加热后总质量

	private long Aad_jcxsyhzzl;// 检查性试验后总质量

	private long Aad_clwzzl;// 残留物的质量

	private long Aad_hf;// 灰分Aad

	private long Aad_pjz;// 平均值

	private String Aad_sh;// 审核

	private long Ql_qmh;// 器皿号

	private long Ql_qmzl;// 器皿质量

	private long Ql_qmzlsyzl;// 器皿质量+式样质量

	private long Ql_syzl;// 式样质量

	private long Ql_ad;// 全硫St,ad

	private long Ql_d;// 全硫St,d

	private long Ql_pjz;// 平均值

	private String Ql_sh;// 审核

	private long Frl_ggzl;// 坩埚质量

	private long Frl_ggzlsyzl;// 坩埚质量+式样质量

	private long Frl_syzl;// 式样质量

	private long Frl_qb;// Qb,ad

	private long Frl_qgr;// Qgr,ad

	private long Frl_qnet;// Qnet,ar

	private long Frl_qnetpjz;// 平均值

	private String Frl_sh;// 审核

	public long getAad_clwzzl() {
		return Aad_clwzzl;
	}

	public void setAad_clwzzl(long aad_clwzzl) {
		Aad_clwzzl = aad_clwzzl;
	}

	public long getAad_hf() {
		return Aad_hf;
	}

	public void setAad_hf(long aad_hf) {
		Aad_hf = aad_hf;
	}

	public long getAad_hmh() {
		return Aad_hmh;
	}

	public void setAad_hmh(long aad_hmh) {
		Aad_hmh = aad_hmh;
	}

	public long getAad_hmzl() {
		return Aad_hmzl;
	}

	public void setAad_hmzl(long aad_hmzl) {
		Aad_hmzl = aad_hmzl;
	}

	public long getAad_hmzlsyzl() {
		return Aad_hmzlsyzl;
	}

	public void setAad_hmzlsyzl(long aad_hmzlsyzl) {
		Aad_hmzlsyzl = aad_hmzlsyzl;
	}

	public long getAad_jcxsyhzzl() {
		return Aad_jcxsyhzzl;
	}

	public void setAad_jcxsyhzzl(long aad_jcxsyhzzl) {
		Aad_jcxsyhzzl = aad_jcxsyhzzl;
	}

	public long getAad_jrhzzl() {
		return Aad_jrhzzl;
	}

	public void setAad_jrhzzl(long aad_jrhzzl) {
		Aad_jrhzzl = aad_jrhzzl;
	}

	public long getAad_pjz() {
		return Aad_pjz;
	}

	public void setAad_pjz(long aad_pjz) {
		Aad_pjz = aad_pjz;
	}

	public String getAad_sh() {
		return Aad_sh;
	}

	public void setAad_sh(String aad_sh) {
		Aad_sh = aad_sh;
	}

	public long getAad_syzl() {
		return Aad_syzl;
	}

	public void setAad_syzl(long aad_syzl) {
		Aad_syzl = aad_syzl;
	}

	public long getFrl_ggzl() {
		return Frl_ggzl;
	}

	public void setFrl_ggzl(long frl_ggzl) {
		Frl_ggzl = frl_ggzl;
	}

	public long getFrl_ggzlsyzl() {
		return Frl_ggzlsyzl;
	}

	public void setFrl_ggzlsyzl(long frl_ggzlsyzl) {
		Frl_ggzlsyzl = frl_ggzlsyzl;
	}

	public long getFrl_qb() {
		return Frl_qb;
	}

	public void setFrl_qb(long frl_qb) {
		Frl_qb = frl_qb;
	}

	public long getFrl_qgr() {
		return Frl_qgr;
	}

	public void setFrl_qgr(long frl_qgr) {
		Frl_qgr = frl_qgr;
	}

	public long getFrl_qnet() {
		return Frl_qnet;
	}

	public void setFrl_qnet(long frl_qnet) {
		Frl_qnet = frl_qnet;
	}

	public long getFrl_qnetpjz() {
		return Frl_qnetpjz;
	}

	public void setFrl_qnetpjz(long frl_qnetpjz) {
		Frl_qnetpjz = frl_qnetpjz;
	}

	public String getFrl_sh() {
		return Frl_sh;
	}

	public void setFrl_sh(String frl_sh) {
		Frl_sh = frl_sh;
	}

	public long getFrl_syzl() {
		return Frl_syzl;
	}

	public void setFrl_syzl(long frl_syzl) {
		Frl_syzl = frl_syzl;
	}

	public long getMad_hhzzl() {
		return Mad_hhzzl;
	}

	public void setMad_hhzzl(long mad_hhzzl) {
		Mad_hhzzl = mad_hhzzl;
	}

	public long getMad_jcxsyhzzl() {
		return Mad_jcxsyhzzl;
	}

	public void setMad_jcxsyhzzl(long mad_jcxsyhzzl) {
		Mad_jcxsyhzzl = mad_jcxsyhzzl;
	}

	public long getMad_ph() {
		return Mad_ph;
	}

	public void setMad_ph(long mad_ph) {
		Mad_ph = mad_ph;
	}

	public long getMad_pjz() {
		return Mad_pjz;
	}

	public void setMad_pjz(long mad_pjz) {
		Mad_pjz = mad_pjz;
	}

	public long getMad_pzl() {
		return Mad_pzl;
	}

	public void setMad_pzl(long mad_pzl) {
		Mad_pzl = mad_pzl;
	}

	public long getMad_pzlsyzl() {
		return Mad_pzlsyzl;
	}

	public void setMad_pzlsyzl(long mad_pzlsyzl) {
		Mad_pzlsyzl = mad_pzlsyzl;
	}

	public long getMad_qsf() {
		return Mad_qsf;
	}

	public void setMad_qsf(long mad_qsf) {
		Mad_qsf = mad_qsf;
	}

	public String getMad_sh() {
		return Mad_sh;
	}

	public void setMad_sh(String mad_sh) {
		Mad_sh = mad_sh;
	}

	public long getMad_syzl() {
		return Mad_syzl;
	}

	public void setMad_syzl(long mad_syzl) {
		Mad_syzl = mad_syzl;
	}

	public long getMt_hhzzl() {
		return Mt_hhzzl;
	}

	public void setMt_hhzzl(long mt_hhzzl) {
		Mt_hhzzl = mt_hhzzl;
	}

	public long getMt_jcxsyhzzl() {
		return Mt_jcxsyhzzl;
	}

	public void setMt_jcxsyhzzl(long mt_jcxsyhzzl) {
		Mt_jcxsyhzzl = mt_jcxsyhzzl;
	}

	public long getMt_ph() {
		return Mt_ph;
	}

	public void setMt_ph(long mt_ph) {
		Mt_ph = mt_ph;
	}

	public long getMt_pjz() {
		return Mt_pjz;
	}

	public void setMt_pjz(long mt_pjz) {
		Mt_pjz = mt_pjz;
	}

	public long getMt_pzl() {
		return Mt_pzl;
	}

	public void setMt_pzl(long Mt_pzl) {
		Mt_pzl = Mt_pzl;
	}

	public long getMt_pzlsyzl() {
		return Mt_pzlsyzl;
	}

	public void setMt_pzlsyzl(long Mt_pzlsyzl) {
		Mt_pzlsyzl = Mt_pzlsyzl;
	}

	public long getMt_qsf() {
		return Mt_qsf;
	}

	public void setMt_qsf(long mt_qsf) {
		Mt_qsf = mt_qsf;
	}

	public String getMt_sh() {
		return Mt_sh;
	}

	public void setMt_sh(String mt_sh) {
		Mt_sh = mt_sh;
	}

	public long getMt_syzl() {
		return Mt_syzl;
	}

	public void setMt_syzl(long mt_syzl) {
		Mt_syzl = mt_syzl;
	}

	public long getQl_ad() {
		return Ql_ad;
	}

	public void setQl_ad(long ql_ad) {
		Ql_ad = ql_ad;
	}

	public long getQl_d() {
		return Ql_d;
	}

	public void setQl_d(long ql_d) {
		Ql_d = ql_d;
	}

	public long getQl_pjz() {
		return Ql_pjz;
	}

	public void setQl_pjz(long ql_pjz) {
		Ql_pjz = ql_pjz;
	}

	public long getQl_qmh() {
		return Ql_qmh;
	}

	public void setQl_qmh(long ql_qmh) {
		Ql_qmh = ql_qmh;
	}

	public long getQl_qmzl() {
		return Ql_qmzl;
	}

	public void setQl_qmzl(long ql_qmzl) {
		Ql_qmzl = ql_qmzl;
	}

	public long getQl_qmzlsyzl() {
		return Ql_qmzlsyzl;
	}

	public void setQl_qmzlsyzl(long ql_qmzlsyzl) {
		Ql_qmzlsyzl = ql_qmzlsyzl;
	}

	public String getQl_sh() {
		return Ql_sh;
	}

	public void setQl_sh(String ql_sh) {
		Ql_sh = ql_sh;
	}

	public long getQl_syzl() {
		return Ql_syzl;
	}

	public void setQl_syzl(long ql_syzl) {
		Ql_syzl = ql_syzl;
	}

	public long getVad_ggh() {
		return Vad_ggh;
	}

	public void setVad_ggh(long vad_ggh) {
		Vad_ggh = vad_ggh;
	}

	public long getVad_ggzl() {
		return Vad_ggzl;
	}

	public void setVad_ggzl(long vad_ggzl) {
		Vad_ggzl = vad_ggzl;
	}

	public long getVad_ggzlsyzl() {
		return Vad_ggzlsyzl;
	}

	public void setVad_ggzlsyzl(long vad_ggzlsyzl) {
		Vad_ggzlsyzl = vad_ggzlsyzl;
	}

	public long getVad_hff() {
		return Vad_hff;
	}

	public void setVad_hff(long vad_hff) {
		Vad_hff = vad_hff;
	}

	public long getVad_hffqsf() {
		return Vad_hffqsf;
	}

	public void setVad_hffqsf(long vad_hffqsf) {
		Vad_hffqsf = vad_hffqsf;
	}

	public long getVad_jrhzzl() {
		return Vad_jrhzzl;
	}

	public void setVad_jrhzzl(long vad_jrhzzl) {
		Vad_jrhzzl = vad_jrhzzl;
	}

	public long getVad_myjrhjszl() {
		return Vad_myjrhjszl;
	}

	public void setVad_myjrhjszl(long vad_myjrhjszl) {
		Vad_myjrhjszl = vad_myjrhjszl;
	}

	public long getVad_pjz() {
		return Vad_pjz;
	}

	public void setVad_pjz(long vad_pjz) {
		Vad_pjz = vad_pjz;
	}

	public String getVad_sh() {
		return Vad_sh;
	}

	public void setVad_sh(String vad_sh) {
		Vad_sh = vad_sh;
	}

	public long getVad_syzl() {
		return Vad_syzl;
	}

	public void setVad_syzl(long vad_syzl) {
		Vad_syzl = vad_syzl;
	}

	public Rulgyfxbean(long Mt_ph, long Mt_pzl, long Mt_pzlsyzl, long Mt_syzl,
			long Mt_hhzzl, long Mt_jcxsyhzzl, long Mt_qsf, long Mt_pjz,
			String Mt_sh, long Mad_ph, long Mad_pzl, long Mad_pzlsyzl,
			long Mad_syzl, long Mad_hhzzl, long Mad_jcxsyhzzl, long Mad_qsf,
			long Mad_pjz, String Mad_sh, long Vad_ggh, long Vad_ggzl,
			long Vad_ggzlsyzl, long Vad_syzl, long Vad_jrhzzl,
			long Vad_myjrhjszl, long Vad_hffqsf, long Vad_hff, long Vad_pjz,
			String Vad_sh, long Aad_hmh, long Aad_hmzl, long Aad_hmzlsyzl,
			long Aad_syzl, long Aad_jrhzzl, long Aad_jcxsyhzzl,
			long Aad_clwzzl, long Aad_hf, long Aad_pjz, String Aad_sh,
			long Ql_qmh, long Ql_qmzl, long Ql_qmzlsyzl, long Ql_syzl,
			long Ql_ad, long Ql_d, long Ql_pjz, String Ql_sh, long Frl_ggzl,
			long Frl_ggzlsyzl, long Frl_syzl, long Frl_qb, long Frl_qgr,
			long Frl_qnet, long Frl_qnetpjz, String Frl_sh)
	{

		this.Mt_ph=Mt_ph;
		this.Mt_pzl=Mt_pzl;
		this.Mt_pzlsyzl=Mt_pzlsyzl;
		this.Mt_syzl=Mt_syzl;
		this.Mt_hhzzl=Mt_hhzzl;
		this.Mt_jcxsyhzzl=Mt_jcxsyhzzl;
		this.Mt_qsf=Mt_qsf;
		this.Mt_pjz=Mt_pjz;
		this.Mt_sh=Mt_sh;
		this.Mad_ph=Mad_ph;
		this.Mad_pzl=Mad_pzl;
		this.Mad_pzlsyzl=Mad_pzlsyzl;
		this.Mad_syzl=Mad_syzl;
		this.Mad_hhzzl=Mad_hhzzl;
		this.Mad_jcxsyhzzl=Mad_jcxsyhzzl;
		this.Mad_qsf=Mad_qsf;
		this.Mad_pjz=Mad_pjz;
		this.Mad_sh=Mad_sh;
		this.Vad_ggh=Vad_ggh;
		this.Vad_ggzl=Vad_ggzl;
		this.Vad_ggzlsyzl=Vad_ggzlsyzl;
		this.Vad_syzl=Vad_syzl;
		this.Vad_jrhzzl=Vad_jrhzzl;
		this.Vad_myjrhjszl=Vad_myjrhjszl;
		this.Vad_hffqsf=Vad_hffqsf;
		this.Vad_hff=Vad_hff;
		this.Vad_pjz=Vad_pjz;
		this.Vad_sh=Vad_sh;
		this.Aad_hmh=Aad_hmh;
		this.Aad_hmzl=Aad_hmzl;
		this.Aad_hmzlsyzl=Aad_hmzlsyzl;
		this.Aad_syzl=Aad_syzl;
		this.Aad_jrhzzl=Aad_jrhzzl;
		this.Aad_jcxsyhzzl=Aad_jcxsyhzzl;
		this.Aad_clwzzl=Aad_clwzzl;
		this.Aad_hf=Aad_hf;
		this.Aad_pjz=Aad_pjz;
		this.Aad_sh=Aad_sh;
		this.Ql_qmh=Ql_qmh;
		this.Ql_qmzl=Ql_qmzl;
		this.Ql_qmzlsyzl=Ql_qmzlsyzl;
		this.Ql_syzl=Ql_syzl;
		this.Ql_ad=Ql_ad;
		this.Ql_d=Ql_d;
		this.Ql_pjz=Ql_pjz;
		this.Ql_sh=Ql_sh;
		this.Frl_ggzl=Frl_ggzl;
		this.Frl_ggzlsyzl=Frl_ggzlsyzl;
		this.Frl_syzl=Frl_syzl;
		this.Frl_qb=Frl_qb;
		this.Frl_qgr=Frl_qgr;
		this.Frl_qnet=Frl_qnet;
		this.Frl_qnetpjz=Frl_qnetpjz;
		this.Frl_sh=Frl_sh;
		
	}
	public String toString()
	{
		
		StringBuffer buffer = new StringBuffer("");
		
		buffer.append("Mt_ph");
		buffer.append(",");
		buffer.append("Mt_PZL");
		buffer.append(",");
		buffer.append("Mt_pzlsyzl");
		buffer.append(",");
		buffer.append("Mt_syzl");
		buffer.append(",");
		buffer.append("Mt_hhzzl");
		buffer.append(",");
		buffer.append("Mt_jcxsyhzzl");
		buffer.append(",");
		buffer.append("Mt_qsf");
		buffer.append(",");
		buffer.append("Mt_pjz");
		buffer.append(",");
		buffer.append("Mt_sh");
		buffer.append(",");
		buffer.append("Mad_ph");
		buffer.append(",");
		buffer.append("Mad_pzl");
		buffer.append(",");
		buffer.append("Mad_pzlsyzl");
		buffer.append(",");
		buffer.append("Mad_syzl");
		buffer.append(",");
		buffer.append("Mad_hhzzl");
		buffer.append(",");
		buffer.append("Mad_jcxsyhzzl");
		buffer.append(",");
		buffer.append("Mad_qsf");
		buffer.append(",");
		buffer.append("Mad_pjz");
		buffer.append(",");
		buffer.append("Mad_sh");
		buffer.append(",");
		buffer.append("Vad_ggh");
		buffer.append(",");
		buffer.append("Vad_ggzl");
		buffer.append(",");
		buffer.append("Vad_ggzlsyzl");
		buffer.append(",");
		buffer.append("Vad_syzl");
		buffer.append(",");
		buffer.append("Vad_jrhzzl");
		buffer.append(",");
		buffer.append("Vad_myjrhjszl");
		buffer.append(",");
		buffer.append("Vad_hffqsf");
		buffer.append(",");
		buffer.append("Vad_hff");
		buffer.append(",");
		buffer.append("Vad_pjz");
		buffer.append(",");
		buffer.append("Vad_sh");
		buffer.append(",");
		buffer.append("Aad_hmh");
		buffer.append(",");
		buffer.append("Aad_hmzl");
		buffer.append(",");
		buffer.append("Aad_hmzlsyzl");
		buffer.append(",");
		buffer.append("Aad_syzl");
		buffer.append(",");
		buffer.append("Aad_jrhzzl");
		buffer.append(",");
		buffer.append("Aad_jcxsyhzzl");
		buffer.append(",");
		buffer.append("Aad_clwzzl");
		buffer.append(",");
		buffer.append("Aad_hf");
		buffer.append(",");
		buffer.append("Aad_pjz");
		buffer.append(",");
		buffer.append("Aad_sh");
		buffer.append(",");
		buffer.append("Ql_qmh");
		buffer.append(",");
		buffer.append("Ql_qmzl");
		buffer.append(",");
		buffer.append("Ql_qmzlsyzl");
		buffer.append(",");
		buffer.append("Ql_syzl");
		buffer.append(",");
		buffer.append("Ql_ad");
		buffer.append(",");
		buffer.append("Ql_d");
		buffer.append(",");
		buffer.append("Ql_pjz");
		buffer.append(",");
		buffer.append("Ql_sh");
		buffer.append(",");
		buffer.append("Frl_ggzl");
		buffer.append(",");
		buffer.append("Frl_ggzlsyzl");
		buffer.append(",");
		buffer.append("Frl_syzl");
		buffer.append(",");
		buffer.append("Frl_qb");
		buffer.append(",");
		buffer.append("Frl_qgr");
		buffer.append(",");
		buffer.append("Frl_qnet");
		buffer.append(",");
		buffer.append("Frl_qnetpjz");
		buffer.append(",");
		buffer.append("Frl_sh");
		
		return buffer.toString();
	}

}