import React , {PropTypes} from 'react';
import { 
	Form,
	Input, 
	Tooltip, 
	Icon, 
	Cascader, 
	Select, 
	Row, 
	Col, 
	Checkbox, 
	Button 
} from 'antd';

import {config} from '../utils';
import styles from './login.less';

const FormItem = Form.Item;
const Register1 = ({
	onSignIn,
  onBack,
	signInButtonLoading,
	form : {
		getFieldDecorator,
		validateFieldsAndScroll
	}
}) => {
	function handleSign(){
		validateFieldsAndScroll((errors,values) => {
			if(errors){
				return;
			}
			onSignIn(values);
		})
	}

  function handleCancel(){
      onBack();
  }

	document.onKeyup = e => e.keyCode === 13 && handleOk();
    const formItemLayout = {
      labelCol: { span: 6 },
      wrapperCol: { span: 14 },
    };
    const tailFormItemLayout = {
      wrapperCol: {
        span: 6,
        offset: 6,
      },
    };
    const ButtonGroup = Button.Group;

	return (
    <div className={styles.signInForm}>
      <div className={styles.logo}>
        <span>用户注册</span>
      </div>
      <Form onSubmit={handleSign}>
        <FormItem
          {...formItemLayout}
          label="用户名"
          hasFeedback
        >
          {getFieldDecorator('userName', {
            rules: [{
              required: true, message: 'Please input your E-mail!',
            }],
          })(
            <Input />
          )}
        </FormItem>
        <FormItem {...formItemLayout} label="邮箱" hasFeedback>
          {getFieldDecorator('email', {
            rules: [{
              type: 'email', message: 'The input is not valid E-mail!',
            }, {
              required: true, message: 'Please input your E-mail!',
            }],
          })(
            <Input />
          )}
        </FormItem>
        <FormItem {...formItemLayout} label="密码" hasFeedback>
          {getFieldDecorator('password', {
            rules: [{
              required: true, message: 'Please input your password!',
            }],
          })(
            <Input type="password" />
          )}
        </FormItem>
        <FormItem {...formItemLayout} label="确认密码" hasFeedback>
          {getFieldDecorator('confirm', {
            rules: [{
              required: true, message: 'Please confirm your password!',
            }],
          })(
            <Input type="password" />
          )}
        </FormItem>
        <FormItem {...formItemLayout} label="手机号">
          {getFieldDecorator('phone', {
            rules: [{ required: true, message: 'Please input your phone number!' }],
          })(
            <Input />
          )}
        </FormItem>
        <div className={styles.loginFormForgot}>
      		 <Button type="primary" htmlType="submit" size="large" loading={signInButtonLoading}>注册</Button>
         	 <Button  size="large" onClick={handleCancel}>取消</Button>
        </div> 	 
      </Form>
      </div>
	)
}

Register1.propTypes = {
	form : PropTypes.object,
	onSignIn : PropTypes.func,
  onBack : PropTypes.func,
	signInButtonLoading : PropTypes.bool,
}

export default Form.create()(Register1);
