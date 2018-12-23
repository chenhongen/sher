import React, { Component } from 'react';
import axios from 'axios';
import { Feedback, Dialog, Form, Input, Field } from '@icedesign/base';
import TopBar from '../../components/TopBar';
import Card from './components/Card';
import config from '../../const';

const FormItem = Form.Item;
const Toast = Feedback.toast;

export default class TableList extends Component {
  static displayName = 'TableList';

  constructor(props) {
    super(props);
    this.state = {
      visible: false,
      uvisible: false
    };
    this.field = new Field(this, {autoUnmount: true});
  }

  // 绑定子组件
  onRef = (ref) => {
    this.child = ref;
  }

  showDialog = () => {
    this.setState({
      visible: true,
    });
  };

  hideDialog = () => {
    this.setState({
      visible: false,
    });
  };

  showUDialog = () => {
    let temp = this.child.getSelected();
    if(temp == null) {
      Toast.prompt("请先选中表格！");
    } else {
      // {k:v}  v为null时，有警告
      this.field.setValues({ ...temp });

      this.setState({
        uvisible: true,
      });
        
    }
  };

  hideUDialog = () => {
    this.setState({
      uvisible: false,
    });
  };

  handleSubmit = () => {
    this.field.validate((errors, values) => {
      if (errors) {
        console.log('Errors in form!!!');
        return;
      }
      
      var that = this;

      axios.post(`${config.SERVER_URL}/tablekv`, values)
      .then(function (response) {
        // 调用组件：刷新列表
        that.child.fetchData();

        that.setState({
          visible: false,
        });
      })
      .catch(function (error) {
        console.log(error);
      });
    });
  }

  handleUpdate = () => {
    this.field.validate((errors, values) => {
      if (errors) {
        console.log('Errors in form!!!');
        return;
      }

      var that = this;

      axios.put(`${config.SERVER_URL}/tablekv`, values)
      .then(function (response) {
        // 调用组件：刷新列表
        that.child.fetchData();

        that.setState({
          uvisible: false,
        });
      })
      .catch(function (error) {
        console.log(error);
      });
    });
  }

  render() {
    const init = this.field.init;
    const formItemLayout = {
      labelCol: {
        fixedSpan: 6,
      },
      wrapperCol: {
        span: 14,
      },
    };

    return (
      <div>
        <TopBar
          extraBefore={
            <Input
              size="large"
              placeholder="请输入关键字进行搜索"
              style={{ width: '240px' }}
            />
          }
          buttonText="新建表格"
          clickEvent={this.showDialog}
          buttonUText="编辑表格"
          clickUEvent={this.showUDialog}
        />
        
        <Card onRef={this.onRef} />

        <Dialog
          style={{ width: 640 }}
          className=""
          autoFocus={false}
          footerAlign="center"
          title="增加"
          onOk={this.handleSubmit}
          onCancel={this.hideDialog}
          onClose={this.hideDialog}
          {...this.props}
          visible={this.state.visible}
        >
          <Form direction="ver" field={this.field}>
            <FormItem label="表名：" {...formItemLayout}>
              <Input
                {...init('name', {
                  rules: [{ required: true, message: '必填选项' }],
                })}
              />
            </FormItem>

            <FormItem label="编码：" {...formItemLayout}>
              <Input
                {...init('code', {
                  rules: [{ required: true, message: '必填选项' }],
                })}
              />
            </FormItem>
            
            <FormItem label="月度：" {...formItemLayout}>
              <Input
                {...init('dimension', {
                  rules: [{ required: true, message: '必填选项' }],
                })}
              />
            </FormItem>

            <FormItem label="Keys：" {...formItemLayout}>
              <Input
                {...init('keys', {
                  rules: [{ required: true, message: '逗号分割，必填选项' }],
                })}
              />
            </FormItem>

            <FormItem label="Values：" {...formItemLayout}>
              <Input
                {...init('values', {
                  rules: [{ required: true, message: '逗号分割，必填选项' }],
                })}
              />
            </FormItem>

            <FormItem label="描述：" {...formItemLayout}>
              <Input
                {...init('remark', {
                  rules: [{ required: false, message: '' }],
                })}
              />
            </FormItem>
          </Form>
        </Dialog>

        <Dialog
          style={{ width: 640 }}
          autoFocus={false}
          footerAlign="center"
          title="修改"
          onOk={this.handleUpdate}
          onCancel={this.hideUDialog}
          onClose={this.hideUDialog}
          visible={this.state.uvisible}
        >
          <Form direction="ver" field={this.field}>
            <FormItem>
              <Input  htmlType="hidden"
                {...init('id')}
              />
            </FormItem>
            <FormItem label="表名：" {...formItemLayout}>
              <Input
                {...init('name', {
                  rules: [{ required: true, message: '必填选项' }],
                })}
              />
            </FormItem>

            <FormItem label="编码：" {...formItemLayout}>
              <Input
                {...init('code', {
                  rules: [{ required: true, message: '必填选项' }],
                })}
              />
            </FormItem>
            
            <FormItem label="月度：" {...formItemLayout}>
              <Input
                {...init('dimension', {
                  rules: [{ required: true, message: '必填选项' }],
                })}
              />
            </FormItem>

            <FormItem label="Keys：" {...formItemLayout}>
              <Input
                {...init('keys', {
                  rules: [{ required: true, message: '逗号分割，必填选项' }],
                })}
              />
            </FormItem>

            <FormItem label="Values：" {...formItemLayout}>
              <Input
                {...init('values', {
                  rules: [{ required: true, message: '逗号分割，必填选项' }],
                })}
              />
            </FormItem>

            <FormItem label="描述：" {...formItemLayout}>
              <Input
                {...init('remark', {
                  rules: [{ required: false, message: '' }],
                })}
              />
            </FormItem>
          </Form>
        </Dialog>
      </div>
    );
  }
}
