/*    */ package org.springframework.context.support;
/*    */ 
/*    */ import org.springframework.context.EmbeddedValueResolverAware;
/*    */ import org.springframework.util.StringValueResolver;
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
/*    */ public class EmbeddedValueResolutionSupport
/*    */   implements EmbeddedValueResolverAware
/*    */ {
/*    */   private StringValueResolver embeddedValueResolver;
/*    */   
/*    */   public void setEmbeddedValueResolver(StringValueResolver resolver)
/*    */   {
/* 36 */     this.embeddedValueResolver = resolver;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected String resolveEmbeddedValue(String value)
/*    */   {
/* 46 */     return this.embeddedValueResolver != null ? this.embeddedValueResolver.resolveStringValue(value) : value;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\support\EmbeddedValueResolutionSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */