package com.e_commerce.miscroservice.user.controller;

import org.springframework.stereotype.Component;

//@Component
public class TestController {

//	@PostConstruct
	public void init(){
//		 查询 根据条件查询 代表 userName和userPass两个字段
//		SELECT * FROM `test_tmp_d_t_o` WHERE (age =18 AND user_name="1") or (age >33) ;
//		 List<TestTmpDTO> id = MybatisOperaterUtil.getInstance().finAll(new TestTmpDTO(),
//				 new MybatisSqlWhereBuild(TestTmpDTO.class).
//						 groupBefore().eq(TestTmpDTO::getAge,18).or().eq(TestTmpDTO::getUserName,"1").groupAfter().
//						 or(). groupBefore().gte(TestTmpDTO::getAge,32).eq(TestTmpDTO::getUserName,"10").groupAfter());
//
//		 System.out.println(id);


//		 long count = MybatisOperaterUtil.getInstance().count(new MybatisSqlWhereBuild(TestTmpDTO.class).
//				 count(TestTmpDTO::getUserName).eq(TestTmpDTO::getAge,23));
//		 System.out.println(count);
//
//
//		 TestTmpDTO testTmpDTO = new TestTmpDTO();
//		 testTmpDTO.setAge(23);
//		 testTmpDTO.setUserName("admin5");
//		 testTmpDTO.setUserPass("admin123");
//
//		 int save = MybatisOperaterUtil.getInstance().save(testTmpDTO);
//		 System.out.println(save);
//		System.out.println(testTmpDTO.getId());
////		 testTmpDTO = new TestTmpDTO();
////		 testTmpDTO.setUserName("admin");
//
////		 int delete = MybatisOperaterUtil.getInstance().delete(new MybatisSqlWhereBuild(TestTmpDTO.class)
////				 .eq(TestTmpDTO::getUserName,"admin"));
////		 System.out.println(delete);
////		testTmpDTO = new TestTmpDTO();
////		 testTmpDTO.setUserName("admin4");
////		 int userName = MybatisOperaterUtil.getInstance().update(testTmpDTO,
////		 new MybatisSqlWhereBuild(TestTmpDTO.class).like(TestTmpDTO::getUserName, "admin2%"));
////		 System.out.println(userName);
////
////		 查询 根据条件查询 代表 userName和userPass两个字段
//		 testTmpDTO = new TestTmpDTO();
//		 testTmpDTO.setUserName(MybatisSqlDefaultUtil.STRING_DEFAULT_VALUE);
//		 TestTmpDTO id = MybatisOperaterUtil.getInstance().findOne(new TestTmpDTO(),
//				 new MybatisSqlWhereBuild(TestTmpDTO.class).like(TestTmpDTO::getUserName, "admin%"));
//
//		 System.out.println(id);
////
//////		查询所有
//         List<TestTmpDTO> testTmpDTOS =
//         MybatisOperaterUtil.getInstance().finAll(new TestTmpDTO(),
//         new MybatisSqlWhereBuild(TestTmpDTO.class).page(2, 10));
//		System.out.println(testTmpDTOS);

	}



}
