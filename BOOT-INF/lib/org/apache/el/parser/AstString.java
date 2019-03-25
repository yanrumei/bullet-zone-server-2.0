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
/*    */ public final class AstString
/*    */   extends SimpleNode
/*    */ {
/*    */   private volatile String string;
/*    */   
/*    */   public AstString(int id)
/*    */   {
/* 31 */     super(id);
/*    */   }
/*    */   
/*    */ 
/*    */   public String getString()
/*    */   {
/* 37 */     if (this.string == null) {
/* 38 */       this.string = this.image.substring(1, this.image.length() - 1);
/*    */     }
/* 40 */     return this.string;
/*    */   }
/*    */   
/*    */   public Class<?> getType(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 46 */     return String.class;
/*    */   }
/*    */   
/*    */   public Object getValue(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 52 */     return getString();
/*    */   }
/*    */   
/*    */   public void setImage(String image)
/*    */   {
/* 57 */     if (image.indexOf('\\') == -1) {
/* 58 */       this.image = image;
/* 59 */       return;
/*    */     }
/* 61 */     int size = image.length();
/* 62 */     StringBuilder buf = new StringBuilder(size);
/* 63 */     for (int i = 0; i < size; i++) {
/* 64 */       char c = image.charAt(i);
/* 65 */       if ((c == '\\') && (i + 1 < size)) {
/* 66 */         char c1 = image.charAt(i + 1);
/* 67 */         if ((c1 == '\\') || (c1 == '"') || (c1 == '\'')) {
/* 68 */           c = c1;
/* 69 */           i++;
/*    */         }
/*    */       }
/* 72 */       buf.append(c);
/*    */     }
/* 74 */     this.image = buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstString.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */