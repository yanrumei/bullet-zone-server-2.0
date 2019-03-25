/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.NamespaceContext;
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
/*     */ public class SimpleNamespaceContext
/*     */   implements NamespaceContext
/*     */ {
/*  41 */   private final Map<String, String> prefixToNamespaceUri = new HashMap();
/*     */   
/*  43 */   private final Map<String, Set<String>> namespaceUriToPrefixes = new HashMap();
/*     */   
/*  45 */   private String defaultNamespaceUri = "";
/*     */   
/*     */ 
/*     */   public String getNamespaceURI(String prefix)
/*     */   {
/*  50 */     Assert.notNull(prefix, "No prefix given");
/*  51 */     if ("xml".equals(prefix)) {
/*  52 */       return "http://www.w3.org/XML/1998/namespace";
/*     */     }
/*  54 */     if ("xmlns".equals(prefix)) {
/*  55 */       return "http://www.w3.org/2000/xmlns/";
/*     */     }
/*  57 */     if ("".equals(prefix)) {
/*  58 */       return this.defaultNamespaceUri;
/*     */     }
/*  60 */     if (this.prefixToNamespaceUri.containsKey(prefix)) {
/*  61 */       return (String)this.prefixToNamespaceUri.get(prefix);
/*     */     }
/*  63 */     return "";
/*     */   }
/*     */   
/*     */   public String getPrefix(String namespaceUri)
/*     */   {
/*  68 */     Set<String> prefixes = getPrefixesSet(namespaceUri);
/*  69 */     return !prefixes.isEmpty() ? (String)prefixes.iterator().next() : null;
/*     */   }
/*     */   
/*     */   public Iterator<String> getPrefixes(String namespaceUri)
/*     */   {
/*  74 */     return getPrefixesSet(namespaceUri).iterator();
/*     */   }
/*     */   
/*     */   private Set<String> getPrefixesSet(String namespaceUri) {
/*  78 */     Assert.notNull(namespaceUri, "No namespaceUri given");
/*  79 */     if (this.defaultNamespaceUri.equals(namespaceUri)) {
/*  80 */       return Collections.singleton("");
/*     */     }
/*  82 */     if ("http://www.w3.org/XML/1998/namespace".equals(namespaceUri)) {
/*  83 */       return Collections.singleton("xml");
/*     */     }
/*  85 */     if ("http://www.w3.org/2000/xmlns/".equals(namespaceUri)) {
/*  86 */       return Collections.singleton("xmlns");
/*     */     }
/*     */     
/*  89 */     Set<String> prefixes = (Set)this.namespaceUriToPrefixes.get(namespaceUri);
/*  90 */     return prefixes != null ? Collections.unmodifiableSet(prefixes) : Collections.emptySet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBindings(Map<String, String> bindings)
/*     */   {
/* 100 */     for (Map.Entry<String, String> entry : bindings.entrySet()) {
/* 101 */       bindNamespaceUri((String)entry.getKey(), (String)entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void bindDefaultNamespaceUri(String namespaceUri)
/*     */   {
/* 110 */     bindNamespaceUri("", namespaceUri);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void bindNamespaceUri(String prefix, String namespaceUri)
/*     */   {
/* 119 */     Assert.notNull(prefix, "No prefix given");
/* 120 */     Assert.notNull(namespaceUri, "No namespaceUri given");
/* 121 */     if ("".equals(prefix)) {
/* 122 */       this.defaultNamespaceUri = namespaceUri;
/*     */     }
/*     */     else {
/* 125 */       this.prefixToNamespaceUri.put(prefix, namespaceUri);
/* 126 */       Set<String> prefixes = (Set)this.namespaceUriToPrefixes.get(namespaceUri);
/* 127 */       if (prefixes == null) {
/* 128 */         prefixes = new LinkedHashSet();
/* 129 */         this.namespaceUriToPrefixes.put(namespaceUri, prefixes);
/*     */       }
/* 131 */       prefixes.add(prefix);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeBinding(String prefix)
/*     */   {
/* 140 */     if ("".equals(prefix)) {
/* 141 */       this.defaultNamespaceUri = "";
/*     */     }
/* 143 */     else if (prefix != null) {
/* 144 */       String namespaceUri = (String)this.prefixToNamespaceUri.remove(prefix);
/* 145 */       if (namespaceUri != null) {
/* 146 */         Set<String> prefixes = (Set)this.namespaceUriToPrefixes.get(namespaceUri);
/* 147 */         if (prefixes != null) {
/* 148 */           prefixes.remove(prefix);
/* 149 */           if (prefixes.isEmpty()) {
/* 150 */             this.namespaceUriToPrefixes.remove(namespaceUri);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/* 161 */     this.prefixToNamespaceUri.clear();
/* 162 */     this.namespaceUriToPrefixes.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Iterator<String> getBoundPrefixes()
/*     */   {
/* 169 */     return this.prefixToNamespaceUri.keySet().iterator();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\xml\SimpleNamespaceContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */