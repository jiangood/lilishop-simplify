package cn.lili.modules.permission.entity.vo;

import cn.lili.common.utils.ObjectUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;


/**
 * 日志
 *
 * @author Chopper
 * @since 2020/12/2 17:50
 */
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "li_system_log")
public class SystemLog implements Serializable {


    private static final long serialVersionUID = -8995552592401630086L;

    @jakarta.persistence.Id
    @Schema(description = "id")
    private String id;


    @Schema(description = "日志记录时间")
    private Long createTime = new Date().getTime();

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

    /**
     * 转换请求参数为Json
     *
     * @param paramMap
     */
    public void setMapToParams(Map<String, String[]> paramMap) {

        this.requestParam = ObjectUtil.mapToString(paramMap);
    }


}
