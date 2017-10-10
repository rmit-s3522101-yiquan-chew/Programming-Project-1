import java.util.*;
import org.junit.*;

import controller.LeaderboardController;

public class LeaderboardTest {
	@Test
    public void testSortByValue() {
        Random random = new Random(System.currentTimeMillis());
        Map<String, Integer> testMap = new HashMap<String, Integer>(100);
        for(int i = 0; i < 100; ++i) {
            testMap.put( "user_Id_" + random.nextInt(), random.nextInt());
        }

        testMap = LeaderboardController.sortByValue(testMap);
        Assert.assertEquals(100, testMap.size());

        Integer previous = null;
        for(Map.Entry<String, Integer> entry : testMap.entrySet()) {
            Assert.assertNotNull(entry.getValue());
            if (previous != null) {
                Assert.assertTrue(entry.getValue() >= previous);
            }
            previous = entry.getValue();
        }
    }
}
