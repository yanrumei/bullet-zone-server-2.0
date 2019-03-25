/*    */ package org.springframework.expression;
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
/*    */ public abstract interface ParserContext
/*    */ {
/* 61 */   public static final ParserContext TEMPLATE_EXPRESSION = new ParserContext()
/*    */   {
/*    */     public String getExpressionPrefix()
/*    */     {
/* 65 */       return "#{";
/*    */     }
/*    */     
/*    */     public String getExpressionSuffix()
/*    */     {
/* 70 */       return "}";
/*    */     }
/*    */     
/*    */     public boolean isTemplate()
/*    */     {
/* 75 */       return true;
/*    */     }
/*    */   };
/*    */   
/*    */   public abstract boolean isTemplate();
/*    */   
/*    */   public abstract String getExpressionPrefix();
/*    */   
/*    */   public abstract String getExpressionSuffix();
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\ParserContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */