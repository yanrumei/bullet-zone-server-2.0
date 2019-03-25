/*     */ package org.springframework.boot.bind;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.BeanWrapperImpl;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.InvalidPropertyException;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.NotWritablePropertyException;
/*     */ import org.springframework.beans.PropertyValue;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.core.env.PropertySource;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.validation.AbstractPropertyBindingResult;
/*     */ import org.springframework.validation.BeanPropertyBindingResult;
/*     */ import org.springframework.validation.DataBinder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RelaxedDataBinder
/*     */   extends DataBinder
/*     */ {
/*  61 */   private static final Object BLANK = new Object();
/*     */   
/*     */   private String namePrefix;
/*     */   
/*     */   private boolean ignoreNestedProperties;
/*     */   
/*  67 */   private MultiValueMap<String, String> nameAliases = new LinkedMultiValueMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RelaxedDataBinder(Object target)
/*     */   {
/*  74 */     super(wrapTarget(target));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RelaxedDataBinder(Object target, String namePrefix)
/*     */   {
/*  83 */     super(wrapTarget(target), 
/*  84 */       StringUtils.hasLength(namePrefix) ? namePrefix : "target");
/*  85 */     this.namePrefix = cleanNamePrefix(namePrefix);
/*     */   }
/*     */   
/*     */   private String cleanNamePrefix(String namePrefix) {
/*  89 */     if (!StringUtils.hasLength(namePrefix)) {
/*  90 */       return null;
/*     */     }
/*  92 */     return namePrefix + ".";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIgnoreNestedProperties(boolean ignoreNestedProperties)
/*     */   {
/* 102 */     this.ignoreNestedProperties = ignoreNestedProperties;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setNameAliases(Map<String, List<String>> aliases)
/*     */   {
/* 110 */     this.nameAliases = new LinkedMultiValueMap(aliases);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RelaxedDataBinder withAlias(String name, String... alias)
/*     */   {
/* 120 */     for (String value : alias) {
/* 121 */       this.nameAliases.add(name, value);
/*     */     }
/* 123 */     return this;
/*     */   }
/*     */   
/*     */   protected void doBind(MutablePropertyValues propertyValues)
/*     */   {
/* 128 */     super.doBind(modifyProperties(propertyValues, getTarget()));
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
/*     */   private MutablePropertyValues modifyProperties(MutablePropertyValues propertyValues, Object target)
/*     */   {
/* 142 */     propertyValues = getPropertyValuesForNamePrefix(propertyValues);
/* 143 */     if ((target instanceof MapHolder)) {
/* 144 */       propertyValues = addMapPrefix(propertyValues);
/*     */     }
/* 146 */     BeanWrapper wrapper = new BeanWrapperImpl(target);
/* 147 */     wrapper.setConversionService(new RelaxedConversionService(
/* 148 */       getConversionService()));
/* 149 */     wrapper.setAutoGrowNestedPaths(true);
/* 150 */     List<PropertyValue> sortedValues = new ArrayList();
/* 151 */     Set<String> modifiedNames = new HashSet();
/* 152 */     List<String> sortedNames = getSortedPropertyNames(propertyValues);
/* 153 */     for (String name : sortedNames) {
/* 154 */       PropertyValue propertyValue = propertyValues.getPropertyValue(name);
/* 155 */       PropertyValue modifiedProperty = modifyProperty(wrapper, propertyValue);
/* 156 */       if (modifiedNames.add(modifiedProperty.getName())) {
/* 157 */         sortedValues.add(modifiedProperty);
/*     */       }
/*     */     }
/* 160 */     return new MutablePropertyValues(sortedValues);
/*     */   }
/*     */   
/*     */   private List<String> getSortedPropertyNames(MutablePropertyValues propertyValues) {
/* 164 */     List<String> names = new LinkedList();
/* 165 */     for (PropertyValue propertyValue : propertyValues.getPropertyValueList()) {
/* 166 */       names.add(propertyValue.getName());
/*     */     }
/* 168 */     sortPropertyNames(names);
/* 169 */     return names;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void sortPropertyNames(List<String> names)
/*     */   {
/* 181 */     for (Iterator localIterator1 = new ArrayList(names).iterator(); localIterator1.hasNext();) { name = (String)localIterator1.next();
/* 182 */       propertyIndex = names.indexOf(name);
/* 183 */       BeanPath path = new BeanPath(name);
/* 184 */       for (String prefix : path.prefixes()) {
/* 185 */         int prefixIndex = names.indexOf(prefix);
/* 186 */         if (prefixIndex >= propertyIndex)
/*     */         {
/* 188 */           names.remove(name);
/* 189 */           names.add(prefixIndex, name);
/*     */         }
/*     */       }
/*     */     }
/*     */     String name;
/*     */     int propertyIndex; }
/*     */   
/* 196 */   private MutablePropertyValues addMapPrefix(MutablePropertyValues propertyValues) { MutablePropertyValues rtn = new MutablePropertyValues();
/* 197 */     for (PropertyValue pv : propertyValues.getPropertyValues()) {
/* 198 */       rtn.add("map." + pv.getName(), pv.getValue());
/*     */     }
/* 200 */     return rtn;
/*     */   }
/*     */   
/*     */   private MutablePropertyValues getPropertyValuesForNamePrefix(MutablePropertyValues propertyValues)
/*     */   {
/* 205 */     if ((!StringUtils.hasText(this.namePrefix)) && (!this.ignoreNestedProperties)) {
/* 206 */       return propertyValues;
/*     */     }
/* 208 */     MutablePropertyValues rtn = new MutablePropertyValues();
/* 209 */     PropertyValue value; String name; for (value : propertyValues.getPropertyValues()) {
/* 210 */       name = value.getName();
/* 211 */       for (String prefix : new RelaxedNames(stripLastDot(this.namePrefix))) {
/* 212 */         for (String separator : new String[] { ".", "_" }) {
/* 213 */           String candidate = StringUtils.hasLength(prefix) ? prefix + separator : prefix;
/*     */           
/* 215 */           if (name.startsWith(candidate)) {
/* 216 */             name = name.substring(candidate.length());
/* 217 */             if ((!this.ignoreNestedProperties) || (!name.contains(".")))
/*     */             {
/* 219 */               PropertyOrigin propertyOrigin = OriginCapablePropertyValue.getOrigin(value);
/* 220 */               rtn.addPropertyValue(new OriginCapablePropertyValue(name, value
/* 221 */                 .getValue(), propertyOrigin));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 227 */     return rtn;
/*     */   }
/*     */   
/*     */   private String stripLastDot(String string) {
/* 231 */     if ((StringUtils.hasLength(string)) && (string.endsWith("."))) {
/* 232 */       string = string.substring(0, string.length() - 1);
/*     */     }
/* 234 */     return string;
/*     */   }
/*     */   
/*     */   private PropertyValue modifyProperty(BeanWrapper target, PropertyValue propertyValue)
/*     */   {
/* 239 */     String name = propertyValue.getName();
/* 240 */     String normalizedName = normalizePath(target, name);
/* 241 */     if (!normalizedName.equals(name)) {
/* 242 */       return new PropertyValue(normalizedName, propertyValue.getValue());
/*     */     }
/* 244 */     return propertyValue;
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
/*     */   protected String normalizePath(BeanWrapper wrapper, String path)
/*     */   {
/* 259 */     return initializePath(wrapper, new BeanPath(path), 0);
/*     */   }
/*     */   
/*     */   protected AbstractPropertyBindingResult createBeanPropertyBindingResult()
/*     */   {
/* 264 */     return new RelaxedBeanPropertyBindingResult(getTarget(), getObjectName(), 
/* 265 */       isAutoGrowNestedPaths(), getAutoGrowCollectionLimit(), 
/* 266 */       getConversionService());
/*     */   }
/*     */   
/*     */   private String initializePath(BeanWrapper wrapper, BeanPath path, int index) {
/* 270 */     String prefix = path.prefix(index);
/* 271 */     String key = path.name(index);
/* 272 */     if (path.isProperty(index)) {
/* 273 */       key = getActualPropertyName(wrapper, prefix, key);
/* 274 */       path.rename(index, key);
/*     */     }
/* 276 */     if (path.name(++index) == null) {
/* 277 */       return path.toString();
/*     */     }
/* 279 */     String name = path.prefix(index);
/* 280 */     TypeDescriptor descriptor = wrapper.getPropertyTypeDescriptor(name);
/* 281 */     if ((descriptor == null) || (descriptor.isMap())) {
/* 282 */       if ((isMapValueStringType(descriptor)) || 
/* 283 */         (isBlanked(wrapper, name, path.name(index)))) {
/* 284 */         path.collapseKeys(index);
/*     */       }
/* 286 */       path.mapIndex(index);
/* 287 */       extendMapIfNecessary(wrapper, path, index);
/*     */     }
/* 289 */     else if (descriptor.isCollection()) {
/* 290 */       extendCollectionIfNecessary(wrapper, path, index);
/*     */     }
/* 292 */     else if (descriptor.getType().equals(Object.class)) {
/* 293 */       if (isBlanked(wrapper, name, path.name(index))) {
/* 294 */         path.collapseKeys(index);
/*     */       }
/* 296 */       path.mapIndex(index);
/* 297 */       if (path.isLastNode(index)) {
/* 298 */         wrapper.setPropertyValue(path.toString(), BLANK);
/*     */       }
/*     */       else {
/* 301 */         String next = path.prefix(index + 1);
/* 302 */         if (wrapper.getPropertyValue(next) == null) {
/* 303 */           wrapper.setPropertyValue(next, new LinkedHashMap());
/*     */         }
/*     */       }
/*     */     }
/* 307 */     return initializePath(wrapper, path, index);
/*     */   }
/*     */   
/*     */   private boolean isMapValueStringType(TypeDescriptor descriptor) {
/* 311 */     if ((descriptor == null) || (descriptor.getMapValueTypeDescriptor() == null)) {
/* 312 */       return false;
/*     */     }
/* 314 */     if (Properties.class.isAssignableFrom(descriptor.getObjectType()))
/*     */     {
/*     */ 
/* 317 */       return true;
/*     */     }
/* 319 */     Class<?> valueType = descriptor.getMapValueTypeDescriptor().getObjectType();
/* 320 */     return (valueType != null) && (CharSequence.class.isAssignableFrom(valueType));
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean isBlanked(BeanWrapper wrapper, String propertyName, String key)
/*     */   {
/* 326 */     Object value = wrapper.isReadableProperty(propertyName) ? wrapper.getPropertyValue(propertyName) : null;
/* 327 */     if (((value instanceof Map)) && 
/* 328 */       (((Map)value).get(key) == BLANK)) {
/* 329 */       return true;
/*     */     }
/*     */     
/* 332 */     return false;
/*     */   }
/*     */   
/*     */   private void extendCollectionIfNecessary(BeanWrapper wrapper, BeanPath path, int index)
/*     */   {
/* 337 */     String name = path.prefix(index);
/*     */     
/* 339 */     TypeDescriptor elementDescriptor = wrapper.getPropertyTypeDescriptor(name).getElementTypeDescriptor();
/* 340 */     if ((!elementDescriptor.isMap()) && (!elementDescriptor.isCollection()) && 
/* 341 */       (!elementDescriptor.getType().equals(Object.class))) {
/* 342 */       return;
/*     */     }
/* 344 */     Object extend = new LinkedHashMap();
/* 345 */     if ((!elementDescriptor.isMap()) && (path.isArrayIndex(index))) {
/* 346 */       extend = new ArrayList();
/*     */     }
/* 348 */     wrapper.setPropertyValue(path.prefix(index + 1), extend);
/*     */   }
/*     */   
/*     */   private void extendMapIfNecessary(BeanWrapper wrapper, BeanPath path, int index) {
/* 352 */     String name = path.prefix(index);
/* 353 */     TypeDescriptor parent = wrapper.getPropertyTypeDescriptor(name);
/* 354 */     if (parent == null) {
/* 355 */       return;
/*     */     }
/* 357 */     TypeDescriptor descriptor = parent.getMapValueTypeDescriptor();
/* 358 */     if (descriptor == null) {
/* 359 */       descriptor = TypeDescriptor.valueOf(Object.class);
/*     */     }
/* 361 */     if ((!descriptor.isMap()) && (!descriptor.isCollection()) && 
/* 362 */       (!descriptor.getType().equals(Object.class))) {
/* 363 */       return;
/*     */     }
/* 365 */     String extensionName = path.prefix(index + 1);
/* 366 */     if (wrapper.isReadableProperty(extensionName)) {
/* 367 */       Object currentValue = wrapper.getPropertyValue(extensionName);
/* 368 */       if (((descriptor.isCollection()) && ((currentValue instanceof Collection))) || (
/* 369 */         (!descriptor.isCollection()) && ((currentValue instanceof Map)))) {
/* 370 */         return;
/*     */       }
/*     */     }
/* 373 */     Object extend = new LinkedHashMap();
/* 374 */     if (descriptor.isCollection()) {
/* 375 */       extend = new ArrayList();
/*     */     }
/* 377 */     if ((descriptor.getType().equals(Object.class)) && (path.isLastNode(index))) {
/* 378 */       extend = BLANK;
/*     */     }
/* 380 */     wrapper.setPropertyValue(extensionName, extend);
/*     */   }
/*     */   
/*     */   private String getActualPropertyName(BeanWrapper target, String prefix, String name) {
/* 384 */     String propertyName = resolvePropertyName(target, prefix, name);
/* 385 */     if (propertyName == null) {
/* 386 */       propertyName = resolveNestedPropertyName(target, prefix, name);
/*     */     }
/* 388 */     return propertyName == null ? name : propertyName;
/*     */   }
/*     */   
/*     */   private String resolveNestedPropertyName(BeanWrapper target, String prefix, String name)
/*     */   {
/* 393 */     StringBuilder candidate = new StringBuilder();
/* 394 */     for (String field : name.split("[_\\-\\.]")) {
/* 395 */       candidate.append(candidate.length() > 0 ? "." : "");
/* 396 */       candidate.append(field);
/* 397 */       String nested = resolvePropertyName(target, prefix, candidate.toString());
/* 398 */       if (nested != null) {
/* 399 */         Class<?> type = target.getPropertyType(nested);
/* 400 */         if ((type != null) && (Map.class.isAssignableFrom(type)))
/*     */         {
/* 402 */           return nested + "[" + name.substring(candidate.length() + 1) + "]";
/*     */         }
/* 404 */         String propertyName = resolvePropertyName(target, 
/* 405 */           joinString(prefix, nested), name
/* 406 */           .substring(candidate.length() + 1));
/* 407 */         if (propertyName != null) {
/* 408 */           return joinString(nested, propertyName);
/*     */         }
/*     */       }
/*     */     }
/* 412 */     return null;
/*     */   }
/*     */   
/*     */   private String resolvePropertyName(BeanWrapper target, String prefix, String name) {
/* 416 */     Iterable<String> names = getNameAndAliases(name);
/* 417 */     for (String nameOrAlias : names) {
/* 418 */       for (String candidate : new RelaxedNames(nameOrAlias)) {
/*     */         try {
/* 420 */           if (target.getPropertyType(joinString(prefix, candidate)) != null) {
/* 421 */             return candidate;
/*     */           }
/*     */         }
/*     */         catch (InvalidPropertyException localInvalidPropertyException) {}
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 429 */     return null;
/*     */   }
/*     */   
/*     */   private String joinString(String prefix, String name) {
/* 433 */     return StringUtils.hasLength(prefix) ? prefix + "." + name : name;
/*     */   }
/*     */   
/*     */   private Iterable<String> getNameAndAliases(String name) {
/* 437 */     List<String> aliases = (List)this.nameAliases.get(name);
/* 438 */     if (aliases == null) {
/* 439 */       return Collections.singleton(name);
/*     */     }
/* 441 */     List<String> nameAndAliases = new ArrayList(aliases.size() + 1);
/* 442 */     nameAndAliases.add(name);
/* 443 */     nameAndAliases.addAll(aliases);
/* 444 */     return nameAndAliases;
/*     */   }
/*     */   
/*     */   private static Object wrapTarget(Object target) {
/* 448 */     if ((target instanceof Map))
/*     */     {
/* 450 */       Map<String, Object> map = (Map)target;
/* 451 */       target = new MapHolder(map);
/*     */     }
/* 453 */     return target;
/*     */   }
/*     */   
/*     */ 
/*     */   static class MapHolder
/*     */   {
/*     */     private Map<String, Object> map;
/*     */     
/*     */ 
/*     */     MapHolder(Map<String, Object> map)
/*     */     {
/* 464 */       this.map = map;
/*     */     }
/*     */     
/*     */     public void setMap(Map<String, Object> map) {
/* 468 */       this.map = map;
/*     */     }
/*     */     
/*     */     public Map<String, Object> getMap() {
/* 472 */       return this.map;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class BeanPath
/*     */   {
/*     */     private List<PathNode> nodes;
/*     */     
/*     */ 
/*     */     BeanPath(String path)
/*     */     {
/* 485 */       this.nodes = splitPath(path);
/*     */     }
/*     */     
/*     */     public List<String> prefixes() {
/* 489 */       List<String> prefixes = new ArrayList();
/* 490 */       for (int index = 1; index < this.nodes.size(); index++) {
/* 491 */         prefixes.add(prefix(index));
/*     */       }
/* 493 */       return prefixes;
/*     */     }
/*     */     
/*     */     public boolean isLastNode(int index) {
/* 497 */       return index >= this.nodes.size() - 1;
/*     */     }
/*     */     
/*     */     private List<PathNode> splitPath(String path) {
/* 501 */       List<PathNode> nodes = new ArrayList();
/* 502 */       String current = extractIndexedPaths(path, nodes);
/* 503 */       for (String name : StringUtils.delimitedListToStringArray(current, ".")) {
/* 504 */         if (StringUtils.hasText(name)) {
/* 505 */           nodes.add(new PropertyNode(name));
/*     */         }
/*     */       }
/* 508 */       return nodes;
/*     */     }
/*     */     
/*     */     private String extractIndexedPaths(String path, List<PathNode> nodes) {
/* 512 */       int startRef = path.indexOf("[");
/* 513 */       String current = path;
/* 514 */       while (startRef >= 0) {
/* 515 */         if (startRef > 0) {
/* 516 */           nodes.addAll(splitPath(current.substring(0, startRef)));
/*     */         }
/* 518 */         int endRef = current.indexOf("]", startRef);
/* 519 */         if (endRef > 0) {
/* 520 */           String sub = current.substring(startRef + 1, endRef);
/* 521 */           if (sub.matches("[0-9]+")) {
/* 522 */             nodes.add(new ArrayIndexNode(sub));
/*     */           }
/*     */           else {
/* 525 */             nodes.add(new MapIndexNode(sub));
/*     */           }
/*     */         }
/* 528 */         current = current.substring(endRef + 1);
/* 529 */         startRef = current.indexOf("[");
/*     */       }
/* 531 */       return current;
/*     */     }
/*     */     
/*     */     public void collapseKeys(int index) {
/* 535 */       List<PathNode> revised = new ArrayList();
/* 536 */       for (int i = 0; i < index; i++) {
/* 537 */         revised.add(this.nodes.get(i));
/*     */       }
/* 539 */       StringBuilder builder = new StringBuilder();
/* 540 */       for (int i = index; i < this.nodes.size(); i++) {
/* 541 */         if (i > index) {
/* 542 */           builder.append(".");
/*     */         }
/* 544 */         builder.append(((PathNode)this.nodes.get(i)).name);
/*     */       }
/* 546 */       revised.add(new PropertyNode(builder.toString()));
/* 547 */       this.nodes = revised;
/*     */     }
/*     */     
/*     */     public void mapIndex(int index) {
/* 551 */       PathNode node = (PathNode)this.nodes.get(index);
/* 552 */       if ((node instanceof PropertyNode)) {
/* 553 */         node = ((PropertyNode)node).mapIndex();
/*     */       }
/* 555 */       this.nodes.set(index, node);
/*     */     }
/*     */     
/*     */     public String prefix(int index) {
/* 559 */       return range(0, index);
/*     */     }
/*     */     
/*     */     public void rename(int index, String name) {
/* 563 */       ((PathNode)this.nodes.get(index)).name = name;
/*     */     }
/*     */     
/*     */     public String name(int index) {
/* 567 */       if (index < this.nodes.size()) {
/* 568 */         return ((PathNode)this.nodes.get(index)).name;
/*     */       }
/* 570 */       return null;
/*     */     }
/*     */     
/*     */     private String range(int start, int end) {
/* 574 */       StringBuilder builder = new StringBuilder();
/* 575 */       for (int i = start; i < end; i++) {
/* 576 */         PathNode node = (PathNode)this.nodes.get(i);
/* 577 */         builder.append(node);
/*     */       }
/* 579 */       if (builder.toString().startsWith(".")) {
/* 580 */         builder.replace(0, 1, "");
/*     */       }
/* 582 */       return builder.toString();
/*     */     }
/*     */     
/*     */     public boolean isArrayIndex(int index) {
/* 586 */       return this.nodes.get(index) instanceof ArrayIndexNode;
/*     */     }
/*     */     
/*     */     public boolean isProperty(int index) {
/* 590 */       return this.nodes.get(index) instanceof PropertyNode;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 595 */       return prefix(this.nodes.size());
/*     */     }
/*     */     
/*     */     private static class PathNode
/*     */     {
/*     */       protected String name;
/*     */       
/*     */       PathNode(String name) {
/* 603 */         this.name = name;
/*     */       }
/*     */     }
/*     */     
/*     */     private static class ArrayIndexNode extends RelaxedDataBinder.BeanPath.PathNode
/*     */     {
/*     */       ArrayIndexNode(String name)
/*     */       {
/* 611 */         super();
/*     */       }
/*     */       
/*     */       public String toString()
/*     */       {
/* 616 */         return "[" + this.name + "]";
/*     */       }
/*     */     }
/*     */     
/*     */     private static class MapIndexNode extends RelaxedDataBinder.BeanPath.PathNode
/*     */     {
/*     */       MapIndexNode(String name)
/*     */       {
/* 624 */         super();
/*     */       }
/*     */       
/*     */       public String toString()
/*     */       {
/* 629 */         return "[" + this.name + "]";
/*     */       }
/*     */     }
/*     */     
/*     */     private static class PropertyNode extends RelaxedDataBinder.BeanPath.PathNode
/*     */     {
/*     */       PropertyNode(String name)
/*     */       {
/* 637 */         super();
/*     */       }
/*     */       
/*     */       public RelaxedDataBinder.BeanPath.MapIndexNode mapIndex() {
/* 641 */         return new RelaxedDataBinder.BeanPath.MapIndexNode(this.name);
/*     */       }
/*     */       
/*     */       public String toString()
/*     */       {
/* 646 */         return "." + this.name;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class RelaxedBeanPropertyBindingResult
/*     */     extends BeanPropertyBindingResult
/*     */   {
/*     */     private RelaxedConversionService conversionService;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     RelaxedBeanPropertyBindingResult(Object target, String objectName, boolean autoGrowNestedPaths, int autoGrowCollectionLimit, ConversionService conversionService)
/*     */     {
/* 664 */       super(objectName, autoGrowNestedPaths, autoGrowCollectionLimit);
/* 665 */       this.conversionService = new RelaxedConversionService(conversionService);
/*     */     }
/*     */     
/*     */     protected BeanWrapper createBeanWrapper()
/*     */     {
/* 670 */       BeanWrapper beanWrapper = new RelaxedDataBinder.RelaxedBeanWrapper(getTarget());
/* 671 */       beanWrapper.setConversionService(this.conversionService);
/* 672 */       beanWrapper.registerCustomEditor(InetAddress.class, new InetAddressEditor());
/* 673 */       return beanWrapper;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class RelaxedBeanWrapper
/*     */     extends BeanWrapperImpl
/*     */   {
/*     */     private static final Set<String> BENIGN_PROPERTY_SOURCE_NAMES;
/*     */     
/*     */ 
/*     */     static
/*     */     {
/* 686 */       Set<String> names = new HashSet();
/* 687 */       names.add("systemEnvironment");
/* 688 */       names.add("systemProperties");
/* 689 */       BENIGN_PROPERTY_SOURCE_NAMES = Collections.unmodifiableSet(names);
/*     */     }
/*     */     
/*     */     RelaxedBeanWrapper(Object target) {
/* 693 */       super();
/*     */     }
/*     */     
/*     */     public void setPropertyValue(PropertyValue pv) throws BeansException
/*     */     {
/*     */       try {
/* 699 */         super.setPropertyValue(pv);
/*     */       }
/*     */       catch (NotWritablePropertyException ex) {
/* 702 */         PropertyOrigin origin = OriginCapablePropertyValue.getOrigin(pv);
/* 703 */         if (isBenign(origin)) {
/* 704 */           RelaxedDataBinder.logger.debug("Ignoring benign property binding failure", ex);
/* 705 */           return;
/*     */         }
/* 707 */         if (origin == null) {
/* 708 */           throw ex;
/*     */         }
/* 710 */         throw new RelaxedBindingNotWritablePropertyException(ex, origin);
/*     */       }
/*     */     }
/*     */     
/*     */     private boolean isBenign(PropertyOrigin origin) {
/* 715 */       String name = origin == null ? null : origin.getSource().getName();
/* 716 */       return BENIGN_PROPERTY_SOURCE_NAMES.contains(name);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\bind\RelaxedDataBinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */