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
/*    */ public class Annotations
/*    */ {
/*    */   private final AnnotationEntry[] annotation_table;
/*    */   
/*    */   Annotations(DataInput input, ConstantPool constant_pool)
/*    */     throws IOException
/*    */   {
/* 35 */     int annotation_table_length = input.readUnsignedShort();
/* 36 */     this.annotation_table = new AnnotationEntry[annotation_table_length];
/* 37 */     for (int i = 0; i < annotation_table_length; i++) {
/* 38 */       this.annotation_table[i] = new AnnotationEntry(input, constant_pool);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public AnnotationEntry[] getAnnotationEntries()
/*    */   {
/* 47 */     return this.annotation_table;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\bcel\classfile\Annotations.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */