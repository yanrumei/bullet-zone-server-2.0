/*    */ package org.apache.catalina.startup;
/*    */ 
/*    */ import org.apache.juli.logging.Log;
/*    */ import org.apache.tomcat.util.IntrospectionUtils;
/*    */ import org.apache.tomcat.util.digester.Digester;
/*    */ import org.apache.tomcat.util.digester.Rule;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SetContextPropertiesRule
/*    */   extends Rule
/*    */ {
/*    */   public void begin(String namespace, String nameX, Attributes attributes)
/*    */     throws Exception
/*    */   {
/* 54 */     for (int i = 0; i < attributes.getLength(); i++) {
/* 55 */       String name = attributes.getLocalName(i);
/* 56 */       if ("".equals(name)) {
/* 57 */         name = attributes.getQName(i);
/*    */       }
/* 59 */       if ((!"path".equals(name)) && (!"docBase".equals(name)))
/*    */       {
/*    */ 
/* 62 */         String value = attributes.getValue(i);
/* 63 */         if ((!this.digester.isFakeAttribute(this.digester.peek(), name)) && 
/* 64 */           (!IntrospectionUtils.setProperty(this.digester.peek(), name, value)) && 
/* 65 */           (this.digester.getRulesValidation())) {
/* 66 */           this.digester.getLogger().warn("[SetContextPropertiesRule]{" + this.digester.getMatch() + "} Setting property '" + name + "' to '" + value + "' did not find a matching property.");
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\SetContextPropertiesRule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */