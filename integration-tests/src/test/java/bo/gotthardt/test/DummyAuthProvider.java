package bo.gotthardt.test;

import bo.gotthardt.model.User;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Bo Gotthardt
 */
@Setter
public class DummyAuthProvider {
    private User user;

    public User getValue(HttpServletRequest request) {
        return user;
    }
}
