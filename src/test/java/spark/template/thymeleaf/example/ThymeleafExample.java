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
