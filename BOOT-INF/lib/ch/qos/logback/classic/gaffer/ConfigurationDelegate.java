/*     */ package ch.qos.logback.classic.gaffer;
/*     */ 
/*     */ import ch.qos.logback.classic.Level;
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.jmx.JMXConfigurator;
/*     */ import ch.qos.logback.classic.jmx.MBeanUtil;
/*     */ import ch.qos.logback.classic.joran.ReconfigureOnChangeTask;
/*     */ import ch.qos.logback.classic.net.ReceiverBase;
/*     */ import ch.qos.logback.classic.turbo.TurboFilter;
/*     */ import ch.qos.logback.core.Appender;
/*     */ import ch.qos.logback.core.CoreConstants;
/*     */ import ch.qos.logback.core.spi.ContextAware;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import ch.qos.logback.core.spi.LifeCycle;
/*     */ import ch.qos.logback.core.status.StatusListener;
/*     */ import ch.qos.logback.core.util.CachingDateFormatter;
/*     */ import ch.qos.logback.core.util.Duration;
/*     */ import groovy.lang.Closure;
/*     */ import groovy.lang.GroovyObject;
/*     */ import groovy.lang.MetaClass;
/*     */ import groovy.lang.Reference;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import org.codehaus.groovy.runtime.BytecodeInterface8;
/*     */ import org.codehaus.groovy.runtime.GStringImpl;
/*     */ import org.codehaus.groovy.runtime.GeneratedClosure;
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
/*     */ public class ConfigurationDelegate
/*     */   extends ContextAwareBase
/*     */   implements GroovyObject
/*     */ {
/*     */   public Object getDeclaredOrigin()
/*     */   {
/*  52 */     CallSite[] arrayOfCallSite = $getCallSiteArray();return this;return null;
/*     */   }
/*     */   
/*     */   public void scan(String scanPeriodStr) {
/*  56 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); if (DefaultTypeTransformation.booleanUnbox(scanPeriodStr)) {
/*  57 */       ReconfigureOnChangeTask rocTask = (ReconfigureOnChangeTask)ScriptBytecodeAdapter.castToType(arrayOfCallSite[0].callConstructor(ReconfigureOnChangeTask.class), ReconfigureOnChangeTask.class);
/*  58 */       arrayOfCallSite[1].call(rocTask, arrayOfCallSite[2].callGroovyObjectGetProperty(this));
/*  59 */       arrayOfCallSite[3].call(arrayOfCallSite[4].callGroovyObjectGetProperty(this), arrayOfCallSite[5].callGetProperty(CoreConstants.class), rocTask);
/*     */       try {
/*  61 */         try { Duration duration = (Duration)ScriptBytecodeAdapter.castToType(arrayOfCallSite[6].call(Duration.class, scanPeriodStr), Duration.class);
/*  62 */           ScheduledExecutorService scheduledExecutorService = (ScheduledExecutorService)ScriptBytecodeAdapter.castToType(arrayOfCallSite[7].call(arrayOfCallSite[8].callGroovyObjectGetProperty(this)), ScheduledExecutorService.class);
/*     */           
/*  64 */           ScheduledFuture scheduledFuture = (ScheduledFuture)ScriptBytecodeAdapter.castToType(arrayOfCallSite[9].call(scheduledExecutorService, rocTask, arrayOfCallSite[10].call(duration), arrayOfCallSite[11].call(duration), arrayOfCallSite[12].callGetProperty(TimeUnit.class)), ScheduledFuture.class);
/*  65 */           arrayOfCallSite[13].call(arrayOfCallSite[14].callGroovyObjectGetProperty(this), scheduledFuture);
/*  66 */           arrayOfCallSite[15].callCurrent(this, arrayOfCallSite[16].call("Setting ReconfigureOnChangeTask scanning period to ", duration));
/*     */         } catch (NumberFormatException nfe) {
/*  68 */           arrayOfCallSite[17].callCurrent(this, arrayOfCallSite[18].call(arrayOfCallSite[19].call("Error while converting [", scanPeriodStr), "] to long"), nfe);
/*     */         }
/*     */       } finally {}
/*     */     }
/*     */   }
/*     */   
/*  74 */   public void statusListener(Class listenerClass) { CallSite[] arrayOfCallSite = $getCallSiteArray();StatusListener statusListener = (StatusListener)ScriptBytecodeAdapter.castToType(arrayOfCallSite[20].call(listenerClass), StatusListener.class);
/*  75 */     arrayOfCallSite[21].call(arrayOfCallSite[22].callGetProperty(arrayOfCallSite[23].callGroovyObjectGetProperty(this)), statusListener);
/*  76 */     if ((statusListener instanceof ContextAware)) {
/*  77 */       arrayOfCallSite[24].call((ContextAware)ScriptBytecodeAdapter.castToType(statusListener, ContextAware.class), arrayOfCallSite[25].callGroovyObjectGetProperty(this));
/*     */     }
/*  79 */     if ((statusListener instanceof LifeCycle)) {
/*  80 */       arrayOfCallSite[26].call((LifeCycle)ScriptBytecodeAdapter.castToType(statusListener, LifeCycle.class));
/*     */     }
/*  82 */     arrayOfCallSite[27].callCurrent(this, new GStringImpl(new Object[] { arrayOfCallSite[28].callGetProperty(listenerClass) }, new String[] { "Added status listener of type [", "]" }));
/*     */   }
/*     */   
/*     */   public void conversionRule(String conversionWord, Class converterClass) {
/*  86 */     CallSite[] arrayOfCallSite = $getCallSiteArray();String converterClassName = (String)ShortTypeHandling.castToString(arrayOfCallSite[29].call(converterClass));
/*     */     
/*  88 */     Map ruleRegistry = (Map)ScriptBytecodeAdapter.castToType(arrayOfCallSite[30].call(arrayOfCallSite[31].callGroovyObjectGetProperty(this), arrayOfCallSite[32].callGetProperty(CoreConstants.class)), Map.class);
/*  89 */     if ((!BytecodeInterface8.isOrigZ()) || (__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) { if (ScriptBytecodeAdapter.compareEqual(ruleRegistry, null)) {
/*  90 */         Object localObject1 = arrayOfCallSite[33].callConstructor(HashMap.class);ruleRegistry = (Map)ScriptBytecodeAdapter.castToType(localObject1, Map.class);
/*  91 */         arrayOfCallSite[34].call(arrayOfCallSite[35].callGroovyObjectGetProperty(this), arrayOfCallSite[36].callGetProperty(CoreConstants.class), ruleRegistry);
/*     */       }
/*     */     }
/*  89 */     else if (ScriptBytecodeAdapter.compareEqual(ruleRegistry, null)) {
/*  90 */       Object localObject2 = arrayOfCallSite[37].callConstructor(HashMap.class);ruleRegistry = (Map)ScriptBytecodeAdapter.castToType(localObject2, Map.class);
/*  91 */       arrayOfCallSite[38].call(arrayOfCallSite[39].callGroovyObjectGetProperty(this), arrayOfCallSite[40].callGetProperty(CoreConstants.class), ruleRegistry);
/*     */     }
/*     */     
/*  94 */     arrayOfCallSite[41].callCurrent(this, arrayOfCallSite[42].call(arrayOfCallSite[43].call(arrayOfCallSite[44].call(arrayOfCallSite[45].call("registering conversion word ", conversionWord), " with class ["), converterClassName), "]"));
/*  95 */     arrayOfCallSite[46].call(ruleRegistry, conversionWord, converterClassName);
/*     */   }
/*     */   
/*  98 */   public void root(Level level) { CallSite[] arrayOfCallSite = $getCallSiteArray();root(level, ScriptBytecodeAdapter.createList(new Object[0]));null; } public void root(Level level, List<String> appenderNames) { CallSite[] arrayOfCallSite = $getCallSiteArray(); if ((!BytecodeInterface8.isOrigZ()) || (__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) {
/*  99 */       if (ScriptBytecodeAdapter.compareEqual(level, null)) {
/* 100 */         arrayOfCallSite[47].callCurrent(this, "Root logger cannot be set to level null");
/*     */       } else {
/* 102 */         arrayOfCallSite[48].callCurrent(this, arrayOfCallSite[49].callGetProperty(org.slf4j.Logger.class), level, appenderNames);
/*     */       }
/*     */     }
/*  99 */     else if (ScriptBytecodeAdapter.compareEqual(level, null)) {
/* 100 */       arrayOfCallSite[50].callCurrent(this, "Root logger cannot be set to level null");
/*     */     } else
/* 102 */       arrayOfCallSite[51].callCurrent(this, arrayOfCallSite[52].callGetProperty(org.slf4j.Logger.class), level, appenderNames);
/*     */   }
/*     */   
/*     */   public void logger(String name, Level level) {
/* 106 */     CallSite[] arrayOfCallSite = $getCallSiteArray();logger(name, level, ScriptBytecodeAdapter.createList(new Object[0]), null);null;
/*     */   }
/*     */   
/*     */   class _logger_closure1 extends Closure implements GeneratedClosure {
/*     */     public _logger_closure1(Object _thisObject, Reference aName) { super(_thisObject);
/*     */       Reference localReference = aName;
/*     */       this.aName = localReference;
/*     */     }
/*     */     
/* 113 */     public Object doCall(Object it) { CallSite[] arrayOfCallSite = $getCallSiteArray(); if ((!BytecodeInterface8.isOrigZ()) || (__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) return Boolean.valueOf(ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[0].callGetProperty(it), this.aName.get())); else return Boolean.valueOf(ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[1].callGetProperty(it), this.aName.get())); return null;
/*     */     }
/*     */     
/*     */     public Object getaName()
/*     */     {
/*     */       CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */       return this.aName.get();
/*     */       return null;
/*     */     }
/*     */     
/*     */     static {}
/*     */   }
/*     */   
/*     */   public void logger(String name, Level level, List<String> appenderNames, Boolean additivity)
/*     */   {
/* 107 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); Boolean localBoolean; if (DefaultTypeTransformation.booleanUnbox(name)) {
/* 108 */       ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger)ScriptBytecodeAdapter.castToType(arrayOfCallSite[53].call((LoggerContext)ScriptBytecodeAdapter.castToType(arrayOfCallSite[54].callGroovyObjectGetProperty(this), LoggerContext.class), name), ch.qos.logback.classic.Logger.class);
/* 109 */       arrayOfCallSite[55].callCurrent(this, arrayOfCallSite[56].call(new GStringImpl(new Object[] { name }, new String[] { "Setting level of logger [", "] to " }), level));
/* 110 */       Level localLevel = level;ScriptBytecodeAdapter.setProperty(localLevel, null, logger, "level");
/*     */       
/* 112 */       Reference aName = new Reference(null); for (Iterator localIterator = (Iterator)ScriptBytecodeAdapter.castToType(arrayOfCallSite[57].call(appenderNames), Iterator.class); localIterator.hasNext();) { ((Reference)aName).set(localIterator.next());
/* 113 */         Appender appender = (Appender)ScriptBytecodeAdapter.castToType(arrayOfCallSite[58].call(this.appenderList, new _logger_closure1(this, aName)), Appender.class);
/* 114 */         if (ScriptBytecodeAdapter.compareNotEqual(appender, null)) {
/* 115 */           arrayOfCallSite[59].callCurrent(this, arrayOfCallSite[60].call(new GStringImpl(new Object[] { aName.get() }, new String[] { "Attaching appender named [", "] to " }), logger));
/* 116 */           arrayOfCallSite[61].call(logger, appender);
/*     */         } else {
/* 118 */           arrayOfCallSite[62].callCurrent(this, new GStringImpl(new Object[] { aName.get() }, new String[] { "Failed to find appender named [", "]" }));
/*     */         }
/*     */       }
/*     */       
/* 122 */       if (ScriptBytecodeAdapter.compareNotEqual(additivity, null)) {
/* 123 */         localBoolean = additivity;ScriptBytecodeAdapter.setProperty(localBoolean, null, logger, "additive");
/*     */       }
/*     */     } else {
/* 126 */       arrayOfCallSite[63].callCurrent(this, "No name attribute for logger");
/*     */     }
/*     */   }
/*     */   
/*     */   public void appender(String name, Class clazz, Closure closure) {
/* 131 */     CallSite[] arrayOfCallSite = $getCallSiteArray();arrayOfCallSite[64].callCurrent(this, arrayOfCallSite[65].call(arrayOfCallSite[66].call("About to instantiate appender of type [", arrayOfCallSite[67].callGetProperty(clazz)), "]"));
/* 132 */     Appender appender = (Appender)ScriptBytecodeAdapter.castToType(arrayOfCallSite[68].call(clazz), Appender.class);
/* 133 */     arrayOfCallSite[69].callCurrent(this, arrayOfCallSite[70].call(arrayOfCallSite[71].call("Naming appender as [", name), "]"));
/* 134 */     String str = name;ScriptBytecodeAdapter.setProperty(str, null, appender, "name");
/* 135 */     Object localObject1 = arrayOfCallSite[72].callGroovyObjectGetProperty(this);ScriptBytecodeAdapter.setProperty(localObject1, null, appender, "context");
/* 136 */     arrayOfCallSite[73].call(this.appenderList, appender);
/* 137 */     if ((!BytecodeInterface8.isOrigZ()) || (__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) { if (ScriptBytecodeAdapter.compareNotEqual(closure, null)) {
/* 138 */         AppenderDelegate ad = (AppenderDelegate)ScriptBytecodeAdapter.castToType(arrayOfCallSite[74].callConstructor(AppenderDelegate.class, appender, this.appenderList), AppenderDelegate.class);
/* 139 */         arrayOfCallSite[75].callCurrent(this, ad, appender);
/* 140 */         Object localObject2 = arrayOfCallSite[76].callGroovyObjectGetProperty(this);ScriptBytecodeAdapter.setGroovyObjectProperty(localObject2, ConfigurationDelegate.class, ad, "context");
/* 141 */         AppenderDelegate localAppenderDelegate1 = ad;ScriptBytecodeAdapter.setGroovyObjectProperty(localAppenderDelegate1, ConfigurationDelegate.class, closure, "delegate");
/* 142 */         Object localObject3 = arrayOfCallSite[77].callGetProperty(Closure.class);ScriptBytecodeAdapter.setGroovyObjectProperty(localObject3, ConfigurationDelegate.class, closure, "resolveStrategy");
/* 143 */         arrayOfCallSite[78].call(closure);
/*     */       }
/*     */     }
/* 137 */     else if (ScriptBytecodeAdapter.compareNotEqual(closure, null)) {
/* 138 */       AppenderDelegate ad = (AppenderDelegate)ScriptBytecodeAdapter.castToType(arrayOfCallSite[79].callConstructor(AppenderDelegate.class, appender, this.appenderList), AppenderDelegate.class);
/* 139 */       arrayOfCallSite[80].callCurrent(this, ad, appender);
/* 140 */       Object localObject4 = arrayOfCallSite[81].callGroovyObjectGetProperty(this);ScriptBytecodeAdapter.setGroovyObjectProperty(localObject4, ConfigurationDelegate.class, ad, "context");
/* 141 */       AppenderDelegate localAppenderDelegate2 = ad;ScriptBytecodeAdapter.setGroovyObjectProperty(localAppenderDelegate2, ConfigurationDelegate.class, closure, "delegate");
/* 142 */       Object localObject5 = arrayOfCallSite[82].callGetProperty(Closure.class);ScriptBytecodeAdapter.setGroovyObjectProperty(localObject5, ConfigurationDelegate.class, closure, "resolveStrategy");
/* 143 */       arrayOfCallSite[83].call(closure);
/*     */     }
/*     */     try {
/* 146 */       try { arrayOfCallSite[84].call(appender);
/*     */       } catch (RuntimeException e) {
/* 148 */         arrayOfCallSite[85].callCurrent(this, arrayOfCallSite[86].call(arrayOfCallSite[87].call("Failed to start apppender named [", name), "]"), e);
/*     */       }
/*     */     } finally {}
/*     */   }
/*     */   
/* 153 */   public void receiver(String name, Class aClass, Closure closure) { CallSite[] arrayOfCallSite = $getCallSiteArray();arrayOfCallSite[88].callCurrent(this, arrayOfCallSite[89].call(arrayOfCallSite[90].call("About to instantiate receiver of type [", arrayOfCallSite[91].callGetProperty(arrayOfCallSite[92].callGroovyObjectGetProperty(this))), "]"));
/* 154 */     ReceiverBase receiver = (ReceiverBase)ScriptBytecodeAdapter.castToType(arrayOfCallSite[93].call(aClass), ReceiverBase.class);
/* 155 */     Object localObject1 = arrayOfCallSite[94].callGroovyObjectGetProperty(this);ScriptBytecodeAdapter.setProperty(localObject1, null, receiver, "context");
/* 156 */     if ((!BytecodeInterface8.isOrigZ()) || (__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) { if (ScriptBytecodeAdapter.compareNotEqual(closure, null)) {
/* 157 */         ComponentDelegate componentDelegate = (ComponentDelegate)ScriptBytecodeAdapter.castToType(arrayOfCallSite[95].callConstructor(ComponentDelegate.class, receiver), ComponentDelegate.class);
/* 158 */         Object localObject2 = arrayOfCallSite[96].callGroovyObjectGetProperty(this);ScriptBytecodeAdapter.setGroovyObjectProperty(localObject2, ConfigurationDelegate.class, componentDelegate, "context");
/* 159 */         ComponentDelegate localComponentDelegate1 = componentDelegate;ScriptBytecodeAdapter.setGroovyObjectProperty(localComponentDelegate1, ConfigurationDelegate.class, closure, "delegate");
/* 160 */         Object localObject3 = arrayOfCallSite[97].callGetProperty(Closure.class);ScriptBytecodeAdapter.setGroovyObjectProperty(localObject3, ConfigurationDelegate.class, closure, "resolveStrategy");
/* 161 */         arrayOfCallSite[98].call(closure);
/*     */       }
/*     */     }
/* 156 */     else if (ScriptBytecodeAdapter.compareNotEqual(closure, null)) {
/* 157 */       ComponentDelegate componentDelegate = (ComponentDelegate)ScriptBytecodeAdapter.castToType(arrayOfCallSite[99].callConstructor(ComponentDelegate.class, receiver), ComponentDelegate.class);
/* 158 */       Object localObject4 = arrayOfCallSite[100].callGroovyObjectGetProperty(this);ScriptBytecodeAdapter.setGroovyObjectProperty(localObject4, ConfigurationDelegate.class, componentDelegate, "context");
/* 159 */       ComponentDelegate localComponentDelegate2 = componentDelegate;ScriptBytecodeAdapter.setGroovyObjectProperty(localComponentDelegate2, ConfigurationDelegate.class, closure, "delegate");
/* 160 */       Object localObject5 = arrayOfCallSite[101].callGetProperty(Closure.class);ScriptBytecodeAdapter.setGroovyObjectProperty(localObject5, ConfigurationDelegate.class, closure, "resolveStrategy");
/* 161 */       arrayOfCallSite[102].call(closure);
/*     */     }
/*     */     try {
/* 164 */       try { arrayOfCallSite[103].call(receiver);
/*     */       } catch (RuntimeException e) {
/* 166 */         arrayOfCallSite[104].callCurrent(this, arrayOfCallSite[105].call(arrayOfCallSite[106].call("Failed to start receiver of type [", arrayOfCallSite[107].call(aClass)), "]"), e);
/*     */       }
/*     */     } finally {}
/*     */   }
/*     */   
/* 171 */   private void copyContributions(AppenderDelegate appenderDelegate, Appender appender) { Reference appenderDelegate = new Reference(appenderDelegate);Reference appender = new Reference(appender);CallSite[] arrayOfCallSite = $getCallSiteArray(); if (((Appender)appender.get() instanceof ConfigurationContributor)) {
/* 172 */       ConfigurationContributor cc = (ConfigurationContributor)ScriptBytecodeAdapter.castToType((Appender)appender.get(), ConfigurationContributor.class);
/* 173 */       arrayOfCallSite[108].call(arrayOfCallSite[109].call(cc), new _copyContributions_closure2(this, appenderDelegate, appender)); } }
/*     */   class _copyContributions_closure2 extends Closure implements GeneratedClosure { public _copyContributions_closure2(Object _thisObject, Reference appenderDelegate, Reference appender) { super(_thisObject);
/*     */       Reference localReference1 = appenderDelegate;
/*     */       this.appenderDelegate = localReference1;
/*     */       Reference localReference2 = appender;
/*     */       this.appender = localReference2; } public Object doCall(Object oldName, Object newName) { CallSite[] arrayOfCallSite = $getCallSiteArray();Closure localClosure = ScriptBytecodeAdapter.getMethodPointer(this.appender.get(), (String)ShortTypeHandling.castToString(new GStringImpl(new Object[] { oldName }, new String[] { "", "" })));ScriptBytecodeAdapter.setProperty(localClosure, null, arrayOfCallSite[0].callGroovyObjectGetProperty(this.appenderDelegate.get()), (String)ShortTypeHandling.castToString(new GStringImpl(new Object[] { newName }, new String[] { "", "" })));return localClosure;return null; }
/*     */     
/*     */     public Object call(Object oldName, Object newName) { CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */       return arrayOfCallSite[1].callCurrent(this, oldName, newName);
/*     */       return null; }
/*     */     public AppenderDelegate getAppenderDelegate() { CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */       return (AppenderDelegate)ScriptBytecodeAdapter.castToType(this.appenderDelegate.get(), AppenderDelegate.class);
/*     */       return null; }
/*     */     public Appender getAppender() { CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */       return (Appender)ScriptBytecodeAdapter.castToType(this.appender.get(), Appender.class);
/*     */       return null; }
/*     */     static {} }
/* 180 */   public void turboFilter(Class clazz, Closure closure) { CallSite[] arrayOfCallSite = $getCallSiteArray();arrayOfCallSite[110].callCurrent(this, arrayOfCallSite[111].call(arrayOfCallSite[112].call("About to instantiate turboFilter of type [", arrayOfCallSite[113].callGetProperty(clazz)), "]"));
/* 181 */     TurboFilter turboFilter = (TurboFilter)ScriptBytecodeAdapter.castToType(arrayOfCallSite[114].call(clazz), TurboFilter.class);
/* 182 */     Object localObject1 = arrayOfCallSite[115].callGroovyObjectGetProperty(this);ScriptBytecodeAdapter.setProperty(localObject1, null, turboFilter, "context");
/*     */     
/* 184 */     if ((!BytecodeInterface8.isOrigZ()) || (__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) { if (ScriptBytecodeAdapter.compareNotEqual(closure, null)) {
/* 185 */         ComponentDelegate componentDelegate = (ComponentDelegate)ScriptBytecodeAdapter.castToType(arrayOfCallSite[116].callConstructor(ComponentDelegate.class, turboFilter), ComponentDelegate.class);
/* 186 */         Object localObject2 = arrayOfCallSite[117].callGroovyObjectGetProperty(this);ScriptBytecodeAdapter.setGroovyObjectProperty(localObject2, ConfigurationDelegate.class, componentDelegate, "context");
/* 187 */         ComponentDelegate localComponentDelegate1 = componentDelegate;ScriptBytecodeAdapter.setGroovyObjectProperty(localComponentDelegate1, ConfigurationDelegate.class, closure, "delegate");
/* 188 */         Object localObject3 = arrayOfCallSite[118].callGetProperty(Closure.class);ScriptBytecodeAdapter.setGroovyObjectProperty(localObject3, ConfigurationDelegate.class, closure, "resolveStrategy");
/* 189 */         arrayOfCallSite[119].call(closure);
/*     */       }
/*     */     }
/* 184 */     else if (ScriptBytecodeAdapter.compareNotEqual(closure, null)) {
/* 185 */       ComponentDelegate componentDelegate = (ComponentDelegate)ScriptBytecodeAdapter.castToType(arrayOfCallSite[120].callConstructor(ComponentDelegate.class, turboFilter), ComponentDelegate.class);
/* 186 */       Object localObject4 = arrayOfCallSite[121].callGroovyObjectGetProperty(this);ScriptBytecodeAdapter.setGroovyObjectProperty(localObject4, ConfigurationDelegate.class, componentDelegate, "context");
/* 187 */       ComponentDelegate localComponentDelegate2 = componentDelegate;ScriptBytecodeAdapter.setGroovyObjectProperty(localComponentDelegate2, ConfigurationDelegate.class, closure, "delegate");
/* 188 */       Object localObject5 = arrayOfCallSite[122].callGetProperty(Closure.class);ScriptBytecodeAdapter.setGroovyObjectProperty(localObject5, ConfigurationDelegate.class, closure, "resolveStrategy");
/* 189 */       arrayOfCallSite[123].call(closure);
/*     */     }
/* 191 */     arrayOfCallSite[124].call(turboFilter);
/* 192 */     arrayOfCallSite[125].callCurrent(this, "Adding aforementioned turbo filter to context");
/* 193 */     arrayOfCallSite[126].call(arrayOfCallSite[127].callGroovyObjectGetProperty(this), turboFilter);
/*     */   }
/*     */   
/*     */   public String timestamp(String datePattern, long timeReference) {
/* 197 */     CallSite[] arrayOfCallSite = $getCallSiteArray();long now = DefaultTypeTransformation.longUnbox(Integer.valueOf(-1));
/*     */     
/* 199 */     if ((!BytecodeInterface8.isOrigL()) || (!BytecodeInterface8.isOrigZ()) || (__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) { Object localObject1; if (ScriptBytecodeAdapter.compareEqual(Long.valueOf(timeReference), Integer.valueOf(-1))) {
/* 200 */         arrayOfCallSite[128].callCurrent(this, "Using current interpretation time, i.e. now, as time reference.");
/* 201 */         localObject1 = arrayOfCallSite[129].call(System.class);now = DefaultTypeTransformation.longUnbox(localObject1);
/*     */       } else {
/* 203 */         long l1 = timeReference;now = l1;
/* 204 */         arrayOfCallSite[130].callCurrent(this, arrayOfCallSite[131].call(arrayOfCallSite[132].call("Using ", Long.valueOf(now)), " as time reference."));
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*     */       Object localObject2;
/* 199 */       if (ScriptBytecodeAdapter.compareEqual(Long.valueOf(timeReference), Integer.valueOf(-1))) {
/* 200 */         arrayOfCallSite[133].callCurrent(this, "Using current interpretation time, i.e. now, as time reference.");
/* 201 */         localObject2 = arrayOfCallSite[134].call(System.class);now = DefaultTypeTransformation.longUnbox(localObject2);
/*     */       } else {
/* 203 */         long l2 = timeReference;now = l2;
/* 204 */         arrayOfCallSite[135].callCurrent(this, arrayOfCallSite[136].call(arrayOfCallSite[137].call("Using ", Long.valueOf(now)), " as time reference."));
/*     */       } }
/* 206 */     CachingDateFormatter sdf = (CachingDateFormatter)ScriptBytecodeAdapter.castToType(arrayOfCallSite[138].callConstructor(CachingDateFormatter.class, datePattern), CachingDateFormatter.class);
/* 207 */     return (String)ShortTypeHandling.castToString(arrayOfCallSite[139].call(sdf, Long.valueOf(now)));return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void jmxConfigurator(String name)
/*     */   {
/* 218 */     CallSite[] arrayOfCallSite = $getCallSiteArray();Object objectName = null;
/* 219 */     Object contextName = arrayOfCallSite[140].callGetProperty(arrayOfCallSite[141].callGroovyObjectGetProperty(this));
/* 220 */     if ((!BytecodeInterface8.isOrigZ()) || (__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) { if (ScriptBytecodeAdapter.compareNotEqual(name, null)) {
/*     */         try {
/*     */           String str1;
/* 223 */           try { localObject1 = arrayOfCallSite[142].callConstructor(ObjectName.class, name);objectName = localObject1;
/*     */           } catch (MalformedObjectNameException e) { Object localObject1;
/* 225 */             str1 = name;contextName = str1;
/*     */           }
/*     */         }
/*     */         finally {}
/*     */       }
/*     */     }
/* 220 */     else if (ScriptBytecodeAdapter.compareNotEqual(name, null))
/*     */       try {
/*     */         String str2;
/* 223 */         try { localObject3 = arrayOfCallSite[143].callConstructor(ObjectName.class, name);objectName = localObject3;
/*     */         } catch (MalformedObjectNameException e) { Object localObject3;
/* 225 */           str2 = name;contextName = str2;
/*     */         }
/*     */       } finally {}
/* 228 */     if ((!BytecodeInterface8.isOrigZ()) || (__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) { if (ScriptBytecodeAdapter.compareEqual(objectName, null)) {
/* 229 */         Object objectNameAsStr = arrayOfCallSite[144].call(MBeanUtil.class, contextName, JMXConfigurator.class);
/* 230 */         Object localObject5 = arrayOfCallSite[145].call(MBeanUtil.class, arrayOfCallSite[146].callGroovyObjectGetProperty(this), this, objectNameAsStr);objectName = localObject5;
/* 231 */         if (ScriptBytecodeAdapter.compareEqual(objectName, null)) {
/* 232 */           arrayOfCallSite[147].callCurrent(this, new GStringImpl(new Object[] { objectNameAsStr }, new String[] { "Failed to construct ObjectName for [", "]" }));
/* 233 */           return;
/*     */         }
/*     */       }
/*     */     }
/* 228 */     else if (ScriptBytecodeAdapter.compareEqual(objectName, null)) {
/* 229 */       Object objectNameAsStr = arrayOfCallSite[148].call(MBeanUtil.class, contextName, JMXConfigurator.class);
/* 230 */       Object localObject6 = arrayOfCallSite[149].call(MBeanUtil.class, arrayOfCallSite[150].callGroovyObjectGetProperty(this), this, objectNameAsStr);objectName = localObject6;
/* 231 */       if (ScriptBytecodeAdapter.compareEqual(objectName, null)) {
/* 232 */         arrayOfCallSite[151].callCurrent(this, new GStringImpl(new Object[] { objectNameAsStr }, new String[] { "Failed to construct ObjectName for [", "]" }));
/* 233 */         return;
/*     */       }
/*     */     }
/*     */     
/* 237 */     Object platformMBeanServer = arrayOfCallSite[152].callGetProperty(ManagementFactory.class);
/* 238 */     if ((!DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[153].call(MBeanUtil.class, platformMBeanServer, objectName)) ? 1 : 0) != 0) {
/* 239 */       JMXConfigurator jmxConfigurator = (JMXConfigurator)ScriptBytecodeAdapter.castToType(arrayOfCallSite[154].callConstructor(JMXConfigurator.class, ScriptBytecodeAdapter.createPojoWrapper((LoggerContext)ScriptBytecodeAdapter.castToType(arrayOfCallSite[155].callGroovyObjectGetProperty(this), LoggerContext.class), LoggerContext.class), platformMBeanServer, objectName), JMXConfigurator.class);
/*     */       try {
/* 241 */         try { arrayOfCallSite[156].call(platformMBeanServer, jmxConfigurator, objectName);
/*     */         } catch (Exception all) {
/* 243 */           arrayOfCallSite[157].callCurrent(this, "Failed to create mbean", all);
/*     */         }
/*     */       }
/*     */       finally {}
/*     */     }
/*     */   }
/*     */   
/*     */   public void scan()
/*     */   {
/*     */     CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */     if ((__$stMC) || (BytecodeInterface8.disabledStandardMetaClass()))
/*     */     {
/*     */       scan(null);
/*     */       null;
/*     */     }
/*     */     else
/*     */     {
/*     */       scan(null);
/*     */       null;
/*     */     }
/*     */   }
/*     */   
/*     */   public void logger(String name, Level level, List<String> appenderNames)
/*     */   {
/*     */     CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */     logger(name, level, appenderNames, null);
/*     */     null;
/*     */   }
/*     */   
/*     */   public void appender(String name, Class clazz)
/*     */   {
/*     */     CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */     appender(name, clazz, null);
/*     */     null;
/*     */   }
/*     */   
/*     */   public void receiver(String name, Class aClass)
/*     */   {
/*     */     CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */     receiver(name, aClass, null);
/*     */     null;
/*     */   }
/*     */   
/*     */   public void turboFilter(Class clazz)
/*     */   {
/*     */     CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */     turboFilter(clazz, null);
/*     */     null;
/*     */   }
/*     */   
/*     */   public String timestamp(String datePattern)
/*     */   {
/*     */     CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */     if ((__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) {
/*     */       return timestamp(datePattern, DefaultTypeTransformation.longUnbox(Integer.valueOf(-1)));
/*     */     } else {
/*     */       return timestamp(datePattern, DefaultTypeTransformation.longUnbox(Integer.valueOf(-1)));
/*     */     }
/*     */     return null;
/*     */   }
/*     */   
/*     */   public void jmxConfigurator()
/*     */   {
/*     */     CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */     if ((__$stMC) || (BytecodeInterface8.disabledStandardMetaClass()))
/*     */     {
/*     */       jmxConfigurator(null);
/*     */       null;
/*     */     }
/*     */     else
/*     */     {
/*     */       jmxConfigurator(null);
/*     */       null;
/*     */     }
/*     */   }
/*     */   
/*     */   static {}
/*     */   
/*     */   public List<Appender> getAppenderList()
/*     */   {
/*     */     return this.appenderList;
/*     */   }
/*     */   
/*     */   public void setAppenderList(List<Appender> paramList)
/*     */   {
/*     */     this.appenderList = paramList;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\gaffer\ConfigurationDelegate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */