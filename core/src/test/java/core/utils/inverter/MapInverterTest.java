package core.utils.inverter;

import com.auth.framework.core.utils.inverter.MapInverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

class MapInverterTest {

    private final static Map<String, Collection<String>> inputRoleMap = new HashMap<>();
    private final static Map<String, String> expectedRoleMap = new HashMap<>();

    @BeforeAll
    static void setup() {
        inputRoleMap.put("Authority A", Arrays.asList("Resource A", "Resource B"));
        inputRoleMap.put("Authority B", Arrays.asList("Resource C", "Resource D"));

        expectedRoleMap.put("Resource A", "Authority A");
        expectedRoleMap.put("Resource B", "Authority A");
        expectedRoleMap.put("Resource C", "Authority B");
        expectedRoleMap.put("Resource D", "Authority B");
    }

    @Test
    public void invert() {
        assertMap(new MapInverter(inputRoleMap).invert());
    }


    private void assertMap(Map<String, String> inputMap) {
        assertKeySet(inputMap.keySet(), MapInverterTest.expectedRoleMap.keySet());
        assertValues(inputMap.values(), MapInverterTest.expectedRoleMap.values());
    }

    private void assertKeySet(Set<String> inputKeySet, Set<String> expectedKeySet) {
        if (inputKeySet.size() != expectedKeySet.size()) {
            Assertions.fail("input key set size and expected key set size does not match");
        }

        for (String expected : expectedKeySet) {
            if (!inputKeySet.contains(expected))
                Assertions.fail("Wrong expected key: " + expected);
        }
    }

    private void assertValues(Collection<String> inputValues, Collection<String> expectedValues) {
        Set<String> inputSet = new HashSet<>(inputValues);
        Set<String> expectedSet = new HashSet<>(expectedValues);

        if (inputSet.size() != expectedSet.size()) {
            Assertions.fail("input set size and expected set size does not match");
        }

        for (String expected : expectedSet) {
            if (!inputSet.contains(expected)) {
                Assertions.fail("Wrong expected value: " + expected);
            }
        }
    }


}