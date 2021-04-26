package core.utils;

import com.auth.framework.core.utils.ResourceFileProvider;
import com.auth.framework.core.utils.XMLReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.*;

class XMLReaderTest {

    private final static Map<String, Collection<String>> expectedRolesMap = new HashMap<>();

    @BeforeAll
    public static void setup() {
        expectedRolesMap.put("Authority A", Arrays.asList("localhost:8080/A", "localhost:8080/B"));
        expectedRolesMap.put("Authority B", Arrays.asList("localhost:8080/C", "localhost:8080/D"));
    }

    @Test
    public void XMLTest() {
        String resourcePath = ResourceFileProvider.provideFullPath("authorities.xml");
        System.out.println("resource path => " + resourcePath);
        XMLReader reader = new XMLReader(resourcePath);
        try {
            Map<String, Collection<String>> actualRolesMap = reader.getActionMapRules();

            assertKeySet(actualRolesMap.keySet());

            Set<String> classPaths = expectedRolesMap.keySet();

            for (String classPath : classPaths) {
                if (actualRolesMap.containsKey(classPath)) {
                    Collection<String> actualRoles = actualRolesMap.get(classPath);
                    Collection<String> expectedRoles = expectedRolesMap.get(classPath);
                    assertRoles(expectedRoles, actualRoles);

                } else {
                    Assertions.fail("XMLReader map doesnt contains " + classPath);
                }
            }

            Assertions.assertTrue(true, actualRolesMap.toString());
        } catch (Exception e) {
            Assertions.fail(e.getCause());
        }
    }

    private void assertKeySet(Set<String> keySet) {
        if (keySet.size() != 2) {
            Assertions.fail("XMLReader map has more than 2 elements: " + keySet);
        }
    }

    private void assertRoles(Collection<String> expectedRoles, Collection<String> actualRoles) {
        if (expectedRoles.size() != actualRoles.size()) {
            Assertions.fail("expected number of roles = " + expectedRoles.size() + ", actual number of roles = " + actualRoles.size());
        }
        for (String actualRole : actualRoles) {
            if (!expectedRoles.contains(actualRole)) {
                Assertions.fail("Unexpected role " + actualRole);
            }
        }
    }
}