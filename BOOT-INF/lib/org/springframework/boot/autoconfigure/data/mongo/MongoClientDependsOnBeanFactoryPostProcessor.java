/*    */ package org.springframework.boot.autoconfigure.data.mongo;
/*    */ 
/*    */ import com.mongodb.MongoClient;
/*    */ import org.springframework.boot.autoconfigure.AbstractDependsOnBeanFactoryPostProcessor;
/*    */ import org.springframework.core.annotation.Order;
/*    */ import org.springframework.data.mongodb.core.MongoClientFactoryBean;
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
/*    */ @Order(Integer.MAX_VALUE)
/*    */ public class MongoClientDependsOnBeanFactoryPostProcessor
/*    */   extends AbstractDependsOnBeanFactoryPostProcessor
/*    */ {
/*    */   public MongoClientDependsOnBeanFactoryPostProcessor(String... dependsOn)
/*    */   {
/* 41 */     super(MongoClient.class, MongoClientFactoryBean.class, dependsOn);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\mongo\MongoClientDependsOnBeanFactoryPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */