/*    */ package org.apache.tomcat.util.bcel.classfile;
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
/*    */ public class ArrayElementValue
/*    */   extends ElementValue
/*    */ {
/*    */   private final ElementValue[] evalues;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   ArrayElementValue(int type, ElementValue[] datums, ConstantPool cpool)
/*    */   {
/* 27 */     super(type, cpool);
/* 28 */     if (type != 91) {
/* 29 */       throw new RuntimeException("Only element values of type array can be built with this ctor - type specified: " + type);
/*    */     }
/*    */     
/* 32 */     this.evalues = datums;
/*    */   }
/*    */   
/*    */ 
/*    */   public String stringifyValue()
/*    */   {
/* 38 */     StringBuilder sb = new StringBuilder();
/* 39 */     sb.append("[");
/* 40 */     for (int i = 0; i < this.evalues.length; i++)
/*    */     {
/* 42 */       sb.append(this.evalues[i].stringifyValue());
/* 43 */       if (i + 1 < this.evalues.length) {
/* 44 */         sb.append(",");
/*    */       }
/*    */     }
/* 47 */     sb.append("]");
/* 48 */     return sb.toString();
/*    */   }
/*    */   
/*    */   public ElementValue[] getElementValuesArray()
/*    */   {
/* 53 */     return this.evalues;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\bcel\classfile\ArrayElementValue.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */