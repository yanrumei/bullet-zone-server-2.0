/*    */ package org.apache.tomcat.util.digester;
/*    */ 
/*    */ import org.xml.sax.Attributes;
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
/*    */ public abstract class Rule
/*    */ {
/* 42 */   protected Digester digester = null;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 48 */   protected String namespaceURI = null;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Digester getDigester()
/*    */   {
/* 59 */     return this.digester;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setDigester(Digester digester)
/*    */   {
/* 70 */     this.digester = digester;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getNamespaceURI()
/*    */   {
/* 81 */     return this.namespaceURI;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setNamespaceURI(String namespaceURI)
/*    */   {
/* 92 */     this.namespaceURI = namespaceURI;
/*    */   }
/*    */   
/*    */   public void begin(String namespace, String name, Attributes attributes)
/*    */     throws Exception
/*    */   {}
/*    */   
/*    */   public void body(String namespace, String name, String text)
/*    */     throws Exception
/*    */   {}
/*    */   
/*    */   public void end(String namespace, String name)
/*    */     throws Exception
/*    */   {}
/*    */   
/*    */   public void finish()
/*    */     throws Exception
/*    */   {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\digester\Rule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */