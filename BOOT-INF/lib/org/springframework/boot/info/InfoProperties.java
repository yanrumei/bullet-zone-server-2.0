/*     */ package org.springframework.boot.info;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.env.PropertiesPropertySource;
/*     */ import org.springframework.core.env.PropertySource;
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
/*     */ public class InfoProperties
/*     */   implements Iterable<Entry>
/*     */ {
/*     */   private final Properties entries;
/*     */   
/*     */   public InfoProperties(Properties entries)
/*     */   {
/*  44 */     Assert.notNull(entries, "Entries must not be null");
/*  45 */     this.entries = copy(entries);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String get(String key)
/*     */   {
/*  54 */     return this.entries.getProperty(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date getDate(String key)
/*     */   {
/*  64 */     String s = get(key);
/*  65 */     if (s != null) {
/*     */       try {
/*  67 */         return new Date(Long.parseLong(s));
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException) {}
/*     */     }
/*     */     
/*     */ 
/*  73 */     return null;
/*     */   }
/*     */   
/*     */   public Iterator<Entry> iterator()
/*     */   {
/*  78 */     return new PropertiesIterator(this.entries, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertySource<?> toPropertySource()
/*     */   {
/*  86 */     return new PropertiesPropertySource(getClass().getSimpleName(), 
/*  87 */       copy(this.entries));
/*     */   }
/*     */   
/*     */   private Properties copy(Properties properties) {
/*  91 */     Properties copy = new Properties();
/*  92 */     copy.putAll(properties);
/*  93 */     return copy;
/*     */   }
/*     */   
/*     */   private final class PropertiesIterator implements Iterator<InfoProperties.Entry>
/*     */   {
/*     */     private final Iterator<Map.Entry<Object, Object>> iterator;
/*     */     
/*     */     private PropertiesIterator(Properties properties) {
/* 101 */       this.iterator = properties.entrySet().iterator();
/*     */     }
/*     */     
/*     */     public boolean hasNext()
/*     */     {
/* 106 */       return this.iterator.hasNext();
/*     */     }
/*     */     
/*     */     public InfoProperties.Entry next()
/*     */     {
/* 111 */       Map.Entry<Object, Object> entry = (Map.Entry)this.iterator.next();
/* 112 */       return new InfoProperties.Entry(InfoProperties.this, (String)entry.getKey(), (String)entry.getValue(), null);
/*     */     }
/*     */     
/*     */     public void remove()
/*     */     {
/* 117 */       throw new UnsupportedOperationException("InfoProperties are immutable.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final class Entry
/*     */   {
/*     */     private final String key;
/*     */     
/*     */     private final String value;
/*     */     
/*     */ 
/*     */     private Entry(String key, String value)
/*     */     {
/* 132 */       this.key = key;
/* 133 */       this.value = value;
/*     */     }
/*     */     
/*     */     public String getKey() {
/* 137 */       return this.key;
/*     */     }
/*     */     
/*     */     public String getValue() {
/* 141 */       return this.value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\info\InfoProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */