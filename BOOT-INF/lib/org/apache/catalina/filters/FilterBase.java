/*    */ package org.apache.catalina.filters;
/*    */ 
/*    */ import java.util.Enumeration;
/*    */ import javax.servlet.Filter;
/*    */ import javax.servlet.FilterConfig;
/*    */ import javax.servlet.ServletException;
/*    */ import org.apache.juli.logging.Log;
/*    */ import org.apache.tomcat.util.IntrospectionUtils;
/*    */ import org.apache.tomcat.util.res.StringManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class FilterBase
/*    */   implements Filter
/*    */ {
/* 35 */   protected static final StringManager sm = StringManager.getManager(FilterBase.class);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected abstract Log getLogger();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void init(FilterConfig filterConfig)
/*    */     throws ServletException
/*    */   {
/* 54 */     Enumeration<String> paramNames = filterConfig.getInitParameterNames();
/* 55 */     while (paramNames.hasMoreElements()) {
/* 56 */       String paramName = (String)paramNames.nextElement();
/* 57 */       if (!IntrospectionUtils.setProperty(this, paramName, filterConfig
/* 58 */         .getInitParameter(paramName))) {
/* 59 */         String msg = sm.getString("filterbase.noSuchProperty", new Object[] { paramName, 
/* 60 */           getClass().getName() });
/* 61 */         if (isConfigProblemFatal()) {
/* 62 */           throw new ServletException(msg);
/*    */         }
/* 64 */         getLogger().warn(msg);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void destroy() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected boolean isConfigProblemFatal()
/*    */   {
/* 84 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\filters\FilterBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */