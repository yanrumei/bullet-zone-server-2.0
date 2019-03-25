/*     */ package org.apache.tomcat.util.digester;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public class RulesBase
/*     */   implements Rules
/*     */ {
/*  56 */   protected HashMap<String, List<Rule>> cache = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  62 */   protected Digester digester = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*  72 */   protected String namespaceURI = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  80 */   protected ArrayList<Rule> rules = new ArrayList();
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
/*     */   public Digester getDigester()
/*     */   {
/*  93 */     return this.digester;
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
/*     */   public void setDigester(Digester digester)
/*     */   {
/* 106 */     this.digester = digester;
/* 107 */     Iterator<Rule> items = this.rules.iterator();
/* 108 */     while (items.hasNext()) {
/* 109 */       Rule item = (Rule)items.next();
/* 110 */       item.setDigester(digester);
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
/*     */   public String getNamespaceURI()
/*     */   {
/* 123 */     return this.namespaceURI;
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
/*     */ 
/*     */   public void setNamespaceURI(String namespaceURI)
/*     */   {
/* 139 */     this.namespaceURI = namespaceURI;
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
/*     */ 
/*     */ 
/*     */   public void add(String pattern, Rule rule)
/*     */   {
/* 156 */     int patternLength = pattern.length();
/* 157 */     if ((patternLength > 1) && (pattern.endsWith("/"))) {
/* 158 */       pattern = pattern.substring(0, patternLength - 1);
/*     */     }
/*     */     
/*     */ 
/* 162 */     List<Rule> list = (List)this.cache.get(pattern);
/* 163 */     if (list == null) {
/* 164 */       list = new ArrayList();
/* 165 */       this.cache.put(pattern, list);
/*     */     }
/* 167 */     list.add(rule);
/* 168 */     this.rules.add(rule);
/* 169 */     if (this.digester != null) {
/* 170 */       rule.setDigester(this.digester);
/*     */     }
/* 172 */     if (this.namespaceURI != null) {
/* 173 */       rule.setNamespaceURI(this.namespaceURI);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/* 185 */     this.cache.clear();
/* 186 */     this.rules.clear();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Rule> match(String namespaceURI, String pattern)
/*     */   {
/* 206 */     List<Rule> rulesList = lookup(namespaceURI, pattern);
/* 207 */     if ((rulesList == null) || (rulesList.size() < 1))
/*     */     {
/* 209 */       String longKey = "";
/* 210 */       Iterator<String> keys = this.cache.keySet().iterator();
/* 211 */       while (keys.hasNext()) {
/* 212 */         String key = (String)keys.next();
/* 213 */         if ((key.startsWith("*/")) && 
/* 214 */           ((pattern.equals(key.substring(2))) || 
/* 215 */           (pattern.endsWith(key.substring(1)))) && 
/* 216 */           (key.length() > longKey.length()))
/*     */         {
/* 218 */           rulesList = lookup(namespaceURI, key);
/* 219 */           longKey = key;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 225 */     if (rulesList == null) {
/* 226 */       rulesList = new ArrayList();
/*     */     }
/* 228 */     return rulesList;
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
/*     */   public List<Rule> rules()
/*     */   {
/* 243 */     return this.rules;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected List<Rule> lookup(String namespaceURI, String pattern)
/*     */   {
/* 264 */     List<Rule> list = (List)this.cache.get(pattern);
/* 265 */     if (list == null) {
/* 266 */       return null;
/*     */     }
/* 268 */     if ((namespaceURI == null) || (namespaceURI.length() == 0)) {
/* 269 */       return list;
/*     */     }
/*     */     
/*     */ 
/* 273 */     ArrayList<Rule> results = new ArrayList();
/* 274 */     Iterator<Rule> items = list.iterator();
/* 275 */     while (items.hasNext()) {
/* 276 */       Rule item = (Rule)items.next();
/* 277 */       if ((namespaceURI.equals(item.getNamespaceURI())) || 
/* 278 */         (item.getNamespaceURI() == null)) {
/* 279 */         results.add(item);
/*     */       }
/*     */     }
/* 282 */     return results;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\digester\RulesBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */