/*    */ package org.hibernate.validator.internal.constraintvalidators.hv;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import org.hibernate.validator.constraints.SafeHtml;
/*    */ import org.hibernate.validator.constraints.SafeHtml.Tag;
/*    */ import org.jsoup.Jsoup;
/*    */ import org.jsoup.nodes.Document;
/*    */ import org.jsoup.nodes.Element;
/*    */ import org.jsoup.nodes.Node;
/*    */ import org.jsoup.parser.Parser;
/*    */ import org.jsoup.safety.Cleaner;
/*    */ import org.jsoup.safety.Whitelist;
/*    */ import org.jsoup.select.Elements;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SafeHtmlValidator
/*    */   implements ConstraintValidator<SafeHtml, CharSequence>
/*    */ {
/*    */   private Whitelist whitelist;
/*    */   
/*    */   public void initialize(SafeHtml safeHtmlAnnotation)
/*    */   {
/* 35 */     switch (safeHtmlAnnotation.whitelistType()) {
/*    */     case BASIC: 
/* 37 */       this.whitelist = Whitelist.basic();
/* 38 */       break;
/*    */     case BASIC_WITH_IMAGES: 
/* 40 */       this.whitelist = Whitelist.basicWithImages();
/* 41 */       break;
/*    */     case NONE: 
/* 43 */       this.whitelist = Whitelist.none();
/* 44 */       break;
/*    */     case RELAXED: 
/* 46 */       this.whitelist = Whitelist.relaxed();
/* 47 */       break;
/*    */     case SIMPLE_TEXT: 
/* 49 */       this.whitelist = Whitelist.simpleText();
/*    */     }
/*    */     
/* 52 */     this.whitelist.addTags(safeHtmlAnnotation.additionalTags());
/*    */     
/* 54 */     for (SafeHtml.Tag tag : safeHtmlAnnotation.additionalTagsWithAttributes()) {
/* 55 */       this.whitelist.addAttributes(tag.name(), tag.attributes());
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean isValid(CharSequence value, ConstraintValidatorContext context)
/*    */   {
/* 61 */     if (value == null) {
/* 62 */       return true;
/*    */     }
/*    */     
/* 65 */     return new Cleaner(this.whitelist).isValid(getFragmentAsDocument(value));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private Document getFragmentAsDocument(CharSequence value)
/*    */   {
/* 75 */     Document fragment = Jsoup.parse(value.toString(), "", Parser.xmlParser());
/* 76 */     Document document = Document.createShell("");
/*    */     
/*    */ 
/* 79 */     Iterator<Element> nodes = fragment.children().iterator();
/* 80 */     while (nodes.hasNext()) {
/* 81 */       document.body().appendChild((Node)nodes.next());
/*    */     }
/*    */     
/* 84 */     return document;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\hv\SafeHtmlValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */