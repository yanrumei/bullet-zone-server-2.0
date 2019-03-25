/*     */ package org.apache.catalina.connector;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import javax.servlet.ServletOutputStream;
/*     */ import javax.servlet.WriteListener;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ public class CoyoteOutputStream
/*     */   extends ServletOutputStream
/*     */ {
/*  35 */   protected static final StringManager sm = StringManager.getManager(CoyoteOutputStream.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected OutputBuffer ob;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected CoyoteOutputStream(OutputBuffer ob)
/*     */   {
/*  47 */     this.ob = ob;
/*     */   }
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
/*  59 */     throw new CloneNotSupportedException();
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
/*  70 */     this.ob = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void write(int i)
/*     */     throws IOException
/*     */   {
/*  79 */     boolean nonBlocking = checkNonBlockingWrite();
/*  80 */     this.ob.writeByte(i);
/*  81 */     if (nonBlocking) {
/*  82 */       checkRegisterForWrite();
/*     */     }
/*     */   }
/*     */   
/*     */   public void write(byte[] b)
/*     */     throws IOException
/*     */   {
/*  89 */     write(b, 0, b.length);
/*     */   }
/*     */   
/*     */   public void write(byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/*  95 */     boolean nonBlocking = checkNonBlockingWrite();
/*  96 */     this.ob.write(b, off, len);
/*  97 */     if (nonBlocking) {
/*  98 */       checkRegisterForWrite();
/*     */     }
/*     */   }
/*     */   
/*     */   public void write(ByteBuffer from) throws IOException
/*     */   {
/* 104 */     boolean nonBlocking = checkNonBlockingWrite();
/* 105 */     this.ob.write(from);
/* 106 */     if (nonBlocking) {
/* 107 */       checkRegisterForWrite();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 117 */     boolean nonBlocking = checkNonBlockingWrite();
/* 118 */     this.ob.flush();
/* 119 */     if (nonBlocking) {
/* 120 */       checkRegisterForWrite();
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
/*     */   private boolean checkNonBlockingWrite()
/*     */   {
/* 134 */     boolean nonBlocking = !this.ob.isBlocking();
/* 135 */     if ((nonBlocking) && (!this.ob.isReady())) {
/* 136 */       throw new IllegalStateException(sm.getString("coyoteOutputStream.nbNotready"));
/*     */     }
/* 138 */     return nonBlocking;
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
/*     */   private void checkRegisterForWrite()
/*     */   {
/* 151 */     this.ob.checkRegisterForWrite();
/*     */   }
/*     */   
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 157 */     this.ob.close();
/*     */   }
/*     */   
/*     */   public boolean isReady()
/*     */   {
/* 162 */     return this.ob.isReady();
/*     */   }
/*     */   
/*     */ 
/*     */   public void setWriteListener(WriteListener listener)
/*     */   {
/* 168 */     this.ob.setWriteListener(listener);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\connector\CoyoteOutputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */