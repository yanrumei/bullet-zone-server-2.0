/*     */ package javax.servlet;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.annotation.HttpConstraint;
/*     */ import javax.servlet.annotation.HttpMethodConstraint;
/*     */ import javax.servlet.annotation.ServletSecurity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletSecurityElement
/*     */   extends HttpConstraintElement
/*     */ {
/*  36 */   private final Map<String, HttpMethodConstraintElement> methodConstraints = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServletSecurityElement() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServletSecurityElement(HttpConstraintElement httpConstraintElement)
/*     */   {
/*  51 */     this(httpConstraintElement, null);
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
/*     */   public ServletSecurityElement(Collection<HttpMethodConstraintElement> httpMethodConstraints)
/*     */   {
/*  64 */     addHttpMethodConstraints(httpMethodConstraints);
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
/*     */   public ServletSecurityElement(HttpConstraintElement httpConstraintElement, Collection<HttpMethodConstraintElement> httpMethodConstraints)
/*     */   {
/*  77 */     super(httpConstraintElement.getEmptyRoleSemantic(), httpConstraintElement
/*  78 */       .getTransportGuarantee(), httpConstraintElement
/*  79 */       .getRolesAllowed());
/*  80 */     addHttpMethodConstraints(httpMethodConstraints);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServletSecurityElement(ServletSecurity annotation)
/*     */   {
/*  89 */     this(new HttpConstraintElement(annotation.value().value(), annotation
/*  90 */       .value().transportGuarantee(), annotation
/*  91 */       .value().rolesAllowed()));
/*     */     
/*  93 */     List<HttpMethodConstraintElement> l = new ArrayList();
/*  94 */     HttpMethodConstraint[] constraints = annotation.httpMethodConstraints();
/*  95 */     if (constraints != null) {
/*  96 */       for (int i = 0; i < constraints.length; i++)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */         HttpMethodConstraintElement e = new HttpMethodConstraintElement(constraints[i].value(), new HttpConstraintElement(constraints[i].emptyRoleSemantic(), constraints[i].transportGuarantee(), constraints[i].rolesAllowed()));
/* 103 */         l.add(e);
/*     */       }
/*     */     }
/* 106 */     addHttpMethodConstraints(l);
/*     */   }
/*     */   
/*     */   public Collection<HttpMethodConstraintElement> getHttpMethodConstraints() {
/* 110 */     Collection<HttpMethodConstraintElement> result = new HashSet();
/* 111 */     result.addAll(this.methodConstraints.values());
/* 112 */     return result;
/*     */   }
/*     */   
/*     */   public Collection<String> getMethodNames() {
/* 116 */     Collection<String> result = new HashSet();
/* 117 */     result.addAll(this.methodConstraints.keySet());
/* 118 */     return result;
/*     */   }
/*     */   
/*     */   private void addHttpMethodConstraints(Collection<HttpMethodConstraintElement> httpMethodConstraints)
/*     */   {
/* 123 */     if (httpMethodConstraints == null) {
/* 124 */       return;
/*     */     }
/* 126 */     for (HttpMethodConstraintElement constraint : httpMethodConstraints) {
/* 127 */       String method = constraint.getMethodName();
/* 128 */       if (this.methodConstraints.containsKey(method)) {
/* 129 */         throw new IllegalArgumentException("Duplicate method name: " + method);
/*     */       }
/*     */       
/* 132 */       this.methodConstraints.put(method, constraint);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\ServletSecurityElement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */