/*     */ package com.fasterxml.jackson.core;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ public class JsonLocation
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  22 */   public static final JsonLocation NA = new JsonLocation("N/A", -1L, -1L, -1, -1);
/*     */   
/*     */ 
/*     */ 
/*     */   final long _totalBytes;
/*     */   
/*     */ 
/*     */   final long _totalChars;
/*     */   
/*     */ 
/*     */   final int _lineNr;
/*     */   
/*     */ 
/*     */   final int _columnNr;
/*     */   
/*     */ 
/*     */   final transient Object _sourceRef;
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonLocation(Object srcRef, long totalChars, int lineNr, int colNr)
/*     */   {
/*  44 */     this(srcRef, -1L, totalChars, lineNr, colNr);
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonLocation(Object sourceRef, long totalBytes, long totalChars, int lineNr, int columnNr)
/*     */   {
/*  50 */     this._sourceRef = sourceRef;
/*  51 */     this._totalBytes = totalBytes;
/*  52 */     this._totalChars = totalChars;
/*  53 */     this._lineNr = lineNr;
/*  54 */     this._columnNr = columnNr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getSourceRef()
/*     */   {
/*  65 */     return this._sourceRef;
/*     */   }
/*     */   
/*     */   public int getLineNr()
/*     */   {
/*  70 */     return this._lineNr;
/*     */   }
/*     */   
/*     */   public int getColumnNr()
/*     */   {
/*  75 */     return this._columnNr;
/*     */   }
/*     */   
/*     */ 
/*     */   public long getCharOffset()
/*     */   {
/*  81 */     return this._totalChars;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getByteOffset()
/*     */   {
/*  89 */     return this._totalBytes;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/*  95 */     StringBuilder sb = new StringBuilder(80);
/*  96 */     sb.append("[Source: ");
/*  97 */     if (this._sourceRef == null) {
/*  98 */       sb.append("UNKNOWN");
/*     */     } else {
/* 100 */       sb.append(this._sourceRef.toString());
/*     */     }
/* 102 */     sb.append("; line: ");
/* 103 */     sb.append(this._lineNr);
/* 104 */     sb.append(", column: ");
/* 105 */     sb.append(this._columnNr);
/* 106 */     sb.append(']');
/* 107 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 113 */     int hash = this._sourceRef == null ? 1 : this._sourceRef.hashCode();
/* 114 */     hash ^= this._lineNr;
/* 115 */     hash += this._columnNr;
/* 116 */     hash ^= (int)this._totalChars;
/* 117 */     hash += (int)this._totalBytes;
/* 118 */     return hash;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 124 */     if (other == this) return true;
/* 125 */     if (other == null) return false;
/* 126 */     if (!(other instanceof JsonLocation)) return false;
/* 127 */     JsonLocation otherLoc = (JsonLocation)other;
/*     */     
/* 129 */     if (this._sourceRef == null) {
/* 130 */       if (otherLoc._sourceRef != null) return false;
/* 131 */     } else if (!this._sourceRef.equals(otherLoc._sourceRef)) { return false;
/*     */     }
/* 133 */     return (this._lineNr == otherLoc._lineNr) && (this._columnNr == otherLoc._columnNr) && (this._totalChars == otherLoc._totalChars) && (getByteOffset() == otherLoc.getByteOffset());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\JsonLocation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */