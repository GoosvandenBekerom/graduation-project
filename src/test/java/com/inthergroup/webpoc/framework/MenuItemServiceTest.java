//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework;

import com.inthergroup.webpoc.framework.domain.Action;
import com.inthergroup.webpoc.framework.domain.MenuItem;
import com.inthergroup.webpoc.framework.services.MenuItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author gvandenbekerom
 * @since 19-Sep-18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MenuItemServiceTest {
    @Autowired
    private MenuItemService service;

    @Test
    public void testFindDepthAfterItem_Expect_0() {
        MenuItem depth0 = new MenuItem("depth0", new Action());

        int actual = service.findDepthAfterItem(depth0);
        assertEquals(0, actual);
    }

    @Test
    public void testFindDepthAfterItem_Expect_1() {
        MenuItem depth0 = new MenuItem("depth0", new Action());
        MenuItem depth1 = new MenuItem("depth1", new Action());
        depth1.setParent(depth0);
        depth0.getChildren().add(depth1);

        int actual = service.findDepthAfterItem(depth0);
        assertEquals(1, actual);
    }

    @Test
    public void testFindDepthAfterItem_Expect_2() {
        MenuItem depth0 = new MenuItem("depth0", new Action());
        MenuItem depth1 = new MenuItem("depth1", new Action());
        MenuItem depth2 = new MenuItem("depth2", new Action());
        depth1.setParent(depth0);
        depth0.getChildren().add(depth1);
        depth2.setParent(depth1);
        depth1.getChildren().add(depth2);

        int actual = service.findDepthAfterItem(depth0);
        assertEquals(2, actual);
    }
}
