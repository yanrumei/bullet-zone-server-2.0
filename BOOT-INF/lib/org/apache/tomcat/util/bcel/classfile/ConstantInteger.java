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
/*    */ public final class ConstantInteger
/*    */   extends Constant
/*    */ {
/*    */   private final int bytes;
/*    */   
/*    */   ConstantInteger(DataInput file)
/*    */     throws IOException
/*    */   {
/* 43 */     super((byte)3);
/* 44 */     this.bytes = file.readInt();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final int getBytes()
/*    */   {
/* 52 */     return this.bytes;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\bcel\classfile\ConstantInteger.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */