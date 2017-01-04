import { query, } from '../services/demo';

import { parse } from 'qs';

export default {
	namespace : 'demo',

	state:{
		list:[],
		loading : false,
		currentItem : {},
		showCreate : false,
		showEdit : false,
		showView : false,
		pagination:{
			showSizeChanger: true,
			showQuickJumper: true,
			showTotal: total => `共 ${total} 条`,
			current:1,
			total:null,
			size:'default'
		}
	},

	subscriptions: {
		setup({ dispatch, history }) {
			history.listen(location => {
				if (location.pathname === '/demo') {
					dispatch({
						type: 'query',
						payload: location.query,
					})
				}
			})
		},
	},

	effects : {
		*query({payload},{call,put}){
			yield put({type: 'showLoading'});
			const {data} = yield call(query,parse(payload));
		    if (data) {
		    	yield put({
		    		type: 'querySuccess',
		    		payload: {
		    			list: data.data,
		    			pagination:{
		    				total: data.page.total,
		    				current: data.page.current,
		    			}
		    		},
		    	})
		    }	
		},
	},

	reducers: {
		showLoading(state) {
			return { ...state, loading: true }
		},

		querySuccess(state, action) {
			return { ...state, ...action.payload, loading: false }
		},

		showCreatePage(state){
			return { ...state, showCreate : true,showView : false};
		},

		showViewPage(state, action){
			return { ...state, ...action.payload, showCreate : false, showView : true,}
		},

		showEditPage(state, action){
			return { ...state, ...action.payload, showCreate : true, showView : false,}
		},

		backSearch(state){
			return {...state,showCreate : false,showView : false,}
		},
  },
}