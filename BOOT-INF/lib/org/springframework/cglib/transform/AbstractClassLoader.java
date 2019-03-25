/*     */ package org.springframework.cglib.transform;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.ProtectionDomain;
/*     */ import org.springframework.asm.Attribute;
/*     */ import org.springframework.asm.ClassReader;
/*     */ import org.springframework.cglib.core.ClassGenerator;
/*     */ import org.springframework.cglib.core.CodeGenerationException;
/*     */ import org.springframework.cglib.core.DebuggingClassWriter;
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
/*     */ public abstract class AbstractClassLoader
/*     */   extends ClassLoader
/*     */ {
/*     */   private ClassFilter filter;
/*     */   private ClassLoader classPath;
/*  35 */   private static ProtectionDomain DOMAIN = (ProtectionDomain)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public Object run()
/*     */     {
/*  38 */       return AbstractClassLoader.class.getProtectionDomain();
/*     */     }
/*  35 */   });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractClassLoader(ClassLoader parent, ClassLoader classPath, ClassFilter filter)
/*     */   {
/*  44 */     super(parent);
/*  45 */     this.filter = filter;
/*  46 */     this.classPath = classPath;
/*     */   }
/*     */   
/*     */   public Class loadClass(String name) throws ClassNotFoundException
/*     */   {
/*  51 */     Class loaded = findLoadedClass(name);
/*     */     
/*  53 */     if ((loaded != null) && 
/*  54 */       (loaded.getClassLoader() == this)) {
/*  55 */       return loaded;
/*     */     }
/*     */     
/*     */ 
/*  59 */     if (!this.filter.accept(name)) {
/*  60 */       return super.loadClass(name);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*  65 */       InputStream is = this.classPath.getResourceAsStream(name
/*  66 */         .replace('.', '/') + ".class");
/*     */       
/*     */ 
/*  69 */       if (is == null)
/*     */       {
/*  71 */         throw new ClassNotFoundException(name);
/*     */       }
/*     */       
/*     */       try
/*     */       {
/*  76 */         r = new ClassReader(is);
/*     */       }
/*     */       finally {
/*     */         ClassReader r;
/*  80 */         is.close();
/*     */       }
/*     */     } catch (IOException e) {
/*     */       ClassReader r;
/*  84 */       throw new ClassNotFoundException(name + ":" + e.getMessage());
/*     */     }
/*     */     try {
/*     */       ClassReader r;
/*  88 */       DebuggingClassWriter w = new DebuggingClassWriter(2);
/*     */       
/*  90 */       getGenerator(r).generateClass(w);
/*  91 */       byte[] b = w.toByteArray();
/*  92 */       Class c = super.defineClass(name, b, 0, b.length, DOMAIN);
/*  93 */       postProcess(c);
/*  94 */       return c;
/*     */     } catch (RuntimeException e) {
/*  96 */       throw e;
/*     */     } catch (Error e) {
/*  98 */       throw e;
/*     */     } catch (Exception e) {
/* 100 */       throw new CodeGenerationException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   protected ClassGenerator getGenerator(ClassReader r) {
/* 105 */     return new ClassReaderGenerator(r, attributes(), getFlags());
/*     */   }
/*     */   
/*     */   protected int getFlags() {
/* 109 */     return 0;
/*     */   }
/*     */   
/*     */   protected Attribute[] attributes() {
/* 113 */     return null;
/*     */   }
/*     */   
/*     */   protected void postProcess(Class c) {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\transform\AbstractClassLoader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */