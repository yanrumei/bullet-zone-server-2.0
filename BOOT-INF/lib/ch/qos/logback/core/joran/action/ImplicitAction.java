package ch.qos.logback.core.joran.action;

import ch.qos.logback.core.joran.spi.ElementPath;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import org.xml.sax.Attributes;

public abstract class ImplicitAction
  extends Action
{
  public abstract boolean isApplicable(ElementPath paramElementPath, Attributes paramAttributes, InterpretationContext paramInterpretationContext);
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\joran\action\ImplicitAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */