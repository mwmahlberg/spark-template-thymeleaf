/*
 * Copyright 2014 Markus Mahlberg
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package spark.template.thymeleaf;

import java.util.Map;

import javax.servlet.ServletContext;

import nz.net.ultraq.thymeleaf.LayoutDialect;

import org.thymeleaf.context.AbstractContext;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;

/**
 * 
 * Thymeleaf template engine for Spark Web Application Framework
 * 
 * @author markus
 * @version 0.0.1
 * @see <a href="http://www.sparkjava.com/documentation.html#views-templates">Spark's Template Engine docs</a> 
 * 
 */
public class ThymeleafTemplateEngine extends TemplateEngine {

	private final org.thymeleaf.TemplateEngine thymeleaf;

	public ThymeleafTemplateEngine() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setPrefix("META-INF/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setCacheTTLMs(3600000L);
		thymeleaf = new org.thymeleaf.TemplateEngine();
		thymeleaf.setTemplateResolver(templateResolver);
		thymeleaf.addDialect(new LayoutDialect());

	}

	public ThymeleafTemplateEngine(org.thymeleaf.TemplateEngine thymeleaf) {

		if (thymeleaf == null) {
			throw new IllegalArgumentException("Thymeleaf must not be null");
		}
		this.thymeleaf = thymeleaf;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String render(ModelAndView modelAndView) {

		if (modelAndView.getModel() instanceof Map<?, ?>) {

			for (Object key : ((Map<?, ?>) modelAndView.getModel()).keySet()) {

				/*
				 * Sadly, this is necessary as
				 * org.thymeleaf.context.Context.setVariables expects the map to
				 * be in the form Map<String,?> and an according instanceof is
				 * not possible. So although we are sure that we have a Map, we
				 * need to make sure that all keys are Strings before casting.
				 */
				if (!(key instanceof String)) {
					throw new IllegalArgumentException(
							"All keys of the model must be Strings");
				}

			}

			Map<String, ?> modelMap = (Map<String, ?>) modelAndView.getModel();

			AbstractContext ctx;
			
			if (modelMap.containsKey("request")
					&& modelMap.containsKey("response")
					&& modelMap.containsKey("servletContext")
					&& modelMap.get("request") instanceof Request
					&& modelMap.get("response") instanceof Response
					&& modelMap.get("servletContext") instanceof ServletContext
					) {
				
				 ctx = new WebContext(((Request) modelMap.get("request")).raw(),
						((Response) modelMap.get("response")).raw(),
						((ServletContext) modelMap.get("servletContext")));
				
			} else {
				ctx = new Context();
			}

			ctx.setVariables(modelMap);

			return thymeleaf.process(modelAndView.getViewName(), ctx);

		} else {
			throw new IllegalArgumentException(
					"modelAndView must be of type java.util.Map");
		}

	}

}
