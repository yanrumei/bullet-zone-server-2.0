/*     */ package ch.qos.logback.core.net;
/*     */ 
/*     */ import ch.qos.logback.core.AppenderBase;
/*     */ import ch.qos.logback.core.Layout;
/*     */ import ch.qos.logback.core.boolex.EvaluationException;
/*     */ import ch.qos.logback.core.boolex.EventEvaluator;
/*     */ import ch.qos.logback.core.helpers.CyclicBuffer;
/*     */ import ch.qos.logback.core.pattern.PatternLayoutBase;
/*     */ import ch.qos.logback.core.sift.DefaultDiscriminator;
/*     */ import ch.qos.logback.core.sift.Discriminator;
/*     */ import ch.qos.logback.core.spi.CyclicBufferTracker;
/*     */ import ch.qos.logback.core.util.ContentTypeUtil;
/*     */ import ch.qos.logback.core.util.OptionHelper;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import javax.mail.Message.RecipientType;
/*     */ import javax.mail.Multipart;
/*     */ import javax.mail.Session;
/*     */ import javax.mail.Transport;
/*     */ import javax.mail.internet.AddressException;
/*     */ import javax.mail.internet.InternetAddress;
/*     */ import javax.mail.internet.MimeBodyPart;
/*     */ import javax.mail.internet.MimeMessage;
/*     */ import javax.mail.internet.MimeMultipart;
/*     */ import javax.naming.InitialContext;
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
/*     */ public abstract class SMTPAppenderBase<E>
/*     */   extends AppenderBase<E>
/*     */ {
/*  63 */   static InternetAddress[] EMPTY_IA_ARRAY = new InternetAddress[0];
/*     */   static final long MAX_DELAY_BETWEEN_STATUS_MESSAGES = 1228800000L;
/*     */   
/*     */   public SMTPAppenderBase() {
/*  67 */     this.lastTrackerStatusPrint = 0L;
/*  68 */     this.delayBetweenStatusMessages = 300000L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  73 */     this.toPatternLayoutList = new ArrayList();
/*     */     
/*  75 */     this.subjectStr = null;
/*     */     
/*  77 */     this.smtpPort = 25;
/*  78 */     this.starttls = false;
/*  79 */     this.ssl = false;
/*  80 */     this.sessionViaJNDI = false;
/*  81 */     this.jndiLocation = "java:comp/env/mail/Session";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  87 */     this.asynchronousSending = true;
/*     */     
/*  89 */     this.charsetEncoding = "UTF-8";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  95 */     this.discriminator = new DefaultDiscriminator();
/*     */     
/*     */ 
/*  98 */     this.errorCount = 0;
/*     */   }
/*     */   
/*     */   long lastTrackerStatusPrint;
/*     */   long delayBetweenStatusMessages;
/*     */   protected Layout<E> subjectLayout;
/*     */   protected Layout<E> layout;
/*     */   private List<PatternLayoutBase<E>> toPatternLayoutList;
/*     */   private String from;
/*     */   private String subjectStr;
/*     */   private String smtpHost;
/*     */   private int smtpPort;
/*     */   private boolean starttls;
/*     */   private boolean ssl;
/*     */   protected abstract Layout<E> makeSubjectLayout(String paramString);
/*     */   
/*     */   public void start() {
/* 115 */     if (this.cbTracker == null) {
/* 116 */       this.cbTracker = new CyclicBufferTracker();
/*     */     }
/*     */     
/* 119 */     if (this.sessionViaJNDI) {
/* 120 */       this.session = lookupSessionInJNDI();
/*     */     } else {
/* 122 */       this.session = buildSessionFromProperties();
/*     */     }
/* 124 */     if (this.session == null) {
/* 125 */       addError("Failed to obtain javax.mail.Session. Cannot start.");
/* 126 */       return;
/*     */     }
/*     */     
/* 129 */     this.subjectLayout = makeSubjectLayout(this.subjectStr);
/*     */     
/* 131 */     this.started = true;
/*     */   }
/*     */   
/*     */   private Session lookupSessionInJNDI() {
/* 135 */     addInfo("Looking up javax.mail.Session at JNDI location [" + this.jndiLocation + "]");
/*     */     try {
/* 137 */       javax.naming.Context initialContext = new InitialContext();
/* 138 */       Object obj = initialContext.lookup(this.jndiLocation);
/* 139 */       return (Session)obj;
/*     */     } catch (Exception e) {
/* 141 */       addError("Failed to obtain javax.mail.Session from JNDI location [" + this.jndiLocation + "]"); }
/* 142 */     return null;
/*     */   }
/*     */   
/*     */   private Session buildSessionFromProperties()
/*     */   {
/* 147 */     Properties props = new Properties(OptionHelper.getSystemProperties());
/* 148 */     if (this.smtpHost != null) {
/* 149 */       props.put("mail.smtp.host", this.smtpHost);
/*     */     }
/* 151 */     props.put("mail.smtp.port", Integer.toString(this.smtpPort));
/*     */     
/* 153 */     if (this.localhost != null) {
/* 154 */       props.put("mail.smtp.localhost", this.localhost);
/*     */     }
/*     */     
/* 157 */     LoginAuthenticator loginAuthenticator = null;
/*     */     
/* 159 */     if (this.username != null) {
/* 160 */       loginAuthenticator = new LoginAuthenticator(this.username, this.password);
/* 161 */       props.put("mail.smtp.auth", "true");
/*     */     }
/*     */     
/* 164 */     if ((isSTARTTLS()) && (isSSL())) {
/* 165 */       addError("Both SSL and StartTLS cannot be enabled simultaneously");
/*     */     } else {
/* 167 */       if (isSTARTTLS())
/*     */       {
/* 169 */         props.put("mail.smtp.starttls.enable", "true");
/*     */       }
/* 171 */       if (isSSL()) {
/* 172 */         String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
/* 173 */         props.put("mail.smtp.socketFactory.port", Integer.toString(this.smtpPort));
/* 174 */         props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
/* 175 */         props.put("mail.smtp.socketFactory.fallback", "true");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 181 */     return Session.getInstance(props, loginAuthenticator);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void append(E eventObject)
/*     */   {
/* 190 */     if (!checkEntryConditions()) {
/* 191 */       return;
/*     */     }
/*     */     
/* 194 */     String key = this.discriminator.getDiscriminatingValue(eventObject);
/* 195 */     long now = System.currentTimeMillis();
/* 196 */     CyclicBuffer<E> cb = (CyclicBuffer)this.cbTracker.getOrCreate(key, now);
/* 197 */     subAppend(cb, eventObject);
/*     */     try
/*     */     {
/* 200 */       if (this.eventEvaluator.evaluate(eventObject))
/*     */       {
/* 202 */         CyclicBuffer<E> cbClone = new CyclicBuffer(cb);
/*     */         
/* 204 */         cb.clear();
/*     */         
/* 206 */         if (this.asynchronousSending)
/*     */         {
/* 208 */           SMTPAppenderBase<E>.SenderRunnable senderRunnable = new SenderRunnable(cbClone, eventObject);
/* 209 */           this.context.getExecutorService().execute(senderRunnable);
/*     */         }
/*     */         else {
/* 212 */           sendBuffer(cbClone, eventObject);
/*     */         }
/*     */       }
/*     */     } catch (EvaluationException ex) {
/* 216 */       this.errorCount += 1;
/* 217 */       if (this.errorCount < 4) {
/* 218 */         addError("SMTPAppender's EventEvaluator threw an Exception-", ex);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 223 */     if (eventMarksEndOfLife(eventObject)) {
/* 224 */       this.cbTracker.endOfLife(key);
/*     */     }
/*     */     
/* 227 */     this.cbTracker.removeStaleComponents(now);
/*     */     
/* 229 */     if (this.lastTrackerStatusPrint + this.delayBetweenStatusMessages < now) {
/* 230 */       addInfo("SMTPAppender [" + this.name + "] is tracking [" + this.cbTracker.getComponentCount() + "] buffers");
/* 231 */       this.lastTrackerStatusPrint = now;
/*     */       
/* 233 */       if (this.delayBetweenStatusMessages < 1228800000L) {
/* 234 */         this.delayBetweenStatusMessages *= 4L;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean eventMarksEndOfLife(E paramE);
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract void subAppend(CyclicBuffer<E> paramCyclicBuffer, E paramE);
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean checkEntryConditions()
/*     */   {
/* 252 */     if (!this.started) {
/* 253 */       addError("Attempting to append to a non-started appender: " + getName());
/* 254 */       return false;
/*     */     }
/*     */     
/* 257 */     if (this.eventEvaluator == null) {
/* 258 */       addError("No EventEvaluator is set for appender [" + this.name + "].");
/* 259 */       return false;
/*     */     }
/*     */     
/* 262 */     if (this.layout == null) {
/* 263 */       addError("No layout set for appender named [" + this.name + "]. For more information, please visit http://logback.qos.ch/codes.html#smtp_no_layout");
/* 264 */       return false;
/*     */     }
/* 266 */     return true;
/*     */   }
/*     */   
/*     */   public synchronized void stop() {
/* 270 */     this.started = false;
/*     */   }
/*     */   
/*     */   InternetAddress getAddress(String addressStr) {
/*     */     try {
/* 275 */       return new InternetAddress(addressStr);
/*     */     } catch (AddressException e) {
/* 277 */       addError("Could not parse address [" + addressStr + "].", e); }
/* 278 */     return null;
/*     */   }
/*     */   
/*     */   private List<InternetAddress> parseAddress(E event)
/*     */   {
/* 283 */     int len = this.toPatternLayoutList.size();
/*     */     
/* 285 */     List<InternetAddress> iaList = new ArrayList();
/*     */     
/* 287 */     for (int i = 0; i < len; i++) {
/*     */       try {
/* 289 */         PatternLayoutBase<E> emailPL = (PatternLayoutBase)this.toPatternLayoutList.get(i);
/* 290 */         String emailAdrr = emailPL.doLayout(event);
/* 291 */         if ((emailAdrr == null) || (emailAdrr.length() != 0))
/*     */         {
/*     */ 
/* 294 */           InternetAddress[] tmp = InternetAddress.parse(emailAdrr, true);
/* 295 */           iaList.addAll(Arrays.asList(tmp));
/*     */         }
/* 297 */       } catch (AddressException e) { addError("Could not parse email address for [" + this.toPatternLayoutList.get(i) + "] for event [" + event + "]", e);
/* 298 */         return iaList;
/*     */       }
/*     */     }
/*     */     
/* 302 */     return iaList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<PatternLayoutBase<E>> getToList()
/*     */   {
/* 309 */     return this.toPatternLayoutList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void sendBuffer(CyclicBuffer<E> cb, E lastEventObject)
/*     */   {
/*     */     try
/*     */     {
/* 320 */       MimeBodyPart part = new MimeBodyPart();
/*     */       
/* 322 */       StringBuffer sbuf = new StringBuffer();
/*     */       
/* 324 */       String header = this.layout.getFileHeader();
/* 325 */       if (header != null) {
/* 326 */         sbuf.append(header);
/*     */       }
/* 328 */       String presentationHeader = this.layout.getPresentationHeader();
/* 329 */       if (presentationHeader != null) {
/* 330 */         sbuf.append(presentationHeader);
/*     */       }
/* 332 */       fillBuffer(cb, sbuf);
/* 333 */       String presentationFooter = this.layout.getPresentationFooter();
/* 334 */       if (presentationFooter != null) {
/* 335 */         sbuf.append(presentationFooter);
/*     */       }
/* 337 */       String footer = this.layout.getFileFooter();
/* 338 */       if (footer != null) {
/* 339 */         sbuf.append(footer);
/*     */       }
/*     */       
/* 342 */       String subjectStr = "Undefined subject";
/* 343 */       if (this.subjectLayout != null) {
/* 344 */         subjectStr = this.subjectLayout.doLayout(lastEventObject);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 349 */         int newLinePos = subjectStr != null ? subjectStr.indexOf('\n') : -1;
/* 350 */         if (newLinePos > -1) {
/* 351 */           subjectStr = subjectStr.substring(0, newLinePos);
/*     */         }
/*     */       }
/*     */       
/* 355 */       MimeMessage mimeMsg = new MimeMessage(this.session);
/*     */       
/* 357 */       if (this.from != null) {
/* 358 */         mimeMsg.setFrom(getAddress(this.from));
/*     */       } else {
/* 360 */         mimeMsg.setFrom();
/*     */       }
/*     */       
/* 363 */       mimeMsg.setSubject(subjectStr, this.charsetEncoding);
/*     */       
/* 365 */       List<InternetAddress> destinationAddresses = parseAddress(lastEventObject);
/* 366 */       if (destinationAddresses.isEmpty()) {
/* 367 */         addInfo("Empty destination address. Aborting email transmission");
/* 368 */         return;
/*     */       }
/*     */       
/* 371 */       InternetAddress[] toAddressArray = (InternetAddress[])destinationAddresses.toArray(EMPTY_IA_ARRAY);
/* 372 */       mimeMsg.setRecipients(Message.RecipientType.TO, toAddressArray);
/*     */       
/* 374 */       String contentType = this.layout.getContentType();
/*     */       
/* 376 */       if (ContentTypeUtil.isTextual(contentType)) {
/* 377 */         part.setText(sbuf.toString(), this.charsetEncoding, ContentTypeUtil.getSubType(contentType));
/*     */       } else {
/* 379 */         part.setContent(sbuf.toString(), this.layout.getContentType());
/*     */       }
/*     */       
/* 382 */       Multipart mp = new MimeMultipart();
/* 383 */       mp.addBodyPart(part);
/* 384 */       mimeMsg.setContent(mp);
/*     */       
/* 386 */       mimeMsg.setSentDate(new Date());
/* 387 */       addInfo("About to send out SMTP message \"" + subjectStr + "\" to " + Arrays.toString(toAddressArray));
/* 388 */       Transport.send(mimeMsg);
/*     */     } catch (Exception e) {
/* 390 */       addError("Error occurred while sending e-mail notification.", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected abstract void fillBuffer(CyclicBuffer<E> paramCyclicBuffer, StringBuffer paramStringBuffer);
/*     */   
/*     */ 
/*     */   public String getFrom()
/*     */   {
/* 400 */     return this.from;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getSubject()
/*     */   {
/* 407 */     return this.subjectStr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFrom(String from)
/*     */   {
/* 415 */     this.from = from;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSubject(String subject)
/*     */   {
/* 423 */     this.subjectStr = subject;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSMTPHost(String smtpHost)
/*     */   {
/* 432 */     setSmtpHost(smtpHost);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSmtpHost(String smtpHost)
/*     */   {
/* 440 */     this.smtpHost = smtpHost;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getSMTPHost()
/*     */   {
/* 447 */     return getSmtpHost();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getSmtpHost()
/*     */   {
/* 454 */     return this.smtpHost;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSMTPPort(int port)
/*     */   {
/* 463 */     setSmtpPort(port);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSmtpPort(int port)
/*     */   {
/* 472 */     this.smtpPort = port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSMTPPort()
/*     */   {
/* 481 */     return getSmtpPort();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSmtpPort()
/*     */   {
/* 490 */     return this.smtpPort;
/*     */   }
/*     */   
/*     */ 
/* 494 */   public String getLocalhost() { return this.localhost; }
/*     */   
/*     */   private boolean sessionViaJNDI;
/*     */   private String jndiLocation;
/*     */   String username;
/*     */   String password;
/*     */   String localhost;
/*     */   boolean asynchronousSending;
/*     */   private String charsetEncoding;
/*     */   protected Session session;
/*     */   protected EventEvaluator<E> eventEvaluator;
/*     */   protected Discriminator<E> discriminator;
/*     */   protected CyclicBufferTracker<E> cbTracker;
/*     */   private int errorCount;
/* 508 */   public void setLocalhost(String localhost) { this.localhost = localhost; }
/*     */   
/*     */ 
/*     */   public CyclicBufferTracker<E> getCyclicBufferTracker() {
/* 512 */     return this.cbTracker;
/*     */   }
/*     */   
/*     */   public void setCyclicBufferTracker(CyclicBufferTracker<E> cbTracker) {
/* 516 */     this.cbTracker = cbTracker;
/*     */   }
/*     */   
/*     */   public Discriminator<E> getDiscriminator() {
/* 520 */     return this.discriminator;
/*     */   }
/*     */   
/*     */   public void setDiscriminator(Discriminator<E> discriminator) {
/* 524 */     this.discriminator = discriminator;
/*     */   }
/*     */   
/*     */   public boolean isAsynchronousSending() {
/* 528 */     return this.asynchronousSending;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAsynchronousSending(boolean asynchronousSending)
/*     */   {
/* 539 */     this.asynchronousSending = asynchronousSending;
/*     */   }
/*     */   
/*     */   public void addTo(String to) {
/* 543 */     if ((to == null) || (to.length() == 0)) {
/* 544 */       throw new IllegalArgumentException("Null or empty <to> property");
/*     */     }
/* 546 */     PatternLayoutBase plb = makeNewToPatternLayout(to.trim());
/* 547 */     plb.setContext(this.context);
/* 548 */     plb.start();
/* 549 */     this.toPatternLayoutList.add(plb);
/*     */   }
/*     */   
/*     */   protected abstract PatternLayoutBase<E> makeNewToPatternLayout(String paramString);
/*     */   
/*     */   public List<String> getToAsListOfString() {
/* 555 */     List<String> toList = new ArrayList();
/* 556 */     for (PatternLayoutBase plb : this.toPatternLayoutList) {
/* 557 */       toList.add(plb.getPattern());
/*     */     }
/* 559 */     return toList;
/*     */   }
/*     */   
/*     */   public boolean isSTARTTLS() {
/* 563 */     return this.starttls;
/*     */   }
/*     */   
/*     */   public void setSTARTTLS(boolean startTLS) {
/* 567 */     this.starttls = startTLS;
/*     */   }
/*     */   
/*     */   public boolean isSSL() {
/* 571 */     return this.ssl;
/*     */   }
/*     */   
/*     */   public void setSSL(boolean ssl) {
/* 575 */     this.ssl = ssl;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEvaluator(EventEvaluator<E> eventEvaluator)
/*     */   {
/* 585 */     this.eventEvaluator = eventEvaluator;
/*     */   }
/*     */   
/*     */   public String getUsername() {
/* 589 */     return this.username;
/*     */   }
/*     */   
/*     */   public void setUsername(String username) {
/* 593 */     this.username = username;
/*     */   }
/*     */   
/*     */   public String getPassword() {
/* 597 */     return this.password;
/*     */   }
/*     */   
/*     */   public void setPassword(String password) {
/* 601 */     this.password = password;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCharsetEncoding()
/*     */   {
/* 609 */     return this.charsetEncoding;
/*     */   }
/*     */   
/*     */   public String getJndiLocation() {
/* 613 */     return this.jndiLocation;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setJndiLocation(String jndiLocation)
/*     */   {
/* 624 */     this.jndiLocation = jndiLocation;
/*     */   }
/*     */   
/*     */   public boolean isSessionViaJNDI() {
/* 628 */     return this.sessionViaJNDI;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSessionViaJNDI(boolean sessionViaJNDI)
/*     */   {
/* 638 */     this.sessionViaJNDI = sessionViaJNDI;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCharsetEncoding(String charsetEncoding)
/*     */   {
/* 648 */     this.charsetEncoding = charsetEncoding;
/*     */   }
/*     */   
/*     */   public Layout<E> getLayout() {
/* 652 */     return this.layout;
/*     */   }
/*     */   
/*     */   public void setLayout(Layout<E> layout) {
/* 656 */     this.layout = layout;
/*     */   }
/*     */   
/*     */   class SenderRunnable implements Runnable
/*     */   {
/*     */     final CyclicBuffer<E> cyclicBuffer;
/*     */     final E e;
/*     */     
/*     */     SenderRunnable(E cyclicBuffer) {
/* 665 */       this.cyclicBuffer = cyclicBuffer;
/* 666 */       this.e = e;
/*     */     }
/*     */     
/*     */     public void run() {
/* 670 */       SMTPAppenderBase.this.sendBuffer(this.cyclicBuffer, this.e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\net\SMTPAppenderBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */