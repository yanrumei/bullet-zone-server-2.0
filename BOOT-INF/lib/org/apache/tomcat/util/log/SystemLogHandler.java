/*     */ package org.apache.tomcat.util.log;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.EmptyStackException;
/*     */ import java.util.Stack;
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
/*     */ public class SystemLogHandler
/*     */   extends PrintStream
/*     */ {
/*     */   private final PrintStream out;
/*     */   
/*     */   public SystemLogHandler(PrintStream wrapped)
/*     */   {
/*  46 */     super(wrapped);
/*  47 */     this.out = wrapped;
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
/*  63 */   private static final ThreadLocal<Stack<CaptureLog>> logs = new ThreadLocal();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  69 */   private static final Stack<CaptureLog> reuse = new Stack();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void startCapture()
/*     */   {
/*  79 */     CaptureLog log = null;
/*  80 */     if (!reuse.isEmpty()) {
/*     */       try {
/*  82 */         log = (CaptureLog)reuse.pop();
/*     */       } catch (EmptyStackException e) {
/*  84 */         log = new CaptureLog();
/*     */       }
/*     */     } else {
/*  87 */       log = new CaptureLog();
/*     */     }
/*  89 */     Stack<CaptureLog> stack = (Stack)logs.get();
/*  90 */     if (stack == null) {
/*  91 */       stack = new Stack();
/*  92 */       logs.set(stack);
/*     */     }
/*  94 */     stack.push(log);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String stopCapture()
/*     */   {
/* 104 */     Stack<CaptureLog> stack = (Stack)logs.get();
/* 105 */     if ((stack == null) || (stack.isEmpty())) {
/* 106 */       return null;
/*     */     }
/* 108 */     CaptureLog log = (CaptureLog)stack.pop();
/* 109 */     if (log == null) {
/* 110 */       return null;
/*     */     }
/* 112 */     String capture = log.getCapture();
/* 113 */     log.reset();
/* 114 */     reuse.push(log);
/* 115 */     return capture;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected PrintStream findStream()
/*     */   {
/* 127 */     Stack<CaptureLog> stack = (Stack)logs.get();
/* 128 */     if ((stack != null) && (!stack.isEmpty())) {
/* 129 */       CaptureLog log = (CaptureLog)stack.peek();
/* 130 */       if (log != null) {
/* 131 */         PrintStream ps = log.getStream();
/* 132 */         if (ps != null) {
/* 133 */           return ps;
/*     */         }
/*     */       }
/*     */     }
/* 137 */     return this.out;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void flush()
/*     */   {
/* 146 */     findStream().flush();
/*     */   }
/*     */   
/*     */   public void close()
/*     */   {
/* 151 */     findStream().close();
/*     */   }
/*     */   
/*     */   public boolean checkError()
/*     */   {
/* 156 */     return findStream().checkError();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void setError() {}
/*     */   
/*     */ 
/*     */   public void write(int b)
/*     */   {
/* 166 */     findStream().write(b);
/*     */   }
/*     */   
/*     */   public void write(byte[] b)
/*     */     throws IOException
/*     */   {
/* 172 */     findStream().write(b);
/*     */   }
/*     */   
/*     */   public void write(byte[] buf, int off, int len)
/*     */   {
/* 177 */     findStream().write(buf, off, len);
/*     */   }
/*     */   
/*     */   public void print(boolean b)
/*     */   {
/* 182 */     findStream().print(b);
/*     */   }
/*     */   
/*     */   public void print(char c)
/*     */   {
/* 187 */     findStream().print(c);
/*     */   }
/*     */   
/*     */   public void print(int i)
/*     */   {
/* 192 */     findStream().print(i);
/*     */   }
/*     */   
/*     */   public void print(long l)
/*     */   {
/* 197 */     findStream().print(l);
/*     */   }
/*     */   
/*     */   public void print(float f)
/*     */   {
/* 202 */     findStream().print(f);
/*     */   }
/*     */   
/*     */   public void print(double d)
/*     */   {
/* 207 */     findStream().print(d);
/*     */   }
/*     */   
/*     */   public void print(char[] s)
/*     */   {
/* 212 */     findStream().print(s);
/*     */   }
/*     */   
/*     */   public void print(String s)
/*     */   {
/* 217 */     findStream().print(s);
/*     */   }
/*     */   
/*     */   public void print(Object obj)
/*     */   {
/* 222 */     findStream().print(obj);
/*     */   }
/*     */   
/*     */   public void println()
/*     */   {
/* 227 */     findStream().println();
/*     */   }
/*     */   
/*     */   public void println(boolean x)
/*     */   {
/* 232 */     findStream().println(x);
/*     */   }
/*     */   
/*     */   public void println(char x)
/*     */   {
/* 237 */     findStream().println(x);
/*     */   }
/*     */   
/*     */   public void println(int x)
/*     */   {
/* 242 */     findStream().println(x);
/*     */   }
/*     */   
/*     */   public void println(long x)
/*     */   {
/* 247 */     findStream().println(x);
/*     */   }
/*     */   
/*     */   public void println(float x)
/*     */   {
/* 252 */     findStream().println(x);
/*     */   }
/*     */   
/*     */   public void println(double x)
/*     */   {
/* 257 */     findStream().println(x);
/*     */   }
/*     */   
/*     */   public void println(char[] x)
/*     */   {
/* 262 */     findStream().println(x);
/*     */   }
/*     */   
/*     */   public void println(String x)
/*     */   {
/* 267 */     findStream().println(x);
/*     */   }
/*     */   
/*     */   public void println(Object x)
/*     */   {
/* 272 */     findStream().println(x);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\log\SystemLogHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */