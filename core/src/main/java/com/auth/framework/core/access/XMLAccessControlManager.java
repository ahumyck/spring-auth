package com.auth.framework.core.access;

import com.auth.framework.core.access.isAdmin.IsUserAdminValidator;
import com.auth.framework.core.role.AttributeGrantedAuthority;
import com.auth.framework.core.users.UserPrincipal;
import com.auth.framework.core.utils.XMLReader;
import com.auth.framework.core.utils.inverter.MapInverter;

import java.util.Map;


public class XMLAccessControlManager implements AccessControlManager {

    private final Map<String, String> roleAccessMap; // resources -> role
    private final IsUserAdminValidator validator;

    public XMLAccessControlManager(String resource, IsUserAdminValidator validator) {
        XMLReader reader = new XMLReader(resource);
        try {
            roleAccessMap = new MapInverter(reader.getActionMapRules()).invert();
            this.validator = validator;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean haveRights(UserPrincipal user, String resource) {
        if (!validator.isAdmin(user)) {
            if (roleAccessMap.containsKey(resource)) {
                return hasAuthority(user, resource);
            } else {
                if (isInheritable(resource)) {
                    return hasAuthority(user, resource);
                } else {
                    return true; // Означает, что для доступа к ресурсу не нужно иметь аттрибуты
                }
            }
        }
        return true;
    }

    private boolean hasAuthority(UserPrincipal user, String resource) {
        final String role = roleAccessMap.get(resource);
        return user.getAttributes()
                .stream()
                .map(AttributeGrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals(role));
    }


    private boolean isInheritable(String userResource) {
        for (String resourcePath : roleAccessMap.keySet()) {
            if (userResource.contains(resourcePath)) {
                roleAccessMap.put(userResource, roleAccessMap.get(resourcePath));
                return true;
            }
        }
        return false;
    }
}
