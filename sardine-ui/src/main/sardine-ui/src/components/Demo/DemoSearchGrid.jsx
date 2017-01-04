import React,{PropTypes} from 'react';
import { Table, Popconfirm, Pagination,Button } from 'antd';

function DemoSearchGrid({
	loading,
	dataSource,
	pagination,
	onPageChange,
	onCreate,
	onViewItem,
	onEditItem,
	onDeleteItem,
}) {

	function handleCreate(e) {
		e.preventDefault();
		onCreate();
	}

	const columns = [{
    	title: '姓名',
    	dataIndex: 'name',
    	key: 'name',
    	render: (text, record) => <a onClick={() => onViewItem(record)}>{text}</a>,
  	}, {
    	title: '年龄',
    	dataIndex: 'age',
    	key: 'age',
    	render: (text) => <span>{text}岁</span>,
  	}, {
    	title: '住址',
    	dataIndex: 'address',
    	key: 'address',
  	}, {
    title: '操作',
    key: 'operation',
    render: (text, record) => (
      <p>
        <a onClick={() => onEditItem(record)}>编辑</a>
        &nbsp;
        <Popconfirm title="确定要删除吗？" onConfirm={() => onDeleteItem(record.id)}>
          <a>删除</a>
        </Popconfirm>
      </p>
    ),
  }
  ]

  const rowSelection = {
  }

  return (
    <div>
      <Table size="small"
        bordered
        rowSelection={rowSelection}
        columns={columns}
        title={() => 
        	<div>
            <Button onClick={handleCreate}>新建</Button>
            <Button>审核</Button>
            <Button>删除</Button>
        	</div>
        }
        dataSource={dataSource}
        loading={loading}
        onChange={onPageChange}
        pagination={pagination}
        rowKey={record => record.id}
      />
    </div>
  )
}

DemoSearchGrid.propTypes = {
	onPageChange: PropTypes.func,
	dataSource: PropTypes.array,
	loading: PropTypes.any,
	pagination: PropTypes.any,
	onCreate : PropTypes.func,
	onViewItem : PropTypes.func,
	onEditItem : PropTypes.func,
	onDeleteItem : PropTypes.func,
}

export default DemoSearchGrid;

