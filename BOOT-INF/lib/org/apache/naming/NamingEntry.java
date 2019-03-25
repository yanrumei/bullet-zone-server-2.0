/*    */ package org.apache.naming;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NamingEntry
/*    */ {
/*    */   public static final int ENTRY = 0;
/*    */   
/*    */ 
/*    */ 
/*    */   public static final int LINK_REF = 1;
/*    */   
/*    */ 
/*    */ 
/*    */   public static final int REFERENCE = 2;
/*    */   
/*    */ 
/*    */   public static final int CONTEXT = 10;
/*    */   
/*    */ 
/*    */   public int type;
/*    */   
/*    */ 
/*    */   public final String name;
/*    */   
/*    */ 
/*    */   public Object value;
/*    */   
/*    */ 
/*    */ 
/*    */   public NamingEntry(String name, Object value, int type)
/*    */   {
/* 34 */     this.name = name;
/* 35 */     this.value = value;
/* 36 */     this.type = type;
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
/*    */   public boolean equals(Object obj)
/*    */   {
/* 51 */     if ((obj instanceof NamingEntry)) {
/* 52 */       return this.name.equals(((NamingEntry)obj).name);
/*    */     }
/* 54 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 61 */     return this.name.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\NamingEntry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */