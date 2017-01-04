import React, { Component, PropTypes } from 'react';
import { Form, Row, Col, Input, Button, Icon, Table, message, Popconfirm,Card,Select,InputNumber} from 'antd';
const FormItem = Form.Item;

const DemoCreateForm = ({
  item = {},
  onOk,
  onCancel,
  form: {
    getFieldDecorator,
    validateFields,
    getFieldsValue,
    },
  }) => {
   function handleOk() {
    validateFields((errors) => {
      if (errors) {
        return;
      }
      const data = { ...getFieldsValue(), key: item.key };
      onOk(data);
    });
  }

  function checkNumber(rule, value, callback) {
    if (!value) {
      callback(new Error('年龄未填写'));
    }
    if (!/^[\d]{1,2}$/.test(value)) {
      callback(new Error('年龄不合法'));
    } else {
      callback();
    }
  }

    const formItemLayout = {
      labelCol: { span: 5 },
      wrapperCol: { span: 19 },
    };

    return (
     <div>
     <div className="ant-table-title">
     <Button onClick={handleOk}> 保存</Button>
     <Button style={{ marginLeft: 8 }} onClick={() => onCancel()}> 取消</Button>
     </div>
     <Card title="基本信息">
      <Row gutter={16}>
       <Col span={12}>
      	<Form horizontal>
        <FormItem {...formItemLayout} label="姓名 :" hasFeedback>
            {getFieldDecorator('name', {
            initialValue: item.name,
            rules: [
              { required: true, message: '名称未填写' },
            ],
          })(
              <Input type="text"/>
            )}
        </FormItem>
        <FormItem {...formItemLayout} label="地址：" hasFeedback>
            {getFieldDecorator('address' ,{
            initialValue: item.address,
            rules: [{ required: true, message: '地址不能为空' },],
        })(
        <Select size="large" defaultValue="华东" style={{ width: 200 }} >
                  	<Option value="华东">华东</Option>
                  	<Option value="华南">华南</Option>
                  	<Option value="东北">东北</Option>
                  	<Option value="西南">西南</Option>
        </Select>
        )}
        </FormItem>
        <FormItem {...formItemLayout} label="年龄：" hasFeedback>
         {getFieldDecorator('age',{
            initialValue: item.age,})(
            <InputNumber min={1} max={100} defaultValue={20} />
             )}
        </FormItem>
      </Form>
      </Col>
   </Row>
    </Card>
    </div>
    );
};

DemoCreateForm.propTypes = {
  form: PropTypes.object,
  item: PropTypes.object,
  onOk: PropTypes.func,
  onCancel: PropTypes.func,
};

export default Form.create()(DemoCreateForm);