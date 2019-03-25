/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.CollectionFactory;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.yaml.snakeyaml.Yaml;
/*     */ import org.yaml.snakeyaml.constructor.Constructor;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.parser.ParserException;
/*     */ import org.yaml.snakeyaml.reader.UnicodeReader;
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
/*     */ public abstract class YamlProcessor
/*     */ {
/*  54 */   private final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  56 */   private ResolutionMethod resolutionMethod = ResolutionMethod.OVERRIDE;
/*     */   
/*  58 */   private Resource[] resources = new Resource[0];
/*     */   
/*  60 */   private List<DocumentMatcher> documentMatchers = Collections.emptyList();
/*     */   
/*  62 */   private boolean matchDefault = true;
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
/*     */   public void setDocumentMatchers(DocumentMatcher... matchers)
/*     */   {
/*  91 */     this.documentMatchers = Arrays.asList(matchers);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMatchDefault(boolean matchDefault)
/*     */   {
/* 101 */     this.matchDefault = matchDefault;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setResolutionMethod(ResolutionMethod resolutionMethod)
/*     */   {
/* 112 */     Assert.notNull(resolutionMethod, "ResolutionMethod must not be null");
/* 113 */     this.resolutionMethod = resolutionMethod;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setResources(Resource... resources)
/*     */   {
/* 121 */     this.resources = resources;
/*     */   }
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
/*     */   protected void process(MatchCallback callback)
/*     */   {
/* 136 */     Yaml yaml = createYaml();
/* 137 */     for (Resource resource : this.resources) {
/* 138 */       boolean found = process(callback, yaml, resource);
/* 139 */       if ((this.resolutionMethod == ResolutionMethod.FIRST_FOUND) && (found)) {
/* 140 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Yaml createYaml()
/*     */   {
/* 149 */     return new Yaml(new StrictMapAppenderConstructor());
/*     */   }
/*     */   
/*     */   private boolean process(MatchCallback callback, Yaml yaml, Resource resource) {
/* 153 */     int count = 0;
/*     */     try {
/* 155 */       if (this.logger.isDebugEnabled()) {
/* 156 */         this.logger.debug("Loading from YAML: " + resource);
/*     */       }
/* 158 */       Reader reader = new UnicodeReader(resource.getInputStream());
/*     */       try {
/* 160 */         for (Object object : yaml.loadAll(reader)) {
/* 161 */           if ((object != null) && (process(asMap(object), callback))) {
/* 162 */             count++;
/* 163 */             if (this.resolutionMethod == ResolutionMethod.FIRST_FOUND) {
/*     */               break;
/*     */             }
/*     */           }
/*     */         }
/* 168 */         if (this.logger.isDebugEnabled()) {
/* 169 */           this.logger.debug("Loaded " + count + " document" + (count > 1 ? "s" : "") + " from YAML resource: " + resource);
/*     */         }
/*     */       }
/*     */       finally
/*     */       {
/* 174 */         reader.close();
/*     */       }
/*     */     }
/*     */     catch (IOException ex) {
/* 178 */       handleProcessError(resource, ex);
/*     */     }
/* 180 */     return count > 0;
/*     */   }
/*     */   
/*     */   private void handleProcessError(Resource resource, IOException ex) {
/* 184 */     if ((this.resolutionMethod != ResolutionMethod.FIRST_FOUND) && (this.resolutionMethod != ResolutionMethod.OVERRIDE_AND_IGNORE))
/*     */     {
/* 186 */       throw new IllegalStateException(ex);
/*     */     }
/* 188 */     if (this.logger.isWarnEnabled()) {
/* 189 */       this.logger.warn("Could not load map from " + resource + ": " + ex.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private Map<String, Object> asMap(Object object)
/*     */   {
/* 196 */     Map<String, Object> result = new LinkedHashMap();
/* 197 */     if (!(object instanceof Map))
/*     */     {
/* 199 */       result.put("document", object);
/* 200 */       return result;
/*     */     }
/*     */     
/* 203 */     Map<Object, Object> map = (Map)object;
/* 204 */     for (Map.Entry<Object, Object> entry : map.entrySet()) {
/* 205 */       Object value = entry.getValue();
/* 206 */       if ((value instanceof Map)) {
/* 207 */         value = asMap(value);
/*     */       }
/* 209 */       Object key = entry.getKey();
/* 210 */       if ((key instanceof CharSequence)) {
/* 211 */         result.put(key.toString(), value);
/*     */       }
/*     */       else
/*     */       {
/* 215 */         result.put("[" + key.toString() + "]", value);
/*     */       }
/*     */     }
/* 218 */     return result;
/*     */   }
/*     */   
/*     */   private boolean process(Map<String, Object> map, MatchCallback callback) {
/* 222 */     Properties properties = CollectionFactory.createStringAdaptingProperties();
/* 223 */     properties.putAll(getFlattenedMap(map));
/*     */     
/* 225 */     if (this.documentMatchers.isEmpty()) {
/* 226 */       if (this.logger.isDebugEnabled()) {
/* 227 */         this.logger.debug("Merging document (no matchers set): " + map);
/*     */       }
/* 229 */       callback.process(properties, map);
/* 230 */       return true;
/*     */     }
/*     */     
/* 233 */     MatchStatus result = MatchStatus.ABSTAIN;
/* 234 */     for (DocumentMatcher matcher : this.documentMatchers) {
/* 235 */       MatchStatus match = matcher.matches(properties);
/* 236 */       result = MatchStatus.getMostSpecific(match, result);
/* 237 */       if (match == MatchStatus.FOUND) {
/* 238 */         if (this.logger.isDebugEnabled()) {
/* 239 */           this.logger.debug("Matched document with document matcher: " + properties);
/*     */         }
/* 241 */         callback.process(properties, map);
/* 242 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 246 */     if ((result == MatchStatus.ABSTAIN) && (this.matchDefault)) {
/* 247 */       if (this.logger.isDebugEnabled()) {
/* 248 */         this.logger.debug("Matched document with default matcher: " + map);
/*     */       }
/* 250 */       callback.process(properties, map);
/* 251 */       return true;
/*     */     }
/*     */     
/* 254 */     if (this.logger.isDebugEnabled()) {
/* 255 */       this.logger.debug("Unmatched document: " + map);
/*     */     }
/* 257 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Map<String, Object> getFlattenedMap(Map<String, Object> source)
/*     */   {
/* 270 */     Map<String, Object> result = new LinkedHashMap();
/* 271 */     buildFlattenedMap(result, source, null);
/* 272 */     return result;
/*     */   }
/*     */   
/*     */   private void buildFlattenedMap(Map<String, Object> result, Map<String, Object> source, String path) {
/* 276 */     for (Map.Entry<String, Object> entry : source.entrySet()) {
/* 277 */       String key = (String)entry.getKey();
/* 278 */       if (StringUtils.hasText(path)) {
/* 279 */         if (key.startsWith("[")) {
/* 280 */           key = path + key;
/*     */         }
/*     */         else {
/* 283 */           key = path + '.' + key;
/*     */         }
/*     */       }
/* 286 */       Object value = entry.getValue();
/* 287 */       if ((value instanceof String)) {
/* 288 */         result.put(key, value);
/*     */       }
/* 290 */       else if ((value instanceof Map))
/*     */       {
/*     */ 
/* 293 */         Map<String, Object> map = (Map)value;
/* 294 */         buildFlattenedMap(result, map, key);
/*     */       } else { int count;
/* 296 */         if ((value instanceof Collection))
/*     */         {
/*     */ 
/* 299 */           Collection<Object> collection = (Collection)value;
/* 300 */           count = 0;
/* 301 */           for (Object object : collection) {
/* 302 */             buildFlattenedMap(result, 
/* 303 */               Collections.singletonMap("[" + count++ + "]", object), key);
/*     */           }
/*     */         }
/*     */         else {
/* 307 */           result.put(key, value != null ? value : "");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
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
/*     */   public static abstract interface MatchCallback
/*     */   {
/*     */     public abstract void process(Properties paramProperties, Map<String, Object> paramMap);
/*     */   }
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
/*     */   public static abstract interface DocumentMatcher
/*     */   {
/*     */     public abstract YamlProcessor.MatchStatus matches(Properties paramProperties);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum MatchStatus
/*     */   {
/* 351 */     FOUND, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 356 */     NOT_FOUND, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 361 */     ABSTAIN;
/*     */     
/*     */     private MatchStatus() {}
/*     */     
/*     */     public static MatchStatus getMostSpecific(MatchStatus a, MatchStatus b)
/*     */     {
/* 367 */       return a.ordinal() < b.ordinal() ? a : b;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum ResolutionMethod
/*     */   {
/* 380 */     OVERRIDE, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 385 */     OVERRIDE_AND_IGNORE, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 390 */     FIRST_FOUND;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private ResolutionMethod() {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected static class StrictMapAppenderConstructor
/*     */     extends Constructor
/*     */   {
/*     */     protected Map<Object, Object> constructMapping(MappingNode node)
/*     */     {
/*     */       try
/*     */       {
/* 407 */         return super.constructMapping(node);
/*     */       }
/*     */       catch (IllegalStateException ex)
/*     */       {
/* 411 */         throw new ParserException("while parsing MappingNode", node.getStartMark(), ex.getMessage(), node.getEndMark());
/*     */       }
/*     */     }
/*     */     
/*     */     protected Map<Object, Object> createDefaultMap()
/*     */     {
/* 417 */       final Map<Object, Object> delegate = super.createDefaultMap();
/* 418 */       new AbstractMap()
/*     */       {
/*     */         public Object put(Object key, Object value) {
/* 421 */           if (delegate.containsKey(key)) {
/* 422 */             throw new IllegalStateException("Duplicate key: " + key);
/*     */           }
/* 424 */           return delegate.put(key, value);
/*     */         }
/*     */         
/*     */         public Set<Map.Entry<Object, Object>> entrySet() {
/* 428 */           return delegate.entrySet();
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\config\YamlProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */