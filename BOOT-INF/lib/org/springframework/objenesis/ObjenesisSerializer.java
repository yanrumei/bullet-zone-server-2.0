/*    */ package org.springframework.objenesis;
/*    */ 
/*    */ import org.springframework.objenesis.strategy.SerializingInstantiatorStrategy;
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
/*    */ public class ObjenesisSerializer
/*    */   extends ObjenesisBase
/*    */ {
/*    */   public ObjenesisSerializer()
/*    */   {
/* 31 */     super(new SerializingInstantiatorStrategy());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ObjenesisSerializer(boolean useCache)
/*    */   {
/* 41 */     super(new SerializingInstantiatorStrategy(), useCache);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\objenesis\ObjenesisSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */