/*     */ package org.apache.catalina.connector;
/*     */ 
/*     */ import java.io.BufferedReader;
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
/*     */ public class CoyoteReader
/*     */   extends BufferedReader
/*     */ {
/*  35 */   private static final char[] LINE_SEP = { '\r', '\n' };
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int MAX_LINE_LENGTH = 4096;
/*     */   
/*     */ 
/*     */   protected InputBuffer ib;
/*     */   
/*     */ 
/*  45 */   protected char[] lineBuffer = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CoyoteReader(InputBuffer ib)
/*     */   {
/*  52 */     super(ib, 1);
/*  53 */     this.ib = ib;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/*  66 */     throw new CloneNotSupportedException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void clear()
/*     */   {
/*  77 */     this.ib = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*  87 */     this.ib.close();
/*     */   }
/*     */   
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/*  94 */     return this.ib.read();
/*     */   }
/*     */   
/*     */ 
/*     */   public int read(char[] cbuf)
/*     */     throws IOException
/*     */   {
/* 101 */     return this.ib.read(cbuf, 0, cbuf.length);
/*     */   }
/*     */   
/*     */ 
/*     */   public int read(char[] cbuf, int off, int len)
/*     */     throws IOException
/*     */   {
/* 108 */     return this.ib.read(cbuf, off, len);
/*     */   }
/*     */   
/*     */ 
/*     */   public long skip(long n)
/*     */     throws IOException
/*     */   {
/* 115 */     return this.ib.skip(n);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean ready()
/*     */     throws IOException
/*     */   {
/* 122 */     return this.ib.ready();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 128 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void mark(int readAheadLimit)
/*     */     throws IOException
/*     */   {
/* 135 */     this.ib.mark(readAheadLimit);
/*     */   }
/*     */   
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/* 142 */     this.ib.reset();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String readLine()
/*     */     throws IOException
/*     */   {
/* 150 */     if (this.lineBuffer == null) {
/* 151 */       this.lineBuffer = new char['က'];
/*     */     }
/*     */     
/* 154 */     String result = null;
/*     */     
/* 156 */     int pos = 0;
/* 157 */     int end = -1;
/* 158 */     int skip = -1;
/* 159 */     StringBuilder aggregator = null;
/* 160 */     while (end < 0) {
/* 161 */       mark(4096);
/* 162 */       while ((pos < 4096) && (end < 0)) {
/* 163 */         int nRead = read(this.lineBuffer, pos, 4096 - pos);
/* 164 */         if (nRead < 0) {
/* 165 */           if ((pos == 0) && (aggregator == null)) {
/* 166 */             return null;
/*     */           }
/* 168 */           end = pos;
/* 169 */           skip = pos;
/*     */         }
/* 171 */         for (int i = pos; (i < pos + nRead) && (end < 0); i++) {
/* 172 */           if (this.lineBuffer[i] == LINE_SEP[0]) {
/* 173 */             end = i;
/* 174 */             skip = i + 1;
/*     */             char nextchar;
/* 176 */             char nextchar; if (i == pos + nRead - 1) {
/* 177 */               nextchar = (char)read();
/*     */             } else {
/* 179 */               nextchar = this.lineBuffer[(i + 1)];
/*     */             }
/* 181 */             if (nextchar == LINE_SEP[1]) {
/* 182 */               skip++;
/*     */             }
/* 184 */           } else if (this.lineBuffer[i] == LINE_SEP[1]) {
/* 185 */             end = i;
/* 186 */             skip = i + 1;
/*     */           }
/*     */         }
/* 189 */         if (nRead > 0) {
/* 190 */           pos += nRead;
/*     */         }
/*     */       }
/* 193 */       if (end < 0) {
/* 194 */         if (aggregator == null) {
/* 195 */           aggregator = new StringBuilder();
/*     */         }
/* 197 */         aggregator.append(this.lineBuffer);
/* 198 */         pos = 0;
/*     */       } else {
/* 200 */         reset();
/* 201 */         skip(skip);
/*     */       }
/*     */     }
/*     */     
/* 205 */     if (aggregator == null) {
/* 206 */       result = new String(this.lineBuffer, 0, end);
/*     */     } else {
/* 208 */       aggregator.append(this.lineBuffer, 0, end);
/* 209 */       result = aggregator.toString();
/*     */     }
/*     */     
/* 212 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\connector\CoyoteReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */