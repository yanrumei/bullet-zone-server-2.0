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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ConstantClass
/*    */   extends Constant
/*    */ {
/*    */   private final int name_index;
/*    */   
/*    */   ConstantClass(DataInput file)
/*    */     throws IOException
/*    */   {
/* 43 */     super((byte)7);
/* 44 */     this.name_index = file.readUnsignedShort();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final int getNameIndex()
/*    */   {
/* 52 */     return this.name_index;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\bcel\classfile\ConstantClass.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */