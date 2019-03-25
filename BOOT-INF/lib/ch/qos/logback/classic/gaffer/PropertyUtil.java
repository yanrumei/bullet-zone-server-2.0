/*    */ package ch.qos.logback.classic.gaffer;
/*    */ 
/*    */ import ch.qos.logback.core.joran.util.StringToObjectConverter;
/*    */ import ch.qos.logback.core.joran.util.beans.BeanUtil;
/*    */ import groovy.lang.Closure;
/*    */ import groovy.lang.GroovyObject;
/*    */ import groovy.lang.MetaClass;
/*    */ import groovy.lang.MetaProperty;
/*    */ import java.lang.reflect.Method;
/*    */ import org.codehaus.groovy.runtime.BytecodeInterface8;
/*    */ import org.codehaus.groovy.runtime.GStringImpl;
/*    */ import org.codehaus.groovy.runtime.GeneratedClosure;
/*    */ import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
/*    */ import org.codehaus.groovy.runtime.callsite.CallSite;
/*    */ import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
/*    */ import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;
/*    */ 
/*    */ public class PropertyUtil
/*    */   implements GroovyObject
/*    */ {
/*    */   public PropertyUtil()
/*    */   {
/*    */     PropertyUtil this;
/*    */     CallSite[] arrayOfCallSite = $getCallSiteArray();
/*    */     MetaClass localMetaClass = $getStaticMetaClass();
/*    */     this.metaClass = localMetaClass;
/*    */   }
/*    */   
/*    */   public static boolean hasAdderMethod(Object obj, String name)
/*    */   {
/* 29 */     CallSite[] arrayOfCallSite = $getCallSiteArray();String addMethod = null; GStringImpl localGStringImpl1; GStringImpl localGStringImpl2; if ((__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) { localGStringImpl1 = new GStringImpl(new Object[] { arrayOfCallSite[0].callStatic(PropertyUtil.class, name) }, new String[] { "add", "" });addMethod = (String)ShortTypeHandling.castToString(localGStringImpl1); } else { localGStringImpl2 = new GStringImpl(new Object[] { upperCaseFirstLetter(name) }, new String[] { "add", "" });addMethod = (String)ShortTypeHandling.castToString(localGStringImpl2); }
/* 30 */     return DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[1].call(arrayOfCallSite[2].callGetProperty(obj), obj, addMethod));return DefaultTypeTransformation.booleanUnbox(Integer.valueOf(0));
/*    */   }
/*    */   
/*    */   public static NestingType nestingType(Object obj, String name, Object value)
/*    */   {
/* 35 */     CallSite[] arrayOfCallSite = $getCallSiteArray();Object decapitalizedName = arrayOfCallSite[3].call(BeanUtil.class, name);
/* 36 */     MetaProperty metaProperty = (MetaProperty)ScriptBytecodeAdapter.castToType(arrayOfCallSite[4].call(obj, decapitalizedName), MetaProperty.class);
/*    */     
/* 38 */     if ((!BytecodeInterface8.isOrigZ()) || (__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) { if (ScriptBytecodeAdapter.compareNotEqual(metaProperty, null)) {
/* 39 */         boolean VALUE_IS_A_STRING = value instanceof String;
/*    */         
/* 41 */         if (((VALUE_IS_A_STRING) && (DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[5].call(StringToObjectConverter.class, arrayOfCallSite[6].call(metaProperty)))) ? 1 : 0) != 0) {
/* 42 */           return (NestingType)ShortTypeHandling.castToEnum(arrayOfCallSite[7].callGetProperty(NestingType.class), NestingType.class);
/*    */         } else {
/* 44 */           return (NestingType)ShortTypeHandling.castToEnum(arrayOfCallSite[8].callGetProperty(NestingType.class), NestingType.class);
/*    */         }
/*    */       }
/*    */     }
/* 38 */     else if (ScriptBytecodeAdapter.compareNotEqual(metaProperty, null)) {
/* 39 */       boolean VALUE_IS_A_STRING = value instanceof String;
/*    */       
/* 41 */       if (((VALUE_IS_A_STRING) && (DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[9].call(StringToObjectConverter.class, arrayOfCallSite[10].call(metaProperty)))) ? 1 : 0) != 0) {
/* 42 */         return (NestingType)ShortTypeHandling.castToEnum(arrayOfCallSite[11].callGetProperty(NestingType.class), NestingType.class);
/*    */       } else {
/* 44 */         return (NestingType)ShortTypeHandling.castToEnum(arrayOfCallSite[12].callGetProperty(NestingType.class), NestingType.class);
/*    */       }
/*    */     }
/* 47 */     if (DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[13].callStatic(PropertyUtil.class, obj, name))) {
/* 48 */       return (NestingType)ShortTypeHandling.castToEnum(arrayOfCallSite[14].callGetProperty(NestingType.class), NestingType.class);
/*    */     }
/* 50 */     return (NestingType)ShortTypeHandling.castToEnum(arrayOfCallSite[15].callGetProperty(NestingType.class), NestingType.class);return null;
/*    */   }
/*    */   
/*    */   public static Object convertByValueMethod(Object component, String name, String value) {
/* 54 */     CallSite[] arrayOfCallSite = $getCallSiteArray();Object decapitalizedName = arrayOfCallSite[16].call(BeanUtil.class, name);
/* 55 */     MetaProperty metaProperty = (MetaProperty)ScriptBytecodeAdapter.castToType(arrayOfCallSite[17].call(component, decapitalizedName), MetaProperty.class);
/* 56 */     Method valueOfMethod = (Method)ScriptBytecodeAdapter.castToType(arrayOfCallSite[18].call(StringToObjectConverter.class, arrayOfCallSite[19].call(metaProperty)), Method.class);
/* 57 */     return arrayOfCallSite[20].call(valueOfMethod, null, value);return null;
/*    */   }
/*    */   
/*    */   public static void attach(NestingType nestingType, Object component, Object subComponent, String name) {
/* 61 */     CallSite[] arrayOfCallSite = $getCallSiteArray();NestingType localNestingType = nestingType;
/* 62 */     if (ScriptBytecodeAdapter.isCase(localNestingType, arrayOfCallSite[21].callGetProperty(NestingType.class))) {
/* 63 */       Object localObject1 = arrayOfCallSite[22].call(BeanUtil.class, name);name = (String)ShortTypeHandling.castToString(localObject1);
/* 64 */       Object value = arrayOfCallSite[23].callStatic(PropertyUtil.class, component, name, subComponent);
/* 65 */       Object localObject2 = value;ScriptBytecodeAdapter.setProperty(localObject2, null, component, (String)ShortTypeHandling.castToString(new GStringImpl(new Object[] { name }, new String[] { "", "" })));
/* 66 */       return;
/* 67 */     } else { if (!ScriptBytecodeAdapter.isCase(localNestingType, arrayOfCallSite[24].callGetProperty(NestingType.class))) break label231; }
/* 68 */     Object localObject3 = arrayOfCallSite[25].call(BeanUtil.class, name);name = (String)ShortTypeHandling.castToString(localObject3);
/* 69 */     Object localObject4 = subComponent;ScriptBytecodeAdapter.setProperty(localObject4, null, component, (String)ShortTypeHandling.castToString(new GStringImpl(new Object[] { name }, new String[] { "", "" })));
/* 70 */     return;
/*    */     label231:
/* 72 */     if (ScriptBytecodeAdapter.isCase(localNestingType, arrayOfCallSite[26].callGetProperty(NestingType.class))) {
/* 73 */       String firstUpperName = (String)ShortTypeHandling.castToString(arrayOfCallSite[27].call(PropertyUtil.class, name));
/* 74 */       ScriptBytecodeAdapter.invokeMethodN(PropertyUtil.class, component, (String)ShortTypeHandling.castToString(new GStringImpl(new Object[] { firstUpperName }, new String[] { "add", "" })), new Object[] { subComponent });
/*    */     }
/*    */   }
/*    */   
/*    */   public static String transformFirstLetter(String s, Closure closure)
/*    */   {
/* 80 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); if ((!BytecodeInterface8.isOrigInt()) || (!BytecodeInterface8.isOrigZ()) || (__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) { if (((ScriptBytecodeAdapter.compareEqual(s, null)) || (ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[28].call(s), Integer.valueOf(0))) ? 1 : 0) != 0) {
/* 81 */         return s;
/*    */       }
/*    */     }
/* 80 */     else if (((ScriptBytecodeAdapter.compareEqual(s, null)) || (ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[29].call(s), Integer.valueOf(0))) ? 1 : 0) != 0) {
/* 81 */       return s;
/*    */     }
/* 83 */     String firstLetter = (String)ShortTypeHandling.castToString(arrayOfCallSite[30].callConstructor(String.class, arrayOfCallSite[31].call(s, Integer.valueOf(0))));
/*    */     
/* 85 */     String modifiedFistLetter = (String)ShortTypeHandling.castToString(arrayOfCallSite[32].call(closure, firstLetter));
/*    */     
/* 87 */     if ((!BytecodeInterface8.isOrigInt()) || (!BytecodeInterface8.isOrigZ()) || (__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) { if (ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[33].call(s), Integer.valueOf(1))) {
/* 88 */         return modifiedFistLetter;
/*    */       } else {
/* 90 */         return (String)ShortTypeHandling.castToString(arrayOfCallSite[34].call(modifiedFistLetter, arrayOfCallSite[35].call(s, Integer.valueOf(1))));
/*    */       }
/*    */     }
/* 87 */     else if (ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[36].call(s), Integer.valueOf(1))) {
/* 88 */       return modifiedFistLetter;
/*    */     } else
/* 90 */       return (String)ShortTypeHandling.castToString(arrayOfCallSite[37].call(modifiedFistLetter, arrayOfCallSite[38].call(s, Integer.valueOf(1)))); return null; }
/*    */   
/*    */   class _upperCaseFirstLetter_closure1 extends Closure implements GeneratedClosure { public _upperCaseFirstLetter_closure1(Object _thisObject) { super(_thisObject); }
/*    */     
/* 94 */     public Object doCall(String it) { CallSite[] arrayOfCallSite = $getCallSiteArray();return arrayOfCallSite[0].call(it);return null; } public Object call(String it) { CallSite[] arrayOfCallSite = $getCallSiteArray(); if ((__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) return arrayOfCallSite[1].callCurrent(this, it); else return doCall(it); return null; } static {} } public static String upperCaseFirstLetter(String s) { CallSite[] arrayOfCallSite = $getCallSiteArray();return (String)ShortTypeHandling.castToString(arrayOfCallSite[39].callStatic(PropertyUtil.class, s, new _upperCaseFirstLetter_closure1(PropertyUtil.class, PropertyUtil.class)));return null;
/*    */   }
/*    */   
/*    */   static {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\gaffer\PropertyUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */