/*    */ package org.springframework.jmx.export.metadata;
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
/*    */ public class ManagedOperationParameter
/*    */ {
/* 28 */   private int index = 0;
/*    */   
/* 30 */   private String name = "";
/*    */   
/* 32 */   private String description = "";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setIndex(int index)
/*    */   {
/* 39 */     this.index = index;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public int getIndex()
/*    */   {
/* 46 */     return this.index;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void setName(String name)
/*    */   {
/* 53 */     this.name = name;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getName()
/*    */   {
/* 60 */     return this.name;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void setDescription(String description)
/*    */   {
/* 67 */     this.description = description;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getDescription()
/*    */   {
/* 74 */     return this.description;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\export\metadata\ManagedOperationParameter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */