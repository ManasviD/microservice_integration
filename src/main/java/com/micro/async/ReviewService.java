package com.micro.async;

import org.micro.Constant;
import org.micro.RestTemplateFactory;
import org.micro.beans.ReviewServiceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
@Component
public class ReviewService {
	@Autowired
	private RestTemplate restTemplate=RestTemplateFactory.restTemplate();
	
	/**
	 * Re-factoring and injecting Hystrix Fall Back
	 * @return
	 */
	@HystrixCommand (fallbackMethod = "defaultCount")
	public String getCount(String id)
	{
		ReviewServiceBean reviewResponse=restTemplate.getForObject(String.format(Constant.REVIEW_SERVICE_END_POINT, id), ReviewServiceBean.class);
		return (reviewResponse.getCount());
	}
	
	public String defaultCount(String id)
	{
		return "Def";
	}

}
