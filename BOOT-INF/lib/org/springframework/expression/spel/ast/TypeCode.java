/*    */ package org.springframework.expression.spel.ast;
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
/*    */ public enum TypeCode
/*    */ {
/* 27 */   OBJECT(Object.class), 
/*    */   
/* 29 */   BOOLEAN(Boolean.TYPE), 
/*    */   
/* 31 */   BYTE(Byte.TYPE), 
/*    */   
/* 33 */   CHAR(Character.TYPE), 
/*    */   
/* 35 */   SHORT(Short.TYPE), 
/*    */   
/* 37 */   INT(Integer.TYPE), 
/*    */   
/* 39 */   LONG(Long.TYPE), 
/*    */   
/* 41 */   FLOAT(Float.TYPE), 
/*    */   
/* 43 */   DOUBLE(Double.TYPE);
/*    */   
/*    */ 
/*    */   private Class<?> type;
/*    */   
/*    */   private TypeCode(Class<?> type)
/*    */   {
/* 50 */     this.type = type;
/*    */   }
/*    */   
/*    */   public Class<?> getType()
/*    */   {
/* 55 */     return this.type;
/*    */   }
/*    */   
/*    */   public static TypeCode forName(String name)
/*    */   {
/* 60 */     String searchingFor = name.toUpperCase();
/* 61 */     TypeCode[] tcs = values();
/* 62 */     for (int i = 1; i < tcs.length; i++) {
/* 63 */       if (tcs[i].name().equals(searchingFor)) {
/* 64 */         return tcs[i];
/*    */       }
/*    */     }
/* 67 */     return OBJECT;
/*    */   }
/*    */   
/*    */   public static TypeCode forClass(Class<?> clazz) {
/* 71 */     TypeCode[] allValues = values();
/* 72 */     for (TypeCode typeCode : allValues) {
/* 73 */       if (clazz == typeCode.getType()) {
/* 74 */         return typeCode;
/*    */       }
/*    */     }
/* 77 */     return OBJECT;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\ast\TypeCode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */