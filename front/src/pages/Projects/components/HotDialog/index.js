import React, { Component } from 'react';
import axios from 'axios';
import { Dialog, Icon, Button } from '@icedesign/base';
import { HotTable } from '@handsontable/react';
import 'handsontable/dist/handsontable.full.css';
import config from './../../../../const';

export default class HotDialog extends Component {
  static displayName = 'HotDialog';

  constructor(props) {
    super(props);
  
    this.state= {
      visible: false,
      data: [],
      colHeaders: [],
      dataSchema: {},
      columns: [],
      K: this.props.keys||[],
      V: this.props.values
    }
    
  }
  
  componentDidMount() {
    
  }

  // 显示表格
  showDialog = () => {
    let obj = {};
    this.state.K.forEach((key) => obj[key]=null); // 生成schema

    // 生成colums，同时隐藏id列（hideColumns方法为pro收费功能）
    let arr = this.state.K.map((key, index) => {
      // return index===0?{data: key, width: 0.01}:{data: key};

      // 不设主键字段，id交由es管理
      return {data: key};
    });

    console.log(obj, arr);

    this.setState({
      visible: true,
      dataSchema: obj,
      columns: arr
    });

    this.loadData();
  };

  // 隐藏表格
  hideDialog = () => {
    this.setState({
      visible: false,
    });
  };

  // 行增加
  addRow = () => {
    this.refs.hot.hotInstance.alter('insert_row');
  }

  // 行删除
  removeRow = () => {
    // var sels = this.refs.hot.hotInstance.getSelected(); // 选中行无效
    // console.log(sels);
    this.refs.hot.hotInstance.alter('remove_row');
  }

  // 加载数据
  loadData = () => {
    var that = this;
    
    axios.get(`${config.SERVER_URL}/table/list`, {
      params: {
          'index': that.props.index,
          'type': that.props.type
      }
    })
    .then(function (response) {
      console.log(response.data.data.list);
      that.setState({
        data: response.data.data.list||{},
      });
    })
    .catch(function (error) {
      console.log(error);
    });
  }

  // 提交数据
  submitData = () => {
    console.log(this.props.index, this.props.type, this.refs.hot.hotInstance.getSourceData());

    var that = this;
    var params = new URLSearchParams();
    params.append('index', this.props.index);       //你要传给后台的参数值 key/value
    params.append('type', this.props.type);
    params.append('jsonStr', JSON.stringify(this.refs.hot.hotInstance.getSourceData()));

    axios.post(`${config.SERVER_URL}/table`, params)
    .then(function (response) {
      // that.props.fetchData();
      that.setState({
        visible: false,
      });
    })
    .catch(function (error) {
      console.log(error);
    });
  };

  render() {
    return (
    <span>
      <Dialog 
        visible={this.state.visible}
        style={{ width: '900px' }}
        onOk={this.submitData}
        onCancel={this.hideDialog}
        shouldUpdatePosition
        minMargin={50}
        onClose={this.hideDialog}
        title={this.props.tablename}
        >
        <Button.Group style={{marginBottom: '6px'}}>
          <Button type="primary" size="medium" onClick={this.addRow}>行增加</Button>
          <Button type="primary" size="medium" shape="warning" onClick={this.removeRow}>行删除</Button>
        </Button.Group>
        <HotTable
          data={this.state.data}
          dataSchema={this.state.dataSchema}
          colHeaders={this.state.V}
          columns={this.state.columns}
          rowHeaders={true}
          width="100%"
          height="450"
          stretchH="all"
          startRows="1"
          startCols={this.state.K.length}
          ref="hot"
        />
      </Dialog>
      <Icon type="edit" style={styles.settingIcon} onClick={this.showDialog} />
    </span>
    );
  }
}

const styles = {
  settingIcon: {
    position: 'absolute',
    top: '10px',
    right: '20px',
    cursor: 'pointer',
  }
}

