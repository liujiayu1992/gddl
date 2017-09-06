var varRecord;
var v_Jiz = 0;
var v_Rulsl = 1;
var v_Rucdj = 2;
var v_Ruchdj = 3;
var v_Rez = 4;
var v_Shoudjqs = 5;
var v_Konggjhf = 6;
var v_Youpjdj = 7;
var v_Youpjrl = 8;

function getItem(field) {
	switch(field) {
		case 'RULSL':
			return v_Rulsl;
		case 'RUCDJ':
			return v_Rucdj;
		case 'RUCHDJ':
			return v_Ruchdj;
		case 'REZ':
			return v_Rez;
		case 'SHOUDJQS':
			return v_Shoudjqs;
		case 'KONGGJHF':
			return v_Konggjhf;
		case 'YOUPJDJ':
			return v_Youpjdj;
		case 'YOUPJRL':
			return v_Youpjrl;
	}
}

function getOriginalValue(record, field) {
	for (var i=0; i<varRecord.length; i++) {
		if (varRecord[i][v_Jiz]==record.get('YUEBJZB_ID')) {
			
			return varRecord[i][getItem(field)];
		}
	}
}

function afteredit(gridDiv_ds, e) {
	if (e.record.get('FENX') != '¿€º∆') {
		if (e.field == 'HAOYL'||e.field == 'RULSL'||e.field == 'YOUHYL') {
			var oldValue = eval(gridDiv_ds.getAt(e.row+1).get(e.field)||0);
			var changeValue = eval(e.value||0)-eval(e.originalValue||0);
			gridDiv_ds.getAt(e.row+1).set(e.field, eval(oldValue + changeValue));
			if (e.field == 'RULSL') {
				computeEveLj(gridDiv_ds, e.row, 'RUCDJ');
				computeEveLj(gridDiv_ds, e.row, 'RUCHDJ');
				computeEveLj(gridDiv_ds, e.row, 'REZ');
				computeEveLj(gridDiv_ds, e.row, 'SHOUDJQS');
				computeEveLj(gridDiv_ds, e.row, 'KONGGJHF');
				computeEveLj(gridDiv_ds, e.row, 'YOUPJDJ');
				computeEveLj(gridDiv_ds, e.row, 'YOUPJRL');
			}
		} else {
			computeEveLj(gridDiv_ds, e.row, e.field);
		}
	}
	cunputPingjdj(gridDiv_ds, e.row);
	cunputPingjdj(gridDiv_ds, e.row + 1);
	computeBiaomdj(gridDiv_ds, e.row);
	computeBiaomdj(gridDiv_ds, e.row+1);
}

function cunputPingjdj(gridDiv_ds, row) {
	var QiCL = eval(gridDiv_ds.getAt(row).get('QICL')||0);
	var QICDJ = eval(gridDiv_ds.getAt(row).get('QICDJ')||0);
	var RULSL = eval(gridDiv_ds.getAt(row).get('RULSL')||0);
	var RUCDJ = eval(gridDiv_ds.getAt(row).get('RUCDJ')||0);
	var RUCHDJ = eval(gridDiv_ds.getAt(row).get('RUCHDJ')||0);
	var PINGJDJ = 0;
	if (RULSL + QiCL > 0) {
		PINGJDJ = Round_new((QiCL*QICDJ + RULSL*(RUCDJ+RUCHDJ))/(RULSL + QiCL),3);
	}
	gridDiv_ds.getAt(row).set('PINGJJ', PINGJDJ)
}

function computeEveLj(gridDiv_ds, row, field) {
	var RULSL = eval(gridDiv_ds.getAt(row).get('RULSL')||0);
	var RULSLLj = eval(gridDiv_ds.getAt(row+1).get('RULSL')||0);
	var _bit = 2;
	if (field =='REZ') {
		_bit = 0;
	} else if (field =='SHOUDJQS') {
		_bit = 1;
	} else if (field == 'RUCDJ') {
		_bit = 3;
	}
	var varData = eval(gridDiv_ds.getAt(row).get(field)||0);
	var orgiData = eval(getOriginalValue(gridDiv_ds.getAt(row+1), field)||0);
	if (orgiData>0 && (RULSL!=RULSLLj)) {
		if (RULSLLj>0)		
			varData = (varData * RULSL + orgiData*(RULSLLj-RULSL)) / RULSLLj;
		else 
			varData = eval(gridDiv_ds.getAt(row+1).get(field)||0);
	}
	
	gridDiv_ds.getAt(row+1).set(field, Round_new(varData, _bit));
}

function computeBiaomdj(gridDiv_ds, row) {	
	var record = gridDiv_ds.getAt(row);
	var PINGJJ = eval(record.get('PINGJJ')||0);
	var REZ = eval(record.get('REZ')||0);
	var BIAOMDJ = 0;
	if (PINGJJ > 0 && REZ > 0) {
		BIAOMDJ = Round_new(PINGJJ*29271 / REZ, 2) ;
		gridDiv_ds.getAt(row).set('BIAOMDJ', BIAOMDJ);
	}
}

function beforeedit(gridDiv_ds, e, SelectLike) {
	if(e.record.get('FENX')=='¿€º∆') {
		if(!SelectLike.checked) {
			e.cancel=true;
		}		
	} 
}