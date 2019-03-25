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
/*    */ public final class ConstantLong
/*    */   extends Constant
/*    */ {
/*    */   private final long bytes;
/*    */   
/*    */   ConstantLong(DataInput input)
/*    */     throws IOException
/*    */   {
/* 43 */     super((byte)5);
/* 44 */     this.bytes = input.readLong();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final long getBytes()
/*    */   {
/* 52 */     return this.bytes;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\bcel\classfile\ConstantLong.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */