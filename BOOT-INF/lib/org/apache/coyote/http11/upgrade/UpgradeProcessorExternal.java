/*     */ package org.apache.coyote.http11.upgrade;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.ServletInputStream;
/*     */ import javax.servlet.ServletOutputStream;
/*     */ import org.apache.coyote.UpgradeToken;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.net.AbstractEndpoint.Handler.SocketState;
/*     */ import org.apache.tomcat.util.net.SSLSupport;
/*     */ import org.apache.tomcat.util.net.SocketEvent;
/*     */ import org.apache.tomcat.util.net.SocketWrapperBase;
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
/*     */ public class UpgradeProcessorExternal
/*     */   extends UpgradeProcessorBase
/*     */ {
/*  35 */   private static final Log log = LogFactory.getLog(UpgradeProcessorExternal.class);
/*  36 */   private static final StringManager sm = StringManager.getManager(UpgradeProcessorExternal.class);
/*     */   
/*     */   private final UpgradeServletInputStream upgradeServletInputStream;
/*     */   
/*     */   private final UpgradeServletOutputStream upgradeServletOutputStream;
/*     */   
/*     */   public UpgradeProcessorExternal(SocketWrapperBase<?> wrapper, UpgradeToken upgradeToken)
/*     */   {
/*  44 */     super(upgradeToken);
/*  45 */     this.upgradeServletInputStream = new UpgradeServletInputStream(this, wrapper);
/*  46 */     this.upgradeServletOutputStream = new UpgradeServletOutputStream(this, wrapper);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  51 */     wrapper.setReadTimeout(-1L);
/*  52 */     wrapper.setWriteTimeout(-1L);
/*     */   }
/*     */   
/*     */ 
/*     */   protected Log getLog()
/*     */   {
/*  58 */     return log;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void close()
/*     */     throws Exception
/*     */   {
/*  66 */     this.upgradeServletInputStream.close();
/*  67 */     this.upgradeServletOutputStream.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ServletInputStream getInputStream()
/*     */     throws IOException
/*     */   {
/*  75 */     return this.upgradeServletInputStream;
/*     */   }
/*     */   
/*     */   public ServletOutputStream getOutputStream() throws IOException
/*     */   {
/*  80 */     return this.upgradeServletOutputStream;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final AbstractEndpoint.Handler.SocketState dispatch(SocketEvent status)
/*     */   {
/*  88 */     if (status == SocketEvent.OPEN_READ) {
/*  89 */       this.upgradeServletInputStream.onDataAvailable();
/*  90 */     } else if (status == SocketEvent.OPEN_WRITE) {
/*  91 */       this.upgradeServletOutputStream.onWritePossible();
/*  92 */     } else { if (status == SocketEvent.STOP) {
/*  93 */         if (log.isDebugEnabled()) {
/*  94 */           log.debug(sm.getString("upgradeProcessor.stop"));
/*     */         }
/*     */         try {
/*  97 */           this.upgradeServletInputStream.close();
/*     */         } catch (IOException ioe) {
/*  99 */           log.debug(sm.getString("upgradeProcessor.isCloseFail", new Object[] { ioe }));
/*     */         }
/*     */         try {
/* 102 */           this.upgradeServletOutputStream.close();
/*     */         } catch (IOException ioe) {
/* 104 */           log.debug(sm.getString("upgradeProcessor.osCloseFail", new Object[] { ioe }));
/*     */         }
/* 106 */         return AbstractEndpoint.Handler.SocketState.CLOSED;
/*     */       }
/*     */       
/* 109 */       if (log.isDebugEnabled()) {
/* 110 */         log.debug(sm.getString("upgradeProcessor.unexpectedState"));
/*     */       }
/* 112 */       return AbstractEndpoint.Handler.SocketState.CLOSED;
/*     */     }
/* 114 */     if ((this.upgradeServletInputStream.isClosed()) && 
/* 115 */       (this.upgradeServletOutputStream.isClosed())) {
/* 116 */       if (log.isDebugEnabled()) {
/* 117 */         log.debug(sm.getString("upgradeProcessor.requiredClose", new Object[] {
/* 118 */           Boolean.valueOf(this.upgradeServletInputStream.isClosed()), 
/* 119 */           Boolean.valueOf(this.upgradeServletOutputStream.isClosed()) }));
/*     */       }
/* 121 */       return AbstractEndpoint.Handler.SocketState.CLOSED;
/*     */     }
/* 123 */     return AbstractEndpoint.Handler.SocketState.UPGRADED;
/*     */   }
/*     */   
/*     */   public final void setSslSupport(SSLSupport sslSupport) {}
/*     */   
/*     */   public void pause() {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http1\\upgrade\UpgradeProcessorExternal.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */