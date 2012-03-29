package de.codecentric.janus.plugin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class Flash {
    private Map<String, Object> entries;

    private Flash() {
        entries = new HashMap<String, Object>();
    }

    public void put(String key, Object value) {
        entries.put(key, value);
    }

    public Object get(String key) {
        return entries.get(key);
    }

    public <T> T get(String key, Class<T> exp) {
        T result = (T) entries.get(key);
        entries.remove(key);
        return result;
    }

    public void clear() {
        entries.clear();
    }
    
    public static Flash getForRequest(HttpServletRequest req) {
        HttpSession session = req.getSession();

        Flash flash = (Flash) session.getAttribute(SessionKeys.FLASH);

        if (flash == null) {
            flash = new Flash();
            session.setAttribute(SessionKeys.FLASH, flash);
        }

        return flash;
    }
}
