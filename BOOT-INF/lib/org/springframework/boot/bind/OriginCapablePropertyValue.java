/*    */ package org.springframework.boot.bind;
/*    */ 
/*    */ import org.springframework.beans.PropertyValue;
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
/*    */ class OriginCapablePropertyValue
/*    */   extends PropertyValue
/*    */ {
/*    */   private static final String ATTRIBUTE_PROPERTY_ORIGIN = "propertyOrigin";
/*    */   private final PropertyOrigin origin;
/*    */   
/*    */   OriginCapablePropertyValue(PropertyValue propertyValue)
/*    */   {
/* 34 */     this(propertyValue.getName(), propertyValue.getValue(), 
/* 35 */       (PropertyOrigin)propertyValue.getAttribute("propertyOrigin"));
/*    */   }
/*    */   
/*    */   OriginCapablePropertyValue(String name, Object value, String originName, PropertySource<?> originSource)
/*    */   {
/* 40 */     this(name, value, new PropertyOrigin(originSource, originName));
/*    */   }
/*    */   
/*    */   OriginCapablePropertyValue(String name, Object value, PropertyOrigin origin) {
/* 44 */     super(name, value);
/* 45 */     this.origin = origin;
/* 46 */     setAttribute("propertyOrigin", origin);
/*    */   }
/*    */   
/*    */   public PropertyOrigin getOrigin() {
/* 50 */     return this.origin;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 55 */     String name = this.origin != null ? this.origin.getName() : getName();
/*    */     
/* 57 */     String source = this.origin.getSource() != null ? this.origin.getSource().getName() : "unknown";
/* 58 */     return "'" + name + "' from '" + source + "'";
/*    */   }
/*    */   
/*    */   public static PropertyOrigin getOrigin(PropertyValue propertyValue) {
/* 62 */     if ((propertyValue instanceof OriginCapablePropertyValue)) {
/* 63 */       return ((OriginCapablePropertyValue)propertyValue).getOrigin();
/*    */     }
/* 65 */     return new OriginCapablePropertyValue(propertyValue).getOrigin();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\bind\OriginCapablePropertyValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */