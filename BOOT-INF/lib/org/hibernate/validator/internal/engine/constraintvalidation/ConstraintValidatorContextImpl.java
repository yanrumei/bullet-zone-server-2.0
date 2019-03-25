/*     */ package org.hibernate.validator.internal.engine.constraintvalidation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.validation.ConstraintValidatorContext;
/*     */ import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
/*     */ import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeBuilderCustomizableContext;
/*     */ import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeBuilderDefinedContext;
/*     */ import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeContextBuilder;
/*     */ import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;
/*     */ import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderDefinedContext;
/*     */ import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeContextBuilder;
/*     */ import javax.validation.ElementKind;
/*     */ import javax.validation.metadata.ConstraintDescriptor;
/*     */ import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
/*     */ import org.hibernate.validator.internal.engine.path.NodeImpl;
/*     */ import org.hibernate.validator.internal.engine.path.PathImpl;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.Contracts;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.spi.time.TimeProvider;
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
/*     */ public class ConstraintValidatorContextImpl
/*     */   implements HibernateConstraintValidatorContext
/*     */ {
/*  38 */   private static final Log log = ;
/*     */   
/*  40 */   private final Map<String, Object> expressionVariables = CollectionHelper.newHashMap();
/*     */   private final List<String> methodParameterNames;
/*     */   private final TimeProvider timeProvider;
/*  43 */   private final List<ConstraintViolationCreationContext> constraintViolationCreationContexts = CollectionHelper.newArrayList(3);
/*     */   private final PathImpl basePath;
/*     */   private final ConstraintDescriptor<?> constraintDescriptor;
/*     */   private boolean defaultDisabled;
/*     */   private Object dynamicPayload;
/*     */   
/*     */   public ConstraintValidatorContextImpl(List<String> methodParameterNames, TimeProvider timeProvider, PathImpl propertyPath, ConstraintDescriptor<?> constraintDescriptor)
/*     */   {
/*  51 */     this.methodParameterNames = methodParameterNames;
/*  52 */     this.timeProvider = timeProvider;
/*  53 */     this.basePath = propertyPath;
/*  54 */     this.constraintDescriptor = constraintDescriptor;
/*     */   }
/*     */   
/*     */   public final void disableDefaultConstraintViolation()
/*     */   {
/*  59 */     this.defaultDisabled = true;
/*     */   }
/*     */   
/*     */   public final String getDefaultConstraintMessageTemplate()
/*     */   {
/*  64 */     return (String)this.constraintDescriptor.getAttributes().get("message");
/*     */   }
/*     */   
/*     */   public final ConstraintValidatorContext.ConstraintViolationBuilder buildConstraintViolationWithTemplate(String messageTemplate)
/*     */   {
/*  69 */     return new ConstraintViolationBuilderImpl(this.methodParameterNames, messageTemplate, 
/*     */     
/*     */ 
/*  72 */       PathImpl.createCopy(this.basePath), null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public <T> T unwrap(Class<T> type)
/*     */   {
/*  79 */     if (type.isAssignableFrom(HibernateConstraintValidatorContext.class)) {
/*  80 */       return (T)type.cast(this);
/*     */     }
/*  82 */     throw log.getTypeNotSupportedForUnwrappingException(type);
/*     */   }
/*     */   
/*     */   public HibernateConstraintValidatorContext addExpressionVariable(String name, Object value)
/*     */   {
/*  87 */     Contracts.assertNotNull(name, "null is not a valid value");
/*  88 */     this.expressionVariables.put(name, value);
/*  89 */     return this;
/*     */   }
/*     */   
/*     */   public TimeProvider getTimeProvider()
/*     */   {
/*  94 */     return this.timeProvider;
/*     */   }
/*     */   
/*     */   public HibernateConstraintValidatorContext withDynamicPayload(Object violationContext)
/*     */   {
/*  99 */     this.dynamicPayload = violationContext;
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public final ConstraintDescriptor<?> getConstraintDescriptor() {
/* 104 */     return this.constraintDescriptor;
/*     */   }
/*     */   
/*     */   public final List<ConstraintViolationCreationContext> getConstraintViolationCreationContexts() {
/* 108 */     if ((this.defaultDisabled) && (this.constraintViolationCreationContexts.size() == 0)) {
/* 109 */       throw log.getAtLeastOneCustomMessageMustBeCreatedException();
/*     */     }
/*     */     
/* 112 */     List<ConstraintViolationCreationContext> returnedConstraintViolationCreationContexts = new ArrayList(this.constraintViolationCreationContexts);
/*     */     
/*     */ 
/* 115 */     if (!this.defaultDisabled) {
/* 116 */       Map<String, Object> parameterMapCopy = CollectionHelper.newHashMap();
/* 117 */       parameterMapCopy.putAll(this.expressionVariables);
/* 118 */       returnedConstraintViolationCreationContexts.add(new ConstraintViolationCreationContext(
/*     */       
/* 120 */         getDefaultConstraintMessageTemplate(), this.basePath, parameterMapCopy, this.dynamicPayload));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 127 */     return returnedConstraintViolationCreationContexts;
/*     */   }
/*     */   
/*     */   public List<String> getMethodParameterNames() {
/* 131 */     return this.methodParameterNames;
/*     */   }
/*     */   
/*     */   private abstract class NodeBuilderBase {
/*     */     protected final String messageTemplate;
/*     */     protected PathImpl propertyPath;
/*     */     
/*     */     protected NodeBuilderBase(String template, PathImpl path) {
/* 139 */       this.messageTemplate = template;
/* 140 */       this.propertyPath = path;
/*     */     }
/*     */     
/*     */     public ConstraintValidatorContext addConstraintViolation() {
/* 144 */       Map<String, Object> parameterMapCopy = CollectionHelper.newHashMap();
/* 145 */       parameterMapCopy.putAll(ConstraintValidatorContextImpl.this.expressionVariables);
/* 146 */       ConstraintValidatorContextImpl.this.constraintViolationCreationContexts.add(new ConstraintViolationCreationContext(this.messageTemplate, this.propertyPath, parameterMapCopy, 
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 151 */         ConstraintValidatorContextImpl.this.dynamicPayload));
/*     */       
/*     */ 
/* 154 */       return ConstraintValidatorContextImpl.this;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ConstraintViolationBuilderImpl extends ConstraintValidatorContextImpl.NodeBuilderBase implements ConstraintValidatorContext.ConstraintViolationBuilder
/*     */   {
/*     */     private final List<String> methodParameterNames;
/*     */     
/*     */     private ConstraintViolationBuilderImpl(String methodParameterNames, PathImpl template) {
/* 163 */       super(template, path);
/* 164 */       this.methodParameterNames = methodParameterNames;
/*     */     }
/*     */     
/*     */     public ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderDefinedContext addNode(String name)
/*     */     {
/* 169 */       dropLeafNodeIfRequired();
/* 170 */       this.propertyPath.addPropertyNode(name);
/*     */       
/* 172 */       return new ConstraintValidatorContextImpl.NodeBuilder(ConstraintValidatorContextImpl.this, this.messageTemplate, this.propertyPath, null);
/*     */     }
/*     */     
/*     */     public ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext addPropertyNode(String name)
/*     */     {
/* 177 */       dropLeafNodeIfRequired();
/*     */       
/* 179 */       return new ConstraintValidatorContextImpl.DeferredNodeBuilder(ConstraintValidatorContextImpl.this, this.messageTemplate, this.propertyPath, name, null);
/*     */     }
/*     */     
/*     */     public ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeBuilderCustomizableContext addBeanNode()
/*     */     {
/* 184 */       return new ConstraintValidatorContextImpl.DeferredNodeBuilder(ConstraintValidatorContextImpl.this, this.messageTemplate, this.propertyPath, null, null);
/*     */     }
/*     */     
/*     */     public ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderDefinedContext addParameterNode(int index)
/*     */     {
/* 189 */       if (this.propertyPath.getLeafNode().getKind() != ElementKind.CROSS_PARAMETER) {
/* 190 */         throw ConstraintValidatorContextImpl.log.getParameterNodeAddedForNonCrossParameterConstraintException(this.propertyPath);
/*     */       }
/*     */       
/* 193 */       dropLeafNodeIfRequired();
/* 194 */       this.propertyPath.addParameterNode((String)this.methodParameterNames.get(index), index);
/*     */       
/* 196 */       return new ConstraintValidatorContextImpl.NodeBuilder(ConstraintValidatorContextImpl.this, this.messageTemplate, this.propertyPath, null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private void dropLeafNodeIfRequired()
/*     */     {
/* 205 */       if ((this.propertyPath.getLeafNode().getKind() == ElementKind.BEAN) || 
/* 206 */         (this.propertyPath.getLeafNode().getKind() == ElementKind.CROSS_PARAMETER)) {
/* 207 */         this.propertyPath = this.propertyPath.getPathWithoutLeafNode();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private class NodeBuilder extends ConstraintValidatorContextImpl.NodeBuilderBase implements ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderDefinedContext, ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeBuilderDefinedContext
/*     */   {
/*     */     private NodeBuilder(String template, PathImpl path)
/*     */     {
/* 216 */       super(template, path);
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext addNode(String name)
/*     */     {
/* 222 */       return addPropertyNode(name);
/*     */     }
/*     */     
/*     */     public ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext addPropertyNode(String name)
/*     */     {
/* 227 */       return new ConstraintValidatorContextImpl.DeferredNodeBuilder(ConstraintValidatorContextImpl.this, this.messageTemplate, this.propertyPath, name, null);
/*     */     }
/*     */     
/*     */     public ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeBuilderCustomizableContext addBeanNode()
/*     */     {
/* 232 */       return new ConstraintValidatorContextImpl.DeferredNodeBuilder(ConstraintValidatorContextImpl.this, this.messageTemplate, this.propertyPath, null, null);
/*     */     }
/*     */   }
/*     */   
/*     */   private class DeferredNodeBuilder extends ConstraintValidatorContextImpl.NodeBuilderBase implements ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext, ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeBuilderCustomizableContext, ConstraintValidatorContext.ConstraintViolationBuilder.NodeContextBuilder, ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeContextBuilder
/*     */   {
/*     */     private final String leafNodeName;
/*     */     
/*     */     private DeferredNodeBuilder(String template, PathImpl path, String nodeName)
/*     */     {
/* 242 */       super(template, path);
/* 243 */       this.leafNodeName = nodeName;
/*     */     }
/*     */     
/*     */     public DeferredNodeBuilder inIterable()
/*     */     {
/* 248 */       this.propertyPath.makeLeafNodeIterable();
/* 249 */       return this;
/*     */     }
/*     */     
/*     */     public ConstraintValidatorContextImpl.NodeBuilder atKey(Object key)
/*     */     {
/* 254 */       this.propertyPath.setLeafNodeMapKey(key);
/* 255 */       addLeafNode();
/* 256 */       return new ConstraintValidatorContextImpl.NodeBuilder(ConstraintValidatorContextImpl.this, this.messageTemplate, this.propertyPath, null);
/*     */     }
/*     */     
/*     */     public ConstraintValidatorContextImpl.NodeBuilder atIndex(Integer index)
/*     */     {
/* 261 */       this.propertyPath.setLeafNodeIndex(index);
/* 262 */       addLeafNode();
/* 263 */       return new ConstraintValidatorContextImpl.NodeBuilder(ConstraintValidatorContextImpl.this, this.messageTemplate, this.propertyPath, null);
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext addNode(String name)
/*     */     {
/* 269 */       return addPropertyNode(name);
/*     */     }
/*     */     
/*     */     public ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext addPropertyNode(String name)
/*     */     {
/* 274 */       addLeafNode();
/* 275 */       return new DeferredNodeBuilder(ConstraintValidatorContextImpl.this, this.messageTemplate, this.propertyPath, name);
/*     */     }
/*     */     
/*     */     public ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeBuilderCustomizableContext addBeanNode()
/*     */     {
/* 280 */       addLeafNode();
/* 281 */       return new DeferredNodeBuilder(ConstraintValidatorContextImpl.this, this.messageTemplate, this.propertyPath, null);
/*     */     }
/*     */     
/*     */     public ConstraintValidatorContext addConstraintViolation()
/*     */     {
/* 286 */       addLeafNode();
/* 287 */       return super.addConstraintViolation();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private void addLeafNode()
/*     */     {
/* 295 */       if (this.leafNodeName == null) {
/* 296 */         this.propertyPath.addBeanNode();
/*     */       }
/*     */       else {
/* 299 */         this.propertyPath.addPropertyNode(this.leafNodeName);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\constraintvalidation\ConstraintValidatorContextImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */