var varRecord;
var v_Gongys = 0;
var v_Jihkj = 1;
var v_Yunsfs = 2;
var v_Hetl = 3;
var v_Meij = 4;
var v_Yunf = 5;
var v_Zaf = 6;
var v_Rez = 7;
var v_Shoudjqs = 8;
var v_Konggjhf = 9;

//function copyData() {
//	varRecord = new Array();
//	for (i=0; i<=gridDiv_ds.getCount()-1; i++) {
//		if (gridDiv_ds.getAt(i).get('FENX')=='ÀÛ¼Æ') {
//			varOneRecord = new Array();
//			varOneRecord[v_Hetl] = gridDiv_ds.getAt(i).get('HETL');
//			varOneRecord[v_Meij] = gridDiv_ds.getAt(i).get('MEIJ');
//			varOneRecord[v_Yunf] = gridDiv_ds.getAt(i).get('YUNF');
//			varOneRecord[v_Zaf] = gridDiv_ds.getAt(i).get('ZAF');
//			varOneRecord[v_Rez] = gridDiv_ds.getAt(i).get('REZ');
//			varOneRecord[v_Shoudjqs] = gridDiv_ds.getAt(i).get('SHOUDJQS');
//			varOneRecord[v_Konggjhf] = gridDiv_ds.getAt(i).get('KONGGJHF');
//			varRecord[i] = varOneRecord;
//		}		
//	}
//}

function getItem(field) {
	switch(field) {
		case 'HETL':
			return v_Hetl;
		case 'MEIJ':
			return v_Meij;
		case 'YUNF':
			return v_Yunf;
		case 'ZAF':
			return v_Zaf;
		case 'REZ':
			return v_Rez;
		case 'SHOUDJQS':
			return v_Shoudjqs;
		case 'KONGGJHF':
			return v_Konggjhf;
	}
}

function getOriginalValue(record, field) {
	for (var i=0; i<varRecord.length; i++) {
		if (varRecord[i][v_Gongys]==record.get('GONGYSB_ID')
			&& varRecord[i][v_Jihkj]==record.get('JIHKJB_ID')
			&& varRecord[i][v_Yunsfs]==record.get('YUNSFSB_ID')
			) {
			
			return varRecord[i][getItem(field)];
		}
	}
}

function afteredit(gridDiv_ds, e) {
	if (e.record.get('FENX') != 'ÀÛ¼Æ') {
		if (e.field == 'HETL') {
			var oldValue = eval(gridDiv_ds.getAt(e.row+1).get(e.field)||0);
			var changeValue = eval(e.value||0)-eval(e.originalValue||0);
			gridDiv_ds.getAt(e.row+1).set(e.field, eval(oldValue + changeValue));
			computeEveLj(gridDiv_ds, e.row, 'MEIJ');
			computeEveLj(gridDiv_ds, e.row, 'YUNF');
			computeEveLj(gridDiv_ds, e.row, 'ZAF');
			computeEveLj(gridDiv_ds, e.row, 'REZ');
			computeEveLj(gridDiv_ds, e.row, 'SHOUDJQS');
			computeEveLj(gridDiv_ds, e.row, 'KONGGJHF');		
		} else {
			computeEveLj(gridDiv_ds, e.row, e.field);
		}
		computeBiaomdj(gridDiv_ds, e.row);
		computeBiaomdj(gridDiv_ds, e.row+1);
	} else {
		computeBiaomdj(gridDiv_ds, e.row);
	}
}

function beforeedit(gridDiv_ds, e, SelectLike) {
	if(e.record.get('FENX')=='ÀÛ¼Æ') {
		if(!SelectLike.checked) {
			e.cancel=true;
		}		
	}	
}

function computeEveLj(gridDiv_ds, row, field) {
	var HETL = eval(gridDiv_ds.getAt(row).get('HETL')||0);
	var HETLLj = eval(gridDiv_ds.getAt(row+1).get('HETL')||0);
	var _bit = 2;
	if (field =='REZ') {
		_bit = 0;
	} else if (field =='SHOUDJQS') {
		_bit = 1;
	} else if (field == 'MEIJ') {
		_bit = 3;
	}
	var varData = eval(gridDiv_ds.getAt(row).get(field)||0);
	var orgiData = eval(getOriginalValue(gridDiv_ds.getAt(row+1), field)||0);
	if (orgiData>0 && (HETL!=HETLLj)) {
		if (HETLLj>0)		
			varData = (varData * HETL + orgiData*(HETLLj-HETL)) / HETLLj;
		else 
			varData = eval(gridDiv_ds.getAt(row+1).get(field)||0);
	}
	
	gridDiv_ds.getAt(row+1).set(field, Round_new(varData, _bit));
}

function computeBiaomdj(gridDiv_ds, row) {	
	var record = gridDiv_ds.getAt(row);
	var MEIJ = eval(record.get('MEIJ')||0);
	var YUNF = eval(record.get('YUNF')||0);
	var ZAF = eval(record.get('ZAF')||0);
	var REZ = eval(record.get('REZ')||0);
	var BIAOMDJ = 0;
	if (REZ > 0) {
		BIAOMDJ = Round_new((MEIJ+YUNF+ZAF)*29271 / REZ, 2) ;
		gridDiv_ds.getAt(row).set('BIAOMDJ', BIAOMDJ);
	}
}