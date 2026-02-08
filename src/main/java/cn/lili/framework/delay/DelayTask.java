package cn.lili.framework.delay;

import cn.hutool.json.JSONUtil;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@Entity
@Table(name = "sys_delay_task")
public class DelayTask {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public DelayTask(String beanId, Date time, Object params, String businessKey) {
        this.businessKey = businessKey;
        this.beanId = beanId;
        this.time = time;
        this.params = JSONUtil.toJsonStr(params);
    }



    /**
     * 唯一KEY
     */
    @Column(unique = true, length = 64)
    private String businessKey;

    /**
     * 执行器beanId
     */
    @Column(length = 30)
    private String beanId;

    /**
     * 执行器 执行时间
     */
    private Date time;

    /**
     * 执行器参数
     */
    @Column(columnDefinition = "TEXT")
    private String params;


}
