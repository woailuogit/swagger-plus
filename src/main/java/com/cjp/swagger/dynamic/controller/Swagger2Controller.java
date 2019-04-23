/*
 *
 *  Copyright 2017-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package com.cjp.swagger.dynamic.controller;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cjp.swagger.dynamic.source.Source;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.PropertySourcedMapping;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

@Controller
@ApiIgnore
public class Swagger2Controller extends springfox.documentation.swagger2.web.Swagger2Controller{
  private Source source;
  private static final Logger LOGGER = LoggerFactory.getLogger(Swagger2Controller.class);
  public Swagger2Controller(Environment environment, DocumentationCache documentationCache,
      ServiceModelToSwagger2Mapper mapper, JsonSerializer jsonSerializer,Source source) {
    super(environment, documentationCache, mapper, jsonSerializer);
    this.source=source;
  }
  private static final String HAL_MEDIA_TYPE = "application/hal+json";
  @RequestMapping(
      value = DEFAULT_URL,
      method = RequestMethod.GET,
      produces = { APPLICATION_JSON_VALUE, HAL_MEDIA_TYPE })
  @PropertySourcedMapping(
      value = "${springfox.documentation.swagger.v2.path}",
      propertyKey = "springfox.documentation.swagger.v2.path")
  @ResponseBody
  public ResponseEntity<Json> getDocumentation(
      @RequestParam(value = "group", required = false) String swaggerGroup,
      HttpServletRequest servletRequest) {
    boolean swaggerSwitch=source.isOpen();
    if(!swaggerSwitch) {
      LOGGER.warn("swagger disable,source isOpen is:{}",swaggerSwitch);
      return new ResponseEntity<Json>(HttpStatus.NOT_FOUND);
    }
    return super.getDocumentation(swaggerGroup, servletRequest);
  }
}
