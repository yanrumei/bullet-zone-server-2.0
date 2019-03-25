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
/*    */ public final class ConstantFloat
/*    */   extends Constant
/*    */ {
/*    */   private final float bytes;
/*    */   
/*    */   ConstantFloat(DataInput file)
/*    */     throws IOException
/*    */   {
/* 43 */     super((byte)4);
/* 44 */     this.bytes = file.readFloat();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final float getBytes()
/*    */   {
/* 52 */     return this.bytes;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\bcel\classfile\ConstantFloat.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */