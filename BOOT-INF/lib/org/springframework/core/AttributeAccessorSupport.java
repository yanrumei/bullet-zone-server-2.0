/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ public abstract class AttributeAccessorSupport
/*     */   implements AttributeAccessor, Serializable
/*     */ {
/*  39 */   private final Map<String, Object> attributes = new LinkedHashMap(0);
/*     */   
/*     */ 
/*     */   public void setAttribute(String name, Object value)
/*     */   {
/*  44 */     Assert.notNull(name, "Name must not be null");
/*  45 */     if (value != null) {
/*  46 */       this.attributes.put(name, value);
/*     */     }
/*     */     else {
/*  49 */       removeAttribute(name);
/*     */     }
/*     */   }
/*     */   
/*     */   public Object getAttribute(String name)
/*     */   {
/*  55 */     Assert.notNull(name, "Name must not be null");
/*  56 */     return this.attributes.get(name);
/*     */   }
/*     */   
/*     */   public Object removeAttribute(String name)
/*     */   {
/*  61 */     Assert.notNull(name, "Name must not be null");
/*  62 */     return this.attributes.remove(name);
/*     */   }
/*     */   
/*     */   public boolean hasAttribute(String name)
/*     */   {
/*  67 */     Assert.notNull(name, "Name must not be null");
/*  68 */     return this.attributes.containsKey(name);
/*     */   }
/*     */   
/*     */   public String[] attributeNames()
/*     */   {
/*  73 */     return (String[])this.attributes.keySet().toArray(new String[this.attributes.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void copyAttributesFrom(AttributeAccessor source)
/*     */   {
/*  82 */     Assert.notNull(source, "Source must not be null");
/*  83 */     String[] attributeNames = source.attributeNames();
/*  84 */     for (String attributeName : attributeNames) {
/*  85 */       setAttribute(attributeName, source.getAttribute(attributeName));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/*  92 */     if (this == other) {
/*  93 */       return true;
/*     */     }
/*  95 */     if (!(other instanceof AttributeAccessorSupport)) {
/*  96 */       return false;
/*     */     }
/*  98 */     AttributeAccessorSupport that = (AttributeAccessorSupport)other;
/*  99 */     return this.attributes.equals(that.attributes);
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 104 */     return this.attributes.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\AttributeAccessorSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */