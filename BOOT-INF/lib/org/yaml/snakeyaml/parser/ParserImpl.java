/*     */ package org.yaml.snakeyaml.parser;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.yaml.snakeyaml.DumperOptions.Version;
/*     */ import org.yaml.snakeyaml.error.Mark;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.events.AliasEvent;
/*     */ import org.yaml.snakeyaml.events.DocumentEndEvent;
/*     */ import org.yaml.snakeyaml.events.DocumentStartEvent;
/*     */ import org.yaml.snakeyaml.events.Event;
/*     */ import org.yaml.snakeyaml.events.Event.ID;
/*     */ import org.yaml.snakeyaml.events.ImplicitTuple;
/*     */ import org.yaml.snakeyaml.events.MappingEndEvent;
/*     */ import org.yaml.snakeyaml.events.MappingStartEvent;
/*     */ import org.yaml.snakeyaml.events.ScalarEvent;
/*     */ import org.yaml.snakeyaml.events.SequenceEndEvent;
/*     */ import org.yaml.snakeyaml.events.SequenceStartEvent;
/*     */ import org.yaml.snakeyaml.events.StreamEndEvent;
/*     */ import org.yaml.snakeyaml.events.StreamStartEvent;
/*     */ import org.yaml.snakeyaml.reader.StreamReader;
/*     */ import org.yaml.snakeyaml.scanner.Scanner;
/*     */ import org.yaml.snakeyaml.scanner.ScannerImpl;
/*     */ import org.yaml.snakeyaml.tokens.AliasToken;
/*     */ import org.yaml.snakeyaml.tokens.AnchorToken;
/*     */ import org.yaml.snakeyaml.tokens.BlockEntryToken;
/*     */ import org.yaml.snakeyaml.tokens.DirectiveToken;
/*     */ import org.yaml.snakeyaml.tokens.ScalarToken;
/*     */ import org.yaml.snakeyaml.tokens.StreamEndToken;
/*     */ import org.yaml.snakeyaml.tokens.StreamStartToken;
/*     */ import org.yaml.snakeyaml.tokens.TagToken;
/*     */ import org.yaml.snakeyaml.tokens.TagTuple;
/*     */ import org.yaml.snakeyaml.tokens.Token;
/*     */ import org.yaml.snakeyaml.tokens.Token.ID;
/*     */ import org.yaml.snakeyaml.util.ArrayStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParserImpl
/*     */   implements Parser
/*     */ {
/* 117 */   private static final Map<String, String> DEFAULT_TAGS = new HashMap();
/*     */   
/* 119 */   static { DEFAULT_TAGS.put("!", "!");
/* 120 */     DEFAULT_TAGS.put("!!", "tag:yaml.org,2002:");
/*     */   }
/*     */   
/*     */ 
/*     */   protected final Scanner scanner;
/*     */   
/*     */   private Event currentEvent;
/*     */   
/*     */   private final ArrayStack<Production> states;
/*     */   public ParserImpl(StreamReader reader)
/*     */   {
/* 131 */     this(new ScannerImpl(reader));
/*     */   }
/*     */   
/*     */   public ParserImpl(Scanner scanner) {
/* 135 */     this.scanner = scanner;
/* 136 */     this.currentEvent = null;
/* 137 */     this.directives = new VersionTagsTuple(null, new HashMap(DEFAULT_TAGS));
/* 138 */     this.states = new ArrayStack(100);
/* 139 */     this.marks = new ArrayStack(10);
/* 140 */     this.state = new ParseStreamStart(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean checkEvent(Event.ID choice)
/*     */   {
/* 147 */     peekEvent();
/* 148 */     return (this.currentEvent != null) && (this.currentEvent.is(choice));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Event peekEvent()
/*     */   {
/* 155 */     if ((this.currentEvent == null) && 
/* 156 */       (this.state != null)) {
/* 157 */       this.currentEvent = this.state.produce();
/*     */     }
/*     */     
/* 160 */     return this.currentEvent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Event getEvent()
/*     */   {
/* 167 */     peekEvent();
/* 168 */     Event value = this.currentEvent;
/* 169 */     this.currentEvent = null;
/* 170 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private class ParseStreamStart
/*     */     implements Production
/*     */   {
/*     */     private ParseStreamStart() {}
/*     */     
/*     */ 
/*     */     public Event produce()
/*     */     {
/* 183 */       StreamStartToken token = (StreamStartToken)ParserImpl.this.scanner.getToken();
/* 184 */       Event event = new StreamStartEvent(token.getStartMark(), token.getEndMark());
/*     */       
/* 186 */       ParserImpl.this.state = new ParserImpl.ParseImplicitDocumentStart(ParserImpl.this, null);
/* 187 */       return event;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseImplicitDocumentStart implements Production {
/*     */     private ParseImplicitDocumentStart() {}
/*     */     
/* 194 */     public Event produce() { if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Directive, Token.ID.DocumentStart, Token.ID.StreamEnd })) {
/* 195 */         ParserImpl.this.directives = new VersionTagsTuple(null, ParserImpl.DEFAULT_TAGS);
/* 196 */         Token token = ParserImpl.this.scanner.peekToken();
/* 197 */         Mark startMark = token.getStartMark();
/* 198 */         Mark endMark = startMark;
/* 199 */         Event event = new DocumentStartEvent(startMark, endMark, false, null, null);
/*     */         
/* 201 */         ParserImpl.this.states.push(new ParserImpl.ParseDocumentEnd(ParserImpl.this, null));
/* 202 */         ParserImpl.this.state = new ParserImpl.ParseBlockNode(ParserImpl.this, null);
/* 203 */         return event;
/*     */       }
/* 205 */       Production p = new ParserImpl.ParseDocumentStart(ParserImpl.this, null);
/* 206 */       return p.produce();
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseDocumentStart implements Production {
/*     */     private ParseDocumentStart() {}
/*     */     
/*     */     public Event produce() {
/* 214 */       while (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.DocumentEnd })) {
/* 215 */         ParserImpl.this.scanner.getToken();
/*     */       }
/*     */       
/*     */       Event event;
/* 219 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.StreamEnd })) {
/* 220 */         Token token = ParserImpl.this.scanner.peekToken();
/* 221 */         Mark startMark = token.getStartMark();
/* 222 */         VersionTagsTuple tuple = ParserImpl.this.processDirectives();
/* 223 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.DocumentStart })) {
/* 224 */           throw new ParserException(null, null, "expected '<document start>', but found " + ParserImpl.this.scanner.peekToken().getTokenId(), ParserImpl.this.scanner.peekToken().getStartMark());
/*     */         }
/*     */         
/* 227 */         token = ParserImpl.this.scanner.getToken();
/* 228 */         Mark endMark = token.getEndMark();
/* 229 */         Event event = new DocumentStartEvent(startMark, endMark, true, tuple.getVersion(), tuple.getTags());
/*     */         
/* 231 */         ParserImpl.this.states.push(new ParserImpl.ParseDocumentEnd(ParserImpl.this, null));
/* 232 */         ParserImpl.this.state = new ParserImpl.ParseDocumentContent(ParserImpl.this, null);
/*     */       }
/*     */       else {
/* 235 */         StreamEndToken token = (StreamEndToken)ParserImpl.this.scanner.getToken();
/* 236 */         event = new StreamEndEvent(token.getStartMark(), token.getEndMark());
/* 237 */         if (!ParserImpl.this.states.isEmpty()) {
/* 238 */           throw new YAMLException("Unexpected end of stream. States left: " + ParserImpl.this.states);
/*     */         }
/* 240 */         if (!ParserImpl.this.marks.isEmpty()) {
/* 241 */           throw new YAMLException("Unexpected end of stream. Marks left: " + ParserImpl.this.marks);
/*     */         }
/* 243 */         ParserImpl.this.state = null;
/*     */       }
/* 245 */       return event;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseDocumentEnd implements Production {
/*     */     private ParseDocumentEnd() {}
/*     */     
/* 252 */     public Event produce() { Token token = ParserImpl.this.scanner.peekToken();
/* 253 */       Mark startMark = token.getStartMark();
/* 254 */       Mark endMark = startMark;
/* 255 */       boolean explicit = false;
/* 256 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.DocumentEnd })) {
/* 257 */         token = ParserImpl.this.scanner.getToken();
/* 258 */         endMark = token.getEndMark();
/* 259 */         explicit = true;
/*     */       }
/* 261 */       Event event = new DocumentEndEvent(startMark, endMark, explicit);
/*     */       
/* 263 */       ParserImpl.this.state = new ParserImpl.ParseDocumentStart(ParserImpl.this, null);
/* 264 */       return event;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseDocumentContent implements Production {
/*     */     private ParseDocumentContent() {}
/*     */     
/* 271 */     public Event produce() { if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Directive, Token.ID.DocumentStart, Token.ID.DocumentEnd, Token.ID.StreamEnd }))
/*     */       {
/* 273 */         Event event = ParserImpl.this.processEmptyScalar(ParserImpl.this.scanner.peekToken().getStartMark());
/* 274 */         ParserImpl.this.state = ((Production)ParserImpl.this.states.pop());
/* 275 */         return event;
/*     */       }
/* 277 */       Production p = new ParserImpl.ParseBlockNode(ParserImpl.this, null);
/* 278 */       return p.produce();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private VersionTagsTuple processDirectives()
/*     */   {
/* 285 */     DumperOptions.Version yamlVersion = null;
/* 286 */     HashMap<String, String> tagHandles = new HashMap();
/* 287 */     while (this.scanner.checkToken(new Token.ID[] { Token.ID.Directive }))
/*     */     {
/* 289 */       DirectiveToken token = (DirectiveToken)this.scanner.getToken();
/* 290 */       if (token.getName().equals("YAML")) {
/* 291 */         if (yamlVersion != null) {
/* 292 */           throw new ParserException(null, null, "found duplicate YAML directive", token.getStartMark());
/*     */         }
/*     */         
/* 295 */         List<Integer> value = token.getValue();
/* 296 */         Integer major = (Integer)value.get(0);
/* 297 */         if (major.intValue() != 1) {
/* 298 */           throw new ParserException(null, null, "found incompatible YAML document (version 1.* is required)", token.getStartMark());
/*     */         }
/*     */         
/*     */ 
/* 302 */         Integer minor = (Integer)value.get(1);
/* 303 */         switch (minor.intValue()) {
/*     */         case 0: 
/* 305 */           yamlVersion = DumperOptions.Version.V1_0;
/* 306 */           break;
/*     */         
/*     */         default: 
/* 309 */           yamlVersion = DumperOptions.Version.V1_1;
/*     */         }
/*     */       }
/* 312 */       else if (token.getName().equals("TAG")) {
/* 313 */         List<String> value = token.getValue();
/* 314 */         String handle = (String)value.get(0);
/* 315 */         String prefix = (String)value.get(1);
/* 316 */         if (tagHandles.containsKey(handle)) {
/* 317 */           throw new ParserException(null, null, "duplicate tag handle " + handle, token.getStartMark());
/*     */         }
/*     */         
/* 320 */         tagHandles.put(handle, prefix);
/*     */       }
/*     */     }
/* 323 */     if ((yamlVersion != null) || (!tagHandles.isEmpty()))
/*     */     {
/* 325 */       for (String key : DEFAULT_TAGS.keySet())
/*     */       {
/* 327 */         if (!tagHandles.containsKey(key)) {
/* 328 */           tagHandles.put(key, DEFAULT_TAGS.get(key));
/*     */         }
/*     */       }
/* 331 */       this.directives = new VersionTagsTuple(yamlVersion, tagHandles);
/*     */     }
/* 333 */     return this.directives;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ArrayStack<Mark> marks;
/*     */   
/*     */ 
/*     */ 
/*     */   private Production state;
/*     */   
/*     */ 
/*     */   private VersionTagsTuple directives;
/*     */   
/*     */ 
/*     */   private class ParseBlockNode
/*     */     implements Production
/*     */   {
/*     */     private ParseBlockNode() {}
/*     */     
/*     */ 
/*     */ 
/*     */     public Event produce()
/*     */     {
/* 358 */       return ParserImpl.this.parseNode(true, false);
/*     */     }
/*     */   }
/*     */   
/*     */   private Event parseFlowNode() {
/* 363 */     return parseNode(false, false);
/*     */   }
/*     */   
/*     */   private Event parseBlockNodeOrIndentlessSequence() {
/* 367 */     return parseNode(true, true);
/*     */   }
/*     */   
/*     */   private Event parseNode(boolean block, boolean indentlessSequence)
/*     */   {
/* 372 */     Mark startMark = null;
/* 373 */     Mark endMark = null;
/* 374 */     Mark tagMark = null;
/* 375 */     Event event; if (this.scanner.checkToken(new Token.ID[] { Token.ID.Alias })) {
/* 376 */       AliasToken token = (AliasToken)this.scanner.getToken();
/* 377 */       Event event = new AliasEvent(token.getValue(), token.getStartMark(), token.getEndMark());
/* 378 */       this.state = ((Production)this.states.pop());
/*     */     } else {
/* 380 */       String anchor = null;
/* 381 */       TagTuple tagTokenTag = null;
/* 382 */       if (this.scanner.checkToken(new Token.ID[] { Token.ID.Anchor })) {
/* 383 */         AnchorToken token = (AnchorToken)this.scanner.getToken();
/* 384 */         startMark = token.getStartMark();
/* 385 */         endMark = token.getEndMark();
/* 386 */         anchor = token.getValue();
/* 387 */         if (this.scanner.checkToken(new Token.ID[] { Token.ID.Tag })) {
/* 388 */           TagToken tagToken = (TagToken)this.scanner.getToken();
/* 389 */           tagMark = tagToken.getStartMark();
/* 390 */           endMark = tagToken.getEndMark();
/* 391 */           tagTokenTag = tagToken.getValue();
/*     */         }
/* 393 */       } else if (this.scanner.checkToken(new Token.ID[] { Token.ID.Tag })) {
/* 394 */         TagToken tagToken = (TagToken)this.scanner.getToken();
/* 395 */         startMark = tagToken.getStartMark();
/* 396 */         tagMark = startMark;
/* 397 */         endMark = tagToken.getEndMark();
/* 398 */         tagTokenTag = tagToken.getValue();
/* 399 */         if (this.scanner.checkToken(new Token.ID[] { Token.ID.Anchor })) {
/* 400 */           AnchorToken token = (AnchorToken)this.scanner.getToken();
/* 401 */           endMark = token.getEndMark();
/* 402 */           anchor = token.getValue();
/*     */         }
/*     */       }
/* 405 */       String tag = null;
/* 406 */       if (tagTokenTag != null) {
/* 407 */         String handle = tagTokenTag.getHandle();
/* 408 */         String suffix = tagTokenTag.getSuffix();
/* 409 */         if (handle != null) {
/* 410 */           if (!this.directives.getTags().containsKey(handle)) {
/* 411 */             throw new ParserException("while parsing a node", startMark, "found undefined tag handle " + handle, tagMark);
/*     */           }
/*     */           
/* 414 */           tag = (String)this.directives.getTags().get(handle) + suffix;
/*     */         } else {
/* 416 */           tag = suffix;
/*     */         }
/*     */       }
/* 419 */       if (startMark == null) {
/* 420 */         startMark = this.scanner.peekToken().getStartMark();
/* 421 */         endMark = startMark;
/*     */       }
/* 423 */       event = null;
/* 424 */       boolean implicit = (tag == null) || (tag.equals("!"));
/* 425 */       if (indentlessSequence) if (this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry })) {
/* 426 */           endMark = this.scanner.peekToken().getEndMark();
/* 427 */           event = new SequenceStartEvent(anchor, tag, implicit, startMark, endMark, Boolean.FALSE);
/*     */           
/* 429 */           this.state = new ParseIndentlessSequenceEntry(null); return event;
/*     */         }
/* 431 */       if (this.scanner.checkToken(new Token.ID[] { Token.ID.Scalar })) {
/* 432 */         ScalarToken token = (ScalarToken)this.scanner.getToken();
/* 433 */         endMark = token.getEndMark();
/*     */         ImplicitTuple implicitValues;
/* 435 */         ImplicitTuple implicitValues; if (((token.getPlain()) && (tag == null)) || ("!".equals(tag))) {
/* 436 */           implicitValues = new ImplicitTuple(true, false); } else { ImplicitTuple implicitValues;
/* 437 */           if (tag == null) {
/* 438 */             implicitValues = new ImplicitTuple(false, true);
/*     */           } else
/* 440 */             implicitValues = new ImplicitTuple(false, false);
/*     */         }
/* 442 */         event = new ScalarEvent(anchor, tag, implicitValues, token.getValue(), startMark, endMark, Character.valueOf(token.getStyle()));
/*     */         
/* 444 */         this.state = ((Production)this.states.pop());
/* 445 */       } else if (this.scanner.checkToken(new Token.ID[] { Token.ID.FlowSequenceStart })) {
/* 446 */         endMark = this.scanner.peekToken().getEndMark();
/* 447 */         event = new SequenceStartEvent(anchor, tag, implicit, startMark, endMark, Boolean.TRUE);
/*     */         
/* 449 */         this.state = new ParseFlowSequenceFirstEntry(null);
/* 450 */       } else if (this.scanner.checkToken(new Token.ID[] { Token.ID.FlowMappingStart })) {
/* 451 */         endMark = this.scanner.peekToken().getEndMark();
/* 452 */         event = new MappingStartEvent(anchor, tag, implicit, startMark, endMark, Boolean.TRUE);
/*     */         
/* 454 */         this.state = new ParseFlowMappingFirstKey(null);
/* 455 */       } else { if (block) if (this.scanner.checkToken(new Token.ID[] { Token.ID.BlockSequenceStart })) {
/* 456 */             endMark = this.scanner.peekToken().getStartMark();
/* 457 */             event = new SequenceStartEvent(anchor, tag, implicit, startMark, endMark, Boolean.FALSE);
/*     */             
/* 459 */             this.state = new ParseBlockSequenceFirstEntry(null); return event; }
/* 460 */         if (block) if (this.scanner.checkToken(new Token.ID[] { Token.ID.BlockMappingStart })) {
/* 461 */             endMark = this.scanner.peekToken().getStartMark();
/* 462 */             event = new MappingStartEvent(anchor, tag, implicit, startMark, endMark, Boolean.FALSE);
/*     */             
/* 464 */             this.state = new ParseBlockMappingFirstKey(null); return event; }
/* 465 */         if ((anchor != null) || (tag != null))
/*     */         {
/*     */ 
/* 468 */           event = new ScalarEvent(anchor, tag, new ImplicitTuple(implicit, false), "", startMark, endMark, Character.valueOf('\000'));
/*     */           
/* 470 */           this.state = ((Production)this.states.pop());
/*     */         } else { String node;
/*     */           String node;
/* 473 */           if (block) {
/* 474 */             node = "block";
/*     */           } else {
/* 476 */             node = "flow";
/*     */           }
/* 478 */           Token token = this.scanner.peekToken();
/* 479 */           throw new ParserException("while parsing a " + node + " node", startMark, "expected the node content, but found " + token.getTokenId(), token.getStartMark());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 485 */     return event;
/*     */   }
/*     */   
/*     */   private class ParseBlockSequenceFirstEntry implements Production
/*     */   {
/*     */     private ParseBlockSequenceFirstEntry() {}
/*     */     
/*     */     public Event produce() {
/* 493 */       Token token = ParserImpl.this.scanner.getToken();
/* 494 */       ParserImpl.this.marks.push(token.getStartMark());
/* 495 */       return new ParserImpl.ParseBlockSequenceEntry(ParserImpl.this, null).produce();
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseBlockSequenceEntry implements Production { private ParseBlockSequenceEntry() {}
/*     */     
/* 501 */     public Event produce() { if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry })) {
/* 502 */         BlockEntryToken token = (BlockEntryToken)ParserImpl.this.scanner.getToken();
/* 503 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry, Token.ID.BlockEnd })) {
/* 504 */           ParserImpl.this.states.push(new ParseBlockSequenceEntry(ParserImpl.this));
/* 505 */           return new ParserImpl.ParseBlockNode(ParserImpl.this, null).produce();
/*     */         }
/* 507 */         ParserImpl.this.state = new ParseBlockSequenceEntry(ParserImpl.this);
/* 508 */         return ParserImpl.this.processEmptyScalar(token.getEndMark());
/*     */       }
/*     */       
/* 511 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEnd })) {
/* 512 */         Token token = ParserImpl.this.scanner.peekToken();
/* 513 */         throw new ParserException("while parsing a block collection", (Mark)ParserImpl.this.marks.pop(), "expected <block end>, but found " + token.getTokenId(), token.getStartMark());
/*     */       }
/*     */       
/*     */ 
/* 517 */       Token token = ParserImpl.this.scanner.getToken();
/* 518 */       Event event = new SequenceEndEvent(token.getStartMark(), token.getEndMark());
/* 519 */       ParserImpl.this.state = ((Production)ParserImpl.this.states.pop());
/* 520 */       ParserImpl.this.marks.pop();
/* 521 */       return event;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseIndentlessSequenceEntry implements Production {
/*     */     private ParseIndentlessSequenceEntry() {}
/*     */     
/*     */     public Event produce() {
/* 529 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry })) {
/* 530 */         Token token = ParserImpl.this.scanner.getToken();
/* 531 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry, Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd }))
/*     */         {
/* 533 */           ParserImpl.this.states.push(new ParseIndentlessSequenceEntry(ParserImpl.this));
/* 534 */           return new ParserImpl.ParseBlockNode(ParserImpl.this, null).produce();
/*     */         }
/* 536 */         ParserImpl.this.state = new ParseIndentlessSequenceEntry(ParserImpl.this);
/* 537 */         return ParserImpl.this.processEmptyScalar(token.getEndMark());
/*     */       }
/*     */       
/* 540 */       Token token = ParserImpl.this.scanner.peekToken();
/* 541 */       Event event = new SequenceEndEvent(token.getStartMark(), token.getEndMark());
/* 542 */       ParserImpl.this.state = ((Production)ParserImpl.this.states.pop());
/* 543 */       return event;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseBlockMappingFirstKey implements Production { private ParseBlockMappingFirstKey() {}
/*     */     
/* 549 */     public Event produce() { Token token = ParserImpl.this.scanner.getToken();
/* 550 */       ParserImpl.this.marks.push(token.getStartMark());
/* 551 */       return new ParserImpl.ParseBlockMappingKey(ParserImpl.this, null).produce();
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseBlockMappingKey implements Production { private ParseBlockMappingKey() {}
/*     */     
/* 557 */     public Event produce() { if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key })) {
/* 558 */         Token token = ParserImpl.this.scanner.getToken();
/* 559 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd })) {
/* 560 */           ParserImpl.this.states.push(new ParserImpl.ParseBlockMappingValue(ParserImpl.this, null));
/* 561 */           return ParserImpl.this.parseBlockNodeOrIndentlessSequence();
/*     */         }
/* 563 */         ParserImpl.this.state = new ParserImpl.ParseBlockMappingValue(ParserImpl.this, null);
/* 564 */         return ParserImpl.this.processEmptyScalar(token.getEndMark());
/*     */       }
/*     */       
/* 567 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEnd })) {
/* 568 */         Token token = ParserImpl.this.scanner.peekToken();
/* 569 */         throw new ParserException("while parsing a block mapping", (Mark)ParserImpl.this.marks.pop(), "expected <block end>, but found " + token.getTokenId(), token.getStartMark());
/*     */       }
/*     */       
/*     */ 
/* 573 */       Token token = ParserImpl.this.scanner.getToken();
/* 574 */       Event event = new MappingEndEvent(token.getStartMark(), token.getEndMark());
/* 575 */       ParserImpl.this.state = ((Production)ParserImpl.this.states.pop());
/* 576 */       ParserImpl.this.marks.pop();
/* 577 */       return event;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseBlockMappingValue implements Production { private ParseBlockMappingValue() {}
/*     */     
/* 583 */     public Event produce() { if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value })) {
/* 584 */         Token token = ParserImpl.this.scanner.getToken();
/* 585 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd })) {
/* 586 */           ParserImpl.this.states.push(new ParserImpl.ParseBlockMappingKey(ParserImpl.this, null));
/* 587 */           return ParserImpl.this.parseBlockNodeOrIndentlessSequence();
/*     */         }
/* 589 */         ParserImpl.this.state = new ParserImpl.ParseBlockMappingKey(ParserImpl.this, null);
/* 590 */         return ParserImpl.this.processEmptyScalar(token.getEndMark());
/*     */       }
/*     */       
/* 593 */       ParserImpl.this.state = new ParserImpl.ParseBlockMappingKey(ParserImpl.this, null);
/* 594 */       Token token = ParserImpl.this.scanner.peekToken();
/* 595 */       return ParserImpl.this.processEmptyScalar(token.getStartMark());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private class ParseFlowSequenceFirstEntry
/*     */     implements Production
/*     */   {
/*     */     private ParseFlowSequenceFirstEntry() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Event produce()
/*     */     {
/* 614 */       Token token = ParserImpl.this.scanner.getToken();
/* 615 */       ParserImpl.this.marks.push(token.getStartMark());
/* 616 */       return new ParserImpl.ParseFlowSequenceEntry(ParserImpl.this, true).produce();
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseFlowSequenceEntry implements Production {
/* 621 */     private boolean first = false;
/*     */     
/*     */     public ParseFlowSequenceEntry(boolean first) {
/* 624 */       this.first = first;
/*     */     }
/*     */     
/*     */     public Event produce() {
/* 628 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowSequenceEnd })) {
/* 629 */         if (!this.first) {
/* 630 */           if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowEntry })) {
/* 631 */             ParserImpl.this.scanner.getToken();
/*     */           } else {
/* 633 */             Token token = ParserImpl.this.scanner.peekToken();
/* 634 */             throw new ParserException("while parsing a flow sequence", (Mark)ParserImpl.this.marks.pop(), "expected ',' or ']', but got " + token.getTokenId(), token.getStartMark());
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 639 */         if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key })) {
/* 640 */           Token token = ParserImpl.this.scanner.peekToken();
/* 641 */           Event event = new MappingStartEvent(null, null, true, token.getStartMark(), token.getEndMark(), Boolean.TRUE);
/*     */           
/* 643 */           ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntryMappingKey(ParserImpl.this, null);
/* 644 */           return event; }
/* 645 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowSequenceEnd })) {
/* 646 */           ParserImpl.this.states.push(new ParseFlowSequenceEntry(ParserImpl.this, false));
/* 647 */           return ParserImpl.this.parseFlowNode();
/*     */         }
/*     */       }
/* 650 */       Token token = ParserImpl.this.scanner.getToken();
/* 651 */       Event event = new SequenceEndEvent(token.getStartMark(), token.getEndMark());
/* 652 */       ParserImpl.this.state = ((Production)ParserImpl.this.states.pop());
/* 653 */       ParserImpl.this.marks.pop();
/* 654 */       return event;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseFlowSequenceEntryMappingKey implements Production { private ParseFlowSequenceEntryMappingKey() {}
/*     */     
/* 660 */     public Event produce() { Token token = ParserImpl.this.scanner.getToken();
/* 661 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value, Token.ID.FlowEntry, Token.ID.FlowSequenceEnd })) {
/* 662 */         ParserImpl.this.states.push(new ParserImpl.ParseFlowSequenceEntryMappingValue(ParserImpl.this, null));
/* 663 */         return ParserImpl.this.parseFlowNode();
/*     */       }
/* 665 */       ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntryMappingValue(ParserImpl.this, null);
/* 666 */       return ParserImpl.this.processEmptyScalar(token.getEndMark());
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseFlowSequenceEntryMappingValue implements Production {
/*     */     private ParseFlowSequenceEntryMappingValue() {}
/*     */     
/* 673 */     public Event produce() { if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value })) {
/* 674 */         Token token = ParserImpl.this.scanner.getToken();
/* 675 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowEntry, Token.ID.FlowSequenceEnd })) {
/* 676 */           ParserImpl.this.states.push(new ParserImpl.ParseFlowSequenceEntryMappingEnd(ParserImpl.this, null));
/* 677 */           return ParserImpl.this.parseFlowNode();
/*     */         }
/* 679 */         ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntryMappingEnd(ParserImpl.this, null);
/* 680 */         return ParserImpl.this.processEmptyScalar(token.getEndMark());
/*     */       }
/*     */       
/* 683 */       ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntryMappingEnd(ParserImpl.this, null);
/* 684 */       Token token = ParserImpl.this.scanner.peekToken();
/* 685 */       return ParserImpl.this.processEmptyScalar(token.getStartMark());
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseFlowSequenceEntryMappingEnd implements Production {
/*     */     private ParseFlowSequenceEntryMappingEnd() {}
/*     */     
/* 692 */     public Event produce() { ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntry(ParserImpl.this, false);
/* 693 */       Token token = ParserImpl.this.scanner.peekToken();
/* 694 */       return new MappingEndEvent(token.getStartMark(), token.getEndMark());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private class ParseFlowMappingFirstKey
/*     */     implements Production
/*     */   {
/*     */     private ParseFlowMappingFirstKey() {}
/*     */     
/*     */ 
/*     */ 
/*     */     public Event produce()
/*     */     {
/* 709 */       Token token = ParserImpl.this.scanner.getToken();
/* 710 */       ParserImpl.this.marks.push(token.getStartMark());
/* 711 */       return new ParserImpl.ParseFlowMappingKey(ParserImpl.this, true).produce();
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseFlowMappingKey implements Production {
/* 716 */     private boolean first = false;
/*     */     
/*     */     public ParseFlowMappingKey(boolean first) {
/* 719 */       this.first = first;
/*     */     }
/*     */     
/*     */     public Event produce() {
/* 723 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowMappingEnd })) {
/* 724 */         if (!this.first) {
/* 725 */           if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowEntry })) {
/* 726 */             ParserImpl.this.scanner.getToken();
/*     */           } else {
/* 728 */             Token token = ParserImpl.this.scanner.peekToken();
/* 729 */             throw new ParserException("while parsing a flow mapping", (Mark)ParserImpl.this.marks.pop(), "expected ',' or '}', but got " + token.getTokenId(), token.getStartMark());
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 734 */         if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key })) {
/* 735 */           Token token = ParserImpl.this.scanner.getToken();
/* 736 */           if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value, Token.ID.FlowEntry, Token.ID.FlowMappingEnd }))
/*     */           {
/* 738 */             ParserImpl.this.states.push(new ParserImpl.ParseFlowMappingValue(ParserImpl.this, null));
/* 739 */             return ParserImpl.this.parseFlowNode();
/*     */           }
/* 741 */           ParserImpl.this.state = new ParserImpl.ParseFlowMappingValue(ParserImpl.this, null);
/* 742 */           return ParserImpl.this.processEmptyScalar(token.getEndMark());
/*     */         }
/* 744 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowMappingEnd })) {
/* 745 */           ParserImpl.this.states.push(new ParserImpl.ParseFlowMappingEmptyValue(ParserImpl.this, null));
/* 746 */           return ParserImpl.this.parseFlowNode();
/*     */         }
/*     */       }
/* 749 */       Token token = ParserImpl.this.scanner.getToken();
/* 750 */       Event event = new MappingEndEvent(token.getStartMark(), token.getEndMark());
/* 751 */       ParserImpl.this.state = ((Production)ParserImpl.this.states.pop());
/* 752 */       ParserImpl.this.marks.pop();
/* 753 */       return event;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseFlowMappingValue implements Production { private ParseFlowMappingValue() {}
/*     */     
/* 759 */     public Event produce() { if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value })) {
/* 760 */         Token token = ParserImpl.this.scanner.getToken();
/* 761 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowEntry, Token.ID.FlowMappingEnd })) {
/* 762 */           ParserImpl.this.states.push(new ParserImpl.ParseFlowMappingKey(ParserImpl.this, false));
/* 763 */           return ParserImpl.this.parseFlowNode();
/*     */         }
/* 765 */         ParserImpl.this.state = new ParserImpl.ParseFlowMappingKey(ParserImpl.this, false);
/* 766 */         return ParserImpl.this.processEmptyScalar(token.getEndMark());
/*     */       }
/*     */       
/* 769 */       ParserImpl.this.state = new ParserImpl.ParseFlowMappingKey(ParserImpl.this, false);
/* 770 */       Token token = ParserImpl.this.scanner.peekToken();
/* 771 */       return ParserImpl.this.processEmptyScalar(token.getStartMark());
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseFlowMappingEmptyValue implements Production {
/*     */     private ParseFlowMappingEmptyValue() {}
/*     */     
/* 778 */     public Event produce() { ParserImpl.this.state = new ParserImpl.ParseFlowMappingKey(ParserImpl.this, false);
/* 779 */       return ParserImpl.this.processEmptyScalar(ParserImpl.this.scanner.peekToken().getStartMark());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Event processEmptyScalar(Mark mark)
/*     */   {
/* 792 */     return new ScalarEvent(null, null, new ImplicitTuple(true, false), "", mark, mark, Character.valueOf('\000'));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\parser\ParserImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */