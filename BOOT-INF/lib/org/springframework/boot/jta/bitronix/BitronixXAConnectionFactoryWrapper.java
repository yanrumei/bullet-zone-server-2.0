/*    */ package org.springframework.boot.jta.bitronix;
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
/*    */ public class BitronixXAConnectionFactoryWrapper
/*    */   implements XAConnectionFactoryWrapper
/*    */ {
/*    */   public ConnectionFactory wrapConnectionFactory(XAConnectionFactory connectionFactory)
/*    */   {
/* 36 */     PoolingConnectionFactoryBean pool = new PoolingConnectionFactoryBean();
/* 37 */     pool.setConnectionFactory(connectionFactory);
/* 38 */     return pool;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\jta\bitronix\BitronixXAConnectionFactoryWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */