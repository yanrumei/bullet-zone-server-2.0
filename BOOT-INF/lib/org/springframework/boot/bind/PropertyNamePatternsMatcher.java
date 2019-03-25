/*    */ package org.springframework.boot.bind;
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
/*    */ abstract interface PropertyNamePatternsMatcher
/*    */ {
/* 27 */   public static final PropertyNamePatternsMatcher ALL = new PropertyNamePatternsMatcher()
/*    */   {
/*    */     public boolean matches(String propertyName)
/*    */     {
/* 31 */       return true;
/*    */     }
/*    */   };
/*    */   
/*    */ 
/* 36 */   public static final PropertyNamePatternsMatcher NONE = new PropertyNamePatternsMatcher()
/*    */   {
/*    */     public boolean matches(String propertyName)
/*    */     {
/* 40 */       return false;
/*    */     }
/*    */   };
/*    */   
/*    */   public abstract boolean matches(String paramString);
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\bind\PropertyNamePatternsMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */