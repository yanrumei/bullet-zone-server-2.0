/*     */ package ch.qos.logback.classic.gaffer;
/*     */ 
/*     */ import ch.qos.logback.core.joran.spi.NoAutoStartUtil;
/*     */ import ch.qos.logback.core.spi.ContextAware;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import ch.qos.logback.core.spi.LifeCycle;
/*     */ import groovy.lang.Closure;
/*     */ import groovy.lang.GroovyObject;
/*     */ import groovy.lang.MetaClass;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.codehaus.groovy.runtime.BytecodeInterface8;
/*     */ import org.codehaus.groovy.runtime.GStringImpl;
/*     */ import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
/*     */ import org.codehaus.groovy.runtime.callsite.CallSite;
/*     */ import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
/*     */ import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ComponentDelegate
/*     */   extends ContextAwareBase
/*     */   implements GroovyObject
/*     */ {
/*     */   public ComponentDelegate(Object arg1) {}
/*     */   
/*     */   public String getLabel()
/*     */   {
/*  34 */     CallSite[] arrayOfCallSite = $getCallSiteArray();return "component";return null; }
/*     */   
/*  36 */   public String getLabelFistLetterInUpperCase() { CallSite[] arrayOfCallSite = $getCallSiteArray(); if ((__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) return (String)ShortTypeHandling.castToString(arrayOfCallSite[0].call(arrayOfCallSite[1].call(arrayOfCallSite[2].call(arrayOfCallSite[3].callCurrent(this), Integer.valueOf(0))), arrayOfCallSite[4].call(arrayOfCallSite[5].callCurrent(this), Integer.valueOf(1)))); else return (String)ShortTypeHandling.castToString(arrayOfCallSite[6].call(arrayOfCallSite[7].call(arrayOfCallSite[8].call(getLabel(), Integer.valueOf(0))), arrayOfCallSite[9].call(getLabel(), Integer.valueOf(1)))); return null;
/*     */   }
/*     */   
/*  39 */   public void methodMissing(String name, Object args) { CallSite[] arrayOfCallSite = $getCallSiteArray();NestingType nestingType = (NestingType)ShortTypeHandling.castToEnum(arrayOfCallSite[10].call(PropertyUtil.class, this.component, name, null), NestingType.class);
/*  40 */     if ((!BytecodeInterface8.isOrigZ()) || (__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) { if (ScriptBytecodeAdapter.compareEqual(nestingType, arrayOfCallSite[11].callGetProperty(NestingType.class))) {
/*  41 */         arrayOfCallSite[12].callCurrent(this, new GStringImpl(new Object[] { arrayOfCallSite[13].callCurrent(this), arrayOfCallSite[14].callCurrent(this), arrayOfCallSite[15].callGetProperty(arrayOfCallSite[16].call(this.component)), name }, new String[] { "", " ", " of type [", "] has no appplicable [", "] property." }));
/*  42 */         return;
/*     */       }
/*     */     }
/*  40 */     else if (ScriptBytecodeAdapter.compareEqual(nestingType, arrayOfCallSite[17].callGetProperty(NestingType.class))) {
/*  41 */       arrayOfCallSite[18].callCurrent(this, new GStringImpl(new Object[] { getLabelFistLetterInUpperCase(), getComponentName(), arrayOfCallSite[19].callGetProperty(arrayOfCallSite[20].call(this.component)), name }, new String[] { "", " ", " of type [", "] has no appplicable [", "] property." }));
/*  42 */       return;
/*     */     }
/*     */     
/*  45 */     String subComponentName = null;
/*  46 */     Class clazz = null;
/*  47 */     Closure closure = null;
/*     */     
/*  49 */     Object localObject1 = arrayOfCallSite[21].callCurrent(this, args);subComponentName = (String)ShortTypeHandling.castToString(arrayOfCallSite[22].call(localObject1, Integer.valueOf(0)));clazz = (Class)ShortTypeHandling.castToClass(arrayOfCallSite[23].call(localObject1, Integer.valueOf(1)));closure = (Closure)ScriptBytecodeAdapter.castToType(arrayOfCallSite[24].call(localObject1, Integer.valueOf(2)), Closure.class);
/*  50 */     if ((!BytecodeInterface8.isOrigZ()) || (__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) { if (ScriptBytecodeAdapter.compareNotEqual(clazz, null)) {
/*  51 */         Object subComponent = arrayOfCallSite[25].call(clazz);
/*  52 */         String str1; if (((DefaultTypeTransformation.booleanUnbox(subComponentName)) && (DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[26].call(subComponent, name))) ? 1 : 0) != 0) {
/*  53 */           str1 = subComponentName;ScriptBytecodeAdapter.setProperty(str1, null, subComponent, "name"); }
/*     */         Object localObject2;
/*  55 */         if ((subComponent instanceof ContextAware)) {
/*  56 */           localObject2 = arrayOfCallSite[27].callGroovyObjectGetProperty(this);ScriptBytecodeAdapter.setProperty(localObject2, null, subComponent, "context");
/*     */         }
/*  58 */         if (DefaultTypeTransformation.booleanUnbox(closure)) {
/*  59 */           ComponentDelegate subDelegate = (ComponentDelegate)ScriptBytecodeAdapter.castToType(arrayOfCallSite[28].callConstructor(ComponentDelegate.class, subComponent), ComponentDelegate.class);
/*     */           
/*  61 */           arrayOfCallSite[29].callCurrent(this, subDelegate);
/*  62 */           Object localObject3 = arrayOfCallSite[30].callGroovyObjectGetProperty(this);ScriptBytecodeAdapter.setGroovyObjectProperty(localObject3, ComponentDelegate.class, subDelegate, "context");
/*  63 */           arrayOfCallSite[31].callCurrent(this, subComponent);
/*  64 */           ComponentDelegate localComponentDelegate1 = subDelegate;ScriptBytecodeAdapter.setGroovyObjectProperty(localComponentDelegate1, ComponentDelegate.class, closure, "delegate");
/*  65 */           Object localObject4 = arrayOfCallSite[32].callGetProperty(Closure.class);ScriptBytecodeAdapter.setGroovyObjectProperty(localObject4, ComponentDelegate.class, closure, "resolveStrategy");
/*  66 */           arrayOfCallSite[33].call(closure);
/*     */         }
/*  68 */         if ((((subComponent instanceof LifeCycle)) && (DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[34].call(NoAutoStartUtil.class, subComponent))) ? 1 : 0) != 0) {
/*  69 */           arrayOfCallSite[35].call(subComponent);
/*     */         }
/*  71 */         arrayOfCallSite[36].call(PropertyUtil.class, nestingType, this.component, subComponent, name);
/*     */       } else {
/*  73 */         arrayOfCallSite[37].callCurrent(this, new GStringImpl(new Object[] { name, arrayOfCallSite[38].callCurrent(this), arrayOfCallSite[39].callCurrent(this), arrayOfCallSite[40].callGetProperty(arrayOfCallSite[41].call(this.component)) }, new String[] { "No 'class' argument specified for [", "] in ", " ", " of type [", "]" }));
/*     */       }
/*     */     }
/*  50 */     else if (ScriptBytecodeAdapter.compareNotEqual(clazz, null)) {
/*  51 */       Object subComponent = arrayOfCallSite[42].call(clazz);
/*  52 */       String str2; if (((DefaultTypeTransformation.booleanUnbox(subComponentName)) && (DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[43].call(subComponent, name))) ? 1 : 0) != 0) {
/*  53 */         str2 = subComponentName;ScriptBytecodeAdapter.setProperty(str2, null, subComponent, "name"); }
/*     */       Object localObject5;
/*  55 */       if ((subComponent instanceof ContextAware)) {
/*  56 */         localObject5 = arrayOfCallSite[44].callGroovyObjectGetProperty(this);ScriptBytecodeAdapter.setProperty(localObject5, null, subComponent, "context");
/*     */       }
/*  58 */       if (DefaultTypeTransformation.booleanUnbox(closure)) {
/*  59 */         ComponentDelegate subDelegate = (ComponentDelegate)ScriptBytecodeAdapter.castToType(arrayOfCallSite[45].callConstructor(ComponentDelegate.class, subComponent), ComponentDelegate.class);
/*     */         
/*  61 */         arrayOfCallSite[46].callCurrent(this, subDelegate);
/*  62 */         Object localObject6 = arrayOfCallSite[47].callGroovyObjectGetProperty(this);ScriptBytecodeAdapter.setGroovyObjectProperty(localObject6, ComponentDelegate.class, subDelegate, "context");
/*  63 */         arrayOfCallSite[48].callCurrent(this, subComponent);
/*  64 */         ComponentDelegate localComponentDelegate2 = subDelegate;ScriptBytecodeAdapter.setGroovyObjectProperty(localComponentDelegate2, ComponentDelegate.class, closure, "delegate");
/*  65 */         Object localObject7 = arrayOfCallSite[49].callGetProperty(Closure.class);ScriptBytecodeAdapter.setGroovyObjectProperty(localObject7, ComponentDelegate.class, closure, "resolveStrategy");
/*  66 */         arrayOfCallSite[50].call(closure);
/*     */       }
/*  68 */       if ((((subComponent instanceof LifeCycle)) && (DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[51].call(NoAutoStartUtil.class, subComponent))) ? 1 : 0) != 0) {
/*  69 */         arrayOfCallSite[52].call(subComponent);
/*     */       }
/*  71 */       arrayOfCallSite[53].call(PropertyUtil.class, nestingType, this.component, subComponent, name);
/*     */     } else {
/*  73 */       arrayOfCallSite[54].callCurrent(this, new GStringImpl(new Object[] { name, getLabel(), getComponentName(), arrayOfCallSite[55].callGetProperty(arrayOfCallSite[56].call(this.component)) }, new String[] { "No 'class' argument specified for [", "] in ", " ", " of type [", "]" }));
/*     */     }
/*     */   }
/*     */   
/*     */   public void cascadeFields(ComponentDelegate subDelegate) {
/*  78 */     CallSite[] arrayOfCallSite = $getCallSiteArray();String k = null; Object localObject; for (Iterator localIterator = (Iterator)ScriptBytecodeAdapter.castToType(arrayOfCallSite[57].call(this.fieldsToCascade), Iterator.class); localIterator.hasNext(); 
/*  79 */         ScriptBytecodeAdapter.setProperty(localObject, null, arrayOfCallSite[58].callGroovyObjectGetProperty(subDelegate), (String)ShortTypeHandling.castToString(new GStringImpl(new Object[] { k }, new String[] { "", "" }))))
/*     */     {
/*  78 */       k = (String)ShortTypeHandling.castToString(localIterator.next());
/*  79 */       localObject = ScriptBytecodeAdapter.getGroovyObjectProperty(ComponentDelegate.class, this, (String)ShortTypeHandling.castToString(new GStringImpl(new Object[] { k }, new String[] { "", "" })));
/*     */     }
/*     */   }
/*     */   
/*     */   public void injectParent(Object subComponent) {
/*  84 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); Object localObject; if (DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[59].call(subComponent, "parent"))) {
/*  85 */       localObject = this.component;ScriptBytecodeAdapter.setProperty(localObject, null, subComponent, "parent");
/*     */     }
/*     */   }
/*     */   
/*     */   public void propertyMissing(String name, Object value) {
/*  90 */     CallSite[] arrayOfCallSite = $getCallSiteArray();NestingType nestingType = (NestingType)ShortTypeHandling.castToEnum(arrayOfCallSite[60].call(PropertyUtil.class, this.component, name, value), NestingType.class);
/*  91 */     if ((!BytecodeInterface8.isOrigZ()) || (__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) { if (ScriptBytecodeAdapter.compareEqual(nestingType, arrayOfCallSite[61].callGetProperty(NestingType.class))) {
/*  92 */         arrayOfCallSite[62].callCurrent(this, new GStringImpl(new Object[] { arrayOfCallSite[63].callCurrent(this), arrayOfCallSite[64].callCurrent(this), arrayOfCallSite[65].callGetProperty(arrayOfCallSite[66].call(this.component)), name }, new String[] { "", " ", " of type [", "] has no appplicable [", "] property " }));
/*  93 */         return;
/*     */       }
/*     */     }
/*  91 */     else if (ScriptBytecodeAdapter.compareEqual(nestingType, arrayOfCallSite[67].callGetProperty(NestingType.class))) {
/*  92 */       arrayOfCallSite[68].callCurrent(this, new GStringImpl(new Object[] { getLabelFistLetterInUpperCase(), getComponentName(), arrayOfCallSite[69].callGetProperty(arrayOfCallSite[70].call(this.component)), name }, new String[] { "", " ", " of type [", "] has no appplicable [", "] property " }));
/*  93 */       return;
/*     */     }
/*  95 */     arrayOfCallSite[71].call(PropertyUtil.class, nestingType, this.component, value, name);
/*     */   }
/*     */   
/*     */   public Object analyzeArgs(Object... args)
/*     */   {
/* 100 */     CallSite[] arrayOfCallSite = $getCallSiteArray();String name = null;
/* 101 */     Class clazz = null;
/* 102 */     Closure closure = null;
/*     */     
/* 104 */     if ((!BytecodeInterface8.isOrigInt()) || (!BytecodeInterface8.isOrigZ()) || (__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) { if (ScriptBytecodeAdapter.compareGreaterThan(arrayOfCallSite[72].call(args), Integer.valueOf(3))) {
/* 105 */         arrayOfCallSite[73].callCurrent(this, new GStringImpl(new Object[] { args }, new String[] { "At most 3 arguments allowed but you passed ", "" }));
/* 106 */         return ScriptBytecodeAdapter.createList(new Object[] { name, clazz, closure });
/*     */       }
/*     */     }
/* 104 */     else if (ScriptBytecodeAdapter.compareGreaterThan(arrayOfCallSite[74].call(args), Integer.valueOf(3))) {
/* 105 */       arrayOfCallSite[75].callCurrent(this, new GStringImpl(new Object[] { args }, new String[] { "At most 3 arguments allowed but you passed ", "" }));
/* 106 */       return ScriptBytecodeAdapter.createList(new Object[] { name, clazz, closure });
/*     */     }
/*     */     
/* 109 */     if ((__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) { if ((arrayOfCallSite[76].call(args, Integer.valueOf(-1)) instanceof Closure)) {
/* 110 */         Object localObject1 = arrayOfCallSite[77].call(args, Integer.valueOf(-1));closure = (Closure)ScriptBytecodeAdapter.castToType(localObject1, Closure.class); Object 
/* 111 */           tmp317_312 = arrayOfCallSite[78].call(args, arrayOfCallSite[79].call(args, Integer.valueOf(-1)));args = (Object[])ScriptBytecodeAdapter.castToType(tmp317_312, Object[].class);tmp317_312;
/*     */       }
/*     */     }
/* 109 */     else if ((BytecodeInterface8.objectArrayGet(args, Integer.valueOf(-1).intValue()) instanceof Closure)) {
/* 110 */       Object localObject2 = BytecodeInterface8.objectArrayGet(args, Integer.valueOf(-1).intValue());closure = (Closure)ScriptBytecodeAdapter.castToType(localObject2, Closure.class); Object 
/* 111 */         tmp402_397 = arrayOfCallSite[80].call(args, BytecodeInterface8.objectArrayGet(args, Integer.valueOf(-1).intValue()));args = (Object[])ScriptBytecodeAdapter.castToType(tmp402_397, Object[].class);tmp402_397; }
/*     */     Object localObject3;
/*     */     Object localObject4;
/* 114 */     if ((!BytecodeInterface8.isOrigInt()) || (!BytecodeInterface8.isOrigZ()) || (__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) { if (ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[81].call(args), Integer.valueOf(1))) {
/* 115 */         localObject3 = arrayOfCallSite[82].callCurrent(this, arrayOfCallSite[83].call(args, Integer.valueOf(0)));clazz = (Class)ShortTypeHandling.castToClass(localObject3);
/*     */       }
/*     */     }
/* 114 */     else if (ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[84].call(args), Integer.valueOf(1))) {
/* 115 */       localObject4 = arrayOfCallSite[85].callCurrent(this, BytecodeInterface8.objectArrayGet(args, 0));clazz = (Class)ShortTypeHandling.castToClass(localObject4); }
/*     */     Object localObject6;
/*     */     Object localObject8;
/* 118 */     if ((!BytecodeInterface8.isOrigInt()) || (!BytecodeInterface8.isOrigZ()) || (__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) { if (ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[86].call(args), Integer.valueOf(2))) {
/* 119 */         Object localObject5 = arrayOfCallSite[87].callCurrent(this, arrayOfCallSite[88].call(args, Integer.valueOf(0)));name = (String)ShortTypeHandling.castToString(localObject5);
/* 120 */         localObject6 = arrayOfCallSite[89].callCurrent(this, arrayOfCallSite[90].call(args, Integer.valueOf(1)));clazz = (Class)ShortTypeHandling.castToClass(localObject6);
/*     */       }
/*     */     }
/* 118 */     else if (ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[91].call(args), Integer.valueOf(2))) {
/* 119 */       Object localObject7 = arrayOfCallSite[92].callCurrent(this, BytecodeInterface8.objectArrayGet(args, 0));name = (String)ShortTypeHandling.castToString(localObject7);
/* 120 */       localObject8 = arrayOfCallSite[93].callCurrent(this, BytecodeInterface8.objectArrayGet(args, 1));clazz = (Class)ShortTypeHandling.castToClass(localObject8);
/*     */     }
/*     */     
/* 123 */     return ScriptBytecodeAdapter.createList(new Object[] { name, clazz, closure });return null;
/*     */   }
/*     */   
/*     */   public Class parseClassArgument(Object arg) {
/* 127 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); if ((arg instanceof Class)) {
/* 128 */       return (Class)ShortTypeHandling.castToClass(arg);
/* 129 */     } else if ((arg instanceof String)) {
/* 130 */       return Class.forName((String)ShortTypeHandling.castToString(arg));
/*     */     } else {
/* 132 */       arrayOfCallSite[94].callCurrent(this, new GStringImpl(new Object[] { arrayOfCallSite[95].callGetProperty(arrayOfCallSite[96].call(arg)) }, new String[] { "Unexpected argument type ", "" }));
/* 133 */       return (Class)ShortTypeHandling.castToClass(null); } return null;
/*     */   }
/*     */   
/*     */   public String parseNameArgument(Object arg)
/*     */   {
/* 138 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); if ((arg instanceof String)) {
/* 139 */       return (String)ShortTypeHandling.castToString(arg);
/*     */     } else {
/* 141 */       arrayOfCallSite[97].callCurrent(this, "With 2 or 3 arguments, the first argument must be the component name, i.e of type string");
/* 142 */       return (String)ShortTypeHandling.castToString(null); } return null;
/*     */   }
/*     */   
/*     */   public String getComponentName()
/*     */   {
/* 147 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); if (DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[98].call(this.component, "name"))) {
/* 148 */       return (String)ShortTypeHandling.castToString(new GStringImpl(new Object[] { arrayOfCallSite[99].callGetProperty(this.component) }, new String[] { "[", "]" }));
/*     */     } else
/* 150 */       return ""; return null;
/*     */   }
/*     */   
/*     */   static {}
/*     */   
/*     */   public final Object getComponent()
/*     */   {
/*     */     return this.component;
/*     */   }
/*     */   
/*     */   public final List getFieldsToCascade()
/*     */   {
/*     */     return this.fieldsToCascade;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\gaffer\ComponentDelegate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */