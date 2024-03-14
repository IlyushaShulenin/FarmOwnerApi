package ru.shulenin.farmownerapi.datasource.redis.repository;

public interface RedisRepositoryTest {
    public void init();
    public void findAllTest();
    public void findByIdTest();
    public void saveTest();
    public void saveAllTest();
    public void deleteTest();
    public void clearTest();
}
