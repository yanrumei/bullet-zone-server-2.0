/*     */ package org.hibernate.validator.internal.metadata.aggregated;
/*     */ 
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.validation.ElementKind;
/*     */ import javax.validation.metadata.GroupConversionDescriptor;
/*     */ import org.hibernate.validator.HibernateValidatorPermission;
/*     */ import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
/*     */ import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
/*     */ import org.hibernate.validator.internal.metadata.core.MetaConstraint;
/*     */ import org.hibernate.validator.internal.metadata.descriptor.PropertyDescriptorImpl;
/*     */ import org.hibernate.validator.internal.metadata.facets.Cascadable;
/*     */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedElement.ConstrainedElementKind;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedField;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedType;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.ReflectionHelper;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredField;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredMethod;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.SetAccessibility;
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
/*     */ public class PropertyMetaData
/*     */   extends AbstractConstraintMetaData
/*     */   implements Cascadable
/*     */ {
/*  62 */   private static final Log log = ;
/*     */   
/*     */ 
/*     */ 
/*     */   private final Member cascadingMember;
/*     */   
/*     */ 
/*     */ 
/*     */   private final Type cascadableType;
/*     */   
/*     */ 
/*     */ 
/*     */   private final ElementType elementType;
/*     */   
/*     */ 
/*     */ 
/*     */   private final GroupConversionHelper groupConversionHelper;
/*     */   
/*     */ 
/*     */ 
/*     */   private final Set<MetaConstraint<?>> typeArgumentsConstraints;
/*     */   
/*     */ 
/*     */ 
/*     */   private PropertyMetaData(String propertyName, Type type, Set<MetaConstraint<?>> constraints, Set<MetaConstraint<?>> typeArgumentsConstraints, Map<Class<?>, Class<?>> groupConversions, Member cascadingMember, UnwrapMode unwrapMode)
/*     */   {
/*  88 */     super(propertyName, type, constraints, ElementKind.PROPERTY, cascadingMember != null, (cascadingMember != null) || 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  94 */       (!constraints.isEmpty()) || (!typeArgumentsConstraints.isEmpty()), unwrapMode);
/*     */     
/*     */ 
/*     */ 
/*  98 */     if (cascadingMember != null) {
/*  99 */       this.cascadingMember = getAccessible(cascadingMember);
/* 100 */       this.cascadableType = ReflectionHelper.typeOf(cascadingMember);
/* 101 */       this.elementType = ((cascadingMember instanceof Field) ? ElementType.FIELD : ElementType.METHOD);
/*     */     }
/*     */     else {
/* 104 */       this.cascadingMember = null;
/* 105 */       this.cascadableType = null;
/* 106 */       this.elementType = ElementType.TYPE;
/*     */     }
/*     */     
/* 109 */     this.typeArgumentsConstraints = Collections.unmodifiableSet(typeArgumentsConstraints);
/* 110 */     this.groupConversionHelper = new GroupConversionHelper(groupConversions);
/* 111 */     this.groupConversionHelper.validateGroupConversions(isCascading(), toString());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Member getAccessible(Member original)
/*     */   {
/* 119 */     if (((AccessibleObject)original).isAccessible()) {
/* 120 */       return original;
/*     */     }
/*     */     
/* 123 */     SecurityManager sm = System.getSecurityManager();
/* 124 */     if (sm != null) {
/* 125 */       sm.checkPermission(HibernateValidatorPermission.ACCESS_PRIVATE_MEMBERS);
/*     */     }
/*     */     
/* 128 */     Class<?> clazz = original.getDeclaringClass();
/*     */     Member member;
/*     */     Member member;
/* 131 */     if ((original instanceof Field)) {
/* 132 */       member = (Member)run(GetDeclaredField.action(clazz, original.getName()));
/*     */     }
/*     */     else {
/* 135 */       member = (Member)run(GetDeclaredMethod.action(clazz, original.getName(), new Class[0]));
/*     */     }
/*     */     
/* 138 */     run(SetAccessibility.action(member));
/*     */     
/* 140 */     return member;
/*     */   }
/*     */   
/*     */   public ElementType getElementType()
/*     */   {
/* 145 */     return this.elementType;
/*     */   }
/*     */   
/*     */   public Class<?> convertGroup(Class<?> from)
/*     */   {
/* 150 */     return this.groupConversionHelper.convertGroup(from);
/*     */   }
/*     */   
/*     */   public Set<GroupConversionDescriptor> getGroupConversionDescriptors()
/*     */   {
/* 155 */     return this.groupConversionHelper.asDescriptors();
/*     */   }
/*     */   
/*     */   public Set<MetaConstraint<?>> getTypeArgumentsConstraints()
/*     */   {
/* 160 */     return this.typeArgumentsConstraints;
/*     */   }
/*     */   
/*     */   public PropertyDescriptorImpl asDescriptor(boolean defaultGroupSequenceRedefined, List<Class<?>> defaultGroupSequence)
/*     */   {
/* 165 */     return new PropertyDescriptorImpl(
/* 166 */       getType(), 
/* 167 */       getName(), 
/* 168 */       asDescriptors(getConstraints()), 
/* 169 */       isCascading(), defaultGroupSequenceRedefined, defaultGroupSequence, 
/*     */       
/*     */ 
/* 172 */       getGroupConversionDescriptors());
/*     */   }
/*     */   
/*     */ 
/*     */   public Object getValue(Object parent)
/*     */   {
/* 178 */     if (this.elementType == ElementType.METHOD) {
/* 179 */       return ReflectionHelper.getValue((Method)this.cascadingMember, parent);
/*     */     }
/*     */     
/* 182 */     return ReflectionHelper.getValue((Field)this.cascadingMember, parent);
/*     */   }
/*     */   
/*     */ 
/*     */   public Type getCascadableType()
/*     */   {
/* 188 */     return this.cascadableType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static <T> T run(PrivilegedAction<T> action)
/*     */   {
/* 198 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 203 */     return 
/* 204 */       "PropertyMetaData [type=" + getType() + ", propertyName=" + getName() + ", cascadingMember=[" + this.cascadingMember + "]]";
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 209 */     return super.hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 214 */     if (this == obj) {
/* 215 */       return true;
/*     */     }
/* 217 */     if (!super.equals(obj)) {
/* 218 */       return false;
/*     */     }
/* 220 */     if (getClass() != obj.getClass()) {
/* 221 */       return false;
/*     */     }
/* 223 */     return true;
/*     */   }
/*     */   
/*     */   public static class Builder extends MetaDataBuilder
/*     */   {
/* 228 */     private static final EnumSet<ConstrainedElement.ConstrainedElementKind> SUPPORTED_ELEMENT_KINDS = EnumSet.of(ConstrainedElement.ConstrainedElementKind.TYPE, ConstrainedElement.ConstrainedElementKind.FIELD, ConstrainedElement.ConstrainedElementKind.METHOD);
/*     */     
/*     */ 
/*     */     private final String propertyName;
/*     */     
/*     */     private final Type propertyType;
/*     */     
/*     */     private Member cascadingMember;
/*     */     
/* 237 */     private final Set<MetaConstraint<?>> typeArgumentsConstraints = CollectionHelper.newHashSet();
/* 238 */     private UnwrapMode unwrapMode = UnwrapMode.AUTOMATIC;
/* 239 */     private boolean unwrapModeExplicitlyConfigured = false;
/*     */     
/*     */     public Builder(Class<?> beanClass, ConstrainedField constrainedField, ConstraintHelper constraintHelper) {
/* 242 */       super(constraintHelper);
/*     */       
/* 244 */       this.propertyName = ReflectionHelper.getPropertyName(constrainedField.getLocation().getMember());
/* 245 */       this.propertyType = ReflectionHelper.typeOf(constrainedField.getLocation().getMember());
/* 246 */       add(constrainedField);
/*     */     }
/*     */     
/*     */     public Builder(Class<?> beanClass, ConstrainedType constrainedType, ConstraintHelper constraintHelper) {
/* 250 */       super(constraintHelper);
/*     */       
/* 252 */       this.propertyName = null;
/* 253 */       this.propertyType = null;
/* 254 */       add(constrainedType);
/*     */     }
/*     */     
/*     */     public Builder(Class<?> beanClass, ConstrainedExecutable constrainedMethod, ConstraintHelper constraintHelper) {
/* 258 */       super(constraintHelper);
/*     */       
/* 260 */       this.propertyName = ReflectionHelper.getPropertyName(constrainedMethod.getLocation().getMember());
/* 261 */       this.propertyType = ReflectionHelper.typeOf(constrainedMethod.getLocation().getMember());
/* 262 */       add(constrainedMethod);
/*     */     }
/*     */     
/*     */     public boolean accepts(ConstrainedElement constrainedElement)
/*     */     {
/* 267 */       if (!SUPPORTED_ELEMENT_KINDS.contains(constrainedElement.getKind())) {
/* 268 */         return false;
/*     */       }
/*     */       
/* 271 */       if ((constrainedElement.getKind() == ConstrainedElement.ConstrainedElementKind.METHOD) && 
/* 272 */         (!((ConstrainedExecutable)constrainedElement).isGetterMethod())) {
/* 273 */         return false;
/*     */       }
/*     */       
/* 276 */       return equals(
/* 277 */         ReflectionHelper.getPropertyName(constrainedElement.getLocation().getMember()), this.propertyName);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public final void add(ConstrainedElement constrainedElement)
/*     */     {
/* 284 */       super.add(constrainedElement);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 290 */       UnwrapMode newUnwrapMode = constrainedElement.unwrapMode();
/* 291 */       if (this.unwrapModeExplicitlyConfigured) {
/* 292 */         if ((!UnwrapMode.AUTOMATIC.equals(newUnwrapMode)) && (!newUnwrapMode.equals(this.unwrapMode))) {
/* 293 */           throw PropertyMetaData.log.getInconsistentValueUnwrappingConfigurationBetweenFieldAndItsGetterException(this.propertyName, 
/*     */           
/* 295 */             getBeanClass().getName());
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 300 */       else if (!UnwrapMode.AUTOMATIC.equals(newUnwrapMode)) {
/* 301 */         this.unwrapMode = constrainedElement.unwrapMode();
/* 302 */         this.unwrapModeExplicitlyConfigured = true;
/*     */       }
/*     */       
/*     */ 
/* 306 */       if (constrainedElement.getKind() == ConstrainedElement.ConstrainedElementKind.FIELD) {
/* 307 */         this.typeArgumentsConstraints.addAll(((ConstrainedField)constrainedElement).getTypeArgumentsConstraints());
/*     */       }
/* 309 */       else if (constrainedElement.getKind() == ConstrainedElement.ConstrainedElementKind.METHOD) {
/* 310 */         this.typeArgumentsConstraints.addAll(((ConstrainedExecutable)constrainedElement).getTypeArgumentsConstraints());
/*     */       }
/*     */       
/* 313 */       if ((constrainedElement.isCascading()) && (this.cascadingMember == null)) {
/* 314 */         this.cascadingMember = constrainedElement.getLocation().getMember();
/*     */       }
/*     */     }
/*     */     
/*     */     public UnwrapMode unwrapMode()
/*     */     {
/* 320 */       return this.unwrapMode;
/*     */     }
/*     */     
/*     */     public PropertyMetaData build()
/*     */     {
/* 325 */       return new PropertyMetaData(this.propertyName, this.propertyType, 
/*     */       
/*     */ 
/* 328 */         adaptOriginsAndImplicitGroups(getConstraints()), this.typeArgumentsConstraints, 
/*     */         
/* 330 */         getGroupConversions(), this.cascadingMember, 
/*     */         
/* 332 */         unwrapMode(), null);
/*     */     }
/*     */     
/*     */     private boolean equals(String s1, String s2)
/*     */     {
/* 337 */       return ((s1 != null) && (s1.equals(s2))) || ((s1 == null) && (s2 == null));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\aggregated\PropertyMetaData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */