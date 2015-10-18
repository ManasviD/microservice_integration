/**
 * 
 */
package org.micro;

/**
 * @author mdiksh
 *
 */
public abstract class Constant {
	
	public static final String SEARCH_SERVICE_END_POINT="http://localhost:4567/search/bykeyword?searchTerm=%s&as=json"; 
	
	public static final String IMAGE_SERVICE_END_POINT="http://localhost:4568/listing/images?productId=%s";
	
	public static final String REVIEW_SERVICE_END_POINT="http://localhost:4568/listing/reviews/seller?sellerid=%s";
	
	public static final int THREAD_POOL_SIZE=100;

}
