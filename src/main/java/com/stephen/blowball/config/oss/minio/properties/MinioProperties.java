package com.stephen.blowball.config.oss.minio.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * redisson 配置
 *
 * @author: stephen qiu
 * @create: 2024-11-07 12:42
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "oss.minio")
public class MinioProperties {
	
	/**
	 * 是否开启Minio
	 */
	private Boolean enable = false;
	
	/**
	 * 域名（一定是协议前缀+ip+port）
	 */
	private String endpoint;
	
	/**
	 * 是否开启TLS
	 */
	private Boolean enableTls = false;
	
	/**
	 * 用户的 SecretId
	 */
	private String secretId;
	
	/**
	 * 用户的 SecretKey
	 */
	private String secretKey;
	
	/**
	 * 桶名称
	 */
	private String bucketName;
}
