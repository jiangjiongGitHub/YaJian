package love.image.util;

import love.image.model.t_user;

public class UserThreadLocal {

	private static ThreadLocal<t_user> threadLocal = new ThreadLocal<>();

	public static t_user getUser() {
		return threadLocal.get();
	}

	public static void Set(t_user user) {
		threadLocal.set(user);
	}

}
