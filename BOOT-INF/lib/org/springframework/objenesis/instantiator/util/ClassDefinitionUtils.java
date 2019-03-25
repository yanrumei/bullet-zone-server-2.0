/*     */ package org.springframework.objenesis.instantiator.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.ProtectionDomain;
/*     */ import sun.misc.Unsafe;
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
/*     */ public final class ClassDefinitionUtils
/*     */ {
/*     */   public static final byte OPS_aload_0 = 42;
/*     */   public static final byte OPS_invokespecial = -73;
/*     */   public static final byte OPS_return = -79;
/*     */   public static final byte OPS_new = -69;
/*     */   public static final byte OPS_dup = 89;
/*     */   public static final byte OPS_areturn = -80;
/*     */   public static final int CONSTANT_Utf8 = 1;
/*     */   public static final int CONSTANT_Integer = 3;
/*     */   public static final int CONSTANT_Float = 4;
/*     */   public static final int CONSTANT_Long = 5;
/*     */   public static final int CONSTANT_Double = 6;
/*     */   public static final int CONSTANT_Class = 7;
/*     */   public static final int CONSTANT_String = 8;
/*     */   public static final int CONSTANT_Fieldref = 9;
/*     */   public static final int CONSTANT_Methodref = 10;
/*     */   public static final int CONSTANT_InterfaceMethodref = 11;
/*     */   public static final int CONSTANT_NameAndType = 12;
/*     */   public static final int CONSTANT_MethodHandle = 15;
/*     */   public static final int CONSTANT_MethodType = 16;
/*     */   public static final int CONSTANT_InvokeDynamic = 18;
/*     */   public static final int ACC_PUBLIC = 1;
/*     */   public static final int ACC_FINAL = 16;
/*     */   public static final int ACC_SUPER = 32;
/*     */   public static final int ACC_INTERFACE = 512;
/*     */   public static final int ACC_ABSTRACT = 1024;
/*     */   public static final int ACC_SYNTHETIC = 4096;
/*     */   public static final int ACC_ANNOTATION = 8192;
/*     */   public static final int ACC_ENUM = 16384;
/*  70 */   public static final byte[] MAGIC = { -54, -2, -70, -66 };
/*  71 */   public static final byte[] VERSION = { 0, 0, 0, 49 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  79 */   private static final ProtectionDomain PROTECTION_DOMAIN = (ProtectionDomain)AccessController.doPrivileged(new PrivilegedAction() {
/*     */     public ProtectionDomain run() {
/*  81 */       return ClassDefinitionUtils.class.getProtectionDomain();
/*     */     }
/*  79 */   });
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
/*     */   public static <T> Class<T> defineClass(String className, byte[] b, ClassLoader loader)
/*     */     throws Exception
/*     */   {
/* 100 */     Class<T> c = UnsafeUtils.getUnsafe().defineClass(className, b, 0, b.length, loader, PROTECTION_DOMAIN);
/*     */     
/* 102 */     Class.forName(className, true, loader);
/* 103 */     return c;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] readClass(String className)
/*     */     throws IOException
/*     */   {
/* 116 */     className = classNameToResource(className);
/*     */     
/* 118 */     byte[] b = new byte['ৄ'];
/*     */     
/*     */ 
/*     */ 
/* 122 */     InputStream in = ClassDefinitionUtils.class.getClassLoader().getResourceAsStream(className);
/*     */     try {
/* 124 */       length = in.read(b);
/*     */     } finally {
/*     */       int length;
/* 127 */       in.close();
/*     */     }
/*     */     int length;
/* 130 */     if (length >= 2500) {
/* 131 */       throw new IllegalArgumentException("The class is longer that 2500 bytes which is currently unsupported");
/*     */     }
/*     */     
/* 134 */     byte[] copy = new byte[length];
/* 135 */     System.arraycopy(b, 0, copy, 0, length);
/* 136 */     return copy;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static void writeClass(String fileName, byte[] bytes)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 158	java/io/BufferedOutputStream
/*     */     //   3: dup
/*     */     //   4: new 160	java/io/FileOutputStream
/*     */     //   7: dup
/*     */     //   8: aload_0
/*     */     //   9: invokespecial 161	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
/*     */     //   12: invokespecial 164	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
/*     */     //   15: astore_2
/*     */     //   16: aload_2
/*     */     //   17: aload_1
/*     */     //   18: invokevirtual 168	java/io/BufferedOutputStream:write	([B)V
/*     */     //   21: aload_2
/*     */     //   22: invokevirtual 169	java/io/BufferedOutputStream:close	()V
/*     */     //   25: goto +10 -> 35
/*     */     //   28: astore_3
/*     */     //   29: aload_2
/*     */     //   30: invokevirtual 169	java/io/BufferedOutputStream:close	()V
/*     */     //   33: aload_3
/*     */     //   34: athrow
/*     */     //   35: return
/*     */     // Line number table:
/*     */     //   Java source line #147	-> byte code offset #0
/*     */     //   Java source line #149	-> byte code offset #16
/*     */     //   Java source line #152	-> byte code offset #21
/*     */     //   Java source line #153	-> byte code offset #25
/*     */     //   Java source line #152	-> byte code offset #28
/*     */     //   Java source line #154	-> byte code offset #35
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	36	0	fileName	String
/*     */     //   0	36	1	bytes	byte[]
/*     */     //   15	15	2	out	java.io.BufferedOutputStream
/*     */     //   28	6	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   16	21	28	finally
/*     */   }
/*     */   
/*     */   public static String classNameToInternalClassName(String className)
/*     */   {
/* 164 */     return className.replace('.', '/');
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String classNameToResource(String className)
/*     */   {
/* 175 */     return classNameToInternalClassName(className) + ".class";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> Class<T> getExistingClass(ClassLoader classLoader, String className)
/*     */   {
/*     */     try
/*     */     {
/* 189 */       return Class.forName(className, true, classLoader);
/*     */     }
/*     */     catch (ClassNotFoundException e) {}
/* 192 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\objenesis\instantiato\\util\ClassDefinitionUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */