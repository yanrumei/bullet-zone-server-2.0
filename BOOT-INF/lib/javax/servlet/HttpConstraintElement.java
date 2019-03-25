/*     */ package javax.servlet;
/*     */ 
/*     */ import java.util.ResourceBundle;
/*     */ import javax.servlet.annotation.ServletSecurity.EmptyRoleSemantic;
/*     */ import javax.servlet.annotation.ServletSecurity.TransportGuarantee;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpConstraintElement
/*     */ {
/*     */   private static final String LSTRING_FILE = "javax.servlet.LocalStrings";
/*  34 */   private static final ResourceBundle lStrings = ResourceBundle.getBundle("javax.servlet.LocalStrings");
/*     */   
/*     */   private final ServletSecurity.EmptyRoleSemantic emptyRoleSemantic;
/*     */   
/*     */   private final ServletSecurity.TransportGuarantee transportGuarantee;
/*     */   
/*     */   private final String[] rolesAllowed;
/*     */   
/*     */ 
/*     */   public HttpConstraintElement()
/*     */   {
/*  45 */     this.emptyRoleSemantic = ServletSecurity.EmptyRoleSemantic.PERMIT;
/*  46 */     this.transportGuarantee = ServletSecurity.TransportGuarantee.NONE;
/*  47 */     this.rolesAllowed = new String[0];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpConstraintElement(ServletSecurity.EmptyRoleSemantic emptyRoleSemantic)
/*     */   {
/*  58 */     this.emptyRoleSemantic = emptyRoleSemantic;
/*  59 */     this.transportGuarantee = ServletSecurity.TransportGuarantee.NONE;
/*  60 */     this.rolesAllowed = new String[0];
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
/*     */   public HttpConstraintElement(ServletSecurity.TransportGuarantee transportGuarantee, String... rolesAllowed)
/*     */   {
/*  73 */     this.emptyRoleSemantic = ServletSecurity.EmptyRoleSemantic.PERMIT;
/*  74 */     this.transportGuarantee = transportGuarantee;
/*  75 */     this.rolesAllowed = rolesAllowed;
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
/*     */   public HttpConstraintElement(ServletSecurity.EmptyRoleSemantic emptyRoleSemantic, ServletSecurity.TransportGuarantee transportGuarantee, String... rolesAllowed)
/*     */   {
/*  92 */     if ((rolesAllowed != null) && (rolesAllowed.length > 0) && 
/*  93 */       (ServletSecurity.EmptyRoleSemantic.DENY.equals(emptyRoleSemantic))) {
/*  94 */       throw new IllegalArgumentException(lStrings.getString("httpConstraintElement.invalidRolesDeny"));
/*     */     }
/*     */     
/*  97 */     this.emptyRoleSemantic = emptyRoleSemantic;
/*  98 */     this.transportGuarantee = transportGuarantee;
/*  99 */     this.rolesAllowed = rolesAllowed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServletSecurity.EmptyRoleSemantic getEmptyRoleSemantic()
/*     */   {
/* 107 */     return this.emptyRoleSemantic;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServletSecurity.TransportGuarantee getTransportGuarantee()
/*     */   {
/* 115 */     return this.transportGuarantee;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getRolesAllowed()
/*     */   {
/* 123 */     return this.rolesAllowed;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\HttpConstraintElement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */