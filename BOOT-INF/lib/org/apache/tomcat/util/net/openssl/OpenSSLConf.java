/*     */ package org.apache.tomcat.util.net.openssl;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.jni.SSLConf;
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
/*     */ public class OpenSSLConf
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  32 */   private static final Log log = LogFactory.getLog(OpenSSLConf.class);
/*  33 */   private static final StringManager sm = StringManager.getManager(OpenSSLConf.class);
/*     */   
/*  35 */   private final List<OpenSSLConfCmd> commands = new ArrayList();
/*     */   
/*     */   public void addCmd(OpenSSLConfCmd cmd) {
/*  38 */     this.commands.add(cmd);
/*     */   }
/*     */   
/*     */   public List<OpenSSLConfCmd> getCommands() {
/*  42 */     return this.commands;
/*     */   }
/*     */   
/*     */   public boolean check(long cctx) throws Exception {
/*  46 */     boolean result = true;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  51 */     for (int i = 0; i < this.commands.size(); i++) {
/*  52 */       OpenSSLConfCmd cmd = (OpenSSLConfCmd)this.commands.get(i);
/*  53 */       String name = cmd.getName();
/*  54 */       String value = cmd.getValue();
/*  55 */       if (name == null) {
/*  56 */         log.error(sm.getString("opensslconf.noCommandName", new Object[] { value }));
/*  57 */         result = false;
/*     */       }
/*     */       else {
/*  60 */         if (log.isDebugEnabled()) {
/*  61 */           log.debug(sm.getString("opensslconf.checkCommand", new Object[] { name, value }));
/*     */         }
/*     */         try {
/*  64 */           rc = SSLConf.check(cctx, name, value);
/*     */         } catch (Exception e) { int rc;
/*  66 */           log.error(sm.getString("opensslconf.checkFailed"));
/*  67 */           return false; }
/*     */         int rc;
/*  69 */         if (rc <= 0) {
/*  70 */           log.error(sm.getString("opensslconf.failedCommand", new Object[] { name, value, 
/*  71 */             Integer.toString(rc) }));
/*  72 */           result = false;
/*  73 */         } else if (log.isDebugEnabled()) {
/*  74 */           log.debug(sm.getString("opensslconf.resultCommand", new Object[] { name, value, 
/*  75 */             Integer.toString(rc) }));
/*     */         }
/*     */       } }
/*  78 */     if (!result) {
/*  79 */       log.error(sm.getString("opensslconf.checkFailed"));
/*     */     }
/*  81 */     return result;
/*     */   }
/*     */   
/*     */   public boolean apply(long cctx, long ctx) throws Exception {
/*  85 */     boolean result = true;
/*  86 */     SSLConf.assign(cctx, ctx);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  91 */     for (int i = 0; i < this.commands.size(); i++) {
/*  92 */       OpenSSLConfCmd cmd = (OpenSSLConfCmd)this.commands.get(i);
/*  93 */       String name = cmd.getName();
/*  94 */       String value = cmd.getValue();
/*  95 */       if (name == null) {
/*  96 */         log.error(sm.getString("opensslconf.noCommandName", new Object[] { value }));
/*  97 */         result = false;
/*     */       }
/*     */       else {
/* 100 */         if (log.isDebugEnabled()) {
/* 101 */           log.debug(sm.getString("opensslconf.applyCommand", new Object[] { name, value }));
/*     */         }
/*     */         try {
/* 104 */           rc = SSLConf.apply(cctx, name, value);
/*     */         } catch (Exception e) { int rc;
/* 106 */           log.error(sm.getString("opensslconf.applyFailed"));
/* 107 */           return false; }
/*     */         int rc;
/* 109 */         if (rc <= 0) {
/* 110 */           log.error(sm.getString("opensslconf.failedCommand", new Object[] { name, value, 
/* 111 */             Integer.toString(rc) }));
/* 112 */           result = false;
/* 113 */         } else if (log.isDebugEnabled()) {
/* 114 */           log.debug(sm.getString("opensslconf.resultCommand", new Object[] { name, value, 
/* 115 */             Integer.toString(rc) }));
/*     */         }
/*     */       } }
/* 118 */     int rc = SSLConf.finish(cctx);
/* 119 */     if (rc <= 0) {
/* 120 */       log.error(sm.getString("opensslconf.finishFailed", new Object[] { Integer.toString(rc) }));
/* 121 */       result = false;
/*     */     }
/* 123 */     if (!result) {
/* 124 */       log.error(sm.getString("opensslconf.applyFailed"));
/*     */     }
/* 126 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\openssl\OpenSSLConf.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */