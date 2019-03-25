/*     */ package com.fasterxml.jackson.core.filter;
/*     */ 
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.core.JsonLocation;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*     */ import com.fasterxml.jackson.core.JsonStreamContext;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.util.JsonParserDelegate;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
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
/*     */ public class FilteringParserDelegate
/*     */   extends JsonParserDelegate
/*     */ {
/*     */   protected TokenFilter rootFilter;
/*     */   protected boolean _allowMultipleMatches;
/*     */   protected boolean _includePath;
/*     */   @Deprecated
/*     */   protected boolean _includeImmediateParent;
/*     */   protected JsonToken _currToken;
/*     */   protected JsonToken _lastClearedToken;
/*     */   protected TokenFilterContext _headContext;
/*     */   protected TokenFilterContext _exposedContext;
/*     */   protected TokenFilter _itemFilter;
/*     */   protected int _matchCount;
/*     */   
/*     */   public FilteringParserDelegate(JsonParser p, TokenFilter f, boolean includePath, boolean allowMultipleMatches)
/*     */   {
/* 117 */     super(p);
/* 118 */     this.rootFilter = f;
/*     */     
/* 120 */     this._itemFilter = f;
/* 121 */     this._headContext = TokenFilterContext.createRootContext(f);
/* 122 */     this._includePath = includePath;
/* 123 */     this._allowMultipleMatches = allowMultipleMatches;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TokenFilter getFilter()
/*     */   {
/* 132 */     return this.rootFilter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getMatchCount()
/*     */   {
/* 139 */     return this._matchCount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 148 */   public JsonToken getCurrentToken() { return this._currToken; }
/* 149 */   public JsonToken currentToken() { return this._currToken; }
/*     */   
/*     */   public final int getCurrentTokenId() {
/* 152 */     JsonToken t = this._currToken;
/* 153 */     return t == null ? 0 : t.id();
/*     */   }
/*     */   
/* 156 */   public final int currentTokenId() { JsonToken t = this._currToken;
/* 157 */     return t == null ? 0 : t.id();
/*     */   }
/*     */   
/* 160 */   public boolean hasCurrentToken() { return this._currToken != null; }
/*     */   
/* 162 */   public boolean hasTokenId(int id) { JsonToken t = this._currToken;
/* 163 */     if (t == null) {
/* 164 */       return 0 == id;
/*     */     }
/* 166 */     return t.id() == id;
/*     */   }
/*     */   
/*     */   public final boolean hasToken(JsonToken t) {
/* 170 */     return this._currToken == t;
/*     */   }
/*     */   
/* 173 */   public boolean isExpectedStartArrayToken() { return this._currToken == JsonToken.START_ARRAY; }
/* 174 */   public boolean isExpectedStartObjectToken() { return this._currToken == JsonToken.START_OBJECT; }
/*     */   
/* 176 */   public JsonLocation getCurrentLocation() { return this.delegate.getCurrentLocation(); }
/*     */   
/*     */   public JsonStreamContext getParsingContext()
/*     */   {
/* 180 */     return _filterContext();
/*     */   }
/*     */   
/*     */   public String getCurrentName()
/*     */     throws IOException
/*     */   {
/* 186 */     JsonStreamContext ctxt = _filterContext();
/* 187 */     if ((this._currToken == JsonToken.START_OBJECT) || (this._currToken == JsonToken.START_ARRAY)) {
/* 188 */       JsonStreamContext parent = ctxt.getParent();
/* 189 */       return parent == null ? null : parent.getCurrentName();
/*     */     }
/* 191 */     return ctxt.getCurrentName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clearCurrentToken()
/*     */   {
/* 202 */     if (this._currToken != null) {
/* 203 */       this._lastClearedToken = this._currToken;
/* 204 */       this._currToken = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public JsonToken getLastClearedToken() {
/* 209 */     return this._lastClearedToken;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void overrideCurrentName(String name)
/*     */   {
/* 217 */     throw new UnsupportedOperationException("Can not currently override name during filtering read");
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
/*     */   public JsonToken nextToken()
/*     */     throws IOException
/*     */   {
/* 232 */     if ((!this._allowMultipleMatches) && (this._currToken != null) && (this._exposedContext == null))
/*     */     {
/* 234 */       if ((this._currToken.isStructEnd()) && (this._headContext.isStartHandled())) {
/* 235 */         return this._currToken = null;
/*     */       }
/*     */       
/*     */ 
/* 239 */       if ((this._currToken.isScalarValue()) && (!this._headContext.isStartHandled()) && (!this._includePath) && (this._itemFilter == TokenFilter.INCLUDE_ALL))
/*     */       {
/* 241 */         return this._currToken = null;
/*     */       }
/*     */     }
/*     */     
/* 245 */     TokenFilterContext ctxt = this._exposedContext;
/*     */     
/* 247 */     if (ctxt != null) {
/*     */       for (;;) {
/* 249 */         JsonToken t = ctxt.nextTokenToRead();
/* 250 */         if (t != null) {
/* 251 */           this._currToken = t;
/* 252 */           return t;
/*     */         }
/*     */         
/* 255 */         if (ctxt == this._headContext) {
/* 256 */           this._exposedContext = null;
/* 257 */           if (!ctxt.inArray()) break;
/* 258 */           t = this.delegate.getCurrentToken();
/*     */           
/*     */ 
/* 261 */           this._currToken = t;
/* 262 */           return t;
/*     */         }
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
/* 277 */         ctxt = this._headContext.findChildOf(ctxt);
/* 278 */         this._exposedContext = ctxt;
/* 279 */         if (ctxt == null) {
/* 280 */           throw _constructError("Unexpected problem: chain of filtered context broken");
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 286 */     JsonToken t = this.delegate.nextToken();
/* 287 */     if (t == null)
/*     */     {
/* 289 */       this._currToken = t;
/* 290 */       return t;
/*     */     }
/*     */     
/*     */ 
/*     */     TokenFilter f;
/*     */     
/* 296 */     switch (t.id()) {
/*     */     case 3: 
/* 298 */       f = this._itemFilter;
/* 299 */       if (f == TokenFilter.INCLUDE_ALL) {
/* 300 */         this._headContext = this._headContext.createChildArrayContext(f, true);
/* 301 */         return this._currToken = t;
/*     */       }
/* 303 */       if (f == null) {
/* 304 */         this.delegate.skipChildren();
/*     */       }
/*     */       else
/*     */       {
/* 308 */         f = this._headContext.checkValue(f);
/* 309 */         if (f == null) {
/* 310 */           this.delegate.skipChildren();
/*     */         }
/*     */         else {
/* 313 */           if (f != TokenFilter.INCLUDE_ALL) {
/* 314 */             f = f.filterStartArray();
/*     */           }
/* 316 */           this._itemFilter = f;
/* 317 */           if (f == TokenFilter.INCLUDE_ALL) {
/* 318 */             this._headContext = this._headContext.createChildArrayContext(f, true);
/* 319 */             return this._currToken = t;
/*     */           }
/* 321 */           this._headContext = this._headContext.createChildArrayContext(f, false);
/*     */           
/*     */ 
/* 324 */           if (this._includePath) {
/* 325 */             t = _nextTokenWithBuffering(this._headContext);
/* 326 */             if (t != null) {
/* 327 */               this._currToken = t;
/* 328 */               return t;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       break;
/* 334 */     case 1:  f = this._itemFilter;
/* 335 */       if (f == TokenFilter.INCLUDE_ALL) {
/* 336 */         this._headContext = this._headContext.createChildObjectContext(f, true);
/* 337 */         return this._currToken = t;
/*     */       }
/* 339 */       if (f == null) {
/* 340 */         this.delegate.skipChildren();
/*     */       }
/*     */       else
/*     */       {
/* 344 */         f = this._headContext.checkValue(f);
/* 345 */         if (f == null) {
/* 346 */           this.delegate.skipChildren();
/*     */         }
/*     */         else {
/* 349 */           if (f != TokenFilter.INCLUDE_ALL) {
/* 350 */             f = f.filterStartObject();
/*     */           }
/* 352 */           this._itemFilter = f;
/* 353 */           if (f == TokenFilter.INCLUDE_ALL) {
/* 354 */             this._headContext = this._headContext.createChildObjectContext(f, true);
/* 355 */             return this._currToken = t;
/*     */           }
/* 357 */           this._headContext = this._headContext.createChildObjectContext(f, false);
/*     */           
/* 359 */           if (this._includePath) {
/* 360 */             t = _nextTokenWithBuffering(this._headContext);
/* 361 */             if (t != null) {
/* 362 */               this._currToken = t;
/* 363 */               return t;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */       break;
/*     */     case 2: 
/*     */     case 4: 
/* 373 */       boolean returnEnd = this._headContext.isStartHandled();
/* 374 */       f = this._headContext.getFilter();
/* 375 */       if ((f != null) && (f != TokenFilter.INCLUDE_ALL)) {
/* 376 */         f.filterFinishArray();
/*     */       }
/* 378 */       this._headContext = this._headContext.getParent();
/* 379 */       this._itemFilter = this._headContext.getFilter();
/* 380 */       if (returnEnd) {
/* 381 */         return this._currToken = t;
/*     */       }
/*     */       
/* 384 */       break;
/*     */     
/*     */ 
/*     */     case 5: 
/* 388 */       String name = this.delegate.getCurrentName();
/*     */       
/* 390 */       f = this._headContext.setFieldName(name);
/* 391 */       if (f == TokenFilter.INCLUDE_ALL) {
/* 392 */         this._itemFilter = f;
/* 393 */         if (!this._includePath)
/*     */         {
/*     */ 
/* 396 */           if ((this._includeImmediateParent) && (!this._headContext.isStartHandled())) {
/* 397 */             t = this._headContext.nextTokenToRead();
/* 398 */             this._exposedContext = this._headContext;
/*     */           }
/*     */         }
/* 401 */         return this._currToken = t;
/*     */       }
/* 403 */       if (f == null) {
/* 404 */         this.delegate.nextToken();
/* 405 */         this.delegate.skipChildren();
/*     */       }
/*     */       else {
/* 408 */         f = f.includeProperty(name);
/* 409 */         if (f == null) {
/* 410 */           this.delegate.nextToken();
/* 411 */           this.delegate.skipChildren();
/*     */         }
/*     */         else {
/* 414 */           this._itemFilter = f;
/* 415 */           if ((f == TokenFilter.INCLUDE_ALL) && 
/* 416 */             (this._includePath)) {
/* 417 */             return this._currToken = t;
/*     */           }
/*     */           
/* 420 */           if (this._includePath) {
/* 421 */             t = _nextTokenWithBuffering(this._headContext);
/* 422 */             if (t != null) {
/* 423 */               this._currToken = t;
/* 424 */               return t;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       break;
/*     */     default: 
/* 431 */       f = this._itemFilter;
/* 432 */       if (f == TokenFilter.INCLUDE_ALL) {
/* 433 */         return this._currToken = t;
/*     */       }
/* 435 */       if (f != null) {
/* 436 */         f = this._headContext.checkValue(f);
/* 437 */         if ((f == TokenFilter.INCLUDE_ALL) || ((f != null) && (f.includeValue(this.delegate))))
/*     */         {
/* 439 */           return this._currToken = t;
/*     */         }
/*     */       }
/*     */       
/*     */       break;
/*     */     }
/*     */     
/*     */     
/* 447 */     return _nextToken2();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JsonToken _nextToken2()
/*     */     throws IOException
/*     */   {
/*     */     for (;;)
/*     */     {
/* 460 */       JsonToken t = this.delegate.nextToken();
/* 461 */       if (t == null) {
/* 462 */         this._currToken = t;
/* 463 */         return t;
/*     */       }
/*     */       
/*     */       TokenFilter f;
/* 467 */       switch (t.id()) {
/*     */       case 3: 
/* 469 */         f = this._itemFilter;
/* 470 */         if (f == TokenFilter.INCLUDE_ALL) {
/* 471 */           this._headContext = this._headContext.createChildArrayContext(f, true);
/* 472 */           return this._currToken = t;
/*     */         }
/* 474 */         if (f == null) {
/* 475 */           this.delegate.skipChildren();
/*     */         }
/*     */         else
/*     */         {
/* 479 */           f = this._headContext.checkValue(f);
/* 480 */           if (f == null) {
/* 481 */             this.delegate.skipChildren();
/*     */           }
/*     */           else {
/* 484 */             if (f != TokenFilter.INCLUDE_ALL) {
/* 485 */               f = f.filterStartArray();
/*     */             }
/* 487 */             this._itemFilter = f;
/* 488 */             if (f == TokenFilter.INCLUDE_ALL) {
/* 489 */               this._headContext = this._headContext.createChildArrayContext(f, true);
/* 490 */               return this._currToken = t;
/*     */             }
/* 492 */             this._headContext = this._headContext.createChildArrayContext(f, false);
/*     */             
/* 494 */             if (this._includePath) {
/* 495 */               t = _nextTokenWithBuffering(this._headContext);
/* 496 */               if (t != null) {
/* 497 */                 this._currToken = t;
/* 498 */                 return t;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         break;
/* 504 */       case 1:  f = this._itemFilter;
/* 505 */         if (f == TokenFilter.INCLUDE_ALL) {
/* 506 */           this._headContext = this._headContext.createChildObjectContext(f, true);
/* 507 */           return this._currToken = t;
/*     */         }
/* 509 */         if (f == null) {
/* 510 */           this.delegate.skipChildren();
/*     */         }
/*     */         else
/*     */         {
/* 514 */           f = this._headContext.checkValue(f);
/* 515 */           if (f == null) {
/* 516 */             this.delegate.skipChildren();
/*     */           }
/*     */           else {
/* 519 */             if (f != TokenFilter.INCLUDE_ALL) {
/* 520 */               f = f.filterStartObject();
/*     */             }
/* 522 */             this._itemFilter = f;
/* 523 */             if (f == TokenFilter.INCLUDE_ALL) {
/* 524 */               this._headContext = this._headContext.createChildObjectContext(f, true);
/* 525 */               return this._currToken = t;
/*     */             }
/* 527 */             this._headContext = this._headContext.createChildObjectContext(f, false);
/* 528 */             if (this._includePath) {
/* 529 */               t = _nextTokenWithBuffering(this._headContext);
/* 530 */               if (t != null) {
/* 531 */                 this._currToken = t;
/* 532 */                 return t;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         break;
/*     */       case 2: 
/*     */       case 4: 
/* 540 */         boolean returnEnd = this._headContext.isStartHandled();
/* 541 */         f = this._headContext.getFilter();
/* 542 */         if ((f != null) && (f != TokenFilter.INCLUDE_ALL)) {
/* 543 */           f.filterFinishArray();
/*     */         }
/* 545 */         this._headContext = this._headContext.getParent();
/* 546 */         this._itemFilter = this._headContext.getFilter();
/* 547 */         if (returnEnd) {
/* 548 */           return this._currToken = t;
/*     */         }
/*     */         
/* 551 */         break;
/*     */       
/*     */ 
/*     */       case 5: 
/* 555 */         String name = this.delegate.getCurrentName();
/* 556 */         f = this._headContext.setFieldName(name);
/* 557 */         if (f == TokenFilter.INCLUDE_ALL) {
/* 558 */           this._itemFilter = f;
/* 559 */           return this._currToken = t;
/*     */         }
/* 561 */         if (f == null) {
/* 562 */           this.delegate.nextToken();
/* 563 */           this.delegate.skipChildren();
/*     */         }
/*     */         else {
/* 566 */           f = f.includeProperty(name);
/* 567 */           if (f == null) {
/* 568 */             this.delegate.nextToken();
/* 569 */             this.delegate.skipChildren();
/*     */           }
/*     */           else {
/* 572 */             this._itemFilter = f;
/* 573 */             if (f == TokenFilter.INCLUDE_ALL) {
/* 574 */               if (this._includePath) {
/* 575 */                 return this._currToken = t;
/*     */               }
/*     */               
/*     */ 
/*     */             }
/* 580 */             else if (this._includePath) {
/* 581 */               t = _nextTokenWithBuffering(this._headContext);
/* 582 */               if (t != null) {
/* 583 */                 this._currToken = t;
/* 584 */                 return t;
/*     */               }
/*     */             }
/*     */           } }
/* 588 */         break;
/*     */       
/*     */       default: 
/* 591 */         f = this._itemFilter;
/* 592 */         if (f == TokenFilter.INCLUDE_ALL) {
/* 593 */           return this._currToken = t;
/*     */         }
/* 595 */         if (f != null) {
/* 596 */           f = this._headContext.checkValue(f);
/* 597 */           if ((f == TokenFilter.INCLUDE_ALL) || ((f != null) && (f.includeValue(this.delegate))))
/*     */           {
/* 599 */             return this._currToken = t;
/*     */           }
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected final JsonToken _nextTokenWithBuffering(TokenFilterContext buffRoot) throws IOException
/*     */   {
/*     */     TokenFilter f;
/*     */     do
/*     */     {
/*     */       do
/*     */       {
/*     */         for (;;)
/*     */         {
/* 616 */           JsonToken t = this.delegate.nextToken();
/* 617 */           if (t == null) {
/* 618 */             return t;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 626 */           switch (t.id()) {
/*     */           case 3: 
/* 628 */             f = this._headContext.checkValue(this._itemFilter);
/* 629 */             if (f == null) {
/* 630 */               this.delegate.skipChildren();
/*     */             }
/*     */             else {
/* 633 */               if (f != TokenFilter.INCLUDE_ALL) {
/* 634 */                 f = f.filterStartArray();
/*     */               }
/* 636 */               this._itemFilter = f;
/* 637 */               if (f == TokenFilter.INCLUDE_ALL) {
/* 638 */                 this._headContext = this._headContext.createChildArrayContext(f, true);
/* 639 */                 return _nextBuffered(buffRoot);
/*     */               }
/* 641 */               this._headContext = this._headContext.createChildArrayContext(f, false); }
/* 642 */             break;
/*     */           
/*     */           case 1: 
/* 645 */             f = this._itemFilter;
/* 646 */             if (f == TokenFilter.INCLUDE_ALL) {
/* 647 */               this._headContext = this._headContext.createChildObjectContext(f, true);
/* 648 */               return t;
/*     */             }
/* 650 */             if (f == null) {
/* 651 */               this.delegate.skipChildren();
/*     */             }
/*     */             else
/*     */             {
/* 655 */               f = this._headContext.checkValue(f);
/* 656 */               if (f == null) {
/* 657 */                 this.delegate.skipChildren();
/*     */               }
/*     */               else {
/* 660 */                 if (f != TokenFilter.INCLUDE_ALL) {
/* 661 */                   f = f.filterStartObject();
/*     */                 }
/* 663 */                 this._itemFilter = f;
/* 664 */                 if (f == TokenFilter.INCLUDE_ALL) {
/* 665 */                   this._headContext = this._headContext.createChildObjectContext(f, true);
/* 666 */                   return _nextBuffered(buffRoot);
/*     */                 }
/* 668 */                 this._headContext = this._headContext.createChildObjectContext(f, false); } }
/* 669 */             break;
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */           case 2: 
/*     */           case 4: 
/* 676 */             f = this._headContext.getFilter();
/* 677 */             if ((f != null) && (f != TokenFilter.INCLUDE_ALL)) {
/* 678 */               f.filterFinishArray();
/*     */             }
/* 680 */             boolean gotEnd = this._headContext == buffRoot;
/* 681 */             boolean returnEnd = (gotEnd) && (this._headContext.isStartHandled());
/*     */             
/* 683 */             this._headContext = this._headContext.getParent();
/* 684 */             this._itemFilter = this._headContext.getFilter();
/*     */             
/* 686 */             if (returnEnd) {
/* 687 */               return t;
/*     */             }
/*     */             
/* 690 */             if ((gotEnd) || (this._headContext == buffRoot)) {
/* 691 */               return null;
/*     */             }
/*     */             
/* 694 */             break;
/*     */           
/*     */ 
/*     */           case 5: 
/* 698 */             String name = this.delegate.getCurrentName();
/* 699 */             f = this._headContext.setFieldName(name);
/* 700 */             if (f == TokenFilter.INCLUDE_ALL) {
/* 701 */               this._itemFilter = f;
/* 702 */               return _nextBuffered(buffRoot);
/*     */             }
/* 704 */             if (f == null) {
/* 705 */               this.delegate.nextToken();
/* 706 */               this.delegate.skipChildren();
/*     */             }
/*     */             else {
/* 709 */               f = f.includeProperty(name);
/* 710 */               if (f == null) {
/* 711 */                 this.delegate.nextToken();
/* 712 */                 this.delegate.skipChildren();
/*     */               }
/*     */               else {
/* 715 */                 this._itemFilter = f;
/* 716 */                 if (f == TokenFilter.INCLUDE_ALL)
/* 717 */                   return _nextBuffered(buffRoot);
/*     */               }
/*     */             }
/*     */             break;
/*     */           }
/*     */         }
/* 723 */         f = this._itemFilter;
/* 724 */         if (f == TokenFilter.INCLUDE_ALL) {
/* 725 */           return _nextBuffered(buffRoot);
/*     */         }
/* 727 */       } while (f == null);
/* 728 */       f = this._headContext.checkValue(f);
/* 729 */     } while ((f != TokenFilter.INCLUDE_ALL) && ((f == null) || (!f.includeValue(this.delegate))));
/*     */     
/* 731 */     return _nextBuffered(buffRoot);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private JsonToken _nextBuffered(TokenFilterContext buffRoot)
/*     */     throws IOException
/*     */   {
/* 742 */     this._exposedContext = buffRoot;
/* 743 */     TokenFilterContext ctxt = buffRoot;
/* 744 */     JsonToken t = ctxt.nextTokenToRead();
/* 745 */     if (t != null) {
/* 746 */       return t;
/*     */     }
/*     */     do
/*     */     {
/* 750 */       if (ctxt == this._headContext) {
/* 751 */         throw _constructError("Internal error: failed to locate expected buffered tokens");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 758 */       ctxt = this._exposedContext.findChildOf(ctxt);
/* 759 */       this._exposedContext = ctxt;
/* 760 */       if (ctxt == null) {
/* 761 */         throw _constructError("Unexpected problem: chain of filtered context broken");
/*     */       }
/* 763 */       t = this._exposedContext.nextTokenToRead();
/* 764 */     } while (t == null);
/* 765 */     return t;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonToken nextValue()
/*     */     throws IOException
/*     */   {
/* 773 */     JsonToken t = nextToken();
/* 774 */     if (t == JsonToken.FIELD_NAME) {
/* 775 */       t = nextToken();
/*     */     }
/* 777 */     return t;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonParser skipChildren()
/*     */     throws IOException
/*     */   {
/* 788 */     if ((this._currToken != JsonToken.START_OBJECT) && (this._currToken != JsonToken.START_ARRAY))
/*     */     {
/* 790 */       return this;
/*     */     }
/* 792 */     int open = 1;
/*     */     
/*     */ 
/*     */     for (;;)
/*     */     {
/* 797 */       JsonToken t = nextToken();
/* 798 */       if (t == null) {
/* 799 */         return this;
/*     */       }
/* 801 */       if (t.isStructStart()) {
/* 802 */         open++;
/* 803 */       } else if (t.isStructEnd()) {
/* 804 */         open--; if (open == 0) {
/* 805 */           return this;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 817 */   public String getText()
/* 817 */     throws IOException { return this.delegate.getText(); }
/* 818 */   public boolean hasTextCharacters() { return this.delegate.hasTextCharacters(); }
/* 819 */   public char[] getTextCharacters() throws IOException { return this.delegate.getTextCharacters(); }
/* 820 */   public int getTextLength() throws IOException { return this.delegate.getTextLength(); }
/* 821 */   public int getTextOffset() throws IOException { return this.delegate.getTextOffset(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BigInteger getBigIntegerValue()
/*     */     throws IOException
/*     */   {
/* 830 */     return this.delegate.getBigIntegerValue();
/*     */   }
/*     */   
/* 833 */   public boolean getBooleanValue() throws IOException { return this.delegate.getBooleanValue(); }
/*     */   
/*     */   public byte getByteValue() throws IOException {
/* 836 */     return this.delegate.getByteValue();
/*     */   }
/*     */   
/* 839 */   public short getShortValue() throws IOException { return this.delegate.getShortValue(); }
/*     */   
/*     */   public BigDecimal getDecimalValue() throws IOException {
/* 842 */     return this.delegate.getDecimalValue();
/*     */   }
/*     */   
/* 845 */   public double getDoubleValue() throws IOException { return this.delegate.getDoubleValue(); }
/*     */   
/*     */   public float getFloatValue() throws IOException {
/* 848 */     return this.delegate.getFloatValue();
/*     */   }
/*     */   
/* 851 */   public int getIntValue() throws IOException { return this.delegate.getIntValue(); }
/*     */   
/*     */   public long getLongValue() throws IOException {
/* 854 */     return this.delegate.getLongValue();
/*     */   }
/*     */   
/* 857 */   public JsonParser.NumberType getNumberType() throws IOException { return this.delegate.getNumberType(); }
/*     */   
/*     */   public Number getNumberValue() throws IOException {
/* 860 */     return this.delegate.getNumberValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 868 */   public int getValueAsInt()
/* 868 */     throws IOException { return this.delegate.getValueAsInt(); }
/* 869 */   public int getValueAsInt(int defaultValue) throws IOException { return this.delegate.getValueAsInt(defaultValue); }
/* 870 */   public long getValueAsLong() throws IOException { return this.delegate.getValueAsLong(); }
/* 871 */   public long getValueAsLong(long defaultValue) throws IOException { return this.delegate.getValueAsLong(defaultValue); }
/* 872 */   public double getValueAsDouble() throws IOException { return this.delegate.getValueAsDouble(); }
/* 873 */   public double getValueAsDouble(double defaultValue) throws IOException { return this.delegate.getValueAsDouble(defaultValue); }
/* 874 */   public boolean getValueAsBoolean() throws IOException { return this.delegate.getValueAsBoolean(); }
/* 875 */   public boolean getValueAsBoolean(boolean defaultValue) throws IOException { return this.delegate.getValueAsBoolean(defaultValue); }
/* 876 */   public String getValueAsString() throws IOException { return this.delegate.getValueAsString(); }
/* 877 */   public String getValueAsString(String defaultValue) throws IOException { return this.delegate.getValueAsString(defaultValue); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 885 */   public Object getEmbeddedObject()
/* 885 */     throws IOException { return this.delegate.getEmbeddedObject(); }
/* 886 */   public byte[] getBinaryValue(Base64Variant b64variant) throws IOException { return this.delegate.getBinaryValue(b64variant); }
/* 887 */   public int readBinaryValue(Base64Variant b64variant, OutputStream out) throws IOException { return this.delegate.readBinaryValue(b64variant, out); }
/* 888 */   public JsonLocation getTokenLocation() { return this.delegate.getTokenLocation(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonStreamContext _filterContext()
/*     */   {
/* 897 */     if (this._exposedContext != null) {
/* 898 */       return this._exposedContext;
/*     */     }
/* 900 */     return this._headContext;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\filter\FilteringParserDelegate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */