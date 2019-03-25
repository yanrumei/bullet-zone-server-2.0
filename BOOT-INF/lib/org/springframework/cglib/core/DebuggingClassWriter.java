/*    */ package org.springframework.cglib.core;
/*    */ 
/*    */ import java.io.BufferedOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.io.OutputStreamWriter;
/*    */ import java.io.PrintStream;
/*    */ import java.io.PrintWriter;
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import org.springframework.asm.ClassReader;
/*    */ import org.springframework.asm.ClassVisitor;
/*    */ import org.springframework.asm.ClassWriter;
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
/*    */ public class DebuggingClassWriter
/*    */   extends ClassVisitor
/*    */ {
/*    */   public static final String DEBUG_LOCATION_PROPERTY = "cglib.debugLocation";
/* 37 */   private static String debugLocation = System.getProperty("cglib.debugLocation");
/* 38 */   static { if (debugLocation != null) {
/* 39 */       System.err.println("CGLIB debugging enabled, writing to '" + debugLocation + "'");
/*    */       try {
/* 41 */         Class clazz = Class.forName("org.springframework.asm.util.TraceClassVisitor");
/* 42 */         traceCtor = clazz.getConstructor(new Class[] { ClassVisitor.class, PrintWriter.class });
/*    */       }
/*    */       catch (Throwable localThrowable) {}
/*    */     }
/*    */   }
/*    */   
/*    */   public DebuggingClassWriter(int flags) {
/* 49 */     super(393216, new ClassWriter(flags));
/*    */   }
/*    */   
/*    */ 
/*    */   private static Constructor traceCtor;
/*    */   private String className;
/*    */   private String superName;
/*    */   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
/*    */   {
/* 58 */     this.className = name.replace('/', '.');
/* 59 */     this.superName = superName.replace('/', '.');
/* 60 */     super.visit(version, access, name, signature, superName, interfaces);
/*    */   }
/*    */   
/*    */   public String getClassName() {
/* 64 */     return this.className;
/*    */   }
/*    */   
/*    */   public String getSuperName() {
/* 68 */     return this.superName;
/*    */   }
/*    */   
/*    */   public byte[] toByteArray()
/*    */   {
/* 73 */     (byte[])AccessController.doPrivileged(new PrivilegedAction()
/*    */     {
/*    */ 
/*    */       public Object run()
/*    */       {
/* 78 */         byte[] b = ((ClassWriter)DebuggingClassWriter.access$001(DebuggingClassWriter.this)).toByteArray();
/* 79 */         if (DebuggingClassWriter.debugLocation != null) {
/* 80 */           String dirs = DebuggingClassWriter.this.className.replace('.', File.separatorChar);
/*    */           try {
/* 82 */             new File(DebuggingClassWriter.debugLocation + File.separatorChar + dirs).getParentFile().mkdirs();
/*    */             
/* 84 */             File file = new File(new File(DebuggingClassWriter.debugLocation), dirs + ".class");
/* 85 */             OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
/*    */             try {
/* 87 */               out.write(b);
/*    */             } finally {
/* 89 */               out.close();
/*    */             }
/*    */             
/* 92 */             if (DebuggingClassWriter.traceCtor != null) {
/* 93 */               file = new File(new File(DebuggingClassWriter.debugLocation), dirs + ".asm");
/* 94 */               out = new BufferedOutputStream(new FileOutputStream(file));
/*    */               try {
/* 96 */                 ClassReader cr = new ClassReader(b);
/* 97 */                 PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
/* 98 */                 ClassVisitor tcv = (ClassVisitor)DebuggingClassWriter.traceCtor.newInstance(new Object[] { null, pw });
/* 99 */                 cr.accept(tcv, 0);
/* :0 */                 pw.flush();
/*    */               } finally {
/* :2 */                 out.close();
/*    */               }
/*    */             }
/*    */           } catch (Exception e) {
/* :6 */             throw new CodeGenerationException(e);
/*    */           }
/*    */         }
/* :9 */         return b;
/*    */       }
/*    */     });
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\core\DebuggingClassWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */