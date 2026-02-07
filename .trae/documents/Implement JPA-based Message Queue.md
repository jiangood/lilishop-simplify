# JPA-based Message Queue Implementation Plan

## Overview
Implement a simple yet effective message queue using JPA to replace the existing RocketMQ implementation. The solution will store messages in a database table and provide similar functionality to the current MQ system.

## Components to Create

### 1. Message Entity
- **Class Name**: `MessageQueue`
- **Purpose**: Store messages in the database
- **Fields**: id, topic, tag, message (JSON), status, createTime, updateTime
- **Annotations**: @Entity, @Table, @Id, @GeneratedValue

### 2. Message Service
- **Class Name**: `MessageQueueService`
- **Purpose**: Handle message operations
- **Methods**: send, sendAsync, findByTopic, markAsProcessed, deleteOldMessages
- **Annotations**: @Service

### 3. Message Listener Interface
- **Class Name**: `MessageQueueListener`
- **Purpose**: Define contract for message consumers
- **Methods**: onMessage

### 4. Message Processor
- **Class Name**: `MessageQueueProcessor`
- **Purpose**: Process messages periodically
- **Annotations**: @Component, @Scheduled
- **Functionality**: Poll for unprocessed messages and dispatch to listeners

### 5. Transactional Event
- **Class Name**: `TransactionCommitSendMessageEvent`
- **Purpose**: Replace existing TransactionCommitSendMQEvent
- **Fields**: topic, tag, message
- **Annotations**: Extends ApplicationEvent

### 6. Message Template
- **Class Name**: `MessageQueueTemplate`
- **Purpose**: Mimic RocketMQTemplate interface
- **Methods**: send, asyncSend, sendWithTags
- **Annotations**: @Component

### 7. Event Listener
- **Class Name**: `TransactionCommitSendMessageListener`
- **Purpose**: Listen for transaction commit events and send messages
- **Annotations**: @Component, @TransactionalEventListener

## Key Features

1. **Persistent Storage**: Messages are stored in the database for reliability
2. **Transactional Support**: Messages can be sent after transaction commit
3. **Asynchronous Processing**: Messages are processed in the background
4. **Topic-Based Routing**: Messages are routed to listeners based on topics
5. **Status Tracking**: Messages have statuses to track processing state
6. **Cleanup Mechanism**: Old processed messages are automatically cleaned up

## Integration Points

1. **Replace RocketMQTemplate**: Use MessageQueueTemplate instead
2. **Replace TransactionCommitSendMQEvent**: Use TransactionCommitSendMessageEvent instead
3. **Update Message Listeners**: Implement MessageQueueListener instead of RocketMQListener
4. **Update Configuration**: Remove RocketMQ configuration and add MessageQueue configuration

## Implementation Steps

1. Create the MessageQueue entity
2. Create the MessageQueueService
3. Create the MessageQueueListener interface
4. Create the MessageQueueProcessor
5. Create the TransactionCommitSendMessageEvent
6. Create the MessageQueueTemplate
7. Create the TransactionCommitSendMessageListener
8. Update existing code to use the new message queue system
9. Test the implementation

## Benefits

1. **Simplified Dependencies**: No need for external message broker
2. **Cost Reduction**: No need to maintain a separate MQ server
3. **Ease of Deployment**: All components are part of the application
4. **Consistent Data Model**: Uses the same database as the application
5. **Transaction Support**: Native transaction integration with JPA

This implementation provides a lightweight alternative to RocketMQ while maintaining similar functionality, making it suitable for smaller deployments or environments where external dependencies are limited.