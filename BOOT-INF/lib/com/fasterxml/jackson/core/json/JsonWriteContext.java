/*     */ package com.fasterxml.jackson.core.json;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonStreamContext;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JsonWriteContext
/*     */   extends JsonStreamContext
/*     */ {
/*     */   public static final int STATUS_OK_AS_IS = 0;
/*     */   public static final int STATUS_OK_AFTER_COMMA = 1;
/*     */   public static final int STATUS_OK_AFTER_COLON = 2;
/*     */   public static final int STATUS_OK_AFTER_SPACE = 3;
/*     */   public static final int STATUS_EXPECT_VALUE = 4;
/*     */   public static final int STATUS_EXPECT_NAME = 5;
/*     */   protected final JsonWriteContext _parent;
/*     */   protected DupDetector _dups;
/*     */   protected JsonWriteContext _child;
/*     */   protected String _currentName;
/*     */   protected Object _currentValue;
/*     */   protected boolean _gotName;
/*     */   
/*     */   protected JsonWriteContext(int type, JsonWriteContext parent, DupDetector dups)
/*     */   {
/*  71 */     this._type = type;
/*  72 */     this._parent = parent;
/*  73 */     this._dups = dups;
/*  74 */     this._index = -1;
/*     */   }
/*     */   
/*     */   protected JsonWriteContext reset(int type) {
/*  78 */     this._type = type;
/*  79 */     this._index = -1;
/*  80 */     this._currentName = null;
/*  81 */     this._gotName = false;
/*  82 */     this._currentValue = null;
/*  83 */     if (this._dups != null) this._dups.reset();
/*  84 */     return this;
/*     */   }
/*     */   
/*     */   public JsonWriteContext withDupDetector(DupDetector dups) {
/*  88 */     this._dups = dups;
/*  89 */     return this;
/*     */   }
/*     */   
/*     */   public Object getCurrentValue()
/*     */   {
/*  94 */     return this._currentValue;
/*     */   }
/*     */   
/*     */   public void setCurrentValue(Object v)
/*     */   {
/*  99 */     this._currentValue = v;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static JsonWriteContext createRootContext()
/*     */   {
/* 112 */     return createRootContext(null);
/*     */   }
/*     */   
/* 115 */   public static JsonWriteContext createRootContext(DupDetector dd) { return new JsonWriteContext(0, null, dd); }
/*     */   
/*     */   public JsonWriteContext createChildArrayContext()
/*     */   {
/* 119 */     JsonWriteContext ctxt = this._child;
/* 120 */     if (ctxt == null) {
/* 121 */       this._child = (ctxt = new JsonWriteContext(1, this, this._dups == null ? null : this._dups.child()));
/* 122 */       return ctxt;
/*     */     }
/* 124 */     return ctxt.reset(1);
/*     */   }
/*     */   
/*     */   public JsonWriteContext createChildObjectContext() {
/* 128 */     JsonWriteContext ctxt = this._child;
/* 129 */     if (ctxt == null) {
/* 130 */       this._child = (ctxt = new JsonWriteContext(2, this, this._dups == null ? null : this._dups.child()));
/* 131 */       return ctxt;
/*     */     }
/* 133 */     return ctxt.reset(2);
/*     */   }
/*     */   
/* 136 */   public final JsonWriteContext getParent() { return this._parent; }
/* 137 */   public final String getCurrentName() { return this._currentName; }
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
/*     */   public JsonWriteContext clearAndGetParent()
/*     */   {
/* 150 */     this._currentValue = null;
/*     */     
/* 152 */     return this._parent;
/*     */   }
/*     */   
/*     */   public DupDetector getDupDetector() {
/* 156 */     return this._dups;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int writeFieldName(String name)
/*     */     throws JsonProcessingException
/*     */   {
/* 165 */     if ((this._type != 2) || (this._gotName)) {
/* 166 */       return 4;
/*     */     }
/* 168 */     this._gotName = true;
/* 169 */     this._currentName = name;
/* 170 */     if (this._dups != null) _checkDup(this._dups, name);
/* 171 */     return this._index < 0 ? 0 : 1;
/*     */   }
/*     */   
/*     */   private final void _checkDup(DupDetector dd, String name) throws JsonProcessingException {
/* 175 */     if (dd.isDup(name)) {
/* 176 */       Object src = dd.getSource();
/* 177 */       throw new JsonGenerationException("Duplicate field '" + name + "'", (src instanceof JsonGenerator) ? (JsonGenerator)src : null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int writeValue()
/*     */   {
/* 184 */     if (this._type == 2) {
/* 185 */       if (!this._gotName) {
/* 186 */         return 5;
/*     */       }
/* 188 */       this._gotName = false;
/* 189 */       this._index += 1;
/* 190 */       return 2;
/*     */     }
/*     */     
/*     */ 
/* 194 */     if (this._type == 1) {
/* 195 */       int ix = this._index;
/* 196 */       this._index += 1;
/* 197 */       return ix < 0 ? 0 : 1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 202 */     this._index += 1;
/* 203 */     return this._index == 0 ? 0 : 3;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void appendDesc(StringBuilder sb)
/*     */   {
/* 209 */     if (this._type == 2) {
/* 210 */       sb.append('{');
/* 211 */       if (this._currentName != null) {
/* 212 */         sb.append('"');
/*     */         
/* 214 */         sb.append(this._currentName);
/* 215 */         sb.append('"');
/*     */       } else {
/* 217 */         sb.append('?');
/*     */       }
/* 219 */       sb.append('}');
/* 220 */     } else if (this._type == 1) {
/* 221 */       sb.append('[');
/* 222 */       sb.append(getCurrentIndex());
/* 223 */       sb.append(']');
/*     */     }
/*     */     else {
/* 226 */       sb.append("/");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 237 */     StringBuilder sb = new StringBuilder(64);
/* 238 */     appendDesc(sb);
/* 239 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\json\JsonWriteContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */