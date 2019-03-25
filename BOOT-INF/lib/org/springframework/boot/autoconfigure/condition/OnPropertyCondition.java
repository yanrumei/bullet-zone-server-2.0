/*     */ package org.springframework.boot.autoconfigure.condition;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*     */ import org.springframework.context.annotation.ConditionContext;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.annotation.Order;
/*     */ import org.springframework.core.env.PropertyResolver;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ @Order(-2147483608)
/*     */ class OnPropertyCondition
/*     */   extends SpringBootCondition
/*     */ {
/*     */   public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */   {
/*  54 */     List<AnnotationAttributes> allAnnotationAttributes = annotationAttributesFromMultiValueMap(metadata
/*  55 */       .getAllAnnotationAttributes(ConditionalOnProperty.class
/*  56 */       .getName()));
/*  57 */     List<ConditionMessage> noMatch = new ArrayList();
/*  58 */     List<ConditionMessage> match = new ArrayList();
/*  59 */     for (AnnotationAttributes annotationAttributes : allAnnotationAttributes) {
/*  60 */       ConditionOutcome outcome = determineOutcome(annotationAttributes, context
/*  61 */         .getEnvironment());
/*  62 */       (outcome.isMatch() ? match : noMatch).add(outcome.getConditionMessage());
/*     */     }
/*  64 */     if (!noMatch.isEmpty()) {
/*  65 */       return ConditionOutcome.noMatch(ConditionMessage.of(noMatch));
/*     */     }
/*  67 */     return ConditionOutcome.match(ConditionMessage.of(match));
/*     */   }
/*     */   
/*     */   private List<AnnotationAttributes> annotationAttributesFromMultiValueMap(MultiValueMap<String, Object> multiValueMap)
/*     */   {
/*  72 */     List<Map<String, Object>> maps = new ArrayList();
/*  73 */     for (Iterator localIterator = multiValueMap.entrySet().iterator(); localIterator.hasNext();) { entry = (Map.Entry)localIterator.next();
/*  74 */       for (int i = 0; i < ((List)entry.getValue()).size(); i++) { Map<String, Object> map;
/*     */         Map<String, Object> map;
/*  76 */         if (i < maps.size()) {
/*  77 */           map = (Map)maps.get(i);
/*     */         }
/*     */         else {
/*  80 */           map = new HashMap();
/*  81 */           maps.add(map);
/*     */         }
/*  83 */         map.put(entry.getKey(), ((List)entry.getValue()).get(i));
/*     */       }
/*     */     }
/*     */     Map.Entry<String, List<Object>> entry;
/*  87 */     Object annotationAttributes = new ArrayList(maps.size());
/*  88 */     for (Map<String, Object> map : maps) {
/*  89 */       ((List)annotationAttributes).add(AnnotationAttributes.fromMap(map));
/*     */     }
/*  91 */     return (List<AnnotationAttributes>)annotationAttributes;
/*     */   }
/*     */   
/*     */   private ConditionOutcome determineOutcome(AnnotationAttributes annotationAttributes, PropertyResolver resolver)
/*     */   {
/*  96 */     Spec spec = new Spec(annotationAttributes);
/*  97 */     List<String> missingProperties = new ArrayList();
/*  98 */     List<String> nonMatchingProperties = new ArrayList();
/*  99 */     spec.collectProperties(resolver, missingProperties, nonMatchingProperties);
/* 100 */     if (!missingProperties.isEmpty()) {
/* 101 */       return ConditionOutcome.noMatch(
/* 102 */         ConditionMessage.forCondition(ConditionalOnProperty.class, new Object[] { spec })
/* 103 */         .didNotFind("property", "properties")
/* 104 */         .items(ConditionMessage.Style.QUOTE, missingProperties));
/*     */     }
/* 106 */     if (!nonMatchingProperties.isEmpty()) {
/* 107 */       return ConditionOutcome.noMatch(
/* 108 */         ConditionMessage.forCondition(ConditionalOnProperty.class, new Object[] { spec })
/* 109 */         .found("different value in property", "different value in properties")
/*     */         
/* 111 */         .items(ConditionMessage.Style.QUOTE, nonMatchingProperties));
/*     */     }
/* 113 */     return ConditionOutcome.match(
/* 114 */       ConditionMessage.forCondition(ConditionalOnProperty.class, new Object[] { spec }).because("matched"));
/*     */   }
/*     */   
/*     */ 
/*     */   private static class Spec
/*     */   {
/*     */     private final String prefix;
/*     */     
/*     */     private final String havingValue;
/*     */     
/*     */     private final String[] names;
/*     */     private final boolean relaxedNames;
/*     */     private final boolean matchIfMissing;
/*     */     
/*     */     Spec(AnnotationAttributes annotationAttributes)
/*     */     {
/* 130 */       String prefix = annotationAttributes.getString("prefix").trim();
/* 131 */       if ((StringUtils.hasText(prefix)) && (!prefix.endsWith("."))) {
/* 132 */         prefix = prefix + ".";
/*     */       }
/* 134 */       this.prefix = prefix;
/* 135 */       this.havingValue = annotationAttributes.getString("havingValue");
/* 136 */       this.names = getNames(annotationAttributes);
/* 137 */       this.relaxedNames = annotationAttributes.getBoolean("relaxedNames");
/* 138 */       this.matchIfMissing = annotationAttributes.getBoolean("matchIfMissing");
/*     */     }
/*     */     
/*     */     private String[] getNames(Map<String, Object> annotationAttributes) {
/* 142 */       String[] value = (String[])annotationAttributes.get("value");
/* 143 */       String[] name = (String[])annotationAttributes.get("name");
/* 144 */       Assert.state((value.length > 0) || (name.length > 0), "The name or value attribute of @ConditionalOnProperty must be specified");
/*     */       
/* 146 */       Assert.state((value.length == 0) || (name.length == 0), "The name and value attributes of @ConditionalOnProperty are exclusive");
/*     */       
/* 148 */       return value.length > 0 ? value : name;
/*     */     }
/*     */     
/*     */     private void collectProperties(PropertyResolver resolver, List<String> missing, List<String> nonMatching)
/*     */     {
/* 153 */       if (this.relaxedNames) {
/* 154 */         resolver = new RelaxedPropertyResolver(resolver, this.prefix);
/*     */       }
/* 156 */       for (String name : this.names) {
/* 157 */         String key = this.prefix + name;
/* 158 */         if (resolver.containsProperty(key)) {
/* 159 */           if (!isMatch(resolver.getProperty(key), this.havingValue)) {
/* 160 */             nonMatching.add(name);
/*     */           }
/*     */           
/*     */         }
/* 164 */         else if (!this.matchIfMissing) {
/* 165 */           missing.add(name);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private boolean isMatch(String value, String requiredValue)
/*     */     {
/* 172 */       if (StringUtils.hasLength(requiredValue)) {
/* 173 */         return requiredValue.equalsIgnoreCase(value);
/*     */       }
/* 175 */       return !"false".equalsIgnoreCase(value);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 180 */       StringBuilder result = new StringBuilder();
/* 181 */       result.append("(");
/* 182 */       result.append(this.prefix);
/* 183 */       if (this.names.length == 1) {
/* 184 */         result.append(this.names[0]);
/*     */       }
/*     */       else {
/* 187 */         result.append("[");
/* 188 */         result.append(StringUtils.arrayToCommaDelimitedString(this.names));
/* 189 */         result.append("]");
/*     */       }
/* 191 */       if (StringUtils.hasLength(this.havingValue)) {
/* 192 */         result.append("=").append(this.havingValue);
/*     */       }
/* 194 */       result.append(")");
/* 195 */       return result.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\condition\OnPropertyCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */