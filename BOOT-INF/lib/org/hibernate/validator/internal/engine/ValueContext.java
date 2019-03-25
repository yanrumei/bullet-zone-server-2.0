/*     */ package org.hibernate.validator.internal.engine;
/*     */ 
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.reflect.Type;
/*     */ import javax.validation.ElementKind;
/*     */ import javax.validation.groups.Default;
/*     */ import org.hibernate.validator.internal.engine.path.PathImpl;
/*     */ import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
/*     */ import org.hibernate.validator.internal.metadata.aggregated.ParameterMetaData;
/*     */ import org.hibernate.validator.internal.metadata.facets.Cascadable;
/*     */ import org.hibernate.validator.internal.metadata.facets.Validatable;
/*     */ import org.hibernate.validator.spi.valuehandling.ValidatedValueUnwrapper;
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
/*     */ public class ValueContext<T, V>
/*     */ {
/*     */   private final T currentBean;
/*     */   private final Class<T> currentBeanType;
/*     */   private PathImpl propertyPath;
/*     */   private Class<?> currentGroup;
/*     */   private V currentValue;
/*     */   private final Validatable currentValidatable;
/*     */   private ElementType elementType;
/*     */   private Type declaredTypeOfValidatedElement;
/*     */   private ValidatedValueUnwrapper<V> validatedValueHandler;
/*  75 */   private UnwrapMode unwrapMode = UnwrapMode.AUTOMATIC;
/*     */   
/*     */   public static <T, V> ValueContext<T, V> getLocalExecutionContext(T value, Validatable validatable, PathImpl propertyPath)
/*     */   {
/*  79 */     Class<T> rootBeanClass = value.getClass();
/*  80 */     return new ValueContext(value, rootBeanClass, validatable, propertyPath);
/*     */   }
/*     */   
/*     */   public static <T, V> ValueContext<T, V> getLocalExecutionContext(Class<T> type, Validatable validatable, PathImpl propertyPath) {
/*  84 */     return new ValueContext(null, type, validatable, propertyPath);
/*     */   }
/*     */   
/*     */   private ValueContext(T currentBean, Class<T> currentBeanType, Validatable validatable, PathImpl propertyPath) {
/*  88 */     this.currentBean = currentBean;
/*  89 */     this.currentBeanType = currentBeanType;
/*  90 */     this.currentValidatable = validatable;
/*  91 */     this.propertyPath = propertyPath;
/*     */   }
/*     */   
/*     */   public final PathImpl getPropertyPath() {
/*  95 */     return this.propertyPath;
/*     */   }
/*     */   
/*     */   public final Class<?> getCurrentGroup() {
/*  99 */     return this.currentGroup;
/*     */   }
/*     */   
/*     */   public final T getCurrentBean() {
/* 103 */     return (T)this.currentBean;
/*     */   }
/*     */   
/*     */   public final Class<T> getCurrentBeanType() {
/* 107 */     return this.currentBeanType;
/*     */   }
/*     */   
/*     */   public Validatable getCurrentValidatable() {
/* 111 */     return this.currentValidatable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Object getCurrentValidatedValue()
/*     */   {
/* 121 */     return this.validatedValueHandler != null ? this.validatedValueHandler.handleValidatedValue(this.currentValue) : this.currentValue;
/*     */   }
/*     */   
/*     */   public final void setPropertyPath(PathImpl propertyPath) {
/* 125 */     this.propertyPath = propertyPath;
/*     */   }
/*     */   
/*     */   public final void appendNode(Cascadable node) {
/* 129 */     this.propertyPath = PathImpl.createCopy(this.propertyPath);
/*     */     
/* 131 */     if (node.getKind() == ElementKind.PROPERTY) {
/* 132 */       this.propertyPath.addPropertyNode(node.getName());
/*     */     }
/* 134 */     else if (node.getKind() == ElementKind.PARAMETER) {
/* 135 */       this.propertyPath.addParameterNode(node.getName(), ((ParameterMetaData)node).getIndex());
/*     */     }
/* 137 */     else if (node.getKind() == ElementKind.RETURN_VALUE) {
/* 138 */       this.propertyPath.addReturnValueNode();
/*     */     }
/*     */   }
/*     */   
/*     */   public final void appendCollectionElementNode() {
/* 143 */     this.propertyPath = PathImpl.createCopy(this.propertyPath);
/* 144 */     this.propertyPath.addCollectionElementNode();
/*     */   }
/*     */   
/*     */   public final void appendBeanNode() {
/* 148 */     this.propertyPath = PathImpl.createCopy(this.propertyPath);
/* 149 */     this.propertyPath.addBeanNode();
/*     */   }
/*     */   
/*     */   public final void appendCrossParameterNode() {
/* 153 */     this.propertyPath = PathImpl.createCopy(this.propertyPath);
/* 154 */     this.propertyPath.addCrossParameterNode();
/*     */   }
/*     */   
/*     */   public final void markCurrentPropertyAsIterable() {
/* 158 */     this.propertyPath.makeLeafNodeIterable();
/*     */   }
/*     */   
/*     */   public final void setKey(Object key) {
/* 162 */     this.propertyPath.setLeafNodeMapKey(key);
/*     */   }
/*     */   
/*     */   public final void setIndex(Integer index) {
/* 166 */     this.propertyPath.setLeafNodeIndex(index);
/*     */   }
/*     */   
/*     */   public final void setCurrentGroup(Class<?> currentGroup) {
/* 170 */     this.currentGroup = currentGroup;
/*     */   }
/*     */   
/*     */   public final void setCurrentValidatedValue(V currentValue) {
/* 174 */     this.propertyPath.setLeafNodeValue(currentValue);
/* 175 */     this.currentValue = currentValue;
/*     */   }
/*     */   
/*     */   public final boolean validatingDefault() {
/* 179 */     return (getCurrentGroup() != null) && (getCurrentGroup().getName().equals(Default.class.getName()));
/*     */   }
/*     */   
/*     */   public final ElementType getElementType() {
/* 183 */     return this.elementType;
/*     */   }
/*     */   
/*     */   public final void setElementType(ElementType elementType) {
/* 187 */     this.elementType = elementType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Type getDeclaredTypeOfValidatedElement()
/*     */   {
/* 197 */     return this.declaredTypeOfValidatedElement;
/*     */   }
/*     */   
/*     */   public final void setDeclaredTypeOfValidatedElement(Type declaredTypeOfValidatedElement) {
/* 201 */     this.declaredTypeOfValidatedElement = declaredTypeOfValidatedElement;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 206 */     StringBuilder sb = new StringBuilder();
/* 207 */     sb.append("ValueContext");
/* 208 */     sb.append("{currentBean=").append(this.currentBean);
/* 209 */     sb.append(", currentBeanType=").append(this.currentBeanType);
/* 210 */     sb.append(", propertyPath=").append(this.propertyPath);
/* 211 */     sb.append(", currentGroup=").append(this.currentGroup);
/* 212 */     sb.append(", currentValue=").append(this.currentValue);
/* 213 */     sb.append(", elementType=").append(this.elementType);
/* 214 */     sb.append(", typeOfValidatedValue=").append(this.declaredTypeOfValidatedElement);
/* 215 */     sb.append('}');
/* 216 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public void setValidatedValueHandler(ValidatedValueUnwrapper<V> handler) {
/* 220 */     this.validatedValueHandler = handler;
/*     */   }
/*     */   
/*     */   public ValidatedValueUnwrapper<V> getValidatedValueHandler() {
/* 224 */     return this.validatedValueHandler;
/*     */   }
/*     */   
/*     */   public UnwrapMode getUnwrapMode() {
/* 228 */     return this.unwrapMode;
/*     */   }
/*     */   
/*     */   public void setUnwrapMode(UnwrapMode unwrapMode) {
/* 232 */     this.unwrapMode = unwrapMode;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\ValueContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */