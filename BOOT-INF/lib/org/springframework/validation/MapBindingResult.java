/*    */ package org.springframework.validation;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MapBindingResult
/*    */   extends AbstractBindingResult
/*    */   implements Serializable
/*    */ {
/*    */   private final Map<?, ?> target;
/*    */   
/*    */   public MapBindingResult(Map<?, ?> target, String objectName)
/*    */   {
/* 48 */     super(objectName);
/* 49 */     Assert.notNull(target, "Target Map must not be null");
/* 50 */     this.target = target;
/*    */   }
/*    */   
/*    */   public final Map<?, ?> getTargetMap()
/*    */   {
/* 55 */     return this.target;
/*    */   }
/*    */   
/*    */   public final Object getTarget()
/*    */   {
/* 60 */     return this.target;
/*    */   }
/*    */   
/*    */   protected Object getActualFieldValue(String field)
/*    */   {
/* 65 */     return this.target.get(field);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\validation\MapBindingResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */