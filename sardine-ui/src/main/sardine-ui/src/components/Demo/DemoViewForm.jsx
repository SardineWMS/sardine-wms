import React, { Component, PropTypes } from 'react';
import { Form, Row, Col, Input, Button, Icon, Table, message, Modal,Card,Select,InputNumber} from 'antd';
const FormItem = Form.Item;

const DemoViewForm = ({
  item = {},
  onEdit,
  onDelete,
  onCreate,
  onBack,
  }) => {

  const formItemLayout = {
      labelCol: { span: 5 },
      wrapperCol: { span: 19 },
    };

 function deleteConfirm() {
  Modal.confirm({
    title: '确定要删除该记录吗 ?',
    content: '重要的事情说三遍 ！',
    okText: '确定',
    cancelText: '取消',
    onOk : onDelete(item.id),
  });
};

    return (
     <div>
     <Button onClick={() => onCreate()}> 新建</Button>
     <Button style={{ marginLeft: 8 }} onClick={() => onEdit(item)}> 编辑</Button>
     <Button style={{ marginLeft: 8 }} onClick={() => deleteConfirm()}> 
          <a>删除</a>
     </Button>
     <Button style={{ marginLeft: 8 }} onClick={() => onBack()}> 返回</Button>

    <Card title="基本信息">
      <Row gutter={16}>
       <Col span={12}>
        <Form horizontal>
        <FormItem {...formItemLayout} label="姓名 :" hasFeedback>
            <span> {item.name} </span>
        </FormItem>
        <FormItem {...formItemLayout} label="地址：" hasFeedback>
            <label>{item.address} </label>
        </FormItem>
        <FormItem {...formItemLayout} label="年龄：" hasFeedback>
            <label>{item.age} </label>
        </FormItem>
      </Form>
      </Col>
      <Col span={12}>
        <Form horizontal>
        <FormItem {...formItemLayout} label="姓名 :" hasFeedback>
            <span> {item.name} </span>
        </FormItem>
        <FormItem {...formItemLayout} label="地址：" hasFeedback>
            <label>{item.address} </label>
        </FormItem>
        <FormItem {...formItemLayout} label="年龄：" hasFeedback>
            <label>{item.age} </label>
        </FormItem>
      </Form>
      </Col>
      </Row>

      <Row gutter={16}>
       <Col span={12}>
      <Card title="基本信息1" bordered={false}>
        <Form horizontal>
        <FormItem {...formItemLayout} label="姓名 :" hasFeedback>
            <label>李福平 </label>
        </FormItem>
        <FormItem {...formItemLayout} label="地址：" hasFeedback>
            <label>贵州 </label>
        </FormItem>
        <FormItem {...formItemLayout} label="年龄：" hasFeedback>
            <label>12 </label>
        </FormItem>
      </Form>
      </Card>
      </Col>
      <Col span={12}>
      <Card title="基本信息2" bordered={false}>
        <Form horizontal>
        <FormItem {...formItemLayout} label="姓名 :" hasFeedback>
            <label>李福平 </label>
        </FormItem>
        <FormItem {...formItemLayout} label="地址：" hasFeedback>
            <label>贵州 </label>
        </FormItem>
        <FormItem {...formItemLayout} label="芳龄：" hasFeedback>
            <label>12 </label>
        </FormItem>
      </Form>
      </Card>
      </Col>
      </Row>

    <Row>
      <Col span={24}>
      <Card title="说明" bordered={false} >
        <Input type="textarea" rows={4} disabled = {true} defaultValue = "小平是个好同志。" />
      </Card>
      </Col>
    </Row>
    </Card>
    </div>
    );
};

DemoViewForm.propTypes = {
  form: PropTypes.object,
  item: PropTypes.object,
  onEdit: PropTypes.func,
  onDelete: PropTypes.func,
  onCreate: PropTypes.func,
  onBack: PropTypes.func,
};

export default DemoViewForm;