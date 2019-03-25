/*    */ package org.springframework.boot.autoconfigure.social;
/*    */ 
/*    */ import org.springframework.core.env.Environment;
/*    */ import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
/*    */ import org.springframework.social.config.annotation.SocialConfigurerAdapter;
/*    */ import org.springframework.social.connect.ConnectionFactory;
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
/*    */ public abstract class SocialAutoConfigurerAdapter
/*    */   extends SocialConfigurerAdapter
/*    */ {
/*    */   public void addConnectionFactories(ConnectionFactoryConfigurer configurer, Environment environment)
/*    */   {
/* 36 */     configurer.addConnectionFactory(createConnectionFactory());
/*    */   }
/*    */   
/*    */   protected abstract ConnectionFactory<?> createConnectionFactory();
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\social\SocialAutoConfigurerAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */