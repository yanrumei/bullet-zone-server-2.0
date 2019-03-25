/*    */ package org.apache.catalina.mapper;
/*    */ 
/*    */ import org.apache.catalina.Wrapper;
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
/*    */ public class WrapperMappingInfo
/*    */ {
/*    */   private final String mapping;
/*    */   private final Wrapper wrapper;
/*    */   private final boolean jspWildCard;
/*    */   private final boolean resourceOnly;
/*    */   
/*    */   public WrapperMappingInfo(String mapping, Wrapper wrapper, boolean jspWildCard, boolean resourceOnly)
/*    */   {
/* 33 */     this.mapping = mapping;
/* 34 */     this.wrapper = wrapper;
/* 35 */     this.jspWildCard = jspWildCard;
/* 36 */     this.resourceOnly = resourceOnly;
/*    */   }
/*    */   
/*    */   public String getMapping() {
/* 40 */     return this.mapping;
/*    */   }
/*    */   
/*    */   public Wrapper getWrapper() {
/* 44 */     return this.wrapper;
/*    */   }
/*    */   
/*    */   public boolean isJspWildCard() {
/* 48 */     return this.jspWildCard;
/*    */   }
/*    */   
/*    */   public boolean isResourceOnly() {
/* 52 */     return this.resourceOnly;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\mapper\WrapperMappingInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */