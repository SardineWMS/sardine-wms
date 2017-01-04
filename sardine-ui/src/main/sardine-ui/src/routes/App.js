import React, {PropTypes} from 'react'
import {connect} from 'dva'
import Login from './Login';
import Header from '../components/Layout/Header';
import Footer from '../components/Layout/Footer';
import Sider from '../components/Layout/sider';
import styles from '../components/Layout/main.less';
import '../components/Layout/common.less';
import Register from './Register';
import {Spin,message} from 'antd'
import {classnames} from '../utils'

function App({children, location, dispatch, app}) {
  const {
    login,
    loading,
    registerLoading,
    signInButtonLoading,
    loginButtonLoading,
    user,
    siderFold,
    darkTheme
  } = app;
  
  const loginProps = {
    loading,
    loginButtonLoading,
    onOk(data) {
      dispatch({type: 'app/login', payload: data})
    },
    onRegister(){
      dispatch({type: 'app/showRegister'})
    }
  }

  const registerProps = {
    signInButtonLoading,
    onSignIn(data) {
      dispatch({type : 'app/register', payload : data})
    },
    onBack(){
      dispatch({type : 'app/registerBack'})
    }
  }

  const headerProps = {
    user,
    siderFold,
    logout() {
      dispatch({type: 'app/logout'})
    },
    switchSider() {
      dispatch({type: 'app/switchSider'})
    }
  }

  const siderProps = {
    siderFold,
    darkTheme,
    location,
    changeTheme(){
      dispatch({type: 'app/changeTheme'})
    }
  }

  return (
    <div>
     {login  ?
        <div className={classnames(styles.layout,{[styles.fold]:siderFold})}>
            <aside  className={classnames(styles.sider,{[styles.light]:!darkTheme})}>
              <Sider {...siderProps}/>
            </aside>
            <div className={styles.main}>
              <Header {...headerProps}/>
              <div className={styles.container}>
                <div className={styles.content}>
                  {children}
                </div>
              </div>
              <Footer />
            </div>
          </div>
        : (registerLoading ? 
            <div className={styles.spin}>
              <Register {...registerProps}/>
            </div>
            : 
            <div className={styles.spin}><Spin tip="加载用户信息..." spinning={loading} size="large">
              <Login {...loginProps}/></Spin>
            </div>  
        )}

 </div>
  )
}

App.propTypes = {
  children: PropTypes.element.isRequired,
  location: PropTypes.object,
  dispatch: PropTypes.func,
  loading: PropTypes.object,
  registerLoading: PropTypes.bool,
  loginButtonLoading: PropTypes.bool,
  login: PropTypes.bool,
  user: PropTypes.object,
  siderFold:PropTypes.bool,
  darkTheme:PropTypes.bool,
}

export default connect(({app}) => ({app}))(App)
