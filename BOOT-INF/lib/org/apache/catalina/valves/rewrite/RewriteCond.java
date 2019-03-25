/*     */ package org.apache.catalina.valves.rewrite;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RewriteCond
/*     */ {
/*     */   public static abstract class Condition
/*     */   {
/*     */     public abstract boolean evaluate(String paramString, Resolver paramResolver);
/*     */   }
/*     */   
/*     */   public static class PatternCondition
/*     */     extends RewriteCond.Condition
/*     */   {
/*     */     public Pattern pattern;
/*  31 */     public Matcher matcher = null;
/*     */     
/*     */     public boolean evaluate(String value, Resolver resolver)
/*     */     {
/*  35 */       Matcher m = this.pattern.matcher(value);
/*  36 */       if (m.matches()) {
/*  37 */         this.matcher = m;
/*  38 */         return true;
/*     */       }
/*  40 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class LexicalCondition
/*     */     extends RewriteCond.Condition
/*     */   {
/*  51 */     public int type = 0;
/*     */     public String condition;
/*     */     
/*     */     public boolean evaluate(String value, Resolver resolver)
/*     */     {
/*  56 */       int result = value.compareTo(this.condition);
/*  57 */       switch (this.type) {
/*     */       case -1: 
/*  59 */         return result < 0;
/*     */       case 0: 
/*  61 */         return result == 0;
/*     */       case 1: 
/*  63 */         return result > 0;
/*     */       }
/*  65 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class ResourceCondition
/*     */     extends RewriteCond.Condition
/*     */   {
/*  77 */     public int type = 0;
/*     */     
/*     */     public boolean evaluate(String value, Resolver resolver)
/*     */     {
/*  81 */       return resolver.resolveResource(this.type, value);
/*     */     }
/*     */   }
/*     */   
/*  85 */   protected String testString = null;
/*  86 */   protected String condPattern = null;
/*     */   
/*     */   public String getCondPattern() {
/*  89 */     return this.condPattern;
/*     */   }
/*     */   
/*     */   public void setCondPattern(String condPattern) {
/*  93 */     this.condPattern = condPattern;
/*     */   }
/*     */   
/*     */   public String getTestString() {
/*  97 */     return this.testString;
/*     */   }
/*     */   
/*     */   public void setTestString(String testString) {
/* 101 */     this.testString = testString;
/*     */   }
/*     */   
/*     */   public void parse(Map<String, RewriteMap> maps) {
/* 105 */     this.test = new Substitution();
/* 106 */     this.test.setSub(this.testString);
/* 107 */     this.test.parse(maps);
/* 108 */     if (this.condPattern.startsWith("!")) {
/* 109 */       this.positive = false;
/* 110 */       this.condPattern = this.condPattern.substring(1);
/*     */     }
/* 112 */     if (this.condPattern.startsWith("<")) {
/* 113 */       LexicalCondition condition = new LexicalCondition();
/* 114 */       condition.type = -1;
/* 115 */       condition.condition = this.condPattern.substring(1);
/* 116 */     } else if (this.condPattern.startsWith(">")) {
/* 117 */       LexicalCondition condition = new LexicalCondition();
/* 118 */       condition.type = 1;
/* 119 */       condition.condition = this.condPattern.substring(1);
/* 120 */     } else if (this.condPattern.startsWith("=")) {
/* 121 */       LexicalCondition condition = new LexicalCondition();
/* 122 */       condition.type = 0;
/* 123 */       condition.condition = this.condPattern.substring(1);
/* 124 */     } else if (this.condPattern.equals("-d")) {
/* 125 */       ResourceCondition ncondition = new ResourceCondition();
/* 126 */       ncondition.type = 0;
/* 127 */     } else if (this.condPattern.equals("-f")) {
/* 128 */       ResourceCondition ncondition = new ResourceCondition();
/* 129 */       ncondition.type = 1;
/* 130 */     } else if (this.condPattern.equals("-s")) {
/* 131 */       ResourceCondition ncondition = new ResourceCondition();
/* 132 */       ncondition.type = 2;
/*     */     } else {
/* 134 */       PatternCondition condition = new PatternCondition();
/* 135 */       int flags = 0;
/* 136 */       if (isNocase()) {
/* 137 */         flags |= 0x2;
/*     */       }
/* 139 */       condition.pattern = Pattern.compile(this.condPattern, flags);
/*     */     }
/*     */   }
/*     */   
/*     */   public Matcher getMatcher() {
/* 144 */     Object condition = this.condition.get();
/* 145 */     if ((condition instanceof PatternCondition)) {
/* 146 */       return ((PatternCondition)condition).matcher;
/*     */     }
/* 148 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 157 */     return "RewriteCond " + this.testString + " " + this.condPattern;
/*     */   }
/*     */   
/*     */ 
/* 161 */   protected boolean positive = true;
/*     */   
/* 163 */   protected Substitution test = null;
/*     */   
/* 165 */   protected ThreadLocal<Condition> condition = new ThreadLocal();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 173 */   public boolean nocase = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 178 */   public boolean ornext = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean evaluate(Matcher rule, Matcher cond, Resolver resolver)
/*     */   {
/* 189 */     String value = this.test.evaluate(rule, cond, resolver);
/* 190 */     Condition condition = (Condition)this.condition.get();
/* 191 */     if (condition == null) {
/* 192 */       if (this.condPattern.startsWith("<")) {
/* 193 */         LexicalCondition ncondition = new LexicalCondition();
/* 194 */         ncondition.type = -1;
/* 195 */         ncondition.condition = this.condPattern.substring(1);
/* 196 */         condition = ncondition;
/* 197 */       } else if (this.condPattern.startsWith(">")) {
/* 198 */         LexicalCondition ncondition = new LexicalCondition();
/* 199 */         ncondition.type = 1;
/* 200 */         ncondition.condition = this.condPattern.substring(1);
/* 201 */         condition = ncondition;
/* 202 */       } else if (this.condPattern.startsWith("=")) {
/* 203 */         LexicalCondition ncondition = new LexicalCondition();
/* 204 */         ncondition.type = 0;
/* 205 */         ncondition.condition = this.condPattern.substring(1);
/* 206 */         condition = ncondition;
/* 207 */       } else if (this.condPattern.equals("-d")) {
/* 208 */         ResourceCondition ncondition = new ResourceCondition();
/* 209 */         ncondition.type = 0;
/* 210 */         condition = ncondition;
/* 211 */       } else if (this.condPattern.equals("-f")) {
/* 212 */         ResourceCondition ncondition = new ResourceCondition();
/* 213 */         ncondition.type = 1;
/* 214 */         condition = ncondition;
/* 215 */       } else if (this.condPattern.equals("-s")) {
/* 216 */         ResourceCondition ncondition = new ResourceCondition();
/* 217 */         ncondition.type = 2;
/* 218 */         condition = ncondition;
/*     */       } else {
/* 220 */         PatternCondition ncondition = new PatternCondition();
/* 221 */         int flags = 0;
/* 222 */         if (isNocase()) {
/* 223 */           flags |= 0x2;
/*     */         }
/* 225 */         ncondition.pattern = Pattern.compile(this.condPattern, flags);
/* 226 */         condition = ncondition;
/*     */       }
/* 228 */       this.condition.set(condition);
/*     */     }
/* 230 */     if (this.positive) {
/* 231 */       return condition.evaluate(value, resolver);
/*     */     }
/* 233 */     return !condition.evaluate(value, resolver);
/*     */   }
/*     */   
/*     */   public boolean isNocase()
/*     */   {
/* 238 */     return this.nocase;
/*     */   }
/*     */   
/*     */   public void setNocase(boolean nocase) {
/* 242 */     this.nocase = nocase;
/*     */   }
/*     */   
/*     */   public boolean isOrnext() {
/* 246 */     return this.ornext;
/*     */   }
/*     */   
/*     */   public void setOrnext(boolean ornext) {
/* 250 */     this.ornext = ornext;
/*     */   }
/*     */   
/*     */   public boolean isPositive() {
/* 254 */     return this.positive;
/*     */   }
/*     */   
/*     */   public void setPositive(boolean positive) {
/* 258 */     this.positive = positive;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\valves\rewrite\RewriteCond.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */