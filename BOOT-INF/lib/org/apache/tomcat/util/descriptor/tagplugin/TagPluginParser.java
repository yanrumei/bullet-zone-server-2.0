/*    */ package org.apache.tomcat.util.descriptor.tagplugin;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.URL;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import javax.servlet.ServletContext;
/*    */ import org.apache.juli.logging.Log;
/*    */ import org.apache.juli.logging.LogFactory;
/*    */ import org.apache.tomcat.util.descriptor.DigesterFactory;
/*    */ import org.apache.tomcat.util.descriptor.XmlErrorHandler;
/*    */ import org.apache.tomcat.util.digester.Digester;
/*    */ import org.apache.tomcat.util.digester.RuleSetBase;
/*    */ import org.xml.sax.InputSource;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXParseException;
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
/*    */ public class TagPluginParser
/*    */ {
/* 41 */   private static final Log log = LogFactory.getLog(TagPluginParser.class);
/*    */   private static final String PREFIX = "tag-plugins/tag-plugin";
/*    */   private final Digester digester;
/* 44 */   private final Map<String, String> plugins = new HashMap();
/*    */   
/*    */   public TagPluginParser(ServletContext context, boolean blockExternal) {
/* 47 */     this.digester = DigesterFactory.newDigester(false, false, new TagPluginRuleSet(null), blockExternal);
/*    */     
/* 49 */     this.digester.setClassLoader(context.getClassLoader());
/*    */   }
/*    */   
/*    */   public void parse(URL url) throws IOException, SAXException {
/* 53 */     try { InputStream is = url.openStream();Throwable localThrowable3 = null;
/* 54 */       try { XmlErrorHandler handler = new XmlErrorHandler();
/* 55 */         this.digester.setErrorHandler(handler);
/*    */         
/* 57 */         this.digester.push(this);
/*    */         
/* 59 */         InputSource source = new InputSource(url.toExternalForm());
/* 60 */         source.setByteStream(is);
/* 61 */         this.digester.parse(source);
/* 62 */         if ((!handler.getWarnings().isEmpty()) || (!handler.getErrors().isEmpty())) {
/* 63 */           handler.logFindings(log, source.getSystemId());
/* 64 */           if (!handler.getErrors().isEmpty())
/*    */           {
/* 66 */             throw ((SAXParseException)handler.getErrors().iterator().next());
/*    */           }
/*    */         }
/*    */       }
/*    */       catch (Throwable localThrowable1)
/*    */       {
/* 53 */         localThrowable3 = localThrowable1;throw localThrowable1;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       }
/*    */       finally
/*    */       {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 69 */         if (is != null) if (localThrowable3 != null) try { is.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else is.close();
/* 70 */       } } finally { this.digester.reset();
/*    */     }
/*    */   }
/*    */   
/*    */   public void addPlugin(String tagClass, String pluginClass) {
/* 75 */     this.plugins.put(tagClass, pluginClass);
/*    */   }
/*    */   
/*    */   public Map<String, String> getPlugins() {
/* 79 */     return this.plugins;
/*    */   }
/*    */   
/*    */   private static class TagPluginRuleSet extends RuleSetBase
/*    */   {
/*    */     public void addRuleInstances(Digester digester) {
/* 85 */       digester.addCallMethod("tag-plugins/tag-plugin", "addPlugin", 2);
/* 86 */       digester.addCallParam("tag-plugins/tag-plugin/tag-class", 0);
/* 87 */       digester.addCallParam("tag-plugins/tag-plugin/plugin-class", 1);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\tagplugin\TagPluginParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */