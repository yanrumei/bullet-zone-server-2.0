/*    */ package org.springframework.aop.aspectj.annotation;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.springframework.aop.aspectj.SingletonAspectInstanceFactory;
/*    */ import org.springframework.core.annotation.OrderUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SingletonMetadataAwareAspectInstanceFactory
/*    */   extends SingletonAspectInstanceFactory
/*    */   implements MetadataAwareAspectInstanceFactory, Serializable
/*    */ {
/*    */   private final AspectMetadata metadata;
/*    */   
/*    */   public SingletonMetadataAwareAspectInstanceFactory(Object aspectInstance, String aspectName)
/*    */   {
/* 48 */     super(aspectInstance);
/* 49 */     this.metadata = new AspectMetadata(aspectInstance.getClass(), aspectName);
/*    */   }
/*    */   
/*    */ 
/*    */   public final AspectMetadata getAspectMetadata()
/*    */   {
/* 55 */     return this.metadata;
/*    */   }
/*    */   
/*    */   public Object getAspectCreationMutex()
/*    */   {
/* 60 */     return this;
/*    */   }
/*    */   
/*    */   protected int getOrderForAspectClass(Class<?> aspectClass)
/*    */   {
/* 65 */     return OrderUtils.getOrder(aspectClass, Integer.valueOf(Integer.MAX_VALUE)).intValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\aspectj\annotation\SingletonMetadataAwareAspectInstanceFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */