package javax.el;

public abstract class EvaluationListener
{
  public void beforeEvaluation(ELContext context, String expression) {}
  
  public void afterEvaluation(ELContext context, String expression) {}
  
  public void propertyResolved(ELContext context, Object base, Object property) {}
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\EvaluationListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */