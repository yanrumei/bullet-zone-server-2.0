/*    */ package org.apache.naming;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import javax.naming.NameClassPair;
/*    */ import javax.naming.NamingEnumeration;
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
/*    */ public class NamingContextEnumeration
/*    */   implements NamingEnumeration<NameClassPair>
/*    */ {
/*    */   protected final Iterator<NamingEntry> iterator;
/*    */   
/*    */   public NamingContextEnumeration(Iterator<NamingEntry> entries)
/*    */   {
/* 40 */     this.iterator = entries;
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
/*    */   public NameClassPair next()
/*    */     throws NamingException
/*    */   {
/* 62 */     return nextElement();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean hasMore()
/*    */     throws NamingException
/*    */   {
/* 72 */     return this.iterator.hasNext();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void close()
/*    */     throws NamingException
/*    */   {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean hasMoreElements()
/*    */   {
/* 87 */     return this.iterator.hasNext();
/*    */   }
/*    */   
/*    */ 
/*    */   public NameClassPair nextElement()
/*    */   {
/* 93 */     NamingEntry entry = (NamingEntry)this.iterator.next();
/* 94 */     return new NameClassPair(entry.name, entry.value.getClass().getName());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\NamingContextEnumeration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */