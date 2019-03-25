/*    */ package org.springframework.util.xml;
/*    */ 
/*    */ import javax.xml.transform.Transformer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class TransformerUtils
/*    */ {
/*    */   public static final int DEFAULT_INDENT_AMOUNT = 2;
/*    */   
/*    */   public static void enableIndenting(Transformer transformer)
/*    */   {
/* 50 */     enableIndenting(transformer, 2);
/*    */   }
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
/*    */   public static void enableIndenting(Transformer transformer, int indentAmount)
/*    */   {
/* 64 */     Assert.notNull(transformer, "Transformer must not be null");
/* 65 */     Assert.isTrue(indentAmount > -1, "The indent amount cannot be less than zero : got " + indentAmount);
/* 66 */     transformer.setOutputProperty("indent", "yes");
/*    */     try
/*    */     {
/* 69 */       transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indentAmount));
/*    */     }
/*    */     catch (IllegalArgumentException localIllegalArgumentException) {}
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void disableIndenting(Transformer transformer)
/*    */   {
/* 82 */     Assert.notNull(transformer, "Transformer must not be null");
/* 83 */     transformer.setOutputProperty("indent", "no");
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\xml\TransformerUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */