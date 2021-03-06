/*    */ package org.springframework.jndi;
/*    */ 
/*    */ import java.util.Properties;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JndiAccessor
/*    */ {
/* 38 */   protected final Log logger = LogFactory.getLog(getClass());
/*    */   
/* 40 */   private JndiTemplate jndiTemplate = new JndiTemplate();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setJndiTemplate(JndiTemplate jndiTemplate)
/*    */   {
/* 49 */     this.jndiTemplate = (jndiTemplate != null ? jndiTemplate : new JndiTemplate());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public JndiTemplate getJndiTemplate()
/*    */   {
/* 56 */     return this.jndiTemplate;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setJndiEnvironment(Properties jndiEnvironment)
/*    */   {
/* 65 */     this.jndiTemplate = new JndiTemplate(jndiEnvironment);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Properties getJndiEnvironment()
/*    */   {
/* 72 */     return this.jndiTemplate.getEnvironment();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jndi\JndiAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */