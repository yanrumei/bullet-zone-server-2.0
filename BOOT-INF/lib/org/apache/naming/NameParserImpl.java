/*    */ package org.apache.naming;
/*    */ 
/*    */ import javax.naming.CompositeName;
/*    */ import javax.naming.Name;
/*    */ import javax.naming.NameParser;
/*    */ import javax.naming.NamingException;
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
/*    */ public class NameParserImpl
/*    */   implements NameParser
/*    */ {
/*    */   public Name parse(String name)
/*    */     throws NamingException
/*    */   {
/* 51 */     return new CompositeName(name);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\NameParserImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */