/*    */ package org.apache.tomcat.util.descriptor.web;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public class SecurityRoleRef
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 41 */   private String name = null;
/*    */   
/*    */   public String getName() {
/* 44 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 48 */     this.name = name;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 55 */   private String link = null;
/*    */   
/*    */   public String getLink() {
/* 58 */     return this.link;
/*    */   }
/*    */   
/*    */   public void setLink(String link) {
/* 62 */     this.link = link;
/*    */   }
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
/*    */   public String toString()
/*    */   {
/* 76 */     StringBuilder sb = new StringBuilder("SecurityRoleRef[");
/* 77 */     sb.append("name=");
/* 78 */     sb.append(this.name);
/* 79 */     if (this.link != null) {
/* 80 */       sb.append(", link=");
/* 81 */       sb.append(this.link);
/*    */     }
/* 83 */     sb.append("]");
/* 84 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\SecurityRoleRef.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */