/*     */ package org.hibernate.validator.internal.engine.messageinterpolation.parser;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.hibernate.validator.internal.engine.messageinterpolation.InterpolationTermType;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
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
/*     */ public class TokenCollector
/*     */ {
/*     */   public static final char BEGIN_TERM = '{';
/*     */   public static final char END_TERM = '}';
/*     */   public static final char EL_DESIGNATOR = '$';
/*     */   public static final char ESCAPE_CHARACTER = '\\';
/*     */   private final String originalMessageDescriptor;
/*     */   private final InterpolationTermType interpolationTermType;
/*     */   private final List<Token> tokenList;
/*     */   private ParserState currentParserState;
/*     */   private int currentPosition;
/*     */   private Token currentToken;
/*     */   
/*     */   public TokenCollector(String originalMessageDescriptor, InterpolationTermType interpolationTermType)
/*     */     throws MessageDescriptorFormatException
/*     */   {
/*  38 */     this.originalMessageDescriptor = originalMessageDescriptor;
/*  39 */     this.interpolationTermType = interpolationTermType;
/*  40 */     this.currentParserState = new BeginState();
/*  41 */     this.tokenList = CollectionHelper.newArrayList();
/*     */     
/*  43 */     parse();
/*     */   }
/*     */   
/*     */   public void terminateToken() {
/*  47 */     if (this.currentToken == null) {
/*  48 */       return;
/*     */     }
/*  50 */     this.currentToken.terminate();
/*  51 */     this.tokenList.add(this.currentToken);
/*  52 */     this.currentToken = null;
/*     */   }
/*     */   
/*     */   public void appendToToken(char character) {
/*  56 */     if (this.currentToken == null) {
/*  57 */       this.currentToken = new Token(character);
/*     */     }
/*     */     else {
/*  60 */       this.currentToken.append(character);
/*     */     }
/*     */   }
/*     */   
/*     */   public void makeParameterToken() {
/*  65 */     this.currentToken.makeParameterToken();
/*     */   }
/*     */   
/*     */   public void makeELToken() {
/*  69 */     this.currentToken.makeELToken();
/*     */   }
/*     */   
/*     */   private void next() throws MessageDescriptorFormatException {
/*  73 */     if (this.currentPosition == this.originalMessageDescriptor.length())
/*     */     {
/*  75 */       this.currentParserState.terminate(this);
/*  76 */       this.currentPosition += 1;
/*  77 */       return;
/*     */     }
/*  79 */     char currentCharacter = this.originalMessageDescriptor.charAt(this.currentPosition);
/*  80 */     this.currentPosition += 1;
/*  81 */     switch (currentCharacter) {
/*     */     case '{': 
/*  83 */       this.currentParserState.handleBeginTerm(currentCharacter, this);
/*  84 */       break;
/*     */     
/*     */     case '}': 
/*  87 */       this.currentParserState.handleEndTerm(currentCharacter, this);
/*  88 */       break;
/*     */     
/*     */     case '$': 
/*  91 */       this.currentParserState.handleELDesignator(currentCharacter, this);
/*  92 */       break;
/*     */     
/*     */     case '\\': 
/*  95 */       this.currentParserState.handleEscapeCharacter(currentCharacter, this);
/*  96 */       break;
/*     */     
/*     */     default: 
/*  99 */       this.currentParserState.handleNonMetaCharacter(currentCharacter, this);
/*     */     }
/*     */   }
/*     */   
/*     */   public final void parse() throws MessageDescriptorFormatException
/*     */   {
/* 105 */     while (this.currentPosition <= this.originalMessageDescriptor.length()) {
/* 106 */       next();
/*     */     }
/*     */   }
/*     */   
/*     */   public void transitionState(ParserState newState) {
/* 111 */     this.currentParserState = newState;
/*     */   }
/*     */   
/*     */   public InterpolationTermType getInterpolationType() {
/* 115 */     return this.interpolationTermType;
/*     */   }
/*     */   
/*     */   public List<Token> getTokenList() {
/* 119 */     return Collections.unmodifiableList(this.tokenList);
/*     */   }
/*     */   
/*     */   public String getOriginalMessageDescriptor() {
/* 123 */     return this.originalMessageDescriptor;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\messageinterpolation\parser\TokenCollector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */