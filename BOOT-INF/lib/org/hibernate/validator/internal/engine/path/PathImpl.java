/*     */ package org.hibernate.validator.internal.engine.path;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.validation.ElementKind;
/*     */ import javax.validation.Path;
/*     */ import javax.validation.Path.Node;
/*     */ import org.hibernate.validator.internal.metadata.aggregated.ExecutableMetaData;
/*     */ import org.hibernate.validator.internal.util.Contracts;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.internal.util.logging.Messages;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PathImpl
/*     */   implements Path, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 7564511574909882392L;
/*  36 */   private static final Log log = ;
/*     */   
/*     */ 
/*     */   private static final String PROPERTY_PATH_SEPARATOR = ".";
/*     */   
/*     */ 
/*     */   private static final String LEADING_PROPERTY_GROUP = "[^\\[\\.]+";
/*     */   
/*     */ 
/*     */   private static final String OPTIONAL_INDEX_GROUP = "\\[(\\w*)\\]";
/*     */   
/*     */   private static final String REMAINING_PROPERTY_STRING = "\\.(.*)";
/*     */   
/*  49 */   private static final Pattern PATH_PATTERN = Pattern.compile("([^\\[\\.]+)(\\[(\\w*)\\])?(\\.(.*))*");
/*     */   
/*     */ 
/*     */   private static final int PROPERTY_NAME_GROUP = 1;
/*     */   
/*     */ 
/*     */   private static final int INDEXED_GROUP = 2;
/*     */   
/*     */ 
/*     */   private static final int INDEX_GROUP = 3;
/*     */   
/*     */ 
/*     */   private static final int REMAINING_STRING_GROUP = 5;
/*     */   
/*     */   private final List<Path.Node> nodeList;
/*     */   
/*     */   private NodeImpl currentLeafNode;
/*     */   
/*     */   private int hashCode;
/*     */   
/*     */ 
/*     */   public static PathImpl createPathFromString(String propertyPath)
/*     */   {
/*  72 */     Contracts.assertNotNull(propertyPath, Messages.MESSAGES.propertyPathCannotBeNull());
/*     */     
/*  74 */     if (propertyPath.length() == 0) {
/*  75 */       return createRootPath();
/*     */     }
/*     */     
/*  78 */     return parseProperty(propertyPath);
/*     */   }
/*     */   
/*     */   public static PathImpl createPathForExecutable(ExecutableMetaData executable) {
/*  82 */     Contracts.assertNotNull(executable, "A method is required to create a method return value path.");
/*     */     
/*  84 */     PathImpl path = createRootPath();
/*     */     
/*  86 */     if (executable.getKind() == ElementKind.CONSTRUCTOR) {
/*  87 */       path.addConstructorNode(executable.getName(), executable.getParameterTypes());
/*     */     }
/*     */     else {
/*  90 */       path.addMethodNode(executable.getName(), executable.getParameterTypes());
/*     */     }
/*     */     
/*  93 */     return path;
/*     */   }
/*     */   
/*     */   public static PathImpl createRootPath() {
/*  97 */     PathImpl path = new PathImpl();
/*  98 */     path.addBeanNode();
/*  99 */     return path;
/*     */   }
/*     */   
/*     */   public static PathImpl createCopy(PathImpl path) {
/* 103 */     return new PathImpl(path);
/*     */   }
/*     */   
/*     */   public boolean isRootPath() {
/* 107 */     return (this.nodeList.size() == 1) && (((Path.Node)this.nodeList.get(0)).getName() == null);
/*     */   }
/*     */   
/*     */   public PathImpl getPathWithoutLeafNode() {
/* 111 */     return new PathImpl(this.nodeList.subList(0, this.nodeList.size() - 1));
/*     */   }
/*     */   
/*     */   public NodeImpl addPropertyNode(String nodeName) {
/* 115 */     NodeImpl parent = this.nodeList.isEmpty() ? null : (NodeImpl)this.nodeList.get(this.nodeList.size() - 1);
/* 116 */     this.currentLeafNode = NodeImpl.createPropertyNode(nodeName, parent);
/* 117 */     this.nodeList.add(this.currentLeafNode);
/* 118 */     this.hashCode = -1;
/* 119 */     return this.currentLeafNode;
/*     */   }
/*     */   
/*     */   public NodeImpl addCollectionElementNode() {
/* 123 */     NodeImpl parent = this.nodeList.isEmpty() ? null : (NodeImpl)this.nodeList.get(this.nodeList.size() - 1);
/* 124 */     this.currentLeafNode = NodeImpl.createCollectionElementNode(parent);
/* 125 */     this.nodeList.add(this.currentLeafNode);
/* 126 */     this.hashCode = -1;
/* 127 */     return this.currentLeafNode;
/*     */   }
/*     */   
/*     */   public NodeImpl addParameterNode(String nodeName, int index) {
/* 131 */     NodeImpl parent = this.nodeList.isEmpty() ? null : (NodeImpl)this.nodeList.get(this.nodeList.size() - 1);
/* 132 */     this.currentLeafNode = NodeImpl.createParameterNode(nodeName, parent, index);
/* 133 */     this.nodeList.add(this.currentLeafNode);
/* 134 */     this.hashCode = -1;
/* 135 */     return this.currentLeafNode;
/*     */   }
/*     */   
/*     */   public NodeImpl addCrossParameterNode() {
/* 139 */     NodeImpl parent = this.nodeList.isEmpty() ? null : (NodeImpl)this.nodeList.get(this.nodeList.size() - 1);
/* 140 */     this.currentLeafNode = NodeImpl.createCrossParameterNode(parent);
/* 141 */     this.nodeList.add(this.currentLeafNode);
/* 142 */     this.hashCode = -1;
/* 143 */     return this.currentLeafNode;
/*     */   }
/*     */   
/*     */   public NodeImpl addBeanNode() {
/* 147 */     NodeImpl parent = this.nodeList.isEmpty() ? null : (NodeImpl)this.nodeList.get(this.nodeList.size() - 1);
/* 148 */     this.currentLeafNode = NodeImpl.createBeanNode(parent);
/* 149 */     this.nodeList.add(this.currentLeafNode);
/* 150 */     this.hashCode = -1;
/* 151 */     return this.currentLeafNode;
/*     */   }
/*     */   
/*     */   public NodeImpl addReturnValueNode() {
/* 155 */     NodeImpl parent = this.nodeList.isEmpty() ? null : (NodeImpl)this.nodeList.get(this.nodeList.size() - 1);
/* 156 */     this.currentLeafNode = NodeImpl.createReturnValue(parent);
/* 157 */     this.nodeList.add(this.currentLeafNode);
/* 158 */     this.hashCode = -1;
/* 159 */     return this.currentLeafNode;
/*     */   }
/*     */   
/*     */   private NodeImpl addConstructorNode(String name, Class<?>[] parameterTypes) {
/* 163 */     NodeImpl parent = this.nodeList.isEmpty() ? null : (NodeImpl)this.nodeList.get(this.nodeList.size() - 1);
/* 164 */     this.currentLeafNode = NodeImpl.createConstructorNode(name, parent, parameterTypes);
/* 165 */     this.nodeList.add(this.currentLeafNode);
/* 166 */     this.hashCode = -1;
/* 167 */     return this.currentLeafNode;
/*     */   }
/*     */   
/*     */   private NodeImpl addMethodNode(String name, Class<?>[] parameterTypes) {
/* 171 */     NodeImpl parent = this.nodeList.isEmpty() ? null : (NodeImpl)this.nodeList.get(this.nodeList.size() - 1);
/* 172 */     this.currentLeafNode = NodeImpl.createMethodNode(name, parent, parameterTypes);
/* 173 */     this.nodeList.add(this.currentLeafNode);
/* 174 */     this.hashCode = -1;
/* 175 */     return this.currentLeafNode;
/*     */   }
/*     */   
/*     */   public NodeImpl makeLeafNodeIterable() {
/* 179 */     this.currentLeafNode = NodeImpl.makeIterable(this.currentLeafNode);
/*     */     
/* 181 */     this.nodeList.remove(this.nodeList.size() - 1);
/* 182 */     this.nodeList.add(this.currentLeafNode);
/* 183 */     this.hashCode = -1;
/* 184 */     return this.currentLeafNode;
/*     */   }
/*     */   
/*     */   public NodeImpl setLeafNodeIndex(Integer index) {
/* 188 */     this.currentLeafNode = NodeImpl.setIndex(this.currentLeafNode, index);
/*     */     
/* 190 */     this.nodeList.remove(this.nodeList.size() - 1);
/* 191 */     this.nodeList.add(this.currentLeafNode);
/* 192 */     this.hashCode = -1;
/* 193 */     return this.currentLeafNode;
/*     */   }
/*     */   
/*     */   public NodeImpl setLeafNodeMapKey(Object key) {
/* 197 */     this.currentLeafNode = NodeImpl.setMapKey(this.currentLeafNode, key);
/*     */     
/* 199 */     this.nodeList.remove(this.nodeList.size() - 1);
/* 200 */     this.nodeList.add(this.currentLeafNode);
/* 201 */     this.hashCode = -1;
/* 202 */     return this.currentLeafNode;
/*     */   }
/*     */   
/*     */   public NodeImpl setLeafNodeValue(Object value) {
/* 206 */     this.currentLeafNode = NodeImpl.setPropertyValue(this.currentLeafNode, value);
/*     */     
/* 208 */     this.nodeList.remove(this.nodeList.size() - 1);
/* 209 */     this.nodeList.add(this.currentLeafNode);
/* 210 */     this.hashCode = -1;
/* 211 */     return this.currentLeafNode;
/*     */   }
/*     */   
/*     */   public NodeImpl getLeafNode() {
/* 215 */     return this.currentLeafNode;
/*     */   }
/*     */   
/*     */   public Iterator<Path.Node> iterator()
/*     */   {
/* 220 */     if (this.nodeList.size() == 0) {
/* 221 */       return Collections.emptyList().iterator();
/*     */     }
/* 223 */     if (this.nodeList.size() == 1) {
/* 224 */       return this.nodeList.iterator();
/*     */     }
/* 226 */     return this.nodeList.subList(1, this.nodeList.size()).iterator();
/*     */   }
/*     */   
/*     */   public String asString() {
/* 230 */     StringBuilder builder = new StringBuilder();
/* 231 */     boolean first = true;
/* 232 */     for (int i = 1; i < this.nodeList.size(); i++) {
/* 233 */       NodeImpl nodeImpl = (NodeImpl)this.nodeList.get(i);
/* 234 */       String name = nodeImpl.asString();
/* 235 */       if (!name.isEmpty())
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 240 */         if (!first) {
/* 241 */           builder.append(".");
/*     */         }
/*     */         
/* 244 */         builder.append(nodeImpl.asString());
/*     */         
/* 246 */         first = false;
/*     */       } }
/* 248 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 253 */     return asString();
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 258 */     if (this == obj) {
/* 259 */       return true;
/*     */     }
/* 261 */     if (obj == null) {
/* 262 */       return false;
/*     */     }
/* 264 */     if (getClass() != obj.getClass()) {
/* 265 */       return false;
/*     */     }
/* 267 */     PathImpl other = (PathImpl)obj;
/* 268 */     if (this.nodeList == null) {
/* 269 */       if (other.nodeList != null) {
/* 270 */         return false;
/*     */       }
/*     */     }
/* 273 */     else if (!this.nodeList.equals(other.nodeList)) {
/* 274 */       return false;
/*     */     }
/* 276 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 282 */     if (this.hashCode == -1) {
/* 283 */       this.hashCode = buildHashCode();
/*     */     }
/*     */     
/* 286 */     return this.hashCode;
/*     */   }
/*     */   
/*     */   private int buildHashCode() {
/* 290 */     int prime = 31;
/* 291 */     int result = 1;
/*     */     
/* 293 */     result = 31 * result + (this.nodeList == null ? 0 : this.nodeList.hashCode());
/* 294 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private PathImpl(PathImpl path)
/*     */   {
/* 303 */     this(path.nodeList);
/* 304 */     this.currentLeafNode = ((NodeImpl)this.nodeList.get(this.nodeList.size() - 1));
/*     */   }
/*     */   
/*     */   private PathImpl() {
/* 308 */     this.nodeList = new ArrayList();
/* 309 */     this.hashCode = -1;
/*     */   }
/*     */   
/*     */   private PathImpl(List<Path.Node> nodeList) {
/* 313 */     this.nodeList = new ArrayList(nodeList);
/* 314 */     this.hashCode = -1;
/*     */   }
/*     */   
/*     */   private static PathImpl parseProperty(String propertyName) {
/* 318 */     PathImpl path = createRootPath();
/* 319 */     String tmp = propertyName;
/*     */     do {
/* 321 */       Matcher matcher = PATH_PATTERN.matcher(tmp);
/* 322 */       if (matcher.matches())
/*     */       {
/* 324 */         String value = matcher.group(1);
/* 325 */         if (!isValidJavaIdentifier(value)) {
/* 326 */           throw log.getInvalidJavaIdentifierException(value);
/*     */         }
/*     */         
/*     */ 
/* 330 */         path.addPropertyNode(value);
/*     */         
/*     */ 
/* 333 */         if (matcher.group(2) != null) {
/* 334 */           path.makeLeafNodeIterable();
/*     */         }
/*     */         
/*     */ 
/* 338 */         String indexOrKey = matcher.group(3);
/* 339 */         if ((indexOrKey != null) && (indexOrKey.length() > 0)) {
/*     */           try {
/* 341 */             Integer i = Integer.valueOf(Integer.parseInt(indexOrKey));
/* 342 */             path.setLeafNodeIndex(i);
/*     */           }
/*     */           catch (NumberFormatException e) {
/* 345 */             path.setLeafNodeMapKey(indexOrKey);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 350 */         tmp = matcher.group(5);
/*     */       }
/*     */       else {
/* 353 */         throw log.getUnableToParsePropertyPathException(propertyName);
/*     */       }
/* 355 */     } while (tmp != null);
/*     */     
/* 357 */     if (path.getLeafNode().isIterable()) {
/* 358 */       path.addBeanNode();
/*     */     }
/*     */     
/* 361 */     return path;
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
/*     */   private static boolean isValidJavaIdentifier(String identifier)
/*     */   {
/* 375 */     Contracts.assertNotNull(identifier, "identifier param cannot be null");
/*     */     
/* 377 */     if ((identifier.length() == 0) || (!Character.isJavaIdentifierStart(identifier.charAt(0)))) {
/* 378 */       return false;
/*     */     }
/*     */     
/* 381 */     for (int i = 1; i < identifier.length(); i++) {
/* 382 */       if (!Character.isJavaIdentifierPart(identifier.charAt(i))) {
/* 383 */         return false;
/*     */       }
/*     */     }
/* 386 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\path\PathImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */