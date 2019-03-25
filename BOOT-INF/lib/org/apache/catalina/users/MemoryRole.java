/*    */ package org.apache.catalina.users;
/*    */ 
/*    */ import org.apache.catalina.UserDatabase;
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
/*    */ 
/*    */ public class MemoryRole
/*    */   extends AbstractRole
/*    */ {
/*    */   protected final MemoryUserDatabase database;
/*    */   
/*    */   MemoryRole(MemoryUserDatabase database, String rolename, String description)
/*    */   {
/* 50 */     this.database = database;
/* 51 */     setRolename(rolename);
/* 52 */     setDescription(description);
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public UserDatabase getUserDatabase()
/*    */   {
/* 75 */     return this.database;
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
/* 89 */     StringBuilder sb = new StringBuilder("<role rolename=\"");
/* 90 */     sb.append(this.rolename);
/* 91 */     sb.append("\"");
/* 92 */     if (this.description != null) {
/* 93 */       sb.append(" description=\"");
/* 94 */       sb.append(this.description);
/* 95 */       sb.append("\"");
/*    */     }
/* 97 */     sb.append("/>");
/* 98 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\users\MemoryRole.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */