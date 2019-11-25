package ie.gmit.part_two;

import java.util.ArrayList;
import java.util.HashMap;

public class UserDatabase {
	private static HashMap<Integer, User> userMap = new HashMap<>();

	static {
		User plsWork1 = new User(1, "joe", "bigjoe@gmail.com", "secretpw1");
		User plsWork2 = new User(2, "bill", "bigbill@gmail.com", "secretpw2");
		User plsWork3 = new User(3, "bob", "bigbob@gmail.com", "secretpw3");
		User plsWork4 = new User(4, "fred", "bigfred@gmail.com", "secretpw4");
		userMap.put(plsWork1.getId(), plsWork1);
		userMap.put(plsWork2.getId(), plsWork2);
		userMap.put(plsWork3.getId(), plsWork3);
		userMap.put(plsWork4.getId(), plsWork4);
	}

	public static User findUser(int id) {
		return userMap.get(id);
	}
	
	public static ArrayList<User> findUsers() {
		return new ArrayList<>(userMap.values());
	}
	
	public static void addUser(int id, User user) {
		userMap.put(id, user);
	}

	public static void updateUser(int id, User user) {
		userMap.put(id, user);
	}

	public static void removeUser(int id) {
		userMap.remove(id);
	}
}
