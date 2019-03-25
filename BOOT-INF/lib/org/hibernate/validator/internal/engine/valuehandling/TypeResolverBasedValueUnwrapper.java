/*    */ package org.hibernate.validator.internal.engine.valuehandling;
/*    */ 
/*    */ import com.fasterxml.classmate.ResolvedType;
/*    */ import com.fasterxml.classmate.TypeResolver;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.List;
/*    */ import org.hibernate.validator.internal.util.TypeResolutionHelper;
/*    */ import org.hibernate.validator.spi.valuehandling.ValidatedValueUnwrapper;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class TypeResolverBasedValueUnwrapper<T>
/*    */   extends ValidatedValueUnwrapper<T>
/*    */ {
/*    */   private final Class<?> clazz;
/*    */   private final TypeResolver typeResolver;
/*    */   
/*    */   TypeResolverBasedValueUnwrapper(TypeResolutionHelper typeResolutionHelper)
/*    */   {
/* 28 */     this.typeResolver = typeResolutionHelper.getTypeResolver();
/* 29 */     this.clazz = resolveSingleTypeParameter(this.typeResolver, getClass(), ValidatedValueUnwrapper.class);
/*    */   }
/*    */   
/*    */   public Type getValidatedValueType(Type valueType)
/*    */   {
/* 34 */     return resolveSingleTypeParameter(this.typeResolver, valueType, this.clazz);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private static Class<?> resolveSingleTypeParameter(TypeResolver typeResolver, Type subType, Class<?> target)
/*    */   {
/* 41 */     ResolvedType resolvedType = typeResolver.resolve(subType, new Type[0]);
/* 42 */     return ((ResolvedType)resolvedType.typeParametersFor(target).get(0)).getErasedType();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\valuehandling\TypeResolverBasedValueUnwrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */