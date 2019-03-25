/*    */ package org.springframework.boot.jta.atomikos;
/*    */ 
/*    */ import org.springframework.beans.factory.BeanNameAware;
/*    */ import org.springframework.beans.factory.DisposableBean;
/*    */ import org.springframework.beans.factory.InitializingBean;
/*    */ import org.springframework.boot.context.properties.ConfigurationProperties;
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
/*    */ @ConfigurationProperties(prefix="spring.jta.atomikos.datasource")
/*    */ public class AtomikosDataSourceBean
/*    */   extends com.atomikos.jdbc.AtomikosDataSourceBean
/*    */   implements BeanNameAware, InitializingBean, DisposableBean
/*    */ {
/*    */   private String beanName;
/*    */   
/*    */   public void setBeanName(String name)
/*    */   {
/* 41 */     this.beanName = name;
/*    */   }
/*    */   
/*    */   public void afterPropertiesSet() throws Exception
/*    */   {
/* 46 */     if (!StringUtils.hasLength(getUniqueResourceName())) {
/* 47 */       setUniqueResourceName(this.beanName);
/*    */     }
/* 49 */     init();
/*    */   }
/*    */   
/*    */   public void destroy() throws Exception
/*    */   {
/* 54 */     close();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\jta\atomikos\AtomikosDataSourceBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */