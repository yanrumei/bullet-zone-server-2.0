/*    */ package org.springframework.boot.context.embedded;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import javax.servlet.ServletContext;
/*    */ import javax.servlet.ServletException;
/*    */ import org.springframework.boot.web.servlet.ServletContextInitializer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InitParameterConfiguringServletContextInitializer
/*    */   implements ServletContextInitializer
/*    */ {
/*    */   private final Map<String, String> parameters;
/*    */   
/*    */   public InitParameterConfiguringServletContextInitializer(Map<String, String> parameters)
/*    */   {
/* 42 */     this.parameters = parameters;
/*    */   }
/*    */   
/*    */   public void onStartup(ServletContext servletContext) throws ServletException
/*    */   {
/* 47 */     for (Map.Entry<String, String> entry : this.parameters.entrySet()) {
/* 48 */       servletContext.setInitParameter((String)entry.getKey(), (String)entry.getValue());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\InitParameterConfiguringServletContextInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */