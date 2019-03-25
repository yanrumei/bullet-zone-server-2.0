/*    */ package org.apache.tomcat.util.descriptor.web;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
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
/*    */ public class ContextTransaction
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 42 */   private final HashMap<String, Object> properties = new HashMap();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Object getProperty(String name)
/*    */   {
/* 49 */     return this.properties.get(name);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setProperty(String name, Object value)
/*    */   {
/* 58 */     this.properties.put(name, value);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void removeProperty(String name)
/*    */   {
/* 66 */     this.properties.remove(name);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Iterator<String> listProperties()
/*    */   {
/* 74 */     return this.properties.keySet().iterator();
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
/*    */   public String toString()
/*    */   {
/* 87 */     StringBuilder sb = new StringBuilder("Transaction[");
/* 88 */     sb.append("]");
/* 89 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\ContextTransaction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */