/*     */ package org.springframework.boot.bind;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public final class RelaxedNames
/*     */   implements Iterable<String>
/*     */ {
/*  37 */   private static final Pattern CAMEL_CASE_PATTERN = Pattern.compile("([^A-Z-])([A-Z])");
/*     */   
/*     */ 
/*  40 */   private static final Pattern SEPARATED_TO_CAMEL_CASE_PATTERN = Pattern.compile("[_\\-.]");
/*     */   
/*     */   private final String name;
/*     */   
/*  44 */   private final Set<String> values = new LinkedHashSet();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RelaxedNames(String name)
/*     */   {
/*  52 */     this.name = (name == null ? "" : name);
/*  53 */     initialize(this.name, this.values);
/*     */   }
/*     */   
/*     */   public Iterator<String> iterator()
/*     */   {
/*  58 */     return this.values.iterator();
/*     */   }
/*     */   
/*     */   private void initialize(String name, Set<String> values) {
/*  62 */     if (values.contains(name)) {
/*  63 */       return;
/*     */     }
/*  65 */     for (Variation variation : Variation.values()) {
/*  66 */       for (Manipulation manipulation : Manipulation.values()) {
/*  67 */         String result = name;
/*  68 */         result = manipulation.apply(result);
/*  69 */         result = variation.apply(result);
/*  70 */         values.add(result);
/*  71 */         initialize(result, values);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static abstract enum Variation
/*     */   {
/*  81 */     NONE, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  90 */     LOWERCASE, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  99 */     UPPERCASE;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Variation() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract String apply(String paramString);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static abstract enum Manipulation
/*     */   {
/* 117 */     NONE, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 126 */     HYPHEN_TO_UNDERSCORE, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 135 */     UNDERSCORE_TO_PERIOD, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 144 */     PERIOD_TO_UNDERSCORE, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 153 */     CAMELCASE_TO_UNDERSCORE, 
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
/* 176 */     CAMELCASE_TO_HYPHEN, 
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
/* 199 */     SEPARATED_TO_CAMELCASE, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 208 */     CASE_INSENSITIVE_SEPARATED_TO_CAMELCASE;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 217 */     private static final char[] SUFFIXES = { '_', '-', '.' };
/*     */     
/*     */     private Manipulation() {}
/*     */     
/*     */     public abstract String apply(String paramString);
/*     */     
/* 223 */     private static String separatedToCamelCase(String value, boolean caseInsensitive) { if (value.isEmpty()) {
/* 224 */         return value;
/*     */       }
/* 226 */       StringBuilder builder = new StringBuilder();
/* 227 */       String[] arrayOfString = RelaxedNames.SEPARATED_TO_CAMEL_CASE_PATTERN.split(value);int i = arrayOfString.length; for (String str1 = 0; str1 < i; str1++) { field = arrayOfString[str1];
/* 228 */         field = caseInsensitive ? field.toLowerCase() : field;
/* 229 */         builder.append(builder
/* 230 */           .length() == 0 ? field : StringUtils.capitalize(field));
/*     */       }
/* 232 */       char lastChar = value.charAt(value.length() - 1);
/* 233 */       char[] arrayOfChar = SUFFIXES;str1 = arrayOfChar.length; for (String field = 0; field < str1; field++) { char suffix = arrayOfChar[field];
/* 234 */         if (lastChar == suffix) {
/* 235 */           builder.append(suffix);
/* 236 */           break;
/*     */         }
/*     */       }
/* 239 */       return builder.toString();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static RelaxedNames forCamelCase(String name)
/*     */   {
/* 250 */     StringBuilder result = new StringBuilder();
/* 251 */     for (char c : name.toCharArray()) {
/* 252 */       result.append((Character.isUpperCase(c)) && (result.length() > 0) && 
/* 253 */         (result.charAt(result.length() - 1) != '-') ? "-" + 
/* 254 */         Character.toLowerCase(c) : Character.valueOf(c));
/*     */     }
/* 256 */     return new RelaxedNames(result.toString());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\bind\RelaxedNames.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */