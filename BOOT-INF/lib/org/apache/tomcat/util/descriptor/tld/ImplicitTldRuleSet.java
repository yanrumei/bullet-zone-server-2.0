/*    */ package org.apache.tomcat.util.descriptor.tld;
/*    */ 
/*    */ import org.apache.tomcat.util.digester.Digester;
/*    */ import org.apache.tomcat.util.digester.Rule;
/*    */ import org.apache.tomcat.util.digester.RuleSetBase;
/*    */ import org.apache.tomcat.util.res.StringManager;
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
/*    */ public class ImplicitTldRuleSet
/*    */   extends RuleSetBase
/*    */ {
/* 33 */   private static final StringManager sm = StringManager.getManager(ImplicitTldRuleSet.class);
/*    */   
/*    */   private static final String PREFIX = "taglib";
/*    */   
/*    */   private static final String VALIDATOR_PREFIX = "taglib/validator";
/*    */   
/*    */   private static final String TAG_PREFIX = "taglib/tag";
/*    */   private static final String TAGFILE_PREFIX = "taglib/tag-file";
/*    */   private static final String FUNCTION_PREFIX = "taglib/function";
/*    */   
/*    */   public void addRuleInstances(Digester digester)
/*    */   {
/* 45 */     digester.addCallMethod("taglib/tlibversion", "setTlibVersion", 0);
/* 46 */     digester.addCallMethod("taglib/tlib-version", "setTlibVersion", 0);
/* 47 */     digester.addCallMethod("taglib/jspversion", "setJspVersion", 0);
/* 48 */     digester.addCallMethod("taglib/jsp-version", "setJspVersion", 0);
/* 49 */     digester.addRule("taglib", new Rule()
/*    */     {
/*    */       public void begin(String namespace, String name, Attributes attributes)
/*    */       {
/* 53 */         TaglibXml taglibXml = (TaglibXml)this.digester.peek();
/* 54 */         taglibXml.setJspVersion(attributes.getValue("version"));
/*    */       }
/* 56 */     });
/* 57 */     digester.addCallMethod("taglib/shortname", "setShortName", 0);
/* 58 */     digester.addCallMethod("taglib/short-name", "setShortName", 0);
/*    */     
/*    */ 
/* 61 */     digester.addRule("taglib/uri", new ElementNotAllowedRule(null));
/* 62 */     digester.addRule("taglib/info", new ElementNotAllowedRule(null));
/* 63 */     digester.addRule("taglib/description", new ElementNotAllowedRule(null));
/* 64 */     digester.addRule("taglib/listener/listener-class", new ElementNotAllowedRule(null));
/*    */     
/* 66 */     digester.addRule("taglib/validator", new ElementNotAllowedRule(null));
/* 67 */     digester.addRule("taglib/tag", new ElementNotAllowedRule(null));
/* 68 */     digester.addRule("taglib/tag-file", new ElementNotAllowedRule(null));
/* 69 */     digester.addRule("taglib/function", new ElementNotAllowedRule(null));
/*    */   }
/*    */   
/*    */   private static class ElementNotAllowedRule
/*    */     extends Rule
/*    */   {
/*    */     public void begin(String namespace, String name, Attributes attributes) throws Exception
/*    */     {
/* 77 */       throw new IllegalArgumentException(ImplicitTldRuleSet.sm.getString("implicitTldRule.elementNotAllowed", new Object[] { name }));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\tld\ImplicitTldRuleSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */