/*    */ package org.apache.tomcat.util.bcel.classfile;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
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
/*    */ public class ElementValuePair
/*    */ {
/*    */   private final ElementValue elementValue;
/*    */   private final ConstantPool constantPool;
/*    */   private final int elementNameIndex;
/*    */   
/*    */   ElementValuePair(DataInput file, ConstantPool constantPool)
/*    */     throws IOException
/*    */   {
/* 39 */     this.constantPool = constantPool;
/* 40 */     this.elementNameIndex = file.readUnsignedShort();
/* 41 */     this.elementValue = ElementValue.readElementValue(file, constantPool);
/*    */   }
/*    */   
/*    */   public String getNameString()
/*    */   {
/* 46 */     ConstantUtf8 c = (ConstantUtf8)this.constantPool.getConstant(this.elementNameIndex, (byte)1);
/*    */     
/* 48 */     return c.getBytes();
/*    */   }
/*    */   
/*    */   public final ElementValue getValue()
/*    */   {
/* 53 */     return this.elementValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\bcel\classfile\ElementValuePair.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */