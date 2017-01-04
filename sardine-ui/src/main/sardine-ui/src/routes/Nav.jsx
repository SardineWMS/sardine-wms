import React, { PropTypes } from 'react';
import TweenOne from 'rc-tween-one';
import Menu from 'antd/lib/menu';
import { Link } from 'react-router';
import {Icon} from 'antd';

const Item = Menu.Item;
class Nav extends React.Component {

  render() {
    return (<TweenOne
      component="header"
      animation={{ opacity: 0, type: 'from' }}
      {...this.props}
    >
      <TweenOne
        className={`${this.props.className}-logo`}
        animation={{ x: -30, type: 'from', ease: 'easeOutQuad' }}
      >
      <h1>Sardine</h1>
      </TweenOne>
      <TweenOne
        className={`${this.props.className}-nav`}
        animation={{ x: 30, type: 'from', ease: 'easeOutQuad' }}
      >
        <Menu
          mode="horizontal" defaultSelectedKeys={['a']}
        >
        <Menu.Item key="home">
          <Link to="/home">首页</Link>
        </Menu.Item>
        <Menu.Item key="Demo">
          <Link to="/demo">Demo</Link>
        </Menu.Item>
        <Menu.Item key="IA">
          <Link>IA</Link>
        </Menu.Item>
        <Menu.Item key="WMS">
          <Link>WMS</Link>
        </Menu.Item>
        </Menu>
      </TweenOne>
    </TweenOne>);
  }
}

Nav.propTypes = {
  className: PropTypes.string,
};

Nav.defaultProps = {
  className: 'header0',
};

export default Nav;
