/*     */ package org.hibernate.validator.messageinterpolation;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.validation.MessageInterpolator;
/*     */ import javax.validation.MessageInterpolator.Context;
/*     */ import javax.validation.ValidationException;
/*     */ import org.hibernate.validator.internal.engine.messageinterpolation.InterpolationTermType;
/*     */ import org.hibernate.validator.internal.engine.messageinterpolation.LocalizedMessage;
/*     */ import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
/*     */ import org.hibernate.validator.internal.engine.messageinterpolation.parser.Token;
/*     */ import org.hibernate.validator.internal.engine.messageinterpolation.parser.TokenCollector;
/*     */ import org.hibernate.validator.internal.engine.messageinterpolation.parser.TokenIterator;
/*     */ import org.hibernate.validator.internal.util.ConcurrentReferenceHashMap;
/*     */ import org.hibernate.validator.internal.util.ConcurrentReferenceHashMap.Option;
/*     */ import org.hibernate.validator.internal.util.ConcurrentReferenceHashMap.ReferenceType;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
/*     */ import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;
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
/*     */ public abstract class AbstractMessageInterpolator
/*     */   implements MessageInterpolator
/*     */ {
/*  45 */   private static final Log log = ;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int DEFAULT_INITIAL_CAPACITY = 100;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final float DEFAULT_LOAD_FACTOR = 0.75F;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String DEFAULT_VALIDATION_MESSAGES = "org.hibernate.validator.ValidationMessages";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String USER_VALIDATION_MESSAGES = "ValidationMessages";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String CONTRIBUTOR_VALIDATION_MESSAGES = "ContributorValidationMessages";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Locale defaultLocale;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ResourceBundleLocator userResourceBundleLocator;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ResourceBundleLocator defaultResourceBundleLocator;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ResourceBundleLocator contributorResourceBundleLocator;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ConcurrentReferenceHashMap<LocalizedMessage, String> resolvedMessages;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ConcurrentReferenceHashMap<String, List<Token>> tokenizedParameterMessages;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ConcurrentReferenceHashMap<String, List<Token>> tokenizedELMessages;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final boolean cachingEnabled;
/*     */   
/*     */ 
/*     */ 
/* 119 */   private static final Pattern LEFT_BRACE = Pattern.compile("\\{", 16);
/* 120 */   private static final Pattern RIGHT_BRACE = Pattern.compile("\\}", 16);
/* 121 */   private static final Pattern SLASH = Pattern.compile("\\\\", 16);
/* 122 */   private static final Pattern DOLLAR = Pattern.compile("\\$", 16);
/*     */   
/*     */   public AbstractMessageInterpolator() {
/* 125 */     this(null);
/*     */   }
/*     */   
/*     */   public AbstractMessageInterpolator(ResourceBundleLocator userResourceBundleLocator) {
/* 129 */     this(userResourceBundleLocator, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractMessageInterpolator(ResourceBundleLocator userResourceBundleLocator, ResourceBundleLocator contributorResourceBundleLocator)
/*     */   {
/* 141 */     this(userResourceBundleLocator, contributorResourceBundleLocator, true);
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
/*     */   public AbstractMessageInterpolator(ResourceBundleLocator userResourceBundleLocator, ResourceBundleLocator contributorResourceBundleLocator, boolean cacheMessages)
/*     */   {
/* 155 */     this.defaultLocale = Locale.getDefault();
/*     */     
/* 157 */     if (userResourceBundleLocator == null) {
/* 158 */       this.userResourceBundleLocator = new PlatformResourceBundleLocator("ValidationMessages");
/*     */     }
/*     */     else {
/* 161 */       this.userResourceBundleLocator = userResourceBundleLocator;
/*     */     }
/*     */     
/* 164 */     if (contributorResourceBundleLocator == null) {
/* 165 */       this.contributorResourceBundleLocator = new PlatformResourceBundleLocator("ContributorValidationMessages", null, true);
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 172 */       this.contributorResourceBundleLocator = contributorResourceBundleLocator;
/*     */     }
/*     */     
/* 175 */     this.defaultResourceBundleLocator = new PlatformResourceBundleLocator("org.hibernate.validator.ValidationMessages");
/*     */     
/* 177 */     this.cachingEnabled = cacheMessages;
/* 178 */     if (this.cachingEnabled)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 185 */       this.resolvedMessages = new ConcurrentReferenceHashMap(100, 0.75F, 16, ConcurrentReferenceHashMap.ReferenceType.SOFT, ConcurrentReferenceHashMap.ReferenceType.SOFT, EnumSet.noneOf(ConcurrentReferenceHashMap.Option.class));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 193 */       this.tokenizedParameterMessages = new ConcurrentReferenceHashMap(100, 0.75F, 16, ConcurrentReferenceHashMap.ReferenceType.SOFT, ConcurrentReferenceHashMap.ReferenceType.SOFT, EnumSet.noneOf(ConcurrentReferenceHashMap.Option.class));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 201 */       this.tokenizedELMessages = new ConcurrentReferenceHashMap(100, 0.75F, 16, ConcurrentReferenceHashMap.ReferenceType.SOFT, ConcurrentReferenceHashMap.ReferenceType.SOFT, EnumSet.noneOf(ConcurrentReferenceHashMap.Option.class));
/*     */     }
/*     */     else
/*     */     {
/* 205 */       this.resolvedMessages = null;
/* 206 */       this.tokenizedParameterMessages = null;
/* 207 */       this.tokenizedELMessages = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String interpolate(String message, MessageInterpolator.Context context)
/*     */   {
/* 215 */     String interpolatedMessage = message;
/*     */     try {
/* 217 */       interpolatedMessage = interpolateMessage(message, context, this.defaultLocale);
/*     */     }
/*     */     catch (MessageDescriptorFormatException e) {
/* 220 */       log.warn(e.getMessage());
/*     */     }
/* 222 */     return interpolatedMessage;
/*     */   }
/*     */   
/*     */   public String interpolate(String message, MessageInterpolator.Context context, Locale locale)
/*     */   {
/* 227 */     String interpolatedMessage = message;
/*     */     try {
/* 229 */       interpolatedMessage = interpolateMessage(message, context, locale);
/*     */     }
/*     */     catch (ValidationException e) {
/* 232 */       log.warn(e.getMessage());
/*     */     }
/* 234 */     return interpolatedMessage;
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
/*     */   private String interpolateMessage(String message, MessageInterpolator.Context context, Locale locale)
/*     */     throws MessageDescriptorFormatException
/*     */   {
/* 252 */     LocalizedMessage localisedMessage = new LocalizedMessage(message, locale);
/* 253 */     String resolvedMessage = null;
/*     */     
/* 255 */     if (this.cachingEnabled) {
/* 256 */       resolvedMessage = (String)this.resolvedMessages.get(localisedMessage);
/*     */     }
/*     */     
/*     */ 
/* 260 */     if (resolvedMessage == null)
/*     */     {
/* 262 */       ResourceBundle userResourceBundle = this.userResourceBundleLocator.getResourceBundle(locale);
/*     */       
/*     */ 
/* 265 */       ResourceBundle constraintContributorResourceBundle = this.contributorResourceBundleLocator.getResourceBundle(locale);
/*     */       
/*     */ 
/* 268 */       ResourceBundle defaultResourceBundle = this.defaultResourceBundleLocator.getResourceBundle(locale);
/*     */       
/*     */ 
/* 271 */       resolvedMessage = message;
/* 272 */       boolean evaluatedDefaultBundleOnce = false;
/*     */       for (;;)
/*     */       {
/* 275 */         String userBundleResolvedMessage = interpolateBundleMessage(resolvedMessage, userResourceBundle, locale, true);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 280 */         if (!hasReplacementTakenPlace(userBundleResolvedMessage, resolvedMessage)) {
/* 281 */           userBundleResolvedMessage = interpolateBundleMessage(resolvedMessage, constraintContributorResourceBundle, locale, true);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 288 */         if ((evaluatedDefaultBundleOnce) && 
/* 289 */           (!hasReplacementTakenPlace(userBundleResolvedMessage, resolvedMessage))) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/* 294 */         resolvedMessage = interpolateBundleMessage(userBundleResolvedMessage, defaultResourceBundle, locale, false);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 300 */         evaluatedDefaultBundleOnce = true;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 305 */     if (this.cachingEnabled) {
/* 306 */       String cachedResolvedMessage = (String)this.resolvedMessages.putIfAbsent(localisedMessage, resolvedMessage);
/* 307 */       if (cachedResolvedMessage != null) {
/* 308 */         resolvedMessage = cachedResolvedMessage;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 313 */     List<Token> tokens = null;
/* 314 */     if (this.cachingEnabled) {
/* 315 */       tokens = (List)this.tokenizedParameterMessages.get(resolvedMessage);
/*     */     }
/* 317 */     if (tokens == null) {
/* 318 */       TokenCollector tokenCollector = new TokenCollector(resolvedMessage, InterpolationTermType.PARAMETER);
/* 319 */       tokens = tokenCollector.getTokenList();
/*     */       
/* 321 */       if (this.cachingEnabled) {
/* 322 */         this.tokenizedParameterMessages.putIfAbsent(resolvedMessage, tokens);
/*     */       }
/*     */     }
/* 325 */     resolvedMessage = interpolateExpression(new TokenIterator(tokens), context, locale);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 332 */     tokens = null;
/* 333 */     if (this.cachingEnabled) {
/* 334 */       tokens = (List)this.tokenizedELMessages.get(resolvedMessage);
/*     */     }
/* 336 */     if (tokens == null) {
/* 337 */       TokenCollector tokenCollector = new TokenCollector(resolvedMessage, InterpolationTermType.EL);
/* 338 */       tokens = tokenCollector.getTokenList();
/*     */       
/* 340 */       if (this.cachingEnabled) {
/* 341 */         this.tokenizedELMessages.putIfAbsent(resolvedMessage, tokens);
/*     */       }
/*     */     }
/* 344 */     resolvedMessage = interpolateExpression(new TokenIterator(tokens), context, locale);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 351 */     resolvedMessage = replaceEscapedLiterals(resolvedMessage);
/*     */     
/* 353 */     return resolvedMessage;
/*     */   }
/*     */   
/*     */   private String replaceEscapedLiterals(String resolvedMessage) {
/* 357 */     resolvedMessage = LEFT_BRACE.matcher(resolvedMessage).replaceAll("{");
/* 358 */     resolvedMessage = RIGHT_BRACE.matcher(resolvedMessage).replaceAll("}");
/* 359 */     resolvedMessage = SLASH.matcher(resolvedMessage).replaceAll(Matcher.quoteReplacement("\\"));
/* 360 */     resolvedMessage = DOLLAR.matcher(resolvedMessage).replaceAll(Matcher.quoteReplacement("$"));
/* 361 */     return resolvedMessage;
/*     */   }
/*     */   
/*     */   private boolean hasReplacementTakenPlace(String origMessage, String newMessage) {
/* 365 */     return !origMessage.equals(newMessage);
/*     */   }
/*     */   
/*     */   private String interpolateBundleMessage(String message, ResourceBundle bundle, Locale locale, boolean recursive) throws MessageDescriptorFormatException
/*     */   {
/* 370 */     TokenCollector tokenCollector = new TokenCollector(message, InterpolationTermType.PARAMETER);
/* 371 */     TokenIterator tokenIterator = new TokenIterator(tokenCollector.getTokenList());
/* 372 */     while (tokenIterator.hasMoreInterpolationTerms()) {
/* 373 */       String term = tokenIterator.nextInterpolationTerm();
/* 374 */       String resolvedParameterValue = resolveParameter(term, bundle, locale, recursive);
/*     */       
/*     */ 
/* 377 */       tokenIterator.replaceCurrentInterpolationTerm(resolvedParameterValue);
/*     */     }
/* 379 */     return tokenIterator.getInterpolatedMessage();
/*     */   }
/*     */   
/*     */   private String interpolateExpression(TokenIterator tokenIterator, MessageInterpolator.Context context, Locale locale) throws MessageDescriptorFormatException
/*     */   {
/* 384 */     while (tokenIterator.hasMoreInterpolationTerms()) {
/* 385 */       String term = tokenIterator.nextInterpolationTerm();
/*     */       
/* 387 */       String resolvedExpression = interpolate(context, locale, term);
/* 388 */       tokenIterator.replaceCurrentInterpolationTerm(resolvedExpression);
/*     */     }
/* 390 */     return tokenIterator.getInterpolatedMessage();
/*     */   }
/*     */   
/*     */   public abstract String interpolate(MessageInterpolator.Context paramContext, Locale paramLocale, String paramString);
/*     */   
/*     */   private String resolveParameter(String parameterName, ResourceBundle bundle, Locale locale, boolean recursive) throws MessageDescriptorFormatException
/*     */   {
/*     */     String parameterValue;
/*     */     try {
/* 399 */       if (bundle != null) {
/* 400 */         String parameterValue = bundle.getString(removeCurlyBraces(parameterName));
/* 401 */         if (recursive) {
/* 402 */           parameterValue = interpolateBundleMessage(parameterValue, bundle, locale, recursive);
/*     */         }
/*     */       }
/*     */       else {
/* 406 */         parameterValue = parameterName;
/*     */       }
/*     */     }
/*     */     catch (MissingResourceException e) {
/*     */       String parameterValue;
/* 411 */       parameterValue = parameterName;
/*     */     }
/* 413 */     return parameterValue;
/*     */   }
/*     */   
/*     */   private String removeCurlyBraces(String parameter) {
/* 417 */     return parameter.substring(1, parameter.length() - 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\messageinterpolation\AbstractMessageInterpolator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */