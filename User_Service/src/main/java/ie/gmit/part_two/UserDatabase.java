package ie.gmit.part_two;

import java.util.ArrayList;
import java.util.HashMap;

public class UserDatabase {
	private static HashMap<Integer, User> userMap = new HashMap<Integer, User>();

	// Returns a list of ALL Users
	public static ArrayList<User> findUsers() {
		return new ArrayList<User>(userMap.values());
	}

	// Returns a SINGLE User given an id
	public static User findUser(int id) {
		return userMap.get(id);
	}

	// Adds a single User
	public static void addUser(int id, User user) {
		userMap.put(id, user);
	}

	// Updates a single User
	public static void updateUser(int id, User user) {
		removeUser(id);
		addUser(id, user);
		// userMap.put(id, user);
	}

	// Removes a single User
	public static void removeUser(int id) {
		userMap.remove(id);
	}

	// Checks for a duplicate User
	public static boolean checkForUser(int id) {
		// If there's no User return false
		if (findUser(id) == null) {
			return false;
		} else {
			return true;
		}
	}
}
