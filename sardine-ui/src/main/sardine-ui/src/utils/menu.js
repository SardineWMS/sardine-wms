module.exports = [
  {
    key: 'users',
    name: 'Demo模块',
    icon: 'user',
  },
  {
    key: 'navigation',
    name: '测试导航',
    icon: 'setting',
    child: [
      {
        key: 'navigation1',
        name: '二级导航1',
      },
      {
        key: 'navigation2',
        name: '二级导航2',
        child: [
          {
            key: 'navigation21',
            name: '三级导航1',
          },
          {
            key: 'navigation22',
            name: '三级导航2',
          },
        ],
      },
    ],
  },
];
