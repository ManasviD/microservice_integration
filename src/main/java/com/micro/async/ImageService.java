package com.micro.async;

import org.micro.Constant;
import org.micro.RestTemplateFactory;
import org.micro.beans.ImageServiceBean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
@Component
public class ImageService {
	private RestTemplate restTemplate=RestTemplateFactory.restTemplate();
	
		/**
		 * Re-factoring and injecting Hystrix Fall Back
		 * @return
		 */
		@HystrixCommand (fallbackMethod = "defaultImage")
		public String getImage(String id)
		{
			ImageServiceBean imageResponse=restTemplate.getForObject(String.format(Constant.IMAGE_SERVICE_END_POINT, id), ImageServiceBean.class);
			return (imageResponse.getResults()[0].getUrl_fullxfull());
		}
		
		public String defaultImage(String id)
		{
			return "Def";
		}
	 

}
