/*     */ package org.yaml.snakeyaml;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.composer.Composer;
/*     */ import org.yaml.snakeyaml.constructor.BaseConstructor;
/*     */ import org.yaml.snakeyaml.constructor.Constructor;
/*     */ import org.yaml.snakeyaml.emitter.Emitable;
/*     */ import org.yaml.snakeyaml.emitter.Emitter;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.events.Event;
/*     */ import org.yaml.snakeyaml.introspector.BeanAccess;
/*     */ import org.yaml.snakeyaml.introspector.PropertyUtils;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ import org.yaml.snakeyaml.parser.Parser;
/*     */ import org.yaml.snakeyaml.parser.ParserImpl;
/*     */ import org.yaml.snakeyaml.reader.StreamReader;
/*     */ import org.yaml.snakeyaml.reader.UnicodeReader;
/*     */ import org.yaml.snakeyaml.representer.Representer;
/*     */ import org.yaml.snakeyaml.resolver.Resolver;
/*     */ import org.yaml.snakeyaml.serializer.Serializer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Yaml
/*     */ {
/*     */   protected final Resolver resolver;
/*     */   private String name;
/*     */   protected BaseConstructor constructor;
/*     */   protected Representer representer;
/*     */   protected DumperOptions dumperOptions;
/*     */   
/*     */   public Yaml()
/*     */   {
/*  63 */     this(new Constructor(), new Representer(), new DumperOptions(), new Resolver());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Yaml(DumperOptions dumperOptions)
/*     */   {
/*  73 */     this(new Constructor(), new Representer(), dumperOptions);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Yaml(Representer representer)
/*     */   {
/*  84 */     this(new Constructor(), representer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Yaml(BaseConstructor constructor)
/*     */   {
/*  95 */     this(constructor, new Representer());
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
/*     */   public Yaml(BaseConstructor constructor, Representer representer)
/*     */   {
/* 108 */     this(constructor, representer, new DumperOptions());
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
/*     */   public Yaml(Representer representer, DumperOptions dumperOptions)
/*     */   {
/* 121 */     this(new Constructor(), representer, dumperOptions, new Resolver());
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
/*     */   public Yaml(BaseConstructor constructor, Representer representer, DumperOptions dumperOptions)
/*     */   {
/* 136 */     this(constructor, representer, dumperOptions, new Resolver());
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
/*     */ 
/*     */   public Yaml(BaseConstructor constructor, Representer representer, DumperOptions dumperOptions, Resolver resolver)
/*     */   {
/* 154 */     if (!constructor.isExplicitPropertyUtils()) {
/* 155 */       constructor.setPropertyUtils(representer.getPropertyUtils());
/* 156 */     } else if (!representer.isExplicitPropertyUtils()) {
/* 157 */       representer.setPropertyUtils(constructor.getPropertyUtils());
/*     */     }
/* 159 */     this.constructor = constructor;
/* 160 */     representer.setDefaultFlowStyle(dumperOptions.getDefaultFlowStyle());
/* 161 */     representer.setDefaultScalarStyle(dumperOptions.getDefaultScalarStyle());
/* 162 */     representer.getPropertyUtils().setAllowReadOnlyProperties(dumperOptions.isAllowReadOnlyProperties());
/*     */     
/* 164 */     representer.setTimeZone(dumperOptions.getTimeZone());
/* 165 */     this.representer = representer;
/* 166 */     this.dumperOptions = dumperOptions;
/* 167 */     this.resolver = resolver;
/* 168 */     this.name = ("Yaml:" + System.identityHashCode(this));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String dump(Object data)
/*     */   {
/* 179 */     List<Object> list = new ArrayList(1);
/* 180 */     list.add(data);
/* 181 */     return dumpAll(list.iterator());
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
/*     */   public Node represent(Object data)
/*     */   {
/* 194 */     return this.representer.represent(data);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String dumpAll(Iterator<? extends Object> data)
/*     */   {
/* 205 */     StringWriter buffer = new StringWriter();
/* 206 */     dumpAll(data, buffer, null);
/* 207 */     return buffer.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void dump(Object data, Writer output)
/*     */   {
/* 219 */     List<Object> list = new ArrayList(1);
/* 220 */     list.add(data);
/* 221 */     dumpAll(list.iterator(), output, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void dumpAll(Iterator<? extends Object> data, Writer output)
/*     */   {
/* 233 */     dumpAll(data, output, null);
/*     */   }
/*     */   
/*     */   private void dumpAll(Iterator<? extends Object> data, Writer output, Tag rootTag) {
/* 237 */     Serializer serializer = new Serializer(new Emitter(output, this.dumperOptions), this.resolver, this.dumperOptions, rootTag);
/*     */     try
/*     */     {
/* 240 */       serializer.open();
/* 241 */       while (data.hasNext()) {
/* 242 */         Node node = this.representer.represent(data.next());
/* 243 */         serializer.serialize(node);
/*     */       }
/* 245 */       serializer.close();
/*     */     } catch (IOException e) {
/* 247 */       throw new YAMLException(e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String dumpAs(Object data, Tag rootTag, DumperOptions.FlowStyle flowStyle)
/*     */   {
/* 292 */     DumperOptions.FlowStyle oldStyle = this.representer.getDefaultFlowStyle();
/* 293 */     if (flowStyle != null) {
/* 294 */       this.representer.setDefaultFlowStyle(flowStyle);
/*     */     }
/* 296 */     List<Object> list = new ArrayList(1);
/* 297 */     list.add(data);
/* 298 */     StringWriter buffer = new StringWriter();
/* 299 */     dumpAll(list.iterator(), buffer, rootTag);
/* 300 */     this.representer.setDefaultFlowStyle(oldStyle);
/* 301 */     return buffer.toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String dumpAsMap(Object data)
/*     */   {
/* 324 */     return dumpAs(data, Tag.MAP, DumperOptions.FlowStyle.BLOCK);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Event> serialize(Node data)
/*     */   {
/* 336 */     SilentEmitter emitter = new SilentEmitter(null);
/* 337 */     Serializer serializer = new Serializer(emitter, this.resolver, this.dumperOptions, null);
/*     */     try {
/* 339 */       serializer.open();
/* 340 */       serializer.serialize(data);
/* 341 */       serializer.close();
/*     */     } catch (IOException e) {
/* 343 */       throw new YAMLException(e);
/*     */     }
/* 345 */     return emitter.getEvents();
/*     */   }
/*     */   
/*     */   private static class SilentEmitter implements Emitable {
/* 349 */     private List<Event> events = new ArrayList(100);
/*     */     
/*     */     public List<Event> getEvents() {
/* 352 */       return this.events;
/*     */     }
/*     */     
/*     */     public void emit(Event event) throws IOException {
/* 356 */       this.events.add(event);
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
/*     */   public Object load(String yaml)
/*     */   {
/* 369 */     return loadFromReader(new StreamReader(yaml), Object.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object load(InputStream io)
/*     */   {
/* 381 */     return loadFromReader(new StreamReader(new UnicodeReader(io)), Object.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object load(Reader io)
/*     */   {
/* 393 */     return loadFromReader(new StreamReader(io), Object.class);
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
/*     */   public <T> T loadAs(Reader io, Class<T> type)
/*     */   {
/* 410 */     return (T)loadFromReader(new StreamReader(io), type);
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
/*     */   public <T> T loadAs(String yaml, Class<T> type)
/*     */   {
/* 427 */     return (T)loadFromReader(new StreamReader(yaml), type);
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
/*     */   public <T> T loadAs(InputStream input, Class<T> type)
/*     */   {
/* 444 */     return (T)loadFromReader(new StreamReader(new UnicodeReader(input)), type);
/*     */   }
/*     */   
/*     */   private Object loadFromReader(StreamReader sreader, Class<?> type) {
/* 448 */     Composer composer = new Composer(new ParserImpl(sreader), this.resolver);
/* 449 */     this.constructor.setComposer(composer);
/* 450 */     return this.constructor.getSingleData(type);
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
/*     */   public Iterable<Object> loadAll(Reader yaml)
/*     */   {
/* 463 */     Composer composer = new Composer(new ParserImpl(new StreamReader(yaml)), this.resolver);
/* 464 */     this.constructor.setComposer(composer);
/* 465 */     Iterator<Object> result = new Iterator() {
/*     */       public boolean hasNext() {
/* 467 */         return Yaml.this.constructor.checkData();
/*     */       }
/*     */       
/*     */       public Object next() {
/* 471 */         return Yaml.this.constructor.getData();
/*     */       }
/*     */       
/*     */       public void remove() {
/* 475 */         throw new UnsupportedOperationException();
/*     */       }
/* 477 */     };
/* 478 */     return new YamlIterable(result);
/*     */   }
/*     */   
/*     */   private static class YamlIterable implements Iterable<Object> {
/*     */     private Iterator<Object> iterator;
/*     */     
/*     */     public YamlIterable(Iterator<Object> iterator) {
/* 485 */       this.iterator = iterator;
/*     */     }
/*     */     
/*     */     public Iterator<Object> iterator() {
/* 489 */       return this.iterator;
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
/*     */ 
/*     */ 
/*     */   public Iterable<Object> loadAll(String yaml)
/*     */   {
/* 504 */     return loadAll(new StringReader(yaml));
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
/*     */   public Iterable<Object> loadAll(InputStream yaml)
/*     */   {
/* 517 */     return loadAll(new UnicodeReader(yaml));
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
/*     */   public Node compose(Reader yaml)
/*     */   {
/* 531 */     Composer composer = new Composer(new ParserImpl(new StreamReader(yaml)), this.resolver);
/* 532 */     this.constructor.setComposer(composer);
/* 533 */     return composer.getSingleNode();
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
/*     */   public Iterable<Node> composeAll(Reader yaml)
/*     */   {
/* 546 */     final Composer composer = new Composer(new ParserImpl(new StreamReader(yaml)), this.resolver);
/* 547 */     this.constructor.setComposer(composer);
/* 548 */     Iterator<Node> result = new Iterator() {
/*     */       public boolean hasNext() {
/* 550 */         return composer.checkNode();
/*     */       }
/*     */       
/*     */       public Node next() {
/* 554 */         return composer.getNode();
/*     */       }
/*     */       
/*     */       public void remove() {
/* 558 */         throw new UnsupportedOperationException();
/*     */       }
/* 560 */     };
/* 561 */     return new NodeIterable(result);
/*     */   }
/*     */   
/*     */   private static class NodeIterable implements Iterable<Node> {
/*     */     private Iterator<Node> iterator;
/*     */     
/*     */     public NodeIterable(Iterator<Node> iterator) {
/* 568 */       this.iterator = iterator;
/*     */     }
/*     */     
/*     */     public Iterator<Node> iterator() {
/* 572 */       return this.iterator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addImplicitResolver(Tag tag, Pattern regexp, String first)
/*     */   {
/* 589 */     this.resolver.addImplicitResolver(tag, regexp, first);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 594 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 605 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setName(String name)
/*     */   {
/* 615 */     this.name = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterable<Event> parse(Reader yaml)
/*     */   {
/* 627 */     final Parser parser = new ParserImpl(new StreamReader(yaml));
/* 628 */     Iterator<Event> result = new Iterator() {
/*     */       public boolean hasNext() {
/* 630 */         return parser.peekEvent() != null;
/*     */       }
/*     */       
/*     */       public Event next() {
/* 634 */         return parser.getEvent();
/*     */       }
/*     */       
/*     */       public void remove() {
/* 638 */         throw new UnsupportedOperationException();
/*     */       }
/* 640 */     };
/* 641 */     return new EventIterable(result);
/*     */   }
/*     */   
/*     */   private static class EventIterable implements Iterable<Event> {
/*     */     private Iterator<Event> iterator;
/*     */     
/*     */     public EventIterable(Iterator<Event> iterator) {
/* 648 */       this.iterator = iterator;
/*     */     }
/*     */     
/*     */     public Iterator<Event> iterator() {
/* 652 */       return this.iterator;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setBeanAccess(BeanAccess beanAccess) {
/* 657 */     this.constructor.getPropertyUtils().setBeanAccess(beanAccess);
/* 658 */     this.representer.getPropertyUtils().setBeanAccess(beanAccess);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\Yaml.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */