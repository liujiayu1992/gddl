Ext.namespace("Ext.zr");    
Ext.namespace("Ext.zr.data");    
   
/* Paging Memory Proxy, allows to use paging grid with in memory dataset */   
Ext.zr.data.PagingMemoryProxy = function(data, config) {    
    Ext.zr.data.PagingMemoryProxy.superclass.constructor.call(this);    
    this.data = data;    
    Ext.apply(this, config);    
};    
   
Ext.extend(Ext.zr.data.PagingMemoryProxy, Ext.data.MemoryProxy, {    
    customFilter: null,    
        
    load : function(params, reader, callback, scope, arg) {    
        params = params || {};    
        var result;    
        try {    
            result = reader.readRecords(this.data);    
        } catch(e) {    
            this.fireEvent("loadexception", this, arg, null, e);    
            callback.call(scope, null, arg, false);    
            return;    
        }    
            
        // filtering    
        if (this.customFilter!=null) {    
            result.records = result.records.filter(this.customFilter);    
            result.totalRecords = result.records.length;    
        } else if (params.filter!==undefined) {    
            result.records = result.records.filter(function(el){    
                if (typeof(el)=="object"){    
                    var att = params.filterCol || 0;    
                    return String(el.data[att]).match(params.filter)?true:false;    
                } else {    
                    return String(el).match(params.filter)?true:false;    
                }    
            });    
            result.totalRecords = result.records.length;    
        }    
            
        // sorting    
        if (params.sort!==undefined) {    
            // use integer as params.sort to specify column, since arrays are not named    
            // params.sort=0; would also match a array without columns    
            var dir = String(params.dir).toUpperCase() == "DESC" ? -1 : 1;    
                var fn = function(r1, r2){    
                return r1==r2 ? 0 : r1 
                };    
            var st = reader.recordType.getField(params.sort).sortType;    
            result.records.sort(function(a, b) {    
                var v = 0;    
                if (typeof(a)=="object"){    
                    v = fn(st(a.data[params.sort]), st(b.data[params.sort])) * dir;    
                } else {    
                    v = fn(a, b) * dir;    
                }    
                if (v==0) {    
                    v = (a.index < b.index ? -1 : 1);    
                }    
                return v;    
            });    
        }    
   
        // paging (use undefined cause start can also be 0 (thus false))    
        if (params.start!==undefined && params.limit!==undefined) {    
            result.records = result.records.slice(params.start, params.start+params.limit);    
        }    
            
        callback.call(scope, result, arg, true);    
    }    
});    