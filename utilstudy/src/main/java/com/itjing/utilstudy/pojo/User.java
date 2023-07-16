package com.itjing.utilstudy.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

/**
 * @Author: zhanling.li
 * @Version: 1.0
 * @BelongsProject: ComponentStudy
 * @BelongsPackage: com.itjing.utilstudy.pojo
 * @CreateTime: 2023-07-16  22:31
 * @Description: TODO
 */
@Data
@Builder
@Accessors(chain = true)
public class User {
    private Long id;
    private String userName;
    private Integer age;
}
