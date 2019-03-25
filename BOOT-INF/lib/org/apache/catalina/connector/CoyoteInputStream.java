/*     */ package org.apache.catalina.connector;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import javax.servlet.ReadListener;
/*     */ import javax.servlet.ServletInputStream;
/*     */ import org.apache.catalina.security.SecurityUtil;
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
/*     */ public class CoyoteInputStream
/*     */   extends ServletInputStream
/*     */ {
/*  38 */   protected static final StringManager sm = StringManager.getManager(CoyoteInputStream.class);
/*     */   
/*     */   protected InputBuffer ib;
/*     */   
/*     */ 
/*     */   protected CoyoteInputStream(InputBuffer ib)
/*     */   {
/*  45 */     this.ib = ib;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void clear()
/*     */   {
/*  53 */     this.ib = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/*  62 */     throw new CloneNotSupportedException();
/*     */   }
/*     */   
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/*  68 */     checkNonBlockingRead();
/*     */     
/*  70 */     if (SecurityUtil.isPackageProtectionEnabled())
/*     */     {
/*     */       try
/*     */       {
/*  74 */         Integer result = (Integer)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public Integer run() throws IOException
/*     */           {
/*  78 */             Integer integer = Integer.valueOf(CoyoteInputStream.this.ib.readByte());
/*  79 */             return integer;
/*     */           }
/*     */           
/*  82 */         });
/*  83 */         return result.intValue();
/*     */       } catch (PrivilegedActionException pae) {
/*  85 */         Exception e = pae.getException();
/*  86 */         if ((e instanceof IOException)) {
/*  87 */           throw ((IOException)e);
/*     */         }
/*  89 */         throw new RuntimeException(e.getMessage(), e);
/*     */       }
/*     */     }
/*     */     
/*  93 */     return this.ib.readByte();
/*     */   }
/*     */   
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/* 100 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/*     */       try
/*     */       {
/* 103 */         Integer result = (Integer)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public Integer run() throws IOException
/*     */           {
/* 107 */             Integer integer = Integer.valueOf(CoyoteInputStream.this.ib.available());
/* 108 */             return integer;
/*     */           }
/*     */           
/* 111 */         });
/* 112 */         return result.intValue();
/*     */       } catch (PrivilegedActionException pae) {
/* 114 */         Exception e = pae.getException();
/* 115 */         if ((e instanceof IOException)) {
/* 116 */           throw ((IOException)e);
/*     */         }
/* 118 */         throw new RuntimeException(e.getMessage(), e);
/*     */       }
/*     */     }
/*     */     
/* 122 */     return this.ib.available();
/*     */   }
/*     */   
/*     */   public int read(final byte[] b)
/*     */     throws IOException
/*     */   {
/* 128 */     checkNonBlockingRead();
/*     */     
/* 130 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/*     */       try
/*     */       {
/* 133 */         Integer result = (Integer)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public Integer run() throws IOException
/*     */           {
/* 137 */             Integer integer = Integer.valueOf(CoyoteInputStream.this.ib.read(b, 0, b.length));
/* 138 */             return integer;
/*     */           }
/*     */           
/* 141 */         });
/* 142 */         return result.intValue();
/*     */       } catch (PrivilegedActionException pae) {
/* 144 */         Exception e = pae.getException();
/* 145 */         if ((e instanceof IOException)) {
/* 146 */           throw ((IOException)e);
/*     */         }
/* 148 */         throw new RuntimeException(e.getMessage(), e);
/*     */       }
/*     */     }
/*     */     
/* 152 */     return this.ib.read(b, 0, b.length);
/*     */   }
/*     */   
/*     */ 
/*     */   public int read(final byte[] b, final int off, final int len)
/*     */     throws IOException
/*     */   {
/* 159 */     checkNonBlockingRead();
/*     */     
/* 161 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/*     */       try
/*     */       {
/* 164 */         Integer result = (Integer)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public Integer run() throws IOException
/*     */           {
/* 168 */             Integer integer = Integer.valueOf(CoyoteInputStream.this.ib.read(b, off, len));
/* 169 */             return integer;
/*     */           }
/*     */           
/* 172 */         });
/* 173 */         return result.intValue();
/*     */       } catch (PrivilegedActionException pae) {
/* 175 */         Exception e = pae.getException();
/* 176 */         if ((e instanceof IOException)) {
/* 177 */           throw ((IOException)e);
/*     */         }
/* 179 */         throw new RuntimeException(e.getMessage(), e);
/*     */       }
/*     */     }
/*     */     
/* 183 */     return this.ib.read(b, off, len);
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
/*     */   public int read(final ByteBuffer b)
/*     */     throws IOException
/*     */   {
/* 200 */     checkNonBlockingRead();
/*     */     
/* 202 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/*     */       try
/*     */       {
/* 205 */         Integer result = (Integer)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public Integer run() throws IOException
/*     */           {
/* 209 */             Integer integer = Integer.valueOf(CoyoteInputStream.this.ib.read(b));
/* 210 */             return integer;
/*     */           }
/*     */           
/* 213 */         });
/* 214 */         return result.intValue();
/*     */       } catch (PrivilegedActionException pae) {
/* 216 */         Exception e = pae.getException();
/* 217 */         if ((e instanceof IOException)) {
/* 218 */           throw ((IOException)e);
/*     */         }
/* 220 */         throw new RuntimeException(e.getMessage(), e);
/*     */       }
/*     */     }
/*     */     
/* 224 */     return this.ib.read(b);
/*     */   }
/*     */   
/*     */ 
/*     */   public int readLine(byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/* 231 */     return super.readLine(b, off, len);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 243 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/*     */       try {
/* 245 */         AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public Void run() throws IOException
/*     */           {
/* 249 */             CoyoteInputStream.this.ib.close();
/* 250 */             return null;
/*     */           }
/*     */         });
/*     */       }
/*     */       catch (PrivilegedActionException pae) {
/* 255 */         Exception e = pae.getException();
/* 256 */         if ((e instanceof IOException)) {
/* 257 */           throw ((IOException)e);
/*     */         }
/* 259 */         throw new RuntimeException(e.getMessage(), e);
/*     */       }
/*     */       
/*     */     } else {
/* 263 */       this.ib.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isFinished()
/*     */   {
/* 269 */     return this.ib.isFinished();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isReady()
/*     */   {
/* 275 */     return this.ib.isReady();
/*     */   }
/*     */   
/*     */ 
/*     */   public void setReadListener(ReadListener listener)
/*     */   {
/* 281 */     this.ib.setReadListener(listener);
/*     */   }
/*     */   
/*     */   private void checkNonBlockingRead()
/*     */   {
/* 286 */     if ((!this.ib.isBlocking()) && (!this.ib.isReady())) {
/* 287 */       throw new IllegalStateException(sm.getString("coyoteInputStream.nbNotready"));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\connector\CoyoteInputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */