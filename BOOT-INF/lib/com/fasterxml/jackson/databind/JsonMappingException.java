/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonLocation;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ public class JsonMappingException
/*     */   extends JsonProcessingException
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   static final int MAX_REFS_TO_LIST = 1000;
/*     */   protected LinkedList<Reference> _path;
/*     */   protected transient Closeable _processor;
/*     */   
/*     */   public static class Reference
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 2L;
/*     */     protected transient Object _from;
/*     */     protected String _fieldName;
/*  66 */     protected int _index = -1;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected String _desc;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected Reference() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  83 */     public Reference(Object from) { this._from = from; }
/*     */     
/*     */     public Reference(Object from, String fieldName) {
/*  86 */       this._from = from;
/*  87 */       if (fieldName == null) {
/*  88 */         throw new NullPointerException("Can not pass null fieldName");
/*     */       }
/*  90 */       this._fieldName = fieldName;
/*     */     }
/*     */     
/*     */     public Reference(Object from, int index) {
/*  94 */       this._from = from;
/*  95 */       this._index = index;
/*     */     }
/*     */     
/*     */ 
/*  99 */     void setFieldName(String n) { this._fieldName = n; }
/* 100 */     void setIndex(int ix) { this._index = ix; }
/* 101 */     void setDescription(String d) { this._desc = d; }
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
/*     */     @JsonIgnore
/* 114 */     public Object getFrom() { return this._from; }
/*     */     
/* 116 */     public String getFieldName() { return this._fieldName; }
/* 117 */     public int getIndex() { return this._index; }
/*     */     
/* 119 */     public String getDescription() { if (this._desc == null) {
/* 120 */         StringBuilder sb = new StringBuilder();
/*     */         
/* 122 */         if (this._from == null) {
/* 123 */           sb.append("UNKNOWN");
/*     */         } else {
/* 125 */           Class<?> cls = (this._from instanceof Class) ? (Class)this._from : this._from.getClass();
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 130 */           int arrays = 0;
/* 131 */           while (cls.isArray()) {
/* 132 */             cls = cls.getComponentType();
/* 133 */             arrays++;
/*     */           }
/* 135 */           sb.append(cls.getName());
/* 136 */           for (;;) { arrays--; if (arrays < 0) break;
/* 137 */             sb.append("[]");
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 147 */         sb.append('[');
/* 148 */         if (this._fieldName != null) {
/* 149 */           sb.append('"');
/* 150 */           sb.append(this._fieldName);
/* 151 */           sb.append('"');
/* 152 */         } else if (this._index >= 0) {
/* 153 */           sb.append(this._index);
/*     */         } else {
/* 155 */           sb.append('?');
/*     */         }
/* 157 */         sb.append(']');
/* 158 */         this._desc = sb.toString();
/*     */       }
/* 160 */       return this._desc;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 165 */       return getDescription();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     Object writeReplace()
/*     */     {
/* 176 */       getDescription();
/* 177 */       return this;
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
/*     */   @Deprecated
/*     */   public JsonMappingException(String msg)
/*     */   {
/* 213 */     super(msg);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public JsonMappingException(String msg, Throwable rootCause)
/*     */   {
/* 219 */     super(msg, rootCause);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public JsonMappingException(String msg, JsonLocation loc)
/*     */   {
/* 225 */     super(msg, loc);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public JsonMappingException(String msg, JsonLocation loc, Throwable rootCause)
/*     */   {
/* 231 */     super(msg, loc, rootCause);
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonMappingException(Closeable processor, String msg)
/*     */   {
/* 237 */     super(msg);
/* 238 */     this._processor = processor;
/* 239 */     if ((processor instanceof JsonParser))
/*     */     {
/*     */ 
/*     */ 
/* 243 */       this._location = ((JsonParser)processor).getTokenLocation();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonMappingException(Closeable processor, String msg, Throwable problem)
/*     */   {
/* 251 */     super(msg, problem);
/* 252 */     this._processor = processor;
/* 253 */     if ((processor instanceof JsonParser)) {
/* 254 */       this._location = ((JsonParser)processor).getTokenLocation();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonMappingException(Closeable processor, String msg, JsonLocation loc)
/*     */   {
/* 262 */     super(msg, loc);
/* 263 */     this._processor = processor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static JsonMappingException from(JsonParser p, String msg)
/*     */   {
/* 270 */     return new JsonMappingException(p, msg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static JsonMappingException from(JsonParser p, String msg, Throwable problem)
/*     */   {
/* 277 */     return new JsonMappingException(p, msg, problem);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static JsonMappingException from(JsonGenerator g, String msg)
/*     */   {
/* 284 */     return new JsonMappingException(g, msg, (Throwable)null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static JsonMappingException from(JsonGenerator g, String msg, Throwable problem)
/*     */   {
/* 291 */     return new JsonMappingException(g, msg, problem);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static JsonMappingException from(DeserializationContext ctxt, String msg)
/*     */   {
/* 298 */     return new JsonMappingException(ctxt.getParser(), msg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static JsonMappingException from(DeserializationContext ctxt, String msg, Throwable t)
/*     */   {
/* 305 */     return new JsonMappingException(ctxt.getParser(), msg, t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static JsonMappingException from(SerializerProvider ctxt, String msg)
/*     */   {
/* 312 */     return new JsonMappingException(ctxt.getGenerator(), msg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JsonMappingException from(SerializerProvider ctxt, String msg, Throwable problem)
/*     */   {
/* 322 */     return new JsonMappingException(ctxt.getGenerator(), msg, problem);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JsonMappingException fromUnexpectedIOE(IOException src)
/*     */   {
/* 333 */     return new JsonMappingException(null, String.format("Unexpected IOException (of type %s): %s", new Object[] { src.getClass().getName(), src.getMessage() }));
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
/*     */   public static JsonMappingException wrapWithPath(Throwable src, Object refFrom, String refFieldName)
/*     */   {
/* 348 */     return wrapWithPath(src, new Reference(refFrom, refFieldName));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JsonMappingException wrapWithPath(Throwable src, Object refFrom, int index)
/*     */   {
/* 360 */     return wrapWithPath(src, new Reference(refFrom, index));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static JsonMappingException wrapWithPath(Throwable src, Reference ref)
/*     */   {
/*     */     JsonMappingException jme;
/*     */     
/*     */ 
/*     */     JsonMappingException jme;
/*     */     
/* 372 */     if ((src instanceof JsonMappingException)) {
/* 373 */       jme = (JsonMappingException)src;
/*     */     } else {
/* 375 */       String msg = src.getMessage();
/*     */       
/* 377 */       if ((msg == null) || (msg.length() == 0)) {
/* 378 */         msg = "(was " + src.getClass().getName() + ")";
/*     */       }
/*     */       
/* 381 */       Closeable proc = null;
/* 382 */       if ((src instanceof JsonProcessingException)) {
/* 383 */         Object proc0 = ((JsonProcessingException)src).getProcessor();
/* 384 */         if ((proc0 instanceof Closeable)) {
/* 385 */           proc = (Closeable)proc0;
/*     */         }
/*     */       }
/* 388 */       jme = new JsonMappingException(proc, msg, src);
/*     */     }
/* 390 */     jme.prependPath(ref);
/* 391 */     return jme;
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
/*     */   public List<Reference> getPath()
/*     */   {
/* 406 */     if (this._path == null) {
/* 407 */       return Collections.emptyList();
/*     */     }
/* 409 */     return Collections.unmodifiableList(this._path);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPathReference()
/*     */   {
/* 418 */     return getPathReference(new StringBuilder()).toString();
/*     */   }
/*     */   
/*     */   public StringBuilder getPathReference(StringBuilder sb)
/*     */   {
/* 423 */     _appendPathDesc(sb);
/* 424 */     return sb;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void prependPath(Object referrer, String fieldName)
/*     */   {
/* 433 */     Reference ref = new Reference(referrer, fieldName);
/* 434 */     prependPath(ref);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void prependPath(Object referrer, int index)
/*     */   {
/* 442 */     Reference ref = new Reference(referrer, index);
/* 443 */     prependPath(ref);
/*     */   }
/*     */   
/*     */   public void prependPath(Reference r)
/*     */   {
/* 448 */     if (this._path == null) {
/* 449 */       this._path = new LinkedList();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 455 */     if (this._path.size() < 1000) {
/* 456 */       this._path.addFirst(r);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @JsonIgnore
/*     */   public Object getProcessor()
/*     */   {
/* 468 */     return this._processor;
/*     */   }
/*     */   
/*     */   public String getLocalizedMessage() {
/* 472 */     return _buildMessage();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 481 */     return _buildMessage();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String _buildMessage()
/*     */   {
/* 489 */     String msg = super.getMessage();
/* 490 */     if (this._path == null) {
/* 491 */       return msg;
/*     */     }
/* 493 */     StringBuilder sb = msg == null ? new StringBuilder() : new StringBuilder(msg);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 499 */     sb.append(" (through reference chain: ");
/* 500 */     sb = getPathReference(sb);
/* 501 */     sb.append(')');
/* 502 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 508 */     return getClass().getName() + ": " + getMessage();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void _appendPathDesc(StringBuilder sb)
/*     */   {
/* 519 */     if (this._path == null) {
/* 520 */       return;
/*     */     }
/* 522 */     Iterator<Reference> it = this._path.iterator();
/* 523 */     while (it.hasNext()) {
/* 524 */       sb.append(((Reference)it.next()).toString());
/* 525 */       if (it.hasNext()) {
/* 526 */         sb.append("->");
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\JsonMappingException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */