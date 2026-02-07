package cn.lili.modules.permission.entity.dos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统日志
 *
 * @author Chopper
 * @since 2020/12/2 17:50
 */
@Data
@Entity
@Table(name = "li_system_log")
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class SystemLog implements Serializable {

    private static final long serialVersionUID = -8995552592401630086L;

    @Id
    @Schema(description = "id")
    private String id;

    @Schema(description = "日志记录时间")
    @Column(name = "create_time")
    private Date createTime = new Date();

    @Schema(description = "请求用户")
    private String username;

    @Schema(description = "请求路径")
    private String requestUrl;

    @Schema(description = "请求参数")
    private String requestParam;

    @Schema(description = "响应参数")
    private String responseBody;

    @Schema(description = "ip")
    private String ip;

    @Schema(description = "方法操作名称")
    private String name;

    @Schema(description = "请求类型")
    private String requestType;

    @Schema(description = "自定义日志内容")
    private String customerLog;

    @Schema(description = "ip信息")
    private String ipInfo;

    @Schema(description = "花费时间")
    private Integer costTime;

    @Schema(description = "商家")
    private Long storeId = -1L;
}
