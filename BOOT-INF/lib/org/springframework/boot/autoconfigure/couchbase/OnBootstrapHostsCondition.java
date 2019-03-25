/*    */ package org.springframework.boot.autoconfigure.couchbase;
/*    */ 
/*    */ import java.util.AbstractMap.SimpleEntry;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionMessage;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.Builder;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.ItemsBuilder;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
/*    */ import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
/*    */ import org.springframework.boot.bind.PropertySourcesPropertyValues;
/*    */ import org.springframework.boot.bind.RelaxedDataBinder;
/*    */ import org.springframework.boot.bind.RelaxedNames;
/*    */ import org.springframework.context.annotation.ConditionContext;
/*    */ import org.springframework.core.env.ConfigurableEnvironment;
/*    */ import org.springframework.core.env.Environment;
/*    */ import org.springframework.core.env.PropertySources;
/*    */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*    */ import org.springframework.validation.DataBinder;
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
/*    */ class OnBootstrapHostsCondition
/*    */   extends SpringBootCondition
/*    */ {
/*    */   public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*    */   {
/* 46 */     Environment environment = context.getEnvironment();
/*    */     
/* 48 */     PropertyResolver resolver = new PropertyResolver(((ConfigurableEnvironment)environment).getPropertySources(), "spring.couchbase");
/*    */     
/* 50 */     Map.Entry<String, Object> entry = resolver.resolveProperty("bootstrap-hosts");
/* 51 */     if (entry != null) {
/* 52 */       return ConditionOutcome.match(
/* 53 */         ConditionMessage.forCondition(OnBootstrapHostsCondition.class.getName(), new Object[0])
/* 54 */         .found("property").items(new Object[] { "spring.couchbase.bootstrap-hosts" }));
/*    */     }
/* 56 */     return ConditionOutcome.noMatch(
/* 57 */       ConditionMessage.forCondition(OnBootstrapHostsCondition.class.getName(), new Object[0])
/* 58 */       .didNotFind("property").items(new Object[] { "spring.couchbase.bootstrap-hosts" }));
/*    */   }
/*    */   
/*    */   private static class PropertyResolver
/*    */   {
/*    */     private final String prefix;
/*    */     private final Map<String, Object> content;
/*    */     
/*    */     PropertyResolver(PropertySources propertySources, String prefix)
/*    */     {
/* 68 */       this.prefix = prefix;
/* 69 */       this.content = new HashMap();
/* 70 */       DataBinder binder = new RelaxedDataBinder(this.content, this.prefix);
/* 71 */       binder.bind(new PropertySourcesPropertyValues(propertySources));
/*    */     }
/*    */     
/*    */     Map.Entry<String, Object> resolveProperty(String name) {
/* 75 */       RelaxedNames prefixes = new RelaxedNames(this.prefix);
/* 76 */       RelaxedNames keys = new RelaxedNames(name);
/* 77 */       for (Iterator localIterator1 = prefixes.iterator(); localIterator1.hasNext();) { prefix = (String)localIterator1.next();
/* 78 */         for (String relaxedKey : keys) {
/* 79 */           String key = prefix + relaxedKey;
/* 80 */           if (this.content.containsKey(relaxedKey))
/* 81 */             return new AbstractMap.SimpleEntry(key, this.content
/* 82 */               .get(relaxedKey));
/*    */         }
/*    */       }
/*    */       String prefix;
/* 86 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\couchbase\OnBootstrapHostsCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */