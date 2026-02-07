package cn.lili.common.message.queue.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name = "li_message_queue")
@Data
public class MessageQueue implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Message topic
     */
    @NotNull
    private String topic;

    /**
     * Message tag
     */
    private String tag;

    /**
     * Message content (JSON format)
     */
    @Column(columnDefinition = "TEXT")
    private String body;

    /**
     * Message status
     * 0: Pending
     * 1: Processing
     * 2: Processed
     * 3: Failed
     */
    private Integer status;

    /**
     * Create time
     */
    private LocalDateTime createTime;

    /**
     * Update time
     */
    private LocalDateTime updateTime;

    /**
     * Retry count
     */
    private Integer retryCount;

    /**
     * Error message
     */
    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    @PrePersist
    public void prePersist() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.status = 0;
        this.retryCount = 0;
    }

    @PreUpdate
    public void preUpdate() {
        this.updateTime = LocalDateTime.now();
    }

}