/*     */ package org.springframework.boot.autoconfigure.condition;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public final class ConditionMessage
/*     */ {
/*     */   private String message;
/*     */   
/*     */   private ConditionMessage()
/*     */   {
/*  43 */     this(null);
/*     */   }
/*     */   
/*     */   private ConditionMessage(String message) {
/*  47 */     this.message = message;
/*     */   }
/*     */   
/*     */   private ConditionMessage(ConditionMessage prior, String message) {
/*  51 */     this.message = (prior + "; " + message);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/*  59 */     return !StringUtils.hasLength(this.message);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  64 */     return this.message == null ? "" : this.message;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  69 */     return ObjectUtils.nullSafeHashCode(this.message);
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/*  74 */     if ((obj == null) || (!ConditionMessage.class.isInstance(obj))) {
/*  75 */       return false;
/*     */     }
/*  77 */     if (obj == this) {
/*  78 */       return true;
/*     */     }
/*  80 */     return ObjectUtils.nullSafeEquals(((ConditionMessage)obj).message, this.message);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConditionMessage append(String message)
/*     */   {
/*  90 */     if (!StringUtils.hasLength(message)) {
/*  91 */       return this;
/*     */     }
/*  93 */     if (!StringUtils.hasLength(this.message)) {
/*  94 */       return new ConditionMessage(message);
/*     */     }
/*     */     
/*  97 */     return new ConditionMessage(this.message + " " + message);
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
/*     */   public Builder andCondition(Class<? extends Annotation> condition, Object... details)
/*     */   {
/* 111 */     Assert.notNull(condition, "Condition must not be null");
/* 112 */     return andCondition("@" + ClassUtils.getShortName(condition), details);
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
/*     */   public Builder andCondition(String condition, Object... details)
/*     */   {
/* 125 */     Assert.notNull(condition, "Condition must not be null");
/* 126 */     String detail = StringUtils.arrayToDelimitedString(details, " ");
/* 127 */     if (StringUtils.hasLength(detail)) {
/* 128 */       return new Builder(condition + " " + detail, null);
/*     */     }
/* 130 */     return new Builder(condition, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ConditionMessage empty()
/*     */   {
/* 138 */     return new ConditionMessage();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ConditionMessage of(String message, Object... args)
/*     */   {
/* 149 */     if (ObjectUtils.isEmpty(args)) {
/* 150 */       return new ConditionMessage(message);
/*     */     }
/* 152 */     return new ConditionMessage(String.format(message, args));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ConditionMessage of(Collection<? extends ConditionMessage> messages)
/*     */   {
/* 162 */     ConditionMessage result = new ConditionMessage();
/* 163 */     if (messages != null) {
/* 164 */       for (ConditionMessage message : messages) {
/* 165 */         result = new ConditionMessage(result, message.toString());
/*     */       }
/*     */     }
/* 168 */     return result;
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
/*     */   public static Builder forCondition(Class<? extends Annotation> condition, Object... details)
/*     */   {
/* 182 */     return new ConditionMessage().andCondition(condition, details);
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
/*     */   public static Builder forCondition(String condition, Object... details)
/*     */   {
/* 195 */     return new ConditionMessage().andCondition(condition, details);
/*     */   }
/*     */   
/*     */ 
/*     */   public final class Builder
/*     */   {
/*     */     private final String condition;
/*     */     
/*     */ 
/*     */     private Builder(String condition)
/*     */     {
/* 206 */       this.condition = condition;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ConditionMessage foundExactly(Object result)
/*     */     {
/* 216 */       return found("").items(new Object[] { result });
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ConditionMessage.ItemsBuilder found(String article)
/*     */     {
/* 226 */       return found(article, article);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ConditionMessage.ItemsBuilder found(String singular, String plural)
/*     */     {
/* 238 */       return new ConditionMessage.ItemsBuilder(ConditionMessage.this, this, "found", singular, plural, null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ConditionMessage.ItemsBuilder didNotFind(String article)
/*     */     {
/* 249 */       return didNotFind(article, article);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ConditionMessage.ItemsBuilder didNotFind(String singular, String plural)
/*     */     {
/* 261 */       return new ConditionMessage.ItemsBuilder(ConditionMessage.this, this, "did not find", singular, plural, null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ConditionMessage resultedIn(Object result)
/*     */     {
/* 271 */       return because("resulted in " + result);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ConditionMessage available(String item)
/*     */     {
/* 281 */       return because(item + " is available");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ConditionMessage notAvailable(String item)
/*     */     {
/* 291 */       return because(item + " is not available");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ConditionMessage because(String reason)
/*     */     {
/* 301 */       if (StringUtils.isEmpty(reason)) {
/* 302 */         return new ConditionMessage(ConditionMessage.this, this.condition, null);
/*     */       }
/* 304 */       return new ConditionMessage(ConditionMessage.this, this.condition + (
/* 305 */         StringUtils.isEmpty(this.condition) ? "" : " ") + reason, null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final class ItemsBuilder
/*     */   {
/*     */     private final ConditionMessage.Builder condition;
/*     */     
/*     */ 
/*     */     private final String reason;
/*     */     
/*     */     private final String singular;
/*     */     
/*     */     private final String plural;
/*     */     
/*     */ 
/*     */     private ItemsBuilder(ConditionMessage.Builder condition, String reason, String singular, String plural)
/*     */     {
/* 325 */       this.condition = condition;
/* 326 */       this.reason = reason;
/* 327 */       this.singular = singular;
/* 328 */       this.plural = plural;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ConditionMessage atAll()
/*     */     {
/* 338 */       return items(Collections.emptyList());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ConditionMessage items(Object... items)
/*     */     {
/* 349 */       return items(ConditionMessage.Style.NORMAL, items);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ConditionMessage items(ConditionMessage.Style style, Object... items)
/*     */     {
/* 361 */       return items(style, items == null ? (Collection)null : 
/* 362 */         Arrays.asList(items));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ConditionMessage items(Collection<?> items)
/*     */     {
/* 373 */       return items(ConditionMessage.Style.NORMAL, items);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ConditionMessage items(ConditionMessage.Style style, Collection<?> items)
/*     */     {
/* 385 */       Assert.notNull(style, "Style must not be null");
/* 386 */       StringBuilder message = new StringBuilder(this.reason);
/* 387 */       items = style.applyTo(items);
/* 388 */       if (((this.condition == null) || (items.size() <= 1)) && 
/* 389 */         (StringUtils.hasLength(this.singular))) {
/* 390 */         message.append(" " + this.singular);
/*     */       }
/* 392 */       else if (StringUtils.hasLength(this.plural)) {
/* 393 */         message.append(" " + this.plural);
/*     */       }
/* 395 */       if ((items != null) && (!items.isEmpty())) {
/* 396 */         message.append(" " + 
/* 397 */           StringUtils.collectionToDelimitedString(items, ", "));
/*     */       }
/* 399 */       return this.condition.because(message.toString());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static abstract enum Style
/*     */   {
/* 409 */     NORMAL, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 416 */     QUOTE;
/*     */     
/*     */ 
/*     */     private Style() {}
/*     */     
/*     */ 
/*     */     public Collection<?> applyTo(Collection<?> items)
/*     */     {
/* 424 */       List<Object> result = new ArrayList();
/* 425 */       for (Object item : items) {
/* 426 */         result.add(applyToItem(item));
/*     */       }
/* 428 */       return result;
/*     */     }
/*     */     
/*     */     protected abstract Object applyToItem(Object paramObject);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\condition\ConditionMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */