package com.zhiren.dc.jilgl.tiel.duih;

import java.io.Serializable;

public class Guohxx implements Serializable {
	private static final long serialVersionUID = -4181003283189633793L;
	public long _id;
	public String _maoz;
	public String _piz;
	public String _sud;
	public String _cheph;
	public String _biaoz;
	public String _daozch;
	public String _koud;
	public String _beiz;
	public Guohxx(String maoz,String piz,String sud,String cheph) {
		_maoz = maoz;
		_piz = piz;
		_sud = sud;
		_cheph = cheph;
	}
	public Guohxx(long id, String cheph, String maoz, String piz, String biaoz, String sud, String daozch, String beiz) {
		this._id = id;
		this._cheph = cheph;
		this._maoz = maoz;
		this._piz = piz;
		this._biaoz = biaoz;
		this._sud = sud;
		this._daozch = daozch;
		this._beiz = beiz;
	}
	public Guohxx(long id, String cheph, String maoz, String piz, String biaoz, String sud, String daozch, String koud, String beiz) {
		this._id = id;
		this._cheph = cheph;
		this._maoz = maoz;
		this._piz = piz;
		this._biaoz = biaoz;
		this._sud = sud;
		this._daozch = daozch;
		this._koud = koud;
		this._beiz = beiz;
	}
}
