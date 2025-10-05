package serverengine.users;

import clientserverdto.UserDTO;
import serverengine.logic.engine.EmulatorEngine;

import java.util.*;

public class UserManager {
    private final Map<String, User> usersMap;

    public UserManager() {
        usersMap = new HashMap<>();
    }

    public synchronized void addUser(String username) {
        usersMap.putIfAbsent(username, new User(username));
    }

    public synchronized void removeUser(String username) {
        usersMap.remove(username);
    }

    public synchronized Map<String, User> getUsers() {
        return Collections.unmodifiableMap(usersMap);
    }

    public synchronized List<UserDTO> getUserDTOs() {
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : usersMap.values()) {
            userDTOs.add(user.createDTO());
        }

        return userDTOs;
    }

    public boolean isUserExists(String username) {
        return usersMap.containsKey(username);
    }

    public synchronized User getUser(String username) {
        return usersMap.get(username);
    }

    public synchronized EmulatorEngine getUserEmulatorEngine(String username) {
        User user = usersMap.get(username);
        if (user != null) {
            return user.getUserEngine();
        }

        return null;
    }
}
