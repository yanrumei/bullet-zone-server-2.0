/*     */ package org.springframework.boot.logging.logback;
/*     */ 
/*     */ import ch.qos.logback.core.joran.action.Action;
/*     */ import ch.qos.logback.core.joran.event.InPlayListener;
/*     */ import ch.qos.logback.core.joran.event.SaxEvent;
/*     */ import ch.qos.logback.core.joran.spi.ActionException;
/*     */ import ch.qos.logback.core.joran.spi.EventPlayer;
/*     */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*     */ import ch.qos.logback.core.joran.spi.Interpreter;
/*     */ import ch.qos.logback.core.util.OptionHelper;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.xml.sax.Attributes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SpringProfileAction
/*     */   extends Action
/*     */   implements InPlayListener
/*     */ {
/*     */   private final Environment environment;
/*  46 */   private int depth = 0;
/*     */   
/*     */   private boolean acceptsProfile;
/*     */   private List<SaxEvent> events;
/*     */   
/*     */   SpringProfileAction(Environment environment)
/*     */   {
/*  53 */     this.environment = environment;
/*     */   }
/*     */   
/*     */   public void begin(InterpretationContext ic, String name, Attributes attributes)
/*     */     throws ActionException
/*     */   {
/*  59 */     this.depth += 1;
/*  60 */     if (this.depth != 1) {
/*  61 */       return;
/*     */     }
/*  63 */     ic.pushObject(this);
/*  64 */     this.acceptsProfile = acceptsProfiles(ic, attributes);
/*  65 */     this.events = new ArrayList();
/*  66 */     ic.addInPlayListener(this);
/*     */   }
/*     */   
/*     */   private boolean acceptsProfiles(InterpretationContext ic, Attributes attributes) {
/*  70 */     String[] profileNames = StringUtils.trimArrayElements(
/*  71 */       StringUtils.commaDelimitedListToStringArray(attributes.getValue("name")));
/*  72 */     if (profileNames.length != 0) {
/*  73 */       for (String profileName : profileNames) {
/*  74 */         OptionHelper.substVars(profileName, ic, this.context);
/*     */       }
/*  76 */       return (this.environment != null) && 
/*  77 */         (this.environment.acceptsProfiles(profileNames));
/*     */     }
/*  79 */     return false;
/*     */   }
/*     */   
/*     */   public void end(InterpretationContext ic, String name) throws ActionException
/*     */   {
/*  84 */     this.depth -= 1;
/*  85 */     if (this.depth != 0) {
/*  86 */       return;
/*     */     }
/*  88 */     ic.removeInPlayListener(this);
/*  89 */     verifyAndPop(ic);
/*  90 */     if (this.acceptsProfile) {
/*  91 */       addEventsToPlayer(ic);
/*     */     }
/*     */   }
/*     */   
/*     */   private void verifyAndPop(InterpretationContext ic) {
/*  96 */     Object o = ic.peekObject();
/*  97 */     Assert.state(o != null, "Unexpected null object on stack");
/*  98 */     Assert.isInstanceOf(SpringProfileAction.class, o, "logback stack error");
/*  99 */     Assert.state(o == this, "ProfileAction different than current one on stack");
/* 100 */     ic.popObject();
/*     */   }
/*     */   
/*     */   private void addEventsToPlayer(InterpretationContext ic) {
/* 104 */     Interpreter interpreter = ic.getJoranInterpreter();
/* 105 */     this.events.remove(0);
/* 106 */     this.events.remove(this.events.size() - 1);
/* 107 */     interpreter.getEventPlayer().addEventsDynamically(this.events, 1);
/*     */   }
/*     */   
/*     */   public void inPlay(SaxEvent event)
/*     */   {
/* 112 */     this.events.add(event);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\logging\logback\SpringProfileAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */