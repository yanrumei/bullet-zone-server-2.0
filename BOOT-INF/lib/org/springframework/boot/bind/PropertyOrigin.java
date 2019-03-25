/*    */ package org.springframework.boot.bind;
/*    */ 
/*    */ import org.springframework.core.env.PropertySource;
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
/*    */ public class PropertyOrigin
/*    */ {
/*    */   private final PropertySource<?> source;
/*    */   private final String name;
/*    */   
/*    */   PropertyOrigin(PropertySource<?> source, String name)
/*    */   {
/* 35 */     this.name = name;
/* 36 */     this.source = source;
/*    */   }
/*    */   
/*    */   public PropertySource<?> getSource() {
/* 40 */     return this.source;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 44 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\bind\PropertyOrigin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */