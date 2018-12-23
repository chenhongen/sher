import React, { Component } from 'react';
import axios from 'axios';
import DataBinder from '@icedesign/data-binder';
import config from './../../../../const';
import { Grid } from '@icedesign/base';
import HotDialog from '../HotDialog';

const { Row, Col } = Grid;

@DataBinder({
  tableData: {
    // 详细请求配置请参见 https://github.com/axios/axios
    //baseURL: 'http://localhost:8081/',
    url: `${config.SERVER_URL}/tablekv/list`,
    responseFormatter: (responseHandler, res, originResponse) => {
      // 拿到接口返回的 res 数据，做一些格式转换处理，使其符合 DataBinder 的要求
      // 最后再按照顺序丢到 responseHandler 方法里继续执行
      if(res.data.list == null)
        res.data.list = [];
      res = {
        status: res.message,
        //success: res.code === "200" ? true : false,
        //message: res.message,
        data: res.data
      };
      responseHandler(res, originResponse);
    },
    method: 'post',
    data: {},
    defaultBindingData: {
      list: [],
      // total: null,
      // pageSize: 10,
    },
  },
})
export default class Card extends Component {
  static displayName = 'Card';

  state = {
    visible: false,
    currtable: {},
    currindex: null
  };

  componentDidMount() {
    let queryCache = { };
    this.fetchData(queryCache);

    // 供父组件调用
    this.props.onRef(this);
  }

  fetchData = (queryCache) => {
    this.props.updateBindingData('tableData', {
      data: {
        ...queryCache
      },
    });
  };

  // 选中
  onSelected = (index) => {
    if(this.state.currindex == index) {
      this.setState({currtable: null, currindex: null});
    } else {
      this.setState({
        currtable: this.props.bindingData.tableData.list[index],
        currindex: index
      });
    }
  }

  // 返回
  getSelected = () => {
    return this.state.currtable;
  }

  render() {
    const data = this.props.bindingData.tableData.list;
    console.log(data);
    
    return (
      <Row wrap gutter="20">
        {data.map((item, index) => {
          return (
            <Col l={8} key={index} onClick={this.onSelected.bind(this, index)}>
              <div style={this.state.currindex==index? styles.selcard: styles.card} >
                <div style={styles.body}>
                  {/* <Icon type="edit" style={styles.settingIcon} onClick={this.showDialog.bind(this, item.id, item.name)} /> */}
                  <HotDialog tablename={item.name} tableid={item.id} 
                    keys={item.keys.replace("，",",").split(",")} 
                    values={item.values.replace("，",",").split(",")} 
                    index={item.code}
                    type={item.dimension}
                  />
                  <div style={styles.name}>{item.name}</div>
                </div>
                <div style={styles.footer}>
                  <div style={styles.remark}>{item.remark}</div>
                  <div style={styles.date}>
                    更新时间：
                    {item.date}
                  </div>
                </div>
              </div>
            </Col>
          );
        })}
      </Row>
    );
  }
}

const styles = {
  card: {
    background: '#fff',
    border: '1px solid rgb(235, 235, 235)',
    marginBottom: '20px',
  },
  selcard: {
    background: '#fff',
    // border: '1px solid #0081CD',
    boxShadow: '0px 0px 8px  #0081CD',
    marginBottom: '20px',
  },
  body: {
    position: 'relative',
    display: 'flex',
    alignItems: 'left',
    justifyContent: 'center',
    flexDirection: 'column',
    padding: '10px 20px 0',
    // height: '160px',
    borderBottom: '1px solid rgb(235, 235, 235)',
  },
  footer: {
    padding: '20px',
  },
  name: {
    fontSize: '16px',
    color: 'rgba(0, 0, 0, 0.85)',
    marginBottom: '10px',
  },
  remark: {
    fontSize: '14px',
    color: 'rgba(0, 0, 0, 0.427)',
    whiteSpace: 'nowrap',
    textOverflow: 'ellipsis',
    marginTop: '6px',
    overflow: 'hidden',
    marginBottom: '10px',
  },
  date: {
    fontSize: '12px',
    color: 'rgba(0, 0, 0, 0.5)',
  },
};
