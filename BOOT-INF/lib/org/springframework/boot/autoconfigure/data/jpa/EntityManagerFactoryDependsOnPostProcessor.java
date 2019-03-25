/*    */ package org.springframework.boot.autoconfigure.data.jpa;
/*    */ 
/*    */ import javax.persistence.EntityManagerFactory;
/*    */ import org.springframework.boot.autoconfigure.AbstractDependsOnBeanFactoryPostProcessor;
/*    */ import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
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
/*    */ public class EntityManagerFactoryDependsOnPostProcessor
/*    */   extends AbstractDependsOnBeanFactoryPostProcessor
/*    */ {
/*    */   public EntityManagerFactoryDependsOnPostProcessor(String... dependsOn)
/*    */   {
/* 41 */     super(EntityManagerFactory.class, AbstractEntityManagerFactoryBean.class, dependsOn);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\jpa\EntityManagerFactoryDependsOnPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */