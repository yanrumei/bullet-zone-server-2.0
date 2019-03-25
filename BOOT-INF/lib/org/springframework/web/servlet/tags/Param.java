/*    */ package org.springframework.web.servlet.tags;
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
/*    */ public class Param
/*    */ {
/*    */   private String name;
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
/*    */   private String value;
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
/*    */   public void setName(String name)
/*    */   {
/* 41 */     this.name = name;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getName()
/*    */   {
/* 48 */     return this.name;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void setValue(String value)
/*    */   {
/* 55 */     this.value = value;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getValue()
/*    */   {
/* 62 */     return this.value;
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 68 */     return "JSP Tag Param: name '" + this.name + "', value '" + this.value + "'";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\tags\Param.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */