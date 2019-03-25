/*     */ package org.apache.catalina.valves.rewrite;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import org.apache.catalina.util.URLEncoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Substitution
/*     */ {
/*     */   public abstract class SubstitutionElement
/*     */   {
/*     */     public SubstitutionElement() {}
/*     */     
/*     */     public abstract String evaluate(Matcher paramMatcher1, Matcher paramMatcher2, Resolver paramResolver);
/*     */   }
/*     */   
/*     */   public class StaticElement
/*     */     extends Substitution.SubstitutionElement
/*     */   {
/*     */     public String value;
/*     */     
/*     */     public StaticElement()
/*     */     {
/*  31 */       super();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  36 */     public String evaluate(Matcher rule, Matcher cond, Resolver resolver) { return this.value; }
/*     */   }
/*     */   
/*     */   public class RewriteRuleBackReferenceElement extends Substitution.SubstitutionElement { public int n;
/*     */     
/*  41 */     public RewriteRuleBackReferenceElement() { super(); }
/*     */     
/*     */     public String evaluate(Matcher rule, Matcher cond, Resolver resolver)
/*     */     {
/*  45 */       String result = rule.group(this.n);
/*  46 */       if (result == null) {
/*  47 */         result = "";
/*     */       }
/*  49 */       if (Substitution.this.escapeBackReferences)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*  54 */         return URLEncoder.DEFAULT.encode(result, resolver.getUriCharset());
/*     */       }
/*  56 */       return result;
/*     */     } }
/*     */   
/*     */   public class RewriteCondBackReferenceElement extends Substitution.SubstitutionElement { public int n;
/*     */     
/*  61 */     public RewriteCondBackReferenceElement() { super(); }
/*     */     
/*     */ 
/*     */ 
/*  65 */     public String evaluate(Matcher rule, Matcher cond, Resolver resolver) { return cond.group(this.n) == null ? "" : cond.group(this.n); } }
/*     */   
/*     */   public class ServerVariableElement extends Substitution.SubstitutionElement { public String key;
/*     */     
/*  69 */     public ServerVariableElement() { super(); }
/*     */     
/*     */ 
/*     */ 
/*  73 */     public String evaluate(Matcher rule, Matcher cond, Resolver resolver) { return resolver.resolve(this.key); } }
/*     */   
/*     */   public class ServerVariableEnvElement extends Substitution.SubstitutionElement { public String key;
/*     */     
/*  77 */     public ServerVariableEnvElement() { super(); }
/*     */     
/*     */ 
/*     */ 
/*  81 */     public String evaluate(Matcher rule, Matcher cond, Resolver resolver) { return resolver.resolveEnv(this.key); } }
/*     */   
/*     */   public class ServerVariableSslElement extends Substitution.SubstitutionElement { public String key;
/*     */     
/*  85 */     public ServerVariableSslElement() { super(); }
/*     */     
/*     */ 
/*     */ 
/*  89 */     public String evaluate(Matcher rule, Matcher cond, Resolver resolver) { return resolver.resolveSsl(this.key); } }
/*     */   
/*     */   public class ServerVariableHttpElement extends Substitution.SubstitutionElement { public String key;
/*     */     
/*  93 */     public ServerVariableHttpElement() { super(); }
/*     */     
/*     */ 
/*     */ 
/*  97 */     public String evaluate(Matcher rule, Matcher cond, Resolver resolver) { return resolver.resolveHttp(this.key); }
/*     */   }
/*     */   
/*     */   public class MapElement extends Substitution.SubstitutionElement {
/* 101 */     public MapElement() { super(); }
/* 102 */     public RewriteMap map = null;
/*     */     public String key;
/* 104 */     public String defaultValue = "";
/*     */     public int n;
/*     */     
/*     */     public String evaluate(Matcher rule, Matcher cond, Resolver resolver) {
/* 108 */       String result = this.map.lookup(rule.group(this.n));
/* 109 */       if (result == null) {
/* 110 */         result = this.defaultValue;
/*     */       }
/* 112 */       return result;
/*     */     }
/*     */   }
/*     */   
/* 116 */   protected SubstitutionElement[] elements = null;
/*     */   
/* 118 */   protected String sub = null;
/* 119 */   public String getSub() { return this.sub; }
/* 120 */   public void setSub(String sub) { this.sub = sub; }
/*     */   
/*     */   void setEscapeBackReferences(boolean escapeBackReferences)
/*     */   {
/* 124 */     this.escapeBackReferences = escapeBackReferences;
/*     */   }
/*     */   
/*     */   public void parse(Map<String, RewriteMap> maps)
/*     */   {
/* 129 */     ArrayList<SubstitutionElement> elements = new ArrayList();
/* 130 */     int pos = 0;
/* 131 */     int percentPos = 0;
/* 132 */     int dollarPos = 0;
/* 133 */     int backslashPos = 0;
/*     */     
/* 135 */     while (pos < this.sub.length()) {
/* 136 */       percentPos = this.sub.indexOf('%', pos);
/* 137 */       dollarPos = this.sub.indexOf('$', pos);
/* 138 */       backslashPos = this.sub.indexOf('\\', pos);
/* 139 */       if ((percentPos == -1) && (dollarPos == -1) && (backslashPos == -1))
/*     */       {
/* 141 */         StaticElement newElement = new StaticElement();
/* 142 */         newElement.value = this.sub.substring(pos, this.sub.length());
/* 143 */         pos = this.sub.length();
/* 144 */         elements.add(newElement);
/* 145 */       } else if (isFirstPos(backslashPos, new int[] { dollarPos, percentPos })) {
/* 146 */         if (backslashPos + 1 == this.sub.length()) {
/* 147 */           throw new IllegalArgumentException(this.sub);
/*     */         }
/* 149 */         StaticElement newElement = new StaticElement();
/* 150 */         newElement.value = (this.sub.substring(pos, backslashPos) + this.sub.substring(backslashPos + 1, backslashPos + 2));
/* 151 */         pos = backslashPos + 2;
/* 152 */         elements.add(newElement);
/* 153 */       } else if (isFirstPos(dollarPos, new int[] { percentPos }))
/*     */       {
/* 155 */         if (dollarPos + 1 == this.sub.length()) {
/* 156 */           throw new IllegalArgumentException(this.sub);
/*     */         }
/* 158 */         if (pos < dollarPos)
/*     */         {
/* 160 */           StaticElement newElement = new StaticElement();
/* 161 */           newElement.value = this.sub.substring(pos, dollarPos);
/* 162 */           pos = dollarPos;
/* 163 */           elements.add(newElement);
/*     */         }
/* 165 */         if (Character.isDigit(this.sub.charAt(dollarPos + 1)))
/*     */         {
/* 167 */           RewriteRuleBackReferenceElement newElement = new RewriteRuleBackReferenceElement();
/* 168 */           newElement.n = Character.digit(this.sub.charAt(dollarPos + 1), 10);
/* 169 */           pos = dollarPos + 2;
/* 170 */           elements.add(newElement);
/* 171 */         } else if (this.sub.charAt(dollarPos + 1) == '{')
/*     */         {
/* 173 */           MapElement newElement = new MapElement();
/* 174 */           int open = this.sub.indexOf('{', dollarPos);
/* 175 */           int colon = this.sub.indexOf(':', dollarPos);
/* 176 */           int def = this.sub.indexOf('|', dollarPos);
/* 177 */           int close = this.sub.indexOf('}', dollarPos);
/* 178 */           if ((-1 >= open) || (open >= colon) || (colon >= close)) {
/* 179 */             throw new IllegalArgumentException(this.sub);
/*     */           }
/* 181 */           newElement.map = ((RewriteMap)maps.get(this.sub.substring(open + 1, colon)));
/* 182 */           if (newElement.map == null) {
/* 183 */             throw new IllegalArgumentException(this.sub + ": No map: " + this.sub.substring(open + 1, colon));
/*     */           }
/* 185 */           if (def > -1) {
/* 186 */             if ((colon >= def) || (def >= close)) {
/* 187 */               throw new IllegalArgumentException(this.sub);
/*     */             }
/* 189 */             newElement.key = this.sub.substring(colon + 1, def);
/* 190 */             newElement.defaultValue = this.sub.substring(def + 1, close);
/*     */           } else {
/* 192 */             newElement.key = this.sub.substring(colon + 1, close);
/*     */           }
/* 194 */           if (newElement.key.startsWith("$")) {
/* 195 */             newElement.n = Integer.parseInt(newElement.key.substring(1));
/*     */           }
/* 197 */           pos = close + 1;
/* 198 */           elements.add(newElement);
/*     */         } else {
/* 200 */           throw new IllegalArgumentException(this.sub + ": missing digit or curly brace.");
/*     */         }
/*     */       }
/*     */       else {
/* 204 */         if (percentPos + 1 == this.sub.length()) {
/* 205 */           throw new IllegalArgumentException(this.sub);
/*     */         }
/* 207 */         if (pos < percentPos)
/*     */         {
/* 209 */           StaticElement newElement = new StaticElement();
/* 210 */           newElement.value = this.sub.substring(pos, percentPos);
/* 211 */           pos = percentPos;
/* 212 */           elements.add(newElement);
/*     */         }
/* 214 */         if (Character.isDigit(this.sub.charAt(percentPos + 1)))
/*     */         {
/* 216 */           RewriteCondBackReferenceElement newElement = new RewriteCondBackReferenceElement();
/* 217 */           newElement.n = Character.digit(this.sub.charAt(percentPos + 1), 10);
/* 218 */           pos = percentPos + 2;
/* 219 */           elements.add(newElement);
/* 220 */         } else if (this.sub.charAt(percentPos + 1) == '{')
/*     */         {
/* 222 */           SubstitutionElement newElement = null;
/* 223 */           int open = this.sub.indexOf('{', percentPos);
/* 224 */           int colon = this.sub.indexOf(':', percentPos);
/* 225 */           int close = this.sub.indexOf('}', percentPos);
/* 226 */           if ((-1 >= open) || (open >= close)) {
/* 227 */             throw new IllegalArgumentException(this.sub);
/*     */           }
/* 229 */           if ((colon > -1) && (open < colon) && (colon < close)) {
/* 230 */             String type = this.sub.substring(open + 1, colon);
/* 231 */             if (type.equals("ENV")) {
/* 232 */               newElement = new ServerVariableEnvElement();
/* 233 */               ((ServerVariableEnvElement)newElement).key = this.sub.substring(colon + 1, close);
/* 234 */             } else if (type.equals("SSL")) {
/* 235 */               newElement = new ServerVariableSslElement();
/* 236 */               ((ServerVariableSslElement)newElement).key = this.sub.substring(colon + 1, close);
/* 237 */             } else if (type.equals("HTTP")) {
/* 238 */               newElement = new ServerVariableHttpElement();
/* 239 */               ((ServerVariableHttpElement)newElement).key = this.sub.substring(colon + 1, close);
/*     */             } else {
/* 241 */               throw new IllegalArgumentException(this.sub + ": Bad type: " + type);
/*     */             }
/*     */           } else {
/* 244 */             newElement = new ServerVariableElement();
/* 245 */             ((ServerVariableElement)newElement).key = this.sub.substring(open + 1, close);
/*     */           }
/* 247 */           pos = close + 1;
/* 248 */           elements.add(newElement);
/*     */         } else {
/* 250 */           throw new IllegalArgumentException(this.sub + ": missing digit or curly brace.");
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 255 */     this.elements = ((SubstitutionElement[])elements.toArray(new SubstitutionElement[0]));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String evaluate(Matcher rule, Matcher cond, Resolver resolver)
/*     */   {
/* 267 */     StringBuffer buf = new StringBuffer();
/* 268 */     for (int i = 0; i < this.elements.length; i++) {
/* 269 */       buf.append(this.elements[i].evaluate(rule, cond, resolver));
/*     */     }
/* 271 */     return buf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean escapeBackReferences;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isFirstPos(int testPos, int... others)
/*     */   {
/* 287 */     if (testPos < 0) {
/* 288 */       return false;
/*     */     }
/* 290 */     for (int other : others) {
/* 291 */       if ((other >= 0) && (other < testPos)) {
/* 292 */         return false;
/*     */       }
/*     */     }
/* 295 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\valves\rewrite\Substitution.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */