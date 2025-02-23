package com.business.i4_be.common;

import com.business.i4_be.config.TearDownExecutor;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

@Import(TearDownExecutor.class)
@ActiveProfiles("test")
@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
public abstract class DeleteSupport {

  @Autowired
  TearDownExecutor tearDownExecutor;

  @AfterEach
  void delete() {
    tearDownExecutor.execute();
  }
}
