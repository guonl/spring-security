package com.guonl.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created on 2018/1/10.
 *
 * @author zlf
 * @since 1.0
 */
@Data
@ConfigurationProperties(prefix = "merryyou.security")
public class SecurityProperties {
    /**
     * 验证码配置
     */
    private ValidateCodeProperties code = new ValidateCodeProperties();

    /**
     * 记住我的有效时间秒
     */
    private int rememberMeSeconds = 60 * 60 * 24 * 7;
}
