/*    */ package org.apache.tomcat.util.bcel.classfile;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class AnnotationEntry
/*    */ {
/*    */   private final int type_index;
/*    */   private final ConstantPool constant_pool;
/*    */   private final List<ElementValuePair> element_value_pairs;
/*    */   
/*    */   AnnotationEntry(DataInput input, ConstantPool constant_pool)
/*    */     throws IOException
/*    */   {
/* 46 */     this.constant_pool = constant_pool;
/*    */     
/* 48 */     this.type_index = input.readUnsignedShort();
/* 49 */     int num_element_value_pairs = input.readUnsignedShort();
/*    */     
/* 51 */     this.element_value_pairs = new ArrayList(num_element_value_pairs);
/* 52 */     for (int i = 0; i < num_element_value_pairs; i++) {
/* 53 */       this.element_value_pairs.add(new ElementValuePair(input, constant_pool));
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getAnnotationType()
/*    */   {
/* 61 */     ConstantUtf8 c = (ConstantUtf8)this.constant_pool.getConstant(this.type_index, (byte)1);
/* 62 */     return c.getBytes();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public List<ElementValuePair> getElementValuePairs()
/*    */   {
/* 69 */     return this.element_value_pairs;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\bcel\classfile\AnnotationEntry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */