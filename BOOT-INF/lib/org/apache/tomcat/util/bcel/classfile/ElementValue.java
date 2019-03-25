/*    */ package org.apache.tomcat.util.bcel.classfile;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ElementValue
/*    */ {
/*    */   private final int type;
/*    */   private final ConstantPool cpool;
/*    */   public static final byte STRING = 115;
/*    */   public static final byte ENUM_CONSTANT = 101;
/*    */   public static final byte CLASS = 99;
/*    */   public static final byte ANNOTATION = 64;
/*    */   public static final byte ARRAY = 91;
/*    */   public static final byte PRIMITIVE_INT = 73;
/*    */   public static final byte PRIMITIVE_BYTE = 66;
/*    */   public static final byte PRIMITIVE_CHAR = 67;
/*    */   public static final byte PRIMITIVE_DOUBLE = 68;
/*    */   public static final byte PRIMITIVE_FLOAT = 70;
/*    */   public static final byte PRIMITIVE_LONG = 74;
/*    */   public static final byte PRIMITIVE_SHORT = 83;
/*    */   public static final byte PRIMITIVE_BOOLEAN = 90;
/*    */   
/*    */   ElementValue(int type, ConstantPool cpool)
/*    */   {
/* 30 */     this.type = type;
/* 31 */     this.cpool = cpool;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract String stringifyValue();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static ElementValue readElementValue(DataInput input, ConstantPool cpool)
/*    */     throws IOException
/*    */   {
/* 52 */     byte type = input.readByte();
/* 53 */     switch (type)
/*    */     {
/*    */     case 66: 
/*    */     case 67: 
/*    */     case 68: 
/*    */     case 70: 
/*    */     case 73: 
/*    */     case 74: 
/*    */     case 83: 
/*    */     case 90: 
/*    */     case 115: 
/* 64 */       return new SimpleElementValue(type, input.readUnsignedShort(), cpool);
/*    */     
/*    */     case 101: 
/* 67 */       input.readUnsignedShort();
/* 68 */       return new EnumElementValue(101, input.readUnsignedShort(), cpool);
/*    */     
/*    */     case 99: 
/* 71 */       return new ClassElementValue(99, input.readUnsignedShort(), cpool);
/*    */     
/*    */ 
/*    */     case 64: 
/* 75 */       return new AnnotationElementValue(64, new AnnotationEntry(input, cpool), cpool);
/*    */     
/*    */     case 91: 
/* 78 */       int numArrayVals = input.readUnsignedShort();
/* 79 */       ElementValue[] evalues = new ElementValue[numArrayVals];
/* 80 */       for (int j = 0; j < numArrayVals; j++)
/*    */       {
/* 82 */         evalues[j] = readElementValue(input, cpool);
/*    */       }
/* 84 */       return new ArrayElementValue(91, evalues, cpool);
/*    */     }
/*    */     
/* 87 */     throw new ClassFormatException("Unexpected element value kind in annotation: " + type);
/*    */   }
/*    */   
/*    */ 
/*    */   final ConstantPool getConstantPool()
/*    */   {
/* 93 */     return this.cpool;
/*    */   }
/*    */   
/*    */   final int getType() {
/* 97 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\bcel\classfile\ElementValue.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */