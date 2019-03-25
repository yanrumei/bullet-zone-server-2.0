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
/*    */ public class AnnotationElementValue
/*    */   extends ElementValue
/*    */ {
/*    */   private final AnnotationEntry annotationEntry;
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
/*    */   AnnotationElementValue(int type, AnnotationEntry annotationEntry, ConstantPool cpool)
/*    */   {
/* 28 */     super(type, cpool);
/* 29 */     if (type != 64) {
/* 30 */       throw new RuntimeException("Only element values of type annotation can be built with this ctor - type specified: " + type);
/*    */     }
/*    */     
/* 33 */     this.annotationEntry = annotationEntry;
/*    */   }
/*    */   
/*    */ 
/*    */   public String stringifyValue()
/*    */   {
/* 39 */     return this.annotationEntry.toString();
/*    */   }
/*    */   
/*    */   public AnnotationEntry getAnnotationEntry()
/*    */   {
/* 44 */     return this.annotationEntry;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\bcel\classfile\AnnotationElementValue.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */