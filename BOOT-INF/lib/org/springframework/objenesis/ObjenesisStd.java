/*    */ package org.springframework.objenesis;
/*    */ 
/*    */ import org.springframework.objenesis.strategy.StdInstantiatorStrategy;
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
/*    */ public class ObjenesisStd
/*    */   extends ObjenesisBase
/*    */ {
/*    */   public ObjenesisStd()
/*    */   {
/* 31 */     super(new StdInstantiatorStrategy());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ObjenesisStd(boolean useCache)
/*    */   {
/* 41 */     super(new StdInstantiatorStrategy(), useCache);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\objenesis\ObjenesisStd.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */