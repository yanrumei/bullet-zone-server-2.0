/*    */ package org.apache.tomcat.util.bcel.classfile;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.EOFException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class Utility
/*    */ {
/*    */   static String compactClassName(String str)
/*    */   {
/* 44 */     return str.replace('/', '.');
/*    */   }
/*    */   
/*    */   static String getClassName(ConstantPool constant_pool, int index) {
/* 48 */     Constant c = constant_pool.getConstant(index, (byte)7);
/* 49 */     int i = ((ConstantClass)c).getNameIndex();
/*    */     
/*    */ 
/* 52 */     c = constant_pool.getConstant(i, (byte)1);
/* 53 */     String name = ((ConstantUtf8)c).getBytes();
/*    */     
/* 55 */     return compactClassName(name);
/*    */   }
/*    */   
/*    */   static void skipFully(DataInput file, int length) throws IOException {
/* 59 */     int total = file.skipBytes(length);
/* 60 */     if (total != length) {
/* 61 */       throw new EOFException();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   static void swallowFieldOrMethod(DataInput file)
/*    */     throws IOException
/*    */   {
/* 70 */     skipFully(file, 6);
/*    */     
/* 72 */     int attributes_count = file.readUnsignedShort();
/* 73 */     for (int i = 0; i < attributes_count; i++) {
/* 74 */       swallowAttribute(file);
/*    */     }
/*    */   }
/*    */   
/*    */   static void swallowAttribute(DataInput file)
/*    */     throws IOException
/*    */   {
/* 81 */     skipFully(file, 2);
/*    */     
/* 83 */     int length = file.readInt();
/* 84 */     skipFully(file, length);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\bcel\classfile\Utility.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */