/*     */ package org.apache.tomcat.util.descriptor;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.digester.Digester;
/*     */ import org.apache.tomcat.util.digester.RuleSet;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ import org.xml.sax.ext.EntityResolver2;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DigesterFactory
/*     */ {
/*  39 */   private static final Log log = LogFactory.getLog(DigesterFactory.class);
/*     */   
/*  41 */   private static final StringManager sm = StringManager.getManager(Constants.PACKAGE_NAME);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  47 */   private static final Class<ServletContext> CLASS_SERVLET_CONTEXT = ServletContext.class;
/*  48 */   static { Class<?> jspContext = null;
/*     */     try {
/*  50 */       jspContext = Class.forName("javax.servlet.jsp.JspContext");
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {}
/*     */     
/*  54 */     CLASS_JSP_CONTEXT = jspContext;
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
/*  71 */     Map<String, String> publicIds = new HashMap();
/*  72 */     Map<String, String> systemIds = new HashMap();
/*     */     
/*     */ 
/*  75 */     add(publicIds, "-//W3C//DTD XMLSCHEMA 200102//EN", locationFor("XMLSchema.dtd"));
/*  76 */     add(publicIds, "datatypes", locationFor("datatypes.dtd"));
/*  77 */     add(systemIds, "http://www.w3.org/2001/xml.xsd", locationFor("xml.xsd"));
/*     */     
/*     */ 
/*  80 */     add(publicIds, "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN", locationFor("web-app_2_2.dtd"));
/*  81 */     add(publicIds, "-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.1//EN", locationFor("web-jsptaglibrary_1_1.dtd"));
/*     */     
/*     */ 
/*  84 */     add(publicIds, "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN", locationFor("web-app_2_3.dtd"));
/*  85 */     add(publicIds, "-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.2//EN", locationFor("web-jsptaglibrary_1_2.dtd"));
/*     */     
/*     */ 
/*  88 */     add(systemIds, "http://www.ibm.com/webservices/xsd/j2ee_web_services_1_1.xsd", 
/*  89 */       locationFor("j2ee_web_services_1_1.xsd"));
/*  90 */     add(systemIds, "http://www.ibm.com/webservices/xsd/j2ee_web_services_client_1_1.xsd", 
/*  91 */       locationFor("j2ee_web_services_client_1_1.xsd"));
/*  92 */     add(systemIds, "http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd", locationFor("web-app_2_4.xsd"));
/*  93 */     add(systemIds, "http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_0.xsd", locationFor("web-jsptaglibrary_2_0.xsd"));
/*  94 */     addSelf(systemIds, "j2ee_1_4.xsd");
/*  95 */     addSelf(systemIds, "jsp_2_0.xsd");
/*     */     
/*     */ 
/*  98 */     add(systemIds, "http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd", locationFor("web-app_2_5.xsd"));
/*  99 */     add(systemIds, "http://java.sun.com/xml/ns/javaee/web-jsptaglibrary_2_1.xsd", locationFor("web-jsptaglibrary_2_1.xsd"));
/* 100 */     addSelf(systemIds, "javaee_5.xsd");
/* 101 */     addSelf(systemIds, "jsp_2_1.xsd");
/* 102 */     addSelf(systemIds, "javaee_web_services_1_2.xsd");
/* 103 */     addSelf(systemIds, "javaee_web_services_client_1_2.xsd");
/*     */     
/*     */ 
/* 106 */     add(systemIds, "http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd", locationFor("web-app_3_0.xsd"));
/* 107 */     add(systemIds, "http://java.sun.com/xml/ns/javaee/web-fragment_3_0.xsd", locationFor("web-fragment_3_0.xsd"));
/* 108 */     addSelf(systemIds, "web-common_3_0.xsd");
/* 109 */     addSelf(systemIds, "javaee_6.xsd");
/* 110 */     addSelf(systemIds, "jsp_2_2.xsd");
/* 111 */     addSelf(systemIds, "javaee_web_services_1_3.xsd");
/* 112 */     addSelf(systemIds, "javaee_web_services_client_1_3.xsd");
/*     */     
/*     */ 
/* 115 */     add(systemIds, "http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd", locationFor("web-app_3_1.xsd"));
/* 116 */     add(systemIds, "http://xmlns.jcp.org/xml/ns/javaee/web-fragment_3_1.xsd", locationFor("web-fragment_3_1.xsd"));
/* 117 */     addSelf(systemIds, "web-common_3_1.xsd");
/* 118 */     addSelf(systemIds, "javaee_7.xsd");
/* 119 */     addSelf(systemIds, "jsp_2_3.xsd");
/* 120 */     addSelf(systemIds, "javaee_web_services_1_4.xsd");
/* 121 */     addSelf(systemIds, "javaee_web_services_client_1_4.xsd");
/*     */     
/* 123 */     SERVLET_API_PUBLIC_IDS = Collections.unmodifiableMap(publicIds);
/* 124 */     SERVLET_API_SYSTEM_IDS = Collections.unmodifiableMap(systemIds);
/*     */   }
/*     */   
/*     */   private static void addSelf(Map<String, String> ids, String id) {
/* 128 */     String location = locationFor(id);
/* 129 */     if (location != null) {
/* 130 */       ids.put(id, location);
/* 131 */       ids.put(location, location);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void add(Map<String, String> ids, String id, String location) {
/* 136 */     if (location != null) {
/* 137 */       ids.put(id, location);
/*     */     }
/*     */   }
/*     */   
/*     */   private static String locationFor(String name) {
/* 142 */     URL location = CLASS_SERVLET_CONTEXT.getResource("resources/" + name);
/* 143 */     if ((location == null) && (CLASS_JSP_CONTEXT != null)) {
/* 144 */       location = CLASS_JSP_CONTEXT.getResource("resources/" + name);
/*     */     }
/* 146 */     if (location == null) {
/* 147 */       log.warn(sm.getString("digesterFactory.missingSchema", new Object[] { name }));
/* 148 */       return null;
/*     */     }
/* 150 */     return location.toExternalForm();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final Class<?> CLASS_JSP_CONTEXT;
/*     */   
/*     */ 
/*     */   public static final Map<String, String> SERVLET_API_PUBLIC_IDS;
/*     */   
/*     */ 
/*     */   public static final Map<String, String> SERVLET_API_SYSTEM_IDS;
/*     */   
/*     */ 
/*     */   public static Digester newDigester(boolean xmlValidation, boolean xmlNamespaceAware, RuleSet rule, boolean blockExternal)
/*     */   {
/* 166 */     Digester digester = new Digester();
/* 167 */     digester.setNamespaceAware(xmlNamespaceAware);
/* 168 */     digester.setValidating(xmlValidation);
/* 169 */     digester.setUseContextClassLoader(true);
/* 170 */     EntityResolver2 resolver = new LocalResolver(SERVLET_API_PUBLIC_IDS, SERVLET_API_SYSTEM_IDS, blockExternal);
/*     */     
/* 172 */     digester.setEntityResolver(resolver);
/* 173 */     if (rule != null) {
/* 174 */       digester.addRuleSet(rule);
/*     */     }
/*     */     
/* 177 */     return digester;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\DigesterFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */