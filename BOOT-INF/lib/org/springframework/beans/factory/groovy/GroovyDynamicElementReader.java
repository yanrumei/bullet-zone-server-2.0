/*     */ package org.springframework.beans.factory.groovy;
/*     */ 
/*     */ import groovy.lang.Closure;
/*     */ import groovy.lang.GroovyObjectSupport;
/*     */ import groovy.lang.Reference;
/*     */ import groovy.xml.StreamingMarkupBuilder;
/*     */ import java.io.StringWriter;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.codehaus.groovy.runtime.BytecodeInterface8;
/*     */ import org.codehaus.groovy.runtime.GStringImpl;
/*     */ import org.codehaus.groovy.runtime.GeneratedClosure;
/*     */ import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
/*     */ import org.codehaus.groovy.runtime.callsite.CallSite;
/*     */ import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
/*     */ import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
/*     */ import org.w3c.dom.Element;
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
/*     */ class GroovyDynamicElementReader
/*     */   extends GroovyObjectSupport
/*     */ {
/*     */   private final String rootNamespace;
/*     */   private final Map<String, String> xmlNamespaces;
/*     */   private final BeanDefinitionParserDelegate delegate;
/*     */   private final GroovyBeanDefinitionWrapper beanDefinition;
/*     */   protected final boolean decorating;
/*     */   private boolean callAfterInvocation;
/*     */   
/*     */   public GroovyDynamicElementReader(String namespace, Map<String, String> namespaceMap, BeanDefinitionParserDelegate delegate, GroovyBeanDefinitionWrapper beanDefinition, boolean decorating)
/*     */   {
/*  50 */     boolean bool1 = true;this.callAfterInvocation = bool1;
/*  51 */     String str = namespace;this.rootNamespace = ((String)ShortTypeHandling.castToString(str));
/*  52 */     Map localMap = namespaceMap;this.xmlNamespaces = ((Map)ScriptBytecodeAdapter.castToType(localMap, Map.class));
/*  53 */     BeanDefinitionParserDelegate localBeanDefinitionParserDelegate = delegate;this.delegate = ((BeanDefinitionParserDelegate)ScriptBytecodeAdapter.castToType(localBeanDefinitionParserDelegate, BeanDefinitionParserDelegate.class));
/*  54 */     GroovyBeanDefinitionWrapper localGroovyBeanDefinitionWrapper = beanDefinition;this.beanDefinition = ((GroovyBeanDefinitionWrapper)ScriptBytecodeAdapter.castToType(localGroovyBeanDefinitionWrapper, $get$$class$org$springframework$beans$factory$groovy$GroovyBeanDefinitionWrapper()));
/*  55 */     boolean bool2 = decorating;this.decorating = DefaultTypeTransformation.booleanUnbox(Boolean.valueOf(bool2));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public class _invokeMethod_closure1
/*     */     extends Closure
/*     */     implements GeneratedClosure
/*     */   {
/*     */     public _invokeMethod_closure1(Object _thisObject, Reference myNamespaces, Reference args, Reference builder, Reference myNamespace, Reference name)
/*     */     {
/*     */       super(_thisObject);
/*     */       Reference localReference1 = myNamespaces;
/*     */       this.myNamespaces = localReference1;
/*     */       Reference localReference2 = args;
/*     */       this.args = localReference2;
/*     */       Reference localReference3 = builder;
/*     */       this.builder = localReference3;
/*     */       Reference localReference4 = myNamespace;
/*     */       this.myNamespace = localReference4;
/*     */       Reference localReference5 = name;
/*     */       this.name = localReference5;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Object doCall(Object it)
/*     */     {
/*  80 */       CallSite[] arrayOfCallSite = $getCallSiteArray();Object namespace = null; for (Iterator localIterator = (Iterator)ScriptBytecodeAdapter.castToType(arrayOfCallSite[0].call(this.myNamespaces.get()), Iterator.class); localIterator.hasNext();) { namespace = localIterator.next();
/*  81 */         arrayOfCallSite[1].call(arrayOfCallSite[2].callGroovyObjectGetProperty(this), ScriptBytecodeAdapter.createMap(new Object[] { arrayOfCallSite[3].callGetProperty(namespace), arrayOfCallSite[4].callGetProperty(namespace) })); }
/*     */       Object localObject2;
/*  83 */       if (((DefaultTypeTransformation.booleanUnbox(this.args.get())) && ((arrayOfCallSite[5].call(this.args.get(), Integer.valueOf(-1)) instanceof Closure)) ? 1 : 0) != 0) {
/*  84 */         Object localObject1 = arrayOfCallSite[6].callGetProperty(Closure.class);ScriptBytecodeAdapter.setProperty(localObject1, null, arrayOfCallSite[7].call(this.args.get(), Integer.valueOf(-1)), (String)"resolveStrategy");
/*  85 */         localObject2 = this.builder.get();ScriptBytecodeAdapter.setProperty(localObject2, null, arrayOfCallSite[8].call(this.args.get(), Integer.valueOf(-1)), (String)"delegate");
/*     */       }
/*  87 */       return ScriptBytecodeAdapter.invokeMethodN(_invokeMethod_closure1.class, ScriptBytecodeAdapter.getProperty(_invokeMethod_closure1.class, arrayOfCallSite[9].callGroovyObjectGetProperty(this), (String)ShortTypeHandling.castToString(new GStringImpl(new Object[] { this.myNamespace.get() }, new String[] { "", "" }))), (String)ShortTypeHandling.castToString(new GStringImpl(new Object[] { this.name.get() }, new String[] { "", "" })), ScriptBytecodeAdapter.despreadList(new Object[0], new Object[] { this.args.get() }, new int[] { 0 }));return null;
/*     */     }
/*     */     
/*     */     public Object getMyNamespaces()
/*     */     {
/*     */       CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */       return this.myNamespaces.get();
/*     */       return null;
/*     */     }
/*     */     
/*     */     public Object getArgs()
/*     */     {
/*     */       CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */       return this.args.get();
/*     */       return null;
/*     */     }
/*     */     
/*     */     public StreamingMarkupBuilder getBuilder()
/*     */     {
/*     */       CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */       return (StreamingMarkupBuilder)ScriptBytecodeAdapter.castToType(this.builder.get(), StreamingMarkupBuilder.class);
/*     */       return null;
/*     */     }
/*     */     
/*     */     public Object getMyNamespace()
/*     */     {
/*     */       CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */       return this.myNamespace.get();
/*     */       return null;
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/*     */       CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */       return (String)ShortTypeHandling.castToString(this.name.get());
/*     */       return null;
/*     */     }
/*     */     
/*     */     public Object doCall()
/*     */     {
/*     */       CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */       return doCall(null);
/*     */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   public Object invokeMethod(String name, Object args)
/*     */   {
/*  61 */     Reference name = new Reference(name);Reference args = new Reference(args);CallSite[] arrayOfCallSite = $getCallSiteArray(); if (DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[0].call((String)name.get(), "doCall"))) {
/*  62 */       Object callable = arrayOfCallSite[1].call((Object)args.get(), Integer.valueOf(0));
/*  63 */       Object localObject1 = arrayOfCallSite[2].callGetProperty(Closure.class);ScriptBytecodeAdapter.setProperty(localObject1, null, callable, (String)"resolveStrategy");
/*  64 */       GroovyDynamicElementReader localGroovyDynamicElementReader = this;ScriptBytecodeAdapter.setProperty(localGroovyDynamicElementReader, null, callable, (String)"delegate");
/*  65 */       Object result = arrayOfCallSite[3].call(callable);
/*     */       boolean bool1;
/*  67 */       if (this.callAfterInvocation) {
/*  68 */         if ((__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) { arrayOfCallSite[4].callCurrent(this); } else { afterInvocation();null; }
/*  69 */         bool1 = false;this.callAfterInvocation = DefaultTypeTransformation.booleanUnbox(Boolean.valueOf(bool1));
/*     */       }
/*  71 */       return result;
/*     */     }
/*     */     else
/*     */     {
/*  75 */       Reference builder = new Reference((StreamingMarkupBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[5].callConstructor(StreamingMarkupBuilder.class), StreamingMarkupBuilder.class));
/*  76 */       Reference myNamespace = new Reference(this.rootNamespace);
/*  77 */       Reference myNamespaces = new Reference(this.xmlNamespaces);
/*     */       
/*  79 */       Object callable = new _invokeMethod_closure1(this, myNamespaces, args, builder, myNamespace, name);
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
/*  90 */       Object localObject2 = arrayOfCallSite[6].callGetProperty(Closure.class);ScriptBytecodeAdapter.setProperty(localObject2, null, callable, (String)"resolveStrategy");
/*  91 */       StreamingMarkupBuilder localStreamingMarkupBuilder = (StreamingMarkupBuilder)builder.get();ScriptBytecodeAdapter.setProperty(localStreamingMarkupBuilder, null, callable, (String)"delegate");
/*  92 */       Object writable = arrayOfCallSite[7].call((StreamingMarkupBuilder)builder.get(), callable);
/*  93 */       Object sw = arrayOfCallSite[8].callConstructor(StringWriter.class);
/*  94 */       arrayOfCallSite[9].call(writable, sw);
/*     */       
/*  96 */       Element element = (Element)ScriptBytecodeAdapter.castToType(arrayOfCallSite[10].callGetProperty(arrayOfCallSite[11].call(arrayOfCallSite[12].callGetProperty(this.delegate), arrayOfCallSite[13].call(sw))), Element.class);
/*  97 */       arrayOfCallSite[14].call(this.delegate, element);
/*  98 */       if (this.decorating) {
/*  99 */         BeanDefinitionHolder holder = (BeanDefinitionHolder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[15].callGetProperty(this.beanDefinition), BeanDefinitionHolder.class);
/* 100 */         Object localObject3 = arrayOfCallSite[16].call(this.delegate, element, holder, null);holder = (BeanDefinitionHolder)ScriptBytecodeAdapter.castToType(localObject3, BeanDefinitionHolder.class);
/* 101 */         arrayOfCallSite[17].call(this.beanDefinition, holder);
/*     */       }
/*     */       else {
/* 104 */         Object beanDefinition = arrayOfCallSite[18].call(this.delegate, element);
/* 105 */         if (DefaultTypeTransformation.booleanUnbox(beanDefinition))
/* 106 */           arrayOfCallSite[19].call(this.beanDefinition, beanDefinition);
/*     */       }
/*     */       boolean bool2;
/* 109 */       if (this.callAfterInvocation) {
/* 110 */         if ((__$stMC) || (BytecodeInterface8.disabledStandardMetaClass())) { arrayOfCallSite[20].callCurrent(this); } else { afterInvocation();null; }
/* 111 */         bool2 = false;this.callAfterInvocation = DefaultTypeTransformation.booleanUnbox(Boolean.valueOf(bool2));
/*     */       }
/* 113 */       return element; } return null;
/*     */   }
/*     */   
/*     */   protected void afterInvocation()
/*     */   {
/*     */     CallSite[] arrayOfCallSite = $getCallSiteArray();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\groovy\GroovyDynamicElementReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */