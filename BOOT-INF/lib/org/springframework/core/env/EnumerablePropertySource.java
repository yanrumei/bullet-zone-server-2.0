/*    */ package org.springframework.core.env;
/*    */ 
/*    */ import org.springframework.util.ObjectUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class EnumerablePropertySource<T>
/*    */   extends PropertySource<T>
/*    */ {
/*    */   public EnumerablePropertySource(String name, T source)
/*    */   {
/* 47 */     super(name, source);
/*    */   }
/*    */   
/*    */   protected EnumerablePropertySource(String name) {
/* 51 */     super(name);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean containsProperty(String name)
/*    */   {
/* 63 */     return ObjectUtils.containsElement(getPropertyNames(), name);
/*    */   }
/*    */   
/*    */   public abstract String[] getPropertyNames();
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\env\EnumerablePropertySource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */