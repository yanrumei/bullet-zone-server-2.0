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
/*    */ public final class ConstantDouble
/*    */   extends Constant
/*    */ {
/*    */   private final double bytes;
/*    */   
/*    */   ConstantDouble(DataInput file)
/*    */     throws IOException
/*    */   {
/* 43 */     super((byte)6);
/* 44 */     this.bytes = file.readDouble();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final double getBytes()
/*    */   {
/* 52 */     return this.bytes;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\bcel\classfile\ConstantDouble.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */