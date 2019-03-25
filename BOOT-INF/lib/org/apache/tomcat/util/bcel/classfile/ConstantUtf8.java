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
/*    */ public final class ConstantUtf8
/*    */   extends Constant
/*    */ {
/*    */   private final String bytes;
/*    */   
/*    */   static ConstantUtf8 getInstance(DataInput input)
/*    */     throws IOException
/*    */   {
/* 37 */     return new ConstantUtf8(input.readUTF());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private ConstantUtf8(String bytes)
/*    */   {
/* 45 */     super((byte)1);
/* 46 */     if (bytes == null) {
/* 47 */       throw new IllegalArgumentException("bytes must not be null!");
/*    */     }
/* 49 */     this.bytes = bytes;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final String getBytes()
/*    */   {
/* 57 */     return this.bytes;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\bcel\classfile\ConstantUtf8.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */