/*    */ package org.apache.tomcat.util.digester;
/*    */ 
/*    */ import org.apache.juli.logging.Log;
/*    */ import org.apache.tomcat.util.IntrospectionUtils;
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
/*    */ public class SetPropertiesRule
/*    */   extends Rule
/*    */ {
/*    */   public void begin(String namespace, String theName, Attributes attributes)
/*    */     throws Exception
/*    */   {
/* 48 */     Object top = this.digester.peek();
/* 49 */     if (this.digester.log.isDebugEnabled()) {
/* 50 */       if (top != null) {
/* 51 */         this.digester.log.debug("[SetPropertiesRule]{" + this.digester.match + "} Set " + top
/* 52 */           .getClass().getName() + " properties");
/*    */       }
/*    */       else {
/* 55 */         this.digester.log.debug("[SetPropertiesRule]{" + this.digester.match + "} Set NULL properties");
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 60 */     for (int i = 0; i < attributes.getLength(); i++) {
/* 61 */       String name = attributes.getLocalName(i);
/* 62 */       if ("".equals(name)) {
/* 63 */         name = attributes.getQName(i);
/*    */       }
/* 65 */       String value = attributes.getValue(i);
/*    */       
/* 67 */       if (this.digester.log.isDebugEnabled()) {
/* 68 */         this.digester.log.debug("[SetPropertiesRule]{" + this.digester.match + "} Setting property '" + name + "' to '" + value + "'");
/*    */       }
/*    */       
/*    */ 
/* 72 */       if ((!this.digester.isFakeAttribute(top, name)) && 
/* 73 */         (!IntrospectionUtils.setProperty(top, name, value)) && 
/* 74 */         (this.digester.getRulesValidation())) {
/* 75 */         this.digester.log.warn("[SetPropertiesRule]{" + this.digester.match + "} Setting property '" + name + "' to '" + value + "' did not find a matching property.");
/*    */       }
/*    */     }
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
/* 90 */     StringBuilder sb = new StringBuilder("SetPropertiesRule[");
/* 91 */     sb.append("]");
/* 92 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\digester\SetPropertiesRule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */