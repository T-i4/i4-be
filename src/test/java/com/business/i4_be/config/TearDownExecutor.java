package com.business.i4_be.config;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.transaction.annotation.Transactional;

@TestComponent
public class TearDownExecutor implements InitializingBean {

  private static final String SCHEMA_NAME = "test.";

  @PersistenceContext
  private EntityManager entityManager;
  private List<String> tableNames;

  @Override
  public void afterPropertiesSet() {
    tableNames = entityManager.getMetamodel()
        .getEntities()
        .stream()
        .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
        .map(e -> {
          Table tableAnnotation = e.getJavaType().getAnnotation(Table.class);
          return (tableAnnotation != null) ? tableAnnotation.name()
              : convertToLowerUnderscore(e.getName());
        })
        .collect(Collectors.toList());
  }

  @Transactional
  public void execute() {
    entityManager.flush();

    // 외래키 제약을 비활성화 (모든 테이블에 대해 트리거를 비활성화)
    tableNames.forEach(tableName ->
        entityManager.createNativeQuery("ALTER TABLE " + SCHEMA_NAME + tableName + " DISABLE TRIGGER ALL").executeUpdate()
    );

    // 모든 테이블을 삭제
    tableNames.forEach(tableName ->
        entityManager.createNativeQuery("TRUNCATE TABLE " + SCHEMA_NAME + tableName + " RESTART IDENTITY CASCADE").executeUpdate()
    );

    // 외래키 제약을 다시 활성화 (모든 테이블에 대해 트리거를 활성화)
    tableNames.forEach(tableName ->
        entityManager.createNativeQuery("ALTER TABLE " + SCHEMA_NAME + tableName + " ENABLE TRIGGER ALL").executeUpdate()
    );
  }

  private String convertToLowerUnderscore(String camelCase) {
    return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
  }
}