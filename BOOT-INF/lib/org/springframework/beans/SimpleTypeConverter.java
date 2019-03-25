/*    */ package org.springframework.beans;
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
/*    */ public class SimpleTypeConverter
/*    */   extends TypeConverterSupport
/*    */ {
/*    */   public SimpleTypeConverter()
/*    */   {
/* 36 */     this.typeConverterDelegate = new TypeConverterDelegate(this);
/* 37 */     registerDefaultEditors();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\SimpleTypeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */