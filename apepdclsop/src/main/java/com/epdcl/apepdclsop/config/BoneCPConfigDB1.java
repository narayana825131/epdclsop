package com.epdcl.apepdclsop.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.epdcl.apepdclsop.util.EncryptedDataSource;
import com.jolbox.bonecp.BoneCPDataSource;

@Configuration
public class BoneCPConfigDB1 {

    @Value("${spring.db1.url}")
    private String jdbcUrl;

    @Value("${spring.db1.username}")
    private String jdbcUsername;

    @Value("${spring.db1.password}")
    private String jdbcPassword;

    @Value("${spring.db1.driverClass}")
    private String driverClass;

    @Value("${spring.db1.idleMaxAgeInMinutes}")
    private Integer idleMaxAgeInMinutes;

    @Value("${spring.db1.idleConnectionTestPeriodInMinutes}")
    private Integer idleConnectionTestPeriodInMinutes;

    @Value("${spring.db1.maxConnectionsPerPartition}")
    private Integer maxConnectionsPerPartition;

    @Value("${spring.db1.minConnectionsPerPartition}")
    private Integer minConnectionsPerPartition;

    @Value("${spring.db1.partitionCount}")
    private Integer partitionCount;

    @Value("${spring.db1.acquireIncrement}")
    private Integer acquireIncrement;

    @Value("${spring.db1.statementsCacheSize}")
    private Integer statementsCacheSize;
    
    @Value("${spring.db1.queryExecuteTimeLimitInMs}")
    private Integer queryExecuteTimeLimitInMs;
    
    @Value("${spring.db1.maxConnectionAgeInSeconds}")
    private Integer maxConnectionAgeInSeconds;
    
    @Value("${spring.db1.acquireRetryAttempts}")
    private Integer acquireRetryAttempts;
    
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
    	String pwd=EncryptedDataSource.decode(jdbcPassword);
    	BoneCPDataSource dataSource = new BoneCPDataSource();
        dataSource.setDriverClass(driverClass);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(jdbcUsername);
        dataSource.setPassword(pwd);
        dataSource.setIdleConnectionTestPeriodInMinutes(idleConnectionTestPeriodInMinutes);
        dataSource.setIdleMaxAgeInMinutes(idleMaxAgeInMinutes);
        dataSource.setMaxConnectionsPerPartition(maxConnectionsPerPartition);
        dataSource.setMinConnectionsPerPartition(minConnectionsPerPartition);
        dataSource.setPartitionCount(partitionCount);
        dataSource.setAcquireIncrement(acquireIncrement);
        dataSource.setStatementsCacheSize(statementsCacheSize);
        dataSource.setAcquireRetryAttempts(acquireRetryAttempts);
        dataSource.setQueryExecuteTimeLimitInMs(queryExecuteTimeLimitInMs);
        dataSource.setMaxConnectionAgeInSeconds(maxConnectionAgeInSeconds);
        return dataSource;
    }

}