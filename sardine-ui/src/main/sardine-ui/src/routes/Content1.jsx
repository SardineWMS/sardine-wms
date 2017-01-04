import React from 'react';
import QueueAnim from 'rc-queue-anim';
import TweenOne from 'rc-tween-one';
import OverPack from 'rc-scroll-anim/lib/ScrollOverPack';

class Content extends React.Component {

  static propTypes = {
    id: React.PropTypes.string,
  };

  static defaultProps = {
    className: 'content2',
  };

  getDelay = e => e.index % 3 * 100 + Math.floor(e.index / 3) * 100 + 200;

  render() {
    const blockArray = [
      { children: { icon: { children: 'https://zos.alipayobjects.com/rmsportal/ScHBSdwpTkAHZkJ.png' }, title: { children: '沙丁鱼' }, content: { children: '沙丁鱼（Sardine）是是硬骨鱼纲鲱形目鲱科沙丁鱼属、小沙丁鱼属和拟沙丁鱼属及鲱科某些食用鱼类的统称。也指制成油浸鱼罐头的普通鲱(Clupea harengus）以及其它小型的鲱或鲱状鱼' } } },
      { children: { icon: { children: 'https://zos.alipayobjects.com/rmsportal/NKBELAOuuKbofDD.png' }, title: { children: '鲇鱼效应' }, content: { children: '鲶鱼效应是采取一种手段或措施，刺激一些企业活跃起来投入到市场中积极参与竞争，从而激活市场中的同行业企业。其实质是一种负激励，是激活员工队伍之奥秘。' } } },
      { children: { icon: { children: 'https://zos.alipayobjects.com/rmsportal/xMSBjgxBhKfyMWX.png' }, title: { children: '生活习性' }, content: { children: '沙丁鱼为近海暖水性鱼类，一般不见于外海和大洋。它们游泳迅速，通常栖息于中上层，但秋、冬季表层水温较低时则栖息于较深海区。' } } },
    ];
    const children = blockArray.map((item, i) => {
      const children = item.children;
      const styleObj = item.style || {};
      return (<li
        key={i}
        style={{ left: `${i % 3 * 33.33}%`, top: `${Math.floor(i / 3) * 200}px`, ...styleObj }}
      >
        <TweenOne
          animation={{ x: '-=10', opacity: 0, type: 'from' }}
          className="img"
          key="img"
          style={children.icon.style}
        >
          <img src={children.icon.children} width="100%" />
        </TweenOne>
        <QueueAnim delay={100} leaveReverse key="text" className="text">
          <h1 key="h1" style={children.title.style}>{children.title.children}</h1>
          <p key="p" style={children.content.style}>{children.content.children}</p>
        </QueueAnim>
      </li>);
    });
    const titleAnim = { y: '+=30', opacity: 0, type: 'from' };
    return (
      <div
        {...this.props}
        className="content-template-wrapper"
      >
        <OverPack
          className={`content-template ${this.props.className}`}
          hideProps={{ h1: { reverse: true }, p: { reverse: true } }}
          location={this.props.id}
        >
          <TweenOne
            key="h1"
            animation={titleAnim}
            component="h1"
          >
            提供专业的WMS服务
          </TweenOne>
          <TweenOne
            key="p"
            animation={titleAnim}
            component="p"
          >
          </TweenOne>
          <QueueAnim
            key="ul"
            component="ul"
            leaveReverse
            type="bottom"
            interval={0}
            delay={this.getDelay}
          >
            {children}
          </QueueAnim>
        </OverPack>
      </div>
    );
  }
}
export default Content;