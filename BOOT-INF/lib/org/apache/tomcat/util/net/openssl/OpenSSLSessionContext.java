/*     */ package org.apache.tomcat.util.net.openssl;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.NoSuchElementException;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSessionContext;
/*     */ import org.apache.tomcat.jni.SSLContext;
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
/*     */ public class OpenSSLSessionContext
/*     */   implements SSLSessionContext
/*     */ {
/*  33 */   private static final StringManager sm = StringManager.getManager(OpenSSLSessionContext.class);
/*  34 */   private static final Enumeration<byte[]> EMPTY = new EmptyEnumeration(null);
/*     */   
/*     */ 
/*     */   private final OpenSSLSessionStats stats;
/*     */   
/*     */   private final OpenSSLContext context;
/*     */   
/*     */   private final long contextID;
/*     */   
/*     */ 
/*     */   OpenSSLSessionContext(OpenSSLContext context)
/*     */   {
/*  46 */     this.context = context;
/*  47 */     this.contextID = context.getSSLContextID();
/*  48 */     this.stats = new OpenSSLSessionStats(this.contextID);
/*     */   }
/*     */   
/*     */   public SSLSession getSession(byte[] bytes)
/*     */   {
/*  53 */     return null;
/*     */   }
/*     */   
/*     */   public Enumeration<byte[]> getIds()
/*     */   {
/*  58 */     return EMPTY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTicketKeys(byte[] keys)
/*     */   {
/*  67 */     if (keys == null) {
/*  68 */       throw new IllegalArgumentException(sm.getString("sessionContext.nullTicketKeys"));
/*     */     }
/*  70 */     SSLContext.setSessionTicketKeys(this.contextID, keys);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSessionCacheEnabled(boolean enabled)
/*     */   {
/*  79 */     long mode = enabled ? 2L : 0L;
/*  80 */     SSLContext.setSessionCacheMode(this.contextID, mode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSessionCacheEnabled()
/*     */   {
/*  88 */     return SSLContext.getSessionCacheMode(this.contextID) == 2L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public OpenSSLSessionStats stats()
/*     */   {
/*  95 */     return this.stats;
/*     */   }
/*     */   
/*     */   public void setSessionTimeout(int seconds)
/*     */   {
/* 100 */     if (seconds < 0) {
/* 101 */       throw new IllegalArgumentException();
/*     */     }
/* 103 */     SSLContext.setSessionCacheTimeout(this.contextID, seconds);
/*     */   }
/*     */   
/*     */   public int getSessionTimeout()
/*     */   {
/* 108 */     return (int)SSLContext.getSessionCacheTimeout(this.contextID);
/*     */   }
/*     */   
/*     */   public void setSessionCacheSize(int size)
/*     */   {
/* 113 */     if (size < 0) {
/* 114 */       throw new IllegalArgumentException();
/*     */     }
/* 116 */     SSLContext.setSessionCacheSize(this.contextID, size);
/*     */   }
/*     */   
/*     */   public int getSessionCacheSize()
/*     */   {
/* 121 */     return (int)SSLContext.getSessionCacheSize(this.contextID);
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
/*     */   public boolean setSessionIdContext(byte[] sidCtx)
/*     */   {
/* 134 */     return SSLContext.setSessionIdContext(this.contextID, sidCtx);
/*     */   }
/*     */   
/*     */   private static final class EmptyEnumeration implements Enumeration<byte[]>
/*     */   {
/*     */     public boolean hasMoreElements() {
/* 140 */       return false;
/*     */     }
/*     */     
/*     */     public byte[] nextElement()
/*     */     {
/* 145 */       throw new NoSuchElementException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\openssl\OpenSSLSessionContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */