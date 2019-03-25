/*    */ package org.apache.tomcat.util.bcel.classfile;
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
/*    */ public class SimpleElementValue
/*    */   extends ElementValue
/*    */ {
/*    */   private final int index;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   SimpleElementValue(int type, int index, ConstantPool cpool)
/*    */   {
/* 27 */     super(type, cpool);
/* 28 */     this.index = index;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getIndex()
/*    */   {
/* 36 */     return this.index;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String stringifyValue()
/*    */   {
/* 44 */     ConstantPool cpool = super.getConstantPool();
/* 45 */     int _type = super.getType();
/* 46 */     switch (_type)
/*    */     {
/*    */     case 73: 
/* 49 */       ConstantInteger c = (ConstantInteger)cpool.getConstant(getIndex(), (byte)3);
/*    */       
/* 51 */       return Integer.toString(c.getBytes());
/*    */     case 74: 
/* 53 */       ConstantLong j = (ConstantLong)cpool.getConstant(getIndex(), (byte)5);
/*    */       
/* 55 */       return Long.toString(j.getBytes());
/*    */     case 68: 
/* 57 */       ConstantDouble d = (ConstantDouble)cpool.getConstant(getIndex(), (byte)6);
/*    */       
/* 59 */       return Double.toString(d.getBytes());
/*    */     case 70: 
/* 61 */       ConstantFloat f = (ConstantFloat)cpool.getConstant(getIndex(), (byte)4);
/*    */       
/* 63 */       return Float.toString(f.getBytes());
/*    */     case 83: 
/* 65 */       ConstantInteger s = (ConstantInteger)cpool.getConstant(getIndex(), (byte)3);
/*    */       
/* 67 */       return Integer.toString(s.getBytes());
/*    */     case 66: 
/* 69 */       ConstantInteger b = (ConstantInteger)cpool.getConstant(getIndex(), (byte)3);
/*    */       
/* 71 */       return Integer.toString(b.getBytes());
/*    */     case 67: 
/* 73 */       ConstantInteger ch = (ConstantInteger)cpool.getConstant(
/* 74 */         getIndex(), (byte)3);
/* 75 */       return String.valueOf((char)ch.getBytes());
/*    */     case 90: 
/* 77 */       ConstantInteger bo = (ConstantInteger)cpool.getConstant(
/* 78 */         getIndex(), (byte)3);
/* 79 */       if (bo.getBytes() == 0) {
/* 80 */         return "false";
/*    */       }
/* 82 */       return "true";
/*    */     case 115: 
/* 84 */       ConstantUtf8 cu8 = (ConstantUtf8)cpool.getConstant(getIndex(), (byte)1);
/*    */       
/* 86 */       return cu8.getBytes();
/*    */     }
/* 88 */     throw new RuntimeException("SimpleElementValue class does not know how to stringify type " + _type);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\bcel\classfile\SimpleElementValue.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */