package org.springframework.scripting;

import java.util.Map;

public abstract interface ScriptEvaluator
{
  public abstract Object evaluate(ScriptSource paramScriptSource)
    throws ScriptCompilationException;
  
  public abstract Object evaluate(ScriptSource paramScriptSource, Map<String, Object> paramMap)
    throws ScriptCompilationException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scripting\ScriptEvaluator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */