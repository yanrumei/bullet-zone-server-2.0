/*     */ package org.springframework.boot.env;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.regex.Pattern;
/*     */ import org.springframework.beans.factory.config.YamlProcessor;
/*     */ import org.springframework.beans.factory.config.YamlProcessor.DocumentMatcher;
/*     */ import org.springframework.beans.factory.config.YamlProcessor.MatchCallback;
/*     */ import org.springframework.beans.factory.config.YamlProcessor.StrictMapAppenderConstructor;
/*     */ import org.springframework.boot.yaml.SpringProfileDocumentMatcher;
/*     */ import org.springframework.core.env.MapPropertySource;
/*     */ import org.springframework.core.env.PropertySource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.Yaml;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ import org.yaml.snakeyaml.representer.Representer;
/*     */ import org.yaml.snakeyaml.resolver.Resolver;
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
/*     */ public class YamlPropertySourceLoader
/*     */   implements PropertySourceLoader
/*     */ {
/*     */   public String[] getFileExtensions()
/*     */   {
/*  50 */     return new String[] { "yml", "yaml" };
/*     */   }
/*     */   
/*     */   public PropertySource<?> load(String name, Resource resource, String profile)
/*     */     throws IOException
/*     */   {
/*  56 */     if (ClassUtils.isPresent("org.yaml.snakeyaml.Yaml", null)) {
/*  57 */       Processor processor = new Processor(resource, profile);
/*  58 */       Map<String, Object> source = processor.process();
/*  59 */       if (!source.isEmpty()) {
/*  60 */         return new MapPropertySource(name, source);
/*     */       }
/*     */     }
/*  63 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class Processor
/*     */     extends YamlProcessor
/*     */   {
/*     */     Processor(Resource resource, String profile)
/*     */     {
/*  73 */       if (profile == null) {
/*  74 */         setMatchDefault(true);
/*  75 */         setDocumentMatchers(new YamlProcessor.DocumentMatcher[] { new SpringProfileDocumentMatcher() });
/*     */       }
/*     */       else {
/*  78 */         setMatchDefault(false);
/*  79 */         setDocumentMatchers(new YamlProcessor.DocumentMatcher[] { new SpringProfileDocumentMatcher(new String[] { profile }) });
/*     */       }
/*  81 */       setResources(new Resource[] { resource });
/*     */     }
/*     */     
/*     */     protected Yaml createYaml()
/*     */     {
/*  86 */       new Yaml(new YamlProcessor.StrictMapAppenderConstructor(), new Representer(), new DumperOptions(), new Resolver()
/*     */       {
/*     */ 
/*     */         public void addImplicitResolver(Tag tag, Pattern regexp, String first)
/*     */         {
/*  91 */           if (tag == Tag.TIMESTAMP) {
/*  92 */             return;
/*     */           }
/*  94 */           super.addImplicitResolver(tag, regexp, first);
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */     public Map<String, Object> process() {
/* 100 */       final Map<String, Object> result = new LinkedHashMap();
/* 101 */       process(new YamlProcessor.MatchCallback()
/*     */       {
/*     */         public void process(Properties properties, Map<String, Object> map) {
/* 104 */           result.putAll(YamlPropertySourceLoader.Processor.this.getFlattenedMap(map));
/*     */         }
/* 106 */       });
/* 107 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\env\YamlPropertySourceLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */