/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.StringValueResolver;
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
/*     */ public class SimpleAliasRegistry
/*     */   implements AliasRegistry
/*     */ {
/*  41 */   private final Map<String, String> aliasMap = new ConcurrentHashMap(16);
/*     */   
/*     */ 
/*     */   public void registerAlias(String name, String alias)
/*     */   {
/*  46 */     Assert.hasText(name, "'name' must not be empty");
/*  47 */     Assert.hasText(alias, "'alias' must not be empty");
/*  48 */     if (alias.equals(name)) {
/*  49 */       this.aliasMap.remove(alias);
/*     */     }
/*     */     else {
/*  52 */       String registeredName = (String)this.aliasMap.get(alias);
/*  53 */       if (registeredName != null) {
/*  54 */         if (registeredName.equals(name))
/*     */         {
/*  56 */           return;
/*     */         }
/*  58 */         if (!allowAliasOverriding()) {
/*  59 */           throw new IllegalStateException("Cannot register alias '" + alias + "' for name '" + name + "': It is already registered for name '" + registeredName + "'.");
/*     */         }
/*     */       }
/*     */       
/*  63 */       checkForAliasCircle(name, alias);
/*  64 */       this.aliasMap.put(alias, name);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean allowAliasOverriding()
/*     */   {
/*  73 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasAlias(String name, String alias)
/*     */   {
/*  83 */     for (Map.Entry<String, String> entry : this.aliasMap.entrySet()) {
/*  84 */       String registeredName = (String)entry.getValue();
/*  85 */       if (registeredName.equals(name)) {
/*  86 */         String registeredAlias = (String)entry.getKey();
/*  87 */         return (registeredAlias.equals(alias)) || (hasAlias(registeredAlias, alias));
/*     */       }
/*     */     }
/*  90 */     return false;
/*     */   }
/*     */   
/*     */   public void removeAlias(String alias)
/*     */   {
/*  95 */     String name = (String)this.aliasMap.remove(alias);
/*  96 */     if (name == null) {
/*  97 */       throw new IllegalStateException("No alias '" + alias + "' registered");
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isAlias(String name)
/*     */   {
/* 103 */     return this.aliasMap.containsKey(name);
/*     */   }
/*     */   
/*     */   public String[] getAliases(String name)
/*     */   {
/* 108 */     List<String> result = new ArrayList();
/* 109 */     synchronized (this.aliasMap) {
/* 110 */       retrieveAliases(name, result);
/*     */     }
/* 112 */     return StringUtils.toStringArray(result);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void retrieveAliases(String name, List<String> result)
/*     */   {
/* 121 */     for (Map.Entry<String, String> entry : this.aliasMap.entrySet()) {
/* 122 */       String registeredName = (String)entry.getValue();
/* 123 */       if (registeredName.equals(name)) {
/* 124 */         String alias = (String)entry.getKey();
/* 125 */         result.add(alias);
/* 126 */         retrieveAliases(alias, result);
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
/*     */   public void resolveAliases(StringValueResolver valueResolver)
/*     */   {
/* 139 */     Assert.notNull(valueResolver, "StringValueResolver must not be null");
/* 140 */     Map<String, String> aliasCopy; synchronized (this.aliasMap) {
/* 141 */       aliasCopy = new HashMap(this.aliasMap);
/* 142 */       for (String alias : aliasCopy.keySet()) {
/* 143 */         String registeredName = (String)aliasCopy.get(alias);
/* 144 */         String resolvedAlias = valueResolver.resolveStringValue(alias);
/* 145 */         String resolvedName = valueResolver.resolveStringValue(registeredName);
/* 146 */         if ((resolvedAlias == null) || (resolvedName == null) || (resolvedAlias.equals(resolvedName))) {
/* 147 */           this.aliasMap.remove(alias);
/*     */         }
/* 149 */         else if (!resolvedAlias.equals(alias)) {
/* 150 */           String existingName = (String)this.aliasMap.get(resolvedAlias);
/* 151 */           if (existingName != null) {
/* 152 */             if (existingName.equals(resolvedName))
/*     */             {
/* 154 */               this.aliasMap.remove(alias);
/* 155 */               break;
/*     */             }
/* 157 */             throw new IllegalStateException("Cannot register resolved alias '" + resolvedAlias + "' (original: '" + alias + "') for name '" + resolvedName + "': It is already registered for name '" + registeredName + "'.");
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 162 */           checkForAliasCircle(resolvedName, resolvedAlias);
/* 163 */           this.aliasMap.remove(alias);
/* 164 */           this.aliasMap.put(resolvedAlias, resolvedName);
/*     */         }
/* 166 */         else if (!registeredName.equals(resolvedName)) {
/* 167 */           this.aliasMap.put(alias, resolvedName);
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
/*     */   protected void checkForAliasCircle(String name, String alias)
/*     */   {
/* 183 */     if (hasAlias(alias, name)) {
/* 184 */       throw new IllegalStateException("Cannot register alias '" + alias + "' for name '" + name + "': Circular reference - '" + name + "' is a direct or indirect alias for '" + alias + "' already");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String canonicalName(String name)
/*     */   {
/* 196 */     String canonicalName = name;
/*     */     String resolvedName;
/*     */     do
/*     */     {
/* 200 */       resolvedName = (String)this.aliasMap.get(canonicalName);
/* 201 */       if (resolvedName != null) {
/* 202 */         canonicalName = resolvedName;
/*     */       }
/*     */       
/* 205 */     } while (resolvedName != null);
/* 206 */     return canonicalName;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\SimpleAliasRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */