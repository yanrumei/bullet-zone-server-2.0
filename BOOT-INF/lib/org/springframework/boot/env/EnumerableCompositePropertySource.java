/*    */ package org.springframework.boot.env;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.List;
/*    */ import org.springframework.core.env.EnumerablePropertySource;
/*    */ import org.springframework.core.env.PropertySource;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EnumerableCompositePropertySource
/*    */   extends EnumerablePropertySource<Collection<PropertySource<?>>>
/*    */ {
/*    */   private volatile String[] names;
/*    */   
/*    */   public EnumerableCompositePropertySource(String sourceName)
/*    */   {
/* 42 */     super(sourceName, new LinkedHashSet());
/*    */   }
/*    */   
/*    */   public Object getProperty(String name)
/*    */   {
/* 47 */     for (PropertySource<?> propertySource : (Collection)getSource()) {
/* 48 */       Object value = propertySource.getProperty(name);
/* 49 */       if (value != null) {
/* 50 */         return value;
/*    */       }
/*    */     }
/* 53 */     return null;
/*    */   }
/*    */   
/*    */   public String[] getPropertyNames()
/*    */   {
/* 58 */     String[] result = this.names;
/* 59 */     if (result == null) {
/* 60 */       List<String> names = new ArrayList();
/* 61 */       for (PropertySource<?> source : new ArrayList(
/* 62 */         (Collection)getSource())) {
/* 63 */         if ((source instanceof EnumerablePropertySource)) {
/* 64 */           names.addAll(Arrays.asList(((EnumerablePropertySource)source)
/* 65 */             .getPropertyNames()));
/*    */         }
/*    */       }
/* 68 */       this.names = ((String[])names.toArray(new String[0]));
/* 69 */       result = this.names;
/*    */     }
/* 71 */     return result;
/*    */   }
/*    */   
/*    */   public void add(PropertySource<?> source) {
/* 75 */     ((Collection)getSource()).add(source);
/* 76 */     this.names = null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\env\EnumerableCompositePropertySource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */