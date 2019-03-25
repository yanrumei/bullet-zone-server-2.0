/*     */ package org.apache.catalina.connector;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
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
/*     */ public class CoyoteWriter
/*     */   extends PrintWriter
/*     */ {
/*  32 */   private static final char[] LINE_SEP = System.lineSeparator().toCharArray();
/*     */   
/*     */ 
/*     */ 
/*     */   protected OutputBuffer ob;
/*     */   
/*     */ 
/*  39 */   protected boolean error = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CoyoteWriter(OutputBuffer ob)
/*     */   {
/*  46 */     super(ob);
/*  47 */     this.ob = ob;
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
/*  60 */     throw new CloneNotSupportedException();
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
/*  71 */     this.ob = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void recycle()
/*     */   {
/*  79 */     this.error = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void flush()
/*     */   {
/*  89 */     if (this.error) {
/*  90 */       return;
/*     */     }
/*     */     try
/*     */     {
/*  94 */       this.ob.flush();
/*     */     } catch (IOException e) {
/*  96 */       this.error = true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */   {
/*     */     try
/*     */     {
/* 108 */       this.ob.close();
/*     */     }
/*     */     catch (IOException localIOException) {}
/*     */     
/* 112 */     this.error = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean checkError()
/*     */   {
/* 119 */     flush();
/* 120 */     return this.error;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void write(int c)
/*     */   {
/* 127 */     if (this.error) {
/* 128 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 132 */       this.ob.write(c);
/*     */     } catch (IOException e) {
/* 134 */       this.error = true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void write(char[] buf, int off, int len)
/*     */   {
/* 143 */     if (this.error) {
/* 144 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 148 */       this.ob.write(buf, off, len);
/*     */     } catch (IOException e) {
/* 150 */       this.error = true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void write(char[] buf)
/*     */   {
/* 158 */     write(buf, 0, buf.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void write(String s, int off, int len)
/*     */   {
/* 165 */     if (this.error) {
/* 166 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 170 */       this.ob.write(s, off, len);
/*     */     } catch (IOException e) {
/* 172 */       this.error = true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void write(String s)
/*     */   {
/* 180 */     write(s, 0, s.length());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void print(boolean b)
/*     */   {
/* 189 */     if (b) {
/* 190 */       write("true");
/*     */     } else {
/* 192 */       write("false");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void print(char c)
/*     */   {
/* 199 */     write(c);
/*     */   }
/*     */   
/*     */ 
/*     */   public void print(int i)
/*     */   {
/* 205 */     write(String.valueOf(i));
/*     */   }
/*     */   
/*     */ 
/*     */   public void print(long l)
/*     */   {
/* 211 */     write(String.valueOf(l));
/*     */   }
/*     */   
/*     */ 
/*     */   public void print(float f)
/*     */   {
/* 217 */     write(String.valueOf(f));
/*     */   }
/*     */   
/*     */ 
/*     */   public void print(double d)
/*     */   {
/* 223 */     write(String.valueOf(d));
/*     */   }
/*     */   
/*     */ 
/*     */   public void print(char[] s)
/*     */   {
/* 229 */     write(s);
/*     */   }
/*     */   
/*     */ 
/*     */   public void print(String s)
/*     */   {
/* 235 */     if (s == null) {
/* 236 */       s = "null";
/*     */     }
/* 238 */     write(s);
/*     */   }
/*     */   
/*     */ 
/*     */   public void print(Object obj)
/*     */   {
/* 244 */     write(String.valueOf(obj));
/*     */   }
/*     */   
/*     */ 
/*     */   public void println()
/*     */   {
/* 250 */     write(LINE_SEP);
/*     */   }
/*     */   
/*     */ 
/*     */   public void println(boolean b)
/*     */   {
/* 256 */     print(b);
/* 257 */     println();
/*     */   }
/*     */   
/*     */ 
/*     */   public void println(char c)
/*     */   {
/* 263 */     print(c);
/* 264 */     println();
/*     */   }
/*     */   
/*     */ 
/*     */   public void println(int i)
/*     */   {
/* 270 */     print(i);
/* 271 */     println();
/*     */   }
/*     */   
/*     */ 
/*     */   public void println(long l)
/*     */   {
/* 277 */     print(l);
/* 278 */     println();
/*     */   }
/*     */   
/*     */ 
/*     */   public void println(float f)
/*     */   {
/* 284 */     print(f);
/* 285 */     println();
/*     */   }
/*     */   
/*     */ 
/*     */   public void println(double d)
/*     */   {
/* 291 */     print(d);
/* 292 */     println();
/*     */   }
/*     */   
/*     */ 
/*     */   public void println(char[] c)
/*     */   {
/* 298 */     print(c);
/* 299 */     println();
/*     */   }
/*     */   
/*     */ 
/*     */   public void println(String s)
/*     */   {
/* 305 */     print(s);
/* 306 */     println();
/*     */   }
/*     */   
/*     */ 
/*     */   public void println(Object o)
/*     */   {
/* 312 */     print(o);
/* 313 */     println();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\connector\CoyoteWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */