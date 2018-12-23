// 以下文件格式为描述路由的协议格式
// 你可以调整 routerConfig 里的内容
// 变量名 routerConfig 为 iceworks 检测关键字，请不要修改名称

import BasicLayout from './layouts/BasicLayout';
import Projects from './pages/Projects';
import Query from './pages/Query';
// import Function from './pages/Function';
import Analysis from './pages/Analysis';
import Setting from './pages/Setting';
import NotFound from './pages/NotFound';

const routerConfig = [
  {
    path: '/',
    layout: BasicLayout,
    component: Projects,
  },
  {
    path: '/query',
    layout: BasicLayout,
    component: Query,
  },
  
  {
    path: '/analysis',
    layout: BasicLayout,
    component: Analysis,
  },
  {
    path: '/setting',
    layout: BasicLayout,
    component: Setting,
  },
  {
    path: '*',
    layout: BasicLayout,
    component: NotFound,
  },
];

export default routerConfig;
