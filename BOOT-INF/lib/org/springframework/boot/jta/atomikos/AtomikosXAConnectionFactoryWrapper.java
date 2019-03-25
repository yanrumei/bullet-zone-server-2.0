/*    */ package org.springframework.boot.jta.atomikos;
/*    */ 
/*    */ import javax.jms.ConnectionFactory;
/*    */ import javax.jms.XAConnectionFactory;
/*    */ import org.springframework.boot.jta.XAConnectionFactoryWrapper;
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
/*    */ public class AtomikosXAConnectionFactoryWrapper
/*    */   implements XAConnectionFactoryWrapper
/*    */ {
/*    */   public ConnectionFactory wrapConnectionFactory(XAConnectionFactory connectionFactory)
/*    */   {
/* 36 */     AtomikosConnectionFactoryBean bean = new AtomikosConnectionFactoryBean();
/* 37 */     bean.setXaConnectionFactory(connectionFactory);
/* 38 */     return bean;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\jta\atomikos\AtomikosXAConnectionFactoryWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */