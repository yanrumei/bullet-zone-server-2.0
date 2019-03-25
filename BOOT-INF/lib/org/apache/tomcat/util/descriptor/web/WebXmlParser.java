/*     */ package org.apache.tomcat.util.descriptor.web;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.List;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.descriptor.DigesterFactory;
/*     */ import org.apache.tomcat.util.descriptor.InputSourceUtil;
/*     */ import org.apache.tomcat.util.descriptor.XmlErrorHandler;
/*     */ import org.apache.tomcat.util.digester.Digester;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WebXmlParser
/*     */ {
/*  34 */   private static final Log log = LogFactory.getLog(WebXmlParser.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  40 */   private static final StringManager sm = StringManager.getManager(Constants.PACKAGE_NAME);
/*     */   
/*     */ 
/*     */ 
/*     */   private final Digester webDigester;
/*     */   
/*     */ 
/*     */   private final WebRuleSet webRuleSet;
/*     */   
/*     */ 
/*     */   private final Digester webFragmentDigester;
/*     */   
/*     */ 
/*     */   private final WebRuleSet webFragmentRuleSet;
/*     */   
/*     */ 
/*     */ 
/*     */   public WebXmlParser(boolean namespaceAware, boolean validation, boolean blockExternal)
/*     */   {
/*  59 */     this.webRuleSet = new WebRuleSet(false);
/*  60 */     this.webDigester = DigesterFactory.newDigester(validation, namespaceAware, this.webRuleSet, blockExternal);
/*     */     
/*  62 */     this.webDigester.getParser();
/*     */     
/*  64 */     this.webFragmentRuleSet = new WebRuleSet(true);
/*  65 */     this.webFragmentDigester = DigesterFactory.newDigester(validation, namespaceAware, this.webFragmentRuleSet, blockExternal);
/*     */     
/*  67 */     this.webFragmentDigester.getParser();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean parseWebXml(URL url, WebXml dest, boolean fragment)
/*     */     throws IOException
/*     */   {
/*  80 */     if (url == null) {
/*  81 */       return true;
/*     */     }
/*  83 */     InputSource source = new InputSource(url.toExternalForm());
/*  84 */     source.setByteStream(url.openStream());
/*  85 */     return parseWebXml(source, dest, fragment);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean parseWebXml(InputSource source, WebXml dest, boolean fragment)
/*     */   {
/*  92 */     boolean ok = true;
/*     */     
/*  94 */     if (source == null) {
/*  95 */       return ok;
/*     */     }
/*     */     
/*  98 */     XmlErrorHandler handler = new XmlErrorHandler();
/*     */     WebRuleSet ruleSet;
/*     */     Digester digester;
/*     */     WebRuleSet ruleSet;
/* 102 */     if (fragment) {
/* 103 */       Digester digester = this.webFragmentDigester;
/* 104 */       ruleSet = this.webFragmentRuleSet;
/*     */     } else {
/* 106 */       digester = this.webDigester;
/* 107 */       ruleSet = this.webRuleSet;
/*     */     }
/*     */     
/* 110 */     digester.push(dest);
/* 111 */     digester.setErrorHandler(handler);
/*     */     
/* 113 */     if (log.isDebugEnabled()) {
/* 114 */       log.debug(sm.getString("webXmlParser.applicationStart", new Object[] {source
/* 115 */         .getSystemId() }));
/*     */     }
/*     */     try
/*     */     {
/* 119 */       digester.parse(source);
/*     */       
/* 121 */       if ((handler.getWarnings().size() > 0) || 
/* 122 */         (handler.getErrors().size() > 0)) {
/* 123 */         ok = false;
/* 124 */         handler.logFindings(log, source.getSystemId());
/*     */       }
/*     */     } catch (SAXParseException e) {
/* 127 */       log.error(sm.getString("webXmlParser.applicationParse", new Object[] {source
/* 128 */         .getSystemId() }), e);
/* 129 */       log.error(sm.getString("webXmlParser.applicationPosition", new Object[] {"" + e
/* 130 */         .getLineNumber(), "" + e
/* 131 */         .getColumnNumber() }));
/* 132 */       ok = false;
/*     */     } catch (Exception e) {
/* 134 */       log.error(sm.getString("webXmlParser.applicationParse", new Object[] {source
/* 135 */         .getSystemId() }), e);
/* 136 */       ok = false;
/*     */     } finally {
/* 138 */       InputSourceUtil.close(source);
/* 139 */       digester.reset();
/* 140 */       ruleSet.recycle();
/*     */     }
/*     */     
/* 143 */     return ok;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setClassLoader(ClassLoader classLoader)
/*     */   {
/* 152 */     this.webDigester.setClassLoader(classLoader);
/* 153 */     this.webFragmentDigester.setClassLoader(classLoader);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\WebXmlParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */