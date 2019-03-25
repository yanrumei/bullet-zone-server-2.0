/*     */ package org.apache.tomcat.util.bcel.classfile;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.IOException;
/*     */ import org.apache.tomcat.util.bcel.Const;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConstantPool
/*     */ {
/*     */   private final Constant[] constant_pool;
/*     */   
/*     */   ConstantPool(DataInput input)
/*     */     throws IOException, ClassFormatException
/*     */   {
/*  48 */     int constant_pool_count = input.readUnsignedShort();
/*  49 */     this.constant_pool = new Constant[constant_pool_count];
/*     */     
/*     */ 
/*     */ 
/*  53 */     for (int i = 1; i < constant_pool_count; i++) {
/*  54 */       this.constant_pool[i] = Constant.readConstant(input);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  62 */       if (this.constant_pool[i] != null) {
/*  63 */         byte tag = this.constant_pool[i].getTag();
/*  64 */         if ((tag == 6) || (tag == 5)) {
/*  65 */           i++;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Constant getConstant(int index)
/*     */   {
/*  80 */     if ((index >= this.constant_pool.length) || (index < 0)) {
/*  81 */       throw new ClassFormatException("Invalid constant pool reference: " + index + ". Constant pool size is: " + this.constant_pool.length);
/*     */     }
/*     */     
/*  84 */     return this.constant_pool[index];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Constant getConstant(int index, byte tag)
/*     */     throws ClassFormatException
/*     */   {
/* 100 */     Constant c = getConstant(index);
/* 101 */     if (c == null) {
/* 102 */       throw new ClassFormatException("Constant pool at index " + index + " is null.");
/*     */     }
/* 104 */     if (c.getTag() != tag) {
/* 105 */       throw new ClassFormatException("Expected class `" + Const.getConstantName(tag) + "' at index " + index + " and got " + c);
/*     */     }
/*     */     
/* 108 */     return c;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\bcel\classfile\ConstantPool.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */