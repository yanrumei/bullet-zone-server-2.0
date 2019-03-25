/*     */ package com.fasterxml.jackson.core.json;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonLocation;
/*     */ import com.fasterxml.jackson.core.JsonParseException;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonStreamContext;
/*     */ import com.fasterxml.jackson.core.io.CharTypes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JsonReadContext
/*     */   extends JsonStreamContext
/*     */ {
/*     */   protected final JsonReadContext _parent;
/*     */   protected DupDetector _dups;
/*     */   protected JsonReadContext _child;
/*     */   protected String _currentName;
/*     */   protected Object _currentValue;
/*     */   protected int _lineNr;
/*     */   protected int _columnNr;
/*     */   
/*     */   public JsonReadContext(JsonReadContext parent, DupDetector dups, int type, int lineNr, int colNr)
/*     */   {
/*  59 */     this._parent = parent;
/*  60 */     this._dups = dups;
/*  61 */     this._type = type;
/*  62 */     this._lineNr = lineNr;
/*  63 */     this._columnNr = colNr;
/*  64 */     this._index = -1;
/*     */   }
/*     */   
/*     */   protected void reset(int type, int lineNr, int colNr) {
/*  68 */     this._type = type;
/*  69 */     this._index = -1;
/*  70 */     this._lineNr = lineNr;
/*  71 */     this._columnNr = colNr;
/*  72 */     this._currentName = null;
/*  73 */     this._currentValue = null;
/*  74 */     if (this._dups != null) {
/*  75 */       this._dups.reset();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonReadContext withDupDetector(DupDetector dups)
/*     */   {
/*  86 */     this._dups = dups;
/*  87 */     return this;
/*     */   }
/*     */   
/*     */   public Object getCurrentValue()
/*     */   {
/*  92 */     return this._currentValue;
/*     */   }
/*     */   
/*     */   public void setCurrentValue(Object v)
/*     */   {
/*  97 */     this._currentValue = v;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JsonReadContext createRootContext(int lineNr, int colNr, DupDetector dups)
/*     */   {
/* 107 */     return new JsonReadContext(null, dups, 0, lineNr, colNr);
/*     */   }
/*     */   
/*     */   public static JsonReadContext createRootContext(DupDetector dups) {
/* 111 */     return new JsonReadContext(null, dups, 0, 1, 0);
/*     */   }
/*     */   
/*     */   public JsonReadContext createChildArrayContext(int lineNr, int colNr) {
/* 115 */     JsonReadContext ctxt = this._child;
/* 116 */     if (ctxt == null) {
/* 117 */       this._child = (ctxt = new JsonReadContext(this, this._dups == null ? null : this._dups.child(), 1, lineNr, colNr));
/*     */     }
/*     */     else {
/* 120 */       ctxt.reset(1, lineNr, colNr);
/*     */     }
/* 122 */     return ctxt;
/*     */   }
/*     */   
/*     */   public JsonReadContext createChildObjectContext(int lineNr, int colNr) {
/* 126 */     JsonReadContext ctxt = this._child;
/* 127 */     if (ctxt == null) {
/* 128 */       this._child = (ctxt = new JsonReadContext(this, this._dups == null ? null : this._dups.child(), 2, lineNr, colNr));
/*     */       
/* 130 */       return ctxt;
/*     */     }
/* 132 */     ctxt.reset(2, lineNr, colNr);
/* 133 */     return ctxt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 142 */   public String getCurrentName() { return this._currentName; }
/* 143 */   public JsonReadContext getParent() { return this._parent; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonReadContext clearAndGetParent()
/*     */   {
/* 156 */     this._currentValue = null;
/*     */     
/* 158 */     return this._parent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonLocation getStartLocation(Object srcRef)
/*     */   {
/* 173 */     long totalChars = -1L;
/* 174 */     return new JsonLocation(srcRef, totalChars, this._lineNr, this._columnNr);
/*     */   }
/*     */   
/*     */   public DupDetector getDupDetector() {
/* 178 */     return this._dups;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean expectComma()
/*     */   {
/* 192 */     int ix = ++this._index;
/* 193 */     return (this._type != 0) && (ix > 0);
/*     */   }
/*     */   
/*     */   public void setCurrentName(String name) throws JsonProcessingException {
/* 197 */     this._currentName = name;
/* 198 */     if (this._dups != null) _checkDup(this._dups, name);
/*     */   }
/*     */   
/*     */   private void _checkDup(DupDetector dd, String name) throws JsonProcessingException {
/* 202 */     if (dd.isDup(name)) {
/* 203 */       Object src = dd.getSource();
/* 204 */       throw new JsonParseException((src instanceof JsonParser) ? (JsonParser)src : null, "Duplicate field '" + name + "'");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 221 */     StringBuilder sb = new StringBuilder(64);
/* 222 */     switch (this._type) {
/*     */     case 0: 
/* 224 */       sb.append("/");
/* 225 */       break;
/*     */     case 1: 
/* 227 */       sb.append('[');
/* 228 */       sb.append(getCurrentIndex());
/* 229 */       sb.append(']');
/* 230 */       break;
/*     */     case 2: 
/*     */     default: 
/* 233 */       sb.append('{');
/* 234 */       if (this._currentName != null) {
/* 235 */         sb.append('"');
/* 236 */         CharTypes.appendQuoted(sb, this._currentName);
/* 237 */         sb.append('"');
/*     */       } else {
/* 239 */         sb.append('?');
/*     */       }
/* 241 */       sb.append('}');
/*     */     }
/*     */     
/* 244 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\json\JsonReadContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */