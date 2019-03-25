/*     */ package org.apache.tomcat.util.descriptor.web;
/*     */ 
/*     */ import org.apache.tomcat.util.digester.Digester;
/*     */ import org.apache.tomcat.util.digester.RuleSetBase;
/*     */ import org.apache.tomcat.util.digester.SetNextRule;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WebRuleSet
/*     */   extends RuleSetBase
/*     */ {
/*  46 */   protected static final StringManager sm = StringManager.getManager(Constants.PACKAGE_NAME);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String prefix;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String fullPrefix;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean fragment;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  71 */   protected final SetSessionConfig sessionConfig = new SetSessionConfig();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  77 */   protected final SetLoginConfig loginConfig = new SetLoginConfig();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  83 */   protected final SetJspConfig jspConfig = new SetJspConfig();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  89 */   protected final NameRule name = new NameRule();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final AbsoluteOrderingRule absoluteOrdering;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final RelativeOrderingRule relativeOrdering;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebRuleSet()
/*     */   {
/* 114 */     this("", false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WebRuleSet(boolean fragment)
/*     */   {
/* 126 */     this("", fragment);
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
/*     */   public WebRuleSet(String prefix, boolean fragment)
/*     */   {
/* 141 */     this.prefix = prefix;
/* 142 */     this.fragment = fragment;
/*     */     
/* 144 */     if (fragment) {
/* 145 */       this.fullPrefix = (prefix + "web-fragment");
/*     */     } else {
/* 147 */       this.fullPrefix = (prefix + "web-app");
/*     */     }
/*     */     
/* 150 */     this.absoluteOrdering = new AbsoluteOrderingRule(fragment);
/* 151 */     this.relativeOrdering = new RelativeOrderingRule(fragment);
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
/*     */ 
/*     */ 
/*     */   public void addRuleInstances(Digester digester)
/*     */   {
/* 168 */     digester.addRule(this.fullPrefix, new SetPublicIdRule("setPublicId"));
/*     */     
/* 170 */     digester.addRule(this.fullPrefix, new IgnoreAnnotationsRule());
/*     */     
/* 172 */     digester.addRule(this.fullPrefix, new VersionRule());
/*     */     
/*     */ 
/*     */ 
/* 176 */     digester.addRule(this.fullPrefix + "/absolute-ordering", this.absoluteOrdering);
/* 177 */     digester.addRule(this.fullPrefix + "/ordering", this.relativeOrdering);
/*     */     
/* 179 */     if (this.fragment)
/*     */     {
/* 181 */       digester.addRule(this.fullPrefix + "/name", this.name);
/* 182 */       digester.addCallMethod(this.fullPrefix + "/ordering/after/name", "addAfterOrdering", 0);
/*     */       
/* 184 */       digester.addCallMethod(this.fullPrefix + "/ordering/after/others", "addAfterOrderingOthers");
/*     */       
/* 186 */       digester.addCallMethod(this.fullPrefix + "/ordering/before/name", "addBeforeOrdering", 0);
/*     */       
/* 188 */       digester.addCallMethod(this.fullPrefix + "/ordering/before/others", "addBeforeOrderingOthers");
/*     */     }
/*     */     else
/*     */     {
/* 192 */       digester.addCallMethod(this.fullPrefix + "/absolute-ordering/name", "addAbsoluteOrdering", 0);
/*     */       
/* 194 */       digester.addCallMethod(this.fullPrefix + "/absolute-ordering/others", "addAbsoluteOrderingOthers");
/*     */       
/* 196 */       digester.addRule(this.fullPrefix + "/deny-uncovered-http-methods", new SetDenyUncoveredHttpMethodsRule());
/*     */     }
/*     */     
/*     */ 
/* 200 */     digester.addCallMethod(this.fullPrefix + "/context-param", "addContextParam", 2);
/*     */     
/* 202 */     digester.addCallParam(this.fullPrefix + "/context-param/param-name", 0);
/* 203 */     digester.addCallParam(this.fullPrefix + "/context-param/param-value", 1);
/*     */     
/* 205 */     digester.addCallMethod(this.fullPrefix + "/display-name", "setDisplayName", 0);
/*     */     
/*     */ 
/* 208 */     digester.addRule(this.fullPrefix + "/distributable", new SetDistributableRule());
/*     */     
/*     */ 
/* 211 */     configureNamingRules(digester);
/*     */     
/* 213 */     digester.addObjectCreate(this.fullPrefix + "/error-page", "org.apache.tomcat.util.descriptor.web.ErrorPage");
/*     */     
/* 215 */     digester.addSetNext(this.fullPrefix + "/error-page", "addErrorPage", "org.apache.tomcat.util.descriptor.web.ErrorPage");
/*     */     
/*     */ 
/*     */ 
/* 219 */     digester.addCallMethod(this.fullPrefix + "/error-page/error-code", "setErrorCode", 0);
/*     */     
/* 221 */     digester.addCallMethod(this.fullPrefix + "/error-page/exception-type", "setExceptionType", 0);
/*     */     
/* 223 */     digester.addCallMethod(this.fullPrefix + "/error-page/location", "setLocation", 0);
/*     */     
/*     */ 
/* 226 */     digester.addObjectCreate(this.fullPrefix + "/filter", "org.apache.tomcat.util.descriptor.web.FilterDef");
/*     */     
/* 228 */     digester.addSetNext(this.fullPrefix + "/filter", "addFilter", "org.apache.tomcat.util.descriptor.web.FilterDef");
/*     */     
/*     */ 
/*     */ 
/* 232 */     digester.addCallMethod(this.fullPrefix + "/filter/description", "setDescription", 0);
/*     */     
/* 234 */     digester.addCallMethod(this.fullPrefix + "/filter/display-name", "setDisplayName", 0);
/*     */     
/* 236 */     digester.addCallMethod(this.fullPrefix + "/filter/filter-class", "setFilterClass", 0);
/*     */     
/* 238 */     digester.addCallMethod(this.fullPrefix + "/filter/filter-name", "setFilterName", 0);
/*     */     
/* 240 */     digester.addCallMethod(this.fullPrefix + "/filter/icon/large-icon", "setLargeIcon", 0);
/*     */     
/* 242 */     digester.addCallMethod(this.fullPrefix + "/filter/icon/small-icon", "setSmallIcon", 0);
/*     */     
/* 244 */     digester.addCallMethod(this.fullPrefix + "/filter/async-supported", "setAsyncSupported", 0);
/*     */     
/*     */ 
/* 247 */     digester.addCallMethod(this.fullPrefix + "/filter/init-param", "addInitParameter", 2);
/*     */     
/* 249 */     digester.addCallParam(this.fullPrefix + "/filter/init-param/param-name", 0);
/*     */     
/* 251 */     digester.addCallParam(this.fullPrefix + "/filter/init-param/param-value", 1);
/*     */     
/*     */ 
/* 254 */     digester.addObjectCreate(this.fullPrefix + "/filter-mapping", "org.apache.tomcat.util.descriptor.web.FilterMap");
/*     */     
/* 256 */     digester.addSetNext(this.fullPrefix + "/filter-mapping", "addFilterMapping", "org.apache.tomcat.util.descriptor.web.FilterMap");
/*     */     
/*     */ 
/*     */ 
/* 260 */     digester.addCallMethod(this.fullPrefix + "/filter-mapping/filter-name", "setFilterName", 0);
/*     */     
/* 262 */     digester.addCallMethod(this.fullPrefix + "/filter-mapping/servlet-name", "addServletName", 0);
/*     */     
/* 264 */     digester.addCallMethod(this.fullPrefix + "/filter-mapping/url-pattern", "addURLPattern", 0);
/*     */     
/*     */ 
/* 267 */     digester.addCallMethod(this.fullPrefix + "/filter-mapping/dispatcher", "setDispatcher", 0);
/*     */     
/*     */ 
/* 270 */     digester.addCallMethod(this.fullPrefix + "/listener/listener-class", "addListener", 0);
/*     */     
/*     */ 
/* 273 */     digester.addRule(this.fullPrefix + "/jsp-config", this.jspConfig);
/*     */     
/*     */ 
/* 276 */     digester.addObjectCreate(this.fullPrefix + "/jsp-config/jsp-property-group", "org.apache.tomcat.util.descriptor.web.JspPropertyGroup");
/*     */     
/* 278 */     digester.addSetNext(this.fullPrefix + "/jsp-config/jsp-property-group", "addJspPropertyGroup", "org.apache.tomcat.util.descriptor.web.JspPropertyGroup");
/*     */     
/*     */ 
/* 281 */     digester.addCallMethod(this.fullPrefix + "/jsp-config/jsp-property-group/deferred-syntax-allowed-as-literal", "setDeferredSyntax", 0);
/*     */     
/* 283 */     digester.addCallMethod(this.fullPrefix + "/jsp-config/jsp-property-group/el-ignored", "setElIgnored", 0);
/*     */     
/* 285 */     digester.addCallMethod(this.fullPrefix + "/jsp-config/jsp-property-group/include-coda", "addIncludeCoda", 0);
/*     */     
/* 287 */     digester.addCallMethod(this.fullPrefix + "/jsp-config/jsp-property-group/include-prelude", "addIncludePrelude", 0);
/*     */     
/* 289 */     digester.addCallMethod(this.fullPrefix + "/jsp-config/jsp-property-group/is-xml", "setIsXml", 0);
/*     */     
/* 291 */     digester.addCallMethod(this.fullPrefix + "/jsp-config/jsp-property-group/page-encoding", "setPageEncoding", 0);
/*     */     
/* 293 */     digester.addCallMethod(this.fullPrefix + "/jsp-config/jsp-property-group/scripting-invalid", "setScriptingInvalid", 0);
/*     */     
/* 295 */     digester.addCallMethod(this.fullPrefix + "/jsp-config/jsp-property-group/trim-directive-whitespaces", "setTrimWhitespace", 0);
/*     */     
/* 297 */     digester.addCallMethod(this.fullPrefix + "/jsp-config/jsp-property-group/url-pattern", "addUrlPattern", 0);
/*     */     
/* 299 */     digester.addCallMethod(this.fullPrefix + "/jsp-config/jsp-property-group/default-content-type", "setDefaultContentType", 0);
/*     */     
/* 301 */     digester.addCallMethod(this.fullPrefix + "/jsp-config/jsp-property-group/buffer", "setBuffer", 0);
/*     */     
/* 303 */     digester.addCallMethod(this.fullPrefix + "/jsp-config/jsp-property-group/error-on-undeclared-namespace", "setErrorOnUndeclaredNamespace", 0);
/*     */     
/*     */ 
/* 306 */     digester.addRule(this.fullPrefix + "/login-config", this.loginConfig);
/*     */     
/*     */ 
/* 309 */     digester.addObjectCreate(this.fullPrefix + "/login-config", "org.apache.tomcat.util.descriptor.web.LoginConfig");
/*     */     
/* 311 */     digester.addSetNext(this.fullPrefix + "/login-config", "setLoginConfig", "org.apache.tomcat.util.descriptor.web.LoginConfig");
/*     */     
/*     */ 
/*     */ 
/* 315 */     digester.addCallMethod(this.fullPrefix + "/login-config/auth-method", "setAuthMethod", 0);
/*     */     
/* 317 */     digester.addCallMethod(this.fullPrefix + "/login-config/realm-name", "setRealmName", 0);
/*     */     
/* 319 */     digester.addCallMethod(this.fullPrefix + "/login-config/form-login-config/form-error-page", "setErrorPage", 0);
/*     */     
/* 321 */     digester.addCallMethod(this.fullPrefix + "/login-config/form-login-config/form-login-page", "setLoginPage", 0);
/*     */     
/*     */ 
/* 324 */     digester.addCallMethod(this.fullPrefix + "/mime-mapping", "addMimeMapping", 2);
/*     */     
/* 326 */     digester.addCallParam(this.fullPrefix + "/mime-mapping/extension", 0);
/* 327 */     digester.addCallParam(this.fullPrefix + "/mime-mapping/mime-type", 1);
/*     */     
/*     */ 
/* 330 */     digester.addObjectCreate(this.fullPrefix + "/security-constraint", "org.apache.tomcat.util.descriptor.web.SecurityConstraint");
/*     */     
/* 332 */     digester.addSetNext(this.fullPrefix + "/security-constraint", "addSecurityConstraint", "org.apache.tomcat.util.descriptor.web.SecurityConstraint");
/*     */     
/*     */ 
/*     */ 
/* 336 */     digester.addRule(this.fullPrefix + "/security-constraint/auth-constraint", new SetAuthConstraintRule());
/*     */     
/* 338 */     digester.addCallMethod(this.fullPrefix + "/security-constraint/auth-constraint/role-name", "addAuthRole", 0);
/*     */     
/* 340 */     digester.addCallMethod(this.fullPrefix + "/security-constraint/display-name", "setDisplayName", 0);
/*     */     
/* 342 */     digester.addCallMethod(this.fullPrefix + "/security-constraint/user-data-constraint/transport-guarantee", "setUserConstraint", 0);
/*     */     
/*     */ 
/* 345 */     digester.addObjectCreate(this.fullPrefix + "/security-constraint/web-resource-collection", "org.apache.tomcat.util.descriptor.web.SecurityCollection");
/*     */     
/* 347 */     digester.addSetNext(this.fullPrefix + "/security-constraint/web-resource-collection", "addCollection", "org.apache.tomcat.util.descriptor.web.SecurityCollection");
/*     */     
/*     */ 
/* 350 */     digester.addCallMethod(this.fullPrefix + "/security-constraint/web-resource-collection/http-method", "addMethod", 0);
/*     */     
/* 352 */     digester.addCallMethod(this.fullPrefix + "/security-constraint/web-resource-collection/http-method-omission", "addOmittedMethod", 0);
/*     */     
/* 354 */     digester.addCallMethod(this.fullPrefix + "/security-constraint/web-resource-collection/url-pattern", "addPattern", 0);
/*     */     
/* 356 */     digester.addCallMethod(this.fullPrefix + "/security-constraint/web-resource-collection/web-resource-name", "setName", 0);
/*     */     
/*     */ 
/* 359 */     digester.addCallMethod(this.fullPrefix + "/security-role/role-name", "addSecurityRole", 0);
/*     */     
/*     */ 
/* 362 */     digester.addRule(this.fullPrefix + "/servlet", new ServletDefCreateRule());
/*     */     
/* 364 */     digester.addSetNext(this.fullPrefix + "/servlet", "addServlet", "org.apache.tomcat.util.descriptor.web.ServletDef");
/*     */     
/*     */ 
/*     */ 
/* 368 */     digester.addCallMethod(this.fullPrefix + "/servlet/init-param", "addInitParameter", 2);
/*     */     
/* 370 */     digester.addCallParam(this.fullPrefix + "/servlet/init-param/param-name", 0);
/*     */     
/* 372 */     digester.addCallParam(this.fullPrefix + "/servlet/init-param/param-value", 1);
/*     */     
/*     */ 
/* 375 */     digester.addCallMethod(this.fullPrefix + "/servlet/jsp-file", "setJspFile", 0);
/*     */     
/* 377 */     digester.addCallMethod(this.fullPrefix + "/servlet/load-on-startup", "setLoadOnStartup", 0);
/*     */     
/* 379 */     digester.addCallMethod(this.fullPrefix + "/servlet/run-as/role-name", "setRunAs", 0);
/*     */     
/*     */ 
/* 382 */     digester.addObjectCreate(this.fullPrefix + "/servlet/security-role-ref", "org.apache.tomcat.util.descriptor.web.SecurityRoleRef");
/*     */     
/* 384 */     digester.addSetNext(this.fullPrefix + "/servlet/security-role-ref", "addSecurityRoleRef", "org.apache.tomcat.util.descriptor.web.SecurityRoleRef");
/*     */     
/*     */ 
/* 387 */     digester.addCallMethod(this.fullPrefix + "/servlet/security-role-ref/role-link", "setLink", 0);
/*     */     
/* 389 */     digester.addCallMethod(this.fullPrefix + "/servlet/security-role-ref/role-name", "setName", 0);
/*     */     
/*     */ 
/* 392 */     digester.addCallMethod(this.fullPrefix + "/servlet/servlet-class", "setServletClass", 0);
/*     */     
/* 394 */     digester.addCallMethod(this.fullPrefix + "/servlet/servlet-name", "setServletName", 0);
/*     */     
/*     */ 
/* 397 */     digester.addObjectCreate(this.fullPrefix + "/servlet/multipart-config", "org.apache.tomcat.util.descriptor.web.MultipartDef");
/*     */     
/* 399 */     digester.addSetNext(this.fullPrefix + "/servlet/multipart-config", "setMultipartDef", "org.apache.tomcat.util.descriptor.web.MultipartDef");
/*     */     
/*     */ 
/* 402 */     digester.addCallMethod(this.fullPrefix + "/servlet/multipart-config/location", "setLocation", 0);
/*     */     
/* 404 */     digester.addCallMethod(this.fullPrefix + "/servlet/multipart-config/max-file-size", "setMaxFileSize", 0);
/*     */     
/* 406 */     digester.addCallMethod(this.fullPrefix + "/servlet/multipart-config/max-request-size", "setMaxRequestSize", 0);
/*     */     
/* 408 */     digester.addCallMethod(this.fullPrefix + "/servlet/multipart-config/file-size-threshold", "setFileSizeThreshold", 0);
/*     */     
/*     */ 
/* 411 */     digester.addCallMethod(this.fullPrefix + "/servlet/async-supported", "setAsyncSupported", 0);
/*     */     
/* 413 */     digester.addCallMethod(this.fullPrefix + "/servlet/enabled", "setEnabled", 0);
/*     */     
/*     */ 
/*     */ 
/* 417 */     digester.addRule(this.fullPrefix + "/servlet-mapping", new CallMethodMultiRule("addServletMapping", 2, 0));
/*     */     
/* 419 */     digester.addCallParam(this.fullPrefix + "/servlet-mapping/servlet-name", 1);
/* 420 */     digester.addRule(this.fullPrefix + "/servlet-mapping/url-pattern", new CallParamMultiRule(0));
/*     */     
/* 422 */     digester.addRule(this.fullPrefix + "/session-config", this.sessionConfig);
/* 423 */     digester.addObjectCreate(this.fullPrefix + "/session-config", "org.apache.tomcat.util.descriptor.web.SessionConfig");
/*     */     
/* 425 */     digester.addSetNext(this.fullPrefix + "/session-config", "setSessionConfig", "org.apache.tomcat.util.descriptor.web.SessionConfig");
/*     */     
/* 427 */     digester.addCallMethod(this.fullPrefix + "/session-config/session-timeout", "setSessionTimeout", 0);
/*     */     
/* 429 */     digester.addCallMethod(this.fullPrefix + "/session-config/cookie-config/name", "setCookieName", 0);
/*     */     
/* 431 */     digester.addCallMethod(this.fullPrefix + "/session-config/cookie-config/domain", "setCookieDomain", 0);
/*     */     
/* 433 */     digester.addCallMethod(this.fullPrefix + "/session-config/cookie-config/path", "setCookiePath", 0);
/*     */     
/* 435 */     digester.addCallMethod(this.fullPrefix + "/session-config/cookie-config/comment", "setCookieComment", 0);
/*     */     
/* 437 */     digester.addCallMethod(this.fullPrefix + "/session-config/cookie-config/http-only", "setCookieHttpOnly", 0);
/*     */     
/* 439 */     digester.addCallMethod(this.fullPrefix + "/session-config/cookie-config/secure", "setCookieSecure", 0);
/*     */     
/* 441 */     digester.addCallMethod(this.fullPrefix + "/session-config/cookie-config/max-age", "setCookieMaxAge", 0);
/*     */     
/* 443 */     digester.addCallMethod(this.fullPrefix + "/session-config/tracking-mode", "addSessionTrackingMode", 0);
/*     */     
/*     */ 
/*     */ 
/* 447 */     digester.addRule(this.fullPrefix + "/taglib", new TaglibLocationRule(false));
/* 448 */     digester.addCallMethod(this.fullPrefix + "/taglib", "addTaglib", 2);
/*     */     
/* 450 */     digester.addCallParam(this.fullPrefix + "/taglib/taglib-location", 1);
/* 451 */     digester.addCallParam(this.fullPrefix + "/taglib/taglib-uri", 0);
/*     */     
/*     */ 
/* 454 */     digester.addRule(this.fullPrefix + "/jsp-config/taglib", new TaglibLocationRule(true));
/* 455 */     digester.addCallMethod(this.fullPrefix + "/jsp-config/taglib", "addTaglib", 2);
/*     */     
/* 457 */     digester.addCallParam(this.fullPrefix + "/jsp-config/taglib/taglib-location", 1);
/* 458 */     digester.addCallParam(this.fullPrefix + "/jsp-config/taglib/taglib-uri", 0);
/*     */     
/* 460 */     digester.addCallMethod(this.fullPrefix + "/welcome-file-list/welcome-file", "addWelcomeFile", 0);
/*     */     
/*     */ 
/* 463 */     digester.addCallMethod(this.fullPrefix + "/locale-encoding-mapping-list/locale-encoding-mapping", "addLocaleEncodingMapping", 2);
/*     */     
/* 465 */     digester.addCallParam(this.fullPrefix + "/locale-encoding-mapping-list/locale-encoding-mapping/locale", 0);
/* 466 */     digester.addCallParam(this.fullPrefix + "/locale-encoding-mapping-list/locale-encoding-mapping/encoding", 1);
/*     */     
/* 468 */     digester.addRule(this.fullPrefix + "/post-construct", new LifecycleCallbackRule("addPostConstructMethods", 2, true));
/*     */     
/* 470 */     digester.addCallParam(this.fullPrefix + "/post-construct/lifecycle-callback-class", 0);
/* 471 */     digester.addCallParam(this.fullPrefix + "/post-construct/lifecycle-callback-method", 1);
/*     */     
/* 473 */     digester.addRule(this.fullPrefix + "/pre-destroy", new LifecycleCallbackRule("addPreDestroyMethods", 2, false));
/*     */     
/* 475 */     digester.addCallParam(this.fullPrefix + "/pre-destroy/lifecycle-callback-class", 0);
/* 476 */     digester.addCallParam(this.fullPrefix + "/pre-destroy/lifecycle-callback-method", 1);
/*     */   }
/*     */   
/*     */   protected void configureNamingRules(Digester digester)
/*     */   {
/* 481 */     digester.addObjectCreate(this.fullPrefix + "/ejb-local-ref", "org.apache.tomcat.util.descriptor.web.ContextLocalEjb");
/*     */     
/* 483 */     digester.addSetNext(this.fullPrefix + "/ejb-local-ref", "addEjbLocalRef", "org.apache.tomcat.util.descriptor.web.ContextLocalEjb");
/*     */     
/*     */ 
/* 486 */     digester.addCallMethod(this.fullPrefix + "/ejb-local-ref/description", "setDescription", 0);
/*     */     
/* 488 */     digester.addCallMethod(this.fullPrefix + "/ejb-local-ref/ejb-link", "setLink", 0);
/*     */     
/* 490 */     digester.addCallMethod(this.fullPrefix + "/ejb-local-ref/ejb-ref-name", "setName", 0);
/*     */     
/* 492 */     digester.addCallMethod(this.fullPrefix + "/ejb-local-ref/ejb-ref-type", "setType", 0);
/*     */     
/* 494 */     digester.addCallMethod(this.fullPrefix + "/ejb-local-ref/local", "setLocal", 0);
/*     */     
/* 496 */     digester.addCallMethod(this.fullPrefix + "/ejb-local-ref/local-home", "setHome", 0);
/*     */     
/* 498 */     digester.addRule(this.fullPrefix + "/ejb-local-ref/mapped-name", new MappedNameRule());
/*     */     
/* 500 */     configureInjectionRules(digester, "web-app/ejb-local-ref/");
/*     */     
/*     */ 
/* 503 */     digester.addObjectCreate(this.fullPrefix + "/ejb-ref", "org.apache.tomcat.util.descriptor.web.ContextEjb");
/*     */     
/* 505 */     digester.addSetNext(this.fullPrefix + "/ejb-ref", "addEjbRef", "org.apache.tomcat.util.descriptor.web.ContextEjb");
/*     */     
/*     */ 
/* 508 */     digester.addCallMethod(this.fullPrefix + "/ejb-ref/description", "setDescription", 0);
/*     */     
/* 510 */     digester.addCallMethod(this.fullPrefix + "/ejb-ref/ejb-link", "setLink", 0);
/*     */     
/* 512 */     digester.addCallMethod(this.fullPrefix + "/ejb-ref/ejb-ref-name", "setName", 0);
/*     */     
/* 514 */     digester.addCallMethod(this.fullPrefix + "/ejb-ref/ejb-ref-type", "setType", 0);
/*     */     
/* 516 */     digester.addCallMethod(this.fullPrefix + "/ejb-ref/home", "setHome", 0);
/*     */     
/* 518 */     digester.addCallMethod(this.fullPrefix + "/ejb-ref/remote", "setRemote", 0);
/*     */     
/* 520 */     digester.addRule(this.fullPrefix + "/ejb-ref/mapped-name", new MappedNameRule());
/*     */     
/* 522 */     configureInjectionRules(digester, "web-app/ejb-ref/");
/*     */     
/*     */ 
/* 525 */     digester.addObjectCreate(this.fullPrefix + "/env-entry", "org.apache.tomcat.util.descriptor.web.ContextEnvironment");
/*     */     
/* 527 */     digester.addSetNext(this.fullPrefix + "/env-entry", "addEnvEntry", "org.apache.tomcat.util.descriptor.web.ContextEnvironment");
/*     */     
/*     */ 
/* 530 */     digester.addRule(this.fullPrefix + "/env-entry", new SetOverrideRule());
/* 531 */     digester.addCallMethod(this.fullPrefix + "/env-entry/description", "setDescription", 0);
/*     */     
/* 533 */     digester.addCallMethod(this.fullPrefix + "/env-entry/env-entry-name", "setName", 0);
/*     */     
/* 535 */     digester.addCallMethod(this.fullPrefix + "/env-entry/env-entry-type", "setType", 0);
/*     */     
/* 537 */     digester.addCallMethod(this.fullPrefix + "/env-entry/env-entry-value", "setValue", 0);
/*     */     
/* 539 */     digester.addRule(this.fullPrefix + "/env-entry/mapped-name", new MappedNameRule());
/*     */     
/* 541 */     configureInjectionRules(digester, "web-app/env-entry/");
/*     */     
/*     */ 
/* 544 */     digester.addObjectCreate(this.fullPrefix + "/resource-env-ref", "org.apache.tomcat.util.descriptor.web.ContextResourceEnvRef");
/*     */     
/* 546 */     digester.addSetNext(this.fullPrefix + "/resource-env-ref", "addResourceEnvRef", "org.apache.tomcat.util.descriptor.web.ContextResourceEnvRef");
/*     */     
/*     */ 
/* 549 */     digester.addCallMethod(this.fullPrefix + "/resource-env-ref/resource-env-ref-name", "setName", 0);
/*     */     
/* 551 */     digester.addCallMethod(this.fullPrefix + "/resource-env-ref/resource-env-ref-type", "setType", 0);
/*     */     
/* 553 */     digester.addRule(this.fullPrefix + "/resource-env-ref/mapped-name", new MappedNameRule());
/*     */     
/* 555 */     configureInjectionRules(digester, "web-app/resource-env-ref/");
/*     */     
/*     */ 
/* 558 */     digester.addObjectCreate(this.fullPrefix + "/message-destination", "org.apache.tomcat.util.descriptor.web.MessageDestination");
/*     */     
/* 560 */     digester.addSetNext(this.fullPrefix + "/message-destination", "addMessageDestination", "org.apache.tomcat.util.descriptor.web.MessageDestination");
/*     */     
/*     */ 
/* 563 */     digester.addCallMethod(this.fullPrefix + "/message-destination/description", "setDescription", 0);
/*     */     
/* 565 */     digester.addCallMethod(this.fullPrefix + "/message-destination/display-name", "setDisplayName", 0);
/*     */     
/* 567 */     digester.addCallMethod(this.fullPrefix + "/message-destination/icon/large-icon", "setLargeIcon", 0);
/*     */     
/* 569 */     digester.addCallMethod(this.fullPrefix + "/message-destination/icon/small-icon", "setSmallIcon", 0);
/*     */     
/* 571 */     digester.addCallMethod(this.fullPrefix + "/message-destination/message-destination-name", "setName", 0);
/*     */     
/* 573 */     digester.addRule(this.fullPrefix + "/message-destination/mapped-name", new MappedNameRule());
/*     */     
/*     */ 
/*     */ 
/* 577 */     digester.addObjectCreate(this.fullPrefix + "/message-destination-ref", "org.apache.tomcat.util.descriptor.web.MessageDestinationRef");
/*     */     
/* 579 */     digester.addSetNext(this.fullPrefix + "/message-destination-ref", "addMessageDestinationRef", "org.apache.tomcat.util.descriptor.web.MessageDestinationRef");
/*     */     
/*     */ 
/* 582 */     digester.addCallMethod(this.fullPrefix + "/message-destination-ref/description", "setDescription", 0);
/*     */     
/* 584 */     digester.addCallMethod(this.fullPrefix + "/message-destination-ref/message-destination-link", "setLink", 0);
/*     */     
/* 586 */     digester.addCallMethod(this.fullPrefix + "/message-destination-ref/message-destination-ref-name", "setName", 0);
/*     */     
/* 588 */     digester.addCallMethod(this.fullPrefix + "/message-destination-ref/message-destination-type", "setType", 0);
/*     */     
/* 590 */     digester.addCallMethod(this.fullPrefix + "/message-destination-ref/message-destination-usage", "setUsage", 0);
/*     */     
/* 592 */     digester.addRule(this.fullPrefix + "/message-destination-ref/mapped-name", new MappedNameRule());
/*     */     
/* 594 */     configureInjectionRules(digester, "web-app/message-destination-ref/");
/*     */     
/*     */ 
/* 597 */     digester.addObjectCreate(this.fullPrefix + "/resource-ref", "org.apache.tomcat.util.descriptor.web.ContextResource");
/*     */     
/* 599 */     digester.addSetNext(this.fullPrefix + "/resource-ref", "addResourceRef", "org.apache.tomcat.util.descriptor.web.ContextResource");
/*     */     
/*     */ 
/* 602 */     digester.addCallMethod(this.fullPrefix + "/resource-ref/description", "setDescription", 0);
/*     */     
/* 604 */     digester.addCallMethod(this.fullPrefix + "/resource-ref/res-auth", "setAuth", 0);
/*     */     
/* 606 */     digester.addCallMethod(this.fullPrefix + "/resource-ref/res-ref-name", "setName", 0);
/*     */     
/* 608 */     digester.addCallMethod(this.fullPrefix + "/resource-ref/res-sharing-scope", "setScope", 0);
/*     */     
/* 610 */     digester.addCallMethod(this.fullPrefix + "/resource-ref/res-type", "setType", 0);
/*     */     
/* 612 */     digester.addRule(this.fullPrefix + "/resource-ref/mapped-name", new MappedNameRule());
/*     */     
/* 614 */     configureInjectionRules(digester, "web-app/resource-ref/");
/*     */     
/*     */ 
/* 617 */     digester.addObjectCreate(this.fullPrefix + "/service-ref", "org.apache.tomcat.util.descriptor.web.ContextService");
/*     */     
/* 619 */     digester.addSetNext(this.fullPrefix + "/service-ref", "addServiceRef", "org.apache.tomcat.util.descriptor.web.ContextService");
/*     */     
/*     */ 
/* 622 */     digester.addCallMethod(this.fullPrefix + "/service-ref/description", "setDescription", 0);
/*     */     
/* 624 */     digester.addCallMethod(this.fullPrefix + "/service-ref/display-name", "setDisplayname", 0);
/*     */     
/* 626 */     digester.addCallMethod(this.fullPrefix + "/service-ref/icon/large-icon", "setLargeIcon", 0);
/*     */     
/* 628 */     digester.addCallMethod(this.fullPrefix + "/service-ref/icon/small-icon", "setSmallIcon", 0);
/*     */     
/* 630 */     digester.addCallMethod(this.fullPrefix + "/service-ref/service-ref-name", "setName", 0);
/*     */     
/* 632 */     digester.addCallMethod(this.fullPrefix + "/service-ref/service-interface", "setInterface", 0);
/*     */     
/* 634 */     digester.addCallMethod(this.fullPrefix + "/service-ref/service-ref-type", "setType", 0);
/*     */     
/* 636 */     digester.addCallMethod(this.fullPrefix + "/service-ref/wsdl-file", "setWsdlfile", 0);
/*     */     
/* 638 */     digester.addCallMethod(this.fullPrefix + "/service-ref/jaxrpc-mapping-file", "setJaxrpcmappingfile", 0);
/*     */     
/* 640 */     digester.addRule(this.fullPrefix + "/service-ref/service-qname", new ServiceQnameRule());
/*     */     
/* 642 */     digester.addRule(this.fullPrefix + "/service-ref/port-component-ref", new CallMethodMultiRule("addPortcomponent", 2, 1));
/*     */     
/* 644 */     digester.addCallParam(this.fullPrefix + "/service-ref/port-component-ref/service-endpoint-interface", 0);
/* 645 */     digester.addRule(this.fullPrefix + "/service-ref/port-component-ref/port-component-link", new CallParamMultiRule(1));
/*     */     
/* 647 */     digester.addObjectCreate(this.fullPrefix + "/service-ref/handler", "org.apache.tomcat.util.descriptor.web.ContextHandler");
/*     */     
/* 649 */     digester.addRule(this.fullPrefix + "/service-ref/handler", new SetNextRule("addHandler", "org.apache.tomcat.util.descriptor.web.ContextHandler"));
/*     */     
/*     */ 
/*     */ 
/* 653 */     digester.addCallMethod(this.fullPrefix + "/service-ref/handler/handler-name", "setName", 0);
/*     */     
/* 655 */     digester.addCallMethod(this.fullPrefix + "/service-ref/handler/handler-class", "setHandlerclass", 0);
/*     */     
/*     */ 
/* 658 */     digester.addCallMethod(this.fullPrefix + "/service-ref/handler/init-param", "setProperty", 2);
/*     */     
/* 660 */     digester.addCallParam(this.fullPrefix + "/service-ref/handler/init-param/param-name", 0);
/*     */     
/* 662 */     digester.addCallParam(this.fullPrefix + "/service-ref/handler/init-param/param-value", 1);
/*     */     
/*     */ 
/* 665 */     digester.addRule(this.fullPrefix + "/service-ref/handler/soap-header", new SoapHeaderRule());
/*     */     
/* 667 */     digester.addCallMethod(this.fullPrefix + "/service-ref/handler/soap-role", "addSoapRole", 0);
/*     */     
/* 669 */     digester.addCallMethod(this.fullPrefix + "/service-ref/handler/port-name", "addPortName", 0);
/*     */     
/* 671 */     digester.addRule(this.fullPrefix + "/service-ref/mapped-name", new MappedNameRule());
/*     */     
/* 673 */     configureInjectionRules(digester, "web-app/service-ref/");
/*     */   }
/*     */   
/*     */   protected void configureInjectionRules(Digester digester, String base)
/*     */   {
/* 678 */     digester.addCallMethod(this.prefix + base + "injection-target", "addInjectionTarget", 2);
/* 679 */     digester.addCallParam(this.prefix + base + "injection-target/injection-target-class", 0);
/* 680 */     digester.addCallParam(this.prefix + base + "injection-target/injection-target-name", 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void recycle()
/*     */   {
/* 689 */     this.jspConfig.isJspConfigSet = false;
/* 690 */     this.sessionConfig.isSessionConfigSet = false;
/* 691 */     this.loginConfig.isLoginConfigSet = false;
/* 692 */     this.name.isNameSet = false;
/* 693 */     this.absoluteOrdering.isAbsoluteOrderingSet = false;
/* 694 */     this.relativeOrdering.isRelativeOrderingSet = false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\WebRuleSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */