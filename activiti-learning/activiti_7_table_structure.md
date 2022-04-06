| 表名                  | 	解释                                          |
|---------------------|----------------------------------------------|
| 一般数据                | (ACT_GE_)                                    |
| ACT_GE_BYTEARRAY    | 二进制数据表，存储通用的流程定义和流程资源。                       |
| ACT_GE_PROPERTY     | 系统相关属性，属性数据表存储整个流程引擎级别的数据，初始化表结构时，会默认插入三条记录。 |
| 流程历史记录              | (ACT_HI_)                                    |
| ACT_HI_ACTINST      | 历史节点表                                        |
| ACT_HI_ATTACHMENT   | 历史附件表                                        |
| ACT_HI_COMMENT      | 历史意见表                                        |
| ACT_HI_DETAIL       | 历史详情表，提供历史变量的查询                              |
| ACT_HI_IDENTITYLINK | 历史流程人员表                                      |
| ACT_HI_PROCINST     | 历史流程实例表                                      |
| ACT_HI_TASKINST     | 历史任务实例表                                      |
| ACT_HI_VARINST      | 历史变量表                                        |
| 用户用户组表              | (ACT_ID_)                                    |
| ACT_ID_GROUP        | 用户组信息表                                       |
| ACT_ID_INFO         | 用户扩展信息表                                      |
| ACT_ID_MEMBERSHIP   | 用户与用户组对应信息表                                  |
| ACT_ID_USER         | 用户信息表                                        |
| 流程定义表               | (ACT_RE_)                                    |
| ACT_RE_DEPLOYMENT   | 部署信息表                                        |
| ACT_RE_MODEL        | 流程设计模型部署表                                    |
| ACT_RE_PROCDEF      | 流程定义数据表                                      |
| 运行实例表               | (ACT_RU_)                                    |
| ACT_RU_EVENT_SUBSCR | 运行时事件 throwEvent、catchEvent 时间监听信息表          |
| ACT_RU_EXECUTION    | 运行时流程执行实例                                    |
| ACT_RU_IDENTITYLINK | 运行时流程人员表，主要存储任务节点与参与者的相关信息                   |
| ACT_RU_JOB          | 运行时定时任务数据表                                   |
| ACT_RU_TASK         | 运行时任务节点表                                     |
| ACT_RU_VARIABLE     | 运行时流程变量数据表                                   |
| 其它                  |
| ACT_EVT_LOG         | 事件日志                                         |
| ACT_PROCDEF_INFO    | 流程定义的动态变更信息                                  |
