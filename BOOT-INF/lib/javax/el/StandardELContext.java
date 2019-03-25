/*     */ package javax.el;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class StandardELContext
/*     */   extends ELContext
/*     */ {
/*     */   private final ELContext wrappedContext;
/*     */   private final VariableMapper variableMapper;
/*     */   private final FunctionMapper functionMapper;
/*     */   private final CompositeELResolver standardResolver;
/*     */   private final CompositeELResolver customResolvers;
/*  33 */   private final Map<String, Object> localBeans = new HashMap();
/*     */   
/*     */   public StandardELContext(ExpressionFactory factory)
/*     */   {
/*  37 */     this.wrappedContext = null;
/*  38 */     this.variableMapper = new StandardVariableMapper(null);
/*     */     
/*  40 */     this.functionMapper = new StandardFunctionMapper(factory.getInitFunctionMap());
/*  41 */     this.standardResolver = new CompositeELResolver();
/*  42 */     this.customResolvers = new CompositeELResolver();
/*     */     
/*  44 */     ELResolver streamResolver = factory.getStreamELResolver();
/*     */     
/*     */ 
/*  47 */     this.standardResolver.add(new BeanNameELResolver(new StandardBeanNameResolver(this.localBeans)));
/*     */     
/*  49 */     this.standardResolver.add(this.customResolvers);
/*  50 */     if (streamResolver != null) {
/*  51 */       this.standardResolver.add(streamResolver);
/*     */     }
/*  53 */     this.standardResolver.add(new StaticFieldELResolver());
/*  54 */     this.standardResolver.add(new MapELResolver());
/*  55 */     this.standardResolver.add(new ResourceBundleELResolver());
/*  56 */     this.standardResolver.add(new ListELResolver());
/*  57 */     this.standardResolver.add(new ArrayELResolver());
/*  58 */     this.standardResolver.add(new BeanELResolver());
/*     */   }
/*     */   
/*     */   public StandardELContext(ELContext context) {
/*  62 */     this.wrappedContext = context;
/*  63 */     this.variableMapper = context.getVariableMapper();
/*  64 */     this.functionMapper = context.getFunctionMapper();
/*  65 */     this.standardResolver = new CompositeELResolver();
/*  66 */     this.customResolvers = new CompositeELResolver();
/*     */     
/*     */ 
/*  69 */     this.standardResolver.add(new BeanNameELResolver(new StandardBeanNameResolver(this.localBeans)));
/*     */     
/*  71 */     this.standardResolver.add(this.customResolvers);
/*     */     
/*  73 */     this.standardResolver.add(context.getELResolver());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void putContext(Class key, Object contextObject)
/*     */   {
/*  80 */     if (this.wrappedContext == null) {
/*  81 */       super.putContext(key, contextObject);
/*     */     } else {
/*  83 */       this.wrappedContext.putContext(key, contextObject);
/*     */     }
/*     */   }
/*     */   
/*     */   public Object getContext(Class key)
/*     */   {
/*  89 */     if (this.wrappedContext == null) {
/*  90 */       return super.getContext(key);
/*     */     }
/*  92 */     return this.wrappedContext.getContext(key);
/*     */   }
/*     */   
/*     */ 
/*     */   public ELResolver getELResolver()
/*     */   {
/*  98 */     return this.standardResolver;
/*     */   }
/*     */   
/*     */   public void addELResolver(ELResolver resolver) {
/* 102 */     this.customResolvers.add(resolver);
/*     */   }
/*     */   
/*     */   public FunctionMapper getFunctionMapper()
/*     */   {
/* 107 */     return this.functionMapper;
/*     */   }
/*     */   
/*     */   public VariableMapper getVariableMapper()
/*     */   {
/* 112 */     return this.variableMapper;
/*     */   }
/*     */   
/*     */   Map<String, Object> getLocalBeans()
/*     */   {
/* 117 */     return this.localBeans;
/*     */   }
/*     */   
/*     */   private static class StandardVariableMapper
/*     */     extends VariableMapper
/*     */   {
/*     */     private Map<String, ValueExpression> vars;
/*     */     
/*     */     public ValueExpression resolveVariable(String variable)
/*     */     {
/* 127 */       if (this.vars == null) {
/* 128 */         return null;
/*     */       }
/* 130 */       return (ValueExpression)this.vars.get(variable);
/*     */     }
/*     */     
/*     */ 
/*     */     public ValueExpression setVariable(String variable, ValueExpression expression)
/*     */     {
/* 136 */       if (this.vars == null)
/* 137 */         this.vars = new HashMap();
/* 138 */       if (expression == null) {
/* 139 */         return (ValueExpression)this.vars.remove(variable);
/*     */       }
/* 141 */       return (ValueExpression)this.vars.put(variable, expression);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class StandardBeanNameResolver
/*     */     extends BeanNameResolver
/*     */   {
/*     */     private final Map<String, Object> beans;
/*     */     
/*     */     public StandardBeanNameResolver(Map<String, Object> beans)
/*     */     {
/* 152 */       this.beans = beans;
/*     */     }
/*     */     
/*     */     public boolean isNameResolved(String beanName)
/*     */     {
/* 157 */       return this.beans.containsKey(beanName);
/*     */     }
/*     */     
/*     */     public Object getBean(String beanName)
/*     */     {
/* 162 */       return this.beans.get(beanName);
/*     */     }
/*     */     
/*     */     public void setBeanValue(String beanName, Object value)
/*     */       throws PropertyNotWritableException
/*     */     {
/* 168 */       this.beans.put(beanName, value);
/*     */     }
/*     */     
/*     */     public boolean isReadOnly(String beanName)
/*     */     {
/* 173 */       return false;
/*     */     }
/*     */     
/*     */     public boolean canCreateBean(String beanName)
/*     */     {
/* 178 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class StandardFunctionMapper
/*     */     extends FunctionMapper
/*     */   {
/* 185 */     private final Map<String, Method> methods = new HashMap();
/*     */     
/*     */     public StandardFunctionMapper(Map<String, Method> initFunctionMap) {
/* 188 */       if (initFunctionMap != null) {
/* 189 */         this.methods.putAll(initFunctionMap);
/*     */       }
/*     */     }
/*     */     
/*     */     public Method resolveFunction(String prefix, String localName)
/*     */     {
/* 195 */       String key = prefix + ':' + localName;
/* 196 */       return (Method)this.methods.get(key);
/*     */     }
/*     */     
/*     */ 
/*     */     public void mapFunction(String prefix, String localName, Method method)
/*     */     {
/* 202 */       String key = prefix + ':' + localName;
/* 203 */       if (method == null) {
/* 204 */         this.methods.remove(key);
/*     */       } else {
/* 206 */         this.methods.put(key, method);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\StandardELContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */