/*     */ package org.apache.catalina.valves.rewrite;
/*     */ 
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RewriteRule
/*     */ {
/*  26 */   protected RewriteCond[] conditions = new RewriteCond[0];
/*     */   
/*  28 */   protected ThreadLocal<Pattern> pattern = new ThreadLocal();
/*  29 */   protected Substitution substitution = null;
/*     */   
/*  31 */   protected String patternString = null;
/*  32 */   protected String substitutionString = null;
/*     */   
/*     */   public void parse(Map<String, RewriteMap> maps)
/*     */   {
/*  36 */     if (!"-".equals(this.substitutionString)) {
/*  37 */       this.substitution = new Substitution();
/*  38 */       this.substitution.setSub(this.substitutionString);
/*  39 */       this.substitution.parse(maps);
/*  40 */       this.substitution.setEscapeBackReferences(isEscapeBackReferences());
/*     */     }
/*     */     
/*  43 */     int flags = 0;
/*  44 */     if (isNocase()) {
/*  45 */       flags |= 0x2;
/*     */     }
/*  47 */     Pattern.compile(this.patternString, flags);
/*     */     
/*  49 */     for (int i = 0; i < this.conditions.length; i++) {
/*  50 */       this.conditions[i].parse(maps);
/*     */     }
/*     */     
/*  53 */     if (isEnv()) {
/*  54 */       for (int i = 0; i < this.envValue.size(); i++) {
/*  55 */         Substitution newEnvSubstitution = new Substitution();
/*  56 */         newEnvSubstitution.setSub((String)this.envValue.get(i));
/*  57 */         newEnvSubstitution.parse(maps);
/*  58 */         this.envSubstitution.add(newEnvSubstitution);
/*  59 */         this.envResult.add(new ThreadLocal());
/*     */       }
/*     */     }
/*  62 */     if (isCookie()) {
/*  63 */       this.cookieSubstitution = new Substitution();
/*  64 */       this.cookieSubstitution.setSub(this.cookieValue);
/*  65 */       this.cookieSubstitution.parse(maps);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addCondition(RewriteCond condition) {
/*  70 */     RewriteCond[] conditions = new RewriteCond[this.conditions.length + 1];
/*  71 */     for (int i = 0; i < this.conditions.length; i++) {
/*  72 */       conditions[i] = this.conditions[i];
/*     */     }
/*  74 */     conditions[this.conditions.length] = condition;
/*  75 */     this.conditions = conditions;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CharSequence evaluate(CharSequence url, Resolver resolver)
/*     */   {
/*  85 */     Pattern pattern = (Pattern)this.pattern.get();
/*  86 */     if (pattern == null)
/*     */     {
/*  88 */       int flags = 0;
/*  89 */       if (isNocase()) {
/*  90 */         flags |= 0x2;
/*     */       }
/*  92 */       pattern = Pattern.compile(this.patternString, flags);
/*  93 */       this.pattern.set(pattern);
/*     */     }
/*  95 */     Matcher matcher = pattern.matcher(url);
/*  96 */     if (!matcher.matches())
/*     */     {
/*  98 */       return null;
/*     */     }
/*     */     
/* 101 */     boolean done = false;
/* 102 */     boolean rewrite = true;
/* 103 */     Matcher lastMatcher = null;
/* 104 */     int pos = 0;
/* 105 */     while (!done) {
/* 106 */       if (pos < this.conditions.length) {
/* 107 */         rewrite = this.conditions[pos].evaluate(matcher, lastMatcher, resolver);
/* 108 */         if (rewrite) {
/* 109 */           Matcher lastMatcher2 = this.conditions[pos].getMatcher();
/* 110 */           if (lastMatcher2 != null) {
/* 111 */             lastMatcher = lastMatcher2;
/*     */           }
/* 113 */           while ((pos < this.conditions.length) && (this.conditions[pos].isOrnext())) {
/* 114 */             pos++;
/*     */           }
/* 116 */         } else if (!this.conditions[pos].isOrnext()) {
/* 117 */           done = true;
/*     */         }
/* 119 */         pos++;
/*     */       } else {
/* 121 */         done = true;
/*     */       }
/*     */     }
/*     */     
/* 125 */     if (rewrite) {
/* 126 */       if (isEnv()) {
/* 127 */         for (int i = 0; i < this.envSubstitution.size(); i++) {
/* 128 */           ((ThreadLocal)this.envResult.get(i)).set(((Substitution)this.envSubstitution.get(i)).evaluate(matcher, lastMatcher, resolver));
/*     */         }
/*     */       }
/* 131 */       if (isCookie()) {
/* 132 */         this.cookieResult.set(this.cookieSubstitution.evaluate(matcher, lastMatcher, resolver));
/*     */       }
/* 134 */       if (this.substitution != null) {
/* 135 */         return this.substitution.evaluate(matcher, lastMatcher, resolver);
/*     */       }
/* 137 */       return url;
/*     */     }
/*     */     
/* 140 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 151 */     return "RewriteRule " + this.patternString + " " + this.substitutionString;
/*     */   }
/*     */   
/*     */ 
/* 155 */   private boolean escapeBackReferences = false;
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
/* 166 */   protected boolean chain = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 175 */   protected boolean cookie = false;
/* 176 */   protected String cookieName = null;
/* 177 */   protected String cookieValue = null;
/* 178 */   protected String cookieDomain = null;
/* 179 */   protected int cookieLifetime = -1;
/* 180 */   protected String cookiePath = null;
/* 181 */   protected boolean cookieSecure = false;
/* 182 */   protected boolean cookieHttpOnly = false;
/* 183 */   protected Substitution cookieSubstitution = null;
/* 184 */   protected ThreadLocal<String> cookieResult = new ThreadLocal();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 191 */   protected boolean env = false;
/* 192 */   protected ArrayList<String> envName = new ArrayList();
/* 193 */   protected ArrayList<String> envValue = new ArrayList();
/* 194 */   protected ArrayList<Substitution> envSubstitution = new ArrayList();
/* 195 */   protected ArrayList<ThreadLocal<String>> envResult = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 202 */   protected boolean forbidden = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 209 */   protected boolean gone = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 216 */   protected boolean host = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 225 */   protected boolean last = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 235 */   protected boolean next = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 242 */   protected boolean nocase = false;
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
/* 254 */   protected boolean noescape = false;
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
/* 268 */   protected boolean nosubreq = false;
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
/* 284 */   protected boolean qsappend = false;
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
/* 302 */   protected boolean redirect = false;
/* 303 */   protected int redirectCode = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 312 */   protected int skip = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 321 */   protected boolean type = false;
/* 322 */   protected String typeValue = null;
/*     */   
/*     */   public boolean isEscapeBackReferences() {
/* 325 */     return this.escapeBackReferences;
/*     */   }
/*     */   
/* 328 */   public void setEscapeBackReferences(boolean escapeBackReferences) { this.escapeBackReferences = escapeBackReferences; }
/*     */   
/*     */   public boolean isChain() {
/* 331 */     return this.chain;
/*     */   }
/*     */   
/* 334 */   public void setChain(boolean chain) { this.chain = chain; }
/*     */   
/*     */   public RewriteCond[] getConditions() {
/* 337 */     return this.conditions;
/*     */   }
/*     */   
/* 340 */   public void setConditions(RewriteCond[] conditions) { this.conditions = conditions; }
/*     */   
/*     */   public boolean isCookie() {
/* 343 */     return this.cookie;
/*     */   }
/*     */   
/* 346 */   public void setCookie(boolean cookie) { this.cookie = cookie; }
/*     */   
/*     */   public String getCookieName() {
/* 349 */     return this.cookieName;
/*     */   }
/*     */   
/* 352 */   public void setCookieName(String cookieName) { this.cookieName = cookieName; }
/*     */   
/*     */   public String getCookieValue() {
/* 355 */     return this.cookieValue;
/*     */   }
/*     */   
/* 358 */   public void setCookieValue(String cookieValue) { this.cookieValue = cookieValue; }
/*     */   
/*     */   public String getCookieResult() {
/* 361 */     return (String)this.cookieResult.get();
/*     */   }
/*     */   
/* 364 */   public boolean isEnv() { return this.env; }
/*     */   
/*     */   public int getEnvSize() {
/* 367 */     return this.envName.size();
/*     */   }
/*     */   
/* 370 */   public void setEnv(boolean env) { this.env = env; }
/*     */   
/*     */   public String getEnvName(int i) {
/* 373 */     return (String)this.envName.get(i);
/*     */   }
/*     */   
/* 376 */   public void addEnvName(String envName) { this.envName.add(envName); }
/*     */   
/*     */   public String getEnvValue(int i) {
/* 379 */     return (String)this.envValue.get(i);
/*     */   }
/*     */   
/* 382 */   public void addEnvValue(String envValue) { this.envValue.add(envValue); }
/*     */   
/*     */   public String getEnvResult(int i) {
/* 385 */     return (String)((ThreadLocal)this.envResult.get(i)).get();
/*     */   }
/*     */   
/* 388 */   public boolean isForbidden() { return this.forbidden; }
/*     */   
/*     */   public void setForbidden(boolean forbidden) {
/* 391 */     this.forbidden = forbidden;
/*     */   }
/*     */   
/* 394 */   public boolean isGone() { return this.gone; }
/*     */   
/*     */   public void setGone(boolean gone) {
/* 397 */     this.gone = gone;
/*     */   }
/*     */   
/* 400 */   public boolean isLast() { return this.last; }
/*     */   
/*     */   public void setLast(boolean last) {
/* 403 */     this.last = last;
/*     */   }
/*     */   
/* 406 */   public boolean isNext() { return this.next; }
/*     */   
/*     */   public void setNext(boolean next) {
/* 409 */     this.next = next;
/*     */   }
/*     */   
/* 412 */   public boolean isNocase() { return this.nocase; }
/*     */   
/*     */   public void setNocase(boolean nocase) {
/* 415 */     this.nocase = nocase;
/*     */   }
/*     */   
/* 418 */   public boolean isNoescape() { return this.noescape; }
/*     */   
/*     */   public void setNoescape(boolean noescape) {
/* 421 */     this.noescape = noescape;
/*     */   }
/*     */   
/* 424 */   public boolean isNosubreq() { return this.nosubreq; }
/*     */   
/*     */   public void setNosubreq(boolean nosubreq) {
/* 427 */     this.nosubreq = nosubreq;
/*     */   }
/*     */   
/* 430 */   public boolean isQsappend() { return this.qsappend; }
/*     */   
/*     */   public void setQsappend(boolean qsappend) {
/* 433 */     this.qsappend = qsappend;
/*     */   }
/*     */   
/* 436 */   public boolean isRedirect() { return this.redirect; }
/*     */   
/*     */   public void setRedirect(boolean redirect) {
/* 439 */     this.redirect = redirect;
/*     */   }
/*     */   
/* 442 */   public int getRedirectCode() { return this.redirectCode; }
/*     */   
/*     */   public void setRedirectCode(int redirectCode) {
/* 445 */     this.redirectCode = redirectCode;
/*     */   }
/*     */   
/* 448 */   public int getSkip() { return this.skip; }
/*     */   
/*     */   public void setSkip(int skip) {
/* 451 */     this.skip = skip;
/*     */   }
/*     */   
/* 454 */   public Substitution getSubstitution() { return this.substitution; }
/*     */   
/*     */   public void setSubstitution(Substitution substitution) {
/* 457 */     this.substitution = substitution;
/*     */   }
/*     */   
/* 460 */   public boolean isType() { return this.type; }
/*     */   
/*     */   public void setType(boolean type) {
/* 463 */     this.type = type;
/*     */   }
/*     */   
/* 466 */   public String getTypeValue() { return this.typeValue; }
/*     */   
/*     */   public void setTypeValue(String typeValue) {
/* 469 */     this.typeValue = typeValue;
/*     */   }
/*     */   
/*     */   public String getPatternString() {
/* 473 */     return this.patternString;
/*     */   }
/*     */   
/*     */   public void setPatternString(String patternString) {
/* 477 */     this.patternString = patternString;
/*     */   }
/*     */   
/*     */   public String getSubstitutionString() {
/* 481 */     return this.substitutionString;
/*     */   }
/*     */   
/*     */   public void setSubstitutionString(String substitutionString) {
/* 485 */     this.substitutionString = substitutionString;
/*     */   }
/*     */   
/*     */   public boolean isHost() {
/* 489 */     return this.host;
/*     */   }
/*     */   
/*     */   public void setHost(boolean host) {
/* 493 */     this.host = host;
/*     */   }
/*     */   
/*     */   public String getCookieDomain() {
/* 497 */     return this.cookieDomain;
/*     */   }
/*     */   
/*     */   public void setCookieDomain(String cookieDomain) {
/* 501 */     this.cookieDomain = cookieDomain;
/*     */   }
/*     */   
/*     */   public int getCookieLifetime() {
/* 505 */     return this.cookieLifetime;
/*     */   }
/*     */   
/*     */   public void setCookieLifetime(int cookieLifetime) {
/* 509 */     this.cookieLifetime = cookieLifetime;
/*     */   }
/*     */   
/*     */   public String getCookiePath() {
/* 513 */     return this.cookiePath;
/*     */   }
/*     */   
/*     */   public void setCookiePath(String cookiePath) {
/* 517 */     this.cookiePath = cookiePath;
/*     */   }
/*     */   
/*     */   public boolean isCookieSecure() {
/* 521 */     return this.cookieSecure;
/*     */   }
/*     */   
/*     */   public void setCookieSecure(boolean cookieSecure) {
/* 525 */     this.cookieSecure = cookieSecure;
/*     */   }
/*     */   
/*     */   public boolean isCookieHttpOnly() {
/* 529 */     return this.cookieHttpOnly;
/*     */   }
/*     */   
/*     */   public void setCookieHttpOnly(boolean cookieHttpOnly) {
/* 533 */     this.cookieHttpOnly = cookieHttpOnly;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\valves\rewrite\RewriteRule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */