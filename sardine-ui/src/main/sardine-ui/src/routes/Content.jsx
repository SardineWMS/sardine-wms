import React, { cloneElement } from 'react';
import ReactDOM from 'react-dom';
import { scrollScreen } from 'rc-scroll-anim';

import Content0 from './Content0';
import Content1 from './Content1';

import './less/antMotion_style.less';

export default class Content extends React.Component {
  render() {
    return (
    <div>
      <Content0 id="Content0" key="Content0"/>
      <Content1 id="Content1" key="Content1"/>
    </div>
    );
  }
}