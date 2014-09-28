# spark-template-thymeleaf

*Note: By default, `spark-template-thymeleaf` expects all templates to be under `META-INF/templates`, to be valid HTML5 (otherwise an exception is thrown during rendering) and have `.html` as the file suffix. So the path for the template in this example would be `/META-INF/templates/testpage.html`*

How to use the Thymeleaf template route for Spark example:

    package spark.template.thymeleaf.example;

    import static spark.Spark.get;

    import java.util.HashMap;
    import java.util.Map;

    import spark.ModelAndView;
    import spark.Request;
    import spark.Response;
    import spark.TemplateViewRoute;
    import spark.template.thymeleaf.ThymeleafTemplateEngine;

    public class ThymeleafExample {

    	public static class Author {
    		private String name;

    		public Author(String name) {
    			this.name = name;
    		}

    		public String getName() {
    			return name;
    		}

    		public void setName(String name) {
    			this.name = name;
    		}
    	}

    	public static class TestViewRoute implements TemplateViewRoute {

    		public ModelAndView handle(Request request, Response response) {
    			Map<String, Object> model = new HashMap<String, Object>();
    			model.put("title", "testpage");
    			model.put("author", new Author(
    					"Markus W Mahlberg <markus.mahlberg@icloud.com"));

    			return new ModelAndView(model, "testpage");
    		}

    	};

    	public static void main(String[] args) {
    		get("/hello", new TestViewRoute(), new ThymeleafTemplateEngine());
    	}

    }
    