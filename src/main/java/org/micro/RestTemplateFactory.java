package org.micro;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;

public class RestTemplateFactory {
	
	  /**
	 	 * Need to override text/html to use JOSN Mapper
	 	 * 
	 	 * @return
	 	 */
	 	public static RestTemplate restTemplate() {
	 	    RestTemplate restTemplate = new RestTemplate();
	 	    List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
	 	    for (HttpMessageConverter<?> converter : converters) {
	 	        if (converter instanceof MappingJackson2HttpMessageConverter) {
	 	            MappingJackson2HttpMessageConverter jsonConverter = (MappingJackson2HttpMessageConverter) converter;
	 	            jsonConverter.setObjectMapper(new ObjectMapper());
	 	            jsonConverter.setSupportedMediaTypes(ImmutableList.of(new MediaType("application", "json", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET), new MediaType("text", "html", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET)));
	 	        }
	 	    }
	 	    return restTemplate;
	 	}

}
