/*     */ package org.apache.catalina.startup;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.tomcat.util.scan.JarFactory;
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
/*     */ public class WebappServiceLoader<T>
/*     */ {
/*     */   private static final String LIB = "/WEB-INF/lib/";
/*     */   private static final String SERVICES = "META-INF/services/";
/*     */   private final Context context;
/*     */   private final ServletContext servletContext;
/*     */   private final Pattern containerSciFilterPattern;
/*     */   
/*     */   public WebappServiceLoader(Context context)
/*     */   {
/*  74 */     this.context = context;
/*  75 */     this.servletContext = context.getServletContext();
/*  76 */     String containerSciFilter = context.getContainerSciFilter();
/*  77 */     if ((containerSciFilter != null) && (containerSciFilter.length() > 0)) {
/*  78 */       this.containerSciFilterPattern = Pattern.compile(containerSciFilter);
/*     */     } else {
/*  80 */       this.containerSciFilterPattern = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<T> load(Class<T> serviceType)
/*     */     throws IOException
/*     */   {
/*  92 */     String configFile = "META-INF/services/" + serviceType.getName();
/*     */     
/*  94 */     LinkedHashSet<String> applicationServicesFound = new LinkedHashSet();
/*  95 */     LinkedHashSet<String> containerServicesFound = new LinkedHashSet();
/*     */     
/*  97 */     ClassLoader loader = this.servletContext.getClassLoader();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 103 */     List<String> orderedLibs = (List)this.servletContext.getAttribute("javax.servlet.context.orderedLibs");
/* 104 */     if (orderedLibs != null)
/*     */     {
/* 106 */       for (String lib : orderedLibs) {
/* 107 */         URL jarUrl = this.servletContext.getResource("/WEB-INF/lib/" + lib);
/* 108 */         if (jarUrl != null)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 113 */           String base = jarUrl.toExternalForm();
/*     */           URL url;
/* 115 */           URL url; if (base.endsWith("/")) {
/* 116 */             url = new URL(base + configFile);
/*     */           } else {
/* 118 */             url = JarFactory.getJarEntryURL(jarUrl, configFile);
/*     */           }
/*     */           try {
/* 121 */             parseConfigFile(applicationServicesFound, url);
/*     */           }
/*     */           catch (FileNotFoundException localFileNotFoundException) {}
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 128 */       loader = this.context.getParentClassLoader();
/*     */     }
/*     */     Object resources;
/*     */     Object resources;
/* 132 */     if (loader == null) {
/* 133 */       resources = ClassLoader.getSystemResources(configFile);
/*     */     } else {
/* 135 */       resources = loader.getResources(configFile);
/*     */     }
/* 137 */     while (((Enumeration)resources).hasMoreElements()) {
/* 138 */       parseConfigFile(containerServicesFound, (URL)((Enumeration)resources).nextElement());
/*     */     }
/*     */     
/*     */ 
/* 142 */     if (this.containerSciFilterPattern != null) {
/* 143 */       Iterator<String> iter = containerServicesFound.iterator();
/* 144 */       while (iter.hasNext()) {
/* 145 */         if (this.containerSciFilterPattern.matcher((CharSequence)iter.next()).find()) {
/* 146 */           iter.remove();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 153 */     containerServicesFound.addAll(applicationServicesFound);
/*     */     
/*     */ 
/* 156 */     if (containerServicesFound.isEmpty()) {
/* 157 */       return Collections.emptyList();
/*     */     }
/* 159 */     return loadServices(serviceType, containerServicesFound);
/*     */   }
/*     */   
/*     */   void parseConfigFile(LinkedHashSet<String> servicesFound, URL url) throws IOException
/*     */   {
/* 164 */     InputStream is = url.openStream();Throwable localThrowable9 = null;
/* 165 */     try { InputStreamReader in = new InputStreamReader(is, StandardCharsets.UTF_8);Throwable localThrowable10 = null;
/* 166 */       try { BufferedReader reader = new BufferedReader(in);Throwable localThrowable11 = null;
/*     */         try { String line;
/* 168 */           while ((line = reader.readLine()) != null) {
/* 169 */             int i = line.indexOf('#');
/* 170 */             if (i >= 0) {
/* 171 */               line = line.substring(0, i);
/*     */             }
/* 173 */             line = line.trim();
/* 174 */             if (line.length() != 0)
/*     */             {
/*     */ 
/* 177 */               servicesFound.add(line);
/*     */             }
/*     */           }
/*     */         }
/*     */         catch (Throwable localThrowable1)
/*     */         {
/* 164 */           localThrowable11 = localThrowable1;throw localThrowable1; } finally {} } catch (Throwable localThrowable4) { localThrowable10 = localThrowable4;throw localThrowable4; } finally {} } catch (Throwable localThrowable7) { localThrowable9 = localThrowable7;throw localThrowable7;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 179 */       if (is != null) if (localThrowable9 != null) try { is.close(); } catch (Throwable localThrowable8) { localThrowable9.addSuppressed(localThrowable8); } else is.close();
/*     */     }
/*     */   }
/*     */   
/*     */   List<T> loadServices(Class<T> serviceType, LinkedHashSet<String> servicesFound) throws IOException {
/* 184 */     ClassLoader loader = this.servletContext.getClassLoader();
/* 185 */     List<T> services = new ArrayList(servicesFound.size());
/* 186 */     for (String serviceClass : servicesFound) {
/*     */       try {
/* 188 */         Class<?> clazz = Class.forName(serviceClass, true, loader);
/* 189 */         services.add(serviceType.cast(clazz.getConstructor(new Class[0]).newInstance(new Object[0])));
/*     */       } catch (ReflectiveOperationException|ClassCastException e) {
/* 191 */         throw new IOException(e);
/*     */       }
/*     */     }
/* 194 */     return Collections.unmodifiableList(services);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\WebappServiceLoader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */