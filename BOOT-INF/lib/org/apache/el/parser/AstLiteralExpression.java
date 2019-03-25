/*    */ package org.apache.el.parser;
/*    */ 
/*    */ import javax.el.ELException;
/*    */ import org.apache.el.lang.EvaluationContext;
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
/*    */ public final class AstLiteralExpression
/*    */   extends SimpleNode
/*    */ {
/*    */   public AstLiteralExpression(int id)
/*    */   {
/* 31 */     super(id);
/*    */   }
/*    */   
/*    */   public Class<?> getType(EvaluationContext ctx) throws ELException
/*    */   {
/* 36 */     return String.class;
/*    */   }
/*    */   
/*    */   public Object getValue(EvaluationContext ctx) throws ELException
/*    */   {
/* 41 */     return this.image;
/*    */   }
/*    */   
/*    */   public void setImage(String image)
/*    */   {
/* 46 */     if (image.indexOf('\\') == -1) {
/* 47 */       this.image = image;
/* 48 */       return;
/*    */     }
/* 50 */     int size = image.length();
/* 51 */     StringBuilder buf = new StringBuilder(size);
/* 52 */     for (int i = 0; i < size; i++) {
/* 53 */       char c = image.charAt(i);
/* 54 */       if ((c == '\\') && (i + 2 < size)) {
/* 55 */         char c1 = image.charAt(i + 1);
/* 56 */         char c2 = image.charAt(i + 2);
/* 57 */         if (((c1 == '#') || (c1 == '$')) && (c2 == '{')) {
/* 58 */           c = c1;
/* 59 */           i++;
/*    */         }
/*    */       }
/* 62 */       buf.append(c);
/*    */     }
/* 64 */     this.image = buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstLiteralExpression.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */