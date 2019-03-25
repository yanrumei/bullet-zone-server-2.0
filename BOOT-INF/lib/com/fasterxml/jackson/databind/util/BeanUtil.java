/*     */ package com.fasterxml.jackson.databind.util;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
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
/*     */ public class BeanUtil
/*     */ {
/*     */   public static String okNameForGetter(AnnotatedMethod am, boolean stdNaming)
/*     */   {
/*  21 */     String name = am.getName();
/*  22 */     String str = okNameForIsGetter(am, name, stdNaming);
/*  23 */     if (str == null) {
/*  24 */       str = okNameForRegularGetter(am, name, stdNaming);
/*     */     }
/*  26 */     return str;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String okNameForRegularGetter(AnnotatedMethod am, String name, boolean stdNaming)
/*     */   {
/*  35 */     if (name.startsWith("get"))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  43 */       if ("getCallbacks".equals(name)) {
/*  44 */         if (isCglibGetCallbacks(am)) {
/*  45 */           return null;
/*     */         }
/*  47 */       } else if ("getMetaClass".equals(name))
/*     */       {
/*  49 */         if (isGroovyMetaClassGetter(am)) {
/*  50 */           return null;
/*     */         }
/*     */       }
/*  53 */       return stdNaming ? stdManglePropertyName(name, 3) : legacyManglePropertyName(name, 3);
/*     */     }
/*     */     
/*     */ 
/*  57 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String okNameForIsGetter(AnnotatedMethod am, String name, boolean stdNaming)
/*     */   {
/*  66 */     if (name.startsWith("is")) {
/*  67 */       Class<?> rt = am.getRawType();
/*  68 */       if ((rt == Boolean.class) || (rt == Boolean.TYPE)) {
/*  69 */         return stdNaming ? stdManglePropertyName(name, 2) : legacyManglePropertyName(name, 2);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  74 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String okNameForSetter(AnnotatedMethod am, boolean stdNaming)
/*     */   {
/*  81 */     String name = okNameForMutator(am, "set", stdNaming);
/*  82 */     if ((name != null) && ((!"metaClass".equals(name)) || (!isGroovyMetaClassSetter(am))))
/*     */     {
/*     */ 
/*  85 */       return name;
/*     */     }
/*  87 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String okNameForMutator(AnnotatedMethod am, String prefix, boolean stdNaming)
/*     */   {
/*  95 */     String name = am.getName();
/*  96 */     if (name.startsWith(prefix)) {
/*  97 */       return stdNaming ? stdManglePropertyName(name, prefix.length()) : legacyManglePropertyName(name, prefix.length());
/*     */     }
/*     */     
/*     */ 
/* 101 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static String okNameForGetter(AnnotatedMethod am)
/*     */   {
/* 112 */     return okNameForGetter(am, false);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static String okNameForRegularGetter(AnnotatedMethod am, String name) {
/* 117 */     return okNameForRegularGetter(am, name, false);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static String okNameForIsGetter(AnnotatedMethod am, String name) {
/* 122 */     return okNameForIsGetter(am, name, false);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static String okNameForSetter(AnnotatedMethod am) {
/* 127 */     return okNameForSetter(am, false);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static String okNameForMutator(AnnotatedMethod am, String prefix) {
/* 132 */     return okNameForMutator(am, prefix, false);
/*     */   }
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
/*     */   protected static boolean isCglibGetCallbacks(AnnotatedMethod am)
/*     */   {
/* 151 */     Class<?> rt = am.getRawType();
/*     */     
/* 153 */     if ((rt == null) || (!rt.isArray())) {
/* 154 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 160 */     Class<?> compType = rt.getComponentType();
/*     */     
/* 162 */     String pkgName = ClassUtil.getPackageName(compType);
/* 163 */     if ((pkgName != null) && 
/* 164 */       (pkgName.contains(".cglib")) && (
/* 165 */       (pkgName.startsWith("net.sf.cglib")) || (pkgName.startsWith("org.hibernate.repackage.cglib")) || (pkgName.startsWith("org.springframework.cglib"))))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 171 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 175 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static boolean isGroovyMetaClassSetter(AnnotatedMethod am)
/*     */   {
/* 184 */     Class<?> argType = am.getRawParameterType(0);
/* 185 */     String pkgName = ClassUtil.getPackageName(argType);
/* 186 */     if ((pkgName != null) && (pkgName.startsWith("groovy.lang"))) {
/* 187 */       return true;
/*     */     }
/* 189 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static boolean isGroovyMetaClassGetter(AnnotatedMethod am)
/*     */   {
/* 197 */     Class<?> rt = am.getRawType();
/* 198 */     if ((rt == null) || (rt.isArray())) {
/* 199 */       return false;
/*     */     }
/* 201 */     String pkgName = ClassUtil.getPackageName(rt);
/* 202 */     if ((pkgName != null) && (pkgName.startsWith("groovy.lang"))) {
/* 203 */       return true;
/*     */     }
/* 205 */     return false;
/*     */   }
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
/*     */   protected static String legacyManglePropertyName(String basename, int offset)
/*     */   {
/* 223 */     int end = basename.length();
/* 224 */     if (end == offset) {
/* 225 */       return null;
/*     */     }
/*     */     
/* 228 */     char c = basename.charAt(offset);
/* 229 */     char d = Character.toLowerCase(c);
/*     */     
/* 231 */     if (c == d) {
/* 232 */       return basename.substring(offset);
/*     */     }
/*     */     
/* 235 */     StringBuilder sb = new StringBuilder(end - offset);
/* 236 */     sb.append(d);
/* 237 */     for (int i = offset + 1; 
/* 238 */         i < end; i++) {
/* 239 */       c = basename.charAt(i);
/* 240 */       d = Character.toLowerCase(c);
/* 241 */       if (c == d) {
/* 242 */         sb.append(basename, i, end);
/* 243 */         break;
/*     */       }
/* 245 */       sb.append(d);
/*     */     }
/* 247 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static String stdManglePropertyName(String basename, int offset)
/*     */   {
/* 255 */     int end = basename.length();
/* 256 */     if (end == offset) {
/* 257 */       return null;
/*     */     }
/*     */     
/* 260 */     char c0 = basename.charAt(offset);
/* 261 */     char c1 = Character.toLowerCase(c0);
/* 262 */     if (c0 == c1) {
/* 263 */       return basename.substring(offset);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 268 */     if ((offset + 1 < end) && 
/* 269 */       (Character.isUpperCase(basename.charAt(offset + 1)))) {
/* 270 */       return basename.substring(offset);
/*     */     }
/*     */     
/* 273 */     StringBuilder sb = new StringBuilder(end - offset);
/* 274 */     sb.append(c1);
/* 275 */     sb.append(basename, offset + 1, end);
/* 276 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databin\\util\BeanUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */