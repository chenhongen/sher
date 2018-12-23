// 菜单配置
// headerMenuConfig：头部导航配置
// asideMenuConfig：侧边导航配置

const headerMenuConfig = [
  {
    name: '反馈',
    path: 'https://github.com/alibaba/ice',
    external: true,
    newWindow: true,
    icon: 'message',
  },
  {
    name: '帮助',
    path: 'https://alibaba.github.io/ice',
    external: true,
    newWindow: true,
    icon: 'bangzhu',
  },
];

// ICON 配置： https://ice.alibaba-inc.com/component/foundationsymbol
const asideMenuConfig = [
  {
    name: '全部表格',
    path: '/',
    icon: 'table',
  },
  {
    name: '查询追溯',
    path: '/query',
    icon: 'cascades',
  },
  {
    name: '数据统计',
    path: '/analysis',
    icon: 'chart',
  },
  {
    name: '基本设置',
    path: '/setting',
    icon: 'shezhi',
  },
];

export { headerMenuConfig, asideMenuConfig };
