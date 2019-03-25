/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.ReflectionUtils.FieldCallback;
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
/*     */ public abstract class PropertyMatches
/*     */ {
/*     */   public static final int DEFAULT_MAX_DISTANCE = 2;
/*     */   private final String propertyName;
/*     */   private String[] possibleMatches;
/*     */   
/*     */   public static PropertyMatches forProperty(String propertyName, Class<?> beanClass)
/*     */   {
/*  58 */     return forProperty(propertyName, beanClass, 2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static PropertyMatches forProperty(String propertyName, Class<?> beanClass, int maxDistance)
/*     */   {
/*  68 */     return new BeanPropertyMatches(propertyName, beanClass, maxDistance);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static PropertyMatches forField(String propertyName, Class<?> beanClass)
/*     */   {
/*  77 */     return forField(propertyName, beanClass, 2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static PropertyMatches forField(String propertyName, Class<?> beanClass, int maxDistance)
/*     */   {
/*  87 */     return new FieldPropertyMatches(propertyName, beanClass, maxDistance);
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
/*     */   private PropertyMatches(String propertyName, String[] possibleMatches)
/*     */   {
/* 102 */     this.propertyName = propertyName;
/* 103 */     this.possibleMatches = possibleMatches;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPropertyName()
/*     */   {
/* 111 */     return this.propertyName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String[] getPossibleMatches()
/*     */   {
/* 118 */     return this.possibleMatches;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract String buildErrorMessage();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void appendHintMessage(StringBuilder msg)
/*     */   {
/* 131 */     msg.append("Did you mean ");
/* 132 */     for (int i = 0; i < this.possibleMatches.length; i++) {
/* 133 */       msg.append('\'');
/* 134 */       msg.append(this.possibleMatches[i]);
/* 135 */       if (i < this.possibleMatches.length - 2) {
/* 136 */         msg.append("', ");
/*     */       }
/* 138 */       else if (i == this.possibleMatches.length - 2) {
/* 139 */         msg.append("', or ");
/*     */       }
/*     */     }
/* 142 */     msg.append("'?");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int calculateStringDistance(String s1, String s2)
/*     */   {
/* 153 */     if (s1.isEmpty()) {
/* 154 */       return s2.length();
/*     */     }
/* 156 */     if (s2.isEmpty()) {
/* 157 */       return s1.length();
/*     */     }
/* 159 */     int[][] d = new int[s1.length() + 1][s2.length() + 1];
/*     */     
/* 161 */     for (int i = 0; i <= s1.length(); i++) {
/* 162 */       d[i][0] = i;
/*     */     }
/* 164 */     for (int j = 0; j <= s2.length(); j++) {
/* 165 */       d[0][j] = j;
/*     */     }
/*     */     
/* 168 */     for (int i = 1; i <= s1.length(); i++) {
/* 169 */       char s_i = s1.charAt(i - 1);
/* 170 */       for (int j = 1; j <= s2.length(); j++)
/*     */       {
/* 172 */         char t_j = s2.charAt(j - 1);
/* 173 */         int cost; int cost; if (s_i == t_j) {
/* 174 */           cost = 0;
/*     */         }
/*     */         else {
/* 177 */           cost = 1;
/*     */         }
/* 179 */         d[i][j] = Math.min(Math.min(d[(i - 1)][j] + 1, d[i][(j - 1)] + 1), d[(i - 1)][(j - 1)] + cost);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 184 */     return d[s1.length()][s2.length()];
/*     */   }
/*     */   
/*     */ 
/*     */   private static class BeanPropertyMatches
/*     */     extends PropertyMatches
/*     */   {
/*     */     public BeanPropertyMatches(String propertyName, Class<?> beanClass, int maxDistance)
/*     */     {
/* 193 */       super(calculateMatches(propertyName, 
/* 194 */         BeanUtils.getPropertyDescriptors(beanClass), maxDistance), null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private static String[] calculateMatches(String propertyName, PropertyDescriptor[] propertyDescriptors, int maxDistance)
/*     */     {
/* 206 */       List<String> candidates = new ArrayList();
/* 207 */       for (PropertyDescriptor pd : propertyDescriptors) {
/* 208 */         if (pd.getWriteMethod() != null) {
/* 209 */           String possibleAlternative = pd.getName();
/* 210 */           if (PropertyMatches.calculateStringDistance(propertyName, possibleAlternative) <= maxDistance) {
/* 211 */             candidates.add(possibleAlternative);
/*     */           }
/*     */         }
/*     */       }
/* 215 */       Collections.sort(candidates);
/* 216 */       return StringUtils.toStringArray(candidates);
/*     */     }
/*     */     
/*     */ 
/*     */     public String buildErrorMessage()
/*     */     {
/* 222 */       String propertyName = getPropertyName();
/* 223 */       String[] possibleMatches = getPossibleMatches();
/* 224 */       StringBuilder msg = new StringBuilder();
/* 225 */       msg.append("Bean property '");
/* 226 */       msg.append(propertyName);
/* 227 */       msg.append("' is not writable or has an invalid setter method. ");
/*     */       
/* 229 */       if (ObjectUtils.isEmpty(possibleMatches)) {
/* 230 */         msg.append("Does the parameter type of the setter match the return type of the getter?");
/*     */       }
/*     */       else {
/* 233 */         appendHintMessage(msg);
/*     */       }
/* 235 */       return msg.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FieldPropertyMatches extends PropertyMatches
/*     */   {
/*     */     public FieldPropertyMatches(String propertyName, Class<?> beanClass, int maxDistance)
/*     */     {
/* 243 */       super(calculateMatches(propertyName, beanClass, maxDistance), null);
/*     */     }
/*     */     
/*     */     private static String[] calculateMatches(String propertyName, Class<?> beanClass, final int maxDistance) {
/* 247 */       final List<String> candidates = new ArrayList();
/* 248 */       ReflectionUtils.doWithFields(beanClass, new ReflectionUtils.FieldCallback()
/*     */       {
/*     */         public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
/* 251 */           String possibleAlternative = field.getName();
/* 252 */           if (PropertyMatches.calculateStringDistance(this.val$propertyName, possibleAlternative) <= maxDistance) {
/* 253 */             candidates.add(possibleAlternative);
/*     */           }
/*     */         }
/* 256 */       });
/* 257 */       Collections.sort(candidates);
/* 258 */       return StringUtils.toStringArray(candidates);
/*     */     }
/*     */     
/*     */     public String buildErrorMessage()
/*     */     {
/* 263 */       String propertyName = getPropertyName();
/* 264 */       String[] possibleMatches = getPossibleMatches();
/* 265 */       StringBuilder msg = new StringBuilder();
/* 266 */       msg.append("Bean property '");
/* 267 */       msg.append(propertyName);
/* 268 */       msg.append("' has no matching field. ");
/*     */       
/* 270 */       if (!ObjectUtils.isEmpty(possibleMatches)) {
/* 271 */         appendHintMessage(msg);
/*     */       }
/* 273 */       return msg.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\PropertyMatches.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */