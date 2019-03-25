/*     */ package org.springframework.boot.orm.jpa.hibernate;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.hibernate.boot.model.naming.Identifier;
/*     */ import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
/*     */ import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpringPhysicalNamingStrategy
/*     */   implements PhysicalNamingStrategy
/*     */ {
/*     */   public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment)
/*     */   {
/*  38 */     return apply(name, jdbcEnvironment);
/*     */   }
/*     */   
/*     */ 
/*     */   public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment)
/*     */   {
/*  44 */     return apply(name, jdbcEnvironment);
/*     */   }
/*     */   
/*     */ 
/*     */   public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment)
/*     */   {
/*  50 */     return apply(name, jdbcEnvironment);
/*     */   }
/*     */   
/*     */ 
/*     */   public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment)
/*     */   {
/*  56 */     return apply(name, jdbcEnvironment);
/*     */   }
/*     */   
/*     */ 
/*     */   public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment jdbcEnvironment)
/*     */   {
/*  62 */     return apply(name, jdbcEnvironment);
/*     */   }
/*     */   
/*     */   private Identifier apply(Identifier name, JdbcEnvironment jdbcEnvironment) {
/*  66 */     if (name == null) {
/*  67 */       return null;
/*     */     }
/*  69 */     StringBuilder builder = new StringBuilder(name.getText().replace('.', '_'));
/*  70 */     for (int i = 1; i < builder.length() - 1; i++) {
/*  71 */       if (isUnderscoreRequired(builder.charAt(i - 1), builder.charAt(i), builder
/*  72 */         .charAt(i + 1))) {
/*  73 */         builder.insert(i++, '_');
/*     */       }
/*     */     }
/*  76 */     return getIdentifier(builder.toString(), name.isQuoted(), jdbcEnvironment);
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
/*     */   protected Identifier getIdentifier(String name, boolean quoted, JdbcEnvironment jdbcEnvironment)
/*     */   {
/*  90 */     if (isCaseInsensitive(jdbcEnvironment)) {
/*  91 */       name = name.toLowerCase(Locale.ROOT);
/*     */     }
/*  93 */     return new Identifier(name, quoted);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isCaseInsensitive(JdbcEnvironment jdbcEnvironment)
/*     */   {
/* 102 */     return true;
/*     */   }
/*     */   
/*     */   private boolean isUnderscoreRequired(char before, char current, char after) {
/* 106 */     return (Character.isLowerCase(before)) && (Character.isUpperCase(current)) && 
/* 107 */       (Character.isLowerCase(after));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\orm\jpa\hibernate\SpringPhysicalNamingStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */