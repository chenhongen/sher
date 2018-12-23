# SHER

> 项目意义

- 让 运维人员/非开发人员 通过简单配置构建类Excel表格（可从excel复制粘贴），方便业务人员填写记录；
- 减少同数量级记录类纸质表单，电子化过程中开发人员工作量。（省去单独表格页面开发、Java后台开发）；
- 自定义表格使用Elastic Search挂载，可快速追溯查询PB级数据；
- SHER=Springboot + Handsontable + Elasticsearch + React；是这里的项目demo，也是一种打破传统表格录入的思想。期待与你一同创新改变。


![image](https://raw.githubusercontent.com/chenhongen/DemoRun/master/image/sher.gif)

> 待办项:

- [x] [Handsontable] 主键列 不可见；[解决hideColumns收费问题]；
- [x] [Handsontable] 行增加按钮[选中行无效 this.refs.hot.hotInstance.getSelected();]；
- [X] [Elastic Search] 支持es查询结果json+id整合，批量增加+批量更新[ResultsExtractor]；
- [x] [SHER] 增加 K,V表编辑；
- [ ] [SHER] 查询追溯[根据具体业务自定义开发]；
- [ ] [SHER] 数据统计[根据具体业务自定义开发]；

> 使用文档

IDE:

- demo前端使用[飞冰](https://alibaba.github.io/ice/)，你也可以选择你擅长的前端语言，因为handsontable已支持所有主流前端语言；
- [ElasticSearch](https://www.elastic.co/products/elasticsearch);
- Spring boot project runs in Eclipse/other java ide；

唯一Java bean表(kvtable)：
- 实际项目中可用关系型数据库实现，sher中为了方便直接使用es实现dao；
- KV表属性：

| 列 | 描述 |
| ------ | ------ |
| name | 表名称 |
| code | 表index | 
| dimension | 表type | 
| keys | 表头存储项，与values一一对应，以逗号分割 | 
| values | 表头显示项，与keys一一对应，以逗号分割 | 
| remark | 表备注 | 
| date | 变更时间 |

ElasticSearch对应关系（仅供参考）：

|   | index | type | id |
| ------ | ------ | ------ | ------ |
| 大型 | 一种表 | 月份 | id |
| 小型 | 表大类 |  表 | id |



