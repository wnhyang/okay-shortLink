package cn.wnhyang.okay.shortlink.job;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyXxlJobTest {
    @Autowired
    private MyXxlJob myXxlJob;

    @Test
    void emailJobHandler() {
        myXxlJob.emailJobHandler();
    }
}