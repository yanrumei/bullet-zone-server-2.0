/*     */ package org.apache.catalina.startup;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
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
/*     */ public final class Tool
/*     */ {
/*  76 */   private static final Log log = LogFactory.getLog(Tool.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  84 */   private static boolean ant = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  91 */   private static final String catalinaHome = System.getProperty("catalina.home");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  97 */   private static boolean common = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 103 */   private static boolean server = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 109 */   private static boolean shared = false;
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
/*     */   public static void main(String[] args)
/*     */   {
/* 124 */     if (catalinaHome == null) {
/* 125 */       log.error("Must set 'catalina.home' system property");
/* 126 */       System.exit(1);
/*     */     }
/*     */     
/*     */ 
/* 130 */     int index = 0;
/*     */     for (;;) {
/* 132 */       if (index == args.length) {
/* 133 */         usage();
/* 134 */         System.exit(1);
/*     */       }
/* 136 */       if ("-ant".equals(args[index])) {
/* 137 */         ant = true;
/* 138 */       } else if ("-common".equals(args[index])) {
/* 139 */         common = true;
/* 140 */       } else if ("-server".equals(args[index])) {
/* 141 */         server = true;
/* 142 */       } else { if (!"-shared".equals(args[index])) break;
/* 143 */         shared = true;
/*     */       }
/*     */       
/* 146 */       index++;
/*     */     }
/* 148 */     if (index > args.length) {
/* 149 */       usage();
/* 150 */       System.exit(1);
/*     */     }
/*     */     
/*     */ 
/* 154 */     if (ant) {
/* 155 */       System.setProperty("ant.home", catalinaHome);
/*     */     }
/*     */     
/* 158 */     ClassLoader classLoader = null;
/*     */     try {
/* 160 */       ArrayList<File> packed = new ArrayList();
/* 161 */       ArrayList<File> unpacked = new ArrayList();
/* 162 */       unpacked.add(new File(catalinaHome, "classes"));
/* 163 */       packed.add(new File(catalinaHome, "lib"));
/* 164 */       if (common) {
/* 165 */         unpacked.add(new File(catalinaHome, "common" + File.separator + "classes"));
/*     */         
/* 167 */         packed.add(new File(catalinaHome, "common" + File.separator + "lib"));
/*     */       }
/*     */       
/* 170 */       if (server) {
/* 171 */         unpacked.add(new File(catalinaHome, "server" + File.separator + "classes"));
/*     */         
/* 173 */         packed.add(new File(catalinaHome, "server" + File.separator + "lib"));
/*     */       }
/*     */       
/* 176 */       if (shared) {
/* 177 */         unpacked.add(new File(catalinaHome, "shared" + File.separator + "classes"));
/*     */         
/* 179 */         packed.add(new File(catalinaHome, "shared" + File.separator + "lib"));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 184 */       classLoader = ClassLoaderFactory.createClassLoader((File[])unpacked.toArray(new File[0]), 
/* 185 */         (File[])packed.toArray(new File[0]), null);
/*     */     }
/*     */     catch (Throwable t) {
/* 188 */       ExceptionUtils.handleThrowable(t);
/* 189 */       log.error("Class loader creation threw exception", t);
/* 190 */       System.exit(1);
/*     */     }
/* 192 */     Thread.currentThread().setContextClassLoader(classLoader);
/*     */     
/*     */ 
/* 195 */     Class<?> clazz = null;
/* 196 */     String className = args[(index++)];
/*     */     try {
/* 198 */       if (log.isDebugEnabled())
/* 199 */         log.debug("Loading application class " + className);
/* 200 */       clazz = classLoader.loadClass(className);
/*     */     } catch (Throwable t) {
/* 202 */       ExceptionUtils.handleThrowable(t);
/* 203 */       log.error("Exception creating instance of " + className, t);
/* 204 */       System.exit(1);
/*     */     }
/*     */     
/* 207 */     Method method = null;
/* 208 */     String[] params = new String[args.length - index];
/* 209 */     System.arraycopy(args, index, params, 0, params.length);
/*     */     try {
/* 211 */       if (log.isDebugEnabled())
/* 212 */         log.debug("Identifying main() method");
/* 213 */       String methodName = "main";
/* 214 */       Class<?>[] paramTypes = new Class[1];
/* 215 */       paramTypes[0] = params.getClass();
/* 216 */       method = clazz.getMethod(methodName, paramTypes);
/*     */     } catch (Throwable t) {
/* 218 */       ExceptionUtils.handleThrowable(t);
/* 219 */       log.error("Exception locating main() method", t);
/* 220 */       System.exit(1);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 225 */       if (log.isDebugEnabled())
/* 226 */         log.debug("Calling main() method");
/* 227 */       Object[] paramValues = new Object[1];
/* 228 */       paramValues[0] = params;
/* 229 */       method.invoke(null, paramValues);
/*     */     } catch (Throwable t) {
/* 231 */       t = ExceptionUtils.unwrapInvocationTargetException(t);
/* 232 */       ExceptionUtils.handleThrowable(t);
/* 233 */       log.error("Exception calling main() method", t);
/* 234 */       System.exit(1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void usage()
/*     */   {
/* 245 */     log.info("Usage:  java org.apache.catalina.startup.Tool [<options>] <class> [<arguments>]");
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\Tool.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */