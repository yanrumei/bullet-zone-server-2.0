/*     */ package org.apache.tomcat.util.buf;
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
/*     */ public abstract class AbstractChunk
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public static final int ARRAY_MAX_SIZE = 2147483639;
/*  36 */   private int hashCode = 0;
/*  37 */   protected boolean hasHashCode = false;
/*     */   
/*     */   protected boolean isSet;
/*     */   
/*  41 */   private int limit = -1;
/*     */   
/*     */ 
/*     */ 
/*     */   protected int start;
/*     */   
/*     */ 
/*     */ 
/*     */   protected int end;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLimit(int limit)
/*     */   {
/*  56 */     this.limit = limit;
/*     */   }
/*     */   
/*     */   public int getLimit()
/*     */   {
/*  61 */     return this.limit;
/*     */   }
/*     */   
/*     */   protected int getLimitInternal()
/*     */   {
/*  66 */     if (this.limit > 0) {
/*  67 */       return this.limit;
/*     */     }
/*  69 */     return 2147483639;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getStart()
/*     */   {
/*  78 */     return this.start;
/*     */   }
/*     */   
/*     */   public int getEnd()
/*     */   {
/*  83 */     return this.end;
/*     */   }
/*     */   
/*     */   public void setEnd(int i)
/*     */   {
/*  88 */     this.end = i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getOffset()
/*     */   {
/*  95 */     return this.start;
/*     */   }
/*     */   
/*     */   public void setOffset(int off)
/*     */   {
/* 100 */     if (this.end < off) {
/* 101 */       this.end = off;
/*     */     }
/* 103 */     this.start = off;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 111 */     return this.end - this.start;
/*     */   }
/*     */   
/*     */   public boolean isNull()
/*     */   {
/* 116 */     if (this.end > 0) {
/* 117 */       return false;
/*     */     }
/* 119 */     return !this.isSet;
/*     */   }
/*     */   
/*     */   public int indexOf(String src, int srcOff, int srcLen, int myOff)
/*     */   {
/* 124 */     char first = src.charAt(srcOff);
/*     */     
/*     */ 
/* 127 */     int srcEnd = srcOff + srcLen;
/*     */     label96:
/* 129 */     for (int i = myOff + this.start; i <= this.end - srcLen; i++)
/* 130 */       if (getBufferElement(i) == first)
/*     */       {
/*     */ 
/*     */ 
/* 134 */         int myPos = i + 1;
/* 135 */         for (int srcPos = srcOff + 1; srcPos < srcEnd;) {
/* 136 */           if (getBufferElement(myPos++) != src.charAt(srcPos++)) {
/*     */             break label96;
/*     */           }
/*     */         }
/* 140 */         return i - this.start;
/*     */       }
/* 142 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void recycle()
/*     */   {
/* 150 */     this.hasHashCode = false;
/* 151 */     this.isSet = false;
/* 152 */     this.start = 0;
/* 153 */     this.end = 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 159 */     if (this.hasHashCode) {
/* 160 */       return this.hashCode;
/*     */     }
/* 162 */     int code = 0;
/*     */     
/* 164 */     code = hash();
/* 165 */     this.hashCode = code;
/* 166 */     this.hasHashCode = true;
/* 167 */     return code;
/*     */   }
/*     */   
/*     */   public int hash()
/*     */   {
/* 172 */     int code = 0;
/* 173 */     for (int i = this.start; i < this.end; i++) {
/* 174 */       code = code * 37 + getBufferElement(i);
/*     */     }
/* 176 */     return code;
/*     */   }
/*     */   
/*     */   protected abstract int getBufferElement(int paramInt);
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\buf\AbstractChunk.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */