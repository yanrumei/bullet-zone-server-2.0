/*     */ package org.apache.tomcat.util.descriptor.web;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.servlet.HttpConstraintElement;
/*     */ import javax.servlet.HttpMethodConstraintElement;
/*     */ import javax.servlet.ServletSecurityElement;
/*     */ import javax.servlet.annotation.ServletSecurity.EmptyRoleSemantic;
/*     */ import javax.servlet.annotation.ServletSecurity.TransportGuarantee;
/*     */ import org.apache.juli.logging.Log;
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
/*     */ public class SecurityConstraint
/*     */   extends XmlEncodingBase
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public static final String ROLE_ALL_ROLES = "*";
/*     */   public static final String ROLE_ALL_AUTHENTICATED_USERS = "**";
/*  61 */   private static final StringManager sm = StringManager.getManager(Constants.PACKAGE_NAME);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  81 */   private boolean allRoles = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  89 */   private boolean authenticatedUsers = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  98 */   private boolean authConstraint = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 105 */   private String[] authRoles = new String[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 112 */   private SecurityCollection[] collections = new SecurityCollection[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 118 */   private String displayName = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 125 */   private String userConstraint = "NONE";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getAllRoles()
/*     */   {
/* 138 */     return this.allRoles;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getAuthenticatedUsers()
/*     */   {
/* 149 */     return this.authenticatedUsers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getAuthConstraint()
/*     */   {
/* 160 */     return this.authConstraint;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAuthConstraint(boolean authConstraint)
/*     */   {
/* 172 */     this.authConstraint = authConstraint;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDisplayName()
/*     */   {
/* 182 */     return this.displayName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDisplayName(String displayName)
/*     */   {
/* 193 */     this.displayName = displayName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUserConstraint()
/*     */   {
/* 204 */     return this.userConstraint;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUserConstraint(String userConstraint)
/*     */   {
/* 216 */     if (userConstraint != null) {
/* 217 */       this.userConstraint = userConstraint;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void treatAllAuthenticatedUsersAsApplicationRole()
/*     */   {
/* 227 */     if (this.authenticatedUsers) {
/* 228 */       this.authenticatedUsers = false;
/*     */       
/* 230 */       String[] results = new String[this.authRoles.length + 1];
/* 231 */       for (int i = 0; i < this.authRoles.length; i++)
/* 232 */         results[i] = this.authRoles[i];
/* 233 */       results[this.authRoles.length] = "**";
/* 234 */       this.authRoles = results;
/* 235 */       this.authConstraint = true;
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
/*     */   public void addAuthRole(String authRole)
/*     */   {
/* 251 */     if (authRole == null) {
/* 252 */       return;
/*     */     }
/* 254 */     if ("*".equals(authRole)) {
/* 255 */       this.allRoles = true;
/* 256 */       return;
/*     */     }
/*     */     
/* 259 */     if ("**".equals(authRole)) {
/* 260 */       this.authenticatedUsers = true;
/* 261 */       return;
/*     */     }
/*     */     
/* 264 */     String[] results = new String[this.authRoles.length + 1];
/* 265 */     for (int i = 0; i < this.authRoles.length; i++)
/* 266 */       results[i] = this.authRoles[i];
/* 267 */     results[this.authRoles.length] = authRole;
/* 268 */     this.authRoles = results;
/* 269 */     this.authConstraint = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCollection(SecurityCollection collection)
/*     */   {
/* 281 */     if (collection == null) {
/* 282 */       return;
/*     */     }
/* 284 */     collection.setCharset(getCharset());
/*     */     
/* 286 */     SecurityCollection[] results = new SecurityCollection[this.collections.length + 1];
/*     */     
/* 288 */     for (int i = 0; i < this.collections.length; i++)
/* 289 */       results[i] = this.collections[i];
/* 290 */     results[this.collections.length] = collection;
/* 291 */     this.collections = results;
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
/*     */   public boolean findAuthRole(String role)
/*     */   {
/* 305 */     if (role == null)
/* 306 */       return false;
/* 307 */     for (int i = 0; i < this.authRoles.length; i++) {
/* 308 */       if (role.equals(this.authRoles[i]))
/* 309 */         return true;
/*     */     }
/* 311 */     return false;
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
/*     */   public String[] findAuthRoles()
/*     */   {
/* 325 */     return this.authRoles;
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
/*     */   public SecurityCollection findCollection(String name)
/*     */   {
/* 339 */     if (name == null)
/* 340 */       return null;
/* 341 */     for (int i = 0; i < this.collections.length; i++) {
/* 342 */       if (name.equals(this.collections[i].getName()))
/* 343 */         return this.collections[i];
/*     */     }
/* 345 */     return null;
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
/*     */   public SecurityCollection[] findCollections()
/*     */   {
/* 358 */     return this.collections;
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
/*     */   public boolean included(String uri, String method)
/*     */   {
/* 373 */     if (method == null) {
/* 374 */       return false;
/*     */     }
/*     */     
/* 377 */     for (int i = 0; i < this.collections.length; i++) {
/* 378 */       if (this.collections[i].findMethod(method))
/*     */       {
/* 380 */         String[] patterns = this.collections[i].findPatterns();
/* 381 */         for (int j = 0; j < patterns.length; j++) {
/* 382 */           if (matchPattern(uri, patterns[j])) {
/* 383 */             return true;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 388 */     return false;
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
/*     */   public void removeAuthRole(String authRole)
/*     */   {
/* 401 */     if (authRole == null) {
/* 402 */       return;
/*     */     }
/* 404 */     if ("*".equals(authRole)) {
/* 405 */       this.allRoles = false;
/* 406 */       return;
/*     */     }
/*     */     
/* 409 */     if ("**".equals(authRole)) {
/* 410 */       this.authenticatedUsers = false;
/* 411 */       return;
/*     */     }
/*     */     
/* 414 */     int n = -1;
/* 415 */     for (int i = 0; i < this.authRoles.length; i++) {
/* 416 */       if (this.authRoles[i].equals(authRole)) {
/* 417 */         n = i;
/* 418 */         break;
/*     */       }
/*     */     }
/* 421 */     if (n >= 0) {
/* 422 */       int j = 0;
/* 423 */       String[] results = new String[this.authRoles.length - 1];
/* 424 */       for (int i = 0; i < this.authRoles.length; i++) {
/* 425 */         if (i != n)
/* 426 */           results[(j++)] = this.authRoles[i];
/*     */       }
/* 428 */       this.authRoles = results;
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
/*     */   public void removeCollection(SecurityCollection collection)
/*     */   {
/* 441 */     if (collection == null)
/* 442 */       return;
/* 443 */     int n = -1;
/* 444 */     for (int i = 0; i < this.collections.length; i++) {
/* 445 */       if (this.collections[i].equals(collection)) {
/* 446 */         n = i;
/* 447 */         break;
/*     */       }
/*     */     }
/* 450 */     if (n >= 0) {
/* 451 */       int j = 0;
/* 452 */       SecurityCollection[] results = new SecurityCollection[this.collections.length - 1];
/*     */       
/* 454 */       for (int i = 0; i < this.collections.length; i++) {
/* 455 */         if (i != n)
/* 456 */           results[(j++)] = this.collections[i];
/*     */       }
/* 458 */       this.collections = results;
/*     */     }
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
/* 470 */     StringBuilder sb = new StringBuilder("SecurityConstraint[");
/* 471 */     for (int i = 0; i < this.collections.length; i++) {
/* 472 */       if (i > 0)
/* 473 */         sb.append(", ");
/* 474 */       sb.append(this.collections[i].getName());
/*     */     }
/* 476 */     sb.append("]");
/* 477 */     return sb.toString();
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
/*     */   private boolean matchPattern(String path, String pattern)
/*     */   {
/* 497 */     if ((path == null) || (path.length() == 0))
/* 498 */       path = "/";
/* 499 */     if ((pattern == null) || (pattern.length() == 0)) {
/* 500 */       pattern = "/";
/*     */     }
/*     */     
/* 503 */     if (path.equals(pattern)) {
/* 504 */       return true;
/*     */     }
/*     */     
/* 507 */     if ((pattern.startsWith("/")) && (pattern.endsWith("/*"))) {
/* 508 */       pattern = pattern.substring(0, pattern.length() - 2);
/* 509 */       if (pattern.length() == 0)
/* 510 */         return true;
/* 511 */       if (path.endsWith("/"))
/* 512 */         path = path.substring(0, path.length() - 1);
/*     */       for (;;) {
/* 514 */         if (pattern.equals(path))
/* 515 */           return true;
/* 516 */         int slash = path.lastIndexOf('/');
/* 517 */         if (slash <= 0)
/*     */           break;
/* 519 */         path = path.substring(0, slash);
/*     */       }
/* 521 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 525 */     if (pattern.startsWith("*.")) {
/* 526 */       int slash = path.lastIndexOf('/');
/* 527 */       int period = path.lastIndexOf('.');
/* 528 */       if ((slash >= 0) && (period > slash) && 
/* 529 */         (path.endsWith(pattern.substring(1)))) {
/* 530 */         return true;
/*     */       }
/* 532 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 536 */     if (pattern.equals("/")) {
/* 537 */       return true;
/*     */     }
/* 539 */     return false;
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
/*     */   public static SecurityConstraint[] createConstraints(ServletSecurityElement element, String urlPattern)
/*     */   {
/* 556 */     Set<SecurityConstraint> result = new HashSet();
/*     */     
/*     */ 
/*     */ 
/* 560 */     Collection<HttpMethodConstraintElement> methods = element.getHttpMethodConstraints();
/* 561 */     Iterator<HttpMethodConstraintElement> methodIter = methods.iterator();
/* 562 */     while (methodIter.hasNext()) {
/* 563 */       HttpMethodConstraintElement methodElement = (HttpMethodConstraintElement)methodIter.next();
/*     */       
/* 565 */       SecurityConstraint constraint = createConstraint(methodElement, urlPattern, true);
/*     */       
/* 567 */       SecurityCollection collection = constraint.findCollections()[0];
/* 568 */       collection.addMethod(methodElement.getMethodName());
/* 569 */       result.add(constraint);
/*     */     }
/*     */     
/*     */ 
/* 573 */     SecurityConstraint constraint = createConstraint(element, urlPattern, false);
/* 574 */     if (constraint != null)
/*     */     {
/* 576 */       SecurityCollection collection = constraint.findCollections()[0];
/* 577 */       Iterator<String> ommittedMethod = element.getMethodNames().iterator();
/* 578 */       while (ommittedMethod.hasNext()) {
/* 579 */         collection.addOmittedMethod((String)ommittedMethod.next());
/*     */       }
/*     */       
/* 582 */       result.add(constraint);
/*     */     }
/*     */     
/*     */ 
/* 586 */     return (SecurityConstraint[])result.toArray(new SecurityConstraint[result.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */   private static SecurityConstraint createConstraint(HttpConstraintElement element, String urlPattern, boolean alwaysCreate)
/*     */   {
/* 592 */     SecurityConstraint constraint = new SecurityConstraint();
/* 593 */     SecurityCollection collection = new SecurityCollection();
/* 594 */     boolean create = alwaysCreate;
/*     */     
/* 596 */     if (element.getTransportGuarantee() != ServletSecurity.TransportGuarantee.NONE)
/*     */     {
/* 598 */       constraint.setUserConstraint(element.getTransportGuarantee().name());
/* 599 */       create = true;
/*     */     }
/* 601 */     if (element.getRolesAllowed().length > 0) {
/* 602 */       String[] roles = element.getRolesAllowed();
/* 603 */       for (String role : roles) {
/* 604 */         constraint.addAuthRole(role);
/*     */       }
/* 606 */       create = true;
/*     */     }
/* 608 */     if (element.getEmptyRoleSemantic() != ServletSecurity.EmptyRoleSemantic.PERMIT) {
/* 609 */       constraint.setAuthConstraint(true);
/* 610 */       create = true;
/*     */     }
/*     */     
/* 613 */     if (create) {
/* 614 */       collection.addPattern(urlPattern);
/* 615 */       constraint.addCollection(collection);
/* 616 */       return constraint;
/*     */     }
/*     */     
/* 619 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SecurityConstraint[] findUncoveredHttpMethods(SecurityConstraint[] constraints, boolean denyUncoveredHttpMethods, Log log)
/*     */   {
/* 627 */     Set<String> coveredPatterns = new HashSet();
/* 628 */     Map<String, Set<String>> urlMethodMap = new HashMap();
/* 629 */     Map<String, Set<String>> urlOmittedMethodMap = new HashMap();
/*     */     
/* 631 */     List<SecurityConstraint> newConstraints = new ArrayList();
/*     */     
/*     */ 
/*     */ 
/* 635 */     for (SecurityConstraint constraint : constraints) {
/* 636 */       SecurityCollection[] collections = constraint.findCollections();
/* 637 */       for (SecurityCollection collection : collections) {
/* 638 */         String[] patterns = collection.findPatterns();
/* 639 */         String[] methods = collection.findMethods();
/* 640 */         String[] omittedMethods = collection.findOmittedMethods();
/*     */         String str1;
/* 642 */         String pattern; if ((methods.length == 0) && (omittedMethods.length == 0)) {
/* 643 */           String[] arrayOfString1 = patterns;int n = arrayOfString1.length; for (str1 = 0; str1 < n; str1++) { pattern = arrayOfString1[str1];
/* 644 */             coveredPatterns.add(pattern);
/*     */           }
/*     */           
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 651 */           Object omNew = null;
/* 652 */           if (omittedMethods.length != 0) {
/* 653 */             omNew = Arrays.asList(omittedMethods);
/*     */           }
/*     */           
/*     */ 
/* 657 */           String[] arrayOfString2 = patterns;str1 = arrayOfString2.length; for (pattern = 0; pattern < str1; pattern++) { String pattern = arrayOfString2[pattern];
/* 658 */             if (!coveredPatterns.contains(pattern)) {
/* 659 */               if (methods.length == 0)
/*     */               {
/*     */ 
/* 662 */                 Set<String> om = (Set)urlOmittedMethodMap.get(pattern);
/* 663 */                 if (om == null) {
/* 664 */                   om = new HashSet();
/* 665 */                   urlOmittedMethodMap.put(pattern, om);
/* 666 */                   om.addAll((Collection)omNew);
/*     */                 } else {
/* 668 */                   om.retainAll((Collection)omNew);
/*     */                 }
/*     */               }
/*     */               else {
/* 672 */                 Set<String> m = (Set)urlMethodMap.get(pattern);
/* 673 */                 if (m == null) {
/* 674 */                   m = new HashSet();
/* 675 */                   urlMethodMap.put(pattern, m);
/*     */                 }
/* 677 */                 for (String method : methods) {
/* 678 */                   m.add(method);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 687 */     for (??? = urlMethodMap.entrySet().iterator(); ((Iterator)???).hasNext();) { Object entry = (Map.Entry)((Iterator)???).next();
/* 688 */       String pattern = (String)((Map.Entry)entry).getKey();
/* 689 */       if (coveredPatterns.contains(pattern))
/*     */       {
/* 691 */         urlOmittedMethodMap.remove(pattern);
/*     */       }
/*     */       else
/*     */       {
/* 695 */         Set<String> omittedMethods = (Set)urlOmittedMethodMap.remove(pattern);
/* 696 */         Set<String> methods = (Set)((Map.Entry)entry).getValue();
/*     */         
/* 698 */         if (omittedMethods == null) {
/* 699 */           StringBuilder msg = new StringBuilder();
/* 700 */           for (Iterator localIterator = methods.iterator(); localIterator.hasNext();) { method = (String)localIterator.next();
/* 701 */             msg.append((String)method);
/* 702 */             msg.append(' '); }
/*     */           Object method;
/* 704 */           if (denyUncoveredHttpMethods) {
/* 705 */             log.info(sm.getString("securityConstraint.uncoveredHttpMethodFix", new Object[] { pattern, msg
/*     */             
/* 707 */               .toString().trim() }));
/* 708 */             SecurityCollection collection = new SecurityCollection();
/* 709 */             for (method = methods.iterator(); ((Iterator)method).hasNext();) { String method = (String)((Iterator)method).next();
/* 710 */               collection.addOmittedMethod(method);
/*     */             }
/* 712 */             collection.addPatternDecoded(pattern);
/* 713 */             collection.setName("deny-uncovered-http-methods");
/* 714 */             SecurityConstraint constraint = new SecurityConstraint();
/* 715 */             constraint.setAuthConstraint(true);
/* 716 */             constraint.addCollection(collection);
/* 717 */             newConstraints.add(constraint);
/*     */           } else {
/* 719 */             log.error(sm.getString("securityConstraint.uncoveredHttpMethod", new Object[] { pattern, msg
/*     */             
/* 721 */               .toString().trim() }));
/*     */           }
/*     */           
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 728 */           omittedMethods.removeAll(methods);
/*     */           
/* 730 */           handleOmittedMethods(omittedMethods, pattern, denyUncoveredHttpMethods, newConstraints, log);
/*     */         }
/*     */       }
/*     */     }
/* 734 */     for (??? = urlOmittedMethodMap.entrySet().iterator(); ((Iterator)???).hasNext();) { Object entry = (Map.Entry)((Iterator)???).next();
/* 735 */       String pattern = (String)((Map.Entry)entry).getKey();
/* 736 */       if (!coveredPatterns.contains(pattern))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 741 */         handleOmittedMethods((Set)((Map.Entry)entry).getValue(), pattern, denyUncoveredHttpMethods, newConstraints, log);
/*     */       }
/*     */     }
/*     */     
/* 745 */     return (SecurityConstraint[])newConstraints.toArray(new SecurityConstraint[newConstraints.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */   private static void handleOmittedMethods(Set<String> omittedMethods, String pattern, boolean denyUncoveredHttpMethods, List<SecurityConstraint> newConstraints, Log log)
/*     */   {
/* 751 */     if (omittedMethods.size() > 0) {
/* 752 */       StringBuilder msg = new StringBuilder();
/* 753 */       for (Iterator localIterator = omittedMethods.iterator(); localIterator.hasNext();) { method = (String)localIterator.next();
/* 754 */         msg.append(method);
/* 755 */         msg.append(' '); }
/*     */       String method;
/* 757 */       if (denyUncoveredHttpMethods) {
/* 758 */         log.info(sm.getString("securityConstraint.uncoveredHttpOmittedMethodFix", new Object[] { pattern, msg
/*     */         
/* 760 */           .toString().trim() }));
/* 761 */         SecurityCollection collection = new SecurityCollection();
/* 762 */         for (String method : omittedMethods) {
/* 763 */           collection.addMethod(method);
/*     */         }
/* 765 */         collection.addPatternDecoded(pattern);
/* 766 */         collection.setName("deny-uncovered-http-methods");
/* 767 */         SecurityConstraint constraint = new SecurityConstraint();
/* 768 */         constraint.setAuthConstraint(true);
/* 769 */         constraint.addCollection(collection);
/* 770 */         newConstraints.add(constraint);
/*     */       } else {
/* 772 */         log.error(sm.getString("securityConstraint.uncoveredHttpOmittedMethod", new Object[] { pattern, msg
/*     */         
/* 774 */           .toString().trim() }));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\SecurityConstraint.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */