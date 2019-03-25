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
/*    */ 
/*    */ public class ClassElementValue
/*    */   extends ElementValue
/*    */ {
/*    */   private final int idx;
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
/*    */   ClassElementValue(int type, int idx, ConstantPool cpool)
/*    */   {
/* 30 */     super(type, cpool);
/* 31 */     this.idx = idx;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String stringifyValue()
/*    */   {
/* 38 */     ConstantUtf8 cu8 = (ConstantUtf8)super.getConstantPool().getConstant(this.idx, (byte)1);
/*    */     
/* 40 */     return cu8.getBytes();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\bcel\classfile\ClassElementValue.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */