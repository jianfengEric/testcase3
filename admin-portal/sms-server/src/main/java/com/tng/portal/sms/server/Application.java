/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tng.portal.sms.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.tng.portal.common.util.PropertiesUtil;

@SpringBootApplication
@ComponentScan(basePackages = "com.tng.portal")
@EnableJpaRepositories(basePackages = {"com.tng.portal.ana.repository", "com.tng.portal.sms.server.repository"})
@EntityScan(basePackages = {"com.tng.portal.ana.entity", "com.tng.portal.sms.server.entity"})
public class Application {

	public static void main(String[] args) {
		PropertiesUtil.setFilter(args);
		SpringApplication.run(Application.class, args);
	}


}
