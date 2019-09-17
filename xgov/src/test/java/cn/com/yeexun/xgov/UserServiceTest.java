package cn.com.yeexun.xgov;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.com.yeexun.user.service.UserClient;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
	
	@Autowired
	UserClient userService;
	
//	@Autowired
//	IDatasourceClient datasourceClient;
	
	@Test
	public void getById() throws Exception {
		String user = userService.getUserById(1L);
		System.out.println(user);
	}
	
	@Test
	public void testGetDatasourceById() throws Exception {
//		String datasource = datasourceClient.getDatasourceById(64L);
//		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" + datasource);
	}

}
