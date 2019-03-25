/*     */ package org.springframework.boot.autoconfigure;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.classreading.MetadataReader;
/*     */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*     */ import org.springframework.util.Assert;
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
/*     */ class AutoConfigurationSorter
/*     */ {
/*     */   private final MetadataReaderFactory metadataReaderFactory;
/*     */   private final AutoConfigurationMetadata autoConfigurationMetadata;
/*     */   
/*     */   AutoConfigurationSorter(MetadataReaderFactory metadataReaderFactory, AutoConfigurationMetadata autoConfigurationMetadata)
/*     */   {
/*  51 */     Assert.notNull(metadataReaderFactory, "MetadataReaderFactory must not be null");
/*  52 */     this.metadataReaderFactory = metadataReaderFactory;
/*  53 */     this.autoConfigurationMetadata = autoConfigurationMetadata;
/*     */   }
/*     */   
/*     */   public List<String> getInPriorityOrder(Collection<String> classNames) {
/*  57 */     final AutoConfigurationClasses classes = new AutoConfigurationClasses(this.metadataReaderFactory, this.autoConfigurationMetadata, classNames);
/*     */     
/*  59 */     List<String> orderedClassNames = new ArrayList(classNames);
/*     */     
/*  61 */     Collections.sort(orderedClassNames);
/*     */     
/*  63 */     Collections.sort(orderedClassNames, new Comparator()
/*     */     {
/*     */       public int compare(String o1, String o2)
/*     */       {
/*  67 */         int i1 = classes.get(o1).getOrder();
/*  68 */         int i2 = classes.get(o2).getOrder();
/*  69 */         return i1 > i2 ? 1 : i1 < i2 ? -1 : 0;
/*     */       }
/*     */       
/*     */ 
/*  73 */     });
/*  74 */     orderedClassNames = sortByAnnotation(classes, orderedClassNames);
/*  75 */     return orderedClassNames;
/*     */   }
/*     */   
/*     */   private List<String> sortByAnnotation(AutoConfigurationClasses classes, List<String> classNames)
/*     */   {
/*  80 */     List<String> toSort = new ArrayList(classNames);
/*  81 */     Set<String> sorted = new LinkedHashSet();
/*  82 */     Set<String> processing = new LinkedHashSet();
/*  83 */     while (!toSort.isEmpty()) {
/*  84 */       doSortByAfterAnnotation(classes, toSort, sorted, processing, null);
/*     */     }
/*  86 */     return new ArrayList(sorted);
/*     */   }
/*     */   
/*     */ 
/*     */   private void doSortByAfterAnnotation(AutoConfigurationClasses classes, List<String> toSort, Set<String> sorted, Set<String> processing, String current)
/*     */   {
/*  92 */     if (current == null) {
/*  93 */       current = (String)toSort.remove(0);
/*     */     }
/*  95 */     processing.add(current);
/*  96 */     for (String after : classes.getClassesRequestedAfter(current)) {
/*  97 */       Assert.state(!processing.contains(after), "AutoConfigure cycle detected between " + current + " and " + after);
/*     */       
/*  99 */       if ((!sorted.contains(after)) && (toSort.contains(after))) {
/* 100 */         doSortByAfterAnnotation(classes, toSort, sorted, processing, after);
/*     */       }
/*     */     }
/* 103 */     processing.remove(current);
/* 104 */     sorted.add(current);
/*     */   }
/*     */   
/*     */   private static class AutoConfigurationClasses
/*     */   {
/* 109 */     private final Map<String, AutoConfigurationSorter.AutoConfigurationClass> classes = new HashMap();
/*     */     
/*     */ 
/*     */     AutoConfigurationClasses(MetadataReaderFactory metadataReaderFactory, AutoConfigurationMetadata autoConfigurationMetadata, Collection<String> classNames)
/*     */     {
/* 114 */       for (String className : classNames) {
/* 115 */         this.classes.put(className, new AutoConfigurationSorter.AutoConfigurationClass(className, metadataReaderFactory, autoConfigurationMetadata));
/*     */       }
/*     */     }
/*     */     
/*     */     public AutoConfigurationSorter.AutoConfigurationClass get(String className)
/*     */     {
/* 121 */       return (AutoConfigurationSorter.AutoConfigurationClass)this.classes.get(className);
/*     */     }
/*     */     
/*     */     public Set<String> getClassesRequestedAfter(String className) {
/* 125 */       Set<String> rtn = new LinkedHashSet();
/* 126 */       rtn.addAll(get(className).getAfter());
/* 127 */       for (Map.Entry<String, AutoConfigurationSorter.AutoConfigurationClass> entry : this.classes
/* 128 */         .entrySet()) {
/* 129 */         if (((AutoConfigurationSorter.AutoConfigurationClass)entry.getValue()).getBefore().contains(className)) {
/* 130 */           rtn.add(entry.getKey());
/*     */         }
/*     */       }
/* 133 */       return rtn;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class AutoConfigurationClass
/*     */   {
/*     */     private final String className;
/*     */     
/*     */     private final MetadataReaderFactory metadataReaderFactory;
/*     */     
/*     */     private final AutoConfigurationMetadata autoConfigurationMetadata;
/*     */     
/*     */     private AnnotationMetadata annotationMetadata;
/*     */     
/*     */     private final Set<String> before;
/*     */     
/*     */     private final Set<String> after;
/*     */     
/*     */ 
/*     */     AutoConfigurationClass(String className, MetadataReaderFactory metadataReaderFactory, AutoConfigurationMetadata autoConfigurationMetadata)
/*     */     {
/* 155 */       this.className = className;
/* 156 */       this.metadataReaderFactory = metadataReaderFactory;
/* 157 */       this.autoConfigurationMetadata = autoConfigurationMetadata;
/* 158 */       this.before = readBefore();
/* 159 */       this.after = readAfter();
/*     */     }
/*     */     
/*     */     public Set<String> getBefore() {
/* 163 */       return this.before;
/*     */     }
/*     */     
/*     */     public Set<String> getAfter() {
/* 167 */       return this.after;
/*     */     }
/*     */     
/*     */     private int getOrder() {
/* 171 */       if (this.autoConfigurationMetadata.wasProcessed(this.className)) {
/* 172 */         return this.autoConfigurationMetadata.getInteger(this.className, "AutoConfigureOrder", 
/* 173 */           Integer.valueOf(Integer.MAX_VALUE)).intValue();
/*     */       }
/*     */       
/*     */ 
/* 176 */       Map<String, Object> attributes = getAnnotationMetadata().getAnnotationAttributes(AutoConfigureOrder.class.getName());
/* 177 */       return attributes == null ? Integer.MAX_VALUE : 
/* 178 */         ((Integer)attributes.get("value")).intValue();
/*     */     }
/*     */     
/*     */     private Set<String> readBefore() {
/* 182 */       if (this.autoConfigurationMetadata.wasProcessed(this.className)) {
/* 183 */         return this.autoConfigurationMetadata.getSet(this.className, "AutoConfigureBefore", 
/* 184 */           Collections.emptySet());
/*     */       }
/* 186 */       return getAnnotationValue(AutoConfigureBefore.class);
/*     */     }
/*     */     
/*     */     private Set<String> readAfter() {
/* 190 */       if (this.autoConfigurationMetadata.wasProcessed(this.className)) {
/* 191 */         return this.autoConfigurationMetadata.getSet(this.className, "AutoConfigureAfter", 
/* 192 */           Collections.emptySet());
/*     */       }
/* 194 */       return getAnnotationValue(AutoConfigureAfter.class);
/*     */     }
/*     */     
/*     */     private Set<String> getAnnotationValue(Class<?> annotation)
/*     */     {
/* 199 */       Map<String, Object> attributes = getAnnotationMetadata().getAnnotationAttributes(annotation.getName(), true);
/* 200 */       if (attributes == null) {
/* 201 */         return Collections.emptySet();
/*     */       }
/* 203 */       Set<String> value = new LinkedHashSet();
/* 204 */       Collections.addAll(value, (String[])attributes.get("value"));
/* 205 */       Collections.addAll(value, (String[])attributes.get("name"));
/* 206 */       return value;
/*     */     }
/*     */     
/*     */     private AnnotationMetadata getAnnotationMetadata() {
/* 210 */       if (this.annotationMetadata == null) {
/*     */         try
/*     */         {
/* 213 */           MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(this.className);
/* 214 */           this.annotationMetadata = metadataReader.getAnnotationMetadata();
/*     */         }
/*     */         catch (IOException ex) {
/* 217 */           throw new IllegalStateException("Unable to read meta-data for class " + this.className, ex);
/*     */         }
/*     */       }
/*     */       
/* 221 */       return this.annotationMetadata;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\AutoConfigurationSorter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */