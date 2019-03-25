/*     */ package org.apache.tomcat.util.bcel.classfile;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.IOException;
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
/*     */ public abstract class Constant
/*     */ {
/*     */   protected final byte tag;
/*     */   
/*     */   Constant(byte tag)
/*     */   {
/*  46 */     this.tag = tag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final byte getTag()
/*     */   {
/*  55 */     return this.tag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static Constant readConstant(DataInput input)
/*     */     throws IOException, ClassFormatException
/*     */   {
/*  67 */     byte b = input.readByte();
/*     */     int skipSize;
/*  69 */     int skipSize; int skipSize; switch (b) {
/*     */     case 7: 
/*  71 */       return new ConstantClass(input);
/*     */     case 3: 
/*  73 */       return new ConstantInteger(input);
/*     */     case 4: 
/*  75 */       return new ConstantFloat(input);
/*     */     case 5: 
/*  77 */       return new ConstantLong(input);
/*     */     case 6: 
/*  79 */       return new ConstantDouble(input);
/*     */     case 1: 
/*  81 */       return ConstantUtf8.getInstance(input);
/*     */     case 8: 
/*     */     case 16: 
/*     */     case 19: 
/*     */     case 20: 
/*  86 */       skipSize = 2;
/*  87 */       break;
/*     */     case 15: 
/*  89 */       skipSize = 3;
/*  90 */       break;
/*     */     case 9: 
/*     */     case 10: 
/*     */     case 11: 
/*     */     case 12: 
/*     */     case 18: 
/*  96 */       skipSize = 4;
/*  97 */       break;
/*     */     case 2: case 13: case 14: case 17: default: 
/*  99 */       throw new ClassFormatException("Invalid byte tag in constant pool: " + b); }
/*     */     int skipSize;
/* 101 */     Utility.skipFully(input, skipSize);
/* 102 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 108 */     return "[" + this.tag + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\bcel\classfile\Constant.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */