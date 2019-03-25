/*     */ package org.springframework.boot.context.embedded.tomcat;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.List;
/*     */ import javax.naming.directory.DirContext;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.WebResourceRoot;
/*     */ import org.apache.catalina.WebResourceRoot.ResourceSetType;
/*     */ import org.apache.catalina.core.StandardContext;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ abstract class TomcatResources
/*     */ {
/*     */   private final Context context;
/*     */   
/*     */   TomcatResources(Context context)
/*     */   {
/*  45 */     this.context = context;
/*     */   }
/*     */   
/*     */   void addResourceJars(List<URL> resourceJarUrls) {
/*  49 */     for (URL url : resourceJarUrls) {
/*  50 */       String file = url.getFile();
/*  51 */       if ((file.endsWith(".jar")) || (file.endsWith(".jar!/"))) {
/*  52 */         String jar = url.toString();
/*  53 */         if (!jar.startsWith("jar:"))
/*     */         {
/*  55 */           jar = "jar:" + jar + "!/";
/*     */         }
/*  57 */         addJar(jar);
/*     */       }
/*     */       else {
/*  60 */         addDir(file, url);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected final Context getContext() {
/*  66 */     return this.context;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void addJar(String paramString);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void addDir(String paramString, URL paramURL);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TomcatResources get(Context context)
/*     */   {
/*  88 */     if (ClassUtils.isPresent("org.apache.catalina.deploy.ErrorPage", null)) {
/*  89 */       return new Tomcat7Resources(context);
/*     */     }
/*  91 */     return new Tomcat8Resources(context);
/*     */   }
/*     */   
/*     */ 
/*     */   private static class Tomcat7Resources
/*     */     extends TomcatResources
/*     */   {
/*     */     private final Method addResourceJarUrlMethod;
/*     */     
/*     */     Tomcat7Resources(Context context)
/*     */     {
/* 102 */       super();
/* 103 */       this.addResourceJarUrlMethod = ReflectionUtils.findMethod(context.getClass(), "addResourceJarUrl", new Class[] { URL.class });
/*     */     }
/*     */     
/*     */ 
/*     */     protected void addJar(String jar)
/*     */     {
/* 109 */       URL url = getJarUrl(jar);
/* 110 */       if (url != null) {
/*     */         try {
/* 112 */           this.addResourceJarUrlMethod.invoke(getContext(), new Object[] { url });
/*     */         }
/*     */         catch (Exception ex) {
/* 115 */           throw new IllegalStateException(ex);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private URL getJarUrl(String jar) {
/*     */       try {
/* 122 */         return new URL(jar);
/*     */       }
/*     */       catch (MalformedURLException ex) {}
/*     */       
/* 126 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */     protected void addDir(String dir, URL url)
/*     */     {
/* 132 */       if ((getContext() instanceof StandardContext)) {
/*     */         try
/*     */         {
/* 135 */           Class<?> fileDirContextClass = Class.forName("org.apache.naming.resources.FileDirContext");
/*     */           
/* 137 */           Method setDocBaseMethod = ReflectionUtils.findMethod(fileDirContextClass, "setDocBase", new Class[] { String.class });
/* 138 */           Object fileDirContext = fileDirContextClass.newInstance();
/* 139 */           setDocBaseMethod.invoke(fileDirContext, new Object[] { dir });
/* 140 */           Method addResourcesDirContextMethod = ReflectionUtils.findMethod(StandardContext.class, "addResourcesDirContext", new Class[] { DirContext.class });
/*     */           
/*     */ 
/* 143 */           addResourcesDirContextMethod.invoke(getContext(), new Object[] { fileDirContext });
/*     */         }
/*     */         catch (Exception ex) {
/* 146 */           throw new IllegalStateException("Tomcat 7 reflection failed", ex);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static class Tomcat8Resources
/*     */     extends TomcatResources
/*     */   {
/*     */     Tomcat8Resources(Context context)
/*     */     {
/* 159 */       super();
/*     */     }
/*     */     
/*     */     protected void addJar(String jar)
/*     */     {
/* 164 */       addResourceSet(jar);
/*     */     }
/*     */     
/*     */     protected void addDir(String dir, URL url)
/*     */     {
/* 169 */       addResourceSet(url.toString());
/*     */     }
/*     */     
/*     */     private void addResourceSet(String resource) {
/*     */       try {
/* 174 */         if (isInsideNestedJar(resource))
/*     */         {
/*     */ 
/*     */ 
/* 178 */           resource = resource.substring(0, resource.length() - 2);
/*     */         }
/* 180 */         URL url = new URL(resource);
/* 181 */         String path = "/META-INF/resources";
/* 182 */         getContext().getResources().createWebResourceSet(WebResourceRoot.ResourceSetType.RESOURCE_JAR, "/", url, path);
/*     */       }
/*     */       catch (Exception localException) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private boolean isInsideNestedJar(String dir)
/*     */     {
/* 191 */       return dir.indexOf("!/") < dir.lastIndexOf("!/");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\tomcat\TomcatResources.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */