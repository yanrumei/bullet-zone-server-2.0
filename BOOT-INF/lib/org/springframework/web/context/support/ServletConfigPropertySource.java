/*    */ package org.springframework.web.context.support;
/*    */ 
/*    */ import javax.servlet.ServletConfig;
/*    */ import org.springframework.core.env.EnumerablePropertySource;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ 
/*    */ 
/*    */ public class ServletConfigPropertySource
/*    */   extends EnumerablePropertySource<ServletConfig>
/*    */ {
/*    */   public ServletConfigPropertySource(String name, ServletConfig servletConfig)
/*    */   {
/* 35 */     super(name, servletConfig);
/*    */   }
/*    */   
/*    */   public String[] getPropertyNames()
/*    */   {
/* 40 */     return StringUtils.toStringArray(((ServletConfig)this.source).getInitParameterNames());
/*    */   }
/*    */   
/*    */   public String getProperty(String name)
/*    */   {
/* 45 */     return ((ServletConfig)this.source).getInitParameter(name);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\support\ServletConfigPropertySource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */