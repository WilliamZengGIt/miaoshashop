package com.ilong.miaoshashop.access;


import com.ilong.miaoshashop.entity.User;

public class UserContext {
	
	private static ThreadLocal<User> userHolder = new ThreadLocal<User>();
	
	public static void setUser(User user) {
		userHolder.set(user);
	}
	
	public static User getUser() {
		return userHolder.get();
	}

}
