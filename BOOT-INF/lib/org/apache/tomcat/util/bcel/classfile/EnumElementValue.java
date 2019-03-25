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
/*    */ public class EnumElementValue
/*    */   extends ElementValue
/*    */ {
/*    */   private final int valueIdx;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   EnumElementValue(int type, int valueIdx, ConstantPool cpool)
/*    */   {
/* 27 */     super(type, cpool);
/* 28 */     if (type != 101) {
/* 29 */       throw new RuntimeException("Only element values of type enum can be built with this ctor - type specified: " + type);
/*    */     }
/* 31 */     this.valueIdx = valueIdx;
/*    */   }
/*    */   
/*    */ 
/*    */   public String stringifyValue()
/*    */   {
/* 37 */     ConstantUtf8 cu8 = (ConstantUtf8)super.getConstantPool().getConstant(this.valueIdx, (byte)1);
/*    */     
/* 39 */     return cu8.getBytes();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\bcel\classfile\EnumElementValue.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */