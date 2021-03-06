/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class Location
/*    */ {
/*    */   private final Resource resource;
/*    */   private final Object source;
/*    */   
/*    */   public Location(Resource resource)
/*    */   {
/* 47 */     this(resource, null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Location(Resource resource, Object source)
/*    */   {
/* 57 */     Assert.notNull(resource, "Resource must not be null");
/* 58 */     this.resource = resource;
/* 59 */     this.source = source;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Resource getResource()
/*    */   {
/* 67 */     return this.resource;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Object getSource()
/*    */   {
/* 77 */     return this.source;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\parsing\Location.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */