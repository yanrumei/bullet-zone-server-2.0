/*     */ package org.springframework.boot.autoconfigure.condition;
/*     */ 
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public class ConditionOutcome
/*     */ {
/*     */   private final boolean match;
/*     */   private final ConditionMessage message;
/*     */   
/*     */   public ConditionOutcome(boolean match, String message)
/*     */   {
/*  41 */     this(match, ConditionMessage.of(message, new Object[0]));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConditionOutcome(boolean match, ConditionMessage message)
/*     */   {
/*  50 */     Assert.notNull(message, "ConditionMessage must not be null");
/*  51 */     this.match = match;
/*  52 */     this.message = message;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ConditionOutcome match()
/*     */   {
/*  60 */     return match(ConditionMessage.empty());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ConditionOutcome match(String message)
/*     */   {
/*  70 */     return new ConditionOutcome(true, message);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ConditionOutcome match(ConditionMessage message)
/*     */   {
/*  79 */     return new ConditionOutcome(true, message);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ConditionOutcome noMatch(String message)
/*     */   {
/*  89 */     return new ConditionOutcome(false, message);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ConditionOutcome noMatch(ConditionMessage message)
/*     */   {
/*  98 */     return new ConditionOutcome(false, message);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isMatch()
/*     */   {
/* 106 */     return this.match;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 114 */     return this.message.isEmpty() ? null : this.message.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConditionMessage getConditionMessage()
/*     */   {
/* 122 */     return this.message;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 127 */     return 
/* 128 */       ObjectUtils.hashCode(this.match) * 31 + ObjectUtils.nullSafeHashCode(this.message);
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 133 */     if (this == obj) {
/* 134 */       return true;
/*     */     }
/* 136 */     if (obj == null) {
/* 137 */       return false;
/*     */     }
/* 139 */     if (getClass() == obj.getClass()) {
/* 140 */       ConditionOutcome other = (ConditionOutcome)obj;
/* 141 */       return (this.match == other.match) && 
/* 142 */         (ObjectUtils.nullSafeEquals(this.message, other.message));
/*     */     }
/* 144 */     return super.equals(obj);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 149 */     return this.message == null ? "" : this.message.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ConditionOutcome inverse(ConditionOutcome outcome)
/*     */   {
/* 159 */     return new ConditionOutcome(!outcome.isMatch(), outcome.getConditionMessage());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\condition\ConditionOutcome.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */