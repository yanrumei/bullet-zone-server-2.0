/*    */ package org.springframework.aop.framework.adapter;
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
/*    */ public class UnknownAdviceTypeException
/*    */   extends IllegalArgumentException
/*    */ {
/*    */   public UnknownAdviceTypeException(Object advice)
/*    */   {
/* 38 */     super("Advice object [" + advice + "] is neither a supported subinterface of [org.aopalliance.aop.Advice] nor an [org.springframework.aop.Advisor]");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public UnknownAdviceTypeException(String message)
/*    */   {
/* 47 */     super(message);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\framework\adapter\UnknownAdviceTypeException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */