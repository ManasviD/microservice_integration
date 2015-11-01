package org.micro.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.apache.log4j.Logger;
import org.micro.Constant;
import org.micro.RestTemplateFactory;
import org.micro.beans.ProductBean;
import org.micro.beans.SearchServiceBean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.micro.async.ImageService;
import com.micro.async.ReviewService;
@Configuration
@EnableAutoConfiguration
@RestController
@EnableHystrix
@EnableDiscoveryClient
public class AdvanceSearchService {
	
	final static Logger logger = Logger.getLogger(AdvanceSearchService.class);
	
	private RestTemplate restTemplate =RestTemplateFactory.restTemplate();
	
	private ExecutorService executor = Executors.newFixedThreadPool(Constant.THREAD_POOL_SIZE); 
	
	private long start;

    @RequestMapping("/asearch")
    public List<ProductBean> greeting(@RequestParam(value="term", defaultValue="test") String term) {
        
    	ArrayList<ProductBean> list=new ArrayList<ProductBean>();
    	List<GetRequestTask> tasks = new ArrayList<GetRequestTask>();
       	/**
    	 * Getting the Service Response from Search Service.
    	 * 
    	 */
    	this.start = System.currentTimeMillis();
    	SearchServiceBean serachResponse=restTemplate.getForObject(String.format(Constant.SEARCH_SERVICE_END_POINT,term), SearchServiceBean.class);
    	if(logger.isDebugEnabled()){
    		logger.debug("Advanced service - Count"+serachResponse.getCount());
    		logger.debug("Search Service,"+0+","+(System.currentTimeMillis()-start));
    	}
    	if (serachResponse!=null)
	    	{
    		for (int i=0;i<serachResponse.getCount();i++)
	    		{
	    			ProductBean pb=new ProductBean();
	    			pb.setDescription(serachResponse.getItem()[i].getDescription());
	    			ImageServiceTask imageTask=new ImageServiceTask(serachResponse.getItem()[i].getId(),pb);
	    			ReviewServiceTask reviewtask=new ReviewServiceTask (serachResponse.getItem()[i].getId(),pb);
	    			
	    			tasks.add(new GetRequestTask(imageTask,executor));
	    			tasks.add(new GetRequestTask(reviewtask,executor));
	    			list.add(pb);
	    		}
	    	}
    	// Waiting for all tasks to complete
    	 while(!tasks.isEmpty()) {
             for(Iterator<GetRequestTask> it = tasks.iterator(); it.hasNext();) {
                 GetRequestTask task = it.next();
                 if(task.isDone()) {
                     it.remove();
                 }
             }
             if(!tasks.isEmpty())
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					logger.error(e);
				}
         }
    	 
    	return list;
    }
    
     class ImageServiceTask implements Runnable{
    	
    	private String id;
    	
    	private ProductBean pb;

    	private  ImageService imageService=new ImageService();
    	
    	public ImageServiceTask (String id, ProductBean pb)
    	{
    		this.id=id;
    		this.pb=pb;
    	}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void run() {
			// TODO Auto-generated method stub
			long imagestart= System.currentTimeMillis()-start;
			pb.setImage(imageService.getImage(this.id));
			if(logger.isDebugEnabled()){
			logger.debug("Image Service,"+imagestart+","+(System.currentTimeMillis()-start));
			}
			
		}
    	
    	
    }
     /**
      * Runnable for Review Service Call 
      * @author mdiksh
      *
      */
     class ReviewServiceTask implements Runnable{
     	
     	private String id;
     	
     	private ProductBean pb;
        
     	private ReviewService reviewService=new ReviewService();
     	
     	public ReviewServiceTask (String id, ProductBean pb)
     	{
     		this.id=id;
     		this.pb=pb;
     	}

 		public String getId() {
 			return id;
 		}



 		public void setId(String id) {
 			this.id = id;
 		}
 		
		public void run() {
			// TODO Auto-generated method stub
			long reviewstart= System.currentTimeMillis()-start;
			pb.setReviewCount(reviewService.getCount(this.id));
			if(logger.isDebugEnabled()){	
			logger.debug("Review Service,"+reviewstart+","+(System.currentTimeMillis()-start));
			}
		}
     	
     	
     }
     
     /**
      * 
      * GET Request Task for Async Runnable Abstraction	.
      *
      */
     class GetRequestTask {
         private FutureTask<String> task;
         private String result="SUCESS";
         public GetRequestTask(Runnable work, ExecutorService executor) {
             this.task = new FutureTask<String>(work, result);
             executor.execute(this.task);
         }
         public boolean isDone() {
             return this.task.isDone();
         }
         public String getResponse() {
             try {
                 return this.result;
             } catch(Exception e) {
                 throw new RuntimeException(e);
             }
         }
     }
}
