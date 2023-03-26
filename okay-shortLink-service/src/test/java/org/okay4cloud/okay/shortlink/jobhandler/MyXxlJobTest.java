package org.okay4cloud.okay.shortlink.jobhandler;

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