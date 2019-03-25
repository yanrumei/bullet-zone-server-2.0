/*     */ package javax.el;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public class ImportHandler
/*     */ {
/*  32 */   private List<String> packageNames = new ArrayList();
/*  33 */   private ConcurrentHashMap<String, String> classNames = new ConcurrentHashMap();
/*  34 */   private Map<String, Class<?>> clazzes = new ConcurrentHashMap();
/*  35 */   private Map<String, Class<?>> statics = new ConcurrentHashMap();
/*     */   
/*     */   public ImportHandler()
/*     */   {
/*  39 */     importPackage("java.lang");
/*     */   }
/*     */   
/*     */   public void importStatic(String name) throws ELException
/*     */   {
/*  44 */     int lastPeriod = name.lastIndexOf('.');
/*     */     
/*  46 */     if (lastPeriod < 0) {
/*  47 */       throw new ELException(Util.message(null, "importHandler.invalidStaticName", new Object[] { name }));
/*     */     }
/*     */     
/*     */ 
/*  51 */     String className = name.substring(0, lastPeriod);
/*  52 */     String fieldOrMethodName = name.substring(lastPeriod + 1);
/*     */     
/*  54 */     Class<?> clazz = findClass(className, true);
/*     */     
/*  56 */     if (clazz == null) {
/*  57 */       throw new ELException(Util.message(null, "importHandler.invalidClassNameForStatic", new Object[] { className, name }));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  62 */     boolean found = false;
/*     */     
/*  64 */     for (Field field : clazz.getFields()) {
/*  65 */       if (field.getName().equals(fieldOrMethodName)) {
/*  66 */         int modifiers = field.getModifiers();
/*  67 */         if ((Modifier.isStatic(modifiers)) && 
/*  68 */           (Modifier.isPublic(modifiers))) {
/*  69 */           found = true;
/*  70 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  75 */     if (!found) {
/*  76 */       for (Method method : clazz.getMethods()) {
/*  77 */         if (method.getName().equals(fieldOrMethodName)) {
/*  78 */           int modifiers = method.getModifiers();
/*  79 */           if ((Modifier.isStatic(modifiers)) && 
/*  80 */             (Modifier.isPublic(modifiers))) {
/*  81 */             found = true;
/*  82 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  88 */     if (!found) {
/*  89 */       throw new ELException(Util.message(null, "importHandler.staticNotFound", new Object[] { fieldOrMethodName, className, name }));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  94 */     Object conflict = (Class)this.statics.get(fieldOrMethodName);
/*  95 */     if (conflict != null) {
/*  96 */       throw new ELException(Util.message(null, "importHandler.ambiguousStaticImport", new Object[] { name, ((Class)conflict)
/*     */       
/*  98 */         .getName() + '.' + fieldOrMethodName }));
/*     */     }
/*     */     
/* 101 */     this.statics.put(fieldOrMethodName, clazz);
/*     */   }
/*     */   
/*     */   public void importClass(String name) throws ELException
/*     */   {
/* 106 */     int lastPeriodIndex = name.lastIndexOf('.');
/*     */     
/* 108 */     if (lastPeriodIndex < 0) {
/* 109 */       throw new ELException(Util.message(null, "importHandler.invalidClassName", new Object[] { name }));
/*     */     }
/*     */     
/*     */ 
/* 113 */     String unqualifiedName = name.substring(lastPeriodIndex + 1);
/* 114 */     String currentName = (String)this.classNames.putIfAbsent(unqualifiedName, name);
/*     */     
/* 116 */     if ((currentName != null) && (!currentName.equals(name)))
/*     */     {
/* 118 */       throw new ELException(Util.message(null, "importHandler.ambiguousImport", new Object[] { name, currentName }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void importPackage(String name)
/*     */   {
/* 130 */     this.packageNames.add(name);
/*     */   }
/*     */   
/*     */   public Class<?> resolveClass(String name)
/*     */   {
/* 135 */     if ((name == null) || (name.contains("."))) {
/* 136 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 140 */     Class<?> result = (Class)this.clazzes.get(name);
/*     */     
/* 142 */     if (result != null) {
/* 143 */       if (NotFound.class.equals(result)) {
/* 144 */         return null;
/*     */       }
/* 146 */       return result;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 151 */     String className = (String)this.classNames.get(name);
/* 152 */     Class<?> clazz; if (className != null) {
/* 153 */       clazz = findClass(className, true);
/* 154 */       if (clazz != null) {
/* 155 */         this.clazzes.put(name, clazz);
/* 156 */         return clazz;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 162 */     for (String p : this.packageNames) {
/* 163 */       className = p + '.' + name;
/* 164 */       Class<?> clazz = findClass(className, false);
/* 165 */       if (clazz != null) {
/* 166 */         if (result != null) {
/* 167 */           throw new ELException(Util.message(null, "importHandler.ambiguousImport", new Object[] { className, result
/*     */           
/* 169 */             .getName() }));
/*     */         }
/* 171 */         result = clazz;
/*     */       }
/*     */     }
/* 174 */     if (result == null)
/*     */     {
/*     */ 
/* 177 */       this.clazzes.put(name, NotFound.class);
/*     */     } else {
/* 179 */       this.clazzes.put(name, result);
/*     */     }
/*     */     
/* 182 */     return result;
/*     */   }
/*     */   
/*     */   public Class<?> resolveStatic(String name)
/*     */   {
/* 187 */     return (Class)this.statics.get(name);
/*     */   }
/*     */   
/*     */ 
/*     */   private Class<?> findClass(String name, boolean throwException)
/*     */   {
/* 193 */     ClassLoader cl = Thread.currentThread().getContextClassLoader();
/* 194 */     String path = name.replace('.', '/') + ".class";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 202 */       if (cl.getResource(path) == null) {
/* 203 */         return null;
/*     */       }
/*     */     }
/*     */     catch (ClassCircularityError localClassCircularityError) {}
/*     */     
/*     */     try
/*     */     {
/* 210 */       clazz = cl.loadClass(name);
/*     */     } catch (ClassNotFoundException e) { Class<?> clazz;
/* 212 */       return null;
/*     */     }
/*     */     
/*     */     Class<?> clazz;
/* 216 */     int modifiers = clazz.getModifiers();
/* 217 */     if ((!Modifier.isPublic(modifiers)) || (Modifier.isAbstract(modifiers)) || 
/* 218 */       (Modifier.isInterface(modifiers))) {
/* 219 */       if (throwException) {
/* 220 */         throw new ELException(Util.message(null, "importHandler.invalidClass", new Object[] { name }));
/*     */       }
/*     */       
/* 223 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 227 */     return clazz;
/*     */   }
/*     */   
/*     */   private static class NotFound {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\ImportHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */