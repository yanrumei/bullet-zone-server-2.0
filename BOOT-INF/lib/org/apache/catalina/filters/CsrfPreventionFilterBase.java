/*     */ package org.apache.catalina.filters;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.Random;
/*     */ import javax.servlet.FilterConfig;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
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
/*     */ public abstract class CsrfPreventionFilterBase
/*     */   extends FilterBase
/*     */ {
/*  32 */   private static final Log log = LogFactory.getLog(CsrfPreventionFilterBase.class);
/*     */   
/*  34 */   private String randomClass = SecureRandom.class.getName();
/*     */   
/*     */   private Random randomSource;
/*     */   
/*  38 */   private int denyStatus = 403;
/*     */   
/*     */   protected Log getLogger()
/*     */   {
/*  42 */     return log;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getDenyStatus()
/*     */   {
/*  49 */     return this.denyStatus;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDenyStatus(int denyStatus)
/*     */   {
/*  60 */     this.denyStatus = denyStatus;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRandomClass(String randomClass)
/*     */   {
/*  71 */     this.randomClass = randomClass;
/*     */   }
/*     */   
/*     */   public void init(FilterConfig filterConfig)
/*     */     throws ServletException
/*     */   {
/*  77 */     super.init(filterConfig);
/*     */     try
/*     */     {
/*  80 */       Class<?> clazz = Class.forName(this.randomClass);
/*  81 */       this.randomSource = ((Random)clazz.getConstructor(new Class[0]).newInstance(new Object[0]));
/*     */     } catch (ReflectiveOperationException e) {
/*  83 */       ServletException se = new ServletException(sm.getString("csrfPrevention.invalidRandomClass", new Object[] { this.randomClass }), e);
/*     */       
/*  85 */       throw se;
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean isConfigProblemFatal()
/*     */   {
/*  91 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String generateNonce()
/*     */   {
/* 102 */     byte[] random = new byte[16];
/*     */     
/*     */ 
/* 105 */     StringBuilder buffer = new StringBuilder();
/*     */     
/* 107 */     this.randomSource.nextBytes(random);
/*     */     
/* 109 */     for (int j = 0; j < random.length; j++) {
/* 110 */       byte b1 = (byte)((random[j] & 0xF0) >> 4);
/* 111 */       byte b2 = (byte)(random[j] & 0xF);
/* 112 */       if (b1 < 10) {
/* 113 */         buffer.append((char)(48 + b1));
/*     */       } else {
/* 115 */         buffer.append((char)(65 + (b1 - 10)));
/*     */       }
/* 117 */       if (b2 < 10) {
/* 118 */         buffer.append((char)(48 + b2));
/*     */       } else {
/* 120 */         buffer.append((char)(65 + (b2 - 10)));
/*     */       }
/*     */     }
/*     */     
/* 124 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   protected String getRequestedPath(HttpServletRequest request) {
/* 128 */     String path = request.getServletPath();
/* 129 */     if (request.getPathInfo() != null) {
/* 130 */       path = path + request.getPathInfo();
/*     */     }
/* 132 */     return path;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\filters\CsrfPreventionFilterBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */