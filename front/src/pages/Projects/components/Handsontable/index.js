import React, { Component } from 'react';
import { HotTable } from '@handsontable/react';
import 'handsontable/dist/handsontable.full.css';

export default class Handsontable extends Component {
  static displayName = 'Handsontable';

  constructor(props) {
    super(props);

    // 列校验(Nullable)
    this.columns = [
      {
        data: 'hjid',
        type: 'numeric',
        width: 40
      },
      {
        data: 'hjname',
        type: 'text'
      },
      {
        data: 'hjdate',
        type: 'date',
        dateFormat: 'MM/DD/YYYY'
      }
    ];
  
    /* <K, V> 由DB存储
    * K: elasticsearch筛选字段；
    * V: handsontable显示列名；   columns={this.columns} 
    */
   
  }

  render() {
    return (
      <div id="hot-app">
        <HotTable data={this.props.data} colHeaders={this.props.colHeaders} rowHeaders={true} width="100%" height="450" stretchH="all" />
      </div>
    );
  }
}

