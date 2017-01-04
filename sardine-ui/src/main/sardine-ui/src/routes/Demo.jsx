import React ,{PropTypes} from 'react';
import { routerRedux } from 'dva/router';
import { connect } from 'dva';
import DemoSearchForm from '../components/Demo/DemoSearchForm';
import DemoSearchGrid from '../components/Demo/DemoSearchGrid';
import DemoCreateForm from '../components/Demo/DemoCreateForm';
import DemoViewForm from '../components/Demo/DemoViewForm';

function Demo({location,dispatch,demo}){
	const{
		loading,
		list,
		total,
		current,
		pagination,
		currentItem,
		showCreate,
		showView,
	} = demo;

	const { field, keyword} = location.query;
	const demoSearchGridProps = {
		dataSource: list,
		loading,
		pagination:pagination,
		onPageChange(page) {
			dispatch(routerRedux.push({
				pathname: '/demo',
				query: {
					page:page.current,
					pageSize:page.pageSize
				},
			}))
		},
		onCreate() {
			dispatch({
				type: 'demo/showCreatePage'
			})
		},
		onViewItem(item){
			dispatch({
				type: 'demo/showViewPage',
				payload: {
					currentItem : item
				}
			})
		},
		onDeleteItem(id) {
			dispatch({
				type: 'users/delete',
				payload: id,
			});
		},
		onEditItem(item) {
  			dispatch({
				type: 'demo/showEditPage',
				payload : {
					currentItem : item
				}
			})
		},
	}

	const userSearchFormProps = {
		field,
		keyword,
		onSearch(fieldsValue) {
			dispatch({
				type: 'demo/query',
				payload: fieldsValue,
			})
    	},
	}

	const createFormProps = {
		item : currentItem,
    	onOk(data) {
    		dispatch({
    			type: 'demo/create',
				payload: fieldsValue,
    		});
    	},
    	onCancel() {
    		dispatch({
    			type: 'demo/backSearch',
    		});
    	},
  	}

  	const viewFormProps = {
  		item: currentItem,
  		onCreate() {
			dispatch({
				type: 'demo/showCreatePage'
			})
  		},
  		onEdit(item) {
  			dispatch({
				type: 'demo/showEditPage',
				payload : {
					currentItem : item
				}
			})
  		},
  		onBack() {
  			dispatch({
    			type: 'demo/backSearch',
    		});
  		},
  	}

  	const CreateFormGen = () =><DemoCreateForm {...createFormProps} />;
  	function refreshWidget(){
  		if(showCreate)
  			return (<CreateFormGen />);
  		if(showView)
  			return (<DemoViewForm {...viewFormProps} />);
  		else {
  			return (<div>
  					<DemoSearchForm {...userSearchFormProps} />
  					<DemoSearchGrid {...demoSearchGridProps} />
  				</div>);
  		}
  	}

  	return (
  		<div className="content-inner">
  		{ refreshWidget() }	
  		</div>
	)
}

Demo.propTypes = {
	demo1 : PropTypes.object,
	location : PropTypes.object,
	dispatch : PropTypes.func,
	showCreate : PropTypes.bool,
	showView : PropTypes.bool,
}

function mapStateToProps({demo}){
	return demo;
}

export default connect(({demo}) => ({demo}))(Demo);