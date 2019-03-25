/*    */ package ch.qos.logback.core.pattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class LiteralConverter<E>
/*    */   extends Converter<E>
/*    */ {
/*    */   String literal;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public LiteralConverter(String literal)
/*    */   {
/* 21 */     this.literal = literal;
/*    */   }
/*    */   
/*    */   public String convert(E o) {
/* 25 */     return this.literal;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\pattern\LiteralConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */