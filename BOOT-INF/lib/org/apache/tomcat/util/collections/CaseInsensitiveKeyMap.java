/*     */ package org.apache.tomcat.util.collections;
/*     */ 
/*     */ import java.util.AbstractMap;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ public class CaseInsensitiveKeyMap<V>
/*     */   extends AbstractMap<String, V>
/*     */ {
/*  43 */   private static final StringManager sm = StringManager.getManager(CaseInsensitiveKeyMap.class);
/*     */   
/*  45 */   private final Map<Key, V> map = new HashMap();
/*     */   
/*     */ 
/*     */   public V get(Object key)
/*     */   {
/*  50 */     return (V)this.map.get(Key.getInstance(key));
/*     */   }
/*     */   
/*     */ 
/*     */   public V put(String key, V value)
/*     */   {
/*  56 */     Key caseInsensitiveKey = Key.getInstance(key);
/*  57 */     if (caseInsensitiveKey == null) {
/*  58 */       throw new NullPointerException(sm.getString("caseInsensitiveKeyMap.nullKey"));
/*     */     }
/*  60 */     return (V)this.map.put(caseInsensitiveKey, value);
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
/*     */   public void putAll(Map<? extends String, ? extends V> m)
/*     */   {
/*  73 */     super.putAll(m);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean containsKey(Object key)
/*     */   {
/*  79 */     return this.map.containsKey(Key.getInstance(key));
/*     */   }
/*     */   
/*     */ 
/*     */   public V remove(Object key)
/*     */   {
/*  85 */     return (V)this.map.remove(Key.getInstance(key));
/*     */   }
/*     */   
/*     */ 
/*     */   public Set<Map.Entry<String, V>> entrySet()
/*     */   {
/*  91 */     return new EntrySet(this.map.entrySet());
/*     */   }
/*     */   
/*     */   private static class EntrySet<V> extends AbstractSet<Map.Entry<String, V>>
/*     */   {
/*     */     private final Set<Map.Entry<CaseInsensitiveKeyMap.Key, V>> entrySet;
/*     */     
/*     */     public EntrySet(Set<Map.Entry<CaseInsensitiveKeyMap.Key, V>> entrySet)
/*     */     {
/* 100 */       this.entrySet = entrySet;
/*     */     }
/*     */     
/*     */     public Iterator<Map.Entry<String, V>> iterator()
/*     */     {
/* 105 */       return new CaseInsensitiveKeyMap.EntryIterator(this.entrySet.iterator());
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 110 */       return this.entrySet.size();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class EntryIterator<V> implements Iterator<Map.Entry<String, V>>
/*     */   {
/*     */     private final Iterator<Map.Entry<CaseInsensitiveKeyMap.Key, V>> iterator;
/*     */     
/*     */     public EntryIterator(Iterator<Map.Entry<CaseInsensitiveKeyMap.Key, V>> iterator)
/*     */     {
/* 120 */       this.iterator = iterator;
/*     */     }
/*     */     
/*     */     public boolean hasNext()
/*     */     {
/* 125 */       return this.iterator.hasNext();
/*     */     }
/*     */     
/*     */     public Map.Entry<String, V> next()
/*     */     {
/* 130 */       Map.Entry<CaseInsensitiveKeyMap.Key, V> entry = (Map.Entry)this.iterator.next();
/* 131 */       return new CaseInsensitiveKeyMap.EntryImpl(((CaseInsensitiveKeyMap.Key)entry.getKey()).getKey(), entry.getValue());
/*     */     }
/*     */     
/*     */     public void remove()
/*     */     {
/* 136 */       this.iterator.remove();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class EntryImpl<V> implements Map.Entry<String, V>
/*     */   {
/*     */     private final String key;
/*     */     private final V value;
/*     */     
/*     */     public EntryImpl(String key, V value)
/*     */     {
/* 147 */       this.key = key;
/* 148 */       this.value = value;
/*     */     }
/*     */     
/*     */     public String getKey()
/*     */     {
/* 153 */       return this.key;
/*     */     }
/*     */     
/*     */     public V getValue()
/*     */     {
/* 158 */       return (V)this.value;
/*     */     }
/*     */     
/*     */     public V setValue(V value)
/*     */     {
/* 163 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Key
/*     */   {
/*     */     private final String key;
/*     */     private final String lcKey;
/*     */     
/*     */     private Key(String key) {
/* 173 */       this.key = key;
/* 174 */       this.lcKey = key.toLowerCase(Locale.ENGLISH);
/*     */     }
/*     */     
/*     */     public String getKey() {
/* 178 */       return this.key;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 183 */       return this.lcKey.hashCode();
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 188 */       if (this == obj) {
/* 189 */         return true;
/*     */       }
/* 191 */       if (obj == null) {
/* 192 */         return false;
/*     */       }
/* 194 */       if (getClass() != obj.getClass()) {
/* 195 */         return false;
/*     */       }
/* 197 */       Key other = (Key)obj;
/* 198 */       return this.lcKey.equals(other.lcKey);
/*     */     }
/*     */     
/*     */     public static Key getInstance(Object o) {
/* 202 */       if ((o instanceof String)) {
/* 203 */         return new Key((String)o);
/*     */       }
/* 205 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\collections\CaseInsensitiveKeyMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */