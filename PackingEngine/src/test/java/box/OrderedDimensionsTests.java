package box;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.util.Arrays;

class OrderedDimensionsTests {
    @Test
    void testSameDimensionsComeOutAsGoIn() {
        float[] dims = new float[]{1.0f, 2.0f, 3.0f};
        OrderedDimensions od = new OrderedDimensions(dims);
        assertTrue(Arrays.equals(dims, od.getOrigDimsCopy()));
    }
}