/*    */ package org.hibernate.validator.parameternameprovider;
/*    */ 
/*    */ import com.thoughtworks.paranamer.AdaptiveParanamer;
/*    */ import com.thoughtworks.paranamer.CachingParanamer;
/*    */ import com.thoughtworks.paranamer.Paranamer;
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import javax.validation.ParameterNameProvider;
/*    */ import org.hibernate.validator.internal.engine.DefaultParameterNameProvider;
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
/*    */ public class ParanamerParameterNameProvider
/*    */   implements ParameterNameProvider
/*    */ {
/*    */   private final ParameterNameProvider fallBackProvider;
/*    */   private final Paranamer paranamer;
/*    */   
/*    */   public ParanamerParameterNameProvider()
/*    */   {
/* 41 */     this(null);
/*    */   }
/*    */   
/*    */   public ParanamerParameterNameProvider(Paranamer paranamer) {
/* 45 */     this.paranamer = (paranamer != null ? paranamer : new CachingParanamer(new AdaptiveParanamer()));
/* 46 */     this.fallBackProvider = new DefaultParameterNameProvider();
/*    */   }
/*    */   
/*    */ 
/*    */   public List<String> getParameterNames(Constructor<?> constructor)
/*    */   {
/*    */     String[] parameterNames;
/*    */     
/* 54 */     synchronized (this.paranamer) {
/* 55 */       parameterNames = this.paranamer.lookupParameterNames(constructor, false);
/*    */     }
/*    */     
/*    */     String[] parameterNames;
/* 59 */     if ((parameterNames != null) && (parameterNames.length == constructor.getParameterTypes().length)) {
/* 60 */       return Arrays.asList(parameterNames);
/*    */     }
/*    */     
/* 63 */     return this.fallBackProvider.getParameterNames(constructor);
/*    */   }
/*    */   
/*    */ 
/*    */   public List<String> getParameterNames(Method method)
/*    */   {
/*    */     String[] parameterNames;
/* 70 */     synchronized (this.paranamer) {
/* 71 */       parameterNames = this.paranamer.lookupParameterNames(method, false);
/*    */     }
/*    */     String[] parameterNames;
/* 74 */     if ((parameterNames != null) && (parameterNames.length == method.getParameterTypes().length)) {
/* 75 */       return Arrays.asList(parameterNames);
/*    */     }
/*    */     
/* 78 */     return this.fallBackProvider.getParameterNames(method);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\parameternameprovider\ParanamerParameterNameProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */