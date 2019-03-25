/*    */ package org.springframework.context;
/*    */ 
/*    */ import org.springframework.core.ResolvableType;
/*    */ import org.springframework.core.ResolvableTypeProvider;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class PayloadApplicationEvent<T>
/*    */   extends ApplicationEvent
/*    */   implements ResolvableTypeProvider
/*    */ {
/*    */   private final T payload;
/*    */   
/*    */   public PayloadApplicationEvent(Object source, T payload)
/*    */   {
/* 44 */     super(source);
/* 45 */     Assert.notNull(payload, "Payload must not be null");
/* 46 */     this.payload = payload;
/*    */   }
/*    */   
/*    */ 
/*    */   public ResolvableType getResolvableType()
/*    */   {
/* 52 */     return ResolvableType.forClassWithGenerics(getClass(), new ResolvableType[] { ResolvableType.forInstance(getPayload()) });
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public T getPayload()
/*    */   {
/* 59 */     return (T)this.payload;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\PayloadApplicationEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */