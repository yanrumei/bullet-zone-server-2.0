package ch.qos.logback.core.joran.action;

import ch.qos.logback.core.joran.spi.InterpretationContext;
import org.xml.sax.Attributes;

public class NOPAction
  extends Action
{
  public void begin(InterpretationContext ec, String name, Attributes attributes) {}
  
  public void end(InterpretationContext ec, String name) {}
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\joran\action\NOPAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */