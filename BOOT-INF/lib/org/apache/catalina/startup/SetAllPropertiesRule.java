/*    */ package org.apache.catalina.startup;
/*    */ 
/*    */ import java.util.HashMap;
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
/*    */ public class SetAllPropertiesRule
/*    */   extends Rule
/*    */ {
/*    */   public SetAllPropertiesRule() {}
/*    */   
/*    */   public SetAllPropertiesRule(String[] exclude)
/*    */   {
/* 39 */     for (int i = 0; i < exclude.length; i++) if (exclude[i] != null) { this.excludes.put(exclude[i], exclude[i]);
/*    */       }
/*    */   }
/*    */   
/* 43 */   protected final HashMap<String, String> excludes = new HashMap();
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
/*    */   public void begin(String namespace, String nameX, Attributes attributes)
/*    */     throws Exception
/*    */   {
/* 59 */     for (int i = 0; i < attributes.getLength(); i++) {
/* 60 */       String name = attributes.getLocalName(i);
/* 61 */       if ("".equals(name)) {
/* 62 */         name = attributes.getQName(i);
/*    */       }
/* 64 */       String value = attributes.getValue(i);
/* 65 */       if ((!this.excludes.containsKey(name)) && 
/* 66 */         (!this.digester.isFakeAttribute(this.digester.peek(), name)) && 
/* 67 */         (!IntrospectionUtils.setProperty(this.digester.peek(), name, value)) && 
/* 68 */         (this.digester.getRulesValidation())) {
/* 69 */         this.digester.getLogger().warn("[SetAllPropertiesRule]{" + this.digester.getMatch() + "} Setting property '" + name + "' to '" + value + "' did not find a matching property.");
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\SetAllPropertiesRule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */