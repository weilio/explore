package free.com.somanyapps;
import free.com.somanyapps.User;
interface IUserManager {
    void addUser(in User user);
    List<User> getAllUsers();
}
