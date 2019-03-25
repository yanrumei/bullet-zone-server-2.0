/*    */ package org.springframework.boot.autoconfigure.mustache;
/*    */ 
/*    */ import com.samskivert.mustache.DefaultCollector;
/*    */ import com.samskivert.mustache.Mustache.VariableFetcher;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.springframework.boot.bind.PropertySourcesPropertyValues;
/*    */ import org.springframework.boot.bind.RelaxedDataBinder;
/*    */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*    */ import org.springframework.context.EnvironmentAware;
/*    */ import org.springframework.core.env.ConfigurableEnvironment;
/*    */ import org.springframework.core.env.Environment;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MustacheEnvironmentCollector
/*    */   extends DefaultCollector
/*    */   implements EnvironmentAware
/*    */ {
/*    */   private ConfigurableEnvironment environment;
/*    */   private Map<String, Object> target;
/*    */   private RelaxedPropertyResolver propertyResolver;
/* 48 */   private final Mustache.VariableFetcher propertyFetcher = new PropertyVariableFetcher(null);
/*    */   
/*    */   public void setEnvironment(Environment environment)
/*    */   {
/* 52 */     this.environment = ((ConfigurableEnvironment)environment);
/* 53 */     this.target = new HashMap();
/* 54 */     new RelaxedDataBinder(this.target).bind(new PropertySourcesPropertyValues(this.environment
/* 55 */       .getPropertySources()));
/* 56 */     this.propertyResolver = new RelaxedPropertyResolver(environment);
/*    */   }
/*    */   
/*    */   public Mustache.VariableFetcher createFetcher(Object ctx, String name)
/*    */   {
/* 61 */     Mustache.VariableFetcher fetcher = super.createFetcher(ctx, name);
/* 62 */     if (fetcher != null) {
/* 63 */       return fetcher;
/*    */     }
/* 65 */     if (this.propertyResolver.containsProperty(name)) {
/* 66 */       return this.propertyFetcher;
/*    */     }
/* 68 */     return null;
/*    */   }
/*    */   
/*    */   private class PropertyVariableFetcher implements Mustache.VariableFetcher {
/*    */     private PropertyVariableFetcher() {}
/*    */     
/*    */     public Object get(Object ctx, String name) throws Exception {
/* 75 */       return MustacheEnvironmentCollector.this.propertyResolver.getProperty(name);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\mustache\MustacheEnvironmentCollector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */