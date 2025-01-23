package eu.tasgroup.gestione.businesscomponent.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


public class RateLimiter {


	//ConcurrentHashMap, sincronizzata
	private final Map<String, UserRequestInfo> REQUEST_MAP = new ConcurrentHashMap<String, UserRequestInfo>();
	private final int MAX_REQUESTS;
	private final long TIME_WINDOW_IN_MILLIS;
	private final long BLOCK_DURATION_IN_MILLIS; //Durata del blocco dopo aver superato il limite

	
	public RateLimiter(int maxRequest, long timeWindowInSeconds, long blockDurationInSeconds) {
		MAX_REQUESTS = maxRequest;
		TIME_WINDOW_IN_MILLIS = TimeUnit.SECONDS.toMillis(timeWindowInSeconds);
		BLOCK_DURATION_IN_MILLIS = TimeUnit.SECONDS.toMillis(blockDurationInSeconds);
	}
	
	public boolean isRateLimited(String clientIP) {
		long currentTime = System.currentTimeMillis();
		REQUEST_MAP.putIfAbsent(clientIP, new UserRequestInfo(0, currentTime, 0));
		
		UserRequestInfo userInfo = REQUEST_MAP.get(clientIP);
		if(currentTime < userInfo.blockEndTime) {
			return true;
		}
		if(currentTime - userInfo.timeStamp > TIME_WINDOW_IN_MILLIS) {
			userInfo.requestCount = 1;
			userInfo.timeStamp = currentTime;
			return false;
		}
		if(userInfo.requestCount < MAX_REQUESTS) {
			userInfo.requestCount++;
			return false;
		}
		
		
		userInfo.blockEndTime = currentTime + BLOCK_DURATION_IN_MILLIS;
		return true;
	}


	private static class UserRequestInfo {
		int requestCount;
		long timeStamp;
		long blockEndTime;
		
		public UserRequestInfo(int requestCount, long timeStamp, long blockEndTime) {
			this.requestCount = requestCount;
			this.timeStamp = timeStamp;
			this.blockEndTime = blockEndTime;
		}
		
		
	}
	
}
