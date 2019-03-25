/*     */ package com.fasterxml.jackson.core.filter;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonStreamContext;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TokenFilterContext
/*     */   extends JsonStreamContext
/*     */ {
/*     */   protected final TokenFilterContext _parent;
/*     */   protected TokenFilterContext _child;
/*     */   protected String _currentName;
/*     */   protected TokenFilter _filter;
/*     */   protected boolean _startHandled;
/*     */   protected boolean _needToHandleName;
/*     */   
/*     */   protected TokenFilterContext(int type, TokenFilterContext parent, TokenFilter filter, boolean startHandled)
/*     */   {
/*  72 */     this._type = type;
/*  73 */     this._parent = parent;
/*  74 */     this._filter = filter;
/*  75 */     this._index = -1;
/*  76 */     this._startHandled = startHandled;
/*  77 */     this._needToHandleName = false;
/*     */   }
/*     */   
/*     */ 
/*     */   protected TokenFilterContext reset(int type, TokenFilter filter, boolean startWritten)
/*     */   {
/*  83 */     this._type = type;
/*  84 */     this._filter = filter;
/*  85 */     this._index = -1;
/*  86 */     this._currentName = null;
/*  87 */     this._startHandled = startWritten;
/*  88 */     this._needToHandleName = false;
/*  89 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TokenFilterContext createRootContext(TokenFilter filter)
/*     */   {
/* 100 */     return new TokenFilterContext(0, null, filter, true);
/*     */   }
/*     */   
/*     */   public TokenFilterContext createChildArrayContext(TokenFilter filter, boolean writeStart) {
/* 104 */     TokenFilterContext ctxt = this._child;
/* 105 */     if (ctxt == null) {
/* 106 */       this._child = (ctxt = new TokenFilterContext(1, this, filter, writeStart));
/* 107 */       return ctxt;
/*     */     }
/* 109 */     return ctxt.reset(1, filter, writeStart);
/*     */   }
/*     */   
/*     */   public TokenFilterContext createChildObjectContext(TokenFilter filter, boolean writeStart) {
/* 113 */     TokenFilterContext ctxt = this._child;
/* 114 */     if (ctxt == null) {
/* 115 */       this._child = (ctxt = new TokenFilterContext(2, this, filter, writeStart));
/* 116 */       return ctxt;
/*     */     }
/* 118 */     return ctxt.reset(2, filter, writeStart);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TokenFilter setFieldName(String name)
/*     */     throws JsonProcessingException
/*     */   {
/* 128 */     this._currentName = name;
/* 129 */     this._needToHandleName = true;
/* 130 */     return this._filter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TokenFilter checkValue(TokenFilter filter)
/*     */   {
/* 139 */     if (this._type == 2) {
/* 140 */       return filter;
/*     */     }
/*     */     
/* 143 */     int ix = ++this._index;
/* 144 */     if (this._type == 1) {
/* 145 */       return filter.includeElement(ix);
/*     */     }
/* 147 */     return filter.includeRootValue(ix);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writePath(JsonGenerator gen)
/*     */     throws IOException
/*     */   {
/* 156 */     if ((this._filter == null) || (this._filter == TokenFilter.INCLUDE_ALL)) {
/* 157 */       return;
/*     */     }
/* 159 */     if (this._parent != null) {
/* 160 */       this._parent._writePath(gen);
/*     */     }
/* 162 */     if (this._startHandled)
/*     */     {
/* 164 */       if (this._needToHandleName) {
/* 165 */         gen.writeFieldName(this._currentName);
/*     */       }
/*     */     } else {
/* 168 */       this._startHandled = true;
/* 169 */       if (this._type == 2) {
/* 170 */         gen.writeStartObject();
/* 171 */         gen.writeFieldName(this._currentName);
/* 172 */       } else if (this._type == 1) {
/* 173 */         gen.writeStartArray();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeImmediatePath(JsonGenerator gen)
/*     */     throws IOException
/*     */   {
/* 186 */     if ((this._filter == null) || (this._filter == TokenFilter.INCLUDE_ALL)) {
/* 187 */       return;
/*     */     }
/* 189 */     if (this._startHandled)
/*     */     {
/* 191 */       if (this._needToHandleName) {
/* 192 */         gen.writeFieldName(this._currentName);
/*     */       }
/*     */     } else {
/* 195 */       this._startHandled = true;
/* 196 */       if (this._type == 2) {
/* 197 */         gen.writeStartObject();
/* 198 */         if (this._needToHandleName) {
/* 199 */           gen.writeFieldName(this._currentName);
/*     */         }
/* 201 */       } else if (this._type == 1) {
/* 202 */         gen.writeStartArray();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void _writePath(JsonGenerator gen) throws IOException
/*     */   {
/* 209 */     if ((this._filter == null) || (this._filter == TokenFilter.INCLUDE_ALL)) {
/* 210 */       return;
/*     */     }
/* 212 */     if (this._parent != null) {
/* 213 */       this._parent._writePath(gen);
/*     */     }
/* 215 */     if (this._startHandled)
/*     */     {
/* 217 */       if (this._needToHandleName) {
/* 218 */         this._needToHandleName = false;
/* 219 */         gen.writeFieldName(this._currentName);
/*     */       }
/*     */     } else {
/* 222 */       this._startHandled = true;
/* 223 */       if (this._type == 2) {
/* 224 */         gen.writeStartObject();
/* 225 */         if (this._needToHandleName) {
/* 226 */           this._needToHandleName = false;
/* 227 */           gen.writeFieldName(this._currentName);
/*     */         }
/* 229 */       } else if (this._type == 1) {
/* 230 */         gen.writeStartArray();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public TokenFilterContext closeArray(JsonGenerator gen) throws IOException
/*     */   {
/* 237 */     if (this._startHandled) {
/* 238 */       gen.writeEndArray();
/*     */     }
/* 240 */     if ((this._filter != null) && (this._filter != TokenFilter.INCLUDE_ALL)) {
/* 241 */       this._filter.filterFinishArray();
/*     */     }
/* 243 */     return this._parent;
/*     */   }
/*     */   
/*     */   public TokenFilterContext closeObject(JsonGenerator gen) throws IOException
/*     */   {
/* 248 */     if (this._startHandled) {
/* 249 */       gen.writeEndObject();
/*     */     }
/* 251 */     if ((this._filter != null) && (this._filter != TokenFilter.INCLUDE_ALL)) {
/* 252 */       this._filter.filterFinishObject();
/*     */     }
/* 254 */     return this._parent;
/*     */   }
/*     */   
/*     */   public void skipParentChecks() {
/* 258 */     this._filter = null;
/* 259 */     for (TokenFilterContext ctxt = this._parent; ctxt != null; ctxt = ctxt._parent) {
/* 260 */       this._parent._filter = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getCurrentValue()
/*     */   {
/* 271 */     return null;
/*     */   }
/*     */   
/*     */   public void setCurrentValue(Object v) {}
/*     */   
/* 276 */   public final TokenFilterContext getParent() { return this._parent; }
/* 277 */   public final String getCurrentName() { return this._currentName; }
/*     */   
/* 279 */   public TokenFilter getFilter() { return this._filter; }
/* 280 */   public boolean isStartHandled() { return this._startHandled; }
/*     */   
/*     */   public JsonToken nextTokenToRead() {
/* 283 */     if (!this._startHandled) {
/* 284 */       this._startHandled = true;
/* 285 */       if (this._type == 2) {
/* 286 */         return JsonToken.START_OBJECT;
/*     */       }
/*     */       
/* 289 */       return JsonToken.START_ARRAY;
/*     */     }
/*     */     
/* 292 */     if ((this._needToHandleName) && (this._type == 2)) {
/* 293 */       this._needToHandleName = false;
/* 294 */       return JsonToken.FIELD_NAME;
/*     */     }
/* 296 */     return null;
/*     */   }
/*     */   
/*     */   public TokenFilterContext findChildOf(TokenFilterContext parent) {
/* 300 */     if (this._parent == parent) {
/* 301 */       return this;
/*     */     }
/* 303 */     TokenFilterContext curr = this._parent;
/* 304 */     while (curr != null) {
/* 305 */       TokenFilterContext p = curr._parent;
/* 306 */       if (p == parent) {
/* 307 */         return curr;
/*     */       }
/* 309 */       curr = p;
/*     */     }
/*     */     
/* 312 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void appendDesc(StringBuilder sb)
/*     */   {
/* 318 */     if (this._parent != null) {
/* 319 */       this._parent.appendDesc(sb);
/*     */     }
/* 321 */     if (this._type == 2) {
/* 322 */       sb.append('{');
/* 323 */       if (this._currentName != null) {
/* 324 */         sb.append('"');
/*     */         
/* 326 */         sb.append(this._currentName);
/* 327 */         sb.append('"');
/*     */       } else {
/* 329 */         sb.append('?');
/*     */       }
/* 331 */       sb.append('}');
/* 332 */     } else if (this._type == 1) {
/* 333 */       sb.append('[');
/* 334 */       sb.append(getCurrentIndex());
/* 335 */       sb.append(']');
/*     */     }
/*     */     else {
/* 338 */       sb.append("/");
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
/* 349 */     StringBuilder sb = new StringBuilder(64);
/* 350 */     appendDesc(sb);
/* 351 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\filter\TokenFilterContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */