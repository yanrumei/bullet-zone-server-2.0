/*      */ package org.yaml.snakeyaml.emitter;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.TreeSet;
/*      */ import java.util.concurrent.ArrayBlockingQueue;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import org.yaml.snakeyaml.DumperOptions;
/*      */ import org.yaml.snakeyaml.DumperOptions.LineBreak;
/*      */ import org.yaml.snakeyaml.DumperOptions.Version;
/*      */ import org.yaml.snakeyaml.error.YAMLException;
/*      */ import org.yaml.snakeyaml.events.AliasEvent;
/*      */ import org.yaml.snakeyaml.events.CollectionEndEvent;
/*      */ import org.yaml.snakeyaml.events.CollectionStartEvent;
/*      */ import org.yaml.snakeyaml.events.DocumentEndEvent;
/*      */ import org.yaml.snakeyaml.events.DocumentStartEvent;
/*      */ import org.yaml.snakeyaml.events.Event;
/*      */ import org.yaml.snakeyaml.events.ImplicitTuple;
/*      */ import org.yaml.snakeyaml.events.MappingEndEvent;
/*      */ import org.yaml.snakeyaml.events.MappingStartEvent;
/*      */ import org.yaml.snakeyaml.events.NodeEvent;
/*      */ import org.yaml.snakeyaml.events.ScalarEvent;
/*      */ import org.yaml.snakeyaml.events.SequenceEndEvent;
/*      */ import org.yaml.snakeyaml.events.SequenceStartEvent;
/*      */ import org.yaml.snakeyaml.events.StreamEndEvent;
/*      */ import org.yaml.snakeyaml.events.StreamStartEvent;
/*      */ import org.yaml.snakeyaml.reader.StreamReader;
/*      */ import org.yaml.snakeyaml.scanner.Constant;
/*      */ import org.yaml.snakeyaml.util.ArrayStack;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Emitter
/*      */   implements Emitable
/*      */ {
/*   63 */   private static final Map<Character, String> ESCAPE_REPLACEMENTS = new HashMap();
/*      */   
/*      */   public static final int MIN_INDENT = 1;
/*      */   public static final int MAX_INDENT = 10;
/*   67 */   private static final char[] SPACE = { ' ' };
/*      */   private static final Map<String, String> DEFAULT_TAG_PREFIXES;
/*      */   
/*   70 */   static { ESCAPE_REPLACEMENTS.put(Character.valueOf('\000'), "0");
/*   71 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\007'), "a");
/*   72 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\b'), "b");
/*   73 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\t'), "t");
/*   74 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\n'), "n");
/*   75 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\013'), "v");
/*   76 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\f'), "f");
/*   77 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\r'), "r");
/*   78 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\033'), "e");
/*   79 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('"'), "\"");
/*   80 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\\'), "\\");
/*   81 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(''), "N");
/*   82 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), "_");
/*   83 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), "L");
/*   84 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), "P");
/*      */     
/*      */ 
/*   87 */     DEFAULT_TAG_PREFIXES = new LinkedHashMap();
/*      */     
/*   89 */     DEFAULT_TAG_PREFIXES.put("!", "!");
/*   90 */     DEFAULT_TAG_PREFIXES.put("tag:yaml.org,2002:", "!!");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private final Writer stream;
/*      */   
/*      */ 
/*      */   private final ArrayStack<EmitterState> states;
/*      */   
/*      */ 
/*      */   private EmitterState state;
/*      */   
/*      */ 
/*      */   private final Queue<Event> events;
/*      */   
/*      */   private Event event;
/*      */   
/*      */   private final ArrayStack<Integer> indents;
/*      */   
/*      */   private Integer indent;
/*      */   
/*      */   private int flowLevel;
/*      */   
/*      */   private boolean rootContext;
/*      */   
/*      */   private boolean mappingContext;
/*      */   
/*      */   private boolean simpleKeyContext;
/*      */   
/*      */   private int column;
/*      */   
/*      */   private boolean whitespace;
/*      */   
/*      */   private boolean indention;
/*      */   
/*      */   private boolean openEnded;
/*      */   
/*      */   private Boolean canonical;
/*      */   
/*      */   private Boolean prettyFlow;
/*      */   
/*      */   private boolean allowUnicode;
/*      */   
/*      */   private int bestIndent;
/*      */   
/*      */   private int indicatorIndent;
/*      */   
/*      */   private int bestWidth;
/*      */   
/*      */   private char[] bestLineBreak;
/*      */   
/*      */   private boolean splitLines;
/*      */   
/*      */   private Map<String, String> tagPrefixes;
/*      */   
/*      */   private String preparedAnchor;
/*      */   
/*      */   private String preparedTag;
/*      */   
/*      */   private ScalarAnalysis analysis;
/*      */   
/*      */   private Character style;
/*      */   
/*      */   public Emitter(Writer stream, DumperOptions opts)
/*      */   {
/*  156 */     this.stream = stream;
/*      */     
/*      */ 
/*  159 */     this.states = new ArrayStack(100);
/*  160 */     this.state = new ExpectStreamStart(null);
/*      */     
/*  162 */     this.events = new ArrayBlockingQueue(100);
/*  163 */     this.event = null;
/*      */     
/*  165 */     this.indents = new ArrayStack(10);
/*  166 */     this.indent = null;
/*      */     
/*  168 */     this.flowLevel = 0;
/*      */     
/*  170 */     this.mappingContext = false;
/*  171 */     this.simpleKeyContext = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  179 */     this.column = 0;
/*  180 */     this.whitespace = true;
/*  181 */     this.indention = true;
/*      */     
/*      */ 
/*  184 */     this.openEnded = false;
/*      */     
/*      */ 
/*  187 */     this.canonical = Boolean.valueOf(opts.isCanonical());
/*  188 */     this.prettyFlow = Boolean.valueOf(opts.isPrettyFlow());
/*  189 */     this.allowUnicode = opts.isAllowUnicode();
/*  190 */     this.bestIndent = 2;
/*  191 */     if ((opts.getIndent() > 1) && (opts.getIndent() < 10)) {
/*  192 */       this.bestIndent = opts.getIndent();
/*      */     }
/*  194 */     this.indicatorIndent = opts.getIndicatorIndent();
/*  195 */     this.bestWidth = 80;
/*  196 */     if (opts.getWidth() > this.bestIndent * 2) {
/*  197 */       this.bestWidth = opts.getWidth();
/*      */     }
/*  199 */     this.bestLineBreak = opts.getLineBreak().getString().toCharArray();
/*  200 */     this.splitLines = opts.getSplitLines();
/*      */     
/*      */ 
/*  203 */     this.tagPrefixes = new LinkedHashMap();
/*      */     
/*      */ 
/*  206 */     this.preparedAnchor = null;
/*  207 */     this.preparedTag = null;
/*      */     
/*      */ 
/*  210 */     this.analysis = null;
/*  211 */     this.style = null;
/*      */   }
/*      */   
/*      */   public void emit(Event event) throws IOException {
/*  215 */     this.events.add(event);
/*  216 */     while (!needMoreEvents()) {
/*  217 */       this.event = ((Event)this.events.poll());
/*  218 */       this.state.expect();
/*  219 */       this.event = null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean needMoreEvents()
/*      */   {
/*  226 */     if (this.events.isEmpty()) {
/*  227 */       return true;
/*      */     }
/*  229 */     Event event = (Event)this.events.peek();
/*  230 */     if ((event instanceof DocumentStartEvent))
/*  231 */       return needEvents(1);
/*  232 */     if ((event instanceof SequenceStartEvent))
/*  233 */       return needEvents(2);
/*  234 */     if ((event instanceof MappingStartEvent)) {
/*  235 */       return needEvents(3);
/*      */     }
/*  237 */     return false;
/*      */   }
/*      */   
/*      */   private boolean needEvents(int count)
/*      */   {
/*  242 */     int level = 0;
/*  243 */     Iterator<Event> iter = this.events.iterator();
/*  244 */     iter.next();
/*  245 */     while (iter.hasNext()) {
/*  246 */       Event event = (Event)iter.next();
/*  247 */       if (((event instanceof DocumentStartEvent)) || ((event instanceof CollectionStartEvent))) {
/*  248 */         level++;
/*  249 */       } else if (((event instanceof DocumentEndEvent)) || ((event instanceof CollectionEndEvent))) {
/*  250 */         level--;
/*  251 */       } else if ((event instanceof StreamEndEvent)) {
/*  252 */         level = -1;
/*      */       }
/*  254 */       if (level < 0) {
/*  255 */         return false;
/*      */       }
/*      */     }
/*  258 */     return this.events.size() < count + 1;
/*      */   }
/*      */   
/*      */   private void increaseIndent(boolean flow, boolean indentless) {
/*  262 */     this.indents.push(this.indent);
/*  263 */     if (this.indent == null) {
/*  264 */       if (flow) {
/*  265 */         this.indent = Integer.valueOf(this.bestIndent);
/*      */       } else {
/*  267 */         this.indent = Integer.valueOf(0);
/*      */       }
/*  269 */     } else if (!indentless) {
/*  270 */       Emitter localEmitter = this;(localEmitter.indent = Integer.valueOf(localEmitter.indent.intValue() + this.bestIndent));
/*      */     }
/*      */   }
/*      */   
/*      */   private class ExpectStreamStart implements EmitterState
/*      */   {
/*      */     private ExpectStreamStart() {}
/*      */     
/*      */     public void expect() throws IOException
/*      */     {
/*  280 */       if ((Emitter.this.event instanceof StreamStartEvent)) {
/*  281 */         Emitter.this.writeStreamStart();
/*  282 */         Emitter.this.state = new Emitter.ExpectFirstDocumentStart(Emitter.this, null);
/*      */       } else {
/*  284 */         throw new EmitterException("expected StreamStartEvent, but got " + Emitter.this.event);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private class ExpectNothing implements EmitterState { private ExpectNothing() {}
/*      */     
/*  291 */     public void expect() throws IOException { throw new EmitterException("expecting nothing, but got " + Emitter.this.event); }
/*      */   }
/*      */   
/*      */   private class ExpectFirstDocumentStart implements EmitterState
/*      */   {
/*      */     private ExpectFirstDocumentStart() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  299 */       new Emitter.ExpectDocumentStart(Emitter.this, true).expect();
/*      */     }
/*      */   }
/*      */   
/*      */   private class ExpectDocumentStart implements EmitterState {
/*      */     private boolean first;
/*      */     
/*      */     public ExpectDocumentStart(boolean first) {
/*  307 */       this.first = first;
/*      */     }
/*      */     
/*      */     public void expect() throws IOException {
/*  311 */       if ((Emitter.this.event instanceof DocumentStartEvent)) {
/*  312 */         DocumentStartEvent ev = (DocumentStartEvent)Emitter.this.event;
/*  313 */         if (((ev.getVersion() != null) || (ev.getTags() != null)) && (Emitter.this.openEnded)) {
/*  314 */           Emitter.this.writeIndicator("...", true, false, false);
/*  315 */           Emitter.this.writeIndent();
/*      */         }
/*  317 */         if (ev.getVersion() != null) {
/*  318 */           String versionText = Emitter.this.prepareVersion(ev.getVersion());
/*  319 */           Emitter.this.writeVersionDirective(versionText);
/*      */         }
/*  321 */         Emitter.this.tagPrefixes = new LinkedHashMap(Emitter.DEFAULT_TAG_PREFIXES);
/*  322 */         if (ev.getTags() != null) {
/*  323 */           Set<String> handles = new TreeSet(ev.getTags().keySet());
/*  324 */           for (String handle : handles) {
/*  325 */             String prefix = (String)ev.getTags().get(handle);
/*  326 */             Emitter.this.tagPrefixes.put(prefix, handle);
/*  327 */             String handleText = Emitter.this.prepareTagHandle(handle);
/*  328 */             String prefixText = Emitter.this.prepareTagPrefix(prefix);
/*  329 */             Emitter.this.writeTagDirective(handleText, prefixText);
/*      */           }
/*      */         }
/*  332 */         boolean implicit = (this.first) && (!ev.getExplicit()) && (!Emitter.this.canonical.booleanValue()) && (ev.getVersion() == null) && ((ev.getTags() == null) || (ev.getTags().isEmpty())) && (!Emitter.this.checkEmptyDocument());
/*      */         
/*      */ 
/*      */ 
/*  336 */         if (!implicit) {
/*  337 */           Emitter.this.writeIndent();
/*  338 */           Emitter.this.writeIndicator("---", true, false, false);
/*  339 */           if (Emitter.this.canonical.booleanValue()) {
/*  340 */             Emitter.this.writeIndent();
/*      */           }
/*      */         }
/*  343 */         Emitter.this.state = new Emitter.ExpectDocumentRoot(Emitter.this, null);
/*  344 */       } else if ((Emitter.this.event instanceof StreamEndEvent))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  350 */         Emitter.this.writeStreamEnd();
/*  351 */         Emitter.this.state = new Emitter.ExpectNothing(Emitter.this, null);
/*      */       } else {
/*  353 */         throw new EmitterException("expected DocumentStartEvent, but got " + Emitter.this.event);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private class ExpectDocumentEnd implements EmitterState { private ExpectDocumentEnd() {}
/*      */     
/*  360 */     public void expect() throws IOException { if ((Emitter.this.event instanceof DocumentEndEvent)) {
/*  361 */         Emitter.this.writeIndent();
/*  362 */         if (((DocumentEndEvent)Emitter.this.event).getExplicit()) {
/*  363 */           Emitter.this.writeIndicator("...", true, false, false);
/*  364 */           Emitter.this.writeIndent();
/*      */         }
/*  366 */         Emitter.this.flushStream();
/*  367 */         Emitter.this.state = new Emitter.ExpectDocumentStart(Emitter.this, false);
/*      */       } else {
/*  369 */         throw new EmitterException("expected DocumentEndEvent, but got " + Emitter.this.event);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private class ExpectDocumentRoot implements EmitterState { private ExpectDocumentRoot() {}
/*      */     
/*  376 */     public void expect() throws IOException { Emitter.this.states.push(new Emitter.ExpectDocumentEnd(Emitter.this, null));
/*  377 */       Emitter.this.expectNode(true, false, false);
/*      */     }
/*      */   }
/*      */   
/*      */   private void expectNode(boolean root, boolean mapping, boolean simpleKey)
/*      */     throws IOException
/*      */   {
/*  384 */     this.rootContext = root;
/*  385 */     this.mappingContext = mapping;
/*  386 */     this.simpleKeyContext = simpleKey;
/*  387 */     if ((this.event instanceof AliasEvent)) {
/*  388 */       expectAlias();
/*  389 */     } else if (((this.event instanceof ScalarEvent)) || ((this.event instanceof CollectionStartEvent))) {
/*  390 */       processAnchor("&");
/*  391 */       processTag();
/*  392 */       if ((this.event instanceof ScalarEvent)) {
/*  393 */         expectScalar();
/*  394 */       } else if ((this.event instanceof SequenceStartEvent)) {
/*  395 */         if ((this.flowLevel != 0) || (this.canonical.booleanValue()) || (((SequenceStartEvent)this.event).getFlowStyle().booleanValue()) || (checkEmptySequence()))
/*      */         {
/*  397 */           expectFlowSequence();
/*      */         } else {
/*  399 */           expectBlockSequence();
/*      */         }
/*      */       }
/*  402 */       else if ((this.flowLevel != 0) || (this.canonical.booleanValue()) || (((MappingStartEvent)this.event).getFlowStyle().booleanValue()) || (checkEmptyMapping()))
/*      */       {
/*  404 */         expectFlowMapping();
/*      */       } else {
/*  406 */         expectBlockMapping();
/*      */       }
/*      */     }
/*      */     else {
/*  410 */       throw new EmitterException("expected NodeEvent, but got " + this.event);
/*      */     }
/*      */   }
/*      */   
/*      */   private void expectAlias() throws IOException {
/*  415 */     if (((NodeEvent)this.event).getAnchor() == null) {
/*  416 */       throw new EmitterException("anchor is not specified for alias");
/*      */     }
/*  418 */     processAnchor("*");
/*  419 */     this.state = ((EmitterState)this.states.pop());
/*      */   }
/*      */   
/*      */   private void expectScalar() throws IOException {
/*  423 */     increaseIndent(true, false);
/*  424 */     processScalar();
/*  425 */     this.indent = ((Integer)this.indents.pop());
/*  426 */     this.state = ((EmitterState)this.states.pop());
/*      */   }
/*      */   
/*      */   private void expectFlowSequence()
/*      */     throws IOException
/*      */   {
/*  432 */     writeIndicator("[", true, true, false);
/*  433 */     this.flowLevel += 1;
/*  434 */     increaseIndent(true, false);
/*  435 */     if (this.prettyFlow.booleanValue()) {
/*  436 */       writeIndent();
/*      */     }
/*  438 */     this.state = new ExpectFirstFlowSequenceItem(null);
/*      */   }
/*      */   
/*      */   private class ExpectFirstFlowSequenceItem implements EmitterState { private ExpectFirstFlowSequenceItem() {}
/*      */     
/*  443 */     public void expect() throws IOException { if ((Emitter.this.event instanceof SequenceEndEvent)) {
/*  444 */         Emitter.this.indent = ((Integer)Emitter.this.indents.pop());
/*  445 */         Emitter.access$2010(Emitter.this);
/*  446 */         Emitter.this.writeIndicator("]", false, false, false);
/*  447 */         Emitter.this.state = ((EmitterState)Emitter.this.states.pop());
/*      */       } else {
/*  449 */         if ((Emitter.this.canonical.booleanValue()) || ((Emitter.this.column > Emitter.this.bestWidth) && (Emitter.this.splitLines)) || (Emitter.this.prettyFlow.booleanValue())) {
/*  450 */           Emitter.this.writeIndent();
/*      */         }
/*  452 */         Emitter.this.states.push(new Emitter.ExpectFlowSequenceItem(Emitter.this, null));
/*  453 */         Emitter.this.expectNode(false, false, false);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private class ExpectFlowSequenceItem implements EmitterState { private ExpectFlowSequenceItem() {}
/*      */     
/*  460 */     public void expect() throws IOException { if ((Emitter.this.event instanceof SequenceEndEvent)) {
/*  461 */         Emitter.this.indent = ((Integer)Emitter.this.indents.pop());
/*  462 */         Emitter.access$2010(Emitter.this);
/*  463 */         if (Emitter.this.canonical.booleanValue()) {
/*  464 */           Emitter.this.writeIndicator(",", false, false, false);
/*  465 */           Emitter.this.writeIndent();
/*      */         }
/*  467 */         Emitter.this.writeIndicator("]", false, false, false);
/*  468 */         if (Emitter.this.prettyFlow.booleanValue()) {
/*  469 */           Emitter.this.writeIndent();
/*      */         }
/*  471 */         Emitter.this.state = ((EmitterState)Emitter.this.states.pop());
/*      */       } else {
/*  473 */         Emitter.this.writeIndicator(",", false, false, false);
/*  474 */         if ((Emitter.this.canonical.booleanValue()) || ((Emitter.this.column > Emitter.this.bestWidth) && (Emitter.this.splitLines)) || (Emitter.this.prettyFlow.booleanValue())) {
/*  475 */           Emitter.this.writeIndent();
/*      */         }
/*  477 */         Emitter.this.states.push(new ExpectFlowSequenceItem(Emitter.this));
/*  478 */         Emitter.this.expectNode(false, false, false);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void expectFlowMapping()
/*      */     throws IOException
/*      */   {
/*  486 */     writeIndicator("{", true, true, false);
/*  487 */     this.flowLevel += 1;
/*  488 */     increaseIndent(true, false);
/*  489 */     if (this.prettyFlow.booleanValue()) {
/*  490 */       writeIndent();
/*      */     }
/*  492 */     this.state = new ExpectFirstFlowMappingKey(null);
/*      */   }
/*      */   
/*      */   private class ExpectFirstFlowMappingKey implements EmitterState { private ExpectFirstFlowMappingKey() {}
/*      */     
/*  497 */     public void expect() throws IOException { if ((Emitter.this.event instanceof MappingEndEvent)) {
/*  498 */         Emitter.this.indent = ((Integer)Emitter.this.indents.pop());
/*  499 */         Emitter.access$2010(Emitter.this);
/*  500 */         Emitter.this.writeIndicator("}", false, false, false);
/*  501 */         Emitter.this.state = ((EmitterState)Emitter.this.states.pop());
/*      */       } else {
/*  503 */         if ((Emitter.this.canonical.booleanValue()) || ((Emitter.this.column > Emitter.this.bestWidth) && (Emitter.this.splitLines)) || (Emitter.this.prettyFlow.booleanValue())) {
/*  504 */           Emitter.this.writeIndent();
/*      */         }
/*  506 */         if ((!Emitter.this.canonical.booleanValue()) && (Emitter.this.checkSimpleKey())) {
/*  507 */           Emitter.this.states.push(new Emitter.ExpectFlowMappingSimpleValue(Emitter.this, null));
/*  508 */           Emitter.this.expectNode(false, true, true);
/*      */         } else {
/*  510 */           Emitter.this.writeIndicator("?", true, false, false);
/*  511 */           Emitter.this.states.push(new Emitter.ExpectFlowMappingValue(Emitter.this, null));
/*  512 */           Emitter.this.expectNode(false, true, false);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private class ExpectFlowMappingKey implements EmitterState { private ExpectFlowMappingKey() {}
/*      */     
/*  520 */     public void expect() throws IOException { if ((Emitter.this.event instanceof MappingEndEvent)) {
/*  521 */         Emitter.this.indent = ((Integer)Emitter.this.indents.pop());
/*  522 */         Emitter.access$2010(Emitter.this);
/*  523 */         if (Emitter.this.canonical.booleanValue()) {
/*  524 */           Emitter.this.writeIndicator(",", false, false, false);
/*  525 */           Emitter.this.writeIndent();
/*      */         }
/*  527 */         if (Emitter.this.prettyFlow.booleanValue()) {
/*  528 */           Emitter.this.writeIndent();
/*      */         }
/*  530 */         Emitter.this.writeIndicator("}", false, false, false);
/*  531 */         Emitter.this.state = ((EmitterState)Emitter.this.states.pop());
/*      */       } else {
/*  533 */         Emitter.this.writeIndicator(",", false, false, false);
/*  534 */         if ((Emitter.this.canonical.booleanValue()) || ((Emitter.this.column > Emitter.this.bestWidth) && (Emitter.this.splitLines)) || (Emitter.this.prettyFlow.booleanValue())) {
/*  535 */           Emitter.this.writeIndent();
/*      */         }
/*  537 */         if ((!Emitter.this.canonical.booleanValue()) && (Emitter.this.checkSimpleKey())) {
/*  538 */           Emitter.this.states.push(new Emitter.ExpectFlowMappingSimpleValue(Emitter.this, null));
/*  539 */           Emitter.this.expectNode(false, true, true);
/*      */         } else {
/*  541 */           Emitter.this.writeIndicator("?", true, false, false);
/*  542 */           Emitter.this.states.push(new Emitter.ExpectFlowMappingValue(Emitter.this, null));
/*  543 */           Emitter.this.expectNode(false, true, false);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private class ExpectFlowMappingSimpleValue implements EmitterState { private ExpectFlowMappingSimpleValue() {}
/*      */     
/*  551 */     public void expect() throws IOException { Emitter.this.writeIndicator(":", false, false, false);
/*  552 */       Emitter.this.states.push(new Emitter.ExpectFlowMappingKey(Emitter.this, null));
/*  553 */       Emitter.this.expectNode(false, true, false);
/*      */     }
/*      */   }
/*      */   
/*      */   private class ExpectFlowMappingValue implements EmitterState { private ExpectFlowMappingValue() {}
/*      */     
/*  559 */     public void expect() throws IOException { if ((Emitter.this.canonical.booleanValue()) || (Emitter.this.column > Emitter.this.bestWidth) || (Emitter.this.prettyFlow.booleanValue())) {
/*  560 */         Emitter.this.writeIndent();
/*      */       }
/*  562 */       Emitter.this.writeIndicator(":", true, false, false);
/*  563 */       Emitter.this.states.push(new Emitter.ExpectFlowMappingKey(Emitter.this, null));
/*  564 */       Emitter.this.expectNode(false, true, false);
/*      */     }
/*      */   }
/*      */   
/*      */   private void expectBlockSequence()
/*      */     throws IOException
/*      */   {
/*  571 */     boolean indentless = (this.mappingContext) && (!this.indention);
/*  572 */     increaseIndent(false, indentless);
/*  573 */     this.state = new ExpectFirstBlockSequenceItem(null);
/*      */   }
/*      */   
/*      */   private class ExpectFirstBlockSequenceItem implements EmitterState { private ExpectFirstBlockSequenceItem() {}
/*      */     
/*  578 */     public void expect() throws IOException { new Emitter.ExpectBlockSequenceItem(Emitter.this, true).expect(); }
/*      */   }
/*      */   
/*      */   private class ExpectBlockSequenceItem implements EmitterState
/*      */   {
/*      */     private boolean first;
/*      */     
/*      */     public ExpectBlockSequenceItem(boolean first) {
/*  586 */       this.first = first;
/*      */     }
/*      */     
/*      */     public void expect() throws IOException {
/*  590 */       if ((!this.first) && ((Emitter.this.event instanceof SequenceEndEvent))) {
/*  591 */         Emitter.this.indent = ((Integer)Emitter.this.indents.pop());
/*  592 */         Emitter.this.state = ((EmitterState)Emitter.this.states.pop());
/*      */       } else {
/*  594 */         Emitter.this.writeIndent();
/*  595 */         Emitter.this.writeWhitespace(Emitter.this.indicatorIndent);
/*  596 */         Emitter.this.writeIndicator("-", true, false, true);
/*  597 */         Emitter.this.states.push(new ExpectBlockSequenceItem(Emitter.this, false));
/*  598 */         Emitter.this.expectNode(false, false, false);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void expectBlockMapping() throws IOException
/*      */   {
/*  605 */     increaseIndent(false, false);
/*  606 */     this.state = new ExpectFirstBlockMappingKey(null);
/*      */   }
/*      */   
/*      */   private class ExpectFirstBlockMappingKey implements EmitterState { private ExpectFirstBlockMappingKey() {}
/*      */     
/*  611 */     public void expect() throws IOException { new Emitter.ExpectBlockMappingKey(Emitter.this, true).expect(); }
/*      */   }
/*      */   
/*      */   private class ExpectBlockMappingKey implements EmitterState
/*      */   {
/*      */     private boolean first;
/*      */     
/*      */     public ExpectBlockMappingKey(boolean first) {
/*  619 */       this.first = first;
/*      */     }
/*      */     
/*      */     public void expect() throws IOException {
/*  623 */       if ((!this.first) && ((Emitter.this.event instanceof MappingEndEvent))) {
/*  624 */         Emitter.this.indent = ((Integer)Emitter.this.indents.pop());
/*  625 */         Emitter.this.state = ((EmitterState)Emitter.this.states.pop());
/*      */       } else {
/*  627 */         Emitter.this.writeIndent();
/*  628 */         if (Emitter.this.checkSimpleKey()) {
/*  629 */           Emitter.this.states.push(new Emitter.ExpectBlockMappingSimpleValue(Emitter.this, null));
/*  630 */           Emitter.this.expectNode(false, true, true);
/*      */         } else {
/*  632 */           Emitter.this.writeIndicator("?", true, false, true);
/*  633 */           Emitter.this.states.push(new Emitter.ExpectBlockMappingValue(Emitter.this, null));
/*  634 */           Emitter.this.expectNode(false, true, false);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private class ExpectBlockMappingSimpleValue implements EmitterState { private ExpectBlockMappingSimpleValue() {}
/*      */     
/*  642 */     public void expect() throws IOException { Emitter.this.writeIndicator(":", false, false, false);
/*  643 */       Emitter.this.states.push(new Emitter.ExpectBlockMappingKey(Emitter.this, false));
/*  644 */       Emitter.this.expectNode(false, true, false);
/*      */     }
/*      */   }
/*      */   
/*      */   private class ExpectBlockMappingValue implements EmitterState { private ExpectBlockMappingValue() {}
/*      */     
/*  650 */     public void expect() throws IOException { Emitter.this.writeIndent();
/*  651 */       Emitter.this.writeIndicator(":", true, false, true);
/*  652 */       Emitter.this.states.push(new Emitter.ExpectBlockMappingKey(Emitter.this, false));
/*  653 */       Emitter.this.expectNode(false, true, false);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean checkEmptySequence()
/*      */   {
/*  660 */     return ((this.event instanceof SequenceStartEvent)) && (!this.events.isEmpty()) && ((this.events.peek() instanceof SequenceEndEvent));
/*      */   }
/*      */   
/*      */   private boolean checkEmptyMapping() {
/*  664 */     return ((this.event instanceof MappingStartEvent)) && (!this.events.isEmpty()) && ((this.events.peek() instanceof MappingEndEvent));
/*      */   }
/*      */   
/*      */   private boolean checkEmptyDocument() {
/*  668 */     if ((!(this.event instanceof DocumentStartEvent)) || (this.events.isEmpty())) {
/*  669 */       return false;
/*      */     }
/*  671 */     Event event = (Event)this.events.peek();
/*  672 */     if ((event instanceof ScalarEvent)) {
/*  673 */       ScalarEvent e = (ScalarEvent)event;
/*  674 */       return (e.getAnchor() == null) && (e.getTag() == null) && (e.getImplicit() != null) && (e.getValue().length() == 0);
/*      */     }
/*      */     
/*  677 */     return false;
/*      */   }
/*      */   
/*      */   private boolean checkSimpleKey() {
/*  681 */     int length = 0;
/*  682 */     if (((this.event instanceof NodeEvent)) && (((NodeEvent)this.event).getAnchor() != null)) {
/*  683 */       if (this.preparedAnchor == null) {
/*  684 */         this.preparedAnchor = prepareAnchor(((NodeEvent)this.event).getAnchor());
/*      */       }
/*  686 */       length += this.preparedAnchor.length();
/*      */     }
/*  688 */     String tag = null;
/*  689 */     if ((this.event instanceof ScalarEvent)) {
/*  690 */       tag = ((ScalarEvent)this.event).getTag();
/*  691 */     } else if ((this.event instanceof CollectionStartEvent)) {
/*  692 */       tag = ((CollectionStartEvent)this.event).getTag();
/*      */     }
/*  694 */     if (tag != null) {
/*  695 */       if (this.preparedTag == null) {
/*  696 */         this.preparedTag = prepareTag(tag);
/*      */       }
/*  698 */       length += this.preparedTag.length();
/*      */     }
/*  700 */     if ((this.event instanceof ScalarEvent)) {
/*  701 */       if (this.analysis == null) {
/*  702 */         this.analysis = analyzeScalar(((ScalarEvent)this.event).getValue());
/*      */       }
/*  704 */       length += this.analysis.scalar.length();
/*      */     }
/*  706 */     return (length < 128) && (((this.event instanceof AliasEvent)) || (((this.event instanceof ScalarEvent)) && (!this.analysis.empty) && (!this.analysis.multiline)) || (checkEmptySequence()) || (checkEmptyMapping()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void processAnchor(String indicator)
/*      */     throws IOException
/*      */   {
/*  714 */     NodeEvent ev = (NodeEvent)this.event;
/*  715 */     if (ev.getAnchor() == null) {
/*  716 */       this.preparedAnchor = null;
/*  717 */       return;
/*      */     }
/*  719 */     if (this.preparedAnchor == null) {
/*  720 */       this.preparedAnchor = prepareAnchor(ev.getAnchor());
/*      */     }
/*  722 */     writeIndicator(indicator + this.preparedAnchor, true, false, false);
/*  723 */     this.preparedAnchor = null;
/*      */   }
/*      */   
/*      */   private void processTag() throws IOException {
/*  727 */     String tag = null;
/*  728 */     if ((this.event instanceof ScalarEvent)) {
/*  729 */       ScalarEvent ev = (ScalarEvent)this.event;
/*  730 */       tag = ev.getTag();
/*  731 */       if (this.style == null) {
/*  732 */         this.style = chooseScalarStyle();
/*      */       }
/*  734 */       if (((!this.canonical.booleanValue()) || (tag == null)) && (((this.style == null) && (ev.getImplicit().canOmitTagInPlainScalar())) || ((this.style != null) && (ev.getImplicit().canOmitTagInNonPlainScalar()))))
/*      */       {
/*      */ 
/*  737 */         this.preparedTag = null;
/*  738 */         return;
/*      */       }
/*  740 */       if ((ev.getImplicit().canOmitTagInPlainScalar()) && (tag == null)) {
/*  741 */         tag = "!";
/*  742 */         this.preparedTag = null;
/*      */       }
/*      */     } else {
/*  745 */       CollectionStartEvent ev = (CollectionStartEvent)this.event;
/*  746 */       tag = ev.getTag();
/*  747 */       if (((!this.canonical.booleanValue()) || (tag == null)) && (ev.getImplicit())) {
/*  748 */         this.preparedTag = null;
/*  749 */         return;
/*      */       }
/*      */     }
/*  752 */     if (tag == null) {
/*  753 */       throw new EmitterException("tag is not specified");
/*      */     }
/*  755 */     if (this.preparedTag == null) {
/*  756 */       this.preparedTag = prepareTag(tag);
/*      */     }
/*  758 */     writeIndicator(this.preparedTag, true, false, false);
/*  759 */     this.preparedTag = null;
/*      */   }
/*      */   
/*      */   private Character chooseScalarStyle() {
/*  763 */     ScalarEvent ev = (ScalarEvent)this.event;
/*  764 */     if (this.analysis == null) {
/*  765 */       this.analysis = analyzeScalar(ev.getValue());
/*      */     }
/*  767 */     if (((ev.getStyle() != null) && (ev.getStyle().charValue() == '"')) || (this.canonical.booleanValue())) {
/*  768 */       return Character.valueOf('"');
/*      */     }
/*  770 */     if ((ev.getStyle() == null) && (ev.getImplicit().canOmitTagInPlainScalar()) && 
/*  771 */       ((!this.simpleKeyContext) || ((!this.analysis.empty) && (!this.analysis.multiline))) && (((this.flowLevel != 0) && (this.analysis.allowFlowPlain)) || ((this.flowLevel == 0) && (this.analysis.allowBlockPlain))))
/*      */     {
/*  773 */       return null;
/*      */     }
/*      */     
/*  776 */     if ((ev.getStyle() != null) && ((ev.getStyle().charValue() == '|') || (ev.getStyle().charValue() == '>')) && 
/*  777 */       (this.flowLevel == 0) && (!this.simpleKeyContext) && (this.analysis.allowBlock)) {
/*  778 */       return ev.getStyle();
/*      */     }
/*      */     
/*  781 */     if (((ev.getStyle() == null) || (ev.getStyle().charValue() == '\'')) && 
/*  782 */       (this.analysis.allowSingleQuoted) && ((!this.simpleKeyContext) || (!this.analysis.multiline))) {
/*  783 */       return Character.valueOf('\'');
/*      */     }
/*      */     
/*  786 */     return Character.valueOf('"');
/*      */   }
/*      */   
/*      */   private void processScalar() throws IOException {
/*  790 */     ScalarEvent ev = (ScalarEvent)this.event;
/*  791 */     if (this.analysis == null) {
/*  792 */       this.analysis = analyzeScalar(ev.getValue());
/*      */     }
/*  794 */     if (this.style == null) {
/*  795 */       this.style = chooseScalarStyle();
/*      */     }
/*  797 */     boolean split = (!this.simpleKeyContext) && (this.splitLines);
/*  798 */     if (this.style == null) {
/*  799 */       writePlain(this.analysis.scalar, split);
/*      */     } else {
/*  801 */       switch (this.style.charValue()) {
/*      */       case '"': 
/*  803 */         writeDoubleQuoted(this.analysis.scalar, split);
/*  804 */         break;
/*      */       case '\'': 
/*  806 */         writeSingleQuoted(this.analysis.scalar, split);
/*  807 */         break;
/*      */       case '>': 
/*  809 */         writeFolded(this.analysis.scalar, split);
/*  810 */         break;
/*      */       case '|': 
/*  812 */         writeLiteral(this.analysis.scalar);
/*  813 */         break;
/*      */       default: 
/*  815 */         throw new YAMLException("Unexpected style: " + this.style);
/*      */       }
/*      */     }
/*  818 */     this.analysis = null;
/*  819 */     this.style = null;
/*      */   }
/*      */   
/*      */ 
/*      */   private String prepareVersion(DumperOptions.Version version)
/*      */   {
/*  825 */     if (version.major() != 1) {
/*  826 */       throw new EmitterException("unsupported YAML version: " + version);
/*      */     }
/*  828 */     return version.getRepresentation();
/*      */   }
/*      */   
/*  831 */   private static final Pattern HANDLE_FORMAT = Pattern.compile("^![-_\\w]*!$");
/*      */   
/*      */   private String prepareTagHandle(String handle) {
/*  834 */     if (handle.length() == 0)
/*  835 */       throw new EmitterException("tag handle must not be empty");
/*  836 */     if ((handle.charAt(0) != '!') || (handle.charAt(handle.length() - 1) != '!'))
/*  837 */       throw new EmitterException("tag handle must start and end with '!': " + handle);
/*  838 */     if ((!"!".equals(handle)) && (!HANDLE_FORMAT.matcher(handle).matches())) {
/*  839 */       throw new EmitterException("invalid character in the tag handle: " + handle);
/*      */     }
/*  841 */     return handle;
/*      */   }
/*      */   
/*      */   private String prepareTagPrefix(String prefix) {
/*  845 */     if (prefix.length() == 0) {
/*  846 */       throw new EmitterException("tag prefix must not be empty");
/*      */     }
/*  848 */     StringBuilder chunks = new StringBuilder();
/*  849 */     int start = 0;
/*  850 */     int end = 0;
/*  851 */     if (prefix.charAt(0) == '!') {
/*  852 */       end = 1;
/*      */     }
/*  854 */     while (end < prefix.length()) {
/*  855 */       end++;
/*      */     }
/*  857 */     if (start < end) {
/*  858 */       chunks.append(prefix.substring(start, end));
/*      */     }
/*  860 */     return chunks.toString();
/*      */   }
/*      */   
/*      */   private String prepareTag(String tag) {
/*  864 */     if (tag.length() == 0) {
/*  865 */       throw new EmitterException("tag must not be empty");
/*      */     }
/*  867 */     if ("!".equals(tag)) {
/*  868 */       return tag;
/*      */     }
/*  870 */     String handle = null;
/*  871 */     String suffix = tag;
/*      */     
/*  873 */     for (String prefix : this.tagPrefixes.keySet()) {
/*  874 */       if ((tag.startsWith(prefix)) && (("!".equals(prefix)) || (prefix.length() < tag.length()))) {
/*  875 */         handle = prefix;
/*      */       }
/*      */     }
/*  878 */     if (handle != null) {
/*  879 */       suffix = tag.substring(handle.length());
/*  880 */       handle = (String)this.tagPrefixes.get(handle);
/*      */     }
/*      */     
/*  883 */     int end = suffix.length();
/*  884 */     String suffixText = end > 0 ? suffix.substring(0, end) : "";
/*      */     
/*  886 */     if (handle != null) {
/*  887 */       return handle + suffixText;
/*      */     }
/*  889 */     return "!<" + suffixText + ">";
/*      */   }
/*      */   
/*  892 */   private static final Pattern ANCHOR_FORMAT = Pattern.compile("^[-_\\w]*$");
/*      */   
/*      */   static String prepareAnchor(String anchor) {
/*  895 */     if (anchor.length() == 0) {
/*  896 */       throw new EmitterException("anchor must not be empty");
/*      */     }
/*  898 */     if (!ANCHOR_FORMAT.matcher(anchor).matches()) {
/*  899 */       throw new EmitterException("invalid character in the anchor: " + anchor);
/*      */     }
/*  901 */     return anchor;
/*      */   }
/*      */   
/*      */   private ScalarAnalysis analyzeScalar(String scalar)
/*      */   {
/*  906 */     if (scalar.length() == 0) {
/*  907 */       return new ScalarAnalysis(scalar, true, false, false, true, true, false);
/*      */     }
/*      */     
/*  910 */     boolean blockIndicators = false;
/*  911 */     boolean flowIndicators = false;
/*  912 */     boolean lineBreaks = false;
/*  913 */     boolean specialCharacters = false;
/*      */     
/*      */ 
/*  916 */     boolean leadingSpace = false;
/*  917 */     boolean leadingBreak = false;
/*  918 */     boolean trailingSpace = false;
/*  919 */     boolean trailingBreak = false;
/*  920 */     boolean breakSpace = false;
/*  921 */     boolean spaceBreak = false;
/*      */     
/*      */ 
/*  924 */     if ((scalar.startsWith("---")) || (scalar.startsWith("..."))) {
/*  925 */       blockIndicators = true;
/*  926 */       flowIndicators = true;
/*      */     }
/*      */     
/*  929 */     boolean preceededByWhitespace = true;
/*  930 */     boolean followedByWhitespace = (scalar.length() == 1) || (Constant.NULL_BL_T_LINEBR.has(scalar.charAt(1)));
/*      */     
/*  932 */     boolean previousSpace = false;
/*      */     
/*      */ 
/*  935 */     boolean previousBreak = false;
/*      */     
/*  937 */     int index = 0;
/*      */     
/*  939 */     while (index < scalar.length()) {
/*  940 */       char ch = scalar.charAt(index);
/*      */       
/*  942 */       if (index == 0)
/*      */       {
/*  944 */         if ("#,[]{}&*!|>'\"%@`".indexOf(ch) != -1) {
/*  945 */           flowIndicators = true;
/*  946 */           blockIndicators = true;
/*      */         }
/*  948 */         if ((ch == '?') || (ch == ':')) {
/*  949 */           flowIndicators = true;
/*  950 */           if (followedByWhitespace) {
/*  951 */             blockIndicators = true;
/*      */           }
/*      */         }
/*  954 */         if ((ch == '-') && (followedByWhitespace)) {
/*  955 */           flowIndicators = true;
/*  956 */           blockIndicators = true;
/*      */         }
/*      */       }
/*      */       else {
/*  960 */         if (",?[]{}".indexOf(ch) != -1) {
/*  961 */           flowIndicators = true;
/*      */         }
/*  963 */         if (ch == ':') {
/*  964 */           flowIndicators = true;
/*  965 */           if (followedByWhitespace) {
/*  966 */             blockIndicators = true;
/*      */           }
/*      */         }
/*  969 */         if ((ch == '#') && (preceededByWhitespace)) {
/*  970 */           flowIndicators = true;
/*  971 */           blockIndicators = true;
/*      */         }
/*      */       }
/*      */       
/*  975 */       boolean isLineBreak = Constant.LINEBR.has(ch);
/*  976 */       if (isLineBreak) {
/*  977 */         lineBreaks = true;
/*      */       }
/*  979 */       if ((ch != '\n') && ((' ' > ch) || (ch > '~'))) {
/*  980 */         if (((ch == '') || ((' ' <= ch) && (ch <= 55295)) || ((57344 <= ch) && (ch <= 65533))) && (ch != 65279))
/*      */         {
/*      */ 
/*  983 */           if (!this.allowUnicode) {
/*  984 */             specialCharacters = true;
/*      */           }
/*      */         } else {
/*  987 */           specialCharacters = true;
/*      */         }
/*      */       }
/*      */       
/*  991 */       if (ch == ' ') {
/*  992 */         if (index == 0) {
/*  993 */           leadingSpace = true;
/*      */         }
/*  995 */         if (index == scalar.length() - 1) {
/*  996 */           trailingSpace = true;
/*      */         }
/*  998 */         if (previousBreak) {
/*  999 */           breakSpace = true;
/*      */         }
/* 1001 */         previousSpace = true;
/* 1002 */         previousBreak = false;
/* 1003 */       } else if (isLineBreak) {
/* 1004 */         if (index == 0) {
/* 1005 */           leadingBreak = true;
/*      */         }
/* 1007 */         if (index == scalar.length() - 1) {
/* 1008 */           trailingBreak = true;
/*      */         }
/* 1010 */         if (previousSpace) {
/* 1011 */           spaceBreak = true;
/*      */         }
/* 1013 */         previousSpace = false;
/* 1014 */         previousBreak = true;
/*      */       } else {
/* 1016 */         previousSpace = false;
/* 1017 */         previousBreak = false;
/*      */       }
/*      */       
/*      */ 
/* 1021 */       index++;
/* 1022 */       preceededByWhitespace = (Constant.NULL_BL_T.has(ch)) || (isLineBreak);
/* 1023 */       followedByWhitespace = (index + 1 >= scalar.length()) || (Constant.NULL_BL_T.has(scalar.charAt(index + 1))) || (isLineBreak);
/*      */     }
/*      */     
/*      */ 
/* 1027 */     boolean allowFlowPlain = true;
/* 1028 */     boolean allowBlockPlain = true;
/* 1029 */     boolean allowSingleQuoted = true;
/* 1030 */     boolean allowBlock = true;
/*      */     
/* 1032 */     if ((leadingSpace) || (leadingBreak) || (trailingSpace) || (trailingBreak)) {
/* 1033 */       allowFlowPlain = allowBlockPlain = 0;
/*      */     }
/*      */     
/* 1036 */     if (trailingSpace) {
/* 1037 */       allowBlock = false;
/*      */     }
/*      */     
/*      */ 
/* 1041 */     if (breakSpace) {
/* 1042 */       allowFlowPlain = allowBlockPlain = allowSingleQuoted = 0;
/*      */     }
/*      */     
/*      */ 
/* 1046 */     if ((spaceBreak) || (specialCharacters)) {
/* 1047 */       allowFlowPlain = allowBlockPlain = allowSingleQuoted = allowBlock = 0;
/*      */     }
/*      */     
/*      */ 
/* 1051 */     if (lineBreaks) {
/* 1052 */       allowFlowPlain = false;
/*      */     }
/*      */     
/* 1055 */     if (flowIndicators) {
/* 1056 */       allowFlowPlain = false;
/*      */     }
/*      */     
/* 1059 */     if (blockIndicators) {
/* 1060 */       allowBlockPlain = false;
/*      */     }
/*      */     
/* 1063 */     return new ScalarAnalysis(scalar, false, lineBreaks, allowFlowPlain, allowBlockPlain, allowSingleQuoted, allowBlock);
/*      */   }
/*      */   
/*      */ 
/*      */   void flushStream()
/*      */     throws IOException
/*      */   {
/* 1070 */     this.stream.flush();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   void writeStreamEnd()
/*      */     throws IOException
/*      */   {
/* 1078 */     flushStream();
/*      */   }
/*      */   
/*      */   void writeIndicator(String indicator, boolean needWhitespace, boolean whitespace, boolean indentation) throws IOException
/*      */   {
/* 1083 */     if ((!this.whitespace) && (needWhitespace)) {
/* 1084 */       this.column += 1;
/* 1085 */       this.stream.write(SPACE);
/*      */     }
/* 1087 */     this.whitespace = whitespace;
/* 1088 */     this.indention = ((this.indention) && (indentation));
/* 1089 */     this.column += indicator.length();
/* 1090 */     this.openEnded = false;
/* 1091 */     this.stream.write(indicator);
/*      */   }
/*      */   
/*      */   void writeIndent() throws IOException { int indent;
/*      */     int indent;
/* 1096 */     if (this.indent != null) {
/* 1097 */       indent = this.indent.intValue();
/*      */     } else {
/* 1099 */       indent = 0;
/*      */     }
/*      */     
/* 1102 */     if ((!this.indention) || (this.column > indent) || ((this.column == indent) && (!this.whitespace))) {
/* 1103 */       writeLineBreak(null);
/*      */     }
/*      */     
/* 1106 */     writeWhitespace(indent - this.column);
/*      */   }
/*      */   
/*      */   private void writeWhitespace(int length) throws IOException {
/* 1110 */     if (length <= 0) {
/* 1111 */       return;
/*      */     }
/* 1113 */     this.whitespace = true;
/* 1114 */     char[] data = new char[length];
/* 1115 */     for (int i = 0; i < data.length; i++) {
/* 1116 */       data[i] = ' ';
/*      */     }
/* 1118 */     this.column += length;
/* 1119 */     this.stream.write(data);
/*      */   }
/*      */   
/*      */   private void writeLineBreak(String data) throws IOException {
/* 1123 */     this.whitespace = true;
/* 1124 */     this.indention = true;
/* 1125 */     this.column = 0;
/* 1126 */     if (data == null) {
/* 1127 */       this.stream.write(this.bestLineBreak);
/*      */     } else {
/* 1129 */       this.stream.write(data);
/*      */     }
/*      */   }
/*      */   
/*      */   void writeVersionDirective(String versionText) throws IOException {
/* 1134 */     this.stream.write("%YAML ");
/* 1135 */     this.stream.write(versionText);
/* 1136 */     writeLineBreak(null);
/*      */   }
/*      */   
/*      */   void writeTagDirective(String handleText, String prefixText)
/*      */     throws IOException
/*      */   {
/* 1142 */     this.stream.write("%TAG ");
/* 1143 */     this.stream.write(handleText);
/* 1144 */     this.stream.write(SPACE);
/* 1145 */     this.stream.write(prefixText);
/* 1146 */     writeLineBreak(null);
/*      */   }
/*      */   
/*      */   private void writeSingleQuoted(String text, boolean split) throws IOException
/*      */   {
/* 1151 */     writeIndicator("'", true, false, false);
/* 1152 */     boolean spaces = false;
/* 1153 */     boolean breaks = false;
/* 1154 */     int start = 0;int end = 0;
/*      */     
/* 1156 */     while (end <= text.length()) {
/* 1157 */       char ch = '\000';
/* 1158 */       if (end < text.length()) {
/* 1159 */         ch = text.charAt(end);
/*      */       }
/* 1161 */       if (spaces) {
/* 1162 */         if ((ch == 0) || (ch != ' ')) {
/* 1163 */           if ((start + 1 == end) && (this.column > this.bestWidth) && (split) && (start != 0) && (end != text.length()))
/*      */           {
/* 1165 */             writeIndent();
/*      */           } else {
/* 1167 */             int len = end - start;
/* 1168 */             this.column += len;
/* 1169 */             this.stream.write(text, start, len);
/*      */           }
/* 1171 */           start = end;
/*      */         }
/* 1173 */       } else if (breaks) {
/* 1174 */         if ((ch == 0) || (Constant.LINEBR.hasNo(ch))) {
/* 1175 */           if (text.charAt(start) == '\n') {
/* 1176 */             writeLineBreak(null);
/*      */           }
/* 1178 */           String data = text.substring(start, end);
/* 1179 */           for (char br : data.toCharArray()) {
/* 1180 */             if (br == '\n') {
/* 1181 */               writeLineBreak(null);
/*      */             } else {
/* 1183 */               writeLineBreak(String.valueOf(br));
/*      */             }
/*      */           }
/* 1186 */           writeIndent();
/* 1187 */           start = end;
/*      */         }
/*      */       }
/* 1190 */       else if ((Constant.LINEBR.has(ch, "\000 '")) && 
/* 1191 */         (start < end)) {
/* 1192 */         int len = end - start;
/* 1193 */         this.column += len;
/* 1194 */         this.stream.write(text, start, len);
/* 1195 */         start = end;
/*      */       }
/*      */       
/*      */ 
/* 1199 */       if (ch == '\'') {
/* 1200 */         this.column += 2;
/* 1201 */         this.stream.write("''");
/* 1202 */         start = end + 1;
/*      */       }
/* 1204 */       if (ch != 0) {
/* 1205 */         spaces = ch == ' ';
/* 1206 */         breaks = Constant.LINEBR.has(ch);
/*      */       }
/* 1208 */       end++;
/*      */     }
/* 1210 */     writeIndicator("'", false, false, false);
/*      */   }
/*      */   
/*      */   private void writeDoubleQuoted(String text, boolean split) throws IOException {
/* 1214 */     writeIndicator("\"", true, false, false);
/* 1215 */     int start = 0;
/* 1216 */     int end = 0;
/* 1217 */     while (end <= text.length()) {
/* 1218 */       Character ch = null;
/* 1219 */       if (end < text.length()) {
/* 1220 */         ch = Character.valueOf(text.charAt(end));
/*      */       }
/* 1222 */       if ((ch == null) || ("\"\\  ﻿".indexOf(ch.charValue()) != -1) || (' ' > ch.charValue()) || (ch.charValue() > '~'))
/*      */       {
/* 1224 */         if (start < end) {
/* 1225 */           int len = end - start;
/* 1226 */           this.column += len;
/* 1227 */           this.stream.write(text, start, len);
/* 1228 */           start = end;
/*      */         }
/* 1230 */         if (ch != null) { String data;
/*      */           String data;
/* 1232 */           if (ESCAPE_REPLACEMENTS.containsKey(ch)) {
/* 1233 */             data = "\\" + (String)ESCAPE_REPLACEMENTS.get(ch); } else { String data;
/* 1234 */             if ((!this.allowUnicode) || (!StreamReader.isPrintable(ch.charValue())))
/*      */             {
/*      */               String data;
/* 1237 */               if (ch.charValue() <= 'ÿ') {
/* 1238 */                 String s = "0" + Integer.toString(ch.charValue(), 16);
/* 1239 */                 data = "\\x" + s.substring(s.length() - 2); } else { String data;
/* 1240 */                 if ((ch.charValue() >= 55296) && (ch.charValue() <= 56319)) { String data;
/* 1241 */                   if (end + 1 < text.length()) {
/* 1242 */                     Character ch2 = Character.valueOf(text.charAt(++end));
/* 1243 */                     String s = "000" + Long.toHexString(Character.toCodePoint(ch.charValue(), ch2.charValue()));
/* 1244 */                     data = "\\U" + s.substring(s.length() - 8);
/*      */                   } else {
/* 1246 */                     String s = "000" + Integer.toString(ch.charValue(), 16);
/* 1247 */                     data = "\\u" + s.substring(s.length() - 4);
/*      */                   }
/*      */                 } else {
/* 1250 */                   String s = "000" + Integer.toString(ch.charValue(), 16);
/* 1251 */                   data = "\\u" + s.substring(s.length() - 4);
/*      */                 }
/*      */               }
/* 1254 */             } else { data = String.valueOf(ch);
/*      */             } }
/* 1256 */           this.column += data.length();
/* 1257 */           this.stream.write(data);
/* 1258 */           start = end + 1;
/*      */         }
/*      */       }
/* 1261 */       if ((0 < end) && (end < text.length() - 1) && ((ch.charValue() == ' ') || (start >= end)) && (this.column + (end - start) > this.bestWidth) && (split)) {
/*      */         String data;
/*      */         String data;
/* 1264 */         if (start >= end) {
/* 1265 */           data = "\\";
/*      */         } else {
/* 1267 */           data = text.substring(start, end) + "\\";
/*      */         }
/* 1269 */         if (start < end) {
/* 1270 */           start = end;
/*      */         }
/* 1272 */         this.column += data.length();
/* 1273 */         this.stream.write(data);
/* 1274 */         writeIndent();
/* 1275 */         this.whitespace = false;
/* 1276 */         this.indention = false;
/* 1277 */         if (text.charAt(start) == ' ') {
/* 1278 */           data = "\\";
/* 1279 */           this.column += data.length();
/* 1280 */           this.stream.write(data);
/*      */         }
/*      */       }
/* 1283 */       end++;
/*      */     }
/* 1285 */     writeIndicator("\"", false, false, false);
/*      */   }
/*      */   
/*      */   private String determineBlockHints(String text) {
/* 1289 */     StringBuilder hints = new StringBuilder();
/* 1290 */     if (Constant.LINEBR.has(text.charAt(0), " ")) {
/* 1291 */       hints.append(this.bestIndent);
/*      */     }
/* 1293 */     char ch1 = text.charAt(text.length() - 1);
/* 1294 */     if (Constant.LINEBR.hasNo(ch1)) {
/* 1295 */       hints.append("-");
/* 1296 */     } else if ((text.length() == 1) || (Constant.LINEBR.has(text.charAt(text.length() - 2)))) {
/* 1297 */       hints.append("+");
/*      */     }
/* 1299 */     return hints.toString();
/*      */   }
/*      */   
/*      */   void writeFolded(String text, boolean split) throws IOException {
/* 1303 */     String hints = determineBlockHints(text);
/* 1304 */     writeIndicator(">" + hints, true, false, false);
/* 1305 */     if ((hints.length() > 0) && (hints.charAt(hints.length() - 1) == '+')) {
/* 1306 */       this.openEnded = true;
/*      */     }
/* 1308 */     writeLineBreak(null);
/* 1309 */     boolean leadingSpace = true;
/* 1310 */     boolean spaces = false;
/* 1311 */     boolean breaks = true;
/* 1312 */     int start = 0;int end = 0;
/* 1313 */     while (end <= text.length()) {
/* 1314 */       char ch = '\000';
/* 1315 */       if (end < text.length()) {
/* 1316 */         ch = text.charAt(end);
/*      */       }
/* 1318 */       if (breaks) {
/* 1319 */         if ((ch == 0) || (Constant.LINEBR.hasNo(ch))) {
/* 1320 */           if ((!leadingSpace) && (ch != 0) && (ch != ' ') && (text.charAt(start) == '\n')) {
/* 1321 */             writeLineBreak(null);
/*      */           }
/* 1323 */           leadingSpace = ch == ' ';
/* 1324 */           String data = text.substring(start, end);
/* 1325 */           for (char br : data.toCharArray()) {
/* 1326 */             if (br == '\n') {
/* 1327 */               writeLineBreak(null);
/*      */             } else {
/* 1329 */               writeLineBreak(String.valueOf(br));
/*      */             }
/*      */           }
/* 1332 */           if (ch != 0) {
/* 1333 */             writeIndent();
/*      */           }
/* 1335 */           start = end;
/*      */         }
/* 1337 */       } else if (spaces) {
/* 1338 */         if (ch != ' ') {
/* 1339 */           if ((start + 1 == end) && (this.column > this.bestWidth) && (split)) {
/* 1340 */             writeIndent();
/*      */           } else {
/* 1342 */             int len = end - start;
/* 1343 */             this.column += len;
/* 1344 */             this.stream.write(text, start, len);
/*      */           }
/* 1346 */           start = end;
/*      */         }
/*      */       }
/* 1349 */       else if (Constant.LINEBR.has(ch, "\000 ")) {
/* 1350 */         int len = end - start;
/* 1351 */         this.column += len;
/* 1352 */         this.stream.write(text, start, len);
/* 1353 */         if (ch == 0) {
/* 1354 */           writeLineBreak(null);
/*      */         }
/* 1356 */         start = end;
/*      */       }
/*      */       
/* 1359 */       if (ch != 0) {
/* 1360 */         breaks = Constant.LINEBR.has(ch);
/* 1361 */         spaces = ch == ' ';
/*      */       }
/* 1363 */       end++;
/*      */     }
/*      */   }
/*      */   
/*      */   void writeLiteral(String text) throws IOException {
/* 1368 */     String hints = determineBlockHints(text);
/* 1369 */     writeIndicator("|" + hints, true, false, false);
/* 1370 */     if ((hints.length() > 0) && (hints.charAt(hints.length() - 1) == '+')) {
/* 1371 */       this.openEnded = true;
/*      */     }
/* 1373 */     writeLineBreak(null);
/* 1374 */     boolean breaks = true;
/* 1375 */     int start = 0;int end = 0;
/* 1376 */     while (end <= text.length()) {
/* 1377 */       char ch = '\000';
/* 1378 */       if (end < text.length()) {
/* 1379 */         ch = text.charAt(end);
/*      */       }
/* 1381 */       if (breaks) {
/* 1382 */         if ((ch == 0) || (Constant.LINEBR.hasNo(ch))) {
/* 1383 */           String data = text.substring(start, end);
/* 1384 */           for (char br : data.toCharArray()) {
/* 1385 */             if (br == '\n') {
/* 1386 */               writeLineBreak(null);
/*      */             } else {
/* 1388 */               writeLineBreak(String.valueOf(br));
/*      */             }
/*      */           }
/* 1391 */           if (ch != 0) {
/* 1392 */             writeIndent();
/*      */           }
/* 1394 */           start = end;
/*      */         }
/*      */       }
/* 1397 */       else if ((ch == 0) || (Constant.LINEBR.has(ch))) {
/* 1398 */         this.stream.write(text, start, end - start);
/* 1399 */         if (ch == 0) {
/* 1400 */           writeLineBreak(null);
/*      */         }
/* 1402 */         start = end;
/*      */       }
/*      */       
/* 1405 */       if (ch != 0) {
/* 1406 */         breaks = Constant.LINEBR.has(ch);
/*      */       }
/* 1408 */       end++;
/*      */     }
/*      */   }
/*      */   
/*      */   void writePlain(String text, boolean split) throws IOException {
/* 1413 */     if (this.rootContext) {
/* 1414 */       this.openEnded = true;
/*      */     }
/* 1416 */     if (text.length() == 0) {
/* 1417 */       return;
/*      */     }
/* 1419 */     if (!this.whitespace) {
/* 1420 */       this.column += 1;
/* 1421 */       this.stream.write(SPACE);
/*      */     }
/* 1423 */     this.whitespace = false;
/* 1424 */     this.indention = false;
/* 1425 */     boolean spaces = false;
/* 1426 */     boolean breaks = false;
/* 1427 */     int start = 0;int end = 0;
/* 1428 */     while (end <= text.length()) {
/* 1429 */       char ch = '\000';
/* 1430 */       if (end < text.length()) {
/* 1431 */         ch = text.charAt(end);
/*      */       }
/* 1433 */       if (spaces) {
/* 1434 */         if (ch != ' ') {
/* 1435 */           if ((start + 1 == end) && (this.column > this.bestWidth) && (split)) {
/* 1436 */             writeIndent();
/* 1437 */             this.whitespace = false;
/* 1438 */             this.indention = false;
/*      */           } else {
/* 1440 */             int len = end - start;
/* 1441 */             this.column += len;
/* 1442 */             this.stream.write(text, start, len);
/*      */           }
/* 1444 */           start = end;
/*      */         }
/* 1446 */       } else if (breaks) {
/* 1447 */         if (Constant.LINEBR.hasNo(ch)) {
/* 1448 */           if (text.charAt(start) == '\n') {
/* 1449 */             writeLineBreak(null);
/*      */           }
/* 1451 */           String data = text.substring(start, end);
/* 1452 */           for (char br : data.toCharArray()) {
/* 1453 */             if (br == '\n') {
/* 1454 */               writeLineBreak(null);
/*      */             } else {
/* 1456 */               writeLineBreak(String.valueOf(br));
/*      */             }
/*      */           }
/* 1459 */           writeIndent();
/* 1460 */           this.whitespace = false;
/* 1461 */           this.indention = false;
/* 1462 */           start = end;
/*      */         }
/*      */       }
/* 1465 */       else if ((ch == 0) || (Constant.LINEBR.has(ch))) {
/* 1466 */         int len = end - start;
/* 1467 */         this.column += len;
/* 1468 */         this.stream.write(text, start, len);
/* 1469 */         start = end;
/*      */       }
/*      */       
/* 1472 */       if (ch != 0) {
/* 1473 */         spaces = ch == ' ';
/* 1474 */         breaks = Constant.LINEBR.has(ch);
/*      */       }
/* 1476 */       end++;
/*      */     }
/*      */   }
/*      */   
/*      */   void writeStreamStart() {}
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\emitter\Emitter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */