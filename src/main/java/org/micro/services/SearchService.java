package org.micro.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.micro.Constant;
import org.micro.beans.ImageServiceBean;
import org.micro.beans.ProductBean;
import org.micro.beans.ReviewServiceBean;
import org.micro.beans.SearchServiceBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;

@RestController
public class SearchService {
	
	final static Logger logger = Logger.getLogger(SearchService.class);
	
    @RequestMapping("/search")
    public List<ProductBean> greeting(@RequestParam(value="term", defaultValue="test") String term) {
        
    	ArrayList<ProductBean> list=new ArrayList<ProductBean>();
    	
    	RestTemplate restTemplate =restTemplate();
    	
    	/**
    	 * Getting the Service Response from Search Service.
    	 * 
    	 */
    	//Calculating the time
    	long start = System.currentTimeMillis();
    	
    	SearchServiceBean serachResponse=restTemplate.getForObject(String.format(Constant.SEARCH_SERVICE_END_POINT, term), SearchServiceBean.class);
    	
    	if(logger.isDebugEnabled()){
    	    logger.debug("Items-Count"+serachResponse.getCount());
    	    logger.debug("Search Service,"+0+","+(System.currentTimeMillis()-start));
    	}
    	
    	if (serachResponse!=null)
	    	{
    		for (int i=0;i<serachResponse.getCount();i++)
	    		{
	    			ProductBean pb=new ProductBean();
	    			pb.setDescription(serachResponse.getItem()[i].getDescription());
	    			
	    			/**
	    			 * Getting the Image Service
	    			 */
	    			long imagestart= System.currentTimeMillis()-start;
	    			ImageServiceBean imageResponse=restTemplate.getForObject(String.format(Constant.IMAGE_SERVICE_END_POINT, serachResponse.getItem()[i].getId()), ImageServiceBean.class);
	    			pb.setImage(imageResponse.getResults()[0].getUrl_fullxfull());
	    			if(logger.isDebugEnabled()){
	    				logger.debug("Image Service,"+imagestart+","+(System.currentTimeMillis()-start));
	    			}
	    			/**
	    			 * Getting the Review Service
	    			 */
	    			long reviewstart= System.currentTimeMillis()-start;
	    			ReviewServiceBean reviewResponse=restTemplate.getForObject(String.format(Constant.REVIEW_SERVICE_END_POINT, serachResponse.getItem()[i].getUser_id()), ReviewServiceBean.class);
	    			pb.setReviewCount(reviewResponse.getCount());
	    			if(logger.isDebugEnabled()){
	    				logger.debug("Review Service,"+reviewstart+","+(System.currentTimeMillis()-start));
	    			}
	    			list.add(pb);
	    		}
	    	}
    	return list;
    }
    /**
	 * Need to override text/html to use JOSN Mapper
	 * 
	 * @return
	 */
	public RestTemplate restTemplate() {
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
