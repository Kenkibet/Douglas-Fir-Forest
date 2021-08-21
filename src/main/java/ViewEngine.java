import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import spark.ModelAndView;
import spark.TemplateEngine;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

public class ViewEngine extends TemplateEngine {
    private final VelocityEngine  velocityEngine;

    /**
     * Constructor
     */
    public ViewEngine() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties.setProperty(
                "class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine = new VelocityEngine(properties);
    }

    public ViewEngine(VelocityEngine velocityEngine) {
        if (velocityEngine == null) {
            throw new IllegalArgumentException("velocityEngine must not be null");
        }
        this.velocityEngine = velocityEngine;
    }

    @Override
    public String render(ModelAndView modelAndView) {
        try {
            Template template = velocityEngine.getTemplate(modelAndView.getViewName());
            Object model = modelAndView.getModel();
            if (model instanceof Map) {
                Map<?, ?> modelMap = (Map<?, ?>) model;
                VelocityContext velocityContext = new VelocityContext(modelMap);
                StringWriter stringWriter = new StringWriter();
                template.merge(velocityContext, stringWriter);
                return stringWriter.toString();
            } else {
                throw new IllegalArgumentException("Invalid modelView");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
