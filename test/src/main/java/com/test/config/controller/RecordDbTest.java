package com.test.config.controller;


//import com.e_commerce.miscroservice.commons.plug.mybatis.util.MybatisOperaterUtil;
//import com.e_commerce.miscroservice.commons.plug.mybatis.util.MybatisSqlWhereBuild;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RecordDbTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:*.xml");
        //查询个数
        // long count = MybatisOperaterUtil.getInstance().count(new MybatisSqlWhereBuild(TestTmpDTO.class).count(TestTmpDTO::getChannel).eq(TestTmpDTO::getAge,23));
        // System.out.println(count); if (1 == 1) { return; }
        //
        // 保存
        // TestTmpDTO testTmpDTO = new TestTmpDTO();
        // testTmpDTO.setAge(23);
        // testTmpDTO.setUserName("admin");
        // testTmpDTO.setUserPass("admin123");
        //
        // int save = MybatisOperaterUtil.getInstance().save(testTmpDTO);
        // System.out.println(save);
        // 更新 testTmpDTO = new TestTmpDTO();
        // testTmpDTO.setChannel("kuayet123");
        // testTmpDTO.setChannel("ssss");
        // int delete = MybatisOperaterUtil.getInstance().delete(new MybatisSqlWhereBuild(TestTmpDTO.class).between(TestTmpDTO::getId, 3, 6));
        // System.out.println(delete);
        // testTmpDTO.setChannel(CHANNEL_PARAM.get());
        // int userName = MybatisOperaterUtil.getInstance().update(testTmpDTO,
        // new MybatisSqlWhereBuild(TestTmpDTO.class).like(TestTmpDTO::getName, "%min"));
        // System.out.println(userName);
        //
        // 查询 根据条件查询 代表 userName和userPass两个字段
        // testTmpDTO = new TestTmpDTO();
        // testTmpDTO.setUserName(MybatisSqlDefaultUtil.STRING_DEFAULT_VALUE);
        // testTmpDTO.setUserPass(MybatisSqlDefaultUtil.STRING_DEFAULT_VALUE);
        // testTmpDTO.setChannel(CHANNEL_PARAM.get());
        // TestTmpDTO id = MybatisOperaterUtil.getInstance().findOne(testTmpDTO,
        // new MybatisSqlWhereBuild(TestTmpDTO.class).eq(TestTmpDTO::getChannel,
        // CHANNEL_PARAM.get()).eq(TestTmpDTO::getId, 1));
        //
        // System.out.println(id);
        //
        //查询所有
//         List<TestTmpDTO> testTmpDTOS =
//         MybatisOperaterUtil.getInstance().finAll(new TestTmpDTO(),
//         new MybatisSqlWhereBuild(TestTmpDTO.class).page(1, 10));
        //
        // System.out.println(testTmpDTOS);
        // ((ClassPathXmlApplicationContext) context).start();
    }
}